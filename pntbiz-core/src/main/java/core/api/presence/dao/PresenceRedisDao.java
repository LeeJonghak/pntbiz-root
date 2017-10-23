package core.api.presence.dao;

import core.api.log.domain.PresenceBeaconLog;
import core.api.log.domain.PresenceLog;
import framework.db.dao.RedisBaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
public class PresenceRedisDao extends RedisBaseDao {

    public String PRESENCE_BEACON_PREFIX = "PRESENCE_BEACON_";
    public String PRESENCE_BEACON_NOTIFY_PREFIX = "NOTIFY_PRESENCE_BEACON_";
    public String PRESENCE_DEVICE_PREFIX = "PRESENCE_DEVICE_";

    public void setPresenceBeacon(String key, PresenceLog presenceLog) {
        this.hmset(PRESENCE_BEACON_PREFIX + key, obj2map(presenceLog));
    }

    public void setPresenceDevice(String key, PresenceBeaconLog presenceBeaconLog) {
        this.hmset(PRESENCE_DEVICE_PREFIX + key, obj2map(presenceBeaconLog));
    }
    
    public void deletepresenceBeacon(String key) {
    	this.del(PRESENCE_BEACON_PREFIX + key);
    }
    
    public void deletepresenceDevice(String key) {
    	this.del(PRESENCE_DEVICE_PREFIX + key);
    }

    public Set<Object> getPresenceBeaconList(String keys) {
        Set<Object> list = this.keys(keys);
        return list;
    }

    public Map<Object, Object> getPresenceBeacon(String key) {
        return this.hgetall(PRESENCE_BEACON_PREFIX + key);
    }

    public void setRedisPresenceData(String key, String value) {
        this.set(PRESENCE_BEACON_PREFIX + key, value);
        this.set(PRESENCE_BEACON_NOTIFY_PREFIX + key, 0);
        this.expire(PRESENCE_BEACON_NOTIFY_PREFIX + key, 5);
    }

    public Object getRedisPresenceData(String key) {
        return this.get(PRESENCE_BEACON_PREFIX + key);
    }

}
