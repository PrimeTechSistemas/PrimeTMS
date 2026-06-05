package br.com.primetechsistema.primetms.shared.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class BaseEntity {

    protected final UUID id;
    protected final LocalDateTime createdAt;

    protected LocalDateTime updatedAt;
    protected UUID createdBy;
    protected UUID updatedBy;
    protected boolean active;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.active = true;
    }

    protected BaseEntity(
            UUID id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            UUID createdBy,
            UUID updatedBy,
            boolean active) {

        this.id = Objects.requireNonNull(id, "id is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.active = active;
    }

    protected void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        touch();
    }

    public void deactivate() {
        this.active = false;
        touch();
    }

    public void markCreated(UUID userId) {
        this.createdBy = userId;
    }

    public void markUpdated(UUID userId) {
        this.updatedBy = userId;
        touch();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
