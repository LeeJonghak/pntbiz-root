package core.api.common.dao;

import framework.db.dao.RedisBaseDao;
import framework.web.util.DateUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhlee on 2017-04-16.
 */
@Repository
public class CommonRedisDao extends RedisBaseDao {
    /**
     * 전송주기 체크
     * @param key
     * @param data
     * @param interval  전송주기 sec
     * @param removeSec 데이터 입력 후 expire sec
     * @return
     */
    public boolean sendCheck(String key, Map<Object, Object> data, Integer interval, Integer removeSec) {
        Long time = DateUtil.str2timestamp(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        // ism에 sendTime을 보내면 오류 생김. 제거
        // data.put("sendTime", time);
        Map<Object, Object> timeData = new HashMap<Object, Object>();
        timeData.put("sendTime", time);
        Map<Object, Object> keyData = this.hgetall(key);
        if(keyData.isEmpty()) {
            this.hmset(key, data);
            this.hmset(key, timeData);  // data.put하면 ism에서 오류
            if(removeSec > 0) {
            	this.expire(key, removeSec);
            }
            return true;
        } else {
            Long sendTime = Long.parseLong(keyData.get("sendTime").toString());
            if(time - sendTime >= interval) {
                this.hmset(key, data);
                if(removeSec > 0) {
                	this.expire(key, removeSec);
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
