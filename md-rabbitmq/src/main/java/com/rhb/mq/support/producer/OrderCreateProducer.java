package com.rhb.mq.support.producer;

import cn.hutool.json.JSONUtil;
import com.rhb.mq.support.constant.QueueConstant;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 *  生产者样例。
 *  具体使用请使用接口编程（xxxServiceImp中使用）
 *
 * @author renhuibo
 * @date 2021/11/17 16:35
 */
@Component
public class OrderCreateProducer{

  @Resource
  BaseProducer baseProducer;

  public void send() {
    ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>(3);
    map.put("hello","弟弟");
    map.put("fuck you","batch");
    map.put("good-bye","Oh,My God");
    String msg = JSONUtil.toJsonStr(map);
    baseProducer.send(QueueConstant.ORDER_TOPIC_EXCHANGE,QueueConstant.ORDER_CREATE_ROUTE_KEY,msg);
  }
}
