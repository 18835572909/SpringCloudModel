package com.rhb.mq.support.constant;

/**
 * 队列常量类
 * 1. Bean的名称取常量名
 * 2. 属性值去常量值
 * 3. 不允许代码中私自定义MQ相关信息
 *
 * @author renhuibo
 * @date 2021/11/17 10:53
 */
public class QueueConstant {

  /********************************************VHOST***********************************************/
  public static final String ORDER_VHOST = "order";
  public static final String DEFAULT_VHOST = "/";

  /**
   * 订单创建
   */
  public static final String ORDER_CREATE_QUEUE = "order.create.queue";
  public static final String ORDER_TOPIC_EXCHANGE = "order.topic.exchange";
  public static final String ORDER_CREATE_ROUTE_KEY = "order.#";

  /**
   * mq消费失败-死信: 当消息消费失败，重新投递到另一个Exchange，这个Exchange称为DLX（dead letter exchange）
   */
  public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
  public static final String DEAD_LETTER_EXCHANGE = "dead.letter.direct.exchange";
  public static final String DEAD_LETTER_ROUTE_KEY = "dead.letter.rk";

  /**
   * MQ消费失败
   */
  public static final String FAIL_DEAD_LETTER_QUEUE = "fail."+DEAD_LETTER_QUEUE;
  public static final String FAIL_DEAD_LETTER_EXCHANGE = "fail."+DEAD_LETTER_EXCHANGE;
  public static final String FAIL_DEAD_LETTER_ROUTE_KEY = "fail."+DEAD_LETTER_ROUTE_KEY;

  /**
   * 下单死信+ttl->延迟下单
   */
  public static final String ORDER_DEAD_LETTER_QUEUE = "order."+DEAD_LETTER_QUEUE;
  public static final String ORDER_DEAD_LETTER_EXCHANGE = "order."+DEAD_LETTER_EXCHANGE;
  public static final String ORDER_DEAD_LETTER_ROUTE_KEY = "order."+DEAD_LETTER_ROUTE_KEY;

  /**
   * 延迟队列
   */
  public static final String DELAY_QUEUE = "delay.queue";
  public static final String DELAY_EXCHANGE = "delay.direct.exchange";
  public static final String DELAY_ROUTE_KEY = "delay.rk";

  /**
   * 下单延迟
   */
  public static final String ORDER_DELAY_QUEUE = "order."+DELAY_QUEUE;
  public static final String ORDER_DELAY_EXCHANGE = "order."+DELAY_EXCHANGE;
  public static final String ORDER_DELAY_ROUTE_KEY = "order."+DELAY_ROUTE_KEY;

}
