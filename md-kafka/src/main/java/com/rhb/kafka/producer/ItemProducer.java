package com.rhb.kafka.producer;

import cn.hutool.core.date.DateUtil;
import com.rhb.kafka.entity.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/12/7 18:11
 */
public class ItemProducer {

  public static List<Item> createItem(int num){
    List<Item> items = new ArrayList<>(num);
    for(int i=0;i<num;i++){
      Item item = Item.builder()
          .id(System.currentTimeMillis())
          .color(i % 2 == 0 ? "红色" : "蓝色")
          .size(i % 2 == 0 ? "L" : "M")
          .imgUrl("http://www.image.cn/" + i)
          .price(new BigDecimal(56.456).add(new BigDecimal(i)))
          .sku(DateUtil.format(new Date(), "yyyyMMddHHmmss" + i))
          .spu(DateUtil.format(new Date(), "yyyyMMddHHmmss" + 2 * i))
          .build();
      items.add(item);
    }
    return items;
  }

}
