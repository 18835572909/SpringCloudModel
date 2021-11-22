package com.rhb.mq.support.type;

import com.rhb.mq.support.constant.QueueConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 队列类型
 *
 * @author renhuibo
 * @date 2021/11/15 15:15
 */
@Getter
@AllArgsConstructor
public enum SysQueueEnum {

  /**
   * 队列定义
   */
  ORDER_CREATE("order", QueueConstant.ORDER_CREATE_QUEUE,"订单创建",SysBizTypeEnum.ORDER_CREATE),

  DLX_SAMPLE(QueueConstant.DEFAULT_VHOST,QueueConstant.DEAD_LETTER_QUEUE,"通用的死信队列",SysBizTypeEnum.SYS_DEFAULT),
  DELAY_QUEUE(QueueConstant.DEFAULT_VHOST,QueueConstant.DELAY_QUEUE,"延迟信队列",SysBizTypeEnum.SYS_DEFAULT),
  FAIL_DEAD_LETTER(QueueConstant.DEFAULT_VHOST,QueueConstant.FAIL_DEAD_LETTER_QUEUE,"MQ消息消费失败的死信队列",SysBizTypeEnum.SYS_DEFAULT),

  ORDER_DEAD_LETTER(QueueConstant.ORDER_VHOST,QueueConstant.ORDER_DEAD_LETTER_QUEUE,"下单的死信队列",SysBizTypeEnum.ORDER_PAY),
  ORDER_DELAY_QUEUE(QueueConstant.ORDER_VHOST,QueueConstant.ORDER_DELAY_QUEUE,"下单的延迟队列",SysBizTypeEnum.ORDER_PAY),

  ;

  private String vhost;
  private String queue;
  private String desc;
  private SysBizTypeEnum bizTypeEnum;

  public static SysQueueEnum vhostAndQueueToEnum(String vhost,String queue){
    if(null == vhost || null == queue){
      return null;
    }

    for(SysQueueEnum sysQueueEnum : values()){
      if(sysQueueEnum.getQueue().equals(queue) && sysQueueEnum.getVhost().equals(vhost)){
        return sysQueueEnum;
      }
    }
    return null;
  }


}
