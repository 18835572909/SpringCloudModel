问题1： 重试机制是MQ发起的还是SpringBoot做的？

问题2： 什么情况下触发重试？手动回执nack会触发吗？
4种场景：
  手动回执nack，不抛异常: 不会触发重试机制
  手动回执nack，抛异常： 会触发重试机制
  自动ack，抛出异常： 会触发
  自动ack，不抛异常：不会触发

问题3： 消费抛出异常的本质，是不是回执了nack？
网上说，自动回执，触发异常的本质是，系统回执了nack信息是什么意思？

