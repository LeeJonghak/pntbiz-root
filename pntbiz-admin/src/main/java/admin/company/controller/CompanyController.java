package admin.company.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import core.admin.company.domain.CompanyAllowRole;
import core.admin.auth.dao.LoginRoleDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import admin.company.service.CompanyService;
import core.admin.company.domain.Company;
import core.admin.company.domain.CompanySearchParam;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private LoginRoleDao loginRoleDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/admin/company/list.do", method=RequestMethod.GET)
	public ModelAndView list(CompanySearchParam param) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/company/list");

		param.setPageSize(30);
		Integer cnt = companyService.getCompanyCount(param);
		List<?> list = companyService.getCompanyList(param);
		list = companyService.bindCompanyList(list);

		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		logger.info("list {}", list);

		return mnv;
	}

	@RequestMapping(value="/admin/company/form.do", method=RequestMethod.GET)
	public ModelAndView form() {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/company/form");

		/**
		 * 전체 역할 목록과 해당 업체에 허용된 역할 목록
		 * 2014-12-04 nohsoo
		 */
		List<?> roleList = loginRoleDao.getLoginRoleListAll();
		mnv.addObject("roleList", roleList);

		return mnv;
	}

	@RequestMapping(value="/admin/company/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/company/mform");

		/**
		 * 전체 역할 목록과 해당 업체에 허용된 역할 목록
		 * 2014-12-04 nohsoo
		 */
		logger.debug(""+company.getComNum());
		List<?> allowRoleList = companyService.getCompanyAllowRoles(company);
		ArrayList<Integer> assignedRoleNumList = new ArrayList<>();

		for(Object companyAllowRole: allowRoleList){
		    assignedRoleNumList.add(((CompanyAllowRole) companyAllowRole).getRoleNum());
		}
		mnv.addObject("assignedRoleNumList", assignedRoleNumList);

        mnv.addObject("companyInfo", companyService.getCompanyInfo(company));
		mnv.addObject("roleList", loginRoleDao.getLoginRoleListAll());

		return mnv;
	}

	/**
	 * 업체 등록 처리
	 * edit 2014-12-04 nohsoo 업체등록시 선택한 허용역할 함께 추가되도록 수정
	 * @throws IOException
	 */
	@RequestMapping(value="/admin/company/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(HttpServletResponse response, Company company, @RequestParam(value = "role", required = false)int[] roleNums) throws IOException {
		response.setHeader("Access-Control-Allow-Origin","*");
		Map<String, String> info = new HashMap<String, String>();

		try {
            /**
             * 초기 위경도 좌표 입력 - 하영빌딩
             * 2015-01-14 jhlee
             */
            company.setLat("37.511129");
            company.setLng("127.056697");
            companyService.registerCompany(company, roleNums);
            info.put("result", "1");
        } catch(DataAccessException dae) {
            info.put("result", "2");
        } finally {
        }

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		System.out.println(json);
		return json;
	}

	@RequestMapping(value="/admin/company/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(HttpServletResponse response, Company company, @RequestParam(value = "role", required = false)int[] roleNums) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		Integer cnt = 0;

		@SuppressWarnings("unused")
		Company companyInfo = companyService.getCompanyInfo(company);
		// 기존아이디가 변경되었을 경우 체크
		// edit 2014-12-04 nohsoo 계정 관리 기능 분리로 인해서 제거
		/*
		if(!comID.equals(pComID)) {
			cnt = companyService.getCompanyIDCheck(company);
		}
		*/
		if(cnt > 0) {
			info.put("result", "3");
		} else {
			try {
				companyService.modifyCompany(company, roleNums);
				info.put("result", "1");
			} catch(DataAccessException dae) {
				info.put("result", "2");
			} finally {
			}
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/admin/company/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(Company company) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		try {
			companyService.removeCompany(company);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		System.out.println(json);
		return json;
	}
}