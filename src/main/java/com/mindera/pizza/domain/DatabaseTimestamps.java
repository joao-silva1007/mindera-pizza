package com.mindera.pizza.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
@EqualsAndHashCode
public class DatabaseTimestamps {

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        LocalDateTime time = LocalDateTime.now();
        this.createdAt = time;
        this.updatedAt = time;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
