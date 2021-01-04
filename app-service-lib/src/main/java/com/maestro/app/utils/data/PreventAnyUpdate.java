package com.maestro.app.utils.data;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PreventAnyUpdate {
    @PrePersist
    void onPrePersist(Object o) {
        throw new RuntimeException("The entity is readonly");
    }

    @PreUpdate
    void onPreUpdate(Object o) {
        throw new RuntimeException("The entity is readonly");
    }

    @PreRemove
    void onPreRemove(Object o) {
        throw new RuntimeException("The entity is readonly");
    }
}
