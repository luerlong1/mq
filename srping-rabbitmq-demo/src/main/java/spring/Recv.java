package spring;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import spring.entiry.Order;

@Configuration
@EnableRabbit
public class Recv {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order.json.queue", durable = "true"),
            exchange = @Exchange(value = "order.json.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "order.json.*"
    ))
    public void processOrder(Order order) {
        System.out.println("[X] Recv : " + order.toString());
    }

    @RabbitListener(queues = {"order.queue", "order.topic.queue"})
    @RabbitHandler
    public void process(Message message) {
        System.out.println("[x] Recv: " + new String(message.getBody()));
    }

    @RabbitListener(queues = {"order.queue", "order.topic.queue"})
    @RabbitHandler
    public void processOrder1(@Payload Order order) {
        System.out.println("[x] Recv: " + order.toString());
    }
}
