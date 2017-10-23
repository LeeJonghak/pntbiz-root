package api.presence.bo.handler;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.enums.PresenceDataType;
import api.presence.bo.service.PresenceServiceDelegator;
import core.api.log.domain.PresenceLog;
import core.common.enums.InterfaceCommandType;
import core.common.enums.SocketCommandType;
import framework.util.ClassUtil;

/**
 * Created by ucjung on 2017-06-22.
 */
public class ScannerValidatorPostHandler implements PresenceTaskEventHandler {
    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        WriteLog(presenceVo, serviceDelegator);

    }

    private void WriteLog(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        PresenceLog presenceLog = new PresenceLog();

        ClassUtil.copyProperties(presenceLog, requestParam);
        presenceLog.setTargetName(requestParam.getMajorVer().toString());
        presenceLog.setBeaconName(requestParam.getMinorVer().toString());

        serviceDelegator.getLogService().registerPresenceLog(presenceLog);
        presenceVo.setTypeData(PresenceDataType.PRESENCE_SCANNER_LOG, presenceLog);
    }
}
