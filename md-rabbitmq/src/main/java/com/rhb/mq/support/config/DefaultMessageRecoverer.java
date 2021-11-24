package com.rhb.mq.support.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

/**
 * 自定义消息拒绝策略
 *
 * @author renhuibo
 * @date 2021/11/24 10:20
 */
@Slf4j
public class DefaultMessageRecoverer implements MessageRecoverer {

  @Override
  public void recover(Message message, Throwable throwable) {
    byte[] body = message.getBody();
    String msg = new String(body);
    log.info("触发重试机制拒绝策略，消息：{}",msg);
  }

}
