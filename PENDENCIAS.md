# Pendências — API Restaurant

Documento de gap analysis com base nos requisitos do projeto.  
Objetivo: checklist do que ainda falta implementar.

---

## Já atendido

- [x] Modelo com dois tipos de usuário: **Cliente** (`Customer`) e **Dono de restaurante** (`Restaurant`), ambos vinculados a `User`
- [x] Campos obrigatórios do usuário: nome, e-mail, login, senha, data da última alteração, endereço
- [x] E-mail único (constraint no banco + validação prévia via `UserRepository.findByEmail` em create/edit de cliente e restaurante)
- [x] Validação prévia cruzada (mesmo e-mail não passa entre cliente e restaurante); no edit ignora se o e-mail não mudou (`!isEmailEquals`)
- [x] `updatedAt` atualizado em create / edit / troca de senha do **cliente** e do **dono**
- [x] CRUD completo de cliente (create, update, delete, get por id, busca por nome)
- [x] CRUD completo de restaurante/dono (create, update, delete, listagem/busca por nome)
- [x] Endpoint separado de troca de senha do cliente: `PATCH /api/v1/customers/{id}/change-password`
- [x] Endpoint separado de troca de senha do dono: `PATCH /api/v1/restaurants/{id}/change-password`
- [x] Endpoint distinto de atualização de dados (sem senha) para cliente e dono
- [x] Hash de senha com BCrypt em todos os fluxos de create / troca de senha (`SecurityConfig` + `PasswordEncoder`)
- [x] Login para **cliente** e **dono**: `POST /api/v1/auth/login` (`UnauthorizedException` → **401**)
- [x] Versionamento de API via path: `/api/v1/customers`, `/api/v1/restaurants`, `/api/v1/auth`
- [x] Handler global de exceções com `ProblemDetail` (RFC 7807): `GlobalExceptionHandler`
  - **404** — `EntityNotFoundException`
  - **400** — `IllegalArgumentException`
  - **401** — `UnauthorizedException`
  - **409** — `DataIntegrityViolationException`
- [x] Remoção dos `try/catch` genéricos nos controllers (erros sobem para o handler)
- [x] Status `201 Created` nos POSTs de criação (cliente, restaurante, cliente vinculado)
- [x] `password` removido do `EditUserDTO` (senha só no endpoint de troca)
- [x] PostgreSQL via Docker Compose (`compose.yaml`)
- [x] `Dockerfile` multi-stage (JDK 25 build + JRE 25 runtime)
- [x] Serviço `app` no Compose (profile `full`) com datasource e `SPRING_DOCKER_COMPOSE_ENABLED=false`
  - Uso: `docker compose --profile full up --build`

---

## 1. Ajustes e inconsistências menores

### Pendente

- [ ] Revisar imports não usados / código morto nos controllers (ex.: `Customer`, `Restaurant`, repositórios e `Autowired` em `RestaurantController`)
- [ ] Alinhar tipo de retorno do `DELETE` de restaurante (`ResponseEntity<ResponseBodyRestaurantDTO>` com body vazio → preferir `ResponseEntity<Void>`)
- [ ] Validação prévia de **login** duplicado via `User` (hoje o conflito de login depende da constraint do banco + handler 409)
- [ ] Validar se a exclusão de restaurante/cliente deve checar vínculos N:N antes do delete

---

## Ordem sugerida de implementação

1. Limpeza menor de controllers / contratos de resposta
2. (Opcional) validação prévia de login duplicado no mesmo padrão do e-mail
3. (Opcional) regra de negócio para exclusão com vínculos

---

## Critérios de aceite (checklist final)

Com base nos entregáveis do enunciado:

- [x] Cadastro, atualização e exclusão de usuários (cliente **e** dono)
- [x] Troca de senha em endpoint separado (ambos os tipos)
- [x] Atualização das demais informações em endpoint distinto (ambos os tipos)
- [x] Registro da data da última alteração
- [x] Busca de usuários pelo nome
- [x] E-mail único no cadastro (constraint + validação prévia em `User`)
- [x] Serviço de validação de login (login + senha) para os dois tipos
- [x] Dois tipos de usuário: dono de restaurante e cliente
- [x] Versionamento de API (`/api/v1/...`)
- [x] ProblemDetail (RFC 7807) nas respostas de erro
- [x] Docker Compose com aplicação + banco relacional (`--profile full`)
