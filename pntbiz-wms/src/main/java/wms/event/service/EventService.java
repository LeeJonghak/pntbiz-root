package wms.event.service;

import wms.component.auth.LoginDetail;
import core.wms.event.domain.Event;
import core.wms.event.domain.EventSearchParam;
import core.wms.event.domain.EventType;
import core.wms.event.domain.EventTypeColumns;
import framework.web.util.PagingParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 이벤트 서비스 인터페이스
 * @author nohsoo 2015-04-16
 */
public interface EventService {

    public void registerEvent(LoginDetail loginDetail, String evtName, String evtDesc, String evtTypeCode, HttpServletRequest request);

    public void modifyEvent(LoginDetail loginDetail, Integer evtNum, String evtName, String evtDesc, String evtTypeCode, HttpServletRequest request) throws Exception;

    public void deleteEvent(LoginDetail loginDetail, Integer evtNum) throws Exception;

    public Event getEventInfo(int evtNum);

    public List<?> getEventList(EventSearchParam paramVo);

    List<?> getEventTypeList(PagingParam paging, HashMap<String, Object> param);

    Integer getEventTypeCount(PagingParam paging, HashMap<String, Object> param);

    public Integer getEventCount(EventSearchParam paramVo);

    public List<?> getEventValues(Integer evtNum);

    public List<?> getEventTypeList(LoginDetail loginDetail);

    public void registerEventType(LoginDetail loginDetail, String eventTypeCode, String eventTypeName, List<EventTypeColumns> columns);

    public List<?> getEventTypeColumList(LoginDetail loginDetail, String evtTypeCode);

    public Map<String, String> getEventTypeColumTypeCodeList();

    public EventType getEventTypeInfo(String evtTypeCode);

    public void modifyEventType(LoginDetail loginDetail, String evtTypeCode, String evtTypeName, List<EventTypeColumns> columns) throws Exception;

    public void deleteEventType(LoginDetail loginDetail, String evtTypeCode);

}
