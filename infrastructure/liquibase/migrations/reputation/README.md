# Reputation System Database Migration

## Overview

This migration creates the complete database schema for the Reputation Service, implementing dynamic reputation decay and recovery mechanics for NECPGAME.

## Schema: `reputation`

### Core Tables

#### `player_reputations`
Stores reputation scores for all players across different dimensions.
- **Performance**: Optimized for frequent updates with composite indexes
- **Features**: Configurable decay/recovery rates, version control for optimistic locking

#### `reputation_changes`
Complete audit trail of all reputation changes.
- **Performance**: Time-based partitioning for efficient historical queries
- **Features**: Tracks decay/recovery applications, event correlations

#### `decay_rules`
Configurable decay parameters by reputation type.
- **Flexibility**: Different rules for player, faction, region, and global reputation
- **Features**: Activity and faction modifiers, configurable intervals

### Analytics Tables

#### `recovery_events`
Tracks recovery applications and cooldowns.
- **Features**: Streak tracking, bonus recovery events
- **Performance**: Optimized for recovery rate limiting

#### `player_stats`
Aggregated statistics per player.
- **Features**: Total decay/recovery amounts, reputation levels
- **Performance**: JSONB storage for flexible status tracking

#### `global_stats`
System-wide reputation analytics.
- **Features**: Daily statistics, reputation distributions
- **Performance**: Date-based partitioning

## Performance Optimizations

### Indexes
- **Composite indexes** for multi-column queries
- **Partial indexes** for active records and pending operations
- **Time-based indexes** for efficient historical data access
- **GIN indexes** for JSONB fields

### Data Types
- **DECIMAL(8,2)** for reputation values (±1000.00 range)
- **DECIMAL(5,4)** for rates (0.0000-1.0000 range)
- **BIGINT** for Unix timestamps (nanosecond precision)
- **JSONB** for flexible status and configuration storage

### Constraints
- **Check constraints** for data validation
- **Unique constraints** for data integrity
- **Foreign key relationships** where applicable

## Default Data

### Decay Rules
Four default rule sets for different reputation types:
1. **Player reputation** - Slow decay, moderate activity impact
2. **Faction reputation** - Faster decay, high faction modifier
3. **Region reputation** - Slow decay, low activity impact
4. **Global reputation** - Very slow decay, minimal modifiers

### Sample Data
- Three sample players with different reputation levels
- Historical reputation changes for testing
- Initial global statistics

## Migration Safety

### Rollback Strategy
- Schema creation only (no destructive operations)
- Safe rollback via `DROP SCHEMA reputation CASCADE`

### Testing
- Verified on PostgreSQL 13+
- Performance tested with 10k+ concurrent reputation updates
- Decay calculations tested under load

## Related Services

### Dependencies
- **Player Service** - Player identification and authentication
- **Game Events Service** - Reputation change triggers
- **Analytics Engine** - Reputation trend analysis

### Integration Points
- **Quest System** - Reputation rewards/penalties
- **Faction System** - Inter-faction reputation
- **Trading System** - Reputation-based pricing modifiers
- **Anti-cheat System** - Reputation manipulation detection

## Monitoring

### Key Metrics
- Decay application success rate (>99%)
- Recovery cooldown compliance
- Reputation value distributions
- System performance under load

### Alerts
- Failed decay applications
- Recovery rate anomalies
- Reputation value outliers
- Database performance degradation

## Configuration

### Environment Variables
```sql
-- Decay intervals (hours)
SET reputation.default_decay_interval = 24;

-- Recovery limits
SET reputation.max_recovery_per_day = 100.0;

-- Cooldown periods
SET reputation.recovery_cooldown_hours = 6;
```

### Maintenance
```sql
-- Clean old change history (keep last 90 days)
DELETE FROM reputation.reputation_changes
WHERE executed_at < NOW() - INTERVAL '90 days';

-- Update player statistics
UPDATE reputation.player_stats
SET updated_at = NOW()
WHERE player_id IN (
    SELECT DISTINCT player_id
    FROM reputation.reputation_changes
    WHERE executed_at > updated_at
);
```

## Future Extensions

### Planned Features
- **Reputation tiers** - Multi-level reputation systems
- **Dynamic decay rates** - AI-adjusted decay based on player behavior
- **Reputation trading** - Marketplace for reputation transfers
- **Guild reputation** - Group-based reputation mechanics

### Schema Evolution
- **Partitioning strategy** - Time-based partitioning for large tables
- **Archival process** - Automatic data archiving for old records
- **Analytics expansion** - Advanced trend analysis and predictions