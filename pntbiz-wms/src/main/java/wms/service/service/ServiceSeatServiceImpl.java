package wms.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.wms.service.dao.ServiceSeatDao;
import core.wms.service.domain.EventResVO;
import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;

@Service
public class ServiceSeatServiceImpl implements ServiceSeatService{
	
	@Autowired
	private ServiceSeatDao dao;
	
	public List<?> retrieveListEvent(ServiceEventVO param) throws Exception{
		return dao.retrieveEventList(param);
	}
	
	public EventResVO retrievEventItem(ServiceEventVO param) throws Exception{
		return dao.retrieveEventView(param);
	}
	
	public void createEventItem(ServiceEventVO param) throws Exception{
		dao.createEventItem(param);
	}
	
	public void updateEventItem(ServiceEventVO param) throws Exception{
		dao.updateEventItem(param);
	}
	
	public void deleteEventItem(ServiceEventVO param) throws Exception{
		dao.deleteEventItem(param);
	}
	
	public void translationPushStageInfo(EventTranslationVO param) throws Exception{
		dao.translationPushStageInfo(param);
	}
}
