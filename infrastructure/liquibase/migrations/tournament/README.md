# Tournament Service Database Migrations

This directory contains Liquibase migrations for the Tournament Service database schema.

## Overview

The Tournament Service manages competitive tournaments, leaderboards, matchmaking, and competitive gameplay experiences in Night City.

## Schema Structure

### tournament.tournaments
Main tournament table containing tournament metadata, settings, and status.

**Key Fields:**
- `id`: UUID primary key
- `name`: Tournament display name
- `tournament_type`: single_elimination, double_elimination, round_robin, swiss
- `status`: draft, active, completed, cancelled
- `max_players`: Maximum number of participants
- `prize_pool`: Total prize pool value
- `rules`: JSONB with tournament-specific rules
- `metadata`: JSONB with additional tournament data

### tournament.participants
Tournament participants with their performance statistics.

**Key Fields:**
- `user_id`: Player UUID
- `tournament_id`: Tournament UUID
- `seed`: Tournament seeding position
- `status`: registered, active, eliminated, disqualified
- `total_score`: Cumulative tournament score
- Performance metrics: wins, losses, draws, average_score

### tournament.matches
Individual tournament matches within brackets.

**Key Fields:**
- `tournament_id`: Parent tournament
- `round`: Tournament round number
- `position`: Bracket position
- `player1_id`, `player2_id`: Competing players
- `winner_id`: Match winner
- `score1`, `score2`: Match scores
- `status`: pending, in_progress, completed, cancelled

### tournament.leaderboard_entries
Real-time leaderboard for active tournaments.

**Key Fields:**
- `tournament_id`: Tournament reference
- `user_id`: Player UUID
- `rank`: Current ranking position
- `score`: Tournament score
- Performance metrics and win rate

### tournament.spectators
Spectator tracking for tournament viewing.

**Key Fields:**
- `tournament_id`: Tournament being spectated
- `user_id`: Spectator UUID
- `joined_at`, `left_at`: Spectator session times

## Performance Optimizations

- **Indexes**: Strategic indexing on frequently queried fields
- **JSONB**: Flexible storage for tournament rules and metadata
- **UUID References**: Foreign key relationships for data integrity
- **Partitioning Ready**: Schema designed for potential table partitioning

## Migration Notes

- **V001**: Initial schema creation with core tournament tables
- Schema designed for MMOFPS performance requirements (<15ms P99 latency)
- Supports complex tournament bracket systems
- Real-time leaderboard updates and spectator tracking

## Related Services

- **tournament-bracket-service-go**: Bracket generation and management
- **tournament-service-go**: Main tournament API service
- **leaderboard-service-go**: Global leaderboard integration