package api.presence.bo.strategy.logger;

import api.presence.bo.domain.BeaconPresenceRequestParam;
import api.presence.bo.domain.PresenceDataPackage;
import core.common.enums.PresenceDataType;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import core.api.log.domain.PresenceBeaconLog;
import framework.util.ClassUtil;

/**
 * Create Presence Process Chain
 *
 * Presence Send Data 처리를 위한 Factory
 *
 * Created by ucjung on 2017-06-05.
 */
public class BeaconLogPresenceStrategy extends AbstractPresenceStrategy {

    private PresenceBeaconLog presenceBeaconLog = new PresenceBeaconLog();
    private BeaconPresenceRequestParam requestParam;

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);
        requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        ClassUtil.copyProperties(presenceBeaconLog, requestParam);
    }

    @Override
    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {
        presenceVo.setTypeData(PresenceDataType.PRESENCE_BEACON_LOG, presenceBeaconLog);
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        writeLog();
    }

    private void writeLog() {
        serviceDelegator.getLogService().registerPresenceBeaconLog(presenceBeaconLog);
    }

}
