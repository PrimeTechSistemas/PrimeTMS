package br.com.primetechsistema.primetms.tenant.infrastructure.persistence.mapper;

import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import br.com.primetechsistema.primetms.tenant.infrastructure.persistence.entity.TenantJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TenantJpaMapper {

    public TenantJpaEntity toEntity(Tenant tenant) {
        TenantJpaEntity entity = new TenantJpaEntity();
        entity.setId(tenant.getTenantId());
        entity.setName(tenant.getName());
        entity.setDocument(tenant.getDocument());
        entity.setResponsibleName(tenant.getResponsibleName());
        entity.setResponsibleEmail(tenant.getResponsibleEmail());
        entity.setResponsiblePhone(tenant.getResponsiblePhone());
        entity.setPlan(tenant.getPlan());
        entity.setStatus(tenant.getStatus());
        entity.setMaxDrivers(tenant.getMaxDrivers());
        entity.setMaxVehicles(tenant.getMaxVehicles());
        entity.setMaxUsers(tenant.getMaxUsers());
        entity.setTrialUntil(tenant.getTrialUntil());
        entity.setSubscriptionEndAt(tenant.getSubscriptionEndAt());
        entity.setTimezone(tenant.getTimezone());
        entity.setLogoUrl(tenant.getLogoUrl());
        entity.setCreatedAt(tenant.getCreatedAt());
        entity.setUpdatedAt(tenant.getUpdatedAt());
        entity.setCreatedBy(tenant.getCreatedBy());
        entity.setUpdatedBy(tenant.getUpdatedBy());
        entity.setActive(tenant.isActive());
        return entity;
    }

    public Tenant toDomain(TenantJpaEntity entity) {
        return new Tenant(
                entity.getId(),
                entity.getName(),
                entity.getDocument(),
                entity.getResponsibleName(),
                entity.getResponsibleEmail(),
                entity.getResponsiblePhone(),
                entity.getPlan(),
                entity.getStatus(),
                entity.getMaxDrivers(),
                entity.getMaxVehicles(),
                entity.getMaxUsers(),
                entity.getTrialUntil(),
                entity.getSubscriptionEndAt(),
                entity.getTimezone(),
                entity.getLogoUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.isActive()
        );
    }
}
