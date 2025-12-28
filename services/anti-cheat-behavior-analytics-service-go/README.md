# Anti-Cheat Behavior Analytics Service

**Issue:** #2212 - Enterprise-grade anti-cheat behavior analytics service for MMOFPS RPG

## Overview

The Anti-Cheat Behavior Analytics Service provides real-time analysis of player behavior to detect cheating patterns in the NECPGAME MMORPG. It uses machine learning algorithms, statistical analysis, and rule-based detection to identify suspicious activities.

## Architecture

### Core Components

- **Analytics Engine**: ML-based behavioral pattern analysis
- **Detection Engine**: Real-time cheating pattern detection
- **Repository Layer**: PostgreSQL + Redis data persistence
- **HTTP API**: RESTful endpoints for management and monitoring
- **Kafka Integration**: Event-driven processing of game events

### Detection Rules

- **Aimbot Detection**: Accuracy and headshot pattern analysis
- **Speed Hack Detection**: Movement speed anomaly detection
- **Wallhack Detection**: Visibility pattern analysis
- **Macro Detection**: Automated input pattern recognition
- **Statistical Anomaly Detection**: Risk score-based analysis

## Quick Start

### Prerequisites

- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Kafka 2.8+

### Setup

1. **Clone and build:**
   ```bash
   cd services/anti-cheat-behavior-analytics-service-go
   make deps
   make build
   ```

2. **Configure database:**
   ```sql
   CREATE DATABASE necpgame;
   -- Run migration scripts from infrastructure/liquibase
   ```

3. **Configure services:**
   ```yaml
   # config.yaml
   database:
     host: "localhost"
     user: "postgres"
     password: "postgres"
   redis:
     addr: "localhost:6379"
   kafka:
     brokers: ["localhost:9092"]
   ```

4. **Run the service:**
   ```bash
   make run
   ```

## API Endpoints

### Health & Monitoring
- `GET /health` - Service health check
- `GET /metrics` - Prometheus metrics

### Player Analysis
- `GET /api/v1/anticheat/players/{playerId}/behavior` - Get player behavior data
- `GET /api/v1/anticheat/players/{playerId}/risk-score` - Get player risk score
- `POST /api/v1/anticheat/players/{playerId}/flag` - Flag player for review

### Match Analysis
- `GET /api/v1/anticheat/matches/{matchId}/analysis` - Analyze match data
- `GET /api/v1/anticheat/matches/{matchId}/anomalies` - Get match anomalies

### Statistics & Reporting
- `GET /api/v1/anticheat/statistics/summary` - Get statistics summary
- `GET /api/v1/anticheat/statistics/trends` - Get statistics trends
- `GET /api/v1/anticheat/statistics/top-risky` - Get top risky players

### Rule Management
- `GET /api/v1/anticheat/rules` - Get detection rules
- `POST /api/v1/anticheat/rules` - Create detection rule
- `PUT /api/v1/anticheat/rules/{ruleId}` - Update detection rule
- `DELETE /api/v1/anticheat/rules/{ruleId}` - Delete detection rule

### Alert Management
- `GET /api/v1/anticheat/alerts` - Get alerts
- `PUT /api/v1/anticheat/alerts/{alertId}/acknowledge` - Acknowledge alert
- `GET /api/v1/anticheat/alerts/{alertId}/details` - Get alert details

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `GOGC` | `75` | Go GC target percentage |
| `PORT` | `8080` | HTTP server port |
| `CONFIG_PATH` | `config.yaml` | Configuration file path |

### Configuration File

See `config.yaml` for detailed configuration options.

## Database Schema

### Tables

- `anticheat.player_behaviors` - Player behavior data
- `anticheat.detection_rules` - Detection rule configurations
- `anticheat.alerts` - Security alerts and notifications

## Event Processing

### Kafka Topics

- `game.anticheat.events` - Game events for analysis
- `game.anticheat.alerts` - Detected cheating alerts

### Event Types

```json
{
  "player_id": "player123",
  "session_id": "session456",
  "type": "aim_assist",
  "timestamp": "2024-01-01T12:00:00Z",
  "data": {
    "accuracy": 0.95,
    "headshots": 15,
    "total_shots": 20
  }
}
```

## Performance

### Targets

- **Latency**: P99 <50ms for API endpoints
- **Throughput**: 2000+ events/second processing
- **Memory**: <30MB per service instance
- **Concurrent Users**: 10,000+ simultaneous tracking

### Optimization Features

- Memory pooling for hot paths
- Redis caching for risk scores
- Connection pooling for database
- Worker pools for concurrent processing

## Monitoring

### Metrics

- Request latency and throughput
- Detection accuracy and false positives
- Database query performance
- Memory and CPU usage
- Kafka consumer lag

### Health Checks

- `/health` - Basic health check
- `/ready` - Readiness probe for Kubernetes

## Development

### Building

```bash
make build          # Build binary
make docker-build   # Build Docker image
make test           # Run tests
make lint           # Run linter
```

### Testing

```bash
make test               # Unit tests
make test-coverage      # Tests with coverage
make load-test          # Load testing
```

### Code Quality

```bash
make format     # Format code
make lint       # Run linter
make security-scan  # Security analysis
```

## Deployment

### Docker

```bash
docker build -t necpgame/anti-cheat-behavior-analytics .
docker run -p 8080:8080 necpgame/anti-cheat-behavior-analytics
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: anti-cheat-analytics
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: analytics
        image: necpgame/anti-cheat-behavior-analytics:latest
        ports:
        - containerPort: 8080
        env:
        - name: PORT
          value: "8080"
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

## Security

### Authentication

- JWT token validation
- API key authentication
- Rate limiting and DDoS protection

### Data Protection

- Encrypted data storage
- Secure API endpoints
- Input validation and sanitization

### Audit Logging

- Comprehensive audit trails
- Suspicious activity logging
- Alert notification system

## Contributing

1. Follow Go coding standards
2. Add unit tests for new features
3. Update documentation
4. Ensure CI/CD passes

## License

Proprietary - NECPGAME