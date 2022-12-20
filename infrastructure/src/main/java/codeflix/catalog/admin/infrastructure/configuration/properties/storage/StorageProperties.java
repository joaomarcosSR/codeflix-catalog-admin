package codeflix.catalog.admin.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(StorageProperties.class);
    private String locationPattern;
    private String fileNamePattern;

    public StorageProperties() {
    }

    public String getLocationPattern() {
        return this.locationPattern;
    }

    public void setLocationPattern(final String locationPattern) {
        this.locationPattern = locationPattern;
    }

    public String getFileNamePattern() {
        return this.fileNamePattern;
    }

    public void setFileNamePattern(final String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    @Override
    public void afterPropertiesSet() {
        log.debug(this.toString());
    }

    @Override
    public String toString() {
        return "StorageProperties{" +
                "locationPattern='" + this.locationPattern + '\'' +
                ", fileNamePattern='" + this.fileNamePattern + '\'' +
                '}';
    }
}
