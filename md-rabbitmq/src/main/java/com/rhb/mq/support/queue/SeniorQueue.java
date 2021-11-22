package com.rhb.mq.support.queue;

import com.rhb.mq.support.constant.QueueConstant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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

  @Bean("delayQueue")
  public Queue delayQueue(){
    Map<String,Object> args = new HashMap<>(3);
    args.put("x-dead-letter-exchange",QueueConstant.ORDER_DEAD_LETTER_EXCHANGE);
    args.put("x-dead-letter-routing-key",QueueConstant.ORDER_DEAD_LETTER_ROUTE_KEY);
    args.put("x-message-ttl",30*1000);
    return QueueBuilder.durable(QueueConstant.ORDER_DELAY_QUEUE).withArguments(args).build();
  }

  @Bean("delayExchange")
  public DirectExchange delayExchange(){
    return new DirectExchange(QueueConstant.ORDER_DELAY_EXCHANGE);
  }

  @Bean
  public Binding delayBinding(){
    return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(QueueConstant.ORDER_DELAY_ROUTE_KEY);
  }

  @Bean("dlxQueue")
  public Queue dlxQueue(){
    return new Queue(QueueConstant.ORDER_DEAD_LETTER_QUEUE);
  }

  @Bean("dlxExchange")
  public DirectExchange dlxExchange(){
    return new DirectExchange(QueueConstant.ORDER_DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public Binding dlxBinding(){
    return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(QueueConstant.ORDER_DEAD_LETTER_ROUTE_KEY);
  }

}
