package api.presence.service;

import core.api.presence.dao.PresenceSetmapDao;
import core.api.presence.domain.PresenceSetmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
	
}
