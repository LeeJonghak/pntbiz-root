package wms.tracking.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.wms.tracking.dao.PresenceBeaconDao;
import core.wms.tracking.domain.PresenceBeaconLog;
import core.wms.tracking.domain.PresenceBeaconLogSearchParam;
import core.wms.tracking.domain.PresenceBeaconTargetSearchParam;

@Service
public class PresenceBeaconServiceImpl implements PresenceBeaconService {

	@Autowired
	private PresenceBeaconDao presenceBeaconDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<?> getPresenceBeaconTargetLogList(PresenceBeaconTargetSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceBeaconDao.getPresenceBeaconTargetLogList(param);
		return list;
	}

	@Override
	public List<?> getPresenceBeaconTargetListByBeacon(PresenceBeaconTargetSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceBeaconDao.getPresenceBeaconTargetListByBeacon(param);
		return list;
	}

	@Override
	public PresenceBeaconLog getPresenceBeaconLogInfo(PresenceBeaconLog presenceBeaconLog)	throws DataAccessException {
		PresenceBeaconLog presenceBeaconLogInfo = null;
		presenceBeaconLogInfo = presenceBeaconDao.getPresenceBeaconLogInfo(presenceBeaconLog);
		logger.info("getPresenceBeaconLogInfo {}", presenceBeaconLogInfo);
		return presenceBeaconLogInfo;
	}

	@Override
	public List<?> getPresenceBeaconLogList(PresenceBeaconLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceBeaconDao.getPresenceBeaconLogList(param);
		return list;
	}

	@Override
	public List<?> getPresenceBeaconLogListByLimit(PresenceBeaconLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = presenceBeaconDao.getPresenceBeaconLogListByLimit(param);
		return list;
	}

    @Override
	public void insertPresenceBeaconLog(PresenceBeaconLog param) throws DataAccessException{
        presenceBeaconDao.insertPresenceBeaconLog(param);
	}
}
