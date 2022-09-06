package com.powershop.mq;

import com.powershop.mapper.LocalMessageMapper;
import com.powershop.pojo.LocalMessage;
import com.powershop.utils.JsonUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemMqSender implements ConfirmCallback {
    @Autowired
    private LocalMessageMapper localMessageMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    //The Spectre
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            // 消息发送成功,更新本地消息为已成功发送状态或者直接删除该本地消息记录
            String txNo = correlationData.getId();
            LocalMessage localMessage = new LocalMessage();
            localMessage.setTxNo(txNo);
            localMessage.setState(1);
            localMessageMapper.updateByPrimaryKeySelective(localMessage);
        }
    }

    public void sendMsg(LocalMessage localMessage) {
        RabbitTemplate rabbitTemplate = (RabbitTemplate) this.amqpTemplate;
        rabbitTemplate.setConfirmCallback(this);//调用当前类的confirm()

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(localMessage.getTxNo());//设置关联数据，给confirm()使用
        rabbitTemplate.convertAndSend("item_exchange","item.add", JsonUtils.objectToJson(localMessage),correlationData);
    }
}
