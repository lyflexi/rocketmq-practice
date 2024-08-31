package org.lyflexi.arocketmqdemo.demo;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;
import org.lyflexi.arocketmqdemo.constant.MqConstant;
import org.lyflexi.arocketmqdemo.domain.MsgModel;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: DLJD
 * @Date: 2023/4/21
 */

/*模拟一个订单的发送流程，创建两个订单，发送的消息分别是
订单号111 消息流程 下订单->物流->签收
订单号112 消息流程 下订单->物流->拒收
*/
public class FOrderlyTest {


    private List<MsgModel> msgModels = Arrays.asList(
            new MsgModel("qwer", 1, "下单"),
            new MsgModel("qwer", 1, "短信"),
            new MsgModel("qwer", 1, "物流"),
            new MsgModel("zxcv", 2, "下单"),
            new MsgModel("zxcv", 2, "短信"),
            new MsgModel("zxcv", 2, "物流")
    );

    @Test
    public void orderlyProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("orderly-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        // 发送顺序消息  发送时要确保有序 并且要发到同一个队列下面去
        msgModels.forEach(msgModel -> {
            Message message = new Message("orderlyTopic", msgModel.toString().getBytes());
            try {

                producer.send(message, new MessageQueueSelector() {
                    // 发 相同的订单号去相同的队列
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        // 在这里 选择队列
                        int hashCode = arg.toString().hashCode();

                        int i = hashCode % mqs.size();
                        return mqs.get(i);
                    }
                }, msgModel.getOrderSn());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        producer.shutdown();
        System.out.println("发送完成");
    }


    @Test
    public void orderlyConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderly-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderlyTopic", "*");
        // MessageListenerConcurrently 并发模式 多线程的  重试16次
        // MessageListenerOrderly 顺序模式 单线程的   无限重试Integer.Max_Value
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("线程id:" + Thread.currentThread().getId());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }


}
