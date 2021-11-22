package com.rhb.mq;

import com.rhb.mq.support.producer.OrderCreateProducer;
import com.rhb.mq.support.producer.OrderPayProducer;
import javax.annotation.Resource;
import org.junit.Test;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/18 17:56
 */
public class WebTest extends BaseTest {

  @Resource
  OrderCreateProducer orderCreateProducer;
  @Resource
  OrderPayProducer orderPayProducer;

  @Test
  public void t1(){
    orderCreateProducer.send();
  }

  @Test
  public void t2(){
    orderPayProducer.payOrder();
  }
}
