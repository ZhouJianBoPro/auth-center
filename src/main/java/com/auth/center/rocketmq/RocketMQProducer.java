package com.auth.center.rocketmq;

import com.auth.center.exception.CustomException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Desc: 生产者客户端
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/1/15 17:51
 **/
@Component
public class RocketMQProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public <T> void sendMessage(String topic, String tag, String key, T payload) {

        Message<T> message = MessageBuilder.withPayload(payload)
                .setHeader(RocketMQHeaders.KEYS, key)
                .build();

        SendResult sendResult = rocketMQTemplate.syncSend(topic + ":" + tag, message);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK) {
            throw new CustomException("发送消息失败" + sendResult.getSendStatus().name());
        }
    }
}
