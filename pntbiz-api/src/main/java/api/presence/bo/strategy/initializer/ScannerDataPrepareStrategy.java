package api.presence.bo.strategy.initializer;

import core.common.presence.bo.domain.ScannerPresenceRedis;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.enums.PresenceDataType;
import api.presence.bo.domain.PresenceDataPackage;
import core.api.beacon.domain.Beacon;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import framework.exception.ExceptionType;
import framework.exception.PresenceException;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import core.common.geofencing.domain.Geofencing;
import framework.util.ClassUtil;

import java.text.ParseException;
import java.util.List;

/**
 * Presence 처리를 위해 필요한 Data 조회 및 설정
 *
 * Created by ucjung on 2017-06-15.
 */
public class ScannerDataPrepareStrategy extends AbstractPresenceStrategy {
    private ScannerPresenceRequestParam requestParam;
    private String presenceRedisKey;

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);
        requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        presenceRedisKey = requestParam.getSUUID() + "_" + requestParam.getUUID() + "_" + requestParam.getMajorVer() +"_" + requestParam.getMinorVer();
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        setPreviousPresenceRedis(presenceVo);
        setCurrentPresenceRedis(presenceVo);
        setInboundGeofence(presenceVo);
        setBeaconInfo(presenceVo);
    }

    // Redis에 저장된 Presence Data를 조회하여 PresenceDataPackage에 set
    private void setPreviousPresenceRedis(PresenceDataPackage presenceVo) {
        ScannerPresenceRedis previousPresenceRedis = serviceDelegator.getPresenceRedisService().getScannerPresenceRedis(presenceRedisKey);
        if (previousPresenceRedis != null)
            presenceVo.setTypeData(PresenceDataType.REDIS_PREVRIVOUS_PRESENCE,previousPresenceRedis);
    }

    // Request Param으로 부터 CurrentPresenceRedis 정보 설정 및 PresenceDataPackage에 set
    public void setCurrentPresenceRedis(PresenceDataPackage presenceVo) {
        ScannerPresenceRedis currentPresenceRedis = new ScannerPresenceRedis();
        ClassUtil.copyProperties(currentPresenceRedis, requestParam);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, currentPresenceRedis);
    }

    private void setInboundGeofence(PresenceDataPackage presenceVo) {
        List<Geofencing> floorGeofences = (List<Geofencing>) serviceDelegator.getGeofencingService().getFloorGeofencingList(requestParam.getSUUID(), requestParam.getFloor());
        presenceVo.setTypeData(PresenceDataType.FLOOR_GEOFENCES, floorGeofences);

        try {
            List<Geofencing> inboundGeofences = serviceDelegator.getGeofencingService().checkGeofence(
                    requestParam.getSUUID(),
                    requestParam.getFloor(),
                    floorGeofences,
                    requestParam.getLat(),
                    requestParam.getLng()
            );
            presenceVo.setTypeData(PresenceDataType.INBOUND_GEOFENCES, inboundGeofences);
        } catch (ParseException e) {
            throw new PresenceException(ExceptionType.JSON_PARSE_EXCEPTION);
        }
    }

    /**
     * 비콘 외부데이타 설정정보 및 제한구역정보 조회
     * @param presenceVo
     */
    private void setBeaconInfo(PresenceDataPackage presenceVo) {
        Beacon beacon = new Beacon();
        beacon.setUUID(requestParam.getUUID());
        beacon.setMajorVer(requestParam.getMajorVer());
        beacon.setMinorVer(requestParam.getMinorVer());

        beacon = serviceDelegator.getBeaconService().getBeaconInfo(beacon);

        BeaconExternalWidthRestrictedZone beaconExternal = (BeaconExternalWidthRestrictedZone) serviceDelegator.getBeaconExternalService().info(beacon.getBeaconNum());
        presenceVo.setTypeData(PresenceDataType.BEACON_EXTERTNAL, beaconExternal);
        presenceVo.setTypeData(PresenceDataType.BEACON_INFO, beacon);
    }

}
