# Security Service

Enterprise-grade authentication, authorization, and security service for NECPGAME.

## Overview

The Security Service provides comprehensive security infrastructure including:

- **JWT-based Authentication** with refresh tokens
- **OAuth 2.0 Integration** (Google, Discord, GitHub, Steam)
- **Role-Based Access Control (RBAC)** with permissions
- **Multi-Factor Authentication (MFA)** support
- **Security Threat Detection** and monitoring
- **Anti-Cheat Validation** for game actions
- **Session Management** with Redis caching

## Features

### Authentication & Authorization
- User registration and login
- JWT token generation and validation
- Refresh token rotation
- Password hashing with bcrypt
- OAuth social login providers
- MFA with TOTP/SMS
- Session management and blacklisting

### Security Monitoring
- Threat detection and logging
- Security event auditing
- Anti-cheat validation for game actions
- Suspicious activity monitoring
- Real-time security alerts

### Enterprise Features
- **Performance**: P99 <25ms for auth, <50ms for authorization
- **Scalability**: 100,000+ concurrent security operations
- **Security**: Multi-layer authentication and validation
- **Monitoring**: Comprehensive logging and metrics
- **Compliance**: Audit trails and security standards

## API Endpoints

### Authentication
```
POST /auth/login          - User login
POST /auth/register       - User registration
POST /auth/refresh        - Token refresh
POST /auth/logout         - User logout
POST /auth/forgot-password - Password reset
```

### Authorization
```
GET  /auth/permissions    - Get user permissions
POST /auth/permissions    - Check specific permission
GET  /auth/roles         - Get user roles
POST /auth/roles         - Assign user role
```

### OAuth Integration
```
GET  /auth/oauth/{provider}     - OAuth login initiation
GET  /auth/oauth/{provider}/callback - OAuth callback handling
POST /auth/oauth/link           - Link OAuth account
```

### Security Monitoring
```
GET  /security/threats    - Get security threats
POST /security/threats    - Create threat alert
POST /security/anticheat  - Validate game action
GET  /security/events     - Get security events
```

### User Management (Admin)
```
GET    /admin/users       - List users
GET    /admin/users/{id}  - Get user details
PUT    /admin/users/{id}  - Update user
DELETE /admin/users/{id}  - Delete user
POST   /admin/users/{id}/lock - Lock user account
```

## Configuration

### Environment Variables

```bash
# Server Configuration
SERVER_PORT=8080
SERVER_BASE_URL=http://localhost:8080
SERVER_TLS_ENABLED=false

# Database
DATABASE_URL=postgres://user:password@localhost:5432/security_db?sslmode=disable

# Redis
REDIS_ADDR=localhost:6379
REDIS_PASSWORD=
REDIS_DB=0

# JWT
JWT_SECRET=your-super-secret-jwt-key-change-in-production
JWT_ACCESS_TOKEN_TTL=15m
JWT_REFRESH_TOKEN_TTL=168h
JWT_ISSUER=security-service

# OAuth Providers
OAUTH_GOOGLE_CLIENT_ID=your-google-client-id
OAUTH_GOOGLE_CLIENT_SECRET=your-google-client-secret
OAUTH_DISCORD_CLIENT_ID=your-discord-client-id
OAUTH_DISCORD_CLIENT_SECRET=your-discord-client-secret

# Security
METRICS_USERNAME=metrics
METRICS_PASSWORD=secure-metrics-password
PROFILING_ENABLED=true
PROFILING_PORT=6555

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080

# Logging
LOG_LEVEL=info
ENVIRONMENT=development
```

## Database Schema

```sql
-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    phone VARCHAR(20),
    phone_verified BOOLEAN DEFAULT FALSE,
    password_hash VARCHAR(255) NOT NULL,
    roles TEXT[] DEFAULT '{}',
    permissions TEXT[] DEFAULT '{}',
    last_login TIMESTAMP,
    login_count INTEGER DEFAULT 0,
    account_status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- User sessions
CREATE TABLE user_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- OAuth accounts
CREATE TABLE oauth_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    provider_data JSONB,
    linked_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id, provider)
);

-- Security roles
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    permissions TEXT[] DEFAULT '{}',
    is_system_role BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- User roles junction
CREATE TABLE user_roles (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID REFERENCES roles(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT NOW(),
    assigned_by UUID REFERENCES users(id),
    PRIMARY KEY (user_id, role_id)
);

-- Security threats
CREATE TABLE security_threats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    description TEXT,
    user_id UUID REFERENCES users(id),
    ip_address INET,
    user_agent TEXT,
    location VARCHAR(100),
    confidence_score DECIMAL(3,2),
    detected_at TIMESTAMP DEFAULT NOW(),
    resolved_at TIMESTAMP,
    actions_taken TEXT[] DEFAULT '{}'
);

-- Security events audit log
CREATE TABLE security_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(50) NOT NULL,
    user_id UUID REFERENCES users(id),
    ip_address INET,
    user_agent TEXT,
    event_data JSONB,
    severity VARCHAR(20) DEFAULT 'info',
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_token ON user_sessions(session_token);
CREATE INDEX idx_security_threats_user_id ON security_threats(user_id);
CREATE INDEX idx_security_threats_status ON security_threats(status);
CREATE INDEX idx_security_events_user_id ON security_events(user_id);
CREATE INDEX idx_security_events_created_at ON security_events(created_at);
```

