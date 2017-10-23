package wms.event.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.common.code.dao.CodeDao;
import core.common.code.domain.Code;
import wms.component.auth.LoginDetail;
import core.wms.event.dao.EventDao;
import core.wms.event.domain.Event;
import core.wms.event.domain.EventSearchParam;
import core.wms.event.domain.EventType;
import core.wms.event.domain.EventTypeColumns;
import core.wms.event.domain.EventValues;
import framework.web.util.PagingParam;

/**
 * 이벤트 서비스
 * @author nohsoo 2015-04-16
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private CodeDao codeDao;

    /**
     * 이벤트 정보(단건)
     * create: nohsoo 2015-04-17
     *
     * @param evtNum 이번트고유번호
     * @return Event
     */
    @Override
    public Event getEventInfo(int evtNum) {
        Event param = new Event();
        param.setEvtNum(evtNum);
        Event event = eventDao.getEventInfo(param);
        return event;
    }

    /**
     * 이벤트 목록
     * create: nohsoo 2015-04-17
     *
     * @param paging
     * @param param
     * @return
     */
    @Override
    public List<?> getEventList(EventSearchParam paramVo) {
        List<?> list = eventDao.getEventList(paramVo);
        return list;
    }

    /**
     * 이벤트 목록 카운트
     * create: nohsoo 2015-04-17
     *
     * @param paging
     * @param param
     * @return
     */
    @Override
    public Integer getEventCount(EventSearchParam paramVo) {
        return eventDao.getEventCount(paramVo);
    }
    /**
     * 이벤트 클럼값 조회
     * create: nohsoo 2015-04-20
     *
     * @param evtNum 이벤트 고유번호
     * @return List
     */
    @Override
    public List<?> getEventValues(Integer evtNum) {
        EventValues param = new EventValues();
        param.setEvtNum(evtNum);
        List<?> list = eventDao.getEventValues(param);
        return list;
    }

    /**
     * 이벤트 등록 처리
     * create: nohsoo 2015-04-17
     *
     * @param loginDetail 로그인정보
     * @param evtName 이벤트명
     * @param evtDesc 이벤트설명
     * @param evtTypeCode 이벤트유형코드
     * @param request HttpServletRequest(동적컬럼처리를 위한 데이터를 가져오기 위해서)
     */
    @Override
    @Transactional
    public void registerEvent(LoginDetail loginDetail, String evtName, String evtDesc,
                              String evtTypeCode, HttpServletRequest request) {

        /**
         * 이벤트 추가
         */
        Event event = new Event();
        event.setEvtTypeCode(evtTypeCode);
        event.setComNum(loginDetail.getCompanyNumber());
        event.setEvtName(evtName);
        event.setEvtDesc(evtDesc);
        eventDao.insertEvent(event);

        /**
         * 컬럼  추가
         */
        EventTypeColumns typeColumnsParam = new EventTypeColumns();
        typeColumnsParam.setEvtTypeCode(evtTypeCode);
        List<?> columnList = eventDao.getEventTypeColumnList(typeColumnsParam);
        for(Object obj: columnList) {
            EventTypeColumns column = (EventTypeColumns)obj;
            EventValues eventValue = new EventValues();
            eventValue.setEvtNum(event.getEvtNum());
            eventValue.setEvtColNum(column.getEvtColNum());

            /**
             * 처리방법에 따른 evtTypeCode 정리
             */
            List<String> defaultType = Arrays.asList(new String[]{"SES","SSS","NNN","DDD","TTT","DDT"});
            List<String> multiItemType = Arrays.asList(new String[]{"SEM"});
            List<String> rangeType = Arrays.asList(new String[]{"NNR","TTR","DDR","DTR"});

            /**
             * 각 evtTypeCode에 해당하는 방식으로 값 정리 및 레코드추가
             */
            if(defaultType.contains(column.getEvtColType())) {
                String value = request.getParameter(column.getEvtColID());
                if(value==null) value="";
                eventValue.setBeginValue(value);

            } else if(multiItemType.contains(column.getEvtColType())) {
                String[] value = request.getParameterValues(column.getEvtColID());
                if(value==null || value.length<=0) {
                    eventValue.setBeginValue("");
                } else {
                    eventValue.setBeginValue(StringUtils.join(value, ","));
                }

            } else if(rangeType.contains(column.getEvtColType())) {
                String[] colIDs = column.getEvtColID().split(",");
                String beginValue = request.getParameter(colIDs[0]);
                if(beginValue==null) beginValue="";
                String endValue = request.getParameter(colIDs[1]);
                if(endValue==null) endValue="";
                eventValue.setBeginValue(beginValue);
                eventValue.setEndValue(endValue);
            }
            eventDao.insertEventValues(eventValue);
        }
    }

    /**
     * 이벤트 수정처리
     * create: nohsoo 2015-04-20
     *
     * @param loginDetail 로그인정보객체
     * @param evtNum 이벤트 고유번호
     * @param evtName 이벤트명
     * @param evtDesc 이벤트 설명
     * @param evtTypeCode 이벤트 유형코드
     * @param request HttpServletRequest(동적컬럼처리를 위한 데이터를 가져오기 위해서)
     */
    @Override
    @Transactional
    public void modifyEvent(LoginDetail loginDetail, Integer evtNum, String evtName, String evtDesc, String evtTypeCode, HttpServletRequest request) throws Exception {
        Event eventInfo = this.getEventInfo(evtNum);
        if(eventInfo==null) {
            throw new Exception("not found event:"+evtNum);
        }

        /**
         * 이벤트 유형코드가 변경된 경우 컬럼 값 모두 제거
         */
        if(eventInfo.getEvtTypeCode()!=evtTypeCode) {
            EventValues eventValueDeleteParam = new EventValues();
            eventValueDeleteParam.setEvtNum(evtNum);
            eventDao.deleteEventValues(eventValueDeleteParam);
        }

        eventInfo.setEvtName(evtName);
        eventInfo.setEvtDesc(evtDesc);
        eventInfo.setEvtTypeCode(evtTypeCode);
        eventDao.updateEvent(eventInfo);

        /**
         * 컬럼 값 수정
         */
        EventTypeColumns typeColumnsParam = new EventTypeColumns();
        typeColumnsParam.setEvtTypeCode(evtTypeCode);
        List<?> columnList = eventDao.getEventTypeColumnList(typeColumnsParam);
        for(Object obj: columnList) {
            EventTypeColumns column = (EventTypeColumns)obj;
            EventValues eventValueParam = new EventValues();
            eventValueParam.setEvtNum(evtNum);
            eventValueParam.setEvtColNum(column.getEvtColNum());
            EventValues eventValue = this.eventDao.getEventValueInfo(eventValueParam);
            if(eventValue==null) {
                eventValue = new EventValues();
                eventValue.setEvtNum(evtNum);
                eventValue.setEvtColNum(column.getEvtColNum());
            }

            /**
             * 처리방법에 따른 evtTypeCode 정리
             */
            List<String> defaultType = Arrays.asList(new String[]{"SES","SSS","NNN","DDD","TTT","DDT"});
            List<String> multiItemType = Arrays.asList(new String[]{"SEM"});
            List<String> rangeType = Arrays.asList(new String[]{"NNR","TTR","DDR","DTR"});

            /**
             * 각 evtTypeCode에 해당하는 방식으로 값 정리 및 레코드추가, 수정
             */
            if(defaultType.contains(column.getEvtColType())) {
                String value = request.getParameter(column.getEvtColID());
                if(value==null) value="";
                eventValue.setBeginValue(value);

            } else if(multiItemType.contains(column.getEvtColType())) {
                String[] value = request.getParameterValues(column.getEvtColID());
                if(value==null || value.length<=0) {
                    eventValue.setBeginValue("");
                } else {
                    eventValue.setBeginValue(StringUtils.join(value, ","));
                }

            } else if(rangeType.contains(column.getEvtColType())) {
                String[] colIDs = column.getEvtColID().split(",");
                String beginValue = request.getParameter(colIDs[0]);
                if(beginValue==null) beginValue="";
                String endValue = request.getParameter(colIDs[1]);
                if(endValue==null) endValue="";
                eventValue.setBeginValue(beginValue);
                eventValue.setEndValue(endValue);
            }

            /**
             * 값 저장
             */
            if(eventValue.getEvtValueNum()>0) {
                eventDao.modifyEventValues(eventValue);
            } else {
                eventDao.insertEventValues(eventValue);
            }
        }
    }


    /**
     * 이벤트 삭제 처리
     * create: nohsoo 2015-04-20
     *
     * @param loginDetail
     * @param evtNum
     * @throws Exception
     */
    @Override
    @Transactional
    public void deleteEvent(LoginDetail loginDetail, Integer evtNum) throws Exception {
        Event eventInfo = this.getEventInfo(evtNum);
        if(eventInfo==null) {
            throw new Exception("not found event:"+evtNum);
        } else {
            Event eventParam = new Event();
            eventParam.setEvtNum(evtNum);
            eventDao.deleteEvent(eventParam);

            EventValues valuesParam = new EventValues();
            valuesParam.setEvtNum(evtNum);
            eventDao.deleteEventValues(valuesParam);
        }
    }

    /**
     * 이벤트 유형 등록 처리
     * create: nohsoo 2015-04-17
     *
     * @param loginDetail
     * @param eventTypeCode
     * @param eventTypeName
     * @param columns
     */
    @Override
    @Transactional
    public void registerEventType(LoginDetail loginDetail, String eventTypeCode, String eventTypeName,
                                  List<EventTypeColumns> columns) {

        EventType eventType = new EventType();
        eventType.setComNum(loginDetail.getCompanyNumber());
        eventType.setEvtTypeCode(eventTypeCode);
        eventType.setEvtTypeName(eventTypeName);
        eventDao.insertEventType(eventType);

        for(EventTypeColumns eventTypeColumns: columns) {
            eventTypeColumns.setEvtTypeCode(eventTypeCode);
            eventDao.insertEventTypeColumns(eventTypeColumns);
        }

    }

    /**
     * 이벤트 유형 수정 처리
     * create: 2015-04-20
     *
     * @param loginDetail
     * @param evtTypeCode
     * @param evtTypeName
     * @param columns
     * @throws Exception
     */
    @Override
    @Transactional
    public void modifyEventType(LoginDetail loginDetail, String evtTypeCode, String evtTypeName, List<EventTypeColumns> columns) throws Exception {
        EventType eventTypeInfo = this.getEventTypeInfo(evtTypeCode);
        if(eventTypeInfo==null) {
            throw new Exception("not found EventType:"+evtTypeCode);
        }

        eventTypeInfo.setEvtTypeCode(evtTypeCode);
        eventTypeInfo.setEvtTypeName(evtTypeName);
        eventDao.modifyEventType(eventTypeInfo);

        for(EventTypeColumns eventTypeColumns: columns) {
            EventTypeColumns columnParam = new EventTypeColumns();
            columnParam.setEvtTypeCode(evtTypeCode);
            columnParam.setEvtColNum(eventTypeColumns.getEvtColNum());
            EventTypeColumns columnInfo = eventDao.getEventTypeColumnInfo(columnParam);
            columnInfo.setEvtColID(eventTypeColumns.getEvtColID());
            columnInfo.setEvtColName(eventTypeColumns.getEvtColName());
            columnInfo.setEvtColType(eventTypeColumns.getEvtColType());
            columnInfo.setEvtColItems(eventTypeColumns.getEvtColItems());
            eventDao.modifyEventTypeColumns(columnInfo);
        }
    }

    /**
     * 이벤트 유형 삭제
     * create: nohsoo 2015-04-20
     *
     * @param loginDetail
     * @param evtTypeCode
     */
    @Override
    @Transactional
    public void deleteEventType(LoginDetail loginDetail, String evtTypeCode) {
        EventType param = new EventType();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setEvtTypeCode(evtTypeCode);
        eventDao.deleteEventType(param);

        /**
         * 컬럼 정보 삭제
         */
        EventTypeColumns columnParam = new EventTypeColumns();
        columnParam.setEvtTypeCode(evtTypeCode);
        eventDao.deleteEventTypeColumns(param);
    }

    /**
     * 이벤트 유형 목록 반환
     * create: nohsoo 2015-04-17
     *
     * @param loginDetail
     * @return
     */
    @Override
    public List<?> getEventTypeList(LoginDetail loginDetail) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        List<?> list = eventDao.getEventTypeList(param);
        return list;
    }

    /**
     * 이벤트 유형수 목록
     * create: nohsoo 2015-04-20
     *
     * @param paging
     * @param param
     * @return
     */
    @Override
    public List<?> getEventTypeList(PagingParam paging, HashMap<String, Object> param) {
        param.put("firstItemNo", paging.getFirstItemNo());
        param.put("pageSize", paging.getPageSize());
        param.put("page", paging.getPage());
        List<?> list = eventDao.getEventTypeList(param);
        return list;
    }

    /**
     * 이벤트 유형 목록
     * create: nohsoo 2015-04-20
     *
     * @param paging
     * @param param
     * @return
     */
    @Override
    public Integer getEventTypeCount(PagingParam paging, HashMap<String, Object> param) {
        return eventDao.getEventTypeCount(param);
    }

    /**
     * 특정 이벤트 유형의 컬럼 목록을 반환
     * create: nohsoo 2015-04-17
     *
     * @param loginDetail
     * @param evtTypeCode
     * @return
     */
    @Override
    public List<?> getEventTypeColumList(LoginDetail loginDetail, String evtTypeCode) {

        EventTypeColumns param = new EventTypeColumns();
        param.setEvtTypeCode(evtTypeCode);
        List<?> list = eventDao.getEventTypeColumnList(param);
        return list;
    }

    /**
     * 이벤트 유형>컬럼 타입 코드 목록 반환
     * create: nohsoo 2014-04-17
     *
     * @return
     */
    @Override
    public Map<String, String> getEventTypeColumTypeCodeList() {
        Code codeParam = new Code();
        codeParam.setgCD("EVTCOLTYPE");
        List<?> list = codeDao.getCodeListByCD(codeParam);

        Map<String, String> result = new HashMap<String, String>();
        for(Object obj: list) {
            Code code = (Code)obj;
            result.put(code.getsCD(), code.getsName());
        }
        return result;
    }

    /**
     * 이벤트 유형 정보(단건)
     * create: nohsoo 2014-04-20
     *
     * @param evtTypeCode
     * @return
     */
    @Override
    public EventType getEventTypeInfo(String evtTypeCode) {
        EventType param = new EventType();
        param.setEvtTypeCode(evtTypeCode);
        EventType eventType = eventDao.getEventTypeInfo(param);
        return eventType;
    }

}
