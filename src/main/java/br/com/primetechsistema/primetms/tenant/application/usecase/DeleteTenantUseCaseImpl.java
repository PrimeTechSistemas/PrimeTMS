package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.port.in.DeleteTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.out.TenantRepositoryPort;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteTenantUseCaseImpl implements DeleteTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    public DeleteTenantUseCaseImpl(TenantRepositoryPort tenantRepositoryPort) {
        this.tenantRepositoryPort = tenantRepositoryPort;
    }

    @Override
    public void execute(UUID tenantId) {
        Tenant tenant = tenantRepositoryPort.findById(tenantId)
                .orElseThrow(() -> new DomainValidationException("Tenant not found"));

        tenant.inactivate();
        tenantRepositoryPort.save(tenant);
    }
}

