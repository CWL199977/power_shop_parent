package com.powershop.mq;

import com.powershop.service.SearchService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class SearchMqRecver {

    @Autowired
    private SearchService searchService;

    /**
     * 接收消息的三要素：
     *  queue
     *  routingkey
     *  exchange
     */
    @RabbitListener(bindings = {@QueueBinding(
            value=@Queue(value = "item_queue", durable = "true"),
            exchange = @Exchange(value = "item_exchange", type = ExchangeTypes.TOPIC),
            key={"item.*"}
    )})
    public void listen(Long msg, Channel channel, Message message){
        try {
            //同步索引库
            searchService.addDoc(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
