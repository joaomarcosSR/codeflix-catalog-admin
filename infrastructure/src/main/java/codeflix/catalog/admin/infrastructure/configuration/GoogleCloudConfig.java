package codeflix.catalog.admin.infrastructure.configuration;

import codeflix.catalog.admin.infrastructure.configuration.properties.google.GoogleCloudProperties;
import codeflix.catalog.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Configuration
@Profile({"development", "production"})
public class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties("google.cloud")
    public GoogleCloudProperties googleCloudProperties() {
        return new GoogleCloudProperties();
    }

    @Bean
    @ConfigurationProperties("google.cloud.storage.catalog-videos")
    public GoogleStorageProperties googleStorageProperties() {
        return new GoogleStorageProperties();
    }

    @Bean
    public Credentials credentials(final GoogleCloudProperties props) throws IOException {
        final byte[] jsonContent = Base64.getMimeDecoder()
                .decode(Objects.requireNonNull(props.getCredentials()));

        return GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent));
    }

    @Bean
    public Storage storage(
            final Credentials credentials,
            final GoogleStorageProperties storageProperties
    ) {
        final HttpTransportOptions transportOptions = HttpTransportOptions.newBuilder()
                .setConnectTimeout(storageProperties.getConnectTimeout())
                .setReadTimeout(storageProperties.getReadTimeout())
                .build();

        final RetrySettings retrySettings = RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(storageProperties.getRetryDelay()))
                .setMaxRetryDelay(Duration.ofMillis(storageProperties.getRetryMaxDelay()))
                .setMaxAttempts(storageProperties.getRetryMaxAttempts())
                .setRetryDelayMultiplier(storageProperties.getRetryMultiplier())
                .build();

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setTransportOptions(transportOptions)
                .setRetrySettings(retrySettings)
                .build()
                .getService();
    }
}
