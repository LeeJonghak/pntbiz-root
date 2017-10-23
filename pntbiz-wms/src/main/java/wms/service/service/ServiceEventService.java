package wms.service.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.service.domain.ContentsEventParamVO;
import core.wms.service.domain.EventResVO;
import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;

public interface ServiceEventService {

	public List<?> retrieveListEvent(ServiceEventVO param) throws Exception;
	
	public EventResVO retrievEventItem(ServiceEventVO param) throws Exception;
	
	public void createEventItem(ServiceEventVO param) throws Exception;
	
	public void updateEventItem(ServiceEventVO param) throws Exception;
	
	public void deleteEventItem(ServiceEventVO param) throws Exception;
	
	public void translationPushStageInfo(EventTranslationVO param) throws Exception;
	
	public List<?> getGeofencingCodeList() throws Exception;
	
	public Integer getContentsCount(ContentsEventParamVO param) throws DataAccessException;

	public List<?> getContentsList(ContentsEventParamVO param) throws DataAccessException;
	
	public List<?> retrieveBeaconEventList(ServiceEventVO param) throws Exception;
	
}
