package core.wms.map.dao;

import core.wms.map.domain.AreaLatlng;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 */
@Repository
public class AreaLatlngDao extends BaseDao {

    public Integer getDongLatlngCount(Map param) throws DataAccessException {
        return (Integer) select("getDongLatlngCount", param);
    }

    public List<?> getAreaLatlngList(Map param) throws DataAccessException {
        return (List<?>) list("getAreaLatlngList", param);
    }

    public void insertAreaLatlng(AreaLatlng areaLatlng) throws DataAccessException {
        insert("insertAreaLatlng", areaLatlng);
    }

    public void deleteAreaLatlng(Map param) throws DataAccessException {
        insert("deleteAreaLatlng", param);
    }

}
