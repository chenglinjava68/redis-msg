package com.mogujie.service.redis.msg.pubsub.client.impl;

import com.mogujie.service.redis.msg.constants.Constants;
import com.mogujie.service.redis.msg.pubsub.client.AbstractPubMessageClient;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by zhizhu on 15/11/18.
 *
 * redis pub/sub : publish message client and persist message(ensure subscriber can persist subscribe)
 *
 */
public class PubClient extends AbstractPubMessageClient {

    private Jedis jedis;

    public PubClient(){
//        jedis = super.getResource();
    }

    /**
     *
     * <p>
     *    publish everyone message will be persist in subscriber's queue.
     *
     * </p>
     * @param message
     *
     * @see  SubListenerClient
     */
    private void put(String channel,String message){
        Set<String> subClients = getJedis().smembers(Constants.SUBSCRIBE_CENTER);
        for(String clientKey : subClients){

            if(clientKey.indexOf(Constants.MESSAGE_SEPARATOR)!=-1) {

                //clientKey: clientId/channel 格式
                String[] splitClientKeyArray = clientKey.split(Constants.MESSAGE_SEPARATOR);

                if(splitClientKeyArray.length==2) {
                    String storeChannel = splitClientKeyArray[1];
                    //push message to the same channel
                    if(channel.equals(storeChannel)) {
                        getJedis().rpush(clientKey, message);
                    }
                }
            }
        }
    }

    @Override
    public void pub(String channel,String message){
        try {
            this.put(channel,message);
            getJedis().publish(channel, message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
            this.jedis = null;
        }
    }

    @Override
    public void close(String channel){

        try {
            jedis.publish(channel, "quit");
            jedis.del(channel);//删除
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
            this.jedis = null;
        }
    }

    private Jedis getJedis() {
        if(jedis==null) {
            this.jedis = super.getResource();
        }
        return jedis;
    }
}
