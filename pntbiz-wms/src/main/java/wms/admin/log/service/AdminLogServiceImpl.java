package wms.admin.log.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.admin.log.dao.AdminLogDao;
import core.wms.admin.log.domain.AdminLog;

@Service
public class AdminLogServiceImpl implements AdminLogService {
	
	@Autowired
	private AdminLogDao adminLogDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	@Transactional
	public void registerAdminLog(AdminLog adminLog) throws DataAccessException {
		logger.debug("adminLog :", adminLog);
		adminLogDao.insertAdminLog(adminLog);
	}
}