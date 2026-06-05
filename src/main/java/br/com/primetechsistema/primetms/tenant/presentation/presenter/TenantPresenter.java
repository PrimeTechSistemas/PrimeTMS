package br.com.primetechsistema.primetms.tenant.presentation.presenter;

import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import br.com.primetechsistema.primetms.tenant.presentation.dto.response.CreateTenantResponse;

public final class TenantPresenter {

    private TenantPresenter() {}

    public static CreateTenantResponse toResponse(Tenant tenant) {
        return new CreateTenantResponse(
                tenant.getTenantId(),
                tenant.getName(),
                tenant.getDocument(),
                tenant.getResponsibleName(),
                tenant.getResponsibleEmail(),
                tenant.getResponsiblePhone(),
                tenant.getPlan(),
                tenant.getStatus(),
                tenant.getMaxDrivers(),
                tenant.getMaxVehicles(),
                tenant.getMaxUsers(),
                tenant.getTrialUntil(),
                tenant.getSubscriptionEndAt(),
                tenant.getTimezone(),
                tenant.getLogoUrl(),
                tenant.getCreatedAt(),
                tenant.getUpdatedAt(),
                tenant.isActive()
        );
    }
}

