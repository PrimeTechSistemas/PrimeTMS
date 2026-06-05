package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.command.UpdateTenantCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTenantUseCaseImpl Tests")
class UpdateTenantUseCaseImplTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @InjectMocks
    private UpdateTenantUseCaseImpl updateTenantUseCase;

    private UUID tenantId;
    private Tenant existingTenant;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        existingTenant = new Tenant(
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
    @DisplayName("Should update tenant name successfully")
    void testUpdateTenantName() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                "Nova ACME",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should update tenant responsible contact")
    void testUpdateTenantResponsible() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                null,
                null,
                "Maria Silva",
                "maria@acme.com",
                "+5511988888888",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should update tenant limits")
    void testUpdateTenantLimits() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                null,
                null,
                null,
                null,
                null,
                null,
                20,
                10,
                5,
                null,
                null,
                null,
                null
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should update tenant timezone")
    void testUpdateTenantTimezone() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "America/New_York",
                null
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should update tenant logo URL")
    void testUpdateTenantLogoUrl() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "https://example.com/logo.png"
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should throw exception when tenant not found")
    void testUpdateTenantNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        UpdateTenantCommand command = new UpdateTenantCommand(
                nonExistentId,
                "Nova ACME",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(tenantRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class, () ->
                updateTenantUseCase.execute(command)
        );

        assertEquals("Tenant not found", exception.getMessage());
        verify(tenantRepositoryPort).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should update multiple fields in a single call")
    void testUpdateMultipleFields() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                "ACME Transportes Ltda",
                null,
                "Carlos Santos",
                "carlos@acme.com",
                "+5511977777777",
                TenantPlan.PROFESSIONAL,
                15,
                8,
                4,
                null,
                LocalDateTime.now().plusDays(365),
                "America/Buenos_Aires",
                "https://acme.com/logo.png"
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should handle all null fields (no-op update)")
    void testUpdateWithAllNullFields() {
        // Arrange
        UpdateTenantCommand command = new UpdateTenantCommand(
                tenantId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(tenantRepositoryPort.findById(tenantId)).thenReturn(Optional.of(existingTenant));
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(existingTenant);

        // Act
        Tenant result = updateTenantUseCase.execute(command);

        // Assert
        assertNotNull(result);
        verify(tenantRepositoryPort).findById(tenantId);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }
}

