package com.mogujie.service.redis.msg.pubsub.client;


import com.mogujie.service.redis.msg.pubsub.AbstractRedisHolder;

import java.util.List;

/**
 * Created by zhizhu on 15/11/24.
 */
abstract public class AbstractMessageClient extends AbstractRedisHolder implements MessageClient {

    public final static Boolean AUTO_KNOWLEDGE = true;

    public final static Boolean UN_AUTO_KNOWLEDGE = false;

    abstract public void pub(String channel, String message);

    abstract public void close(String channel);

    abstract public void sub(String channel);

    abstract public void unsubscribe(String channel);

    abstract public void ack(String message);

    abstract public List<String> consume(Integer limit);

    abstract public void someAck(List<String> message);

    abstract public Long count();
}
