package core.admin.log.dao;
import java.util.List;

import core.admin.log.domain.AdminLog;
import core.admin.log.domain.AdminLogSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class AdminLogDao extends BaseDao {
	
	public Integer getAdminLogCount(AdminLogSearchParam param) throws DataAccessException {
		return (Integer) select("getAdminLogCount", param);
	}
	public List<?> getAdminLogList(AdminLogSearchParam param) throws DataAccessException {
		return (List<?>) list("getAdminLogList", param);
	}	
	public void insertAdminLog(AdminLog adminLog) throws DataAccessException {
		insert("insertAdminLog", adminLog);
	}
	
}