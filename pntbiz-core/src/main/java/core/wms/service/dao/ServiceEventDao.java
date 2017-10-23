package core.wms.service.dao;

import java.util.List;

import core.wms.service.domain.ContentsEventParamVO;
import core.wms.service.domain.EventResVO;
import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class ServiceEventDao extends BaseDao {
	
	public List<?> retrieveEventList(ServiceEventVO param) throws DataAccessException {
		return (List<?>) list("SP_CMS_SERVICE_GET_EVENT_LIST", param);
	}
	
	public EventResVO retrieveEventView(ServiceEventVO param) throws DataAccessException {
		return (EventResVO)select("SP_CMS_SERVICE_GET_EVENT_VIEW", param);
	}
	
	public ServiceEventVO createEventItem(ServiceEventVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_CREATE_EVENT", param);
		return param;
	}
	
	public ServiceEventVO updateEventItem(ServiceEventVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_UPDATE_EVENT", param);
		return param;
	}
	
	public ServiceEventVO deleteEventItem(ServiceEventVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_DELETE_EVENT", param);
		return param;
	}
	
	public EventTranslationVO translationPushStageInfo(EventTranslationVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_TRANSLATIONS", param);
		return param;
	}
	
	public List<?> getGeofencingCodeList() throws DataAccessException{
		return list("getGeofencingforEvent");
	}
	
	public Integer getContentsCount(ContentsEventParamVO param) throws DataAccessException {
		return (Integer) select("getContentsCountForEvent", param);
	}
	
	public List<?> getContentsList(ContentsEventParamVO param) throws DataAccessException {
		return (List<?>) list("getContentsListForEvent", param);
	}
	
	public List<?> retrieveBeaconEventList(ServiceEventVO param) throws DataAccessException {
		return (List<?>) list("SP_CMS_SERVICE_GET_BP_EVENT_LIST", param);
	}
	
}
