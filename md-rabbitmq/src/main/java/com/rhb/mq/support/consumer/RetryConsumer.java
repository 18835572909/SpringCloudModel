package com.rhb.mq.support.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/23 10:27
 */
public class RetryConsumer extends BaseConsumer {

  @Override
  public String execute(String msg) {
    return null;
  }


  @Override
  public void consume(Message message, Channel channel, String msg) {

  }
}
