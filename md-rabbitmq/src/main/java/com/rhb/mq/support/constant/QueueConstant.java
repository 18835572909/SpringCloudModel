package com.rhb.mq.support.constant;

/**
 * 队列
 *
 * @author renhuibo
 * @date 2021/11/17 10:53
 */
public class QueueConstant {

  /********************************************VHOST***********************************************/
  public static final String ORDER_VHOST = "order";

  /********************************************QUEUE***********************************************/
  public static final String ORDER_CREATE_QUEUE = "order.create";

  public static final String DLX_SAMPLE_QUEUE = "dlx.sample";

  /********************************************EXCHANGE********************************************/
  public static final String ORDER_TOPIC_EXCHANGE = "order.topic";


  /********************************************ROUTE—KEY******************************************/
  public static final String ORDER_CREATE_QUEUE_ROUTE_KEY = "order.#";


}
