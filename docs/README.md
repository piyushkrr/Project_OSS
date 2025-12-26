# Online Shopping System - Documentation

Welcome to the comprehensive documentation for the Online Shopping System. This documentation covers all aspects of the system including architecture, design, workflows, and API specifications.

## 📚 Documentation Index

### 1. [Introduction](./1_Introduction.md)
Comprehensive overview of the Online Shopping System project.

**Contents:**
- Executive Summary
- Project Purpose and Scope
- Core Functionalities (Customer & Admin)
- Technology Stack
- Key Features (Security, Performance, UX)
- System Architecture Overview
- Non-Functional Requirements
- Project Status and Future Enhancements

**Audience**: All stakeholders, new team members, project managers

---

### 2. [UML Diagrams](./2_UML_Diagrams.md)
Complete set of UML diagrams illustrating system design and interactions.

**Contents:**
- **Use Case Diagram**: User and admin interactions
- **Class Diagrams**: Entity models for all services
  - User Auth Service (User, Address, Role)
  - Product Service (Product, Image)
  - Order Service (Order, OrderItem, Cart, CartItem)
  - Payment Service (Payment, Coupon, SavedPaymentMethod)
- **Sequence Diagrams**: Key workflows
  - User Registration and Login
  - Browse and Add to Cart
  - Checkout and Place Order
  - Payment Processing
  - Admin Product Management
- **Component Diagram**: Service architecture and dependencies
- **Activity Diagrams**: Complete shopping flow, admin workflows

**Audience**: Developers, architects, technical leads

---

### 3. [ER Diagram](./3_ER_Diagram.md)
Detailed database schema and entity relationships.

**Contents:**
- Database Overview (Database-per-Service pattern)
- **User Auth Database**: users, addresses tables
- **Product Database**: products, images tables
- **Order Database**: orders, order_items, carts, cart_items tables
- **Payment Database**: payments, saved_payment_methods, coupons tables
- Complete System ER Diagram with cross-service relationships
- Table Specifications (data types, constraints, indexes)
- Data Encryption details
- Database Statistics and Backup Strategy

**Audience**: Database administrators, backend developers, architects

---

### 4. [Workflow Documentation](./4_Workflow.md)
Comprehensive workflow diagrams for all system processes.

**Contents:**
- **User Workflows**:
  - User Registration
  - User Login
  - Product Browsing and Search
  - Shopping Cart Management
  - Checkout and Order Placement
  - Payment Processing
  - Order Tracking
- **Admin Workflows**:
  - Product Management (Create, Update, Delete)
  - Coupon Management
  - Order Management
- **System Workflows**:
  - Service Startup Sequence
  - JWT Authentication Flow
  - Inter-Service Communication
- **Payment Workflows**:
  - Payment Processing (Detailed)
  - Coupon Validation
- **Error Handling Workflows**

**Audience**: Developers, QA engineers, business analysts

---

### 5. [Architecture Documentation](./5_Architecture.md)
Detailed system architecture and design decisions.

**Contents:**
- Architecture Overview and Principles
- Microservices Architecture
  - Service Catalog
  - Service Interaction Diagrams
- Component Details (all 7 services)
- Communication Patterns
  - Synchronous (REST/Feign)
  - Service Discovery (Eureka)
  - API Gateway Pattern
- Data Architecture (Database-per-Service)
- Security Architecture
  - Authentication Flow (JWT)
  - Authorization (RBAC)
  - Security Layers
- Deployment Architecture
  - Current (Development)
  - Planned (Production with Kubernetes)
- Scalability & Performance
- Technology Decisions & Rationale

**Audience**: Architects, senior developers, DevOps engineers

---

### 6. [Swagger/OpenAPI Configuration](./6_Swagger_Configuration.md)
Guide for implementing API documentation using Swagger/OpenAPI.

**Contents:**
- Overview and Benefits
- Maven Dependencies
- Configuration Classes for each service
- Application Properties setup
- Controller Annotations (examples)
- DTO/Model Annotations
- API Documentation Access URLs
- Best Practices
- Implementation Steps
- Troubleshooting Guide

**Audience**: Backend developers, API consumers

---

### 7. [Deployment Guide](./7_Deployment_Guide.md)
Complete guide for running the application manually and using automated scripts.

**Contents:**
- Prerequisites (Java, Maven, MySQL, Node.js, Angular CLI)
- **Manual Startup**: Step-by-step instructions for starting each service
  - MySQL Database
  - Discovery Server
  - API Gateway
  - All Microservices (User Auth, Product, Order, Payment)
  - Frontend (Angular)
- **Automated Startup - Linux/Mac**: Using `start_all.sh` script
  - Script features and execution flow
  - Viewing logs
- **Automated Startup - Windows**: Using `start_services.bat` script
  - Script features and execution flow
  - Viewing logs
- Verification steps and health checks
- Stopping services (manual and automated)
- Troubleshooting common issues
- Quick reference (URLs, commands, credentials)

**Audience**: All developers, DevOps engineers, new team members

---

## 🏗️ System Architecture Quick Reference

