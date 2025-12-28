# NECPGAME Scripts & Automation

This directory contains comprehensive automation scripts and systems for the NECPGAME MMOFPS project.

## 🏗️ **Systems Overview**

### 1. **Core Error Handling & Logging** (`core/error-handling/`)
Enterprise-grade error handling, structured logging, and HTTP middleware for all Go services.

**Features:**
- Structured error types with HTTP status mapping
- Correlation ID tracking across services
- Structured JSON logging with Zap
- HTTP middleware stack (recovery, logging, auth, rate limiting)
- Response helpers with consistent API format

**Usage:**
```go
// Structured error handling
err := errors.NewValidationError("INVALID_INPUT", "Player ID required")
err.WithField("field", "playerId")

// Enhanced logging
logger.WithRequestID(requestID).LogError(err, "Validation failed")

// HTTP middleware
r.Use(errorhandling.LoggingMiddleware(logger))
r.Use(errorhandling.ErrorHandler(logger))
r.Use(errorhandling.AuthMiddleware(logger))
```

### 2. **Performance Monitoring** (`performance-monitoring/`)
Real-time performance monitoring, alerting, and analysis for MMOFPS systems.

**Features:**
- 25+ Prometheus metrics for game sessions, combat, network, DB
- Intelligent alerting with Slack/Discord/Email notifications
- Real-time performance analysis with automated reports
- Customizable alert rules and thresholds
- Resource monitoring (CPU, memory, network)

**Key Metrics:**
- Combat response time P95 <100ms
- Network latency <50ms
- Error rate <5%
- Cache hit rate >90%
- Session drop rate <2%

### 3. **OpenAPI Tools** (`openapi/`)
Enterprise-grade OpenAPI specification management, refactoring, and code generation tools.

**Features:**
- **DRY Compliance Analysis** - Field duplication detection and BASE-ENTITY recommendations
- **Self-Contained Domains** - Eliminate external dependencies, enable direct code generation
- **Structure Standardization** - Migrate to enterprise-grade domain architecture
- **BASE-ENTITY Migration** - Automated conversion to composition-based schemas
- **Python Bundling** - Node.js-free OpenAPI bundling for ogen compatibility
- **Advanced Code Generation** - External reference support with automatic bundling
- **Comprehensive Validation** - Migration results, reference integrity, generation testing

**Available Tools:**
```bash
# 🔍 ANALYSIS & PLANNING
# Analyze field duplication for DRY compliance
python scripts/openapi/analyze-entity-fields.py proto/openapi/social-domain/

# 📦 SELF-CONTAINMENT (RECOMMENDED APPROACH)
# Make domains autonomous - eliminates bundling needs
python scripts/openapi/domain_self_containment.py companion-domain --embed-base-entity --validate

# 🏗️ STRUCTURE MIGRATION
# Standardize domain architecture
python scripts/openapi/migrate-domain-structure.py social-domain --execute

# 🔄 BASE-ENTITY MIGRATION
# Convert to composition-based schemas
python scripts/openapi/migrate-to-base-entity.py proto/openapi/social-domain/ --all-entities --execute

# ✅ VALIDATION & TESTING
# Comprehensive migration validation
python scripts/openapi/validate-migration.py proto/openapi/companion-domain/

# 📋 BUNDLING (ALTERNATIVE APPROACH)
# Python-based OpenAPI bundling (no Node.js needed)
python scripts/openapi/openapi_bundler.py spec.yaml --output bundled.yaml

# 🚀 CODE GENERATION
# Advanced generation with external reference support
python scripts/openapi/openapi_code_generator.py spec.yaml --target generated/ --validate

# 🛠️ UTILITIES
# Fix broken references after migration
python scripts/openapi/fix-refs-after-migration.py proto/openapi/domain/
```

**Recommended Workflow:**
```bash
# 1. Analyze current domain state
python scripts/openapi/analyze-entity-fields.py proto/openapi/companion-domain/

# 2. Make domain self-contained (preferred)
python scripts/openapi/domain_self_containment.py companion-domain --embed-base-entity --validate

# 3. Generate Go code directly (no bundling needed!)
ogen --target services/companion-domain-service-go/pkg/api --package api proto/openapi/companion-domain/main.yaml

# 4. Build and validate
cd services/companion-domain-service-go && go build . && go test ./...
```

### 4. **Data Synchronization** (`data-sync/`)

### 5. **Load Testing Suite** (`load-testing/`)
Comprehensive load testing for 10k+ concurrent users.

**Features:**
- Multi-service load testing (combat, matchmaking, inventory, economy)
- Real-time metrics collection and bottleneck detection
- Distributed testing across multiple machines
- WebSocket and HTTP concurrent load
- Automated scaling recommendations

**Supported Tests:**
- Combat performance (damage, kills, abilities)
- Matchmaking queue times and success rates
- Inventory operations (equip, trade, craft)
- Economy transactions (buy, sell, auctions)

### 6. **Backup & Recovery** (`backup/`)
Enterprise backup and disaster recovery system.

**Features:**
- Multi-storage backend support (local, S3, GCS)
- Compression and encryption
- Point-in-time recovery
- Automated retention policies
- Data integrity verification

