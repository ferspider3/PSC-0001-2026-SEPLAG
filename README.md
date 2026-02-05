# PSC-0001-2026-SEPLAG
Repositório para a entrega da fase técnica do PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG e demais Órgãos - Engenheiro da Computação- Sênior

# 🎵 PROJETO PRÁTICO - IMPLEMENTAÇÃO BACK END JAVA SÊNIOR - Music API — Spring Boot 3

API RESTful robusta para gerenciamento de artistas e álbuns, desenvolvida com Spring Boot 3, seguindo boas práticas de arquitetura, segurança e observabilidade.

O projeto é totalmente conteinerizado e inclui autenticação JWT, documentação interativa, monitoramento de saúde, integração com Object Storage (MinIO) e notificações em tempo real via WebSockets.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- WebSocket (STOMP)
- Spring Actuator
- MinIO (S3-Compatible Storage)
- Docker e Docker Compose
- JUnit 5
- Mockito
- Swagger / OpenAPI

---

## ▶️ Como Executar o Projeto

O projeto é 100% conteinerizado.  
Você só precisa ter Docker e Docker Compose instalados.

### Passos

1. Clone o repositório e acesse a pasta raiz
2. Suba os containers com Docker Compose
3. Aguarde a inicialização

A API estará disponível em instantes.  
O banco de dados e os dados iniciais (Linkin Park, Guns N' Roses, etc.) são carregados automaticamente.

---

## 🛠️ Portais de Acesso Rápidos

Serviço: Swagger (Documentação)  
URL: http://localhost:8080/swagger-ui/index.html  
Credenciais: admin / password  

Serviço: Health Check  
URL: http://localhost:8080/actuator/health  

Serviço: MinIO (Object Storage)  
URL: http://localhost:9001  
Credenciais: admin / 90V521T8ET4UrV51tPhScGlIcIv7t5  

---

## 🛡️ Autenticação e Segurança

A API é protegida por JWT (JSON Web Token).

### Como autenticar no Swagger

1. Endpoint de login: POST /v1/auth/login
2. Usuário inicial:
   - Username: admin
   - Password: password
3. Copie o token retornado
4. No Swagger, clique em Authorize
5. Utilize o formato: Bearer SEU_TOKEN_AQUI

---

## 📊 Funcionalidades Implementadas

### Paginação, Ordenação e Filtros Dinâmicos

- Filtro por tipo de artista:
  GET /v1/albums?artistType=BANDA

- Busca por nome do artista:
  GET /v1/albums?artistName=Guns

- Paginação e ordenação:
  GET /v1/albums?page=0&size=5&sortBy=title&direction=asc

---

### Monitoramento (Spring Actuator)

Endpoints habilitados para liveness e readiness, permitindo integração com Kubernetes e ferramentas de observabilidade.

---

### Notificações em Tempo Real (WebSocket)

Sempre que um novo álbum é cadastrado, a API publica um evento no tópico:

/topic/new-album

Endpoint de conexão STOMP:

/ws

---

### Upload de Capas de Álbuns (MinIO / S3)

Integração com Object Storage S3-Compatible para upload e recuperação de imagens de capas de álbuns.

---

## 🧪 Testes Unitários

O projeto utiliza JUnit 5 e Mockito para testes automatizados.

Execução dos testes via Docker:

docker exec -it music-api-api mvn test

---

## 🏗️ Estrutura do Projeto (Maven)

src
- main
  - java
    - domain
    - service
    - controller
    - dto
  - resources
    - application.properties
    - db/migration
- test
  - java

---

## ✅ Requisitos Cumpridos

- CRUD completo de Artistas e Álbuns
- Relacionamento Many-to-Many
- Autenticação com Spring Security + JWT
- Paginação e Ordenação
- Health Checks (Actuator)
- WebSockets para notificações
- Testes Unitários com Mockito

---

## 💡 Dica para o Avaliador

Para validar paginação e ordenação rapidamente, utilize:

GET /v1/albums?page=0&size=5&sortBy=title&direction=asc

---

Projeto com foco em boas práticas, escalabilidade e padrões de mercado.
