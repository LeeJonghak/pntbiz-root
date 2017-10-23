package core.api.geofencing.dao;

import core.api.geofencing.domain.GeofencingLatlng;
import core.api.geofencing.domain.GeofencingZone;
import framework.db.dao.BaseDao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import core.common.geofencing.domain.Geofencing;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class GeofencingDao extends BaseDao {

    public Geofencing getGeofencing(Geofencing geofencing) {
        return (Geofencing) select("getGeofencing", geofencing);
    }

	
    public List<?> getGeofencingZone(GeofencingZone geofencingZone) {
        return list("getGeofencingZone", geofencingZone);
    }

    @SuppressWarnings("unchecked")
	@Cacheable(value = "CACHE_GEOFENCE_LATLNG_LIST", key="#geofencing.fcNum.toString()")
    public List<GeofencingLatlng> getGeofencingLatlngs(Geofencing geofencing) {
        return (List<GeofencingLatlng>)list("getGeofencingLatlngs", geofencing);
    }

    @Cacheable(value = "CACHE_GEOFENCE_LIST", key="#param.get('UUID').toString()")
    public List<?> getGeofencingList(HashMap<String, Object> param) {
        return this.list("getGeofencingList", param);
    }

    @Cacheable(value = "CACHE_FLOOR_GEOFENCE_LIST", key="#param.get('UUID').toString().concat('-').concat(#param.get('floor').toString())")
    public List<?> getFloorGeofencingList(HashMap<String, Object> param) {
        return this.list("getGeofencingList", param);
    }

    public List<?> getGeofencingListByAll(HashMap<String, Object> param) {
        return this.list("getGeofencingListByAll", param);
    }

    public List<?> getGeofencingListByCache(String companyUUID) {
        return this.list("getGeofencingListByCache", companyUUID);
    }

    public List<?> getGeofencingContentsList(HashMap<String, Object> param) {
        return this.list("getGeofencingContentsList", param);
    }

    public List<?> getGeofencingActionList(HashMap<String, Object> param) {
        return this.list("getGeofencingActionList", param);
    }

    public List<GeofencingZone> getGeofencingFloorCodeList(HashMap<String, Object> param) {
        return (List<GeofencingZone>) list("getGeofencingFloorCodeList", param);
    }
    
    public void updateGeofencing(Geofencing geofencing) {
        update("updateGeofencing", geofencing);
    }

}
