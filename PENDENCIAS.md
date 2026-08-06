# Pendências — API Restaurant

Documento de gap analysis com base nos requisitos do projeto.  
Objetivo: checklist do que ainda falta implementar.

---

## Já atendido

- [x] Modelo com dois tipos de usuário: **Cliente** (`Customer`) e **Dono de restaurante** (`Restaurant`), ambos vinculados a `User`
- [x] Campos obrigatórios do usuário: nome, e-mail, login, senha, data da última alteração, endereço
- [x] E-mail único (constraint no banco e na entidade `User`)
- [x] `updatedAt` atualizado em create / edit / troca de senha do **cliente**
- [x] CRUD de cliente (create, update, delete, get por id)
- [x] Endpoint separado de troca de senha do cliente: `PATCH /api/customers/{id}/change-password`
- [x] Endpoint distinto de atualização de dados do cliente (sem senha): `PUT /api/customers/{id}`
- [x] Busca de clientes por nome: `GET /api/customers?customerName=...`
- [x] Serviço de validação de login (parcial — só cliente): `POST /api/auth/login`
- [x] PostgreSQL via Docker Compose (`compose.yaml`)

---

## 1. CRUD completo do dono de restaurante

Espelhar o que já existe para cliente.

### Pendente

- [ ] **Atualização** de restaurante/dono — hoje o `PUT /api/restaurants/{restaurantId}` é stub (retorna string fixa)
- [ ] **Exclusão** de restaurante/dono — hoje o `DELETE /api/restaurants/{restaurantId}` é stub
- [ ] Endpoint **separado** para troca de senha do dono (ex.: `PATCH /api/v1/restaurants/{id}/change-password`)
- [ ] Endpoint **distinto** para atualização das demais informações (sem senha)
- [ ] Busca de donos/restaurantes **por nome**
- [ ] Hash de senha (BCrypt) no create do restaurante — hoje a senha é salva em texto puro em `RestaurantService.createRestaurant`
- [ ] Hash de senha também em `CustomerService.createCustomerToRestaurant` (mesmo problema)

### Referência no código

- `RestaurantController` — stubs de put/delete
- `RestaurantService` — só create + listagem
- `RestaurantRepository` — falta métodos como `findByUserLogin` e `findByUserNameContainingIgnoreCase`

---

## 2. Login para os dois tipos de usuário

### Pendente

- [ ] Autenticar também o **dono de restaurante** (hoje `AuthService` só busca em `Customer`)
- [ ] Retornar status HTTP adequado em falha (ex.: **401 Unauthorized**), não só exception genérica
- [ ] Mensagens/erros consistentes quando login ou senha forem inválidos

### Referência no código

- `AuthService.login`
- `AuthController`
- `RestaurantRepository` — adicionar busca por login do usuário

---

## 3. Versionamento de API

### Pendente

- [ ] Incluir versão nas rotas (ex.: `/api/v1/customers`, `/api/v1/restaurants`, `/api/v1/auth/login`)
- [ ] Ajustar todos os controllers para a estratégia escolhida (path versioning recomendado por simplicidade)

### Situação atual

Rotas em `/api/...` sem versão.

---

## 4. ProblemDetail (RFC 7807)

### Pendente

- [ ] Handler global de exceções (`@ControllerAdvice` / `@RestControllerAdvice`)
- [ ] Respostas de erro no formato `ProblemDetail` (RFC 7807)
- [ ] Remover (ou reduzir) `try/catch` genérico nos controllers que devolvem `400` vazio + `printStackTrace`
- [ ] Mapear casos comuns, por exemplo:
  - **404** — recurso não encontrado (`EntityNotFoundException`)
  - **400** — dados inválidos / senha atual incorreta
  - **401** — login/senha inválidos
  - **409** — e-mail duplicado (conflito de unicidade)

---

## 5. Unicidade de e-mail e tratamento de conflito

### Pendente

- [ ] Validação prévia de e-mail já cadastrado (antes de salvar), além da constraint do banco
- [ ] Resposta padronizada de conflito (**409**) via ProblemDetail
- [ ] Garantir unicidade em **todos** os fluxos de criação (cliente, restaurante, cliente vinculado a restaurante)

---

## 6. Dockerização completa da aplicação

### Pendente

- [ ] Criar `Dockerfile` da API Spring Boot
- [ ] Incluir o serviço da aplicação no `compose.yaml` (orquestração **app + banco**)
- [ ] Configurar variáveis de ambiente / datasource para a app falar com o Postgres do Compose

### Situação atual

O `compose.yaml` sobe apenas o PostgreSQL.

---

## 7. Ajustes e inconsistências menores

- [ ] Remover `password` do DTO de edição de dados gerais (`EditUserDTO`) — senha deve existir só no endpoint de troca de senha
- [ ] Atualizar `updatedAt` também nos fluxos de edição/exclusão/senha do **dono de restaurante**
- [ ] Padronizar status de criação (`201 Created`) onde fizer sentido (cliente hoje retorna `200` no POST)
- [ ] Revisar imports não usados / código morto nos controllers

---

## Ordem sugerida de implementação

1. Completar CRUD + senha + busca do **dono de restaurante** (espelhando o cliente)
2. Unificar **login** para cliente e dono
3. Implementar **ProblemDetail** + tratamento de erros
4. **Versionar** a API (`/api/v1/...`)
5. **Dockerizar** a aplicação no Compose
6. Revisar hash de senha e unicidade de e-mail em **todos** os fluxos

---

## Critérios de aceite (checklist final)

Com base nos entregáveis do enunciado:

- [ ] Cadastro, atualização e exclusão de usuários (cliente **e** dono)
- [ ] Troca de senha em endpoint separado (ambos os tipos)
- [ ] Atualização das demais informações em endpoint distinto (ambos os tipos)
- [ ] Registro da data da última alteração
- [ ] Busca de usuários pelo nome
- [ ] E-mail único no cadastro
- [ ] Serviço de validação de login (login + senha)
- [ ] Dois tipos de usuário: dono de restaurante e cliente
- [ ] Versionamento de API
- [ ] ProblemDetail (RFC 7807) nas respostas de erro
- [ ] Docker Compose com aplicação + banco relacional
