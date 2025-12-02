# 📖 Go Performance Bible - Part 4

**MMO-Specific Patterns & Optimizations**

**Специфичные паттерны для MMOFPS RPG**

---

# SESSION MANAGEMENT

## 🔴 CRITICAL: Redis Session Store

**Что:** Централизованные сессии для horizontal scaling

```go
import "github.com/go-redis/redis/v8"

type SessionStore struct {
    redis *redis.Client
}

func (s *SessionStore) Get(sessionID string) (*Session, error) {
    data, err := s.redis.Get(ctx, "session:"+sessionID).Bytes()
    if err != nil {
        return nil, err
    }
    
    var session Session
    proto.Unmarshal(data, &session) // Protobuf для speed
    return &session, nil
}

func (s *SessionStore) Set(session *Session) error {
    data, _ := proto.Marshal(session)
    return s.redis.Set(ctx, "session:"+session.ID, data, 24*time.Hour).Err()
}
```

**Gains:** Stateless servers, horizontal scaling

---

## 🟡 HIGH: Session Pooling

**Что:** Переиспользование session objects

```go
var sessionPool = sync.Pool{
    New: func() interface{} {
        return &Session{
            Inventory: make(map[string]*Item, 100),
            Buffs:     make([]*Buff, 0, 10),
        }
    },
}

func (s *SessionStore) GetSession(id string) *Session {
    session := sessionPool.Get().(*Session)
    s.loadFromRedis(session, id)
    return session
}

func (s *SessionStore) ReleaseSession(session *Session) {
    session.Reset()
    sessionPool.Put(session)
}
```

---

# INVENTORY OPTIMIZATION

## 🔴 CRITICAL: Inventory Caching

**Что:** Кеш inventory в памяти с lazy loading

```go
type InventoryCache struct {
    items sync.Map // playerID -> *Inventory
    ttl   time.Duration
}

func (ic *InventoryCache) Get(playerID string) (*Inventory, error) {
    // L1: In-memory cache
    if cached, ok := ic.items.Load(playerID); ok {
        inv := cached.(*CachedInventory)
        if time.Since(inv.LoadedAt) < ic.ttl {
            return inv.Inventory, nil
        }
    }
    
    // L2: Load from DB + cache
    inv, err := ic.db.LoadInventory(playerID)
    if err == nil {
        ic.items.Store(playerID, &CachedInventory{
            Inventory: inv,
            LoadedAt:  time.Now(),
        })
    }
    return inv, err
}

// Batch invalidation
func (ic *InventoryCache) InvalidateMultiple(playerIDs []string) {
    for _, id := range playerIDs {
        ic.items.Delete(id)
    }
}
```

**Gains:** DB queries ↓95%, Latency ↓80%

---

## 🟡 HIGH: Inventory Diff Updates

**Что:** Отправляй только изменения inventory

```go
type InventoryUpdate struct {
    PlayerID   uint64
    Added      []Item  `json:",omitempty"`
    Removed    []uint64 `json:",omitempty"` // Item IDs
    Updated    []Item  `json:",omitempty"`
}

func (inv *Inventory) GetDiff(prev *Inventory) *InventoryUpdate {
    update := &InventoryUpdate{PlayerID: inv.PlayerID}
    
    // Compare and build diff
    for id, item := range inv.Items {
        if prevItem, ok := prev.Items[id]; !ok {
            update.Added = append(update.Added, item)
        } else if !item.Equals(prevItem) {
            update.Updated = append(update.Updated, item)
        }
    }
    
    for id := range prev.Items {
        if _, ok := inv.Items[id]; !ok {
            update.Removed = append(update.Removed, id)
        }
    }
    
    return update
}
```

**Gains:** Bandwidth ↓70-90% (только изменения)

---

# GUILD/CLAN OPERATIONS

## 🔴 CRITICAL: Guild Action Batching

**Что:** Batch guild operations для performance

```go
type GuildActionBatcher struct {
    actions chan GuildAction
    batch   []GuildAction
    mu      sync.Mutex
}

func (gab *GuildActionBatcher) Start() {
    ticker := time.NewTicker(100 * time.Millisecond)
    
    for {
        select {
        case action := <-gab.actions:
            gab.mu.Lock()
            gab.batch = append(gab.batch, action)
            gab.mu.Unlock()
            
        case <-ticker.C:
            gab.mu.Lock()
            if len(gab.batch) > 0 {
                gab.processBatch(gab.batch)
                gab.batch = gab.batch[:0]
            }
            gab.mu.Unlock()
        }
    }
}

func (gab *GuildActionBatcher) processBatch(actions []GuildAction) {
    // 1 DB transaction для всех actions
    tx, _ := gab.db.Begin()
    for _, action := range actions {
        action.Execute(tx)
    }
    tx.Commit()
}
```

