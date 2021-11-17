package com.rhb.mq.support.producer;

import cn.hutool.core.util.CharsetUtil;
import com.rhb.mq.support.constant.RedisKeyConstant;
import com.rhb.mq.support.util.SnowFlakeUtil;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/12 18:56
 */
@Slf4j
@Component
public class BaseProducer {

  @Resource
  RabbitTemplate rabbitTemplate;

  @Resource
  RedisTemplate redisTemplate;

  /**
   * MQ发送基础方法
   *
   * @param exchange 交换机
   * @param routingKey 路由键
   * @param msg 消息实体
   */
  public void send(String exchange, String routingKey, Object msg){
    /**
     * convertSendAndReceive() :  确定消费者接收到消息，才会发送下一条信息，每条消息之间会有间隔时间
     * convertAndSend() :  没有顺序，不需要等待，直接运行
     */
    rabbitTemplate.convertAndSend(exchange,routingKey,msg,message -> {
      String msgId = ""+SnowFlakeUtil.getId();
      message.getMessageProperties()
          .setMessageId(msgId);
      redisTemplate.opsForSet().add(RedisKeyConstant.MQ_MESSAGE_ID,msgId);
      message.getMessageProperties()
          .setContentType(MessageProperties.CONTENT_TYPE_JSON);
      message.getMessageProperties()
          .setContentEncoding(CharsetUtil.UTF_8);
      return message;
    });
  }

}
