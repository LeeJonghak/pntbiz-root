package core.wms.monitoring.dao;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import framework.db.dao.RedisBaseDao;

@Repository
public class MonitoringDao extends RedisBaseDao {

    public Map<Object, Object> getRedisInfo(String key) {
        return this.hgetall(key);
    }

    public Set<Object> getRedisScanners(String keys) {
        return this.keys(keys);
    }
}
