package message.service.listener;

import com.rabbitmq.client.BlockedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RabbitBlockedListener implements BlockedListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitBlockedListener.class);

    @Override
    public void handleBlocked(String s) throws IOException {
        logger.info("=========================connection blocked, reason: {}", s);
    }

    @Override
    public void handleUnblocked() throws IOException {
        logger.info("==============================connection unblocked");
    }
}
