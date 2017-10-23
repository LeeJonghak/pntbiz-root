package admin.authpackage.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import core.admin.authpackage.dao.domain.AuthPackage;
import core.admin.authpackage.dao.domain.AuthPackageSearchParam;
import admin.authpackage.service.AuthPackageService;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.StringUtil;

/**
 * 웹관리자 - 최고 관리자 - 패키지 인증 관리
 * @author nohsoo
 *
 */
@Controller
public class AuthPackageController {

	@Autowired
	private AuthPackageService authPackageService; 	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/admin/authpackage/list.do", method=RequestMethod.GET)
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, 
			AuthPackageSearchParam param) throws IOException, ParseException{
		
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/authpackage/list");
		
		param.setOpt(StringUtil.NVL(request.getParameter("opt"), ""));
		param.setKeyword(StringUtil.NVL(request.getParameter("keyword"), ""));
		
		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = authPackageService.getAuthPackageCount(param);
		List<?> list = authPackageService.getAuthPackageList(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString  = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();
		
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		logger.info("list {}", list);
		
		return mnv;
	}	

	@RequestMapping(value="/admin/authpackage/form.do", method=RequestMethod.GET)
	public ModelAndView form(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/authpackage/form");			
		return mnv;
	}
	
	@RequestMapping(value="/admin/authpackage/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			AuthPackage authPackage) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/authpackage/mform");	
		
		authPackage.setPackageName(StringUtil.NVL(request.getParameter("packageName"), ""));		
		AuthPackage authPackageInfo = authPackageService.getAuthPackageInfo(authPackage);
		mnv.addObject("authPackageInfo", authPackageInfo);
		logger.info("authPackageInfo {}", authPackage);
		return mnv;
	}
	
	@RequestMapping(value="/admin/authpackage/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			AuthPackage authPackage) throws IOException {		
		Map<String, Object> info = new HashMap<String, Object>();
		authPackage.setPackageName(StringUtil.NVL(request.getParameter("packageName"), ""));		
		// 등록
		try {
			authPackageService.registerAuthPackage(authPackage);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	@RequestMapping(value="/admin/authpackage/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			AuthPackage authPackage) throws IOException {		
		Map<String, Object> info = new HashMap<String, Object>();
		authPackage.setPackageName(StringUtil.NVL(request.getParameter("packageName"), ""));
		authPackage.setPackageOldName(StringUtil.NVL(request.getParameter("packageOldName"), ""));
		// 수정
		try {
			authPackageService.modifyAuthPackage(authPackage);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	@RequestMapping(value="/admin/authpackage/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, HttpSession session, AuthPackage authPackage) throws IOException {		
		Map<String, String> info = new HashMap<String, String>();
		authPackage.setPackageName(StringUtil.NVL(request.getParameter("packageOldName"), ""));		
		try {
			authPackageService.removeAuthPackage(authPackage);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;		
	}
}
