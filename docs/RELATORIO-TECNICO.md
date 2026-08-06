# Relatório Técnico — Restaurant API

Documento em texto (versão preliminar do relatório oficial em PDF).  
Projeto: API REST de clientes e donos de restaurante.  
Repositório: https://github.com/BernardoSemiOficial/api-restaurante

---

## 1. Descrição da arquitetura

A aplicação segue o modelo clássico em camadas do Spring Boot:

```
Cliente HTTP (Postman / Swagger / frontend)
        │
        ▼
   Controllers  (/api/v1/...)
        │
        ▼
    Services    (regras de negócio, validação, BCrypt)
        │
        ▼
  Repositories  (Spring Data JPA)
        │
        ▼
   PostgreSQL   (schema em schema-restaurant.sql)
```

### Componentes principais

| Camada | Pacote | Responsabilidade |
|---|---|---|
| API | `controller` | Expõe endpoints REST versionados |
| Negócio | `service` | CRUD, login, unicidade de e-mail, hash de senha |
| Persistência | `repository` | Acesso a `Customer`, `Restaurant`, `User` |
| Modelo | `model` | Entidades JPA |
| Contratos | `interfaces` | DTOs de request/response |
| Config | `config` | Security (BCrypt) e OpenAPI |
| Erros | `exception` | `GlobalExceptionHandler` com ProblemDetail |

### Decisões de arquitetura

- **Versionamento por path:** `/api/v1/...`
- **Dois tipos de usuário:** `Customer` e `Restaurant`, ambos com um `User` (dados comuns: nome, e-mail, login, senha, `updatedAt`, endereço)
- **Senha:** armazenada com BCrypt; troca apenas em endpoint `PATCH .../change-password`
- **Atualização de perfil:** endpoint `PUT` distinto, sem campo de senha no DTO de edição
- **Erros:** padronizados com ProblemDetail (RFC 7807) — 400, 401, 404, 409
- **E-mail único:** constraint no banco + validação prévia em `UserRepository.findByEmail` (abrangendo cliente e dono)
- **Docker:** `Dockerfile` multi-stage + `compose.yaml` com Postgres e serviço `app` (profile `full`)

---

## 2. Modelagem das entidades e relacionamentos

### Diagrama lógico

```
Address 1 ─── 1 User
                │
        ┌───────┴────────┐
        │                │
   Customer 1        Restaurant 1
        │                │
        └────── N:N ─────┘
         (customer_restaurant)
```

### Entidades

**Address**  
CEP, rua, número, cidade, estado.

**User** (`user_account`)  
Nome, e-mail (UNIQUE), login, senha (hash), `updatedAt`, referência a Address.

**Customer**  
Referência 1:1 a User + CPF.

**Restaurant**  
Referência 1:1 a User + CNPJ + tipo de cozinha (`cuisineType`).

**Relacionamento N:N**  
Tabela pivô `customer_restaurant` liga clientes a restaurantes.

---

## 3. Descrição dos endpoints (com exemplos)

Base URL local: `http://localhost:8080`

### 3.1 Auth

#### `POST /api/v1/auth/login`

Valida login e senha para cliente ou dono.

Request:

```json
{
  "login": "joao.silva",
  "password": "Senha@123"
}
```

Sucesso (200):

```text
Login realizado com sucesso
```

Erro (401) — ProblemDetail:

```json
{
  "type": "https://api.restaurant.com/errors/unauthorized",
  "title": "Não autorizado",
  "status": 401,
  "detail": "Não foi possível realizar o login"
}
```

### 3.2 Customers

| Método | Path | Descrição |
|---|---|---|
| GET | `/api/v1/customers?customerName=João` | Busca por nome |
| GET | `/api/v1/customers/{id}` | Busca por ID |
| POST | `/api/v1/customers` | Cadastro |
| PUT | `/api/v1/customers/{id}` | Atualização de dados (sem senha) |
| PATCH | `/api/v1/customers/{id}/change-password` | Troca de senha |
| DELETE | `/api/v1/customers/{id}` | Exclusão |

#### Exemplo — criar cliente (`POST /api/v1/customers`)

```json
{
  "cpf": "12345678901",
  "user": {
    "name": "João Silva",
    "email": "joao@email.com",
    "login": "joao.silva",
    "password": "Senha@123",
    "address": {
      "zipCode": "01310-100",
      "street": "Av. Paulista",
      "number": "1000",
      "city": "São Paulo",
      "state": "SP"
    }
  }
}
```

Sucesso: **201 Created** com body do cliente.

Erro e-mail duplicado: **409 Conflict** (ProblemDetail).

#### Exemplo — troca de senha

```json
{
  "currentPassword": "Senha@123",
  "newPassword": "NovaSenha@456"
}
```

Sucesso: **200**.  
Senha atual incorreta: **400** (ProblemDetail).

### 3.3 Restaurants

| Método | Path | Descrição |
|---|---|---|
| GET | `/api/v1/restaurants?restaurantName=` | Lista/busca por nome |
| POST | `/api/v1/restaurants` | Cadastro do dono/restaurante |
| PUT | `/api/v1/restaurants/{id}` | Atualização (sem senha) |
| PATCH | `/api/v1/restaurants/{id}/change-password` | Troca de senha |
| DELETE | `/api/v1/restaurants/{id}` | Exclusão |
| GET | `/api/v1/restaurants/{id}/customers` | Clientes do restaurante |
| POST | `/api/v1/restaurants/{id}/customers` | Cria cliente vinculado |

