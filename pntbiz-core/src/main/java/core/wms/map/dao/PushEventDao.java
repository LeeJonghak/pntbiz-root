package core.wms.map.dao;

import framework.db.dao.BaseDao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 푸시 이벤트 관련
 *
 * @author nsyun 2015-03-25
 */
@Repository
public class PushEventDao extends BaseDao {

    /**
     * 할당 가능한 이벤트 목록
     * @param param firstItemNo, pageSize
     * @return
     */
    public List<?> callCmsServicePushStageList(HashMap<String, Object> param) {

        List<?> list = list("callCmsServicePushStageList", param);

        return list;

    }

    /**
     * 비콘 이벤트 관련
     *
     * @param param pnsStageSeqNo
     * @return
     */
    public void callCmsServiceSetBeaconPushSrInfo(HashMap<String, Object> param) {
        select("callCmsServiceSetBeaconPushSrInfo", param);
    }

    /**
     *
     * @param param PNS_ID
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap<String, Object> getPnsSrInfo(HashMap<String, Object> param) {
        return (HashMap<String, Object>)select("getPnsSrInfo", param);
    }

}
