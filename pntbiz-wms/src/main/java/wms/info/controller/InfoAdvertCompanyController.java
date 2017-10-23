package wms.info.controller;

import framework.Security;
import framework.web.util.*;
import core.wms.info.domain.AdvertCompany;
import core.wms.info.domain.AdvertCompanySearchParam;
import wms.info.service.AdvertCompanyService;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
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

import com.jcraft.jsch.SftpException;

import wms.component.auth.LoginDetail;

@Controller
public class InfoAdvertCompanyController {		
	
	@Autowired
	private AdvertCompanyService advertCompanyService;
	private Logger logger = LoggerFactory.getLogger(getClass());


	@RequestMapping(value="/info/ac/list.do", method=RequestMethod.GET)
	public ModelAndView infoAcList(Model model, HttpServletRequest request, HttpServletResponse response,
			AdvertCompanySearchParam param) throws DataAccessException, ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/ac/list");

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = advertCompanyService.getAdvertCompanyCount(param);
		List<?> list = advertCompanyService.getAdvertCompanyList(param);
		list = advertCompanyService.bindAdertCompanyList(list);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString  = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("result", "1");
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("pagination", page);
		mnv.addObject("page", param.getPage());
		mnv.addObject("param", param);
		
		return mnv;
	}
	
	@RequestMapping(value="/info/ac/form.do", method=RequestMethod.GET)
	public ModelAndView infoAcForm(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/ac/form");
		return mnv;
	}
	
	@RequestMapping(value="/info/ac/mform.do", method=RequestMethod.GET)
	public ModelAndView infoAcMForm(Model model, HttpServletRequest request, HttpServletResponse response,
			AdvertCompany advertCompany) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/ac/mform");
		
		Integer acNum = Integer.parseInt(StringUtil.NVL(request.getParameter("acNum"), "0"));
		
		LoginDetail loginDetail = Security.getLoginDetail();
		advertCompany.setComNum(loginDetail.getCompanyNumber());
		advertCompany.setAcNum(acNum);
		
		// 콘텐츠 정보
		AdvertCompany advertCompanyInfo = advertCompanyService.getAdvertCompanyInfo(advertCompany);
//		advertCompanyInfo = advertCompanyService.bindContents(advertCompanyInfo);
		
		mnv.addObject("advertCompanyInfo", advertCompanyInfo);
		
		return mnv;
	}
	
	// 광고업체 등록
	@RequestMapping(value="/info/ac/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAcReg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			AdvertCompany advertCompany) throws ServletException, SftpException, IOException {	
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		advertCompany.setComNum(loginDetail.getCompanyNumber());

		try {
			advertCompanyService.registerAdvertCompany(request, advertCompany);	
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	// 광고업체 수정
	@RequestMapping(value="/info/ac/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAcMod(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			AdvertCompany advertCompany) throws ServletException, SftpException, IOException {	
		Map<String, Object> info = new HashMap<String, Object>();		
		
		Integer acNum = Integer.parseInt(StringUtil.NVL(request.getParameter("acNum"), "0"));
		
		LoginDetail loginDetail = Security.getLoginDetail();
		advertCompany.setComNum(loginDetail.getCompanyNumber());
		advertCompany.setAcNum(acNum);
		
		try {
			advertCompanyService.modifyAdvertCompany(request, advertCompany);	
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	// 광고업체 삭제
	@RequestMapping(value="/info/ac/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAcDel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			AdvertCompany advertCompany) throws ServletException, SftpException, IOException {	
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
				
		String acNum = StringUtil.NVL(request.getParameter("acNum"), "0");
		advertCompany.setComNum(loginDetail.getCompanyNumber());		
		advertCompany.setAcNum(Integer.parseInt(acNum));		
		
		// 광고업체 정보
//		AdvertCompany advertCompanyInfo = advertCompanyService.getAdvertCompanyInfo(advertCompany);
		
		try {
			advertCompanyService.removeAdvertCompany(advertCompany);	
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	@RequestMapping(value="/info/ac/listall.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAdListAll(HttpServletRequest request, HttpServletResponse response,
			AdvertCompanySearchParam param) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		try {
			param.setComNum(loginDetail.getCompanyNumber());
			List<?> list = advertCompanyService.getAdvertCompanyListAll(param);
			info.put("result", "1");
			info.put("data", list);
			logger.info("advert list : {}", list);
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	
}