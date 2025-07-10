# Microsserviço Pessoa

## Descrição
Microsserviço responsável pelo gerenciamento de pessoas no sistema. Realiza operações CRUD para entidades Pessoa e integra com outros serviços através de mensageria Kafka.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Kafka**
- **Flyway** (migrações de banco)
- **Lombok**
- **SpringDoc OpenAPI** (Swagger)
- **Resilience4j** (Circuit Breaker)
- **Maven**

## Funcionalidades
- Cadastro de pessoas
- Atualização de dados de pessoas
- Exclusão de pessoas
- Integração com serviço Serasa (verificação de negativação)
- Envio de logs de auditoria
- Circuit Breaker para resilência

## Configuração

### Banco de Dados
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5434/pessoa_db?currentSchema=pessoa_db
    username: postgresql
    password: postgresql
```

### Kafka
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      retries: 3
    consumer:
      group-id: pessoa-reply-group
      auto-offset-reset: earliest
```

## Como Executar

### Pré-requisitos
- Java 21
- PostgreSQL
- Apache Kafka
- Maven

### Passos
1. Configure o banco PostgreSQL na porta 5434
2. Configure o Kafka na porta 9092
3. Execute o comando:
```bash
./mvnw spring-boot:run
```

O serviço estará disponível em: `http://localhost:8090`

## API Endpoints

### Swagger UI
- **URL**: `http://localhost:8090/swagger-ui.html`

### Principais Endpoints
- `POST /api/pessoa` - Cadastrar pessoa
- `PUT /api/pessoa/{id}` - Atualizar pessoa
- `DELETE /api/pessoa/{id}` - Deletar pessoa

## Integração com Outros Serviços

### Serviço Serasa
- **Tópico Request**: `verificar-serasa-request`
- **Tópico Response**: `verificar-serasa-response`
- **Tipo**: Comunicação síncrona com timeout de 3 segundos

### Serviço de Log
- **Tópico**: `enviar-log`
- **Tipo**: Comunicação assíncrona

## Resilência
O serviço utiliza Circuit Breaker para o serviço Serasa:
- **Janela deslizante**: 8 chamadas
- **Mínimo de chamadas**: 4
- **Taxa de falhas**: 50%
- **Tempo de espera**: 10 segundos

## Estrutura do Projeto
```
src/main/java/com/example/pessoa/
├── config/          # Configurações (Kafka, Jackson, Swagger, Exception)
├── constants/       # Constantes do sistema
├── controller/      # Controllers REST
├── dto/            # Data Transfer Objects
├── mapper/         # Mapeadores de entidades
├── model/          # Entidades JPA
├── repository/     # Repositórios
└── service/        # Serviços de negócio
```

## Migrations
As migrações são executadas automaticamente pelo Flyway:
- `V1__create_table_pessoa.sql` - Criação da tabela pessoa