package wms.info.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.Security;
import framework.web.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.wms.admin.log.domain.AdminLog;
import wms.admin.log.service.AdminLogService;
import core.wms.auth.domain.Login;
import core.wms.auth.domain.LoginOtp;
import wms.auth.service.LoginService;
import wms.component.auth.LoginDetail;

@Controller
public class InfoAccountController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private AdminLogService adminLogService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/info/account/itsme.do", method=RequestMethod.GET)
	public ModelAndView infoAccountItsme(Model model, HttpServletRequest request, HttpServletResponse response,
			Login login) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/account/itsme");

		LoginDetail loginDetail = Security.getLoginDetail();
		login.setUserID(loginDetail.getUsername());
		Login loginInfo = loginService.getLoginInfo(login);

		mnv.addObject("loginInfo", loginInfo);

		logger.info("loginInfo {}", loginInfo);
		return mnv;
	}

	@RequestMapping(value="/info/account/itsmeok.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAccountItsmeOK(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Login login) throws Exception {
		Map<String, String> info = new HashMap<String, String>();
		LoginDetail loginDetail = Security.getLoginDetail();
		login.setUserID(loginDetail.getUsername());
		login.setUserPW(StringUtil.NVL(request.getParameter("userPW"), ""));
		Login loginchk = loginService.getLoginInfo(login);
		if(loginchk==null) {
			info.put("result", "2");
		} else {
			// 비밀번호가 맞을 경우
			if(loginchk.getUserPW().equals(Security.encrypt(login.getUserPW()))) {
				String key = Security.encrypt(loginDetail.getUsername());
				info.put("result", "1");
				info.put("key", key);
			} else {
				info.put("result", "2");
			}
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/info/account/mform.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView infoAccountMForm(Model model, HttpServletRequest request, HttpServletResponse response,
			Login login) throws IOException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/account/mform");

		String key = StringUtil.NVL(request.getParameter("key"), "");

		LoginDetail loginDetail = Security.getLoginDetail();
		login.setUserID(loginDetail.getUsername());
		Login loginInfo = loginService.getLoginInfo(login);

		if("".equals(key) || !Security.encrypt(loginDetail.getUsername()).equals(key)) {
			response.sendRedirect("/info/account/itsme.do");
		} else {
			mnv.addObject("loginInfo", loginInfo);
			logger.info("loginInfo {}", loginInfo);
		}
		return mnv;
	}

	@RequestMapping(value="/info/account/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAccountMod(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Login login) throws Exception {
		Map<String, String> info = new HashMap<String, String>();

		//String key = StringUtil.NVL(request.getParameter("key"), "");

		LoginDetail loginDetail = Security.getLoginDetail();
		login.setUserID(loginDetail.getUsername());
		Login loginchk = loginService.getLoginInfo(login);

		if(loginchk==null) {
			info.put("result", "2");
		} else {
			//if("".equals(key) || !StringUtil.encrypt(loginDetail.getUsername()).equals(key)) {
			//	info.put("result", "2");
			//} else {
				try {
					login.setLoginDate(DateUtil.getDate("yyyyMMddHHmss"));
					login.setPwModDate(DateUtil.getDate("yyyyMMddHHmss"));
					if(StringUtils.isEmpty(login.getUserPW())) {
						/**
						 * 비밀번호를 입력하지 않았을 경우에는 기존 암호를 사용
						 */
						loginService.modifyLoginExclusionPassword(login, loginchk.getUserPW());
						info.put("result", "1");

						AdminLog adminLog = new AdminLog();
						adminLog.setAdminID(loginDetail.getUsername());
						adminLog.setTableName("TB_LOGIN");
						adminLog.setCrudType("U");
						adminLog.setPk(login.getUserID());
						adminLogService.registerAdminLog(adminLog);

					} else {
						if(loginchk.getUserPW().equals(Security.encrypt(login.getUserPW()))) {
							info.put("result", "3");
						} else {
							loginService.modifyLogin(login);
							info.put("result", "1");

							AdminLog adminLog = new AdminLog();
							adminLog.setAdminID(loginDetail.getUsername());
							adminLog.setTableName("TB_LOGIN");
							adminLog.setCrudType("U");
							adminLog.setPk(login.getUserID());
							adminLogService.registerAdminLog(adminLog);
						}
					}
				} catch(DataAccessException Mod) {
					logger.error("exception", Mod);
					info.put("result", "2");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");
				} finally {
				}
			//}
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/info/passwd/mform.do", method=RequestMethod.POST)
	public ModelAndView infoPasswordMForm(Model model, HttpServletRequest request, HttpServletResponse response,
			LoginOtp loginOtp, Login login) throws IOException {
		ModelAndView mnv = new ModelAndView();
		String key = StringUtil.NVL(request.getParameter("key"), "");
		try {
			LoginOtp loginOtpInfo = loginService.getOtpNum(loginOtp);
			String dkey = Security.encrypt(loginOtpInfo.getUserID() + loginOtpInfo.getOtpNum());
			if(key.equals(dkey)) {
				login.setUserID(loginOtp.getUserID());
				Login loginInfo = loginService.getLoginInfo(login);
				mnv.addObject("loginInfo", loginInfo);
				mnv.addObject("key", key);
				mnv.setViewName("/info/passwd/mform");
			} else {
				mnv.setViewName("/auth/login");
				response.sendRedirect("/auth/login.do");
			}
		} catch(Exception e) {
			mnv.setViewName("/auth/login");
			response.sendRedirect("/auth/login.do");
		}
		return mnv;
	}

	@RequestMapping(value="/info/passwd/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoPasswdMod(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			LoginOtp loginOtp, Login login) throws Exception {
		Map<String, String> info = new HashMap<String, String>();

		String key = StringUtil.NVL(request.getParameter("key"), "");
//	String userID = StringUtil.NVL(request.getParameter("userID"), "");
//	String userPW = StringUtil.NVL(request.getParameter("userPW"), "");
//	String userName = StringUtil.NVL(request.getParameter("userName"), "");
//
//	login.setUserID(userID);
		try {
			loginOtp.setUserID(login.getUserID());
			LoginOtp loginOtpInfo = loginService.getOtpNum(loginOtp);
			String dkey = Security.encrypt(loginOtpInfo.getUserID() + loginOtpInfo.getOtpNum());
			if(key.equals(dkey)) {
				Login loginchk = loginService.getLoginInfo(login);
				if(loginchk==null) {
					info.put("result", "3");
				} else {
					try {
						login.setLoginDate(DateUtil.getDate("yyyyMMddHHmss"));
						login.setPwModDate(DateUtil.getDate("yyyyMMddHHmss"));
						loginService.modifyLogin(login);
						// 수정후 otp인증번호 초기화하여 1회성으로 처리 되도록
						loginOtpInfo.setOtpNum("OTPNUM");
						loginService.modifyOtpNum(loginOtpInfo);
						info.put("result", "1");
					} catch(DataAccessException Mod) {
						logger.error("exception", Mod);
						info.put("result", "2");
					} catch(Exception e) {
						logger.error("exception", e);
						info.put("result", "2");
					}
				}
			} else {
				info.put("result", "2");
			}
		} catch(Exception e) {
			info.put("result", "2");
			logger.error("exception", e);
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}


}