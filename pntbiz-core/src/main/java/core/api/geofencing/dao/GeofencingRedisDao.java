package core.api.geofencing.dao;

import framework.db.dao.RedisBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class GeofencingRedisDao extends RedisBaseDao {

    public String GEOFENCE_LIST_PREFIX = "GEOFENCE_LIST_";
    public String GEOFENCE_INFO_PREFIX = "GEOFENCE_INFO_";
    public String GEOFENCE_LATLNG_PREFIX = "GEOFENCE_LATLNG_";
    public String GEOFENCE_ZONE_TAG_PREFIX = "GEOFENCE_ZONE_TAG_";

    @Deprecated
    public boolean checkGeofenceList(String uuid) {
        Long length = this.scard(GEOFENCE_LIST_PREFIX + uuid);
        if(length > 0) {
            return true;
        } else {
            return false;
        }
    }
    public Set<?> getGeofenceList(String uuid) {
        Set<?> set = this.smembers(GEOFENCE_LIST_PREFIX + uuid);
        return set;
    }
    public Map<Object, Object> getGeofenceInfo(String fcNum) {
        return this.hgetall(GEOFENCE_INFO_PREFIX + fcNum);
    }
    public List<Object> getGeofenceLatlng(String fcNum) {
        return this.lrange(GEOFENCE_LATLNG_PREFIX + fcNum, 0, -1);
    }

    public void setGeofenceZoneTag(String key, Map<Object, Object> zoneData) {
        this.hmset(GEOFENCE_ZONE_TAG_PREFIX + key, zoneData);
    }
    public Map<Object, Object> getGeofenceZoneTag(String key) {
        return this.hgetall(GEOFENCE_ZONE_TAG_PREFIX + key);
    }
    public void deleteGeofenceZoneTag(String key) {
        this.del(GEOFENCE_ZONE_TAG_PREFIX + key);
    }


}
