package rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitmq.ExchangeCostant;
import rabbitmq.util.ConnectionUtil;

import javax.management.remote.rmi._RMIConnection_Stub;
import java.io.IOException;
import java.util.Arrays;

public class Send {
    private static final String[] routingKeys = {"info", "warn", "error", "debug"};

    public static void main(String[] args) throws Exception {

        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(ExchangeCostant.EXCHANGE_DIRCET
                    , BuiltinExchangeType.DIRECT);
            for (String routingKey : routingKeys) {
                String message = "Send message " + routingKey;
                System.out.println(message);
                channel.basicPublish(ExchangeCostant.EXCHANGE_DIRCET, routingKey, null, message.getBytes());
            }
        }
    }
}
