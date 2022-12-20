package codeflix.catalog.admin.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleStorageProperties implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(GoogleStorageProperties.class);

    private String bucket;
    private int connectTimeout;
    private int readTimeout;
    private int retryMaxAttempts;
    private int retryDelay;
    private int retryMaxDelay;
    private double retryMultiplier;

    @Override
    public void afterPropertiesSet() {
        log.debug(this.toString());
    }

    @Override
    public String toString() {
        return "GoogleStorageProperties{" +
                "bucket='" + this.bucket + '\'' +
                ", connectTimeout=" + this.connectTimeout +
                ", readTimeout=" + this.readTimeout +
                ", retryMaxAttempts=" + this.retryMaxAttempts +
                ", retryDelay=" + this.retryDelay +
                ", retryMaxDelay=" + this.retryMaxDelay +
                ", retryMultiplier=" + this.retryMultiplier +
                '}';
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getRetryMaxAttempts() {
        return this.retryMaxAttempts;
    }

    public void setRetryMaxAttempts(final int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public int getRetryDelay() {
        return this.retryDelay;
    }

    public void setRetryDelay(final int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public int getRetryMaxDelay() {
        return this.retryMaxDelay;
    }

    public void setRetryMaxDelay(final int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
    }

    public double getRetryMultiplier() {
        return this.retryMultiplier;
    }

    public void setRetryMultiplier(final double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }
}
