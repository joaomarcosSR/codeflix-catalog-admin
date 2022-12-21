package codeflix.catalog.admin.infrastructure.configuration;

import codeflix.catalog.admin.infrastructure.configuration.annotations.VideoCreatedQueue;
import codeflix.catalog.admin.infrastructure.configuration.annotations.VideoEncodedQueue;
import codeflix.catalog.admin.infrastructure.configuration.annotations.VideoEvents;
import codeflix.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    @VideoCreatedQueue
    public QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    @VideoEncodedQueue
    public QueueProperties videoEncodedQueueProperties() {
        return new QueueProperties();
    }

    @Configuration
    static class Admin {

        @Bean
        @VideoEvents
        public Exchange videoEventsExchange(@VideoCreatedQueue final QueueProperties props) {
            return new DirectExchange(props.getExchange());
        }

        @Bean
        @VideoCreatedQueue
        public Queue videoCreatedQueue(@VideoCreatedQueue final QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @VideoCreatedQueue
        public Binding videoCreatedBinding(
                @VideoEvents final DirectExchange exchange,
                @VideoCreatedQueue final Queue queue,
                @VideoCreatedQueue final QueueProperties props
        ) {
            return BindingBuilder
                    .bind(queue)
                    .to(exchange)
                    .with(props.getRoutingKey());
        }

        @Bean
        @VideoEncodedQueue
        public Queue videoEncodedQueue(@VideoEncodedQueue final QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @VideoEncodedQueue
        public Binding videoEncodedBinding(
                @VideoEvents final DirectExchange exchange,
                @VideoEncodedQueue final Queue queue,
                @VideoEncodedQueue final QueueProperties props
        ) {
            return BindingBuilder
                    .bind(queue)
                    .to(exchange)
                    .with(props.getRoutingKey());
        }
    }
}
