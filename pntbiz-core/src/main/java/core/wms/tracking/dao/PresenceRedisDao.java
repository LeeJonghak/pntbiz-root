package core.wms.tracking.dao;

import framework.db.dao.RedisBaseDao;
import framework.web.util.DateUtil;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 */
@Repository
public class PresenceRedisDao extends RedisBaseDao {

    public List<Map<Object, Object>> findRedisItem(String keyPattern) {
        Set<Object> keys = this.keys(keyPattern+"*");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        for(Object keyObject: keys) {
            String key = (String)keyObject;
            Map<Object, Object> item = this.hgetall(key);

            list.add(item);
        }

        return list;
    }

    public List<String> findRedisString(String keyPattern) {
        Set<Object> keys = this.keys(keyPattern+"*");
        List<String> list = new ArrayList<String>();
        for(Object keyObject: keys) {
            String key = (String)keyObject;
            String str = (String)this.get(key);

            list.add(str);
        }

        return list;
    }

    public List<Map<Object, Object>> getPreBeaconList(String uuid) {

        Set<Object> keys = this.keys("PRESENCE_BEACON_"+uuid+"*");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        for(Object keyObject: keys) {
            String key = (String)keyObject;
            Map<Object, Object> item = this.hgetall(key);

            if(item.containsKey("lastTime")) {
                int timestamp = Integer.parseInt(String.valueOf(item.get("lastTime")));
                String datetime = DateUtil.timestamp2str(timestamp, "yyyy/MM/dd HH:mm:ss");
                item.put("lastTime2", datetime);
            }
            list.add(item);
        }

        return list;
    }

}
