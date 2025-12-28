# Stock Analytics Charts Service

High-performance stock analytics and charting service for the NECPGAME MMOFPS RPG economy system.

## Overview

This service provides real-time stock market data, technical analysis indicators, and interactive charting capabilities optimized for financial gameplay mechanics.

## Features

### 🚀 High-Performance Architecture
- **Real-time data processing** for financial markets
- **WebSocket streaming** for live chart updates
- **Optimized memory pools** for chart calculations
- **Enterprise-grade logging** with structured JSON output

### 📊 Technical Analysis
- **RSI (Relative Strength Index)** calculations
- **MACD (Moving Average Convergence Divergence)** indicators
- **Bollinger Bands** for volatility analysis
- **Custom indicator combinations**

### 📈 Chart Types
- **Candlestick charts** with OHLC data
- **Line charts** for trend analysis
- **Area charts** for volume visualization
- **Custom chart configurations**

### 🔄 Real-time Streaming
- **WebSocket connections** for live data
- **Market overview** with major indices
- **Sector performance** tracking
- **Volume leader analysis**

## API Endpoints

### Health & Monitoring
- `GET /health` - Service health check
- `GET /ready` - Service readiness check
- `GET /metrics` - Prometheus metrics

### Charts & Analytics
- `GET /api/v1/stocks/{symbol}/charts/{timeframe}` - Get stock chart data
- `GET /api/v1/stocks/{symbol}/indicators` - Get technical indicators
- `GET /api/v1/stocks/market-overview` - Get market overview
- `GET /api/v1/stocks/{symbol}/realtime` - Stream real-time data (WebSocket)

### Custom Charts
- `POST /api/v1/charts/custom` - Create custom chart configuration

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8150` | Service port |
| `DATABASE_URL` | required | PostgreSQL connection string |
| `JWT_SECRET` | required | JWT signing secret |
| `LOG_LEVEL` | `info` | Logging level (debug, info, warn, error) |
| `READ_TIMEOUT` | `10s` | HTTP read timeout |
| `WRITE_TIMEOUT` | `10s` | HTTP write timeout |
| `IDLE_TIMEOUT` | `60s` | HTTP idle timeout |
| `MAX_DB_CONNECTIONS` | `100` | Maximum database connections |
| `MIN_DB_CONNECTIONS` | `20` | Minimum database connections |
| `DB_CONN_MAX_LIFETIME` | `1h` | Database connection max lifetime |
| `DB_CONN_MAX_IDLE_TIME` | `30m` | Database connection max idle time |
| `CACHE_TTL` | `30s` | Cache TTL for real-time data |
| `DATA_BATCH_SIZE` | `500` | Batch size for data processing |
| `WEBSOCKET_POOL_SIZE` | `1000` | WebSocket connection pool size |

### Database Schema

The service expects the following tables:

```sql
-- Stock prices table for chart data
CREATE TABLE stock_prices (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    timeframe VARCHAR(10) NOT NULL,
    open_price DECIMAL(15,4) NOT NULL,
    high_price DECIMAL(15,4) NOT NULL,
    low_price DECIMAL(15,4) NOT NULL,
    close_price DECIMAL(15,4) NOT NULL,
    volume BIGINT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Custom charts configuration
CREATE TABLE custom_charts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    chart_type VARCHAR(20) NOT NULL,
    timeframe VARCHAR(10) NOT NULL,
    indicators JSONB,
    style JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_stock_prices_symbol_timeframe_timestamp
ON stock_prices (symbol, timeframe, timestamp DESC);

CREATE INDEX idx_custom_charts_user_id
ON custom_charts (user_id);
```

## Development

### Prerequisites

- Go 1.24+
- PostgreSQL 13+
- Redis (optional, for caching)

### Quick Start

1. **Clone and setup:**
   ```bash
   cd services/stock-analytics-charts-service-go
   go mod download
   ```

2. **Environment setup:**
   ```bash
   export DATABASE_URL="postgres://user:pass@localhost:5432/stockdb?sslmode=disable"
   export JWT_SECRET="your-development-secret-key"
   export LOG_LEVEL="debug"
   ```

3. **Run database migrations:**
   ```bash
   make migrate-up
   ```

4. **Generate API code:**
   ```bash
   make generate-api
   ```

5. **Build and run:**
   ```bash
   make build
   ./bin/stock-analytics-charts-service
   ```

### Development Commands

```bash
# Full development pipeline
make all

# Run tests with coverage
make test

# Build optimized binary
make build

# Run linter
make lint

# Format code
make fmt

# Run in development mode
make dev

# Docker build and run
make docker-build
make docker-run
```

## Performance Optimizations

### Memory Management
- **Object pools** for chart data structures
- **Zero-allocation hot paths** for calculations
- **Optimized struct alignment** for cache efficiency

### Database Optimizations
- **Connection pooling** with configurable limits
- **Query optimization** with proper indexing
- **Batch processing** for large datasets

### Network Optimizations
- **WebSocket compression** for real-time data
- **HTTP/2 support** for concurrent requests
- **Timeout management** for different endpoints

## Monitoring

### Health Checks
- `/health` - Basic health check
- `/ready` - Readiness for traffic
- `/metrics` - Prometheus metrics

### Key Metrics
- Request latency by endpoint
- Database connection pool stats
- WebSocket connection count
- Chart calculation performance
- Memory usage patterns

## Security

### Authentication
- JWT-based authentication
- Role-based access control
- API key validation

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- Rate limiting for API endpoints
- CORS configuration

### Financial Data Security
- Encrypted data transmission
- Audit logging for sensitive operations
- Access control for market data

## Deployment

### Docker Deployment
```bash
# Build and run
make docker-build
docker run -p 8150:8150 \
  -e DATABASE_URL="postgres://..." \
  -e JWT_SECRET="..." \
  stock-analytics-charts-service:latest
```

### Kubernetes Deployment
See `k8s/` directory for Kubernetes manifests with:
- Horizontal Pod Autoscaling
- Resource limits and requests
- Health check probes
- ConfigMaps and Secrets

## Testing

### Unit Tests
```bash
go test -v ./server/
```

### Integration Tests
```bash
go test -v -tags=integration ./tests/
```

### Load Testing
```bash
make load-test
```

### Performance Testing
```bash
make profile
```

## API Documentation

Complete OpenAPI 3.0 specification available at:
`proto/openapi/economy-domain/analytics/stock-analytics-charts-service/main.yaml`

## Contributing

1. Follow Go best practices
2. Add tests for new features
3. Update documentation
4. Run full CI pipeline: `make all`

## License

MIT License - see LICENSE file for details.

---

**Issue:** #141889233
**Service:** Stock Analytics Charts Service
**Performance:** Enterprise-grade financial data processing


