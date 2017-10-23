package wms.map.service;

import wms.component.auth.LoginDetail;

import java.util.List;

/**
 */
public interface PushEventService {

    public List<?> callCmsServicePushStageList(int firstItemNo, int pageSize, int totalCnt);
    public void assignBeaconEvent(LoginDetail loginDetail, long beaconNum, int pnsStageSeqNo) throws Exception;

}
