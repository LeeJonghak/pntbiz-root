package wms.component.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import core.wms.auth.dao.LoginDao;
import core.wms.auth.domain.Login;
import framework.web.util.DateUtil;


public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private LoginDao loginDao;

	private String userid;
	private String userpw;
	private String inputname;
	private String defaultFailureUrl;
	private String lockedUrl;
	private String expiredUrl;

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}

	public String getInputname() {
		return inputname;
	}

	public void setInputname(String inputname) {
		this.inputname = inputname;
	}

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public String getLockedUrl() {
		return lockedUrl;
	}

	public void setLockedUrl(String lockedUrl) {
		this.lockedUrl = lockedUrl;
	}

	public String getExpiredUrl() {
		return expiredUrl;
	}

	public void setExpiredUrl(String expiredUrl) {
		this.expiredUrl = expiredUrl;
	}

	public UserAuthenticationFailureHandler() {
		this.userid = "userid";
		this.userpw = "userpw";
		this.inputname = "userid";
		this.defaultFailureUrl = "/auth/login.do?failure=1";
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException)
			throws IOException, ServletException {
		Login login = new Login();
		login.setUserID(request.getParameter(this.userid));
		Login loginInfo = loginDao.getLogin(login.getUserID());

		if(loginInfo == null) {
			response.sendRedirect(defaultFailureUrl);
		} else {
			if(loginInfo.getUserStatus() >=9) {
				response.sendRedirect(lockedUrl);
			} else {
				// 로그인 90일 이상 체크
				String sDate = DateUtil.timestamp2str(Long.parseLong(loginInfo.getLoginDate()), "yyyyMMdd");
				String eDate = DateUtil.getDate("yyyyMMdd");
				// 비밀번호 변경일 90일 이상
				String sDate2 = DateUtil.timestamp2str(Long.parseLong(loginInfo.getPwModDate()), "yyyyMMdd");
				String eDate2 = DateUtil.getDate("yyyyMMdd");
				if(DateUtil.getBetweenDayCount(sDate, eDate) >= 90 || DateUtil.getBetweenDayCount(sDate2, eDate2) >= 90) {
					response.sendRedirect(expiredUrl);
				} else {
					try {
						if(loginInfo.getLoginFailCnt() >= 4) {
							login.setUserStatus(9);
						}
						loginDao.updateLoginFail(login);
					} catch (Exception e) {
						e.printStackTrace();
					}
					response.sendRedirect(defaultFailureUrl);
				}
			}
		}
	}

}