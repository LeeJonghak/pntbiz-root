package core.wms.auth.dao;

import java.util.HashMap;
import java.util.Map;

import core.wms.auth.domain.LoginOtp;
import core.wms.auth.domain.Login;
import core.wms.auth.domain.LoginRole;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class LoginDao extends BaseDao {
	
	// user check
	public Integer getLoginCheck(String userID, String userPW) throws DataAccessException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userID", userID);
		map.put("userPW", userPW);
		return (Integer) select("getLoginCheck", map);
	}

	public Login getLogin(String userID) throws DataAccessException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userID", userID);
		return (Login) select("getLogin", map);
	}
	
	// 2015-02-17 jhlee : id 충돌 > getLoginRoleCount > getLoginRoleCountByAdmin 변경
	public Integer getLoginRoleCount(LoginRole loginRole) throws DataAccessException {
		return (Integer) select("getLoginRoleCountByAdmin", loginRole);
	}
	//end
	
	public void insertLogin(Login login) throws DataAccessException {
		insert("insertLogin", login);
	}
	
	public void updateLogin(Login login) throws DataAccessException {
		update("updateLogin", login);	
	}
	
	public void deleteLogin(Login login) throws DataAccessException {
		delete("deleteLogin", login);
	}
	
	public void updateLoginFail(Login login) throws DataAccessException {
		update("updateLoginFail", login);	
	}
	
	public LoginOtp getLoginOtp(LoginOtp loginOtp) throws DataAccessException {
		return (LoginOtp) select("getLoginOtp", loginOtp);
	}
	
	public void insertLoginOtp(LoginOtp loginOtp) throws DataAccessException {
		insert("insertLoginOtp", loginOtp);
	}
	
	public void updateLoginOtp(LoginOtp loginOtp) throws DataAccessException {
		update("updateLoginOtp", loginOtp);	
	}
}