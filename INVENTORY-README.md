# Inventory Client Application

A Spring Boot microservice application for managing product inventory with batch tracking capabilities. This application provides RESTful APIs to manage products and their associated batch information, including quantity tracking, expiry dates, and pricing.

## 🚀 Features

- **Product Management**: Create, retrieve, and update product information
- **Batch Tracking**: Manage product batches with expiry dates, quantities, and pricing
- **Inventory Operations**: Add products to inventory and update batch quantities
- **Batch Expiry Sorting**: Retrieve batches sorted by expiry time
- **Quantity Validation**: Ensures batch quantities are not exceeded during updates
- **Microservices Architecture**: Built with Spring Cloud for service discovery and load balancing
- **Circuit Breaker**: Integrated with Resilience4j for fault tolerance
- **Comprehensive Testing**: Unit tests with Mockito for service layer

## 🛠️ Tech Stack

- **Java**: 21
- **Spring Boot**: 4.0.0
- **Spring Data JPA**: For database operations
- **H2 Database**: In-memory database (can be configured for PostgreSQL)
- **Lombok**: For reducing boilerplate code
- **Netflix Eureka Client**: Service discovery
- **JUnit 5 & Mockito**: For unit testing

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code) - Optional

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd InventoryClient
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run the `InventoryClientApplication` class directly from your IDE.

The application will start on **port 9091** by default.

### 4. Access H2 Console

Once the application is running, you can access the H2 database console at:
```
http://localhost:9091/h2-console
```

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:orderDB`
- Username: `user`
- Password: `password`

## 📡 API Endpoints

All endpoints are prefixed with `/v1`

### 1. Health Check
```
GET /v1/
```
Returns a greeting message.

**Response:**
```
"hello from inventory"
```

### 2. Get Product Inventory Details
```
GET /v1/inventory/{productId}
```
Retrieves all batches for a specific product, sorted by expiry time (newest first).

**Path Parameters:**
- `productId` (long): The ID of the product

**Response:**
```json
[
  {
    "id": 1,
    "batchId": 101,
    "batchType": "Type A",
    "expiryTime": "2024-12-31T23:59:59Z",
    "price": 99.99,
    "quantity": 100
  }
]
```

**Status Codes:**
- `200 OK`: Success
- `400 BAD_REQUEST`: Error occurred

### 3. Add Products to Inventory
```
POST /v1/inventory
```
Adds a new product and batch to the inventory. If the product or batch already exists, it updates them accordingly.

**Request Body:**
```json
{
  "productId": 100,
  "batchId": 1,
  "productName": "Sample Product",
  "productDescription": "Product description",
  "expiryDate": "2024-12-31T23:59:59Z",
  "batchType": "Type A",
  "quantity": 100,
  "price": 99.99
}
```

**Response:**
```json
{
  "id": 10,
  "productName": "Sample Product",
  "productDescription": "Product description",
  "batches": [...]
}
```

**Status Code:**
- `200 OK`: Success

### 4. Update Inventory Quantity
```
POST /v1/inventory/update
```
Updates the batch quantity by reducing it. Validates that the requested quantity is available.

**Request Body:**
```json
{
  "productId": 100,
  "productName": "Sample Product",
  "batchId": 1,
  "amount": 99.99,
  "quantity": 10
}
```

**Response:**
```
"Updated"
```

**Status Codes:**
- `200 OK`: Success
- `400 BAD_REQUEST`: Invalid quantity (exceeds available or is negative)

## 📁 Project Structure

```
InventoryClient/
├── src/
│   ├── main/
│   │   ├── java/com/example/InventoryClient/
│   │   │   ├── API/
│   │   │   │   └── ProductController.java          # REST API endpoints
│   │   │   ├── DTO/
│   │   │   │   ├── ProductDTO.java                 # Data transfer object for updates
│   │   │   │   └── ProductOnboardDTO.java          # Data transfer object for onboarding
│   │   │   ├── Exception/
│   │   │   │   ├── InvalidArgumentException.java   # Custom exception
│   │   │   │   └── ProductControllerAdvice.java    # Global exception handler
│   │   │   ├── Model/
│   │   │   │   ├── Product.java                    # Product entity
│   │   │   │   └── BatchProduct.java               # BatchProduct entity
│   │   │   ├── repository/
│   │   │   │   ├── ProductRepository.java          # Product data access
│   │   │   │   └── BatchProductRepository.java     # BatchProduct data access
│   │   │   ├── service/
│   │   │   │   ├── ProductService.java             # Product service interface
│   │   │   │   ├── BatchProductService.java        # BatchProduct service interface
│   │   │   │   └── impl/
│   │   │   │       ├── ProductServiceImpl.java     # Product service implementation
│   │   │   │       └── BatchProductServiceImpl.java # BatchProduct service implementation
│   │   │   └── InventoryClientApplication.java     # Main application class
│   │   └── resources/
│   │       └── application.yaml                    # Application configuration
│   └── test/
│       └── java/com/example/InventoryClient/
│           ├── service/impl/
│           │   ├── ProductServiceImplTest.java     # Product service unit tests
│           │   └── BatchProductServiceImplTest.java # BatchProduct service unit tests
│           └── InventoryClientApplicationTests.java # Application context test
├── pom.xml                                         # Maven dependencies
└── README.md                                       # Project documentation
```

## 🧪 Running Tests

Execute all tests using Maven:

```bash
mvn test
```

To run tests with coverage report:

```bash
mvn test jacoco:report
```

### Test Coverage

The project includes comprehensive unit tests for:
- `ProductServiceImpl`: Tests for product CRUD operations and batch management
- `BatchProductServiceImpl`: Tests for batch product operations

All tests use **Mockito** for mocking dependencies and **JUnit 5** as the testing framework.

## 🗄️ Database Configuration

### Current Configuration (H2 - In-Memory)

The application is configured to use H2 in-memory database by default:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:orderDB
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: update
```

