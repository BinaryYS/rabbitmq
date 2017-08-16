package com.amqp.rabbitmq.jianshu;

/**
 * Created by song.yang on 2017/8/16
 * <p>
 * E-mail: song.yang@lanmaoly.com
 */
public interface IMessageProducer {

    public void sendMessage(Object message);
}
