package com.rhb.mq;

import com.rhb.mq.support.producer.OrderCreateProducer;
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

  @Test
  public void t1(){
    orderCreateProducer.send();
  }

}
