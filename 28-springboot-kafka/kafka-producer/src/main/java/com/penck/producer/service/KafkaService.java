package com.penck.producer.service;

import com.penck.producer.config.KafkaProperties;
import com.penck.producer.config.ZookeeperProperties;
import kafka.admin.AdminUtils;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by peng on 2017/4/19.
 */

@Service
public class KafkaService {
    private final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    private ZookeeperProperties zookeeperProperties;

    @Inject
    private KafkaProperties kafkaProperties;

    @Inject
    ConfigurableApplicationContext context;

    public List<String> listTopics() {
        logger.debug("List Topics");
        SimpleConsumer consumer = new SimpleConsumer(kafkaProperties.getHost(),
                kafkaProperties.getPort(),
                kafkaProperties.getSoTimeout(),
                kafkaProperties.getBufferSize(),
                kafkaProperties.getClientId());
        List<String> topics = new ArrayList<>();
        TopicMetadataRequest request = new TopicMetadataRequest(topics);
        TopicMetadataResponse response = consumer.send(request);
        List<TopicMetadata> metadata = response.topicsMetadata();

        for (TopicMetadata item : metadata) {
            topics.add(item.topic());
        }

        logger.debug("Found {} Topics", topics.size());
        return topics;
    }

    public boolean doesTopicExist(String topic) {
        logger.debug("Does Topic {} exist?", topic);
        SimpleConsumer consumer = new SimpleConsumer(kafkaProperties.getHost(),
                kafkaProperties.getPort(),
                kafkaProperties.getSoTimeout(),
                kafkaProperties.getBufferSize(),
                kafkaProperties.getClientId());
        List<String> topics = new ArrayList<>();
        TopicMetadataRequest request = new TopicMetadataRequest(topics);
        TopicMetadataResponse response = consumer.send(request);
        List<TopicMetadata> metadata = response.topicsMetadata();

        for (TopicMetadata item : metadata) {
            if (item.topic().equals(topic)) {
                logger.debug("Found Topic {}.", topic);
                return true;
            }
        }
        logger.debug("Did not find Topic {}.", topic);
        return false;
    }

    public void createTopic(String topic) {
        logger.debug("Create Topic {}.", topic);
        if (doesTopicExist(topic)) {
            logger.debug("Topic %s already exists.", topic);
            return;
        }
        // Create a ZooKeeper client
        ZkClient zkClient = new ZkClient(zookeeperProperties.getHost() + ":" + zookeeperProperties.getPort(),
                zookeeperProperties.getSessionTimeoutMs(),
                zookeeperProperties.getConnectionTimeoutMs(),
                ZKStringSerializer$.MODULE$);

        // Create the topic
        Properties topicConfig = new Properties();
        AdminUtils.createTopic(zkClient, topic, kafkaProperties.getNumPartitions(), kafkaProperties.getReplicationFactor(), topicConfig);
        logger.debug("Topic {} is created.", topic);
    }

    public void sendMessage(String topic, String message) {
        if (!doesTopicExist(topic)) {
            logger.debug("Cannot send message {}. Topic {} does not exist!", message, topic);
            return;
        }

        MessageChannel channel = context.getBean(topic, MessageChannel.class);
        channel.send(new GenericMessage<>(message));
    }
}
