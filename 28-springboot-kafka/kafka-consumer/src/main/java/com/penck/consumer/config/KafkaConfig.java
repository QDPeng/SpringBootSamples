package com.penck.consumer.config;

import com.penck.consumer.ConsumerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.dsl.kafka.Kafka;
import org.springframework.integration.dsl.kafka.KafkaHighLevelConsumerMessageSourceSpec;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.kafka.support.ZookeeperConnect;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by peng on 2017/4/19.
 */
@Configuration
public class KafkaConfig {
    private final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Inject
    ConfigurableApplicationContext context;

    @Bean
    IntegrationFlow consumer() {
        logger.info("starting consumer..");
        KafkaHighLevelConsumerMessageSourceSpec messageSourceSpec = Kafka.inboundChannelAdapter(
                new ZookeeperConnect(zookeeperProperties.getZookeeperAddress()))
                .consumerProperties(props ->
                        props.put("auto.offset.reset", "smallest").put("auto.commit.interval.ms", "100"))
                .addConsumer("myGroup", metadata -> metadata.consumerTimeout(100)
                        .topicStreamMap(m -> m.put("test", 1))
                        .maxMessages(10)
                        .valueDecoder(String::new));

        Consumer<SourcePollingChannelAdapterSpec> endpointConfigurer = e -> e.poller(p -> p.fixedDelay(100));

        return IntegrationFlows
                .from(messageSourceSpec, endpointConfigurer)
                .<Map<String, List<String>>>handle((payload, headers) -> {
                    payload.entrySet().forEach(e -> logger.info(e.getKey() + '=' + e.getValue()));
                    return null;
                })
                .get();
    }
}
