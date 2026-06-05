package br.com.primetechsistema.primetms.tenant.presentation.dto.response;

import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTenantResponse(
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
        boolean active
) {
}

