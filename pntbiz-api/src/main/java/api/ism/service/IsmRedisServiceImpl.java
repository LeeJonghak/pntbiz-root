package api.ism.service;

import api.ism.dao.IsmRedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jhlee on 2017-04-16.
 */
@Service
public class IsmRedisServiceImpl implements IsmRedisService {

    @Autowired
    private IsmRedisDao ismRedisDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getSosSendPrefix() {
        return ismRedisDao.SOS_SEND_PREFIX;
    }
    @Override
    public Map<Object, Object> getSosSendData(String key) {
        return ismRedisDao.getSosSendData(key);
    }
    @Override
    public void deleteSosSendData(String key) {
        ismRedisDao.del(key);
    }
}
