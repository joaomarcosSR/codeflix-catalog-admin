package codeflix.catalog.admin.domain.resource;

import codeflix.catalog.admin.domain._share.value.object.ValueObject;

import java.util.Objects;

public class Resource extends ValueObject {
    private final String checksum;
    private final byte[] content;
    private final String contentType;
    private final String name;

    private Resource(final String checksum, final byte[] content, final String contentType, final String name) {
        this.checksum = Objects.requireNonNull(checksum);
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
    }

    public static Resource with(final String checksum, final byte[] content, final String contentType, final String name) {
        return new Resource(checksum, content, contentType, name);
    }

    public String checksum() {
        return this.checksum;
    }

    public byte[] content() {
        return this.content;
    }

    public String contentType() {
        return this.contentType;
    }

    public String name() {
        return this.name;
    }
}
