package br.com.primetechsistema.primetms.user.domain.model;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.shared.domain.model.BaseEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserAccount extends BaseEntity {

    private final UUID tenantId;

    private String name;
    private String email;
    private String passwordHash;
    private UserRole role;
    private UUID driverId;
    private LocalDateTime lastLogin;
    private boolean mfaEnabled;
    private UserStatus status;

    public UserAccount(
            UUID tenantId,
            String name,
            String email,
            String passwordHash,
            UserRole role) {

        super();
        this.tenantId = requireNonNull(tenantId, "tenantId");
        this.name = requireText(name, "name");
        this.email = normalizeEmail(email);
        this.passwordHash = requireText(passwordHash, "passwordHash");
        this.role = requireNonNull(role, "role");
        this.mfaEnabled = false;
        this.status = UserStatus.ACTIVE;
    }

    public UserAccount(
            UUID userId,
            UUID tenantId,
            String name,
            String email,
            String passwordHash,
            UserRole role,
            UUID driverId,
            LocalDateTime lastLogin,
            boolean mfaEnabled,
            UserStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            UUID createdBy,
            UUID updatedBy,
            boolean active) {

        super(userId, createdAt, updatedAt, createdBy, updatedBy, active);
        this.tenantId = requireNonNull(tenantId, "tenantId");
        this.name = requireText(name, "name");
        this.email = normalizeEmail(email);
        this.passwordHash = requireText(passwordHash, "passwordHash");
        this.role = requireNonNull(role, "role");
        this.driverId = driverId;
        this.lastLogin = lastLogin;
        this.mfaEnabled = mfaEnabled;
        this.status = requireNonNull(status, "status");

        validateDriverLink();
        validateStatusConsistency();
    }

    public UUID getUserId() {
        return getId();
    }

    public void rename(String name) {
        this.name = requireText(name, "name");
        touch();
    }

    public void changeEmail(String email) {
        this.email = normalizeEmail(email);
        touch();
    }

    public void changePassword(String passwordHash) {
        this.passwordHash = requireText(passwordHash, "passwordHash");
        touch();
    }

    public void changeRole(UserRole role) {
        UserRole newRole = requireNonNull(role, "role");

        if (newRole != UserRole.DRIVER && driverId != null) {
            throw new DomainValidationException("User linked to a driver must have DRIVER role");
        }

        this.role = newRole;
        touch();
    }

    public void linkDriver(UUID driverId) {
        if (role != UserRole.DRIVER) {
            throw new DomainValidationException("Only DRIVER users can be linked to a driver");
        }

        this.driverId = requireNonNull(driverId, "driverId");
        touch();
    }

    public void unlinkDriver() {
        this.driverId = null;
        touch();
    }

    public void registerLogin() {
        if (status != UserStatus.ACTIVE || !isActive()) {
            throw new DomainValidationException("Only active users can login");
        }

        this.lastLogin = LocalDateTime.now();
        touch();
    }

    public void enableMfa() {
        this.mfaEnabled = true;
        touch();
    }

    public void disableMfa() {
        this.mfaEnabled = false;
        touch();
    }

    public void block() {
        this.status = UserStatus.BLOCKED;
        super.deactivate();
    }

    public void unblock() {
        activate();
    }

    public void inactivate() {
        deactivate();
    }

    @Override
    public void activate() {
        this.status = UserStatus.ACTIVE;
        super.activate();
    }

    @Override
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        super.deactivate();
    }

    private void validateDriverLink() {
        if (driverId != null && role != UserRole.DRIVER) {
            throw new DomainValidationException("User linked to a driver must have DRIVER role");
        }
    }

    private void validateStatusConsistency() {
        if (status == UserStatus.ACTIVE && !isActive()) {
            throw new DomainValidationException("Active user status requires active entity");
        }

        if (status != UserStatus.ACTIVE && isActive()) {
            throw new DomainValidationException("Inactive or blocked user status requires inactive entity");
        }
    }

    private static String normalizeEmail(String email) {
        String normalizedEmail = requireText(email, "email").toLowerCase();

        if (!normalizedEmail.contains("@")) {
            throw new DomainValidationException("email must be valid");
        }

        return normalizedEmail;
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
}
