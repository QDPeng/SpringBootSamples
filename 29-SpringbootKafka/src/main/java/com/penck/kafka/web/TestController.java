package com.penck.kafka.web;

import com.penck.kafka.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by peng on 2017/4/20.
 */
@RestController
public class TestController {

    @Autowired
    private Producer producer;

    @RequestMapping("/produce")
    public void produce() {
        producer.sendMessage();
    }
}
