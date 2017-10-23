package wms.info.controller;

import framework.Security;
import framework.web.util.*;
import core.wms.info.domain.CodeAction;
import core.wms.info.domain.CodeActionSearchParam;
import wms.info.service.CodeActionService;

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

import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;

import com.jcraft.jsch.SftpException;

import wms.component.auth.LoginDetail;

@Controller
public class InfoCodeController {		
	
	@Autowired
	private CodeActionService codeActionService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeService codeService;

	@RequestMapping(value="/info/code/list.do", method=RequestMethod.GET)
	public ModelAndView infoCodeList(Model model, HttpServletRequest request, HttpServletResponse response,
			CodeActionSearchParam param, Code code) throws DataAccessException, ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/code/list");
		
		code.setgCD("CODEACTTYPE");
		List<?> codeActCD = codeService.getCodeListByCD(code);

		mnv.addObject("codeActCD", codeActCD);
		mnv.addObject("param", param);

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = codeActionService.getCodeActionCount(param);
		List<?> list = codeActionService.getCodeActionList(param);
		list = codeActionService.bindCodeActionList(list);
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
	
	@RequestMapping(value="/info/code/form.do", method=RequestMethod.GET)
	public ModelAndView infoCodeForm(Model model, HttpServletRequest request, HttpServletResponse response,
			Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/code/form");
		
		code.setgCD("CODEACTTYPE");
		List<?> codeActCD = codeService.getCodeListByCD(code);
		
		mnv.addObject("codeActCD", codeActCD);
		
		return mnv;
	}
	
	@RequestMapping(value="/info/code/mform.do", method=RequestMethod.GET)
	public ModelAndView infoCodeMForm(Model model, HttpServletRequest request, HttpServletResponse response,
			CodeAction codeAction, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/info/code/mform");
		
		code.setgCD("CODEACTTYPE");
		List<?> codeActCD = codeService.getCodeListByCD(code);
		
		Integer codeNum = Integer.parseInt(StringUtil.NVL(request.getParameter("codeNum"), "0"));
		
		LoginDetail loginDetail = Security.getLoginDetail();
		codeAction.setComNum(loginDetail.getCompanyNumber());
		codeAction.setCodeNum(codeNum);
		
		// 콘텐츠 정보
		CodeAction codeActionInfo = codeActionService.getCodeActionInfo(codeAction);
//		codeActionInfo = codeActionService.bindContents(codeActionInfo);
		logger.info("codeActionInfo : {}", codeActionInfo);
		
		mnv.addObject("codeActCD", codeActCD);
		mnv.addObject("codeActionInfo", codeActionInfo);
		
		return mnv;
	}
	
	// 코드 등록
	@RequestMapping(value="/info/code/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAcReg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			CodeAction codeAction) throws ServletException, SftpException, IOException {	
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		codeAction.setComNum(loginDetail.getCompanyNumber());

		try {
			codeActionService.registerCodeAction(request, codeAction);	
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	// 코드 수정
	@RequestMapping(value="/info/code/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAcMod(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			CodeAction codeAction) throws ServletException, SftpException, IOException {	
		Map<String, Object> info = new HashMap<String, Object>();		
		
		Integer codeNum = Integer.parseInt(StringUtil.NVL(request.getParameter("codeNum"), "0"));
		
		LoginDetail loginDetail = Security.getLoginDetail();
		codeAction.setComNum(loginDetail.getCompanyNumber());
		codeAction.setCodeNum(codeNum);
		
		try {
			codeActionService.modifyCodeAction(request, codeAction);	
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	// 코드 삭제
	@RequestMapping(value="/info/code/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String infoAcDel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			CodeAction codeAction) throws ServletException, SftpException, IOException {	
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
				
		String codeNum = StringUtil.NVL(request.getParameter("codeNum"), "0");
		codeAction.setComNum(loginDetail.getCompanyNumber());		
		codeAction.setCodeNum(Integer.parseInt(codeNum));		
		
		// 코드 정보
//		CodeAction codeActionInfo = codeActionService.getCodeActionInfo(codeAction);
		
		try {
			codeActionService.removeCodeAction(codeAction);	
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