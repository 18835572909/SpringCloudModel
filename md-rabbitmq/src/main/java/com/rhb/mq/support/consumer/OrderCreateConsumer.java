package com.rhb.mq.support.consumer;

import com.rabbitmq.client.Channel;
import com.rhb.mq.support.constant.QueueConstant;
import com.rhb.mq.support.consumer.service.MqOrderService;
import javax.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 订单消息
 *
 * @author renhuibo
 * @date 2021/11/17 10:33
 */
@Component
public class OrderCreateConsumer extends BaseConsumer {

  @Resource
  MqOrderService mqOrderService;

  @RabbitListener(bindings = @QueueBinding(
      value = @Queue(value = QueueConstant.ORDER_CREATE_QUEUE,durable = "true"),
      exchange = @Exchange(value = QueueConstant.ORDER_TOPIC_EXCHANGE,type = "topic"),
      key = QueueConstant.ORDER_CREATE_ROUTE_KEY))
  @Override
  public void consume(Message message, Channel channel, String msg) {
    super.consume(message,channel,msg,QueueConstant.ORDER_VHOST,QueueConstant.ORDER_CREATE_QUEUE);
  }

  @Override
  public String execute(String msg) {
    throw new RuntimeException("主动抛出");
  }
}
