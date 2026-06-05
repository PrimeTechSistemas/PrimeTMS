package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.command.UpdateTenantCommand;
import br.com.primetechsistema.primetms.tenant.application.port.in.UpdateTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.out.TenantRepositoryPort;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import org.springframework.stereotype.Service;

@Service
public class UpdateTenantUseCaseImpl implements UpdateTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    public UpdateTenantUseCaseImpl(TenantRepositoryPort tenantRepositoryPort) {
        this.tenantRepositoryPort = tenantRepositoryPort;
    }

    @Override
    public Tenant execute(UpdateTenantCommand command) {
        Tenant tenant = tenantRepositoryPort.findById(command.tenantId())
                .orElseThrow(() -> new DomainValidationException("Tenant not found"));

        if (command.name() != null) {
            tenant.rename(command.name());
        }

        if (command.document() != null) {
            tenant.changeDocument(command.document());
        }

        if (command.responsibleName() != null || command.responsibleEmail() != null || command.responsiblePhone() != null) {
            String name = command.responsibleName() != null ? command.responsibleName() : tenant.getResponsibleName();
            String email = command.responsibleEmail() != null ? command.responsibleEmail() : tenant.getResponsibleEmail();
            String phone = command.responsiblePhone() != null ? command.responsiblePhone() : tenant.getResponsiblePhone();
            tenant.changeResponsible(name, email, phone);
        }

        if (command.plan() != null) {
            tenant.changePlan(command.plan(), command.subscriptionEndAt());
        }

        if (command.maxDrivers() != null || command.maxVehicles() != null || command.maxUsers() != null) {
            int maxDrivers = command.maxDrivers() != null ? command.maxDrivers() : tenant.getMaxDrivers();
            int maxVehicles = command.maxVehicles() != null ? command.maxVehicles() : tenant.getMaxVehicles();
            int maxUsers = command.maxUsers() != null ? command.maxUsers() : tenant.getMaxUsers();
            tenant.changeLimits(maxDrivers, maxVehicles, maxUsers);
        }

        if (command.trialUntil() != null) {
            tenant.extendTrial(command.trialUntil());
        }

        if (command.subscriptionEndAt() != null) {
            tenant.renewSubscription(command.subscriptionEndAt());
        }

        if (command.timezone() != null) {
            tenant.changeTimezone(command.timezone());
        }

        if (command.logoUrl() != null) {
            tenant.changeLogoUrl(command.logoUrl());
        }

        return tenantRepositoryPort.save(tenant);
    }
}

