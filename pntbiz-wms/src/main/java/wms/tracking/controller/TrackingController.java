package wms.tracking.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.wms.tracking.domain.*;
import framework.web.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.wms.admin.company.domain.Company;
import core.wms.beacon.domain.Beacon;
import core.wms.map.domain.Floor;
import framework.Security;
import framework.web.util.DateUtil;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import wms.admin.company.service.CompanyService;
import wms.beacon.service.BeaconService;
import wms.component.auth.LoginDetail;
import wms.map.service.FloorService;
import wms.tracking.service.PresenceBeaconService;
import wms.tracking.service.PresenceService;
import wms.tracking.service.PresenceSetmapService;

@Controller
public class TrackingController {

	@Autowired
	private PresenceSetmapService presenceSetmapService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PresenceService presenceService;

	@Autowired
	private PresenceBeaconService presenceBeaconService;

	@Autowired
	private BeaconService beaconService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private FloorService floorService;


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/tracking/main.do", method=RequestMethod.GET)
	public ModelAndView main(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ParseException {
		ModelAndView mnv = new ModelAndView();

		LoginDetail loginDetail = Security.getLoginDetail();
		Integer comNum = loginDetail.getCompanyNumber();
		String userID = loginDetail.getUsername();

		Company company = new Company();
 		company.setComNum(comNum);
 		Company companyInfo = companyService.getCompanyInfo(company);

 		// 층 정보
        Floor floorParam = new Floor();
        floorParam.setComNum(comNum);
        List<?> floorList = floorService.getFloorList(floorParam);
        floorList = floorService.bindFloorList((List<Floor>) floorList);

        // 실시간 위치 측위
 		PresenceSetmap setmap = new PresenceSetmap();
 		setmap.setComNum(comNum);
 		PresenceSetmap setmapInfo = presenceSetmapService.getPresenceSetmapInfo(setmap);

 		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("comNum", comNum);
		mnv.addObject("userID", userID);
		mnv.addObject("floorList", floorList);
		mnv.addObject("setmapInfo", setmapInfo);

		mnv.setViewName("/tracking/main");
		return mnv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/tracking/main2.do", method=RequestMethod.GET)
	public ModelAndView main2() throws ParseException {
		ModelAndView mnv = new ModelAndView();

		LoginDetail loginDetail = Security.getLoginDetail();
		Integer comNum = loginDetail.getCompanyNumber();

		Company company = new Company();
		company.setComNum(comNum);
		Company companyInfo = companyService.getCompanyInfo(company);
		mnv.addObject("companyInfo", companyInfo);

		mnv.setViewName("/tracking/main2");
		return mnv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/tracking/map.embed.do", method=RequestMethod.GET)
	public ModelAndView mainEmbeded(@RequestParam(value = "comNum", required = false)Integer comNum) throws ParseException {
		ModelAndView mnv = new ModelAndView();

		if(comNum==null) {
			comNum = Security.getLoginDetail().getCompanyNumber();
		}

		Company company = new Company();
		company.setComNum(comNum);
		Company companyInfo = companyService.getCompanyInfo(company);
		mnv.addObject("companyInfo", companyInfo);

		mnv.setViewName("/tracking/map.embed");
		return mnv;
	}

	@RequestMapping(value="/tracking/presence/setmap.do", method=RequestMethod.GET)
	public ModelAndView setmap(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Company company, PresenceSetmap presenceSetmap, Floor floor) {
		ModelAndView mnv = new ModelAndView();

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		floor.setComNum(loginDetail.getCompanyNumber());
		List<?> floorList = floorService.getFloorGroup(floor);

		presenceSetmap.setComNum(loginDetail.getCompanyNumber());
		presenceSetmap.setMapType("S");
		PresenceSetmap presenceSetmapInfo = presenceSetmapService.getPresenceSetmapInfo(presenceSetmap);

		if(presenceSetmapInfo == null) {
			presenceSetmapService.registerPresenceSetmap(presenceSetmap);
			presenceSetmapInfo = presenceSetmapService.getPresenceSetmapInfo(presenceSetmap);
		}

		if(presenceSetmapInfo.getComNum().equals(loginDetail.getCompanyNumber())) {
			mnv.setViewName("/tracking/setmap");
			mnv.addObject("companyInfo", companyInfo);
			mnv.addObject("floorList", floorList);
			mnv.addObject("presenceSetmapInfo", presenceSetmapInfo);
			logger.info("presenceSetmapInfo {}", presenceSetmapInfo);
		} else {
			mnv.setViewName("notpriv");
		}
		return mnv;
	}


	@RequestMapping(value="/tracking/presence/setmap.info.do", method=RequestMethod.POST)
	@ResponseBody
	public String setmapInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Company company, PresenceSetmap presenceSetmap) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		presenceSetmap.setComNum(companyInfo.getComNum());
		presenceSetmap.setMapType("S");

		try {
			PresenceSetmap presenceSetmapInfo = presenceSetmapService.getPresenceSetmapInfo(presenceSetmap);
			info.put("result", "1");
			info.put("data", presenceSetmapInfo);
		} catch(DataAccessException dae) {
			dae.printStackTrace();
			info.put("result", "2");
		} finally {
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/tracking/presence/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String modmap(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Company company, PresenceSetmap presenceSetmap) throws IOException {
		Map<String, String> info = new HashMap<String, String>();

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		presenceSetmap.setComNum(companyInfo.getComNum());
		presenceSetmap.setInitZoom(Integer.parseInt(StringUtil.NVL(request.getParameter("initZoom"), "20")));
		presenceSetmap.setInitFloor(StringUtil.NVL(request.getParameter("initFloor"), "1"));
		presenceSetmap.setCheckTimeInterval(Integer.parseInt(StringUtil.NVL(request.getParameter("checkTimeInterval"), "3000")));
		presenceSetmap.setRemoveTimeInterval(Integer.parseInt(StringUtil.NVL(request.getParameter("removeTimeInterval"), "20000")));
		presenceSetmap.setMoveTimeInterval(Integer.parseInt(StringUtil.NVL(request.getParameter("moveTimeInterval"), "3000")));
		presenceSetmap.setMoveUnit(Float.parseFloat(StringUtil.NVL(request.getParameter("moveUnit"), "0.1")));

		try {
			presenceSetmapService.modifyPresenceSetmap(presenceSetmap);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			dae.printStackTrace();
			info.put("result", "2");
		} finally {
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/tracking/presence/scanner/target.do", method=RequestMethod.GET)
	public ModelAndView scannerTarget(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/scanner/target");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		mnv.addObject("companyInfo", companyInfo);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/scanner/target2.do", method=RequestMethod.GET)
	public ModelAndView scannerTarget2(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
									  Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/scanner/target2");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		mnv.addObject("companyInfo", companyInfo);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/scanner/target.beacon.list.do", method=RequestMethod.POST)
	@ResponseBody
	public String scannerTargetBeaconList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			PresenceTargetSearchParam param) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		List<?> list = presenceService.getPresenceTargetListByBeacon(param);

		if(list != null) {
			info.put("result", "1");
			info.put("data", list);
		} else {
			info.put("result", "2");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/tracking/presence/scanner/target.log.list.do", method=RequestMethod.POST)
	@ResponseBody
	public String scannerTargetLogList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			PresenceTargetSearchParam param, Company company) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		Long beaconNum = param.getBeaconNum();
		Beacon beaconInfo = beaconService.getBeacon(beaconNum);

		String sDate = StringUtil.NVL(request.getParameter("sDate"), DateUtil.getDate("yyyy-MM-dd") + "00:00:00");
		String eDate = StringUtil.NVL(request.getParameter("eDate"), DateUtil.getDate("yyyy-MM-dd") + "23:59:59");

		param.setSUUID(companyInfo.getUUID());
		param.setUUID(beaconInfo.getUUID());
		param.setMajorVer(beaconInfo.getMajorVer());
		param.setMinorVer(beaconInfo.getMinorVer());
		param.setsDate(sDate);
		param.seteDate(eDate);

		List<?> list = presenceService.getPresenceTargetLogList(param);

		if(list != null) {
			info.put("result", "1");
			info.put("data", list);
		} else {
			info.put("result", "2");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}


	@RequestMapping(value="/tracking/presence/scanner/log.do", method=RequestMethod.GET)
	public ModelAndView scannerLog(PresenceLogSearchParam param, Company company) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/scanner/log");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setSUUID(companyInfo.getUUID());

		List<?> list = presenceService.getPresenceLogListByLimit(param);

		mnv.addObject("list", list);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/scanner/gflog.do", method=RequestMethod.GET)
	public ModelAndView scannerGflog(Model model, HttpServletRequest request, HttpServletResponse response,
			PresenceGfLogSearchParam param, Company company) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/scanner/gflog");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setSUUID(companyInfo.getUUID());
		param.setPageSize(30);

		List<?> list = presenceService.getPresenceGfLogListByLimit(param);
		Integer cnt = presenceService.getPresenceGfLogCount(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("pagination", page);
		mnv.addObject("page", param.getPage());
		mnv.addObject("param", param);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/scanner/iolog.do", method=RequestMethod.GET)
	public ModelAndView scannerIolog(Model model, HttpServletRequest request, HttpServletResponse response,
			PresenceIoLogSearchParam param, Company company) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/scanner/iolog");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setSUUID(companyInfo.getUUID());

		List<?> list = presenceService.getPresenceIoLogListByLimit(param);

		mnv.addObject("list", list);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/scanner/redisBeacon.do", method=RequestMethod.GET)
	@ResponseBody
	public String scannerRedisBeaconList(@RequestParam(value = "uuid", required = true)String uuid) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();

		List<Map<Object, Object>> data = presenceService.getPreBeaconList(uuid);
		info.put("data", data);

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/tracking/presence/scanner/findRedisItem.do", method=RequestMethod.GET)
	@ResponseBody
	public String findRedisItem(@RequestParam(value = "keyPattern", required = true)String keyPattern,
								@RequestParam(value = "type", required = false, defaultValue = "map")String type) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();

		if("map".equals(type)) {
			List<Map<Object, Object>> data = presenceService.findRedisItem(keyPattern);
			info.put("data", data);
		} else if("string".equals(type)) {
			List<String> data = presenceService.findRedisString(keyPattern);
			info.put("data", data);
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

    @RequestMapping(value="/tracking/presence/scanner/removeRedisItem.do", method=RequestMethod.GET)
    @ResponseBody
    public String removeRedisItem(@RequestParam(value = "key", required = true)String key) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            presenceService.removeRedisItem(key);
            info.put("success", "1");
        } catch(Exception exception) {
            info.put("success", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }


	@RequestMapping(value="/tracking/presence/beacon/target.do", method=RequestMethod.GET)
	public ModelAndView beaconTarget(HttpSession session, Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/beacon/target");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		mnv.addObject("companyInfo", companyInfo);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/beacon/target2.do", method=RequestMethod.GET)
	public ModelAndView beaconTarget2(Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/beacon/target2");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		mnv.addObject("companyInfo", companyInfo);

		return mnv;
	}


	@RequestMapping(value="/tracking/presence/beacon/target.user.list.do", method=RequestMethod.POST)
	@ResponseBody
	public String beaconTargetBeaconList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			PresenceBeaconTargetSearchParam param) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		// 비콘 정보가 없음
		List<?> list = presenceBeaconService.getPresenceBeaconTargetListByBeacon(param);

		if(list != null) {
			info.put("result", "1");
			info.put("data", list);
		} else {
			info.put("result", "2");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/tracking/presence/beacon/target.log.list.do", method=RequestMethod.POST)
	@ResponseBody
	public String beaconTargetLogList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			PresenceBeaconTargetSearchParam param, Company company) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		String sDate = StringUtil.NVL(request.getParameter("sDate"), DateUtil.getDate("yyyy-MM-dd") + "00:00:00");
		String eDate = StringUtil.NVL(request.getParameter("eDate"), DateUtil.getDate("yyyy-MM-dd") + "23:59:59");

		param.setUUID(companyInfo.getUUID());
		param.setsDate(sDate);
		param.seteDate(eDate);

		List<?> list = presenceBeaconService.getPresenceBeaconTargetLogList(param);
		if(list != null) {
			info.put("result", "1");
			info.put("data", list);
		} else {
			info.put("result", "2");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/tracking/presence/beacon/log.do", method=RequestMethod.GET)
	public ModelAndView beaconLog(Model model, HttpServletRequest request, HttpServletResponse response,
			PresenceBeaconLogSearchParam param, Company company) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/beacon/log");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setUUID(companyInfo.getUUID());

		List<?> list = presenceBeaconService.getPresenceBeaconLogListByLimit(param);

		mnv.addObject("list", list);

		return mnv;
	}

	@RequestMapping(value="/tracking/presence/scanner/floorlog.do", method=RequestMethod.GET)
	public ModelAndView scannerFloorlog(PresenceFloorLogSearchParam param, Company company) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/tracking/scanner/floorlog");

		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setSUUID(companyInfo.getUUID());
		param.setPageSize(30);

		List<?> list = presenceService.getPresenceFloorLogListByLimit(param);
		Integer cnt = presenceService.getPresenceFloorLogCount(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("pagination", page);
		mnv.addObject("page", param.getPage());
		mnv.addObject("param", param);

		return mnv;
	}

}
