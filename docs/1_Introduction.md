# Online Shopping System (OSS) - Project Introduction

## 1. Executive Summary

The **Online Shopping System (OSS)** is a comprehensive, enterprise-grade e-commerce platform designed for a global retail giant. Built on a modern microservices architecture, the system provides a scalable, secure, and high-performance solution for online retail operations, supporting millions of users and handling thousands of concurrent transactions.

## 2. Project Purpose

This project aims to deliver a full-featured online shopping platform that enables:
- **Customers** to browse products, manage shopping carts, place orders, and track deliveries
- **Administrators** to manage inventory, process orders, handle customer service, and analyze business metrics
- **Business** to scale globally while maintaining high availability and security standards

## 3. Project Scope

### 3.1 Core Functionalities

#### Customer-Facing Features
- **User Management**: Registration, authentication (JWT-based), profile management, and multi-factor authentication
- **Product Catalog**: Advanced search, filtering, categorization, product reviews, and ratings
- **Shopping Experience**: Shopping cart management, wishlist functionality, and personalized recommendations
- **Order Management**: Seamless checkout process, order tracking, and order history
- **Payment Processing**: Multiple payment methods (credit/debit cards, digital wallets), secure transactions, and coupon support
- **Address Management**: Multiple shipping addresses with validation

#### Administrative Features
- **Inventory Management**: Product CRUD operations, stock management, and category organization
- **Order Processing**: Order fulfillment, status updates, and cancellation handling
- **Coupon Management**: Create, edit, and manage promotional coupons
- **User Management**: User administration and role-based access control (RBAC)
- **Analytics**: Order analytics, user behavior tracking, and business intelligence

### 3.2 Technical Scope

The system implements a **microservices architecture** with the following services:

1. **Discovery Server (Eureka)** - Service registry and discovery
2. **API Gateway** - Single entry point, routing, and load balancing
3. **User Auth Service** - Authentication, authorization, and user management
4. **Product Service** - Product catalog and inventory management
5. **Order Service** - Order processing, cart management, and order tracking
6. **Payment Service** - Payment processing, saved payment methods, and coupon validation
7. **Frontend Application** - Angular-based responsive web interface

## 4. Technology Stack

### 4.1 Backend Technologies
- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Microservices**: Spring Cloud 2023.0.0
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Security**: Spring Security with JWT authentication
- **Database**: MySQL (relational data)
- **ORM**: Hibernate/JPA
- **Connection Pooling**: HikariCP
- **Inter-Service Communication**: OpenFeign
- **Build Tool**: Maven

### 4.2 Frontend Technologies
- **Framework**: Angular (latest version)
- **Language**: TypeScript
- **Styling**: CSS3
- **HTTP Client**: Angular HttpClient
- **State Management**: RxJS

### 4.3 Infrastructure & DevOps
- **Containerization**: Docker (planned)
- **Orchestration**: Kubernetes (planned)
- **Version Control**: Git
- **CI/CD**: Jenkins/GitHub Actions (planned)
- **Monitoring**: Prometheus & Grafana (planned)

## 5. Key Features

### 5.1 Security Features
- **JWT-based Authentication**: Stateless, secure token-based authentication
- **Role-Based Access Control (RBAC)**: USER and ADMIN roles with granular permissions
- **Data Encryption**: Sensitive data (phone numbers, addresses, payment info) encrypted at rest
- **Password Security**: BCrypt password hashing
- **CORS Configuration**: Secure cross-origin resource sharing
- **SQL Injection Protection**: Parameterized queries via JPA

### 5.2 Performance Features
- **Microservices Architecture**: Independent scaling of services
- **Connection Pooling**: Optimized database connections
- **Lazy Loading**: Efficient data fetching strategies
- **Caching**: Strategic caching for frequently accessed data
- **Load Balancing**: Client-side load balancing via Eureka

### 5.3 User Experience Features
- **Responsive Design**: Mobile-first, adaptive UI
- **Real-time Updates**: Live order status tracking
- **Email Notifications**: Order confirmations and updates
- **Image Management**: Product images with multiple views
- **Search & Filter**: Advanced product discovery
- **Wishlist**: Save products for later purchase

## 6. System Architecture Overview

### 6.1 Microservices Pattern
The system follows a **decomposition by business capability** pattern, where each service owns its domain and database:

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

### 6.2 Database Architecture
- **Database per Service**: Each microservice has its own MySQL database
- **Data Isolation**: Services do not directly access other services' databases
- **Inter-Service Communication**: RESTful APIs via Feign clients

## 7. Non-Functional Requirements

### 7.1 Performance
- Support **10,000+ concurrent users**
- API response time < 200ms for 95% of requests
- Page load time < 2 seconds
- Handle **100,000 transactions per hour** during peak periods

### 7.2 Scalability
- Horizontal scaling of all microservices
- Auto-scaling based on demand
- Support for 10 million+ products
- Regional deployment capability

### 7.3 Availability & Reliability
- **99.99% uptime** target
- Failover mechanisms for critical components
- Comprehensive disaster recovery plan
- Zero-downtime deployments

### 7.4 Security
- TLS 1.3 encryption for data in transit
- Encryption for sensitive data at rest
- PCI DSS compliance for payment processing
- Regular security audits
- Protection against OWASP Top 10 vulnerabilities

## 8. Project Status

### Current Implementation
✅ Microservices architecture fully implemented  
✅ User authentication and authorization  
✅ Product catalog with image management  
✅ Shopping cart functionality  
✅ Order management and tracking  
✅ Payment processing with multiple methods  
✅ Coupon system  
✅ Email notifications  
✅ Admin panel for coupon management  
✅ Responsive frontend application  

### Future Enhancements
- Product reviews and ratings system
- Advanced analytics dashboard
- Mobile application (React Native)
- Real-time chat support
- Recommendation engine
- Multi-language support
- Advanced search with Elasticsearch

## 9. Target Audience

### Primary Users
- **Customers**: End-users purchasing products online
- **Administrators**: Staff managing inventory, orders, and system configuration
- **System Administrators**: DevOps team managing infrastructure

### Secondary Stakeholders
- **Business Analysts**: Analyzing sales data and user behavior
- **Customer Support**: Handling customer inquiries and issues
- **Marketing Team**: Managing promotions and coupons

## 10. Success Criteria

The project is considered successful when it achieves:
- ✅ All core features implemented and tested
- ✅ System handles target load (10,000 concurrent users)
- ✅ Security audit passed with no critical vulnerabilities
- ✅ 99.99% uptime maintained for 30 consecutive days
- User satisfaction rating > 4.5/5
- Average page load time < 2 seconds
- Successful processing of 100,000+ transactions per day

## 11. Development Team

- **Backend Developers**: Java/Spring Boot microservices
- **Frontend Developers**: Angular application
- **DevOps Engineers**: Infrastructure and deployment
- **QA Engineers**: Testing and quality assurance
- **Security Specialists**: Security implementation and auditing
- **Product Owner**: Requirements and prioritization

## 12. Contact & Support

For technical inquiries, bug reports, or feature requests, please contact the development team through the project repository or designated communication channels.

---

**Document Version**: 1.0  
**Last Updated**: December 26, 2025  
**Status**: Active Development
