package com.penck.consumer.service;

import com.penck.consumer.config.KafkaProperties;
import com.penck.consumer.config.ZookeeperProperties;
import kafka.admin.AdminUtils;
import kafka.common.TopicExistsException;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.core.*;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.integration.kafka.listener.KafkaTopicOffsetManager;
import org.springframework.integration.kafka.listener.OffsetManager;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.kafka.serializer.common.StringDecoder;
import org.springframework.integration.kafka.support.*;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
/**
 * Created by peng on 2017/4/19.
 */
@Service
public class KafkaService {
    private final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Autowired
    KafkaProperties kafkaProperties;

    @Value("${consumer.topic}")
    private String topic;

    @ServiceActivator(inputChannel = "toKafka")
    @Bean
    public MessageHandler handler() throws Exception {
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext());
        handler.setTopicExpression(new LiteralExpression(this.topic));
//        handler.setMessageKeyExpression(new LiteralExpression(this.messageKey));
        return handler;
    }

    @Bean
    public ConnectionFactory kafkaBrokerConnectionFactory() throws Exception {
        return new DefaultConnectionFactory(kafkaConfiguration());
    }

    @Bean
    public Configuration kafkaConfiguration() {
        BrokerAddressListConfiguration configuration = new BrokerAddressListConfiguration(
                BrokerAddress.fromAddress(getBrokerAddress()));
        configuration.setSocketTimeout(500);
        return configuration;
    }

    @Bean
    public KafkaProducerContext producerContext() throws Exception {
        KafkaProducerContext kafkaProducerContext = new KafkaProducerContext();
        ProducerMetadata<String, String> producerMetadata = new ProducerMetadata<>(this.topic, String.class,
                String.class, new StringSerializer(), new StringSerializer());
        Properties props = new Properties();
        props.put("linger.ms", "1000");
        ProducerFactoryBean<String, String> producer = new ProducerFactoryBean<>(producerMetadata, getBrokerAddress(), props);
        ProducerConfiguration<String, String> config = new ProducerConfiguration<>(producerMetadata, producer.getObject());
        Map<String, ProducerConfiguration<?, ?>> producerConfigurationMap =
                Collections.<String, ProducerConfiguration<?, ?>>singletonMap(this.topic, config);
        kafkaProducerContext.setProducerConfigurations(producerConfigurationMap);
        return kafkaProducerContext;
    }

    @Bean
    public OffsetManager offsetManager() {
        return new KafkaTopicOffsetManager(new ZookeeperConnect(getZookeeperConnect()), "si-offsets");
    }

    @Bean
    public KafkaMessageListenerContainer container(OffsetManager offsetManager) throws Exception {
        final KafkaMessageListenerContainer kafkaMessageListenerContainer = new KafkaMessageListenerContainer(
                kafkaBrokerConnectionFactory(), new Partition(this.topic, 0));
        kafkaMessageListenerContainer.setOffsetManager(offsetManager);
        kafkaMessageListenerContainer.setMaxFetch(100);
        kafkaMessageListenerContainer.setConcurrency(1);
        return kafkaMessageListenerContainer;
    }

    @Bean
    public KafkaMessageDrivenChannelAdapter adapter(KafkaMessageListenerContainer container) {
        KafkaMessageDrivenChannelAdapter kafkaMessageDrivenChannelAdapter = new KafkaMessageDrivenChannelAdapter(container);
        StringDecoder decoder = new StringDecoder();
        kafkaMessageDrivenChannelAdapter.setKeyDecoder(decoder);
        kafkaMessageDrivenChannelAdapter.setPayloadDecoder(decoder);
        kafkaMessageDrivenChannelAdapter.setOutputChannel(received());
        return kafkaMessageDrivenChannelAdapter;
    }

    @Bean
    public PollableChannel received() {
        return new QueueChannel();
    }

    @Bean
    public TopicCreator topicCreator() {
        return new TopicCreator(this.topic, getZookeeperConnect());
    }

    public static class TopicCreator implements SmartLifecycle {
        private final String topic;
        private final String zkConnect;
        private volatile boolean running;

        public TopicCreator(String topic, String zkConnect) {
            this.topic = topic;
            this.zkConnect = zkConnect;
        }

        @Override
        public void start() {
            ZkClient client = new ZkClient(this.zkConnect, 10000, 10000, ZKStringSerializer$.MODULE$);
            try {
                AdminUtils.createTopic(client, this.topic, 1, 1, new Properties());
            } catch (TopicExistsException e) {
            }
            this.running = true;
        }

        @Override
        public void stop() {
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public int getPhase() {
            return Integer.MIN_VALUE;
        }

        @Override
        public boolean isAutoStartup() {
            return true;
        }

        @Override
        public void stop(Runnable callback) {
            callback.run();
        }
    }

    public String getBrokerAddress() {
        return kafkaProperties.getHost() + ":" + kafkaProperties.getPort();
    }

    public String getZookeeperConnect() {
        return zookeeperProperties.getHost() + ":" + zookeeperProperties.getPort();
    }
}

