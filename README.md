# ms-storage

Microservico responsavel pelo armazenamento e persistencia dos clientes do banco JAVER.

## Tecnologias

- Java 21
- Spring Boot 4.0.3
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- JUnit 5 + Mockito + AssertJ

## Pre-requisitos

- Java 21+
- Maven 3.9+

## Como executar
```bash
mvn spring-boot:run
```

O servico sobe na porta **8081**.

## Endpoints

### Criar cliente
```
POST /clientes
```
```json
{
  "nome": "Joao Silva",
  "telefone": 11999998888,
  "correntista": true,
  "scoreCredito": 750.0,
  "saldoCc": 2500.0
}
```

### Buscar por ID
```
GET /clientes/{id}
```

### Listar todos
```
GET /clientes
```

### Atualizar
```
PUT /clientes/{id}
```

### Deletar
```
DELETE /clientes/{id}
```

## Console H2

Disponivel em http://localhost:8081/h2-console
```
JDBC URL: jdbc:h2:mem:javerdb
Usuario:  sa
Senha:    (em branco)
```

## Como testar
```bash
mvn test
```

## Arquitetura
```
Controller
    |
Service (interface + impl)
    |
Repository (Spring Data JPA)
    |
H2 Database
```

## Decisoes tecnicas

- DTOs separados da entidade JPA para desacoplar contrato da API do modelo de persistencia
- Interface de Service para seguir o principio de inversao de dependencia (SOLID)
- GlobalExceptionHandler centraliza tratamento de erros com @RestControllerAdvice
- Testes unitarios isolados com Mockito sem subir contexto Spring
