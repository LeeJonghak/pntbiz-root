package wms.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jcraft.jsch.SftpException;

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import core.wms.beacon.domain.Beacon;
import core.wms.beacon.domain.BeaconSearchParam;
import core.wms.beacon.domain.BeaconState;
import wms.beacon.service.BeaconService;
import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.JsonUtil;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import wms.geofencing.service.GeofencingService;
import core.wms.map.domain.Floor;
import wms.map.service.FloorService;
import core.wms.scanner.domain.ScannerSearchParam;
import wms.scanner.service.ScannerService;
import core.wms.tracking.domain.PresenceGfLogSearchParam;
import core.wms.tracking.domain.PresenceSetmap;
import core.wms.tracking.domain.PresenceTargetSearchParam;
import wms.tracking.service.PresenceService;
import wms.tracking.service.PresenceSetmapService;

@Controller
public class DashboardController {

	@Autowired
    private BeaconService beaconService;

    @Autowired
    private GeofencingService geofencingService;

    @Autowired
	private ScannerService scannerService;

    @Autowired
	private FloorService floorService;

    @Autowired
	private CompanyService companyService;

    @Autowired
	private PresenceService presenceService;

    @Autowired
	private PresenceSetmapService presenceSetmapService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dashboard/main.do", method=RequestMethod.GET)
	public ModelAndView dashboard(HttpServletRequest request) throws Exception {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/dashboard/main");

        LoginDetail loginDetail = Security.getLoginDetail();
        Integer comNum = loginDetail.getCompanyNumber();
        String userID = loginDetail.getUsername();

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", comNum);

        Company company = new Company();
 		company.setComNum(comNum);
 		Company companyInfo = companyService.getCompanyInfo(company);

        // 총 비콘 갯수
 		BeaconSearchParam bParam = new BeaconSearchParam();
 		bParam.setComNum(comNum);
        int beaconCnt = beaconService.getBeaconCount(bParam);

        // 지오펜싱 갯수
        GeofencingGroupSearchParam gParam = new GeofencingGroupSearchParam();
        gParam.setComNum(comNum);
        int geofenceCnt = geofencingService.getGeofencingCount(gParam);

        // 스캐너 갯수
        ScannerSearchParam scannerSearchPparam = new ScannerSearchParam();
        scannerSearchPparam.setComNum(comNum);
        int scannerCnt = scannerService.getScannerCount(scannerSearchPparam);

        // 층 정보
        Floor floorParam = new Floor();
        floorParam.setComNum(comNum);
        int floorCnt = floorService.getFloorCount(floorParam);
        List<?> floorList = floorService.getFloorList(floorParam);
        floorList = floorService.bindFloorList((List<Floor>) floorList);

        // 싱크정보 (마지막으로 수정된 데이터 LIST)
/*        Sync sync = new Sync();
        sync.setComNum(comNum);
        List<?> syncList = syncService.getSyncList(sync);*/

        // 망실 통계 비콘
/*        StatBeaconSearchParam statBeaconSearchParam = new StatBeaconSearchParam();
        statBeaconSearchParam.setComNum(loginDetail.getCompanyNumber());*/

     	// 측위용 비콘 총 갯수
     	//int preBeaconCnt = statService.statBeaconPresenceCount(statBeaconSearchParam);


 		// 망실가능성 높은 비콘
/*     	statBeaconSearchParam.setBeaconName(null);
 		String lastDate = DateUtil.getAddDate(DateUtil.getDate("yyyy-MM-dd 00:00:00"), -20, "yyyy-MM-dd HH:mm:ss");
 		statBeaconSearchParam.setLastDate(lastDate);
 		int lossBeaconCnt = statService.statBeaconMonitorCount(statBeaconSearchParam);

 		// 망실가능성 높은 비콘리스트
 		List<?> lossList = statService.statBeaconMonitorList(statBeaconSearchParam);*/

     	HashMap<String, Object> paramLossBe = new HashMap<String, Object>();
     	paramLossBe.put("SUUID", companyInfo.getUUID());
     	paramLossBe.put("comNum", comNum);
		List<Beacon> lossBeList = beaconService.getLogLossBeaconStateList(paramLossBe);

		// 주요 Zone별 진출입 횟수
		PresenceGfLogSearchParam paramPresenceGf = new PresenceGfLogSearchParam();
		paramPresenceGf.setSUUID(companyInfo.getUUID());
		List<?> presenceGfInOutList = presenceService.getChartPresenceGfInOutLogListByLimit(paramPresenceGf);

 		// 스캐너(비콘) 로깅 리스트
/*		PresenceLogSearchParam plsParam = new PresenceLogSearchParam();
		plsParam.setSUUID(companyInfo.getUUID());
		List<?> presenceLogList = presenceService.getPresenceLogListByLimit(plsParam);*/

		// 비콘(스마트폰) 로깅 리스트)
/*		PresenceBeaconLogSearchParam pblsParam = new PresenceBeaconLogSearchParam();
		pblsParam.setUUID(companyInfo.getUUID());
		List<?> beaconLogList = presenceBeaconService.getPresenceBeaconLogListByLimit(pblsParam);*/

		// 실시간 위치 측위
		PresenceSetmap setmap = new PresenceSetmap();
		setmap.setComNum(comNum);
		PresenceSetmap setmapInfo = presenceSetmapService.getPresenceSetmapInfo(setmap);


		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("comNum", comNum);
		mnv.addObject("userID", userID);
 		mnv.addObject("beaconCnt", beaconCnt);
        mnv.addObject("geofenceCnt", geofenceCnt);
        mnv.addObject("scannerCnt", scannerCnt);
        mnv.addObject("floorCnt", floorCnt);
        mnv.addObject("floorList", floorList);
        //mnv.addObject("syncList", syncList);
        //mnv.addObject("preCnt", preBeaconCnt);
     	//mnv.addObject("lossBeaconCnt", lossBeaconCnt);
     	//mnv.addObject("lastDate", lastDate);
     	mnv.addObject("lossBeList", lossBeList);
     	//mnv.addObject("presenceLogList", presenceLogList);
     	//mnv.addObject("beaconLogList", beaconLogList);
     	mnv.addObject("presenceGfInOutList", presenceGfInOutList);
     	mnv.addObject("setmapInfo", setmapInfo);

		return mnv;
	}

