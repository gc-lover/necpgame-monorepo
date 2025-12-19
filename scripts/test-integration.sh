#!/bin/bash
# Integration Test Execution Script
# Issue: #1904

set -e

echo "🔗 Running Integration Tests..."
echo "==============================="

COMPOSE_FILE="docker-compose.test.yml"
TIMEOUT=600  # 10 minutes timeout
RETRIES=3

# Check if docker-compose test file exists
if [ ! -f "$COMPOSE_FILE" ]; then
    echo "❌ docker-compose.test.yml not found"
    echo "Creating basic test configuration..."

    cat > "$COMPOSE_FILE" << 'EOF'
version: '3.8'
services:
  test-db:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: necpgame_test
      POSTGRES_USER: test
      POSTGRES_PASSWORD: test
    ports:
      - "5433:5432"
    volumes:
      - test-db-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U test -d necpgame_test"]
      interval: 10s
      timeout: 5s
      retries: 5

  test-backend:
    build:
      context: .
      dockerfile: Dockerfile.test
    depends_on:
      test-db:
        condition: service_healthy
    environment:
      DATABASE_URL: postgres://test:test@test-db:5432/necpgame_test?sslmode=disable
      SERVER_ADDR: 0.0.0.0:8080
    ports:
      - "8081:8080"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  test-db-data:
EOF

    echo "OK Created docker-compose.test.yml"
fi

# Function to cleanup containers
cleanup() {
    echo "🧹 Cleaning up test containers..."
    docker-compose -f "$COMPOSE_FILE" down -v 2>/dev/null || true
}

# Function to run integration tests
run_integration_tests() {
    echo "🚀 Starting test environment..."

    # Start services with timeout
    if timeout "$TIMEOUT" docker-compose -f "$COMPOSE_FILE" up --build --abort-on-container-exit; then
        echo -e "\033[0;32mOK Integration tests passed\033[0m"
        return 0
    else
        echo -e "\033[0;31m❌ Integration tests failed\033[0m"
        return 1
    fi
}

# Function to check API health
check_api_health() {
    local max_attempts=30
    local attempt=1

    echo "🏥 Checking API health..."

    while [ $attempt -le $max_attempts ]; do
        if curl -f -s http://localhost:8081/health >/dev/null 2>&1; then
            echo "OK API health check passed"
            return 0
        fi

        echo "Attempt $attempt/$max_attempts: API not ready, waiting..."
        sleep 10
        ((attempt++))
    done

    echo "❌ API health check failed after $max_attempts attempts"
    return 1
}

# Function to run API contract tests
run_api_contract_tests() {
    echo "📋 Running API contract tests..."

    # Wait for API to be ready
    if ! check_api_health; then
        return 1
    fi

    # Basic API tests
    echo "Testing basic endpoints..."

    # Health endpoint
    if ! curl -f -s http://localhost:8081/health | grep -q "OK"; then
        echo "❌ Health endpoint failed"
        return 1
    fi

    # Metrics endpoint (if available)
    if curl -f -s http://localhost:8081/metrics >/dev/null 2>&1; then
        echo "OK Metrics endpoint available"
    else
        echo "WARNING  Metrics endpoint not available (optional)"
    fi

    # Try to get OpenAPI spec (if available)
    if curl -f -s http://localhost:8081/docs >/dev/null 2>&1; then
        echo "OK OpenAPI docs available"
    else
        echo "WARNING  OpenAPI docs not available (optional)"
    fi

    echo "OK API contract tests passed"
    return 0
}

# Trap to ensure cleanup on exit
trap cleanup EXIT

# Retry logic
attempt=1
while [ $attempt -le $RETRIES ]; do
    echo ""
    echo "🔄 Integration test attempt $attempt/$RETRIES"

    if run_integration_tests && run_api_contract_tests; then
        echo ""
        echo -e "\033[0;32m🎉 All integration tests passed!\033[0m"
        exit 0
    else
        echo -e "\033[0;33mWARNING  Attempt $attempt failed\033[0m"
        cleanup

        if [ $attempt -eq $RETRIES ]; then
            echo ""
            echo -e "\033[0;31m💥 All integration test attempts failed\033[0m"
            echo "Check logs and fix integration issues"
            exit 1
        fi

        echo "Retrying in 10 seconds..."
        sleep 10
        ((attempt++))
    fi
done