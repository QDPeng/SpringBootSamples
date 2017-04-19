package com.penck.consumer.web;

import com.penck.consumer.config.KafkaProperties;
import com.penck.consumer.config.ZookeeperProperties;
import com.penck.consumer.domain.UplinkMessage;
import com.penck.consumer.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Created by peng on 2017/4/19.
 */
@Controller
@RequestMapping("/")
public class ConsumerController {
    private final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @Inject
    ConfigurableApplicationContext context;

    @Inject
    KafkaService kafkaService;

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Autowired
    KafkaProperties kafkaProperties;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        logger.debug("zookeeperHost: {}", zookeeperProperties.getHost());
        logger.debug("zookeeperPort: {}", zookeeperProperties.getPort());
        logger.debug("kafkaHost: {}", kafkaProperties.getHost());
        logger.debug("kafkaPort: {}", kafkaProperties.getPort());

//        PollableChannel fromKafka = context.getBean("received", PollableChannel.class);
//        Message<?> received = fromKafka.receive(10000);
//        while (received != null) {
//            System.out.println(received);
//            logger.debug("Headers: " + received.getHeaders());
//            logger.debug("Payload: " + received.getPayload());
//            received = fromKafka.receive(10000);
//        }

        return "index";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String tempHome() {
        return "home";
    }

    @MessageMapping("/kafka")
    public UplinkMessage handleKafka(UplinkMessage incoming) {
        logger.info("Received message: " + incoming.getMessage());
        return incoming;
    }
}

