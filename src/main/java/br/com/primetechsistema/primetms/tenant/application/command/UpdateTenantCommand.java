package br.com.primetechsistema.primetms.tenant.application.command;

import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateTenantCommand(
        UUID tenantId,
        String name,
        String document,
        String responsibleName,
        String responsibleEmail,
        String responsiblePhone,
        TenantPlan plan,
        Integer maxDrivers,
        Integer maxVehicles,
        Integer maxUsers,
        LocalDateTime trialUntil,
        LocalDateTime subscriptionEndAt,
        String timezone,
        String logoUrl
) {
}

