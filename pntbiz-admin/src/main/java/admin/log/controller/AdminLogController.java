package admin.log.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import core.admin.log.domain.AdminLogSearchParam;
import admin.log.service.AdminLogService;
import framework.web.util.Pagination;

@Controller
public class AdminLogController {

	@Autowired
	private AdminLogService adminLogService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/admin/log/list.do", method=RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request, AdminLogSearchParam param) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/log/list");

		param.setPageSize(30);
		Integer cnt = adminLogService.getAdminLogCount(param);
		List<?> list = adminLogService.getAdminLogList(param);

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
}