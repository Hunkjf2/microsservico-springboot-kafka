# Microsserviço Serasa

## Descrição
Microsserviço responsável por simular consultas de negativação no Serasa. Processa solicitações de verificação de CPF e retorna informações sobre a situação financeira da pessoa.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Kafka**
- **Lombok**
- **Jackson** (processamento JSON)
- **Maven**

## Funcionalidades
- Simulação de consulta Serasa por CPF
- Processamento de mensagens síncronas via Kafka
- Base de dados mockada para testes
- Resposta automática para solicitações de verificação

## Configuração

### Kafka
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      retries: 3
    consumer:
      group-id: serasa-group
      auto-offset-reset: earliest
```

## Como Executar

### Pré-requisitos
- Java 21
- Apache Kafka
- Maven

### Passos
1. Configure o Kafka na porta 9092
2. Execute o comando:
```bash
./mvnw spring-boot:run
```

O serviço estará disponível em: `http://localhost:8070`

## Tópicos Kafka

### Consumo
- **`verificar-serasa-request`**: Recebe solicitações de verificação de CPF

### Produção
- **`verificar-serasa-response`**: Envia resposta da consulta

## Fluxo de Comunicação
1. Recebe CPF no tópico `verificar-serasa-request`
2. Processa a consulta (simulação)
3. Retorna resultado (true/false) no tópico `verificar-serasa-response`

## Base de Dados Mockada
O serviço utiliza uma lista estática de CPFs para simular negativações:

```

### Lógica de Negócio
- CPFs na lista mockada retornam `true` (negativado)
- CPFs não listados retornam `false` (não negativado)
- Caracteres não numéricos são removidos automaticamente

## Estrutura do Projeto
```
src/main/java/com/example/serasa/
├── config/          # Configurações Kafka (Consumer/Producer)
├── constants/       # Constantes (tópicos)
├── dto/            # Data Transfer Objects
└── service/        # Serviços de negócio e consumo Kafka
```

## Exemplo de Uso

### Request (CPF enviado no tópico)
```
"12345678901"
```

### Response (retorno no tópico de resposta)
```
"true"
```

## Configurações de Log
O serviço registra:
- Recebimento de consultas de CPF
- Resultado das consultas
- Logs do Apache Kafka em nível INFO

## Características Técnicas
- **Comunicação Síncrona**: Utiliza padrão request-reply do Kafka
- **Tolerância a Falhas**: Configurado com retry de 3 tentativas
- **Processamento**: Assíncrono através de listeners Kafka
- **Acknowledgment**: Manual imediato para garantir processamento

## Integração
Este microsserviço é consumido pelo **Microsserviço Pessoa** durante o processo de cadastro, fornecendo informações sobre a situação financeira da pessoa baseada no CPF.