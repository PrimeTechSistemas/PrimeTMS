package br.com.primetechsistema.primetms.tenant.infrastructure.persistence.adapter;

import br.com.primetechsistema.primetms.tenant.application.port.out.TenantRepositoryPort;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import br.com.primetechsistema.primetms.tenant.infrastructure.persistence.mapper.TenantJpaMapper;
import br.com.primetechsistema.primetms.tenant.infrastructure.persistence.repository.TenantJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TenantRepositoryAdapter implements TenantRepositoryPort {

    private final TenantJpaRepository tenantJpaRepository;
    private final TenantJpaMapper tenantJpaMapper;

    public TenantRepositoryAdapter(
            TenantJpaRepository tenantJpaRepository,
            TenantJpaMapper tenantJpaMapper) {

        this.tenantJpaRepository = tenantJpaRepository;
        this.tenantJpaMapper = tenantJpaMapper;
    }

    @Override
    public Tenant save(Tenant tenant) {
        return tenantJpaMapper.toDomain(
                tenantJpaRepository.save(tenantJpaMapper.toEntity(tenant))
        );
    }

    @Override
    public Optional<Tenant> findById(UUID tenantId) {
        return tenantJpaRepository.findById(tenantId)
                .map(tenantJpaMapper::toDomain);
    }

    @Override
    public boolean existsByDocument(String document) {
        return tenantJpaRepository.existsByDocument(document);
    }
}
