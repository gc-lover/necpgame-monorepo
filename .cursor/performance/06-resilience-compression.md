# 📖 Go Performance Bible - Part 6

**Resilience, Compression & Production Patterns**

---

# COMPRESSION ALGORITHMS

## 🔴 CRITICAL: Adaptive Compression

**Что:** Выбирай compression в зависимости от данных

```go
type AdaptiveCompressor struct {
    lz4   *lz4.Compressor   // Fast, medium ratio
    zstd  *zstd.Encoder     // Slower, high ratio
    threshold int            // Size threshold
}

func (ac *AdaptiveCompressor) Compress(data []byte) ([]byte, error) {
    // Small data: no compression (overhead > gain)
    if len(data) < ac.threshold {
        return data, nil
    }
    
    // Real-time data (position updates): LZ4 (fast!)
    if isRealtimeData(data) {
        return ac.lz4.Compress(data), nil
    }
    
    // Bulk data (inventory, stats): Zstandard (best ratio)
    return ac.zstd.Compress(data), nil
}
```

**Benchmarks:**
- LZ4: 500MB/s encode, ratio 2-3x
- Zstandard: 100MB/s encode, ratio 3-5x
- No compression для <100 bytes

---

## 🟡 HIGH: Dictionary Compression

**Что:** Shared dictionary для game data

```go
type DictionaryCompressor struct {
    dict []byte // Pre-trained dictionary
    zstd *zstd.Encoder
}

func NewDictionaryCompressor() *DictionaryCompressor {
    // Train dictionary на типичных game packets
    samples := collectSamplePackets() // 100+ примеров
    dict := zstd.BuildDict(samples)
    
    encoder, _ := zstd.NewWriter(nil, zstd.WithEncoderDict(dict))
    
    return &DictionaryCompressor{
        dict: dict,
        zstd: encoder,
    }
}

func (dc *DictionaryCompressor) Compress(data []byte) []byte {
    return dc.zstd.EncodeAll(data, nil)
}
```

**Gains:** Compression ratio ↑30-50% vs standard

---

# DATABASE RESILIENCE

## 🔴 CRITICAL: Connection Retry with Backoff

**Что:** Exponential backoff для DB reconnect

```go
type DBConnector struct {
    config DBConfig
}

func (dbc *DBConnector) Connect() (*sql.DB, error) {
    var db *sql.DB
    var err error
    
    backoff := 100 * time.Millisecond
    maxBackoff := 30 * time.Second
    
    for retries := 0; retries < 10; retries++ {
        db, err = sql.Open("postgres", dbc.config.DSN)
        if err == nil {
            if err = db.Ping(); err == nil {
                return db, nil // Success
            }
        }
        
        log.Warn("DB connection failed, retrying",
            "retry", retries,
            "backoff", backoff,
            "error", err)
        
        time.Sleep(backoff)
        backoff *= 2 // Exponential backoff
        if backoff > maxBackoff {
            backoff = maxBackoff
        }
    }
    
    return nil, fmt.Errorf("failed to connect after retries: %w", err)
}
```

**Prevents:** Service crash при DB outage

---

## 🟡 HIGH: Circuit Breaker для DB

**Что:** Автоматическое отключение при DB проблемах

```go
type DBCircuitBreaker struct {
    db     *sql.DB
    cb     *gobreaker.CircuitBreaker
}

func NewDBCircuitBreaker(db *sql.DB) *DBCircuitBreaker {
    cb := gobreaker.NewCircuitBreaker(gobreaker.Settings{
        Name:        "database",
        MaxRequests: 3,
        Interval:    10 * time.Second,
        Timeout:     30 * time.Second,
        OnStateChange: func(name string, from, to gobreaker.State) {
            log.Warn("Circuit breaker state changed",
                "name", name,
                "from", from,
                "to", to)
        },
    })
    
    return &DBCircuitBreaker{db: db, cb: cb}
}

func (dbcb *DBCircuitBreaker) Query(query string, args ...interface{}) (*sql.Rows, error) {
    result, err := dbcb.cb.Execute(func() (interface{}, error) {
        return dbcb.db.Query(query, args...)
    })
    
    if err != nil {
        return nil, err
    }
    
    return result.(*sql.Rows), nil
}
```

