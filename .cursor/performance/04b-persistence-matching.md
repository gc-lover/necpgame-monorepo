# 📖 Go Performance Bible - Part 4B

**MMO Patterns: Persistence, Matchmaking, Anti-Cheat**

---

# LEADERBOARD OPTIMIZATION

## 🔴 CRITICAL: Redis Sorted Sets

```go
type LeaderboardService struct {
    redis *redis.Client
}

func (ls *LeaderboardService) UpdateScore(playerID string, score int64) error {
    return ls.redis.ZAdd(ctx, "leaderboard:global", &redis.Z{
        Score:  float64(score),
        Member: playerID,
    }).Err()
}

func (ls *LeaderboardService) GetTopN(n int) ([]Player, error) {
    // O(log N + M)
    members, _ := ls.redis.ZRevRangeWithScores(ctx, "leaderboard:global", 0, int64(n-1)).Result()
    return players, nil
}

// Player rank: O(log N)
func (ls *LeaderboardService) GetRank(playerID string) (int64, error) {
    return ls.redis.ZRevRank(ctx, "leaderboard:global", playerID).Result()
}
```

**Gains:** O(log N) vs O(N) in SQL, millions of players

---

## 🟡 HIGH: Leaderboard Sharding

```go
type ShardedLeaderboard struct {
    shards map[string]*redis.Client // region -> redis
}

func (sl *ShardedLeaderboard) GetGlobalTopN(n int) ([]Player, error) {
    var allTopPlayers []Player
    
    // Parallel fetch
    g, ctx := errgroup.WithContext(ctx)
    
    for region, shard := range sl.shards {
        region, shard := region, shard
        g.Go(func() error {
            topN, _ := shard.ZRevRangeWithScores(ctx, "leaderboard:"+region, 0, int64(n-1)).Result()
            // Append...
            return nil
        })
    }
    
    g.Wait()
    
    // Merge and sort globally
    sort.Slice(allTopPlayers, func(i, j int) bool {
        return allTopPlayers[i].Score > allTopPlayers[j].Score
    })
    
    return allTopPlayers[:n], nil
}
```

**Gains:** Scale to billions

---

# SHARDING STRATEGIES

## 🔴 CRITICAL: Player Sharding

```go
type PlayerShard struct {
    shards []*sql.DB // 10 shards
}

func (ps *PlayerShard) GetShard(playerID uint64) *sql.DB {
    shardID := playerID % uint64(len(ps.shards))
    return ps.shards[shardID]
}

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
            results, _ := shard.Query("SELECT * FROM players WHERE id = ANY($1)", ids)
            
            mu.Lock()
            players = append(players, results...)
            mu.Unlock()
            
            return nil
        })
    }
    
    return players, g.Wait()
}
```

**Gains:** Linear scaling

---

# CQRS PATTERN

## 🟡 HIGH: Read/Write Separation

```go
// Write model (normalized)
type PlayerCommandRepo struct {
    masterDB *sql.DB
}

func (r *PlayerCommandRepo) UpdateStats(cmd UpdateStatsCommand) error {
    return r.masterDB.Exec("UPDATE players SET ...")
}

// Read model (denormalized, cached)
type PlayerQueryRepo struct {
    replicaDB *sql.DB
    cache     *redis.Client
}

func (r *PlayerQueryRepo) GetProfile(playerID string) (*Profile, error) {
    // Cache first
    if cached, err := r.cache.Get(ctx, "profile:"+playerID).Result(); err == nil {
        return unmarshal(cached), nil
    }
    
    // Replica query
    profile, _ := r.replicaDB.QueryRow("SELECT * FROM player_profiles WHERE id = $1", playerID)
    
    r.cache.Set(ctx, "profile:"+playerID, marshal(profile), 5*time.Minute)
    return profile, nil
}
```

**Benefits:** Independent scaling, optimized queries

---

# EVENT SOURCING

## 🟡 HIGH: Event Store

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
    // DB (audit trail)
    _, err := es.db.Exec(`INSERT INTO player_events ...`, event)
    
    // Kafka (real-time)
    es.kafka.Produce(&kafka.Message{
        Topic: "player-events",
        Value: marshal(event),
    })
    
    return err
}

// Replay для восстановления
func (es *EventStore) Replay(playerID uint64) (*Player, error) {
    events, _ := es.db.Query(`SELECT event_type, payload FROM player_events WHERE player_id = $1 ORDER BY timestamp`, playerID)
    
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

**Benefits:** Full audit trail, state recovery

---

# HOT RELOAD CONFIG

## 🟡 HIGH: Dynamic Config

```go
type ConfigService struct {
    config  atomic.Value // *GameConfig
    watcher *fsnotify.Watcher
}

func (cs *ConfigService) Start() {
    cs.watcher.Add("/etc/game/config.yaml")
    
    go func() {
        for event := range cs.watcher.Events {
            if event.Op&fsnotify.Write == fsnotify.Write {
                cs.reloadConfig()
            }
        }
    }()
}

func (cs *ConfigService) reloadConfig() {
    newConfig, err := loadConfigFromFile("/etc/game/config.yaml")
    if err != nil {
        log.Error("Failed to reload", err)
        return
    }
    
    cs.config.Store(newConfig) // Atomic swap
    log.Info("Config reloaded", "version", newConfig.Version)
}

// Lock-free read
func (cs *ConfigService) Get() *GameConfig {
    return cs.config.Load().(*GameConfig)
}
```

**Benefits:** No downtime, balance tweaks, A/B testing

---

# PERSISTENCE STRATEGIES

## 🔴 CRITICAL: Write-Behind Pattern

```go
type PlayerStateWriter struct {
    dirtyPlayers sync.Map
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
    
    if len(players) > 0 {
        psw.db.BatchUpdatePlayers(players)
    }
}
```

**Gains:** Writes ↓95%, instant latency, DB load ↓90%

---

## 🟡 HIGH: Snapshot + Delta

```go
type PlayerPersistence struct {
    snapshotInterval time.Duration
}

func (pp *PlayerPersistence) Save(player *Player) {
    now := time.Now()
    
    // Full snapshot every 5 min
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
    player, _ := pp.loadSnapshot(playerID)
    deltas, _ := pp.deltaWriter.GetDeltas(playerID, player.LastSnapshot)
    
    for _, delta := range deltas {
        player.ApplyDelta(delta)
    }
    
    return player, nil
}
```

**Benefits:** Fast saves, fast recovery

---

**Next:** [Part 4B continuation](./04b-persistence-matching.md)  
**Previous:** [Part 3B](./03b-tools-summary.md)

