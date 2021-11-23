package com.rhb.mq.support.config;

import com.rhb.mq.support.constant.QueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * MQ多数据源配置
 *
 * CachingConnectionFactory: 用来设置多数据源的
 * SimpleRabbitListenerContainerFactory: 用来设置mq消费特性
 * SimpleRabbitListenerContainerFactoryConfigurer: 用来绑定数据源和MQ消费特性
 *
 * RabbitTemplate: 消息的管理操作
 * RabbitAdmin: RabbitMq的管理操作
 *
 * 重试次数消耗完后，处理策略（3种）
 * RejectAndDontRequeueRecoverer：重试耗尽后，直接reject，丢弃消息。默认就是这种方式
 * ImmediateRequeueMessageRecoverer：重试耗尽后，返回nack，消息重新入队
 * RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机（死信）
 *
 * @author renhuibo
 * @date 2021/11/15 17:09
 */
@Slf4j
//@Configuration
public class RabbitMqConfig {

//  @Resource
//  RabbitTemplate rabbitTemplate;

  @Autowired
  private SimpleRabbitListenerContainerFactoryConfigurer rabbitListenerContainerFactoryConfigurer;

  final String host = "124.71.80.133";
  final int port = 5672;
  final String username = "root";
  final String password = "Ngd3zEBPHkY2CX4g";
  final int connectionTimeout = 30 * 1000;

  @Primary
  @Bean("defaultConnectionFactory")
  public CachingConnectionFactory defaultConnectionFactory(){
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(host);
    connectionFactory.setPort(port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setVirtualHost(QueueConstant.DEFAULT_VHOST);
    connectionFactory.setConnectionTimeout(connectionTimeout);
    connectionFactory.setPublisherReturns(true);
    connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
    return connectionFactory;
  }

  @Bean(name="defaultContainerFactory")
  public SimpleRabbitListenerContainerFactory defaultContainerFactory(
      @Qualifier("defaultConnectionFactory")CachingConnectionFactory connectionFactory){

    SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
    containerFactory.setConnectionFactory(connectionFactory);
    containerFactory.setPrefetchCount(1);
    containerFactory.setConsumerBatchEnabled(false);
    containerFactory.setConcurrentConsumers(1);
    containerFactory.setMaxConcurrentConsumers(1);
    containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());

    rabbitListenerContainerFactoryConfigurer.configure(containerFactory,connectionFactory);
    return containerFactory;
  }

  @Bean("defaultRabbitTemplate")
  public RabbitTemplate defaultRabbitTemplate(
      @Qualifier("defaultConnectionFactory")CachingConnectionFactory connectionFactory){

    connectionFactory.setPublisherConfirms(true);
    connectionFactory.setPublisherReturns(true);

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMandatory(true);
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
    return rabbitTemplate;
  }

  @Bean("defaultRabbitAdmin")
  public RabbitAdmin defaultRabbitAdmin(
      @Qualifier("defaultConnectionFactory")CachingConnectionFactory connectionFactory){
    return new RabbitAdmin(connectionFactory);
  }



  @Bean("orderConnectionFactory")
  public CachingConnectionFactory orderConnectionFactory(){
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(host);
    connectionFactory.setPort(port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setVirtualHost(QueueConstant.DEFAULT_VHOST);
    connectionFactory.setConnectionTimeout(connectionTimeout);
    connectionFactory.setPublisherReturns(true);
    connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
    return connectionFactory;
  }

  @Bean(name="orderContainerFactory")
  public SimpleRabbitListenerContainerFactory orderContainerFactory(
      @Qualifier("defaultConnectionFactory")CachingConnectionFactory connectionFactory){

    SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
    containerFactory.setConnectionFactory(connectionFactory);
    containerFactory.setPrefetchCount(1);
    containerFactory.setConsumerBatchEnabled(false);
    containerFactory.setConcurrentConsumers(1);
    containerFactory.setMaxConcurrentConsumers(1);
    containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());

    rabbitListenerContainerFactoryConfigurer.configure(containerFactory,connectionFactory);
    return containerFactory;
  }

  @Bean("orderRabbitTemplate")
  public RabbitTemplate orderRabbitTemplate(
      @Qualifier("defaultConnectionFactory")CachingConnectionFactory connectionFactory){

    connectionFactory.setPublisherConfirms(true);
    connectionFactory.setPublisherReturns(true);

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMandatory(true);
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
    return rabbitTemplate;
  }

  @Bean("orderRabbitAdmin")
  public RabbitAdmin orderRabbitAdmin(
      @Qualifier("defaultConnectionFactory")CachingConnectionFactory connectionFactory){
    return new RabbitAdmin(connectionFactory);
  }

}
