}

// Batch query across shards
func (ps *PlayerShard) GetPlayersBatch(playerIDs []uint64) ([]Player, error) {
    // Group by shard
    shardGroups := make(map[int][]uint64)
    for _, id := range playerIDs {
        shardID := int(id % uint64(len(ps.shards)))
        shardGroups[shardID] = append(shardGroups[shardID], id)
    }
    
    // Parallel queries
    var mu sync.Mutex
    var players []Player
    
    g, ctx := errgroup.WithContext(ctx)
    for shardID, ids := range shardGroups {
        shardID, ids := shardID, ids
        g.Go(func() error {
            shard := ps.shards[shardID]
            results, err := shard.Query("SELECT * FROM players WHERE id = ANY($1)", ids)
            
            mu.Lock()
            players = append(players, results...)
            mu.Unlock()
            
            return err
        })
    }
    
    return players, g.Wait()
}
```

**Gains:** Linear scaling, no single DB bottleneck

---

# CQRS PATTERN

## 🟡 HIGH: Read/Write Separation

**Что:** Разделение read (query) и write (command) моделей

```go
// Write model (normalized, transactional)
type PlayerCommandRepo struct {
    masterDB *sql.DB
}

func (r *PlayerCommandRepo) UpdatePlayerStats(cmd UpdateStatsCommand) error {
    // Write to master
    return r.masterDB.Exec("UPDATE players SET ...")
}

// Read model (denormalized, optimized for queries)
type PlayerQueryRepo struct {
    replicaDB *sql.DB
    cache     *redis.Client
}

func (r *PlayerQueryRepo) GetPlayerProfile(playerID string) (*PlayerProfile, error) {
    // Try cache
    if cached, err := r.cache.Get(ctx, "profile:"+playerID).Result(); err == nil {
        return unmarshal(cached), nil
    }
    
    // Read from replica
    profile, err := r.replicaDB.QueryRow("SELECT * FROM player_profiles WHERE id = $1", playerID)
    
    // Cache result
    r.cache.Set(ctx, "profile:"+playerID, marshal(profile), 5*time.Minute)
    
    return profile, err
}
```

**Benefits:**
- Write model: ACID, normalized
- Read model: Fast, denormalized, cached
- Independent scaling

---

# EVENT SOURCING

## 🟡 HIGH: Event Store для Audit Trail

**Что:** Храни все события для replay/audit

```go
type EventStore struct {
    db    *sql.DB
    kafka *kafka.Producer
}

type PlayerEvent struct {
    EventID   uint64
    PlayerID  uint64
    EventType string
    Payload   []byte
    Timestamp time.Time
}

func (es *EventStore) Append(event PlayerEvent) error {
    // 1. Append to DB (audit trail)
    _, err := es.db.Exec(`
        INSERT INTO player_events (player_id, event_type, payload, timestamp)
        VALUES ($1, $2, $3, $4)
    `, event.PlayerID, event.EventType, event.Payload, event.Timestamp)
    
    // 2. Publish to Kafka (real-time processing)
    es.kafka.Produce(&kafka.Message{
        Topic: "player-events",
        Value: marshal(event),
    })
    
    return err
}

// Replay events для восстановления состояния
func (es *EventStore) Replay(playerID uint64) (*Player, error) {
    events, _ := es.db.Query(`
        SELECT event_type, payload 
        FROM player_events 
        WHERE player_id = $1 
        ORDER BY timestamp
    `)
    
    player := &Player{ID: playerID}
    for events.Next() {
        var eventType string
        var payload []byte
        events.Scan(&eventType, &payload)
        
        player.ApplyEvent(eventType, payload)
    }
    
    return player, nil
}
```

**Benefits:**
- Полный audit trail
- Можно восстановить любое состояние
- Анализ игрового поведения

---

# HOT RELOAD CONFIG

## 🟡 HIGH: Dynamic Config Reload

**Что:** Обновление конфига без restart

```go
type ConfigService struct {
    config atomic.Value // *GameConfig
    watcher *fsnotify.Watcher
}

func (cs *ConfigService) Start() {
    cs.watcher.Add("/etc/game/config.yaml")
    
    go func() {
        for {
            select {
            case event := <-cs.watcher.Events:
                if event.Op&fsnotify.Write == fsnotify.Write {
                    cs.reloadConfig()
                }
            }
        }
    }()
}

func (cs *ConfigService) reloadConfig() {
    newConfig, err := loadConfigFromFile("/etc/game/config.yaml")
    if err != nil {
        log.Error("Failed to reload config", err)
        return
    }
    
    // Atomic swap
    cs.config.Store(newConfig)
    log.Info("Config reloaded", "version", newConfig.Version)
}

// Lock-free read
func (cs *ConfigService) Get() *GameConfig {
    return cs.config.Load().(*GameConfig)
}
```

**Benefits:** 
- No downtime для config changes
- Balance adjustments без restart
- A/B testing на лету

---

# PERSISTENCE STRATEGIES

## 🔴 CRITICAL: Write-Behind Pattern

**Что:** Async writes для player state

```go
type PlayerStateWriter struct {
    dirtyPlayers sync.Map // playerID -> *Player
    ticker       *time.Ticker
}

func (psw *PlayerStateWriter) MarkDirty(player *Player) {
    psw.dirtyPlayers.Store(player.ID, player)
}

func (psw *PlayerStateWriter) Start() {
    psw.ticker = time.NewTicker(5 * time.Second)
    
    go func() {
        for range psw.ticker.C {
            psw.flush()
        }
    }()
}

