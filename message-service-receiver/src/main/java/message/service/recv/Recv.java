package message.service.recv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Recv {
    private static Logger logger = LoggerFactory.getLogger(Recv.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queue.amqp-hello}", durable = "true"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange.amqp-hello}", type = "fanout", ignoreDeclarationExceptions = "true")
    ))
    public void onMessage(Message message){
       logger.info(new String(message.getBody()));
    }

}
