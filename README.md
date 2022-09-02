# Mindera Pizza

---
## Description

API that allows the management of orders from the restaurant *Pizza Mindera*.

---
##Running the application locally

To run the project locally one simply needs to run the `main` method in the PizzaApplication class.
Alternatively, it is also possible to run the following maven command to run the project

```
mvn spring-boot:run
```

---
## Problem

Serviço que permita a gestão de encomendas para um restaurante da Pizza Mindera

- Story1: Eu como gestor da pizzaria pretendo fazer o registo de uma encomenda com os produtos pizza 4 queijos e coca cola, para isso devo aceder ao endpoint de recepção de encomendas; após isso devo aceitar o pedido para começar a prepara o pedido; fechar o pedido assim que terminar de preparar
- Story2: Eu como gestor da pizzaria pretendo fazer o registo de uma encomenda com os produtos pão de alho, para isso devo aceder ao endpoint de recepção de encomendas; após isso vejo que não tenho stock do produto e devo cancelar a encomenda.
- Story3: Eu como gestor da pizzaria pretendo ver a informação da encomenda com o id 44, consultando o endpoint "mostrar uma encomenda especifica" tenho acesso a toda a sua informação
- Story4: Eu como gestor da pizzaria pretendo ver todas as encomendas na loja até ao momento feitas pelo cliente com o nome José António, consultando o endpoint "mostrar todas as encomendas e informação relacionada" consigo obter essa informação
- Story5: Eu como gestor da pizzaria pretendo ver todas as encomendas em fase de preparação na cozinha, consultando o endpoint "mostrar todas as encomendas e informação relacionada" consigo obter essa informação

Terás de necessitar de pelo menos as entidades: order, client, product, etc

Criação de endpoints para gerir encomendas
- Criação de Swagger
- Ações possíveis via REST:
    - Endpoint para receção de pedidos
        - Endpoint para receber encomendas na loja
    - Endpoint para aceitar encomenda
        - Quando é recebido uma encomenda, deve ser feita a aceitação da mesma
    - Endpoint para fechar encomenda
        - Após a conclusão da encomenda e está pronta para entrega
    - Endpoint para cancelar encomenda
        - Quando o restaurante por não ter possibilidade de fazer a encomenda, cancela e não executa
    - Endpoint para mostrar todas as encomendas e informação relacionada
        - Possibilidade de filtrar por nome do cliente, numero de Telemovel do cliente, Nif do cliente, código da encomenda, 
    - Endpoint para mostrar uma encomenda especifica e informação relacionada (via id)


A informação das encomendas deve ser guardada e atualizada na base de dados (ex: H2)

Devem ser criados testes (JUNIT) para a criação de testes unitários às funcionalidades do serviço


---

## Technologies
- Java 17
- Spring Boot 2.7.2
- H2

---

## Data model
![Mindera Pizza data model](./assets/minderapizza.png)

---

## Features

### Current:

### To Do v1.0:
- Receive a new order
- Accept an existing order
- Close an existing order
- Cancel an existing order
- Fetch all orders (usage of filter optional)
- Fetch a single order by ID

### To Do v2.0:
- CRUD Clients
- CRUD Addresses
- CRUD Ingredients
- CRUD Products
- CRUD Categories

---

## Endpoints

### Current:

### To Do v1.0:

- POST /api/order | Create an order
- POST /api/order/:id/status | Change an order's status
- GET /api/order with the possibility of adding RequestParameters ex: name, phone number, nif, status, etc | Fetch N orders
- GET /api/order/:id | Fetch a single order

### To Do v2.0:
- POST /api/client | Create a client
- GET /api/client | Fetch N clients
- GET /api/client/:id | Fetch a single client
- POST /api/client/:id/address | Create an address
- GET /api/client/:id/address | Fetch a client's N addresses
- POST /api/ingredient | Create a ingredient
- GET /api/ingredient | Fetch N ingredients
- POST /api/product | Create a product
- GET /api/product | Fetch N products
- POST /api/category | Create a category
- GET /api/category | Fetch N categories
