# Restaurant API

API REST em Spring Boot para gestão de **clientes** e **donos de restaurante**, com versionamento `/api/v1`, erros no padrão **ProblemDetail (RFC 7807)**, documentação **Swagger/OpenAPI** e execução via **Docker Compose**.

**Repositório:** https://github.com/BernardoSemiOficial/api-restaurante

---

## Stack

- Java 25 / Spring Boot 4.1
- Spring Data JPA + PostgreSQL
- Spring Security (BCrypt)
- SpringDoc OpenAPI (Swagger UI)
- Docker / Docker Compose

---

## Como executar

### Opção 1 — App + banco com Docker Compose (recomendado)

```bash
docker compose --profile full up --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Postgres (host): `localhost:5436`

Para parar:

```bash
docker compose --profile full down
```

### Opção 2 — Desenvolvimento local (Maven)

Com Docker disponível, o Spring Boot sobe automaticamente o serviço `postgres` do `compose.yaml` (sem o profile `full`, só o banco).

```bash
./mvnw spring-boot:run
```

Requisitos: JDK 25+ e Docker (para o Postgres).

---

## Variáveis de ambiente (serviço `app` no Compose)

| Variável | Valor padrão | Descrição |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/restaurant` | JDBC na rede Docker |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Senha do banco |
| `SPRING_DOCKER_COMPOSE_ENABLED` | `false` | Evita o container da app subir outro Compose |

Postgres no Compose:

| Variável | Valor |
|---|---|
| `POSTGRES_DB` | `restaurant` |
| `POSTGRES_USER` | `postgres` |
| `POSTGRES_PASSWORD` | `postgres` |
| Porta no host | `5436` → `5432` no container |

---

## Endpoints principais (`/api/v1`)

| Método | Path | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Validação de login (cliente ou dono) |
| `GET` | `/customers?customerName=` | Busca clientes por nome |
| `GET` | `/customers/{id}` | Busca cliente por ID |
| `POST` | `/customers` | Cria cliente |
| `PUT` | `/customers/{id}` | Atualiza dados (sem senha) |
| `PATCH` | `/customers/{id}/change-password` | Troca de senha |
| `DELETE` | `/customers/{id}` | Exclui cliente |
| `GET` | `/restaurants?restaurantName=` | Lista/busca restaurantes por nome |
| `POST` | `/restaurants` | Cria restaurante/dono |
| `PUT` | `/restaurants/{id}` | Atualiza dados (sem senha) |
| `PATCH` | `/restaurants/{id}/change-password` | Troca de senha |
| `DELETE` | `/restaurants/{id}` | Exclui restaurante |
| `GET` | `/restaurants/{id}/customers` | Clientes vinculados |
| `POST` | `/restaurants/{id}/customers` | Cria cliente vinculado ao restaurante |

Documentação interativa: **Swagger UI** em `/swagger-ui/index.html`.

---

## Documentação do projeto

| Arquivo | Conteúdo |
|---|---|
| [docs/RELATORIO-TECNICO.md](docs/RELATORIO-TECNICO.md) | Rascunho do relatório PDF (textos + placeholders de imagem) |
| [docs/imagens/](docs/imagens/) | Pasta para prints do PDF (Swagger, Postman, DER) |
| [PENDENCIAS.md](PENDENCIAS.md) | Checklist de entregáveis e critérios |
| [Restaurant API.postman_collection.json](Restaurant%20API.postman_collection.json) | Coleção Postman (`/api/v1`) |
| [HELP.md](HELP.md) | Notas do Spring Initializr |

Importe o JSON no Postman e use a variável `baseUrl` (`http://localhost:8080`).  
Para o PDF: preencha a capa em `docs/RELATORIO-TECNICO.md`, adicione os prints em `docs/imagens/` e exporte o Markdown.

---

## Estrutura resumida

```
src/main/java/com/api/restaurant/
  config/        # Security, OpenAPI
  controller/    # Auth, Customer, Restaurant
  service/
  repository/
  model/
  interfaces/    # DTOs
  exception/     # ProblemDetail / handler global
```
