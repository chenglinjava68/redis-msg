package com.mogujie.service.redis.msg;

import com.mogujie.service.redis.msg.pubsub.client.impl.PubClient;
import com.mogujie.service.redis.msg.pubsub.client.impl.SubListenerClient;
import com.mogujie.service.redis.msg.pubsub.client.impl.SubNonblockingClient;
import com.mogujie.service.redis.msg.pubsub.listener.AbstractMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhizhu on 15/11/18.
 *
 * HOW TO USE
 */
public class Application {


    private static Logger logger = LoggerFactory.getLogger(Application.class);
    final static String CHANNEL = "pubsub-test-channel";
    final static String CLIENT_ID = "mail-test-channel";


    public static void main(String[] args) throws InterruptedException {
        //send message
        sendMsg();
        //listener model :receive message
//        receiveMsg();
        //non-blocking model: receive message
//        receiveMsgNonblock();

    }

    /**
     * use listener
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
    private static void receiveMsg() throws InterruptedException {

        final SubListenerClient subClient = new SubListenerClient(new AbstractMessageListener(CLIENT_ID) {
            @Override
            public void normalMessage(String channel, String message) {
                logger.info("收到消息正常消息 channel : {}, message : {}",channel,message);
                try {
                    Thread.sleep(100);
                    this.ack(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },false);

        subClient.sub(CHANNEL);
    }

    /**
     * consume message from the client queue
     * @throws InterruptedException
     */
    private static void receiveMsgNonblock() throws InterruptedException {

//        final SubNonblockingClient client = new SubNonblockingClient(CLIENT_ID);//auto ack
        final SubNonblockingClient client = new SubNonblockingClient(CLIENT_ID,false);
        logger.info(client.getClientId());
        client.sub(CHANNEL);

        logger.info("消息总数:"+client.count());

//        sendMsg();
        logger.info("订阅成功!");
        Thread.sleep(1000);
        logger.info("开始消费消息");
        while (true) {
            Thread.sleep(1000);
            List<String> results = client.consume(10);
            logger.info("收到消息：size {}, 数据 : {}", results.size(), results);
            client.someAck(results);//batch ack
        }
    }

    /**
     * publish message
     * @throws InterruptedException
     */
    private static void sendMsg() throws InterruptedException {
        PubClient pubClient = new PubClient();
        int i = 0 ;
        logger.info("开始发送消息");
        while (true) {
            String message = "zhizhu send message " + i;
            i++;
            pubClient.pub(CHANNEL,message);

            if(i==2) {
                break;
            }
        }
    }

}
