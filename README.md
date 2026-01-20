# Small Business Inventory API

A robust RESTful API for a small business inventory management system, built with Spring Boot. This API provides functionalities for managing products, categories, sales, stock, and users with role-based access control.

## Key Features

- **Authentication:** Secure JWT-based authentication for accessing endpoints.
- **Role-Based Access Control:** Differentiated access levels for `ADMIN` and `USER` roles.
- **Product Management:** Full CRUD (Create, Read, Update, Delete) operations for products.
- **Category Management:** Full CRUD operations for product categories.
- **User Management:** CRUD operations for managing users (Admin-only).
- **Sales Processing:** Endpoint for processing sales, which automatically updates product stock.
- **Stock Auditing:** Automatically logs all stock movements (e.g., sales, manual adjustments).
- **Low Stock Alerts:** An endpoint to fetch all products that have fallen below their minimum stock level.

## Technologies & Dependencies

- **Java 21:** Core programming language.
- **Spring Boot 3.5.5:** Main application framework.
- **Spring Security:** For authentication and authorization.
- **Spring Data JPA:** For data persistence and repository management.
- **PostgreSQL:** Production-ready relational database.
- **Lombok:** To reduce boilerplate code (getters, setters, constructors).
- **Java JWT (auth0):** For generating and validating JSON Web Tokens.
- **Maven:** For project build and dependency management.
- **JUnit 5 & Mockito:** For unit and integration testing.

## Architecture Overview

The API follows a classic layered architecture pattern to ensure a clean separation of concerns, making it scalable and maintainable.

- **`controller`**: Handles incoming HTTP requests, validates request data, and serializes responses. It is the entry point to the API.
- **`service`**: Contains the core business logic. It orchestrates calls between repositories and other services to fulfill a request.
- **`repository`**: The data access layer, responsible for all communication with the database. It uses Spring Data JPA interfaces.
- **`model`**: Contains the JPA entity classes that map to database tables.
- **`dto` (Data Transfer Object)**: Defines objects used to transfer data between the client and the server, decoupling the API's public contract from the internal database structure.
- **`converters`**: Utility classes responsible for converting between DTOs and JPA entities.
- **`security`**: Manages authentication and authorization, including JWT generation, validation, and the security filter chain.
- **`exception`**: Implements a global exception handler to provide consistent and clear error responses.

## Package Structure

```
com.gabrielhenrique.small_business_inventory
│
├── config          # Spring Security and other bean configurations.
├── controller      # REST API controllers for each entity.
├── converters      # DTO-to-Entity and Entity-to-DTO converters.
├── dto             # Data Transfer Objects for API requests and responses.
├── exception       # Global and custom exception classes.
├── model           # JPA entities and enums.
├── repository      # Spring Data JPA repositories.
├── security        # JWT token service and security filter.
└── service         # Business logic layer for each entity.
```

## Local Setup and Configuration

### Prerequisites

- Java 21 (or higher)
- Maven 3.6 (or higher)
- A running PostgreSQL instance

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/inventory-manager-api.git
cd inventory-manager-api
```

### 2. Configure the Application

Open the `src/main/resources/application.properties` file and update the following properties to match your local environment:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Secret (replace with a strong, base64-encoded secret)
api.security.token.secret=your_jwt_secret_here
```

### 3. Run the Application

You can run the API using the Maven wrapper included in the project:

```bash
# On Windows
./mvnw.cmd spring-boot:run

# On macOS/Linux
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Endpoints

All endpoints are prefixed with `/api`.

| Endpoint                                  | Method | Description                                 | Access       |
| ----------------------------------------- | ------ | ------------------------------------------- | ------------ |
| `/auth/register`                          | `POST` | Register a new user.                        | Public       |
| `/auth/login`                             | `POST` | Authenticate and receive a JWT.             | Public       |
| `/categories`                             | `GET`  | Get all categories.                         | Authenticated |
| `/categories/{id}`                        | `GET`  | Get a single category by ID.                | Authenticated |
| `/categories`                             | `POST` | Create a new category.                      | Admin        |
| `/categories/{id}`                        | `PUT`  | Update an existing category.                | Admin        |
| `/categories/{id}`                        | `DELETE`| Delete a category.                          | Admin        |
| `/products`                               | `GET`  | Get all products.                           | Authenticated |
| `/products/{id}`                          | `GET`  | Get a single product by ID.                 | Authenticated |
| `/products/alerts`                        | `GET`  | Get products with low stock.                | Authenticated |
| `/products`                               | `POST` | Create a new product.                       | Admin        |
| `/products/{id}`                          | `PUT`  | Update an existing product.                 | Admin        |
| `/products/{id}`                          | `DELETE`| Delete a product.                           | Admin        |
| `/sales`                                  | `POST` | Process a new sale.                         | Authenticated |
| `/sales`                                  | `GET`  | Get a history of all sales.                 | Authenticated |
| `/users`                                  | `GET`  | Get all users.                              | Admin        |
| `/users/{id}`                             | `GET`  | Get a single user by ID.                    | Admin        |
| `/users/{id}`                             | `PUT`  | Update an existing user.                    | Admin        |
| `/users/{id}`                             | `DELETE`| Delete a user.                              | Admin        |
| `/stock-movements`                        | `GET`  | Get a history of all stock movements.       | Authenticated |

```
