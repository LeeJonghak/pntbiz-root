package custom.Hanwhatechwin.presence.bo.service;

import api.beacon.service.BeaconExternalService;
import api.beacon.service.BeaconService;
import api.common.service.CommonRedisService;
import api.geofencing.service.GeofencingRedisService;
import api.geofencing.service.GeofencingService;
import api.ism.service.IsmRedisService;
import api.log.service.LogService;
import api.map.service.FloorAreaService;
import api.map.service.FloorCodeService;
import api.presence.bo.service.RestApiRequestService;
import api.presence.bo.service.SocketRequestService;
import api.presence.service.PresenceRedisService;
import custom.Hanwhatechwin.intersyslink.service.HanwhatechwinService;
import org.springframework.stereotype.Service;

/**
 * Created by jhlee on 2017-09-25.
 */
@Service
public interface HanwhatechwinPresenceServiceDelegator {

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

    HanwhatechwinService getHanwhatechwinService();
}
