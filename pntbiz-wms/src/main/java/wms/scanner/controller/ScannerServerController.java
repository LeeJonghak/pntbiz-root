package wms.scanner.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
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

import core.wms.scanner.domain.ScannerServer;
import wms.scanner.service.ScannerServerService;
import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;

import com.jcraft.jsch.SftpException;

import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;

@Controller
public class ScannerServerController {
	@Autowired
	private ScannerServerService scannerServerService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CompanyService companyService;

	@RequestMapping(value="/scanner/server/list.json", method=RequestMethod.GET)
	@ResponseBody
	public String listJson(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			Company company, ScannerServer scannerServer) throws IOException, ParseException{

		Map<String, Object> info = new HashMap<String, Object>();
		try {
			LoginDetail loginDetail = Security.getLoginDetail();
			scannerServer.setComNum(loginDetail.getCompanyNumber());

			List<?> list = scannerServerService.getScannerServerList(scannerServer);

			info.put("result", "1");
			info.put("list", list);
		}
		catch (Exception e) {
			info.put("result", "2");
			info.put("message", e.getMessage());
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/scanner/server/list.do", method=RequestMethod.GET)
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer, Company company) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/server/list");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		scannerServer.setComNum(loginDetail.getCompanyNumber());

		List<?> list = scannerServerService.getScannerServerList(scannerServer);
		
		mnv.addObject("list", list);		
		
		return mnv;
	}
	
	@RequestMapping(value="/scanner/server/form.do", method=RequestMethod.GET)
	public ModelAndView form(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer, Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/server/form");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);		
		
		mnv.addObject("companyInfo", companyInfo);
		return mnv;
	}

	@RequestMapping(value="/scanner/server/get.json", method=RequestMethod.GET)
	@ResponseBody
	public String getJson(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
							  ScannerServer scannerServer) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();
		try {
			Integer servNum = Integer.parseInt(StringUtil.NVL(request.getParameter("servNum"), ""));
			scannerServer.setServNum(servNum);
			ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);

			info.put("result", "1");
			info.put("data", scannerServerInfo);
		} catch(Exception e) {
			info.put("result", "2");
			logger.error("exception", e);
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		return jsonUtil.toJson();
	}

	@RequestMapping(value="/scanner/server/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer, Company company) {
		ModelAndView mnv = new ModelAndView();	
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		Integer servNum = Integer.parseInt(StringUtil.NVL(request.getParameter("servNum"), ""));
		scannerServer.setServNum(servNum);
		ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);
		
		if(scannerServerInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			mnv.setViewName("/scanner/server/mform");
			mnv.addObject("scannerServerInfo", scannerServerInfo);
			mnv.addObject("companyInfo", companyInfo);
			logger.info("scannerServerInfo {}", scannerServerInfo);	
		} else {
			mnv.setViewName("priv");
		}
		return mnv;
		
	}
	
	@RequestMapping(value="/scanner/server/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		scannerServer.setComNum(loginDetail.getCompanyNumber());	
		scannerServer.setFtpHost(StringUtil.NVL(request.getParameter("ftpHost"), ""));
		scannerServer.setFtpPort(Integer.parseInt(StringUtil.NVL(request.getParameter("ftpPort"), "22")));
		scannerServer.setFtpID(StringUtil.NVL(request.getParameter("ftpID"), ""));
		scannerServer.setFtpPW(StringUtil.NVL(request.getParameter("ftpPW"), ""));
		
		try {
			scannerServerService.registerScannerServer(scannerServer);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	@RequestMapping(value="/scanner/server/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		Integer servNum = Integer.parseInt(StringUtil.NVL(request.getParameter("servNum"), "0"));
		scannerServer.setServNum(servNum);
		scannerServer.setComNum(loginDetail.getCompanyNumber());
		ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);

        if(request.getParameterMap().containsKey("ftpHost")) {
        	scannerServer.setFtpHost(StringUtil.NVL(request.getParameter("ftpHost"), ""));
        }
        if(request.getParameterMap().containsKey("ftpPort")) {
        	scannerServer.setFtpPort(Integer.parseInt(StringUtil.NVL(request.getParameter("ftpPort"), "22")));
        }
        if(request.getParameterMap().containsKey("ftpID")) {
        	scannerServer.setFtpID(StringUtil.NVL(request.getParameter("ftpID"), ""));
        }
        if(request.getParameterMap().containsKey("ftpPW")) {
        	scannerServer.setFtpPW(StringUtil.NVL(request.getParameter("ftpPW"), ""));
        }
		
		if(scannerServerInfo.getComNum() == loginDetail.getCompanyNumber()) {
			try {				
				scannerServerService.modifyScannerServer(scannerServer);
				info.put("result", "1");
			} catch(DataAccessException dae) {
				info.put("result", "2");
			} finally {
			}
		} else {
			info.put("result", "3");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	@RequestMapping(value="/scanner/server/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer) throws ServletException, IOException, ParseException, SftpException {		
		Map<String, String> info = new HashMap<String, String>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		String servNum = StringUtil.NVL(request.getParameter("servNum"), "");
		scannerServer.setServNum(Integer.parseInt(servNum));
		ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);
		
		if(scannerServerInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			try {
				scannerServerService.removeScannerServer(scannerServer);
				info.put("result", "1");
			} catch(DataAccessException dae) {
				info.put("result", "2");
			} finally {				
			}
		} else {
			info.put("result", "3");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}
}
