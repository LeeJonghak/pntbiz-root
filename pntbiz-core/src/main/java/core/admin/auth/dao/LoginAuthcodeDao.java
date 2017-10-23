package core.admin.auth.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.admin.auth.domain.LoginAuthcode;
import framework.db.dao.BaseDao;
import framework.web.util.PagingParam;

@Repository
public class LoginAuthcodeDao extends BaseDao {
	
	public Integer getLoginAuthcodeCheck(LoginAuthcode loginAuthcode) throws DataAccessException {
		return (Integer) select("getLoginAuthcodeCheck", loginAuthcode);
	}

	public Integer getLoginAuthcodeCount(PagingParam param) throws DataAccessException {
		return (Integer) select("getLoginAuthcodeCount", param);
	}
	
	public List<?> getLoginAuthcodeList(PagingParam param) throws DataAccessException {
		return (List<?>) list("getLoginAuthcodeList", param);
	}
	
	public List<?> getLoginAuthcodeListAll() throws DataAccessException {
		return (List<?>) list("getLoginAuthcodeListAll");
	}
	
	public LoginAuthcode getLoginAuthcodeInfo(LoginAuthcode loginAuthcode) throws DataAccessException {
		return (LoginAuthcode) select("getLoginAuthcodeInfo", loginAuthcode);
	}
	
	public void insertLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException {
		insert("insertLoginAuthcode", loginAuthcode);
	}

	public void updateLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException {
		update("updateLoginAuthcode", loginAuthcode);
	}

	public void deleteLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException {
		delete("deleteLoginAuthcode", loginAuthcode);
	}

}
