# 📖 Go Performance Bible - Part 7B

**Redis Advanced & Database Comparison**

---

# REDIS ADVANCED

## 🔴 CRITICAL: Pipelining

```go
func (s *Service) UpdateMultiplePlayers(players []Player) error {
    pipe := s.redis.Pipeline()
    
    for _, player := range players {
        pipe.Set(ctx, "player:"+player.ID, marshal(player), TTL)
        pipe.ZAdd(ctx, "leaderboard", &redis.Z{
            Score:  float64(player.Rating),
            Member: player.ID,
        })
    }
    
    _, err := pipe.Exec(ctx)
    return err
}
```

**Gains:** Round-trips ↓99%  
**1000 players:** 1000 → 1 round-trip

---

## 🔴 CRITICAL: Lua Scripts

```go
var updateInventoryScript = redis.NewScript(`
    local key = KEYS[1]
    local item = ARGV[1]
    local qty = tonumber(ARGV[2])
    
    local inv = redis.call('HGET', key, 'inventory')
    local data = cjson.decode(inv)
    
    data[item] = (data[item] or 0) + qty
    
    redis.call('HSET', key, 'inventory', cjson.encode(data))
    return data[item]
`)

func (s *Service) AddItem(playerID, itemID string, qty int) error {
    _, err := updateInventoryScript.Run(ctx, s.redis,
        []string{"player:" + playerID}, itemID, qty).Result()
    return err
}
```

**Benefits:** Atomic, ↓90% round-trips, prevents duplication

---

## 🟡 HIGH: Redis Cluster

```go
func NewRedisCluster(addrs []string) *redis.ClusterClient {
    return redis.NewClusterClient(&redis.ClusterOptions{
        Addrs: addrs,
        PoolSize: 50,
        ReadTimeout: 100 * time.Millisecond,
    })
}
```

**Benefits:** 16k shards, millions ops/sec

---

## 🟡 HIGH: Redis Sentinel (HA)

```go
func NewRedisSentinel(master string, sentinels []string) *redis.Client {
    return redis.NewFailoverClient(&redis.FailoverOptions{
        MasterName:    master,
        SentinelAddrs: sentinels,
        PoolSize:      50,
    })
}
```

**Benefits:** Auto failover

---

## 🟢 MEDIUM: Redis Streams

```go
func (s *Service) PublishEvent(stream string, data map[string]interface{}) error {
    return s.redis.XAdd(ctx, &redis.XAddArgs{
        Stream: stream,
        Values: data,
    }).Err()
}

func (s *Service) ConsumeEvents(stream, consumer string) {
    for {
        streams, _ := s.redis.XReadGroup(ctx, &redis.XReadGroupArgs{
            Group:    "game-servers",
            Consumer: consumer,
            Streams:  []string{stream, ">"},
            Count:    100,
        }).Result()
        
        for _, msg := range streams[0].Messages {
            s.processEvent(msg)
            s.redis.XAck(ctx, stream, "game-servers", msg.ID)
        }
    }
}
```

---

## 🟢 MEDIUM: Bloom Filter

```go
import "github.com/bits-and-blooms/bloom/v3"

type BannedFilter struct {
    filter *bloom.BloomFilter
    redis  *redis.Client
}

func (bf *BannedFilter) IsBanned(playerID string) bool {
    // Quick check (may have false positives)
    if !bf.filter.TestString(playerID) {
        return false // Definitely NOT banned
    }
    
    // Confirm в Redis
    return bf.redis.SIsMember(ctx, "banned", playerID).Val()
}
```

**Gains:** Check ↓90% latency

---

## 🟡 HIGH: Memory Optimization

```redis
# redis.conf
maxmemory 8gb
maxmemory-policy allkeys-lru

# Compression
hash-max-ziplist-entries 512
hash-max-ziplist-value 64

# Persistence
save 900 1
save 300 10
save 60 10000

# Pure cache (no persistence):
# save ""
# appendonly no
```

---

## 🟢 MEDIUM: Keyspace Notifications

