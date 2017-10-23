package api.presence.bo.handler;

import api.log.service.LogService;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.PresenceDataType;
import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.service.RestApiRequestService;
import core.common.enums.InterfaceCommandType;
import framework.util.JsonUtils;
import core.api.log.domain.GeofenceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Zone InOut Task 후처리 모듈
 *
 * Zone In 상태
 * ㄴ Zone InOut Log Insert
 * ㄴ Zone In 외부 Interface 처리
 * Floor Out 상태 :
 * ㄴ Zone InoOut Log Update
 * ㄴ Zone Out 외부 Interface 처리
 *
 * Created by ucjung on 2017-06-11.
 */
public class GeofenceInOutPostHandler implements PresenceTaskEventHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestApiRequestService restApi;
    private LogService logService;
    private GeofenceLog geofenceLog = new GeofenceLog();
    private PresenceDataPackage presenceVo;


    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        this.presenceVo = presenceVo;
        restApi = serviceDelegator.getRestApiRequestService();
        logService = serviceDelegator.getLogService();
        ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);

        ScannerPresenceRedis current = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
        Map<String, ZoneInOutState> geofencesInOutState = current.getGeofencesInOutState();

		Object[] keys = geofencesInOutState.keySet().toArray();
		for(int i=0; i<keys.length; i++) {
			String zoneKey = (String)keys[i];
			ZoneInOutState inOutState = geofencesInOutState.get(zoneKey);

			switch (inOutState.getState()) {
				case IN:
					setGeofenceLogData(requestParam, inOutState);
					doGeogenceIn(inOutState, geofencesInOutState);
					break;
				case STAY:
					doGeogenceStay(inOutState, geofencesInOutState);
					break;
				case OUT:
					setGeofenceLogData(requestParam, inOutState);
					doGeogenceOut(inOutState, geofencesInOutState);
					break;
				case DONE:
					doGeogenceDone(inOutState, geofencesInOutState);
					break;
				default:
					logger.info("Geofence {} Post Process : \n{}", inOutState.getState(), JsonUtils.writeValue(inOutState));
			}
		}

        current.setGeofencesInOutState(geofencesInOutState);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, current);
    }

    // Set Geofence Log Vo
    private void setGeofenceLogData(ScannerPresenceRequestParam requestParam, ZoneInOutState inOutState) {
        geofenceLog.setSUUID(requestParam.getSUUID());
        geofenceLog.setUUID(requestParam.getUUID());
        geofenceLog.setMajorVer(requestParam.getMajorVer());
        geofenceLog.setMinorVer(requestParam.getMinorVer());
        geofenceLog.setFloor(requestParam.getFloor());
        geofenceLog.setLogNum(inOutState.getLogNum());
        geofenceLog.setFcNum(Long.valueOf(inOutState.getZoneId()));
        geofenceLog.setFcName(inOutState.getName());
        geofenceLog.setBeaconName("");
        geofenceLog.setTargetName("");
        geofenceLog.setLogDesc("");
        geofenceLog.setInDate(inOutState.getInTime());
        geofenceLog.setOutDate(inOutState.getOutTime());
        geofenceLog.setPermitted(inOutState.getPermitted());
    }

    private void doGeogenceIn(ZoneInOutState state, Map<String, ZoneInOutState> geofencesInOutState) {
        logger.info("Geofence In Post Process : \n{}", JsonUtils.writeValue(state));
        state.setLogNum(logService.registerGeofenceLog(geofenceLog));
        geofencesInOutState.put(state.getZoneId(), state);
        restApi.send(InterfaceCommandType.GEOFENCE_IN, presenceVo, state);
    }

    private void doGeogenceStay(ZoneInOutState state, Map<String, ZoneInOutState> geofencesInOutState) {
        logger.info("Geofence Stay Post Process : \n{}", JsonUtils.writeValue(state));
        restApi.send(InterfaceCommandType.GEOFENCE_STAY, presenceVo, state);
    }

    private void doGeogenceOut(ZoneInOutState state, Map<String, ZoneInOutState> geofencesInOutState) {
        logger.info("Geofence Out Post Process : \n{}", JsonUtils.writeValue(state));
        logService.modifyGeofenceLog(geofenceLog);
        restApi.send(InterfaceCommandType.GEOFENCE_OUT, presenceVo, state);
    }

    private void doGeogenceDone(ZoneInOutState state, Map<String, ZoneInOutState> geofencesInOutState) {
        logger.info("Geofence Done Post Process : \n{}", JsonUtils.writeValue(state));
        geofencesInOutState.remove(state.getZoneId());
    }

}
