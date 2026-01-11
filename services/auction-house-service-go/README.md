# Auction House Service - Dynamic Pricing Engine

## Overview

The Auction House Service implements advanced dynamic pricing mechanics for NECPGAME, providing a sophisticated economic system with BazaarBot-inspired algorithms and double auction market clearing.

## Key Features

- **Dynamic Pricing**: BazaarBot algorithm with adaptive price belief updates
- **Double Auction Market**: Real-time bid/ask matching with efficient clearing
- **Real-time Trading**: WebSocket-powered live market updates
- **Market Analytics**: Advanced price prediction and trend analysis
- **Anti-cheat Protection**: Comprehensive fraud detection and prevention
- **Performance Optimized**: MMOFPS-grade performance with <10ms P99 latency

## Domain Purpose

The Auction House serves as the economic heart of NECPGAME, enabling dynamic player-driven economies with sophisticated pricing mechanics that adapt to supply/demand fluctuations in real-time.

## Performance Targets

- **P99 Latency**: <10ms for price queries, <50ms for trade execution
- **Memory**: <100KB per active auction lot
- **Concurrent traders**: 50,000+ simultaneous connections
- **Price updates**: Real-time (<100ms propagation)
- **Market clearing**: <20ms for double auction execution

## Architecture

### Core Components

#### Pricing Engine
- **BazaarBot Algorithm**: Kalman filter-inspired price belief updates
- **Double Auction**: Bid/ask matching with midpoint clearing
- **Price Prediction**: Trend analysis and confidence scoring
- **Market Health**: Efficiency metrics and stability scoring

#### Auction Registry
- **Lot Management**: Active auction tracking and lifecycle
- **Bid Processing**: Real-time bid validation and execution
- **Trade Recording**: Complete transaction history
- **Market Data**: Real-time price and volume tracking

### Data Models

#### AuctionLot
```go
type AuctionLot struct {
    ID            string    `json:"id"`
    ItemID        string    `json:"item_id"`
    SellerID      string    `json:"seller_id"`
    CurrentPrice  float64   `json:"current_price"`
    BuyoutPrice   float64   `json:"buyout_price"`
    EndTime       int64     `json:"end_time"`
    Status        string    `json:"status"`
    // ... optimized struct alignment
}
```

#### Price Belief (BazaarBot)
```go
type PriceBelief struct {
    ItemID         string  `json:"item_id"`
    CurrentBelief  float64 `json:"current_belief"`
    BeliefVariance float64 `json:"belief_variance"`
    Confidence     float64 `json:"confidence"`
    LearningRate   float64 `json:"learning_rate"`
}
```

## API Endpoints

### Auction Management
- `GET /lots` - Retrieve active auction lots
- `POST /lots` - Create new auction lot
- `GET /lots/{id}` - Get lot details
- `POST /lots/{id}/bids` - Place bid on lot

### Market Data
- `GET /market/prices` - Current market prices
- `GET /market/analytics` - Market analytics and trends

### Trading History
- `GET /trader/{id}/history` - Player trading history

## Database Schema

### Key Tables
- `auction_house.auction_lots` - Active auction listings
- `auction_house.bids` - Bid history and management
- `auction_house.trade_records` - Completed transactions
- `auction_house.market_prices` - Real-time pricing data
- `auction_house.price_beliefs` - BazaarBot algorithm state
- `auction_house.supply_demand_history` - Market data history

### Performance Optimizations
- Composite indexes for query patterns
- Partial indexes for active records
- JSONB for flexible algorithm parameters
- Time-based partitioning for history tables

## Configuration

### Environment Variables
```bash
AUCTION_HOUSE_DB_HOST=localhost
AUCTION_HOUSE_DB_PORT=5432
AUCTION_HOUSE_DB_NAME=necpgame
AUCTION_HOUSE_REDIS_URL=redis://localhost:6379
AUCTION_HOUSE_METRICS_PORT=9090
```

### Algorithm Parameters
```yaml
pricing:
  bazaarbot:
    learning_rate: 0.05
    initial_variance: 0.5
    confidence_threshold: 0.8
  double_auction:
    min_order_size: 1
    max_spread: 0.1
    clearing_mechanism: midpoint
```

## Development

### Prerequisites
- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Docker & Docker Compose

### Local Setup
```bash
# Clone and setup
cd services/auction-house-service-go
go mod download

# Run database migrations
liquibase update

# Start service
go run cmd/server/main.go
```

### Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Performance tests
go test -bench=. -benchmem ./...
```

## Deployment

### Docker Build
```dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o auction-house ./cmd/server

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/auction-house .
CMD ["./auction-house"]
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auction-house
spec:
  replicas: 3
  selector:
    matchLabels:
      app: auction-house
  template:
    metadata:
      labels:
        app: auction-house
    spec:
      containers:
      - name: auction-house
        image: necpgame/auction-house:latest
        ports:
        - containerPort: 8080
        env:
        - name: AUCTION_HOUSE_DB_HOST
          value: "postgres-service"
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

## Monitoring

### Key Metrics
- **Auction completion rate**: >95%
- **Average bid-to-trade conversion**: >80%
- **Market efficiency scores**: >85%
- **Price prediction accuracy**: >75%
- **Response time P99**: <50ms

### Health Checks
- Database connectivity
- Redis availability
- Market data freshness
- Algorithm convergence

## Security

### Anti-cheat Measures
- Bid manipulation detection
- Price anomaly monitoring
- Player behavior analytics
- Rate limiting and throttling

### Data Protection
- Encrypted sensitive trading data
- Audit logging for all transactions
- GDPR compliance for player data
- Secure API authentication

## Related Services

### Dependencies
- **Game Mechanics Master Index** - Item definitions and validation
- **Player Service** - Authentication and wallet management
- **Economy Service** - Currency transactions and balances
- **Analytics Engine** - Market trend analysis and reporting

### Integration Points
- **Real-time WebSocket Service** - Live price updates
- **Notification Service** - Bid alerts and trade confirmations
- **Anti-cheat System** - Fraud detection and prevention

## Roadmap

### Phase 1 (Current)
- Basic auction mechanics
- BazaarBot pricing algorithm
- Real-time market data

### Phase 2 (Next)
- Advanced analytics dashboard
- Mobile app integration
- Cross-region trading

### Phase 3 (Future)
- AI-powered market making
- Decentralized auction protocols
- NFT marketplace integration

## Contributing

### Code Standards
- Struct alignment optimization for memory efficiency
- Comprehensive error handling
- Extensive unit and integration tests
- Performance benchmarking

### Review Process
- Architecture review for new features
- Performance impact assessment
- Security audit for trading logic
- Database migration review

## License

MIT License - see LICENSE file for details.