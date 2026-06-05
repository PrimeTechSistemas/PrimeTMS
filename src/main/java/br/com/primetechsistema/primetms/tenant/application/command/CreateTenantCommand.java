package br.com.primetechsistema.primetms.tenant.application.command;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;

import java.time.LocalDateTime;

public record CreateTenantCommand(
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

    public CreateTenantCommand {
        requireText(name, "name");
        requireText(document, "document");
        requireText(responsibleName, "responsibleName");
        requireText(responsibleEmail, "responsibleEmail");
        requireText(responsiblePhone, "responsiblePhone");
        requireNonNull(plan, "plan");
        requirePositive(maxDrivers, "maxDrivers");
        requirePositive(maxVehicles, "maxVehicles");
        requirePositive(maxUsers, "maxUsers");
        requireText(timezone, "timezone");
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(fieldName + " is required");
        }
    }

    private static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new DomainValidationException(fieldName + " must be greater than zero");
        }
    }

    private static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new DomainValidationException(fieldName + " is required");
        }
    }
}
