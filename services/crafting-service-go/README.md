# Crafting Service

## Issue: #2229

Enterprise-grade crafting system service for NECPGAME MMOFPS RPG with recipe management, order processing, station booking, and production chains.

## Features

### Core Functionality
- **Recipe Management**: Create, retrieve, and manage crafting recipes with materials and results
- **Order Processing**: Handle crafting orders with progress tracking and quality control
- **Station Booking**: Reserve crafting stations with availability management
- **Contract System**: Manage production contracts for complex crafting tasks
- **Production Chains**: Support multi-step manufacturing processes
- **Quality Control**: Material efficiency and skill-based crafting results

### Performance Optimizations
- **MMOFPS Standards**: P99 <15ms latency, 2000+ RPS sustained load
- **Memory Pooling**: Zero allocations in hot paths, optimized struct field ordering
- **Connection Pooling**: PostgreSQL (50 connections) + Redis caching
- **Circuit Breaker**: Resilience patterns for external service failures
- **Worker Pools**: Concurrent order processing with configurable workers
- **Cache Optimization**: 98%+ hit rate for recipe and station data

### Enterprise Features
- **Event-Driven**: Kafka integration for crafting events and order updates
- **Audit Trail**: Complete logging of all crafting operations
- **Role-Based Access**: JWT authentication with crafting permissions
- **Monitoring**: Prometheus metrics, health checks, profiling endpoints
- **Production Ready**: Docker containerization, graceful shutdown, error handling

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   HTTP REST     │    │   Business      │    │   PostgreSQL    │
│   API (Chi)     │◄──►│   Logic         │◄──►│   + Redis Cache  │
│   (JWT Auth)    │    │   (Service)     │    │   (Hot Data)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Kafka Events  │    │   Prometheus    │    │   Health        │
│   Streaming     │    │   Metrics       │    │   Checks        │
│   (Async)       │    │   (Real-time)   │    │   (/health)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## API Endpoints

### Recipes Management
- `GET /api/v1/economy/crafting/recipes` - Get recipes by category/tier/quality
- `GET /api/v1/economy/crafting/recipes/{recipeId}` - Get specific recipe
- `POST /api/v1/economy/crafting/recipes` - Create new recipe
- `PUT /api/v1/economy/crafting/recipes/{recipeId}` - Update recipe
- `DELETE /api/v1/economy/crafting/recipes/{recipeId}` - Delete recipe

### Order Management
- `GET /api/v1/economy/crafting/orders` - Get player orders
- `GET /api/v1/economy/crafting/orders/{orderId}` - Get specific order
- `POST /api/v1/economy/crafting/orders` - Create crafting order
- `PUT /api/v1/economy/crafting/orders/{orderId}` - Update order
- `DELETE /api/v1/economy/crafting/orders/{orderId}` - Cancel order

### Station Management
- `GET /api/v1/economy/crafting/stations` - Get available stations
- `GET /api/v1/economy/crafting/stations/{stationId}` - Get station details
- `POST /api/v1/economy/crafting/stations/{stationId}/book` - Book station
- `POST /api/v1/economy/crafting/stations/{stationId}/release` - Release station

### Contracts & Production Chains
- `GET /api/v1/economy/crafting/contracts` - Get contracts
- `GET /api/v1/economy/crafting/contracts/{contractId}` - Get contract
- `POST /api/v1/economy/crafting/contracts` - Create contract
- `PUT /api/v1/economy/crafting/contracts/{contractId}` - Update contract
- `GET /api/v1/economy/crafting/chains` - Get production chains
- `GET /api/v1/economy/crafting/chains/{chainId}` - Get chain
- `POST /api/v1/economy/crafting/chains` - Create production chain
- `PUT /api/v1/economy/crafting/chains/{chainId}` - Update chain

### Health & Monitoring
- `GET /health` - Health check
- `GET /ready` - Readiness probe
- `GET /metrics` - Prometheus metrics
- `GET /debug/pprof/` - Profiling endpoints

## Configuration

### Environment Variables
```bash
# Server
PORT=8210

# Database
DATABASE_URL=postgres://user:password@localhost:5432/necpgame?sslmode=disable

# Cache
REDIS_URL=redis://localhost:6379

# Security
JWT_SECRET=your-secret-key

# Environment
ENVIRONMENT=development
LOG_LEVEL=info
```

### Database Schema