**Benefits:** Fast fail, auto recovery

---

# GRACEFUL DEGRADATION

## 🔴 CRITICAL: Feature Flags

**Что:** Отключай features под нагрузкой

```go
type FeatureFlags struct {
    flags sync.Map
}

func (ff *FeatureFlags) IsEnabled(feature string) bool {
    if val, ok := ff.flags.Load(feature); ok {
        return val.(bool)
    }
    return true // Default: enabled
}

// Автоматическое отключение при высокой нагрузке
func (ff *FeatureFlags) AdaptToLoad(cpuUsage float64) {
    if cpuUsage > 80 {
        // Disable non-critical features
        ff.flags.Store("chat_history", false)
        ff.flags.Store("detailed_stats", false)
        ff.flags.Store("leaderboard_updates", false)
        
        log.Warn("High CPU, disabled non-critical features", "cpu", cpuUsage)
    } else if cpuUsage < 50 {
        // Re-enable
        ff.flags.Store("chat_history", true)
        ff.flags.Store("detailed_stats", true)
        ff.flags.Store("leaderboard_updates", true)
    }
}
```

**Benefits:** Service stays up, critical features work

---

## 🟡 HIGH: Fallback Strategies

**Что:** Fallback при недоступности сервиса

```go
func (s *Service) GetPlayerProfile(playerID string) (*Profile, error) {
    // Try primary source
    profile, err := s.primaryDB.GetProfile(playerID)
    if err == nil {
        return profile, nil
    }
    
    // Fallback 1: Cache (stale OK)
    if cached, ok := s.cache.Get(playerID); ok {
        log.Warn("Using stale cache", "player", playerID)
        return cached.(*Profile), nil
    }
    
    // Fallback 2: Replica DB
    profile, err = s.replicaDB.GetProfile(playerID)
    if err == nil {
        return profile, nil
    }
    
    // Fallback 3: Default profile
    log.Error("All sources failed, returning default", "player", playerID)
    return &Profile{
        ID:     playerID,
        Level:  1,
        Health: 100,
    }, nil
}
```

---

# BACKPRESSURE HANDLING

## 🟡 HIGH: Load Shedding

**Что:** Отбрасывай requests при перегрузке

```go
type LoadShedder struct {
    maxConcurrent int32
    current       atomic.Int32
}

func (ls *LoadShedder) Allow() (func(), bool) {
    current := ls.current.Add(1)
    
    if current > ls.maxConcurrent {
        ls.current.Add(-1)
        return nil, false // Reject
    }
    
    // Return release function
    release := func() {
        ls.current.Add(-1)
    }
    
    return release, true
}

func Handler(w http.ResponseWriter, r *http.Request) {
    release, allowed := loadShedder.Allow()
    if !allowed {
        http.Error(w, "Service overloaded", http.StatusServiceUnavailable)
        return
    }
    defer release()
    
    // Process request...
}
```

**Benefits:** Protect from cascading failure

---

# MEMORY LEAK PREVENTION

## 🟡 HIGH: Bounded Map Growth

**Что:** Предотврати unbounded map growth

```go
type BoundedMap struct {
    data     sync.Map
    keys     []string
    maxSize  int
    mu       sync.Mutex
}

func (bm *BoundedMap) Set(key string, value interface{}) {
    bm.mu.Lock()
    defer bm.mu.Unlock()
    
    // Check if already exists
    if _, ok := bm.data.Load(key); !ok {
        // Add to key list
        bm.keys = append(bm.keys, key)
        
        // Evict oldest if full
        if len(bm.keys) > bm.maxSize {
            oldest := bm.keys[0]
            bm.data.Delete(oldest)
            bm.keys = bm.keys[1:]
        }
    }
    
    bm.data.Store(key, value)
}
```

**Prevents:** Memory leak от неограниченного роста

