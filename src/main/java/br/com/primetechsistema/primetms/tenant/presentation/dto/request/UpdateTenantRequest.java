package br.com.primetechsistema.primetms.tenant.presentation.dto.request;

import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;

import java.time.LocalDateTime;

public record UpdateTenantRequest(
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

