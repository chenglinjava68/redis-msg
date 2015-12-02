package com.mogujie.service.redis.msg.pubsub.client;

import java.util.List;

/**
 * Created by zhizhu on 15/11/20.
 */
public interface MessageClient {

    /**
     *
     * <p>
     *     publish message into the specify channel
     * </p>
     *
     * @param channel   channel name. unique id.
     * @param message   send message content.
     */
    public void pub(String channel, String message);

    /**
     * <p>
     *     stop pub sever
     * </p>
     */
    public void close(String channel);


    /**
     *
     * <p>
     *     subscribe specify channel
     * </p>
     *
     * @param channel   channel name. need subscribe channel.
     */
    public void sub(String channel);

    /**
     *
     * <p>
     *    use this method can cancel used subscribe channel
     * </p>
     *
     * @param channel   channel name. cancel subscribe channel.
     */
    public void unsubscribe(String channel);

    /**
     * <p>
     *    acknowledge message
     * </p>
     */
    public void ack(String message);

    /**
     * <p>
     *     batch ack message
     * </p>
     * @param message
     */
    public void someAck(List<String> message);

    /**
     *
     * <p>
     *    consume message
     * </p>
     *
     * @param limit
     * @return
     */
    public List<String> consume(Integer limit);

    /**
     * <p>
     *     get the messages total number
     * </p>
     *
     * @return
     */
    public Long count();

}
