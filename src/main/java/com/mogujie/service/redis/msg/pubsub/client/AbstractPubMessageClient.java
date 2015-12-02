package com.mogujie.service.redis.msg.pubsub.client;


import java.util.List;

/**
 * Created by zhizhu on 15/11/20.
 */
abstract public class AbstractPubMessageClient extends AbstractMessageClient {


    /**
     *
     * @param channel
     * @param message
     *
     * publish message into the specify channel
     *
     */
    abstract public void pub(String channel,String message);

    /**
     *
     * @param channel
     *
     * stop pub sever
     */
    abstract public void close(String channel);


    /**
     *
     * @param channel
     *
     * subscribe specify channel
     */
    @Override
    public void sub(String channel) {

    }

    /**
     *
     * @param channel
     *
     *  cancel subscribe channel.
     */
    @Override
    public void unsubscribe(String channel) {

    }

    @Override
    public void ack(String message) {

    }

    @Override
    public void someAck(List<String> message) {

    }

    @Override
    public List<String> consume(Integer limit) {
        return null;
    }

    @Override
    public Long count() {
        return null;
    }
}
