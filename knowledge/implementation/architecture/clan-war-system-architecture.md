<!-- Issue: #290 -->

# Clan War System Architecture

## Overview

This document defines the complete technical architecture for the clan war system in NECPGAME, supporting large-scale PvP warfare between guilds with territory control, battle phases, and reward distribution for MMO gameplay.

## Performance Requirements

**Target Metrics:**
- Concurrent wars: 100+ simultaneous clan wars
- Players per war: 200-500 active participants
- Battle instances: 50+ parallel territory battles
- Response time: P95 <500ms for critical operations
- Data consistency: Strong consistency for scoring, eventual for events

## System Components

### Core Microservices

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Clan War      │    │   Guild          │    │   Territory     │
│   Service       │◄──►│   Service        │◄──►│   Service       │
│                 │    │                  │    │                 │
│ • War Mgmt      │    │ • Clan Info      │    │ • Territory     │
│ • Battle Logic  │    │ • Alliances      │    │ • Resources     │
│ • Scoring       │    │ • Permissions    │    │ • Ownership     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                        │                        │
         └────────────────────────┼────────────────────────┘
                                  │
                    ┌─────────────────┐    ┌─────────────────┐
                    │   Battle        │    │   Event         │
                    │   Engine        │    │   Service       │
                    │                 │    │                 │
                    │ • PvP Logic     │    │ • Notifications │
                    │ • Matchmaking   │    │ • War Events    │
                    │ • Scoring       │    │ • Broadcasts    │
                    └─────────────────┘    └─────────────────┘
```

### Infrastructure Components

**Database Layer:**
- PostgreSQL: Primary data store for wars, battles, territories
- Redis: Session store, real-time battle state, pub/sub events
- TimescaleDB: Historical battle data and analytics

**Event Streaming:**
- Apache Kafka: War events, battle updates, territory changes
- Event sourcing: Complete audit trail of war progression

**Load Balancing:**
- Geographic distribution for global clan wars
- Auto-scaling based on active war count

## War Phases Architecture

### Phase 1: Preparation (24 hours)
**Objectives:** Alliance formation, resource gathering, strategy planning

**Key Activities:**
- War declaration and acceptance
- Alliance invitations and confirmations
- Territory scouting and defense preparation
- Resource stockpiling and troop deployment

**Technical Implementation:**
- War record creation with metadata
- Alliance management through guild service
- Territory locking for preparation period
- Event notifications to all participants

### Phase 2: Active Warfare (7 days)
**Objectives:** Territory conquest, battle victories, point accumulation

**Daily Cycle:**
- Territory battles (scheduled events)
- Siege events (defensive operations)
- Open-world PvP (continuous conflict)
- Point scoring and leaderboard updates

**Technical Implementation:**
- Battle instance spawning and management
- Real-time scoring and statistics
- Territory ownership transitions
- Automated event scheduling

### Phase 3: Resolution (Immediate)
**Objectives:** Winner determination, reward distribution, cleanup

**Key Activities:**
- Final score calculation and verification
- Territory transfers to winning clan
- Reward distribution (resources, items, titles)
- War statistics and achievements

**Technical Implementation:**
- Atomic transactions for territory transfers
- Reward calculation and distribution
- Historical data archiving
- Cleanup of temporary war data

## Territory Control System

### Territory Types

**Resource Territories:**
- Mines, farms, factories providing ongoing income
- Contested through daily resource battles
- Ownership provides passive bonuses

**Strategic Territories:**
- Key locations (bridges, forts, cities)
- Control provides tactical advantages
- High-value targets with heavy defense

**Capital Territories:**
- Clan headquarters and main cities
- Maximum defensive bonuses
- Siege mechanics for conquest

### Territory Mechanics

**Ownership States:**
```yaml
territory_states:
  - neutral: No clan ownership, open for claiming
  - owned: Single clan ownership with base defense
  - contested: Multiple clans battling for control
  - locked: Temporary lock during war phases
  - besieged: Under active siege attack
