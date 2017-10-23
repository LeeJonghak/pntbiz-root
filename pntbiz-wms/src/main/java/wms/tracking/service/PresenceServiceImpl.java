package wms.tracking.service;

import java.util.List;
import java.util.Map;

import core.wms.tracking.dao.PresenceRedisDao;
import core.wms.tracking.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.wms.tracking.dao.InfluxdbPresenceDao;
import core.wms.tracking.dao.PresenceDao;
import framework.web.util.StringUtil;

@Service
public class PresenceServiceImpl implements PresenceService {

	@Autowired
	private PresenceDao presenceDao;

	@Autowired
	private InfluxdbPresenceDao influxdbPresenceDao;

	@Autowired
	private PresenceRedisDao presenceRedisDao;

	private @Value("#{config['log.db.type']}") String _log_db_type;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<?> getPresenceTargetLogList(PresenceTargetSearchParam param) throws DataAccessException {
		if(StringUtil.NVL(_log_db_type).equals("influxDB"))
			return influxdbPresenceDao.getPresenceTargetLogList(param);
		else
			return presenceDao.getPresenceTargetLogList(param);
	}

	@Override
	public List<?> getPresenceTargetListByBeacon(PresenceTargetSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getPresenceTargetListByBeacon(param);
		return list;
	}

	@Override
	public PresenceLog getPresenceLogInfo(PresenceLog presenceLog)	throws DataAccessException {
		PresenceLog presenceLogInfo = null;
		presenceLogInfo = presenceDao.getPresenceLogInfo(presenceLog);
		logger.info("getPresenceLogInfo {}", presenceLogInfo);
		return presenceLogInfo;
	}

	@Override
	public List<?> getPresenceLogList(PresenceLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getPresenceLogList(param);
		return list;
	}

	@Override
	public List<?> getPresenceLogListByLimit(PresenceLogSearchParam param) throws DataAccessException {
		if(StringUtil.NVL(_log_db_type).equals("influxDB"))
			return influxdbPresenceDao.getPresenceLogListByLimit(param);
		else
			return presenceDao.getPresenceLogListByLimit(param);
	}

    @Override
    public void insertPresenceLog(PresenceLog param) throws DataAccessException{
        presenceDao.insertPresenceLog(param);
    }


	@Override
	public List<?> getPresenceGfLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getPresenceGfLogListByLimit(param);
		return list;
	}

	@Override
	public Integer getPresenceGfLogCount(PresenceGfLogSearchParam param) throws DataAccessException {
		Integer cnt = presenceDao.getPresenceGfLogCount(param);
		return cnt;
	}

	@Override
	public List<?> getPresenceFloorLogListByLimit(PresenceFloorLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getPresenceFloorLogListByLimit(param);
		return list;
	}

	@Override
	public Integer getPresenceFloorLogCount(PresenceFloorLogSearchParam param) throws DataAccessException {
		Integer cnt = presenceDao.getPresenceFloorLogCount(param);
		return cnt;
	}

	@Override
    public void insertPresenceGeofenceLog(PresenceGfLog param) throws DataAccessException{
	    presenceDao.insertPresenceGeofenceLog(param);
    }

	@Override
	public List<?> getPresenceIoLogListByLimit(PresenceIoLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getPresenceIoLogListByLimit(param);
		return list;
	}

	@Override
	public List<?> getChartPresenceGfInOutLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getChartPresenceGfInOutLogListByLimit(param);
		return list;
	}

	@Override
	public List<?> getChartPresenceGfLogListByLimit(PresenceGfLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getChartPresenceGfLogListByLimit(param);
		return list;
	}

	@Override
	public List<?> getChartPresenceLogList(PresenceLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceDao.getChartPresenceLogList(param);
		return list;
	}

	@Override
	public List<Map<Object, Object>> getPreBeaconList(String uuid) {
		List<Map<Object, Object>> list = presenceRedisDao.getPreBeaconList(uuid);
		return list;
	}

	@Override
	public List<Map<Object, Object>> findRedisItem(String keyPattern) {
		List<Map<Object, Object>> list = presenceRedisDao.findRedisItem(keyPattern);
		return list;
	}

	@Override
	public List<String> findRedisString(String keyPattern) {
		List<String> list = presenceRedisDao.findRedisString(keyPattern);
		return list;
	}

	@Override
	public void removeRedisItem(String key) {
		presenceRedisDao.del(key);
	}

}
