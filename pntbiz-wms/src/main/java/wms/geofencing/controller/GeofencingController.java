package wms.geofencing.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//import map.service.MyContentsService;
import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.Security;
import framework.web.Validation;
import framework.web.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jcraft.jsch.SftpException;

import core.common.code.domain.Code;
import wms.component.auth.LoginDetail;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingGroup;
import core.wms.geofencing.domain.GeofencingGroupMapping;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import core.wms.geofencing.domain.GeofencingLatlng;
import wms.geofencing.service.GeofencingService;
import core.wms.map.domain.Floor;
import wms.map.service.FloorService;
import core.wms.sync.domain.Sync;
import wms.service.SyncService;

@Controller
public class GeofencingController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private GeofencingService geofencingService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private SyncService syncService;

//    private MyContentsService myContentsService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 전체 지오펜스 목록
     *
     * @author nohsoo 2015-03-11
     * @param floor 층번호
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/geofencing/info/all.json", method=RequestMethod.GET)
    @ResponseBody
    public String geofencingAll(@RequestParam(value = "floor", required = false)String floor) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            List<?> list = geofencingService.getGeofencingAll(loginDetail, floor);
            List<?> latlngs = geofencingService.getGeofencingLatlngAll(loginDetail, floor);

            HashMap<Long, ArrayList<HashMap<String, Object>>> latlngMap = new HashMap<Long, ArrayList<HashMap<String, Object>>>();

            for(Object obj: latlngs) {
                GeofencingLatlng latlng = (GeofencingLatlng) obj;
                if(!latlngMap.containsKey(latlng.getFcNum())) {
                    latlngMap.put(latlng.getFcNum(), new ArrayList<HashMap<String, Object>>());
                }
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("fcNum", latlng.getFcNum());
                map.put("lat", latlng.getLat());
                map.put("lng", latlng.getLng());
                map.put("orderSeq", latlng.getOrderSeq());
                map.put("radius", latlng.getRadius());
                latlngMap.get(latlng.getFcNum()).add(map);
            }

            ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
            for(Object obj: list) {
                Geofencing geofencing = (Geofencing)obj;
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("comNum", geofencing.getComNum());
                map.put("fcNum", geofencing.getFcNum());
                map.put("fcType", geofencing.getFcType());
                map.put("fcShape", geofencing.getFcShape());
                map.put("fcName", geofencing.getFcName());
                map.put("floor", geofencing.getFloor());
                map.put("evtEnter", geofencing.getEvtEnter());
                map.put("evtLeave", geofencing.getEvtLeave());
                map.put("evtStay", geofencing.getEvtStay());
                map.put("userID", geofencing.getUserID());
                map.put("modDate", geofencing.getModDate());
                map.put("regDate", geofencing.getRegDate());
                map.put("latlngs", latlngMap.get(geofencing.getFcNum()));

                result.add(map);
            }

            info.put("result", "1");
            info.put("geofencings", result);
        }
        catch (Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    /**
     * 모든 지오펜스 좌표정보
     *
     * @author nohsoo 2015-03-11
     * @param floor 층번호
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/geofencing/info/latlng/all.json", method=RequestMethod.GET)
    @ResponseBody
    public String geofencingLatlngAll(@RequestParam(value = "floor", required = false)String floor) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            List<?> list = geofencingService.getGeofencingLatlngAll(loginDetail, floor);
            info.put("result", "1");
            info.put("beacons", list);
        }
        catch (Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

	@RequestMapping(value="/geofencing/info/list.do", method=RequestMethod.GET)
	public ModelAndView geofencingList(HttpServletRequest request, GeofencingGroupSearchParam param) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/geofencing/info/list");

        LoginDetail loginDetail = Security.getLoginDetail();

        param.setPageSize(30);
        param.setComNum(loginDetail.getCompanyNumber());

        Integer cnt = geofencingService.getGeofencingCount(param);
        List<?> list = geofencingService.getGeofencingList(param);

        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString = param.getQueryString();
        String page = pagination.print();

        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        mnv.addObject("pagination", page);
        mnv.addObject("page", param.getPage());
        mnv.addObject("geofencingParam", param);

        return mnv;
	}

    /**
     * 지오펜스 등록 폼
     * @param model
     * @return
     */
	@RequestMapping(value="/geofencing/info/form.do", method=RequestMethod.GET)
	public ModelAndView geofencingForm(Model model, GeofencingGroupSearchParam param) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/geofencing/info/form");

        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());

        // 층 목록
        Floor paramFloor = new Floor();
        paramFloor.setComNum(loginDetail.getCompanyNumber());
        List<?> floorList = floorService.getFloorList(paramFloor);
        // 층 번호 정렬
        Collections.sort(floorList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Floor floor1 = (Floor) o1;
                Floor floor2 = (Floor) o2;
                return floor1.getFloor().compareTo(floor2.getFloor());
            }
        });
        mnv.addObject("floorList", floorList);
        mnv.addObject("fcGroupList", geofencingService.getGeofencingGroupList(param));

		// 170925 hs - add
		Company company = new Company();
		company.setComNum(loginDetail.getCompanyNumber());
		List<?> floorCodeList = floorService.getFloorCodeList(company);
		mnv.addObject("floorCodeList", floorCodeList);

		return mnv;
	}

    /**
     * 지오펜스 등록 처리
     *
     * create 2015-02-27
     * jhlee 2015-03-12 sync정보 업데이트 추가
     * @param geofencing
     * @param shapeDataJson
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/geofencing/info/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String geofencingReg(@ModelAttribute @Valid Geofencing geofencing,
                                @RequestParam(value = "shapeData", required = true)String shapeDataJson)
            throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            // 펜스 좌표정보 객체 정보 Json Binding
            ObjectMapper jsonMapper = new ObjectMapper();
            ArrayList<GeofencingLatlng> latlngs = jsonMapper.readValue(shapeDataJson, jsonMapper.getTypeFactory().constructCollectionType(List.class, GeofencingLatlng.class));

            //펜스 생성 처리
            LoginDetail loginDetail = Security.getLoginDetail();
            this.geofencingService.registerGeofencing(loginDetail, geofencing, latlngs);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("GEOFENCING");
			syncService.updateSync(sync);

            if(!StringUtil.NVL(geofencing.getFcGroupNum()).equals(0)) {
            	GeofencingGroupMapping mapping = new GeofencingGroupMapping();
            	mapping.setFcNum(geofencing.getFcNum());
            	mapping.setFcGroupNum(geofencing.getFcGroupNum());
                this.geofencingService.registerGeofencingGroupMapping(mapping);
            }

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }
        JsonUtil jsonUtil = new JsonUtil(info);

        return jsonUtil.toJson();
    }

    /**
     * 펜스정보 수정 처리
     *
     * @param geofencingParam
     * @param shapeDataJson
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/geofencing/info/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String geofencingMod(@Validated({Validation.Add.class}) @ModelAttribute Geofencing geofencingParam,
                                @RequestParam(value = "shapeData", required = true)String shapeDataJson)
            throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            // 펜스 좌표정보 객체 정보 Json Binding
            ObjectMapper jsonMapper = new ObjectMapper();
            ArrayList<GeofencingLatlng> latlngs = jsonMapper.readValue(shapeDataJson, jsonMapper.getTypeFactory().constructCollectionType(List.class, GeofencingLatlng.class));
            Geofencing geofencingInfo = geofencingService.getGeofencing(geofencingParam.getFcNum());

            /**
             * 펜스 수정 처리
             */
            LoginDetail loginDetail = Security.getLoginDetail();
            this.geofencingService.modifyGeofencing(loginDetail, geofencingParam, latlngs);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("GEOFENCING");
			syncService.updateSync(sync);

			GeofencingGroupMapping mapping = new GeofencingGroupMapping();
            BeanUtils.copyProperties(mapping, geofencingParam);
            if(!StringUtil.NVL(geofencingParam.getFcGroupNum(), 0).equals(0)) {
				if(!StringUtil.NVL(geofencingInfo.getFcGroupNum()).equals(0)) {
					mapping.setFcNum(geofencingInfo.getFcNum());
	            	mapping.setFcGroupNum(geofencingParam.getFcGroupNum());
					mapping.setFromFcNum(geofencingInfo.getFcNum());
	            	mapping.setFromFcGroupNum(geofencingInfo.getFcGroupNum());
					this.geofencingService.modifyGeofencingGroupMapping(mapping);
				} else {
					mapping.setFcNum(geofencingInfo.getFcNum());
	            	mapping.setFcGroupNum(geofencingParam.getFcGroupNum());
					this.geofencingService.registerGeofencingGroupMapping(mapping);
				}
            } else {
                try {
                    mapping.setFcNum(geofencingInfo.getFcNum());
                    mapping.setFcGroupNum(geofencingInfo.getFcGroupNum());
                    this.geofencingService.deleteGeofencingGroupMapping(mapping);
                } catch(Exception e) {}
            }

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }
        JsonUtil jsonUtil = new JsonUtil(info);

        return jsonUtil.toJson();
    }

    /**
     * 펜스 수정폼
     * edit: nohsoo 2015-02-27
     *
     * @param fcNum
     * @return
     */
	@RequestMapping(value="/geofencing/info/mform.do", method=RequestMethod.GET)
	public ModelAndView geofencingMForm(
			GeofencingGroupSearchParam param,
			@RequestParam(value = "fcNum")Long fcNum) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/geofencing/info/mform");

        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());

        GeofencingGroupSearchParam groupParam = new GeofencingGroupSearchParam();
        groupParam.setComNum(loginDetail.getCompanyNumber());
        List<?> fcGroupList = geofencingService.getGeofencingGroupList(groupParam);
        mnv.addObject("fcGroupList", fcGroupList);

        // 층 목록
        Floor paramFloor = new Floor();
        paramFloor.setComNum(loginDetail.getCompanyNumber());
        List<?> floorList = floorService.getFloorGroup(paramFloor);
        // 층 번호 정렬
        Collections.sort(floorList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Floor floor1 = (Floor)o1;
                Floor floor2 = (Floor)o2;
                return floor1.getFloor().compareTo(floor2.getFloor());
            }
        });
        mnv.addObject("floorList", floorList);

        Geofencing geofencing = geofencingService.getGeofencing(fcNum);
        mnv.addObject("geofencing", geofencing);

        List<?> latlngList = geofencingService.getGeofencingLatlngList(fcNum);
        mnv.addObject("latlngList", latlngList);

		Company company = new Company();
		company.setComNum(loginDetail.getCompanyNumber());
		List<?> floorCodeList = floorService.getFloorCodeList(company);
		mnv.addObject("floorCodeList", floorCodeList);

		return mnv;
	}

    /**
     * 지오펜스 삭제 처리
     *     - 지오펜스 삭제, 지오펜스에 할당된 컨텐츠 삭제, 지오펜스에 할당된 태스크(CodeAction) 삭제
     * jhlee 2015-03-12 sync정보 업데이트 추가
     * @author nohsoo 2015-03-12
     * @param fcNum
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/geofencing/info/del.do", method = RequestMethod.POST)
    @ResponseBody
    public String geofencingDel(@RequestParam(value = "fcNum", required = true)Long fcNum) throws IOException {


        Map<String, String> info = new HashMap<String, String>();
        try {
        	LoginDetail loginDetail = Security.getLoginDetail();

            geofencingService.deleteGeofencing(fcNum);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("GEOFENCING");
			syncService.updateSync(sync);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }
        JsonUtil jsonUtil = new JsonUtil(info);

        return jsonUtil.toJson();
    }

    @RequestMapping(value="/geofencing/info/sync.do", method=RequestMethod.POST)
    @ResponseBody
    public String geofencingSync(Company company) throws IOException {
        Map<String, String> info = new HashMap<String, String>();
        LoginDetail loginDetail = Security.getLoginDetail();
        try {
            company.setComNum(loginDetail.getCompanyNumber());
            Company companyInfo = companyService.getCompanyInfo(company);
            geofencingService.syncGeofencingRedisData(companyInfo);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/geofencing/group/list.do", method=RequestMethod.GET)
	public ModelAndView geofencingGroupList(Model model, HttpServletRequest request,
			GeofencingGroupSearchParam param, Code code) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/geofencing/group/list");

		LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = geofencingService.getGeofencingGroupCount(param);
		List<?> list = geofencingService.getGeofencingGroupList(param);
//		list = geofencingService.bindContentsList(list, conCD);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString  = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("result", "1");
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("pagination", page);
		mnv.addObject("page", param.getPage());
		mnv.addObject("geofencingParam", param);

		return mnv;
	}

	@RequestMapping(value="/geofencing/group/form.do", method=RequestMethod.GET)
	public ModelAndView geofencingGroupForm(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/geofencing/group/form");
		return mnv;
	}

	@RequestMapping(value="/geofencing/group/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String geofencingGroupReg(GeofencingGroup geofencingGroup, Sync sync) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		geofencingGroup.setComNum(loginDetail.getCompanyNumber());

		// 등록
		try {
			geofencingService.registerGeofencingGroup(geofencingGroup);
			sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
 		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/geofencing/group/mform.do", method=RequestMethod.GET)
	public ModelAndView geofencingGroupMForm(GeofencingGroup geofencingGroup) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/geofencing/group/mform");

		LoginDetail loginDetail = Security.getLoginDetail();
		geofencingGroup.setComNum(loginDetail.getCompanyNumber());
        GeofencingGroup geofencingGroupInfo = geofencingService.getGeofencingGroup(geofencingGroup);

        mnv.addObject("geofencingGroup",  geofencingGroupInfo);
		return mnv;
	}

    @RequestMapping(value="/geofencing/group/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String geofencingGroupMod(GeofencingGroup geofencingGroup, Sync sync) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        LoginDetail loginDetail = Security.getLoginDetail();
        geofencingGroup.setComNum(loginDetail.getCompanyNumber());

        try {
            geofencingService.modifyGeofencingGroup(geofencingGroup);
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/geofencing/group/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String geofencingGroupDel(GeofencingGroup geofencingGroup, Sync sync) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        LoginDetail loginDetail = Security.getLoginDetail();
        geofencingGroup.setComNum(loginDetail.getCompanyNumber());

        try {
            geofencingService.deleteGeofencingGroup(geofencingGroup);
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }
}