//package org.lyflexi.bootrocketmq_consumer.listener;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.spring.annotation.MessageModel;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: DLJD
// * @Date: 2023/4/22
// */
//@Component
//@RocketMQMessageListener(topic = "modeTopic",
//        consumerGroup = "mode-consumer-group-b",
//        messageModel = MessageModel.BROADCASTING // 广播模式
//)
//public class DC4 implements RocketMQListener<String>  {
//    @Override
//    public void onMessage(String message) {
//        System.out.println("我是mode-consumer-group-b组的第一个消费者:" + message);
//    }
//}
