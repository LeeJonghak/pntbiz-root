package wms.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.wms.service.dao.ServicePushDao;
import core.wms.service.domain.PushResVO;
import core.wms.service.domain.PushSendVO;
import core.wms.service.domain.ServiceEventVO;

@Service
public class ServicePushServiceImpl implements ServicePushService{

	@Autowired
	private ServicePushDao dao;
	
	public List<?> retrievePushStageList(ServiceEventVO param) throws Exception{
		return dao.retrievePushStageList(param);
	}
	
	public PushResVO retrievePushStageDetail(ServiceEventVO param) throws Exception{
		return dao.retrievePushStageDetail(param);
	}
	
	public PushSendVO sendPush(PushSendVO param) throws Exception{
		return dao.sendPush(param);
	}
	
	public List<?> retrievePushResultList(ServiceEventVO param) throws Exception{
		return dao.retrievePushResultList(param);
	}
	
	public List<?> retrievePushResultDtlList(ServiceEventVO param) throws Exception{
		return dao.retrievePushResultDtlList(param);
	}
}
