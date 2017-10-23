package wms.tracking.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.tracking.domain.PresenceBeaconLog;
import core.wms.tracking.domain.PresenceBeaconLogSearchParam;
import core.wms.tracking.domain.PresenceBeaconTargetSearchParam;

public interface PresenceBeaconService {

	public List<?> getPresenceBeaconTargetLogList(PresenceBeaconTargetSearchParam param) throws DataAccessException;
	public List<?> getPresenceBeaconTargetListByBeacon(PresenceBeaconTargetSearchParam param) throws DataAccessException;

	public PresenceBeaconLog getPresenceBeaconLogInfo(PresenceBeaconLog presenceBeaconLog) throws DataAccessException;
	public List<?> getPresenceBeaconLogList(PresenceBeaconLogSearchParam param) throws DataAccessException;
	public List<?> getPresenceBeaconLogListByLimit(PresenceBeaconLogSearchParam param) throws DataAccessException;

    public void insertPresenceBeaconLog(PresenceBeaconLog param) throws DataAccessException;
}