package com.penck.producer.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.kafka.support.KafkaProducerContext;
import org.springframework.integration.kafka.support.ProducerConfiguration;
import org.springframework.integration.kafka.support.ProducerFactoryBean;
import org.springframework.integration.kafka.support.ProducerMetadata;
import org.springframework.messaging.MessageHandler;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Created by peng on 2017/4/19.
 */

@Configuration
public class KafkaConfig {
    private final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Autowired
    KafkaProperties kafkaProperties;

    @ServiceActivator(inputChannel = "topic-01")
    @Bean
    public MessageHandler topic01Handler() throws Exception {
        String topic = "topic-01";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "topic-02")
    @Bean
    public MessageHandler topic02Handler() throws Exception {
        String topic = "topic-02";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "topic-03")
    @Bean
    public MessageHandler topic03Handler() throws Exception {
        String topic = "topic-03";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "topic-04")
    @Bean
    public MessageHandler topic04Handler() throws Exception {
        String topic = "topic-04";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    public KafkaProducerContext producerContext(String topic) throws Exception {
        KafkaProducerContext kafkaProducerContext = new KafkaProducerContext();
        ProducerMetadata<String, String> producerMetadata = new ProducerMetadata<>(topic, String.class,
                String.class, new StringSerializer(), new StringSerializer());
        Properties props = new Properties();
        props.put("linger.ms", "1000");
        ProducerFactoryBean<String, String> producer = new ProducerFactoryBean<>(producerMetadata, kafkaProperties.getBrokerAddress(), props);
        ProducerConfiguration<String, String> config = new ProducerConfiguration<>(producerMetadata, producer.getObject());
        Map<String, ProducerConfiguration<?, ?>> producerConfigurationMap =
                Collections.<String, ProducerConfiguration<?, ?>>singletonMap(topic, config);
        kafkaProducerContext.setProducerConfigurations(producerConfigurationMap);
        return kafkaProducerContext;
    }
}