```go
type KeyspaceListener struct {
    pubsub *redis.PubSub
}

func (kl *KeyspaceListener) Start() {
    kl.pubsub = kl.redis.PSubscribe(ctx, "__keyevent@0__:expired")
    
    go func() {
        for msg := range kl.pubsub.Channel() {
            kl.handleSessionExpired(msg.Payload)
        }
    }()
}
```

**Use:** Session timeouts, buff expiration

---

## 🟢 MEDIUM: MULTI/EXEC Transactions

```go
func (s *Service) TransferGold(from, to string, amount int64) error {
    pipe := s.redis.TxPipeline()
    
    pipe.DecrBy(ctx, "gold:"+from, amount)
    pipe.IncrBy(ctx, "gold:"+to, amount)
    
    _, err := pipe.Exec(ctx)
    return err
}
```

**Prevents:** Partial updates, duplication

---

# DATABASE COMPARISON

## PostgreSQL vs Alternatives

| Use Case | PostgreSQL | TimescaleDB | ClickHouse |
|----------|------------|-------------|------------|
| **Players/Inventory** | ✅ Perfect | ⚪ Overkill | ❌ Wrong |
| **Time-Series** | 🟡 OK | ✅ Great | ✅ Best |
| **Analytics** | 🟡 Slow | 🟡 OK | ✅ Perfect |
| **Real-Time** | ✅ Great | ✅ Great | 🟡 OK |
| **Transactions** | ✅ ACID | ✅ ACID | ❌ No |

---

## Redis vs Alternatives

| Feature | Redis | ScyllaDB | Memcached |
|---------|-------|----------|-----------|
| **Cache** | ✅ Perfect | ⚪ Overkill | ✅ Simple |
| **Sessions** | ✅ Perfect | 🟡 OK | 🟡 OK |
| **Leaderboards** | ✅ Sorted Sets | 🟡 OK | ❌ No |
| **Pub/Sub** | ✅ Built-in | 🟡 OK | ❌ No |
| **>1M ops/sec** | 🟡 Cluster | ✅ Native | 🟡 OK |

---

# 💡 FINAL RECOMMENDATION

## ✅ PostgreSQL + Redis достаточно!

**Для MMORPG FPS хватит для:**
- 1M+ players
- 100k+ concurrent
- 50k+ req/sec
- Petabytes data (sharding)

**Добавь ТОЛЬКО если:**

### ClickHouse
**When:** >100M events/day, complex analytics

**Pros:** 100x aggregations, 10x compression  
**Cons:** No updates, eventual consistency

**Verdict:** 🟡 Add only if PostgreSQL analytics slow

---

### TimescaleDB
**When:** Много time-series, хочешь SQL

**Pros:** Проще партиционирования  
**Cons:** Еще одна зависимость

**Verdict:** ⚪ Nice, но НЕ обязательно

---

### ScyllaDB
**When:** Redis Cluster недостаточно (>1M ops/sec/node)

**Pros:** 10x throughput vs Redis  
**Cons:** Сложнее, другой API

**Verdict:** ⚪ Overkill для 99% игр

---

## ❌ НЕ НУЖНЫ:

| DB | Зачем? | Замена |
|----|--------|--------|
| **MongoDB** | Flexible | PostgreSQL JSONB |
| **Cassandra** | Distributed | Redis Cluster |
| **Neo4j** | Graphs | PostgreSQL recursive |
| **Elasticsearch** | Search | PostgreSQL FTS |
| **DynamoDB** | AWS | PostgreSQL + Redis |

---

# 📊 SCALE TARGETS

## PostgreSQL + Redis supports:

**Players:**
- Active: 1,000,000+
- Concurrent: 100,000+
- Peak: 200,000+

**Throughput:**
- Reads: 100,000+ req/sec
- Writes: 50,000+ req/sec
- Cache hits: 95%+

**Latency:**
- P50: <5ms
- P95: <20ms
- P99: <50ms

**Data:**
- PostgreSQL: Petabytes (sharding)
- Redis: Terabytes (cluster)

---

**Previous:** [Part 7A](./07a-postgresql-advanced.md)  
**Next:** [Summary & Checklist](./03b-tools-summary.md)  
**Main:** [GO_BACKEND_PERFORMANCE_BIBLE.md](../GO_BACKEND_PERFORMANCE_BIBLE.md)

