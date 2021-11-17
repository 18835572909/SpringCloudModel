package com.rhb.mq.support.queue;

import com.rhb.mq.support.constant.QueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/17 16:48
 */
@Component
public class SampleQueue {

  @Bean
  public Queue queue(){
    return new Queue(QueueConstant.ORDER_CREATE_QUEUE);
  }

  public Exchange exchange(){
    return new TopicExchange(QueueConstant.ORDER_TOPIC_EXCHANGE);
  }

  public Binding binding(){
    return new Binding()
  }

}
