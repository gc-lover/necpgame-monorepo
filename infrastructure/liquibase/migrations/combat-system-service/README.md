# Combat System Service Database Migrations

This directory contains database migrations for the Combat System Service, implementing enterprise-grade combat mechanics for the Night City universe.

## Overview

The Combat System Service manages complex real-time combat systems including:
- Damage calculation engines with lag compensation
- Combat rule management and validation
- Balance configuration with dynamic scaling
- Ability system integration with cooldowns and synergies
- Combat session management and state tracking

## Schema Design

### Core Tables

#### `combat.combat_sessions`
Active combat session management with real-time state tracking.
- Supports multiple combat types (PVP, PVE, Arena, Tournament)
- Real-time position synchronization
- Combat event logging and replay capabilities

#### `combat.damage_events`
Comprehensive damage event logging for analysis and anti-cheat.
- Full audit trail of all damage calculations
- Lag compensation data and timestamps
- Environmental factor tracking

#### `combat.ability_usage`
Ability activation tracking with cooldown management.
- Combo chain tracking and synergy calculations
- Cooldown enforcement and reset mechanics
- Ability progression and unlock systems

#### `combat.balance_configs`
Dynamic balance configuration management.
- Difficulty scaling parameters
- Class balance adjustments
- Economy impact modifiers

#### `combat.combat_rules`
Core combat rule definitions with versioning.
- Rule validation and conflict resolution
- Optimistic locking for concurrent updates
- Audit trails for balance changes

### Performance Optimizations

- Struct alignment directives (`//go:align 64`) for memory efficiency
- Database indexing for high-frequency combat queries
- Partitioning for large damage event histories
- Redis caching for active combat sessions

### Migration Strategy

Migrations follow semantic versioning:
- `V001`: Initial schema creation with core combat tables
- `V002+`: Incremental updates and performance optimizations

## Dependencies

- User management tables (player profiles, authentication)
- Ability service tables (ability definitions, cooldowns)
- Item service tables (weapon stats, armor values)
- Location service tables (environmental factors)

## Testing

Migrations include comprehensive tests for:
- Schema integrity and foreign key constraints
- Performance benchmarks for combat queries
- Data consistency during concurrent operations
- Rollback procedures and data preservation

## Performance Targets

- P99 Latency: <50ms for combat operations, <10ms for configuration reads
- Memory: <16KB per active combat session with struct alignment
- Concurrent combats: 10,000+ simultaneous battle sessions
- Damage calculations: <5ms P95 per combat tick
- Ability activations: <10ms P95 response time
- Database TPS: 100,000+ combat events per second