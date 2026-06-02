# IESPFLIX - Projeto Flix Backend

API REST de uma plataforma de streaming desenvolvida com Spring Boot. O projeto cobre cadastro
de usuários, autenticação, catálogo de filmes e séries, favoritos, cartões tokenizados, planos e
assinaturas.

## Tecnologias

- Java 17+
- Spring Boot 3.5
- Spring Web, Spring Data JPA e Bean Validation
- Spring Security com HTTP Basic e BCrypt
- H2 Database
- SpringDoc OpenAPI / Swagger UI
- OpenFeign com integração ViaCEP
- JUnit, Mockito e JaCoCo

## Pré-requisitos

- Java 17 ou superior disponível no `PATH`
- Conexão com a internet na primeira execução do Maven Wrapper

O Maven não precisa estar instalado globalmente.

## Executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

A aplicação inicia em `http://localhost:8080`.

Links úteis:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Console H2: `http://localhost:8080/h2`

Configuração do H2:

- JDBC URL: `jdbc:h2:file:~/teckback20262`
- Usuário: `sa`
- Senha: vazia

## Fluxos Obrigatórios

### Cadastro de usuário e assinatura

O cadastro recebe todos os campos exigidos pelo RF1. A senha é salva como hash BCrypt. O número
completo do cartão e o código de segurança são recebidos apenas para tokenização local de
demonstração: eles não são persistidos e não aparecem na resposta.

```http
POST /api/v1/usuarios
Content-Type: application/json

{
  "nomeCompleto": "Maria da Silva",
  "dataNascimento": "1995-05-20",
  "email": "maria@example.com",
  "senha": "Senha@123",
  "confirmarSenha": "Senha@123",
  "cpfCnpj": "52998224725",
  "numeroCartao": "4111111111111111",
  "validadeCartao": "12/2030",
  "codigoSegurancaCartao": "123",
  "nomeTitularCartao": "Maria da Silva"
}
```

### Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "maria@example.com",
  "senha": "Senha@123"
}
```

As rotas protegidas usam HTTP Basic com o mesmo e-mail e senha cadastrados.

### Alterar cartão após autenticação

```bash
curl -u maria@example.com:Senha@123 \
  -X PUT http://localhost:8080/api/v1/metodos-pagamento/1 \
  -H "Content-Type: application/json" \
  -d "{\"numeroCartao\":\"5555555555554444\",\"validadeCartao\":\"12/2031\",\"codigoSegurancaCartao\":\"321\",\"nomeTitularCartao\":\"Maria da Silva\"}"
```

### Catálogo detalhado

```http
GET /api/v1/conteudos
GET /api/v1/conteudos/{id}
GET /api/v1/conteudos?tipo=FILME
GET /api/v1/conteudos?tipo=SERIE
```

Cada conteúdo possui título, gênero, ano, duração, relevância, sinopse e URL do trailer.

### Favoritos separados por tipo

```http
GET /api/v1/favoritos/usuario/{usuarioId}/filmes
GET /api/v1/favoritos/usuario/{usuarioId}/series
```

### Cadastro administrativo de conteúdo

As operações de escrita do catálogo exigem o perfil `ADMIN`. Para demonstração em sala, existe o
administrador local `admin` com senha `admin123`.

```bash
curl -u admin:admin123 \
  -X POST http://localhost:8080/api/v1/conteudos \
  -H "Content-Type: application/json" \
  -d "{\"titulo\":\"Interestelar\",\"tipo\":\"FILME\",\"ano\":2014,\"duracaoMinutos\":169,\"relevancia\":9.5,\"sinopse\":\"Ficção científica\",\"trailerUrl\":\"https://example.com/trailer\",\"genero\":\"Ficção Científica\"}"
```

## Serviço Externo

A integração com o ViaCEP usa OpenFeign. Um exemplo pode ser executado por `POST /funcionarios`
enviando `nome`, `cargo` e `cep`; o serviço completa os dados de endereço retornados pela API.

## Testes

No Windows:

```powershell
.\mvnw.cmd test
```

No Linux ou macOS:

```bash
./mvnw test
```

O relatório JaCoCo é gerado em `target/site/jacoco/index.html`.
