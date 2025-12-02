# 📖 Go Performance Bible - Part 4A

**MMO Patterns: Sessions, Inventory, Guilds**

---

# SESSION MANAGEMENT

## 🔴 CRITICAL: Redis Session Store

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
    proto.Unmarshal(data, &session)
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

```go
type InventoryCache struct {
    items sync.Map // playerID -> *Inventory
    ttl   time.Duration
}

func (ic *InventoryCache) Get(playerID string) (*Inventory, error) {
    // L1: In-memory
    if cached, ok := ic.items.Load(playerID); ok {
        inv := cached.(*CachedInventory)
        if time.Since(inv.LoadedAt) < ic.ttl {
            return inv.Inventory, nil
        }
    }
    
    // L2: DB + cache
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

```go
type InventoryUpdate struct {
    PlayerID uint64
    Added    []Item   `json:",omitempty"`
    Removed  []uint64 `json:",omitempty"`
    Updated  []Item   `json:",omitempty"`
}

func (inv *Inventory) GetDiff(prev *Inventory) *InventoryUpdate {
    update := &InventoryUpdate{PlayerID: inv.PlayerID}
    
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

**Gains:** Bandwidth ↓70-90%

---

# GUILD/CLAN OPERATIONS

## 🔴 CRITICAL: Guild Action Batching

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
    tx, _ := gab.db.Begin()
    for _, action := range actions {
        action.Execute(tx)
    }
    tx.Commit()
}
```

**Gains:** DB transactions ↓95%

---

## 🟡 HIGH: Guild Member Cache

```go
type GuildMemberCache struct {
    members sync.Map // guildID -> []PlayerID
    ttl     time.Duration
}

func (gc *GuildMemberCache) OnMemberJoin(guildID, playerID string) {
    gc.members.Delete(guildID) // Invalidate
    gc.notifyMembers(guildID, "member_joined", playerID)
}
```

---

# TRADING/AUCTION

## 🔴 CRITICAL: Optimistic Locking

```go
type Item struct {
    ID      uint64
    OwnerID uint64
    Version int64 // Optimistic lock
}

func (s *TradingService) TransferItem(from, to, itemID uint64) error {
    for retries := 0; retries < 3; retries++ {
        item, _ := s.repo.GetItem(itemID)
        
        if item.OwnerID != from {
            return ErrNotOwner
        }
        
        // Update with version check
        updated, _ := s.repo.UpdateItemOwner(itemID, to, item.Version)
        if updated {
            return nil
        }
        
        time.Sleep(10 * time.Millisecond)
    }
    
    return ErrConcurrentModification
}
```

**Prevents:** Deadlocks, item duplication

---

## 🟡 HIGH: Transaction Queue

```go
type TradeQueue struct {
    queue      chan *Trade
    workerPool *WorkerPool
}

func (tq *TradeQueue) ProcessTrades() {
    for trade := range tq.queue {
        tq.workerPool.Submit(func() {
            tq.processTrade(trade)
        })
    }
}
```

---

**Next:** [Part 4B - Persistence & Matching](./04b-persistence-matching.md)  
**Previous:** [Part 3B](./03b-tools-summary.md)

