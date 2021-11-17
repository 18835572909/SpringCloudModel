package com.rhb.mq.support.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/16 20:42
 */
@Getter
@AllArgsConstructor
public enum SysBizTypeEnum {
  /**
   * 业务类型
   */
  SYS_DEFAULT("系统默认"),
  ORDER_CREATE("订单创建");

  private String desc;

}
