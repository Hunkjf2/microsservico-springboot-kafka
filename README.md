# microsservico-springboot-kafka







Consumer com consulta

graph TD
%% Cliente
Client[Cliente/Frontend]

    %% Microserviço Principal
    subgraph MS ["Microserviço de Usuários"]
        %% Controllers
        UserController[User Controller<br/>POST /users<br/>PUT /users/:id<br/>DELETE /users/:id<br/>GET /users<br/>GET /users/:id]
        
        %% Service Layer
        UserService[User Service]
        UserRepo[User Repository]
        
        %% Event Publisher
        EventPublisher[Event Publisher]
    end
    
    %% Bases de Dados
    WriteDB[(Base de Escrita<br/>PostgreSQL Master)]
    ReadDB[(Base de Leitura<br/>PostgreSQL Read Replica)]
    
    %% Kafka
    subgraph K ["Apache Kafka"]
        UserTopic[user-events Topic<br/>CREATE/UPDATE/DELETE]
    end
    
    %% Serviço de Email
    subgraph ES ["Serviço de Email"]
        EmailConsumer[Email Consumer]
        EmailService[Email Service]
        EmailProvider[Provedor Email<br/>SMTP/SendGrid]
    end
    
    %% Fluxos de Escrita (com eventos)
    Client -->|POST/PUT/DELETE| UserController
    UserController --> UserService
    UserService --> UserRepo
    UserRepo -->|Escrita| WriteDB
    UserService -->|Apenas Escrita| EventPublisher
    EventPublisher -->|CREATE/UPDATE/DELETE Events| UserTopic
    
    %% Fluxos de Consulta (REST direto)
    Client -->|GET| UserController
    UserController -->|Consulta Direta| UserService
    UserService --> UserRepo
    UserRepo -->|Leitura| ReadDB
    
    %% Consumer fazendo consulta REST
    UserTopic -->|Consome Eventos<br/>de Escrita| EmailConsumer
    EmailConsumer -->|GET /users/:id<br/>para dados atuais| UserController
    EmailConsumer --> EmailService
    EmailService --> EmailProvider
    
    %% Estilos
    classDef clientStyle fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef serviceStyle fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef kafkaStyle fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef emailStyle fill:#fce4ec,stroke:#880e4f,stroke-width:2px
    classDef dbStyle fill:#f1f8e9,stroke:#33691e,stroke-width:2px
    classDef writeFlow stroke:#d32f2f,stroke-width:3px
    classDef readFlow stroke:#388e3c,stroke-width:3px
    
    class Client clientStyle
    class UserController,UserService,UserRepo,EventPublisher serviceStyle
    class UserTopic kafkaStyle
    class EmailConsumer,EmailService,EmailProvider emailStyle
    class WriteDB,ReadDB dbStyle