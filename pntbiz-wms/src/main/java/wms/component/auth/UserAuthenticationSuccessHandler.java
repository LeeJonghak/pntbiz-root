package wms.component.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Repository;

import core.wms.auth.dao.LoginDao;
import core.wms.auth.domain.Login;
import framework.web.util.DateUtil;

@Repository
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private LoginDao loginDao;

	private String userid;
	private String targetUrlParameter;
	private String useReferer;
	private String defaultUrl;
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

	public String getTargetUrlParameter() {
		return targetUrlParameter;
	}

	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	}

	public String getUseReferer() {
		return useReferer;
	}

	public void setUseReferer(String useReferer) {
		this.useReferer = useReferer;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
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

	public UserAuthenticationSuccessHandler() {

	}

	/**
	 * login 성공처리
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication)
			throws IOException, ServletException {
		LoginDetail loginDetail = (LoginDetail) authentication.getPrincipal();
		Login login = new Login();
		login.setUserID(loginDetail.getUsername());
		login.setUserStatus(0);
		login.setLoginFailCnt(0);
		login.setLoginDate(DateUtil.getDate()); // dao에 timestamp로 자동으로 저장됨

		Login loginInfo = loginDao.getLogin(login.getUserID());
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
					loginDao.updateLogin(login);
				} catch (Exception e) {
					e.printStackTrace();
				}
				response.sendRedirect(defaultUrl);
			}
		}
	}

}