package wms.info.controller;

import framework.Security;
import framework.web.util.*;
import core.wms.info.domain.LocationInquirySearchParam;
import core.wms.info.domain.LocationRecord;
import core.wms.info.domain.LocationRecordSearchParam;
import wms.info.service.LocationInquiryService;

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
import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;

@Controller
public class InfoLocationInquiryController {

	@Autowired
	private LocationInquiryService locationInquiryService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CodeService codeService;

	@RequestMapping(value="/info/location/list.do", method=RequestMethod.GET)
	public ModelAndView locationList(Model model, HttpServletRequest request, HttpServletResponse response,
			LocationInquirySearchParam param, Code code) throws DataAccessException, ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/location/list");

		code.setgCD("MOBILEOS");
		List<?> osCD = codeService.getCodeListByCD(code);
		code.setgCD("LOCSERVICE");
		List<?> serviceCD = codeService.getCodeListByCD(code);

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		sDate = (sDate == null || "".equals(sDate)) ? DateUtil.getDate("yyyy-MM-dd") : sDate;
		eDate = (eDate == null || "".equals(eDate)) ? DateUtil.getDate("yyyy-MM-dd") : eDate;
		param.setsDate(sDate);
		param.seteDate(eDate);

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = locationInquiryService.getLocationInquiryCount(param);
		List<?> list = locationInquiryService.getLocationInquiryList(param);
		list = locationInquiryService.bindLocationInquiryList(list, osCD, serviceCD);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString  = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("osCD", osCD);
		mnv.addObject("serviceCD", serviceCD);
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		mnv.addObject("sDate", sDate);
		mnv.addObject("eDate", eDate);
		logger.info("list {}", list);

		logger.info("code {}", code);

		return mnv;
	}

	@RequestMapping(value="/info/location/record.do", method=RequestMethod.GET)
	public ModelAndView recordList(Model model, HttpServletRequest request, HttpServletResponse response,
			LocationRecordSearchParam param, Code code) throws DataAccessException, ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/location/record");

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		sDate = (sDate == null || "".equals(sDate)) ? DateUtil.getDate("yyyy-MM-dd") : sDate;
		eDate = (eDate == null || "".equals(eDate)) ? DateUtil.getDate("yyyy-MM-dd") : eDate;
		param.setsDate(sDate);
		param.seteDate(eDate);

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = locationInquiryService.getLocationRecordCount(param);
		List<?> list = locationInquiryService.getLocationRecordList(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString  = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		mnv.addObject("sDate", sDate);
		mnv.addObject("eDate", eDate);
		logger.info("list {}", list);

		logger.info("code {}", code);

		return mnv;
	}

	@RequestMapping(value="/info/location/recordForm.do", method=RequestMethod.GET)
	public ModelAndView contentsForm(Model model, HttpServletRequest request, HttpServletResponse response,
			Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/location/recordForm");
		return mnv;
	}

	@RequestMapping(value="/info/location/recordReg.do", method=RequestMethod.POST)
	@ResponseBody
	public String contentsReg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			LocationRecord locationRecord) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		locationRecord.setComNum(loginDetail.getCompanyNumber());
		locationRecord.setRecType("R");
		locationRecord.setRecID(loginDetail.getUsername());
		locationRecord.setReqName(request.getParameter("reqName"));
		locationRecord.setOpt("");
		locationRecord.setUseDesc(request.getParameter("useDesc"));
		locationRecord.setRemoteIP(StringUtil.NVL(request.getParameter("remoteIP"), ""));

		// 등록
		try {
			locationInquiryService.registerLocationRecord(locationRecord);
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