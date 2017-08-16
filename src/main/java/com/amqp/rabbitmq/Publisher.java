package com.amqp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song.yang on 2017/8/16
 * <p>
 * E-mail: song.yang@lanmaoly.com
 */
@Component
public class Publisher {

    private final static Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    private Map<Integer, String> trans2RoutingKeyMap = new HashMap<Integer, String>();

    /**
     * 交易队列个数
     */
    private final static int TRANS_QUEUE_COUNT = 4;

    /**
     * 交易异步通知
     */
    private final static String NOTIFY_TRANS2_ROUTINGKEY = "bha-neo-service.trans2.notify.queue";

    Publisher() {
        trans2RoutingKeyMap.put(0, "bha-neo-service.trans2.queue1");
        trans2RoutingKeyMap.put(1, "bha-neo-service.trans2.queue2");
        trans2RoutingKeyMap.put(2, "bha-neo-service.trans2.queue3");
        trans2RoutingKeyMap.put(3, "bha-neo-service.trans2.queue4");
    }

    /**
     * 重新发送消息
     * @param message
     */
    public void reSend(Message message) {
        String exchange = message.getMessageProperties().getReceivedExchange();
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        amqpTemplate.send(exchange, routingKey, message);
    }

    /**
     * 发送交易
     * @param order
     */
    public void sendTrans2(Object order) {
        String routingKey = hashTrans(new Long(10));
        amqpTemplate.convertAndSend("bha-neo-service.trans.exchange", routingKey, order);
    }

    /**
     * 发送异步通知
     * @param order
     */
    public void sendNotifyTrans2(Object order) {
        //异步通知删除交易明细 优化网络IO
        //order.setRecords(null);
        amqpTemplate.convertAndSend("bha-neo-service.notify.exchange", NOTIFY_TRANS2_ROUTINGKEY, order);

    }

    private String hashTrans(Long projectId) {
        int remainder;
        if (projectId == null) {
            remainder = 0;
        } else {
            remainder = (int) (projectId % TRANS_QUEUE_COUNT);
        }
        return trans2RoutingKeyMap.get(remainder);
    }

    public static void main(String[] args) {
        Object order = new Object();

        new Publisher().sendTrans2(order);
    }
}
