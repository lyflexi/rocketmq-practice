package org.lyflexi.arocketmqdemo.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.lyflexi.arocketmqdemo.constant.MqConstant;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: DLJD
 * @Date: 2023/4/21
 */

/*RocketMQ发送批量消息
Rocketmq可以一次性发送一组消息，那么这一组消息会被当做一个消息消费
*/
public class EBatchTest {

    @Test
    public void testBatchProducer() throws Exception {
        // 创建默认的生产者
        DefaultMQProducer producer = new DefaultMQProducer("batch-producer-group");
        // 设置nameServer地址
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动实例
        producer.start();
        List<Message> msgs = Arrays.asList(
                new Message("batchTopic", "我是一组消息的A消息".getBytes()),
                new Message("batchTopic", "我是一组消息的B消息".getBytes()),
                new Message("batchTopic", "我是一组消息的C消息".getBytes())
        );
        SendResult send = producer.send(msgs);
        System.out.println(send);
        // 关闭实例
        producer.shutdown();
    }


    @Test
    public void msConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("batch-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("batchTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了" + new Date());
                System.out.println(msgs.size());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }

}
