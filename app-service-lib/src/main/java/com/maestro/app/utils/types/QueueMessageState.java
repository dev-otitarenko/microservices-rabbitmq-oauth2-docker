package com.maestro.app.utils.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QueueMessageState {
    PENDING(0),
    RUNNING(1),
    SUCCESS(2),
    WARNING(3),
    FAILURE(4),;

    private int status;

    QueueMessageState(int status) {
        this.status = status;
    }

    @JsonValue
    public int getValue() {
        return this.status;
    }
}
