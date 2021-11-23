package com.rhb.mq.support.queue;

import com.rhb.mq.support.constant.QueueConstant;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/23 15:25
 */
//@Component
public class MultiConnectionQueue {

//  @Resource(name = "defaultConnectionFactory")
  ConnectionFactory defaultConnectionFactory;

//  @Resource(name = "orderConnectionFactory")
  ConnectionFactory orderConnectionFactory;

  @Bean
  @SneakyThrows
  public String defaultQueue(){
    return defaultConnectionFactory
        .createConnection()
        .createChannel(false)
        .queueDeclare(QueueConstant.MULTI_T1_QUEUE,false,false,false,null)
        .getQueue();
  }

  @Bean
  @SneakyThrows
  public String orderQueue(){
    return orderConnectionFactory
        .createConnection()
        .createChannel(false)
        .queueDeclare(QueueConstant.MULTI_T2_QUEUE,false,false,false,null)
        .getQueue();
  }

  @Bean
  public Queue defaultQueue(@Qualifier("orderRabbitAdmin") RabbitAdmin rabbitAdmin){
    return new Queue(QueueConstant.MULTI_T2_QUEUE);
  }

}
