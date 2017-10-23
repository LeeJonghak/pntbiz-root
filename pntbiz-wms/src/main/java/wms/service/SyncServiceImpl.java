package wms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.sync.dao.SyncDao;
import core.wms.sync.domain.Sync;

@Service
public class SyncServiceImpl implements SyncService {
		
	@Autowired
	private SyncDao syncDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());	

	@Override
	@Transactional
	public Sync getSyncInfo(Sync sync) throws DataAccessException {
		Sync syncInfo = null;
		syncInfo = syncDao.getSyncInfo(sync);
		// 추가
		if(syncInfo == null) {
			syncDao.insertSync(sync);
			syncInfo = syncDao.getSyncInfo(sync);
		} 
		logger.info("syncInfo {}", syncInfo);	
		return syncInfo;
	}
	
	@Override
	public List<?> getSyncList(Sync sync) throws DataAccessException {
		List<?> syncList = syncDao.getSyncList(sync);
		logger.debug("syncList info:", syncList);
		return syncList;
	}
	
	@Override
	@Transactional
	public void updateSync(Sync sync) throws DataAccessException {
		Sync syncInfo = null;
		syncInfo = syncDao.getSyncInfo(sync);
		// 추가
		if(syncInfo == null) {
			syncDao.insertSync(sync);
		} 
		syncDao.updateSync(sync);
	}
}
