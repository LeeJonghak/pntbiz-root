package wms.auth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.wms.auth.domain.Login;
import core.wms.auth.domain.LoginOtp;
import framework.Security;
import framework.web.util.CookieUtil;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import wms.auth.service.LoginService;
import wms.component.auth.LoginDetail;

@Controller
public class AuthController {

	@Autowired
	private LoginService loginService;

	@RequestMapping("/auth/login.do")
	public ModelAndView Login(HttpServletRequest request) throws IOException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/auth/login");
		CookieUtil cookie = new CookieUtil(request);
		if(cookie.exists("saveID")) {
			Cookie ckSaveID = cookie.getCookie("saveID");
			String saveID = StringUtil.NVL(ckSaveID.getValue());
			String checked = ("1".equals(saveID)) ? "checked" : "";
			mnv.addObject("checked", checked);
		}
		if(cookie.exists("userID")) {
			Cookie ckUserID = cookie.getCookie("userID");
			String userID = StringUtil.NVL(ckUserID.getValue());
			mnv.addObject("userID", userID);
		}
		return mnv;
	}

	@RequestMapping("/auth/otpchk.do")
	public String OtpCheck(HttpServletRequest request) throws IOException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/auth/otp");

		// 로그인이 되어 있는 경우는 메인으로 이동
		try {
			LoginDetail loginDetail = Security.getLoginDetail();
			if(!"".equals(loginDetail.getUsername()) && loginDetail != null) {
				return "redirect:" +"/";
			} else {
				return "redirect:" + "/auth/otp.do";
			}
		} catch(Exception e) {
			return "redirect:" + "/auth/otp.do";
		}
	}

	@RequestMapping("/auth/otp.do")
	public ModelAndView Otp(HttpServletRequest request) throws IOException{
		ModelAndView mnv = new ModelAndView();
		try {
			LoginDetail loginDetail = Security.getLoginDetail();
			if(!"".equals(loginDetail.getUsername()) && loginDetail != null) {
				mnv.setViewName("/auth/login");
			} else {
				mnv.setViewName("/auth/otp");
			}
		} catch(Exception e) {
			mnv.setViewName("/auth/otp");
		}
		return mnv;
	}

	// otp 인증번호 발송
	@RequestMapping(value= "/auth/otpNum.do", method=RequestMethod.POST)
	@ResponseBody
	public String OtpNum(HttpServletRequest request,
			LoginOtp loginOtp, Login login) throws IOException{
		Map<String, Object> info = new HashMap<String, Object>();
		loginOtp.setUserID(StringUtil.NVL(request.getParameter("userID"), ""));
		try {
			loginService.sendOtpNum(loginOtp);
			info.put("result", "1");
		} catch (Exception e) {
			info.put("result", "2");
			info.put("message", e.getMessage());
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 계정 잠금 해제 처리
	@RequestMapping(value= "/auth/activate.do", method=RequestMethod.POST)
	@ResponseBody
	public String Activate(HttpServletRequest request,
			LoginOtp loginOtp) throws IOException{
		Map<String, Object> info = new HashMap<String, Object>();
		loginOtp.setUserID(StringUtil.NVL(request.getParameter("userID"), ""));
		loginOtp.setOtpNum(StringUtil.NVL(request.getParameter("otpNum"), ""));
		try {
			if(loginService.checkOtpNum(loginOtp) == true) {
				String key = Security.encrypt(loginOtp.getUserID() + loginOtp.getOtpNum());
				info.put("result", "1");
				info.put("key", key);
			} else {
				info.put("result", "2");
			}
		} catch (Exception e) {
			info.put("result", "3");
			info.put("message", e.getMessage());
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}


}