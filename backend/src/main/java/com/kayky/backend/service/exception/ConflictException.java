package com.kayky.backend.service.exception;

public class ConflictException extends RuntimeException {

    private final String messageKey;

    public ConflictException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}