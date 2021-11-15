package com.rhb.mq.support.consumer;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/15 15:23
 */
@Slf4j
public class BaseConsumer {

  public void consume(Message message, Channel channel, Object object){
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    try{
      /**
       * 业务
       */
      channel.basicAck(deliveryTag,false);
    }catch (IOException e){
      try {
        /**
         * 第三个参数: 是否重回队列
         * 容易出现死循环
         */
        channel.basicNack(deliveryTag,false,true);
      } catch (IOException e1) {
        log.info("拒签失败：{}",e1.getCause());
      }
    }


  }

}