func (psw *PlayerStateWriter) flush() {
    var players []*Player
    
    psw.dirtyPlayers.Range(func(key, value interface{}) bool {
        players = append(players, value.(*Player))
        psw.dirtyPlayers.Delete(key)
        return true
    })
    
    if len(players) == 0 {
        return
    }
    
    // Batch write to DB
    psw.db.BatchUpdatePlayers(players)
}
```

**Gains:** 
- Writes ↓95% (batch every 5s)
- Latency: instant (no wait for DB)
- DB load ↓90%

---

## 🟡 HIGH: Snapshot + Delta Pattern

**Что:** Snapshots + incremental saves

```go
type PlayerPersistence struct {
    snapshotInterval time.Duration
    deltaWriter      *DeltaWriter
}

func (pp *PlayerPersistence) Save(player *Player) {
    now := time.Now()
    
    // Full snapshot каждые 5 минут
    if now.Sub(player.LastSnapshot) > pp.snapshotInterval {
        pp.saveSnapshot(player)
        player.LastSnapshot = now
        player.Deltas = nil
        return
    }
    
    // Incremental delta
    delta := player.GetDelta()
    pp.deltaWriter.Append(delta)
}

func (pp *PlayerPersistence) Load(playerID uint64) (*Player, error) {
    // Load last snapshot
    player, _ := pp.loadSnapshot(playerID)
    
    // Apply deltas
    deltas, _ := pp.deltaWriter.GetDeltas(playerID, player.LastSnapshot)
    for _, delta := range deltas {
        player.ApplyDelta(delta)
    }
    
    return player, nil
}
```

**Benefits:**
- Fast saves (small deltas)
- Fast recovery (snapshot + deltas)
- Reduce write amplification

---

# MATCHMAKING OPTIMIZATION

## 🔴 CRITICAL: Skill-Based Buckets

**Что:** Bucketing по skill для O(1) matching

```go
type MatchmakingQueue struct {
    buckets []*SkillBucket // 0-1000, 1000-2000, etc.
    mu      sync.RWMutex
}

type SkillBucket struct {
    minSkill int
    maxSkill int
    players  []*Player
    mu       sync.Mutex
}

func (mq *MatchmakingQueue) AddPlayer(player *Player) {
    bucketID := player.Skill / 1000 // 1000 skill per bucket
    bucket := mq.buckets[bucketID]
    
    bucket.mu.Lock()
    bucket.players = append(bucket.players, player)
    bucket.mu.Unlock()
    
    // Try match immediately
    go mq.tryMatch(bucketID)
}

func (mq *MatchmakingQueue) tryMatch(bucketID int) {
    bucket := mq.buckets[bucketID]
    
    bucket.mu.Lock()
    defer bucket.mu.Unlock()
    
    if len(bucket.players) >= 10 { // Match size
        match := bucket.players[:10]
        bucket.players = bucket.players[10:]
        
        go mq.createMatch(match)
    }
}
```

**Gains:**
- Matching: O(1) vs O(N)
- Latency: <10ms vs seconds
- Scalable to millions in queue

---

## 🟡 HIGH: Matchmaking Timeout Expansion

**Что:** Расширение поиска при долгом ожидании

```go
func (mq *MatchmakingQueue) FindMatch(player *Player) (*Match, error) {
    baseBucket := player.Skill / 1000
    searchRadius := 0
    
    ticker := time.NewTicker(5 * time.Second)
    defer ticker.Stop()
    
    for {
        select {
        case <-ticker.C:
            searchRadius++ // Expand search
            
            // Try matching in expanded range
            for i := baseBucket - searchRadius; i <= baseBucket + searchRadius; i++ {
                if match := mq.tryMatchInBucket(i, player); match != nil {
                    return match, nil
                }
            }
            
        case <-time.After(60 * time.Second):
            return nil, ErrMatchmakingTimeout
        }
    }
}
```

---

# ANTI-CHEAT INTEGRATION

## 🟡 HIGH: Server-Side Validation

**Что:** Валидация игровых действий

```go
type ActionValidator struct {
    lastActions sync.Map // playerID -> *LastAction
}

func (av *ActionValidator) ValidateShot(playerID uint64, shot *ShotAction) error {
    // Check rate (не >10 shots/sec)
    if last, ok := av.lastActions.Load(playerID); ok {
        lastAction := last.(*LastAction)
        if time.Since(lastAction.Timestamp) < 100*time.Millisecond {
            return ErrTooFast // Potential aimbot
        }
    }
    
    // Check distance (не >1000m)
    if shot.Distance > 1000 {
        return ErrImpossibleShot
    }
    
    // Check line of sight
    if !av.hasLineOfSight(shot.From, shot.To) {
        return ErrWallhack
    }
    
    av.lastActions.Store(playerID, &LastAction{Timestamp: time.Now()})
    return nil
}
```

---

# 📊 MMO-Specific Gains

## After implementing MMO patterns:

| Feature | Without | With | Gain |
|---------|---------|------|------|
| **Session lookup** | 50ms | 1-2ms | -96% |
| **Inventory load** | 100ms | 5-10ms | -90% |
| **Guild ops** | 500ms | 10-20ms | -96% |
| **Leaderboard** | O(N) slow | O(log N) | 1000x+ |
| **Matchmaking** | O(N²) | O(1) | ∞ scale |
| **Trading** | Deadlocks | No locks | Stable |

---

**Previous:** [Part 3A](./03a-profiling-testing.md)  
**Main:** [GO_BACKEND_PERFORMANCE_BIBLE.md](../GO_BACKEND_PERFORMANCE_BIBLE.md)

