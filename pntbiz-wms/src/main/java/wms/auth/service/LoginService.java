package wms.auth.service;

import org.springframework.dao.DataAccessException;

import core.wms.auth.domain.Login;
import core.wms.auth.domain.LoginOtp;

public interface LoginService {
	// 관리자로그인
	public Login getLoginInfo(Login login) throws DataAccessException;

	public void modifyLogin(Login login) throws Exception;
	public void modifyLoginExclusionPassword(Login login, String originalPassword) throws Exception;

	public LoginOtp getOtpNum(LoginOtp loginOtp) throws DataAccessException;
	public void sendOtpNum(LoginOtp loginOtp) throws DataAccessException;
	public boolean checkOtpNum(LoginOtp loginOtp) throws DataAccessException;

	public void modifyOtpNum(LoginOtp loginOtp) throws Exception;
}