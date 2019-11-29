import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spring.Application;
import spring.entiry.Order;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AmqpTest {
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Before
    public void setUp() {
//        rabbitAdmin.declareExchange(new TopicExchange("spring.topic.exchange"));
//        rabbitAdmin.declareQueue(new Queue("spring.tocpic.queue"));
//        rabbitAdmin.declareBinding(new Binding("spring.topic.exchange",
//                Binding.DestinationType.QUEUE,
//                "spring.topic.exchange",
//                "spring.topic.#",
//                null));

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("spring.topic.queue"))
                .to(new TopicExchange("spring.topic.exchange"))
                .with("spring.topic.#"));
    }

    @Test
    public void testSendMessage() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述");
        messageProperties.getHeaders().put("type", "自定义消息类型");
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        Message message = new Message("Hello World".getBytes(), messageProperties);
        rabbitTemplate.send("order.exchange", "order.binding", message,correlationData);

    }

    @Test
    public void testSendMessageWithUniqueID() {
        Message message = getMessageWithBulider("Hello world");
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("order.topic.exchange", "order.binding", message, correlationData);

    }

    @Test
    public void testSendMessageWithPojo() {
        Order order = new Order();
        order.setAge(11);
        order.setName("王五");
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("order.topic.exchange", "order.xxx", order, correlationData);

    }

    public Message getMessageWithBulider(String messageBody) {
        Message message = MessageBuilder.withBody(messageBody.getBytes())
                .setMessageId("123")
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("desc", "信息描述").build();
        return message;
    }

    public Message getJsonMessageWithBulider(Object object) {
        Message message = MessageBuilder.withBody(JSONObject.toJSONString(object).getBytes())
                .setMessageId("123")
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setHeader("desc", "信息描述").build();
        return message;
    }
}
