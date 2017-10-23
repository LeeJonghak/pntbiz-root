package wms.beacon.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.common.beacon.domain.BeaconExternal;
import core.common.beacon.domain.BeaconRestrictedZone;
import core.common.enums.BooleanType;
import core.common.enums.ZoneType;
import core.wms.admin.company.domain.Company;
import framework.web.util.PagingParam;
import core.wms.beacon.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;
import wms.admin.company.service.CompanyService;
import wms.beacon.service.BeaconService;
import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.StringUtil;
import core.wms.map.domain.Floor;
import wms.map.service.FloorService;
import core.wms.sync.domain.Sync;
import wms.service.SyncService;

@Controller
public class BeaconController {

    @Autowired
    private BeaconService beaconService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private SyncService syncService;

    @Autowired
    private CodeService codeService;

	@Autowired
	private CompanyService companyService;

//    private MyContentsService myContentsService;

	@Value("#{config['contentsURL']}")
	private String contentsURL;

	@Value("#{config['beacon.image.src']}")
	private String beaconImageSrc;

    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 전체 비콘 목록을 Json 형태로 반환
     *
     * @return
     */
    @RequestMapping(value= "/beacon/info/all.json", method=RequestMethod.GET)
    @ResponseBody
    public String beaconAll(BeaconSearchParam param) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            param.setComNum(loginDetail.getCompanyNumber());
            param.setPageSizeZero();

            List<?> list = beaconService.getBeaconList(param);

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

    /**
     * 비콘 목록
     * edit: nohsoo 2015-02-26
     */
	@RequestMapping(value= "/beacon/info/list.do", method=RequestMethod.GET)
	public ModelAndView beaconListOld(BeaconSearchParam param, Code code) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/beacon/info/list");

        LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		param.setPageSize(30);

        code.setgCD("BEACONTYPE");
		List<?> beaconTypeCD = codeService.getCodeListByCD(code);
		mnv.addObject("beaconTypeCD", beaconTypeCD);

        Integer cnt = beaconService.getBeaconCount(param);
        List<?> list = beaconService.getBeaconList(param);
        list = beaconService.bindBeaconList(list, beaconTypeCD);
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

