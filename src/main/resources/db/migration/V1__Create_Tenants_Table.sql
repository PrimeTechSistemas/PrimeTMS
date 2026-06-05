CREATE TABLE tb_tenants (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(150) NOT NULL,
    document VARCHAR(30) NOT NULL UNIQUE,
    responsible_name VARCHAR(150) NOT NULL,
    responsible_email VARCHAR(150) NOT NULL,
    responsible_phone VARCHAR(30) NOT NULL,
    plan VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    max_drivers INTEGER NOT NULL CHECK (max_drivers > 0),
    max_vehicles INTEGER NOT NULL CHECK (max_vehicles > 0),
    max_users INTEGER NOT NULL CHECK (max_users > 0),
    trial_until TIMESTAMP,
    subscription_end_at TIMESTAMP,
    timezone VARCHAR(80) NOT NULL,
    logo_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID,
    updated_by UUID,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT check_trial_dates CHECK (
        (plan != 'TRIAL' AND status != 'TRIAL') OR trial_until IS NOT NULL
    ),
    CONSTRAINT check_subscription_dates CHECK (
        (plan = 'TRIAL') OR subscription_end_at IS NOT NULL
    )
);

CREATE INDEX idx_tenants_document ON tb_tenants(document);
CREATE INDEX idx_tenants_active ON tb_tenants(active);
CREATE INDEX idx_tenants_created_at ON tb_tenants(created_at);
CREATE INDEX idx_tenants_status ON tb_tenants(status);

COMMENT ON TABLE tb_tenants IS 'Tabela de clientes (tenants) do sistema multi-tenant';
COMMENT ON COLUMN tb_tenants.id IS 'Identificador único do tenant';
COMMENT ON COLUMN tb_tenants.document IS 'Documento do tenant (CNPJ/CPF) - único';
COMMENT ON COLUMN tb_tenants.plan IS 'Plano de subscrição: TRIAL, BASIC, PROFESSIONAL, ENTERPRISE';
COMMENT ON COLUMN tb_tenants.status IS 'Status do tenant: ACTIVE, TRIAL, SUSPENDED, BLOCKED, INACTIVE';
COMMENT ON COLUMN tb_tenants.active IS 'Flag de ativação lógica';

