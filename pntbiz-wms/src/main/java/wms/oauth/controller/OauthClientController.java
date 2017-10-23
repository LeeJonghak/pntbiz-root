package wms.oauth.controller;

import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.*;
import core.wms.oauth.domain.OauthClient;
import wms.oauth.service.OauthAccessTokenService;
import wms.oauth.service.OauthClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-24.
 */
@Controller
public class OauthClientController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OauthClientService oauthClientService;

    @Autowired
    private OauthAccessTokenService oauthAccessTokenService;

    @RequestMapping("/oauth/client/list.do")
    public String list(ModelMap map, PagingParam paging) {

        LoginDetail loginDetail = Security.getLoginDetail();

        paging.setPageSize(30);

        QueryParam param = QueryParam.create();
        param.put("firstItemNo", paging.getFirstItemNo());
        param.put("pageSize", paging.getPageSize());
        param.put("page", paging.getPage());
        param.put("comNum", loginDetail.getCompanyNumber());
        Integer cnt = oauthClientService.getOauthClientCount(param.build());
        List<?> list = oauthClientService.getOauthClientList(param.build());

        Pagination pagination = new Pagination(paging.getPage(), cnt, paging.getPageSize(), paging.getBlockSize());
        String page = pagination.print();

        map.addAttribute("cnt", cnt);
        map.addAttribute("list", list);
        map.addAttribute("page", page);
        map.addAttribute("param", param);
        logger.info("list {}", list);

        return "/oauth/client/list";
    }

    @RequestMapping(value="/oauth/client/form.do", method= RequestMethod.GET)
    public String form() {

        return "/oauth/client/form";
    }

    @RequestMapping(value="/oauth/client/reg.do", method= RequestMethod.POST)
    @ResponseBody
    public String reg(@RequestParam(value = "grantTypes")String grantTypes,
                      @RequestParam(value = "memo")String memo) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            OauthClient vo = new OauthClient();
            vo.setGrantTypes(grantTypes);
            vo.setMemo(memo);
            oauthClientService.registerOauthClient(loginDetail, vo);
            info.put("result", "1");
        } catch (Exception exception) {
            info.put("result", "2");
            logger.error("exception", exception);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/oauth/client/mform.do", method= RequestMethod.GET)
    public String form(ModelMap map, @RequestParam(value = "id", required = true)String clientID) {

        OauthClient oauthClientInfo = oauthClientService.getOauthClientInfo(QueryParam.create("clientID", clientID).build());
        map.addAttribute("oauthClientInfo", oauthClientInfo);

        List<?> accessTokenList = oauthAccessTokenService.getOauthAccessTokenList(QueryParam.create("clientID", oauthClientInfo.getClientID()).build());
        map.addAttribute("accessTokenList", accessTokenList);

        return "/oauth/client/mform";
    }

    @RequestMapping(value="/oauth/client/mod.do", method= RequestMethod.POST)
    @ResponseBody
    public String mod(@RequestParam(value = "clientID", required = true)String clientID,
                      @RequestParam(value = "grantTypes")String grantTypes,
                      @RequestParam(value = "memo")String memo) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            OauthClient oauthClient = oauthClientService.getOauthClientInfo(QueryParam.create("clientID", clientID).build());
            if(oauthClient==null) {
                info.put("result", "3");
            } else {
                oauthClient.setMemo(memo==null?"":memo);
                oauthClient.setGrantTypes(grantTypes);
                oauthClientService.modifyOauthClient(oauthClient);
                info.put("result", "1");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            logger.error("exception", exception);
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/oauth/client/del.do", method= RequestMethod.POST)
    @ResponseBody
    public String del(@RequestParam(value = "clientID", required = true)String clientID) throws IOException {
        Map<String, String> info = new HashMap<String, String>();
        try {

            OauthClient oauthClient = oauthClientService.getOauthClientInfo(QueryParam.create("clientID", clientID).build());
            if(oauthClient==null) {
                info.put("result", "3");
            } else {
                oauthClientService.removeOauthClient(oauthClient);
                info.put("result", "1");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            logger.error("exception", exception);
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }


}
