package com.rhb.mq.support.consumer;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.rhb.mq.support.constant.QueueConstant;
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
      vhost = QueueConstant.DEFAULT_VHOST;
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
    sysQueueMessage.setMsgId(messageId);
    SetOperations<String, String> mqMsgIdSet = redisTemplate.opsForSet();
    Boolean existsMsg = mqMsgIdSet.isMember(RedisKeyConstant.MQ_MESSAGE_ID, messageId);
    if(null == existsMsg || !existsMsg){
      log.info("冥等性校验，丢弃当前信息：{}",messageId);
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
    } catch (RuntimeException re){
      log.info("runtime exception ...");
      sysQueueMessage.setErrLog(ExceptionUtil.getMessage(re));
      sysQueueMessage.setConsumeTag(0);
      sysQueueMessage.setOverDate(new Date());
      // TODO 消息落盘
      log.info("Sys.Msg：{}", JSONUtil.parseObj(sysQueueMessage).toString());

      try {
        channel.basicNack(deliveryTag,false,false);
      } catch (IOException e) {
        e.printStackTrace();
      }

      throw new RuntimeException("运行异常。。。");
    }catch (Exception e){
      log.info("exception ...");
      sysQueueMessage.setErrLog(ExceptionUtil.getMessage(e));
      try {
        /**
         * 第三个参数: 是否重回队列(容易出现死循环)
         * deliveryTag- 收到的标签AMQP.Basic.GetOk或AMQP.Basic.Deliver
         * multiple- true 拒绝所有消息，包括提供的交付标签；false 仅拒绝提供的交付标签。
         * requeue - 如果被拒绝的消息应该重新排队而不是丢弃/死信，则为真
         *    (false：通俗的讲，消费失败直接丢弃。 true：消息重新排队)
         */
        sysQueueMessage.setConsumeTag(0);
        channel.basicNack(deliveryTag,false,false);
      } catch (IOException e1) {
        log.info("拒签失败：{}",e1.getCause());
      }
    }
    sysQueueMessage.setOverDate(new Date());
    // TODO 消息落盘
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
   * @param message 消费信息
   * @param channel channel
   * @param msg 消费body
   */
  public abstract void consume(Message message, Channel channel, String msg);

}
