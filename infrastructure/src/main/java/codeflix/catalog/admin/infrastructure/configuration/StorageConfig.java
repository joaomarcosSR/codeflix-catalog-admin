package codeflix.catalog.admin.infrastructure.configuration;

import codeflix.catalog.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import codeflix.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import codeflix.catalog.admin.infrastructure.services.StorageService;
import codeflix.catalog.admin.infrastructure.services.impl.GoogleCloudStorageService;
import codeflix.catalog.admin.infrastructure.services.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalog-videos")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean(name = "storageService")
    @Profile({"development", "production"})
    public StorageService googleStorageService(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GoogleCloudStorageService(props.getBucket(), storage);
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
