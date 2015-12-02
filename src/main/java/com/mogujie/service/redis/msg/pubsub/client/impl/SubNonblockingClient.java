package com.mogujie.service.redis.msg.pubsub.client.impl;

import com.mogujie.service.redis.msg.constants.Constants;
import com.mogujie.service.redis.msg.pubsub.client.AbstractSubMessageClient;
import com.mogujie.service.redis.msg.pubsub.handler.AbstractMessageHandler;
import com.mogujie.service.redis.msg.pubsub.handler.impl.SubscriberNonblockingHandler;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by zhizhu on 15/11/19.
 */
public class SubNonblockingClient<T extends AbstractMessageHandler> extends AbstractSubMessageClient {

    private String clientId;
    private Jedis jedis;
    private T handler;

    private boolean autoAck = AUTO_KNOWLEDGE;

    /**
     * use default handler
     */
    public SubNonblockingClient(String clientId) {
//        this.clientId = GenerateChannelIdUtil.generateChannelId();
        this.clientId = clientId;
//        jedis = super.getResource();
        this.handler = (T) new SubscriberNonblockingHandler();
    }

    public SubNonblockingClient(String clientId,boolean autoAck) {
//        this.clientId = GenerateChannelIdUtil.generateChannelId();
        this.clientId = clientId;
//        jedis = super.getResource();
        this.handler = (T) new SubscriberNonblockingHandler();
        this.autoAck = autoAck;
    }

    /**
     * stop the handle input parameter.Unified processing
     * @param channel
     *
     */
//    public SubNonblockingClient(T handler) {
//        this.clientId = GenerateChannelIdUtil.generateChannelId();
//        jedis = super.getResource();
//        this.handler = handler;//new SubscriberNonblockingHandler(super.getResource());
//    }

    @Override
    public void sub(String channel) {
        try {
            String key = clientId+ Constants.MESSAGE_SEPARATOR + channel;

            handler.specifyQueue(key);

            boolean exist = getJedis().sismember(Constants.SUBSCRIBE_CENTER, key);
            if(!exist){
                getJedis().sadd(Constants.SUBSCRIBE_CENTER, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null) {
                jedis.close();
                this.jedis = null;
            }
        }
    }

    @Override
    public void unsubscribe(String channel) {

        try {
            String key = clientId + Constants.MESSAGE_SEPARATOR + channel;
            //remove from subscribe set
            getJedis().srem(Constants.SUBSCRIBE_CENTER, key);
            //del subscriber queue(list)
            getJedis().del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null) {
                jedis.close();
                this.jedis = null;
            }
        }
    }

    @Override
    public void ack(String message) {
        if(!autoAck) {
            handler.ack(message);
        }
    }

    @Override
    public void someAck(List<String> messageList) {
        if(!autoAck) {
            if(messageList!=null&&messageList.size()>0) {
                for (String message : messageList) {
                    if(!autoAck) {
                        handler.ack(message);
                    }
                }
            }
        }
    }

    public List<String> consume(Integer limit) {
        return handler.consume(limit,autoAck);
    }

    @Override
    public Long count() {
        return handler.count();
    }

    public String getClientId() {
        return clientId;
    }

    private Jedis getJedis() {
        if(jedis==null) {
            this.jedis = super.getResource();
        }
        return jedis;
    }
}
