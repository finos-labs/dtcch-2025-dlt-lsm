package io.builders.demo.core.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate

import java.time.OffsetDateTime

@MappedSuperclass
abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 7933099L

    @Column
    OffsetDateTime createdAt

    @Column
    OffsetDateTime updatedAt

    @PrePersist
    void onCreate() {
        this.createdAt = OffsetDateTime.now()
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = OffsetDateTime.now()
    }

}
