# Game Mechanics Master Index Database Migration

## Overview

This migration creates the complete database schema for the Game Mechanics Master Index service, providing centralized management of all game mechanics in the NECPGAME ecosystem.

## Migration Details

**Version:** V001
**File:** `V001__create_game_mechanics_master_index_tables.sql`
**Issue:** #2176 - Game Mechanics Systems Master Index

## Schema Structure

### Schemas Created
- `game_mechanics` - Main schema for all mechanics-related tables

### Tables Created

#### 1. `game_mechanics.mechanics`
Core mechanics registry table with partitioning by status for optimal performance.

**Key Features:**
- Partitioned by status (active/inactive/deprecated)
- Comprehensive mechanic metadata
- Health check integration
- Tag-based organization

#### 2. `game_mechanics.dependencies`
Mechanic dependency relationships with referential integrity.

**Key Features:**
- Prevents circular dependencies
- Supports required/optional/conflict types
- Hard/soft dependency classification

#### 3. `game_mechanics.configurations`
Versioned configuration management with monthly partitioning.

**Key Features:**
- Time-based partitioning for historical tracking
- JSONB storage for flexible configurations
- Version control and rollback capabilities

#### 4. `game_mechanics.health_status`
Real-time health monitoring with daily partitioning for MMOFPS performance.

**Key Features:**
- Performance metrics collection
- Service status tracking
- Error rate monitoring
- Time-series optimization

#### 5. `game_mechanics.system_health`
System-wide health metrics and scoring.

**Key Features:**
- Aggregated health calculations
- Performance trend analysis
- Automated health scoring

## Performance Optimizations

### Partitioning Strategy
- **Mechanics:** Partitioned by status for query isolation
- **Configurations:** Monthly partitions for version history
- **Health Status:** Daily partitions for time-series queries

### Indexing Strategy
- Composite indexes for common query patterns
- Partial indexes for active mechanics (most frequent queries)
- GIN indexes for JSONB and array fields
- Time-based indexes for health monitoring

### MMOFPS-Specific Optimizations
- Memory-aligned data structures
- Optimized for <10ms P99 latency
- <50KB memory per registry entry
- Support for 100k+ concurrent connections

## Initial Data

The migration includes initial seeding with core game mechanics across all categories:

- **Combat:** Weapon systems, damage calculation, quickhacks
- **Economy:** Currency systems, trading, crafting
- **Social:** Relationships, guilds, communication
- **World:** Weather effects, fast travel, exploration
- **Progression:** Skill trees, experience gain, leveling
- **Quest:** Main story, dynamic quests, objectives
- **Multiplayer:** Matchmaking, PvP arenas, team mechanics

## Permissions

### Application User (`necpgame_app`)
- Full CRUD permissions on all tables
- Sequence usage permissions

### Read-Only User (`necpgame_readonly`)
- SELECT permissions for reporting and analytics

## Maintenance Functions

### Partition Management
- `create_monthly_config_partition()` - Automated monthly partition creation for configurations
- `create_daily_health_partition()` - Automated daily partition creation for health data

### Health Monitoring
- `calculate_system_health()` - Automated system health score calculation
- System health tracking with historical data

## Views

### `active_mechanics_health`
- Active mechanics with current health status
- Optimized for dashboard and monitoring queries

### `mechanic_dependencies_detailed`
- Dependency relationships with mechanic names
- Human-readable dependency analysis

## Migration Rollback

To rollback this migration:

```sql
-- Drop schema and all dependent objects
DROP SCHEMA IF EXISTS game_mechanics CASCADE;

-- Remove migration metadata
DELETE FROM infrastructure.liquibase_migration_metadata
WHERE version = 'V001';
```

## Testing

### Unit Tests
- Table creation verification
- Index performance validation
- Partition functionality testing
- Permission validation

### Integration Tests
- Service startup with new schema
- Basic CRUD operations
- Health monitoring functionality
- Dependency resolution testing

### Performance Tests
- Query performance under load
- Partition pruning efficiency
- Memory usage validation
- Concurrent connection handling

## Monitoring

After deployment, monitor:

1. **Table Sizes:** Growth patterns of partitioned tables
2. **Query Performance:** P99 latency for common operations
3. **Partition Health:** Automatic partition creation
4. **Health Scores:** System health trends

## Related Components

- **Service:** `game-mechanics-master-index-service-go`
- **API Spec:** `proto/openapi/game-mechanics-master-index/main.yaml`
- **Documentation:** `knowledge/mechanics/game-mechanics-systems-master-index.yaml`
- **Health Checks:** Integrated with service health monitoring

## Deployment Checklist

- [ ] Schema permissions verified
- [ ] Initial data seeded correctly
- [ ] Indexes created successfully
- [ ] Partitions functional
- [ ] Views accessible
- [ ] Functions working
- [ ] Migration metadata recorded
- [ ] Service can connect and operate
- [ ] Health monitoring operational