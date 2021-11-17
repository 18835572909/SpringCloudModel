package com.rhb.mq.support.consumer.service.impl;

import com.rhb.mq.support.consumer.service.MqOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  订单创建
 *
 * @author renhuibo
 * @date 2021/11/17 10:37
 */
@Slf4j
@Service
@Transactional
public class MqOrderServiceImpl implements MqOrderService {

  @Override
  public String createOrder(String msg) {
    log.info("创建订单...");
    return System.currentTimeMillis()+"";
  }

}
