package core.api.map.dao;

import core.api.map.domain.FloorCode;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FloorCodeDao extends BaseDao {

    public FloorCode getFloorCodeInfo(FloorCode param) throws DataAccessException {
        return (FloorCode) select("getFloorCodeInfo", param);
    }

    public List<FloorCode> getFloorCodeList(FloorCode param) throws DataAccessException {
        return (List<FloorCode>) list("getFloorCodeList", param);
    }

    public List<FloorCode> getFloorCodeLevelNoDescList(FloorCode floorCode) {
        return (List<FloorCode>) list("getFloorCodeLevelNoDescList", floorCode);
    }

    public List<FloorCode> getFloorCodeLevelNoAscList(FloorCode floorCode) {
        return (List<FloorCode>) list("getFloorCodeLevelNoAscList", floorCode);
    }

}
