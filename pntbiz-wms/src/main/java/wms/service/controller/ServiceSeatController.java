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
import wms.service.service.ServiceSeatService;
import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;
import wms.component.auth.LoginDetail;
import wms.contents.service.ContentsService;
import framework.web.util.Pagination;

@Controller
public class ServiceSeatController {		
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEventController.class);
	
	@Autowired
	private ServiceSeatService service;
	
	@Autowired
	private ServiceEventService eventService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private ContentsService contentsService;
	
	/**
	 * 서비스 > 좌석업그레이드 관리 > 이벤트 리스트
	 * 이벤트 리스트
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/seat/list.do", method=RequestMethod.GET)
	public String serviceEventList(Model model, @ModelAttribute ServiceEventVO param, 
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		param.setSendType(ServiceConstants.SEND_TYPE_APPLICATION_PUSH);
		List<?> list = service.retrieveListEvent(param);
		
		Pagination pagination = new Pagination(param.getPage(), param.getTotalCount(), param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.queryString();
		String page = pagination.print();
		
		model.addAttribute("cnt", param.getTotalCount());
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("param", param);
		
		logger.info("list {}", list);
		
		return "/service/seat/list";
	}
	
	/**
	 * 서비스 > 좌석업그레이드 관리 > 이벤트 등록화면
	 * 이벤트 등록화면
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/seat/form.do", method=RequestMethod.GET)
	public ModelAndView serviceEventForm(Model model, ServiceEventVO param, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
		try{
			EventResVO detail = service.retrievEventItem(param);
			model.addAttribute("item", detail);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		mnv.setViewName("/service/seat/form");
		return mnv;
	}
	
	/**
	 * 서비스 > 좌석업그레이드 관리 > 이벤트 등록 처리
	 * 이벤트 등록처리
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/seat/form.do", method=RequestMethod.POST)
	public ModelAndView event_create(Model model, ServiceEventVO param, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
		try{
			service.createEventItem(param);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}

		if ( ServiceConstants.RET_CODE_SUCCESS.equals(param.getRetCode())){
			mnv.setViewName("redirect:/service/seat/list.do");
			return mnv;
		}else{
			mnv.setViewName("redirect:/service/seat/list.do");
			return mnv;
		}
	}
	
	/**
	 * 서비스 > 좌석업그레이드 관리 > 이벤트 이관 화면
	 * 이관화면
	 * 
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/seat/mform.do", method=RequestMethod.GET)
	public ModelAndView serviceEventMForm(Model model, ServiceEventVO param, Code code, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mnv = new ModelAndView();
		
		try{
			EventResVO detail = service.retrievEventItem(param);
			model.addAttribute("item", detail);
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		code.setgCD("GFBPZONE");
		List<?> conCD = codeService.getCodeListByCD(code);
		model.addAttribute("conCD", conCD);		
		
		List<?> blocklst = eventService.getGeofencingCodeList();
		model.addAttribute("blocklst", blocklst);
		
		mnv.setViewName("/service/seat/mform");
		return mnv;
	}
	
	/**
	 * 서비스 > 좌석업그레이드 관리 > 이벤트 이관처리
	 * 이벤트 이관처리
	 * 
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/seat/mform.do", method=RequestMethod.POST)
	public ModelAndView translation_push(Model model, EventTranslationVO param, Code code, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		
//		LoginDetail loginDetail = CommonUtil.getLoginDetail();
		
		try{
			param.setSendType(ServiceConstants.SEND_TYPE_APPLICATION_PUSH);
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
			mnv.setViewName("redirect:/service/seat/mform.do?eventId=" + param.getEventId());
			return mnv;
		}
	}
	
	/**
	 * 서비스 > 좌석업그레이드 관리 > 이벤트 등록 > 팝업 컨텐츠 조회
	 * 컨텐츠 리스트
	 * 
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/seat/popup/content_list.do", method=RequestMethod.GET)
	public ModelAndView popupContentList(Model model, ContentsEventParamVO param, Code code, 
											HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/service/seat/contents_list");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		
		code.setgCD("CONTYPE");
		List<?> conCD = codeService.getCodeListByCD(code);
		
		param.setEventType("02");
		Integer cnt = eventService.getContentsCount(param);
		List<?> list = eventService.getContentsList(param);
		list = contentsService.bindContentsList(list, conCD);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();
		
		mnv.addObject("conCD", conCD);
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		logger.info("list {}", list);
		
		return mnv;
	}
}