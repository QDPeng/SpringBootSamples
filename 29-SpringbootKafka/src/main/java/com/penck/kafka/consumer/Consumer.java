package com.penck.kafka.consumer;

import com.penck.kafka.domain.Message;
import com.penck.kafka.util.JSONUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by peng on 2017/4/20.
 */
@Component
public class Consumer {
    @KafkaListener(topics = "test1")
    public void processMessage(String content) {
        try {
            Message m = JSONUtils.json2pojo(content, Message.class);
            System.out.println(m.getId() + ",msg=" + m.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
