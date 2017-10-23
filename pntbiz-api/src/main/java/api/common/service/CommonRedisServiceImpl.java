package api.common.service;

import core.api.common.dao.CommonRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jhlee on 2017-04-15.
 */
@Service
public class CommonRedisServiceImpl implements CommonRedisService {

    @Autowired
    private CommonRedisDao commonRedisDao;

    @Override
    public boolean sendCheck(String key, Map<Object, Object> map, Integer interval, Integer removeSec) {
        return commonRedisDao.sendCheck(key, map, interval, removeSec);
    }
}