**Gains:** DB transactions ↓95% (100 actions → 1 tx)

---

## 🟡 HIGH: Guild Member Cache

**Что:** Кеш списка членов гильдии

```go
type GuildMemberCache struct {
    members sync.Map // guildID -> []PlayerID
    ttl     time.Duration
}

// При изменениях - invalidate
func (gc *GuildMemberCache) OnMemberJoin(guildID, playerID string) {
    gc.members.Delete(guildID) // Invalidate
    gc.notifyMembers(guildID, "member_joined", playerID)
}
```

---

# LEADERBOARD OPTIMIZATION

## 🔴 CRITICAL: Redis Sorted Sets

**Что:** Real-time leaderboards через Redis

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
    // O(log N + M) где M = результатов
    members, err := ls.redis.ZRevRangeWithScores(ctx, "leaderboard:global", 0, int64(n-1)).Result()
    // ...
    return players, nil
}

// Rank для конкретного игрока: O(log N)
func (ls *LeaderboardService) GetRank(playerID string) (int64, error) {
    return ls.redis.ZRevRank(ctx, "leaderboard:global", playerID).Result()
}
```

**Gains:** 
- Query time: O(log N) vs O(N) в SQL
- Real-time updates
- Millions of players supported

---

## 🟡 HIGH: Leaderboard Sharding

**Что:** Разбей на региональные leaderboards

```go
type ShardedLeaderboard struct {
    shards map[string]*redis.Client // region -> redis
}

func (sl *ShardedLeaderboard) UpdateScore(region, playerID string, score int64) error {
    shard := sl.shards[region]
    return shard.ZAdd(ctx, "leaderboard:"+region, &redis.Z{
        Score:  float64(score),
        Member: playerID,
    }).Err()
}

// Global leaderboard: merge top-K from each shard
func (sl *ShardedLeaderboard) GetGlobalTopN(n int) ([]Player, error) {
    var allTopPlayers []Player
    
    // Parallel fetch from all shards
    g, ctx := errgroup.WithContext(ctx)
    
    for region, shard := range sl.shards {
        region, shard := region, shard
        g.Go(func() error {
            topN, _ := shard.ZRevRangeWithScores(ctx, "leaderboard:"+region, 0, int64(n-1)).Result()
            // Append to allTopPlayers...
            return nil
        })
    }
    
    g.Wait()
    
    // Merge and sort top N globally
    sort.Slice(allTopPlayers, func(i, j int) bool {
        return allTopPlayers[i].Score > allTopPlayers[j].Score
    })
    
    return allTopPlayers[:n], nil
}
```

**Gains:** Scale to billions of players

---

# TRADING/AUCTION PATTERNS

## 🔴 CRITICAL: Optimistic Locking

**Что:** Избегай deadlocks в trading

```go
type Item struct {
    ID      uint64
    OwnerID uint64
    Version int64 // Optimistic lock version
}

func (s *TradingService) TransferItem(fromPlayer, toPlayer, itemID uint64) error {
    for retries := 0; retries < 3; retries++ {
        // Read current version
        item, err := s.repo.GetItem(itemID)
        if err != nil {
            return err
        }
        
        if item.OwnerID != fromPlayer {
            return ErrNotOwner
        }
        
        // Update with version check (optimistic lock)
        updated, err := s.repo.UpdateItemOwner(itemID, toPlayer, item.Version)
        if err != nil {
            return err
        }
        
        if updated {
            return nil // Success
        }
        
        // Version conflict - retry
        time.Sleep(10 * time.Millisecond)
    }
    
    return ErrConcurrentModification
}
```

**Prevents:** Deadlocks, item duplication

---

## 🟡 HIGH: Transaction Queue

**Что:** Очередь для trading transactions

```go
type TradeQueue struct {
    queue     chan *Trade
    batcher   *Batcher
    workerPool *WorkerPool
}

func (tq *TradeQueue) ProcessTrades() {
    for trade := range tq.queue {
        tq.workerPool.Submit(func() {
            tq.processTrade(trade)
        })
    }
}

// Batch commit к DB
func (tq *TradeQueue) processTrade(trade *Trade) error {
    return tq.batcher.Add(func(tx *sql.Tx) error {
        return trade.Execute(tx)
    })
}
```

---

# SHARDING STRATEGIES

## 🔴 CRITICAL: Player Sharding

**Что:** Разбивка игроков по шардам

```go
type PlayerShard struct {
    shards []*sql.DB // 10 shards
}

func (ps *PlayerShard) GetShard(playerID uint64) *sql.DB {
    shardID := playerID % uint64(len(ps.shards))
    return ps.shards[shardID]
}

func (ps *PlayerShard) GetPlayer(playerID uint64) (*Player, error) {
    shard := ps.GetShard(playerID)
    return shard.QueryRow("SELECT * FROM players WHERE id = $1", playerID)
