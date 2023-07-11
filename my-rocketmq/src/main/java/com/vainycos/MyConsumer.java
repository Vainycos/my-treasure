package com.vainycos;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author: Vainycos
 * @description 消费者
 * @date: 2023/7/11 16:08
 */
@Service
@RocketMQMessageListener(topic = "${rocketmq.topic}", consumerGroup =
        "${rocketmq.consumerGroup}", selectorExpression =
        "${rocketmq.selectorExpression}")
public class MyConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String info) {
        // MQ消费成功 do something...
        JSONObject jsonObject = JSONObject.parseObject(info);
    }
}
