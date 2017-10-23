package core.wms.tracking.dao;

import java.util.List;

import core.wms.beacon.domain.BeaconStateSearchParam;
import core.wms.tracking.domain.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class PresenceDao extends BaseDao {

	public List<?> getPresenceTargetLogList(PresenceTargetSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceTargetLogList", param);
	}
	public List<?> getPresenceTargetListByBeacon(PresenceTargetSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceTargetListByBeacon", param);
	}
	public PresenceLog getPresenceLogInfo(PresenceLog presenceLog) throws DataAccessException {
		return (PresenceLog) select("getPresenceLogInfo", presenceLog);
	}
	public List<?> getPresenceLogList(PresenceLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceLogList", param);
	}
	public List<?> getPresenceLogListByLimit(PresenceLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceLogListByLimit", param);
	}
    public void insertPresenceLog(PresenceLog presenceLog) throws DataAccessException {
        insert("insertPresenceLog", presenceLog);
    }

	public List<?> getPresenceGfLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceGfLogListByLimit", param);
	}

	public Integer getPresenceGfLogCount(PresenceGfLogSearchParam param) throws DataAccessException {
		Integer count = (Integer)this.select("getPresenceGfLogCount", param);
		return count;
	}

	public List<?> getPresenceFloorLogListByLimit(PresenceFloorLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceFloorLogListByLimit", param);
	}

	public Integer getPresenceFloorLogCount(PresenceFloorLogSearchParam param) throws DataAccessException {
		Integer count = (Integer)this.select("getPresenceFloorLogCount", param);
		return count;
	}

    public void insertPresenceGeofenceLog(PresenceGfLog presenceGfLog) throws DataAccessException {
        insert("insertPresenceGeofenceLog", presenceGfLog);
    }


	public List<?> getPresenceIoLogListByLimit(PresenceIoLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getPresenceIoLogListByLimit", param);
	}


	public List<?> getChartPresenceGfInOutLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getChartPresenceGfInOutLogListByLimit", param);
	}
	public List<?> getChartPresenceGfLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getChartPresenceGfLogListByLimit", param);
	}
	public List<?> getChartPresenceLogList(PresenceLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getChartPresenceLogList", param);
	}
}