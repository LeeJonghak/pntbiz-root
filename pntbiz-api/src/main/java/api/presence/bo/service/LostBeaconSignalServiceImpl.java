package api.presence.bo.service;

import api.log.service.LogService;
import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.*;
import api.presence.service.PresenceRedisService;
import core.api.log.domain.GeofenceLog;
import framework.util.JsonUtils;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Beacon 신호가 수신되지 않아 특정 시간이 지날 경우 처리 로직
 * Created by ucjung on 2017-06-26.
 */
@Service
public class LostBeaconSignalServiceImpl implements LostBeaconSignalService {
    private static final String PRESENCE_BEACON_NOTIFY_PREFIX = "NOTIFY_PRESENCE_BEACON_";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InterfaceConfigService interfaceConfigService;

    @Autowired
    private PresenceRedisService presenceRedisService;

    @Autowired
    private RestApiRequestService restApiRequestService;

    @Autowired
    private SocketRequestService socketRequestService;

    @Autowired
    private LogService logService;

    @Override
    @Async
    public void excute(String key) {
        if (key.startsWith(PRESENCE_BEACON_NOTIFY_PREFIX)) {
            String presenceBeaconKey = key.replace(PRESENCE_BEACON_NOTIFY_PREFIX,"");
            logger.info("Lost Signal : get redis Info {}", presenceBeaconKey);
            ScannerPresenceRedis scannerPresenceRedis = presenceRedisService.getScannerPresenceRedis(presenceBeaconKey);
            if (scannerPresenceRedis != null) {
                int currentTime = (int) DateUtil.str2timestamp(DateUtil.getDate("yyyy-MM-dd hh:mm:ss"));

                logger.info("Lost Signal {}: remove previous redis Info", presenceBeaconKey);
                // 레디스 정보 삭제
                presenceRedisService.deletePresenceBeacon(presenceBeaconKey);

                PresenceDataPackage presenceVo = new PresenceDataPackage();
                presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, scannerPresenceRedis);

                presenceVo.setTypeData(PresenceDataType.INTERFACE_CONFIG,interfaceConfigService.gets(scannerPresenceRedis.getSUUID()));

                ZoneInOutState floorInOutState = scannerPresenceRedis.getFloorInOutState();
                if (isStateOut(floorInOutState.getState()) == ZoneInOutStateType.OUT) {
                    floorInOutState.setOutTime(currentTime);
                    restApiRequestService.send(InterfaceCommandType.FLOOR_OUT, presenceVo, floorInOutState);
                    if (floorInOutState.getPermitted() == BooleanType.FALSE) {
                        restApiRequestService.send(InterfaceCommandType.RESTRICTION_OUT, presenceVo, floorInOutState);
                        socketRequestService.send(SocketCommandType.NOTIFICATION, InterfaceCommandType.RESTRICTION_OUT, presenceVo, floorInOutState);
                    }
                }

                Map<String, ZoneInOutState> geofencesInOutState = scannerPresenceRedis.getGeofencesInOutState();
                for (String zoneKey :geofencesInOutState.keySet()) {
                    ZoneInOutState inOutState = geofencesInOutState.get(zoneKey);
                    if (isStateOut(inOutState.getState()) == ZoneInOutStateType.OUT) {
                        inOutState.setOutTime(currentTime);
                        doGeogenceOut(inOutState, presenceVo);
                    }
                }
            }
        }
    }

    private void doGeogenceOut(ZoneInOutState inOutState, PresenceDataPackage presenceVo) {
        logger.info("Geofence Out Post Process : \n{}", JsonUtils.writeValue(inOutState));
        GeofenceLog geofenceLog = new GeofenceLog();
        geofenceLog.setLogNum(inOutState.getLogNum());
        geofenceLog.setOutDate(inOutState.getOutTime());
        inOutState.setLogNum(inOutState.getLogNum());
        logService.modifyGeofenceLog(geofenceLog);
        restApiRequestService.send(InterfaceCommandType.GEOFENCE_OUT, presenceVo, inOutState);

        if (inOutState.getPermitted() == BooleanType.FALSE) {
            restApiRequestService.send(InterfaceCommandType.RESTRICTION_OUT, presenceVo, inOutState);
            socketRequestService.send(SocketCommandType.NOTIFICATION, InterfaceCommandType.RESTRICTION_OUT, presenceVo, inOutState);
        }
    }

    private ZoneInOutStateType isStateOut(ZoneInOutStateType InOutState) {
        switch (InOutState) {
            case OUT_PROCESSING:
            case IN:
            case STAY_PROCESSING:
            case STAY:
            case STAYING:
                return ZoneInOutStateType.OUT;
            default:
                return ZoneInOutStateType.DONE;
        }
    }
}
