package core.wms.map.dao;

import core.wms.map.domain.Area;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 */
@Repository
public class AreaDao extends BaseDao {

    public Integer getAreaCount(Map param) throws DataAccessException {
        return (Integer) select("getAreaCount", param);
    }

    public List<?> getAreaList(Map param) throws DataAccessException {
        return (List<?>) list("getAreaList", param);
    }
    
    public List<?> getAreaLatlngList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("getAreaLatlngList", param);
    }
    
    public List<?> getAreaCenterList(Map param) throws DataAccessException {
        return (List<?>) list("getAreaCenterList", param);
    }
    
    public List<?> getAreaCenterLatlngList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("getAreaCenterLatlngList", param);
    }

    public Area getAreaInfo(Map param) throws DataAccessException {
        return (Area) select("getAreaInfo", param);
    }

    public void insertArea(Area area) throws DataAccessException {
        insert("insertArea", area);
    }

    public void modifyArea(Area area) throws DataAccessException {
        insert("modifyArea", area);
    }

    public void deleteArea(Map param) throws DataAccessException {
        insert("deleteArea", param);
    }

}
