# 📖 Go Performance Bible - Part 5A

**Advanced Database & Cache Patterns**

---

# ADVANCED DATABASE

## 🔴 CRITICAL: Time-Series Partitioning

```go
type PartitionManager struct {
    db *sql.DB
}

func (pm *PartitionManager) EnsurePartitions() {
    // Создать партиции на 7 дней вперед
    for i := 0; i < 7; i++ {
        date := time.Now().AddDate(0, 0, i)
        tableName := fmt.Sprintf("game_events_%s", date.Format("2006_01_02"))
        
        pm.db.Exec(`CREATE TABLE IF NOT EXISTS ` + tableName + `
            PARTITION OF game_events
            FOR VALUES FROM ('` + date.Format("2006-01-02") + `')
            TO ('` + date.AddDate(0, 0, 1).Format("2006-01-02") + `')`)
    }
    
    // Auto retention (удалить старые)
    pm.db.Exec(`DROP TABLE IF EXISTS game_events_` + 
        time.Now().AddDate(0, 0, -30).Format("2006_01_02"))
}
```

**Gains:** Query ↓90%, auto retention

---

## 🟡 HIGH: Materialized Views

```go
type RankingService struct {
    db *sql.DB
}

func (rs *RankingService) RefreshRankings() error {
    return rs.db.Exec("REFRESH MATERIALIZED VIEW CONCURRENTLY player_rankings").Err()
}

func (rs *RankingService) GetTopPlayers(n int) ([]Player, error) {
    return rs.db.Query("SELECT * FROM player_rankings ORDER BY total_score DESC LIMIT $1", n)
}
```

**Gains:** 5000ms → 50ms (100x!)

---

## 🟡 HIGH: Covering Indexes

```sql
-- Index covers entire query (no table lookup)
CREATE INDEX idx_players_covering 
ON players(level, is_active, id, health);

-- Query uses ONLY index
SELECT id, health FROM players 
WHERE level = 10 AND is_active = true;
```

**Gains:** Query ↓50-70%

---

## 🟢 MEDIUM: Partial Indexes

```sql
-- Only active players
CREATE INDEX idx_active_players 
ON players(level) 
WHERE is_active = true;

-- Only premium users
CREATE INDEX idx_premium 
ON inventory(player_id) 
WHERE is_premium = true;
```

**Gains:** Index size ↓60-80%

---

# DISTRIBUTED CACHE

## 🔴 CRITICAL: Pub/Sub Invalidation

```go
type DistributedCache struct {
    local  sync.Map
    redis  *redis.Client
    pubsub *redis.PubSub
}

func (dc *DistributedCache) Start() {
    dc.pubsub = dc.redis.Subscribe(ctx, "cache:invalidate")
    
    go func() {
        for msg := range dc.pubsub.Channel() {
            keys := parseKeys(msg.Payload)
            for _, key := range keys {
                dc.local.Delete(key)
            }
        }
    }()
}

func (dc *DistributedCache) Invalidate(key string) {
    dc.local.Delete(key)
    dc.redis.Publish(ctx, "cache:invalidate", key) // Notify all
}
```

**Benefits:** Consistent cache, no stale data

---

## 🟡 HIGH: Cache Warming

```go
type CacheWarmer struct {
    cache *Cache
    db    *sql.DB
}

func (cw *CacheWarmer) WarmUp() error {
    log.Info("Warming cache...")
    
    // Top 1000 players
    topPlayers, _ := cw.db.Query("SELECT * FROM players ORDER BY rating DESC LIMIT 1000")
    for topPlayers.Next() {
        var player Player
        topPlayers.Scan(&player)
        cw.cache.Set(player.ID, &player)
    }
    
    // Game configs
    configs, _ := cw.db.Query("SELECT * FROM game_configs")
    for configs.Next() {
        var config Config
        configs.Scan(&config)
        cw.cache.Set("config:"+config.Key, &config)
    }
    
    return nil
}
```

**Benefits:** No cache misses at startup

---

## 🟡 HIGH: Negative Caching

```go
const NegativeCacheMarker = "NOT_FOUND"

func (c *Cache) Get(key string) (interface{}, bool) {
    if val, ok := c.data.Load(key); ok {
        if val == NegativeCacheMarker {
            return nil, false // Cached absence
        }
        return val, true
    }
    
    val, err := c.db.Get(key)
    if err == ErrNotFound {
        c.data.Store(key, NegativeCacheMarker)
        return nil, false
    }
    
    if err == nil {
        c.data.Store(key, val)
    }
    
    return val, err == nil
}
```

**Prevents:** Repeated DB queries for missing data

---

**Next:** [Part 5B - World & Resilience](./05b-world-resilience.md)  
**Previous:** [Part 4C](./04c-matchmaking-anticheat.md)

