package org.lyflexi.arocketmqdemo.demo;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.lyflexi.arocketmqdemo.constant.MqConstant;

import java.util.Date;
import java.util.List;

/**
 * @Author: DLJD
 * @Date: 2023/4/21
 */

/*RocketMQ发送延迟消息
消息放入mq后，过一段时间，才会被监听到，然后消费
比如下订单业务，提交了一个订单就可以发送一个延时消息，30min后去检查这个订单的状态，如果还是未付款就取消订单释放库存。
*/
public class DMsTest {


    @Test
    public void msProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ms-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("orderMsTopic", "订单号，座位号".getBytes());
        // 给消息设置一个延迟时间
        // messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        message.setDelayTimeLevel(3);
        // 发延迟消息
        producer.send(message);
        System.out.println("发送时间" + new Date());
        producer.shutdown();
    }

    /**
     * 发送时间Fri Apr 21 16:19:54 CST 2023
     * 收到消息了Fri Apr 21 16:20:20 CST 2023
     * --------------
     * 发送时间Fri Apr 21 16:21:08 CST 2023
     * 收到消息Fri Apr 21 16:21:18 CST 2023
     *
     * @throws Exception
     */
    @Test
    public void msConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ms-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderMsTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了" + new Date());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }


}
