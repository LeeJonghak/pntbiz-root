package api.geofencing.service;

import core.common.geofencing.domain.Geofencing;

import java.util.List;
import java.util.Map;

/**
 * Created by jhlee on 2017-04-15.
 */
public interface GeofencingRedisService {

    public Map<Object, Object> getGeofencingInfo(String uuid);
    public List<Map<Object, Object>> getGeofencingList(String uuid);
    public List<Geofencing> checkGeofence(String SUUID, String floor, Double lat, Double lng);

    public String getGeofenceZoneTagPrefix();
    public void setGeofenceZoneTag(String key, Map<Object, Object> zoneData);
    public Map<Object, Object> getGeofenceZoneTag(String key);
    public void deleteGeofenceZoneTag(String key);

}
