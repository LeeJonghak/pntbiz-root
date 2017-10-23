package wms.map.service;

import wms.component.auth.LoginDetail;
import core.wms.map.dao.MyContentsDao;
import core.wms.map.dao.PushEventDao;
import core.wms.map.domain.ContentsMapping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 */
@Service
public class PushEventServiceImpl implements PushEventService {

    @Autowired
    private PushEventDao pushEventDao;

    @Autowired
    private MyContentsDao myContentsDao;

    public List<?> callCmsServicePushStageList(int firstItemNo, int pageSize, int totalCnt) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("firstItemNo", firstItemNo);
        param.put("pageSize", pageSize);
        List<?> list = pushEventDao.callCmsServicePushStageList(param);

        totalCnt = (Integer)param.get("TOT_CNT");

        return list;
    }

    @Override
    public void assignBeaconEvent(LoginDetail loginDetail, long beaconNum, int pnsStageSeqNo) throws Exception {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("pnsStageSeqNo", pnsStageSeqNo);
        pushEventDao.callCmsServiceSetBeaconPushSrInfo(param);
        String retCode = (String)param.get("RET_CODE");
        if(!StringUtils.equals(retCode, "0000")) {
            throw new Exception((String)param.get("RET_MSG"));
        } else {
            Integer pnsId = (Integer) param.get("O_PNS_ID");

            HashMap<String, Object> pnsParam = new HashMap<String, Object>();
            pnsParam.put("PNS_ID", pnsId);
            HashMap<String, Object> pnsSrInfo = pushEventDao.getPnsSrInfo(pnsParam);

            if(pnsSrInfo==null) {
                throw new Exception("NOT FOUND PNS_ID:"+pnsId);
            } else {

                long conNum = (Long)pnsSrInfo.get("CONNUM");
                ContentsMapping vo = new ContentsMapping();
                vo.setConNum(conNum);
                vo.setRefNum(beaconNum);
                vo.setRefType("BC");
                vo.setRefSubType("EVENT");
                vo.setEvtNum(pnsId);
                myContentsDao.insertContentMapping(vo);

            }
        }

    }

}
