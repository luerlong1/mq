package rabbitmq.consumerack;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;

import java.util.concurrent.TimeUnit;

public class Recv {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_CONSUMER_ACK,
                BuiltinExchangeType.DIRECT);
        boolean durable = true;
        channel.queueDeclare(QueueCostant.CONSUMER_ACK, durable, false, false, null);
        channel.queueBind(QueueCostant.CONSUMER_ACK, ExchangeCostant.EXCHANGE_CONSUMER_ACK, "consumer.ack");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String message = new String(delivery.getBody(), "UTF-8");

                if (2 == delivery.getEnvelope().getDeliveryTag()) {
                    throw new IllegalStateException("消息无法被正常处理, DeliveryTag is [" + delivery.getEnvelope().getDeliveryTag() + "]");
                }

                System.out.println("ConsumerTag is [" + consumerTag + "]," +
                        " [x] Received '" + message + "'," +
                        " DeliveryTag is [" + delivery.getEnvelope().getDeliveryTag() + "]," +
                        " Thread is [" + Thread.currentThread().getName() + "]"
                );
                // 模拟处理消息的耗时
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 返回消息确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                System.out.println(" [x] Done");
            } catch (Exception e) {
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            }

        };
        boolean autoAck = false;
        channel.basicConsume(QueueCostant.CONSUMER_ACK, autoAck, deliverCallback, consumerTag -> {
        });
    }
}
