package com.amqp.rabbitmq.jianshu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by song.yang on 2017/8/16
 * <p>
 * E-mail: song.yang@lanmaoly.com
 */
public class MessageConsumer implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    @Override
    public void onMessage(Message message) {
        logger.info("------消费者处理消息------");
        logger.info("receive message",message);
    }
}
