package admin.login.controller;

import core.admin.auth.dao.LoginRoleAuthoritiesDao;
import core.admin.auth.domain.LoginRole;
import core.admin.auth.domain.LoginRoleAuthorities;
import admin.auth.service.LoginAuthcodeService;
import admin.auth.service.LoginRoleService;
import admin.auth.service.LoginService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginRoleController {

    @Autowired
    private LoginRoleService loginRoleService;

    @Autowired
    private LoginAuthcodeService loginAuthcodeService;

    @Autowired
    private LoginRoleAuthoritiesDao loginRoleAuthoritiesDao;

    @Autowired
    private LoginService loginService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="/admin/login/role/list.do", method= RequestMethod.GET)
    public ModelAndView list(ModelAndView mnv, HttpServletRequest request, PagingParam param) throws IOException, ParseException {
        mnv.setViewName("/admin/login/role/list");

        //1.get List
        param.setPageSize(30);
        Integer cnt = loginRoleService.getLoginRoleCount(param);
        List<?> list = loginRoleService.getLoginRoleList(param);
        list = loginRoleService.bindLoginRoleList(list);

        //2.set page
        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString  = request.getServletPath();
        String page = pagination.print();

        //3.set return param
        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        mnv.addObject("page", page);
        mnv.addObject("param", param);

        return mnv;
    }

    @RequestMapping(value="/admin/login/role/form.do", method=RequestMethod.GET)
    public ModelAndView form(ModelAndView mnv) {
        mnv.setViewName("/admin/login/role/form");

        /**
         * 사용 가능한 권한 목록
         */
        List<?> authList = loginAuthcodeService.getLoginAuthcodeListAll();
        mnv.addObject("authList", authList);

        return mnv;
    }

    @RequestMapping(value="/admin/login/role/mform.do", method=RequestMethod.GET)
    public ModelAndView mform(ModelAndView mnv, @RequestParam(value = "roleNum", required = true)int roleNum) {
        mnv.setViewName("/admin/login/role/mform");

        LoginRole param = new LoginRole();
        param.setRoleNum(roleNum);

        mnv.addObject("loginRole", loginRoleService.getLoginRoleInfo(param));		//역할 정보
        mnv.addObject("authList", loginAuthcodeService.getLoginAuthcodeListAll());	//사용 가능한 권한 목록

        /**
         * 할당된 권한 목록
         */
        LoginRoleAuthorities loginRoleAuthorities = new LoginRoleAuthorities();
        loginRoleAuthorities.setRoleNum(roleNum);
        List<?> assignedAuthList = loginRoleAuthoritiesDao.getLoginRoleAuthorities(loginRoleAuthorities);
        List<Integer> assignedAuthNumList = new ArrayList<Integer>();

        for(Object loginRoleAuth: assignedAuthList){
        	assignedAuthNumList.add(((LoginRoleAuthorities) loginRoleAuth).getAuthNum());
        }
        mnv.addObject("assignedAuthNumList", assignedAuthNumList);

        return mnv;
    }

    @RequestMapping(value="/admin/login/role/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String reg(LoginRole loginRole, @RequestParam(value = "admin/auth", required = false)int[] authNums) throws IOException {
    	Map<String, String> info = new HashMap<String, String>();
        try {
            loginRoleService.registerLoginRole(loginRole, authNums);
            info.put("result", "1");
        } catch(Exception exception) {
            logger.error("exception", exception);
            info.put("result", "2");
        } finally {
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/admin/login/role/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String mod(LoginRole loginRole, @RequestParam(value = "admin/auth", required = false)int[] authNums) throws IOException {
    	Map<String, String> info = new HashMap<String, String>();
        LoginRole chk = loginRoleService.getLoginRoleInfo(loginRole);
        if(chk==null) {
        	info.put("result", "3");
        } else {
            try {
                loginRoleService.modifyLoginRole(loginRole, authNums);
                info.put("result", "1");
            } catch(DataAccessException Mod) {
                logger.error("exception", Mod);
                info.put("result", "2");
            } catch (Exception e) {
                logger.error("exception", e);
                info.put("result", "2");
            } finally {
            }
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value="/admin/login/role/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String del(LoginRole loginRole) throws IOException {
        Map<String, String> info = new HashMap<String, String>();
        /**
         * 삭제하려는 역할이 로그인 계정에 할당되어 있는지 확인
         */
        Integer cnt = loginService.getLoginRoleCount(loginRole);
        if(cnt>0) {
            info.put("result", "3");
        } else {
            try {
                loginRoleService.removeLoginRole(loginRole);
                info.put("result", "1");
            } catch (DataAccessException dae) {
                info.put("result", "2");
            } finally {
            }
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }
}
