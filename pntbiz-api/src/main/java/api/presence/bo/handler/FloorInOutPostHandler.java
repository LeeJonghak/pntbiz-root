package api.presence.bo.handler;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ZoneInOutState;
import api.presence.bo.service.PresenceServiceDelegator;
import core.api.log.domain.FloorLog;
import core.common.enums.InterfaceCommandType;
import core.common.enums.PresenceDataType;
import core.common.enums.ZoneInOutStateType;
import framework.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FloorInOut Task 후처리 모듈
 *
 * Floor In 상태
 * ㄴ Floor InOut Log Insert
 * ㄴ Floor In 외부 Interface 처리
 * Floor Out 상태 :
 * ㄴ Floor InoOut Log Update
 * ㄴ Floor Out 외부 Interface 처리
 *
 * Created by ucjung on 2017-06-11.
 */
public class FloorInOutPostHandler implements PresenceTaskEventHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PresenceDataPackage presenceVo;
    private FloorLog floorLog = new FloorLog();

    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        this.presenceVo = presenceVo;
        ScannerPresenceRedis previous = presenceVo.getTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE);
        ScannerPresenceRedis current = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);

        if (previous != null) {
            doPostFloorOut(previous.getFloorInOutState(), serviceDelegator);
        }

        doPostFloorIn(current.getFloorInOutState(), serviceDelegator);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, current);
    }

    private void doPostFloorIn(ZoneInOutState currentState, PresenceServiceDelegator serviceDelegator) {
        if (currentState == null) return;
        if (currentState.getState().equals(ZoneInOutStateType.IN)) {
            logger.info("Floor In Post Process : \n{}", JsonUtils.writeValue(currentState));
            ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
            setFloorLogData((ScannerPresenceRequestParam) presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM), currentState);
            currentState.setLogNum(serviceDelegator.getLogService().registerFloorLog(floorLog));
            serviceDelegator.getRestApiRequestService().send(InterfaceCommandType.FLOOR_IN, presenceVo, currentState);

        }
    }

    private void doPostFloorOut(ZoneInOutState previousState, PresenceServiceDelegator serviceDelegator) {
        if (previousState == null) return;
        if (previousState.getState().equals(ZoneInOutStateType.OUT)) {
            logger.info("Floor Out Post Process : \n{}", JsonUtils.writeValue(previousState));
            setFloorLogData((ScannerPresenceRequestParam) presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM), previousState);
            serviceDelegator.getLogService().modifyFloorLog(floorLog);
            serviceDelegator.getRestApiRequestService().send(InterfaceCommandType.FLOOR_OUT, presenceVo, previousState);
        }
    }

    private void setFloorLogData(ScannerPresenceRequestParam requestParam, ZoneInOutState inOutState) {
        floorLog.setSUUID(requestParam.getSUUID());
        floorLog.setUUID(requestParam.getUUID());
        floorLog.setMajorVer(requestParam.getMajorVer());
        floorLog.setMinorVer(requestParam.getMinorVer());
        floorLog.setFloor(requestParam.getFloor());
        floorLog.setLogNum(inOutState.getLogNum());
        floorLog.setBeaconName("");
        floorLog.setTargetName("");
        floorLog.setLogDesc("");
        floorLog.setInDate(inOutState.getInTime());
        floorLog.setOutDate(inOutState.getOutTime());
        floorLog.setPermitted(inOutState.getPermitted());
    }

}
