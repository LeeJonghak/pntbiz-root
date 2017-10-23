package api.presence.bo.strategy.floor;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.PresenceDataType;
import core.common.enums.ZoneInOutStateType;
import core.common.enums.ZoneType;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import api.presence.bo.service.PresenceServiceDelegator;
import framework.web.util.DateUtil;

/**
 * Floor In/Out 일반 모듈
 *
 * 별도의 펜스를 사용하지 않고 측위서버에서 전송되는 층정보(floor)를 기준으로 In/Stay/Out 판별
 *
 * Created by ucjung on 2017-06-15.
 */
public class FloorCommonStrategy extends AbstractPresenceStrategy {
    private ScannerPresenceRedis currentPresenceRedis;
    private ScannerPresenceRedis previousPresenceRedis;
    private ScannerPresenceRequestParam requestParam;
    private String createdDate;

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);

        createdDate = presenceVo.getTypeData(PresenceDataType.DATE_CREATION);
        requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        currentPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
        previousPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE);
    }

    @Override
    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {
        super.setPresenceDataPackage(presenceVo);
        presenceVo.setTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE, previousPresenceRedis);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, currentPresenceRedis);
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        if (presenceVo.isExistTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE))
            if (previousPresenceRedis.getFloorInOutState().getZoneId().equals(requestParam.getFloor()))
                setFloorStay();
            else
                setFloorChange();
        else
            setFloorIn();
    }

    private void setFloorChange() {
        setFloorIn();
        setPreviousFloorOut();
    }

    private void setPreviousFloorOut() {
        previousPresenceRedis.getFloorInOutState().setState(ZoneInOutStateType.OUT);
        previousPresenceRedis.getFloorInOutState().setOutTime((int) DateUtil.str2timestamp(createdDate, "yyyy-MM-dd a HH:mm:ss"));
    }

    private void setFloorStay() {
        currentPresenceRedis.setFloorInOutState(previousPresenceRedis.getFloorInOutState());
        currentPresenceRedis.getFloorInOutState().setState(ZoneInOutStateType.STAY);
    }

    // 신규 Floor In 처리
    private void setFloorIn() {
        ZoneInOutState floorState = new ZoneInOutState();
        floorState.setZoneId(requestParam.getFloor());
        floorState.setName(requestParam.getFloor());
        floorState.setState(ZoneInOutStateType.IN);
        floorState.setZoneType(ZoneType.FLOOR);
        floorState.setInTime((int) DateUtil.str2timestamp(createdDate, "yyyy-MM-dd a HH:mm:ss"));
        currentPresenceRedis.setFloorInOutState(floorState);
    }
}
