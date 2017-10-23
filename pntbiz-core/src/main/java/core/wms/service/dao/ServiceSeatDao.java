package core.wms.service.dao;

import java.util.List;

import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.service.domain.EventResVO;
import framework.db.dao.BaseDao;

@Repository
public class ServiceSeatDao extends BaseDao {
	
	public List<?> retrieveEventList(ServiceEventVO param) throws DataAccessException {
		return (List<?>) list("SP_CMS_SERVICE_GET_EVENT_SEAT_LIST", param);
	}
	
	public EventResVO retrieveEventView(ServiceEventVO param) throws DataAccessException {
		return (EventResVO)select("SP_CMS_SERVICE_GET_EVENT_SEAT_VIEW", param);
	}
	
	public ServiceEventVO createEventItem(ServiceEventVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_CREATE_EVENT_SEAT", param);
		return param;
	}
	
	public ServiceEventVO updateEventItem(ServiceEventVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_UPDATE_EVENT_SEAT", param);
		return param;
	}
	
	public ServiceEventVO deleteEventItem(ServiceEventVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_DELETE_EVENT_SEAT", param);
		return param;
	}
	
	public EventTranslationVO translationPushStageInfo(EventTranslationVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_TRANSLATIONS_SEAT", param);
		return param;
	}
	
}