    /**
     * 비콘 등록 폼
     *
     * @param model
     * @return
     */
	@RequestMapping(value= "/beacon/info/form.do", method=RequestMethod.GET)
	public ModelAndView beaconForm(Model model, BeaconGroupSearchParam param, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/beacon/info/form");

        LoginDetail loginDetail = Security.getLoginDetail();

        code.setgCD("BEACONTYPE");
		List<?> beaconTypeCD = codeService.getCodeListByCD(code);
		mnv.addObject("beaconTypeCD", beaconTypeCD);

        // 층 목록
        Floor paramFloor = new Floor();
        paramFloor.setComNum(loginDetail.getCompanyNumber());
        List<?> floorList = floorService.getFloorGroup(paramFloor);
        // 층 번호 정렬
        Collections.sort(floorList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Floor floor1 = (Floor) o1;
                Floor floor2 = (Floor) o2;
                return floor1.getFloor().compareTo(floor2.getFloor());
            }
        });

        // 비콘 그릅 목록
        param.setComNum(loginDetail.getCompanyNumber());
        List<?> beaconGroupList = beaconService.getBeaconGroupList(param);
        mnv.addObject("beaconGroupList", beaconGroupList);

        model.addAttribute("floorList", floorList);

		Company company = new Company();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		Floor floor = new Floor();
		floor.setComNum(loginDetail.getCompanyNumber());
		List<?> floorCodeList = floorService.getFloorCodeList(company);

		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("floorCodeList", floorCodeList);

		return mnv;
	}

    /**
     * 비콘 등록 처리
     * create: nohsoo 2015-02-26
     * jhlee 2015-03-12 sync정보 업데이트 추가
     *
     * @param UUID
     * @param majorVer
     * @param minorVer
     * @param beaconName
     * @param txPower
     * @param lat
     * @param lng
     * @param beaconDesc
     * @return
     * @throws IOException
     */
    @RequestMapping(value= "/beacon/info/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconReg(@RequestParam(value = "UUID", required = true)String UUID,
                            @RequestParam(value = "majorVer", required = true)Integer majorVer,
                            @RequestParam(value = "minorVer", required = true)Integer minorVer,
                            @RequestParam(value = "beaconType", required = false)String beaconType,
							@RequestParam(value = "macAddr", required = false)String macAddr,
                            @RequestParam(value = "beaconName", required = true)String beaconName,
                            @RequestParam(value = "txPower", required = false)Float txPower,
                            @RequestParam(value = "lat", required = true)Double lat,
                            @RequestParam(value = "lng", required = true)Double lng,
                            @RequestParam(value = "floor", defaultValue = "1")String floor,
                            @RequestParam(value = "beaconGroupNum", required = false)Integer beaconGroupNum,
                            @RequestParam(value = "beaconDesc", required = false)String beaconDesc,
                            @RequestParam(value = "field1", required = false)String field1,
                            @RequestParam(value = "field2", required = false)String field2,
                            @RequestParam(value = "field3", required = false)String field3,
                            @RequestParam(value = "field4", required = false)String field4,
                            @RequestParam(value = "field5", required = false)String field5,
                            @RequestParam(value = "externalId", required = false)String externalId,
                            @RequestParam(value = "key", required=false)String[] key,
                            @RequestParam(value = "value", required=false)String[] value,
                            @RequestParam(value = "displayName", required=false)String[] displayName,
                            @RequestParam(value = "barcode", required = false)String barcode
                            ) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            /**
             * UUID 값은 소문자로 변경
             */
        	// 2015-05-12 jhlee : 소문자 변경 주석 처리
            //UUID = StringUtils.lowerCase(UUID);

            Beacon vo = new Beacon();
            vo.setUUID(UUID);
            vo.setBeaconType(beaconType);
			vo.setMacAddr(macAddr);
            vo.setBeaconName(beaconName);
            vo.setTxPower(txPower);
            vo.setLat(lat);
            vo.setLng(lng);
            vo.setBeaconDesc(beaconDesc);
            vo.setMajorVer(majorVer);
            vo.setMinorVer(minorVer);
            vo.setFloor(floor);
            vo.setField1(field1);
            vo.setField2(field2);
            vo.setField3(field3);
            vo.setField4(field4);
            vo.setField5(field5);
            vo.setExternalId(externalId);
            for(int i=0; i<key.length; i++) {
                if(!key[i].equals("") && !value[i].equals("")) {
                    if(!displayName[i].equals(""))
                        vo.addExternalAttribute(key[i], value[i], displayName[i]);
                    else
                        vo.addExternalAttribute(key[i], value[i]);
                }
            }
            vo.setBarcode(barcode);

            LoginDetail loginDetail = Security.getLoginDetail();
            beaconService.registerBeacon(loginDetail, vo);

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);

			if(!StringUtil.NVL(beaconGroupNum).equals(0)) {
				BeaconGroupMapping mapping = new BeaconGroupMapping();
            	mapping.setBeaconNum(vo.getBeaconNum());
            	mapping.setBeaconGroupNum(beaconGroupNum);
                this.beaconService.registerBeaconGroupMapping(mapping);
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
     * 비콘 수정 폼
     * edit: nohsoo 2015-02-26
     *
     * @param beaconNum
     * @return
     */
	@RequestMapping(value= "/beacon/info/mform.do", method=RequestMethod.GET)
	public ModelAndView beaconMForm(@RequestParam(value = "beaconNum")Long beaconNum
								, BeaconGroupSearchParam param, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/beacon/info/mform");

        Beacon beacon = beaconService.getBeacon(beaconNum);
        mnv.addObject("beacon",  beacon);

        LoginDetail loginDetail = Security.getLoginDetail();

        code.setgCD("BEACONTYPE");
		List<?> beaconTypeCD = codeService.getCodeListByCD(code);
		mnv.addObject("beaconTypeCD", beaconTypeCD);

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

        // 비콘 그릅 목록
		BeaconGroupSearchParam groupParam = new BeaconGroupSearchParam();
		groupParam.setComNum(loginDetail.getCompanyNumber());
        List<?> beaconGroupList = beaconService.getBeaconGroupList(groupParam);
        mnv.addObject("beaconGroupList", beaconGroupList);

        mnv.addObject("floorList", floorList);

		Company company = new Company();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);

		Floor floor = new Floor();
		floor.setComNum(loginDetail.getCompanyNumber());
		List<?> floorCodeList = floorService.getFloorCodeList(company);

		mnv.addObject("companyInfo", companyInfo);
		mnv.addObject("floorCodeList", floorCodeList);

		return mnv;
	}

    /**
     * 비콘 수정 처리
     * create: nohsoo 2015-02-26
     * jhlee 2015-03-12 sync정보 업데이트 추가
     *
     * @param beaconNum
     * @param UUID
     * @param majorVer
     * @param minorVer
     * @param beaconName
     * @param txPower
     * @param lat
     * @param lng
     * @param beaconDesc
     * @return
     * @throws IOException
     */
    @RequestMapping(value= "/beacon/info/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconMod(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                            @RequestParam(value = "UUID", required = true)String UUID,
                            @RequestParam(value = "majorVer", required = true)Integer majorVer,
                            @RequestParam(value = "minorVer", required = true)Integer minorVer,
                            @RequestParam(value = "beaconType", required = false)String beaconType,
							@RequestParam(value = "macAddr", required = false)String macAddr,
                            @RequestParam(value = "beaconName", required = true)String beaconName,
                            @RequestParam(value = "txPower", required = false)Float txPower,
                            @RequestParam(value = "lat", required = false)Double lat,
                            @RequestParam(value = "lng", required = false)Double lng,
                            @RequestParam(value = "floor", defaultValue = "1")String floor,
                            @RequestParam(value = "beaconGroupNum", required = false)Integer beaconGroupNum,
                            @RequestParam(value = "beaconDesc", required = false)String beaconDesc,
                            @RequestParam(value = "codeNum", required = false)Integer codeNum,
                            @RequestParam(value = "field1", required = false)String field1,
                            @RequestParam(value = "field2", required = false)String field2,
                            @RequestParam(value = "field3", required = false)String field3,
                            @RequestParam(value = "field4", required = false)String field4,
                            @RequestParam(value = "field5", required = false)String field5,
                            @RequestParam(value = "externalId", required = false)String externalId,
                            @RequestParam(value = "key", required=false)String[] key,
                            @RequestParam(value = "value", required=false)String[] value,
                            @RequestParam(value = "displayName", required=false)String[] displayName,
                            @RequestParam(value = "barcode", required = false)String barcode
                            ) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {
            /**
             * UUID 값은 소문자로 변경
             */
        	// 2015-05-12 jhlee : 소문자 변경 주석 처리
            //UUID = StringUtils.lowerCase(UUID);

            Beacon vo = new Beacon();
            vo.setBeaconNum(beaconNum);
            vo.setUUID(UUID);
            vo.setBeaconType(beaconType);
			vo.setMacAddr(macAddr);
            vo.setBeaconName(beaconName);
            vo.setTxPower(txPower);
            vo.setLat(lat);
            vo.setLng(lng);
            vo.setBeaconDesc(beaconDesc);
            vo.setMajorVer(majorVer);
            vo.setMinorVer(minorVer);
            vo.setFloor(floor);
            vo.setField1(field1);
            vo.setField2(field2);
            vo.setField3(field3);
            vo.setField4(field4);
            vo.setField5(field5);
            vo.setExternalId(externalId);
            for(int i=0; i<key.length; i++) {
                if(!key[i].equals("") && !value[i].equals("")) {
                    if(!displayName[i].equals(""))
                        vo.addExternalAttribute(key[i], value[i], displayName[i]);
                    else
                        vo.addExternalAttribute(key[i], value[i]);
                }
            }
            vo.setBarcode(barcode);

            LoginDetail loginDetail = Security.getLoginDetail();

            beaconService.modifyBeacon(vo);


            /**
             * CodeAction 등록 처리
             * @author nohsoo 2015-03-12
             */
            if(codeNum!=null) {

                if(codeNum>0) {
                    beaconService.setBeaconCodeAction(loginDetail, beaconNum, codeNum);
                }
            }


            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);

			Beacon beaconInfo = beaconService.getBeacon(beaconNum);

			BeaconGroupMapping mapping = new BeaconGroupMapping();
			if(!StringUtil.NVL(beaconGroupNum).equals(0)) {
				if(!StringUtil.NVL(beaconInfo.getBeaconGroupNum()).equals(0)) {
					mapping.setBeaconNum(beaconInfo.getBeaconNum());
	            	mapping.setBeaconGroupNum(beaconGroupNum);
					mapping.setFromBeaconNum(beaconInfo.getBeaconNum());
	            	mapping.setFromBeaconGroupNum(beaconInfo.getBeaconGroupNum());
					this.beaconService.modifyGeofencingGroupMapping(mapping);
				} else {
					mapping.setBeaconNum(beaconInfo.getBeaconNum());
	            	mapping.setBeaconGroupNum(beaconGroupNum);
					this.beaconService.registerBeaconGroupMapping(mapping);
				}
            } else {
            	mapping.setBeaconNum(beaconInfo.getBeaconNum());
            	mapping.setBeaconGroupNum(beaconInfo.getBeaconGroupNum());
                this.beaconService.deleteBeaconGroupMapping(mapping);
            }

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value= "/beacon/info/bcImgUpload.do", method=RequestMethod.POST)
    @ResponseBody
    public String bcImgUpload(HttpServletRequest request, HttpServletResponse response, Beacon beacon) throws IOException {

    	Map<String, Object> info = new HashMap<String, Object>();
		// 전송정보
		MultipartRequest multi = new MultipartRequest(request, Security.getLocalImagePath(), (1048576 * 5), "utf-8");

		LoginDetail loginDetail = Security.getLoginDetail();
		beacon.setComNum(loginDetail.getCompanyNumber());

		Long beaconNum = Long.valueOf((StringUtil.NVL(multi.getParameter("beaconNum"), "")));

		// 기존이미지 삭제 시
		Beacon beaconInfo = beaconService.getBeacon(beaconNum);
		beacon.setBeaconNum(beaconNum);
		beacon.setImgSrc(beaconInfo.getImgSrc());

        try {
        	beaconService.modifyBeaconImage(multi, beacon);
/*            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);*/


			Beacon newBeaconInfo = beaconService.getBeacon(beacon.getBeaconNum());

			info.put("imgSrc", newBeaconInfo.getImgSrc());

			// 비콘 설치 사진
			String fileDate = StringUtil.getSubString(newBeaconInfo.getImgSrc(), 0, 6);
			String beaconImageURL = contentsURL + "/" + newBeaconInfo.getComNum() + beaconImageSrc + "/" + fileDate + "/";
			String imgUrl = beaconImageURL + newBeaconInfo.getImgSrc();
			info.put("imgUrl", imgUrl);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value= "/beacon/info/bcImgDel.do", method=RequestMethod.POST)
    @ResponseBody
    public String bcImgDel(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	Map<String, Object> info = new HashMap<String, Object>();

		Long beaconNum = Long.valueOf((StringUtil.NVL(request.getParameter("beaconNum"), "")));

		Beacon beaconInfo = beaconService.getBeacon(beaconNum);

        try {
        	beaconService.deleteBeaconImage(beaconInfo);
/*            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("BEACON");
			syncService.updateSync(sync);*/

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    /**
     * 비콘 위치 이동 처리
     *
     * @author nohsoo 2015-03-15
     * @param beaconNum
     * @param lat
     * @param lng
     * @return
     * @throws IOException
     */
    @RequestMapping(value= "/beacon/info/latlng.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconLatLngMod(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                            @RequestParam(value = "lat", required = true)Double lat,
                            @RequestParam(value = "lng", required = true)Double lng) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Beacon vo = new Beacon();
            //where
            vo.setBeaconNum(beaconNum);
            vo.setComNum(loginDetail.getCompanyNumber());
            //set
            vo.setLat(lat);
            vo.setLng(lng);
            beaconService.modifyBeacon(vo);

            Sync sync = new Sync();
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

    /**
     * 비콘 삭제 처리
     * create: nohsoo 2015-02-26
     * jhlee 2015-03-12 sync정보 업데이트 추가
     * @param beaconNum
     * @return
     * @throws IOException
     */
    @RequestMapping(value= "/beacon/info/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconDel(@RequestParam(value = "beaconNum", required = true)Long beaconNum) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

        	LoginDetail loginDetail = Security.getLoginDetail();

            beaconService.deleteBeacon(beaconNum);

            Sync sync = new Sync();
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

	@RequestMapping(value= "/beacon/monitor/list.do", method=RequestMethod.GET)
	public ModelAndView monitorList(BeaconStateSearchParam param) throws Exception {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/beacon/monitor/list");

        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setPageSize(30);

        Integer cnt = beaconService.getBeaconStateCount(param);
        List<?> list = beaconService.getBeaconStateList(param);
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

	@RequestMapping(value= "/beacon/group/list.do", method=RequestMethod.GET)
	public ModelAndView beaconGroupList(BeaconGroupSearchParam param) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/beacon/group/list");

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		param.setPageSize(30);
		//param.initPage(30, 10);

		Integer cnt = beaconService.getBeaconGroupCount(param);
		List<?> list = beaconService.getBeaconGroupList(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
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

	@RequestMapping(value= "/beacon/group/form.do", method=RequestMethod.GET)
	public ModelAndView beaconGroupForm() {
		ModelAndView mnv = new ModelAndView();

		mnv.setViewName("/beacon/group/form");
		return mnv;
	}

	@RequestMapping(value= "/beacon/group/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String beaconGroupReg(BeaconGroup beaconGroup, Sync sync) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		beaconGroup.setComNum(loginDetail.getCompanyNumber());

		// 등록
		try {
			beaconService.registerBeaconGroup(beaconGroup);
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

	@RequestMapping(value= "/beacon/group/mform.do", method=RequestMethod.GET)
	public ModelAndView beaconGroupMForm(BeaconGroup beaconGroup) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/beacon/group/mform");

		LoginDetail loginDetail = Security.getLoginDetail();
		beaconGroup.setComNum(loginDetail.getCompanyNumber());
        BeaconGroup beaconGroupInfo = beaconService.getBeaconGroup(beaconGroup);

        mnv.addObject("beaconGroup",  beaconGroupInfo);
		return mnv;
	}

    @RequestMapping(value= "/beacon/group/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconGroupMod(BeaconGroup beaconGroup, Sync sync) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        LoginDetail loginDetail = Security.getLoginDetail();
        beaconGroup.setComNum(loginDetail.getCompanyNumber());

        try {
            beaconService.modifyBeaconGroup(beaconGroup);
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

    @RequestMapping(value= "/beacon/group/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconGroupDel(BeaconGroup beaconGroup, Sync sync) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        LoginDetail loginDetail = Security.getLoginDetail();
        beaconGroup.setComNum(loginDetail.getCompanyNumber());

        try {
            beaconService.deleteBeaconGroup(beaconGroup);
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

    /**
            * 출인통제구역 목록
            * edit: KimSolip 2017-08-25
    */
    @RequestMapping(value= "/beacon/restrictedZone/list.do", method=RequestMethod.GET)
    public ModelAndView beaconRestrictedZoneList(BeaconExternal beacon, PagingParam param) {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/beacon/restrictedZone/list");

        BeaconRestrictedZone beaconRestrictedZone = new BeaconRestrictedZone();
        beaconRestrictedZone.setBeaconNum(beacon.getBeaconNum());
        Integer cnt = beaconService.getBeaconRestrictedZoneCount(beaconRestrictedZone);
        List<BeaconRestrictedZone> list = beaconService.getBeaconRestrictedZoneList(beaconRestrictedZone);

        param.setPageSize(30);
        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        String page = pagination.print();

        mnv.addObject("beacon", beacon);
        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        mnv.addObject("pagination", page);
        mnv.addObject("page", param.getPage());
        mnv.addObject("param", param);

        return mnv;
    }

    /**
            * 출인통제구역 등록
            * edit: KimSolip 2017-08-25
    */
    @RequestMapping(value= "/beacon/restrictedZone/form.do", method=RequestMethod.GET)
    public ModelAndView beaconRestrictedZoneForm(BeaconExternal beacon, @RequestParam(value = "permitted", required = true)String permitted) {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/beacon/restrictedZone/form");

        mnv.addObject("beacon", beacon);
        mnv.addObject("permitted", permitted);

        return mnv;
    }

    /**
            * 출인통제구역 수정
            * edit: KimSolip 2017-08-25
            */
    @RequestMapping(value= "/beacon/restrictedZone/mform.do", method=RequestMethod.GET)
    public ModelAndView beaconRestrictedZoneMform(BeaconExternal beacon, BeaconRestrictedZone beaconRestrictedZone) {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/beacon/restrictedZone/mform");

        beaconRestrictedZone.setBeaconNum(beacon.getBeaconNum());
        beaconRestrictedZone = beaconService.getBeaconRestrictedZoneInfo(beaconRestrictedZone);

        mnv.addObject("beaconRestrictedZone",  beaconRestrictedZone);
        mnv.addObject("beacon", beacon);

        return mnv;
    }

    /**
            * 출인통제구역 등록 처리
            * edit: KimSolip 2017-08-25
    */
    @RequestMapping(value= "/beacon/restrictedZone/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconRestricedZoneReg(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                                   @RequestParam(value = "permitted", required=true)BooleanType permitted,
                                   @RequestParam(value = "zoneType", required=true)ZoneType zoneType,
                                   @RequestParam(value = "zoneId", required=true)String zoneId,
                                   @RequestParam(value = "startDate", required=false)Integer startDate,
                                   @RequestParam(value = "endDate", required=false)Integer endDate,
                                   @RequestParam(value = "key", required=false)String[] key,
                                   @RequestParam(value = "value", required=false)String[] value
    ) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try{
            BeaconRestrictedZone beaconRestrictedZone = new BeaconRestrictedZone();
            beaconRestrictedZone.setBeaconNum(beaconNum);
            beaconRestrictedZone.setPermitted(permitted);
            beaconRestrictedZone.setZoneType(zoneType);
            beaconRestrictedZone.setZoneId(zoneId);
            beaconRestrictedZone.setStartDate(startDate);
            beaconRestrictedZone.setEndDate(endDate);
            for(int i=0; i<key.length; i++) {
                if(!key[i].equals("") && !value[i].equals(""))
                    beaconRestrictedZone.putAdditionalAttribute(key[i], value[i]);
            }
            beaconService.registerBeaconRestrictedZone(beaconRestrictedZone);
            info.put("result", "1");
        } catch (Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value= "/beacon/restrictedZone/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconRestrictedZoneMod(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                            @RequestParam(value = "zoneType", required=true)ZoneType zoneType,
                            @RequestParam(value = "zoneId", required=true)String zoneId,
                            @RequestParam(value = "permitted", required=true)BooleanType permitted,
                            @RequestParam(value = "startDate", required=false)Integer startDate,
                            @RequestParam(value = "endDate", required=false)Integer endDate,
                            @RequestParam(value = "key", required=false)String[] key,
                            @RequestParam(value = "value", required=false)String[] value
    ) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {
            BeaconRestrictedZone beaconRestrictedZone = new BeaconRestrictedZone();
            beaconRestrictedZone.setBeaconNum(beaconNum);
            beaconRestrictedZone.setZoneType(zoneType);
            beaconRestrictedZone.setZoneId(zoneId);
            beaconRestrictedZone.setPermitted(permitted);
            beaconRestrictedZone.setStartDate(startDate);
            beaconRestrictedZone.setEndDate(endDate);
            for(int i=0; i<key.length; i++) {
                if(!key[i].equals("") && !value[i].equals(""))
                    beaconRestrictedZone.putAdditionalAttribute(key[i], value[i]);
            }
            beaconService.modifyBeaconRestrictedZone(beaconRestrictedZone);
            info.put("result", "1");
        } catch (Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value= "/beacon/restrictedZone/modpermitted.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconRestrictedZoneModPermitted(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                                          @RequestParam(value = "permitted", required=true)BooleanType permitted) throws IOException {

        try {
            BeaconRestrictedZone beaconRestrictedZone = new BeaconRestrictedZone();
            beaconRestrictedZone.setBeaconNum(beaconNum);
            beaconRestrictedZone.setPermitted(permitted);
            beaconService.modifyAllBeaonRestrictedZoneForPermitted(beaconRestrictedZone);
            return "1";
        } catch (Exception e) {
            logger.error("exception", e);
            return "2";
        }
    }

    @RequestMapping(value= "/beacon/restrictedZone/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String beaconRestrcitedZoneDel(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                                    @RequestParam(value = "zoneType", required=true)ZoneType zoneType,
                                    @RequestParam(value = "zoneId", required=true)String zoneId
    ) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {
            BeaconRestrictedZone beaconRestrictedZone = new BeaconRestrictedZone();
            beaconRestrictedZone.setBeaconNum(beaconNum);
            beaconRestrictedZone.setZoneType(zoneType);
            beaconRestrictedZone.setZoneId(zoneId);
            beaconService.deleteBeaconRestrictedZone(beaconRestrictedZone);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

}
