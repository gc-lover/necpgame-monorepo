# Stock Analytics Tools Service

Advanced financial analytics tools service for complex quantitative analysis and algorithmic trading strategies in the NECPGAME MMOFPS RPG economy system.

## Overview

This service provides enterprise-grade financial analysis tools including fundamental analysis, technical analysis, portfolio optimization, risk assessment, price prediction using machine learning, and strategy backtesting.

## Features

### 🚀 High-Performance Analytical Engine
- **Fundamental Analysis**: Company valuation, financial ratios, analyst ratings
- **Technical Analysis**: Advanced indicators, pattern recognition, trend analysis
- **Portfolio Optimization**: Modern Portfolio Theory, risk-adjusted returns
- **Risk Assessment**: VaR, stress testing, scenario analysis
- **Price Prediction**: ML-based forecasting with multiple models
- **Strategy Backtesting**: Historical performance analysis

### 📊 Advanced Analytics Capabilities
- **Correlation Analysis**: Multi-asset correlation matrices
- **Volatility Analysis**: GARCH models, volatility cones
- **Concurrent Processing**: Controlled parallelism for expensive computations
- **Real-time Data Processing**: Optimized for financial market data

### 🔬 Machine Learning Integration
- **Ensemble Models**: Multiple prediction algorithms
- **Neural Networks**: Deep learning for complex patterns
- **Time Series Analysis**: ARIMA, LSTM models
- **Model Validation**: Cross-validation and accuracy metrics

## API Endpoints

### Health & Monitoring
- `GET /health` - Service health check
- `GET /ready` - Service readiness check
- `GET /metrics` - Prometheus metrics

### Fundamental Analysis
- `GET /api/v1/analysis/fundamental/{symbol}` - Comprehensive fundamental analysis

### Technical Analysis
- `GET /api/v1/analysis/technical/{symbol}` - Advanced technical analysis

### Portfolio Management
- `POST /api/v1/portfolio/optimization` - Portfolio optimization using MPT

### Risk Management
- `POST /api/v1/risk/assessment` - Comprehensive risk assessment

### Predictive Analytics
- `GET /api/v1/prediction/price/{symbol}` - ML-based price prediction

### Strategy Testing
- `POST /api/v1/backtesting/strategy` - Strategy backtesting with historical data

### Statistical Analysis
- `GET /api/v1/analysis/correlations` - Correlation analysis between assets
- `GET /api/v1/analysis/volatility/{symbol}` - Advanced volatility analysis

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8151` | Service port |
| `DATABASE_URL` | required | PostgreSQL connection string |
| `JWT_SECRET` | required | JWT signing secret |
| `LOG_LEVEL` | `info` | Logging level (debug, info, warn, error) |
| `READ_TIMEOUT` | `30s` | HTTP read timeout |
| `WRITE_TIMEOUT` | `30s` | HTTP write timeout |
| `IDLE_TIMEOUT` | `60s` | HTTP idle timeout |
| `MAX_DB_CONNECTIONS` | `200` | Maximum database connections |
| `MIN_DB_CONNECTIONS` | `50` | Minimum database connections |
| `DB_CONN_MAX_LIFETIME` | `1h` | Database connection max lifetime |
| `DB_CONN_MAX_IDLE_TIME` | `30m` | Database connection max idle time |
| `CALCULATION_TIMEOUT` | `5m` | Timeout for analytical calculations |
| `MAX_CONCURRENT_ANALYSIS` | `10` | Maximum concurrent analysis operations |
| `CACHE_TTL` | `1h` | Cache TTL for expensive calculations |
| `ML_MODEL_PATH` | `/models` | Path to ML model files |

## Database Schema

The service requires extensive financial data tables:

```sql
-- Fundamental data tables
CREATE TABLE company_fundamentals (
    symbol VARCHAR(10) PRIMARY KEY,
    company_name TEXT,
    sector VARCHAR(100),
    industry VARCHAR(100),
    market_cap BIGINT,
    employees INTEGER,
    last_updated TIMESTAMPTZ
);

-- Financial ratios table
CREATE TABLE financial_ratios (
    symbol VARCHAR(10) REFERENCES company_fundamentals(symbol),
    date DATE,
    pe_ratio DECIMAL(10,4),
    pb_ratio DECIMAL(10,4),
    roe DECIMAL(10,4),
    roa DECIMAL(10,4),
    debt_to_equity DECIMAL(10,4),
    current_ratio DECIMAL(10,4),
    PRIMARY KEY (symbol, date)
);

-- Technical indicators cache
CREATE TABLE technical_indicators (
    symbol VARCHAR(10),
    timeframe VARCHAR(10),
    indicator_name VARCHAR(50),
    timestamp TIMESTAMPTZ,
    value DECIMAL(20,8),
    PRIMARY KEY (symbol, timeframe, indicator_name, timestamp)
);

-- ML model predictions
CREATE TABLE price_predictions (
    symbol VARCHAR(10),
    model_name VARCHAR(50),
    prediction_date DATE,
    predicted_price DECIMAL(15,4),
    confidence DECIMAL(5,4),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (symbol, model_name, prediction_date)
);

