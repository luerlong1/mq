package rabbitmq.consumerack;

import com.rabbitmq.client.*;
import rabbitmq.ExchangeCostant;
import rabbitmq.QueueCostant;
import rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Send {
    static SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_CONSUMER_ACK,
                BuiltinExchangeType.DIRECT, true, false, createBackUpExchange(channel));
        // confirm模式
        channel.confirmSelect();
        channel.addConfirmListener(
                (deliveryTag, multiple) -> {
                    System.out.println("Ack Callback DeliveryTag is [" + deliveryTag + "] multiple is " + multiple);
                    if (multiple) {
                        confirmSet.headSet(deliveryTag + 1).clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                    }
                },

                (deliveryTag, multiple) -> {
                }
        );
        channel.addReturnListener(returnMessage -> {
            String message = new String(returnMessage.getBody());
            System.out.println("No routing message " + message);
        });

        boolean mandatory = false;
        for (int i = 1; i <= 100; i++) {
            // 推送持久化消息
            long nextSeqNo = channel.getNextPublishSeqNo();
            String message = "Send message " + nextSeqNo;
            channel.basicPublish(ExchangeCostant.EXCHANGE_CONSUMER_ACK, "consumer.ack", mandatory, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            confirmSet.add(nextSeqNo);
            System.out.println(" [x] sent ' " + message + " '");
        }
    }

    public static Map<String, Object> createBackUpExchange(Channel channel) throws Exception {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange", ExchangeCostant.EXCHANGE_BACKUP);

        // 声明一个广播类型的交换机
        channel.exchangeDeclare(ExchangeCostant.EXCHANGE_BACKUP, BuiltinExchangeType.FANOUT, true, false, null);
        channel.queueDeclare(QueueCostant.BACK_UP, true, false, false, null);
        channel.queueBind(QueueCostant.BACK_UP, ExchangeCostant.EXCHANGE_BACKUP, "");

        return arguments;
    }
}
