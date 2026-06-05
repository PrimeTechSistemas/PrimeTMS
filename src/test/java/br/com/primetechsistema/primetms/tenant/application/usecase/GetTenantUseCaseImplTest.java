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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetTenantUseCaseImpl Tests")
class GetTenantUseCaseImplTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @InjectMocks
    private GetTenantUseCaseImpl getTenantUseCase;

    private UUID tenantId;
    private Tenant testTenant;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        testTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.TRIAL,
                TenantStatus.TRIAL,
                10,
                5,
                3,
                now.plusDays(30),
                null,
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
    @DisplayName("Should return tenant when id is found")
    void testGetTenantSuccess() {
        // Arrange
        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(testTenant));

        // Act
        Tenant result = getTenantUseCase.execute(tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("ACME Transportes", result.getName());
        assertEquals("12345678000100", result.getDocument());
        assertEquals("João Silva", result.getResponsibleName());
        assertEquals("joao@acme.com", result.getResponsibleEmail());
        assertEquals(TenantPlan.TRIAL, result.getPlan());
        assertEquals(TenantStatus.TRIAL, result.getStatus());

        verify(tenantRepositoryPort).findById(tenantId);
    }

    @Test
    @DisplayName("Should throw exception when tenant is not found")
    void testGetTenantNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(tenantRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class, () ->
                getTenantUseCase.execute(nonExistentId)
        );

        assertEquals("Tenant not found", exception.getMessage());
        verify(tenantRepositoryPort).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should return inactive tenant")
    void testGetInactiveTenant() {
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

        // Act
        Tenant result = getTenantUseCase.execute(tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(TenantStatus.INACTIVE, result.getStatus());
        assertFalse(result.isActive());
    }

    @Test
    @DisplayName("Should return suspended tenant")
    void testGetSuspendedTenant() {
        // Arrange
        Tenant suspendedTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.PROFESSIONAL,
                TenantStatus.SUSPENDED,
                20,
                10,
                5,
                null,
                LocalDateTime.now().plusDays(180),
                "America/Sao_Paulo",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                false
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(suspendedTenant));

        // Act
        Tenant result = getTenantUseCase.execute(tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(TenantStatus.SUSPENDED, result.getStatus());
        assertFalse(result.isActive());
    }
}

