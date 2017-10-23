package api.presence.bo.handler;

import api.log.service.LogService;
import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import core.api.log.domain.FloorLog;
import core.api.log.domain.GeofenceLog;
import core.common.enums.PresenceDataType;
import core.common.enums.BooleanType;
import core.common.enums.InterfaceCommandType;
import core.common.enums.SocketCommandType;
import core.common.enums.ZoneInOutStateType;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.service.RestApiRequestService;
import api.presence.bo.service.SocketRequestService;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 비인가 지역 후처리 핸들러
 * - IN 처리된 비인가 Floor or Geofence에 대한 Interface 처리
 *
 * Created by ucjung on 2017-08-24
 */
public class RestrictionPostHandler implements PresenceTaskEventHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestApiRequestService restApiRequestService;
    private SocketRequestService socketRequestService;
    private LogService logService;
    private PresenceDataPackage presenceVo;

    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        this.presenceVo = presenceVo;

        restApiRequestService = serviceDelegator.getRestApiRequestService();
        socketRequestService = serviceDelegator.getSocketRequestService();
        logService = serviceDelegator.getLogService();

        ScannerPresenceRedis previous = presenceVo.getTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE);
        ScannerPresenceRedis current = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);

        if (previous != null)
            if (previous.getFloorInOutState() != null)
                if (previous.getFloorInOutState().getState() == ZoneInOutStateType.OUT)
                    sendRestrict(previous.getFloorInOutState());

        sendRestrict(current.getFloorInOutState());
        updateFloorLog(current.getFloorInOutState());

        Map<String, ZoneInOutState> geofencesInOutStates = current.getGeofencesInOutState();
        for (String key: geofencesInOutStates.keySet()) {
            sendRestrict(geofencesInOutStates.get(key));
            updateGeofenceLog(geofencesInOutStates.get(key));
        }

        invokeSosChangeEvent(presenceVo, serviceDelegator);
    }

    private void updateGeofenceLog(ZoneInOutState zoneInOutState) {
        if (zoneInOutState == null) return;
        if (zoneInOutState.getState() == ZoneInOutStateType.IN && zoneInOutState.getPermitted() == BooleanType.FALSE) {
            GeofenceLog geofenceLog = new GeofenceLog();
            geofenceLog.setLogNum(zoneInOutState.getLogNum());
            geofenceLog.setPermitted(zoneInOutState.getPermitted());
            geofenceLog.setOutDate(zoneInOutState.getOutTime());
            logService.modifyGeofenceLog(geofenceLog);
        }
    }

    private void updateFloorLog(ZoneInOutState zoneInOutState) {
        if (zoneInOutState == null) return;
        if (zoneInOutState.getState() == ZoneInOutStateType.IN && zoneInOutState.getPermitted() == BooleanType.FALSE) {
            FloorLog floorLog = new FloorLog();
            floorLog.setLogNum(zoneInOutState.getLogNum());
            floorLog.setPermitted(zoneInOutState.getPermitted());
            floorLog.setOutDate(zoneInOutState.getOutTime());
            logService.modifyFloorLog(floorLog);
        }
    }

    private void sendRestrict(ZoneInOutState zoneInOutState) {
        if ( zoneInOutState == null) return;
        if (zoneInOutState.getPermitted() == BooleanType.FALSE) {
            if (zoneInOutState.getState() == ZoneInOutStateType.IN) {
                restApiRequestService.send(InterfaceCommandType.RESTRICTION_IN, presenceVo, zoneInOutState);
                socketRequestService.send(SocketCommandType.NOTIFICATION, InterfaceCommandType.RESTRICTION_IN, presenceVo, zoneInOutState);
            } else if (zoneInOutState.getState() == ZoneInOutStateType.OUT) {
                restApiRequestService.send(InterfaceCommandType.RESTRICTION_OUT, presenceVo, zoneInOutState);
                socketRequestService.send(SocketCommandType.NOTIFICATION, InterfaceCommandType.RESTRICTION_OUT, presenceVo, zoneInOutState);
            } else  if (zoneInOutState.getState() == ZoneInOutStateType.STAY) {
                socketRequestService.send(SocketCommandType.NOTIFICATION, InterfaceCommandType.RESTRICTION_STAY, presenceVo, zoneInOutState);
            }
        }
    }

    private void invokeSosChangeEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        ScannerPresenceRedis previous = presenceVo.getTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE);
        ScannerPresenceRedis current = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
        Integer previousSos = (previous == null) ? 0 : previous.getSos();
        Integer currentSos = current.getSos();

        if (currentSos != previousSos) {
            if (currentSos == 1)
                serviceDelegator.getSocketRequestService().send(SocketCommandType.NOTIFICATION, InterfaceCommandType.SOS_ON, presenceVo);
            else
                serviceDelegator.getSocketRequestService().send(SocketCommandType.NOTIFICATION, InterfaceCommandType.SOS_OFF, presenceVo);
        }
    }
}
