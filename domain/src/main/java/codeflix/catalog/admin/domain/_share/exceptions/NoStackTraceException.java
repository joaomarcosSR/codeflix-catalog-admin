package codeflix.catalog.admin.domain._share.exceptions;

public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(final String aMessage) {
        this(aMessage, null);
    }

    public NoStackTraceException(final String aMessage, final Throwable aCause) {
        super(aMessage, aCause, true, false);
    }
}
