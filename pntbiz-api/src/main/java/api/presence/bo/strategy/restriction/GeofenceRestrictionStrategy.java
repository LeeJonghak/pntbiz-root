package api.presence.bo.strategy.restriction;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.PresenceDataType;
import core.common.enums.ZoneInOutStateType;
import core.common.enums.ZoneType;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import core.common.geofencing.domain.Geofencing;
import framework.web.util.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.common.enums.ZoneInOutStateType.*;

/**
 * In/Out Geofence 기반의 층 처리 전략 Class
 *
 * Created by ucjung on 2017-08-22.
 */
public class GeofenceRestrictionStrategy extends AbstractPresenceStrategy {
    private ScannerPresenceRedis currentPresenceRedis;
    private ScannerPresenceRedis previousPresenceRedis;
    private ScannerPresenceRequestParam requestParam;
    private String createdDate;
    private List<Geofencing> inboundGeofences;
    private Map<String, ZoneInOutState> geofencesInOutState = new HashMap<>();

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);

        createdDate = presenceVo.getTypeData(PresenceDataType.DATE_CREATION);
        requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        currentPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
        previousPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE);
        inboundGeofences = presenceVo.getTypeData(PresenceDataType.INBOUND_GEOFENCES);
    }

    @Override
    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {
        super.setPresenceDataPackage(presenceVo);
        presenceVo.setTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE, previousPresenceRedis);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, currentPresenceRedis);
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        // 1. In_Geofence 에 In 인 경우
        //      Floor In 상태인 경우 Pass
        //      Floor In 상태가 아닌 경우 Floor In 상태로 변경
        // 2. Out_Geofence 에 In 인 경우
        //      Floor In 상태인 경우 Out 상태로 변경
        //      Floor In 상태가 아닌 경우 Floor In 상태로 변경

        boolean isSameFloor = false;
        ZoneInOutStateType previousInOutStateType = DONE;

        // 이전상태 정보 설정 : 동일층 여부, 층상태
        if (presenceVo.isExistTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE)) {
            isSameFloor = previousPresenceRedis.getFloor().equals(requestParam.getFloor());
            previousInOutStateType = (previousPresenceRedis.getFloorInOutState() != null) ? previousPresenceRedis.getFloorInOutState().getState() : DONE;
        }

        // 현재층의 In/Out 여부 설정
        ZoneInOutStateType currentInOutStateType = checkInOutGeofence();

        if (isSameFloor == true) {                          // 1.       현재상태와 이전상태 층이 같을 경우
            if (currentInOutStateType == IN) {              // 1.1      현재상태가 IN 상태인 경우
                switch (previousInOutStateType) {
                    case IN :                               // 1.1.1    이전상태가 IN 상태인 경우 : STAY 설정
                    case STAY :                             // 1.1.1    이전상태가 STAY 상태인 경우 : STAY 설정
                        setFloorStay();
                        break;
                    case OUT :                              // 1.1.2    이전상태가 OUT, DONE 상태인 경우 : 현재상태 IN 설정
                    case DONE :
                        setFloorIn();
                        break;
                }
            } else if (currentInOutStateType == OUT){       // 1.2      현재상태가 OUT 상태인 경우
                switch (previousInOutStateType) {
                    case IN :                               // 1.2.1    이전상태가 IN, STAY 상태인 경우 : 이전층 OUT 설정
                    case STAY :
                        setPreviousFloorOut();
                        break;
                }
            } else if (currentInOutStateType == DONE) {     // 1.3      현재상태가 IN/OUT 펜스에 없는 경우
                switch (previousInOutStateType) {
                    case IN :                               // 1.2.1    이전상태가 IN, STAY 상태인 경우 : 현재상태 STAY 설정
                    case STAY :
                        setFloorStay();
                        break;
                }
            }
        } else {                                            // 2.       현재상태와 이전상태 층이 다를 경우
            if (
                    previousInOutStateType == IN ||         // 2.1      이전상태가 IN, STAY 일 경우 OUT 설정
                    previousInOutStateType == STAY
                )
                setPreviousFloorOut();
            if (currentInOutStateType == IN)                // 2.2      현재상태가 IN인 경우 IN 설정
                setFloorIn();
        }

    }

    /**
     * Inbound Geofence 중 In/Out Geofence 존재여부를 확인한다.
     * @return
     */
    private ZoneInOutStateType checkInOutGeofence() {
        ZoneInOutStateType result = DONE;
        for(Geofencing geofencing: inboundGeofences) {
            if (geofencing.getFcGroupName() == null) continue;
            if (geofencing.getFcGroupName().equals("FloorIn")) {
                result = IN;
            } else if (geofencing.getFcGroupName().equals("FloorOut")) {
                result = OUT;
            }
        }
        return result;
    }

    private void setPreviousFloorOut() {
        previousPresenceRedis.getFloorInOutState().setState(OUT);
        previousPresenceRedis.getFloorInOutState().setOutTime((int) DateUtil.str2timestamp(createdDate, "yyyy-MM-dd a HH:mm:ss"));
    }

    private void setFloorStay() {
        currentPresenceRedis.setFloorInOutState(previousPresenceRedis.getFloorInOutState());
        currentPresenceRedis.getFloorInOutState().setState(STAY);
    }

    // 신규 Floor In 처리
    private void setFloorIn() {
        ZoneInOutState floorState = new ZoneInOutState();
        floorState.setZoneId(requestParam.getFloor());
        floorState.setName(requestParam.getFloor());
        floorState.setState(IN);
        floorState.setZoneType(ZoneType.FLOOR);
        floorState.setInTime((int) DateUtil.str2timestamp(createdDate, "yyyy-MM-dd a HH:mm:ss"));
        currentPresenceRedis.setFloorInOutState(floorState);
    }
}
