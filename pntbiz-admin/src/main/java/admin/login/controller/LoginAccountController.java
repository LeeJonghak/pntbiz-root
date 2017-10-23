package admin.login.controller;

import core.admin.company.dao.CompanyAllowRoleDao;
import core.admin.company.domain.CompanySearchParam;
import admin.company.service.CompanyService;
import core.admin.auth.dao.LoginRoleDao;
import core.admin.auth.domain.Login;
import core.admin.auth.domain.LoginSearchParam;
import admin.auth.service.LoginService;
import framework.auth.LoginDetail;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginAccountController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private LoginRoleDao loginRoleDao;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyAllowRoleDao companyAllowRoleDao;

	private Logger logger = LoggerFactory.getLogger(getClass());


	@RequestMapping(value="/admin/login/account/list.do", method=RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request, LoginSearchParam param) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/login/account/list");

		LoginDetail loginDetail = Security.getLoginDetail();

		param.setPageSize(30);
		if(loginDetail.getCompanyNumber()>0) {
			param.setComNum(loginDetail.getCompanyNumber());
		}

		Integer cnt = loginService.getLoginCount(param);
		List<?> list = loginService.getLoginList(param);
		list = loginService.bindLoginList(list);

		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString  = request.getServletPath();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);

		logger.info("list {}", list);

		return mnv;
	}

	@RequestMapping(value="/admin/login/account/form.do", method=RequestMethod.GET)
	public ModelAndView form() {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/login/account/form");

		this.setAccountRegInfo(mnv);

		return mnv;
	}

	@RequestMapping(value="/admin/login/account/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(HttpServletRequest request, Login login) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/login/account/mform");

		this.setAccountRegInfo(mnv);
		mnv.addObject("loginInfo", loginService.getLoginInfo(login));

		return mnv;
	}

	private void setAccountRegInfo(ModelAndView mnv){
		LoginDetail loginDetail = Security.getLoginDetail();
		CompanySearchParam comParam = new CompanySearchParam();
		comParam.setPageSizeZero();
		List<?> roleList = null;

		/**
		 * 업체 목록, 역할(role) 목록
		 */
		if(loginDetail.getCompanyNumber()>0) {
			comParam.setComNum(loginDetail.getCompanyNumber());
			roleList = companyAllowRoleDao.getCompanyAllowRoleList(comParam);
		} else {
			roleList = loginRoleDao.getLoginRoleListAll();
		}

		mnv.addObject("companyList", companyService.getCompanyList(comParam));
		mnv.addObject("roles", roleList);
	}

	@RequestMapping(value="/admin/login/account/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, Login login) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		Login loginchk = loginService.getLoginInfo(login);

		if(loginchk!=null) {
			info.put("result", "3");
		} else {
			try {
				loginService.registerLogin(login);
				info.put("result", "1");
			} catch(DataAccessException dae) {
				logger.error("exception", dae);
				info.put("result", "2");
			} catch(Exception e) {
				logger.error("exception", e);
				info.put("result", "2");
			} finally {
			}
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/admin/login/account/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, Login login) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		Login loginchk = loginService.getLoginInfo(login);

		if(loginchk==null) {
			info.put("result", "3");
		} else {
			try {
				loginService.modifyLogin(login);
				info.put("result", "1");
			} catch(DataAccessException Mod) {
				logger.error("exception", Mod);
				info.put("result", "2");
			} catch(Exception e) {
				logger.error("exception", e);
				info.put("result", "2");
			} finally {
			}
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/admin/login/account/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, HttpSession session, Login login) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		try {
			loginService.removeLogin(login);
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
