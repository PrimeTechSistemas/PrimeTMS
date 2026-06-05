package br.com.primetechsistema.primetms.tenant.presentation.controller;

import br.com.primetechsistema.primetms.tenant.application.command.CreateTenantCommand;
import br.com.primetechsistema.primetms.tenant.application.command.UpdateTenantCommand;
import br.com.primetechsistema.primetms.tenant.application.port.in.CreateTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.in.DeleteTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.in.GetTenantUseCase;
import br.com.primetechsistema.primetms.tenant.application.port.in.UpdateTenantUseCase;
import br.com.primetechsistema.primetms.tenant.domain.model.Tenant;
import br.com.primetechsistema.primetms.tenant.presentation.dto.request.CreateTenantRequest;
import br.com.primetechsistema.primetms.tenant.presentation.dto.response.CreateTenantResponse;
import br.com.primetechsistema.primetms.tenant.presentation.presenter.TenantPresenter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final CreateTenantUseCase createTenantUseCase;
    private final GetTenantUseCase getTenantUseCase;
    private final UpdateTenantUseCase updateTenantUseCase;
    private final DeleteTenantUseCase deleteTenantUseCase;

    public TenantController(CreateTenantUseCase createTenantUseCase,
                            GetTenantUseCase getTenantUseCase,
                            UpdateTenantUseCase updateTenantUseCase,
                            DeleteTenantUseCase deleteTenantUseCase) {
        this.createTenantUseCase = createTenantUseCase;
        this.getTenantUseCase = getTenantUseCase;
        this.updateTenantUseCase = updateTenantUseCase;
        this.deleteTenantUseCase = deleteTenantUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateTenantResponse> create(@Valid @RequestBody CreateTenantRequest request) {
        CreateTenantCommand command = new CreateTenantCommand(
                request.name(),
                request.document(),
                request.responsibleName(),
                request.responsibleEmail(),
                request.responsiblePhone(),
                request.plan(),
                request.maxDrivers(),
                request.maxVehicles(),
                request.maxUsers(),
                request.trialUntil(),
                request.subscriptionEndAt(),
                request.timezone(),
                request.logoUrl()
        );

        Tenant tenant = createTenantUseCase.execute(command);
        CreateTenantResponse response = TenantPresenter.toResponse(tenant);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateTenantResponse> getById(@PathVariable("id") java.util.UUID id) {
        var tenant = getTenantUseCase.execute(id);
        var response = TenantPresenter.toResponse(tenant);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateTenantResponse> update(@PathVariable("id") java.util.UUID id,
                                                       @RequestBody br.com.primetechsistema.primetms.tenant.presentation.dto.request.UpdateTenantRequest request) {

        UpdateTenantCommand command = new UpdateTenantCommand(
                id,
                request.name(),
                request.document(),
                request.responsibleName(),
                request.responsibleEmail(),
                request.responsiblePhone(),
                request.plan(),
                request.maxDrivers(),
                request.maxVehicles(),
                request.maxUsers(),
                request.trialUntil(),
                request.subscriptionEndAt(),
                request.timezone(),
                request.logoUrl()
        );

        var updated = updateTenantUseCase.execute(command);
        var response = TenantPresenter.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") java.util.UUID id) {
        deleteTenantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

