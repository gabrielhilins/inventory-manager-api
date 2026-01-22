# Small Business Inventory API

A robust RESTful API for a small business inventory management system, built with Spring Boot. This API provides functionalities for managing products, categories, sales, stock, and users with role-based access control.

## Technologies & Dependencies

- **Java 21:** Core programming language.
- **Spring Boot 3.2.0:** Main application framework.
- **Spring Security:** For authentication and authorization.
- **Spring Data JPA:** For data persistence and repository management.
- **PostgreSQL:** Production-ready relational database.
- **H2 Database:** For local development and testing.
- **Lombok:** To reduce boilerplate code.
- **Java JWT:** For generating and validating JSON Web Tokens.
- **Maven:** For project build and dependency management.
- **Docker:** For containerization.

## Features

- **Authentication:** Secure JWT-based authentication (`/login`, `/register`, `/logout`).
- **Role-Based Access Control:** Differentiated access for `ADMIN` and `USER` roles.
- **Product Management:** Full CRUD for products.
- **Category Management:** Full CRUD for product categories.
- **User Management:** CRUD for users (Admin-only).
- **Sales Processing:** Endpoint for processing sales, which automatically updates product stock.
- **Stock Auditing:** Logs all stock movements and provides history per product.
- **Containerization:** A multi-stage `Dockerfile` is provided for building a lightweight, production-ready image.

## Prerequisites

- Java 21 (or higher)
- Maven 3.6 (or higher)
- Docker (optional, for containerized execution)

## Running the Application

### Local Development (with H2)

By default, the application is configured to run with the H2 in-memory database. This is ideal for quick local testing without any external setup.

1.  **Clone the Repository:**
    ```bash
    git clone <your-repo-url>
    cd inventory-manager-api
    ```

2.  **Set Environment Variables:**
    The application requires a few environment variables to run, even for H2.

    ```sh
    # A strong, base64-encoded secret for signing JWTs
    export API_SECURITY_TOKEN_SECRET="your_super_secret_key_here"
    export API_SECURITY_TOKEN_ISSUER="my-app"
    export API_SECURITY_TOKEN_EXPIRATION_HOURS=2

    # Default admin user to be created on startup
    export ADMIN_USER_NAME="admin"
    export ADMIN_USER_EMAIL="admin@example.com"
    export ADMIN_USER_PASSWORD="admin_password"
    ```

3.  **Run the Application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The API will be available at `http://localhost:8080`.
    You can access the H2 console at `http://localhost:8080/h2-console` and use the default JDBC URL `jdbc:h2:mem:testdb`.

### Production Mode (with PostgreSQL)

To run the application with a PostgreSQL database, you must provide the database connection details via environment variables.

```sh
# Set the active profile to 'prod'
export SPRING_PROFILES_ACTIVE=prod

# PostgreSQL Connection Details
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/inventory_db"
export SPRING_DATASOURCE_USERNAME="your_postgres_user"
export SPRING_DATASOURCE_PASSWORD="your_postgres_password"

# Plus all the variables from the local development section
export API_SECURITY_TOKEN_SECRET="..."
# ...and so on
```
Then, run the application as before:
```bash
./mvnw spring-boot:run
```

## Running with Docker

The project includes a multi-stage `Dockerfile` to create a lean and efficient container.

1.  **Build the Docker Image:**
    ```bash
    docker build -t inventory-manager-api .
    ```

2.  **Run the Container:**
    You can run the container using either the default H2 database or by connecting it to a PostgreSQL database.

    **Option A: Run with H2 (for testing)**
    ```bash
    docker run -p 8080:8080 \
      -e API_SECURITY_TOKEN_SECRET="your_super_secret_key_here" \
      -e API_SECURITY_TOKEN_ISSUER="my-app" \
      -e API_SECURITY_TOKEN_EXPIRATION_HOURS=2 \
      -e ADMIN_USER_NAME="admin" \
      -e ADMIN_USER_EMAIL="admin@example.com" \
      -e ADMIN_USER_PASSWORD="admin_password" \
      inventory-manager-api
    ```

    **Option B: Run with PostgreSQL**
    ```bash
    docker run -p 8080:8080 \
      -e SPRING_PROFILES_ACTIVE="prod" \
      -e SPRING_DATASOURCE_URL="jdbc:postgresql://your-db-host:5432/inventory_db" \
      -e SPRING_DATASOURCE_USERNAME="your_postgres_user" \
      -e SPRING_DATASOURCE_PASSWORD="your_postgres_password" \
      -e API_SECURITY_TOKEN_SECRET="your_super_secret_key_here" \
      -e API_SECURITY_TOKEN_ISSUER="my-app" \
      -e API_SECURITY_TOKEN_EXPIRATION_HOURS=2 \
      -e ADMIN_USER_NAME="admin" \
      -e ADMIN_USER_EMAIL="admin@example.com" \
      -e ADMIN_USER_PASSWORD="admin_password" \
      inventory-manager-api
    ```

## API Endpoints

All endpoints are prefixed with `/api`.

| Endpoint | Method | Description | Access |
|---|---|---|---|
| `/health` | `GET` | Health check. | Public |
| `/auth/register` | `POST` | Register a new user. | Public |
| `/auth/login` | `POST` | Authenticate and receive a JWT. | Public |
| `/auth/logout` | `POST` | Invalidate the current JWT. | Authenticated |
| `/users/me` | `GET` | Get current user's info. | Authenticated |
| `/users` | `GET` | Get all users. | Admin |
| `/users/{id}` | `GET` | Get a single user by ID. | Admin |
| `/users/{id}` | `PUT` | Update a user. | Admin |
| `/users/{id}` | `DELETE` | Delete a user. | Admin |
| `/categories` | `GET` | Get all categories. | Public |
| `/categories/{id}` | `GET` | Get a single category by ID. | Public |
| `/categories` | `POST` | Create a new category. | Admin |
| `/categories/{id}` | `PUT` | Update a category. | Admin |
| `/categories/{id}`| `DELETE`| Delete a category. | Admin |
| `/products` | `GET` | Get all products. | Public |
| `/products/{id}` | `GET` | Get a single product by ID. | Public |
| `/products/alerts` | `GET` | Get products with low stock. | Public |
| `/products` | `POST` | Create a new product. | Admin |
| `/products/{id}` | `PUT` | Update a product. | Admin |
| `/products/{id}` | `DELETE`| Delete a product. | Admin |
| `/sales` | `POST` | Process a new sale. | Authenticated |
| `/sales` | `GET` | Get a history of all sales. | Authenticated |
| `/stock/history/{productId}` | `GET` | Get stock history for a product. | Authenticated |

```
