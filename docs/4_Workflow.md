# Online Shopping System - Workflow Documentation

## Table of Contents
1. [User Workflows](#1-user-workflows)
2. [Admin Workflows](#2-admin-workflows)
3. [System Workflows](#3-system-workflows)
4. [Payment Workflows](#4-payment-workflows)
5. [Error Handling Workflows](#5-error-handling-workflows)

---

## 1. User Workflows

### 1.1 User Registration Workflow

```mermaid
flowchart TD
    Start([User Visits Website]) --> ClickRegister[Click Register]
    ClickRegister --> FillForm[Fill Registration Form<br/>Email, Password, Name]
    FillForm --> Submit[Submit Form]
    
    Submit --> ValidateClient{Client-Side<br/>Validation}
    ValidateClient -->|Invalid| ShowClientError[Show Validation Errors]
    ShowClientError --> FillForm
    
    ValidateClient -->|Valid| SendRequest[POST /api/auth/register]
    SendRequest --> ValidateServer{Server-Side<br/>Validation}
    
    ValidateServer -->|Email Exists| EmailError[Return 409 Conflict]
    EmailError --> ShowEmailError[Show: Email Already Registered]
    ShowEmailError --> FillForm
    
    ValidateServer -->|Invalid Data| ValidationError[Return 400 Bad Request]
    ValidationError --> ShowValidationError[Show Validation Errors]
    ShowValidationError --> FillForm
    
    ValidateServer -->|Valid| HashPassword[Hash Password<br/>BCrypt]
    HashPassword --> SaveUser[Save User to Database]
    SaveUser --> GenerateJWT[Generate JWT Token]
    GenerateJWT --> SendResponse[Return JWT + User Info]
    SendResponse --> StoreToken[Store JWT in localStorage]
    StoreToken --> RedirectHome[Redirect to Home Page]
    RedirectHome --> End([Registration Complete])

    style Start fill:#4CAF50
    style End fill:#4CAF50
    style EmailError fill:#F44336
    style ValidationError fill:#F44336
```

### 1.2 User Login Workflow

```mermaid
flowchart TD
    Start([User on Login Page]) --> EnterCreds[Enter Email & Password]
    EnterCreds --> ClickLogin[Click Login Button]
    ClickLogin --> SendRequest[POST /api/auth/login]
    
    SendRequest --> FindUser{Find User<br/>by Email}
    FindUser -->|Not Found| UserNotFound[Return 401 Unauthorized]
    UserNotFound --> ShowError1[Show: Invalid Credentials]
    ShowError1 --> EnterCreds
    
    FindUser -->|Found| VerifyPassword{Verify Password<br/>BCrypt Compare}
    VerifyPassword -->|Incorrect| WrongPassword[Return 401 Unauthorized]
    WrongPassword --> ShowError2[Show: Invalid Credentials]
    ShowError2 --> EnterCreds
    
    VerifyPassword -->|Correct| GenerateJWT[Generate JWT Token<br/>24hr Expiry]
    GenerateJWT --> SendResponse[Return JWT + User Info]
    SendResponse --> StoreToken[Store JWT in localStorage]
    StoreToken --> SetAuthHeader[Set Authorization Header]
    SetAuthHeader --> RedirectDashboard[Redirect to Dashboard]
    RedirectDashboard --> End([Login Successful])

    style Start fill:#4CAF50
    style End fill:#4CAF50
    style UserNotFound fill:#F44336
    style WrongPassword fill:#F44336
```

### 1.3 Product Browsing and Search Workflow

```mermaid
flowchart TD
    Start([User on Home Page]) --> ViewProducts[GET /api/products]
    ViewProducts --> LoadProducts[Load All Products]
    LoadProducts --> DisplayGrid[Display Product Grid]
    
    DisplayGrid --> UserAction{User Action}
    
    UserAction -->|Search| EnterSearch[Enter Search Query]
    EnterSearch --> SearchAPI[GET /api/products?search=query]
    SearchAPI --> FilterResults[Filter Products by Name/Description]
    FilterResults --> DisplayGrid
    
    UserAction -->|Filter by Category| SelectCategory[Select Category]
    SelectCategory --> FilterAPI[GET /api/products?category=X]
    FilterAPI --> FilterByCategory[Filter Products by Category]
    FilterByCategory --> DisplayGrid
    
    UserAction -->|Sort| SelectSort[Select Sort Option]
    SelectSort --> SortProducts[Sort by Price/Name/Date]
    SortProducts --> DisplayGrid
    
    UserAction -->|View Details| ClickProduct[Click Product Card]
    ClickProduct --> ViewDetail[Navigate to Product Detail Page]
    ViewDetail --> LoadDetail[GET /api/products/{id}]
    LoadDetail --> ShowDetail[Display Product Details<br/>Images, Description, Price, Stock]
    
    ShowDetail --> DetailAction{User Action}
    DetailAction -->|Add to Cart| AddToCart[Add to Cart Workflow]
    DetailAction -->|Back| DisplayGrid
    DetailAction -->|Buy Now| DirectCheckout[Direct to Checkout]
    
    style Start fill:#4CAF50
```

### 1.4 Shopping Cart Workflow

```mermaid
flowchart TD
    Start([User Logged In]) --> AddItem[Click Add to Cart]
    AddItem --> CheckAuth{Authenticated?}
    
    CheckAuth -->|No| RedirectLogin[Redirect to Login]
    RedirectLogin --> LoginFlow[Login Workflow]
    LoginFlow --> AddItem
    
    CheckAuth -->|Yes| SendAdd[POST /api/cart<br/>productId, quantity]
    SendAdd --> GetCart{Cart Exists?}
    
    GetCart -->|No| CreateCart[Create New Cart]
    CreateCart --> AddCartItem[Add Cart Item]
    
    GetCart -->|Yes| CheckItem{Item in Cart?}
    CheckItem -->|Yes| UpdateQty[Update Quantity]
    CheckItem -->|No| AddCartItem
    
    AddCartItem --> SaveCart[Save to Database]
    UpdateQty --> SaveCart
    SaveCart --> ShowSuccess[Show Success Message]
    ShowSuccess --> UpdateBadge[Update Cart Badge Count]
    
    UpdateBadge --> UserChoice{User Choice}
    UserChoice -->|Continue Shopping| Browse[Browse More Products]
    UserChoice -->|View Cart| ViewCart[GET /api/cart]
    
    ViewCart --> DisplayCart[Display Cart Items<br/>Product, Quantity, Price, Subtotal]
    DisplayCart --> CartAction{Cart Action}
    
    CartAction -->|Update Quantity| ChangeQty[Change Quantity Input]
    ChangeQty --> UpdateAPI[PUT /api/cart/items/{id}]
    UpdateAPI --> RecalculateCart[Recalculate Totals]
    RecalculateCart --> DisplayCart
    
    CartAction -->|Remove Item| ClickRemove[Click Remove]
    ClickRemove --> ConfirmRemove{Confirm?}
    ConfirmRemove -->|No| DisplayCart
    ConfirmRemove -->|Yes| RemoveAPI[DELETE /api/cart/items/{id}]
    RemoveAPI --> RecalculateCart
    
    CartAction -->|Proceed to Checkout| Checkout[Checkout Workflow]
    CartAction -->|Continue Shopping| Browse
    
    style Start fill:#4CAF50
```

### 1.5 Checkout and Order Placement Workflow

```mermaid
flowchart TD
    Start([User in Cart]) --> ClickCheckout[Click Proceed to Checkout]
    ClickCheckout --> GetCart[GET /api/cart]
    GetCart --> ValidateCart{Cart Empty?}
    
    ValidateCart -->|Yes| ShowEmpty[Show: Cart is Empty]
    ShowEmpty --> RedirectShop[Redirect to Shop]
    
    ValidateCart -->|No| GetAddresses[GET /api/users/addresses]
    GetAddresses --> ShowAddresses[Display Saved Addresses]
    ShowAddresses --> AddressChoice{Select Address}
    
    AddressChoice -->|Use Saved| SelectSaved[Select Saved Address]
    AddressChoice -->|Add New| AddNewAddress[Fill New Address Form]
    AddNewAddress --> SaveAddress[POST /api/users/addresses]
    SaveAddress --> SelectSaved
    
    SelectSaved --> ReviewOrder[Display Order Summary<br/>Items, Quantities, Prices]
    ReviewOrder --> ShowTotal[Calculate Total Amount]
    ShowTotal --> CouponOption{Apply Coupon?}
    
    CouponOption -->|Yes| EnterCoupon[Enter Coupon Code]
    EnterCoupon --> ValidateCoupon[POST /api/coupons/validate]
    ValidateCoupon --> CouponValid{Coupon Valid?}
    
    CouponValid -->|No| ShowCouponError[Show: Invalid/Expired Coupon]
    ShowCouponError --> ShowTotal
    
    CouponValid -->|Yes| ApplyDiscount[Apply Discount]
    ApplyDiscount --> UpdateTotal[Update Total Amount]
    UpdateTotal --> PlaceOrder
    
    CouponOption -->|No| PlaceOrder[Click Place Order]
    PlaceOrder --> CreateOrder[POST /api/orders]
    CreateOrder --> ValidateStock{Check Stock<br/>for All Items}
    
    ValidateStock -->|Out of Stock| StockError[Return 400: Out of Stock]
    StockError --> ShowStockError[Show: Some Items Out of Stock]
    ShowStockError --> ReviewOrder
    
    ValidateStock -->|Available| GenerateTracking[Generate Order Tracking ID]
    GenerateTracking --> SaveOrder[Save Order to Database]
    SaveOrder --> ClearCart[Clear Shopping Cart]
    ClearCart --> SendEmail[Send Order Confirmation Email]
    SendEmail --> RedirectPayment[Redirect to Payment Page]
    RedirectPayment --> End([Order Placed])

    style Start fill:#4CAF50
    style End fill:#4CAF50
    style StockError fill:#F44336
```

### 1.6 Payment Workflow

```mermaid
flowchart TD
    Start([Order Placed]) --> PaymentPage[Display Payment Page<br/>Order Summary, Total]
    PaymentPage --> PaymentChoice{Payment Method}
    
    PaymentChoice -->|Saved Payment| SelectSaved[GET /api/payments/saved-methods]
    SelectSaved --> DisplaySaved[Display Saved Payment Methods]
    DisplaySaved --> ChooseSaved[Select Payment Method]
    ChooseSaved --> ProcessPayment
    
    PaymentChoice -->|New Payment| EnterPayment[Enter Payment Details<br/>Card Number, CVV, Expiry]
    EnterPayment --> SaveOption{Save for Future?}
    SaveOption -->|Yes| SavePayment[POST /api/payments/saved-methods]
    SavePayment --> ProcessPayment
    SaveOption -->|No| ProcessPayment[POST /api/payments]
    
    ProcessPayment --> ValidatePayment{Validate Payment<br/>Details}
    ValidatePayment -->|Invalid| PaymentError[Return 400: Invalid Payment]
    PaymentError --> ShowPaymentError[Show: Payment Details Invalid]
    ShowPaymentError --> PaymentPage
    
    ValidatePayment -->|Valid| ProcessTransaction[Process Payment<br/>Simulated Gateway]
    ProcessTransaction --> TransactionResult{Transaction<br/>Successful?}
    
    TransactionResult -->|Failed| TransactionFailed[Return 402: Payment Failed]
    TransactionFailed --> ShowTransactionError[Show: Payment Failed<br/>Try Again]
    ShowTransactionError --> PaymentPage
    
    TransactionResult -->|Success| SavePaymentRecord[Save Payment Record]
    SavePaymentRecord --> UpdateOrderStatus[Update Order Status<br/>to CONFIRMED]
    UpdateOrderStatus --> IncrementCoupon{Coupon Used?}
    
    IncrementCoupon -->|Yes| UpdateCouponUsage[Increment Coupon Usage Count]
    UpdateCouponUsage --> SendConfirmation
    IncrementCoupon -->|No| SendConfirmation[Send Payment Confirmation Email]
    
    SendConfirmation --> ShowSuccess[Display Success Page<br/>Order Tracking ID]
    ShowSuccess --> End([Payment Complete])

    style Start fill:#4CAF50
    style End fill:#4CAF50
    style PaymentError fill:#F44336
    style TransactionFailed fill:#F44336
```

### 1.7 Order Tracking Workflow

```mermaid
flowchart TD
    Start([User Logged In]) --> OrdersPage[Navigate to My Orders]
    OrdersPage --> GetOrders[GET /api/orders]
    GetOrders --> DisplayOrders[Display Order List<br/>Tracking ID, Date, Status, Total]
    
    DisplayOrders --> SelectOrder[Click on Order]
    SelectOrder --> GetOrderDetail[GET /api/orders/{id}]
    GetOrderDetail --> ShowOrderDetail[Display Order Details<br/>Items, Quantities, Prices<br/>Shipping Address, Status]
    
    ShowOrderDetail --> StatusCheck{Order Status}
    StatusCheck -->|PENDING| ShowPending[Show: Order Being Processed]
    StatusCheck -->|CONFIRMED| ShowConfirmed[Show: Order Confirmed<br/>Preparing for Shipment]
    StatusCheck -->|SHIPPED| ShowShipped[Show: Order Shipped<br/>Estimated Delivery Date]
    StatusCheck -->|DELIVERED| ShowDelivered[Show: Order Delivered]
    StatusCheck -->|CANCELLED| ShowCancelled[Show: Order Cancelled]
    
    ShowOrderDetail --> ActionChoice{User Action}
    ActionChoice -->|Cancel Order| CheckCancelable{Status = PENDING?}
    CheckCancelable -->|No| ShowCancelError[Show: Cannot Cancel<br/>Order Already Processed]
    CheckCancelable -->|Yes| ConfirmCancel{Confirm<br/>Cancellation?}
    ConfirmCancel -->|No| ShowOrderDetail
    ConfirmCancel -->|Yes| CancelOrder[PUT /api/orders/{id}/cancel]
    CancelOrder --> UpdateStatus[Update Status to CANCELLED]
    UpdateStatus --> RefundPayment[Initiate Refund Process]
    RefundPayment --> SendCancelEmail[Send Cancellation Email]
    SendCancelEmail --> ShowOrderDetail
    
    ActionChoice -->|View Invoice| GenerateInvoice[Generate PDF Invoice]
    GenerateInvoice --> DownloadInvoice[Download Invoice]
    
    ActionChoice -->|Back to Orders| DisplayOrders
    
    style Start fill:#4CAF50
```

---

## 2. Admin Workflows

### 2.1 Product Management Workflow

```mermaid
flowchart TD
    Start([Admin Login]) --> Dashboard[Admin Dashboard]
    Dashboard --> ProductMgmt[Navigate to Product Management]
    ProductMgmt --> Action{Select Action}
    
    Action -->|Create| CreateForm[Click Add New Product]
    CreateForm --> FillDetails[Fill Product Details<br/>Name, Description, Price<br/>Category, Stock]
    FillDetails --> UploadImages[Upload Product Images<br/>Main Image + Additional]
    UploadImages --> SubmitCreate[Submit Create Form]
    SubmitCreate --> ValidateCreate{Validate Input}
    
    ValidateCreate -->|Invalid| ShowCreateError[Show Validation Errors]
    ShowCreateError --> FillDetails
    
    ValidateCreate -->|Valid| ProcessImages[Process and Compress Images]
    ProcessImages --> SaveProduct[POST /api/products]
    SaveProduct --> ProductCreated[Product Created Successfully]
    ProductCreated --> ProductMgmt
    
    Action -->|View/Edit| ListProducts[GET /api/products]
    ListProducts --> DisplayList[Display Product List<br/>with Edit/Delete Options]
    DisplayList --> SelectProduct{Select Product}
    
    SelectProduct -->|Edit| LoadProduct[GET /api/products/{id}]
    LoadProduct --> EditForm[Display Edit Form<br/>Pre-filled with Product Data]
    EditForm --> ModifyDetails[Modify Product Details]
    ModifyDetails --> UpdateImages{Update Images?}
    UpdateImages -->|Yes| UploadNewImages[Upload New Images]
    UploadNewImages --> SubmitUpdate
    UpdateImages -->|No| SubmitUpdate[Submit Update Form]
    SubmitUpdate --> ValidateUpdate{Validate Input}
    
    ValidateUpdate -->|Invalid| ShowUpdateError[Show Validation Errors]
    ShowUpdateError --> EditForm
    
    ValidateUpdate -->|Valid| UpdateProduct[PUT /api/products/{id}]
    UpdateProduct --> ProductUpdated[Product Updated Successfully]
    ProductUpdated --> ProductMgmt
    
    SelectProduct -->|Delete| ConfirmDelete{Confirm<br/>Deletion?}
    ConfirmDelete -->|No| DisplayList
    ConfirmDelete -->|Yes| CheckOrders{Product in<br/>Active Orders?}
    CheckOrders -->|Yes| ShowDeleteWarning[Show: Cannot Delete<br/>Product in Active Orders]
    ShowDeleteWarning --> DisplayList
    CheckOrders -->|No| DeleteProduct[DELETE /api/products/{id}]
    DeleteProduct --> ProductDeleted[Product Deleted Successfully]
    ProductDeleted --> ProductMgmt
    
    style Start fill:#FF5722
    style ProductCreated fill:#4CAF50
    style ProductUpdated fill:#4CAF50
    style ProductDeleted fill:#4CAF50
```

### 2.2 Coupon Management Workflow

```mermaid
flowchart TD
    Start([Admin Dashboard]) --> CouponMgmt[Navigate to Coupon Management]
    CouponMgmt --> Action{Select Action}
    
    Action -->|Create| CreateForm[Click Add New Coupon]
    CreateForm --> FillCoupon[Fill Coupon Details<br/>Code, Description<br/>Discount Type, Value<br/>Min Purchase, Expiry<br/>Usage Limit]
    FillCoupon --> SubmitCreate[Submit Create Form]
    SubmitCreate --> ValidateCreate{Validate Input}
    
    ValidateCreate -->|Invalid| ShowCreateError[Show Validation Errors<br/>- Code must be unique<br/>- Discount value > 0<br/>- Valid expiry date]
    ShowCreateError --> FillCoupon
    
    ValidateCreate -->|Valid| SaveCoupon[POST /api/coupons]
    SaveCoupon --> CouponCreated[Coupon Created Successfully]
    CouponCreated --> CouponMgmt
    
    Action -->|View/Edit| ListCoupons[GET /api/coupons]
    ListCoupons --> DisplayList[Display Coupon List<br/>Code, Discount, Expiry<br/>Usage, Status]
    DisplayList --> SelectCoupon{Select Coupon}
    
    SelectCoupon -->|Edit| LoadCoupon[GET /api/coupons/{id}]
    LoadCoupon --> EditForm[Display Edit Form<br/>Pre-filled with Coupon Data]
    EditForm --> ModifyCoupon[Modify Coupon Details]
    ModifyCoupon --> SubmitUpdate[Submit Update Form]
    SubmitUpdate --> ValidateUpdate{Validate Input}
    
    ValidateUpdate -->|Invalid| ShowUpdateError[Show Validation Errors]
    ShowUpdateError --> EditForm
    
    ValidateUpdate -->|Valid| UpdateCoupon[PUT /api/coupons/{id}]
    UpdateCoupon --> CouponUpdated[Coupon Updated Successfully]
    CouponUpdated --> CouponMgmt
    
    SelectCoupon -->|Delete| ConfirmDelete{Confirm<br/>Deletion?}
    ConfirmDelete -->|No| DisplayList
    ConfirmDelete -->|Yes| DeleteCoupon[DELETE /api/coupons/{id}]
    DeleteCoupon --> CouponDeleted[Coupon Deleted Successfully]
    CouponDeleted --> CouponMgmt
    
    SelectCoupon -->|Toggle Active| ToggleStatus[Toggle Active Status]
    ToggleStatus --> UpdateStatus[PUT /api/coupons/{id}]
    UpdateStatus --> StatusUpdated[Status Updated]
    StatusUpdated --> DisplayList
    
    style Start fill:#FF5722
    style CouponCreated fill:#4CAF50
    style CouponUpdated fill:#4CAF50
    style CouponDeleted fill:#4CAF50
```

### 2.3 Order Management Workflow (Admin)

```mermaid
flowchart TD
    Start([Admin Dashboard]) --> OrderMgmt[Navigate to Order Management]
    OrderMgmt --> GetAllOrders[GET /api/orders/all]
    GetAllOrders --> DisplayOrders[Display All Orders<br/>Tracking ID, User, Date<br/>Status, Total]
    
    DisplayOrders --> FilterOrders{Filter Options}
    FilterOrders -->|By Status| FilterStatus[Filter by PENDING/<br/>CONFIRMED/SHIPPED/<br/>DELIVERED/CANCELLED]
    FilterOrders -->|By Date| FilterDate[Filter by Date Range]
    FilterOrders -->|By User| FilterUser[Filter by User Email]
    FilterStatus --> DisplayOrders
    FilterDate --> DisplayOrders
    FilterUser --> DisplayOrders
    
    DisplayOrders --> SelectOrder[Click on Order]
    SelectOrder --> GetOrderDetail[GET /api/orders/{id}]
    GetOrderDetail --> ShowDetail[Display Order Details<br/>User Info, Items<br/>Shipping Address<br/>Payment Status]
    
    ShowDetail --> AdminAction{Admin Action}
    
    AdminAction -->|Update Status| SelectStatus[Select New Status]
    SelectStatus --> ValidateTransition{Valid Status<br/>Transition?}
    ValidateTransition -->|No| ShowTransitionError[Show: Invalid Status Change]
    ShowTransitionError --> ShowDetail
    ValidateTransition -->|Yes| UpdateStatus[PUT /api/orders/{id}/status]
    UpdateStatus --> SendNotification[Send Status Update Email]
    SendNotification --> StatusUpdated[Status Updated Successfully]
    StatusUpdated --> ShowDetail
    
    AdminAction -->|View Customer| GetCustomer[GET /api/users/{userId}]
    GetCustomer --> ShowCustomer[Display Customer Details]
    ShowCustomer --> ShowDetail
    
    AdminAction -->|Print Invoice| GenerateInvoice[Generate PDF Invoice]
    GenerateInvoice --> PrintInvoice[Print/Download Invoice]
    
    AdminAction -->|Back to List| DisplayOrders
    
    style Start fill:#FF5722
    style StatusUpdated fill:#4CAF50
```

---

## 3. System Workflows

### 3.1 Service Startup Workflow

```mermaid
flowchart TD
    Start([System Startup]) --> CheckMySQL{MySQL<br/>Running?}
    CheckMySQL -->|No| StartMySQL[Start MySQL Service]
    StartMySQL --> MySQLReady
    CheckMySQL -->|Yes| MySQLReady[MySQL Ready]
    
    MySQLReady --> BuildServices[Maven Build<br/>mvn clean install]
    BuildServices --> BuildSuccess{Build<br/>Successful?}
    BuildSuccess -->|No| BuildFailed[Show Build Errors]
    BuildFailed --> End1([Startup Failed])
    
    BuildSuccess -->|Yes| StartEureka[Start Discovery Server<br/>Port 8761]
    StartEureka --> WaitEureka[Wait for Eureka<br/>Health Check]
    WaitEureka --> EurekaReady{Eureka<br/>Ready?}
    EurekaReady -->|No| WaitEureka
    EurekaReady -->|Yes| StartGateway[Start API Gateway<br/>Port 8181]
    
    StartGateway --> RegisterGateway[Register with Eureka]
    RegisterGateway --> WaitGateway[Wait 10 seconds]
    WaitGateway --> StartServices[Start Microservices<br/>Parallel]
    
    StartServices --> StartAuth[Start User Auth Service<br/>Port 9090]
    StartServices --> StartProduct[Start Product Service<br/>Port 8083]
    StartServices --> StartOrder[Start Order Service<br/>Port 8084]
    StartServices --> StartPayment[Start Payment Service<br/>Port 8085]
    
    StartAuth --> RegisterAuth[Register with Eureka]
    StartProduct --> RegisterProduct[Register with Eureka]
    StartOrder --> RegisterOrder[Register with Eureka]
    StartPayment --> RegisterPayment[Register with Eureka]
    
    RegisterAuth --> AllRegistered
    RegisterProduct --> AllRegistered
    RegisterOrder --> AllRegistered
    RegisterPayment --> AllRegistered[All Services Registered]
    
    AllRegistered --> StartFrontend[Start Frontend<br/>ng serve<br/>Port 4200]
    StartFrontend --> FrontendReady[Frontend Ready]
    FrontendReady --> SystemReady[System Fully Operational]
    SystemReady --> End2([Startup Complete])
    
    style Start fill:#4CAF50
    style End1 fill:#F44336
    style End2 fill:#4CAF50
    style SystemReady fill:#4CAF50
```

### 3.2 JWT Authentication Workflow

```mermaid
flowchart TD
    Start([Client Request]) --> HasToken{Request has<br/>JWT Token?}
    HasToken -->|No| PublicEndpoint{Public<br/>Endpoint?}
    PublicEndpoint -->|Yes| AllowAccess[Allow Access]
    PublicEndpoint -->|No| Return401[Return 401 Unauthorized]
    Return401 --> End1([Request Denied])
    
    HasToken -->|Yes| ExtractToken[Extract Token from<br/>Authorization Header]
    ExtractToken --> ValidateFormat{Valid JWT<br/>Format?}
    ValidateFormat -->|No| Return401
    
    ValidateFormat -->|Yes| VerifySignature{Verify JWT<br/>Signature?}
    VerifySignature -->|Invalid| Return401
    
    VerifySignature -->|Valid| CheckExpiry{Token<br/>Expired?}
    CheckExpiry -->|Yes| Return401
    
    CheckExpiry -->|No| ExtractUser[Extract User Info<br/>from Token]
    ExtractUser --> LoadUser[Load User from Database]
    LoadUser --> UserExists{User<br/>Exists?}
    UserExists -->|No| Return401
    
    UserExists -->|Yes| CheckRole{Check Role<br/>Requirements}
    CheckRole -->|Insufficient| Return403[Return 403 Forbidden]
    Return403 --> End2([Access Denied])
    
    CheckRole -->|Authorized| SetContext[Set Security Context<br/>with User Details]
    SetContext --> AllowAccess
    AllowAccess --> ProcessRequest[Process Request]
    ProcessRequest --> End3([Request Successful])
    
    style Start fill:#4CAF50
    style End1 fill:#F44336
    style End2 fill:#F44336
    style End3 fill:#4CAF50
```

### 3.3 Inter-Service Communication Workflow

```mermaid
flowchart TD
    Start([Service A needs data<br/>from Service B]) --> CreateClient[Use Feign Client]
    CreateClient --> QueryEureka[Query Eureka for<br/>Service B Instances]
    QueryEureka --> InstancesFound{Instances<br/>Available?}
    
    InstancesFound -->|No| ServiceUnavailable[Return 503 Service Unavailable]
    ServiceUnavailable --> End1([Request Failed])
    
    InstancesFound -->|Yes| LoadBalance[Client-Side Load Balancing<br/>Select Instance]
    LoadBalance --> BuildRequest[Build HTTP Request<br/>with Headers]
    BuildRequest --> AddJWT{JWT Required?}
    AddJWT -->|Yes| AddAuthHeader[Add Authorization Header]
    AddAuthHeader --> SendRequest
    AddJWT -->|No| SendRequest[Send HTTP Request]
    
    SendRequest --> ReceiveResponse{Response<br/>Status?}
    ReceiveResponse -->|2xx Success| ParseResponse[Parse Response Body]
    ParseResponse --> ReturnData[Return Data to Caller]
    ReturnData --> End2([Request Successful])
    
    ReceiveResponse -->|4xx Client Error| HandleClientError[Log Error<br/>Return Error to Caller]
    HandleClientError --> End3([Client Error])
    
    ReceiveResponse -->|5xx Server Error| HandleServerError[Log Error<br/>Retry Logic Future]
    HandleServerError --> End4([Server Error])
    
    ReceiveResponse -->|Timeout| HandleTimeout[Connection Timeout]
    HandleTimeout --> End5([Timeout Error])
    
    style Start fill:#4CAF50
    style End1 fill:#F44336
    style End2 fill:#4CAF50
    style End3 fill:#FF9800
    style End4 fill:#F44336
    style End5 fill:#F44336
```

---

## 4. Payment Workflows

### 4.1 Payment Processing Workflow (Detailed)

```mermaid
flowchart TD
    Start([Payment Request]) --> ValidateOrder[Validate Order ID]
    ValidateOrder --> OrderExists{Order<br/>Exists?}
    OrderExists -->|No| Return404[Return 404 Not Found]
    Return404 --> End1([Payment Failed])
    
    OrderExists -->|Yes| CheckOrderStatus{Order Status<br/>= PENDING?}
    CheckOrderStatus -->|No| Return400[Return 400 Bad Request<br/>Order Already Processed]
    Return400 --> End1
    
    CheckOrderStatus -->|Yes| ValidateAmount{Payment Amount<br/>= Order Total?}
    ValidateAmount -->|No| Return400
    
    ValidateAmount -->|Yes| ValidatePaymentMethod{Valid Payment<br/>Method?}
    ValidatePaymentMethod -->|No| Return400
    
    ValidatePaymentMethod -->|Yes| GenerateTxnID[Generate Transaction ID]
    GenerateTxnID --> SimulateGateway[Simulate Payment Gateway<br/>Process Payment]
    SimulateGateway --> GatewayResponse{Gateway<br/>Response?}
    
    GatewayResponse -->|Failed| SaveFailedPayment[Save Payment Record<br/>Status: FAILED]
    SaveFailedPayment --> Return402[Return 402 Payment Required]
    Return402 --> End1
    
    GatewayResponse -->|Success| SaveSuccessPayment[Save Payment Record<br/>Status: SUCCESS]
    SaveSuccessPayment --> UpdateOrder[Update Order Status<br/>to CONFIRMED]
    UpdateOrder --> SendEmail[Send Payment Confirmation Email]
    SendEmail --> ReturnSuccess[Return 200 OK<br/>Payment Details]
    ReturnSuccess --> End2([Payment Successful])
    
    style Start fill:#4CAF50
    style End1 fill:#F44336
    style End2 fill:#4CAF50
```

### 4.2 Coupon Validation Workflow

```mermaid
flowchart TD
    Start([Validate Coupon Request]) --> GetCoupon[Find Coupon by Code]
    GetCoupon --> CouponExists{Coupon<br/>Exists?}
    CouponExists -->|No| ReturnInvalid[Return: Invalid Coupon Code]
    ReturnInvalid --> End1([Validation Failed])
    
    CouponExists -->|Yes| CheckActive{Coupon<br/>Active?}
    CheckActive -->|No| ReturnInactive[Return: Coupon Inactive]
    ReturnInactive --> End1
    
    CheckActive -->|Yes| CheckExpiry{Expiry Date<br/>> Today?}
    CheckExpiry -->|No| ReturnExpired[Return: Coupon Expired]
    ReturnExpired --> End1
    
    CheckExpiry -->|Yes| CheckMinPurchase{Order Amount >=<br/>Min Purchase?}
    CheckMinPurchase -->|No| ReturnMinAmount[Return: Minimum Purchase<br/>Not Met]
    ReturnMinAmount --> End1
    
    CheckMinPurchase -->|Yes| CheckUsageLimit{Usage Count <<br/>Usage Limit?}
    CheckUsageLimit -->|No| ReturnLimitReached[Return: Usage Limit Reached]
    ReturnLimitReached --> End1
    
    CheckUsageLimit -->|Yes| CalculateDiscount{Discount<br/>Type?}
    CalculateDiscount -->|PERCENTAGE| CalcPercentage[Discount = Amount * Value / 100]
    CalculateDiscount -->|FIXED| CalcFixed[Discount = Value]
    
    CalcPercentage --> ReturnValid[Return: Valid Coupon<br/>Discount Amount]
    CalcFixed --> ReturnValid
    ReturnValid --> End2([Validation Successful])
    
    style Start fill:#4CAF50
    style End1 fill:#F44336
    style End2 fill:#4CAF50
```

---

## 5. Error Handling Workflows

### 5.1 Global Error Handling Workflow

```mermaid
flowchart TD
    Start([Exception Thrown]) --> ExceptionType{Exception<br/>Type?}
    
    ExceptionType -->|ValidationException| Return400[Return 400 Bad Request<br/>Validation Error Details]
    ExceptionType -->|AuthenticationException| Return401[Return 401 Unauthorized<br/>Invalid Credentials]
    ExceptionType -->|AccessDeniedException| Return403[Return 403 Forbidden<br/>Insufficient Permissions]
    ExceptionType -->|ResourceNotFoundException| Return404[Return 404 Not Found<br/>Resource Not Found]
    ExceptionType -->|DuplicateResourceException| Return409[Return 409 Conflict<br/>Resource Already Exists]
    ExceptionType -->|PaymentException| Return402[Return 402 Payment Required<br/>Payment Failed]
    ExceptionType -->|ServiceUnavailableException| Return503[Return 503 Service Unavailable<br/>Service Down]
    ExceptionType -->|Other| Return500[Return 500 Internal Server Error<br/>Unexpected Error]
    
    Return400 --> LogError[Log Error Details]
    Return401 --> LogError
    Return403 --> LogError
    Return404 --> LogError
    Return409 --> LogError
    Return402 --> LogError
    Return503 --> LogError
    Return500 --> LogError
    
    LogError --> SendResponse[Send Error Response<br/>JSON Format]
    SendResponse --> End([Error Handled])
    
    style Start fill:#F44336
    style End fill:#FF9800
```

---

**Document Version**: 1.0  
**Last Updated**: December 26, 2025  
**Maintained By**: Development Team
