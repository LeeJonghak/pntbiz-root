package wms.tracking.service;

import java.util.List;
import java.util.Map;

import core.wms.tracking.domain.*;
import org.springframework.dao.DataAccessException;

public interface PresenceService {

	public List<?> getPresenceTargetLogList(PresenceTargetSearchParam param) throws DataAccessException;
	public List<?> getPresenceTargetListByBeacon(PresenceTargetSearchParam param) throws DataAccessException;

	public PresenceLog getPresenceLogInfo(PresenceLog presenceLog) throws DataAccessException;
	public List<?> getPresenceLogList(PresenceLogSearchParam param) throws DataAccessException;
	public List<?> getPresenceLogListByLimit(PresenceLogSearchParam param) throws DataAccessException;
    public void insertPresenceLog(PresenceLog param) throws DataAccessException;

	public List<?> getPresenceGfLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException;
	public Integer getPresenceGfLogCount(PresenceGfLogSearchParam param) throws DataAccessException;
	public List<?> getPresenceFloorLogListByLimit(PresenceFloorLogSearchParam param) throws DataAccessException;
	public Integer getPresenceFloorLogCount(PresenceFloorLogSearchParam param) throws DataAccessException;
    public void insertPresenceGeofenceLog(PresenceGfLog param) throws DataAccessException;


	public List<?> getPresenceIoLogListByLimit(PresenceIoLogSearchParam param) throws DataAccessException;

	public List<?> getChartPresenceGfInOutLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException;
	public List<?> getChartPresenceGfLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException;
	public List<?> getChartPresenceLogList(PresenceLogSearchParam plsParam1) throws DataAccessException;

	public List<Map<Object, Object>> getPreBeaconList(String uuid);

	public List<Map<Object, Object>> findRedisItem(String keyPattern);

	public List<String> findRedisString(String keyPattern);

	public void removeRedisItem(String key);
}