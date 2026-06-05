package br.com.primetechsistema.primetms.tenant.infrastructure.persistence.repository;

import br.com.primetechsistema.primetms.tenant.infrastructure.persistence.entity.TenantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantJpaRepository extends JpaRepository<TenantJpaEntity, UUID> {

    boolean existsByDocument(String document);
}
