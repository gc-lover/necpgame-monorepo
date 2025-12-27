# Social Orders Reputation Integration Service

**Issue:** [#140894823](https://github.com/gc-lover/necpgame-monorepo/issues/140894823)

## Overview

This service integrates the player orders system with the reputation system in the MMOFPS RPG. It provides detailed mechanics for how reputation affects order costs, requirements, and completion rewards.

## Features

### 🎯 Core Functionality

- **Reputation-Based Cost Calculation**: Orders cost/rewards are modified based on character reputation
- **Reputation Requirements**: Certain orders require minimum reputation levels to accept
- **Post-Completion Reputation Changes**: Reputation changes after order completion based on quality
- **Contractor Ranking**: Ranking system for contractors based on reputation and performance
- **Reputation Bonuses**: Dynamic bonuses/penalties for orders based on faction reputation

### 🔧 Technical Features

- **REST API**: Full REST API for all operations
- **Enterprise-grade**: Production-ready with proper error handling and logging
- **Docker Support**: Containerized deployment
- **Performance Optimized**: Efficient algorithms for reputation calculations

## API Endpoints

### Calculate Order Cost with Reputation
```
GET /orders-reputation/orders/{order_id}/cost?client_id={uuid}&contractor_id={uuid}
```
Returns order cost modified by reputation of client and contractor.

### Check Reputation Requirements
```
GET /orders-reputation/orders/{order_id}/requirements?character_id={uuid}&role=client|contractor
```
Validates if character meets reputation requirements for order.

### Apply Completion Reputation Changes
```
POST /orders-reputation/orders/{order_id}/complete
```
Applies reputation changes after order completion based on quality and feedback.

### Get Contractor Ranking
```
GET /orders-reputation/contractors/ranking?faction_id={uuid}&order_type={string}&limit=50&offset=0
```
Returns ranking of contractors by reputation and performance.

### Get Reputation Bonuses
```
GET /orders-reputation/bonuses?character_id={uuid}&faction_id={uuid}
```
Returns available reputation bonuses for character.

## Architecture

### Service Structure
```
services/social-orders-reputation-integration-service-go/
├── main.go                 # Entry point
├── server/
│   └── server.go          # HTTP server and routes
├── pkg/orders-reputation/
│   └── service.go         # Core business logic
├── Dockerfile             # Container definition
├── Makefile              # Build automation
└── README.md             # This file
```

### Dependencies
- **go-chi/chi**: HTTP router
- **google/uuid**: UUID handling
- **uber-go/zap**: Structured logging
- **lib/pq**: PostgreSQL driver (future use)

## Development

### Prerequisites
- Go 1.21+
- Docker (optional)

### Quick Start

1. **Clone and setup:**
   ```bash
   cd services/social-orders-reputation-integration-service-go
   go mod tidy
   ```

2. **Run locally:**
   ```bash
   make run
   # or
   go run main.go
   ```

3. **Build:**
   ```bash
   make build
   ```

4. **Run tests:**
   ```bash
   make test
   ```

5. **Docker:**
   ```bash
   make docker-build
   make docker-run
   ```

### Configuration

The service uses environment variables for configuration:

```bash
# Database (future use)
DATABASE_URL=postgres://user:pass@localhost/db

# External services (future integration)
REPUTATION_SERVICE_URL=http://reputation-service:8080
ORDERS_SERVICE_URL=http://orders-service:8080

# Logging
LOG_LEVEL=info
```

## Integration Points

### External Services
- **Reputation Service**: For reputation data and changes
- **Orders Service**: For order details and status
- **Player Service**: For character information
- **Economy Service**: For currency calculations

### Database Schema (Future)
```sql
-- Reputation modifiers table
CREATE TABLE reputation_modifiers (
    faction_id UUID,
    reputation_tier VARCHAR(20),
    cost_modifier DECIMAL(5,2),
    reward_modifier DECIMAL(5,2)
);

-- Order reputation history
CREATE TABLE order_reputation_history (
    order_id UUID,
    character_id UUID,
    reputation_change INTEGER,
    reason VARCHAR(100),
    timestamp TIMESTAMP
);
```

## Business Logic

### Reputation Tiers
1. **Hated** (-∞ to -1000)
2. **Hostile** (-1000 to -500)
3. **Unfriendly** (-500 to -100)
4. **Neutral** (-100 to 100)
5. **Friendly** (100 to 500)
6. **Honored** (500 to 1000)
7. **Revered** (1000 to 2000)
8. **Exalted** (2000+)

### Cost Modifiers
- **High Reputation** (Honored+): 15% discount on costs, 10% bonus on rewards
- **Low Reputation** (Hostile-): 25% surcharge on costs, 20% penalty on rewards
- **Neutral Reputation**: 5% discount on costs

### Completion Reputation Changes
- **Excellent**: +100 reputation, +200 XP
- **Good**: +50 reputation, +150 XP
- **Average**: +10 reputation, +100 XP
- **Poor**: -25 reputation, +50 XP
- **Failed**: -100 reputation, +10 XP

Additional modifiers:
- **On Time**: +25 reputation bonus
- **Late**: -25 reputation penalty
- **High Client Rating** (4-5): +25 reputation bonus
- **Low Client Rating** (1-2): -25 reputation penalty

## Monitoring & Observability

### Health Check
```
GET /health
```

### Metrics (Future)
- Order cost calculation latency
- Reputation requirement check success rate
- Contractor ranking query performance
- Reputation change application throughput

### Logging
Structured JSON logging with:
- Request IDs
- Operation types
- Performance metrics
- Error details

## Deployment

### Docker Compose
```yaml
version: '3.8'
services:
  social-orders-reputation-integration:
    build: .
    ports:
      - "8080:8080"
    environment:
      - LOG_LEVEL=info
    depends_on:
      - postgres
      - reputation-service
      - orders-service
```

### Kubernetes
See `k8s/` directory for deployment manifests.

## Testing

### Unit Tests
```bash
go test ./pkg/orders-reputation/
```

### Integration Tests
```bash
make integration-test
```

### Load Testing (Future)
```bash
# Using k6 or similar
k6 run load-test.js
```

## Contributing

1. Follow Go best practices
2. Add tests for new functionality
3. Update documentation
4. Use conventional commits

## Related Issues

- [#140894823](https://github.com/gc-lover/necpgame-monorepo/issues/140894823) - Main implementation
- Reputation system integration
- Orders system integration
- Economy system integration
