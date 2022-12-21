package codeflix.catalog.admin.infrastructure.configuration.properties.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class QueueProperties implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(QueueProperties.class);

    private String exchange;
    private String routingKey;
    private String queue;

    public QueueProperties() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(this.toString());
    }

    @Override
    public String toString() {
        return "QueueProperties{" +
                "exchange='" + this.exchange + '\'' +
                ", routingKey='" + this.routingKey + '\'' +
                ", queue='" + this.queue + '\'' +
                '}';
    }

    public String getExchange() {
        return this.exchange;
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return this.routingKey;
    }

    public void setRoutingKey(final String routingKey) {
        this.routingKey = routingKey;
    }

    public String getQueue() {
        return this.queue;
    }

    public void setQueue(final String queue) {
        this.queue = queue;
    }
}
