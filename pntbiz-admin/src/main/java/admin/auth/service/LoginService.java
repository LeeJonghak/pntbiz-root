package admin.auth.service;

import java.text.ParseException;
import java.util.List;

import core.admin.auth.domain.LoginRole;
import core.admin.auth.domain.Login;
import org.springframework.dao.DataAccessException;

import core.admin.user.domain.User;

import framework.web.util.PagingParam;

public interface LoginService {
	// 관리자로그인
	public User getUser(String userID, String userPW);

	// 로그인계정 관리
	// 2014-11-17 nohsoo
	public Integer getLoginCount(PagingParam param) throws DataAccessException;
	public List<?> getLoginList(PagingParam param) throws DataAccessException;
	public List<?> bindLoginList(List<?> list) throws ParseException;
	public Integer getLoginRoleCount(LoginRole loginRole)  throws DataAccessException;

	public Login getLoginInfo(Login login) throws DataAccessException;


	public void registerLogin(Login login) throws Exception;
	public void modifyLogin(Login login) throws Exception;
	public void removeLogin(Login login) throws DataAccessException;
	// end
}