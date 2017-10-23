package wms.service.service;

import java.util.List;

import core.wms.service.domain.EventResVO;
import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;

public interface ServiceSeatService {

	public List<?> retrieveListEvent(ServiceEventVO param) throws Exception;
	
	public EventResVO retrievEventItem(ServiceEventVO param) throws Exception;
	
	public void createEventItem(ServiceEventVO param) throws Exception;
	
	public void updateEventItem(ServiceEventVO param) throws Exception;
	
	public void deleteEventItem(ServiceEventVO param) throws Exception;
	
	public void translationPushStageInfo(EventTranslationVO param) throws Exception;
	
}
