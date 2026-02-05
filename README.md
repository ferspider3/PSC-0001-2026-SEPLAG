# Processo Seletivo – Engenheiro da Computação Sênior (Backend Java) - SEPLAG

Este repositório contém a **API Backend** desenvolvida como entrega da **fase técnica** do **PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG e demais Órgãos**.

A aplicação foi construída com foco em **boas práticas**, **segurança**, **escalabilidade**, **observabilidade** e **reprodutibilidade de ambiente**, utilizando **Spring Boot 3** e um ecossistema moderno totalmente **conteinerizado**.

---

## 📋 Dados da Inscrição
- **Processo Seletivo:** PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG e demais Órgãos
- **Cargo:** Engenheiro da Computação – Sênior
- **Inscrição:** 16251
- **Candidato:** Fernando Aranha de Oliveira Soares

---

## 🎵 Projeto Prático – Music API

API RESTful para **gerenciamento de artistas e álbuns**, com autenticação JWT, documentação interativa, notificações em tempo real e integração com Object Storage.

### Principais Diferenciais
- Arquitetura em camadas bem definida
- Segurança com **Spring Security + JWT**
- **WebSockets** para eventos em tempo real
- **MinIO (S3-Compatible)** para upload de capas de álbuns
- **Spring Actuator** para health checks e observabilidade
- Integração de dados com **versionamento e histórico** (Requisito Sênior)

---

## 🛠️ Tecnologias Utilizadas

### Core & Backend
- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA**
- **WebSocket (STOMP)**
- **Spring Actuator**

### Infraestrutura & DevOps
- **Docker** e **Docker Compose**
- **MinIO** (Object Storage S3-Compatible)

### Qualidade & Documentação
- **JUnit 5** e **Mockito**
- **Swagger / OpenAPI**

---

## 🚀 Como Executar o Sistema

Siga os passos abaixo para configurar e rodar a aplicação localmente:

### 1. Pré-requisitos
Certifique-se de que você possui o **Git** instalado:
- [Download Git](https://git-scm.com/install/windows)

Após a instalação, confirme que o Git está instalado, no seu terminal execute:
```bash
git --version
```

Certifique-se de que você possui o **Docker** instalado:
- [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)

Após a instalação, confirme se o serviço está ativo, no seu terminal execute:
```bash
docker --version

docker ps
```
<small>
Se este comando retornar um erro de "pipe" ou "connection refused", o Docker Desktop ainda não terminou de inicializar.
</small>

### ATENÇÃO: Pare todos os containers que estiverem executando
```bash
docker rm -f $(docker ps -aq)
```

### ATENÇÃO: Remove todos os containers parados, redes não utilizadas e imagens sem uso
```bash
docker system prune -a --volumes -f
```
<small>Remoção de qualquer vestígio para não ocorrer erro durante a inicialização do Docker</small>

### 2. Clonar o Projeto
Clone o repositório no seu ambiente local, em um diretório abra o seu terminal e execute:
```bash
git clone https://github.com/ferspider3/PSC-0001-2026-SEPLAG
```
Acesse a pasta do repositório
```bash
cd PSC-0001-2026-SEPLAG
```

### 3. Inicialização via Docker
Limpa containers, imagens e volumes antigos do projeto
```bash
docker-compose down --rmi all --volumes --remove-orphans
```

Build e inicialização do container
```bash
docker-compose up -d --build
```

Aguarde a inicialização completa dos serviços.

> ✔️ O banco de dados e os **dados iniciais** (Linkin Park, Guns N' Roses, etc.) são carregados automaticamente.

---

## 🛠️ Portais de Acesso Rápido

- **Swagger (Documentação da API)**  \
  http://localhost:8080/swagger-ui/index.html

- **Health Check (Actuator)**  \
  http://localhost:8080/actuator/health

- **MinIO – Object Storage**  \
  http://localhost:9001  \
  **Usuário:** admin  \
  **Senha:** 90V521T8ET4UrV51tPhScGlIcIv7t5

---

## 🛡️ Autenticação e Segurança

A API é protegida por **JWT (JSON Web Token)**.

### Como autenticar via Swagger

1. Realize o login:
   - **Endpoint:** `POST /v1/auth/login`
   - **Usuário:** admin
   - **Senha:** password

2. Copie o `accessToken` retornado.
3. No Swagger, clique em **Authorize**.
4. Utilize o formato:
   ```json
   {
     "accessToken": "SEU_TOKEN_AQUI"
   }
   ```

---

## 🌍 Integração de Regionais (Requisito Sênior)

A API possui um módulo de integração com o sistema **Argus**, responsável por sincronizar dados de regionais, mantendo **histórico**, **versionamento** e **integridade**.

### Como Testar a Sincronização

1. **Carga Inicial**:
   - `POST /v1/regionais/sync`
   - Execute via Swagger

2. **Consulta dos Dados**:
   - `GET /v1/regionais`
   - Resultado esperado: registros com `"ativo": true`

### Regras de Negócio Implementadas
- **Novos registros:** inseridos automaticamente com `ativo = true`
- **Registros ausentes no endpoint:** marcados como `ativo = false` (soft delete)
- **Alteração de nome:**
  - Registro antigo é desativado
  - Novo registro é criado, preservando histórico

---

## 📊 Funcionalidades Implementadas

### CRUD Completo
- Artistas
- Álbuns

### Relacionamentos
- **Many-to-Many** entre Artistas e Álbuns

### Paginação, Ordenação e Filtros Dinâmicos

- Filtro por tipo de artista:
  ```http
  GET /v1/albums?artistType=BANDA
  ```

- Busca por nome do artista:
  ```http
  GET /v1/albums?artistName=Guns
  ```

- Paginação e ordenação:
  ```http
  GET /v1/albums?page=0&size=5&sortBy=title&direction=asc
  ```

---

## 📡 Notificações em Tempo Real (WebSocket)

Sempre que um **novo álbum** é cadastrado, a API publica um evento em:

- **Tópico:** `/topic/new-album`
- **Endpoint STOMP:** `/ws`

---

## 🖼️ Upload de Capas de Álbuns

Upload e recuperação de imagens utilizando **MinIO (S3-Compatible Storage)**, garantindo desacoplamento e escalabilidade.

---

## 🧪 Testes Automatizados

Testes unitários desenvolvidos com **JUnit 5** e **Mockito**.

Execução dos testes dentro do container:
```bash
docker exec -it music-api-api mvn test
```

---

## 🏗️ Estrutura do Projeto

```text
src
 ├─ main
 │  ├─ java
 │  │  ├─ domain
 │  │  ├─ service
 │  │  ├─ controller
 │  │  └─ dto
 │  └─ resources
 │     ├─ application.properties
 │     └─ db/migration
 └─ test
    └─ java
```

---

## ✅ Checklist de Requisitos Atendidos

- ✔️ CRUD de Artistas e Álbuns
- ✔️ Relacionamento Many-to-Many
- ✔️ Autenticação com JWT
- ✔️ Paginação e Ordenação
- ✔️ Health Checks (Actuator)
- ✔️ WebSockets
- ✔️ Integração com Object Storage
- ✔️ Testes Unitários

---

## 💡 Dica ao Avaliador

Para validar rapidamente paginação e ordenação:
```http
GET /v1/albums?page=0&size=5&sortBy=title&direction=asc
```

---

Projeto desenvolvido com foco em **padrões de mercado**, **qualidade de código** e **facilidade de avaliação técnica**.

_2026_

