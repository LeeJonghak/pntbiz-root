package wms.contents.controller;

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

import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;
import wms.component.auth.LoginDetail;
import core.wms.contents.domain.Contents;
import core.wms.contents.domain.ContentsMapping;
import core.wms.contents.domain.ContentsMappingSearchParam;
import core.wms.contents.domain.ContentsSearchParam;
import core.wms.contents.domain.ContentsType;
import core.wms.contents.domain.ContentsTypeComponent;
import core.wms.contents.domain.ContentsTypeSearchParam;
import wms.contents.service.ContentsService;
import core.wms.event.domain.EventSearchParam;
import wms.event.service.EventService;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.StringUtil;
import core.wms.sync.domain.Sync;
import wms.service.SyncService;

@Controller
public class ContentsController {

	@Autowired
	private ContentsService contentsService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CodeService codeService;

	@Autowired
	private SyncService syncService;

	@Autowired
    private EventService eventService;

	@RequestMapping(value = "/contents/list.do", method = RequestMethod.GET)
	public ModelAndView contentsList(Model model, HttpServletRequest request, HttpServletResponse response,
			ContentsSearchParam param, Code code) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/list");

		code.setgCD("CONTYPE");
		List<?> conCD = codeService.getCodeListByCD(code);
		mnv.addObject("conCD", conCD);

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		param.setConType2("CHD");

		// 일괄매핑(이벤트리스트)
		EventSearchParam eParam = new EventSearchParam();
        eParam.setComNum(loginDetail.getCompanyNumber());
        eParam.setPageSizeZero();
		List<?> eventList = eventService.getEventList(eParam);

		param.setPageSize(30);
		Integer cnt = contentsService.getContentsCount(param);
		List<?> list = contentsService.getContentsList(param);
		list = contentsService.bindContentsList(list, conCD);

		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("result", "1");
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("eventList", eventList);
		mnv.addObject("pagination", page);
		mnv.addObject("page", param.getPage());
		mnv.addObject("param", param);

