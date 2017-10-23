package core.wms.stat.dao;

import java.util.List;

import core.wms.stat.domain.StatBeaconSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class StatDao extends BaseDao {	
	
	public Integer statBeaconMonitorCount(StatBeaconSearchParam param) throws DataAccessException {
		return (Integer) select("statBeaconMonitorCount", param);
	}
	
	public List<?> statBeaconMonitorList(StatBeaconSearchParam param) throws DataAccessException {
		return (List<?>) list("statBeaconMonitorList", param);
	}
	
	public Integer statBeaconPresenceCount(StatBeaconSearchParam param) throws DataAccessException {
		return (Integer) select("statBeaconPresenceCount", param);
	}
}
