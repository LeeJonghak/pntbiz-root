package api.presence.bo.strategy.restriction;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.PresenceDataType;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import core.common.beacon.domain.BeaconRestrictedZone;
import core.common.enums.ZoneInOutStateType;
import core.common.enums.ZoneType;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.strategy.AbstractPresenceStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 비인가 지역 처리 전략
 *
 * 진입한 Floor or Geofence에 대하여 승인된 구역이지를 확인하고 Zone 정보에 반영한다.
 *
 * Created by ucjung on 2017-08-22.
 */
public class RestrictionZoneStrategy extends AbstractPresenceStrategy {
    private ScannerPresenceRedis currentPresenceRedis;
    private List<BeaconRestrictedZone> restrictedZones;

    @Override
    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        super.setInitData(presenceVo, serviceDelegator);
        currentPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
    }

    @Override
    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {
        super.setPresenceDataPackage(presenceVo);
        presenceVo.setTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE, currentPresenceRedis);
    }

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        if (currentPresenceRedis == null) return;

        // 제한구역 정보 조회
        restrictedZones = ((BeaconExternalWidthRestrictedZone)presenceVo.getTypeData(PresenceDataType.BEACON_EXTERTNAL)).getRestrictedZone();

        ZoneInOutState floorInOutState = currentPresenceRedis.getFloorInOutState();
        currentPresenceRedis.setFloorInOutState(checkRestrict(floorInOutState));

        Map<String, ZoneInOutState> geofencesInOutStates = currentPresenceRedis.getGeofencesInOutState();
        for (String key: geofencesInOutStates.keySet()) {
            geofencesInOutStates.put(key, checkRestrict(geofencesInOutStates.get(key)));
        }
        currentPresenceRedis.setGeofencesInOutState(geofencesInOutStates);
    }

    private ZoneInOutState checkRestrict(ZoneInOutState zone) {
        if (zone == null) return zone;
        if (zone.getState() == ZoneInOutStateType.IN) {
            for (BeaconRestrictedZone restrictedZone : restrictedZones) {
                if (restrictedZone.getZoneType() == zone.getZoneType() && restrictedZone.getZoneId().equals(zone.getZoneId())) {
                    zone.setPermitted(restrictedZone.getPermitted());
                    break;
                }
            }
        }
        return zone;
    }

    private List<RestrictedZone> getRestrictedZones() {
        List<RestrictedZone> restrictedZones = new ArrayList<>();

        RestrictedZone restrictedZone =  new RestrictedZone();
        restrictedZone.setZoneId("3");
        restrictedZone.setZoneType(ZoneType.FLOOR);
        restrictedZone.setRestricted("R");

        restrictedZones.add(restrictedZone);

        RestrictedZone restrictedZone2 =  new RestrictedZone();
        restrictedZone2.setZoneId("101543");
        restrictedZone2.setZoneType(ZoneType.GEOFENCE);
        restrictedZone2.setRestricted("R");

        restrictedZones.add(restrictedZone2);

        return restrictedZones;
    }

    public class RestrictedZone {
        private String zoneId;
        private ZoneType zoneType;
        private String restricted;  // P : 허가지역, R : 제한지역

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public ZoneType getZoneType() {
            return zoneType;
        }

        public void setZoneType(ZoneType zoneType) {
            this.zoneType = zoneType;
        }

        public String getRestricted() {
            return restricted;
        }

        public void setRestricted(String restricted) {
            this.restricted = restricted;
        }
    }
}
