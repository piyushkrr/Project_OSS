@echo off
setlocal

:: Function to check if a port is in use (simulated using netstat)
:: Usage: call :check_port port_number
:: Returns: errorlevel 0 if in use, 1 if not
goto :main

:check_port
netstat -an | find ":%1" | find "LISTENING" >nul
exit /b %errorlevel%

:main

echo Checking MySQL status...
:: Check default MySQL port 3306
call :check_port 3306
if %errorlevel% equ 0 (
    echo MySQL is running.
) else (
    echo MySQL is likely NOT running (Port 3306 not active).
    echo Please make sure MySQL is started.
    :: We don't exit here strictly like the sh script because service names vary on Windows, 
    :: but we warn the user.
    echo Press any key to continue anyway or Ctrl+C to abort...
    pause >nul
)

echo Building Backend Services...
cd backend
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo Backend build failed!
    exit /b 1
)
cd ..

:: Create log directory
if not exist logs mkdir logs

echo Starting Discovery Server...
start /b "Discovery Server" cmd /c "java -jar backend\discovery-server\target\discovery-server-0.0.1-SNAPSHOT.jar > logs\discovery-server.log 2>&1"
echo Waiting for Discovery Server to start...

:wait_discovery
timeout /t 5 /nobreak >nul
curl -s http://localhost:8761 >nul
if %errorlevel% neq 0 (
    echo Waiting for Discovery Server...
    goto :wait_discovery
)
echo Discovery Server started.

echo Starting API Gateway...
start /b "API Gateway" cmd /c "java -jar backend\api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar > logs\api-gateway.log 2>&1"
:: Give API Gateway some time to register
timeout /t 10 /nobreak >nul
echo API Gateway started.

echo Starting User Auth Service...
start /b "User Auth Service" cmd /c "java -jar backend\user-auth-service\target\user-auth-service-0.0.1-SNAPSHOT.jar > logs\user-auth-service.log 2>&1"

echo Starting Product Service...
start /b "Product Service" cmd /c "java -jar backend\product-service\target\product-service-0.0.1-SNAPSHOT.jar > logs\product-service.log 2>&1"

echo Starting Order Service...
start /b "Order Service" cmd /c "java -jar backend\order-service\target\order-service-0.0.1-SNAPSHOT.jar > logs\order-service.log 2>&1"

echo Starting Payment Service...
start /b "Payment Service" cmd /c "java -jar backend\payment-service\target\payment-service-0.0.1-SNAPSHOT.jar > logs\payment-service.log 2>&1"

:: Optional: Start legacy Shopping Service if needed
:: echo Starting Shopping Service...
:: start /b "Shopping Service" cmd /c "java -jar backend\shopping-service\target\shopping-service-0.0.1-SNAPSHOT.jar > logs\shopping-service.log 2>&1"

echo Backend Services Started. Check 'logs\' directory for output.

echo Starting Frontend...
cd frontend
call :check_port 4200
if %errorlevel% neq 0 (
    echo Frontend starting on port 4200...
    start /b "Frontend" cmd /c "ng serve > ..\logs\frontend.log 2>&1"
) else (
    echo Port 4200 is already in use. Skipping frontend start.
)
cd ..

echo All services initiated.
endlocal
