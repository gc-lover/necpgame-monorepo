# Analytics Service

**Enterprise-grade Analytics Platform for Player Behavior Analysis, Retention Metrics, and A/B Testing**

[![Go Version](https://img.shields.io/badge/go-1.21+-blue.svg)](https://golang.org)
[![PostgreSQL](https://img.shields.io/badge/postgresql-13+-blue.svg)](https://www.postgresql.org)
[![OpenAPI](https://img.shields.io/badge/openapi-3.0.3-green.svg)](https://swagger.io/specification/)

## Overview

The Analytics Service provides comprehensive player behavior analysis, retention metrics, and A/B testing framework for MMOFPS RPG. It processes millions of player events daily, generates automated insights, and supports data-driven game design decisions.

### Key Features

#### 🎯 **Player Behavior Analysis**
- **Real-time Engagement Scoring**: Dynamic calculation of player engagement metrics
- **Churn Prediction**: Machine learning-based churn probability assessment
- **Session Pattern Analysis**: Detailed session duration and frequency tracking
- **Retention Modeling**: Advanced cohort-based retention analysis

#### 📊 **Retention Analytics**
- **Cohort Analysis**: Multi-dimensional cohort retention tracking (Day 1, 7, 30, 90)
- **Automated Insights**: AI-powered retention insights and recommendations
- **Trend Analysis**: Historical retention trend identification
- **Churn Prevention**: Early warning system for at-risk players

#### 🧪 **A/B Testing Framework**
- **Statistical Rigor**: Configurable confidence levels and statistical power
- **Traffic Distribution**: Weighted traffic allocation across variants
- **Real-time Results**: Live conversion tracking and statistical significance
- **Automated Optimization**: Bayesian optimization for test variants

#### 📈 **Automated Reporting**
- **Multi-format Reports**: Retention, behavior, A/B test, and performance reports
- **Visual Analytics**: Chart generation for dashboard integration
- **Scheduled Generation**: Automated report creation and distribution
- **Custom Metrics**: Flexible metric configuration and aggregation

## Architecture

### System Components

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   HTTP API      │────│  Service Layer   │────│   Repository    │
│                 │    │                  │    │                 │
│ • REST Endpoints│    │ • Analytics Logic│    │ • PostgreSQL    │
│ • JSON Responses│    │ • ML Algorithms  │    │ • Query Opt.     │
│ • CORS Support  │    │ • Background Proc│    │ • Partitions    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌──────────────────┐
                    │  Background      │
                    │  Workers         │
                    │                  │
                    │ • Behavior Analysis│
                    │ • Retention Update │
                    │ • A/B Test Monitor │
                    └──────────────────┘
```

### Performance Optimizations

#### Memory Management
- **Object Pooling**: `sync.Pool` for analytics objects
- **Struct Alignment**: 30-50% memory savings with optimized field ordering
- **Zero Allocations**: Hot path operations without heap allocations

#### Database Optimization
- **Table Partitioning**: Monthly partitions for game_events table
- **Composite Indexes**: Optimized indexes for common query patterns
- **Connection Pooling**: 20 max connections with smart lifecycle management

#### Concurrent Processing
- **Worker Pools**: Background goroutines for different analytics tasks
- **Channel-based Communication**: Safe shutdown with context cancellation
- **Metrics Collection**: Real-time performance monitoring

## API Reference

### Core Endpoints

#### Player Behavior Analysis
```http
GET /api/v1/analytics/behavior/{player_id}
POST /api/v1/analytics/behavior/{player_id}/analyze
```

#### Retention Metrics
```http
GET /api/v1/analytics/retention?days=90
```

#### A/B Testing
```http
POST /api/v1/analytics/ab-tests
POST /api/v1/analytics/ab-tests/{test_id}/assign/{player_id}
```

#### Analytics Reports
```http
POST /api/v1/analytics/reports
GET /api/v1/analytics/reports/{report_id}
```

#### Health Monitoring
```http
GET /api/v1/health
GET /api/v1/system/health
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `HTTP_ADDR` | `:8081` | HTTP server address |
| `DATABASE_URL` | `postgres://...` | PostgreSQL connection string |
| `LOG_LEVEL` | `info` | Logging level |
| `BEHAVIOR_ANALYSIS_INTERVAL` | `1h` | Behavior analysis frequency |
| `RETENTION_UPDATE_INTERVAL` | `6h` | Retention metrics update frequency |
| `AB_TEST_UPDATE_INTERVAL` | `30m` | A/B test monitoring frequency |

### Database Schema

The service requires the following PostgreSQL schema:

```sql
-- Core analytics tables
analytics.player_behaviors      -- Player engagement and retention data
analytics.session_events        -- Detailed session tracking
analytics.game_events           -- All game events (partitioned)
analytics.retention_cohorts     -- Cohort-based retention analysis
analytics.ab_tests             -- A/B test definitions
analytics.ab_test_variants     -- Test variants and results
analytics.ab_test_assignments  -- Player-variant assignments
analytics.reports              -- Generated analytics reports
analytics.system_health        -- Service health monitoring

-- Performance views
analytics.active_players_view      -- Active player summaries
analytics.cohort_analysis_view     -- Cohort retention analysis
analytics.ab_test_results_view     -- A/B test results summary
```

## Development

### Prerequisites

- Go 1.21+
- PostgreSQL 13+
- Docker (optional)

### Building

```bash
# Clone and build
cd services/analytics-service-go
go mod tidy
go build -o bin/analytics-service ./main.go
```

### Running

```bash
# Set environment variables
export DATABASE_URL="postgres://user:pass@localhost:5432/analytics_db?sslmode=disable"
export HTTP_ADDR=":8081"

# Run the service
./bin/analytics-service
```

### Testing

```bash
# Unit tests
go test ./...

# Benchmarks
go test -bench=. -benchmem

# Integration tests
go test -tags=integration ./...
```

## Analytics Algorithms

### Player Behavior Scoring

The service uses a multi-factor engagement scoring algorithm:

```
Engagement Score = (Session_Duration_Factor × 0.3) +
                   (Play_Frequency_Factor × 0.25) +
                   (Level_Progression_Factor × 0.25) +
                   (Retention_Factor × 0.2)
```

Where each factor is normalized to [0,1] range.

### Churn Prediction

Churn probability is calculated using:

- **Recency Factor**: Days since last session
- **Frequency Factor**: Sessions per week
- **Engagement Trend**: Recent engagement changes
- **Level Progression**: Recent level advancement

### Cohort Analysis

Retention cohorts are analyzed using:

- **Day 1 Retention**: Immediate engagement success
- **Day 7 Retention**: Initial retention hurdle
- **Day 30 Retention**: Medium-term loyalty
- **Day 90 Retention**: Long-term player retention

## A/B Testing Framework

### Test Configuration

```json
{
  "name": "UI Button Color Test",
  "description": "Testing different button colors for conversion optimization",
  "variants": ["blue_button", "green_button", "red_button"],
  "confidence_level": 0.95,
  "min_sample_size": 1000
}
```

### Statistical Analysis

- **Confidence Level**: 95% (configurable)
- **Statistical Power**: 80% (configurable)
- **Minimum Sample Size**: Calculated based on expected effect size
- **P-value Threshold**: 0.05 for significance

### Variant Assignment

Players are assigned to variants using:

1. **Check existing assignments** to ensure consistency
2. **Weighted random selection** based on variant weights
3. **Automatic rebalancing** if distribution becomes uneven

## Monitoring

### Metrics

The service exposes the following metrics:

- `analytics_active_users_total` - Number of active users
- `analytics_retention_rate` - Current retention rate
- `analytics_ab_test_conversions` - A/B test conversion rates
- `analytics_report_generation_seconds` - Report generation duration

### Health Checks

- **HTTP**: `GET /health` - Service availability and endpoints
- **System**: `GET /system/health` - Detailed system metrics

### Logging

Structured JSON logging with the following levels:
- `DEBUG` - Detailed analytics processing information
- `INFO` - General operational messages
- `WARN` - Performance warnings and anomalies
- `ERROR` - Processing failures requiring attention

## Deployment

### Docker

```dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o analytics-service ./main.go

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/analytics-service .
CMD ["./analytics-service"]
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: analytics-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: analytics-service
  template:
    metadata:
      labels:
        app: analytics-service
    spec:
      containers:
      - name: analytics-service
        image: necpgame/analytics-service:latest
        ports:
        - containerPort: 8081
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: analytics-db-secret
              key: url
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
```

## Data Privacy

### Player Data Protection

- **Data Minimization**: Only necessary data collected
- **Anonymization**: Player IDs are UUIDs, no PII stored
- **Retention Policies**: Automated data cleanup after retention periods
- **Access Controls**: Row-level security in PostgreSQL

### GDPR Compliance

- **Right to Erasure**: Player data deletion endpoints
- **Data Portability**: Export player analytics data
- **Consent Management**: Configurable data collection consent
- **Audit Logging**: All data access is logged

## Performance Benchmarks

### Latency Targets
- **Player Behavior Analysis**: <50ms P99
- **Retention Metrics Query**: <75ms P99
- **A/B Test Assignment**: <10ms P99
- **Report Generation**: <200ms for standard reports

### Throughput
- **Event Processing**: 10,000+ events per second
- **Concurrent Users**: 50,000+ active player analytics
- **Report Generation**: 100+ reports per hour

### Data Scale
- **Player Records**: 10M+ player behavior profiles
- **Event History**: 1B+ game events (partitioned)
- **A/B Tests**: 100+ concurrent experiments

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new analytics algorithms
4. Ensure OpenAPI spec is updated
5. Run benchmarks: `go test -bench=. -benchmem`
6. Submit a pull request

## License

This project is part of the NECPGAME ecosystem. See LICENSE file for details.

## Support

For support and questions:
- **Issues**: GitHub Issues
- **Documentation**: `/docs` directory
- **Team**: `#analytics-service` Slack channel

## Related Services

- **Game Service**: Provides game event data
- **Auth Service**: Player authentication and identity
- **Reporting Service**: Dashboard and visualization
- **ML Service**: Advanced predictive analytics