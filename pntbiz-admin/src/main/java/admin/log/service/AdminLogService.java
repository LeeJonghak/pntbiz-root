package admin.log.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.admin.log.domain.AdminLog;
import core.admin.log.domain.AdminLogSearchParam;

public interface AdminLogService {
	public Integer getAdminLogCount(AdminLogSearchParam param) throws DataAccessException;
	public List<?> getAdminLogList(AdminLogSearchParam param) throws DataAccessException;	
	
	public void registerAdminLog(AdminLog adminLog) throws DataAccessException;
}