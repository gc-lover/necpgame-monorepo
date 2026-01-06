# Battle Pass Service

## Overview

Enterprise-grade Battle Pass system providing seasonal progression mechanics for MMOFPS RPG gameplay. Delivers low-latency reward distribution with comprehensive player progression tracking.

## Purpose

The Battle Pass Service manages seasonal content progression, where players earn experience points through gameplay activities to unlock tiered rewards. The system supports both free and premium progression paths, enabling monetization while maintaining accessibility.

## Functionality

### Core Features
- **Seasonal Management**: Create and manage Battle Pass seasons with configurable durations and reward structures
- **Player Progression**: Track XP earned through gameplay, level advancement, and reward unlocking
- **Dual-Tier Rewards**: Free tier rewards for all players, premium tier rewards for pass holders
- **Reward Claiming**: Secure, atomic reward distribution with inventory integration
- **Real-time Updates**: Live progress synchronization across game sessions
- **Analytics**: Comprehensive player statistics and engagement metrics

### Key Operations
- Season lifecycle management (upcoming → active → ended)
- XP granting from gameplay events (missions, matches, achievements)
- Reward availability checking and claiming
- Premium pass status management
- Cross-season statistics aggregation

## Structure

### API Architecture
```
battle-pass-service/
├── main.yaml          # OpenAPI 3.0 specification
├── README.md          # This documentation
└── examples/          # Request/response examples
```

### Service Components
- **Season Manager**: Handles season configuration and lifecycle
- **Progress Tracker**: Manages player XP and level progression
- **Reward Engine**: Processes reward claiming and distribution
- **Analytics Service**: Aggregates player statistics and insights
- **Premium Validator**: Manages premium pass entitlements

## Dependencies

### External Services
- **Player Service**: Player identity and authentication
- **Inventory Service**: Reward item storage and management
- **Gameplay Service**: XP granting event sources
- **Economy Service**: Premium pass transactions

### Data Storage
- **PostgreSQL**: Primary data store with optimized schemas
- **Redis**: Session caching and real-time progress updates
- **CDN**: Static reward asset delivery

## Performance Targets

### Latency Requirements
- **Health Check**: <100ms P95
- **Progress Query**: <50ms P95
- **XP Grant**: <200ms P95
- **Reward Claim**: <300ms P95
- **Season Query**: <150ms P95

### Throughput Requirements
- **Concurrent Players**: 10,000+ simultaneous progress updates
- **XP Grants/Second**: 1,000+ events processed
- **Reward Claims/Minute**: 500+ claims processed
- **Season Transitions**: Zero-downtime handling

### Resource Utilization
- **Memory**: <2GB per instance baseline, <4GB under load
- **CPU**: <20% average utilization, <60% peak
- **Storage**: <100MB per season metadata
- **Network**: <10Mbps per instance baseline

## Usage

### Authentication
All API endpoints require JWT Bearer token authentication with player context.

### XP Granting
```javascript
// Grant XP for mission completion
const response = await fetch('/progress/player123/xp', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <jwt_token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    amount: 250,
    reason: 'mission_complete',
    metadata: { missionId: 'tutorial_01' }
  })
});
```

### Reward Claiming
```javascript
// Claim free tier reward at level 5
const response = await fetch('/rewards/player123/claim', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <jwt_token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    level: 5,
    tier: 'free'
  })
});
```

### Progress Monitoring
```javascript
// Get current player progress
const progress = await fetch('/progress/player123', {
  headers: {
    'Authorization': 'Bearer <jwt_token>'
  }
});

console.log(`Level ${progress.currentLevel}, ${progress.currentXp}/${progress.xpToNextLevel} XP`);
```

## Validation

### OpenAPI Compliance
- Valid OpenAPI 3.0 specification
- All endpoints documented with examples
- Schema validation for requests/responses

### Code Generation
- Go client/server code generation tested
- TypeScript client generation verified
- Python client generation validated

### Documentation Generation
- HTML documentation renders correctly
- Interactive API explorer functional
- Schema examples validated

## Development

### Local Setup
```bash
# Install dependencies
go mod download

# Run tests
go test ./...

# Start service
go run cmd/server/main.go
```

### Testing
```bash
# Unit tests
go test ./internal/...

# Integration tests
go test ./tests/...

# Load testing
hey -n 1000 -c 10 http://localhost:8080/health
```

### Deployment
```bash
# Build container
docker build -t battle-pass-service .

# Deploy to Kubernetes
kubectl apply -f k8s/battle-pass-deployment.yaml
```

## Monitoring

### Key Metrics
- **Player Engagement**: Daily/weekly active players with Battle Pass
- **XP Velocity**: Average XP earned per player per session
- **Claim Rate**: Percentage of available rewards claimed
- **Premium Conversion**: Free to premium pass upgrade rate
- **Season Completion**: Players reaching max level percentage

### Alerts
- XP grant failures (>1% error rate)
- Reward claim timeouts (>5% slow requests)
- Season transition delays (>30 seconds)
- Premium validation failures (>0.1% false positives)

## Security

### Authentication
- JWT token validation on all endpoints
- Player identity verification
- Session management with Redis

### Authorization
- Player-scoped data access only
- Premium reward entitlement checks
- Rate limiting on XP grants (100/minute per player)

### Data Protection
- Encrypted player progress data at rest
- Secure reward distribution (no double-claims)
- Audit logging for all reward transactions

## Future Enhancements

### Phase 2 Features
- **Guild Battle Passes**: Team-based seasonal progression
- **Dynamic Rewards**: AI-generated personalized rewards
- **Cross-Season Bonuses**: Multi-season achievement rewards
- **Live Events**: In-season special challenges and rewards

### Performance Optimizations
- **Edge Caching**: Global CDN for static season data
- **Batch Operations**: Bulk XP grants for team activities
- **Predictive Loading**: Pre-load upcoming season data
- **Compression**: Efficient reward metadata transmission

