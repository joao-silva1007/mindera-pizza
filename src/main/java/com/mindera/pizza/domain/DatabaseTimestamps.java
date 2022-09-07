package com.mindera.pizza.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EqualsAndHashCode
public abstract class DatabaseTimestamps {

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
