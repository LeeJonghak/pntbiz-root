package wms.scanner.controller;

import core.wms.map.domain.FloorCode;
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
import framework.web.util.*;
import core.wms.map.domain.Floor;
import wms.map.service.FloorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jcraft.jsch.SftpException;

import wms.component.auth.LoginDetail;
import core.wms.scanner.domain.Scanner;
import core.wms.scanner.domain.ScannerSearchParam;
import wms.scanner.service.ScannerService;
import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;

@Controller
public class ScannerController {

	@Autowired
	private ScannerService scannerService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CompanyService companyService;

	@Autowired
	private FloorService floorService;

	@RequestMapping(value="/scanner/list.json", method=RequestMethod.GET)
	@ResponseBody
	public String listJson(ScannerSearchParam param, Company company) throws IOException, ParseException{

		Map<String, Object> info = new HashMap<String, Object>();
		try {
			LoginDetail loginDetail = Security.getLoginDetail();
			param.setComNum(loginDetail.getCompanyNumber());
			param.setPageSize(30);

			Integer cnt = scannerService.getScannerCount(param);
			List<?> list = scannerService.getScannerList(param);
			Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
			pagination.queryString = param.getQueryString();
			String page = pagination.print();

			info.put("result", "1");
			info.put("cnt", cnt);
			info.put("list", list);
			info.put("pagination", page);
			info.put("page", param.getPage());
			info.put("param", param);
		}
		catch (Exception e) {
			info.put("result", "2");
			info.put("message", e.getMessage());
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/scanner/list.do", method=RequestMethod.GET)
	public ModelAndView list(ScannerSearchParam param, Company company) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/scanner/list");

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		param.setPageSize(30);

		Integer cnt = scannerService.getScannerCount(param);
		List<?> list = scannerService.getScannerList(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("pagination", page);
		mnv.addObject("page", page);
		mnv.addObject("param", param);

		return mnv;
	}

	@RequestMapping(value="/scanner/checkDup.do", method=RequestMethod.POST)
	@ResponseBody
	public String dup(Scanner scanner) throws ServletException, IOException, ParseException, SftpException {
		// 세션정보
		LoginDetail loginDetail = Security.getLoginDetail();
		scanner.setComNum(loginDetail.getCompanyNumber());

		boolean res = scannerService.checkScannerDuplication(scanner);
		Map<String, String> info = new HashMap<String, String>();
		if(res == true) {
			info.put("result", "1"); // 사용중
		} else {
			info.put("result", "2"); // 사용가능
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/scanner/form.do", method=RequestMethod.GET)
	public ModelAndView form(Company company, Floor floor) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/scanner/form");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		floor.setComNum(loginDetail.getCompanyNumber());
		List<?> floorList = floorService.getFloorGroup(floor);

		List<?> floorCodeList = floorService.getFloorCodeList(company); // floor ?? hs -add(17.09.20)

		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("floorList", floorList);
		mnv.addObject("floorCodeList", floorCodeList); // hs -add(17.09.20)
		return mnv;
	}


	/**
	 * 스캐너 정보
	 * create nohsoo 2015-07-06
	 */
	@RequestMapping(value="/scanner/get.json", method=RequestMethod.GET)
	@ResponseBody
	public String getJson(Scanner scanner) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();
		try {
			Scanner scannerInfo = scannerService.getScannerInfoByNum(scanner);

			info.put("result", "1");
			info.put("data", scannerInfo);
		} catch(Exception e) {
			info.put("result", "2");
			logger.error("exception", e);
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		return jsonUtil.toJson();
	}

	@RequestMapping(value="/scanner/mform.do", method=RequestMethod.GET)
	public ModelAndView mform(Scanner scanner, Company company, Floor floor) {
		ModelAndView mnv = new ModelAndView();

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		/*Integer scannerNum = Integer.parseInt(StringUtil.NVL(request.getParameter("scannerNum"), ""));
		scanner.setScannerNum(scannerNum);*/
		Scanner scannerInfo = scannerService.getScannerInfoByNum(scanner);

		floor.setComNum(loginDetail.getCompanyNumber());
		List<?> floorList = floorService.getFloorGroup(floor);

		List<?> floorCodeList = floorService.getFloorCodeList(company); // hs-add(17.09.20)

		if(scannerInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			mnv.setViewName("/scanner/scanner/mform");
			mnv.addObject("scannerInfo", scannerInfo);
			mnv.addObject("floorList", floorList);
			mnv.addObject("floorCodeList", floorCodeList); // hs-add(17.09.20)
			mnv.addObject("companyInfo", companyInfo);
			logger.info("scannerInfo {}", scannerInfo);
		} else {
			mnv.setViewName("priv");
		}
		return mnv;

	}

	@RequestMapping(value="/scanner/bform.do", method=RequestMethod.GET)
	public ModelAndView bform() {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/scanner/bform");
		return mnv;
	}

	@RequestMapping(value="/scanner/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String reg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Scanner scanner) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();

		scanner.setComNum(loginDetail.getCompanyNumber());
		scanner.setMacAddr(StringUtil.NVL(request.getParameter("macAddr"), ""));
		scanner.setMajorVer(StringUtil.NVL(request.getParameter("majorVer"), ""));
		scanner.setScannerName(StringUtil.NVL(request.getParameter("scannerName"), ""));
		scanner.setLat(Double.parseDouble(StringUtil.NVL(request.getParameter("lat"), "0")));
		scanner.setLng(Double.parseDouble(StringUtil.NVL(request.getParameter("lng"), "0")));
		scanner.setRssi(Float.parseFloat(StringUtil.NVL(request.getParameter("rssi"), "0")));
		scanner.setSrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("srssi"), "15")));
		scanner.setMrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("mrssi"), "-100")));
		scanner.setDrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("drssi"), "100")));
		scanner.setExMeter(Float.parseFloat(StringUtil.NVL(request.getParameter("exMeter"), "15")));
		scanner.setCalPoint(Float.parseFloat(StringUtil.NVL(request.getParameter("calPoint"), "4")));
		scanner.setMaxSig(Float.parseFloat(StringUtil.NVL(request.getParameter("maxSig"), "30")));
		scanner.setMaxBuf(Float.parseFloat(StringUtil.NVL(request.getParameter("maxBuf"), "20")));
		scanner.setFwVer(StringUtil.NVL(request.getParameter("fwVer"), "1.3"));

		try {
			scannerService.registerScanner(scanner);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/scanner/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String mod(HttpServletRequest request, Scanner scanner) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();

		Integer scannerNum = Integer.parseInt(StringUtil.NVL(request.getParameter("scannerNum"), "0"));
		scanner.setScannerNum(scannerNum);
		scanner.setComNum(loginDetail.getCompanyNumber());
		Scanner scannerInfo = scannerService.getScannerInfoByNum(scanner);

        /**
         * 2015-06-19 nohsoo 각 파라미터로 전달 받았을 경우만 업데이트 되도록 수정
         */
        if(request.getParameterMap().containsKey("macAddr")) {
            scanner.setMacAddr(StringUtil.NVL(request.getParameter("macAddr"), ""));
        }
        if(request.getParameterMap().containsKey("majorVer")) {
            scanner.setMajorVer(StringUtil.NVL(request.getParameter("majorVer"), ""));
        }
        if(request.getParameterMap().containsKey("scannerName")) {
            scanner.setScannerName(StringUtil.NVL(request.getParameter("scannerName"), ""));
        }
        if(request.getParameterMap().containsKey("lat")) {
            scanner.setLat(Double.parseDouble(StringUtil.NVL(request.getParameter("lat"), "0")));
        }
        if(request.getParameterMap().containsKey("lng")) {
            scanner.setLng(Double.parseDouble(StringUtil.NVL(request.getParameter("lng"), "0")));
        }
        if(request.getParameterMap().containsKey("rssi")) {
            scanner.setRssi(Float.parseFloat(StringUtil.NVL(request.getParameter("rssi"), "0")));
        }
        if(request.getParameterMap().containsKey("srssi")) {
            scanner.setSrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("srssi"), "15")));
        }
        if(request.getParameterMap().containsKey("mrssi")) {
            scanner.setMrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("mrssi"), "-100")));
        }
        if(request.getParameterMap().containsKey("drssi")) {
            scanner.setDrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("drssi"), "100")));
        }
        if(request.getParameterMap().containsKey("exMeter")) {
            scanner.setExMeter(Float.parseFloat(StringUtil.NVL(request.getParameter("exMeter"), "15")));
        }
        if(request.getParameterMap().containsKey("calPoint")) {
            scanner.setCalPoint(Float.parseFloat(StringUtil.NVL(request.getParameter("calPoint"), "4")));
        }
        if(request.getParameterMap().containsKey("maxSig")) {
            scanner.setMaxSig(Float.parseFloat(StringUtil.NVL(request.getParameter("maxSig"), "30")));
        }
        if(request.getParameterMap().containsKey("maxBuf")) {
            scanner.setMaxBuf(Float.parseFloat(StringUtil.NVL(request.getParameter("maxBuf"), "20")));
        }
        if(request.getParameterMap().containsKey("fwVer")) {
            scanner.setFwVer(StringUtil.NVL(request.getParameter("fwVer"), "1.3"));
        }
        /**
         * 2015-06-19 nohsoo 스캐너 배치도에서 스캐너 정보중 sid, drssi 변경기능을 포함시키기 위해서 추가
         */
        if(request.getParameterMap().containsKey("sid")) {
            scanner.setSid(StringUtil.NVL(request.getParameter("sid"), ""));
        }

		if(scannerInfo.getComNum() == loginDetail.getCompanyNumber()) {
			try {
				scannerService.modifyScanner(scanner);
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

	@RequestMapping(value="/scanner/batch.do", method=RequestMethod.POST)
	@ResponseBody
	public String batch(HttpServletRequest request, Scanner scanner) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		scanner.setComNum(loginDetail.getCompanyNumber());
//		scanner.setRssi(Float.parseFloat(StringUtil.NVL(request.getParameter("rssi"), "0")));
//		scanner.setSrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("srssi"), "15")));
//		scanner.setMrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("mrssi"), "-100")));
//		scanner.setDrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("drssi"), "100")));
//		scanner.setExMeter(Float.parseFloat(StringUtil.NVL(request.getParameter("exMeter"), "15")));
//		scanner.setCalPoint(Float.parseFloat(StringUtil.NVL(request.getParameter("calPoint"), "4")));
//		scanner.setMaxSig(Float.parseFloat(StringUtil.NVL(request.getParameter("maxSig"), "30")));
//		scanner.setMaxBuf(Float.parseFloat(StringUtil.NVL(request.getParameter("maxBuf"), "20")));
//		scanner.setFwVer(StringUtil.NVL(request.getParameter("fwVer"), "1.3"));
        if(request.getParameterMap().containsKey("rssi") && !"".equals(StringUtil.NVL(request.getParameter("rssi")))) {
            scanner.setRssi(Float.parseFloat(StringUtil.NVL(request.getParameter("rssi"), "0")));
        }
        if(request.getParameterMap().containsKey("srssi") && !"".equals(StringUtil.NVL(request.getParameter("srssi")))) {
            scanner.setSrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("srssi"), "15")));
        }
        if(request.getParameterMap().containsKey("mrssi") && !"".equals(StringUtil.NVL(request.getParameter("mrssi")))) {
            scanner.setMrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("mrssi"), "-100")));
        }
        if(request.getParameterMap().containsKey("drssi") && !"".equals(StringUtil.NVL(request.getParameter("drssi")))) {
            scanner.setDrssi(Float.parseFloat(StringUtil.NVL(request.getParameter("drssi"), "100")));
        }
        if(request.getParameterMap().containsKey("exMeter") && !"".equals(StringUtil.NVL(request.getParameter("exMeter")))) {
            scanner.setExMeter(Float.parseFloat(StringUtil.NVL(request.getParameter("exMeter"), "15")));
        }
        if(request.getParameterMap().containsKey("calPoint") && !"".equals(StringUtil.NVL(request.getParameter("calPoint")))) {
            scanner.setCalPoint(Float.parseFloat(StringUtil.NVL(request.getParameter("calPoint"), "4")));
        }
        if(request.getParameterMap().containsKey("maxSig") && !"".equals(StringUtil.NVL(request.getParameter("maxSig")))) {
            scanner.setMaxSig(Float.parseFloat(StringUtil.NVL(request.getParameter("maxSig"), "30")));
        }
        if(request.getParameterMap().containsKey("maxBuf") && !"".equals(StringUtil.NVL(request.getParameter("maxBuf")))) {
            scanner.setMaxBuf(Float.parseFloat(StringUtil.NVL(request.getParameter("maxBuf"), "20")));
        }
        if(request.getParameterMap().containsKey("fwVer") && !"".equals(StringUtil.NVL(request.getParameter("fwVer")))) {
            scanner.setFwVer(StringUtil.NVL(request.getParameter("fwVer"), "1.3"));
        }

		if(scanner.getComNum() != null) {
			try {
				scannerService.batchScanner(scanner);
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

	@RequestMapping(value="/scanner/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String del(Scanner scanner) throws ServletException, IOException, ParseException, SftpException {
		Map<String, String> info = new HashMap<String, String>();

		LoginDetail loginDetail = Security.getLoginDetail();

		/*String scannerNum = StringUtil.NVL(request.getParameter("scannerNum"), "");
		scanner.setScannerNum(Integer.parseInt(scannerNum));*/
		Scanner scannerInfo = scannerService.getScannerInfoByNum(scanner);

		if(scannerInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			try {
				scannerService.removeScanner(scanner);
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