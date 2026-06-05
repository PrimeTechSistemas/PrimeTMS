package br.com.primetechsistema.primetms.tenant.application.port.in;

import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;

import java.util.UUID;

public interface GetTenantUseCase {

    Tenant execute(UUID tenantId);
}

