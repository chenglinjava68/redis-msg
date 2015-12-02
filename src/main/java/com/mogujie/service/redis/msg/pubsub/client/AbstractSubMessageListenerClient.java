package com.mogujie.service.redis.msg.pubsub.client;

import java.util.List;

/**
 * Created by zhizhu on 15/11/19.
 */
abstract public class AbstractSubMessageListenerClient extends AbstractSubMessageClient {

    /**
     * consume message
     * @param limit
     * @return
     */
    public List<String> consume(Integer limit) {
        return null;
    }

    @Override
    public void someAck(List<String> message) {

    }
}
