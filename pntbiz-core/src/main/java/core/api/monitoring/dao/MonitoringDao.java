package core.api.monitoring.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import framework.db.dao.RedisBaseDao;

@Repository
public class MonitoringDao extends RedisBaseDao {

    public void setRedisInfo(String key, Object param) {
        this.hmset(key, this.obj2map(param));
    }

    public Map<Object, Object> getRedisInfo(String key) {
        return this.hgetall(key);
    }

    public boolean expireRedis(String key, int expr) {
        return this.expire(key, expr);
    }
}
