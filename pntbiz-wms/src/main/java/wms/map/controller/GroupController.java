package wms.map.controller;

import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.*;
import core.wms.map.domain.Group;
import wms.map.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 그룹 관리(비콘, 노드, 펜스 등을 그룹으로 설정 가능)
 * 2015-05-07 nohsoo
 */
@Controller
public class GroupController {


    @Autowired
    private GroupService groupService;

    @RequestMapping(value="/map/group/list.do", method= RequestMethod.GET)
    public String groupList(HttpServletRequest request, ModelMap map,
                            @RequestParam(value = "page", defaultValue = "1")int pageNumber,
                            @RequestParam(value = "opt", required = false)String opt,
                            @RequestParam(value = "keyword", required = false)String keyword){

        LoginDetail loginDetail = Security.getLoginDetail();
        int pageSize = 30;
        int blockSize = 10;
        PagingParam paging = new PagingParam();
        paging.setPage(pageNumber);
        paging.initPage(pageSize, blockSize);
        HashMap<String, Object> param = new HashMap<String, Object>();
        if(opt!=null && keyword!=null) {
            param.put("opt", opt);
            param.put("keyword", keyword);
        }
        param.put("comNum", loginDetail.getCompanyNumber());

        Integer cnt = groupService.getGroupCount(paging, param);
        List<?> list = groupService.getGroupList(paging, param);

        Pagination pagination = new Pagination(paging.getPage(), cnt, paging.getPageSize(), paging.getBlockSize());
        pagination.queryString  = request.getServletPath();
        String page = pagination.print();

        map.addAttribute("cnt", cnt);
        map.addAttribute("list", list);
        map.addAttribute("page", page);
        map.addAttribute("param", paging);

        return "/map/group/list";
    }

    @RequestMapping(value="/map/group/form.do", method=RequestMethod.GET)
    public String groupForm() {
        return "/map/group/form";
    }

    @RequestMapping(value="/map/group/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String groupReg(@RequestParam(value = "groupName", required = true)String groupName) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            Group vo = new Group();
            vo.setGroupName(groupName);
            groupService.registerGroup(loginDetail, vo);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/map/group/mform.do", method=RequestMethod.GET)
    public String groupMForm(ModelMap map, @RequestParam(value = "groupNum", required = true)Integer groupNum) {
        Group param = new Group();
        param.setGroupNum(groupNum);
        Group groupInfo = groupService.getGroup(param);
        map.addAttribute("groupInfo", groupInfo);

        return "/map/group/mform";
    }

    @RequestMapping(value="/map/group/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String groupMod(@RequestParam(value = "groupNum", required = true)Integer groupNum,
                           @RequestParam(value = "groupName", required = true)String groupName) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {

            Group param = new Group();
            param.setGroupNum(groupNum);
            Group groupInfo = groupService.getGroup(param);
            groupInfo.setGroupName(groupName);
            groupService.modifyGroup(groupInfo);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/map/group/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String groupDel(@RequestParam(value = "groupNum", required = true)Integer groupNum) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {

            Group param = new Group();
            param.setGroupNum(groupNum);
            groupService.deleteGroup(param);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();

    }

}
