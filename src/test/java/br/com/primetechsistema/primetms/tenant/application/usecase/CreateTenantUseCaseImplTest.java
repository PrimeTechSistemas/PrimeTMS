package br.com.primetechsistema.primetms.tenant.application.usecase;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import br.com.primetechsistema.primetms.tenant.application.command.CreateTenantCommand;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTenantUseCaseImpl Tests")
class CreateTenantUseCaseImplTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @InjectMocks
    private CreateTenantUseCaseImpl createTenantUseCase;

    private CreateTenantCommand validCommand;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDateTime.now().plusDays(30);
        validCommand = new CreateTenantCommand(
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.TRIAL,
                10,
                5,
                3,
                futureDate,
                null,
                "America/Sao_Paulo",
                null
        );
    }

    @Test
    @DisplayName("Should create tenant successfully when valid command is provided")
    void testCreateTenantSuccess() {
        // Arrange
        Tenant tenantToReturn = new Tenant(
                validCommand.name(),
                validCommand.document(),
                validCommand.responsibleName(),
                validCommand.responsibleEmail(),
                validCommand.responsiblePhone(),
                validCommand.plan(),
                validCommand.maxDrivers(),
                validCommand.maxVehicles(),
                validCommand.maxUsers(),
                validCommand.trialUntil(),
                validCommand.subscriptionEndAt(),
                validCommand.timezone(),
                validCommand.logoUrl()
        );

        when(tenantRepositoryPort.existsByDocument(validCommand.document())).thenReturn(false);
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(tenantToReturn);

        // Act
        Tenant result = createTenantUseCase.execute(validCommand);

        // Assert
        assertNotNull(result);
        assertEquals(validCommand.name(), result.getName());
        assertEquals(validCommand.document(), result.getDocument());
        assertEquals(validCommand.responsibleName(), result.getResponsibleName());
        assertEquals(validCommand.responsibleEmail(), result.getResponsibleEmail());
        assertEquals(TenantStatus.TRIAL, result.getStatus());
        assertTrue(result.isActive());

        verify(tenantRepositoryPort).existsByDocument(validCommand.document());
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should throw exception when document already exists")
    void testCreateTenantDocumentAlreadyExists() {
        // Arrange
        when(tenantRepositoryPort.existsByDocument(validCommand.document())).thenReturn(true);

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class, () ->
                createTenantUseCase.execute(validCommand)
        );

        assertEquals("Tenant document already exists", exception.getMessage());
        verify(tenantRepositoryPort).existsByDocument(validCommand.document());
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void testCreateTenantBlankName() {
        // Act & Assert - Exception thrown during command construction
        assertThrows(DomainValidationException.class, () ->
                new CreateTenantCommand(
                        "   ",
                        "12345678000100",
                        "João Silva",
                        "joao@acme.com",
                        "+5511999999999",
                        TenantPlan.TRIAL,
                        10,
                        5,
                        3,
                        futureDate,
                        null,
                        "America/Sao_Paulo",
                        null
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when maxDrivers is zero")
    void testCreateTenantZeroMaxDrivers() {
        // Act & Assert - Exception thrown during command construction
        assertThrows(DomainValidationException.class, () ->
                new CreateTenantCommand(
                        "ACME Transportes",
                        "12345678000100",
                        "João Silva",
                        "joao@acme.com",
                        "+5511999999999",
                        TenantPlan.TRIAL,
                        0,
                        5,
                        3,
                        futureDate,
                        null,
                        "America/Sao_Paulo",
                        null
                )
        );
    }

    @Test
    @DisplayName("Should create tenant with ACTIVE status when plan is BASIC")
    void testCreateTenantActiveStatus() {
        // Arrange
        LocalDateTime subscriptionEnd = LocalDateTime.now().plusDays(365);
        CreateTenantCommand basicCommand = new CreateTenantCommand(
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.BASIC,
                10,
                5,
                3,
                null,
                subscriptionEnd,
                "America/Sao_Paulo",
                null
        );

        Tenant tenantToReturn = new Tenant(
                basicCommand.name(),
                basicCommand.document(),
                basicCommand.responsibleName(),
                basicCommand.responsibleEmail(),
                basicCommand.responsiblePhone(),
                basicCommand.plan(),
                basicCommand.maxDrivers(),
                basicCommand.maxVehicles(),
                basicCommand.maxUsers(),
                basicCommand.trialUntil(),
                basicCommand.subscriptionEndAt(),
                basicCommand.timezone(),
                basicCommand.logoUrl()
        );

        when(tenantRepositoryPort.existsByDocument(basicCommand.document())).thenReturn(false);
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(tenantToReturn);

        // Act
        Tenant result = createTenantUseCase.execute(basicCommand);

        // Assert
        assertEquals(TenantStatus.ACTIVE, result.getStatus());
    }
}


