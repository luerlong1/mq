import message.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Send {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        String str = "Hello World";
        Message message = new Message(str.getBytes(), new MessageProperties());
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.send("amqp.hello.exchange", "", message);
        }

    }
}
