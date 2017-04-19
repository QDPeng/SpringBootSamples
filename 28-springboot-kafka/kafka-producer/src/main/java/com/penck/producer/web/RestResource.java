package com.penck.producer.web;

import com.penck.producer.domain.MessagePayload;
import com.penck.producer.domain.TopicPayload;
import com.penck.producer.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peng on 2017/4/19.
 */
@RestController
@RequestMapping("/rest/api/v1")
public class RestResource {
    private final Logger logger = LoggerFactory.getLogger(RestResource.class);

    @Inject
    private KafkaService kafkaService;

    /**
     * GET  /topics -> get all topics.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<String>>> listTopics() {
        logger.info("REST request to get all Topics");
        List<String> topics = kafkaService.listTopics();
        HttpStatus status = topics != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        HashMap<String, List<String>> topicJson = new HashMap<>();
        topicJson.put("topics", topics);
        return new ResponseEntity<>(topicJson, status);
    }

    /**
     * POST  /topics -> create a topic.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createTopic(@RequestBody TopicPayload topicPayload) {
        String topic = topicPayload.getTopic();
        logger.info("REST request to create a Topic: {}", topic);
        kafkaService.createTopic(topic);
        HashMap<String, String> topicJson = new HashMap<>();
        topicJson.put("topic", topic);
        return new ResponseEntity<>(topicJson, HttpStatus.OK);
    }

    /**
     * POST  /messages -> send a message.
     */
    @RequestMapping(value = "/messages",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody MessagePayload messagePayload) {
        String topic = messagePayload.getTopic();
        String message = messagePayload.getMessage();
        logger.info("REST request to send a Message {} to a Topic {}", message, topic);
        kafkaService.sendMessage(topic, message);
        HashMap<String, String> topicJson = new HashMap<>();
        topicJson.put("status", "success");
        return new ResponseEntity<>(topicJson, HttpStatus.OK);
    }
}
