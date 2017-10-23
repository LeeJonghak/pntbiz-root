package wms.stat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import core.wms.stat.domain.StatBeaconSearchParam;
import wms.stat.service.StatService;
import wms.component.auth.LoginDetail;
import framework.web.util.DateUtil;

@Controller
public class StatController {		
	
	@Autowired
	private StatService statService;
	
	@RequestMapping(value="/stat/stat.do", method=RequestMethod.GET)
	public ModelAndView stat(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/stat/stat");
		return mnv;
	}
	
	@RequestMapping(value="/stat/report.do", method=RequestMethod.GET)
	public ModelAndView report(Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/stat/report");
		return mnv;
	}
	
	@RequestMapping(value="/stat/beacon/monitor.do", method=RequestMethod.GET)
	public ModelAndView beaconMonitor(Model model, HttpServletRequest request, HttpServletResponse response, 
			StatBeaconSearchParam param) throws Exception {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/stat/beacon/monitor");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		
		// 비콘 총 갯수
		int totalCnt = statService.statBeaconMonitorCount(param);
		// 측위용 비콘 총 갯수
		int preCnt = statService.statBeaconPresenceCount(param);		
		// 망실가능성 높은 비콘
		param.setBeaconName(null);
		String lastDate = DateUtil.getAddDate(DateUtil.getDate("yyyy-MM-dd 00:00:00"), -20, "yyyy-MM-dd HH:mm:ss");
		param.setLastDate(lastDate);
		int lossCnt = statService.statBeaconMonitorCount(param);
		// 망실가능성 높은 비콘리스트
		List<?> list = statService.statBeaconMonitorList(param);
		
		mnv.addObject("totalCnt", totalCnt);
		mnv.addObject("preCnt", preCnt);
		mnv.addObject("lossCnt", lossCnt);	
		mnv.addObject("lastDate", lastDate);
		mnv.addObject("list", list);
		
		return mnv;
	}	
	
	
}