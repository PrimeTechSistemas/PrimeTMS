package br.com.primetechsistema.primetms.audit.domain.model;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class AuditLog {

    private final UUID id;
    private final UUID tenantId;
    private final UUID userId;
    private final AuditAction action;
    private final String entityType;
    private final UUID entityId;
    private final String oldValue;
    private final String newValue;
    private final String ipAddress;
    private final String userAgent;
    private final LocalDateTime createdAt;

    public AuditLog(
            UUID tenantId,
            UUID userId,
            AuditAction action,
            String entityType,
            UUID entityId,
            String oldValue,
            String newValue,
            String ipAddress,
            String userAgent) {

        this(
                UUID.randomUUID(),
                tenantId,
                userId,
                action,
                entityType,
                entityId,
                oldValue,
                newValue,
                ipAddress,
                userAgent,
                LocalDateTime.now()
        );
    }

    public AuditLog(
            UUID id,
            UUID tenantId,
            UUID userId,
            AuditAction action,
            String entityType,
            UUID entityId,
            String oldValue,
            String newValue,
            String ipAddress,
            String userAgent,
            LocalDateTime createdAt) {

        this.id = Objects.requireNonNull(id, "id is required");
        this.tenantId = requireNonNull(tenantId, "tenantId");
        this.userId = requireNonNull(userId, "userId");
        this.action = requireNonNull(action, "action");
        this.entityType = requireText(entityType, "entityType");
        this.entityId = requireNonNull(entityId, "entityId");
        this.oldValue = trimToNull(oldValue);
        this.newValue = trimToNull(newValue);
        this.ipAddress = trimToNull(ipAddress);
        this.userAgent = trimToNull(userAgent);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");

        validateValuesByAction();
    }

    public UUID getAuditLogId() {
        return id;
    }

    private void validateValuesByAction() {
        if (action == AuditAction.CREATED && newValue == null) {
            throw new DomainValidationException("newValue is required for CREATED audit logs");
        }

        if (action == AuditAction.UPDATED && oldValue == null && newValue == null) {
            throw new DomainValidationException("oldValue or newValue is required for UPDATED audit logs");
        }

        if (action == AuditAction.DELETED && oldValue == null) {
            throw new DomainValidationException("oldValue is required for DELETED audit logs");
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(fieldName + " is required");
        }

        return value.trim();
    }

    private static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new DomainValidationException(fieldName + " is required");
        }

        return value;
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
