package com.auth.center.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/1/15 19:19
 **/
@Component
@RocketMQMessageListener(
        topic = "test_topic",
        consumerGroup = "${rocketmq.consumer.group}",
        consumeMode = ConsumeMode.CONCURRENTLY
)
public class RocketMQConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        String tag = messageExt.getTags();
        String key = messageExt.getKeys();
        String body = new String(messageExt.getBody());
        System.out.println("tag = " + tag + ", key = " + key + ", body = " + body);
    }
}
