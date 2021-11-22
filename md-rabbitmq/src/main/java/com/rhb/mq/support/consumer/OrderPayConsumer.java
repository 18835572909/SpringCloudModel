package com.rhb.mq.support.consumer;

import com.rabbitmq.client.Channel;
import com.rhb.mq.support.constant.QueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/22 14:45
 */
@Slf4j
//@Component
public class OrderPayConsumer extends BaseConsumer {

  @Override
  public String execute(String msg) {
    log.info("Delay队列消费：{}",msg);
    return "";
  }

  @RabbitListener(bindings = @QueueBinding(
      value = @Queue(value = QueueConstant.ORDER_DELAY_QUEUE,durable = "true"),
      exchange = @Exchange(value = QueueConstant.ORDER_DELAY_EXCHANGE,type = "direct",durable = "true"),
      key = QueueConstant.ORDER_DELAY_ROUTE_KEY
  ))
  @Override
  public void consume(Message message, Channel channel, String msg) {
    super.consume(message, channel, msg, QueueConstant.ORDER_VHOST, QueueConstant.ORDER_DELAY_QUEUE);
  }

}
