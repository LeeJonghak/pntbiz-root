package wms.info.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.Security;
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

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;

import com.jcraft.jsch.SftpException;

import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;

@Controller
public class InfoCompanyController {
	
	@Autowired
	private CompanyService companyService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/info/company/mform.do", method=RequestMethod.GET)
	public ModelAndView infoCompanyMForm(Model model, HttpServletRequest request, HttpServletResponse response,
			Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/company/mform");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		// 업체정보
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		
		mnv.addObject("companyInfo", companyInfo);
		logger.info("companyInfo {}", companyInfo);
		return mnv;
	}
	
	@RequestMapping(value="/info/company/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String floorMod(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Company company) throws ServletException, SftpException, IOException {		
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());

		if(StringUtils.equals(company.getOauthEnabled(), "on")) {
			company.setOauthEnabled("1");
		} else {
			company.setOauthEnabled("0");
		}

		try {
			companyService.modifyCompany(company);
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