---

## 🟡 HIGH: TTL-Based Cleanup

**Что:** Auto cleanup старых entries

```go
type TTLMap struct {
    data sync.Map
    ttl  time.Duration
}

type TTLEntry struct {
    Value     interface{}
    ExpiresAt time.Time
}

func (tm *TTLMap) Set(key string, value interface{}) {
    tm.data.Store(key, &TTLEntry{
        Value:     value,
        ExpiresAt: time.Now().Add(tm.ttl),
    })
}

func (tm *TTLMap) Get(key string) (interface{}, bool) {
    val, ok := tm.data.Load(key)
    if !ok {
        return nil, false
    }
    
    entry := val.(*TTLEntry)
    if time.Now().After(entry.ExpiresAt) {
        tm.data.Delete(key) // Expired
        return nil, false
    }
    
    return entry.Value, true
}

// Background cleanup
func (tm *TTLMap) StartCleanup() {
    ticker := time.NewTicker(1 * time.Minute)
    
    go func() {
        for range ticker.C {
            now := time.Now()
            tm.data.Range(func(key, value interface{}) bool {
                entry := value.(*TTLEntry)
                if now.After(entry.ExpiresAt) {
                    tm.data.Delete(key)
                }
                return true
            })
        }
    }()
}
```

---

# METRICS & OBSERVABILITY

## 🟡 HIGH: Custom Metrics для Game

**Что:** Game-specific метрики

```go
var (
    playersOnline = promauto.NewGauge(prometheus.GaugeOpts{
        Name: "game_players_online",
        Help: "Current players online",
    })
    
    matchDuration = promauto.NewHistogram(prometheus.HistogramOpts{
        Name:    "game_match_duration_seconds",
        Buckets: []float64{60, 300, 600, 1200, 1800, 3600}, // 1m-1h
    })
    
    shotsFired = promauto.NewCounterVec(prometheus.CounterOpts{
        Name: "game_shots_fired_total",
    }, []string{"weapon_type"})
    
    headshotRate = promauto.NewGaugeVec(prometheus.GaugeOpts{
        Name: "game_headshot_rate",
    }, []string{"player_id"})
)

func (s *GameServer) UpdateMetrics() {
    playersOnline.Set(float64(s.PlayerCount()))
    
    for _, player := range s.Players() {
        rate := float64(player.Headshots) / float64(player.TotalShots)
        headshotRate.WithLabelValues(player.ID).Set(rate)
    }
}
```

---

# 📊 Summary Part 5-6

**New techniques added: 20+**

### Database:
- Time-series partitioning
- Materialized views
- Covering indexes
- Partial indexes

### Cache:
- Pub/Sub invalidation
- Cache warming
- Negative caching
- TTL cleanup

### FPS-Specific:
- Server-side rewind (lag compensation)
- Dead reckoning
- Frustum culling
- Occluder culling

### Distributed:
- Zone sharding
- gRPC server-to-server
- Dynamic instances

### Resilience:
- Connection retry
- Circuit breakers (DB)
- Feature flags
- Load shedding
- Fallback strategies

### Physics:
- Broadphase collision

### Anti-Exploit:
- Economy monitoring
- Rate limiting per action

---

**All Parts:**
1. [Memory, Concurrency, DB](./01-memory-concurrency-db.md)
2. [Network](./02a-network-optimizations.md) + [Game](./02b-game-patterns.md)
3. [Profiling](./03a-profiling-testing.md) + [Tools](./03b-tools-summary.md)
4. [MMO Sessions](./04a-mmo-sessions-inventory.md) + [Persistence](./04b-persistence-matching.md) + [Anti-Cheat](./04c-matchmaking-anticheat.md)
5. [Advanced MMO](./05-advanced-mmo-techniques.md)
6. [Resilience & Compression](./06-resilience-compression.md) ⭐ NEW!

**Main:** [GO_BACKEND_PERFORMANCE_BIBLE.md](../GO_BACKEND_PERFORMANCE_BIBLE.md)

