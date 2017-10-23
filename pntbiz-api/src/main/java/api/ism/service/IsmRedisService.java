package api.ism.service;

import java.util.Map;
/**
 * Created by jhlee on 2017-04-16.
 */
public interface IsmRedisService {
    public String getSosSendPrefix();
    public Map<Object, Object> getSosSendData(String key);
    public void deleteSosSendData(String key);
}
