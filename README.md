# Online Marketplace Platform

A microservices-based online marketplace backend built with Java Spring Boot.

## Architecture

- **API Gateway** (Port 8080): Routes and authenticates requests
- **Member Service** (Port 8081): User registration and authentication
- **Product Service** (Port 8082): Product catalog management
- **Cart Service** (Port 8083): Shopping cart operations

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- PostgreSQL (Member & Cart services)
- MongoDB (Product service)
- Redis (Session/Cache - optional)
- JWT for authentication

## Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL running on port 5432
- MongoDB running on port 27017

## Database Setup

### PostgreSQL
Create databases:
```sql
CREATE DATABASE marketplace_member;
CREATE DATABASE marketplace_cart;
```

### MongoDB
MongoDB will auto-create the `marketplace_product` database on first connection.

## Running the Services

### 1. Start Member Service
```bash
cd member
mvn spring-boot:run
```

### 2. Start Product Service
```bash
cd product
mvn spring-boot:run
```

### 3. Start Cart Service
```bash
cd cart
mvn spring-boot:run
```

### 4. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

## API Endpoints

All requests go through API Gateway at `http://localhost:8080`

### Authentication (No auth required)

**Register**
```bash
POST /api/member/register
Content-Type: application/json

{
  "username": "john",
  "password": "password123",
  "email": "john@example.com",
  "fullName": "John Doe"
}
```

**Login**
```bash
POST /api/member/login
Content-Type: application/json

{
  "username": "john",
  "password": "password123"
}
```
Returns JWT token to use in subsequent requests.

### Products (Auth required)

**Seed Products** (Creates 50,000 products)
```bash
POST /api/product/seed
Authorization: Bearer <your-jwt-token>
```

**Search Products**
```bash
GET /api/product?name=Product&page=0&size=20
Authorization: Bearer <your-jwt-token>
```

**Get Product by ID**
```bash
GET /api/product/{id}
Authorization: Bearer <your-jwt-token>
```

### Shopping Cart (Auth required)

**Add to Cart**
```bash
POST /api/cart
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "productId": "product-id-here",
  "productName": "Product Name",
  "price": 99.99,
  "quantity": 2
}
```

**View Cart**
```bash
GET /api/cart
Authorization: Bearer <your-jwt-token>
```

**Remove from Cart**
```bash
DELETE /api/cart/{productId}
Authorization: Bearer <your-jwt-token>
```

## Security

- Passwords are hashed using BCrypt (Spring Security)
- JWT tokens are used for authentication with HS256 algorithm
- API Gateway validates JWT tokens for all protected endpoints
- Token expiration: 24 hours

## Building All Services

```bash
# Build API Gateway
cd api-gateway && mvn clean package -DskipTests

# Build Member Service
cd member && mvn clean package -DskipTests

# Build Product Service  
cd product && mvn clean package -DskipTests

# Build Cart Service
cd cart && mvn clean package -DskipTests
```

## Testing Flow

1. Register a new user via `/api/member/register`
2. Login via `/api/member/login` to get JWT token
3. Seed products via `/api/product/seed`
4. Search products via `/api/product?name=Product`
5. Add products to cart via `/api/cart`
6. View cart via `/api/cart`
7. Remove items via `/api/cart/{productId}`

## Project Structure

```
training-project-2025-11/
├── api-gateway/          # API Gateway with JWT validation
├── member/              # Member/Auth service (Postgres)
├── product/             # Product catalog service (MongoDB)
├── cart/                # Shopping cart service (Postgres)
└── requirements/        # Project requirements
```

## Notes

- All services use the same JWT secret key for token validation
- Product seeding creates 50,000 products with random prices
- Cart operations are user-specific (extracted from JWT)
- No inventory checking implemented (unlimited stock assumption)
