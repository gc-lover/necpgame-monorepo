# NECPGAME Makefile

.PHONY: help build test deploy clean

# Default target
help:
	@echo "NECPGAME Development Commands"
	@echo ""
	@echo "Local Development:"
	@echo "  make docker-up          - Start all services with Docker Compose"
	@echo "  make docker-down        - Stop all services"
	@echo "  make docker-logs        - Show logs from all services"
	@echo ""
	@echo "Building:"
	@echo "  make build-auth         - Build auth-service"
	@echo "  make build-ability      - Build ability-service"
	@echo "  make build-all          - Build all services"
	@echo ""
	@echo "Kubernetes:"
	@echo "  make k8s-deploy         - Deploy to Kubernetes"
	@echo "  make k8s-undeploy       - Remove from Kubernetes"
	@echo "  make k8s-status         - Show Kubernetes status"
	@echo ""
	@echo "Database:"
	@echo "  make db-migrate         - Run database migrations"
	@echo "  make db-seed            - Seed database with test data"
	@echo ""
	@echo "Testing:"
	@echo "  make test               - Run all tests"
	@echo "  make test-auth          - Test auth service"
	@echo "  make test-ability       - Test ability service"
	@echo ""
	@echo "Cleanup:"
	@echo "  make clean              - Clean build artifacts"
	@echo "  make clean-all          - Clean everything"

# Docker Compose commands
docker-up:
	docker-compose up -d

docker-down:
	docker-compose down

docker-logs:
	docker-compose logs -f

docker-build:
	docker-compose build

# Build commands
build-auth:
	cd services/auth-service-go && go build -o auth-service .

build-ability:
	cd services/ability-service-go && go build -o ability-service .

build-matchmaking:
	cd services/matchmaking-service-go && go build -o matchmaking-service ./cmd/api

build-all: build-auth build-ability build-matchmaking

# Kubernetes commands
k8s-deploy:
	kubectl apply -f k8s/

k8s-undeploy:
	kubectl delete -f k8s/

k8s-status:
	kubectl get pods,services,ingress -l app

# Database commands
db-migrate:
	@echo "Running database migrations..."
	# Add migration commands here

db-seed:
	@echo "Seeding database..."
	# Add seeding commands here

# Testing
test:
	go test ./...

test-auth:
	cd services/auth-service-go && go test ./...

test-ability:
	cd services/ability-service-go && go test ./...

test-matchmaking:
	cd services/matchmaking-service-go && go test ./...

# Cleanup
clean:
	find . -name "*.exe" -delete
	find . -name "*.test" -delete
	find . -name "*.out" -delete

clean-all: clean
	docker-compose down -v
	docker system prune -f
	kubectl delete namespace necpgame --ignore-not-found=true