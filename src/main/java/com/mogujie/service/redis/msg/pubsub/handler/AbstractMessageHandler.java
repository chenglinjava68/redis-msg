package com.mogujie.service.redis.msg.pubsub.handler;

import com.mogujie.service.redis.msg.constants.Constants;
import com.mogujie.service.redis.msg.pubsub.AbstractRedisHolder;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by zhizhu on 15/11/20.
 */
abstract public class AbstractMessageHandler extends AbstractRedisHolder {

    public String queueName;

    public void specifyQueue(String queueName) {
        this.queueName = queueName;
    }

    public void ack(String message) {
        /**
         *
         * 时间复杂度中N表示链表中元素的数量。在指定Key关联的链表中，删除前count个值等于value的元素。
         * 如果count大于0，从头向尾遍历并删除，如果count小于0，则从尾向头遍历并删除。如果count等于0，
         * 则删除链表中所有等于value的元素。如果指定的Key不存在，则直接返回0。
         *
         */
        Jedis jedis = super.getResource();
        try {
            jedis.lrem(queueName, 1, message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public List<String> consume(Integer limit,boolean auto) {
        return null;
    }

    public Long count() {
        Jedis jedis = getResource();
        Long count = 0L;
        try {
            if(getResource().smembers(Constants.SUBSCRIBE_CENTER).contains(queueName)) {

                count = jedis.llen(queueName);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        } finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }
}
