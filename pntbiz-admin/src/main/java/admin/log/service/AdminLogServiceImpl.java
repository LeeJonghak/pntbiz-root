package admin.log.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.admin.log.dao.AdminLogDao;
import core.admin.log.domain.AdminLog;
import core.admin.log.domain.AdminLogSearchParam;

@Service
public class AdminLogServiceImpl implements AdminLogService {
	
	@Autowired
	private AdminLogDao adminLogDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Integer getAdminLogCount(AdminLogSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = adminLogDao.getAdminLogCount(param);
		logger.info("getAdminLogCount {}", cnt);	
		return cnt;
	}

	@Override
	public List<?> getAdminLogList(AdminLogSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = adminLogDao.getAdminLogList(param);
		logger.info("getAdminLogList {}", list.size());	
		return list;
	}

	@Override
	@Transactional
	public void registerAdminLog(AdminLog AdminLog) throws DataAccessException {
		adminLogDao.insertAdminLog(AdminLog);
	}
}