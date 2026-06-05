package br.com.primetechsistema.primetms.tenant.application.port.in;

import java.util.UUID;

public interface DeleteTenantUseCase {

    void execute(UUID tenantId);
}

