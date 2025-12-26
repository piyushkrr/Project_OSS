# Online Shopping System - UML Diagrams

## Table of Contents
1. [Use Case Diagram](#1-use-case-diagram)
2. [Class Diagrams](#2-class-diagrams)
3. [Sequence Diagrams](#3-sequence-diagrams)
4. [Component Diagram](#4-component-diagram)
5. [Activity Diagrams](#5-activity-diagrams)

---

## 1. Use Case Diagram

### 1.1 Overall System Use Cases

```mermaid
graph TB
    subgraph "Online Shopping System"
        UC1[Browse Products]
        UC2[Search Products]
        UC3[View Product Details]
        UC4[Register Account]
        UC5[Login]
        UC6[Manage Profile]
        UC7[Add to Cart]
        UC8[View Cart]
        UC9[Place Order]
        UC10[Make Payment]
        UC11[Track Order]
        UC12[View Order History]
        UC13[Manage Addresses]
        UC14[Apply Coupon]
        UC15[Save Payment Method]
        
        UC16[Manage Products]
        UC17[Manage Coupons]
        UC18[View All Orders]
    end

    Customer((Customer))
    Admin((Admin))

    Customer --> UC1
    Customer --> UC2
    Customer --> UC3
    Customer --> UC4
    Customer --> UC5
    Customer --> UC6
    Customer --> UC7
    Customer --> UC8
    Customer --> UC9
    Customer --> UC10
    Customer --> UC11
    Customer --> UC12
    Customer --> UC13
    Customer --> UC14
    Customer --> UC15

    Admin --> UC5
    Admin --> UC16
    Admin --> UC17
    Admin --> UC18

    UC9 -.includes.-> UC7
    UC10 -.includes.-> UC9
    UC14 -.extends.-> UC10

    style Customer fill:#4CAF50
    style Admin fill:#FF5722
```

---

## 2. Class Diagrams

### 2.1 User Auth Service - Class Diagram

```mermaid
classDiagram
    class User {
        -Long id
        -String email
        -String password
        -String firstName
        -String lastName
        -String phoneNumber
        -String address
        -Role role
        +getAuthorities() Collection~GrantedAuthority~
        +getUsername() String
        +isAccountNonExpired() boolean
        +isAccountNonLocked() boolean
        +isCredentialsNonExpired() boolean
        +isEnabled() boolean
    }

    class Address {
        -Long id
        -Long userId
        -String addressLine1
        -String addressLine2
        -String city
        -String state
        -String zipCode
        -String country
        -boolean isDefault
    }

    class Role {
        <<enumeration>>
        USER
        ADMIN
    }

    class AuthenticationService {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -JwtUtil jwtUtil
        +register(RegisterRequest) AuthenticationResponse
        +login(LoginRequest) AuthenticationResponse
        +validateToken(String) boolean
    }

    class JwtUtil {
        -String SECRET_KEY
        -long EXPIRATION_TIME
        +generateToken(UserDetails) String
        +extractUsername(String) String
        +validateToken(String, UserDetails) boolean
        +extractExpiration(String) Date
    }

    class UserController {
        -AuthenticationService authService
        +getCurrentUser() User
        +updateProfile(User) User
    }

    class AddressController {
        -AddressService addressService
        +getAddresses() List~Address~
        +addAddress(Address) Address
        +setDefaultAddress(Long) Address
        +deleteAddress(Long) void
    }

    User "1" -- "1" Role
    User "1" -- "*" Address : has
    AuthenticationService --> User
    AuthenticationService --> JwtUtil
    UserController --> AuthenticationService
    AddressController --> Address
```

### 2.2 Product Service - Class Diagram

```mermaid
classDiagram
    class Product {
        -Long id
        -String name
        -String description
        -BigDecimal price
        -String category
        -Integer stockQuantity
        -byte[] mainImage
        -String imageType
        -String mainImageUrl
        -List~Image~ images
        -Double rating
        -Boolean isPopular
        -LocalDateTime createdAt
        +getMainImageUrl() String
        +onCreate() void
    }

    class Image {
        -Long id
        -byte[] imageData
        -String imageType
        -String imageUrl
    }

    class ProductService {
        -ProductRepository productRepository
        -ImageRepository imageRepository
        +getAllProducts() List~Product~
        +getProductById(Long) Product
        +createProduct(Product, MultipartFile) Product
        +updateProduct(Long, Product) Product
        +deleteProduct(Long) void
        +getProductImage(Long) byte[]
        +searchProducts(String) List~Product~
    }

    class ProductController {
        -ProductService productService
        +getAllProducts() List~ProductDTO~
        +getProductById(Long) ProductDTO
        +createProduct(ProductDTO) ProductDTO
        +updateProduct(Long, ProductDTO) ProductDTO
        +deleteProduct(Long) void
        +getProductImage(Long) ResponseEntity
    }

    class ProductDTO {
        -Long id
        -String name
        -String description
        -BigDecimal price
        -String category
        -Integer stockQuantity
        -String mainImageUrl
        -Double rating
        -Boolean isPopular
    }

    Product "1" -- "*" Image : contains
    ProductService --> Product
    ProductService --> Image
    ProductController --> ProductService
    ProductController --> ProductDTO
```

### 2.3 Order Service - Class Diagram

```mermaid
classDiagram
    class Order {
        -Long id
        -String orderTrackingId
        -Long userId
        -List~OrderItem~ orderItems
        -BigDecimal totalAmount
        -OrderStatus status
        -String shippingAddress
        -String phoneNumber
        -LocalDateTime createdAt
        -UserDTO user
    }

    class OrderItem {
        -Long id
        -Order order
        -Long productId
        -String productName
        -Integer quantity
        -BigDecimal price
        -BigDecimal subtotal
    }

    class Cart {
        -Long id
        -Long userId
        -List~CartItem~ cartItems
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class CartItem {
        -Long id
        -Cart cart
        -Long productId
        -String productName
        -Integer quantity
        -BigDecimal price
    }

    class OrderStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        SHIPPED
        DELIVERED
        CANCELLED
    }

    class OrderService {
        -OrderRepository orderRepository
        -CartService cartService
        -ProductClient productClient
        -UserAuthClient userAuthClient
        -EmailService emailService
        +placeOrder(PlaceOrderRequest) Order
        +getOrderById(Long) Order
        +getUserOrders(Long) List~Order~
        +updateOrderStatus(Long, OrderStatus) Order
        +cancelOrder(Long) void
    }

    class CartService {
        -CartRepository cartRepository
        -CartItemRepository cartItemRepository
        -ProductClient productClient
        +getCart(Long) Cart
        +addToCart(Long, CartItemRequest) Cart
        +updateCartItem(Long, Long, Integer) Cart
        +removeFromCart(Long, Long) void
        +clearCart(Long) void
    }

    Order "1" -- "*" OrderItem : contains
    Order "1" -- "1" OrderStatus
    Cart "1" -- "*" CartItem : contains
    OrderService --> Order
    OrderService --> CartService
    CartService --> Cart
```

### 2.4 Payment Service - Class Diagram

```mermaid
classDiagram
    class Payment {
        -Long id
        -Long orderId
        -String paymentMethod
        -String transactionId
        -BigDecimal amount
        -String status
        -LocalDateTime paymentDate
    }

    class SavedPaymentMethod {
        -Long id
        -Long userId
        -String provider
        -String maskedNumber
        -String expiryDate
        -boolean isDefault
    }

    class Coupon {
        -Long id
        -String code
        -String description
        -String discountType
        -BigDecimal discountValue
        -BigDecimal minPurchaseAmount
        -LocalDate expiryDate
        -Integer usageLimit
        -Integer usedCount
        -boolean active
        +isValid() boolean
        +incrementUsage() void
    }

    class PaymentService {
        -PaymentRepository paymentRepository
        -SavedPaymentMethodRepository savedPaymentMethodRepository
        -OrderClient orderClient
        +processPayment(PaymentRequest) Payment
        +getPaymentByOrderId(Long) Payment
        +savePaymentMethod(SavedPaymentMethod) SavedPaymentMethod
        +getSavedPaymentMethods(Long) List~SavedPaymentMethod~
    }

    class CouponService {
        -CouponRepository couponRepository
        +validateCoupon(String, BigDecimal) CouponValidationResponse
        +createCoupon(Coupon) Coupon
        +updateCoupon(Long, Coupon) Coupon
        +deleteCoupon(Long) void
        +getAllCoupons() List~Coupon~
    }

    class PaymentController {
        -PaymentService paymentService
        +processPayment(PaymentRequest) Payment
        +getPaymentByOrderId(Long) Payment
    }

    class CouponController {
        -CouponService couponService
        +validateCoupon(String, BigDecimal) CouponValidationResponse
        +createCoupon(Coupon) Coupon
        +updateCoupon(Long, Coupon) Coupon
        +deleteCoupon(Long) void
        +getAllCoupons() List~Coupon~
    }

    PaymentService --> Payment
    PaymentService --> SavedPaymentMethod
    CouponService --> Coupon
    PaymentController --> PaymentService
    CouponController --> CouponService
```

---

## 3. Sequence Diagrams

### 3.1 User Registration and Login

```mermaid
sequenceDiagram
    actor User
    participant Frontend
    participant Gateway as API Gateway
    participant Auth as Auth Service
    participant DB as Database

    Note over User,DB: Registration Flow
    User->>Frontend: Enter registration details
    Frontend->>Gateway: POST /api/auth/register
    Gateway->>Auth: POST /auth/register
    Auth->>Auth: Validate input
    Auth->>Auth: Hash password (BCrypt)
    Auth->>DB: Save user
    DB-->>Auth: User saved
    Auth->>Auth: Generate JWT token
    Auth-->>Gateway: JWT + User info
    Gateway-->>Frontend: JWT + User info
    Frontend->>Frontend: Store JWT in localStorage
    Frontend-->>User: Registration successful

    Note over User,DB: Login Flow
    User->>Frontend: Enter email & password
    Frontend->>Gateway: POST /api/auth/login
    Gateway->>Auth: POST /auth/login
    Auth->>DB: Find user by email
    DB-->>Auth: User data
    Auth->>Auth: Verify password
    Auth->>Auth: Generate JWT token
    Auth-->>Gateway: JWT + User info
    Gateway-->>Frontend: JWT + User info
    Frontend->>Frontend: Store JWT in localStorage
    Frontend-->>User: Login successful
```

### 3.2 Browse and Add to Cart

```mermaid
sequenceDiagram
    actor User
    participant Frontend
    participant Gateway as API Gateway
    participant Product as Product Service
    participant Order as Order Service
    participant Auth as Auth Service
    participant DB as Database

    Note over User,DB: Browse Products
    User->>Frontend: Visit shop page
    Frontend->>Gateway: GET /api/products
    Gateway->>Product: GET /products
    Product->>DB: Query all products
    DB-->>Product: Product list
    Product-->>Gateway: Product list with images
    Gateway-->>Frontend: Product list
    Frontend-->>User: Display products

    Note over User,DB: Add to Cart
    User->>Frontend: Click "Add to Cart"
    Frontend->>Gateway: POST /api/cart<br/>Authorization: Bearer {JWT}
    Gateway->>Gateway: Validate JWT
    Gateway->>Auth: Extract user from JWT
    Auth-->>Gateway: User ID
    Gateway->>Order: POST /cart
    Order->>Product: GET /products/{id}
    Product-->>Order: Product details
    Order->>DB: Find or create cart
    DB-->>Order: Cart
    Order->>DB: Add cart item
    DB-->>Order: Cart updated
    Order-->>Gateway: Cart with items
    Gateway-->>Frontend: Cart updated
    Frontend-->>User: Item added to cart
```

### 3.3 Checkout and Place Order

```mermaid
sequenceDiagram
    actor User
    participant Frontend
    participant Gateway as API Gateway
    participant Order as Order Service
    participant Product as Product Service
    participant Auth as Auth Service
    participant Email as Email Service
    participant DB as Database

    User->>Frontend: Click "Checkout"
    Frontend->>Gateway: GET /api/cart<br/>Authorization: Bearer {JWT}
    Gateway->>Order: GET /cart
    Order->>DB: Get cart items
    DB-->>Order: Cart with items
    Order-->>Gateway: Cart details
    Gateway-->>Frontend: Cart details
    Frontend-->>User: Show checkout page

    User->>Frontend: Enter shipping details
    User->>Frontend: Click "Place Order"
    Frontend->>Gateway: POST /api/orders<br/>Authorization: Bearer {JWT}
    Gateway->>Order: POST /orders
    
    loop For each cart item
        Order->>Product: GET /products/{id}
        Product-->>Order: Validate product & stock
    end

    Order->>Auth: GET /users/{userId}
    Auth-->>Order: User details

    Order->>DB: Create order
    Order->>DB: Create order items
    Order->>DB: Clear cart
    DB-->>Order: Order created

    Order->>Email: Send order confirmation
    Email-->>Order: Email sent

    Order-->>Gateway: Order details
    Gateway-->>Frontend: Order confirmation
    Frontend-->>User: Order placed successfully
```

### 3.4 Payment Processing

```mermaid
sequenceDiagram
    actor User
    participant Frontend
    participant Gateway as API Gateway
    participant Payment as Payment Service
    participant Order as Order Service
    participant Coupon as Coupon Service
    participant DB as Database

    User->>Frontend: Enter payment details
    
    opt Apply Coupon
        User->>Frontend: Enter coupon code
        Frontend->>Gateway: POST /api/coupons/validate
        Gateway->>Coupon: Validate coupon
        Coupon->>DB: Check coupon validity
        DB-->>Coupon: Coupon details
        Coupon->>Coupon: Validate expiry & usage
        Coupon-->>Gateway: Discount amount
        Gateway-->>Frontend: Discount applied
        Frontend-->>User: Show discounted total
    end

    User->>Frontend: Click "Pay Now"
    Frontend->>Gateway: POST /api/payments<br/>Authorization: Bearer {JWT}
    Gateway->>Payment: POST /payments
    
    Payment->>Order: GET /orders/{orderId}
    Order-->>Payment: Order details

    Payment->>Payment: Process payment (simulated)
    Payment->>DB: Save payment record
    DB-->>Payment: Payment saved

    opt Coupon was applied
        Payment->>Coupon: Increment coupon usage
        Coupon->>DB: Update coupon usage count
    end

    Payment->>Order: PATCH /orders/{id}/status
    Order->>DB: Update order status to CONFIRMED
    DB-->>Order: Order updated
    Order-->>Payment: Order confirmed

    Payment-->>Gateway: Payment successful
    Gateway-->>Frontend: Payment confirmation
    Frontend-->>User: Payment successful
```

### 3.5 Admin - Manage Products

```mermaid
sequenceDiagram
    actor Admin
    participant Frontend
    participant Gateway as API Gateway
    participant Product as Product Service
    participant DB as Database

    Note over Admin,DB: Create Product
    Admin->>Frontend: Fill product form + upload image
    Frontend->>Gateway: POST /api/products<br/>Authorization: Bearer {JWT}
    Gateway->>Gateway: Validate JWT & ADMIN role
    Gateway->>Product: POST /products
    Product->>Product: Process image
    Product->>DB: Save product
    DB-->>Product: Product saved
    Product-->>Gateway: Product created
    Gateway-->>Frontend: Success
    Frontend-->>Admin: Product created

    Note over Admin,DB: Update Product
    Admin->>Frontend: Edit product details
    Frontend->>Gateway: PUT /api/products/{id}<br/>Authorization: Bearer {JWT}
    Gateway->>Gateway: Validate JWT & ADMIN role
    Gateway->>Product: PUT /products/{id}
    Product->>DB: Update product
    DB-->>Product: Product updated
    Product-->>Gateway: Product updated
    Gateway-->>Frontend: Success
    Frontend-->>Admin: Product updated

    Note over Admin,DB: Delete Product
    Admin->>Frontend: Click delete
    Frontend->>Gateway: DELETE /api/products/{id}<br/>Authorization: Bearer {JWT}
    Gateway->>Gateway: Validate JWT & ADMIN role
    Gateway->>Product: DELETE /products/{id}
    Product->>DB: Delete product
    DB-->>Product: Product deleted
    Product-->>Gateway: Success
    Gateway-->>Frontend: Success
    Frontend-->>Admin: Product deleted
```

---

## 4. Component Diagram

```mermaid
graph TB
    subgraph "Client Layer"
        WEB[Web Browser<br/>Angular SPA]
    end

    subgraph "API Gateway Layer"
        GW[API Gateway<br/>Spring Cloud Gateway<br/>Port 8181]
    end

    subgraph "Service Discovery"
        EUREKA[Eureka Server<br/>Netflix Eureka<br/>Port 8761]
    end

    subgraph "Microservices Layer"
        subgraph "User Auth Service - Port 9090"
            AUTH_CTRL[Controllers<br/>AuthController<br/>UserController<br/>AddressController]
            AUTH_SVC[Services<br/>AuthenticationService<br/>AddressService<br/>EmailService]
            AUTH_REPO[Repositories<br/>UserRepository<br/>AddressRepository]
            AUTH_ENTITY[Entities<br/>User<br/>Address]
        end

        subgraph "Product Service - Port 8083"
            PROD_CTRL[Controllers<br/>ProductController]
            PROD_SVC[Services<br/>ProductService]
            PROD_REPO[Repositories<br/>ProductRepository<br/>ImageRepository]
            PROD_ENTITY[Entities<br/>Product<br/>Image]
        end

        subgraph "Order Service - Port 8084"
            ORD_CTRL[Controllers<br/>OrderController<br/>CartController]
            ORD_SVC[Services<br/>OrderService<br/>CartService<br/>EmailService]
            ORD_REPO[Repositories<br/>OrderRepository<br/>CartRepository]
            ORD_ENTITY[Entities<br/>Order<br/>OrderItem<br/>Cart<br/>CartItem]
            ORD_CLIENT[Feign Clients<br/>ProductClient<br/>UserAuthClient]
        end

        subgraph "Payment Service - Port 8085"
            PAY_CTRL[Controllers<br/>PaymentController<br/>CouponController<br/>SavedPaymentController]
            PAY_SVC[Services<br/>PaymentService<br/>CouponService]
            PAY_REPO[Repositories<br/>PaymentRepository<br/>CouponRepository<br/>SavedPaymentRepository]
            PAY_ENTITY[Entities<br/>Payment<br/>Coupon<br/>SavedPaymentMethod]
            PAY_CLIENT[Feign Clients<br/>OrderClient]
        end
    end

    subgraph "Data Layer"
        DB_AUTH[(MySQL<br/>user_auth_db)]
        DB_PROD[(MySQL<br/>product_db)]
        DB_ORD[(MySQL<br/>order_db)]
        DB_PAY[(MySQL<br/>payment_db)]
    end

    subgraph "External Services"
        SMTP[Email Service<br/>SMTP Server]
    end

    WEB --> GW
    GW --> EUREKA
    GW --> AUTH_CTRL
    GW --> PROD_CTRL
    GW --> ORD_CTRL
    GW --> PAY_CTRL

    AUTH_CTRL --> AUTH_SVC
    AUTH_SVC --> AUTH_REPO
    AUTH_REPO --> AUTH_ENTITY
    AUTH_ENTITY --> DB_AUTH
    AUTH_SVC --> SMTP

    PROD_CTRL --> PROD_SVC
    PROD_SVC --> PROD_REPO
    PROD_REPO --> PROD_ENTITY
    PROD_ENTITY --> DB_PROD

    ORD_CTRL --> ORD_SVC
    ORD_SVC --> ORD_REPO
    ORD_REPO --> ORD_ENTITY
    ORD_ENTITY --> DB_ORD
    ORD_SVC --> ORD_CLIENT
    ORD_CLIENT -.-> PROD_CTRL
    ORD_CLIENT -.-> AUTH_CTRL
    ORD_SVC --> SMTP

    PAY_CTRL --> PAY_SVC
    PAY_SVC --> PAY_REPO
    PAY_REPO --> PAY_ENTITY
    PAY_ENTITY --> DB_PAY
    PAY_SVC --> PAY_CLIENT
    PAY_CLIENT -.-> ORD_CTRL

    AUTH_CTRL --> EUREKA
    PROD_CTRL --> EUREKA
    ORD_CTRL --> EUREKA
    PAY_CTRL --> EUREKA

    style WEB fill:#4CAF50
    style GW fill:#2196F3
    style EUREKA fill:#9C27B0
```

---

## 5. Activity Diagrams

### 5.1 Complete Shopping Flow

```mermaid
flowchart TD
    Start([User Visits Website]) --> Browse[Browse Products]
    Browse --> Search{Search/Filter?}
    Search -->|Yes| ApplyFilter[Apply Filters]
    Search -->|No| ViewProduct[View Product Details]
    ApplyFilter --> ViewProduct
    
    ViewProduct --> AddCart{Add to Cart?}
    AddCart -->|No| Browse
    AddCart -->|Yes| CheckAuth{Authenticated?}
    
    CheckAuth -->|No| Login[Login/Register]
    Login --> AddToCart[Add Item to Cart]
    CheckAuth -->|Yes| AddToCart
    
    AddToCart --> ContinueShopping{Continue Shopping?}
    ContinueShopping -->|Yes| Browse
    ContinueShopping -->|No| ViewCart[View Cart]
    
    ViewCart --> ModifyCart{Modify Cart?}
    ModifyCart -->|Yes| UpdateCart[Update Quantities/Remove Items]
    UpdateCart --> ViewCart
    ModifyCart -->|No| Checkout[Proceed to Checkout]
    
    Checkout --> EnterShipping[Enter Shipping Address]
    EnterShipping --> ReviewOrder[Review Order]
    ReviewOrder --> ApplyCoupon{Apply Coupon?}
    ApplyCoupon -->|Yes| ValidateCoupon[Validate Coupon Code]
    ValidateCoupon --> CouponValid{Coupon Valid?}
    CouponValid -->|No| ReviewOrder
    CouponValid -->|Yes| ApplyDiscount[Apply Discount]
    ApplyDiscount --> SelectPayment
    ApplyCoupon -->|No| SelectPayment[Select Payment Method]
    
    SelectPayment --> SavedPayment{Use Saved Payment?}
    SavedPayment -->|Yes| ProcessPayment[Process Payment]
    SavedPayment -->|No| EnterPayment[Enter Payment Details]
    EnterPayment --> SaveOption{Save Payment Method?}
    SaveOption -->|Yes| SavePaymentMethod[Save Payment Method]
    SaveOption -->|No| ProcessPayment
    SavePaymentMethod --> ProcessPayment
    
    ProcessPayment --> PaymentSuccess{Payment Successful?}
    PaymentSuccess -->|No| PaymentFailed[Show Error Message]
    PaymentFailed --> SelectPayment
    PaymentSuccess -->|Yes| CreateOrder[Create Order]
    
    CreateOrder --> SendEmail[Send Confirmation Email]
    SendEmail --> ClearCart[Clear Shopping Cart]
    ClearCart --> ShowConfirmation[Show Order Confirmation]
    ShowConfirmation --> TrackOrder{Track Order?}
    TrackOrder -->|Yes| OrderTracking[View Order Status]
    TrackOrder -->|No| End([End])
    OrderTracking --> End

    style Start fill:#4CAF50
    style End fill:#F44336
    style ProcessPayment fill:#FF9800
    style CreateOrder fill:#2196F3
```

### 5.2 Admin Product Management Flow

```mermaid
flowchart TD
    Start([Admin Login]) --> Dashboard[Admin Dashboard]
    Dashboard --> Action{Select Action}
    
    Action -->|Create Product| CreateForm[Fill Product Form]
    CreateForm --> UploadImage[Upload Product Images]
    UploadImage --> SetDetails[Set Price, Stock, Category]
    SetDetails --> SubmitCreate[Submit New Product]
    SubmitCreate --> ValidateCreate{Validation OK?}
    ValidateCreate -->|No| ShowError1[Show Validation Errors]
    ShowError1 --> CreateForm
    ValidateCreate -->|Yes| SaveProduct[Save Product to DB]
    SaveProduct --> Success1[Show Success Message]
    Success1 --> Dashboard
    
    Action -->|View Products| ListProducts[Display Product List]
    ListProducts --> SelectProduct{Select Product}
    SelectProduct -->|Edit| EditForm[Load Product Details]
    EditForm --> ModifyDetails[Modify Product Info]
    ModifyDetails --> SubmitEdit[Submit Changes]
    SubmitEdit --> ValidateEdit{Validation OK?}
    ValidateEdit -->|No| ShowError2[Show Validation Errors]
    ShowError2 --> EditForm
    ValidateEdit -->|Yes| UpdateProduct[Update Product in DB]
    UpdateProduct --> Success2[Show Success Message]
    Success2 --> Dashboard
    
    SelectProduct -->|Delete| ConfirmDelete{Confirm Deletion?}
    ConfirmDelete -->|No| ListProducts
    ConfirmDelete -->|Yes| DeleteProduct[Delete Product from DB]
    DeleteProduct --> Success3[Show Success Message]
    Success3 --> Dashboard
    
    Action -->|Manage Coupons| CouponMgmt[Coupon Management]
    CouponMgmt --> Dashboard
    
    Action -->|View Orders| OrderMgmt[Order Management]
    OrderMgmt --> Dashboard
    
    Action -->|Logout| End([End])

    style Start fill:#4CAF50
    style End fill:#F44336
    style SaveProduct fill:#2196F3
    style UpdateProduct fill:#2196F3
    style DeleteProduct fill:#F44336
```

---

**Document Version**: 1.0  
**Last Updated**: December 26, 2025  
**Maintained By**: Development Team
