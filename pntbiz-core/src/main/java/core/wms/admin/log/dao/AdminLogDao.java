package core.wms.admin.log.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.admin.log.domain.AdminLog;
import framework.db.dao.BaseDao;

@Repository
public class AdminLogDao extends BaseDao {
	
	public void insertAdminLog(AdminLog adminLog) throws DataAccessException {
		insert("insertAdminLog", adminLog);
	}
	
}