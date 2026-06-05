package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.port.in.GetTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.out.TenantRepositoryPort;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetTenantUseCaseImpl implements GetTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    public GetTenantUseCaseImpl(TenantRepositoryPort tenantRepositoryPort) {
        this.tenantRepositoryPort = tenantRepositoryPort;
    }

    @Override
    public Tenant execute(UUID tenantId) {
        return tenantRepositoryPort.findById(tenantId)
                .orElseThrow(() -> new DomainValidationException("Tenant not found"));
    }
}

