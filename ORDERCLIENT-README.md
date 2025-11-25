# OrderClient

A Spring Boot microservice application for managing orders. This service handles order creation, integrates with an inventory service, and manages order status and payment information.

##  Features

- **Order Management**: Create and manage orders with order items
- **Inventory Integration**: Communicates with inventory service to update stock
- **Service Discovery**: Integrated with Netflix Eureka for service discovery
- **Circuit Breaker**: Uses Resilience4j for fault tolerance
- **RESTful API**: REST endpoints for order operations
- **Exception Handling**: Global exception handling with custom error responses
- **In-Memory Database**: Uses H2 database for development and testing

##  Technology Stack

- **Java**: 21
- **Spring Boot**: 4.0.0
- **Spring Data JPA**: For database operations
- **H2 Database**: In-memory database
- **Lombok**: For reducing boilerplate code
- **Maven**: Build tool
- **RestClient**: For HTTP service-to-service communication

##  Prerequisites

- Java 21 or higher
- Maven 3.6+ 
- (Optional) Eureka Server running for service discovery

##  Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd OrderClient
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   
   On Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. **Access the application**
   - Application runs on: `http://localhost:9090`
   - H2 Console: `http://localhost:9090/h2-console`
     - JDBC URL: `jdbc:h2:mem:orderDB`
     - Username: `user`
     - Password: `password`

##  API Endpoints

### Base URL
```
http://localhost:9090/v1
```

### Endpoints

#### 1. Greetings
- **GET** `/v1/`
- **Description**: Simple greeting endpoint
- **Response**: `"Hello"`

#### 2. Create Order
- **POST** `/v1/order`
- **Description**: Creates a new order and updates inventory
- **Request Body**:
  ```json
  {
    "userId": 1,
    "userName": "John Doe",
    "shippingAddress": "123 Main St, City, Country",
    "productId": 101,
    "productName": "Product Name",
    "batchId": 2024,
    "amount": 99.99,
    "quantity": 2
  }
  ```
- **Success Response**: 
  - Status: `200 OK`
  - Body: `"Success"`
- **Error Response**:
  - Status: `404 NOT FOUND`
  - Body: Exception response with error details

##  Database Schema

### Orders Table
- `id`: Primary key (auto-generated)
- `userId`: User identifier
- `userName`: Name of the user
- `status`: Order status (ACTIVE, etc.)
- `paymentStatus`: Payment status (SUCCESS, etc.)
- `totalAmount`: Total order amount
- `shippingAddress`: Delivery address

### OrderItem Table
- `id`: Primary key (auto-generated)
- `productName`: Name of the product
- `productBatchNo`: Batch number
- `quantity`: Quantity ordered
- `price`: Price per unit
- `order`: Foreign key to Orders

##  Configuration

### Application Properties
The application configuration is in `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: OrderClient
  datasource:
    url: jdbc:h2:mem:orderDB
    driverClassName: org.h2.Driver
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.H2Dialect
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 9090
```

### External Service Configuration
The service communicates with an inventory service at:
- Base URL: `http://localhost:9091/v1`
- Endpoint: `/inventory/update`

**Note**: Ensure the inventory service is running on port 9091 before creating orders.

##  Project Structure

```
OrderClient/
├── src/
│   ├── main/
│   │   ├── java/com/example/OrderClient/
│   │   │   ├── API/
│   │   │   │   └── OrderController.java
│   │   │   ├── config/
│   │   │   │   └── SwaggerConfiguration.java
│   │   │   ├── DTO/
│   │   │   │   ├── OrderDTO.java
│   │   │   │   └── ProductDTO.java
│   │   │   ├── Exception/
│   │   │   │   ├── ExceptionResponse.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── OrderFailedException.java
│   │   │   ├── model/
│   │   │   │   ├── ORDER_STATUS.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Orders.java
│   │   │   │   ├── PAYMENT_STATUS.java
│   │   │   │   └── Payment.java
│   │   │   ├── repository/
│   │   │   │   ├── OrderItemRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── PaymentRepository.java
│   │   │   ├── service/
│   │   │   │   ├── OrderService.java
│   │   │   │   └── impl/
│   │   │   │       └── OrderServiceImpl.java
│   │   │   └── OrderClientApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/example/OrderClient/
└── pom.xml
```

##  Testing

Run tests using Maven:
```bash
mvn test
```

##  Monitoring & Health Checks

The application includes Spring Boot Actuator for monitoring. Health check endpoints are available (configuration may need to be enabled in `application.yml`).

##  Exception Handling

The application includes a global exception handler that catches:
- `OrderFailedException`: Returns 404 NOT FOUND with error details

##  Service Integration

This service integrates with:
- **Inventory Service** (port 9091): Updates inventory when orders are created
- **Eureka Server**: For service discovery (if configured)

##  Development Notes

- The application uses H2 in-memory database, so data is lost on restart
- For production, configure a persistent database (PostgreSQL, MySQL, etc.)
- Update the inventory service URL in `OrderServiceImpl.java` if needed
- Ensure the inventory service is running before testing order creation


