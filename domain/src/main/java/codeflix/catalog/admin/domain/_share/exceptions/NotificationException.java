package codeflix.catalog.admin.domain._share.exceptions;

import codeflix.catalog.admin.domain._share.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
