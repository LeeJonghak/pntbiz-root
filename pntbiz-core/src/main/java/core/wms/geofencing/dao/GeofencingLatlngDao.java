package core.wms.geofencing.dao;

import core.wms.geofencing.domain.GeofencingLatlng;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class GeofencingLatlngDao extends BaseDao {

    public List<?> getGeofencingLatlngAll(HashMap<String, Object> param) {
        List<?> list = this.list("getGeofencingLatlngAll", param);
        return list;
    }

    public List<?> getGeofencingLatlngList(HashMap<String, Object> param) {
        List<?> list = this.list("getGeofencingLatlngList", param);
        return list;
    }

    public void insertGeofencingLatlng(GeofencingLatlng geofencingLatlng) {
        this.insert("insertGeofencingLatlng", geofencingLatlng);
    }

    public void deleteGeofencingLatlng(HashMap<String, Object> param) {
        this.delete("deleteGeofencingLatlng", param);
    }

}
