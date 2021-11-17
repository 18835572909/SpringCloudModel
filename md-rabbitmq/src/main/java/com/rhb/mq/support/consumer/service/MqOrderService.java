package com.rhb.mq.support.consumer.service;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/17 10:35
 */
public interface MqOrderService {

  /**
   * 创建订单
   *
   * @param msg mq消息
   * @return 订单id
   */
  String createOrder(String msg);

}
