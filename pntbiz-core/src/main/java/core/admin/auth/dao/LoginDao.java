package core.admin.auth.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.admin.auth.domain.LoginRole;
import core.admin.auth.domain.Login;
import core.admin.user.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;
import framework.web.util.PagingParam;

@Repository
public class LoginDao extends BaseDao{

	// user check
	public Integer getLoginCheck(String userID, String userPW) throws DataAccessException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userID", userID);
		map.put("userPW", userPW);
		return (Integer) select("getLoginCheck", map);
	}

	public User getUser(String userID, String userPW) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userID", userID);
		map.put("userPW", userPW);
		return (User) select("getUser", map);
	}

	public Login getLogin(String userID) throws DataAccessException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userID", userID);
		return (Login) select("getLogin", map);
	}

	// 2014-11-17 nohsoo
	public Integer getLoginCount(PagingParam param) throws DataAccessException {
		return (Integer) select("getLoginCount", param);
	}

	public List<?> getLoginList(PagingParam param) throws DataAccessException {
		return (List<?>) list("getLoginList", param);
	}
	// 2015-02-17 jhlee : id 충돌 > getLoginRoleCount > getLoginRoleCountByAdmin 변경
	public Integer getLoginRoleCount(LoginRole loginRole) throws DataAccessException {
		return (Integer) select("getLoginRoleCountByAdmin", loginRole);
	}

	public void insertLogin(Login login) throws DataAccessException {
		insert("insertLogin", login);
	}

	public void updateLogin(Login login) throws DataAccessException {
		update("updateLogin", login);
	}

	public void deleteLogin(Login login) throws DataAccessException {
		delete("deleteLogin", login);
	}
	//end
}