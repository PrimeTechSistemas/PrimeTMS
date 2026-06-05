
ALTER TABLE tb_tenants ALTER COLUMN plan TYPE VARCHAR(30) USING plan::text;
ALTER TABLE tb_tenants ALTER COLUMN status TYPE VARCHAR(30) USING status::text;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tenant_plan') THEN
        BEGIN
            EXECUTE 'DROP TYPE tenant_plan';
        EXCEPTION WHEN others THEN

            RAISE NOTICE 'Could not drop type tenant_plan: %', SQLERRM;
        END;
    END IF;

    IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tenant_status') THEN
        BEGIN
            EXECUTE 'DROP TYPE tenant_status';
        EXCEPTION WHEN others THEN
            RAISE NOTICE 'Could not drop type tenant_status: %', SQLERRM;
        END;
    END IF;
END$$;

