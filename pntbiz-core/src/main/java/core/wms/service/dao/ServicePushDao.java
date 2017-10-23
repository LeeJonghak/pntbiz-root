package core.wms.service.dao;

import java.util.List;

import core.wms.service.domain.ServiceEventVO;
import core.wms.service.domain.PushSendVO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.service.domain.PushResVO;
import framework.db.dao.BaseDao;

@Repository
public class ServicePushDao extends BaseDao{

	public List<?> retrievePushStageList(ServiceEventVO param) throws DataAccessException {
		return (List<?>) list("SP_CMS_SERVICE_GET_PUSH_STAGE_LIST", param);
	}
	
	public PushResVO retrievePushStageDetail(ServiceEventVO param) throws DataAccessException {
		return (PushResVO) select("SP_CMS_SERVICE_GET_PUSH_STAGE_DETAIL", param);
	}
	
	public PushSendVO sendPush(PushSendVO param) throws DataAccessException{
		select("SP_CMS_SERVICE_SEND_PUSH", param);
		return param;
	}
	
	public List<?> retrievePushResultList(ServiceEventVO param) throws DataAccessException {
		return (List<?>) list("SP_CMS_SERVICE_GET_PUSH_RESULT_LIST", param);
	}
	
	public List<?> retrievePushResultDtlList(ServiceEventVO param) throws DataAccessException {
		return (List<?>) list("SP_CMS_SERVICE_GET_PUSH_RESULT_DTL", param);
	}
	
}
