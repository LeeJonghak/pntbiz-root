package api.ism.dao;

import framework.db.dao.RedisBaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class IsmRedisDao extends RedisBaseDao {

    public String SOS_SEND_PREFIX = "SOS_SEND_";

    public Map<Object, Object> getSosSendData(String key) {
        return this.hgetall(key);
    }

}
