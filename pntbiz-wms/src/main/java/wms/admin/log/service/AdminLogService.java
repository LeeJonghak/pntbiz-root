package wms.admin.log.service;

import org.springframework.dao.DataAccessException;

import core.wms.admin.log.domain.AdminLog;

public interface AdminLogService {	
	public void registerAdminLog(AdminLog adminLog) throws DataAccessException;
}