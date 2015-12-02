package com.mogujie.service.redis.msg.pubsub.listener;

import com.mogujie.service.redis.msg.constants.Constants;
import com.mogujie.service.redis.msg.pubsub.handler.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


/**
 * Created by zhizhu on 15/11/20.
 */
abstract public class AbstractMessageListener extends JedisPubSub {

    private Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);

    private String clientId;
    private PubSubHandler handler;
    private boolean autoAck = true;

    public AbstractMessageListener(String clientId){
//        this.clientId = GenerateChannelIdUtil.generateChannelId();
        this.clientId = clientId;
        handler = new PubSubHandler();
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public void specifyQueueName(String channel) {
        String key = clientId + Constants.MESSAGE_SEPARATOR + channel;
        handler.specifyQueue(key);
    }

    public String getQueueName() {
        return handler.queueName;
    }


    /**
     * handle illegal message
     * @param channel
     * @param message
     */
    abstract public void illegalMessage(String channel,String message);

    /**
     * handle normal message
     * @param channel
     * @param message
     */
    abstract public void normalMessage(String channel,String message);

    /**
     * 取得订阅的消息后的处理
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        //unsubscribe channel when message value equalsIgnoreCase "quit" or "exit".
        if(message.equalsIgnoreCase("quit")||message.equalsIgnoreCase("exit")){
            this.unsubscribe(channel);
        }

        //handle message
        handler.handle(channel, message);
    }

    public void ack(String message) {
        handler.ack(message);
    }

    /**
     * 取得按表达式的方式订阅的消息后的处理
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        logger.info("message receive: {}, pattern channel: {} , pattern : {}", message, channel, pattern);
    }

    /**
     * 初始化订阅时候的处理
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        handler.subscribe(channel);
        logger.info("subscribe channel : {}, total subscribe channels : {}", channel, subscribedChannels);
    }

    /**
     * 取消订阅时候的处理
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        handler.unsubscribe(channel);
        logger.info("unsubscribe channel : {}; total channels : {}", channel, subscribedChannels);
    }

    /**
     * 取消按表达式的方式订阅时候的处理
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        logger.info("unsubscribe pattern: {}; total channels : {}", pattern, subscribedChannels);
    }

    /**
     * 初始化按表达式的方式订阅时候的处理
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("subscribe pattern: {}; total channels : {}",pattern,subscribedChannels);
    }

    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
        for(String channel : channels){
            handler.unsubscribe(channel);
        }
    }

    public Long count() {
        return handler.count();
    }

    class PubSubHandler extends AbstractMessageHandler {

        private Jedis jedis;
        PubSubHandler(){
            this.jedis = super.getResource();
        }
        public void handle(String channel,String message){
            int index = message.indexOf(Constants.MESSAGE_SEPARATOR);
            if(index < 0){
                return;
            }
            Long messageId = Long.valueOf(message.substring(0,index));

            /**
             * clientId/channel-name
             */
            String key = clientId + Constants.MESSAGE_SEPARATOR + channel;
            while(true){
                //get first message
                String lm = jedis.lindex(key, 0);
                if(lm == null){
                    break;
                }

                int li = lm.indexOf(Constants.MESSAGE_SEPARATOR);

                /**
                 * Illegal message
                 *  1.del illegal message
                 *  2.handle illegal message
                 */
                if(li < 0){//not include Constants.MESSAGE_SEPARATOR
                    //delete illegal message from current list
                    String result = jedis.lpop(key);

                    if(result == null){
                        break;
                    }

                    //handle illegal message
                    illegalMessage(channel, lm);

                    continue;
                }

                //first message id in queue
                Long listFirstMessageId = Long.valueOf(lm.substring(0,li));//获取消息的id

                /**
                 * when messageId >= listFirstMessageId.Consume backlog message
                 */
                if(messageId >= listFirstMessageId){
                    //Del current message
                    if(autoAck) {
                        jedis.lpop(key);
                    } else {
                        jedis.lrange(key,0,1);
                    }
                    //handle normal message
                    normalMessage(channel, lm);
                    continue;
                }else{
                    break;
                }
            }
        }

        /**
         * invoke subscribe when init subscribe
         * @param channel
         */
        public void subscribe(String channel){
            String key = clientId + Constants.MESSAGE_SEPARATOR + channel;
            boolean exist = jedis.sismember(Constants.SUBSCRIBE_CENTER,key);
            if(!exist){
                jedis.sadd(Constants.SUBSCRIBE_CENTER, key);
            }
        }

        /**
         * cancel subscribe
         * @param channel
         */
        public void unsubscribe(String channel){
//            String key = clientId + Constants.MESSAGE_SEPARATOR + channel;
            //remove from subscribe set
            jedis.srem(Constants.SUBSCRIBE_CENTER, queueName);
            //del subscriber queue(list)
            jedis.del(queueName);
        }
    }

}
