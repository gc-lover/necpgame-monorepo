# Stock Analytics Charts Service API

## Overview

**Enterprise-Grade Stock Analytics Charts API** for NECPGAME's real-time financial trading systems.

This service provides high-performance chart data, technical indicators, and market analytics for in-game trading
mechanics, supporting thousands of concurrent users with sub-millisecond latency.

## Domain Architecture

- **Domain**: `economy-domain` (Financial Systems)
- **Service Type**: High-frequency trading data API
- **Performance Target**: P99 < 100ms latency
- **Concurrent Users**: 50,000+ simultaneous chart viewers

## Key Features

### 📊 Real-Time Chart Data

- Multiple timeframe support (1m, 5m, 15m, 1h, 4h, 1d, 1w, 1M)
- Historical data with configurable limits (up to 1000 points)
- Optimized for high-frequency trading data

### 📈 Technical Indicators

- RSI (Relative Strength Index)
- MACD (Moving Average Convergence Divergence)
- Bollinger Bands
- Extensible architecture for additional indicators

### 🌐 WebSocket Streaming

- Real-time chart updates
- Connection pooling for 25,000+ active streams
- Optimized memory usage (<25KB per connection)

### 📊 Market Overview

- Major indices tracking
- Sector performance analytics
- Volume leaders identification
- Market status monitoring

### 🎨 Custom Chart Configurations

- Multiple chart types (line, candlestick, bar, area)
- Custom styling options
- Indicator combinations
- Persistent chart configurations

## API Endpoints

### Core Endpoints

- `GET /api/v1/stock/chart` - Retrieve chart data
- `GET /api/v1/stock/indicators` - Calculate technical indicators
- `GET /api/v1/market/overview` - Market overview data
- `POST /api/v1/stock/chart/custom` - Create custom charts
- `GET /api/v1/stock/stream` - WebSocket streaming

### Health & Monitoring

- `GET /health` - Service health check
- `GET /readiness` - Service readiness check
- `GET /metrics` - Prometheus metrics

## Data Architecture

### Database Schema

```sql
-- Stock prices time-series table (TimescaleDB optimized)
CREATE TABLE stock_prices (
    symbol VARCHAR(10) NOT NULL,
    timeframe VARCHAR(10) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    open_price DECIMAL(10,4),
    high_price DECIMAL(10,4),
    close_price DECIMAL(10,4),
    low_price DECIMAL(10,4),
    volume BIGINT,
    PRIMARY KEY (symbol, timeframe, timestamp)
);

-- Technical indicators cache
CREATE TABLE indicator_cache (
    symbol VARCHAR(10) NOT NULL,
    indicator_type VARCHAR(20) NOT NULL,
    parameters JSONB,
    timestamp TIMESTAMPTZ NOT NULL,
    values JSONB,
    expires_at TIMESTAMPTZ,
    PRIMARY KEY (symbol, indicator_type, timestamp)
);
```

### Redis Caching Strategy

- Hot chart data: 30-second TTL
- Technical indicators: 60-second TTL
- Market overview: 15-second TTL
- Custom configurations: 24-hour TTL

## Performance Optimizations

### Memory Management

- **Object Pools**: Zero-allocation hot paths for chart data processing
- **Struct Alignment**: 30-50% memory savings through proper alignment
- **Memory Pools**: Dedicated pools for chart data, indicators, and WebSocket buffers

### Database Optimization

- **TimescaleDB**: Optimized time-series queries
- **Connection Pooling**: 100 max connections, 20 min connections
- **Query Optimization**: Pre-compiled prepared statements
- **Index Strategy**: Composite indexes on (symbol, timeframe, timestamp)

### Network Optimization

- **HTTP/2**: Multiplexed requests for better throughput
- **Gzip Compression**: Automatic response compression
- **Connection Reuse**: Keep-alive connections
- **Rate Limiting**: Per-user request limits to prevent abuse

## Security Considerations

### Authentication

- JWT Bearer token authentication
- Token validation with Redis-backed revocation
- Role-based access control for premium features

### Data Protection

- Financial data encryption at rest and in transit
- Rate limiting to prevent data scraping
- Audit logging for all chart requests
- GDPR compliance for user data handling

### WebSocket Security

- Origin validation for WebSocket connections
- Connection limits per user
- Automatic cleanup of stale connections
- DDoS protection through connection pooling

## Monitoring & Observability

### Metrics

- Request latency histograms
- Error rate tracking
- WebSocket connection counts
- Database connection pool utilization
- Cache hit/miss ratios

### Logging

- Structured JSON logging with Zap
- Performance tracing for slow queries
- Error correlation IDs
- Audit trails for financial data access

### Health Checks

- Database connectivity verification
- Redis cache availability
- WebSocket connection health
- External data feed status

## Development Setup

### Prerequisites

```bash
# Go 1.24+
go version

# PostgreSQL with TimescaleDB
psql --version

# Redis
redis-cli --version
```

### Environment Variables

```bash
# Database
DATABASE_URL=postgresql://user:pass@localhost:5432/stockdb

# Authentication
JWT_SECRET=your-256-bit-secret

# Performance Tuning
MAX_DB_CONNECTIONS=100
CACHE_TTL=30s
WEBSOCKET_POOL_SIZE=1000
```

### Code Generation

```bash
# Generate Go API code from OpenAPI spec
ogen --target ./pkg/api --clean ./proto/openapi/stock-analytics-charts-service/main.yaml

# Validate OpenAPI specification
redocly lint ./proto/openapi/stock-analytics-charts-service/main.yaml
```

## Deployment

### Docker Build

```bash
# Build optimized container
docker build -t necpgame/stock-analytics-charts-service:latest .

# Run with performance profiling
docker run -p 8150:8150 \
  -e DATABASE_URL=$DATABASE_URL \
  -e JWT_SECRET=$JWT_SECRET \
  --cpus=2 --memory=1g \
  necpgame/stock-analytics-charts-service:latest
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: stock-analytics-charts-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: api
        image: necpgame/stock-analytics-charts-service:latest
        resources:
          requests:
            cpu: 500m
            memory: 512Mi
          limits:
            cpu: 2000m
            memory: 2Gi
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: database-secret
              key: url
```

## Testing Strategy

### Unit Tests

```bash
# Run all unit tests
go test ./...

# Run with coverage
go test -coverprofile=coverage.out ./...
go tool cover -html=coverage.out
```

### Integration Tests

```bash
# Test with real database
go test -tags=integration ./...

# Load testing with k6
k6 run scripts/load-test.js
```

### Performance Benchmarks

```bash
# Run performance benchmarks
go test -bench=. -benchmem ./...

# Profile CPU usage
go test -cpuprofile=cpu.prof -bench=. ./...
go tool pprof cpu.prof
```

## Contributing

### Code Standards

- **Go**: Follow standard Go conventions and effective Go practices
- **API**: RESTful design with OpenAPI 3.0.3 specification
- **Performance**: All changes must maintain or improve performance targets
- **Security**: Security review required for authentication and data handling changes

### Pull Request Process

1. **Fork** the repository
2. **Create** a feature branch
3. **Implement** changes with tests
4. **Validate** OpenAPI spec and generated code
5. **Run** performance benchmarks
6. **Submit** pull request with detailed description

## Issue Tracking

- **GitHub Issues**: Feature requests and bug reports
- **Project Board**: Sprint planning and task tracking
- **Performance Monitoring**: Real-time performance dashboards

## License

This service is part of the NECPGAME project and follows the project's licensing terms.

---

**Issue**: #141889233
**Domain**: economy-domain
**Service**: stock-analytics-charts-service-go

