# Microsserviço Pessoa

## Descrição
Microsserviço responsável pelo gerenciamento de pessoas no sistema. Realiza operações CRUD para entidades Pessoa e integra com outros serviços através de mensageria Kafka para consulta no Serasa e envio de logs de auditoria.

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
- **Bean Validation**
- **Maven**

## Funcionalidades
- Cadastro de pessoas com validação de CPF
- Atualização de dados de pessoas
- Exclusão de pessoas
- Integração com serviço Serasa (verificação de negativação)
- Envio de logs de auditoria
- Circuit Breaker para resilência
- Documentação automática da API com Swagger

## Configuração

### Banco de Dados
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/pessoa_db?currentSchema=pessoa_db
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
- PostgreSQL (porta 5433)
- Apache Kafka (porta 9092)
- Maven

### Passos
1. Configure o banco PostgreSQL na porta 5433
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

## Estrutura do Projeto
```
src/main/java/com/example/pessoa/
├── PessoaApplication.java           # Classe principal da aplicação
├── config/
│   ├── exception/
│   │   ├── ExceptionHandler.java    # Utilitário para tratamento de exceções
│   │   ├── GlobalExceptionHandler.java # Manipulador global de exceções
│   │   ├── PessoaNaoEncontradaException.java # Exceção customizada
│   │   └── ProcessingException.java # Exceção de processamento
│   ├── jackson/
│   │   └── ObjectMapperConfig.java  # Configuração do Jackson
│   ├── kafka/
│   │   └── KafkaConfig.java         # Configuração Kafka (Producer/Consumer)
│   └── swagger/
│       └── SwaggerConfig.java       # Configuração do Swagger
├── constants/
│   ├── global/
│   │   └── MenssagemSistema.java   # Mensagens do sistema
│   ├── log/
│   │   ├── Operacao.java           # Constantes de operações de log
│   │   └── TopicLog.java           # Tópicos de log
│   └── serasa/
│       └── TopicSerasa.java        # Tópicos do Serasa
├── controller/
│   └── PessoaController.java       # Controller REST para pessoas
├── dto/
│   ├── ErrorResponse.java          # DTO para respostas de erro
│   ├── LogEventDto.java            # DTO para eventos de log
│   └── PessoaDto.java              # DTO da entidade pessoa
├── mapper/
│   └── PessoaMapper.java           # Mapeador entre DTO e entidade
├── model/
│   └── Pessoa.java                 # Entidade JPA pessoa
├── repository/
│   └── PessoaRepository.java       # Repositório JPA para pessoas
└── service/
    ├── kafka/
    │   ├── KafkaAssincronoService.java     # Serviço Kafka assíncrono
    │   ├── KafkaSerializationService.java  # Serviço de serialização
    │   └── KafkaSincronoService.java       # Serviço Kafka síncrono
    ├── log/
    │   └── LogService.java                 # Serviço de envio de logs
    ├── pessoa/
    │   └── PessoaService.java              # Serviço de negócio de pessoas
    └── serasa/
        └── SerasaService.java              # Serviço de integração Serasa

src/main/resources/
├── application.yml                  # Configurações da aplicação
└── db/migration/
    └── V1__create_table_pessoa.sql # Script de criação da tabela pessoa
```

## Modelo de Dados

### Tabela `pessoa`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGSERIAL | Chave primária |
| nome | VARCHAR(150) | Nome da pessoa |
| cpf | VARCHAR(11) | CPF (validado) |
| data_nascimento | DATE | Data de nascimento |
| negativado | BOOLEAN | Status de negativação (consultado no Serasa) |
| data_hora_criacao | TIMESTAMP | Data e hora da criação (automática) |

## Integração com Outros Serviços

### Serviço Serasa
- **Tópico Request**: `verificar-serasa-request`
- **Tópico Response**: `verificar-serasa-response`
- **Tipo**: Comunicação síncrona com timeout de 3 segundos
- **Circuit Breaker**: Ativado com fallback

### Serviço de Log
- **Tópico**: `enviar-log`
- **Tipo**: Comunicação assíncrona
- **Operações Logadas**: CADASTRO, ATUALIZAÇÃO, EXCLUSÃO

## Resilência
O serviço utiliza Circuit Breaker para o serviço Serasa:
- **Mínimo de chamadas**: 3
- **Taxa de falhas**: 50%
- **Tempo de espera**: 10 segundos
- **Transição automática**: Habilitada

## Validações
- **Nome**: 2 a 150 caracteres, obrigatório
- **CPF**: Formato válido (validação brasileira), obrigatório
- **Data de Nascimento**: Data no passado, obrigatória

## Exemplo de Payload

### Cadastro/Atualização de Pessoa
```json
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "dataNascimento": "1990-01-01"
}
```

### Resposta
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678901",
  "dataNascimento": "1990-01-01",
  "negativado": false,
  "dataHoraCriacao": "2024-01-01T10:00:00"
}
```

## Tratamento de Erros
- Validação automática de campos
- Exceções customizadas com mensagens claras
- Resposta padronizada para erros
- Logs detalhados para depuração

## Migrations
As migrações são executadas automaticamente pelo Flyway:
- `V1__create_table_pessoa.sql` - Criação da tabela pessoa

## Configurações de Log
- Logs do Hibernate SQL em nível DEBUG
- Logs do Apache Kafka em nível INFO
