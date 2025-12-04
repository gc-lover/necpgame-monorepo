# Services Dashboard

Comprehensive dashboard for monitoring microservices performance, health, and historical trends.

## Features

- **Service Status**: Health checks, uptime, request rates
- **Benchmark History**: Performance trends over time
- **Metrics Integration**: Prometheus metrics support
- **Trend Analysis**: Identify improving/degrading services
- **Historical Data**: Track changes across commits/versions

## API Endpoints

- `GET /api/services` - List all services with status
- `GET /api/benchmarks/history?service=X&benchmark=Y` - Benchmark history
- `GET /api/trends` - Performance trends
- `GET /api/summary` - Dashboard summary
- `GET /api/service/{name}` - Service details
- `GET /api/prometheus/?query=...` - Prometheus proxy

## Running

```bash
cd infrastructure/services-dashboard
go run main.go
```

Open http://localhost:8080

## Configuration

Set `PROMETHEUS_URL` environment variable to connect to Prometheus:
```bash
export PROMETHEUS_URL=http://localhost:9090
```