### Switching to PostgreSQL

To use PostgreSQL instead of H2:

1. Uncomment the PostgreSQL dependency in `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Update `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/inventorydb
    username: your_username
    password: your_password
    driverClassName: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

## 🔐 Entity Relationships

- **Product** (One) ↔ (Many) **BatchProduct**
  - A product can have multiple batches
  - Each batch belongs to one product
  - Cascade operations are configured for automatic batch management

## 🌐 Microservices Integration

This application is configured for microservices architecture with:

- **Spring Cloud Gateway**: API Gateway support (TODO)
- **Netflix Eureka**: Service discovery client (TODO)
- **Spring Cloud Load Balancer**: Client-side load balancing (TODO)
- **Resilience4j Circuit Breaker**: Fault tolerance (TODO)

To enable full microservices functionality, ensure you have:
- Eureka Server running
- Gateway Server configured (if using)

## 📝 API Examples

### Example 1: Add Product to Inventory

```bash
curl -X POST http://localhost:9091/v1/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 100,
    "batchId": 1,
    "productName": "Laptop",
    "productDescription": "Gaming Laptop",
    "expiryDate": "2025-12-31T23:59:59Z",
    "batchType": "Premium",
    "quantity": 50,
    "price": 1299.99
  }'
```

### Example 2: Get Product Inventory

```bash
curl -X GET http://localhost:9091/v1/inventory/100
```

### Example 3: Update Inventory Quantity

```bash
curl -X POST http://localhost:9091/v1/inventory/update \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 100,
    "productName": "Laptop",
    "batchId": 1,
    "amount": 1299.99,
    "quantity": 5
  }'
```

## 🐛 Error Handling

The application includes global exception handling:

- **InvalidArgumentException**: Thrown when invalid operations are attempted (e.g., insufficient quantity)
- **ProductControllerAdvice**: Global exception handler that returns appropriate HTTP status codes

## 🔄 Future Enhancements

Potential improvements for the application:

- [ ] Add authentication and authorization
- [ ] Implement pagination for inventory listings
- [ ] Add batch expiry notifications
- [ ] Implement inventory reports and analytics
- [ ] Add integration tests
- [ ] Implement caching for frequently accessed products
- [ ] Add API documentation with Swagger/OpenAPI
- [ ] Implement audit logging
- [ ] Add batch expiry alerts


