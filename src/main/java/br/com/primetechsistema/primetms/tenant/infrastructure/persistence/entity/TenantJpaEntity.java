package br.com.primetechsistema.primetms.tenant.infrastructure.persistence.entity;

import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_tenants")
public class TenantJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "document", nullable = false, unique = true, length = 30)
    private String document;

    @Column(name = "responsible_name", nullable = false, length = 150)
    private String responsibleName;

    @Column(name = "responsible_email", nullable = false, length = 150)
    private String responsibleEmail;

    @Column(name = "responsible_phone", nullable = false, length = 30)
    private String responsiblePhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 30)
    private TenantPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TenantStatus status;

    @Column(name = "max_drivers", nullable = false)
    private int maxDrivers;

    @Column(name = "max_vehicles", nullable = false)
    private int maxVehicles;

    @Column(name = "max_users", nullable = false)
    private int maxUsers;

    @Column(name = "trial_until")
    private LocalDateTime trialUntil;

    @Column(name = "subscription_end_at")
    private LocalDateTime subscriptionEndAt;

    @Column(name = "timezone", nullable = false, length = 80)
    private String timezone;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "active", nullable = false)
    private boolean active;
}
