package rabbitmq.faount;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;

public class Recv2 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_FAOUNT, BuiltinExchangeType.FANOUT);
        // 声明第一个队列
        channel.queueDeclare(QueueCostant.FAOUNT_THIRD, false, false, false, null);
        channel.queueBind(QueueCostant.FAOUNT_THIRD, ExchangeCostant.EXCHANGE_FAOUNT, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("-----" + consumerTag + "------");
            System.out.println(" [x] Received '" + message + "'"
                    + " DeliveryTag is " + delivery.getEnvelope().getDeliveryTag());
        };

        channel.basicConsume(QueueCostant.FAOUNT_THIRD, true, deliverCallback, consumerTag -> {
        });

    }
}
