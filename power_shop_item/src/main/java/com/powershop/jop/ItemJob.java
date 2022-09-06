package com.powershop.jop;

import com.powershop.mapper.LocalMessageMapper;
import com.powershop.mq.ItemMqSender;
import com.powershop.pojo.LocalMessage;
import com.powershop.pojo.LocalMessageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemJob {

    @Autowired
    private LocalMessageMapper localMessageMapper;
    @Autowired
    private ItemMqSender itemMqSender;

    public void scanLocalMessage(){
        //1、扫描localMessage
        LocalMessageExample localMessageExample = new LocalMessageExample();
        LocalMessageExample.Criteria criteria = localMessageExample.createCriteria();
        criteria.andStateEqualTo(0);
        List<LocalMessage> localMessageList = localMessageMapper.selectByExample(localMessageExample);
        for (LocalMessage localMessage : localMessageList) {
            //2、发送消息
            itemMqSender.sendMsg(localMessage);
        }
    }
}
