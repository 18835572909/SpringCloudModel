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
  DLX_SAMPLE("/",QueueConstant.DLX_SAMPLE_QUEUE,"通用的死信队列",SysBizTypeEnum.SYS_DEFAULT);

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
