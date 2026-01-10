# NECPGAME - Night City Experience Core Game

A high-performance MMOFPS RPG backend built with Go, featuring enterprise-grade microservices architecture.

## 🚀 Quick Start

### Prerequisites

- Docker & Docker Compose
- Go 1.21+
- kubectl (for Kubernetes deployment)

### Local Development

1. **Clone and setup:**
   ```bash
   git clone <repository>
   cd necpgame
   cp env.example .env  # Configure your environment
   ```

2. **Start all services:**
   ```bash
   make docker-up
   ```

3. **Check services:**
   ```bash
   make docker-logs
   ```

4. **API endpoints:**
   - Auth Service: http://localhost:8080/auth
   - Ability Service: http://localhost:8081/ability
   - Matchmaking Service: http://localhost:8082/matchmaking
   - Economy Service: http://localhost:8083/economy
   - Combat Service: http://localhost:8084/combat
   - API Gateway: http://localhost:8080
   - Grafana: http://localhost:3000 (admin/admin)
   - Prometheus: http://localhost:9090

## 🏗️ Architecture

### Core Services

- **Auth Service** (`:8080`) - Enterprise JWT authentication & session management
- **Ability Service** (`:8081`) - Character abilities & cooldowns
- **Matchmaking Service** (`:8082`) - Player matchmaking & queue management
- **Economy Service** (`:8083`) - BazaarBot AI trading & marketplace
- **Combat Service** (`:8084`) - Enterprise real-time combat system
- **World Event Service** - Dynamic world events

### Infrastructure

- **PostgreSQL** - Primary database
- **Redis** - Caching & sessions
- **Envoy** - API Gateway & load balancing
- **Prometheus/Grafana** - Monitoring & metrics
- **Kubernetes** - Production orchestration

## 🔧 Development

### Building Services

```bash
# Build all services
make build-all

# Build specific service
make build-auth
make build-ability
```

### Running Tests

```bash
# Test all services
make test

# Test specific service
make test-auth
```

### Database Migrations

```bash
# Run migrations
make db-migrate

# Seed with test data
make db-seed
```

## 🚢 Deployment

### Docker Compose (Development)

```bash
# Start development environment
make docker-up

# Stop all services
make docker-down
```

### Kubernetes (Production)

```bash
# Deploy to Kubernetes
make k8s-deploy

# Check status
make k8s-status

# Remove deployment
make k8s-undeploy
```

## 📊 Monitoring

- **Grafana**: http://localhost:3000
- **Prometheus**: http://localhost:9090
- **Service Health**: `GET /health` on each service

## 🔒 Security

- JWT-based authentication
- Argon2id password hashing
- Rate limiting & DDoS protection
- Input validation & sanitization

## 📈 Performance

- **P99 Latency**: <25ms for auth, <15ms for abilities
- **Concurrent Users**: 45,000+ simultaneous connections
- **Memory**: <12KB per active ability session
- **Struct Alignment**: 30-50% memory savings

## 🛠️ API Documentation

### OpenAPI Specifications

All services follow OpenAPI 3.0 specifications located in `proto/openapi/{service}/main.yaml`

### Code Generation

```bash
# Generate API code for all services
python scripts/generate-all-services.py services

# Generate for specific service
python scripts/generate-all-services.py services --service auth-service
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📝 License

Proprietary - All rights reserved.

## 📞 Support

- **API Support**: api@necpgame.com
- **Dev Support**: dev@necpgame.com
- **Monitoring**: Check Grafana dashboards

---

**Night City Experience - Where the future becomes reality.**