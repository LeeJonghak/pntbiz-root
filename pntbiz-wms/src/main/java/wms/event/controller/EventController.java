package wms.event.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import wms.component.auth.LoginDetail;
import core.wms.event.domain.Event;
import core.wms.event.domain.EventSearchParam;
import core.wms.event.domain.EventType;
import core.wms.event.domain.EventTypeColumns;
import core.wms.event.domain.EventValues;
import wms.event.service.EventService;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import framework.web.util.PagingParam;

/**
 * 이벤트 컨트롤러
 * @author nohsoo 2015-04-16
 */
@Controller
public class EventController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventService eventService;

    /**
     * 이벤트 목록
     * create: nohsoo 2015-04-16
     *
     * @return
     */
    @RequestMapping(value="/event/event/list.do", method= RequestMethod.GET)
    public String eventList(ModelMap map, EventSearchParam param) {
    	// Map<String, Object> map = new HashMap<String, Object>();
        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setPageSize(30);

        List<?> list = eventService.getEventList(param);
        Integer cnt = eventService.getEventCount(param);
        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString  = param.getQueryString();
        String page = pagination.print();

        map.put("cnt", cnt);
        map.put("list", list);
        map.put("pagination", page);
        map.put("page", param.getPage());
        map.put("param", param);

        return "/event/event/list";
    }

    /**
     * 이벤트 조건 항목 뷰
     * create: nohsoo 2015-04-17
     * edit  : nohsoo 2015-04-20 evtNum 처리 추가
     *
     * @param map
     * @param evtTypeCode
     * @return
     */
    @RequestMapping(value="/event/event/eventcondition.ajax.do", method=RequestMethod.GET)
    public String eventConditionForm(ModelMap map,
                                     @RequestParam(value = "evtTypeCode", required = true)String evtTypeCode,
                                     @RequestParam(value = "evtNum", required = false)Integer evtNum) {
        LoginDetail loginDetail = Security.getLoginDetail();
        List<?> columnList = eventService.getEventTypeColumList(loginDetail, evtTypeCode);
        map.addAttribute("columnList", columnList);

        if(evtNum!=null && evtNum>0) {
            Event event = eventService.getEventInfo(evtNum);
            map.addAttribute("eventInfo", event);

            List<?> valuesList = eventService.getEventValues(evtNum);
            Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
            for(Object obj: valuesList) {
                EventValues value = (EventValues)obj;
                valuesMap.put(value.getEvtColNum(), value);
            }
            map.addAttribute("valuesMap", valuesMap);
        }

        return "/event/event/ajax.eventcondition";
    }


    /**
     * 이벤트 등록 폼
     * create: nohsoo 2015-04-16
     * edit:   nohsoo 2015-04-17 이벤트유형 목록 선택가능하도록 수정
     *
     * @return
     */
    @RequestMapping(value="/event/event/form.do", method=RequestMethod.GET)
    public String eventForm(ModelMap map) {
        LoginDetail loginDetail = Security.getLoginDetail();
        List<?> eventTypeList = eventService.getEventTypeList(loginDetail);
        map.addAttribute("eventTypeList", eventTypeList);


        return "/event/event/form";
    }

    /**
     * 이벤트 수정 폼
     * create: nohsoo 2015-04-17
     *
     * @param map
     * @param evtNum 이벤트고유번호
     * @return
     */
    @RequestMapping(value="/event/event/mform.do", method=RequestMethod.GET)
    public String eventMForm(ModelMap map, @RequestParam(value = "evtNum")Integer evtNum) {
        LoginDetail loginDetail = Security.getLoginDetail();
        List<?> eventTypeList = eventService.getEventTypeList(loginDetail);
        map.addAttribute("eventTypeList", eventTypeList);

        Event event = eventService.getEventInfo(evtNum);
        map.addAttribute("eventInfo", event);

        return "/event/event/mform";
    }

    /**
     * 이벤트 등록 처리
     * create: nohsoo 2015-04-17
     *
     * @param evtName 이벤트명
     * @param evtDesc 이벤트설명
     * @param evtTypeCode 이벤트유형코드
     * @param request HttpServletRequest(커스텀 컬럼 값을 가져오기 위해서, 동적컬럼처리)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/event/event/reg.do", method = RequestMethod.POST)
    @ResponseBody
    public String eventReg(@RequestParam(value = "evtName")String evtName,
                           @RequestParam(value = "evtDesc")String evtDesc,
                           @RequestParam(value = "evtTypeCode")String evtTypeCode,
                           HttpServletRequest request) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {

            LoginDetail loginDetail = Security.getLoginDetail();
            eventService.registerEvent(loginDetail, evtName, evtDesc, evtTypeCode, request);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    /**
     * 이벤트 수정 처리
     * create: 2015-04-20
     *
     * @param evtNum 이벤트 고유번호
     * @param evtName 이벤트명
     * @param evtDesc 이벤트 설명
     * @param evtTypeCode 이벤트 유형코드
     * @param request HttpServletRequest(커스텀 컬럼 값을 가져오기 위해서, 동적컬럼처리)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/event/event/mod.do", method = RequestMethod.POST)
    @ResponseBody
    public String eventMod(@RequestParam(value = "evtNum")Integer evtNum,
                           @RequestParam(value = "evtName")String evtName,
                           @RequestParam(value = "evtDesc")String evtDesc,
                           @RequestParam(value = "evtTypeCode")String evtTypeCode,
                           HttpServletRequest request) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {

            LoginDetail loginDetail = Security.getLoginDetail();
            eventService.modifyEvent(loginDetail, evtNum, evtName, evtDesc, evtTypeCode, request);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    /**
     * 이벤트 삭제
     * create: nohsoo 2015-04-20
     *
     * @param evtNum 이벤트 고유번호
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/event/event/del.do", method = RequestMethod.POST)
    @ResponseBody
    public String eventMod(@RequestParam(value = "evtNum")Integer evtNum) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {

            LoginDetail loginDetail = Security.getLoginDetail();
            eventService.deleteEvent(loginDetail, evtNum);

            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    /**
     * 이벤트 유형 목록
     * create: nohsoo 2015-04-16
     *
     * @param map
     * @param request
     * @param pageNumber
     * @param opt
     * @param keyword
     * @return
     */
    @RequestMapping(value="/event/type/list.do", method= RequestMethod.GET)
    public String eventTypeList(ModelMap map, HttpServletRequest request, PagingParam paging,
                                @RequestParam(value = "opt", required = false)String opt,
                                @RequestParam(value = "keyword", required = false)String keyword) {

        LoginDetail loginDetail = Security.getLoginDetail();

        paging.setPageSize(30);
        HashMap<String, Object> param = new HashMap<String, Object>();
        if(opt!=null && keyword!=null) {
            param.put("opt", opt);
            param.put("keyword", keyword);
        }
        param.put("comNum", loginDetail.getCompanyNumber());
        List<?> list = eventService.getEventTypeList(paging, param);
        Integer cnt = eventService.getEventTypeCount(paging, param);
        Pagination pagination = new Pagination(paging.getPage(), cnt, paging.getPageSize(), paging.getBlockSize());
        pagination.queryString  = request.getServletPath();
        String page = pagination.print();

        map.put("cnt", cnt);
        map.put("list", list);
        map.put("pagination", page);
        map.put("page", paging.getPage());
        map.put("param", param);

        return "/event/type/list";
    }

    /**
     * 이벤트 유형 등록 폼
     * create: nohsoo 2015-04-16
     *
     * @param map
     * @return
     */
    @RequestMapping(value="/event/type/form", method= RequestMethod.GET)
    public String eventTypeForm(ModelMap map) {

        Map<String, String> columnCodeList = eventService.getEventTypeColumTypeCodeList();
        map.addAttribute("columnCodeList", columnCodeList);

        return "/event/type/form";
    }

    /**
     * 이벤트 유형 수정 폼
     * create: nohsoo 2015-04-20
     *
     * @param map
     * @param evtTypeCode
     * @return
     */
    @RequestMapping(value="/event/type/mform", method= RequestMethod.GET)
    public String eventTypeMForm(ModelMap map, @RequestParam(value = "evtTypeCode", required = true)String evtTypeCode) {

        Map<String, String> columnCodeList = eventService.getEventTypeColumTypeCodeList();
        map.addAttribute("columnCodeList", columnCodeList);

        EventType eventType = eventService.getEventTypeInfo(evtTypeCode);
        map.addAttribute("eventType", eventType);

        LoginDetail loginDetail = Security.getLoginDetail();
        List<?> evtTypeColumnList = eventService.getEventTypeColumList(loginDetail, evtTypeCode);
        map.addAttribute("evtTypeColumnList", evtTypeColumnList);

        return "/event/type/mform";
    }

    /**
     * 이벤트 유형 등록 처리
     * create: nohsoo 2015-04-16
     *
     * @param evtTypeCode
     * @param evtTypeName
     * @param evtColType
     * @param evtColID
     * @param evtColName
     * @param items
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/event/type/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String eventTypeReg(@RequestParam(value = "evtTypeCode", required = true)String evtTypeCode,
                               @RequestParam(value = "evtTypeName", required = true)String evtTypeName,
                               @RequestParam(value = "evtColType", required = true)String[] evtColType,
                               @RequestParam(value = "evtColID", required = true)String[] evtColID,
                               @RequestParam(value = "evtColName", required = true)String[] evtColName,
                               @RequestParam(value = "items", required = true)String[] items) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            List<EventTypeColumns> columns = new ArrayList<EventTypeColumns>();
            for(int i=0; i<evtColType.length; i++) {
                if(StringUtils.isNotEmpty(evtColType[i])
                        && StringUtils.isNotEmpty(evtColID[i])
                        && StringUtils.isNotEmpty(evtColName[i])) {

                    EventTypeColumns eventTypeColumns = new EventTypeColumns();
                    eventTypeColumns.setEvtColID(evtColID[i]);
                    eventTypeColumns.setEvtColType(evtColType[i]);
                    eventTypeColumns.setEvtColName(evtColName[i]);
                    eventTypeColumns.setEvtColItems(items[i]);
                    columns.add(eventTypeColumns);
                }
            }
            eventService.registerEventType(loginDetail, evtTypeCode, evtTypeName, columns);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }


    @RequestMapping(value="/event/type/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String eventTypeMod(@RequestParam(value = "evtTypeCode", required = true)String evtTypeCode,
                               @RequestParam(value = "evtTypeName", required = true)String evtTypeName,
                               @RequestParam(value = "evtColNum", required = true)Integer[] evtColNum,
                               @RequestParam(value = "evtColType", required = true)String[] evtColType,
                               @RequestParam(value = "evtColID", required = true)String[] evtColID,
                               @RequestParam(value = "evtColName", required = true)String[] evtColName,
                               @RequestParam(value = "items", required = true)String[] items) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            List<EventTypeColumns> columns = new ArrayList<EventTypeColumns>();
            for(int i=0; i<evtColType.length; i++) {
                if(StringUtils.isNotEmpty(evtColType[i])
                        && StringUtils.isNotEmpty(evtColID[i])
                        && StringUtils.isNotEmpty(evtColName[i])) {

                    EventTypeColumns eventTypeColumns = new EventTypeColumns();
                    eventTypeColumns.setEvtColNum(evtColNum[i]);
                    eventTypeColumns.setEvtColID(evtColID[i]);
                    eventTypeColumns.setEvtColType(evtColType[i]);
                    eventTypeColumns.setEvtColName(evtColName[i]);
                    eventTypeColumns.setEvtColItems(items[i]);
                    columns.add(eventTypeColumns);
                }
            }
            eventService.modifyEventType(loginDetail, evtTypeCode, evtTypeName, columns);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value="/event/type/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String eventTypeDel(EventSearchParam param) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            param.setComNum(loginDetail.getCompanyNumber());

            Integer evtCnt = eventService.getEventCount(param);
            if(evtCnt>0) {
                info.put("result", "3");
            } else {
                eventService.deleteEventType(loginDetail, param.getEvtTypeCode());
                info.put("result", "1");
            }

        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

}
