package com.rhb.mq.support.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.system.SystemUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 思否博客： 仍旧抛出RabbitTemplate的非单个Bean异常
 *
 * @author renhuibo
 * @date 2021/11/24 11:46
 */
//@Configuration
@RequiredArgsConstructor
@Slf4j
public class MultiRabbitMqConfig  {

  @Value("${spring.rabbitmq.multi.default.host}")
  private String host;
  @Value("${spring.rabbitmq.multi.default.username}")
  private String username;
  @Value("${spring.rabbitmq.multi.default.password}")
  private String password;
  @Value("${spring.rabbitmq.multi.default.port}")
  private int port;
  @Value("${spring.rabbitmq.multi.default.vhost}")
  private String vhost;
  @Value("${spring.rabbitmq.multi.default.queue}")
  private String queue;

  @Value("${spring.rabbitmq.multi.order.host}")
  private String orderHost;
  @Value("${spring.rabbitmq.multi.order.username}")
  private String orderUsername;
  @Value("${spring.rabbitmq.multi.order.password}")
  private String orderPassword;
  @Value("${spring.rabbitmq.multi.order.port}")
  private int orderPort;
  @Value("${spring.rabbitmq.multi.order.vhost}")
  private String orderVhost;
  @Value("${spring.rabbitmq.multi.order.queue}")
  private String orderQueue;

  private final DefaultListableBeanFactory defaultListableBeanFactory;
  private final AutowireCapableBeanFactory autowireCapableBeanFactory;

  @PostConstruct
  public void initRabbitmq() {
    Map<String,RabbitProperties> multiMqPros = new HashMap<String,RabbitProperties>(2){
      {
        put("first", RabbitProperties.builder()
            .host(host)
            .port(port)
            .username(username)
            .password(password)
            .virtualHost(vhost)
            .queueName(queue)
            .build());
        put("second", RabbitProperties.builder()
            .host(orderHost)
            .port(orderPort)
            .username(orderUsername)
            .password(orderPassword)
            .virtualHost(orderVhost)
            .queueName(orderQueue)
            .build());
      }
    };

    multiMqPros.forEach((key, rabbitProperties) -> {
      AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(CachingConnectionFactory.class)
          .addPropertyValue("cacheMode", CachingConnectionFactory.CacheMode.CHANNEL)
          .addPropertyValue("host", rabbitProperties.getHost())
          .addPropertyValue("port", rabbitProperties.getPort())
          .addPropertyValue("username", rabbitProperties.getUsername())
          .addPropertyValue("password", rabbitProperties.getPassword())
          .addPropertyValue("virtualHost", rabbitProperties.getVirtualHost())
          .getBeanDefinition();
      String connectionFactoryName = String.format("%s%s", key, "ConnectionFactory");
      defaultListableBeanFactory.registerBeanDefinition(connectionFactoryName, beanDefinition);
      CachingConnectionFactory connectionFactory = defaultListableBeanFactory.getBean(connectionFactoryName, CachingConnectionFactory.class);

      String rabbitAdminName = String.format("%s%s", key, "RabbitAdmin");
      AbstractBeanDefinition rabbitAdminBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(RabbitAdmin.class)
          .addConstructorArgValue(connectionFactory)
          .addPropertyValue("autoStartup", true)
          .getBeanDefinition();
      defaultListableBeanFactory.registerBeanDefinition(rabbitAdminName, rabbitAdminBeanDefinition);
      RabbitAdmin rabbitAdmin = defaultListableBeanFactory.getBean(rabbitAdminName, RabbitAdmin.class);
      log.info("rabbitAdmin:[{}]", rabbitAdmin);

      RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
      defaultListableBeanFactory.registerSingleton(String.format("%s%s", key, "RabbitTemplate"), rabbitTemplate);

      SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
      // 设置监听的队列
      simpleMessageListenerContainer.setQueueNames(rabbitProperties.getQueueName());
      // 指定要创建的并发使用者的数量,默认值是1,当并发高时可以增加这个的数值，同时下方max的数值也要增加
      simpleMessageListenerContainer.setConcurrentConsumers(3);
      // 最大的并发消费者
      simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
      // 设置是否重回队列
      simpleMessageListenerContainer.setDefaultRequeueRejected(false);
      // 设置签收模式
      simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
      // 设置非独占模式
      simpleMessageListenerContainer.setExclusive(false);
      // 设置consumer未被 ack 的消息个数
      simpleMessageListenerContainer.setPrefetchCount(1);
      // 设置消息监听器
      simpleMessageListenerContainer.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
        try {
          log.info("============> Thread:[{}] 接收到消息:[{}] ", Thread.currentThread().getName(), new String(message.getBody()));
          log.info("====>connection:[{}]", channel.getConnection());
          channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          // 发生异常此处需要捕获到
          channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
      });
      /**
       * 1、simpleMessageListenerContainer.start();
       * 2、simpleMessageListenerContainer.stop();
       * 3、如果后期rabbitmq 的配置是从数据库中读取，即用户在页面上添加一个配置，就动态创建这个
       * 此时就需要调用 SimpleMessageListenerContainer#start 方法
       */
      String containerName = String.format("%s%s", key, "SimpleMessageListenerContainer");
      defaultListableBeanFactory.registerSingleton(containerName, autowireCapableBeanFactory.initializeBean(simpleMessageListenerContainer, containerName));
    });

    int cpuCoreCount = Runtime.getRuntime().availableProcessors();
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
        cpuCoreCount+1,
        cpuCoreCount+1,
        0,TimeUnit.SECONDS,
        new LinkedBlockingDeque<>(),
        ThreadFactoryBuilder.create().setNamePrefix("mq").build(),
        new ThreadPoolExecutor.AbortPolicy());

    threadPool.submit(()->{
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      RabbitTemplate firstRabbitTemplate = (RabbitTemplate) defaultListableBeanFactory.getBean("firstRabbitTemplate");
      firstRabbitTemplate.convertAndSend("exchange-rabbit-multi-01", "", "first queue message");
      log.info("first over...");
    });

    threadPool.submit(()->{
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      RabbitTemplate secondRabbitTemplate = (RabbitTemplate) defaultListableBeanFactory.getBean("secondRabbitTemplate");
      secondRabbitTemplate.convertAndSend("exchange-rabbit-multi-02", "", "second queue message");
      log.info("second over...");
    });

  }

}
