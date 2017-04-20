package com.penck.kafka.producer;

import com.penck.kafka.domain.Message;
import com.penck.kafka.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Created by peng on 2017/4/20.
 */
@Component
public class Producer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage() {
        Message m = new Message();
        m.setId(System.currentTimeMillis());
        m.setMsg(UUID.randomUUID().toString());
        m.setSendTime(new Date());
        try {
            kafkaTemplate.send("test1", JSONUtils.obj2json(m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
