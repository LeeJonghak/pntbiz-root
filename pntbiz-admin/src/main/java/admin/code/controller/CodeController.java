package admin.code.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.Security;
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

import framework.auth.LoginDetail;

import admin.code.service.CodeService;
import core.common.code.domain.Code;
import core.common.code.domain.CodeSearchParam;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.StringUtil;

@Controller
public class CodeController {

	@Autowired
	private CodeService codeService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/admin/code/list.do", method=RequestMethod.GET)
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, CodeSearchParam param) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/code/list");

		int pageSize = 30;
		int blockSize = 10;
		//param.setPageParam(pageSize, blockSize);
		param.initPage(pageSize, blockSize);
		Integer cnt = codeService.getCodeCount(param);
		List<?> list = codeService.getCodeList(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		//pagination.linkPage = "/admin/codeList.do";
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

	@RequestMapping(value="/admin/code/form.do", method=RequestMethod.GET)
	public ModelAndView form(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView mnv = new ModelAndView();

		LoginDetail loginDetail = Security.getLoginDetail();

		mnv.addObject("comNum", loginDetail.getCompanyNumber());

		mnv.setViewName("/admin/code/form");
		return mnv;
	}

	@RequestMapping(value="/admin/code/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/code/mform");
		String gCD = StringUtil.NVL(request.getParameter("gCD"), "");
		String sCD = StringUtil.NVL(request.getParameter("sCD"), "");
		code.setgCD(gCD);
		code.setsCD(sCD);
		Code codeInfo = codeService.getCodeInfo(code);
		mnv.addObject("codeInfo", codeInfo);
		logger.info("codeInfo {}", codeInfo);
		return mnv;
	}

	@RequestMapping(value="/admin/code/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response, HttpSession session, Code code) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		Integer cnt = codeService.getCodeCheck(code);
		if(cnt > 0) {
			info.put("result", "3");
		} else {
			try {
				codeService.registerCode(code);
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

	@RequestMapping(value="/admin/code/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(HttpServletRequest request, HttpServletResponse response, HttpSession session, Code code) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		String pgCD = code.getPgCD();
		String gCD = code.getgCD();
		String psCD = code.getPsCD();
		String sCD = code.getsCD();
		Integer cnt = 0;
		// 기존코드가 변경되었을 경우 코드 체크
		if(!(pgCD.equals(gCD) && psCD.equals(sCD))) {
			cnt = codeService.getCodeCheck(code);
		}
		if(cnt > 0) {
			info.put("result", "3");
		} else {
			try {
				codeService.modifyCode(code);
				info.put("result", "1");
			} catch(DataAccessException Mod) {
				info.put("result", "2");
			} finally {
			}
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/admin/code/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, HttpSession session, Code code) throws IOException {
		Map<String, String> info = new HashMap<String, String>();
		try {
			codeService.removeCode(code);
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