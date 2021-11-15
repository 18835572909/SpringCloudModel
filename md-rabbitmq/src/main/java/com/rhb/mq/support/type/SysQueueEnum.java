package com.rhb.mq.support.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/15 15:15
 */
@Getter
@AllArgsConstructor
public enum SysQueueEnum {

  /**
   * 系统队列定义
   */
  DLX_SAMPLE("/","dlx_sample_queue","通用的死信队列");

  private String vhost;
  private String queue;
  private String desc;

}
