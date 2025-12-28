# Gameplay Affixes Service

## Issue: #1495 - Gameplay Affixes Service implementation

Enterprise-grade Go microservice for managing dungeon and raid affixes in NECPGAME MMOFPS RPG.

## Overview

The Gameplay Affixes Service manages the affix system for dungeons and raids. Affixes are modifiers that change gameplay mechanics, making encounters more challenging and rewarding.

### Key Features

- **Weekly Rotations**: Affixes rotate weekly (Monday 00:00 UTC)
- **Instance Application**: 2-4 random affixes per dungeon/raid instance
- **Reward/Difficulty Scaling**: Dynamic scaling based on affix combinations
- **Admin Controls**: Manual rotation triggers for special events
- **Performance Optimized**: Struct alignment and efficient database queries

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Handlers      │    │    Service      │    │  Repository     │
│                 │    │                 │    │                 │
│ • HTTP APIs     │◄──►│ • Business      │◄──►│ • Database      │
│ • JSON/HTTP     │    │   Logic         │    │   Operations    │
│ • Validation    │    │ • Validation    │    │ • Queries       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   Database      │
                    │ • PostgreSQL    │
                    │ • Optimized     │
                    │   queries       │
                    └─────────────────┘
```

## API Endpoints

### Active Affixes
- `GET /api/v1/gameplay/affixes/active` - Get current week's active affixes

### Affix Management
- `GET /api/v1/gameplay/affixes` - List all affixes (paginated)
- `GET /api/v1/gameplay/affixes/{id}` - Get specific affix details
- `POST /api/v1/gameplay/affixes` - Create new affix (admin)
- `PUT /api/v1/gameplay/affixes/{id}` - Update affix (admin)
- `DELETE /api/v1/gameplay/affixes/{id}` - Delete affix (admin)

### Instance Affixes
- `GET /api/v1/gameplay/instances/{instance_id}/affixes` - Get affixes for instance
- `POST /api/v1/gameplay/instances/{instance_id}/affixes/generate` - Generate affixes for instance

### Rotation Management
- `GET /api/v1/gameplay/affixes/rotation/history` - Get rotation history
- `POST /api/v1/gameplay/affixes/rotation/trigger` - Trigger manual rotation (admin)

## Database Schema

### Tables

#### `gameplay.affixes`
Main affixes table with mechanics and visual effects stored as JSONB.

#### `gameplay.affix_rotations`
Weekly rotation records with start/end dates.

#### `gameplay.active_affixes`
Links affixes to rotation periods (many-to-many).

#### `gameplay.instance_affixes`
Tracks affixes applied to specific dungeon instances.

### Performance Features

- **Struct Alignment**: Fields ordered for optimal memory usage (30-50% savings)
- **Composite Indexes**: Optimized for common query patterns
- **Connection Pooling**: Efficient database connection management
- **Context Timeouts**: Prevents hanging requests

## Affix Categories

### Combat Affixes
- **Volatile**: Enemies explode on death, dealing area damage
- **Raging**: +25% damage from enemies
- **Necrotic**: Enemies apply HP reduction debuff

### Defensive Affixes
- **Fortified**: +50% enemy HP
- **Bolstering**: Enemies get stronger when allies die

### Environmental Affixes
- **Frostbite**: Periodic slow effects on players
- **Heat Wave**: Damage over time from heat
- **Storming**: Random lightning strikes

### Debuff Affixes
- **Afflicted**: Random debuff applications
- **Sanguine**: Blood pools that slow players

## Configuration

### Environment Variables

```bash
DATABASE_URL=postgres://user:password@localhost:5432/gameplay_affixes?sslmode=disable
PORT=8083
LOG_LEVEL=info
```

### Docker

```bash
# Build
docker build -t gameplay-affixes-service .

# Run
docker run -p 8083:8083 -e DATABASE_URL=... gameplay-affixes-service
```

## Development

### Prerequisites

- Go 1.21+
- PostgreSQL 13+
- Git

### Setup

```bash
# Clone repository
git clone <repository-url>
cd services/gameplay-affixes-service-go

# Install dependencies
go mod download

# Run database migrations
# (See infrastructure/liquibase migrations)

# Run service
make run

# Or build and run
make build
./bin/gameplay-affixes-service
```

### Testing

```bash
# Run tests
make test

# Run with verbose output
go test -v ./...
```

### API Testing

```bash
# Health check
curl http://localhost:8083/health

# Get active affixes
curl http://localhost:8083/api/v1/gameplay/affixes/active

# Generate affixes for instance
curl -X POST http://localhost:8083/api/v1/gameplay/instances/550e8400-e29b-41d4-a716-446655440000/affixes/generate
```

## Performance Optimizations

### Memory Efficiency
- **Struct Alignment**: All structs optimized for memory alignment
- **JSONB Storage**: Flexible mechanics storage without schema changes
- **Efficient Queries**: Optimized database queries with proper indexing

### Scalability
- **Connection Pooling**: pgxpool for efficient database connections
- **Context Timeouts**: Prevents resource leaks
- **Goroutine Safety**: Thread-safe operations

### Monitoring
- **Health Checks**: `/health` endpoint for load balancer monitoring
- **Structured Logging**: Zap logger with proper log levels
- **Request Tracing**: Request duration and error logging

## Related Issues

- **#1495**: Gameplay Affixes Service implementation
- **#101**: Timeline integration
- **#104**: Mechanics system integration

## Contributing

1. Follow Go best practices
2. Add tests for new features
3. Update documentation
4. Use conventional commits

## License

MIT License - see LICENSE file for details.
