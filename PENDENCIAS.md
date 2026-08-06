# Pendências — API Restaurant

Documento de gap analysis com base nos requisitos do projeto e nos **Entregáveis e Critérios de Avaliação**.  
Objetivo: checklist do que já foi feito e do que ainda falta para a entrega.

---

## Já atendido (visão técnica)

- [x] Modelo com dois tipos de usuário: **Cliente** (`Customer`) e **Dono de restaurante** (`Restaurant`), ambos vinculados a `User`
- [x] Campos obrigatórios do usuário: nome, e-mail, login, senha, data da última alteração, endereço
- [x] E-mail único (constraint no banco + validação prévia via `UserRepository.findByEmail` em create/edit)
- [x] Validação prévia cruzada (mesmo e-mail não passa entre cliente e restaurante); no edit ignora se o e-mail não mudou
- [x] `updatedAt` atualizado em create / edit / troca de senha do **cliente** e do **dono**
- [x] CRUD completo de cliente e de restaurante/dono
- [x] Endpoint separado de troca de senha (cliente e dono)
- [x] Endpoint distinto de atualização de dados (sem senha) para cliente e dono
- [x] Hash de senha com BCrypt (`SecurityConfig` + `PasswordEncoder`)
- [x] Login para **cliente** e **dono**: `POST /api/v1/auth/login` (`UnauthorizedException` → **401**)
- [x] Versionamento de API via path: `/api/v1/...`
- [x] `ProblemDetail` (RFC 7807) via `GlobalExceptionHandler` (404 / 400 / 401 / 409)
- [x] PostgreSQL + `Dockerfile` + serviço `app` no Compose (profile `full`)
  - Uso: `docker compose --profile full up --build`
- [x] Documentação Swagger/OpenAPI (SpringDoc): UI em `/swagger-ui/index.html`, controllers e DTOs com exemplos de sucesso/erro
- [x] README do repositório
- [x] Relatório técnico em texto (`docs/RELATORIO-TECNICO.md`) — base para o PDF

---

## Entregáveis e Critérios de Avaliação

### 1. Funcionalidade

| Critério | Status |
|---|---|
| Backend atende aos requisitos especificados | [x] Em grande parte — faltam Postman e PDF oficial |
| Endpoints funcionam com tratamento de erros adequado | [x] `GlobalExceptionHandler` + `ProblemDetail` |
| Estratégia de versionamento de API | [x] Path `/api/v1/...` |
| Padrão ProblemDetail (RFC 7807) | [x] |
| Dois tipos de usuário (dono e cliente) | [x] |
| Busca de usuários por nome | [x] Cliente e restaurante |
| Unicidade de e-mail no cadastro | [x] Constraint + `UserRepository.findByEmail` |
| Serviço de validação de login (login + senha) | [x] `POST /api/v1/auth/login` |
| Endpoint separado para troca de senha | [x] `PATCH .../change-password` (ambos) |
| Endpoint distinto para atualização das demais informações | [x] `PUT` sem senha (ambos) |

### 2. Qualidade do Código

| Critério | Status |
|---|---|
| Boas práticas Spring Boot, SOLID e OO | [~] Parcial — estrutura em camadas ok; ainda há limpeza (imports mortos, etc.) |
| Código organizado, testável e bem estruturado | [~] Parcial — falta reforçar testes automatizados e revisão de código morto |

#### Pendente (qualidade)

- [ ] (Opcional) validação prévia de **login** duplicado via `User` (hoje depende da constraint + 409)
- [ ] (Opcional) regra de exclusão com checagem de vínculos N:N
- [ ] (Opcional) ampliar cobertura de testes automatizados

### 3. Documentação com Swagger

| Critério | Status |
|---|---|
| Endpoints documentados com Swagger/OpenAPI | [x] SpringDoc + anotações nos controllers |
| Exemplos de requisições e respostas de sucesso e erro | [x] `@Schema` nos DTOs + exemplos de ProblemDetail |

#### Já atendido

