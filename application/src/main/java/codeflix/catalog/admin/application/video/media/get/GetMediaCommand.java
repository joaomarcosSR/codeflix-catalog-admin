package codeflix.catalog.admin.application.video.media.get;

public record GetMediaCommand(
        String videoId,
        String type
) {
    public static GetMediaCommand with(final String anId, final String aType) {
        return new GetMediaCommand(anId, aType);
    }
}
