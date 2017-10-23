package wms.scanner.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.wms.scanner.domain.ScannerDeploy;
import core.wms.scanner.domain.ScannerServer;
import wms.scanner.service.ScannerDeployService;
import wms.scanner.service.ScannerServerService;
import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;
import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;

import com.jcraft.jsch.SftpException;

import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;

@Controller
public class ScannerDeployController {
	
	@Value("#{config['scanner.ftp.host']}")
	private String scannerHost;
	@Value("#{config['scanner.ftp.port']}")
	private String scannerPort;
	@Value("#{config['scanner.ftp.id']}")
	private String scannerID;
	@Value("#{config['scanner.ftp.passwd']}")
	private String scannerPW;
	@Value("#{config['scanner.ftp.privatekey']}")
	private String scannerPrivateKey;  
	@Value("#{config['scanner.nodejs.position.path']}")
	private String scannerNodejsPosPath;
	@Value("#{config['scanner.nodejs.node.path']}")
	private String scannerNodejsNodePath;

	@Value("#{config['scanner.nodejs.edge.path']}")
	private String scannerNodejsEdgePath;
	@Value("#{config['scanner.presence.position.path']}")
	private String scannerPresencePosPath;
	@Value("#{config['scanner.presence.node.path']}")
	private String scannerPresenceNodePath;
	@Value("#{config['scanner.presence.bposition.path']}")
	private String scannerPresenceBPosPath;
	@Value("#{config['scanner.presence.bnode.path']}")
	private String scannerPresenceBNodePath;
	@Value("#{config['scanner.presence.materials.path']}")
	private String scannerPresenceMaterialsPath;	
	@Value("#{config['scanner.presence.contents.path']}")
	private String scannerPresenceContentsPath;	
	@Value("#{config['scanner.presence.geofence.path']}")
	private String scannerPresenceGeofencePath;
	@Value("#{config['scanner.presence.gate.path']}")
	private String scannerPresenceGatePath;	
	@Value("#{config['scanner.presence.floor.path']}")
	private String scannerPresenceFloorPath;	
	
	@Autowired
	private ScannerDeployService scannerDeployService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ScannerServerService scannerServerService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CompanyService companyService;
	
	@RequestMapping(value="/scanner/deploy/info.do", method=RequestMethod.GET)
	public ModelAndView deployInfo(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Code code) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/deploy/info");	
		
		code.setgCD("DEPTYPE");
		List<?> depCD = codeService.getCodeListByCD(code);
		
		mnv.addObject("depCD", depCD);
		
