package core.wms.tracking.dao;

import java.util.List;

import core.wms.tracking.domain.PresenceBeaconLog;
import core.wms.tracking.domain.PresenceBeaconLogSearchParam;
import core.wms.tracking.domain.PresenceBeaconTargetSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class PresenceBeaconDao extends BaseDao {

	public List<?> getPresenceBeaconTargetLogList(PresenceBeaconTargetSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceBeaconTargetLogList", param);
	}
	public List<?> getPresenceBeaconTargetListByBeacon(PresenceBeaconTargetSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceBeaconTargetListByBeacon", param);
	}

	public PresenceBeaconLog getPresenceBeaconLogInfo(PresenceBeaconLog presenceBeaconLog) throws DataAccessException {
		return (PresenceBeaconLog) select("getPresenceBeaconLogInfo", presenceBeaconLog);
	}
	public List<?> getPresenceBeaconLogList(PresenceBeaconLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceBeaconLogList", param);
	}
	public List<?> getPresenceBeaconLogListByLimit(PresenceBeaconLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceBeaconLogListByLimit", param);
	}
	public void insertPresenceBeaconLog(PresenceBeaconLog presenceBeaconLog) throws DataAccessException {
		insert("insertPresenceBeaconLog", presenceBeaconLog);
	}
	public void updatePresenceBeaconLog(PresenceBeaconLog presenceBeaconLog) throws DataAccessException {
		update("updatePresenceBeaconLog", presenceBeaconLog);
	}
}