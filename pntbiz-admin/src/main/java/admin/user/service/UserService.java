package admin.user.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import core.admin.user.domain.User;
import core.admin.user.domain.UserSearchParam;

public interface UserService {		
	public Integer getUserCount(UserSearchParam param) throws DataAccessException;
	public List<?> getUserList(UserSearchParam param) throws DataAccessException;
	public User getUserInfo(User user) throws DataAccessException;
	public User getUserInfoByID(User user) throws DataAccessException;
	public List<?> bindUserList(List<?> list) throws ParseException;
}