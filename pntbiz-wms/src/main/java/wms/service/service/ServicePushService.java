package wms.service.service;

import java.util.List;

import core.wms.service.domain.PushResVO;
import core.wms.service.domain.PushSendVO;
import core.wms.service.domain.ServiceEventVO;

public interface ServicePushService {

	public List<?> retrievePushStageList(ServiceEventVO param) throws Exception;
	
	public PushResVO retrievePushStageDetail(ServiceEventVO param) throws Exception;
	
	public PushSendVO sendPush(PushSendVO param) throws Exception;
	
	public List<?> retrievePushResultList(ServiceEventVO param) throws Exception;
	
	public List<?> retrievePushResultDtlList(ServiceEventVO param) throws Exception;
}
