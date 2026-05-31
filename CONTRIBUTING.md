# Contribuindo com o PrimeTMS

Obrigado por contribuir! Este guia explica como trabalhar neste projeto.

---

## Pré-requisitos

* JDK 21
* PostgreSQL rodando localmente ou via Docker
* Maven Wrapper (já incluso no projeto)

---

## Configuração local

### 1. Clone o repositório

```bash
git clone https://github.com/PrimeTechSistemas/PrimeTMS.git
cd PrimeTMS
```

### 2. Crie o banco de dados

```sql
CREATE DATABASE prime_tms;
```

### 3. Crie o arquivo `.env`

Na raiz do projeto, crie um arquivo chamado `.env`:

```env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

DB_URL=jdbc:postgresql://localhost:5432/prime_tms
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### 4. Suba a aplicação

```bash
./mvnw spring-boot:run
```

### 5. Verifique a aplicação

Acesse:

```text
http://localhost:8080/actuator/health
```

---

## Fluxo de trabalho

⚠️ Nunca faça push diretamente para a branch `main`.

Todo desenvolvimento deve acontecer em uma branch própria e entrar no projeto através de Pull Request.

```text
main (protegida)
│
├── feature/sua-feature
│
├── Pull Request
│
├── CI executa automaticamente
│
├── Review do @ArthenyoCarlos
│
└── Merge para main
```

---

## Padrão de branches

| Tipo                | Padrão              | Exemplo                        |
| ------------------- | ------------------- | ------------------------------ |
| Nova feature        | `feature/descricao` | `feature/cadastro-motorista`   |
| Correção de bug     | `fix/descricao`     | `fix/calculo-frete-incorreto`  |
| Urgente em produção | `hotfix/descricao`  | `hotfix/login-quebrado`        |
| Tarefa técnica      | `chore/descricao`   | `chore/atualizar-dependencias` |
| Documentação        | `docs/descricao`    | `docs/atualizar-readme`        |

---

## Padrão de commits

Seguimos o padrão **Conventional Commits**:

```text
feat: adiciona endpoint de listagem de fretes
fix: corrige npe no serviço de motoristas
refactor: extrai lógica de cálculo para FreteService
docs: atualiza README com instruções de setup
test: adiciona testes para FreteService
chore: atualiza dependências do Maven
```

### Formato

```text
tipo: descrição curta em minúsculo
```

Tipos mais utilizados:

| Tipo     | Descrição                                  |
| -------- | ------------------------------------------ |
| feat     | Nova funcionalidade                        |
| fix      | Correção de bug                            |
| refactor | Refatoração sem alteração de comportamento |
| docs     | Alterações na documentação                 |
| test     | Inclusão ou ajuste de testes               |
| chore    | Tarefas técnicas e manutenção              |

---

## Migrations do Flyway

Novas migrations devem seguir o padrão:

```text
src/main/resources/db/migration/V{numero}__{descricao}.sql
```

Exemplos:

```text
V1__create_table_motorista.sql
V2__add_column_status_frete.sql
V3__create_index_frete_data.sql
```

> ⚠️ Nunca altere uma migration já commitada na branch main. Sempre crie uma nova migration.

---

## Arquitetura do projeto

O PrimeTMS segue uma abordagem de **Monólito Modular** baseada em **Clean Architecture**.

Estrutura padrão dos módulos:

```text
modules/{dominio}
├── application
├── domain
├── infrastructure
└── presentation
```

### Camadas

| Camada         | Responsabilidade                    |
| -------------- | ----------------------------------- |
| application    | Casos de uso, comandos e queries    |
| domain         | Entidades e regras de negócio       |
| infrastructure | Persistência e integrações externas |
| presentation   | Controllers, DTOs e APIs            |

### Regra de dependência

```text
presentation → application → domain
infrastructure → application / domain
domain → sem dependências de frameworks
```

Novos módulos devem seguir exatamente essa estrutura.

---

## Abrindo um Pull Request

### 1. Atualize sua branch local

```bash
git checkout main
git pull
```

### 2. Crie uma nova branch

```bash
git checkout -b feature/sua-feature
```

### 3. Desenvolva e faça commits

Siga os padrões definidos neste documento.

### 4. Abra o Pull Request

Abra o PR para a branch `main` através do GitHub.

### 5. Preencha o template

O template do Pull Request é obrigatório.

### 6. Aguarde validações

O Pull Request será revisado somente após:

* CI aprovado
* Template preenchido corretamente
* Review do **@ArthenyoCarlos**

> PRs com falha no CI ou template incompleto não serão revisados.

---

## Boas práticas

* Mantenha os PRs pequenos e objetivos.
* Escreva código limpo e legível.
* Adicione testes sempre que possível.
* Evite mudanças não relacionadas ao escopo da tarefa.
* Documente alterações relevantes.
* Respeite a arquitetura definida do projeto.

---

## Dúvidas

Caso tenha dúvidas ou encontre algum problema:

1. Verifique a documentação do projeto.
2. Consulte as Issues abertas.
3. Abra uma nova Issue descrevendo claramente o problema.

---

Obrigado por contribuir com o **PrimeTMS** e ajudar a construir uma plataforma cada vez melhor.