## Development

### Prerequisites

- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Docker (optional)

### Setup

```bash
# Clone repository
git clone <repository-url>
cd security-service-go

# Install dependencies
make mod-tidy

# Set up database
createdb security_db
psql security_db < schema.sql

# Set up environment
cp .env.example .env
# Edit .env with your configuration

# Run tests
make test

# Build service
make build

# Run service
make run
```

### Testing

```bash
# Unit tests
make test-unit

# Integration tests
make test-integration

# Security scanning
make security-scan

# Benchmarking
make bench
```

### Docker Development

```bash
# Build image
make docker-build

# Run with docker-compose
make docker-compose-up

# View logs
make logs

# Health check
make health-check
```

## API Usage Examples

### User Registration

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

### User Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "SecurePass123!"
  }'
```

Response:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 900,
  "user": {
    "id": "uuid",
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["user"],
    "permissions": ["read:profile"]
  }
}
```

### Permission Check

```bash
curl -X POST http://localhost:8080/auth/permissions \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "permission": "admin:users"
  }'
```

Response:
```json
{
  "permission": "admin:users",
  "granted": false,
  "reason": "Permission denied for user"
}
```

### Anti-Cheat Validation

```bash
curl -X POST http://localhost:8080/security/anticheat \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "action_type": "combat",
    "parameters": {
      "accuracy": 0.98,
      "reaction_time": 50
    }
  }'
```

## Security Features

### Authentication Security
- **Password Hashing**: bcrypt with configurable cost
- **JWT Security**: HMAC-SHA256 with secure secrets
- **Token Rotation**: Automatic refresh token rotation
- **Session Management**: Secure session handling with Redis

### Authorization Security
- **RBAC Implementation**: Role-based access control
- **Permission Granularity**: Fine-grained permission system
- **Context-Aware Checks**: Permission validation with context

### Threat Detection
- **Behavioral Analysis**: User behavior pattern monitoring
- **Anomaly Detection**: Statistical analysis for suspicious activity
- **Rate Limiting**: Configurable rate limits per endpoint
- **IP Blacklisting**: Automatic IP blocking for threats

### Anti-Cheat Integration
- **Game Action Validation**: Real-time validation of player actions
- **Pattern Recognition**: Machine learning-based cheat detection
- **Evidence Collection**: Detailed logging for investigation
- **Automated Response**: Configurable actions for violations

## Monitoring & Observability

### Metrics
- Authentication success/failure rates
- Authorization check latency
- Session creation/destruction rates
- Threat detection statistics
- Anti-cheat validation performance

### Logging
- Structured JSON logging with correlation IDs
- Security event auditing
- Performance monitoring
- Error tracking with stack traces

### Health Checks
- Database connectivity
- Redis availability
- Service responsiveness
- Security subsystem status

## Deployment

### Production Checklist

- [ ] Configure strong JWT secrets
- [ ] Set up TLS certificates
- [ ] Configure OAuth provider credentials
- [ ] Set up database backups
- [ ] Configure monitoring and alerting
- [ ] Set up log aggregation
- [ ] Configure rate limiting
- [ ] Test disaster recovery procedures

### Scaling Considerations

- **Horizontal Scaling**: Stateless design supports multiple instances
- **Database Sharding**: User-based sharding for large deployments
- **Redis Clustering**: Distributed Redis for session management
- **Load Balancing**: HTTP load balancers with sticky sessions
- **CDN Integration**: For static assets and OAuth redirects

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

### Code Standards

- Follow Go best practices
- Write comprehensive tests
- Include security considerations
- Document API changes
- Update README for new features

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the troubleshooting guide
