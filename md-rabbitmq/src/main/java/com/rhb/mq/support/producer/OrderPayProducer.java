package com.rhb.mq.support.producer;

import cn.hutool.json.JSONUtil;
import com.rhb.mq.support.constant.QueueConstant;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/22 14:53
 */
@Component
public class OrderPayProducer extends BaseProducer {

    public void payOrder(){
      Map<String,Object> msgMap = new HashMap<>(3);
      msgMap.put("uname","张杰");
      msgMap.put("order_id",System.currentTimeMillis());
      msgMap.put("order_amount",new BigDecimal(50.56));
      String msg = JSONUtil.toJsonStr(msgMap);
      this.send(QueueConstant.ORDER_DELAY_EXCHANGE,QueueConstant.ORDER_DELAY_ROUTE_KEY,msg);
    }

}
