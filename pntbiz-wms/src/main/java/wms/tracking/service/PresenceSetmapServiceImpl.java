package wms.tracking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.tracking.dao.PresenceSetmapDao;
import core.wms.tracking.domain.PresenceSetmap;

@Service
public class PresenceSetmapServiceImpl implements PresenceSetmapService {
	
	@Autowired
	private PresenceSetmapDao presenceSetmapDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public PresenceSetmap getPresenceSetmapInfo(PresenceSetmap presenceSetmap)	throws DataAccessException {
		PresenceSetmap presenceSetmapInfo = null;
		presenceSetmapInfo = presenceSetmapDao.getPresenceSetmapInfo(presenceSetmap);
		logger.info("getPresenceSetmapInfo {}", presenceSetmapInfo);
		return presenceSetmapInfo;
	}
	
	@Override
	@Transactional
	public void registerPresenceSetmap(PresenceSetmap presenceSetmap)	throws DataAccessException {
		presenceSetmapDao.insertPresenceSetmap(presenceSetmap);		
	}
	
	@Override
	@Transactional
	public void modifyPresenceSetmap(PresenceSetmap presenceSetmap) throws DataAccessException {
		presenceSetmapDao.updatePresenceSetmap(presenceSetmap);
	}
	
}