```

**Defense System:**
- Base defense value based on territory type
- Clan improvements (walls, towers, traps)
- Defender bonuses during siege events
- Attacker penalties for long-distance assaults

**Resource Generation:**
- Base income per territory type
- Clan bonuses and improvements
- Seasonal modifiers
- War-time production penalties

## Battle System Architecture

### Battle Types

**Territory Battles:**
- Scheduled daily events for resource territories
- 10-20 players per side, 15-minute duration
- Capture-the-point mechanics with scoring zones

**Siege Events:**
- Large-scale assaults on strategic territories
- 50+ players per side, 30-60 minute duration
- Multiple objectives and defensive positions

**Open World PvP:**
- Continuous conflict in war zones
- Kill scoring and territory influence
- No formal start/end times

### Battle Mechanics

**Scoring System:**
```sql
-- Points calculation per kill
kill_points = base_points * (
  1.0 +                        -- Base multiplier
  territory_bonus +            -- Location bonus
  time_bonus +                 -- Time-based bonus
  streak_bonus                 -- Kill streak bonus
)
```

**Victory Conditions:**
- Point threshold (territory battles)
- Objective completion (siege events)
- Time expiration with point differential
- Territory capture and hold duration

**Fair Play Mechanisms:**
- Anti-cheat validation for suspicious activity
- Automated bot detection
- Manual review queue for disputed results
- Compensation system for technical issues

## Rewards and Progression System

### Reward Categories

**Individual Rewards:**
- Experience points and level progression
- Currency (gold, premium currency)
- Equipment and cosmetic items
- Achievement unlocks and titles

**Clan Rewards:**
- Guild funds and resources
- Territory improvements and buildings
- Clan-wide bonuses and perks
- Alliance reputation points

**Special Rewards:**
- Seasonal achievements and leaderboards
- Unique war-themed cosmetics
- Limited-time event rewards
- Tournament qualifications

### Reward Distribution

**Fair Distribution Algorithm:**
```python
def distribute_rewards(war_result, participants):
    total_points = sum(p.points for p in participants)
    for participant in participants:
        contribution_ratio = participant.points / total_points
        base_reward = get_base_reward(participant.level)
        final_reward = base_reward * (
            contribution_ratio * performance_multiplier +
            clan_bonus + alliance_bonus +
            territory_bonus
        )
        participant.rewards.append(final_reward)
