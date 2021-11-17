package com.rhb.mq.support.entity;

import com.rhb.mq.support.type.SysBizTypeEnum;
import com.rhb.mq.support.type.SysQueueEnum;
import java.util.Date;
import lombok.Data;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/16 20:34
 */
@Data
public class SysQueueMessage {

  private String id;

  private String vhost;

  private String queue;

  private String msg;

  private SysQueueEnum queueEnum;
  // 业务类型
  private SysBizTypeEnum bizTypeEnum;
  // 业务id
  private String bizId;
  // 消费状态
  private Integer consumeTag;
  // 异常日志
  private String errLog;
  // 接受时间
  private Date receiveDate;
  // 完成时间
  private Date overDate;

}
