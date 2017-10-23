package core.wms.map.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;
import core.wms.map.domain.FloorCode;

@Repository
public class FloorCodeDao extends BaseDao {

    public Integer getFloorCodeCount(FloorCode param) {
        return (Integer) select("getFloorCodeCount", param);
    }

    public List<?> getFloorCodeList(FloorCode param) {
        return list("getFloorCodeList", param);
    }

    public FloorCode getFloorCodeInfo(FloorCode param) throws DataAccessException {
        return (FloorCode) select("getFloorCodeInfo", param);
    }

    public void insertFloorCode(FloorCode param) throws DataAccessException {
        insert("insertFloorCode", param);
    }

    public void updateFloorCode(FloorCode param) throws DataAccessException {
        update("updateFloorCode", param);
    }

    public void deleteFloorCodeList(Map<String, Object> param) throws DataAccessException {
        delete("deleteFloorCodeList", param);
    }

    public List<?> getFloorCodeIdList(Map<String, Object> param) {
        return list("getFloorCodeIdList", param);
    }
}
