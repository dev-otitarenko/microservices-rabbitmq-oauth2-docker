package com.maestro.app.utils.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorStatus {
    ERROR("error"), INFO("info"), WARNING("warning");

    private String status;

    ErrorStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getValue() {
        return this.status;
    }
}