```

**Guaranteed Rewards:**
- Minimum participation rewards
- Effort-based bonuses (not just victory)
- Streak protection for losing clans
- Compensation for technical disconnects

## Database Schema Design

### Core Tables

**clan_wars:**
```sql
CREATE TABLE clan_wars (
    id UUID PRIMARY KEY,
    attacking_clan_id UUID REFERENCES clans(id),
    defending_clan_id UUID REFERENCES clans(id),
    status VARCHAR(20) NOT NULL, -- preparation, active, resolution, completed
    phase_start_time TIMESTAMP WITH TIME ZONE,
    phase_end_time TIMESTAMP WITH TIME ZONE,
    attacking_allies JSONB, -- Array of allied clan IDs
    defending_allies JSONB, -- Array of allied clan IDs
    war_settings JSONB, -- Territory list, duration, rules
    attacking_score BIGINT DEFAULT 0,
    defending_score BIGINT DEFAULT 0,
    winner_clan_id UUID REFERENCES clans(id),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Performance indexes
CREATE INDEX idx_clan_wars_status ON clan_wars(status);
CREATE INDEX idx_clan_wars_participants ON clan_wars(attacking_clan_id, defending_clan_id);
CREATE INDEX idx_clan_wars_active ON clan_wars(status) WHERE status IN ('preparation', 'active');
```

**war_battles:**
```sql
CREATE TABLE war_battles (
    id UUID PRIMARY KEY,
    war_id UUID REFERENCES clan_wars(id) ON DELETE CASCADE,
    territory_id UUID REFERENCES territories(id),
    battle_type VARCHAR(20) NOT NULL, -- territory, siege, open_world
    status VARCHAR(20) NOT NULL, -- scheduled, active, completed, cancelled
    scheduled_start TIMESTAMP WITH TIME ZONE,
    actual_start TIMESTAMP WITH TIME ZONE,
    actual_end TIMESTAMP WITH TIME ZONE,
    attacking_participants JSONB,
    defending_participants JSONB,
    attacking_score BIGINT DEFAULT 0,
    defending_score BIGINT DEFAULT 0,
    winner VARCHAR(20), -- attacking, defending, draw
    battle_events JSONB, -- Detailed event log
    created_at TIMESTAMP WITH TIME ZONE
);

-- Indexes for real-time queries
CREATE INDEX idx_war_battles_war ON war_battles(war_id);
CREATE INDEX idx_war_battles_active ON war_battles(status, scheduled_start) WHERE status = 'active';
CREATE INDEX idx_war_battles_territory ON war_battles(territory_id);
```

**war_participants:**
```sql
CREATE TABLE war_participants (
    id UUID PRIMARY KEY,
    war_id UUID REFERENCES clan_wars(id) ON DELETE CASCADE,
    player_id UUID REFERENCES players(id) ON DELETE CASCADE,
    clan_id UUID REFERENCES clans(id),
    alliance_side VARCHAR(20), -- attacking, defending
    joined_at TIMESTAMP WITH TIME ZONE,
    last_activity TIMESTAMP WITH TIME ZONE,
    total_score BIGINT DEFAULT 0,
    battles_participated INTEGER DEFAULT 0,
    kills INTEGER DEFAULT 0,
    deaths INTEGER DEFAULT 0,
    objectives_completed INTEGER DEFAULT 0,
    rewards_earned JSONB,
    UNIQUE(war_id, player_id)
);

-- Performance indexes
CREATE INDEX idx_war_participants_war ON war_participants(war_id);
CREATE INDEX idx_war_participants_player ON war_participants(player_id);
CREATE INDEX idx_war_participants_clan ON war_participants(clan_id);
CREATE INDEX idx_war_participants_active ON war_participants(last_activity) WHERE last_activity > NOW() - INTERVAL '24 hours';
```

**territories:**
```sql
CREATE TABLE territories (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL, -- resource, strategic, capital
    region VARCHAR(50),
    coordinates JSONB, -- Geographic boundaries
    owner_clan_id UUID REFERENCES clans(id),
    ownership_start TIMESTAMP WITH TIME ZONE,
    defense_level INTEGER DEFAULT 1,
    resource_generation JSONB, -- Income rates by resource type
    improvements JSONB, -- Buildings, upgrades, defenses
    battle_history JSONB, -- Recent battle results
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Spatial and ownership indexes
CREATE INDEX idx_territories_owner ON territories(owner_clan_id);
CREATE INDEX idx_territories_type ON territories(type);
CREATE INDEX idx_territories_region ON territories(region);
```

### Partitioning Strategy

**Time-based partitioning for war data:**
```sql
-- Partition war_participants by war end date
CREATE TABLE war_participants_2025q1 PARTITION OF war_participants
    FOR VALUES FROM ('2025-01-01') TO ('2025-04-01');

-- Partition war_battles by month for active wars
CREATE TABLE war_battles_202512 PARTITION OF war_battles
    FOR VALUES FROM ('2025-12-01') TO ('2026-01-01');
```

## API Design (High Level)

### War Management API

```
POST   /api/v1/clan-wars              # Declare war
GET    /api/v1/clan-wars/{id}         # Get war details
PUT    /api/v1/clan-wars/{id}         # Update war settings
DELETE /api/v1/clan-wars/{id}         # Cancel war (admin only)

POST   /api/v1/clan-wars/{id}/alliances # Invite allies
POST   /api/v1/clan-wars/{id}/start    # Start active phase
POST   /api/v1/clan-wars/{id}/end      # Force end war
```

### Battle Management API

```
POST   /api/v1/clan-wars/{warId}/battles # Create battle
GET    /api/v1/clan-wars/{warId}/battles # List battles
GET    /api/v1/clan-wars/{warId}/battles/{id} # Get battle details

POST   /api/v1/battles/{id}/join       # Join battle
POST   /api/v1/battles/{id}/leave      # Leave battle
POST   /api/v1/battles/{id}/score      # Report score event
```

### Territory Management API

```
GET    /api/v1/territories             # List territories
GET    /api/v1/territories/{id}        # Get territory details
PUT    /api/v1/territories/{id}/owner  # Transfer ownership

GET    /api/v1/territories/{id}/improvements # Get improvements
POST   /api/v1/territories/{id}/improvements # Add improvement
DELETE /api/v1/territories/{id}/improvements/{impId} # Remove improvement
```

### Statistics and Leaderboards API

```
GET    /api/v1/clan-wars/{id}/stats    # War statistics
GET    /api/v1/clan-wars/{id}/leaderboard # Participant rankings
GET    /api/v1/territories/{id}/history # Territory ownership history

GET    /api/v1/clans/{id}/war-history # Clan's war history
GET    /api/v1/players/{id}/war-stats # Player's war statistics
```

## Event-Driven Architecture

### Event Types

**War Events:**
- `war.declared`: War announcement with participants
- `war.phase_changed`: Phase transitions (preparation → active → resolution)
- `war.ended`: Final results and rewards
- `war.cancelled`: War termination

**Battle Events:**
- `battle.scheduled`: Upcoming battle announcement
- `battle.started`: Battle commencement
- `battle.score_update`: Real-time scoring
- `battle.ended`: Battle results

**Territory Events:**
- `territory.contested`: Territory under attack
- `territory.captured`: Ownership change
- `territory.improved`: Defense/structure upgrades

### Event Processing

**Real-time Updates:**
```javascript
// Redis pub/sub for live battle updates
const subscriber = redis.createClient();
subscriber.subscribe('war:123:battles:456:events');

subscriber.on('message', (channel, message) => {
  const event = JSON.parse(message);
  updateBattleUI(event);
});
```

**Event Sourcing:**
- Complete audit trail for dispute resolution
- Replay capability for debugging
- Analytics data for balancing

## Integration Patterns

### Service Mesh Communication

**Clan War Service Dependencies:**
- Guild Service: Clan information, permissions, alliances
- Territory Service: Territory data, ownership, resources
- Battle Engine: PvP logic, matchmaking, scoring
- Event Service: Notifications, broadcasts, achievements
- Economy Service: Reward distribution, currency transactions

### Cross-Service Transactions

**Saga Pattern for War Resolution:**
1. Calculate final scores (Clan War Service)
2. Verify score integrity (Audit Service)
3. Transfer territories (Territory Service)
4. Distribute rewards (Economy Service)
5. Update achievements (Achievement Service)
6. Send notifications (Event Service)

**Compensation Actions:**
- Rollback territory transfers on reward failure
- Refund partial distributions
- Maintain data consistency across services

## Monitoring and Analytics

### Key Metrics

**War Health:**
- Active wars count
- Average war duration
- Player participation rate
- Technical issues per war

**Battle Performance:**
- Battle completion rate
- Average battle duration
- Player retention during battles
- Server performance during peak battles

**Economic Impact:**
- Total rewards distributed
- Resource generation from territories
- Clan economy growth during wars

### Alerting Rules

```
ALERT ClanWarHighLoad
  IF clan_war_active_count > 50 FOR 5m
  LABELS { severity = "warning" }

ALERT ClanWarBattleFailure
  IF clan_war_battle_failure_rate > 5% FOR 10m
  LABELS { severity = "critical" }

ALERT ClanWarDataInconsistency
  IF clan_war_score_discrepancy > 0 FOR 1m
  LABELS { severity = "critical" }
```

## Security Considerations

### Anti-Cheat Measures

**Battle Integrity:**
- Client-server score validation
- Suspicious pattern detection
- Manual review queue for high-value battles
- Automated ban system for confirmed cheating

**War Manipulation:**
- Alliance validation and limits
- Declaration cooldowns and restrictions
- Administrative override capabilities
- Audit trail for all war actions

### Data Protection

**Player Privacy:**
- Anonymized statistics for public leaderboards
- Opt-in data sharing for research
- Secure handling of personal battle records

**Clan Security:**
- Permission-based access to war planning
- Secure communication channels for strategy
- Protection against espionage mechanics

## Deployment Architecture

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: clan-war-service
spec:
  replicas: 5
  template:
    spec:
      containers:
      - name: clan-war-api
        image: necpgame/clan-war-service:v1.0.0
        resources:
          requests:
            cpu: 1000m
            memory: 2Gi
          limits:
            cpu: 2000m
            memory: 4Gi
        env:
        - name: WAR_CONCURRENCY_LIMIT
          value: "20"
        - name: BATTLE_MAX_PARTICIPANTS
          value: "100"
```

### Scaling Strategy

**Horizontal Pod Autoscaling:**
```yaml
metrics:
- type: Resource
  resource:
    name: cpu
    target:
      type: Utilization
      averageUtilization: 70
- type: Pods
  pods:
    metric:
      name: clan_war_active_wars
    target:
      type: AverageValue
      averageValue: "10"
```

**Geographic Distribution:**
- Regional clusters for global clan wars
- Cross-region battle matchmaking
- Data replication for consistency

## Migration Strategy

### Phase 1: Core Infrastructure
1. Deploy clan war service
2. Implement basic war declaration and phases
3. Set up territory system foundations

### Phase 2: Battle System
1. Implement battle engine integration
2. Add territory control mechanics
3. Deploy scoring and reward systems

### Phase 3: Advanced Features
1. Alliance system completion
2. Advanced siege mechanics
3. Tournament and seasonal features

## Success Criteria

- [ ] War declaration and alliance management working
- [ ] Territory system with ownership and resources functional
- [ ] Battle system supporting all battle types
- [ ] Real-time scoring and leaderboards operational
- [ ] Reward distribution system complete
- [ ] Event system providing live updates
- [ ] P95 latency <500ms for critical operations
- [ ] Support for 100+ concurrent wars
- [ ] Comprehensive monitoring and alerting
- [ ] Anti-cheat and fair play mechanisms active

## Risk Assessment

### High Risk
- **Data consistency during high-load battles:** Mitigated by strong consistency requirements
- **Fair play and anti-cheat complexity:** Mitigated by comprehensive validation system
- **Real-time performance at scale:** Mitigated by horizontal scaling and optimization

### Medium Risk
- **Complex alliance mechanics:** Mitigated by clear API design
- **Territory transfer atomicity:** Mitigated by saga pattern implementation
- **Event storm during peak battles:** Mitigated by event batching and filtering

## Next Steps

### Immediate Implementation Tasks
1. **Database:** Create clan_war, war_battle, war_participant, territory tables
2. **API Designer:** Design comprehensive REST API for war management
3. **Backend:** Implement clan war service with phase management
4. **Network:** Design real-time event system for battle updates
5. **DevOps:** Deploy scalable infrastructure for war load

### Subsystem Development
- Clan War Management Service (Go microservice)
- Battle Engine Integration (existing service extension)
- Territory Control System (database + API)
- Real-time Event Streaming (Kafka + WebSocket)
- Reward Distribution Engine (economy service integration)

This architecture provides a comprehensive, scalable system for large-scale clan warfare in NECPGAME, supporting hundreds of concurrent wars with fair play mechanics, real-time updates, and engaging reward systems.