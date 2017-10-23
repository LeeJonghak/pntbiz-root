package wms.service.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import wms.service.ServiceConstants;
import core.wms.service.domain.ContentsEventParamVO;
import core.wms.service.domain.EventResVO;
import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;
import wms.service.service.ServiceEventService;
import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;

import wms.component.auth.LoginDetail;

import wms.contents.service.ContentsService;
import framework.web.util.Pagination;

@Controller
public class ServiceBeaconController {		
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEventController.class);
	
	@Autowired
	private ServiceEventService service;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private ContentsService contentsService;
	
	/**
	 * 서비스 > 비콘푸쉬이벤트관리 > 이벤트리스트
	 * 이벤트 리스트
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/beacon/list.do", method=RequestMethod.GET)
	public String serviceEventList(Model model, @ModelAttribute ServiceEventVO param, 
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		param.setSendType(ServiceConstants.SEND_TYPE_BEACON_PUSH);
		List<?> list = service.retrieveBeaconEventList(param);
		
		Pagination pagination = new Pagination(param.getPage(), param.getTotalCount(), param.getPageSize(), param.getBlockSize());
		pagination.queryString  = param.queryString();
		String page = pagination.print();
		
		model.addAttribute("cnt", param.getTotalCount());
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("param", param);
		
		logger.info("list {}", list);
		
		return "/service/beacon/list";
	}
	
	/**
	 * 서비스 > 비콘푸쉬이벤트관리 > 이벤트등록 화면
	 * 이벤트 등록 화면
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/beacon/form.do", method=RequestMethod.GET)
	public ModelAndView serviceEventForm(Model model, ServiceEventVO param, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
		try{
			EventResVO detail = service.retrievEventItem(param);
			model.addAttribute("item", detail);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		mnv.setViewName("/service/beacon/form");
		return mnv;
	}
	
	/**
	 * 서비스 > 비콘푸쉬이벤트관리 > 이벤트등록 처리
	 * 이벤트 등록 처리
	 *  
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/beacon/form.do", method=RequestMethod.POST)
	public ModelAndView event_create(Model model, ServiceEventVO param, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
		try{
			service.createEventItem(param);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}

		if ( ServiceConstants.RET_CODE_SUCCESS.equals(param.getRetCode())){
			mnv.setViewName("redirect:/service/beacon/list.do");
			return mnv;
		}else{
			mnv.setViewName("redirect:/service/beacon/list.do");
			return mnv;
		}
	}
	
	/**
	 * 서비스 > 비콘푸쉬이벤트관리 > 이관하기 화면
	 * 이관하기 화면
	 *  
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/beacon/mform.do", method=RequestMethod.GET)
	public ModelAndView serviceEventMForm(Model model, ServiceEventVO param, Code code, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
		EventResVO detail = null;
		try{
			detail = service.retrievEventItem(param);
			model.addAttribute("item", detail);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		code.setgCD("GFBPZONE");
		List<?> conCD = codeService.getCodeListByCD(code);
		model.addAttribute("conCD", conCD);
		
		String contsType = detail.getContsType();
		if ( ServiceConstants.CONTS_TYPE_SEAT_UPGRADE.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SEAT_UPGRADE_SNS.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SEAT_UPGRADE_YOUTUBE.equals(contsType) ){

			//좌석업그레이드
			mnv.setViewName("/service/beacon/mform_seat");
			
		}else if ( ServiceConstants.CONTS_TYPE_SCRATCH.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SCRATCH_SNS.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SCRATCH_YOUTUBE.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_COUPON.equals(contsType)){

			//현장이벤트, 쿠폰
			mnv.setViewName("/service/beacon/mform");
		}else{
			//컨텐츠
			mnv.setViewName("/service/beacon/mform_contents");
		}
		
		return mnv;
	}
	
	/**
	 * 서비스 > 비콘푸쉬이벤트관리 > 이관하기 처리
	 * 이관하기 처리
	 * 
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/beacon/mform.do", method=RequestMethod.POST)
	public ModelAndView translation_push(Model model, EventTranslationVO param, Code code, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
//		LoginDetail loginDetail = CommonUtil.getLoginDetail();
		
		try{
			param.setSendType(ServiceConstants.SEND_TYPE_BEACON_PUSH);
			param.setUseYn(1);
			param.setIsDeleted(0);
			param.setCreatedNo(0);
			
			service.translationPushStageInfo(param);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		if ( ServiceConstants.RET_CODE_SUCCESS.equals(param.getRetCode())){
			mnv.setViewName("redirect:/service/push/info/list.do");
			return mnv;
		}else{
			mnv.setViewName("redirect:/service/beacon/mform.do?eventId=" + param.getEventId());
			return mnv;
		}
	}
	
	/**
	 * 서비스 > 비콘푸쉬이벤트관리 > 이벤트 등록 > 컨텐츠조회 팝업
	 * 컨텐츠 조회 목록
	 * 
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/beacon/popup/content_list.do", method=RequestMethod.GET)
	public ModelAndView popupContentList(Model model, ContentsEventParamVO param, Code code, 
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/service/beacon/contents_list");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		
		code.setgCD("CONTYPE");
		List<?> conCD = codeService.getCodeListByCD(code);
		
		param.setEventType("03");
		Integer cnt = service.getContentsCount(param);
		List<?> list = service.getContentsList(param);
		list = contentsService.bindContentsList(list, conCD);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();
		
		mnv.addObject("conCD", conCD);
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		logger.debug("list {}", list);
		
		return mnv;
	}
}