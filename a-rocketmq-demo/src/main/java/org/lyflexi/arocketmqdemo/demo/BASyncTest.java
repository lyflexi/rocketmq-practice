package org.lyflexi.arocketmqdemo.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;
import org.lyflexi.arocketmqdemo.constant.MqConstant;

/**
 * @Author: DLJD
 * @Date: 2023/4/21
 */


/*RocketMQ发送异步消息
异步消息通常用在对响应时间敏感的业务场景，即发送端不能容忍长时间地等待Broker的响应。发送完以后会有一个异步消息通知*/
public class BASyncTest {


    @Test
    public void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("asyncTopic", "我是一个异步消息".getBytes());
        //不带返回值的send方法，就代表是异步send
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.err.println("发送失败:" + e.getMessage());
            }
        });
        System.out.println("我先执行");
        System.in.read();
    }

}
