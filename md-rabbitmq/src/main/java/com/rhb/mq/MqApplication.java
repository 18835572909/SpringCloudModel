package com.rhb.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/12 18:28
 */
@Slf4j
@SpringBootApplication(
    exclude = DataSourceAutoConfiguration.class
)
public class MqApplication implements ApplicationRunner {

  public static void main(String[] args) {
    SpringApplication.run(MqApplication.class);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Mq Application Start ... ");
  }
}
