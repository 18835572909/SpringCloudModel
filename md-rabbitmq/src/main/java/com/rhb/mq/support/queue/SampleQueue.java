package com.rhb.mq.support.queue;

import com.rhb.mq.support.constant.QueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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

  @Bean("orderCreateQueue")
  public Queue orderCreateQueue(){
    return new Queue(QueueConstant.ORDER_CREATE_QUEUE);
  }

  @Bean("orderTopicExchange")
  public TopicExchange orderTopicExchange(){
    return new TopicExchange(QueueConstant.ORDER_TOPIC_EXCHANGE);
  }

  @Bean
  public Binding binding(Queue orderCreateQueue,TopicExchange orderTopicExchange){
    return BindingBuilder.bind(orderCreateQueue).to(orderTopicExchange).with(QueueConstant.ORDER_CREATE_ROUTE_KEY);
  }
}
