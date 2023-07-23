package com.study.domain.exceptions;

import com.study.domain.validation.handler.Notification;

public class NotificationException extends DomainException{
    public NotificationException(String message, Notification notification) {
        super(message, notification.getErrors());
    }
}
