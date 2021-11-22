package com.rhb.mq.support.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/15 17:09
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

  @Resource
  RabbitTemplate rabbitTemplate;

  /**
   * 防止：生成者消息丢失
   *
   * 1. 开启监听
   * rabbitmq:
   *     publisher-returns: true
   *     publisher-confirm-type: correlated
   * 2. 处理事件
   *
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

}
