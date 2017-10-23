package api.presence.bo.service;

import api.beacon.service.BeaconExternalService;
import api.beacon.service.BeaconService;
import api.common.service.CommonRedisService;
import api.geofencing.service.GeofencingRedisService;
import api.geofencing.service.GeofencingService;
import api.ism.service.IsmRedisService;
import api.log.service.LogService;
import api.map.service.FloorAreaService;
import api.map.service.FloorCodeService;
import api.presence.service.PresenceRedisService;
import org.springframework.stereotype.Service;

/**
 * Created by ucjung on 2017-06-07.
 */
@Service
public interface PresenceServiceDelegator {

    String getSocketURL();

    CommonRedisService getCommonRedisService();

    IsmRedisService getIsmRedisService();

    PresenceRedisService getPresenceRedisService();

    LogService getLogService();

    GeofencingService getGeofencingService();

    GeofencingRedisService getGeofencingRedisService();

    FloorAreaService getFloorAreaService();

    FloorCodeService getFloorCodeService();

    SocketRequestService getSocketRequestService();

    RestApiRequestService getRestApiRequestService();

    BeaconExternalService getBeaconExternalService();

    BeaconService getBeaconService();
}
