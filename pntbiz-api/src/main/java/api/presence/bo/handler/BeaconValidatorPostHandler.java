package api.presence.bo.handler;

import api.presence.bo.domain.BeaconPresenceRequestParam;
import api.presence.bo.domain.PresenceDataPackage;
import core.common.enums.PresenceDataType;
import api.presence.bo.service.PresenceServiceDelegator;
import core.api.log.domain.PresenceBeaconLog;
import core.common.enums.SocketCommandType;
import framework.util.ClassUtil;

/**
 * Created by ucjung on 2017-06-22.
 */
public class BeaconValidatorPostHandler implements PresenceTaskEventHandler {
    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        WriteLog(presenceVo, serviceDelegator);
        EmitSocketIO(presenceVo, serviceDelegator);
    }

    private void WriteLog(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        BeaconPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        PresenceBeaconLog presenceBeaconLog = new PresenceBeaconLog();

        ClassUtil.copyProperties(presenceBeaconLog, requestParam);

        serviceDelegator.getLogService().registerPresenceBeaconLog(presenceBeaconLog);

        presenceVo.setTypeData(PresenceDataType.PRESENCE_BEACON_LOG, presenceBeaconLog);
    }

    private void EmitSocketIO(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        serviceDelegator.getSocketRequestService().send(SocketCommandType.UPDATE_MARKER, presenceVo);
    }
}
