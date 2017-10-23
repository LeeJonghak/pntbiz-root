package core.wms.geofencing.dao;

import core.wms.geofencing.domain.GeofencingGroupMapping;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingGroup;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import framework.db.dao.BaseDao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class GeofencingDao extends BaseDao {

    public List<?> getGeofencingAll(HashMap<String, Object> param) {
        List<?> list = this.list("getGeofencingAll", param);
        return list;
    }

    public List<?> getGeofencingList(GeofencingGroupSearchParam param) {
        return this.list("getGeofencingList", param);
    }

    public int getGeofencingCount(GeofencingGroupSearchParam param) {
        return (int)this.select("getGeofencingCount", param);
    }

    public Geofencing getGeofencing(HashMap<String, Object> param) {
        return (Geofencing)this.select("getGeofencing", param);
    }

    public long insertGeofencing(Geofencing geofencing) {
        this.insert("insertGeofencing", geofencing);
        return geofencing.getFcNum();
    }

    public void modifyGeofencing(Geofencing geofencing) {
        this.update("modifyGeofencing", geofencing);
    }

    public void deleteGeofencing(HashMap<String, Object> param) {
        this.delete("deleteGeofencing", param);
    }

    public Integer getGeofencingGroupCount(GeofencingGroupSearchParam param) throws DataAccessException {
    	Integer cnt = 0;
        cnt = (Integer) select("getGeofencingGroupCount", param);
        return cnt;
    }

    public List<?> getGeofencingGroupList(GeofencingGroupSearchParam param) throws DataAccessException {
        List<?> list = null;
        list = (List<?>) list("getGeofencingGroupList", param);
        return list;
    }

    public GeofencingGroup getGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
        return (GeofencingGroup) select("getGeofencingGroup", geofencingGroup);
    }

    public void insertGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
        insert("insertGeofencingGroup", geofencingGroup);
    }

    public void updateGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
        update("updateGeofencingGroup", geofencingGroup);
    }

    public void deleteGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
        delete("deleteGeofencingGroup", geofencingGroup);
    }

    public void insertGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException {
        insert("insertGeofencingGroupMapping", geofencingGroupMapping);
    }

    public void updateGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException {
        insert("updateGeofencingGroupMapping", geofencingGroupMapping);
    }

    public void deleteGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException {
        delete("deleteGeofencingGroupMapping", geofencingGroupMapping);
    }


}
