package rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;


public class Recv {
    static String[] routingKeys = {"lazy.*", "*.black.#"};

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(QueueCostant.TOPIC_FIRST, false, false, false, null);
        for (String routingKey : routingKeys) {
            channel.queueBind(QueueCostant.TOPIC_FIRST, ExchangeCostant.EXCHANGE_TOPIC, routingKey);

        }
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(QueueCostant.TOPIC_FIRST, true, deliverCallback, consumerTag -> {
        });


    }
}
