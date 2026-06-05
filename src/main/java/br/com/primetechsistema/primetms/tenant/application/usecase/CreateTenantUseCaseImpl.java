package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.command.CreateTenantCommand;
import br.com.primetechsistema.primetms.tenant.application.port.in.CreateTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.out.TenantRepositoryPort;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import org.springframework.stereotype.Service;

@Service
public class CreateTenantUseCaseImpl implements CreateTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    public CreateTenantUseCaseImpl(TenantRepositoryPort tenantRepositoryPort) {
        this.tenantRepositoryPort = tenantRepositoryPort;
    }

    @Override
    public Tenant execute(CreateTenantCommand command) {
        if (tenantRepositoryPort.existsByDocument(command.document())) {
            throw new DomainValidationException("Tenant document already exists");
        }

        Tenant tenant = new Tenant(
                command.name(),
                command.document(),
                command.responsibleName(),
                command.responsibleEmail(),
                command.responsiblePhone(),
                command.plan(),
                command.maxDrivers(),
                command.maxVehicles(),
                command.maxUsers(),
                command.trialUntil(),
                command.subscriptionEndAt(),
                command.timezone(),
                command.logoUrl()
        );

        return tenantRepositoryPort.save(tenant);
    }
}
