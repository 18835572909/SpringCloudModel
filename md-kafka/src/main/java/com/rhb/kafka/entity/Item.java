package com.rhb.kafka.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/12/7 18:09
 */
@Data
@Builder
public class Item {
  private Long id;
  private String sku;
  private String spu;
  private String size;
  private String color;
  private BigDecimal price;
  private String imgUrl;
}
