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

## Arquitetura

O projeto deve seguir uma abordagem de monolito modular com Clean Architecture. Cada módulo representa uma capacidade de negócio e deve ter fronteiras claras, evitando dependências diretas entre detalhes internos de módulos diferentes.

Estrutura recomendada:

```text
src/main/java/br/com/primetechsistema/primetms
├── shared
│   ├── application
│   ├── domain
│   ├── infrastructure
│   └── presentation
│
└── modules
    └── dominio
        ├── application
        │   ├── command
        │   ├── query
        │   ├── usecase
        │   └── port
        │
        ├── domain
        │   ├── model
        │   ├── event
        │   ├── exception
        │   └── service
        │
        ├── infrastructure
        │   ├── persistence
        │   ├── mapper
        │   └── config
        │
        └── presentation
            ├── controller
            └── dto
```

Exemplo para um módulo `cliente`:

```text
modules/cliente
├── application
│   ├── command
│   ├── query
│   ├── usecase
│   └── port
├── domain
│   ├── model
│   ├── event
│   ├── exception
│   └── service
├── infrastructure
│   ├── persistence
│   ├── mapper
│   └── config
└── presentation
    ├── controller
    └── dto
```

Responsabilidades principais:

- `domain`: regras de negócio, entidades, value objects, eventos e contratos centrais do domínio.
- `application`: casos de uso, comandos, consultas e portas necessárias para executar regras de negócio.
- `infrastructure`: implementações técnicas, persistência, integrações externas, configurações e mappers.
- `presentation`: controllers REST, DTOs de entrada/saída e adaptação da API HTTP.
- `shared`: código compartilhado entre módulos, mantendo apenas elementos realmente transversais.

Regra de dependência:

```text
presentation -> application -> domain
infrastructure -> application/domain
domain -> sem dependência de frameworks ou camadas externas
```

Novos módulos devem ser criados dentro de `modules` e seguir o mesmo padrão.

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
