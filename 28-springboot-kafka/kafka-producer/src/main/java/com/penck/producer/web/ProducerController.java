package com.penck.producer.web;


import com.penck.producer.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Created by peng on 2017/4/19.
 */
@Controller
@RequestMapping("/")
public class ProducerController {
    private final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    @Inject
    ConfigurableApplicationContext context;

    @Inject
    KafkaService kafkaService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("Home Controller");
//        kafkaService.sendMessage("test", "test message");
//        kafkaService.sendMessage("topic-01", "topic-01 message");
//        kafkaService.sendMessage("topic-02", "topic-02 message");
//        kafkaService.sendMessage("topic-03", "topic-03 message");
//        kafkaService.sendMessage("topic-04", "topic-04 message");

        return "index";
    }
}

