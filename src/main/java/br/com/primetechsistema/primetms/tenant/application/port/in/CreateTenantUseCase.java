package br.com.primetechsistema.primetms.tenant.application.port.in;

import br.com.primetechsistema.primetms.tenant.application.command.CreateTenantCommand;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;

public interface CreateTenantUseCase {

    Tenant execute(CreateTenantCommand command);
}