		return mnv;
	}

	@RequestMapping(value = "/contents/form.do", method = RequestMethod.GET)
	public ModelAndView contentsForm(Model model, HttpServletRequest request, HttpServletResponse response, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/form");
		code.setgCD("CONTYPE");
		List<?> conCD = codeService.getCodeListByCD(code);
		code.setgCD("EXPFLAG");
		List<?> expFlag = codeService.getCodeListByCD(code);

		mnv.addObject("conCD", conCD);
		mnv.addObject("expFlag", expFlag);
		return mnv;
	}

	@RequestMapping(value = "/contents/mform.do", method = RequestMethod.GET)
	public ModelAndView contentsMForm(Model model, HttpServletRequest request, HttpServletResponse response, Code code,
			Contents contents) throws ParseException {
		ModelAndView mnv = new ModelAndView();

		Integer conNum = Integer.parseInt(StringUtil.NVL(request.getParameter("conNum"), ""));

		LoginDetail loginDetail = Security.getLoginDetail();
		contents.setComNum(loginDetail.getCompanyNumber());
		contents.setConNum(conNum);

		// 콘텐츠 정보
		Contents contentsInfo = contentsService.getContentsInfo(contents);
		contentsInfo = contentsService.bindContents(contentsInfo);

		if ((contentsInfo.getComNum() == loginDetail.getCompanyNumber())
				&& (contentsInfo.getUserID().equals(loginDetail.getUsername()))) {
			code.setgCD("CONTYPE");
			List<?> conCD = codeService.getCodeListByCD(code);
			code.setgCD("EXPFLAG");
			List<?> expFlag = codeService.getCodeListByCD(code);

			mnv.addObject("conCD", conCD);
			mnv.addObject("expFlag", expFlag);
			mnv.addObject("contentsInfo", contentsInfo);
			mnv.setViewName("/contents/mform");
		} else {
			mnv.setViewName("notpriv");
		}
		return mnv;
	}

	// 콘텐츠 파일 삭제
	@RequestMapping(value = "/contents/fileDel.do", method = RequestMethod.POST)
	@ResponseBody
	public String fileDel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Contents contents) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		String conNum = StringUtil.NVL(request.getParameter("conNum"), "");
		String fileType = StringUtil.NVL(request.getParameter("fileType"), "");
		String num = StringUtil.NVL(request.getParameter("num"), "");

		contents.setConNum(Integer.parseInt(conNum));
		Contents contentsInfo = contentsService.getContentsInfo(contents);

		LoginDetail loginDetail = Security.getLoginDetail();

		if ((contentsInfo.getComNum() == loginDetail.getCompanyNumber())
				&& (contentsInfo.getUserID().equals(loginDetail.getUsername()))) {
			try {
				contentsService.deleteContentsFile(contentsInfo, fileType, num, "update");
				info.put("result", "1");
				info.put("fileType", fileType);
				info.put("num", num);
			} catch (DataAccessException dae) {
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

	// 콘텐츠 등록
	@RequestMapping(value = "/contents/reg.do", method = RequestMethod.POST)
	@ResponseBody
	public String contentsReg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Contents contents, Sync sync) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		// 전송정보
		MultipartRequest multi = new MultipartRequest(request, Security.getLocalImagePath(), 5242880, "utf-8");
		LoginDetail loginDetail = Security.getLoginDetail();
		contents.setComNum(loginDetail.getCompanyNumber());
		contents.setUserID(loginDetail.getUsername());

		// 등록
		try {
			contentsService.registerContents(multi, contents);
			sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("CONTENTS");
			syncService.updateSync(sync);
			info.put("result", "1");
		} catch (DataAccessException dae) {
			info.put("result", "2");
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 콘텐츠 수정
	@RequestMapping(value = "/contents/mod.do", method = RequestMethod.POST)
	@ResponseBody
	public String contentsMod(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Contents contents, Sync sync) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		MultipartRequest multi = new MultipartRequest(request, Security.getLocalImagePath(), 5242880, "utf-8");

		LoginDetail loginDetail = Security.getLoginDetail();
		contents.setComNum(loginDetail.getCompanyNumber());
		contents.setUserID(loginDetail.getUsername());
		contents.setConNum(Integer.parseInt(StringUtil.NVL(multi.getParameter("conNum"), "0")));
		Contents contentsInfo = contentsService.getContentsInfo(contents);
		// 이미지 삭제 정보
		contents.setImgSrc1(contentsInfo.getImgSrc1());
		contents.setImgSrc2(contentsInfo.getImgSrc2());
		contents.setImgSrc3(contentsInfo.getImgSrc3());
		contents.setImgSrc4(contentsInfo.getImgSrc4());
		contents.setImgSrc5(contentsInfo.getImgSrc5());
		contents.setSoundSrc1(contentsInfo.getSoundSrc1());
		contents.setSoundSrc2(contentsInfo.getSoundSrc2());
		contents.setSoundSrc3(contentsInfo.getSoundSrc3());

		if ((contentsInfo.getComNum() == loginDetail.getCompanyNumber())
				&& (contentsInfo.getUserID().equals(loginDetail.getUsername()))) {
			try {
				contentsService.modifyContents(multi, contents);
				sync.setComNum(loginDetail.getCompanyNumber());
				sync.setSyncType("CONTENTS");
				syncService.updateSync(sync);
				info.put("result", "1");
			} catch (DataAccessException dae) {
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

	// 콘텐츠 삭제
	@RequestMapping(value = "/contents/del.do", method = RequestMethod.POST)
	@ResponseBody
	public String contentsDel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Contents contents, Sync sync) throws ServletException, SftpException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		MultipartRequest multi = new MultipartRequest(request, Security.getLocalImagePath(), 5242880, "utf-8");

		String conNum = StringUtil.NVL(multi.getParameter("conNum"), "0");
		contents.setConNum(Integer.parseInt(conNum));
		boolean chk = false;

		// 콘텐츠 정보
		Contents contentsInfo = contentsService.getContentsInfo(contents);
		LoginDetail loginDetail = Security.getLoginDetail();

		if ((contentsInfo.getComNum() == loginDetail.getCompanyNumber())
				&& (contentsInfo.getUserID().equals(loginDetail.getUsername()))) {
			try {
				contentsService.removeContents(contents);
				sync.setComNum(loginDetail.getCompanyNumber());
				sync.setSyncType("CONTENTS");
				syncService.updateSync(sync);
				info.put("result", "1");
				chk = true;
			} catch (DataAccessException dae) {
				info.put("result", "2");
			} finally {
				if (chk == true) {
					// 삭제 후 정보가 없으므로 삭제 전의 정보를 참조
					contentsService.deleteContentsFile(contentsInfo, "img", "1", "");
					contentsService.deleteContentsFile(contentsInfo, "img", "2", "");
					contentsService.deleteContentsFile(contentsInfo, "img", "3", "");
					contentsService.deleteContentsFile(contentsInfo, "img", "4", "");
					contentsService.deleteContentsFile(contentsInfo, "img", "5", "");
					contentsService.deleteContentsFile(contentsInfo, "sound", "1", "");
					contentsService.deleteContentsFile(contentsInfo, "sound", "2", "");
					contentsService.deleteContentsFile(contentsInfo, "sound", "3", "");
				}
			}
		} else {
			info.put("result", "3");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/contents/mapping.json", method = RequestMethod.GET)
	@ResponseBody
	public String contentsMappingJson(Model model, HttpServletRequest request, HttpServletResponse response, Code code,
			ContentsMappingSearchParam param) throws ParseException, IOException {

		Map<String, Object> info = new HashMap<String, Object>();
		try {
			LoginDetail loginDetail = Security.getLoginDetail();
			param.setComNum(loginDetail.getCompanyNumber());

			code.setgCD("REFTYPE");
			List<?> refCD = codeService.getCodeListByCD(code);
			code.setgCD("REFTYPE2");
			List<?> refCD2 = codeService.getCodeListByCD(code);
			code.setgCD("REFSUBTYPE");
			List<?> refSubCD = codeService.getCodeListByCD(code);
			code.setgCD("CONTYPE");
			List<?> conCD = codeService.getCodeListByCD(code);

			int pageSize = 30;
			int blockSize = 10;
			param.initPage(pageSize, blockSize);
			Integer cnt = contentsService.getContentsMappingCount(param);
			List<?> list = contentsService.getContentsMappingList(param);
			list = contentsService.bindContentsMappingList(list, refCD, refCD2, refSubCD, conCD);
			Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
			pagination.queryString = request.getServletPath();
			pagination.queryString = param.getQueryString();
			String page = pagination.print();

			info.put("result", "1");
			info.put("cnt", cnt);
			info.put("list", list);
			info.put("pagination", page);
			info.put("page", param.getPage());
			info.put("param", param);
		} catch (Exception e) {
			info.put("result", "2");
			info.put("message", e.getMessage());
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/contents/mapping.do", method = RequestMethod.GET)
	public ModelAndView contentsMapping(Model model, HttpServletRequest request, HttpServletResponse response,
			Code code, ContentsMappingSearchParam param) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/mapping");
		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		code.setgCD("REFTYPE");
		List<?> refCD = codeService.getCodeListByCD(code);
		code.setgCD("REFTYPE2");
		List<?> refCD2 = codeService.getCodeListByCD(code);
		code.setgCD("REFSUBTYPE");
		List<?> refSubCD = codeService.getCodeListByCD(code);
		code.setgCD("CONTYPE");
		List<?> conCD = codeService.getCodeListByCD(code);

		mnv.addObject("refCD", refCD);
		mnv.addObject("refCD2", refCD2);
		mnv.addObject("refSubCD", refSubCD);
		mnv.addObject("conCD", conCD);

		param.setComNum(loginDetail.getCompanyNumber());

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = contentsService.getContentsMappingCount(param);
		List<?> list = contentsService.getContentsMappingList(param);
		list = contentsService.bindContentsMappingList(list, refCD, refCD2, refSubCD, conCD);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = request.getServletPath();
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

	/**
	 * 콘텐츠 일괄매핑
	 * 매핑타입 선택시 해당 매핑에 해당하는 DATA 정보
	 * @param model
	 * @param request
	 * @param response
	 * @param code
	 * @param mappingType
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = "/contents/mappingData.ajax.do", method = RequestMethod.POST)
	@ResponseBody
	public String mappingDataAjax(Model model, HttpServletRequest request, HttpServletResponse response, Code code,
			@RequestParam(value = "mappingType", required = false) String mappingType)
					throws ParseException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();

		Integer comNum = loginDetail.getCompanyNumber();

		List<ContentsMapping> mappingList = contentsService.bindMappingDataList(comNum, mappingType);

		try {
			info.put("result", "1");
			if (mappingList != null) {
				info.put("list", mappingList);
			}

		} catch (Exception e) {
			info.put("result", "2");

		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	/**
     * 컨텐츠 일괄 매핑 처리
     *
     * @param beaconNum
     * @param conNums
     * @return
     * @throws IOException
     */
     @RequestMapping(value="/contents/mapping.ajax.do", method=RequestMethod.POST)
     @ResponseBody
     public String mappingAjax(@RequestParam(value = "mappingType") String mappingType
    		 				,@RequestParam(value = "fcEventType") String fcEventType
    		 				,@RequestParam(value = "refNums") Long[] refNums
                           	,@RequestParam(value = "conNums") Long[] conNums
                           	,@RequestParam(value = "evnetNum") Integer evnetNum
                           	,ContentsMapping contentsMapping) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            contentsMapping.setConNums(conNums);
            contentsMapping.setRefType(mappingType);
            contentsMapping.setRefNums(refNums);
            contentsMapping.setRefSubType(fcEventType);
            contentsMapping.setEvtNum(evnetNum);

            contentsService.assignContentsReference(loginDetail, contentsMapping);

            String mappingTypeText = "";
            if(mappingType.endsWith("BC") || mappingType.endsWith("BCG")) {
            	mappingTypeText = "BEACON";
            }else {
            	mappingTypeText = "GEOFENCING";
            }

            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType(mappingTypeText);
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

	@RequestMapping(value="/contents/deleteMapping.ajax.do", method=RequestMethod.POST)
	@ResponseBody
	public String deleteMappingAjax(@RequestParam(value = "mappingInfoList") String[] mappingInfoList
    		 				,@RequestParam(value = "delType") String delType
                           	,ContentsMapping contentsMapping) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();

		String mappingTypeText = "";

        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            for(int i=0; i<mappingInfoList.length; i++) {
            	String mappingInfo = mappingInfoList[i];

        		String refType = contentsService.unassignContentsReference(mappingInfo, delType);

                if(refType.endsWith("BC") || refType.endsWith("BCG")) {
                	mappingTypeText = "BEACON";
                }else {
                	mappingTypeText = "GEOFENCING";
                }

        		Sync sync = new Sync();
                sync.setComNum(loginDetail.getCompanyNumber());
                sync.setSyncType(mappingTypeText);
                syncService.updateSync(sync);

            }

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

	@RequestMapping(value = "/contents/info/list.do", method = RequestMethod.GET)
	public ModelAndView contentsInfoList(Model model, HttpServletRequest request,
			ContentsSearchParam param, Code code) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/info/list");

		code.setgCD("CONTYPE");
		List<?> conCD = codeService.getCodeListByCD(code);
		mnv.addObject("conCD", conCD);

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		param.setConType2("CHD");

		// 일괄매핑(이벤트리스트)
		EventSearchParam eParam = new EventSearchParam();
        eParam.setComNum(loginDetail.getCompanyNumber());
        eParam.setPageSizeZero();
		List<?> eventList = eventService.getEventList(eParam);

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = contentsService.getContentsCount(param);
		List<?> list = contentsService.getContentsList(param);
		list = contentsService.bindContentsList(list, conCD);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("result", "1");
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("eventList", eventList);
		mnv.addObject("pagination", page);
		mnv.addObject("page", param.getPage());
		mnv.addObject("param", param);

		return mnv;
	}

	@RequestMapping(value = "/contents/info/form.do", method = RequestMethod.GET)
	public ModelAndView contentsInfoForm(Model model, HttpServletRequest request,
			ContentsTypeSearchParam param) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/info/form");

		List<?> contentsTypeList = contentsService.getContentsTypeList(param);
		logger.debug("contentsTypeList :", contentsTypeList);

		mnv.addObject("contentsTypeList", contentsTypeList);
		return mnv;
	}

	@RequestMapping(value = "/contents/info/mform.do", method = RequestMethod.GET)
	public ModelAndView contentsInfoMForm(Model model, HttpServletRequest request, Code code,
			Contents contents) throws ParseException {
		ModelAndView mnv = new ModelAndView();

		Integer conNum = Integer.parseInt(StringUtil.NVL(request.getParameter("conNum"), ""));

		LoginDetail loginDetail = Security.getLoginDetail();
		contents.setComNum(loginDetail.getCompanyNumber());
		contents.setConNum(conNum);

		// 콘텐츠 정보
		Contents contentsInfo = contentsService.getContentsInfo(contents);
		contentsInfo = contentsService.bindContents(contentsInfo);

		if ((contentsInfo.getComNum() == loginDetail.getCompanyNumber())
				&& (contentsInfo.getUserID().equals(loginDetail.getUsername()))) {
			code.setgCD("CONTYPE");
			List<?> conCD = codeService.getCodeListByCD(code);
			code.setgCD("EXPFLAG");
			List<?> expFlag = codeService.getCodeListByCD(code);

			mnv.addObject("conCD", conCD);
			mnv.addObject("expFlag", expFlag);
			mnv.addObject("contentsInfo", contentsInfo);
			mnv.setViewName("/contents/info/mform");
		} else {
			mnv.setViewName("notpriv");
		}
		return mnv;
	}

	@RequestMapping(value = "/contents/info/component.ajax.do", method = RequestMethod.POST)
	@ResponseBody
	public String contentsInfoComponentAjax(Model model, HttpServletRequest request, ContentsTypeComponent contentsTypeComponent,
			@RequestParam(value = "typeNum", required = true) Integer typeNum)
					throws ParseException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();

		Integer comNum = loginDetail.getCompanyNumber();

		contentsTypeComponent.setTypeNum(typeNum);
		List<?> contentsTypeComponentList = contentsService.getContentsTypeComponentList(contentsTypeComponent);

		try {
			info.put("result", "1");
			info.put("list", contentsTypeComponentList);
		} catch (Exception e) {
			info.put("result", "2");

		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value="/contents/type/list.do", method=RequestMethod.GET)
	public ModelAndView contentsTypeList(Model model, HttpServletRequest request,
			ContentsTypeSearchParam param, Code code) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/type/list");

		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());

		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = contentsService.getContentsTypeCount(param);
		List<?> list = contentsService.getContentsTypeList(param);
//		list = contentsService.bindContentsList(list, conCD);
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

	@RequestMapping(value="/contents/type/form.do", method=RequestMethod.GET)
	public ModelAndView contentsTypeForm(Model model, Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/contents/type/form");
		code.setgCD("COMPTYPE");
		List<?> compTypeCD = codeService.getCodeListByCD(code);
		mnv.addObject("compTypeCD", compTypeCD);
		return mnv;
	}

	@RequestMapping(value = "/contents/type/mform.do", method = RequestMethod.GET)
	public ModelAndView contentsTypeMForm(Model model, Code code,
			ContentsType contentsType, ContentsTypeComponent contentsTypeComponent) throws ParseException {
		ModelAndView mnv = new ModelAndView();

		LoginDetail loginDetail = Security.getLoginDetail();
		contentsType.setComNum(loginDetail.getCompanyNumber());

		code.setgCD("COMPTYPE");
		List<?> compTypeCD = codeService.getCodeListByCD(code);

		ContentsType contentsTypeInfo = contentsService.getContentsTypeInfo(contentsType);
		contentsTypeComponent.setTypeNum(contentsTypeInfo.getTypeNum());
		List<?> contentsTypeComponentList = contentsService.getContentsTypeComponentList(contentsTypeComponent);

		if(contentsTypeInfo.getComNum() == loginDetail.getCompanyNumber()) {
			mnv.addObject("compTypeCD", compTypeCD);
			mnv.addObject("contentsTypeInfo", contentsTypeInfo);
			mnv.addObject("contentsTypeComponentList", contentsTypeComponentList);
			mnv.setViewName("/contents/type/mform");
		} else {
			mnv.setViewName("notpriv");
		}
		return mnv;
	}

	@RequestMapping(value="/contents/type/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String contentsTypeReg(HttpServletRequest request,
			@RequestParam(value="field") String[] fieldList,
			@RequestParam(value="fieldName") String[] fieldNameList,
			@RequestParam(value="fieldType") String[] fieldTypeList,
			@RequestParam(value="fieldJson") String fieldJson,
			ContentsType contentsType, ContentsTypeComponent contentsTypeComponent, Sync sync) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		contentsType.setComNum(loginDetail.getCompanyNumber());

		// 등록
		try {
			List<ContentsTypeComponent> contentsTypeComponentlist = new ArrayList<ContentsTypeComponent>();
			for(int i=0; i<fieldList.length; i++) {
				ContentsTypeComponent component = new ContentsTypeComponent();
				component.setCompField(fieldList[i]);
				component.setCompName(fieldNameList[i]);
				component.setCompType(fieldTypeList[i]);
				component.setOrderNum(i);
				contentsTypeComponentlist.add(i, component);
			}
			contentsService.registerContentsType(contentsType, contentsTypeComponentlist);

			// fieldJson 파싱해서 사용할 경우
			/*
			JsonNode node = JsonUtil.toNode(fieldJson);
			Iterator<JsonNode> nodeList = node.getElements();
			int j = 0;
        	while (nodeList.hasNext()) {
    			JsonNode fieldTypeNode = nodeList.next();
    			Iterator<JsonNode> fieldTypeList = fieldTypeNode.getElements();
    			while (fieldTypeList.hasNext()) {
    				JsonNode fieldIDNode = fieldTypeList.next();
    				String id = fieldIDNode.getTextValue();

    				ContentsTypeComponent componentInfo = list.get(j);
    				componentInfo.setCompType(id);
    				contentsService.registerContentsTypeComponent(componentInfo);
    			}
    			j++;
    		}
    		*/
			sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("CONTENTS");
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

	@RequestMapping(value="/contents/type/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String contentsTypeMod(HttpServletRequest request,
			@RequestParam(value="field") String[] fieldList,
			@RequestParam(value="fieldName") String[] fieldNameList,
			@RequestParam(value="fieldNum") Integer[] fieldNumList,
			@RequestParam(value="fieldJson") String fieldJson,
			ContentsType contentsType, ContentsTypeComponent contentsTypeComponent, Sync sync) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		LoginDetail loginDetail = Security.getLoginDetail();
		contentsType.setComNum(loginDetail.getCompanyNumber());

		// 수정
		try {
			List<ContentsTypeComponent> contentsTypeComponentlist = new ArrayList<ContentsTypeComponent>();
			for(int i=0; i<fieldList.length; i++) {
				ContentsTypeComponent component = new ContentsTypeComponent();
				component.setCompField(fieldList[i]);
				component.setCompName(fieldNameList[i]);
				component.setCompNum(fieldNumList[i]);
				component.setOrderNum(i);
				contentsTypeComponentlist.add(i, component);
			}
			contentsService.modifyContentsType(contentsType, contentsTypeComponentlist);

			sync.setComNum(loginDetail.getCompanyNumber());
			sync.setSyncType("CONTENTS");
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

	@RequestMapping(value = "/contents/type/del.do", method = RequestMethod.POST)
	@ResponseBody
	public String contentsTypeDel(HttpServletRequest request,
			ContentsType contentsType, Sync sync) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		ContentsType contentsTypeInfo = contentsService.getContentsTypeInfo(contentsType);
		LoginDetail loginDetail = Security.getLoginDetail();

		if (contentsTypeInfo.getComNum() == loginDetail.getCompanyNumber()) {
			try {
				contentsService.removeContentsType(contentsType);
				sync.setComNum(loginDetail.getCompanyNumber());
				sync.setSyncType("CONTENTS");
				syncService.updateSync(sync);
				info.put("result", "1");
			} catch (DataAccessException dae) {
				info.put("result", "2");
			}
		} else {
			info.put("result", "3");
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}
}