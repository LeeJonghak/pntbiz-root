package core.api.map.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.api.map.domain.Floor;
import framework.db.dao.BaseDao;

@Repository
public class FloorDao extends BaseDao {

	public List<?> getFloorList(Floor floor) throws DataAccessException {
		return list("getFloorList", floor);
	}
	
}