```
┌─────────────┐
│   Frontend  │ (Angular - Port 4200)
└──────┬──────┘
       │
       ▼
┌─────────────────┐
│  API Gateway    │ (Port 8181)
└────────┬────────┘
         │
    ┌────┴────────────────────────┐
    │  Discovery Server (Eureka)  │ (Port 8761)
    └────┬────────────────────────┘
         │
    ┌────┴─────────────────────────────────┐
    │                                      │
    ▼                                      ▼
┌───────────────┐                  ┌──────────────┐
│ User Auth     │                  │   Product    │
│ Service       │                  │   Service    │
│ (Port 9090)   │                  │ (Port 8083)  │
└───────────────┘                  └──────────────┘
    │                                      │
    ▼                                      ▼
┌───────────────┐                  ┌──────────────┐
│   Order       │                  │   Payment    │
│   Service     │                  │   Service    │
│ (Port 8084)   │                  │ (Port 8085)  │
└───────────────┘                  └──────────────┘
```

## 🗄️ Database Architecture

- **user_auth_db**: User accounts and addresses
- **product_db**: Product catalog and images
- **order_db**: Orders, order items, shopping carts
- **payment_db**: Payments, saved payment methods, coupons

Each microservice owns its database (Database-per-Service pattern).

## 🔐 Security Features

- **JWT Authentication**: Stateless, secure token-based auth
- **RBAC**: Role-based access control (USER, ADMIN)
- **Data Encryption**: AES-256 for sensitive data
- **BCrypt**: Password hashing
- **CORS**: Configured for frontend origin

## 🚀 Quick Start

### Prerequisites
- Java 17
- Maven 3.8+
- MySQL 8.0+
- Node.js 16+ (for frontend)
- Angular CLI

### Running the Application

```bash
# Start all services
bash start_all.sh

# Or manually:
# 1. Start MySQL
sudo service mysql start

# 2. Build backend
cd backend
mvn clean install -DskipTests

# 3. Start services (in order)
java -jar discovery-server/target/discovery-server-0.0.1-SNAPSHOT.jar &
java -jar api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar &
java -jar user-auth-service/target/user-auth-service-0.0.1-SNAPSHOT.jar &
java -jar product-service/target/product-service-0.0.1-SNAPSHOT.jar &
java -jar order-service/target/order-service-0.0.1-SNAPSHOT.jar &
java -jar payment-service/target/payment-service-0.0.1-SNAPSHOT.jar &

# 4. Start frontend
cd ../frontend
ng serve
```

### Access Points

- **Frontend**: http://localhost:4200
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8181
- **Swagger UI** (after configuration):
  - User Auth: http://localhost:9090/swagger-ui.html
  - Product: http://localhost:8083/swagger-ui.html
  - Order: http://localhost:8084/swagger-ui.html
  - Payment: http://localhost:8085/swagger-ui.html

## 📊 Technology Stack

### Backend
- Spring Boot 3.2.1
- Spring Cloud 2023.0.0
- Java 17
- MySQL 8.0
- Netflix Eureka
- Spring Cloud Gateway
- OpenFeign
- Spring Security + JWT

### Frontend
- Angular (latest)
- TypeScript
- RxJS
- CSS3

### Tools
- Maven
- Git
- Docker (planned)
- Kubernetes (planned)

## 👥 Team Roles

- **Backend Developers**: Microservices development
- **Frontend Developers**: Angular application
- **DevOps Engineers**: Infrastructure and deployment
- **QA Engineers**: Testing and quality assurance
- **Architects**: System design and technical decisions
- **Product Owner**: Requirements and prioritization

## 📝 Contributing

When contributing to documentation:

1. **Keep it Current**: Update docs when code changes
2. **Use Mermaid**: For diagrams (renders in GitHub)
3. **Be Concise**: Clear and to the point
4. **Add Examples**: Code snippets and screenshots
5. **Link Related Docs**: Cross-reference other documentation

## 📞 Support

For questions or issues:
- **Technical Issues**: Check troubleshooting sections in relevant docs
- **Architecture Questions**: Refer to Architecture Documentation
- **API Questions**: Check Swagger documentation
- **Database Questions**: Refer to ER Diagram documentation

## 📄 License

Apache 2.0 License

---

**Documentation Version**: 1.0  
**Last Updated**: December 26, 2025  
**Maintained By**: Development Team

---

## 📖 Reading Guide

### For New Developers
1. Start with [Introduction](./1_Introduction.md)
2. Review [Architecture](./5_Architecture.md)
3. Study [UML Diagrams](./2_UML_Diagrams.md)
4. Understand [Workflows](./4_Workflow.md)
5. Set up [Swagger](./6_Swagger_Configuration.md) for API testing

### For Database Administrators
1. Review [ER Diagram](./3_ER_Diagram.md)
2. Check [Architecture - Data Layer](./5_Architecture.md#5-data-architecture)
3. Understand cross-service data references

### For Frontend Developers
1. Read [Introduction](./1_Introduction.md)
2. Study [Workflows](./4_Workflow.md) - User Workflows
3. Review [Architecture - API Gateway](./5_Architecture.md#32-api-gateway)
4. Check [Swagger](./6_Swagger_Configuration.md) for API contracts

### For DevOps Engineers
1. Review [Architecture - Deployment](./5_Architecture.md#7-deployment-architecture)
2. Understand [Workflows - System Workflows](./4_Workflow.md#3-system-workflows)
3. Check service dependencies in [Architecture](./5_Architecture.md)

---

**Happy Coding! 🚀**
