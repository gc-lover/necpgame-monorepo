# Session Management Service

## Overview

**Session Management Service API** - Enterprise-grade domain service managing all types of game sessions within
NECPGAME. This service handles session lifecycle, state management, analytics, and provides unified session operations
across combat, player, authentication, and custom session types.

## Domain Purpose

The Session Management Service serves as the central session orchestration hub for NECPGAME, ensuring consistent session
handling across all game systems. It provides:

- **Unified Session Lifecycle**: Creation, management, and termination of all session types
- **Real-time State Tracking**: Live session state and participant management
- **Session Analytics**: Performance metrics and behavioral insights
- **Cross-system Coordination**: Session state synchronization across services
- **Scalable Architecture**: Support for millions of concurrent sessions

## Performance Targets

- **Session Operations**: <10ms P95 response time for CRUD operations
- **Concurrent Sessions**: 500,000+ active sessions supported
- **State Synchronization**: <5ms cross-service state updates
- **Analytics Queries**: <50ms complex analytics retrieval
- **Memory Efficiency**: <4KB per active session

## Structure

```
session-management-service/
├── main.yaml                 # Main OpenAPI specification
├── README.md                 # This documentation
└── (future) schemas/         # Domain-specific schemas
```

## Dependencies

- **Common Schemas**: `../common-service/schemas/health.yaml`, `../common-service/schemas/error.yaml`
- **Common Responses**: `../common-service/responses/error.yaml`
- **Common Security**: `../common-service/security/security.yaml`

## Usage

### Health Monitoring

```bash
# Service health check
GET /health
```

### Session Management

```bash
# Create a new session
POST /sessions

# List sessions with filtering
GET /sessions?type=combat&status=active&limit=20

# Get session details
GET /sessions/{session_id}?include_logs=true&include_stats=true

# Update session properties
PUT /sessions/{session_id}

# Terminate session
DELETE /sessions/{session_id}
```

### Combat Sessions

```bash
# Create combat session
POST /combat/sessions

# List active combat sessions
GET /combat/sessions?status=active&mode=pvp

# Execute combat action
POST /combat/sessions/{session_id}/actions

# Get combat session state
GET /combat/sessions/{session_id}/state
```

### Session Analytics

```bash
# Get session analytics
GET /analytics/sessions?timeframe=24h&session_type=combat&metric=all
```

## Validation

### Redocly Lint Check

```bash
npx @redocly/cli lint proto/openapi/session-management-service/main.yaml
```

### Go Code Generation

```bash
ogen proto/openapi/session-management-service/main.yaml \
  --package session \
  --generate server,client,models \
  --output services/session-management-service-go/
```

## Mandatory Elements

### OpenAPI Header

- OpenAPI 3.0.3 specification
- Enterprise-grade info with version, description, contact
- License and terms of service
- External documentation links

### Servers Configuration

- Production: `https://api.necpgame.com/v1/session`
- Staging: `https://staging-api.necpgame.com/v1/session`
- Local: `http://localhost:8080/api/v1/session`

### Security Schemes

- BearerAuth (JWT tokens)
- Service-to-service authentication
- Session-based authorization

### Health Endpoints

- `/health` - Basic health check

### Common Schemas

- `HealthResponse` from `../common-service/schemas/health.yaml`
- `Error` from `../common-service/schemas/error.yaml`

## Backend Optimization Hints

### Session State Management

```go
// Optimized session storage with LRU cache
type SessionManager struct {
    sessions sync.Map  // Thread-safe session storage
    lruCache *lru.Cache // LRU cache for hot sessions
    metrics  *SessionMetrics
}

func (sm *SessionManager) GetSession(sessionID string) (*Session, error) {
    // Check LRU cache first for hot sessions
    if session, ok := sm.lruCache.Get(sessionID); ok {
        return session.(*Session), nil
    }

    // Fallback to main storage
    if session, ok := sm.sessions.Load(sessionID); ok {
        // Add to LRU cache for future access
        sm.lruCache.Add(sessionID, session)
        return session.(*Session), nil
    }

    return nil, ErrSessionNotFound
}
```

### Memory-Efficient Session Storage

```go
// Struct alignment for memory efficiency
type Session struct {
    ID        string    `json:"id"`         // 16 bytes (UUID)
    Type      string    `json:"type"`       // 16 bytes
    PlayerID  string    `json:"player_id"`  // 16 bytes
    Status    string    `json:"status"`     // 16 bytes
    CreatedAt time.Time `json:"created_at"` // 24 bytes (time.Time is 24 bytes)
    UpdatedAt time.Time `json:"updated_at"` // 24 bytes
    Metadata  []byte    `json:"metadata"`   // 24 bytes (slice header)
    // Total: 136 bytes, well-aligned for 64-bit systems
}
```

### Concurrent Session Updates