- [x] Dependência `springdoc-openapi-starter-webmvc-ui` (3.1.0)
- [x] `OpenApiConfig` com título, descrição e versão `v1`
- [x] Controllers `Auth`, `Customer` e `Restaurant` com `@Tag`, `@Operation` e `@ApiResponses`
- [x] Exemplos de sucesso e erro (400 / 401 / 404 / 409 com ProblemDetail)
- [x] Schemas/exemplos nos DTOs de request e response
- [x] UI acessível (Security com `permitAll`): `http://localhost:8080/swagger-ui/index.html`

### 4. Banco de Dados

| Critério | Status |
|---|---|
| Banco relacional obrigatório | [x] PostgreSQL |
| Banco recomendado (MySQL ou PostgreSQL) | [x] PostgreSQL |
| Banco em container Docker no Compose | [x] Serviço `postgres` em `compose.yaml` |

### 5. Collections para Testes (Postman)

| Critério | Status |
|---|---|
| Coleção Postman em JSON no repositório | [ ] **Pendente** |

#### Cenários obrigatórios na coleção

- [ ] Cadastro de usuário válido (cliente e/ou dono)
- [ ] Tentativa de cadastro inválido (e-mail duplicado, campos obrigatórios faltando)
- [ ] Alteração de senha com sucesso e erro (endpoint exclusivo)
- [ ] Atualização de dados do usuário com sucesso e erro (endpoint distinto)
- [ ] Busca de usuários pelo nome
- [ ] Validação de login (obrigatória)

### 6. Relatório Técnico (ÚNICO ENTREGÁVEL OFICIAL)

> O único arquivo a ser entregue oficialmente será o **relatório em PDF** (submetido separadamente).  
> O código fica no repositório; o PDF é o entregável formal.

| Conteúdo obrigatório do PDF | Status |
|---|---|
| Descrição detalhada da arquitetura da aplicação | [x] Em texto: `docs/RELATORIO-TECNICO.md` (exportar para PDF) |
| Modelagem das entidades e relacionamentos | [x] Em texto no relatório |
| Descrição dos endpoints (com exemplos de uso) | [x] Em texto no relatório |
| Descrição da documentação Swagger (prints ou trechos) | [~] Trechos ok; faltam prints no PDF |
| Descrição da coleção Postman (prints e exemplos) | [ ] **Pendente** (depende do item 5) |
| Estrutura do banco de dados (tabelas) | [x] Em texto no relatório |
| Passo a passo para executar com Docker Compose (env + exemplos) | [x] Em texto no relatório + README |

### 7. Execução com Docker

| Critério | Status |
|---|---|
| Compose para subir aplicação **e** banco | [x] `compose.yaml` com `postgres` + `app` (profile `full`) |

#### Observação

- Arquivo no projeto: `compose.yaml` (equivalente ao `docker-compose.yml` pedido no enunciado).
- Subir tudo: `docker compose --profile full up --build`

### 8. Repositório de Código

| Critério | Status |
|---|---|
| Repositório GitHub/GitLab aberto | [ ] Confirmar publicação / visibilidade |
| Código-fonte no repositório | [x] |
| README | [x] `README.md` |
| Documentação Swagger no projeto | [x] SpringDoc + UI em `/swagger-ui/index.html` |
| Coleção JSON do Postman no projeto | [ ] **Pendente** (item 5) |
| Relatório PDF submetido separadamente | [~] Base em texto pronta (`docs/RELATORIO-TECNICO.md`); PDF ainda não gerado |

---

## Ajustes técnicos menores restantes

- [ ] (Opcional) validação prévia de login duplicado
- [ ] (Opcional) exclusão com validação de vínculos N:N

---

## Ordem sugerida para fechar a entrega

1. **Coleção Postman JSON** (item 5) — cobrir os cenários obrigatórios
2. Incluir prints Swagger/Postman no relatório e **exportar PDF** (item 6)
3. Publicar/abrir o repositório e submeter o PDF

---

## Resumo executivo

| Área | Situação |
|---|---|
| 1. Funcionalidade | Praticamente completa |
| 2. Qualidade do código | Parcial (testes opcionais) |
| 3. Swagger | Completo |
| 4. Banco de dados | Completo |
| 5. Postman | **Falta** |
| 6. Relatório | Texto pronto; **PDF** pendente |
| 7. Docker Compose (app + banco) | Completo |
| 8. Repositório | README ok; falta Postman |
