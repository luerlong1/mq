package spring;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpDeclare {

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("order.queue");
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with("order.binding");
    }


    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange("order.topic.exchange");
    }

    @Bean
    public Queue orderTopicQueue() {
        return new Queue("order.topic.queue");
    }

    @Bean
    public Binding orderTopicBinding() {
        return BindingBuilder.bind(orderTopicQueue())
                .to(orderTopicExchange())
                .with("order.#");
    }
}
