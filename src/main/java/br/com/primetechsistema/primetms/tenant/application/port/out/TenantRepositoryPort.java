package br.com.primetechsistema.primetms.tenant.application.port.out;

import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepositoryPort {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(UUID tenantId);

    boolean existsByDocument(String document);
}
