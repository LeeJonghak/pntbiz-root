package core.wms.event.dao;

import java.util.HashMap;
import java.util.List;

import core.wms.event.domain.Event;
import core.wms.event.domain.EventSearchParam;
import core.wms.event.domain.EventType;
import core.wms.event.domain.EventValues;
import core.wms.event.domain.EventTypeColumns;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

/**
 * Event DAO
 * @author nohsoo 2015-04-16
 */
@Repository
public class EventDao extends BaseDao {

    public void insertEvent(Event param) {
        this.insert("insertEvent", param);
    }

    public Event getEventInfo(Event param) {
        Event event = (Event)this.select("getEventInfo", param);
        return event;
    }

    public List<?> getEventList(EventSearchParam paramVo) {
        return this.list("getEventList", paramVo);
    }

    public Integer getEventCount(EventSearchParam paramVo) {
        return (Integer)this.select("getEventCount", paramVo);
    }

    public List<?> getEventValues(EventValues param) {
        return this.list("getEventValues", param);
    }

    public EventValues getEventValueInfo(EventValues param) {
        return (EventValues)this.select("getEventValueInfo", param);
    }

    public void insertEventValues(EventValues eventValues) {
        this.insert("insertEventValues", eventValues);
    }

    public void modifyEventValues(EventValues eventValue) {
        this.update("modifyEventValues", eventValue);
    }

    public void deleteEventValues(EventValues param) {
        this.delete("deleteEventValues", param);
    }

    public void modifyEvent(Event event) {
        this.update("modifyEvent", event);
    }

    public void deleteEvent(Event event) {
        this.delete("deleteEvent", event);
    }

    public List<?> getEventTypeList(HashMap<String, Object> param) {
        List<?> list = this.list("getEventTypeList", param);
        return list;
    }

    public Integer getEventTypeCount(HashMap<String, Object> param) {
        Integer count = (Integer)this.select("getEventTypeCount", param);
        return count;
    }

    public void insertEventType(EventType eventType) {
        this.insert("insertEventType", eventType);
    }

    public EventTypeColumns getEventTypeColumnInfo(EventTypeColumns param) {
        EventTypeColumns eventTypeColumns = (EventTypeColumns)this.select("getEventTypeColumnInfo", param);
        return eventTypeColumns;
    }

    public List<?> getEventTypeColumnList(EventTypeColumns eventTypeColumns) {
        List<?> list = this.list("getEventTypeColumnList", eventTypeColumns);
        return list;
    }

    public void insertEventTypeColumns(EventTypeColumns eventTypeColumns) {
        this.insert("insertEventTypeColumns", eventTypeColumns);
    }

    public void modifyEventTypeColumns(EventTypeColumns eventTypeColumns) {
        this.update("modifyEventTypeColumns", eventTypeColumns);
    }

    public void updateEvent(Event event) {
        this.update("updateEvent", event);
    }

    public EventType getEventTypeInfo(EventType param) {
        EventType eventType = (EventType)this.select("getEventTypeInfo", param);
        return eventType;
    }

    public void modifyEventType(EventType eventType) {
        this.update("modifyEventType", eventType);
    }

    public void deleteEventType(EventType param) {
        this.delete("deleteEventType", param);
    }

    public void deleteEventTypeColumns(EventType param) {
        this.delete("deleteEventTypeColumns", param);
    }
}
