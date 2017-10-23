package api.common.service;

import java.util.Map;

/**
 * Created by jhlee on 2017-04-15.
 */
public interface CommonRedisService {
    public boolean sendCheck(String key, Map<Object, Object> map, Integer interval, Integer removeSec);
}
