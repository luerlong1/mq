package rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;


public class Send {


    public static void main(String[] args) throws Exception{
        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {
            String message = "Hello World";
            channel.basicPublish("", QueueCostant.HELLO,null,message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
