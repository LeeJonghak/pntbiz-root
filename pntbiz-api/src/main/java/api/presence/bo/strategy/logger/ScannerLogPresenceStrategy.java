package api.presence.bo.strategy.logger;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.enums.PresenceDataType;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import core.api.log.domain.PresenceLog;
import framework.util.ClassUtil;

/**
 * Scanner Presence Log Strategy
 *
 * Created by ucjung on 2017-06-05.
 */
public class ScannerLogPresenceStrategy extends AbstractPresenceStrategy {
    private PresenceLog presenceLog = new PresenceLog();
    private ScannerPresenceRedis currentPresenceRedis;
    private ScannerPresenceRequestParam requestParam;

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);
        requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        ClassUtil.copyProperties(presenceLog, requestParam);
        currentPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
    }

    @Override
    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {
        presenceVo.setTypeData(PresenceDataType.PRESENCE_SCANNER_LOG, presenceLog);
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        writeLog();
        writeRedis();
    }

    private void writeLog() {
        //serviceDelegator.getLogService().registerPresenceLog(presenceLog);
    }

    private void writeRedis() {
         String redisKey = requestParam.getSUUID() + "_" + requestParam.getId();
        serviceDelegator.getPresenceRedisService().setScannerPresenceRedis(currentPresenceRedis);
    }

}
