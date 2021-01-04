package com.maestro.app.utils.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QueueEventType {
    OPEN(0),
    SAVE(1),
    SILENT_SAVE(2),
    SUBMIT_FORCE(3),
    SUBMIT(4),
    REMOVE(5),
    ACCEPT(6),
    DECLINE(7),
    RESET(8),
    CREATE(9);

    private int status;

    QueueEventType(int status) {
        this.status = status;
    }

    @JsonValue
    public int getValue() {
        return this.status;
    }
}
