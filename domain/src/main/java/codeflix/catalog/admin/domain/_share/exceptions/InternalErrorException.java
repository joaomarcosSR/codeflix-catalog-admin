package codeflix.catalog.admin.domain._share.exceptions;

public class InternalErrorException extends NoStackTraceException {

    protected InternalErrorException(final String aMessage, final Throwable t) {
        super(aMessage, t);
    }

    public static InternalErrorException with(final String aMessage, final Throwable t) {
        return new InternalErrorException(aMessage, t);
    }
}
