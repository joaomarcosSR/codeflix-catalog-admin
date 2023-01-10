package codeflix.catalog.admin.infrastructure.amqp;

import codeflix.catalog.admin.application.video.media.update.UpdateMediaStatusCommand;
import codeflix.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import codeflix.catalog.admin.domain.video.MediaStatus;
import codeflix.catalog.admin.infrastructure.configuration.json.Json;
import codeflix.catalog.admin.infrastructure.video.models.VideoEncoderCompleted;
import codeflix.catalog.admin.infrastructure.video.models.VideoEncoderError;
import codeflix.catalog.admin.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {

    static final String LISTENER_ID = "videoEncodedListener";
    private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);
    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(final UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final VideoEncoderResult aResult = Json.readValue(message, VideoEncoderResult.class);

        if (aResult instanceof VideoEncoderCompleted dto) {
            log.info("[message:video.listener.income] [status:completed] [payload:{}]", message);
            final UpdateMediaStatusCommand aCommand = UpdateMediaStatusCommand.with(
                    MediaStatus.COMPLETED,
                    dto.id(),
                    dto.video().resourceId(),
                    dto.video().encodedVideoFolder(),
                    dto.video().filePath()
            );

            this.updateMediaStatusUseCase.execute(aCommand);
        } else if (aResult instanceof VideoEncoderError)
            log.error("[message:video.listener.income] [status:error] [payload:{}]", message);
        else log.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
    }
}
