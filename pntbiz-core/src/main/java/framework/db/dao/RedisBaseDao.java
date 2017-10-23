package framework.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis Base dao
 * create by jhlee 17.04.05
 * history
 * 17.04.05 기본 구조 작성 - jedis가 아닌 redis의 명령어를 사용하도록 구성
 *          레디스의 데이터(구조)를 아래의 오퍼레이션이라는 형태로 가져와서 사용함.
 *          ValueOperation = set, get....
 *          ListOperation = lpush, rpush, lpop, rpop....
 *          ZsetOperation = intersect union reverse range add score .....
 *          HashOperation = hset hmset ...
 *          HyperLogLogOperations = 사용안해봄...... *
 * 17.04.06 lpop rpop의 delay가 동작안함. 원인파악 중
 * 17.04.07 sdiff, sinter 등등 함수는 테스트가 필요. 테스트 중
 * 17.04.11 파이프라인/트랜잭션 검토
 * 17.04.13 ltrim lrem lset 추가 / lpush rpush 등등 리턴타입 변경
 * 17.04.14 obj2map 추가 / 기존에 사용하는 도메인을 map으로 변경할 수 있도록 처리
 */

public class RedisBaseDao {

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setEnableTransactionSupport(true);
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    }
    */

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 작성중..
     * @return
     */
    public List<Object> transaction() {
        redisTemplate.multi();
        redisTemplate.exec();
        redisTemplate.discard();
        return null;
    }

    public Map<Object, Object> obj2map(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<Object, Object> map = new HashMap<Object, Object>();
        for(int i=0; i<fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                String key = fields[i].getName();
                String value = "";
                try {
                    if (!(fields[i].get(obj).equals(null) || fields[i].get(obj).equals(""))) {
                        value = fields[i].get(obj).toString();
                    }
                } catch(NullPointerException e) {
                    //e.printStackTrace();
                }
                map.put(key, value);
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public Object get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value;
    }
    public void del(String key) {
        redisTemplate.delete(key);
    }
    public void hset(Object key, Object hkey, Object hvalue) {
        redisTemplate.opsForHash().put(key, hkey, hvalue);
    }
    public Object hget(Object key, Object hkey) {
        Object value = redisTemplate.opsForHash().get(key, hkey);
        return value;
    }
    public void hdel(Object key, Object hkey) {
        redisTemplate.opsForHash().delete(key, hkey);
    }
    public void hmset(Object key, Map<Object, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }
    public List<Object> hmget(Object key, List<Object> fieldList) {
        List<Object> list = redisTemplate.opsForHash().multiGet(key, fieldList);
        return list;
    }
    public Map<Object, Object> hgetall(Object key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return map;
    }
    public Set<Object> keys(Object key) {
        Set<Object> set = redisTemplate.keys(key);
        return set;
    }
    public boolean expire(Object key, Integer intsec) {
        Long sec = Long.valueOf(intsec.longValue());
        return redisTemplate.expire(key, sec, TimeUnit.SECONDS);
    }
    public boolean expire(Object key, Long sec) {
        return redisTemplate.expire(key, sec, TimeUnit.SECONDS);
    }
    public boolean expire(Object key, Long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }
    public Long lpush(Object key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }
    //    public Long lpush(Object key, Object... value) {
//        return redisTemplate.opsForList().leftPushAll(key, value);
//    }
    public Long lpush(Object key, List<Object> valueList) {
        return redisTemplate.opsForList().leftPushAll(key, valueList);
    }
    public List<Object> lrange(Object key, Integer intStartIndex, Integer intEndIndex) {
        Long startIndex = Long.valueOf(intStartIndex.longValue());
        Long endIndex = Long.valueOf(intEndIndex.longValue());
        return redisTemplate.opsForList().range(key, startIndex, endIndex);
    }
    public List<Object> lrange(Object key, Long startIndex, Long endIndex) {
        return redisTemplate.opsForList().range(key, startIndex, endIndex);
    }
    public Object lindex(Object key, Integer intindex) {
        Long index = Long.valueOf(intindex.longValue());
        return redisTemplate.opsForList().index(key, index);
    }
    public Object lindex(Object key, Long index) {
        return redisTemplate.opsForList().index(key, index);
    }
    public Long llen(Object key) {
        return redisTemplate.opsForList().size(key);
    }
    // 해당 index의 value를 변경
    public void lset(Object key, Integer intindex, Object value) {
        Long index = Long.valueOf(intindex.longValue());
        redisTemplate.opsForList().set(key, index, value);
    }
    public void lset(Object key, Long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }
    // 해당 값(value)을 num만큼 제거
    public Long lrem(Object key, Integer intnum, Object value) {
        Long num = Long.valueOf(intnum.longValue());
        return redisTemplate.opsForList().remove(key, num, value);
    }
    public Long lrem(Object key, Long num, Object value) {
        return redisTemplate.opsForList().remove(key, num, value);
    }
    // 해당 index를 제외한 나머지를 제거
    public void ltrim(Object key, Integer intStartIndex, Integer intEndIndex) {
        Long startIndex = Long.valueOf(intStartIndex.longValue());
        Long endIndex = Long.valueOf(intEndIndex.longValue());
        redisTemplate.opsForList().trim(key, startIndex, endIndex);
    }
    public void ltrim(Object key, Long startIndex, Long endIndex) {
        redisTemplate.opsForList().trim(key, startIndex, endIndex);
    }
    public Object lpop(Object key) {
        return redisTemplate.opsForList().leftPop(key);
    }
    public Object lpop(Object key, Integer intsec) {
        Long sec = Long.valueOf(intsec.longValue());
        return redisTemplate.opsForList().leftPop(key, sec, TimeUnit.SECONDS);
    }
    public Object lpop(Object key, Long sec) {
        return redisTemplate.opsForList().leftPop(key, sec, TimeUnit.SECONDS);
    }
    public Object lpop(Object key, Long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForList().leftPop(key, timeout, timeUnit);
    }
    public Long rpush(Object key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
    //    public Long rpush(Object key, Object... value) {
//        return redisTemplate.opsForList().rightPushAll(key, value);
//    }
    public Long rpush(Object key, List<Object> valueList) {
        return redisTemplate.opsForList().rightPushAll(key, valueList);
    }
    public Object rpop(Object key) {
        return redisTemplate.opsForList().rightPop(key);
    }
    public Object rpop(Object key, Integer intsec) {
        Long sec = Long.valueOf(intsec.longValue());
        return redisTemplate.opsForList().rightPop(key, sec, TimeUnit.SECONDS);
    }
    public Object rpop(Object key, Long sec) {
        return redisTemplate.opsForList().rightPop(key, sec, TimeUnit.SECONDS);
    }
    public Object rpop(Object key, Long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForList().rightPop(key, timeout, timeUnit);
    }
    public Long sadd(Object key, Object value) {
        Long cnt = redisTemplate.opsForSet().add(key, value);
        return cnt;
    }
    //    public Long sadd(Object key, Object... value) {
//        Long cnt = redisTemplate.opsForSet().add(key, value);
//        return cnt;
//    }
    public Long scard(Object key) {
        Long cnt = redisTemplate.opsForSet().size(key);
        return cnt;
    }
    public Set<Object> smembers(Object key) {
        Set<Object> set = redisTemplate.opsForSet().members(key);
        return set;
    }
    public Long srem(Object key, Object value) {
        Long cnt = redisTemplate.opsForSet().remove(key, value);
        return cnt;
    }
    //    public Long srem(Object key, Object... value) {
//        Long cnt = redisTemplate.opsForSet().remove(key, value);
//        return cnt;
//    }
    // data = key1 - key2
    public Set<Object> sdiff(Object key1, Object key2) {
        Set<Object> set = redisTemplate.opsForSet().difference(key1, key2);
        return set;
    }
    // data = key - keyList (동작안함)
    public Set<Object> sdiff(Object key, List<Object> keyList) {
        Set<Object> set = redisTemplate.opsForSet().difference(key, keyList);
        return set;
    }
    public Long sdiffstore(Object key, Object key1, Object key2) {
        return redisTemplate.opsForSet().differenceAndStore(key, key1, key2);
    }
    public Long sdiffstore(Object key, List<Object> keyList, Object key1) {
        return redisTemplate.opsForSet().differenceAndStore(key, keyList, key1);
    }
    public Set<Object> sinter(Object key1, Object key2) {
        Set<Object> set = redisTemplate.opsForSet().intersect(key1, key2);
        return set;
    }
    public Set<Object> sinter(Object key1, List<Object> keyList) {
        Set<Object> set = redisTemplate.opsForSet().intersect(key1, keyList);
        return set;
    }
    // key = key1 ∩ key2
    public Long sinterstore(Object key, Object key1, Object key2) {
        return redisTemplate.opsForSet().intersectAndStore(key, key1, key2);
    }
    // key = key1 ∩ keyList
    public Long sinterstore(Object key, List<Object> keyList, Object key1) {
        return redisTemplate.opsForSet().intersectAndStore(key, keyList, key1);
    }
    public Set<Object> sunion(Object key1, Object key2) {
        Set<Object> set = redisTemplate.opsForSet().union(key1, key2);
        return set;
    }
    public Set<Object> sunion(Object key1, List<Object> keyList) {
        Set<Object> set = redisTemplate.opsForSet().union(key1, keyList);
        return set;
    }
    // key = key1 ∪ key2
    public Long sunionstore(Object key, Object key1, Object key2) {
        return redisTemplate.opsForSet().unionAndStore(key, key1, key2);
    }
    // key = key1 ∪ keyList
    public Long sunionstore(Object key, List<Object> keyList, Object key1) {
        return redisTemplate.opsForSet().unionAndStore(key, keyList, key1);
    }
}
