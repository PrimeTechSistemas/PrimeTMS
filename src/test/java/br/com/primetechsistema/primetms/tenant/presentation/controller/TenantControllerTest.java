package br.com.primetechsistema.primetms.tenant.presentation.controller;

import br.com.primetechsistema.primetms.tenant.application.command.CreateTenantCommand;
import br.com.primetechsistema.primetms.tenant.application.port.in.CreateTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.in.DeleteTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.in.GetTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.in.UpdateTenantUseCase;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantPlan;
import br.com.primetechsistema.primetms.tenant.domain.model.TenantStatus;
import br.com.primetechsistema.primetms.tenant.presentation.dto.request.CreateTenantRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("TenantController Tests")
class TenantControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CreateTenantUseCase createTenantUseCase;

    @Mock
    private GetTenantUseCase getTenantUseCase;

    @Mock
    private UpdateTenantUseCase updateTenantUseCase;

    @Mock
    private DeleteTenantUseCase deleteTenantUseCase;

    @InjectMocks
    private TenantController tenantController;

    private UUID tenantId;
    private Tenant testTenant;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tenantController)
                .setControllerAdvice(new br.com.primetechsistema.primetms.shared.presentation.handler.GlobalExceptionHandler())
                .build();
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        tenantId = UUID.randomUUID();
        now = LocalDateTime.now();

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
    @DisplayName("POST /api/tenants should create tenant and return 201")
    void testCreateTenant() throws Exception {
        // Arrange
        CreateTenantRequest request = new CreateTenantRequest(
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.TRIAL,
                10,
                5,
                3,
                now.plusDays(30),
                null,
                "America/Sao_Paulo",
                null
        );

        when(createTenantUseCase.execute(any(CreateTenantCommand.class))).thenReturn(testTenant);

        // Act & Assert
        mockMvc.perform(post("/api/tenants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tenantId", notNullValue()))
                .andExpect(jsonPath("$.name").value("ACME Transportes"))
                .andExpect(jsonPath("$.document").value("12345678000100"))
                .andExpect(jsonPath("$.plan").value("TRIAL"))
                .andExpect(jsonPath("$.status").value("TRIAL"));

        verify(createTenantUseCase).execute(any(CreateTenantCommand.class));
    }

    @Test
    @DisplayName("POST /api/tenants should return 400 for invalid email")
    void testCreateTenantInvalidEmail() throws Exception {
        // Arrange - Invalid email will be caught by @Email validation
        CreateTenantRequest request = new CreateTenantRequest(
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "invalid-email",
                "+5511999999999",
                TenantPlan.TRIAL,
                10,
                5,
                3,
                now.plusDays(30),
                null,
                "America/Sao_Paulo",
                null
        );

        // Act & Assert - Bean Validation catches @Email and returns 400
        mockMvc.perform(post("/api/tenants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/tenants should return 400 for missing required fields")
    void testCreateTenantMissingFields() throws Exception {
        // Arrange - Null name will be caught by @NotBlank validation
        CreateTenantRequest request = new CreateTenantRequest(
                null,
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.TRIAL,
                10,
                5,
                3,
                now.plusDays(30),
                null,
                "America/Sao_Paulo",
                null
        );

        // Act & Assert - Bean Validation catches @NotBlank and returns 400
        mockMvc.perform(post("/api/tenants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/tenants/{id} should return tenant and 200")
    void testGetTenant() throws Exception {
        // Arrange
        when(getTenantUseCase.execute(tenantId)).thenReturn(testTenant);

        // Act & Assert
        mockMvc.perform(get("/api/tenants/{id}", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantId").value(tenantId.toString()))
                .andExpect(jsonPath("$.name").value("ACME Transportes"))
                .andExpect(jsonPath("$.document").value("12345678000100"))
                .andExpect(jsonPath("$.plan").value("TRIAL"))
                .andExpect(jsonPath("$.status").value("TRIAL"));

        verify(getTenantUseCase).execute(tenantId);
    }

    @Test
    @DisplayName("GET /api/tenants/{id} should return 404 for non-existent tenant")
    void testGetTenantNotFound() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(getTenantUseCase.execute(nonExistentId))
                .thenThrow(new br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException("Tenant not found"));

        // Act & Assert
        mockMvc.perform(get("/api/tenants/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        verify(getTenantUseCase).execute(nonExistentId);
    }

    @Test
    @DisplayName("PUT /api/tenants/{id} should update tenant and return 200")
    void testUpdateTenant() throws Exception {
        // Arrange
        var updateRequest = new br.com.primetechsistema.primetms.tenant.presentation.dto.request.UpdateTenantRequest(
                "ACME Transportes Ltda",
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

        when(updateTenantUseCase.execute(any())).thenReturn(testTenant);

        // Act & Assert
        mockMvc.perform(put("/api/tenants/{id}", tenantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantId").value(tenantId.toString()));

        verify(updateTenantUseCase).execute(any());
    }

    @Test
    @DisplayName("DELETE /api/tenants/{id} should delete tenant and return 204")
    void testDeleteTenant() throws Exception {
        // Arrange
        doNothing().when(deleteTenantUseCase).execute(tenantId);

        // Act & Assert
        mockMvc.perform(delete("/api/tenants/{id}", tenantId))
                .andExpect(status().isNoContent());

        verify(deleteTenantUseCase).execute(tenantId);
    }

    @Test
    @DisplayName("DELETE /api/tenants/{id} should return 404 for non-existent tenant")
    void testDeleteTenantNotFound() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException("Tenant not found"))
                .when(deleteTenantUseCase).execute(nonExistentId);

        // Act & Assert
        mockMvc.perform(delete("/api/tenants/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        verify(deleteTenantUseCase).execute(nonExistentId);
    }

    @Test
    @DisplayName("GET /api/tenants/{id} should return tenant data with audit fields")
    void testGetTenantWithAuditFields() throws Exception {
        // Arrange
        UUID createdBy = UUID.randomUUID();
        Tenant auditedTenant = new Tenant(
                tenantId,
                "ACME Transportes",
                "12345678000100",
                "João Silva",
                "joao@acme.com",
                "+5511999999999",
                TenantPlan.BASIC,
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
                createdBy,
                null,
                true
        );

        when(getTenantUseCase.execute(tenantId)).thenReturn(auditedTenant);

        // Act & Assert
        mockMvc.perform(get("/api/tenants/{id}", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()))
                .andExpect(jsonPath("$.active").value(true));

        verify(getTenantUseCase).execute(tenantId);
    }
}






