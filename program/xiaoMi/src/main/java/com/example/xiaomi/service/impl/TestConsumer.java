package com.example.xiaomi.service.impl;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
        topic = "test-topic",
        consumerGroup = "my-consumer-group"
)
public class TestConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }
}