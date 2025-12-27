# Social Orders Creation Service

**Issue:** [#140894825](https://github.com/gc-lover/necpgame-monorepo/issues/140894825)

## Overview

This service provides advanced order creation functionality with comprehensive validation, optimization suggestions, and contractor recommendations for the MMOFPS RPG orders system.

## Features

### 🎯 Core Functionality

- **Order Draft Creation**: Create order drafts with automatic validation and suggestions
- **Parameter Validation**: Comprehensive validation of order parameters, reputation requirements, and feasibility
- **Optimization Engine**: AI-powered suggestions for improving order success rates and cost efficiency
- **Contractor Matching**: Intelligent contractor recommendations based on skills, reputation, and availability
- **One-Click Creation**: Create orders with automatic validation and optimization applied

### 🔧 Technical Features

- **REST API**: Full REST API with comprehensive error handling
- **Enterprise-grade**: Production-ready with proper logging and monitoring
- **Docker Support**: Containerized deployment
- **Performance Optimized**: Efficient validation and optimization algorithms

## API Endpoints

### Create Order Draft
```
POST /orders-creation/draft
```
Creates an order draft with validation results, optimization suggestions, and contractor recommendations.

### Validate Order Parameters
```
POST /orders-creation/validate
```
Validates order parameters and returns detailed validation results with errors and warnings.

### Optimize Order Parameters
```
POST /orders-creation/optimize
```
Provides optimization suggestions to improve order success rates and efficiency.

### Suggest Contractors
```
POST /orders-creation/contractors/suggest?max_suggestions=5
```
Returns recommended contractors for the order based on matching criteria.

### Create Order with Validation
```
POST /orders-creation/create-with-validation
```
Creates an order with automatic validation, optimization, and contractor notifications.

## Architecture

### Service Structure
```
services/social-orders-creation-service-go/
├── main.go                 # Entry point
├── server/
│   └── server.go          # HTTP server and routes
├── pkg/orders-creation/
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
   cd services/social-orders-creation-service-go
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

## Business Logic

### Order Validation Rules

1. **Title Requirements**: Minimum 5 characters, maximum 100 characters
2. **Reward Validation**: Must be positive amount, valid currency
3. **Deadline Checks**: Cannot be in the past, reasonable timeframes
4. **Reputation Requirements**: Validates minimum reputation for order types
5. **Balance Verification**: Checks if client has sufficient funds

### Optimization Engine

The service provides optimization suggestions for:

- **Reward Amount**: Optimal pricing for contractor attraction
- **Deadline Extension**: Better success rates with more time
- **Risk Level**: Balancing risk vs. reward vs. feasibility
- **Requirements**: Adjusting skill/reputation requirements

### Contractor Matching

Contractor suggestions are based on:

- **Skill Match**: Percentage of required skills possessed
- **Reputation Score**: Overall reputation rating (0-100)
- **Success Rate**: Historical completion success percentage
- **Availability**: Current availability status
- **Pricing**: Competitive hourly rates

### Success Probability Calculation

Success probability is calculated based on:

- **Risk Level**: Low (+15%), Medium (+5%), High (-10%), Extreme (-25%)
- **Reward Amount**: Higher rewards attract better contractors (+10% for >1000)
- **Contractor Quality**: Better contractors increase success rates
- **Time Pressure**: Tighter deadlines reduce success rates

## Integration Points

### External Services
- **Orders Service**: Core order management and storage
- **Reputation Service**: Reputation data and validation
- **Player Service**: Character information and balance checks
- **Notification Service**: Contractor notifications
- **Economy Service**: Currency validation and transactions

### Database Schema (Future)
```sql
-- Order drafts table
CREATE TABLE order_drafts (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    order_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'draft',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Contractor suggestions cache
CREATE TABLE contractor_suggestions (
    order_draft_id UUID REFERENCES order_drafts(id),
    contractor_id UUID NOT NULL,
    match_score DECIMAL(5,2),
    recommendation_reason TEXT,
    suggested_at TIMESTAMP DEFAULT NOW()
);
```

## Monitoring & Observability

### Health Check
```
GET /health
```

### Metrics (Future)
- Order draft creation latency
- Validation success/failure rates
- Optimization suggestion acceptance rates
- Contractor matching performance
- Order creation throughput

### Logging
Structured JSON logging with:
- Request IDs for tracing
- Operation types and performance metrics
- Validation results and error details
- Contractor matching decisions

## Deployment

### Docker Compose
```yaml
version: '3.8'
services:
  social-orders-creation:
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
go test ./pkg/orders-creation/
```

### Integration Tests
```bash
make integration-test
```

### API Testing Example
```bash
# Create order draft
curl -X POST http://localhost:8080/orders-creation/draft \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Order",
    "description": "A test order for validation",
    "order_type": "combat",
    "reward": {"currency": "ed", "amount": 500},
    "risk_level": "medium"
  }'
```

## Contributing

1. Follow Go best practices and project conventions
2. Add comprehensive tests for new functionality
3. Update API documentation for endpoint changes
4. Use conventional commits for all changes

## Related Issues

- [#140894825](https://github.com/gc-lover/necpgame-monorepo/issues/140894825) - Main implementation
- Order validation and optimization
- Contractor matching algorithms
- Reputation integration
- Economy system integration
