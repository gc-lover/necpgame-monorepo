# Social NPC Hiring World Impact Service

**Issue:** [#140894831](https://github.com/gc-lover/necpgame-monorepo/issues/140894831)

## Overview

This service analyzes and tracks the comprehensive effects of NPC hiring on the game world in MMOFPS RPG. It calculates economic impacts, social changes, political consequences, and regional development caused by NPC activities.

## Features

### 🎯 Core Functionality

- **Hire Impact Analysis**: Detailed analysis of individual NPC hire effects on the world
- **World Aggregation**: Aggregate impact analysis across all active NPC hires
- **Impact Prediction**: Predictive modeling of hiring consequences before they occur
- **Loyalty Effects**: Analysis of how NPC loyalty levels affect world dynamics
- **Economic Impact Tracking**: Comprehensive economic analysis of NPC activities
- **Social Changes Monitoring**: Tracking social effects and community responses
- **Political Consequences**: Calculation of political ramifications from NPC operations

### 🔧 Technical Features

- **REST API**: Full REST API with comprehensive impact analysis
- **Enterprise-grade**: Production-ready with proper logging and monitoring
- **Docker Support**: Containerized deployment
- **Performance Optimized**: Efficient impact calculation algorithms

## API Endpoints

### Get NPC Hire World Impact
```
GET /npc-hiring-impact/{hire_id}/world-impact
```
Returns detailed world impact analysis for a specific NPC hire.

### Get World Impacts from NPC Hiring
```
GET /npc-hiring-impact/world-impacts?region_id={uuid}&time_period=day|hour|week|month
```
Returns aggregated world impacts from all NPC hiring activities.

### Predict NPC Hiring Impact
```
POST /npc-hiring-impact/impact-prediction
```
Predicts world impacts before hiring an NPC.

### Get NPC Loyalty Effects
```
GET /npc-hiring-impact/loyalty-effects?npc_id={uuid}&loyalty_level={1-10}&region_id={uuid}
```
Analyzes effects of NPC loyalty levels on world dynamics.

### Get Economic Impact
```
GET /npc-hiring-impact/economic-impact?region_id={uuid}&npc_type={type}&time_range={range}
```
Returns detailed economic impact analysis.

### Get Social Changes
```
GET /npc-hiring-impact/social-changes?region_id={uuid}&change_type={type}
```
Analyzes social changes caused by NPC activities.

### Calculate Political Consequences
```
POST /npc-hiring-impact/political-consequences
```
Calculates political consequences of NPC hiring decisions.

## Architecture

### Service Structure
```
services/social-npc-hiring-world-impact-service-go/
├── main.go                 # Entry point
├── server/
│   └── server.go          # HTTP server and routes
├── pkg/npc-hiring-impact/
│   └── service.go         # Core business logic
├── Dockerfile             # Container definition
├── Makefile              # Build automation
└── README.md             # This file
```

### Dependencies
- **go-chi/chi**: HTTP router
- **google/uuid**: UUID handling
- **uber-go/zap**: Structured logging
- **lib/pq**: PostgreSQL driver (future use)

## Development

### Prerequisites
- Go 1.21+
- Docker (optional)

### Quick Start

1. **Clone and setup:**
   ```bash
   cd services/social-npc-hiring-world-impact-service-go
   go mod tidy
   ```

2. **Run locally:**
   ```bash
   make run
   # or
   go run main.go
   ```

3. **Build:**
   ```bash
   make build
   ```

4. **Run tests:**
   ```bash
   make test
   ```

5. **Docker:**
   ```bash
   make docker-build
   make docker-run
   ```

### Configuration

The service uses environment variables for configuration:

```bash
# Database (future use)
DATABASE_URL=postgres://user:pass@localhost/db

# External services (future integration)
NPC_SERVICE_URL=http://npc-service:8080
REPUTATION_SERVICE_URL=http://reputation-service:8080

# Logging
LOG_LEVEL=info
```

## Business Logic

### Impact Analysis Categories

#### Economic Impact
- **Wage Distribution**: How NPC payments affect local economy
- **Market Effects**: Changes in service prices and competition
- **Tax Revenue**: Government income from NPC activities

#### Social Impact
- **Community Trust**: How NPC presence affects public opinion
- **Security Perception**: Changes in perceived safety levels
- **NPC Integration**: How well NPCs are accepted by society
- **Reputation Effects**: Faction reputation changes from NPC actions

#### Political Impact
- **Faction Relations**: Changes in relationships between factions
- **Power Balance**: Shifts in regional power dynamics
- **Political Events**: Potential political events triggered by NPC activities

#### Regional Development
- **Infrastructure**: Improvements to city infrastructure
- **Economic Growth**: Overall economic development
- **Population Changes**: Immigration/emigration patterns
- **Quality of Life**: Improvements to living standards

### Loyalty Effects

NPC loyalty levels significantly affect world impact:

- **High Loyalty (8-10)**: Positive world effects, increased stability, better economic growth
- **Medium Loyalty (4-7)**: Neutral effects, standard impact levels
- **Low Loyalty (1-3)**: Negative effects, increased instability, economic downturns

### Prediction Engine

The service uses predictive modeling to forecast impacts:

- **Activity Multipliers**: Different impact levels based on NPC activity intensity
- **Duration Effects**: Long-term vs short-term impact calculations
- **Risk Assessment**: Political, economic, and social risk evaluation
- **Recommendations**: Actionable suggestions to mitigate negative impacts

### Political Consequences

Advanced political analysis includes:

- **Immediate Consequences**: Direct effects of hiring decisions
- **Long-term Implications**: Strategic changes over time
- **Risk Mitigation**: Strategies to reduce negative political outcomes
- **Faction Dynamics**: Complex interactions between game factions

## Integration Points

### External Services
- **NPC Service**: NPC data and hiring information
- **Reputation Service**: Reputation data and changes
- **Orders Service**: Order completion data
- **Economy Service**: Economic data and calculations
- **World Service**: Regional and city information
- **Faction Service**: Faction relationship data

### Database Schema (Future)
```sql
-- NPC hire impact tracking
CREATE TABLE npc_hire_impacts (
    hire_id UUID PRIMARY KEY,
    npc_id UUID NOT NULL,
    region_id UUID,
    economic_impact JSONB,
    social_impact JSONB,
    political_impact JSONB,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- World impact aggregations
CREATE TABLE world_impact_aggregations (
    region_id UUID,
    time_period VARCHAR(20),
    total_hired_npcs INTEGER,
    aggregated_impacts JSONB,
    calculated_at TIMESTAMP DEFAULT NOW()
);

-- Loyalty effects tracking
CREATE TABLE npc_loyalty_effects (
    npc_id UUID,
    loyalty_level INTEGER,
    world_effects JSONB,
    recorded_at TIMESTAMP DEFAULT NOW()
);
```

## Monitoring & Observability

### Health Check
```
GET /health
```

### Metrics (Future)
- Impact calculation latency
- Prediction accuracy rates
- World state query performance
- Political consequence analysis throughput

### Logging
Structured JSON logging with:
- Request IDs for tracing
- Impact calculation details
- Prediction confidence levels
- Error analysis and recovery

## Testing

### Unit Tests
```bash
go test ./pkg/npc-hiring-impact/
```

### Integration Tests
```bash
make integration-test
```

### API Testing Example
```bash
# Get world impact for a specific hire
curl -X GET "http://localhost:8080/npc-hiring-impact/123e4567-e89b-12d3-a456-426614174000/world-impact"

# Predict hiring impact
curl -X POST http://localhost:8080/npc-hiring-impact/impact-prediction \
  -H "Content-Type: application/json" \
  -d '{
    "npc_id": "123e4567-e89b-12d3-a456-426614174000",
    "hire_duration_hours": 48,
    "activity_level": "high",
    "region_id": "456e7890-e89b-12d3-a456-426614174001"
  }'
```

## Contributing

1. Follow Go best practices and project conventions
2. Add comprehensive tests for new impact calculations
3. Update API documentation for endpoint changes
4. Include impact prediction validation

## Related Issues

- [#140894831](https://github.com/gc-lover/necpgame-monorepo/issues/140894831) - Main implementation
- NPC hiring system integration
- World simulation and impact modeling
- Reputation system integration
- Political system integration
