package com.debo.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;



public class RabbitMqController {

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

        //加载配置文件
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext-rabbitmq-recep.xml");
        //获取rabbitmqTemplate模板
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);



    public void test(){

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", "1");
        map.put("name", "pig");
        //根据key发送到对应的队列
        rabbitTemplate.convertAndSend("que_pig_key", map);

        map.put("id", "2");
        map.put("name", "cat");
        //根据key发送到对应的队列
        rabbitTemplate.convertAndSend("que_cat_key", map);
    }

    public static void main(String[] args) {
        new RabbitMqController().test();
    }

}
