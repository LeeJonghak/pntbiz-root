package custom.NHBank.presence.bo.service;

import api.log.service.LogService;
import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.service.InterfaceConfigService;
import api.presence.bo.service.LostBeaconSignalService;
import api.presence.bo.service.RestApiRequestService;
import api.presence.bo.service.SocketRequestService;
import api.presence.service.PresenceRedisService;
import core.api.presence.dao.PresenceRedisDao;
import core.common.enums.*;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Beacon 신호가 수신되지 않아 특정 시간이 지날 경우 처리 로직
 * Created by ucjung on 2017-06-26.
 *
 * 농협 Lost Signal 신호발송 처리
 */
@Service
public class NHBankLostBeaconSignalServiceImpl implements LostBeaconSignalService {
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

    @Autowired
    private PresenceRedisDao presenceRedisDao;

    @Override
    @Async
    public void excute(String key) {
        if (key.startsWith(PRESENCE_BEACON_NOTIFY_PREFIX)) {
            String presenceBeaconKey = key.replace(PRESENCE_BEACON_NOTIFY_PREFIX,"");
            logger.info("Lost Signal Start : get redis Info {}", presenceBeaconKey);
            ScannerPresenceRedis scannerPresenceRedis = presenceRedisService.getScannerPresenceRedis(presenceBeaconKey);
            if (scannerPresenceRedis != null) {
                int currentTime = (int) DateUtil.str2timestamp(DateUtil.getDate("yyyy-MM-dd hh:mm:ss"));

                PresenceDataPackage presenceVo = new PresenceDataPackage();
                presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, scannerPresenceRedis);
                presenceVo.setTypeData(PresenceDataType.INTERFACE_CONFIG,interfaceConfigService.gets(scannerPresenceRedis.getSUUID()));

                if (scannerPresenceRedis.getFloorInOutState() != null) {
                    if (scannerPresenceRedis.getFloorInOutState().getState() != ZoneInOutStateType.OUT) {
                        logger.info("Lost Signal {}: Emit Notification Lost Signal", presenceBeaconKey);
                        socketRequestService.send(SocketCommandType.NOTIFICATION, InterfaceCommandType.LOST_SIGNAL, presenceVo);
                        logger.info("Lost Signal END : get redis Info {}", presenceBeaconKey);
                        return;
                    }
                }
                logger.info("Lost Signal {}: remove previous redis Info", presenceBeaconKey);
                // 레디스 정보 삭제
                presenceRedisService.deletePresenceBeacon(presenceBeaconKey);

                logger.info("Lost Signal END : get redis Info {}", presenceBeaconKey);
            }
        }
    }
}
