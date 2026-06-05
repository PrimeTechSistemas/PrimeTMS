package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.port.out.TenantRepositoryPort;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTenantUseCaseImpl Tests")
class DeleteTenantUseCaseImplTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @InjectMocks
    private DeleteTenantUseCaseImpl deleteTenantUseCase;

    private UUID tenantId;
    private Tenant activeTenant;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        activeTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.PROFESSIONAL,
                TenantStatus.ACTIVE,
                10,
                5,
                3,
                null,
                now.plusDays(365),
                "America/Sao_Paulo",
                null,
                now,
                now,
                null,
                null,
                true
        );
    }

    @Test
    @DisplayName("Should deactivate tenant successfully")
    void testDeleteTenantSuccess() {
        // Arrange
        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(activeTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(activeTenant);

        // Act
        deleteTenantUseCase.execute(tenantId);

        // Assert
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(tenantCaptor.capture());

        Tenant savedTenant = tenantCaptor.getValue();
        assertFalse(savedTenant.isActive());
        assertEquals(TenantStatus.INACTIVE, savedTenant.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when tenant not found")
    void testDeleteTenantNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(tenantRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class, () ->
                deleteTenantUseCase.execute(nonExistentId)
        );

        assertEquals("Tenant not found", exception.getMessage());
        verify(tenantRepositoryPort).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should deactivate trial tenant")
    void testDeleteTrialTenant() {
        // Arrange
        Tenant trialTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.TRIAL,
                TenantStatus.TRIAL,
                5,
                2,
                1,
                LocalDateTime.now().plusDays(7),
                null,
                "America/Sao_Paulo",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                true
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(trialTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(trialTenant);

        // Act
        deleteTenantUseCase.execute(tenantId);

        // Assert
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(tenantCaptor.capture());

        Tenant savedTenant = tenantCaptor.getValue();
        assertFalse(savedTenant.isActive());
    }

    @Test
    @DisplayName("Should deactivate already inactive tenant (idempotent)")
    void testDeleteAlreadyInactiveTenant() {
        // Arrange
        Tenant inactiveTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.BASIC,
                TenantStatus.INACTIVE,
                10,
                5,
                3,
                null,
                LocalDateTime.now().plusDays(365),
                "America/Sao_Paulo",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                false
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(inactiveTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(inactiveTenant);

        // Act
        deleteTenantUseCase.execute(tenantId);

        // Assert
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(tenantCaptor.capture());

        Tenant savedTenant = tenantCaptor.getValue();
        assertEquals(TenantStatus.INACTIVE, savedTenant.getStatus());
    }

    @Test
    @DisplayName("Should deactivate suspended tenant")
    void testDeleteSuspendedTenant() {
        // Arrange
        Tenant suspendedTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.ENTERPRISE,
                TenantStatus.SUSPENDED,
                20,
                10,
                5,
                null,
                LocalDateTime.now().plusDays(365),
                "America/Sao_Paulo",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                false
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(suspendedTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(suspendedTenant);

        // Act
        deleteTenantUseCase.execute(tenantId);

        // Assert
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(tenantCaptor.capture());

        Tenant savedTenant = tenantCaptor.getValue();
        assertEquals(TenantStatus.INACTIVE, savedTenant.getStatus());
        assertFalse(savedTenant.isActive());
    }

    @Test
    @DisplayName("Should deactivate blocked tenant")
    void testDeleteBlockedTenant() {
        // Arrange
        Tenant blockedTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.BASIC,
                TenantStatus.BLOCKED,
                10,
                5,
                3,
                null,
                LocalDateTime.now().plusDays(365),
                "America/Sao_Paulo",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                false
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(blockedTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(blockedTenant);

        // Act
        deleteTenantUseCase.execute(tenantId);

        // Assert
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(tenantCaptor.capture());

        Tenant savedTenant = tenantCaptor.getValue();
        assertEquals(TenantStatus.INACTIVE, savedTenant.getStatus());
        assertFalse(savedTenant.isActive());
    }
}

