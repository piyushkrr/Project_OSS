# Online Shopping System - Deployment & Startup Guide

## Table of Contents
1. [Prerequisites](#1-prerequisites)
2. [Manual Startup (Step-by-Step)](#2-manual-startup-step-by-step)
3. [Automated Startup - Linux/Mac](#3-automated-startup-linuxmac)
4. [Automated Startup - Windows](#4-automated-startup-windows)
5. [Verification](#5-verification)
6. [Stopping Services](#6-stopping-services)
7. [Troubleshooting](#7-troubleshooting)

---

## 1. Prerequisites

### 1.1 Required Software

| Software | Version | Purpose |
|----------|---------|---------|
| **Java JDK** | 17 or higher | Backend services |
| **Maven** | 3.8+ | Build tool |
| **MySQL** | 8.0+ | Database |
| **Node.js** | 16+ | Frontend runtime |
| **Angular CLI** | Latest | Frontend build tool |
| **Git** | Latest | Version control |

### 1.2 Verify Installations

```bash
# Check Java version
java -version
# Expected: openjdk version "17.x.x" or higher

# Check Maven version
mvn -version
# Expected: Apache Maven 3.8.x or higher

# Check MySQL
mysql --version
# Expected: mysql Ver 8.0.x

# Check Node.js
node --version
# Expected: v16.x.x or higher

# Check Angular CLI
ng version
# Expected: Angular CLI: 15.x.x or higher
```

### 1.3 Port Availability

Ensure the following ports are available:

| Port | Service |
|------|---------|
| 3306 | MySQL Database |
| 4200 | Angular Frontend |
| 8761 | Discovery Server (Eureka) |
| 8181 | API Gateway |
| 9090 | User Auth Service |
| 8083 | Product Service |
| 8084 | Order Service |
| 8085 | Payment Service |

**Check port availability:**
```bash
# Linux/Mac
netstat -tuln | grep -E ":(3306|4200|8761|8181|9090|8083|8084|8085)"

# Windows
netstat -ano | findstr "3306 4200 8761 8181 9090 8083 8084 8085"
```

---

## 2. Manual Startup (Step-by-Step)

### 2.1 Start MySQL Database

#### Linux/Mac
```bash
# Start MySQL service
sudo service mysql start

# Or using systemctl
sudo systemctl start mysql

# Verify MySQL is running
sudo service mysql status
```

#### Windows
```cmd
# Start MySQL service
net start MySQL80

# Or using Services GUI
# Press Win+R, type "services.msc", find MySQL80, and start it
```

**Verify MySQL Connection:**
```bash
mysql -u root -p
# Enter your MySQL root password
# If successful, you'll see the MySQL prompt: mysql>
# Type 'exit' to quit
```

---

### 2.2 Build Backend Services

Navigate to the backend directory and build all services:

```bash
# Navigate to project root
cd /home/labuser/Downloads/Project_OSS

# Navigate to backend directory
cd backend

# Clean and build all services (skip tests for faster build)
mvn clean install -DskipTests

# Wait for build to complete
# Expected output: BUILD SUCCESS
# Total time: ~1-2 minutes
```

**Expected Output:**
```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for Online Shopping System Parent 0.0.1-SNAPSHOT:
[INFO] 
[INFO] Online Shopping System Parent ...................... SUCCESS
[INFO] Shopping Service ................................... SUCCESS
[INFO] Discovery Server ................................... SUCCESS
[INFO] API Gateway ........................................ SUCCESS
[INFO] User Auth Service .................................. SUCCESS
[INFO] Product Service .................................... SUCCESS
[INFO] Order Service ...................................... SUCCESS
[INFO] Payment Service .................................... SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

### 2.3 Start Discovery Server (Eureka)

**Terminal 1:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Start Discovery Server
java -jar backend/discovery-server/target/discovery-server-0.0.1-SNAPSHOT.jar

# Wait for startup message:
# "Started DiscoveryServerApplication in X.XXX seconds"
```

**Verify Eureka is running:**
- Open browser: http://localhost:8761
- You should see the Eureka Dashboard
- **Keep this terminal running**

---

### 2.4 Start API Gateway

**Terminal 2:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Wait 10-15 seconds after Eureka starts, then start API Gateway
java -jar backend/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar

# Wait for startup message:
# "Started ApiGatewayApplication in X.XXX seconds"
```

**Verify API Gateway:**
- Check Eureka Dashboard: http://localhost:8761
- You should see "API-GATEWAY" registered
- **Keep this terminal running**

---

### 2.5 Start User Auth Service

**Terminal 3:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Start User Auth Service
java -jar backend/user-auth-service/target/user-auth-service-0.0.1-SNAPSHOT.jar

# Wait for startup message:
# "Started UserAuthServiceApplication in X.XXX seconds"
```

**Verify Registration:**
- Check Eureka Dashboard: http://localhost:8761
- You should see "USER-AUTH-SERVICE" registered
- **Keep this terminal running**

---

### 2.6 Start Product Service

**Terminal 4:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Start Product Service
java -jar backend/product-service/target/product-service-0.0.1-SNAPSHOT.jar

# Wait for startup message:
# "Started ProductServiceApplication in X.XXX seconds"
```

**Verify Registration:**
- Check Eureka Dashboard: http://localhost:8761
- You should see "PRODUCT-SERVICE" registered
- **Keep this terminal running**

---

### 2.7 Start Order Service

**Terminal 5:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Start Order Service
java -jar backend/order-service/target/order-service-0.0.1-SNAPSHOT.jar

# Wait for startup message:
# "Started OrderServiceApplication in X.XXX seconds"
```

**Verify Registration:**
- Check Eureka Dashboard: http://localhost:8761
- You should see "ORDER-SERVICE" registered
- **Keep this terminal running**

---

### 2.8 Start Payment Service

**Terminal 6:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Start Payment Service
java -jar backend/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar

# Wait for startup message:
# "Started PaymentServiceApplication in X.XXX seconds"
```

**Verify Registration:**
- Check Eureka Dashboard: http://localhost:8761
- You should see "PAYMENT-SERVICE" registered
- **Keep this terminal running**

---

### 2.9 Start Frontend (Angular)

**Terminal 7:**
```bash
# From project root
cd /home/labuser/Downloads/Project_OSS

# Navigate to frontend directory
cd frontend

# Install dependencies (first time only)
npm install

# Start Angular development server
ng serve

# Wait for compilation:
# "Application bundle generation complete."
# "Local: http://localhost:4200/"
```

**Verify Frontend:**
- Open browser: http://localhost:4200
- You should see the Online Shopping System homepage
- **Keep this terminal running**

---

### 2.10 Manual Startup Summary

**Total Terminals Required:** 7 (or use background processes)

**Startup Order:**
1. MySQL Database
2. Discovery Server (Eureka) - Port 8761
3. API Gateway - Port 8181
4. User Auth Service - Port 9090
5. Product Service - Port 8083
6. Order Service - Port 8084
7. Payment Service - Port 8085
8. Frontend (Angular) - Port 4200

**Total Startup Time:** ~3-5 minutes

---

## 3. Automated Startup - Linux/Mac

### 3.1 Using start_all.sh Script

The project includes an automated startup script for Linux/Mac systems.

**Script Location:** `/home/labuser/Downloads/Project_OSS/start_all.sh`

### 3.2 Script Features

- ✅ Checks MySQL status before starting
- ✅ Builds all backend services
- ✅ Starts services in correct order
- ✅ Waits for Eureka to be ready
- ✅ Runs services in background
- ✅ Logs output to `logs/` directory
- ✅ Starts frontend development server

### 3.3 Running the Script

```bash
# Navigate to project root
cd /home/labuser/Downloads/Project_OSS

# Make script executable (first time only)
chmod +x start_all.sh

# Run the script
bash start_all.sh
```

### 3.4 Script Execution Flow

```
1. Check MySQL Status
   ├─ If running → Continue
   └─ If not running → Exit with error message

2. Build Backend Services
   ├─ Run: mvn clean install -DskipTests
   ├─ If successful → Continue
   └─ If failed → Exit with error message

3. Create logs directory
   └─ mkdir -p logs

4. Start Discovery Server
   ├─ Run in background with nohup
   ├─ Wait for health check (http://localhost:8761)
   └─ Continue when ready

5. Start API Gateway
   ├─ Run in background with nohup
   └─ Wait 10 seconds for registration

6. Start Microservices (Parallel)
   ├─ User Auth Service
   ├─ Product Service
   ├─ Order Service
   └─ Payment Service

7. Start Frontend
   ├─ Check if port 4200 is available
   ├─ Run: ng serve in background
   └─ Output to logs/frontend.log

8. Complete
   └─ Display "All services initiated."
```

### 3.5 Script Output

```bash
Checking MySQL status...
MySQL is running.
Building Backend Services...
[INFO] BUILD SUCCESS
[INFO] Total time: 01:16 min

Starting Discovery Server...
Waiting for Discovery Server to start...
Discovery Server started.

Starting API Gateway...
API Gateway started.

Starting User Auth Service...
Starting Product Service...
Starting Order Service...
Starting Payment Service...
Backend Services Started. Check 'logs/' directory for output.

Starting Frontend...
Frontend starting on port 4200...
All services initiated.
```

### 3.6 Viewing Logs

All service logs are saved in the `logs/` directory:

```bash
# View all log files
ls -lh logs/

# View Discovery Server logs
tail -f logs/discovery-server.log

# View API Gateway logs
tail -f logs/api-gateway.log

# View User Auth Service logs
tail -f logs/user-auth-service.log

# View Product Service logs
tail -f logs/product-service.log

# View Order Service logs
tail -f logs/order-service.log

# View Payment Service logs
tail -f logs/payment-service.log

# View Frontend logs
tail -f logs/frontend.log

# View all logs simultaneously
tail -f logs/*.log
```

---

## 4. Automated Startup - Windows

### 4.1 Using start_services.bat Script

The project includes an automated startup script for Windows systems.

**Script Location:** `/home/labuser/Downloads/Project_OSS/start_services.bat`

### 4.2 Script Features

- ✅ Checks MySQL status before starting
- ✅ Builds all backend services
- ✅ Starts services in correct order
- ✅ Waits for Eureka to be ready
- ✅ Runs services in background using `start` command
- ✅ Logs output to `logs\` directory
- ✅ Starts frontend development server

### 4.3 Running the Script

**Option 1: Double-click**
1. Navigate to project folder in File Explorer
2. Double-click `start_services.bat`
3. A command prompt window will open and execute the script

**Option 2: Command Prompt**
```cmd
REM Navigate to project root
cd C:\path\to\Project_OSS

REM Run the script
start_services.bat
```

**Option 3: PowerShell**
```powershell
# Navigate to project root
cd C:\path\to\Project_OSS

# Run the script
.\start_services.bat
```

### 4.4 Script Execution Flow

```
1. Check MySQL Status
   ├─ Run: sc query MySQL80
   ├─ If running → Continue
   └─ If not running → Exit with error message

2. Build Backend Services
   ├─ Run: mvn clean install -DskipTests
   ├─ If successful → Continue
   └─ If failed → Exit with error message

3. Create logs directory
   └─ mkdir logs (if not exists)

4. Start Discovery Server
   ├─ Run in new window: start "Discovery Server" java -jar ...
   ├─ Wait for health check
   └─ Continue when ready

5. Start API Gateway
   ├─ Run in new window: start "API Gateway" java -jar ...
   └─ Wait 10 seconds for registration

6. Start Microservices (Parallel)
   ├─ User Auth Service (new window)
   ├─ Product Service (new window)
   ├─ Order Service (new window)
   └─ Payment Service (new window)

7. Start Frontend
   ├─ Check if port 4200 is available
   ├─ Run: start "Frontend" ng serve
   └─ Output to logs\frontend.log

8. Complete
   └─ Display "All services initiated."
```

### 4.5 Script Output

```
Checking MySQL status...
MySQL is running.

Building Backend Services...
[INFO] BUILD SUCCESS
[INFO] Total time: 01:16 min

Starting Discovery Server...
Waiting for Discovery Server to start...
Discovery Server started.

Starting API Gateway...
API Gateway started.

Starting User Auth Service...
Starting Product Service...
Starting Order Service...
Starting Payment Service...
Backend Services Started. Check 'logs\' directory for output.

Starting Frontend...
Frontend starting on port 4200...
All services initiated.
Press any key to continue . . .
```

### 4.6 Viewing Logs (Windows)

All service logs are saved in the `logs\` directory:

```cmd
REM View log files in File Explorer
explorer logs

REM View Discovery Server logs
type logs\discovery-server.log

REM View API Gateway logs
type logs\api-gateway.log

REM View User Auth Service logs
type logs\user-auth-service.log

REM View Product Service logs
type logs\product-service.log

REM View Order Service logs
type logs\order-service.log

REM View Payment Service logs
type logs\payment-service.log

REM View Frontend logs
type logs\frontend.log
```

**Using PowerShell:**
```powershell
# View logs with tail equivalent
Get-Content logs\discovery-server.log -Wait -Tail 50
```

---

## 5. Verification

### 5.1 Check All Services are Running

**Linux/Mac:**
```bash
# Check Java processes
ps aux | grep -E "discovery-server|api-gateway|user-auth-service|product-service|order-service|payment-service" | grep -v grep

# Check Node process (Frontend)
ps aux | grep "ng serve" | grep -v grep

# Check listening ports
netstat -tuln | grep -E ":(8761|8181|9090|8083|8084|8085|4200)"
```

**Windows:**
```cmd
REM Check Java processes
tasklist | findstr java

REM Check Node process
tasklist | findstr node

REM Check listening ports
netstat -ano | findstr "8761 8181 9090 8083 8084 8085 4200"
```

### 5.2 Verify Service Registration

**Eureka Dashboard:**
1. Open browser: http://localhost:8761
2. Check "Instances currently registered with Eureka"
3. You should see:
   - API-GATEWAY
   - USER-AUTH-SERVICE
   - PRODUCT-SERVICE
   - ORDER-SERVICE
   - PAYMENT-SERVICE

### 5.3 Test API Gateway

```bash
# Test API Gateway health
curl http://localhost:8181/actuator/health

# Expected response:
# {"status":"UP"}
```

### 5.4 Test Frontend

1. Open browser: http://localhost:4200
2. You should see the homepage
3. Try browsing products
4. Try logging in (if you have test users)

### 5.5 Service Health Checks

**Discovery Server:**
```bash
curl http://localhost:8761
# Should return Eureka dashboard HTML
```

**API Gateway:**
```bash
curl http://localhost:8181/actuator/health
# Should return: {"status":"UP"}
```

**Product Service (via Gateway):**
```bash
curl http://localhost:8181/api/products
# Should return product list JSON
```

**User Auth Service (via Gateway):**
```bash
curl -X POST http://localhost:8181/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'
# Should return JWT token or 401 if user doesn't exist
```

---

## 6. Stopping Services

### 6.1 Manual Shutdown

If you started services manually in separate terminals:

1. Press `Ctrl+C` in each terminal window
2. Stop in reverse order:
   - Frontend
   - Payment Service
   - Order Service
   - Product Service
   - User Auth Service
   - API Gateway
   - Discovery Server
   - MySQL (optional)

### 6.2 Automated Shutdown - Linux/Mac

```bash
# Kill all Java processes (backend services)
pkill -f "discovery-server|api-gateway|user-auth-service|product-service|order-service|payment-service"

# Kill Angular dev server
pkill -f "ng serve"

# Or kill all Java and Node processes (use with caution)
killall java
killall node

# Stop MySQL (optional)
sudo service mysql stop
```

**Create a stop script (stop_all.sh):**
```bash
#!/bin/bash

echo "Stopping all services..."

# Stop frontend
pkill -f "ng serve"
echo "Frontend stopped."

# Stop backend services
pkill -f "payment-service"
pkill -f "order-service"
pkill -f "product-service"
pkill -f "user-auth-service"
pkill -f "api-gateway"
pkill -f "discovery-server"
echo "Backend services stopped."

# Clean up log files (optional)
# rm -rf logs/*.log

echo "All services stopped."
```

### 6.3 Automated Shutdown - Windows

**Create a stop script (stop_services.bat):**
```batch
@echo off
echo Stopping all services...

REM Stop Java processes
taskkill /F /FI "IMAGENAME eq java.exe"
echo Backend services stopped.

REM Stop Node processes
taskkill /F /FI "IMAGENAME eq node.exe"
echo Frontend stopped.

REM Stop MySQL (optional)
REM net stop MySQL80

echo All services stopped.
pause
```

**Run the stop script:**
```cmd
stop_services.bat
```

---

## 7. Troubleshooting

### 7.1 MySQL Not Running

**Error:**
```
MySQL is NOT running. Please start it using 'sudo service mysql start'.
```

**Solution:**
```bash
# Linux/Mac
sudo service mysql start

# Windows
net start MySQL80
```

### 7.2 Build Failures

**Error:**
```
[ERROR] Failed to execute goal ... compilation failure
```

**Solutions:**
1. Check Java version: `java -version` (must be 17+)
2. Clean Maven cache: `mvn clean`
3. Update dependencies: `mvn dependency:resolve`
4. Check for missing files in source directories

### 7.3 Port Already in Use

**Error:**
```
Port 8761 is already in use
```

**Solution:**

**Linux/Mac:**
```bash
# Find process using the port
lsof -i :8761

# Kill the process
kill -9 <PID>
```

**Windows:**
```cmd
REM Find process using the port
netstat -ano | findstr :8761

REM Kill the process
taskkill /PID <PID> /F
```

### 7.4 Service Not Registering with Eureka

**Symptoms:**
- Service starts but doesn't appear in Eureka dashboard

**Solutions:**
1. Wait 30-60 seconds (registration takes time)
2. Check service logs for errors
3. Verify Eureka URL in service's `application.properties`
4. Restart the service

### 7.5 Frontend Build Errors

**Error:**
```
An unhandled exception occurred: Cannot find module '@angular/...'
```

**Solution:**
```bash
# Delete node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Reinstall dependencies
npm install

# Try starting again
ng serve
```

### 7.6 Database Connection Errors

**Error:**
```
Could not open JDBC Connection for transaction
```

**Solutions:**
1. Verify MySQL is running
2. Check database credentials in `application.properties`
3. Ensure databases exist:
   ```sql
   CREATE DATABASE IF NOT EXISTS user_auth_db;
   CREATE DATABASE IF NOT EXISTS product_db;
   CREATE DATABASE IF NOT EXISTS order_db;
   CREATE DATABASE IF NOT EXISTS payment_db;
   ```

### 7.7 Out of Memory Errors

**Error:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solution:**
```bash
# Increase JVM heap size
java -Xmx2g -jar backend/service-name/target/service-name.jar
```

### 7.8 Checking Service Logs

If a service fails to start, check its log file:

```bash
# Linux/Mac
tail -100 logs/service-name.log

# Windows
type logs\service-name.log
```

---

## 8. Quick Reference

### 8.1 Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:4200 | Main application |
| **Eureka Dashboard** | http://localhost:8761 | Service registry |
| **API Gateway** | http://localhost:8181 | API entry point |
| **User Auth Service** | http://localhost:9090 | Direct access (dev only) |
| **Product Service** | http://localhost:8083 | Direct access (dev only) |
| **Order Service** | http://localhost:8084 | Direct access (dev only) |
| **Payment Service** | http://localhost:8085 | Direct access (dev only) |

### 8.2 Common Commands

```bash
# Build backend
cd backend && mvn clean install -DskipTests

# Start all services (Linux/Mac)
bash start_all.sh

# Start all services (Windows)
start_services.bat

# Check running services
ps aux | grep java

# View logs
tail -f logs/*.log

# Stop all services (Linux/Mac)
pkill -f "java|ng serve"
```

### 8.3 Default Credentials

**Admin User:**
- Email: `admin@example.com`
- Password: `admin123`

**Test User:**
- Email: `user@example.com`
- Password: `user123`

*(Check data seeder classes for actual credentials)*

---

**Document Version**: 1.0  
**Last Updated**: December 26, 2025  
**Maintained By**: Development Team
