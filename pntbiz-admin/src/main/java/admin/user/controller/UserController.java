package admin.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import admin.code.service.CodeService;
import admin.user.service.UserInfoService;
import admin.user.service.UserService;

@Controller
public class UserController {	
	
	@Autowired
	private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeService codeService;
	
	/*@RequestMapping(value="/user/list.do", method=RequestMethod.GET)
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			UserSearchParam param, Code code) throws IOException, ParseException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/user/list");
		
		code.setgCD("GENDER");
		List<?> genderCD = codeService.getCodeListByCD(code);
		code.setgCD("AGREE");
		List<?> agreeCD = codeService.getCodeListByCD(code);
		
		LoginDetail loginDetail = CommonUtil.getLoginDetail();
		param.setComNum(loginDetail.getCompanyNumber());
		
		int pageSize = 30;
		int blockSize = 10;
		param.initPage(pageSize, blockSize);
		Integer cnt = userService.getUserCount(param);
		List<?> list = userService.getUserList(param);
		list = userService.bindUserList(list);
		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());		
		pagination.linkPage = request.getServletPath();
		pagination.queryString = param.getQueryString();
		String page = pagination.print();
		
		mnv.addObject("genderCD", genderCD);
		mnv.addObject("agreeCD", agreeCD);
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);		
		logger.info("page {}", page);
		
		return mnv;
	}*/

//    @RequestMapping(value="/user/list.do", method=RequestMethod.GET)
//    public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
//                             UserSearchParam param, Code code,
//                             @RequestParam(value = "opt", required = false)String opt,
//                             @RequestParam(value = "keyword", required = false)String keywrod,
//                             @RequestParam(value = "gender", required = false)String gender) throws IOException, ParseException{
//
//        ModelAndView mnv = new ModelAndView();
//        mnv.setViewName("/user/userinfo-list");
//
//        code.setgCD("GENDER");
//        List<?> genderCD = codeService.getCodeListByCD(code);
//        code.setgCD("AGREE");
//        List<?> agreeCD = codeService.getCodeListByCD(code);
//
//        LoginDetail loginDetail = CommonUtil.getLoginDetail();
//        param.setComNum(loginDetail.getCompanyNumber());
//
//        int pageSize = 30;
//        int blockSize = 10;
//        param.initPage(pageSize, blockSize);
//        UserInfo userInfoParam = new UserInfo();
//        userInfoParam.setFirstItemNo(param.getFirstItemNo());
//        userInfoParam.setPageSize(param.getPageSize());
//        if(StringUtils.isNotEmpty(gender)) {
//            userInfoParam.setGENDER("m".equals(gender)? "남" : "여");
//        }
//
//        if(StringUtils.isNotEmpty(opt) && StringUtils.isNotEmpty(keywrod)) {
//            userInfoParam.setOpt(opt);
//            userInfoParam.setKeyword(keywrod);
//        }
//        Integer cnt = userInfoService.getUserInfoCount(userInfoParam);
//        List<?> list = userInfoService.getUserInfoList(userInfoParam);
//        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
//        pagination.linkPage = request.getServletPath();
//        pagination.queryString = param.getQueryString();
//        String page = pagination.print();
//
//        mnv.addObject("genderCD", genderCD);
//        mnv.addObject("agreeCD", agreeCD);
//        mnv.addObject("cnt", cnt);
//        mnv.addObject("list", list);
//        mnv.addObject("page", page);
//        mnv.addObject("param", param);
//        logger.info("page {}", page);
//
//        return mnv;
//    }
	
	// 회원목록
//	@RequestMapping(value="/user/list.do", method=RequestMethod.GET)
//	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
//			UserSearchParam param, Code code) throws IOException, ParseException{
//		ModelAndView mnv = new ModelAndView();
//		mnv.setViewName("/user/list");
//		
//		code.setgCD("USER");
//		List<?> userCD = codeService.getCodeListByUser(code);
//		code.setgCD("GENDER");
//		List<?> genderCD = codeService.getCodeListByUser(code);
//		
//		//User user = (User) session.getAttribute("loginUserInfo");
//		LoginDetail loginDetail = CommonUtil.getLoginDetail();
//		param.setComNum(loginDetail.getCompanyNumber());
//		
//		int pageSize = 30;
//		int blockSize = 10;
//		param.initPage(pageSize, blockSize);
//		Integer cnt = userService.getUserCount(param);
//		List<?> list = userService.getUserList(param);
//		list = userService.bindUserList(list, userCD, genderCD);
//		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());		
//		pagination.linkPage = request.getServletPath();
//		pagination.queryString = param.getQueryString();
//		String page = pagination.print();
//		
//		mnv.addObject("cnt", cnt);
//		mnv.addObject("list", list);
//		mnv.addObject("page", page);
//		mnv.addObject("param", param);
//		logger.info("page {}", page);
//		return mnv;
//	}
	
}