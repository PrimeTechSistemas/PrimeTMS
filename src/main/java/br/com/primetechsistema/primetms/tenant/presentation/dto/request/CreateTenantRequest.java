package br.com.primetechsistema.primetms.tenant.presentation.dto.request;

import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateTenantRequest(
        @NotBlank(message = "tenant.name.required")
        String name,

        @NotBlank(message = "tenant.document.required")
        String document,

        @NotBlank(message = "tenant.responsibleName.required")
        String responsibleName,

        @Email(message = "tenant.responsibleEmail.invalid")
        @NotBlank(message = "tenant.responsibleEmail.required")
        String responsibleEmail,

        @NotBlank(message = "tenant.responsiblePhone.required")
        String responsiblePhone,

        @NotNull(message = "tenant.plan.required")
        TenantPlan plan,

        @Positive(message = "tenant.maxDrivers.positive")
        int maxDrivers,

        @Positive(message = "tenant.maxVehicles.positive")
        int maxVehicles,

        @Positive(message = "tenant.maxUsers.positive")
        int maxUsers,

        LocalDateTime trialUntil,

        LocalDateTime subscriptionEndAt,

        @NotBlank(message = "tenant.timezone.required")
        String timezone,

        String logoUrl
) {
}