-- Backtesting results
CREATE TABLE backtest_results (
    strategy_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    strategy_name TEXT,
    symbols TEXT[],
    start_date DATE,
    end_date DATE,
    total_return DECIMAL(10,4),
    volatility DECIMAL(10,4),
    sharpe_ratio DECIMAL(10,4),
    max_drawdown DECIMAL(10,4),
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

## Performance Optimizations

### Computation Management
- **Concurrency Control**: Semaphore-based limiting of expensive operations
- **Memory Pools**: Specialized pools for different analytical objects
- **Timeout Management**: Configurable timeouts for different operation types
- **Resource Limits**: CPU and memory limits for heavy computations

### Database Optimizations
- **Connection Pooling**: High-connection pool for analytical workloads
- **Query Optimization**: Efficient queries for time-series data
- **Indexing Strategy**: Optimized indexes for analytical queries
- **Caching Layer**: Redis integration for expensive calculations

### Algorithm Optimizations
- **Vectorized Operations**: NumPy-style operations using Gonum
- **Parallel Processing**: Concurrent analysis of multiple assets
- **Memory Efficiency**: Streaming processing for large datasets
- **Precision Control**: Configurable numerical precision

## Machine Learning Models

### Supported Models
- **Linear Regression**: Basic trend prediction
- **Neural Networks**: Complex pattern recognition
- **Ensemble Methods**: Combined model predictions
- **Time Series Models**: ARIMA, LSTM networks

### Model Management
- **Model Loading**: Runtime model loading from configurable paths
- **Version Control**: Model versioning and rollback capabilities
- **Accuracy Tracking**: Performance monitoring and alerting
- **Retraining Pipeline**: Automated model updates

## Risk Management

### Risk Metrics
- **Value at Risk (VaR)**: Statistical risk measurement
- **Expected Shortfall**: Tail risk assessment
- **Stress Testing**: Scenario-based risk analysis
- **Beta Calculation**: Market risk measurement

### Portfolio Risk
- **Correlation Analysis**: Asset relationship assessment
- **Volatility Modeling**: GARCH and other volatility models
- **Diversification Metrics**: Portfolio concentration analysis
- **Drawdown Analysis**: Maximum loss assessment

## Development

### Prerequisites

- Go 1.24+
- PostgreSQL 13+
- Redis (optional, for caching)
- Python 3.8+ (for ML model training)

### Quick Start

1. **Setup environment:**
   ```bash
   cd services/stock-analytics-tools-service-go
   go mod download
   ```

2. **Configure environment:**
   ```bash
   export DATABASE_URL="postgres://user:pass@localhost:5432/analytics?sslmode=disable"
   export JWT_SECRET="your-secret-key"
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
   ./bin/stock-analytics-tools-service
   ```

## Testing

### Unit Tests
```bash
go test -v ./internal/...
```

### Integration Tests
```bash
go test -v -tags=integration ./tests/
```

### Performance Tests
```bash
go test -bench=. -benchmem ./internal/calculations/
```

### Load Testing
```bash
# Test portfolio optimization under load
ab -n 100 -c 5 -p portfolio_request.json -T application/json http://localhost:8151/api/v1/portfolio/optimization
```

## Deployment

### Docker Deployment
```bash
# Build with ML dependencies
make docker-build

# Run with resource limits
docker run --rm -p 8151:8151 \
  --memory=4g --cpus=2 \
  -e DATABASE_URL="postgres://..." \
  -e JWT_SECRET="..." \
  -v /path/to/models:/models \
  stock-analytics-tools-service:latest
```

### Kubernetes Deployment
See `k8s/` directory for:
- Horizontal Pod Autoscaling based on CPU and custom metrics
- Resource limits and requests for analytical workloads
- Persistent volumes for ML models
- ConfigMaps for analytical parameters

### Cloud Deployment
- **AWS**: ECS/Fargate with GPU support for ML workloads
- **GCP**: Cloud Run with Vertex AI integration
- **Azure**: AKS with Azure Machine Learning

## Monitoring

### Key Metrics
- Analysis operation latency by type
- Concurrent analysis operations gauge
- Database connection pool utilization
- ML model prediction accuracy
- Memory usage per analysis type
- Cache hit/miss ratios

### Alerting
- Analysis queue full alerts
- High latency operation alerts
- Database connection issues
- ML model accuracy degradation

## Security

### Data Protection
- JWT-based authentication for all endpoints
- Input validation and sanitization
- SQL injection prevention
- Financial data encryption at rest

### Access Control
- Role-based permissions for different analysis types
- API rate limiting per user/organization
- Audit logging for sensitive operations
- Financial regulation compliance (SOX, GDPR)

## Contributing

1. Follow Go best practices and performance guidelines
2. Add comprehensive tests for analytical functions
3. Document mathematical models and algorithms
4. Update performance benchmarks
5. Run full test suite: `make test`

## API Documentation

Complete OpenAPI 3.0 specification available at:
`proto/openapi/economy-domain/analytics/stock-analytics-tools-service/main.yaml`

## License

MIT License - see LICENSE file for details.

---

**Issue:** #141889238
**Service:** Stock Analytics Tools Service
**Performance:** Enterprise-grade quantitative financial analysis
