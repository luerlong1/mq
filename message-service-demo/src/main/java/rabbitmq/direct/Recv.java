package rabbitmq.direct;

import com.rabbitmq.client.*;
import rabbitmq.ExchangeCostant;
import rabbitmq.util.ConnectionUtil;

public class Recv {
    private static final String[] routingKeys = {"info", "warn"};

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_DIRCET
                , BuiltinExchangeType.DIRECT);
        // 声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();

        for (String routingKey : routingKeys) {
            channel.queueBind(queueName, ExchangeCostant.EXCHANGE_DIRCET, routingKey);
        }


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }
}
