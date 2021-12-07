package com.rhb.kafka.consumer;

import cn.hutool.json.JSONUtil;
import com.rhb.kafka.entity.Item;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/12/7 18:35
 */
@Slf4j
@Component
public class ItemConsumer {

  @KafkaListener(topics = {"#{'${kafka.producer.topic}'}"})
  public void getItemInfo(ConsumerRecord<String, String> record){
    Optional<String> kafkaMessage = Optional.ofNullable(record.value());
    kafkaMessage.ifPresent(x -> {
      Item item = JSONUtil.toBean(x, Item.class);
      log.info("消费kafka中的数据:{}", item);
    });
  }

}
