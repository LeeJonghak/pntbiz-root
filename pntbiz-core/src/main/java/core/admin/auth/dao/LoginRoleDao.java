package core.admin.auth.dao;

import java.util.List;

import core.admin.auth.domain.LoginRole;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;
import framework.web.util.PagingParam;

@Repository
public class LoginRoleDao extends BaseDao {
	
	public Integer getLoginRoleCount(PagingParam param) throws DataAccessException {
		return (Integer) select("getLoginRoleCount", param);
	}
	
	public List<?> getLoginRoleList(PagingParam param) throws DataAccessException {
		return (List<?>) list("getLoginRoleList", param);
	}
	
	public List<?> getLoginRoleListAll() {
		return (List<?>) list("getLoginRoleListAll");
	}
	
	public LoginRole getLoginRoleInfo(LoginRole loginRole)	throws DataAccessException {
		return (LoginRole) select("getLoginRoleInfo", loginRole);
	}
	
	public Integer insertLoginRole(LoginRole loginRole) throws DataAccessException {
		insert("insertLoginRole", loginRole);
		return loginRole.getRoleNum();
	}

	public void updateLoginRole(LoginRole loginRole) throws DataAccessException {
		update("updateLoginRole", loginRole);
	}
	
	public void deleteLoginRole(LoginRole loginRole) throws DataAccessException {
		delete("deleteLoginRole", loginRole);
	}
	
}
