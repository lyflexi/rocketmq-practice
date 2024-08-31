package org.lyflexi.seckill_service.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.lyflexi.seckill_service.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @Author: DLJD
 * @Date: 2023/4/24
 */
@Component
@RocketMQMessageListener(topic = "seckillTopic3",
        consumerGroup = "seckill-consumer-group2",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 40
)
public class SeckillListener implements RocketMQListener<MessageExt> {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    int ZX_TIME = 20000;

    /**
     * 扣减库存
     * 写订单表
     *
     * @param message
     */
//    @Override
//    public void onMessage(MessageExt message) {
//        String msg = new String(message.getBody());
//        // userId + "-" + goodsId
//        Integer userId = Integer.parseInt(msg.split("-")[0]);
//        Integer goodsId = Integer.parseInt(msg.split("-")[1]);
//        // 方案一: MySQL默认隔离级别是RR，所以一定要在事务块外面加锁：保证第一个并发先提交事务，第二个再获取锁，这样才可以实现并发安全，要不然锁不住
//        但是jvm锁没法集群
    // jvm  EntrySet WaitSet
//        synchronized (this) {
//            goodsService.realSeckill(userId, goodsId);
//        }
//    }


//     方案二  分布式锁  mysql(行锁)   可以做分布式锁，但不适合并发较大场景
//    @Override
//    public void onMessage(MessageExt message) {
//        String msg = new String(message.getBody());
//        // userId + "-" + goodsId
//        Integer userId = Integer.parseInt(msg.split("-")[0]);
//        Integer goodsId = Integer.parseInt(msg.split("-")[1]);
//        goodsService.realSeckill(userId, goodsId);
//    }

    // 方案三: redis setnx 分布式锁  压力会分摊到redis和程序中执行  缓解db的压力
    @Override
    public void onMessage(MessageExt message) {
        String msg = new String(message.getBody());
        Integer userId = Integer.parseInt(msg.split("-")[0]);
        Integer goodsId = Integer.parseInt(msg.split("-")[1]);
        int currentThreadTime = 0;
        while (true) {
            // redis分布式锁，这里给一个key的过期时间,可以避免死锁的发生
            Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock:" + goodsId, "", Duration.ofSeconds(30));
            if (flag) {
                // 拿到锁成功
                try {
                    goodsService.realSeckill(userId, goodsId);
                    return;
                } finally {
                    // 删除
                    redisTemplate.delete("lock:" + goodsId);
                }
            } else {
                //自选次数+1
                currentThreadTime += 200;
                try {
                    //这次没拿到锁，我们认定紧接着下次拿到锁的概率还是很小，所以我们让该线程睡一会
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
