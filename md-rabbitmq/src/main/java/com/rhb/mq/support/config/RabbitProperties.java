package com.rhb.mq.support.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/24 11:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RabbitProperties {
  private String host;
  private Integer port;
  private String username;
  private String password;
  private String virtualHost;
  private String queueName;
}
