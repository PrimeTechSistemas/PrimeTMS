# PrimeTMS

API do PrimeTMS construída com Spring Boot, Java 21, PostgreSQL e Flyway.

## Tecnologias

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Flyway
- Spring Boot Actuator
- Lombok
- Maven Wrapper

## Requisitos

- JDK 21 instalado
- PostgreSQL disponível localmente ou via container
- Maven Wrapper do projeto (`mvnw` / `mvnw.cmd`)

## Configuração Local

O projeto usa profiles do Spring:

- `dev`: configuração local, ativada por padrão.
- `prod`: configuração para produção, ativada com `SPRING_PROFILES_ACTIVE=prod`.

As variáveis locais estão no arquivo `.env`, que não deve ser versionado.

Exemplo de configuração local:

```env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

DB_URL=jdbc:postgresql://localhost:5432/prime_tms
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

Antes de iniciar a aplicação, crie o banco:

```sql
CREATE DATABASE prime_tms;
```

## Executando

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Em Linux/macOS:

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão em:

```text
http://localhost:8080
```

## Testes e Validação

Validar o projeto:

```powershell
.\mvnw.cmd validate
```

Executar testes:

```powershell
.\mvnw.cmd test
```

Os testes que sobem o contexto Spring podem exigir um PostgreSQL disponível conforme as variáveis do profile ativo.

## Banco de Dados

As migrations do Flyway devem ficar em:

```text
src/main/resources/db/migration
```

Padrão recomendado de nome:

```text
V1__create_initial_tables.sql
V2__add_new_columns.sql
```

## Organização do Código

O projeto deve seguir uma organização por domínio/feature. Cada módulo agrupa seus próprios controllers, DTOs, entidades, repositories, services e mappers.

Estrutura recomendada:

```text
src/main/java/br/com/primetechsistema/primetms
├── common
│   ├── config
│   ├── exception
│   ├── response
│   ├── validation
│   └── security
│
└── dominio
    ├── controller
    ├── dto
    ├── entity
    ├── repository
    ├── service
    └── mapper
```

Exemplo para um domínio `cliente`:

```text
cliente
├── controller
├── dto
├── entity
├── repository
├── service
└── mapper
```

Esse padrão evita que o projeto cresça com pastas genéricas muito grandes, como `controller`, `service` e `repository` concentrando arquivos de todos os domínios.

## Produção

Em produção, use o profile `prod` e configure as variáveis de ambiente no servidor, pipeline ou orquestrador:

```env
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
DB_URL=jdbc:postgresql://host:5432/prime_tms
DB_USERNAME=usuario
DB_PASSWORD=senha
DB_POOL_MAX_SIZE=20
DB_POOL_MIN_IDLE=5
```

O profile `prod` não deve depender de valores sensíveis no repositório.

## Health Check

Com Actuator habilitado, os endpoints expostos são:

```text
GET /actuator/health
GET /actuator/info
```

## Estrutura Principal

```text
src/main/java/br/com/primetechsistema/primetms
src/main/resources/application.yaml
src/main/resources/application-dev.yaml
src/main/resources/application-prod.yaml
src/main/resources/db/migration
```
