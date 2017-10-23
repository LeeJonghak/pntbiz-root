package core.admin.auth.dao;

import java.util.List;

import core.admin.auth.domain.LoginAuthcode;
import core.admin.auth.domain.LoginRole;
import core.admin.auth.domain.LoginRoleAuthorities;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;
import framework.web.util.PagingParam;

@Repository
public class LoginRoleAuthoritiesDao extends BaseDao {	
	
	public Integer getLoginRoleAuthoritiesCount(PagingParam param) 	throws DataAccessException {
		return (Integer) select("getLoginRoleAuthoritiesCount", param);
	}
	
	public List<?> getLoginRoleAuthoritiesList(PagingParam param) throws DataAccessException {
		return (List<?>) list("getLoginRoleAuthoritiesList", param);
	}
	
	public LoginRoleAuthorities getLoginRoleAuthoritiesInfo(LoginRoleAuthorities loginRoleAuthorities)
			throws DataAccessException {
		return (LoginRoleAuthorities) select("getLoginRoleAuthoritiesInfo", loginRoleAuthorities);
	}
	
	public void insertLoginRoleAuthorities(LoginRoleAuthorities loginRoleAuthorities) 
			throws DataAccessException {
		insert("insertLoginRoleAuthorities", loginRoleAuthorities);
	}
	
	public void updateLoginRoleAuthorities(LoginRoleAuthorities loginRoleAuthorities) 
			throws DataAccessException {
		update("updateLoginRoleAuthorities", loginRoleAuthorities);
	}
	
	public void updateLoginRoleAuthoritiesAll(LoginAuthcode loginAuthcode) throws DataAccessException {
		update("updateLoginRoleAuthoritiesAll", loginAuthcode);
	}
	
	public void deleteLoginRoleAuthorities(LoginRoleAuthorities loginRoleAuthorities) 
			throws DataAccessException {
		delete("deleteLoginRoleAuthorities", loginRoleAuthorities);
	}
	
	public void clearLoginRoleAuthorities(LoginRole loginRole) throws DataAccessException {
		insert("clearLoginRoleAuthorities", loginRole);
	}
	
	public void deleteLoginRoleAuthoritiesAll(LoginAuthcode loginAuthcode) throws DataAccessException {
		delete("deleteLoginRoleAuthoritiesAll", loginAuthcode);
	}
	
	public List<?> getLoginRoleAuthorities(LoginRoleAuthorities loginRoleAuthorities) 
			throws DataAccessException {		
		return (List<?>) list("getLoginRoleAuthorities", loginRoleAuthorities);
	}
}