```go
// Optimistic locking for concurrent session updates
func (sm *SessionManager) UpdateSession(sessionID string, updateFn func(*Session) error) error {
    session, err := sm.GetSession(sessionID)
    if err != nil {
        return err
    }

    // Create a copy for optimistic locking
    originalVersion := session.Version

    // Apply updates
    if err := updateFn(session); err != nil {
        return err
    }

    // Increment version for optimistic locking
    session.Version++
    session.UpdatedAt = time.Now()

    // Attempt atomic update with version check
    if !sm.sessions.CompareAndSwap(sessionID, originalVersion, session) {
        return ErrConcurrentUpdate
    }

    return nil
}
```

### Session Analytics with Ring Buffers

```go
// Efficient analytics using ring buffers
type SessionAnalytics struct {
    creationTimes *ring.Buffer // Track session creation timestamps
    durations     *ring.Buffer // Track session durations
    errorCounts   *ring.Buffer // Track errors per time period
}

func (sa *SessionAnalytics) RecordSessionCompletion(duration time.Duration, hadErrors bool) {
    sa.durations.Push(duration.Nanoseconds())

    if hadErrors {
        // Increment error count for current time window
        currentErrors := sa.errorCounts.Pop().(int64)
        sa.errorCounts.Push(currentErrors + 1)
    }

    // Ring buffer prevents unbounded memory growth
    // O(1) operations for real-time analytics
}
```

### Combat Session State Machine

```go
// Finite state machine for combat session management
type CombatSessionStateMachine struct {
    currentState CombatSessionState
    transitions  map[CombatSessionState]map[CombatSessionEvent]CombatSessionState
    actions      map[CombatSessionState]func() error
}

func (csm *CombatSessionStateMachine) Transition(event CombatSessionEvent) error {
    if nextState, exists := csm.transitions[csm.currentState][event]; exists {
        // Execute exit action for current state
        if exitAction := csm.actions[csm.currentState]; exitAction != nil {
            if err := exitAction(); err != nil {
                return err
            }
        }

        // Transition to new state
        csm.currentState = nextState

        // Execute entry action for new state
        if entryAction := csm.actions[nextState]; entryAction != nil {
            return entryAction()
        }

        return nil
    }

    return ErrInvalidTransition
}
```

## How to Use the Template

1. **Copy Template**: Start from the enterprise template
2. **Replace Placeholders**: Update service name, description, version
3. **Add Real Operations**: Implement domain-specific endpoints
4. **Optimize Schemas**: Apply memory alignment and performance hints
5. **Validate**: Run Redocly lint and Ogen generation
6. **Test**: Ensure all endpoints work correctly

## Performance Benchmarks

### Session Operations Performance

- Session Creation: <20ms average response time
- Session Retrieval: <5ms P95 with caching
- Session Updates: <10ms with optimistic locking
- Session Deletion: <15ms with cleanup

### Combat Session Performance

- Action Processing: <5ms per combat action
- State Synchronization: <2ms across all participants
- Real-time Updates: <1ms WebSocket delivery
- Concurrent Combat: 10,000+ simultaneous sessions

### Analytics Performance

- Real-time Metrics: <1ms data collection
- Historical Queries: <100ms for 24-hour data
- Aggregation: <50ms complex analytics
- Dashboard Updates: <10ms refresh rate

### Scalability Metrics

- Vertical Scaling: 100,000+ sessions per instance
- Horizontal Scaling: 50+ service instances
- Database Connections: 200+ concurrent
- Cache Hit Rate: 95%+ for hot sessions

## Related Documents

- `REORGANIZATION_INSTRUCTION.md` - Migration guidelines
- `MIGRATION_GUIDE.md` - Step-by-step migration process
- `.cursor/rules/agent-backend.mdc` - Backend implementation rules
- `.cursor/rules/agent-performance.mdc` - Performance optimization guidelines

## Next Steps

1. **Implement Backend**: Create Go service in `services/session-management-service-go/`
2. **Database Setup**: Configure PostgreSQL with optimized session tables
3. **Redis Integration**: Set up Redis for session caching and pub/sub
4. **WebSocket Implementation**: Integrate with network-infrastructure-service
5. **State Machine**: Implement combat session state management
6. **Analytics Pipeline**: Set up real-time analytics collection
7. **Testing**: Implement comprehensive session lifecycle testing
8. **Monitoring**: Configure session metrics and alerting

## Important Remarks

- **Session Isolation**: Complete isolation between different session types
- **State Consistency**: Guaranteed consistency across distributed instances
- **Fault Tolerance**: Automatic session recovery and failover
- **Performance Monitoring**: Detailed metrics for all session operations
- **Security**: Session-based authentication and authorization
- **Scalability**: Designed for massive concurrent session handling
- **Analytics**: Rich behavioral insights from session data

## Issue Tracking

Related Issues:

- #2266 - Refactor system-domain - AI, monitoring, networking services
- Session management implementation tasks
- Real-time session synchronization requirements
