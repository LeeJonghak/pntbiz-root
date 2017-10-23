package core.admin.user.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;
import core.admin.user.domain.User;
import core.admin.user.domain.UserSearchParam;

@Repository
public class UserDao extends BaseDao {
	
	// User Info	
	public Integer getUserCount(UserSearchParam param) throws DataAccessException {
		return (Integer) select("getUserCount", param);
	}
	
	public List<?> getUserList(UserSearchParam param) throws DataAccessException {
		return (List<?>) list("getUserList", param);
	}
	
	public User getUserInfo(User user) throws DataAccessException {
		return (User) select("getUserInfo", user);
	}
	
	public User getUserInfoByID(User user) throws DataAccessException {
		return (User) select("getUserInfoByID", user);
	}
	
}