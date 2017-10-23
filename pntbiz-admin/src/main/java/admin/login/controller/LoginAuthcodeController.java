package admin.login.controller;

import core.admin.auth.domain.LoginAuthcode;
import admin.auth.service.LoginAuthcodeService;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.PagingParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginAuthcodeController {

    @Autowired
    private LoginAuthcodeService loginAuthcodeService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="/admin/login/authcode/list.do", method= RequestMethod.GET)
    public ModelAndView list(ModelAndView mnv, HttpServletRequest request, PagingParam param) throws IOException, ParseException {
        mnv.setViewName("/admin/login/authcode/list");

        param.setPageSize(30);
        Integer cnt = loginAuthcodeService.getLoginAuthcodeCount(param);
        List<?> list = loginAuthcodeService.getLoginAuthcodeList(param);
        list = loginAuthcodeService.bindLoginAuthcodeList(list);

        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString  = request.getServletPath();
        String page = pagination.print();

        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        mnv.addObject("page", page);
        mnv.addObject("param", param);
        logger.info("list {}", list);

        return mnv;
    }

    @RequestMapping(value="/admin/login/authcode/form.do", method=RequestMethod.GET)
    public String form() {
        return "/admin/login/authcode/form";
    }

    @RequestMapping(value="/admin/login/authcode/mform.do", method=RequestMethod.GET)
    public ModelAndView mform(ModelAndView mnv, @RequestParam(value = "authNum", required = true)int authNum) {
        mnv.setViewName("/admin/login/authcode/mform");

        LoginAuthcode param = new LoginAuthcode();
        param.setAuthNum(authNum);

        LoginAuthcode auth = loginAuthcodeService.getLoginAuthcodeInfo(param);
        mnv.addObject("admin/auth", auth);
        logger.info("authcodeInfo {}", auth);

        return mnv;
    }

    @RequestMapping(value="/admin/login/authcode/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String reg(LoginAuthcode loginAuthcode) throws IOException {
    	Map<String, String> info = new HashMap<String, String>();
        int checkcnt = loginAuthcodeService.getLoginAuthcodeCheck(loginAuthcode.getAuthCode());
        if(checkcnt>0) {
        	info.put("result", "3");
        } else {
            try {
                loginAuthcodeService.registerLoginAuthcode(loginAuthcode);
                info.put("result", "1");
            } catch(DataAccessException dae) {
            	info.put("result", "2");
            } finally {
            }
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/admin/login/authcode/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String mod(LoginAuthcode loginAuthcode) throws IOException{
    	Map<String, String> info = new HashMap<String, String>();

        LoginAuthcode chk = loginAuthcodeService.getLoginAuthcodeInfo(loginAuthcode);
        if(chk==null) {
        	info.put("result", "3");
        } else {
            try {
                loginAuthcodeService.modifyLoginAuthcode(loginAuthcode);
                info.put("result", "1");
            } catch(DataAccessException Mod) {
            	info.put("result", "2");
            } finally {
            }
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/admin/login/authcode/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String del(LoginAuthcode loginAuthcode) throws IOException {
        Map<String, String> info = new HashMap<String, String>();
        try {
            loginAuthcodeService.removeLoginAuthcode(loginAuthcode);
            info.put("result", "1");
        } catch(DataAccessException dae) {
            info.put("result", "2");
        } finally {
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

}
