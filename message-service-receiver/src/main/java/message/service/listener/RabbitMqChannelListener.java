package message.service.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.impl.AMQImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ChannelListener;
import org.springframework.stereotype.Component;

public class RabbitMqChannelListener implements ChannelListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqChannelListener.class);
    @Override
    public void onCreate(Channel channel, boolean b) {
        logger.info("rabbitmq channel [" + channel + "]create");
    }

    @Override
    public void onShutDown(ShutdownSignalException signal) {
        if (signal.isHardError()) {
            AMQImpl.Connection.Close close = (AMQImpl.Connection.Close) signal.getReason();
            logger.warn("=====================Connection onShutDown replyCode: {}, methodId: {}, classId: {}, replyText: {}",
                    close.getReplyCode(), close.getMethodId(), close.getClassId(), close.getReplyText());
        } else {
            AMQImpl.Channel.Close close = (AMQImpl.Channel.Close) signal.getReason();
            logger.warn("=====================Channel onShutDown replyCode: {}, methodId: {}, classId: {}, replyText: {}",
                    close.getReplyCode(), close.getMethodId(), close.getClassId(), close.getReplyText());
        }
    }
}
