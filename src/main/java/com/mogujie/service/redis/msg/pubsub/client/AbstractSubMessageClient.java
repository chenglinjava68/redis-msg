package com.mogujie.service.redis.msg.pubsub.client;

import java.util.List;

/**
 * Created by zhizhu on 15/11/19.
 */
abstract public class AbstractSubMessageClient extends AbstractMessageClient {

    /**
     *
     * @param channel
     *
     * subscribe specify channel
     */
    abstract public void sub(String channel);

    /**
     *
     * @param channel
     *
     *  cancel subscribe channel.
     */
    abstract public void unsubscribe(String channel);

    /**
     *  acknowledge message
     */
    abstract public void ack(String message);

    /**
     * batch ack message
     * @param message
     */
    abstract public void someAck(List<String> message);

    /**
     * consume message
     * @param limit
     * @return
     */
    abstract public List<String> consume(Integer limit);

    /**
     * get message total number
     *
     * @return
     */
    abstract public Long count();

    /**
     *
     * @param channel
     * @param message
     *
     * publish message into the specify channel
     *
     */
    public void pub(String channel,String message){};

    /**
     *
     * @param channel
     *
     * stop pub sever
     */
    public void close(String channel){};



}
