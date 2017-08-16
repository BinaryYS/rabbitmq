//package com.amqp.rabbitmq;
//
//import com.lanmao.dto.ExceptionMsgDTO;
//import com.lanmao.neo.bha.trans.TransactionOrder;
//import com.lanmao.neo.bha.trans.internal.mq.ErrorMessageQueueHandler;
//import com.lanmao.neo.bha.trans.internal.mq.TransMessageQueueHandler;
//import com.lanmao.neo.mq.utils.RabbitMQUtils;
//import com.lanmao.runtime.core.logging.LoggingTracer;
//import com.rabbitmq.client.Channel;
//import org.apache.commons.lang.builder.ReflectionToStringBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * Created by pengmaokui on 2016/11/1.
// */
//@Component
//public class Receiver implements ChannelAwareMessageListener {
//
//	private final static Logger logger = LoggerFactory.getLogger(Receiver.class);
//
//	/**
//	 * 最大处理次数
//	 */
//	private final static int MAX_HANHLE_COUNT = 4;
//
//	private final static String REDELIVERED_COUNT_STR = "x-redelivered-count";
////
////	@Autowired
////	private TransMessageQueueHandler transMessageQueueHandler;
//
//	@Autowired
//	private Publisher publisher;
//
////	@Autowired
////	private ErrorMessageQueueHandler errorMessageQueueHandler;
//
//	@Override
//	public void onMessage(Message message, Channel channel) throws Exception {
//		LoggingTracer.set(UUID.randomUUID().toString());
//		Object deliverCount = message.getMessageProperties().getHeaders().get(REDELIVERED_COUNT_STR);
//		long deleiverCountLong = 0;
//		if (deliverCount != null) {
//			deleiverCountLong = (Long) deliverCount;
//		}
//		//大于最大处理次数丢弃改消息
//		if (deleiverCountLong >= MAX_HANHLE_COUNT) {
//			RabbitMQUtils.basicReject(channel, message, false);
//			throw new RuntimeException("超过了最大的处理次数,该消息丢弃:" + message);
//		}
//		message.getMessageProperties().getHeaders().put(REDELIVERED_COUNT_STR, ++deleiverCountLong);
//	}
//
//	@RabbitListener(queues = {"bha-neo-service.trans2.queue1"}, containerFactory = "rabbitListenerContainerFactory")
//	public void handleMessageOne(TransactionOrder order, Message message, Channel channel) throws Exception {
//		logger.info("bha-neo-service.trans2.queue1接收消息:{}", ReflectionToStringBuilder.toString(order));
//		onMessage(message, channel);
//		//处理消息
//		messageHandler(order, message, channel);
//	}
//
//	@RabbitListener(queues = {"bha-neo-service.trans2.queue2"}, containerFactory = "rabbitListenerContainerFactory")
//	public void handleMessageTwo(TransactionOrder order, Message message, Channel channel) throws Exception {
//		logger.info("bha-neo-service.trans2.queue2接收消息:{}", ReflectionToStringBuilder.toString(order));
//		onMessage(message, channel);
//		//处理消息
//		messageHandler(order, message, channel);
//	}
//
//	@RabbitListener(queues = {"bha-neo-service.trans2.queue3"}, containerFactory = "rabbitListenerContainerFactory")
//	public void handleMessageThree(TransactionOrder order, Message message, Channel channel) throws Exception {
//		logger.info("bha-neo-service.trans2.queue3接收消息:{}", ReflectionToStringBuilder.toString(order));
//		onMessage(message, channel);
//		//处理消息
//		messageHandler(order, message, channel);
//	}
//
//	@RabbitListener(queues = {"bha-neo-service.trans2.queue4"}, containerFactory = "rabbitListenerContainerFactory")
//	public void handleMessageFour(TransactionOrder order, Message message, Channel channel) throws Exception {
//		logger.info("bha-neo-service.trans2.queue4接收消息:{}", ReflectionToStringBuilder.toString(order));
//		onMessage(message, channel);
//
//		//处理消息
//		messageHandler(order, message, channel);
//	}
//
//	@RabbitListener(queues = {"bha-neo-service.trans2.notify.queue"}, containerFactory = "notifyRabbitListenerContainerFactory")
//	public void handleNotifyMessage(TransactionOrder order, Message message, Channel channel) throws Exception {
//		logger.info("bha-neo-service.trans2.notify.queue接收消息:{}", ReflectionToStringBuilder.toString(order));
//		onMessage(message, channel);
//
//		//处理消息
//		try {
//			transMessageQueueHandler.handlerTrans2NotifyMessage(order, message, channel);
//		} catch (RuntimeException e) {
//			logger.error("trans2.notify处理消息异常", e);
//			//重新发送消息
//			publisher.reSend(message);
//			//删除消息
//			RabbitMQUtils.basicReject(channel, message, false);
//		}
//	}
//
//	private void messageHandler(TransactionOrder order, Message message, Channel channel) throws IOException {
//		try {
//			transMessageQueueHandler.handlerMessageQueue(order, message, channel);
//		} catch (RuntimeException e) {
//			logger.error("交易处理失败,重新发送至消息对了.requestNo:" + order.getRequestNo(), e);
//			//重新发送消息
//			publisher.reSend(message);
//			//删除消息
//			RabbitMQUtils.basicReject(channel, message, false);
//		}
//	}
//
//	@RabbitListener(queues = {"bha-neo-app.error.notify.queue"}, containerFactory = "errorListenerContainerFactory")
//	public void handleErrorMessage(ExceptionMsgDTO dto,Message message, Channel channel) throws Exception {
//		logger.info("bha-neo-app.error.notify.queue接收消息,requestNo:{}", dto.getRequestNo());
//		onMessage(message, channel);
//		//处理消息
//		try {
//			errorMessageQueueHandler.handlerErrorMessage(dto, message, channel);
//		} catch (RuntimeException e) {
//			logger.error("错误消息处理失败,重新发送至消息对了.requestNo:" + dto.getRequestNo(), e);
//			//重新发送消息
//			publisher.reSend(message);
//			//删除消息
//			RabbitMQUtils.basicReject(channel, message, false);
//		}
//	}
//
//
//
//}
