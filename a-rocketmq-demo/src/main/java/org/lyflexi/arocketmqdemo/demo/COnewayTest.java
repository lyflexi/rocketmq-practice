package org.lyflexi.arocketmqdemo.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;
import org.lyflexi.arocketmqdemo.constant.MqConstant;

/**
 * @Author: DLJD
 * @Date: 2023/4/21
 */

/*RocketMQ发送单向消息
这种方式主要用在不关心发送结果的场景，这种方式吞吐量很大，但是存在消息丢失的风险，例如日志信息的发送
*/
public class COnewayTest {


    @Test
    public void onewayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();
        Message message = new Message("onewayTopic", "日志xxx".getBytes());
        producer.sendOneway(message);
        System.out.println("成功");
        producer.shutdown();
    }


}
