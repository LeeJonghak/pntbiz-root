package api.component.scheduler.service;

import core.common.geofencing.domain.Geofencing;
import api.geofencing.service.GeofencingRedisService;
import api.geofencing.service.GeofencingService;
import core.api.log.domain.GeofenceLog;
import core.api.log.domain.PresenceLog;
import api.log.service.LogService;
import api.presence.service.PresenceRedisService;
import framework.web.util.DateUtil;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import framework.web.websocket.WebSocketThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("ISMScheduler")
public class ISMScheduler {

	@Value("#{config['presence.socket.url']}")
	private String socketURL;
	@Value("#{config['ism.url']}")
	private String ismURL;

	@Autowired
	private PresenceRedisService presenceRedisService;

	@Autowired
	private GeofencingService geofencingService;
	
	@Autowired
	private GeofencingRedisService geofencingRedisService;
	
	@Autowired
	private LogService logService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void batchGeofenceInout() throws Exception {
		
		logger.debug("##################### batch start ##########################");
		String SUUID = "44790BA4-7EB3-4095-9E14-4B43AE67512B";

        List<PresenceLog> list = presenceRedisService.getPresenceBeaconList("PRESENCE_BEACON_*");
        String floor = "";
        Double lat = 0.00;
        Double lng = 0.00;
        String fcName = "";
        Integer sos = 0;
        long lastTime = 0;
        long interval = 30;	// 60초 지나면 퇴실 처리
        long time = DateUtil.str2timestamp(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        String creationDate = DateUtil.getDate("yyyy-MM-dd a hh:mm:ss");
        Map<String, Object> inoutData = new HashMap<String, Object>();
        GeofenceLog geofenceLog = new GeofenceLog();
        
        logger.debug("##################### batch count : "+ list.size() + " ##########################");

        for(PresenceLog presenceLog : list) {
        	// 퇴실 보정시간 체크
            lastTime = presenceLog.getLastTime();
            if((lastTime + interval) < time) {
	            floor = presenceLog.getFloor();
	            lat = presenceLog.getLat();
	            lng = presenceLog.getLng();
	            fcName = presenceLog.getFcName();
	            sos = presenceLog.getSos();
	            
	            String uuid = presenceLog.getUUID();
            	String majorVer = presenceLog.getMajorVer().toString();
            	String minorVer = presenceLog.getMinorVer().toString();
            	String key = SUUID + "_" + uuid + "_" + majorVer + "_" + minorVer;
            	String id = uuid + "_" + majorVer + "_" + minorVer;
            	
                // 퇴실 처리
                inoutData.put("uuid", presenceLog.getUUID());
                inoutData.put("majorVer", presenceLog.getMajorVer());
                inoutData.put("minorVer", presenceLog.getMinorVer());
                inoutData.put("lat", presenceLog.getLat());
                inoutData.put("lng", presenceLog.getLng());
                inoutData.put("battery", presenceLog.getBattery());
                inoutData.put("regionCode", presenceLog.getRegionCode());
                inoutData.put("zoneCode", presenceLog.getZoneCode());
                inoutData.put("buildingCode", presenceLog.getBuildingCode());
                inoutData.put("floorCode", presenceLog.getFloorCode());
                inoutData.put("columnCode", presenceLog.getColumnCode());
                inoutData.put("areaCode", presenceLog.getAreaCode());
                inoutData.put("creationDate", creationDate);
                inoutData.put("currentStatus", -1);
                JsonUtil jsonUtil = new JsonUtil(inoutData);
                String inoutSendJson = jsonUtil.toJson();
                
                Map<Object, Object> zoneRedisData = geofencingRedisService.getGeofenceZoneTag(key);
                
	            if("".equals(fcName) || fcName == null) {
	                if(!zoneRedisData.isEmpty()) {
                        geofenceLog.setOutDate(Integer.parseInt(zoneRedisData.get("outDate").toString()));
                        // 지오펜스 로그데이터가 들어가기 전이면 logNum 이 없음.
                        try {
                            geofenceLog.setLogNum(Integer.parseInt(zoneRedisData.get("logNum").toString()));
                            logService.modifyGeofenceLog(geofenceLog);
                        } catch(Exception e) {}
                        geofencingRedisService.deleteGeofenceZoneTag(key);
	                }
	            	try {
//                        WebSocketThread.send(ismURL + "/rtl/location", "POST", 1000, inoutSendJson);
                        presenceRedisService.deletePresenceBeacon(key);
                        logger.debug("##################### batch redis delete " + key + "##########################");
                    } catch(Exception e) {
                    	e.printStackTrace();
                    }
	            	try {
                    	inoutData.put("id", id);
                    	inoutData.put("UUID", SUUID);
                    	inoutData.put("sos", sos);
                    	inoutData.put("floor", floor);
                    	inoutData.put("scannerPos", "");
                    	inoutData.put("showMarker", 0);
                        JsonUtil jsonUtil2 = new JsonUtil(inoutData);
                        String inoutSendJson2 = jsonUtil2.toJson();
                        WebSocketThread.send(socketURL + "/presence/send", "POST", 1000, inoutSendJson2);
                    } catch(Exception e) {
                    	e.printStackTrace();
                    }
                    continue;
	            	
	            } else {
	            	List<Geofencing> fenceList = geofencingService.checkGeofence(SUUID, floor, lat, lng);
	            	int cnt = 0;
		            for(Geofencing _fence : fenceList) {	
		                // 현재 있는 층의 챔버지역일 경우 패스
		            	logger.debug("##################### batch fence : " + _fence.getField1() + "##########################");
		                if(_fence.getField1().equals("CHAMBER") && _fence.getFloor().equals(floor)) {
		                	cnt++;
		                    break;
		                } else {
		                	
		                } // end if !chamber
		            } // end for fenceList
		            
		            // 챔버영역이 아닌 경우
		            if(cnt == 0) {
		            	logger.debug("##################### batch out process : " + minorVer + "_" + fcName + "##########################");
	                	if(!zoneRedisData.isEmpty()) {
	                        geofenceLog.setOutDate(Integer.parseInt(zoneRedisData.get("outDate").toString()));
	                        try {
	                            geofenceLog.setLogNum(Integer.parseInt(StringUtil.NVL(zoneRedisData.get("logNum").toString(), "0")));
	                            logService.modifyGeofenceLog(geofenceLog);
	                        } catch(Exception e) {
	                        	e.printStackTrace();
	                        }
	                        geofencingRedisService.deleteGeofenceZoneTag(key);
		                }
                        try {
                            WebSocketThread.send(ismURL + "/rtl/location", "POST", 1000, inoutSendJson);
                            presenceRedisService.deletePresenceBeacon(key);
                        } catch(Exception e) {
                        	e.printStackTrace();
                        }
                        try {
                        	inoutData.put("id", id);
                        	inoutData.put("UUID", SUUID);
                        	inoutData.put("sos", sos);
                        	inoutData.put("floor", floor);
                        	inoutData.put("scannerPos", null);
                        	inoutData.put("showMarker", 0);
                            JsonUtil jsonUtil2 = new JsonUtil(inoutData);
                            String inoutSendJson2 = jsonUtil2.toJson();
                            WebSocketThread.send(socketURL + "/presence/send", "POST", 1000, inoutSendJson2);
                        } catch(Exception e) {
                        	e.printStackTrace();
                        }
		            }
	            } // end if blank fcname
            } // end if time
        } // end for presence list
	}

}
