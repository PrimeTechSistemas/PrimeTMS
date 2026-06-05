package br.com.primetechsistema.primetms.tenant.application.port.in;

import br.com.primetechsistema.primetms.tenant.application.command.UpdateTenantCommand;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;

public interface UpdateTenantUseCase {

    Tenant execute(UpdateTenantCommand command);
}

