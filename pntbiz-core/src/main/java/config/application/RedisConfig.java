package config.application;

import framework.socket.DefaultMessageDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis Clustet Confit
 *
 *
 * Created by ucjung on 2017-04-18.
 */

@Configuration
@Component
public class RedisConfig {
    static final private Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("#{'${redis.cluster.node.host}'.split(',')}")
    private List<String> redisNodeHosts;

    @Value("#{'${redis.cluster.node.port}'.split(',')}")
    private List<Integer> redisNodePorts;

    @Value("${redis.cluster.pool.maxtotal}")
    private int maxTotal;

    @Value("${redis.cluster.pool.idle}")
    private int maxIdle;

    @Value("${redis.cluster.pool.maxwaitmillis}")
    private int maxWaitMillis;

    @Value("${redis.single.node.password}")
    private String password;

    private RedisClusterConfiguration getRedisClusterConfiguration() {

        List<String> clusterNodes = new ArrayList<>(redisNodeHosts.size());

        logger.info("#============================================> [START] Config Redis Cluster node ");

        for (int i = 0; i < redisNodeHosts.size() ; i ++) {
            logger.info("Redis Node ==> {}", redisNodeHosts.get(i) + ":" + redisNodePorts.get(i));
            clusterNodes.add(redisNodeHosts.get(i) + ":" + redisNodePorts.get(i));
        }

        logger.info("#============================================> [END  ] Config Redis Cluster node ");

        return new RedisClusterConfiguration(clusterNodes);
    }

    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        return jedisPoolConfig;
    }

    @Bean
    JedisConnectionFactory jedisConnFactory(){

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        if(redisNodeHosts.size() > 1) {
            jedisConnectionFactory = new JedisConnectionFactory(getRedisClusterConfiguration());
            jedisConnectionFactory.setPoolConfig(getJedisPoolConfig());
        } else {
            jedisConnectionFactory = new JedisConnectionFactory();
            jedisConnectionFactory.setHostName(redisNodeHosts.get(0));
            jedisConnectionFactory.setPort(redisNodePorts.get(0));
            jedisConnectionFactory.setPassword(password);
        }

        return jedisConnectionFactory;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

        template.setConnectionFactory(jedisConnFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);

        return template;
    }

    @Bean
    RedisCachePrefix redisCachePrefix() {
        return new DefaultRedisCachePrefix("_");
    }

    @Bean
    RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());

        redisCacheManager.setDefaultExpiration(1000);
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setCachePrefix(redisCachePrefix());

        return redisCacheManager;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer() {

        RedisMessageListenerContainer listeners = new RedisMessageListenerContainer();
        listeners.setConnectionFactory( jedisConnFactory());

        MessageListenerAdapter listener = new MessageListenerAdapter( new DefaultMessageDelegate());
        listener.setSerializer( new StringRedisSerializer());
        listener.afterPropertiesSet();

        listeners.addMessageListener( listener, new PatternTopic( "*" ) );
        listeners.afterPropertiesSet();

        return  listeners;
    }

}
