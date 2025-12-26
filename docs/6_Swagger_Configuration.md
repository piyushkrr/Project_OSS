# Online Shopping System - Swagger/OpenAPI Documentation

## Table of Contents
1. [Overview](#1-overview)
2. [Dependencies](#2-dependencies)
3. [Configuration](#3-configuration)
4. [Service-Specific Setup](#4-service-specific-setup)
5. [API Documentation Access](#5-api-documentation-access)
6. [Best Practices](#6-best-practices)

---

## 1. Overview

This document provides instructions for adding Swagger/OpenAPI documentation to all microservices in the Online Shopping System. Swagger provides interactive API documentation that allows developers to explore and test API endpoints.

### Benefits
- **Interactive API Testing**: Test endpoints directly from the browser
- **Automatic Documentation**: Generate docs from code annotations
- **API Discovery**: Easy exploration of available endpoints
- **Client SDK Generation**: Generate client libraries automatically

---

## 2. Dependencies

### 2.1 Maven Dependencies

Add the following dependencies to each microservice's `pom.xml`:

```xml
<!-- Springdoc OpenAPI (Swagger) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

> **Note**: We're using `springdoc-openapi` instead of the older `springfox` as it has better support for Spring Boot 3.x.

### 2.2 Update POM Files

#### User Auth Service - pom.xml
Location: `/home/labuser/Downloads/Project_OSS/backend/user-auth-service/pom.xml`

Add after existing dependencies:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

#### Product Service - pom.xml
Location: `/home/labuser/Downloads/Project_OSS/backend/product-service/pom.xml`

Add after existing dependencies:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

#### Order Service - pom.xml
Location: `/home/labuser/Downloads/Project_OSS/backend/order-service/pom.xml`

Add after existing dependencies:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

#### Payment Service - pom.xml
Location: `/home/labuser/Downloads/Project_OSS/backend/payment-service/pom.xml`

Add after existing dependencies:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

---

## 3. Configuration

### 3.1 Application Properties

Add Swagger configuration to each service's `application.properties`:

```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.show-actuator=false
```

### 3.2 OpenAPI Configuration Class

Create a configuration class for each service to customize the API documentation.

#### User Auth Service Configuration

Create file: `backend/user-auth-service/src/main/java/com/oss/auth/config/OpenApiConfig.java`

```java
package com.oss.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userAuthServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Auth Service API")
                        .description("Authentication and User Management Service for Online Shopping System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@onlineshopping.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:9090").description("Local Development"),
                        new Server().url("http://localhost:8181").description("API Gateway")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token authentication")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
```

#### Product Service Configuration

Create file: `backend/product-service/src/main/java/com/oss/product/config/OpenApiConfig.java`

```java
package com.oss.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .description("Product Catalog and Inventory Management Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@onlineshopping.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8083").description("Local Development"),
                        new Server().url("http://localhost:8181").description("API Gateway")));
    }
}
```

#### Order Service Configuration

Create file: `backend/order-service/src/main/java/com/oss/order/config/OpenApiConfig.java`

```java
package com.oss.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("Order and Shopping Cart Management Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@onlineshopping.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8084").description("Local Development"),
                        new Server().url("http://localhost:8181").description("API Gateway")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
```

#### Payment Service Configuration

Create file: `backend/payment-service/src/main/java/com/oss/payment/config/OpenApiConfig.java`

```java
package com.oss.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payment Service API")
                        .description("Payment Processing and Coupon Management Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@onlineshopping.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8085").description("Local Development"),
                        new Server().url("http://localhost:8181").description("API Gateway")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
```

---

## 4. Service-Specific Setup

### 4.1 Controller Annotations

Add Swagger annotations to your controllers for better documentation.

#### Example: Product Controller

```java
package com.oss.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Management", description = "APIs for managing products in the catalog")
public class ProductController {

    @Operation(
        summary = "Get all products",
        description = "Retrieve a list of all products in the catalog"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved products",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        )
    })
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        // Implementation
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieve a specific product by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found"
        )
    })
    @GetMapping("/{id}")
    public ProductDTO getProductById(
        @Parameter(description = "Product ID", required = true)
        @PathVariable Long id
    ) {
        // Implementation
    }

    @Operation(
        summary = "Create new product",
        description = "Create a new product (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin access required"
        )
    })
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        // Implementation
    }
}
```

#### Example: Auth Controller

```java
package com.oss.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and registration APIs")
public class AuthController {

    @Operation(
        summary = "User registration",
        description = "Register a new user account"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already registered"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        )
    })
    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
        // Implementation
    }

    @Operation(
        summary = "User login",
        description = "Authenticate user and receive JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
        )
    })
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        // Implementation
    }
}
```

### 4.2 DTO/Model Annotations

Add schema annotations to DTOs for better API documentation.

```java
package com.oss.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Product Data Transfer Object")
public class ProductDTO {

    @Schema(description = "Product ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Product name", example = "Laptop", required = true)
    private String name;

    @Schema(description = "Product description", example = "High-performance laptop")
    private String description;

    @Schema(description = "Product price", example = "999.99", required = true)
    private BigDecimal price;

