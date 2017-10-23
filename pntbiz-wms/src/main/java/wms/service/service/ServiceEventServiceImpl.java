package wms.service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.wms.service.dao.ServiceEventDao;
import core.wms.service.domain.ContentsEventParamVO;
import core.wms.service.domain.EventResVO;
import core.wms.service.domain.EventTranslationVO;
import core.wms.service.domain.ServiceEventVO;

@Service
public class ServiceEventServiceImpl implements ServiceEventService{
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEventServiceImpl.class);
	
	@Autowired
	private ServiceEventDao dao;
	
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
	
	public List<?> getGeofencingCodeList() throws Exception{
		return dao.getGeofencingCodeList();
	}
	
	@Override
	public Integer getContentsCount(ContentsEventParamVO param) throws DataAccessException {
		Integer cnt = 0;
		cnt = dao.getContentsCount(param);
		logger.info("getContentsCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getContentsList(ContentsEventParamVO param) throws DataAccessException {
		List<?> contentsList = null;
		contentsList = dao.getContentsList(param);
		logger.info("getContentsList {}", contentsList.size());
		return contentsList;
	}
	
	public List<?> retrieveBeaconEventList(ServiceEventVO param) throws Exception{
		return dao.retrieveBeaconEventList(param);
	}
}