	/**
	 * 차트 DATA
	 * @param request
	 * @param response
	 * @param session
	 * @param param
	 * @param company
	 * @return
	 * @throws ServletException
	 * @throws SftpException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value="/dashboard/logData.ajax.do", method=RequestMethod.POST)
	@ResponseBody
	public String logDataLogDataAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			PresenceTargetSearchParam param, Company company) throws ServletException, SftpException, IOException, ParseException {

		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		Integer comNum = loginDetail.getCompanyNumber();

 		company.setComNum(comNum);
		Company companyInfo = companyService.getCompanyInfo(company);

		try {
			// 망실 가능성이 높은 비콘 관제 현황 차트
			HashMap<String, Object> paramLossBe = new HashMap<String, Object>();
			// jhlee 160802 comNum 추가
			paramLossBe.put("SUUID", companyInfo.getUUID());
			paramLossBe.put("comNum", comNum);
			List<BeaconState> lossBeChartList = beaconService.getChartLogLossBeaconStateList(paramLossBe);

			// 마이크로펜스 관제 현황 차트
			PresenceGfLogSearchParam paramPresenceGf = new PresenceGfLogSearchParam();
			paramPresenceGf.setSUUID(companyInfo.getUUID());
			List<?> presenceGfList = presenceService.getChartPresenceGfLogListByLimit(paramPresenceGf);

			// 스캐너 위치측위 현황 차트
	/*		PresenceLogSearchParam plsParam1 = new PresenceLogSearchParam();
			plsParam1.setSUUID(companyInfo.getUUID());
			List<?> presenceLogList = presenceService.getChartPresenceLogList(plsParam1);*/

			// 비콘 위치측위 현황 차트
	/*		PresenceBeaconLogSearchParam pblsParam = new PresenceBeaconLogSearchParam();
			pblsParam.setUUID(companyInfo.getUUID());
			List<?> beaconLogList = presenceBeaconService.getPresenceBeaconLogListByLimit(pblsParam);*/

			if(lossBeChartList != null) {
				info.put("lossBeChartList", lossBeChartList);
			}else {
				info.put("lossBeChartList", null);
			}

			if(presenceGfList != null) {
				info.put("presenceGfList", presenceGfList);
			}else {
				info.put("presenceGfList", null);
			}


		}catch(Exception e) {
			info.put("result", "2");

		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}


	@RequestMapping(value="/dashboard/monitor.do", method=RequestMethod.GET)
	public ModelAndView monitor() throws Exception {

		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/dashboard/monitor");

		return mnv;
	}
}