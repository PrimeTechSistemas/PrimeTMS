package br.com.primetechsistema.primetms.tenant.domain.model;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.shared.domain.model.BaseEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Tenant extends BaseEntity {

    private String name;
    private String document;
    private String responsibleName;
    private String responsibleEmail;
    private String responsiblePhone;
    private TenantPlan plan;
    private TenantStatus status;
    private int maxDrivers;
    private int maxVehicles;
    private int maxUsers;
    private LocalDateTime trialUntil;
    private LocalDateTime subscriptionEndAt;
    private String timezone;
    private String logoUrl;

    public Tenant(
            String name,
            String document,
            String responsibleName,
            String responsibleEmail,
            String responsiblePhone,
            TenantPlan plan,
            int maxDrivers,
            int maxVehicles,
            int maxUsers,
            LocalDateTime trialUntil,
            LocalDateTime subscriptionEndAt,
            String timezone,
            String logoUrl) {

        super();
        this.name = requireText(name, "name");
        this.document = requireText(document, "document");
        this.responsibleName = requireText(responsibleName, "responsibleName");
        this.responsibleEmail = normalizeEmail(responsibleEmail);
        this.responsiblePhone = requireText(responsiblePhone, "responsiblePhone");
        this.plan = requireNonNull(plan, "plan");
        this.status = plan == TenantPlan.TRIAL ? TenantStatus.TRIAL : TenantStatus.ACTIVE;
        this.maxDrivers = requirePositive(maxDrivers, "maxDrivers");
        this.maxVehicles = requirePositive(maxVehicles, "maxVehicles");
        this.maxUsers = requirePositive(maxUsers, "maxUsers");
        this.trialUntil = trialUntil;
        this.subscriptionEndAt = subscriptionEndAt;
        this.timezone = requireText(timezone, "timezone");
        this.logoUrl = trimToNull(logoUrl);

        validateTrial();
        validateSubscription();
    }

    public Tenant(
            UUID tenantId,
            String name,
            String document,
            String responsibleName,
            String responsibleEmail,
            String responsiblePhone,
            TenantPlan plan,
            TenantStatus status,
            int maxDrivers,
            int maxVehicles,
            int maxUsers,
            LocalDateTime trialUntil,
            LocalDateTime subscriptionEndAt,
            String timezone,
            String logoUrl,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            UUID createdBy,
            UUID updatedBy,
            boolean active) {

        super(tenantId, createdAt, updatedAt, createdBy, updatedBy, active);
        this.name = requireText(name, "name");
        this.document = requireText(document, "document");
        this.responsibleName = requireText(responsibleName, "responsibleName");
        this.responsibleEmail = normalizeEmail(responsibleEmail);
        this.responsiblePhone = requireText(responsiblePhone, "responsiblePhone");
        this.plan = requireNonNull(plan, "plan");
        this.status = requireNonNull(status, "status");
        this.maxDrivers = requirePositive(maxDrivers, "maxDrivers");
        this.maxVehicles = requirePositive(maxVehicles, "maxVehicles");
        this.maxUsers = requirePositive(maxUsers, "maxUsers");
        this.trialUntil = trialUntil;
        this.subscriptionEndAt = subscriptionEndAt;
        this.timezone = requireText(timezone, "timezone");
        this.logoUrl = trimToNull(logoUrl);

        validateTrial();
        validateSubscription();
        validateStatusConsistency();
    }

    public UUID getTenantId() {
        return getId();
    }

    public void rename(String name) {
        this.name = requireText(name, "name");
        touch();
    }

    public void changeDocument(String document) {
        this.document = requireText(document, "document");
        touch();
    }

    public void changeResponsible(String name, String email, String phone) {
        this.responsibleName = requireText(name, "responsibleName");
        this.responsibleEmail = normalizeEmail(email);
        this.responsiblePhone = requireText(phone, "responsiblePhone");
        touch();
    }

    public void changePlan(TenantPlan plan, LocalDateTime subscriptionEndAt) {
        TenantPlan newPlan = requireNonNull(plan, "plan");
        this.plan = newPlan;
        this.subscriptionEndAt = subscriptionEndAt;

        if (newPlan == TenantPlan.TRIAL) {
            this.status = TenantStatus.TRIAL;
        } else {
            this.status = TenantStatus.ACTIVE;
            this.trialUntil = null;
        }

        validateSubscription();
        super.activate();
    }

    public void changeLimits(int maxDrivers, int maxVehicles, int maxUsers) {
        this.maxDrivers = requirePositive(maxDrivers, "maxDrivers");
        this.maxVehicles = requirePositive(maxVehicles, "maxVehicles");
        this.maxUsers = requirePositive(maxUsers, "maxUsers");
        touch();
    }

    public void extendTrial(LocalDateTime trialUntil) {
        if (plan != TenantPlan.TRIAL && status != TenantStatus.TRIAL) {
            throw new DomainValidationException("Only trial tenants can have trialUntil changed");
        }

        this.trialUntil = requireFuture(trialUntil, "trialUntil");
        this.status = TenantStatus.TRIAL;
        super.activate();
    }

    public void renewSubscription(LocalDateTime subscriptionEndAt) {
        this.subscriptionEndAt = requireFuture(subscriptionEndAt, "subscriptionEndAt");

        if (status != TenantStatus.BLOCKED) {
            this.status = TenantStatus.ACTIVE;
            super.activate();
        } else {
            touch();
        }
    }

    public void changeTimezone(String timezone) {
        this.timezone = requireText(timezone, "timezone");
        touch();
    }

    public void changeLogoUrl(String logoUrl) {
        this.logoUrl = trimToNull(logoUrl);
        touch();
    }

    public void suspend() {
        this.status = TenantStatus.SUSPENDED;
        super.deactivate();
    }

    public void block() {
        this.status = TenantStatus.BLOCKED;
        super.deactivate();
    }

    public void inactivate() {
        deactivate();
    }

    @Override
    public void activate() {
        this.status = plan == TenantPlan.TRIAL ? TenantStatus.TRIAL : TenantStatus.ACTIVE;
        super.activate();
    }

    @Override
    public void deactivate() {
        this.status = TenantStatus.INACTIVE;
        super.deactivate();
    }

    private void validateTrial() {
        if ((plan == TenantPlan.TRIAL || status == TenantStatus.TRIAL) && trialUntil == null) {
            throw new DomainValidationException("trialUntil is required for trial tenants");
        }
    }

    private void validateSubscription() {
        if (plan != TenantPlan.TRIAL && subscriptionEndAt == null) {
            throw new DomainValidationException("subscriptionEndAt is required for non-trial tenants");
        }
    }

    private void validateStatusConsistency() {
        if ((status == TenantStatus.ACTIVE || status == TenantStatus.TRIAL) && !isActive()) {
            throw new DomainValidationException("Active tenant status requires active entity");
        }

        if ((status == TenantStatus.INACTIVE || status == TenantStatus.SUSPENDED || status == TenantStatus.BLOCKED)
                && isActive()) {
            throw new DomainValidationException("Inactive tenant status requires inactive entity");
        }
    }

    private static String normalizeEmail(String email) {
        String normalizedEmail = requireText(email, "responsibleEmail").toLowerCase();

        if (!normalizedEmail.contains("@")) {
            throw new DomainValidationException("responsibleEmail must be valid");
        }

        return normalizedEmail;
    }

    private static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new DomainValidationException(fieldName + " must be greater than zero");
        }

        return value;
    }

    private static LocalDateTime requireFuture(LocalDateTime value, String fieldName) {
        LocalDateTime dateTime = requireNonNull(value, fieldName);

        if (!dateTime.isAfter(LocalDateTime.now())) {
            throw new DomainValidationException(fieldName + " must be in the future");
        }

        return dateTime;
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
