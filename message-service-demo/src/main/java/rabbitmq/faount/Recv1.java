package rabbitmq.faount;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;

public class Recv1 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_FAOUNT, BuiltinExchangeType.FANOUT);
        // 声明第一个队列
        channel.queueDeclare(QueueCostant.FAOUNT_FIRST, false, false, false, null);
        channel.queueBind(QueueCostant.FAOUNT_FIRST, ExchangeCostant.EXCHANGE_FAOUNT, "");

        // 声明第二个队列
        channel.queueDeclare(QueueCostant.FAOUNT_SECOND, false, false, false, null);
        channel.queueBind(QueueCostant.FAOUNT_SECOND, ExchangeCostant.EXCHANGE_FAOUNT, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("-----" + consumerTag + "------");
            System.out.println(" [x] Received '" + message + "'"
                    + " DeliveryTag is " + delivery.getEnvelope().getDeliveryTag());
        };

        channel.basicConsume(QueueCostant.FAOUNT_FIRST, true, deliverCallback, consumerTag -> {
        });
        channel.basicConsume(QueueCostant.FAOUNT_SECOND, true, deliverCallback, consumerTag -> {
        });

    }
}