**Supported Data Sources:**
- PostgreSQL databases
- Redis caches
- File systems
- Custom data sources

### 7. **Reports** (`reports/`)
Storage for script execution reports and analysis results.

**Contents:**
- Migration reports (BASE-ENTITY, structure changes)
- Validation reports (OpenAPI compliance)
- Analysis reports (field duplication, DRY metrics)
- Performance reports (load testing results)

## 🚀 **Quick Start**

### Initialize Core Systems
```bash
# 1. Set up error handling for all services
cd scripts/core/error-handling
python3 apply-to-services.py
./update_go_modules.sh

# 2. Configure performance monitoring
cd ../performance-monitoring
# Edit configuration files and start monitoring

# 3. Set up load testing
cd ../../load-testing
python3 mmofps_load_tester.py --type combat --clients 1000 --duration 300

# 4. Configure data synchronization
cd ../data-sync
# Configure sync nodes and start synchronization

# 5. Set up backup system
cd ../backup
# Configure backup schedules and storage backends
```

### Run Comprehensive Testing
```bash
# Run full system validation
python3 final-qa-testing.py

# Performance testing
python3 test_synchronization_performance.py --type combat --clients 100 --duration 60

# Load testing with 10k users
python3 mmofps_load_tester.py --type full --clients 10000 --duration 600
```

## 📊 **Performance Targets**

| Component | Target | Current Status |
|-----------|--------|----------------|
| Combat Response Time | P95 <100ms | ✅ Implemented |
| Network Latency | <50ms | ✅ Implemented |
| Error Rate | <5% | ✅ Implemented |
| Cache Hit Rate | >90% | ✅ Implemented |
| Session Drop Rate | <2% | ✅ Implemented |
| Concurrent Users | 10k+ | ✅ Tested |
| DB Query Time | P99 <50ms | ✅ Optimized |
| Memory Usage | <30MB/service | ✅ Optimized |

## 🔧 **Configuration**

### Global Configuration
```yaml
# config.yaml
global:
  environment: production
  log_level: info
  monitoring:
    prometheus: true
    grafana: true
  backup:
    retention_days: 30
    compression: true
    encryption: true
```

### Service-Specific Configuration
```yaml
# Service configuration
combat_service:
  monitoring:
    enabled: true
    alert_thresholds:
      response_time_ms: 100
      error_rate: 0.05

backup_service:
  schedules:
    - name: daily_database_backup
      type: postgresql
      schedule: "0 2 * * *"  # Daily at 2 AM
      retention: 30
```

## 📈 **Monitoring Dashboard**

### Key Metrics to Monitor
- **System Health**: CPU, memory, disk usage
- **Game Performance**: Response times, error rates
- **Player Experience**: Session lengths, drop rates
- **Business Metrics**: Revenue, player retention
- **Security**: Failed logins, suspicious activities

### Alert Thresholds
- Response time >100ms (Warning)
- Error rate >5% (Error)
- Memory usage >80% (Warning)
- Network latency >50ms (Critical)
- Session drops >2% (Error)

## 🚨 **Alerting**

### Notification Channels
- **Slack**: Real-time alerts for critical issues
- **Email**: Daily/weekly reports and warnings
- **Discord**: Community alerts for major outages
- **PagerDuty**: Critical system alerts

### Alert Rules
```yaml
alerts:
  - name: high_response_time
    condition: response_time > 100ms for 5m
    severity: warning
    channels: [slack, email]

  - name: high_error_rate
    condition: error_rate > 5% for 1m
    severity: error
    channels: [slack, pagerduty]

  - name: service_down
    condition: health_check fails for 30s
    severity: critical
    channels: [slack, pagerduty, email]
```

## 🔄 **CI/CD Integration**

### Automated Testing
```bash
# Run all tests
make test

# Load testing in CI
make load-test-clients=1000-duration=60

# Performance validation
make perf-test

# Security scanning
make security-scan
```

### Deployment Pipeline
```yaml
# .github/workflows/deploy.yml
- name: Run QA Tests
  run: |
    python3 scripts/final-qa-testing.py

- name: Load Testing
  run: |
    python3 scripts/load-testing/mmofps_load_tester.py --type smoke --clients 100 --duration 30

- name: Deploy to Staging
  run: |
    helm upgrade necpgame-staging ./k8s/helm/necpgame --set global.environment=staging
```

## 📚 **Documentation**

- [Error Handling Guide](core/error-handling/README.md)
- [Performance Monitoring](performance-monitoring/README.md)
- [Load Testing Suite](load-testing/README.md)
- [Data Synchronization](data-sync/README.md)
- [Backup & Recovery](backup/README.md)
- [OpenAPI Tools](openapi/README.md)
- [Reports](reports/)
- [Kubernetes Deployment](k8s/helm/necpgame/README.md)

## 🤝 **Contributing**

1. Follow Go coding standards and project structure
2. Add comprehensive tests for new features
3. Update documentation and examples
4. Ensure CI/CD passes for all changes
5. Add performance benchmarks for new components

## 📞 **Support**

For issues and questions:
- Check existing documentation
- Review GitHub issues
- Contact the development team
- Check monitoring dashboards for system status

---

**Built for scale. Designed for performance. Ready for production.**
