package rabbitmq.workqueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;

public class NewTask {
    public static void main(String[] args) throws Exception {
        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(ExchangeCostant.EXCHANGE_WORK_QUEUE,
                    BuiltinExchangeType.DIRECT);
            StringBuilder builder = new StringBuilder("message : ");
            for (int i = 0; i < 10; i++) {
                builder.append(".");
                String message = builder.toString();
                // 推送持久化消息
                channel.basicPublish(ExchangeCostant.EXCHANGE_WORK_QUEUE, "work.queue", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println(" [x] sent ' " + message + " '");
            }

        }
    }
}
