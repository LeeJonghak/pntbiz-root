package core.api.common.dao;

import core.api.common.domain.SmsSendLog;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 */
@Repository
public class SmsSendLogDao extends BaseDao {

    public SmsSendLog getSmsSendLogInfo(Map<String, Object> param) {
        return (SmsSendLog) select("getSmsSendLogInfo", param);
    }

    public void insertSmsSendLog(SmsSendLog vo) {
        insert("insertSmsSendLog", vo);
    }

    public int updateSmsSendLog(SmsSendLog vo) {
        return update("updateSmsSendLog", vo);
    }

}
