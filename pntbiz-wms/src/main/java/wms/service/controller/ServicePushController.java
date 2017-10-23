package wms.service.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import core.wms.service.domain.PushResVO;
import core.wms.service.domain.PushSendVO;
import core.wms.service.domain.ServiceEventVO;
import wms.service.service.ServiceEventService;
import wms.service.service.ServicePushService;
import core.common.code.domain.Code;
import wms.admin.code.service.CodeService;
import framework.web.util.Pagination;

@Controller
public class ServicePushController {		
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEventController.class);
	
	@Autowired
	private ServicePushService pushService;
	
	@Autowired
	private ServiceEventService eventService;
	
	@Autowired
	private CodeService codeService;
	
	/***
	 * 푸쉬발송대기목록
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/service/push/info/list.do", method=RequestMethod.GET)
	public ModelAndView servicePushInfoList(Model model, @ModelAttribute ServiceEventVO param,
													HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mnv = new ModelAndView();
		
		param.setSendType(ServiceConstants.SEND_TYPE_ALL);
		
		List<?> list = pushService.retrievePushStageList(param);
		
		Pagination pagination = new Pagination(param.getPage(), param.getTotalCount(), param.getPageSize(), param.getBlockSize());
		pagination.queryString  = param.queryString();
		String page = pagination.print();
		
		model.addAttribute("cnt", param.getTotalCount());
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("param", param);
		
		logger.debug("list {}", list);
		
		mnv.setViewName("/service/push/info/list");
		return mnv;
	}
	
	/**
	 * push 발송하기 페이지
	 * 
	 * @param model
	 * @param param
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/push/info/form.do", method=RequestMethod.GET)
	public ModelAndView servicePushInfoForm(Model model, ServiceEventVO param, Code code, 
														HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mnv = new ModelAndView();
		
		PushResVO detail = pushService.retrievePushStageDetail(param);
		model.addAttribute("item", detail);
		
		code.setgCD("GFBPZONE");
		List<?> conCD = codeService.getCodeListByCD(code);
		mnv.addObject("conCD", conCD);
		
		List<?> blocklst = eventService.getGeofencingCodeList();
		model.addAttribute("blocklst", blocklst);
		
		String contsType = detail.getContsType();
		
		if ( ServiceConstants.CONTS_TYPE_SEAT_UPGRADE.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SEAT_UPGRADE_SNS.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SEAT_UPGRADE_YOUTUBE.equals(contsType) ){

			//좌석업그레이드
			mnv.setViewName("/service/push/info/form_seat");
			
		}else if ( ServiceConstants.CONTS_TYPE_SCRATCH.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SCRATCH_SNS.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_SCRATCH_YOUTUBE.equals(contsType)
				|| ServiceConstants.CONTS_TYPE_COUPON.equals(contsType)){

			//현장이벤트, 쿠폰
			mnv.setViewName("/service/push/info/form");
		}else{
			//컨텐츠
			mnv.setViewName("/service/push/info/form_contents");
		}
		
		return mnv;
	}
	
	/**
	 * push 발송 저장
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/push/info/form.do", method=RequestMethod.POST)
	public ModelAndView proc_sendpush(Model model, @ModelAttribute PushSendVO param,
														HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mnv = new ModelAndView();
		
		try{
			param.setUseYn(1);
			param.setIsDeleted(0);
			param.setCreatedNo(0);
			
			pushService.sendPush(param);
			
		}catch( Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		if ( ServiceConstants.RET_CODE_SUCCESS.equals(param.getRetCode())){
			mnv.setViewName("redirect:/service/push/rule/list.do");
			return mnv;
		}else{
			mnv.setViewName("redirect:/service/push/info/list.do");
			return mnv;
		}
		
	}
	
	/**
	 * 서비스 > 푸쉬발송결과관리
	 * 푸쉬발송결과 list
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/push/rule/list.do", method=RequestMethod.GET)
	public ModelAndView servicePushRuleList(Model model, @ModelAttribute ServiceEventVO param,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ModelAndView mnv = new ModelAndView();
		
		param.setSendType(ServiceConstants.SEND_TYPE_ALL);
		
		List<?> list = pushService.retrievePushResultList(param);
		
		Pagination pagination = new Pagination(param.getPage(), param.getTotalCount(), param.getPageSize(), param.getBlockSize());
		pagination.queryString  = param.queryString();
		String page = pagination.print();
		
		model.addAttribute("cnt", param.getTotalCount());
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("param", param);
		
		logger.debug("list {}", list);
		mnv.setViewName("/service/push/rule/list");
		return mnv;
	}
	
	/**
	 * 서비스 > 푸쉬발송결과관리
	 * 상세보기
	 * 
	 * @param model
	 * @param param
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/service/push/rule/form.do", method=RequestMethod.GET)
	public ModelAndView servicePushRuleForm(Model model, @ModelAttribute ServiceEventVO param, HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mnv = new ModelAndView();
		
		List<?> list = pushService.retrievePushResultDtlList(param);
		
		Pagination pagination = new Pagination(param.getPage(), param.getTotalCount(), param.getPageSize(), param.getBlockSize());
		pagination.queryString  = param.queryString() + "&pnsId=" + param.getPnsId();
		String page = pagination.print();
		logger.debug("결과 : " + page);
		
		model.addAttribute("cnt", param.getTotalCount());
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("param", param);
		
		mnv.setViewName("/service/push/rule/form");
		return mnv;
	}
	
}