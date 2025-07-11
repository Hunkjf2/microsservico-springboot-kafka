# Microsserviço Log

## Descrição
Microsserviço responsável pelo gerenciamento de logs de auditoria no sistema. Consome mensagens de eventos de outros microsserviços e armazena informações de auditoria no banco de dados PostgreSQL.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Kafka**
- **Flyway** (migrações de banco)
- **Lombok**
- **Jackson** (processamento JSON)
- **Maven**

## Funcionalidades
- Consumo de eventos de auditoria via Kafka
- Armazenamento de logs no banco PostgreSQL
- Processamento assíncrono de mensagens
- Migração automática do banco de dados

## Configuração

### Banco de Dados
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/log_db?currentSchema=log_db
    username: postgresql
    password: postgresql
```

### Kafka
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: log-service-group
      auto-offset-reset: earliest
      enable-auto-commit: true
```

## Como Executar

### Pré-requisitos
- Java 21
- PostgreSQL (porta 5432)
- Apache Kafka (porta 9092)
- Maven

### Passos
1. Configure o banco PostgreSQL na porta 5432
2. Configure o Kafka na porta 9092
3. Execute o comando:
```bash
./mvnw spring-boot:run
```

O serviço estará disponível em: `http://localhost:8060`

## Tópicos Kafka

### Consumo
- **`enviar-log`**: Recebe eventos de log dos outros microsserviços

## Estrutura do Projeto
```
src/main/java/com/example/log/
├── LogApplication.java              # Classe principal da aplicação
├── config/
│   ├── jackson/
│   │   └── ObjectMapperConfig.java  # Configuração do Jackson para JSON
│   └── kafka/
│       └── KafkaConfig.java         # Configuração do Kafka Consumer
├── constants/
│   └── TopicLog.java                # Constantes dos tópicos Kafka
├── dto/
│   ├── LogDto.java                  # DTO para transferência de dados de log
│   ├── LogEventDto.java             # DTO para eventos de log recebidos
│   └── PessoaDto.java               # DTO da entidade pessoa (para deserialização)
├── model/
│   └── Log.java                     # Entidade JPA para logs
├── repository/
│   └── LogRepository.java           # Repositório JPA para logs
└── service/
    ├── LogConsumerService.java      # Serviço consumidor Kafka
    └── LogService.java              # Serviço de negócio para logs

src/main/resources/
├── application.yml                  # Configurações da aplicação
└── db/migration/
    └── V1__create_table_log.sql     # Script de criação da tabela log
```

## Modelo de Dados

### Tabela `log`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGSERIAL | Chave primária |
| id_usuario | BIGINT | ID do usuário que executou a operação |
| nome_usuario | VARCHAR(100) | Nome do usuário |
| operacao | VARCHAR(30) | Tipo de operação (CADASTRO, ATUALIZAÇÃO, EXCLUSÃO) |
| dados | TEXT | Dados completos da operação em JSON |
| nome_microsservico | VARCHAR(60) | Nome do microsserviço que gerou o log |
| data_hora_criacao | TIMESTAMP | Data e hora da criação do log |

## Exemplo de Mensagem Consumida
```json
{
  "pessoaDto": {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678901",
    "dataNascimento": "1990-01-01",
    "negativado": false,
    "dataHoraCriacao": "2024-01-01T10:00:00"
  },
  "operacao": "CADASTRO",
  "microservico": "microservico-pessoa",
  "idUsuario": 1,
  "nomeUsuario": "Admin"
}
```

## Migrations
As migrações são executadas automaticamente pelo Flyway:
- `V1__create_table_log.sql` - Criação da tabela log

## Configurações de Log
- Logs do Spring Kafka em nível DEBUG
- Logs do Hibernate em nível DEBUG para SQL
- Logs de conexão do pool em nível ERROR

## Tratamento de Erros
- Captura e registra erros de processamento de mensagens
- Não interrompe o fluxo de consumo em caso de erro
- Logs detalhados para depuração

## Integração
Este microsserviço é consumido por:
- **Microsserviço Pessoa**: Envia logs de operações CRUD de pessoas

## Monitoramento
- Logs estruturados para rastreamento de operações
- Métricas de consumo do Kafka
- Auditoria completa de todas as operações do sistema