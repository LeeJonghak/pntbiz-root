package api.presence.bo.handler;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import api.presence.bo.service.PresenceServiceDelegator;
import core.api.beacon.domain.Beacon;
import core.common.enums.InterfaceCommandType;
import core.common.enums.PresenceDataType;
import core.common.enums.SocketCommandType;

/**
 * ScannerDataPrepare 전략 Task 후처리 프로세스
 *
 * Location 변경 이벤트 인터페이스 처리
 *
 * Created by ucjung on 2017-09-12.
 */
public class ScannerDataPreparePostHandler implements PresenceTaskEventHandler {
    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        EmitSocketIO(presenceVo, serviceDelegator);
        UpateBeaconStateInfo(presenceVo, serviceDelegator);
    }

    private void EmitSocketIO(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        serviceDelegator.getSocketRequestService().send(SocketCommandType.UPDATE_MARKER, presenceVo);
        serviceDelegator.getRestApiRequestService().send(InterfaceCommandType.LOCATION_CHANGE, presenceVo);
    }

    private void UpateBeaconStateInfo(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        Beacon beacon = presenceVo.getTypeData(PresenceDataType.BEACON_INFO);
        Boolean shoudUpdateBeaconInfo = false;

        if (beacon == null) return;

        if (beacon.getBattery() != requestParam.getBattery()) {
            shoudUpdateBeaconInfo = true;
        }

        if (shoudUpdateBeaconInfo == true) {
            beacon.setBattery(requestParam.getBattLevel());
            serviceDelegator.getBeaconService().updateScannerBeaconState(beacon);
        }
    }

}
