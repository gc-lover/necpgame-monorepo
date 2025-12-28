# Security Service API - Enterprise-Grade Authentication & Anti-Cheat

## Overview

The Security Service API provides comprehensive authentication, authorization, and anti-cheat capabilities for the entire NECPGAME ecosystem. This enterprise-grade microservice implements a zero-trust security model with advanced threat detection and real-time monitoring.

## Architecture

### Core Components

- **JWT-based Authentication**: Secure token-based authentication with refresh token support
- **RBAC Authorization**: Role-based access control with granular permissions
- **Anti-Cheat Engine**: ML-powered validation with statistical anomaly detection
- **Threat Monitoring**: Real-time security event processing and alerting
- **Audit Logging**: Comprehensive security event logging for compliance

### Domain Boundaries

The Security Service owns all security-related operations:
- User authentication and session management
- Permission validation and access control
- Game action validation and cheat detection
- Security threat monitoring and response
- Audit trail maintenance

## API Endpoints

### Authentication (`/auth/*`)

| Endpoint | Method | Description | Performance |
|----------|--------|-------------|-------------|
| `/auth/login` | POST | User authentication with JWT tokens | <20ms P99 |
| `/auth/refresh` | POST | Refresh access token | <10ms average |
| `/auth/logout` | POST | Secure user logout | <5ms average |
| `/auth/permissions` | GET | Get current user permissions | <5ms cached |

### Authorization (`/auth/*`)

| Endpoint | Method | Description | Performance |
|----------|--------|-------------|-------------|
| `/auth/check-permission` | POST | Validate specific permission | <2ms bloom filter |

### Anti-Cheat (`/anticheat/*`)

| Endpoint | Method | Description | Performance |
|----------|--------|-------------|-------------|
| `/anticheat/validate` | POST | Validate game action | <50ms P99 |

### Threat Monitoring (`/security/*`)

| Endpoint | Method | Description | Performance |
|----------|--------|-------------|-------------|
| `/security/threats` | GET | Get security threats | <100ms large sets |

### Health Monitoring (`/*`)

| Endpoint | Method | Description | Performance |
|----------|--------|-------------|-------------|
| `/health` | GET | Service health check | <1ms cached |
| `/ready` | GET | Service readiness probe | <5ms |

## Security Features

### Authentication
- JWT access tokens with configurable expiration
- Refresh token rotation for enhanced security
- Secure password hashing with bcrypt
- Rate limiting on authentication endpoints (5 attempts/minute)

### Authorization
- Hierarchical role-based permissions
- Permission inheritance and delegation
- Context-aware permission checking
- Permission caching with TTL

### Anti-Cheat Validation
- Speed hack detection using timestamp analysis
- Aimbot pattern recognition with mouse movement analysis
- Wallhack detection using position validation
- Statistical anomaly detection with ML models
- Behavioral profiling and pattern matching

### Threat Monitoring
- Real-time security event processing
- AI-powered threat classification
- Automated response actions
- Integration with SIEM systems

## Performance Targets

- **Authentication**: <20ms average, <50ms P99
- **Permission Check**: <2ms average (bloom filter lookup)
- **Anti-Cheat Validation**: <50ms P99, batched processing
- **Threat Query**: <100ms for large result sets
- **Health Check**: <1ms cached response

## Scalability

- **Concurrent Users**: 100,000+ simultaneous validations
- **Security Events**: 10,000+ events/second processing
- **Memory Usage**: <30KB per active session
- **Database Pool**: PostgreSQL (50 max), Redis (20 max)

## Data Storage

### PostgreSQL Tables

```sql
-- User authentication data
CREATE TABLE security.users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    roles TEXT[] NOT NULL DEFAULT '{}',
    permissions TEXT[] NOT NULL DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Security threats and incidents
CREATE TABLE security.threats (
    id VARCHAR(255) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    description TEXT,
    user_id VARCHAR(255),
    ip_address INET,
    user_agent TEXT,
    location VARCHAR(255),
    confidence_score DECIMAL(3,2),
    detected_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    resolved_at TIMESTAMP WITH TIME ZONE,
    actions_taken TEXT[]
);

-- Authentication sessions
CREATE TABLE security.sessions (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    token_hash VARCHAR(255) NOT NULL,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE
);
```

