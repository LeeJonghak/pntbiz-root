package core.wms.geofencing.dao;

import framework.db.dao.RedisBaseDao;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingLatlng;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Set;

@Repository
public class GeofencingRedisDao extends RedisBaseDao {

    public String GEOFENCE_LIST_PREFIX = "GEOFENCE_LIST_";
    public String GEOFENCE_INFO_PREFIX = "GEOFENCE_INFO_";
    public String GEOFENCE_LATLNG_PREFIX = "GEOFENCE_LATLNG_";

    @Deprecated
    public boolean checkGeofenceList(String uuid) {
        Long length = this.scard(GEOFENCE_LIST_PREFIX + uuid);
        if(length > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Long setGeofenceList(String uuid, String fcNum) {
        return this.sadd(GEOFENCE_LIST_PREFIX + uuid, fcNum);
    }
    public Set<?> getGeofenceList(String uuid) {
        Set<?> set = this.smembers(uuid);
        return set;
    }
    public void deleteGeofenceList(String uuid, String fcNum) {
        this.srem(GEOFENCE_LIST_PREFIX + uuid, fcNum);
    }

    public void setGeofenceInfo(String fcNum, Geofencing geofencing) {
        this.hmset(GEOFENCE_INFO_PREFIX + fcNum, this.obj2map(geofencing));
    }
    public void deleteGeofenceInfo(String fcNum) {
        this.del(GEOFENCE_INFO_PREFIX + fcNum);
    }

    public void setGeofenceLatlng(String fcNum, ArrayList<GeofencingLatlng> latlngs) {
        this.del(GEOFENCE_LATLNG_PREFIX + fcNum);
        for (GeofencingLatlng latlng : latlngs) {
            this.setGeofenceLatlng(fcNum, latlng);
        }
    }
    public void setGeofenceLatlng(String fcNum, GeofencingLatlng gflatlng) {
        String latlng = gflatlng.getLat().toString() + "|" + gflatlng.getLng().toString();
        this.rpush(GEOFENCE_LATLNG_PREFIX + fcNum, latlng);
    }
    public void setGeofenceLatlng(String fcNum, Double lat, Double lng) {
        String latlng = lat.toString() + "|" + lng.toString();
        this.rpush(GEOFENCE_LATLNG_PREFIX + fcNum, latlng);
    }
    public void deleteGeofenceLatlng(String fcNum) {
        this.del(GEOFENCE_LATLNG_PREFIX + fcNum);
    }

}
