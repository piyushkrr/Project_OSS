#!/bin/bash

# Function to check if a port is in use
check_port() {
    local port=$1
    if lsof -i :$port > /dev/null; then
        return 0
    else
        return 1
    fi
}

echo "Checking MySQL status..."
if service mysql status | grep -q "active (running)"; then
    echo "MySQL is running."
else
    echo "MySQL is NOT running. Please start it using 'sudo service mysql start'."
    exit 1
fi

echo "Building Backend Services..."
cd backend
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "Backend build failed!"
    exit 1
fi
cd ..

# Create log directory
mkdir -p logs

echo "Starting Discovery Server..."
nohup java -jar backend/discovery-server/target/discovery-server-0.0.1-SNAPSHOT.jar > logs/discovery-server.log 2>&1 &
echo "Waiting for Discovery Server to start..."
while ! curl -s http://localhost:8761 > /dev/null; do
    sleep 5
    echo "Waiting for Discovery Server..."
done
echo "Discovery Server started."

echo "Starting API Gateway..."
nohup java -jar backend/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar > logs/api-gateway.log 2>&1 &
# Give API Gateway some time to register
sleep 10
echo "API Gateway started."

echo "Starting User Auth Service..."
nohup java -jar backend/user-auth-service/target/user-auth-service-0.0.1-SNAPSHOT.jar > logs/user-auth-service.log 2>&1 &

echo "Starting Product Service..."
nohup java -jar backend/product-service/target/product-service-0.0.1-SNAPSHOT.jar > logs/product-service.log 2>&1 &

echo "Starting Order Service..."
nohup java -jar backend/order-service/target/order-service-0.0.1-SNAPSHOT.jar > logs/order-service.log 2>&1 &

echo "Starting Payment Service..."
nohup java -jar backend/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar > logs/payment-service.log 2>&1 &

# Optional: Start legacy Shopping Service if needed
# echo "Starting Shopping Service..."
# nohup java -jar backend/shopping-service/target/shopping-service-0.0.1-SNAPSHOT.jar > logs/shopping-service.log 2>&1 &

echo "Backend Services Started. Check 'logs/' directory for output."

echo "Starting Frontend..."
cd frontend
if ! check_port 4200; then
    nohup ng serve > ../logs/frontend.log 2>&1 &
    echo "Frontend starting on port 4200..."
else
    echo "Port 4200 is already in use. Skipping frontend start."
fi
cd ..

echo "All services initiated."
