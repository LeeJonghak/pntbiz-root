package wms.service.controller;

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import core.wms.service.domain.AttendanceSearchParam;
import wms.service.service.ServiceAttendanceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ServiceAttendanceController {

	@Autowired
	private ServiceAttendanceService serviceAttendanceService;

	@Autowired
	private CompanyService companyService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 출석체크 리스트
	@RequestMapping(value="/service/attendance/list.do", method=RequestMethod.GET)
	public ModelAndView list(AttendanceSearchParam param, Company company) throws Exception {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/service/attendance/list");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setUUID(companyInfo.getUUID());
		param.setPageSize(30);
		if("".equals(param.getAttdDate()) || param.getAttdDate() == null) {
			param.setAttdDate(DateUtil.getDate("yyyyMMdd"));
		}

		Integer cnt = serviceAttendanceService.getAttendanceCount(param);
		List<?> list = serviceAttendanceService.getAttendanceList(param);

		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		mnv.addObject("attdDate", param.getAttdDate());
		logger.info("list {}", list);

		return mnv;
	}

	@RequestMapping(value="/service/attendance/reset.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse responsesession) throws IOException {
		Map<String, String> info = new HashMap<String, String>();

		try {
			AttendanceSearchParam param = new AttendanceSearchParam();
			param.setAttdDate(request.getParameter("attdDate"));
			serviceAttendanceService.deleteAttendance(param);

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