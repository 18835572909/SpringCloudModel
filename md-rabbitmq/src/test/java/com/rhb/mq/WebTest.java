package com.rhb.mq;

import com.rhb.mq.support.producer.OrderCreateProducer;
import com.rhb.mq.support.producer.OrderPayProducer;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/18 17:56
 */
@Slf4j
public class WebTest extends BaseTest {

  @Resource
  OrderCreateProducer orderCreateProducer;
  @Resource
  OrderPayProducer orderPayProducer;

  @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
  private int tryCount;

  @Test
  public void t1(){
    log.info("重试：{}",tryCount);
    orderCreateProducer.send();
  }

  @Test
  public void t2(){
    orderPayProducer.payOrder();
  }
}