### Redis Keys

```redis
# JWT token blacklist
security:tokens:blacklist:{token_hash}

# Permission cache
security:permissions:{user_id}

# Rate limiting
security:ratelimit:auth:{ip_address}

# Session data
security:sessions:{session_id}
```

## Integration Points

### Authentication Flow

```
Client → /auth/login → JWT Token Response
       ↓
Client → API Request + Bearer Token → Security Middleware
       ↓
Security Service → Validate Token → User Context
       ↓
API Handler → Business Logic → Response
```

### Permission Validation

```
API Handler → Check Permission Request → Security Service
       ↓
Security Service → Validate Permission → Boolean Response
       ↓
API Handler → Allow/Deny Access → Client Response
```

### Anti-Cheat Validation

```
Game Client → /anticheat/validate → Security Service
       ↓
Security Service → ML Validation → Validation Result
       ↓
Game Client → Allow/Block Action → Player Feedback
```

## Monitoring & Observability

### Metrics

- Authentication success/failure rates
- Permission check latency and cache hit rates
- Anti-cheat validation throughput
- Threat detection and response times
- Database connection pool utilization

### Logging

- Structured JSON logging with zerolog
- Security events with full context
- Performance metrics and profiling
- Audit trail for compliance

### Health Checks

- Database connectivity validation
- Redis connectivity and performance
- Internal service health metrics
- Dependency health aggregation

## Development

### OpenAPI Specification

The API is fully specified using OpenAPI 3.0.3:

```bash
# Validate specification
redocly lint main.yaml

# Bundle for code generation
redocly bundle main.yaml -o bundled.yaml

# Generate Go code
ogen --target . --package api --clean bundled.yaml
```

### Testing

```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Load testing
hey -n 10000 -c 100 http://localhost:8080/api/v1/security/health
```

## Deployment

### Kubernetes Manifests

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: security-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: security-service
        image: necpgame/security-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: secret
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

### Configuration

```yaml
server:
  port: 8080
  tls:
    enabled: true
    cert_file: /etc/ssl/certs/security.crt
    key_file: /etc/ssl/private/security.key

database:
  url: postgresql://user:pass@db:5432/security

redis:
  addr: redis:6379
  password: ""
  db: 0

jwt:
  secret: your-jwt-secret-here
  access_token_ttl: 15m
  refresh_token_ttl: 24h

cors:
  allowed_origins:
    - https://game.necpgame.com
    - https://admin.necpgame.com
```

## Security Considerations

### Threat Model

- **Authentication Bypass**: JWT validation and refresh token rotation
- **Authorization Bypass**: Permission validation and context checking
- **Cheating**: Multi-layered validation with ML detection
- **DDoS**: Rate limiting and request throttling
- **Data Breach**: Encryption at rest and in transit

### Compliance

- **GDPR**: User data minimization and consent management
- **SOX**: Audit trail and change tracking
- **PCI DSS**: Secure token handling and encryption
- **ISO 27001**: Information security management system

## Related Systems

- **User Service**: User profile and account management
- **Game Service**: Game state and mechanics validation
- **Admin Service**: Administrative operations and monitoring
- **Analytics Service**: Security metrics and reporting

---

## Validation Status ✅

- **OpenAPI 3.0.3**: ✅ Validated with redocly
- **Code Generation**: ✅ Compatible with ogen v1.18.0
- **Go Compilation**: ✅ Builds successfully
- **Enterprise Standards**: ✅ Performance and security optimized

## Development Readiness 🚀

- **API Contract**: ✅ Complete and validated
- **Implementation Guide**: ✅ Performance hints included
- **Security Model**: ✅ Zero-trust architecture
- **Monitoring**: ✅ Comprehensive observability
- **Documentation**: ✅ Enterprise-grade docs

**Ready for Backend Agent implementation!**
