package rabbitmq.workqueue;

import com.rabbitmq.client.*;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;

public class Work {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_WORK_QUEUE,
                BuiltinExchangeType.DIRECT);
        // 限流
        channel.basicQos(1);
        // 声明一个队列
        // 持久化队列
        boolean durable = true;
        channel.queueDeclare(QueueCostant.WORK_QUEUE, durable, false, false, null);
        channel.queueBind(QueueCostant.WORK_QUEUE, ExchangeCostant.EXCHANGE_WORK_QUEUE, "work.queue");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("-----" + consumerTag + "------");
            System.out.println(" [x] Received '" + message + "'"
                    + " DeliveryTag is " + delivery.getEnvelope().getDeliveryTag());
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // System.out.println(" [x] Done");
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println(" [x] Done");
        };

        boolean autoAck = false;
        channel.basicConsume(QueueCostant.WORK_QUEUE, autoAck, deliverCallback, consumerTag -> {
        });

    }

    private static void doWork(String message) throws InterruptedException {
        for (char ch : message.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
