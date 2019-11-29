package rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;


public class Recv {

    public static void main(String[] args) throws Exception {


        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QueueCostant.HELLO, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(QueueCostant.HELLO, true, deliverCallback, consumerTag -> {
        });


    }
}
