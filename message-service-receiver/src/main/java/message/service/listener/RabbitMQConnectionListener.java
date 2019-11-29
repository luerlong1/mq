package message.service.listener;

import com.rabbitmq.client.BlockedListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;

public class RabbitMQConnectionListener implements ConnectionListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConnectionListener.class);
    private BlockedListener blockedListener = new RabbitBlockedListener();

    @Override
    public void onCreate(Connection connection) {
        connection.addBlockedListener(blockedListener);
        logger.info("================onCreate: {}", connection);
    }

    public void onClose(Connection connection) {
        connection.removeBlockedListener(blockedListener);
        logger.info("================onClose: {}", connection);
    }

    public void onShutDown(ShutdownSignalException signal) {
        logger.info("================onClose: {}", signal);
    }
}
