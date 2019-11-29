package rabbitmq.faount;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitmq.ExchangeCostant;
import rabbitmq.util.ConnectionUtil;

public class Send {
    public static void main(String[] args) throws Exception {
        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(ExchangeCostant.EXCHANGE_FAOUNT
                    , BuiltinExchangeType.FANOUT);

            for (int i = 0; i < 5; i++) {
                String message = "This number is" + i;
                // 路由关键字不能为null，填写""
                channel.basicPublish(ExchangeCostant.EXCHANGE_FAOUNT, "", null, message.getBytes());
                System.out.println(" [x] sent ' " + message + " '");
            }


        }
    }
}