```sql
-- Crafting recipes with materials and results
CREATE TABLE economy.crafting_recipes (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    tier INTEGER NOT NULL,
    quality VARCHAR(50) NOT NULL,
    materials JSONB NOT NULL,
    result JSONB NOT NULL,
    skill_req INTEGER NOT NULL,
    time_req INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Crafting orders with progress tracking
CREATE TABLE economy.crafting_orders (
    id VARCHAR(255) PRIMARY KEY,
    player_id VARCHAR(255) NOT NULL,
    recipe_id VARCHAR(255) NOT NULL REFERENCES economy.crafting_recipes(id),
    station_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    progress DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    quality VARCHAR(50),
    created_at TIMESTAMP NOT NULL
);

-- Crafting stations with booking system
CREATE TABLE economy.crafting_stations (
    id VARCHAR(255) PRIMARY KEY,
    type VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    booked_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Performance indexes
CREATE INDEX idx_crafting_recipes_category_tier ON economy.crafting_recipes(category, tier);
CREATE INDEX idx_crafting_orders_player ON economy.crafting_orders(player_id, created_at DESC);
CREATE INDEX idx_crafting_stations_type_status ON economy.crafting_stations(type, status);
```

## Quick Start

### Local Development
```bash
# Install dependencies
go mod tidy

# Run locally
make run

# Or build and run
make build
./bin/crafting-service
```

### Docker
```bash
# Build image
make docker-build

# Run container
make docker-run
```

### Docker Compose
```bash
# Start all services
make docker-compose-up
```

## Performance Benchmarks

### Latency Targets
- **Recipe queries**: P99 <15ms
- **Order creation**: P99 <20ms
- **Station booking**: P99 <10ms
- **API endpoints**: P99 <25ms
- **Database queries**: P95 <5ms

### Throughput Targets
- **Concurrent users**: 10,000+ supported
- **Orders/sec**: 500+ creation rate
- **Queries/sec**: 2,000+ sustained
- **Cache hit rate**: 98%+ for recipes
- **Memory per connection**: ~5KB

### Resource Usage
- **CPU per 1000 users**: <15%
- **Memory per service**: <200MB
- **Network bandwidth**: <2Mbps per 1000 users
- **Database connections**: 50 max

## Monitoring

### Key Metrics
```
crafting_recipes_created_total     - Recipe creation rate
crafting_orders_created_total      - Order creation rate
crafting_orders_completed_total    - Order completion rate
crafting_stations_booked_total     - Station booking rate
crafting_request_duration_seconds  - API latency histogram
crafting_active_orders             - Active order count
crafting_errors_total              - Error rate tracking
```

### Health Checks
- **Readiness**: Database and Redis connectivity
- **Liveness**: Service responsiveness and order processing
- **Custom**: Crafting queue health and station availability

## Crafting System Design

### Recipe Categories
- **Weapons**: Guns, melee weapons, explosives
- **Armor**: Body armor, helmets, implants
- **Vehicles**: Cars, motorcycles, drones
- **Cyberware**: Neural implants, body modifications
- **Consumables**: Medkits, boosters, grenades
- **Tools**: Hacking tools, crafting stations

### Quality Tiers
- **Common**: Basic materials, standard quality
- **Uncommon**: Better materials, improved quality
- **Rare**: Premium materials, high quality
- **Epic**: Exotic materials, exceptional quality
- **Legendary**: Unique materials, masterpiece quality

### Station Types
- **Workbench**: Basic crafting station
- **Forge**: Metalworking and weapon crafting
- **Lab**: Cyberware and electronic crafting
- **Garage**: Vehicle modification and repair
- **Clinic**: Medical implant crafting

### Order States
- **queued**: Waiting for station availability
- **in_progress**: Currently being crafted
- **completed**: Successfully finished
- **failed**: Crafting failed (materials lost)
- **cancelled**: Player cancelled order

## Development

### Code Structure
```
internal/
├── config/        # Configuration management
├── handlers/      # HTTP request handlers
├── service/       # Business logic layer
├── repository/    # Data access layer
└── metrics/       # Monitoring and metrics

pkg/               # Shared packages (future)
cmd/               # CLI tools (future)
```

### Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Benchmarks
go test -bench=. ./...
```

### Code Quality
```bash
# Format code
make fmt

# Lint code
make lint

# Full CI pipeline
make all
```

## Integration Points

### Event-Driven Architecture
- **Kafka Topics**: `economy.crafting.events`, `economy.orders.lifecycle`
- **Event Types**: order_created, order_completed, station_booked, recipe_used
- **Consumer Groups**: crafting-processor, order-manager, station-monitor

### External Services
- **Database**: PostgreSQL for persistent recipe/order data
- **Cache**: Redis for hot recipe and station data
- **Message Queue**: Kafka for crafting event streaming
- **Monitoring**: Prometheus for metrics collection
- **Authentication**: JWT token validation

### Game Integration
- **Inventory Service**: Material consumption and result delivery
- **Economy Service**: Crafting costs and rewards
- **Skill Service**: Crafting skill progression
- **Achievement Service**: Crafting milestone unlocks
- **Quest Service**: Crafting-based quest objectives

## Contributing

1. Follow Go best practices and SOLID principles
2. Write comprehensive tests for new crafting mechanics
3. Update API documentation for endpoint changes
4. Ensure performance benchmarks pass for new features
5. Follow conventional commit messages

## License

Apache License 2.0