#### Exemplo — criar restaurante

```json
{
  "cnpj": "12345678000199",
  "cuisineType": "Italiana",
  "user": {
    "name": "Maria Souza",
    "email": "maria@restaurante.com",
    "login": "maria.souza",
    "password": "Senha@123",
    "address": {
      "zipCode": "01310-100",
      "street": "Av. Paulista",
      "number": "500",
      "city": "São Paulo",
      "state": "SP"
    }
  }
}
```

Sucesso: **201 Created**.

---

## 4. Documentação Swagger / OpenAPI

A API usa **SpringDoc OpenAPI** (`springdoc-openapi-starter-webmvc-ui`).

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

### O que está documentado

- Tags: **Auth**, **Customer**, **Restaurant**
- Operações com `@Operation` e `@ApiResponses`
- Exemplos de sucesso e erro (incluindo ProblemDetail para 400/401/409)
- Schemas dos DTOs com `@Schema` e exemplos de campos
- Metadados em `OpenApiConfig` (título, descrição, versão `v1`)

> No PDF final, incluir prints da UI Swagger (lista de endpoints e um exemplo de erro ProblemDetail).

---

## 5. Coleção Postman

**Status:** ainda não incluída no repositório.

Cenários previstos (conforme enunciado):

1. Cadastro de usuário válido  
2. Cadastro inválido (e-mail duplicado / campos faltando)  
3. Alteração de senha (sucesso e erro)  
4. Atualização de dados (sucesso e erro)  
5. Busca por nome  
6. Validação de login  

> Quando a coleção JSON for adicionada, referenciar o arquivo aqui e incluir prints no PDF.

Enquanto isso, os mesmos cenários podem ser exercitados via **Swagger UI** ou curl.

---

## 6. Estrutura do banco de dados

Script: `src/main/resources/schema-restaurant.sql`  
Modo: `spring.sql.init.mode=always` / `ddl-auto=none`

### Tabelas

| Tabela | Colunas principais |
|---|---|
| `address` | id, zip_code, street, number, city, state |
| `user_account` | id, name, email (UNIQUE), login, password, updated_at, address_id |
| `customer` | id, user_id (UNIQUE), cpf |
| `restaurant` | id, user_id (UNIQUE), cnpj, cuisine_type |
| `customer_restaurant` | customer_id, restaurant_id (PK composta) |

### Relacionamentos (FKs)

- `user_account.address_id` → `address.id`
- `customer.user_id` → `user_account.id`
- `restaurant.user_id` → `user_account.id`
- `customer_restaurant.customer_id` → `customer.id`
- `customer_restaurant.restaurant_id` → `restaurant.id`

---

## 7. Execução com Docker Compose

Arquivo: `compose.yaml` (equivalente ao `docker-compose.yml` do enunciado).

### Serviços

| Serviço | Função |
|---|---|
| `postgres` | Banco PostgreSQL (sempre disponível) |
| `app` | API Spring Boot (profile Compose `full`) |

### Passo a passo

1. Clonar o repositório:

```bash
git clone git@github.com:BernardoSemiOficial/api-restaurante.git
cd api-restaurante
```

2. Subir aplicação + banco:

```bash
docker compose --profile full up --build
```

3. Aguardar o healthcheck do Postgres e o start da API.

4. Validar:

- Health/API: http://localhost:8080/swagger-ui/index.html  
- Login de teste após cadastrar um usuário via `POST /api/v1/customers` ou `/restaurants`

5. Parar:

```bash
docker compose --profile full down
```

### Variáveis de ambiente da aplicação (Compose)

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/restaurant
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_DOCKER_COMPOSE_ENABLED=false
```

### Variáveis do Postgres

```text
POSTGRES_DB=restaurant
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

Porta publicada no host: **5436** → 5432 no container.

### Dockerfile

Build multi-stage:

1. `eclipse-temurin:25-jdk` — compila com Maven Wrapper  
2. `eclipse-temurin:25-jre` — executa o JAR `restaurant-0.0.1-SNAPSHOT.jar`

---

## 8. Tratamento de erros (ProblemDetail)

Handler: `GlobalExceptionHandler`

| Status | Exceção típica | Situação |
|---|---|---|
| 404 | `EntityNotFoundException` | Recurso não encontrado |
| 400 | `IllegalArgumentException` | Ex.: senha atual incorreta |
| 401 | `UnauthorizedException` | Login/senha inválidos |
| 409 | `DataIntegrityViolationException` | E-mail/login duplicado |

Content-Type de erro: `application/problem+json`.

---

## 9. Próximos passos para o PDF oficial

Este arquivo Markdown é a base textual do relatório. Para o entregável em PDF:

1. Exportar/converter este conteúdo para PDF  
2. Incluir prints do Swagger UI  
3. Incluir prints da coleção Postman (após criar o JSON)  
4. Opcional: diagrama de entidades (DER) em imagem  

Entregável oficial: **PDF submetido separadamente**; código e este texto ficam no GitHub.
