package codeflix.catalog.admin.infrastructure.configuration;

import codeflix.catalog.admin.infrastructure.configuration.annotations.VideoCreatedQueue;
import codeflix.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import codeflix.catalog.admin.infrastructure.services.EventService;
import codeflix.catalog.admin.infrastructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {
    @Bean
    @VideoCreatedQueue
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties properties,
            final RabbitOperations operations
    ) {
        return new RabbitEventService(properties.getExchange(), properties.getRoutingKey(), operations);
    }
}
