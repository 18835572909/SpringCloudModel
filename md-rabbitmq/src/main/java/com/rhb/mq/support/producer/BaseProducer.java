package com.rhb.mq.support.producer;

import cn.hutool.core.util.CharsetUtil;
import com.rhb.mq.support.util.SnowFlakeUtil;
import javax.annotation.Resource;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/12 18:56
 */
public class BaseProducer {

  @Resource
  RabbitTemplate rabbitTemplate;

  public void test(){
    Object obj = new Object();
    rabbitTemplate.convertAndSend(obj,message -> {
      message.getMessageProperties()
          .setMessageId(""+SnowFlakeUtil.getId());
      message.getMessageProperties()
          .setContentType(MessageProperties.CONTENT_TYPE_JSON);
      message.getMessageProperties()
          .setContentEncoding(CharsetUtil.UTF_8);
      return message;
    });
  }


}
