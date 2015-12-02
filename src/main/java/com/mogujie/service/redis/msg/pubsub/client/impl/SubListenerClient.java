package com.mogujie.service.redis.msg.pubsub.client.impl;

import com.mogujie.service.redis.msg.pubsub.client.AbstractSubMessageListenerClient;
import com.mogujie.service.redis.msg.pubsub.listener.AbstractMessageListener;
import redis.clients.jedis.Jedis;


/**
 * Created by zhizhu on 15/11/20.
 */
public class SubListenerClient<T extends AbstractMessageListener> extends AbstractSubMessageListenerClient {

    private Jedis jedis;
    private T listener;

    private boolean autoAck = AUTO_KNOWLEDGE;

    public SubListenerClient(T listener){
        jedis = super.getResource();
        this.listener = listener;
    }

    public SubListenerClient(T listener,boolean autoAck){
        this.autoAck = autoAck;
        jedis = super.getResource();
        this.listener = listener;
        this.listener.setAutoAck(autoAck);
    }

    @Override
    public void sub(String channel){
        listener.specifyQueueName(channel);

        //consume backlog message before subscribe channel
        while (true) {
            Long messages = jedis.llen(listener.getQueueName());

            if(messages==0) {
                break;
            }

            String lastMessage = jedis.lindex(listener.getQueueName(),messages-1);
            listener.onMessage(channel,lastMessage);
        }

        jedis.subscribe(listener, channel);
    }

    @Override
    public void unsubscribe(String channel){
        listener.unsubscribe(channel);
    }

    @Override
    public void ack(String message) {
        if(!autoAck) {
            listener.ack(message);
        }
    }

    @Override
    public Long count() {
        return listener.count();
    }
}
