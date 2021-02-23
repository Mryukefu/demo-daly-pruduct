package com.example.demodalypruduct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.Date;
@RestController
public class TestController {

    public static final String DELAYED_QUEUE_NAME = "delay.queue.demo.delay.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delay.queue.demo.delay.exchange";
    public static final String DELAYED_ROUTING_KEY = "delay.queue.demo.delay.routingkey";


    @Autowired
    public RabbitTemplate rabbitTemplate;

    @RequestMapping("delayMsg2")
    public void delayMsg2() {
        System.out.println("当前时间：{},收到请求，msg:{},delayTime:{}"+new Date()+"hello"+10000);
        sendDelayMsg("hello", 10000);
    }

    public void sendDelayMsg(String msg, Integer delayTime) {
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, msg, a ->{
            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }


    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveD(Message message) throws IOException {
        String msg = new String(message.getBody());
        System.out.println("当前时间：{},延时队列收到消息：{}"+new Date().toString()+msg);

    }
}
