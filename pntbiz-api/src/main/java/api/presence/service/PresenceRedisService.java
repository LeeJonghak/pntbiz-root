package api.presence.service;

import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.api.log.domain.PresenceBeaconLog;
import core.api.log.domain.PresenceLog;

import java.util.List;

/**
 * Created by jhlee on 2017-04-15.
 */
public interface PresenceRedisService {
    public void setPresenceBeacon(String key, PresenceLog presenceLog);
    public void setPresenceDevice(String key, PresenceBeaconLog presenceBeaconLog);
    
    public void deletePresenceBeacon(String key);
    public void deletePresenceDevice(String key);

    public List<PresenceLog> getPresenceBeaconList(String keys);

    public PresenceLog getPresenceBeacon(String keys);

    public ScannerPresenceRedis getScannerPresenceRedis(String key);
    public void setScannerPresenceRedis(ScannerPresenceRedis scannerPresenceRedis);
}
