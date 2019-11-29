package message.service;

import lombok.Data;
import message.NamedThreadFactory;
import message.RabbitTaskExecutorConfiguration;
import message.service.listener.RabbitMQConnectionListener;
import message.service.listener.RabbitMqChannelListener;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.UUID;

@Configuration
//@Import(value = {RabbitAutoConfiguration.class, RabbitTaskExecutorConfiguration.class})
public class RabbitConfiguration {
    private final RabbitProperties rabbitProperties;

    public RabbitConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public RabbitConnectionFactoryEx connectionFactoryrEx(CachingConnectionFactory connectionFactory,
                                                          AsyncTaskExecutor amqpClientExecutor) {
        RabbitConnectionFactoryEx connectionFactoryEx = new RabbitConnectionFactoryEx();
        connectionFactory.addConnectionListener(new RabbitMQConnectionListener());
        connectionFactory.addChannelListener(new RabbitMqChannelListener());
        connectionFactory.setExecutor(amqpClientExecutor);
        connectionFactoryEx.setConnectionFactory(connectionFactory);
        return connectionFactoryEx;
    }

    @Bean(name = "rabbitListenerContainerFactoryEx")
    @ConditionalOnMissingBean(name = "rabbitListenerContainerFactoryEx")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "simple")
    public SimpleRabbitListenerContainerFactoryEx simpleRabbitListenerContainerFactoryEx(
            SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory, AsyncTaskExecutor rabbitTaskExecutor) {

        SimpleRabbitListenerContainerFactoryEx rabbitListenerContainerFactoryEx = new SimpleRabbitListenerContainerFactoryEx();
//        rabbitListenerContainerFactory.setConnectionFactory();
        rabbitListenerContainerFactory.setTaskExecutor(rabbitTaskExecutor);
        rabbitListenerContainerFactory.setConsumerTagStrategy((queueName) -> {
            return queueName + "-" + UUID.randomUUID().toString().replaceAll("-", "");
        });
        rabbitListenerContainerFactoryEx.setRabbitListenerContainerFactory(rabbitListenerContainerFactory);
        return rabbitListenerContainerFactoryEx;
    }

    @Bean(name = "rabbitListenerContainerFactoryEx")
    @ConditionalOnMissingBean(name = "rabbitListenerContainerFactoryEx")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "direct")
    public DirectRabbitListenerContainerFactoryEx simpleRabbitListenerContainerFactoryEx(
            CachingConnectionFactory connectionFactory,
            DirectRabbitListenerContainerFactory rabbitListenerContainerFactory, AsyncTaskExecutor rabbitTaskExecutor) {

        DirectRabbitListenerContainerFactoryEx rabbitListenerContainerFactoryEx = new DirectRabbitListenerContainerFactoryEx();
        rabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        rabbitListenerContainerFactory.setTaskExecutor(rabbitTaskExecutor);
        rabbitListenerContainerFactory.setConsumerTagStrategy((queueName) -> {
            return queueName + "-" + UUID.randomUUID().toString().replaceAll("-", "");
        });
        rabbitListenerContainerFactoryEx.setRabbitListenerContainerFactory(rabbitListenerContainerFactory);
        return rabbitListenerContainerFactoryEx;
    }


    @Data
    protected class RabbitConnectionFactoryEx {
        private CachingConnectionFactory connectionFactory;

        public RabbitConnectionFactoryEx() {

        }
    }

    @Data
    protected class SimpleRabbitListenerContainerFactoryEx {
        private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

        public SimpleRabbitListenerContainerFactoryEx() {

        }
    }

    @Data
    protected class DirectRabbitListenerContainerFactoryEx {
        private DirectRabbitListenerContainerFactory rabbitListenerContainerFactory;

        public DirectRabbitListenerContainerFactoryEx() {

        }
    }
}
