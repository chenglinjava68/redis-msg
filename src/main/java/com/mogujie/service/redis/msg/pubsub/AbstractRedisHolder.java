package com.mogujie.service.redis.msg.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhizhu on 15/11/20.
 */
abstract public class AbstractRedisHolder {

    protected Jedis getResource() {
        return RedisHodler.pool.getResource();
    }

    private static class RedisHodler {
        private static JedisPool pool;
        private static Properties prop;
        private static FileInputStream inputStream;
        static {
            prop = new Properties();

            try {

                String filePath = Thread.currentThread().getContextClassLoader().getResource("redis-config.properties").getPath();
                /******** 读取prop配置 ********/
                inputStream = new FileInputStream(filePath);
                prop.load(inputStream);
                inputStream.close();
                inputStream = null;

                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(Integer.parseInt(prop.getProperty("maxTotal")));
                config.setMaxWaitMillis(Integer.parseInt(prop.getProperty("maxWait")));
                config.setMaxIdle(Integer.parseInt(prop.getProperty("maxIdle")));
                config.setMinIdle(Integer.parseInt(prop.getProperty("minIdle")));

                config.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("testOnBorrow")));
                config.setTestOnReturn(Boolean.parseBoolean(prop.getProperty("testOnReturn")));
                config.setTestWhileIdle(Boolean.parseBoolean(prop.getProperty("testWhileIdle")));

                config.setNumTestsPerEvictionRun(Integer.parseInt(prop.getProperty("numTestsPerEvictionRun")));
                config.setMinEvictableIdleTimeMillis(Integer.parseInt(prop.getProperty("minEvictableIdleTimeMillis")));
                config.setTimeBetweenEvictionRunsMillis(Integer.parseInt(prop.getProperty("timeBetweenEvictionRunsMillis")));


                //pubsub
                pool = new JedisPool(config,prop.getProperty("pub_sub_jedis_host"),Integer.parseInt(prop.getProperty("pub_sub_jedis_port")));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
