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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Presence 로직 처리 시 사용할 Service Layer 위임자.
 *
 * Created by ucjung on 2017-06-07.
 */
@Service
public class PresenceServiceDelegatorImpl implements  PresenceServiceDelegator {
    @Value("#{config['presence.socket.url']}")
    private String socketURL;

    @Autowired
    private CommonRedisService commonRedisService;

    @Autowired
    private IsmRedisService ismRedisService;

    @Autowired
    protected PresenceRedisService presenceRedisService;

    @Autowired
    protected LogService logService;

    @Autowired
    private GeofencingService geofencingService;

    @Autowired
    private GeofencingRedisService geofencingRedisService;

    @Autowired
    private FloorAreaService floorAreaService;

    @Autowired
    private FloorCodeService floorCodeService;

    @Autowired
    private SocketRequestService socketRequestService;

    @Autowired
    private RestApiRequestService restApiRequestService;

    @Autowired
    private BeaconExternalService beaconExternalService;

    @Autowired
    private BeaconService beaconService;

    @Override
    public String getSocketURL() {
        return socketURL;
    }

    @Override
    public CommonRedisService getCommonRedisService() {
        return commonRedisService;
    }

    @Override
    public IsmRedisService getIsmRedisService() {
        return ismRedisService;
    }

    @Override
    public PresenceRedisService getPresenceRedisService() {
        return presenceRedisService;
    }

    @Override
    public LogService getLogService() {
        return logService;
    }

    @Override
    public GeofencingService getGeofencingService() {
        return geofencingService;
    }

    @Override
    public GeofencingRedisService getGeofencingRedisService() {
        return geofencingRedisService;
    }

    @Override
    public FloorAreaService getFloorAreaService() {
        return floorAreaService;
    }

    @Override
    public FloorCodeService getFloorCodeService() {
        return floorCodeService;
    }

    @Override
    public SocketRequestService getSocketRequestService() {
        return socketRequestService;
    }

    @Override
    public RestApiRequestService getRestApiRequestService() {
        return restApiRequestService;
    }

    @Override
    public BeaconExternalService getBeaconExternalService() {
        return beaconExternalService;
    }

    @Override
    public BeaconService getBeaconService() {
        return beaconService;
    }
}
