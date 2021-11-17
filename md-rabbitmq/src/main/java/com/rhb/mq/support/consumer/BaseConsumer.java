package com.rhb.mq.support.consumer;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.rhb.mq.support.constant.RedisKeyConstant;
import com.rhb.mq.support.entity.SysQueueMessage;
import com.rhb.mq.support.type.SysQueueEnum;
import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.util.Assert;

/**
 * 消费基类
 *
 * @author renhuibo
 * @date 2021/11/15 15:23
 */
@Slf4j
public abstract class BaseConsumer {

  @Resource
  RedisTemplate<String,String> redisTemplate;

  /**
   * 消息基类方法
   *
   * @param message 消息
   * @param channel channel
   * @param msg string 消息
   * @param vhost vhost
   * @param queue queueName
   */
  public void consume(Message message, Channel channel, String msg, String vhost, String queue){
    if(StrUtil.isEmpty(vhost)){
      vhost = "/";
    }

    Assert.isTrue(StrUtil.isNotEmpty(queue),"参数错误：BaseProducer#send.queue");

    SysQueueEnum sysQueueEnum = SysQueueEnum.vhostAndQueueToEnum(vhost, queue);
    if(null == sysQueueEnum){
      log.error("消息处理失败,消息类型不正确，直接丢弃:{}",vhost+"."+queue);
      return;
    }

    /**
     * 消息持久化
     */
    SysQueueMessage sysQueueMessage = new SysQueueMessage();
    sysQueueMessage.setVhost(vhost);
    sysQueueMessage.setQueue(queue);
    sysQueueMessage.setMsg(msg);
    sysQueueMessage.setBizTypeEnum(sysQueueEnum.getBizTypeEnum());
    sysQueueMessage.setQueueEnum(sysQueueEnum);
    sysQueueMessage.setReceiveDate(new Date());

    long deliveryTag = message.getMessageProperties().getDeliveryTag();

    /**
     * 冥等性
     */
    String messageId = message.getMessageProperties().getMessageId();
    log.info("messageId:{}",messageId);
    SetOperations<String, String> mqMsgIdSet = redisTemplate.opsForSet();
    if(StrUtil.isNotEmpty(messageId)
        && null != mqMsgIdSet
        && mqMsgIdSet.isMember(RedisKeyConstant.MQ_MESSAGE_ID,messageId)){
      return;
    }
    try{
      /**
       * 业务处理
       */
      String bizId = this.execute(msg);
      sysQueueMessage.setBizId(bizId);
      channel.basicAck(deliveryTag,false);
      sysQueueMessage.setConsumeTag(1);
    }catch (IOException e){
      sysQueueMessage.setErrLog(ExceptionUtil.getMessage(e));
      try {
        /**
         * 第三个参数: 是否重回队列(容易出现死循环)
         */
        sysQueueMessage.setConsumeTag(0);
        channel.basicNack(deliveryTag,false,true);
      } catch (IOException e1) {
        log.info("拒签失败：{}",e1.getCause());
      }
    }catch (Exception e){
      sysQueueMessage.setErrLog(ExceptionUtil.getMessage(e));
    }
    sysQueueMessage.setOverDate(new Date());

    /**
     * 消息落盘DB
     */
    log.info("Sys.Msg：{}", JSONUtil.parseObj(sysQueueMessage).toString());
  }

  /**
   * 业务执行流程
   *
   * @param msg 消息
   * @return 业务处理主键id
   */
  public abstract String execute(String msg);

  /**
   * 消费调用
   *
   * @param message
   * @param channel
   * @param msg
   */
  public abstract void consume(Message message, Channel channel, String msg);

}
