package custom.Sample.scanner.controller;

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.Pagination;
import wms.map.service.FloorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import core.wms.scanner.domain.ScannerSearchParam;
import wms.scanner.service.ScannerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
public class SampleScannerController {

	@Autowired
	private ScannerService scannerService;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private FloorService floorService;

	@RequestMapping(value= "/scanner/list.do", method=RequestMethod.GET)
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			ScannerSearchParam param, Company company) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/scanner/scanner/list");
		
		LoginDetail loginDetail = Security.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		
		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = scannerService.getScannerCount(param);
		List<?> list = scannerService.getScannerList(param);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		//pagination.linkPage = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();
		
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("pagination", page);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		
		return mnv;
	}
}