package wms.component.scheduler.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import framework.db.dao.RedisBaseDao;
import core.wms.monitoring.domain.ScannerRedisParam;

@Repository
public class SchedulerRedisDao extends RedisBaseDao {

    public void setScannerInfo(ScannerRedisParam param) {
        this.hmset(param.getKey(), this.obj2map(param));
    }

    public Map<Object, Object> getRedisInfo(String key) {
        return this.hgetall(key);
    }

    public boolean expireRedis(String key, int expr) {
        return this.expire(key, expr);
    }
}
