package com.rhb.mq.support.config;

import com.rhb.mq.support.constant.QueueConstant;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CachingConnectionFactory: 用来设置多数据源的
 * SimpleRabbitListenerContainerFactory: 用来设置mq消费特性
 * SimpleRabbitListenerContainerFactoryConfigurer: 用来绑定数据源和MQ消费特性
 *
 * RabbitTemplate: 消息的管理操作
 * RabbitAdmin: RabbitMq的管理操作
 *
 * @author renhuibo
 * @date 2021/11/15 17:09
 */
@Slf4j
//@Configuration
public class RabbitMqConfig2 {

  @Resource
  RabbitTemplate rabbitTemplate;

  @Autowired
  private SimpleRabbitListenerContainerFactoryConfigurer rabbitListenerContainerFactoryConfigurer;

  /**
   * 防止：生成者消息丢失
   *
   * 1. 开启监听
   * rabbitmq:
   *     publisher-returns: true
   *     publisher-confirm-type: correlated
   * 2. 处理事件
   */
  @PostConstruct
  public void enableConfirmCallback(){
    //当消息成功发到交换机ack=true，没有发送到交换机ack = false
    rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
      if(!ack){
        //记录日志、发送邮件通知、落库定时任务扫描重发
        log.info("消息发送交换机失败，请及时查看\n data:{}\ncause:{}",correlationData,cause);
      }
    }));

    //当消息成功发送到交换机，没有路由到队列触发此监听
    rabbitTemplate.setReturnsCallback(returnedMessage -> {
      //记录日志、发送邮件通知、落库定时任务扫描重发
      log.info("路由到队列失败，请及时查看\n returnedMessage:{}",returnedMessage);
    });
  }

  /**
   * 重试次数消耗完后，处理策略（3种）
   * RejectAndDontRequeueRecoverer：重试耗尽后，直接reject，丢弃消息。默认就是这种方式
   * ImmediateRequeueMessageRecoverer：重试耗尽后，返回nack，消息重新入队
   * RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机（死信）
   */
  @Bean
  public MessageRecoverer retryFailStrategy(){
//    return new DefaultMessageRecoverer();
    return new RepublishMessageRecoverer(rabbitTemplate, QueueConstant.DEAD_LETTER_EXCHANGE,QueueConstant.DEAD_LETTER_ROUTE_KEY);
  }

}
