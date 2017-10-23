package api.presence.bo.strategy.geofence;

import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.PresenceDataType;
import api.presence.bo.domain.PresenceDataPackage;
import core.common.enums.ZoneType;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import core.common.geofencing.domain.Geofencing;
import framework.web.util.DateUtil;

import java.util.*;

import static core.common.enums.ZoneInOutStateType.*;

/**
 * Goefence In/Out 일반 모듈
 *
 * Geofence 기반 측위서버에서 전송되는 층정보(floor)를 기준으로 In/Stay/Out 판별
 *
 * Created by ucjung on 2017-06-15.
 */
public class GeofenceCommonStrategy extends AbstractPresenceStrategy {
    private ScannerPresenceRedis currentPresenceRedis;
    private ScannerPresenceRedis previousPresenceRedis;
    private ScannerPresenceRequestParam requestParam;
    private List<Geofencing> inboundGeofences;
    private String createdDate;
    private Integer createdTimeStamp;
    private Map<String, ZoneInOutState> geofencesInOutState = new HashMap<>();

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);

        createdDate = presenceVo.getTypeData(PresenceDataType.DATE_CREATION);
        createdTimeStamp = (int) DateUtil.str2timestamp(createdDate, "yyyy-MM-dd a HH:mm:ss");
        requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        currentPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
        previousPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE);
        inboundGeofences = presenceVo.getTypeData(PresenceDataType.INBOUND_GEOFENCES);

        if (previousPresenceRedis != null) {
            if (previousPresenceRedis.getGeofencesInOutState() != null) {
                geofencesInOutState = previousPresenceRedis.getGeofencesInOutState();
            }
        }
    }

    @Override
    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {
        super.setPresenceDataPackage(presenceVo);
        presenceVo.setTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE, previousPresenceRedis);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, currentPresenceRedis);
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        Set<String> processedZones = new HashSet<>();

        locatedFenceInProcess(processedZones);
        locatedFenceOutProcess(processedZones);

        currentPresenceRedis.setGeofencesInOutState(geofencesInOutState);
    }

    private String locatedFenceInProcess(Set<String> processedZones) {
        String key = null;
        ZoneInOutState geofenceState;
        for (Geofencing inboundFence: inboundGeofences) {
            key = inboundFence.getFcNum().toString();
            processedZones.add(key);
            if (geofencesInOutState.containsKey(inboundFence.getFcNum().toString())) {
                // 기존 Fence 존재
                geofenceState = geofencesInOutState.get(key);
                geofenceState.setOutTime(0);
            } else {
                geofenceState = createNewZoneInOutState(key, inboundFence);
            }

            switch(geofenceState.getState()) {
                case IN_PROCESSING:
                    if (createdTimeStamp >= geofenceState.getInTime() + geofenceState.getEnterTimeInterval()) {
                        geofenceState.changeState(IN);
                    }
                    break;
                case IN:
                    geofenceState.setInTime(createdTimeStamp);
                    geofenceState.setStayTime(createdTimeStamp);
                case STAY_PROCESSING:
                    if (createdTimeStamp >= geofenceState.getStayTime() + geofenceState.getStayTimeInterval()) {
                        geofenceState.setStayTime(createdTimeStamp);
                        geofenceState.changeState(STAY);
                    } else {
                        geofenceState.changeState(STAY_PROCESSING);
                    }
                    break;
                case STAY:
                    geofenceState.changeState(STAYING);
                    break;
                case STAYING:
                    if (geofenceState.getStayTimeInterval() > 0) {
                        if (createdTimeStamp > geofenceState.getStayTime() + geofenceState.getStayTimeInterval()) {
                            geofenceState.setStayTime(createdTimeStamp);
                            geofenceState.changeState(STAY);
                        }
                    }
                    break;
                case OUT_PROCESSING:
                    if (geofenceState.getBeforeState() == STAY)
                        geofenceState.changeState(STAYING);
                    else if (geofenceState.getBeforeState() == IN)
                        geofenceState.changeState(STAY_PROCESSING);
                    else
                        geofenceState.changeState(geofenceState.getBeforeState());
                    break;
                case OUT:
                    geofenceState.changeState(IN_PROCESSING);
                    break;
            }
            geofencesInOutState.put(key, geofenceState);
        }
        return key;
    }

    private void locatedFenceOutProcess(Set<String> processedZones) {
        ZoneInOutState geofenceState;
        for (String key: geofencesInOutState.keySet()) {
            if (processedZones.contains(key))
                continue;
            else
                processedZones.add(key);

            geofenceState = geofencesInOutState.get(key);

            switch(geofenceState.getState()) {
                case IN_PROCESSING:
                    geofenceState.changeState(DONE);
                    break;
                case IN:
                case STAY_PROCESSING:
                case STAY:
                case STAYING:
                    geofenceState.setOutTime(0);
                case OUT_PROCESSING:
                    if (geofenceState.getOutTime() == 0) {
                        geofenceState.setOutTime(createdTimeStamp);
                    }
                    if (createdTimeStamp >= geofenceState.getOutTime() + geofenceState.getLeaveTimeInterval()) {
                        geofenceState.changeState(OUT);
                    } else {
                        geofenceState.changeState(OUT_PROCESSING);
                    }
                    break;
                case OUT:
                    geofenceState.changeState(DONE);
                    break;
            }
            geofencesInOutState.put(key, geofenceState);
        }
    }

    private ZoneInOutState createNewZoneInOutState(String key, Geofencing inboundFence) {
        ZoneInOutState geofenceState;
        geofenceState = new ZoneInOutState();
        geofenceState.setZoneId(key);
        geofenceState.setName(inboundFence.getFcName());
        geofenceState.setBeforeState(IN_PROCESSING);
        geofenceState.setState(IN_PROCESSING);
        geofenceState.setZoneType(ZoneType.GEOFENCE);
        geofenceState.setEnterTimeInterval(inboundFence.getEvtEnter());
        geofenceState.setStayTimeInterval(inboundFence.getEvtStay());
        geofenceState.setLeaveTimeInterval(inboundFence.getEvtLeave());
        geofenceState.setInTime((int) DateUtil.str2timestamp(createdDate, "yyyy-MM-dd a HH:mm:ss"));
        geofenceState.setFcGroupNum(inboundFence.getFcGroupNum());
        geofenceState.setFcGroupName(inboundFence.getFcGroupName());
        return geofenceState;
    }


}
