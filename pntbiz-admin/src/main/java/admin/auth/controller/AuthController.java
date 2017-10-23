package admin.auth.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import admin.auth.service.LoginService;

import framework.web.util.CookieUtil;
import framework.web.util.StringUtil;
 
@Controller
public class AuthController {	
	
	@Autowired
	private LoginService loginService;	
	
	@RequestMapping("/auth/login.do")
	public ModelAndView Login(HttpServletRequest request) throws IOException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/auth/login");
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
	

}