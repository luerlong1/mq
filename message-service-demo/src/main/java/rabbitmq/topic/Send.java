package rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitmq.ExchangeCostant;
import rabbitmq.util.ConnectionUtil;

import java.util.Arrays;

public class Send {
    static String[] routingKeys = {"lazy", "lazy.fisrt", "lazy.fisrt.info", "black.info.name",
            "aaa.black.info", "bbb.black"};

    public static void main(String[] args) throws Exception {

        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(ExchangeCostant.EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);
            StringBuilder builder = new StringBuilder("Send message ");
            for (String routingKey : routingKeys) {
                String message = "Send message " + routingKey;
                channel.basicPublish(ExchangeCostant.EXCHANGE_TOPIC, routingKey, null, message.getBytes());

                System.out.println(" [x] Sent '" + message + "'");
            }
        }


    }
}
