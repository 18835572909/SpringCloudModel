package com.rhb.kafka;

import cn.hutool.json.JSONUtil;
import com.rhb.kafka.constant.KafkaConstant;
import com.rhb.kafka.entity.Item;
import com.rhb.kafka.producer.ItemProducer;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/12/7 17:50
 */
@Slf4j
@SpringBootApplication
public class KafkaApplication implements CommandLineRunner {

  @Resource
  KafkaTemplate kafkaTemplate;

  public static void main(String[] args) {
    SpringApplication.run(KafkaApplication.class,args);
  }

  @Override
  public void run(String... args) throws Exception {
    while(true){
      List<Item> items = ItemProducer.createItem(5);
      String jsonStr = JSONUtil.toJsonStr(items);

      log.info("kafka-producer: {}",jsonStr);

      kafkaTemplate.send(KafkaConstant.TEST_TOPIC,jsonStr);
    }
  }
}