		return mnv;
	}

	@RequestMapping(value="/scanner/deploy/list.do", method=RequestMethod.GET)
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerDeploy scannerDeploy, Company company) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/deploy/list");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		scannerDeploy.setComNum(loginDetail.getCompanyNumber());
		
		List<?> list = scannerDeployService.getScannerDeployList(scannerDeploy);
		
		mnv.addObject("list", list);
		
		return mnv;
	}
	
	@RequestMapping(value="/scanner/deploy/form.do", method=RequestMethod.GET)
	public ModelAndView form(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer, ScannerDeploy scannerDeploy, Company company, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/deploy/form");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		
		scannerServer.setComNum(companyInfo.getComNum());
		List<?> slist = scannerServerService.getScannerServerList(scannerServer);
		
		code.setgCD("DEPTYPE");
		List<?> depCD = codeService.getCodeListByCD(code);
		
		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("slist", slist);
		mnv.addObject("depCD", depCD);
		return mnv;
	}

	@RequestMapping(value="/scanner/deploy/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerServer scannerServer, ScannerServer scannerServerChk, ScannerDeploy scannerDeploy, Company company, Code code) {
		ModelAndView mnv = new ModelAndView();	
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		
		Integer depNum = Integer.parseInt(StringUtil.NVL(request.getParameter("depNum"), ""));
		scannerDeploy.setServNum(depNum);
		ScannerDeploy scannerDeployInfo = scannerDeployService.getScannerDeployInfoByNum(scannerDeploy);		
		scannerServerChk.setServNum(scannerDeployInfo.getServNum());
		ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServerChk);
		
		scannerServer.setComNum(companyInfo.getComNum());
		List<?> slist = scannerServerService.getScannerServerList(scannerServer);
		
		code.setgCD("DEPTYPE");
		List<?> depCD = codeService.getCodeListByCD(code);
		
		if(scannerServerInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			mnv.setViewName("/scanner/deploy/mform");
			mnv.addObject("scannerDeployInfo", scannerDeployInfo);
			mnv.addObject("companyInfo", companyInfo);
			mnv.addObject("slist", slist);
			mnv.addObject("depCD", depCD);
			logger.info("scannerDeployInfo {}", scannerDeployInfo);	
		} else {
			mnv.setViewName("priv");
		}
		return mnv;
		
	}
	
	@RequestMapping(value="/scanner/deploy/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerDeploy scannerDeploy, Company company) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		
		scannerDeploy.setServNum(Integer.parseInt(StringUtil.NVL(request.getParameter("servNum"), "0")));	
		scannerDeploy.setDepName(StringUtil.NVL(request.getParameter("depName"), ""));
		scannerDeploy.setDepPath(StringUtil.NVL(request.getParameter("depPath"), ""));
		scannerDeploy.setDepFile(StringUtil.NVL(request.getParameter("depFile"), ""));
//		scannerDeploy.setDepContents(StringUtil.NVL(request.getParameter("depContents"), ""));
		
		try {
			scannerDeployService.registerScannerDeploy(scannerDeploy);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {	
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();	
		return json;
	}
	
	@RequestMapping(value="/scanner/deploy/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerDeploy scannerDeploy, ScannerServer scannerServer) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		Integer depNum = Integer.parseInt(StringUtil.NVL(request.getParameter("depNum"), "0"));
		scannerDeploy.setServNum(depNum);
		scannerDeploy.setComNum(loginDetail.getCompanyNumber());
		ScannerDeploy scannerDeployInfo = scannerDeployService.getScannerDeployInfoByNum(scannerDeploy);
		
		scannerServer.setServNum(scannerDeployInfo.getServNum());
		ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);
		
		if(request.getParameterMap().containsKey("servNum")) {
			scannerDeploy.setServNum(Integer.parseInt(StringUtil.NVL(request.getParameter("servNum"), "0")));
        }
		if(request.getParameterMap().containsKey("depType")) {
        	scannerDeploy.setDepName(StringUtil.NVL(request.getParameter("depType"), ""));
        }
        if(request.getParameterMap().containsKey("depName")) {
        	scannerDeploy.setDepName(StringUtil.NVL(request.getParameter("depName"), ""));
        }
        if(request.getParameterMap().containsKey("depPath")) {
        	scannerDeploy.setDepPath(StringUtil.NVL(request.getParameter("depPath"), ""));
        }
        if(request.getParameterMap().containsKey("depFile")) {
        	scannerDeploy.setDepFile(StringUtil.NVL(request.getParameter("depFile"), ""));
        }		
		if(scannerServerInfo.getComNum() == loginDetail.getCompanyNumber()) {
			try {				
				scannerDeployService.modifyScannerDeploy(scannerDeploy);
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
	
	@RequestMapping(value="/scanner/deploy/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerDeploy scannerDeploy, ScannerServer scannerServer) throws ServletException, IOException, ParseException, SftpException {		
		Map<String, String> info = new HashMap<String, String>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		
		String depNum = StringUtil.NVL(request.getParameter("depNum"), "");
		scannerDeploy.setServNum(Integer.parseInt(depNum));
		ScannerDeploy scannerDeployInfo = scannerDeployService.getScannerDeployInfoByNum(scannerDeploy);
		
		scannerServer.setServNum(scannerDeployInfo.getServNum());
		ScannerServer scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);
		
		if(scannerServerInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			try {
				scannerDeployService.removeScannerDeploy(scannerDeploy);
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
	
	@RequestMapping(value="/scanner/deploy/dep.do", method=RequestMethod.POST)
	@ResponseBody
	public String dep(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerDeploy scannerDeploy, ScannerServer scannerServer, Company company) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());		
		Company companyInfo = companyService.getCompanyInfo(company);
		
		Integer depNum = Integer.parseInt(StringUtil.NVL(request.getParameter("depNum"), "0"));
		String depType = StringUtil.NVL(request.getParameter("depType"), "");
		String depFormat = StringUtil.NVL(request.getParameter("depFormat"), "json");
		
		ScannerDeploy scannerDeployInfo = scannerDeploy;
		ScannerServer scannerServerInfo = scannerServer;
		
		String json = "";
		
		// master 배포
		if(depNum == 0 && !("").equals(depType)) {
			List<String> depPath = new ArrayList<String>();
			String depFile = "";
			StringBuffer data = null;
			
			if(depType.equals("POS")) {
				depPath.add(scannerNodejsPosPath);
				depPath.add(scannerPresencePosPath);
				depFile = "scannerPos." + depFormat;
				data = scannerDeployService.getScannerPosList(companyInfo, depFormat);
			}
			if(depType.equals("NODE")) {
				depPath.add(scannerNodejsNodePath);
				depPath.add(scannerPresenceNodePath);
				depFile = "scannerNode." + depFormat;
				data = scannerDeployService.getScannerNodeList(companyInfo, depFormat);
			}
			if(depType.equals("BPOS")) {
				depPath.add(scannerPresenceBPosPath);
				depFile = "beaconPos." + depFormat;
				data = scannerDeployService.getScannerBeaconPosList(companyInfo, depFormat);
			}
			if(depType.equals("BNODE")) {
				depPath.add(scannerPresenceBNodePath);
				depFile = "beaconNode." + depFormat;
				data = scannerDeployService.getScannerBeaconNodeList(companyInfo, depFormat);
			}
			if(depType.equals("GF")) {
				depPath.add(scannerPresenceGeofencePath);
				depFile = "geofenceList." + depFormat;
				data = scannerDeployService.getScannerGeofenceList(companyInfo, depFormat);
			}
			if(depType.equals("MAT")) {
				depPath.add(scannerPresenceMaterialsPath);
				depFile = "materialsList." + depFormat;
				data = scannerDeployService.getScannerMaterialsList(companyInfo, depFormat);
			}
			if(depType.equals("CON")) {
				depPath.add(scannerPresenceContentsPath);
				depFile = "contentsList." + depFormat;
				data = scannerDeployService.getScannerContentsList(companyInfo, depFormat);
			}
			if(depType.equals("GATE")) {
				depPath.add(scannerPresenceGatePath);
				depFile = "gateList." + depFormat;
				data = scannerDeployService.getScannerGateList(companyInfo, depFormat);
			}		
			if(depType.equals("FLOOR")) {
				depPath.add(scannerPresenceFloorPath);
				depFile = "floorList." + depFormat;
				data = scannerDeployService.getScannerFloorList(companyInfo, depFormat);
			}	
			String[] host = scannerHost.split(",");
			String[] port = scannerPort.split(",");
			String[] id = scannerID.split(",");
			String[] pw = scannerPW.split(",");
			String[] key = scannerPrivateKey.split(",");
			for(int i=0; i<host.length; i ++) {
				try {
					for(int j=0; j<depPath.size(); j++) {
						scannerDeployInfo.setDepType(depType);
						scannerDeployInfo.setDepPath(depPath.get(j));
						scannerDeployInfo.setDepFile(depFile);
						scannerDeployInfo.setDepJson(data);						
						scannerServerInfo.setFtpHost(host[i]);
						scannerServerInfo.setFtpPort(Integer.parseInt(port[i]));
						scannerServerInfo.setFtpID(id[i]);
						scannerServerInfo.setFtpPW(pw[i]);
						try {
							scannerServerInfo.setFtpPrivateKey(key[i]);
						} catch(Exception e) {}
						scannerServerInfo.setComNum(companyInfo.getComNum());
						scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					}
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");		
				}
			}
			
		// 개별서버 배포
		} else {
			scannerDeploy.setDepNum(depNum);
			scannerDeploy.setComNum(loginDetail.getCompanyNumber());
			scannerDeployInfo = scannerDeployService.getScannerDeployInfoByNum(scannerDeploy);
			
			scannerServer.setServNum(scannerDeployInfo.getServNum());
			scannerServer.setComNum(loginDetail.getCompanyNumber());
			scannerServerInfo = scannerServerService.getScannerServerInfoByNum(scannerServer);
			
			if(scannerDeployInfo.getDepType().equals("POS")) {
				StringBuffer data = scannerDeployService.getScannerPosList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}						
			}
			if(scannerDeployInfo.getDepType().equals("NODE")) {
				StringBuffer data = scannerDeployService.getScannerNodeList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}			
			}
			if(scannerDeployInfo.getDepType().equals("NODE2")) {
				StringBuffer data = scannerDeployService.getScannerNodeList2(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");
				}
			}
			if(scannerDeployInfo.getDepType().equals("BPOS")) {
				StringBuffer data = scannerDeployService.getScannerBeaconPosList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}						
			}
			if(scannerDeployInfo.getDepType().equals("BNODE")) {
				StringBuffer data = scannerDeployService.getScannerBeaconNodeList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}			
			}			
			if(scannerDeployInfo.getDepType().equals("GF")) {
				StringBuffer data = scannerDeployService.getScannerGeofenceList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}				
			}
			if(scannerDeployInfo.getDepType().equals("MAT")) {
				StringBuffer data = scannerDeployService.getScannerMaterialsList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}			
			}
			if(scannerDeployInfo.getDepType().equals("CON")) {
				StringBuffer data = scannerDeployService.getScannerContentsList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}			
			}
			if(scannerDeployInfo.getDepType().equals("GATE")) {
				StringBuffer data = scannerDeployService.getScannerGateList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}				
			}
			if(scannerDeployInfo.getDepType().equals("FLOOR")) {
				StringBuffer data = scannerDeployService.getScannerFloorList(companyInfo, depFormat);
				scannerDeployInfo.setDepJson(data);
				scannerDeploy.setDepContents(data.toString());
				try {
					scannerDeployService.deployFile(scannerServerInfo, scannerDeployInfo);
					scannerDeployService.modifyScannerDeploy(scannerDeploy);
					info.put("result", "1");
				} catch(Exception e) {
					logger.error("exception", e);
					info.put("result", "2");			
				}				
			}
		}		
		JsonUtil jsonUtil = new JsonUtil(info);		
		json = jsonUtil.toJson();		
		return json;
	}
	
}
