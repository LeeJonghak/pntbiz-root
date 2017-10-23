package wms.map.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import core.wms.beacon.domain.Beacon;
import core.wms.beacon.domain.BeaconGroupSearchParam;
import wms.beacon.service.BeaconService;
import wms.component.auth.LoginDetail;
import core.wms.event.domain.EventSearchParam;
import wms.event.service.EventService;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.PagingParam;
import framework.web.util.StringUtil;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import wms.geofencing.service.GeofencingService;
import core.wms.info.domain.CodeAction;
import core.wms.map.domain.ChaosArea;
import core.wms.map.domain.Contents;
import core.wms.map.domain.Floor;
import core.wms.map.domain.Node;
import core.wms.map.domain.NodePair;
import wms.map.service.ChaosAreaService;
import wms.map.service.FloorService;
import wms.map.service.MyContentsService;
import wms.map.service.NodeService;
import core.wms.sync.domain.Sync;
import wms.service.SyncService;

@Controller
public class MapController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BeaconService beaconService;

	@Autowired
	private FloorService floorService;

	@Autowired
	private CompanyService companyService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private MyContentsService contentsService;

    @Autowired
    private GeofencingService geofencingService;

    @Autowired
    private SyncService syncService;

    // nohsoo
    //@Autowired
    //private PushEventService pushEventService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ChaosAreaService chaosAreaService;


	// 층별정보페이지
	@RequestMapping(value="/map/floor/floor.do", method=RequestMethod.GET)
	public ModelAndView floor(Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/floor");
		Gson gson = new Gson();

		// 로그인정보
		LoginDetail loginDetail = Security.getLoginDetail();

		// 업체정보
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		//RTL_LOCATION 정보
		mnv.addObject("floorCodeList", gson.toJson(floorService.getFloorCodeList(company)));

		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("UUID", companyInfo.getUUID());
		logger.info("uuid {}", companyInfo.getUUID());
		return mnv;
	}

	@RequestMapping(value="/map/floor/floor2.do", method=RequestMethod.GET)
	public ModelAndView floor2(Company company) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/floor2");
		Gson gson = new Gson();

		// 로그인정보
		LoginDetail loginDetail = Security.getLoginDetail();

		// 업체정보
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		//RTL_LOCATION 정보
		mnv.addObject("floorCodeList", gson.toJson(floorService.getFloorCodeList(company)));

		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("UUID", companyInfo.getUUID());
		logger.info("uuid {}", companyInfo.getUUID());
		return mnv;
	}

	@RequestMapping(value="/map/floor/floorCodeList.do", method=RequestMethod.GET)
	@ResponseBody
	public Object floorCodeList(Company company) {

		Map<String, Object> info = new HashMap<String, Object>();

		// 로그인정보
		LoginDetail loginDetail = Security.getLoginDetail();

		// 업체정보
		company.setComNum(loginDetail.getCompanyNumber());
		List<?> floorCodeList = floorService.getFloorCodeList(company);


		info.put("data", floorCodeList);
		info.put("result", "1");
		return info;
	}


	// 층별정보
	@RequestMapping(value="/map/floor/info.do", method=RequestMethod.POST)
	@ResponseBody
	public String floorInfo(HttpServletResponse response, Floor floor) throws ServletException, SftpException, IOException, ParseException {
		Map<String, Object> info = new HashMap<String, Object>();
		LoginDetail loginDetail = Security.getLoginDetail();
		floor.setComNum(loginDetail.getCompanyNumber());

		Floor floorInfo = floorService.getFloorInfo(floor);

		if(floorInfo != null) {
			floorInfo = floorService.bindFloor(floorInfo);
			info.put("result", "1");
			info.put("floor", floorInfo);
		} else {
			info.put("result", "2");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 층별리스트
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/map/floor/list.do", method=RequestMethod.GET)
	@ResponseBody
	public String floorList(HttpServletResponse response, Floor floor) throws ServletException, SftpException, IOException, ParseException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		floor.setComNum(loginDetail.getCompanyNumber());

		List<?> list = floorService.getFloorList(floor);
		list = floorService.bindFloorList((List<Floor>) list);

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

	// 층별그룹
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/map/floor/group.do", method=RequestMethod.POST)
	@ResponseBody
	public String floorGroup(HttpServletResponse response, Floor floor) throws ServletException, SftpException, IOException, ParseException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		floor.setComNum(loginDetail.getCompanyNumber());

		List<?> list = floorService.getFloorGroup(floor);
		list = floorService.bindFloorList((List<Floor>) list);

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

    // 층별정보 중복체크
    @RequestMapping(value="/map/floor/dup.do", method=RequestMethod.POST)
    @ResponseBody
    public String floorDup(Floor floor) throws ServletException, SftpException, IOException {
        Map<String, String> info = new HashMap<String, String>();

        LoginDetail loginDetail = Security.getLoginDetail();
        floor.setComNum(loginDetail.getCompanyNumber());

        int cnt = floorService.getFloorCount(floor);

        if(cnt > 0) {
            info.put("result", "2"); // 이미등록
        } else {
            info.put("result", "1"); // 등록가능
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

	// 층별정보 등록
	@RequestMapping(value="/map/floor/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String floorReg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Floor floor, Sync sync) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		// 전송정보
		MultipartRequest multi = new MultipartRequest(request, Security.getLocalImagePath(), 5242880, "utf-8");

		LoginDetail loginDetail = Security.getLoginDetail();
		floor.setComNum(loginDetail.getCompanyNumber());
        floor.setFloor(StringUtil.NVL(StringUtil.NVL(multi.getParameter("floor"), "")));


		// 등록
		try {
		    //1. 중복체크
		    if(floorService.checkFloorDuplication(floor)) {
	            info.put("result", "3"); // 이미등록
	        }else{
	            //2. 등록
    			floorService.registerFloor(multi, floor);

                sync.setComNum(loginDetail.getCompanyNumber());
    			sync.setSyncType("FLOOR");
    			syncService.updateSync(sync);

    			info.put("result", "1");
	        }
		} catch(DataAccessException dae) {
			info.put("result", "2");
			dae.printStackTrace();
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 층별정보 수정
	@RequestMapping(value="/map/floor/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String floorMod(HttpServletRequest request, HttpServletResponse response, Floor floor, Sync sync) throws ServletException, SftpException, IOException, ParseException {
		Map<String, Object> info = new HashMap<String, Object>();
		// 전송정보
		MultipartRequest multi = new MultipartRequest(request, Security.getLocalImagePath(), 5242880, "utf-8");

		LoginDetail loginDetail = Security.getLoginDetail();

		System.out.println(floor.getFloorNum());

		floor.setComNum(loginDetail.getCompanyNumber());
        floor.setFloorNum(Integer.parseInt((StringUtil.NVL(multi.getParameter("floorNum"), ""))));
        floor.setFloor(StringUtil.NVL(StringUtil.NVL(multi.getParameter("floor"), "")));

		// 업데이트
        try {
		    //1. 중복체크
            if(floorService.checkFloorDuplication(floor)){
                info.put("result", "3"); // 이미등록
            }else{
                // 기존이미지 삭제 시
                Floor floorInfo = floorService.getFloorInfo(floor);
                floor.setImgSrc(floorInfo.getImgSrc());

    			floorService.modifyFloor(multi, floor);
    			floorInfo = floorService.getFloorInfo(floor);
    			floorInfo = floorService.bindFloor(floorInfo);
    			sync.setComNum(loginDetail.getCompanyNumber());
    			sync.setSyncType("FLOOR");
    			syncService.updateSync(sync);
    			info.put("result", "1");
    			//info.put("floor", floor.getFloor());
    			info.put("floor", floorInfo);
            }
		} catch(Exception dae) {
			info.put("result", "2");
            dae.printStackTrace();
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 층별정보 삭제
	@RequestMapping(value="/map/floor/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String floorDel(HttpServletRequest request, HttpServletResponse response, Floor floor, Sync sync) throws ServletException, SftpException, IOException, ParseException {
		Map<String, Object> info = new HashMap<String, Object>();
		Integer floorParam = Integer.parseInt(StringUtil.NVL(request.getParameter("floorNum"), ""));

		LoginDetail loginDetail = Security.getLoginDetail();
		floor.setComNum(loginDetail.getCompanyNumber());
		floor.setFloorNum(floorParam);

		Floor floorInfo = floorService.getFloorInfo(floor);
		boolean chk = false;

		try {
			floorService.removeFloor(floor);
			sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("FLOOR");
			syncService.updateSync(sync);
			info.put("result", "1");
			chk = true;
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {
			if(chk == true) {
				String res = "";
				res = floorService.deleteFloorImageLocal(floorInfo, "1", "");
				logger.debug(res);
			}
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/map/bplan.do", method=RequestMethod.GET)
	public ModelAndView bplan(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/bplan");
		return mnv;
	}

    @RequestMapping(value="/map/bplan2.do", method=RequestMethod.GET)
    public ModelAndView bplan2(Model model, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/bplan2");
        return mnv;
    }

    /**
     * 비콘 정보
     *     비콘 배치도에서 비콘 클릭시 비콘 정보 표시
     * @param beaconNum
     * @return
     */
    @RequestMapping(value="/map/beaconInfo.ajax.do", method=RequestMethod.GET)
    public ModelAndView bplanBeaconInfo(@RequestParam(value = "beaconNum", required = true)long beaconNum) {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/ajax.beaconinfo");

        LoginDetail loginDetail = Security.getLoginDetail();

        /**
         * 비콘 그룹정보
         */
        BeaconGroupSearchParam beaconGroupSearchParam = new BeaconGroupSearchParam();
        beaconGroupSearchParam.setComNum(loginDetail.getCompanyNumber());
        List<?> beaconGroupList = beaconService.getBeaconGroupList(beaconGroupSearchParam);
        mnv.addObject("beaconGroupList", beaconGroupList);

        /**
         * 비콘 정보
         */
        Beacon beacon = this.beaconService.getBeacon(beaconNum);
        mnv.addObject("beacon", beacon);

        /**
         * 비콘에 할당된 컨텐츠 목록
         */
        List<?> contents = this.contentsService.getMyBeaconContentsList(loginDetail, beaconNum);
        mnv.addObject("contents", contents);


        /**
         * 태스크 목록(CODE_ACTION)
         */
        List<?> codeActionList = contentsService.getCodeActionAll(loginDetail);
        mnv.addObject("codeActionList", codeActionList);

        /**
         * 할당된 코드액션(태스크) 목록
         */
        List<?> codeAction = this.contentsService.getMyBeaconCodeActionList(loginDetail, beaconNum);
        if(codeAction.size()>0) {
            mnv.addObject("codeAction", codeAction.get(0));
        }

        /**
         * nohsoo 2015-04-22 할당가능한 이벤트 목록
         */
        EventSearchParam eParam = new EventSearchParam();
        eParam.setComNum(loginDetail.getCompanyNumber());
        eParam.setPageSizeZero();
        List<?> eventList = eventService.getEventList(eParam);
        mnv.addObject("eventList", eventList);

        return mnv;
    }

	/**
	 * 비콘 관제 레이어팝업
	 * @param beaconNum
	 * @return
	 */
	@RequestMapping(value="/map/beaconStatus.ajax.do", method=RequestMethod.GET)
	public ModelAndView bplanBeaconStatus(@RequestParam(value = "beaconNum", required = true)long beaconNum) {

		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/ajax.beacon-status");

		@SuppressWarnings("unused")
		LoginDetail loginDetail = Security.getLoginDetail();

		/**
		 * 비콘 정보
		 */
		Beacon beacon = this.beaconService.getBeacon(beaconNum);
		mnv.addObject("beacon", beacon);

		return mnv;
	}

    /**
     * 펜스 정보
     *     비콘 배치도에서 펜스 클릭시 정보 표시
     *
     * @param fcNum
     * @return
     */
    @RequestMapping(value="/map/geofencingInfo.ajax.do", method=RequestMethod.GET)
    public ModelAndView bplanGeofencingInfo(@RequestParam(value = "fcNum", required = true)long fcNum) {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/ajax.geofencinginfo");

        LoginDetail loginDetail = Security.getLoginDetail();

        /**
         * nohsoo 2015-04-22 할당가능한 이벤트 목록
         */
        EventSearchParam eParam = new EventSearchParam();
        eParam.setComNum(loginDetail.getCompanyNumber());
        eParam.setPageSizeZero();
        List<?> eventList = eventService.getEventList(eParam);
        mnv.addObject("eventList", eventList);

        /**
         * 펜스 정보
         */
        Geofencing geofencing = geofencingService.getGeofencing(fcNum);
        mnv.addObject("geofencing", geofencing);

        /**
         * 지오펜스에 할당된 컨텐츠 목록
         */
        List<?> contents = this.contentsService.getMyGeofencingContentsList(loginDetail, geofencing.getFcNum());
        mnv.addObject("contents", contents);

        /**
         * 펜스 이벤트별 컨텐츠 할당 상태 확인
         */
        HashMap<String, Boolean> assignedEvent = new HashMap<String, Boolean>();
        assignedEvent.put("enter", false);
        assignedEvent.put("stay", false);
        assignedEvent.put("leave", false);
        for(Object obj: contents) {
            Contents con = (Contents)obj;
            if(StringUtils.equals("ENTER", con.getRefSubType())) {
                assignedEvent.put("enter", true);
            }
            if(StringUtils.equals("STAY", con.getRefSubType())) {
                assignedEvent.put("stay", true);
            }
            if(StringUtils.equals("LEAVE", con.getRefSubType())) {
                assignedEvent.put("leave", true);
            }
        }
        mnv.addObject("assignedEvent", assignedEvent);

        /**
         * 태스크 목록(CODE_ACTION)
         */
        List<?> codeActionList = contentsService.getCodeActionAll(loginDetail);
        mnv.addObject("codeActionList", codeActionList);

        /**
         * 펜스에 할당된 태스크 목록(CODE_ACTION)
         */
        HashMap<String, Object> myCodeActionMap = new HashMap<String, Object>();
        List<?> myCodeActionList = contentsService.getMyGeofencingCodeActionList(loginDetail, fcNum);
        for(Object obj: myCodeActionList) {
            CodeAction codeAction = (CodeAction) obj;
            myCodeActionMap.put(codeAction.getRefSubType(), codeAction);
        }
        mnv.addObject("myCodeActionMap", myCodeActionMap);

        /**
         * 지오펜싱 그룹
         */
        GeofencingGroupSearchParam geofencingGroupSearchParam = new GeofencingGroupSearchParam();
        geofencingGroupSearchParam.setComNum(loginDetail.getCompanyNumber());
        List<?> fcGroupList = geofencingService.getGeofencingGroupList(geofencingGroupSearchParam);
        mnv.addObject("fcGroupList", fcGroupList);

        return mnv;
    }

    /**
     * 할당 가능한 컨텐츠 목록
     *
     * @param request
     * @param refNum
     * @param refType
     * @param pageNumber
     * @param opt
     * @param keyword
     * @return
     */
    @RequestMapping(value="/map/contents.ajax.do", method=RequestMethod.GET)
    public ModelAndView bplanContentList(HttpServletRequest request,
                                         @RequestParam(value = "refNum", required = false)Long refNum,
                                         @RequestParam(value = "refType", required = false)String refType,
                                         @RequestParam(value = "page", defaultValue = "1")int pageNumber,
                                         @RequestParam(value = "opt", required = false)String opt,
                                         @RequestParam(value = "keyword", required = false)String keyword) {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/ajax.contentlist");


        int pageSize = 9999;
        int blockSize = 10;
        PagingParam paging = new PagingParam();
        paging.setPage(pageNumber);
        paging.initPage(pageSize, blockSize);
        LoginDetail loginDetail = Security.getLoginDetail();
        List<?> list = this.contentsService.getContentsList(loginDetail, refNum, refType, paging, opt, keyword);
        Integer cnt = this.contentsService.countContentsList(loginDetail, refNum, refType, opt, keyword);
        Pagination pagination = new Pagination(paging.getPage(), cnt, paging.getPageSize(), paging.getBlockSize());
        pagination.queryString  = request.getServletPath();
        String page = pagination.print();

        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        mnv.addObject("page", page);
        mnv.addObject("param", paging);

        return mnv;

    }

    /**
     * 비콘에 컨텐츠 할당 처리
     *
     * @param beaconNum
     * @param conNums
     * @return
     * @throws IOException
     */
     @RequestMapping(value="/map/assignBeaconContent.do", method=RequestMethod.POST)
     @ResponseBody
     public String assignBeaconContent(@RequestParam(value = "beaconNum")Long beaconNum,
                                       @RequestParam(value = "conNums")Long[] conNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            contentsService.assignMyBeaconContent(loginDetail, beaconNum, conNums);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("BEACON");
            syncService.updateSync(sync);

            info.put("result", "1");
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 노드에 컨텐츠 할당 처리
     *
     * @author 2015-03-31 nohsoo
     * @param nodeNum
     * @param conNums
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/assignNodeContent.do", method=RequestMethod.POST)
    @ResponseBody
    public String assignNodeContent(@RequestParam(value = "nodeNum")Long nodeNum,
                                    @RequestParam(value = "conNums")Long[] conNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            contentsService.assignMyNodeContent(loginDetail, nodeNum, conNums);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);

            info.put("result", "1");

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }


    /**
     * 비콘에 이벤트(PNS) 할당 처리.
     * edit: nohsoo 2015-04-21 사용 않함
     * @param beaconNum
     * @param pnsNums
     * @return
     * @throws IOException
     */
    /*@RequestMapping(value="/map/assignBeaconEvent.do", method=RequestMethod.POST)
    @ResponseBody
    public String assignBeaconEvent(@RequestParam(value = "beaconNum")Long beaconNum,
                                    @RequestParam(value = "pnsStageNos")Integer[] pnsNums) throws IOException {

        Map<String, PresenceRequestParam> info = new HashMap<String, PresenceRequestParam>();

        try {
            LoginDetail loginDetail = CommonUtil.getLoginDetail();
            for(Integer pnsNum: pnsNums) {
                pushEventService.assignBeaconEvent(loginDetail, beaconNum, pnsNum);
            }

            *//**
             * 싱크업데이트
             *//*
            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("BEACON");
            syncService.updateSync(sync);

            info.put("result", "1");
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }*/

    /**
     * 비콘에 이벤트 할당(맵핑)
     * create: nohsoo 2015-04-21
     *
     * @param beaconNum 비콘고유번호
     * @param evtNums 이벤트고유번호
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/assignBeaconEvent.do", method=RequestMethod.POST)
    @ResponseBody
    public String assignBeaconEvent(@RequestParam(value = "beaconNum")Long beaconNum,
                                    @RequestParam(value = "evtNums")Integer[] evtNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {


        } catch (Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/setBeaconEvent.do", method=RequestMethod.POST)
    @ResponseBody
    public String setBeaconEvent(@RequestParam(value = "beaconNum")Long beaconNum,
                                     @RequestParam(value = "refSubType")String refSubType,
                                     @RequestParam(value = "conNum")Long conNum,
                                     @RequestParam(value = "evtNum")Integer evtNum) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            beaconService.setBeaconEvent(loginDetail, beaconNum, refSubType, conNum, evtNum);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("BEACON");
            syncService.updateSync(sync);

            info.put("result", "1");

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 지오펜스에 컨텐츠 할당 처리
     *
     * @param fcNum 펜스번호
     * @param fenceEventType 이벤트종류 [ENTER,LEAVE,STAY]
     * @param conNums 컨텐츠 번호들[]
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/assignGeofencingContent.do", method=RequestMethod.POST)
    @ResponseBody
    public String assignGeofencingContent(@RequestParam(value = "fcNum")Long fcNum,
                                          @RequestParam(value = "eventType")String fenceEventType,
                                          @RequestParam(value = "conNums")Long[] conNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            contentsService.assignMyGeofencingContent(loginDetail, fenceEventType, fcNum, conNums);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("GEOFENCING");
            syncService.updateSync(sync);

            info.put("result", "1");
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/setGeofencingCodeAction.do", method=RequestMethod.POST)
    @ResponseBody
    public String setGeofencingCodeAction(@RequestParam(value = "fcNum")Long fcNum,
                                          @RequestParam(value = "eventType")String fenceEventType,
                                          @RequestParam(value = "codeNum", required = false)Integer codeNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            geofencingService.setGeofencingCodeAction(loginDetail, fcNum, fenceEventType, codeNum);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("GEOFENCING");
            syncService.updateSync(sync);

            info.put("result", "1");
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;


    }

    /**
     * 펜스에 이벤트 할당
     * create: nohsoo 2015-04-22
     *
     * @param fcNum
     * @param refSubType
     * @param evtNum
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/setGeofencingEvent.do", method=RequestMethod.POST)
    @ResponseBody
    public String setGeofencingEvent(@RequestParam(value = "fcNum")Long fcNum,
                                     @RequestParam(value = "refSubType")String refSubType,
                                     @RequestParam(value = "conNum")Long conNum,
                                     @RequestParam(value = "evtNum", required = false)Integer evtNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {

            LoginDetail loginDetail = Security.getLoginDetail();
            geofencingService.setGeofencingEvent(loginDetail, fcNum, refSubType, conNum, evtNum);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("GEOFENCING");
            syncService.updateSync(sync);

            info.put("result", "1");

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 비콘에 할당된 컨텐츠 제거
     *
     * @param beaconNum
     * @param conNums
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/unassignBeaconContent.do", method=RequestMethod.POST)
    @ResponseBody
    public String unassignBeaconContent(@RequestParam(value = "beaconNum")Long beaconNum,
                                      @RequestParam(value = "conNums")Long[] conNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            contentsService.unassignMyBeaconContent(loginDetail, beaconNum, conNums);
            info.put("result", "1");

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("BEACON");
            syncService.updateSync(sync);

        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }


    /**
     * 비콘에 할당된 컨텐츠 제거
     *
     * @author nohsoo 2015-03-31
     * @param nodeNum
     * @param conNums
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/unassignNodeContent.do", method=RequestMethod.POST)
    @ResponseBody
    public String unassignNodeContent(@RequestParam(value = "nodeNum")Long nodeNum,
                                        @RequestParam(value = "conNums")Long[] conNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            contentsService.unassignMyNodeContent(loginDetail, nodeNum, conNums);
            info.put("result", "1");

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);
        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 지오펜스에 할당된 컨텐츠 제거
     *     태스크[CODE_ACTION] 함께 제거
     *
     * @author nohsoo 2015-03-12
     * @param fcNum
     * @param conNums
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/unassignGeofencingContent.do", method=RequestMethod.POST)
    @ResponseBody
    public String unassignGeofencingContent(@RequestParam(value = "fcNum")Long fcNum,
                                            @RequestParam(value = "conNums")String[] conNums) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            contentsService.unassignMyGeofencingContent(loginDetail, fcNum, conNums);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("GEOFENCING");
            syncService.updateSync(sync);

            info.put("result", "1");
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 노드 정보
     *     비콘 배치도에서 노드 클릭시 정보 표시
     * @param nodeNum
     * @return
     */
    @RequestMapping(value="/map/nodeInfo.ajax.do", method=RequestMethod.GET)
    public ModelAndView bplanNodeInfo(@RequestParam(value = "nodeNum", required = true)long nodeNum) {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/ajax.nodeinfo");

        LoginDetail loginDetail = Security.getLoginDetail();

        /**
         * 노드 정보
         */
        Node node = this.nodeService.getNode(loginDetail, nodeNum);
        mnv.addObject("node", node);


        /**
         * POI 카테고리명
         */
        HashMap<String, String> poiCategory = nodeService.getPOICateList();
        mnv.addObject("poiCategory", poiCategory);

        /**
         * 노드에 할당된 컨텐츠 목록
         */
        List<?> contents = this.contentsService.getMyNodeContentsList(loginDetail, nodeNum);
        mnv.addObject("contents", contents);

        return mnv;
    }

    /**
     * nohsoo 2015-04-21 사용 않함
     */
    /*@RequestMapping(value="/map/event.ajax.do", method=RequestMethod.GET)
    public ModelAndView bplanEventList(HttpServletRequest request,
                                       @RequestParam(value = "refNum", required = false)Long refNum,
                                       @RequestParam(value = "page", defaultValue = "1")int pageNumber,
                                       @RequestParam(value = "opt", required = false)String opt,
                                       @RequestParam(value = "keyword", required = false)String keyword) {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/ajax.eventlist");


        int pageSize = 9999;
        int blockSize = 10;
        PagingParam paging = new PagingParam();
        paging.setPage(pageNumber);
        paging.initPage(pageSize, blockSize);
        @SuppressWarnings("unused")
		LoginDetail loginDetail = CommonUtil.getLoginDetail();

        Integer cnt = 0;
        List<?> list = this.pushEventService.callCmsServicePushStageList(paging.getFirstItemNo()+1, paging.getPageSize(), cnt);
        Pagination pagination = new Pagination(paging.getPage(), cnt, paging.getPageSize(), paging.getBlockSize());
        pagination.queryString  = request.getServletPath();
        @SuppressWarnings("unused")
		String page = pagination.print();

        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        *//*mnv.addObject("page", page);*//*

        mnv.addObject("param", paging);

        return mnv;
    }*/


    @RequestMapping(value="/map/event.ajax.do", method=RequestMethod.GET)
    public String bplanEventList(EventSearchParam param, ModelMap map) {

        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setPageSizeZero();

        List<?> list = eventService.getEventList(param);
        Integer cnt = eventService.getEventCount(param);
        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString  = param.getQueryString();
        String page = pagination.print();

        map.addAttribute("cnt", cnt);
        map.addAttribute("list", list);
        map.addAttribute("page", page);
        map.addAttribute("param", param);

        return "/map/ajax.eventlist2";
    }


    @RequestMapping(value="/map/nodeAll.json", method=RequestMethod.GET)
    @ResponseBody
    public String nodeAllJson(@RequestParam(value = "floor", required = false)String floor,
                              @RequestParam(value = "type", required = false, defaultValue = "B")String type) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            List<?> list = this.nodeService.getNodeAll(loginDetail, floor, type);
            info.put("result", "1");
            info.put("nodes", list);

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/addNode.do", method=RequestMethod.POST)
    @ResponseBody
    public String addNode(@RequestParam(value = "floor", required = false)String floor,
                          @RequestParam(value = "nodeName", defaultValue = "")String nodeName,
                          @RequestParam(value = "lat", required = true)Double lat,
                          @RequestParam(value = "lng", required = true)Double lng,
                          @RequestParam(value = "type", required = false, defaultValue = "B")String type) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Node vo = new Node();
            vo.setFloor(floor);
            vo.setNodeName(nodeName);
            vo.setLat(lat);
            vo.setLng(lng);
            vo.setType(type);
            this.nodeService.registerNode(loginDetail, vo);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);

			Node nodeInfo = nodeService.getNode(loginDetail, vo.getNodeNum());

            info.put("result", "1");
            info.put("node", nodeInfo);

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/modifyNode.do", method=RequestMethod.POST)
    @ResponseBody
    public String modifyNode(
                          @RequestParam(value = "nodeNum", required = true)Long nodeNum,
                          @RequestParam(value = "floor", required = false)String floor,
                          @RequestParam(value = "nodeName2", required = false)String nodeName,
                          @RequestParam(value = "lat", required = false)Double lat,
                          @RequestParam(value = "lng", required = false)Double lng,
                          @RequestParam(value = "cate", required = false)String cate,
                          @RequestParam(value = "jointName", required = false)String jointName) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Node vo = new Node();
            // where
            vo.setNodeNum(nodeNum);
            //set
            vo.setFloor(floor);
            vo.setNodeName(nodeName);
            vo.setLat(lat);
            vo.setLng(lng);
            vo.setCate(cate);
            vo.setJointName(jointName);
            this.nodeService.modifyNode(loginDetail, vo);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);

            Node node = this.nodeService.getNode(loginDetail, nodeNum);
            info.put("result", "1");
            info.put("node", node);

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/delNode.do", method=RequestMethod.POST)
    @ResponseBody
    public String delNode(
            @RequestParam(value = "nodeNum", required = true)Long nodeNum) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Node node = this.nodeService.getNode(loginDetail, nodeNum);
            if(node==null) {
                info.put("result", "2");
                info.put("message", "노드가 존재하지 않습니다.");
            } else {
                this.nodeService.deleteNode(loginDetail, nodeNum);
                this.nodeService.deleteNodePair(loginDetail, node.getNodeID());

                Sync sync = new Sync();
                sync.setComNum(loginDetail.getCompanyNumber());
                sync.setSyncType("NODE");
                syncService.updateSync(sync);

                info.put("result", "1");
                info.put("node", node);
            }

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/pairAll.json", method=RequestMethod.GET)
    @ResponseBody
    public String pairAllJson(@RequestParam(value = "floor", required = false)String floor,
                              @RequestParam(value = "type", required = false, defaultValue = "B")String type) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            List<?> list = this.nodeService.getPairAll(loginDetail, floor, type);
            info.put("result", "1");
            info.put("pairs", list);

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/registerPair.do", method=RequestMethod.POST)
    @ResponseBody
    public String registerPair(
            @RequestParam(value = "startNodeID", required = true)Integer startNodeID,
            @RequestParam(value = "endNodeID", required = true)Integer endNodeID,
            @RequestParam(value = "floor", required = false)String floor,
            @RequestParam(value = "type", required = false, defaultValue = "B")String type) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            NodePair nodePair = this.nodeService.registerNodePair(loginDetail, startNodeID, endNodeID, floor, type);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);

            info.put("result", "1");
            info.put("nodePair", nodePair);

        } catch(Exception e) {
            logger.error("exception", e);

            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/map/delPair.do", method=RequestMethod.POST)
    @ResponseBody
    public String delPair(
            @RequestParam(value = "startNodeID", required = true)Integer startNodeID,
            @RequestParam(value = "endNodeID", required = true)Integer endNodeID) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            this.nodeService.deleteNodePair(loginDetail, startNodeID, endNodeID);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);

            info.put("result", "1");

        } catch(Exception e) {
            logger.error("exception", e);

            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

	@RequestMapping(value="/map/bplanform.do", method=RequestMethod.GET)
	public ModelAndView bplanform(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/bplanform");
		return mnv;
	}

	@RequestMapping(value="/map/gplan.do", method=RequestMethod.GET)
	public ModelAndView gplan(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/gplan");
		return mnv;
	}

	@RequestMapping(value="/map/gplanform.do", method=RequestMethod.GET)
	public ModelAndView gplanform(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/map/gplanform");
		return mnv;
	}


    /**
     * 혼돈영역을 등록
     * create: nohsoo 2015-04-22
     *
     * @param floor 층번호
     * @param lat 위도
     * @param lng 경도
     * @param radius 반지름(미터 단위)
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/registerChaosArea.do", method=RequestMethod.POST)
    @ResponseBody
    public String registerChaosArea(@RequestParam(value = "floor", required = true)String floor,
                                    @RequestParam(value = "lat", required = true)Double lat,
                                    @RequestParam(value = "lng", required = true)Double lng,
                                    @RequestParam(value = "radius", required = true)Float radius) throws IOException {
        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            ChaosArea chaosAreaInfo = chaosAreaService.registerChaosArea(loginDetail, floor, lat, lng, radius);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("CHAOS");
            syncService.updateSync(sync);

            info.put("result", "1");
            info.put("chaosArea", chaosAreaInfo);
        } catch(Exception e) {
            logger.error("exception", e);

            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 혼돈지역 정보 수정(위치, 반지름 값)
     * create: nohsoo 2015-04-22
     *
     * @param areaNum 고유번호
     * @param lat 위도
     * @param lng 경도
     * @param radius 반지름(미터 단위)
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/modifyChaosArea.do", method=RequestMethod.POST)
    @ResponseBody
    public String modifyChaosArea(@RequestParam(value = "areaNum", required = true)Integer areaNum,
                                  @RequestParam(value = "lat", required = true)Double lat,
                                  @RequestParam(value = "lng", required = true)Double lng,
                                  @RequestParam(value = "radius", required = true)Float radius) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            chaosAreaService.modifyChaosArea(loginDetail, areaNum, lat, lng, radius);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("CHAOS");
            syncService.updateSync(sync);

            info.put("result", "1");
        } catch(Exception e) {
            logger.error("exception", e);

            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 혼돈지역 삭제 처리
     * create: nohsoo 2015-04-22
     *
     * @param areaNum
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/deleteChaosArea.do", method=RequestMethod.POST)
    @ResponseBody
    public String deleteChaosArea(@RequestParam(value = "areaNum", required = true)Integer areaNum) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            chaosAreaService.deleteChaosArea(loginDetail, areaNum);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("CHAOS");
            syncService.updateSync(sync);

            info.put("result", "1");
        } catch(Exception e) {
            logger.error("exception", e);

            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    /**
     * 혼돈 지역 목록 조회
     * create: nohso o2015-04-22
     * @param floor
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/map/listChaosArea.do", method=RequestMethod.GET)
    @ResponseBody
    public String listChaosArea(@RequestParam(value = "floor")String floor) throws IOException {

        HashMap<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            List<?> list = chaosAreaService.getChaosAreaListAll(loginDetail, floor);
            info.put("result", "1");
            info.put("chaosareas", list);
        } catch(Exception e) {
            logger.error("exception", e);

            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

}