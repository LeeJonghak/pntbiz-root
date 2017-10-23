package admin.auth.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import core.admin.auth.domain.LoginAuthcode;
import framework.web.util.PagingParam;

public interface LoginAuthcodeService {
	// Authcode 중복 확인
	public Integer getLoginAuthcodeCheck(String authCode);

	public Integer getLoginAuthcodeCount(PagingParam param) throws DataAccessException;
	public List<?> getLoginAuthcodeList(PagingParam param) throws DataAccessException;
	public List<?> getLoginAuthcodeListAll() throws DataAccessException;
	public List<?> bindLoginAuthcodeList(List<?> list) throws ParseException;
	public LoginAuthcode getLoginAuthcodeInfo(LoginAuthcode loginAuthcode) throws DataAccessException;
	
	public void registerLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException;
	public void modifyLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException;
	public void removeLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException;
}