    @Schema(description = "Product category", example = "Electronics")
    private String category;

    @Schema(description = "Available stock quantity", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Product image URL", example = "http://localhost:8181/api/products/1/image")
    private String mainImageUrl;

    @Schema(description = "Product rating", example = "4.5")
    private Double rating;

    @Schema(description = "Is product popular", example = "true")
    private Boolean isPopular;

    // Getters and setters
}
```

---

## 5. API Documentation Access

### 5.1 Direct Service Access

After implementing Swagger, each service will have its own documentation available at:

| Service | Swagger UI URL | OpenAPI JSON |
|---------|----------------|--------------|
| **User Auth Service** | http://localhost:9090/swagger-ui.html | http://localhost:9090/api-docs |
| **Product Service** | http://localhost:8083/swagger-ui.html | http://localhost:8083/api-docs |
| **Order Service** | http://localhost:8084/swagger-ui.html | http://localhost:8084/api-docs |
| **Payment Service** | http://localhost:8085/swagger-ui.html | http://localhost:8085/api-docs |

### 5.2 Access via API Gateway

For production use, access through API Gateway:

| Service | Gateway Swagger UI URL |
|---------|------------------------|
| **User Auth Service** | http://localhost:8181/auth/swagger-ui.html |
| **Product Service** | http://localhost:8181/products/swagger-ui.html |
| **Order Service** | http://localhost:8181/orders/swagger-ui.html |
| **Payment Service** | http://localhost:8181/payments/swagger-ui.html |

> **Note**: API Gateway routing configuration may need to be updated to properly route Swagger UI requests.

### 5.3 Testing with JWT

For endpoints requiring authentication:

1. **Login** via `/auth/login` endpoint
2. **Copy JWT token** from the response
3. **Click "Authorize"** button in Swagger UI
4. **Enter**: `Bearer {your-jwt-token}`
5. **Click "Authorize"** to apply
6. **Test protected endpoints**

---

## 6. Best Practices

### 6.1 Documentation Guidelines

1. **Use Descriptive Summaries**: Clearly describe what each endpoint does
2. **Include Examples**: Provide example values for parameters and responses
3. **Document Error Codes**: List all possible HTTP status codes
4. **Group Related Endpoints**: Use `@Tag` to organize endpoints
5. **Mark Required Fields**: Use `required = true` for mandatory parameters
6. **Hide Internal Endpoints**: Use `@Hidden` annotation for internal APIs

### 6.2 Security Considerations

```java
// Exclude Swagger endpoints from security
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

### 6.3 Production Considerations

For production environments, consider:

1. **Disable Swagger UI**: Set `springdoc.swagger-ui.enabled=false`
2. **Restrict Access**: Require authentication for API docs
3. **Use API Keys**: Implement API key authentication
4. **Rate Limiting**: Apply rate limits to documentation endpoints
5. **CORS Configuration**: Properly configure CORS for Swagger UI

```properties
# Production Configuration
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

---

## 7. Implementation Steps

### Step 1: Add Dependencies
Add Springdoc OpenAPI dependency to all service `pom.xml` files.

### Step 2: Create Configuration Classes
Create `OpenApiConfig.java` in each service's config package.

### Step 3: Update Application Properties
Add Swagger configuration to `application.properties`.

### Step 4: Annotate Controllers
Add `@Tag`, `@Operation`, and `@ApiResponse` annotations to controllers.

### Step 5: Annotate DTOs
Add `@Schema` annotations to DTO classes.

### Step 6: Update Security Configuration
Permit access to Swagger endpoints in security configuration.

### Step 7: Build and Test
```bash
cd backend
mvn clean install
bash ../start_all.sh
```

### Step 8: Verify Documentation
Visit each service's Swagger UI URL and verify documentation is displayed correctly.

---

## 8. Example API Documentation Screenshots

### Expected Swagger UI Features:

1. **API Overview**: List of all endpoints grouped by tags
2. **Endpoint Details**: Method, path, parameters, request body
3. **Try It Out**: Interactive testing of endpoints
4. **Schemas**: Model definitions with field descriptions
5. **Authentication**: JWT token input for secured endpoints
6. **Responses**: Example responses with status codes

---

## 9. Troubleshooting

### Common Issues:

**Issue**: Swagger UI not loading
- **Solution**: Check if dependency is correctly added and service is running

**Issue**: Endpoints not showing
- **Solution**: Verify controller package is scanned by Spring Boot

**Issue**: JWT authentication not working
- **Solution**: Ensure security configuration permits Swagger endpoints

**Issue**: 404 on /swagger-ui.html
- **Solution**: Try /swagger-ui/index.html (newer versions)

---

## 10. Additional Resources

- **Springdoc OpenAPI Documentation**: https://springdoc.org/
- **OpenAPI Specification**: https://swagger.io/specification/
- **Swagger UI**: https://swagger.io/tools/swagger-ui/

---

**Document Version**: 1.0  
**Last Updated**: December 26, 2025  
**Maintained By**: Development Team

**Note**: This is a configuration guide. Actual implementation requires adding dependencies, creating configuration files, and annotating controllers as described above.
