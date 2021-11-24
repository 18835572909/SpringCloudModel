package com.rhb.mq.support.queue;

import com.rhb.mq.support.constant.QueueConstant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 延迟队列使用
 *
 * @author renhuibo
 * @date 2021/11/22 13:58
 */
@Component
public class SeniorQueue {

  @Bean("orderDelayQueue")
  public Queue orderDelayQueue(){
    Map<String,Object> args = new HashMap<>(3);
    args.put("x-dead-letter-exchange",QueueConstant.ORDER_DEAD_LETTER_EXCHANGE);
    args.put("x-dead-letter-routing-key",QueueConstant.ORDER_DEAD_LETTER_ROUTE_KEY);
    args.put("x-message-ttl",30*1000);
    return QueueBuilder.durable(QueueConstant.ORDER_DELAY_QUEUE).withArguments(args).build();
  }

  @Bean("orderDelayExchange")
  public DirectExchange orderDelayExchange(){
    return new DirectExchange(QueueConstant.ORDER_DELAY_EXCHANGE);
  }

  @Bean
  public Binding orderDelayBinding(
      @Qualifier("orderDelayQueue") Queue orderDelayQueue,
      @Qualifier("orderDelayExchange") DirectExchange orderDelayExchange){
    return BindingBuilder.bind(orderDelayQueue).to(orderDelayExchange).with(QueueConstant.ORDER_DELAY_ROUTE_KEY);
  }

  @Bean("orderDlxQueue")
  public Queue orderDlxQueue(){
    return new Queue(QueueConstant.ORDER_DEAD_LETTER_QUEUE);
  }

  @Bean("orderDlxExchange")
  public DirectExchange orderDlxExchange(){
    return new DirectExchange(QueueConstant.ORDER_DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public Binding orderDlxBinding(
      @Qualifier("orderDlxQueue") Queue orderDlxQueue,
      @Qualifier("orderDlxExchange") DirectExchange orderDlxExchange){
    return BindingBuilder.bind(orderDlxQueue).to(orderDlxExchange).with(QueueConstant.ORDER_DEAD_LETTER_ROUTE_KEY);
  }

  @Bean("dlxQueue")
  public Queue dlxQueue(){
    return new Queue(QueueConstant.DEAD_LETTER_QUEUE);
  }

  @Bean("dlxExchange")
  public DirectExchange dlxExchange(){
    return new DirectExchange(QueueConstant.DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public Binding dlxBinding(
      @Qualifier("dlxQueue") Queue dlxQueue,
      @Qualifier("dlxExchange") DirectExchange dlxExchange){
    return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(QueueConstant.DEAD_LETTER_ROUTE_KEY);
  }

}
