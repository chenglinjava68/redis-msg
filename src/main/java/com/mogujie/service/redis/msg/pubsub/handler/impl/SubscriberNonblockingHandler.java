package com.mogujie.service.redis.msg.pubsub.handler.impl;

import com.mogujie.service.redis.msg.pubsub.handler.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhizhu on 15/11/19.
 */
public class SubscriberNonblockingHandler extends AbstractMessageHandler {

    private Logger logger = LoggerFactory.getLogger(SubscriberNonblockingHandler.class);

    private Jedis jedis;

    public SubscriberNonblockingHandler() {
//        this.jedis = super.getResource();
    }

    @Override
    public List<String> consume(Integer limit,boolean autoAck) {
        try {
            if(autoAck) {
                //1.auto acknowledge
                return lpopMessage(limit);
            } else {
                //2.acknowledge when client receive message
                return getMessage(limit);
            }
        } finally {
            if(jedis!=null) {
                jedis.close();
                this.jedis=null;
            }
        }
    }

    private List<String> getMessage(Integer limit) {

        List<String> result = new ArrayList<String>();

        if(limit>0) {
            result = getJedis().lrange(queueName, 0, limit);
        }
        return result;
    }

    private List<String> lpopMessage(Integer limit) {

        List<String> result = new ArrayList<String>();

        if(limit>0) {
            for(int i = 0 ; i < limit ; i++){
                String value = getJedis().lpop(queueName);
                if(value==null) {
                    break;
                }
                result.add(value);
            }
        }
        return result;
    }

    private Jedis getJedis() {
        if(this.jedis==null) {
            this.jedis = super.getResource();
        }
        return this.jedis;
    }
}

