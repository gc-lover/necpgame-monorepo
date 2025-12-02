# 📖 Go Performance Bible - Part 4C

**MMO Patterns: Matchmaking & Anti-Cheat**

---

# MATCHMAKING OPTIMIZATION

## 🔴 CRITICAL: Skill-Based Buckets

```go
type MatchmakingQueue struct {
    buckets []*SkillBucket // 0-1000, 1000-2000, etc.
}

type SkillBucket struct {
    minSkill int
    maxSkill int
    players  []*Player
    mu       sync.Mutex
}

func (mq *MatchmakingQueue) AddPlayer(player *Player) {
    bucketID := player.Skill / 1000
    bucket := mq.buckets[bucketID]
    
    bucket.mu.Lock()
    bucket.players = append(bucket.players, player)
    bucket.mu.Unlock()
    
    go mq.tryMatch(bucketID)
}

func (mq *MatchmakingQueue) tryMatch(bucketID int) {
    bucket := mq.buckets[bucketID]
    
    bucket.mu.Lock()
    defer bucket.mu.Unlock()
    
    if len(bucket.players) >= 10 {
        match := bucket.players[:10]
        bucket.players = bucket.players[10:]
        
        go mq.createMatch(match)
    }
}
```

**Gains:** Matching O(1) vs O(N), <10ms vs seconds

---

## 🟡 HIGH: Timeout Expansion

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
            
            for i := baseBucket - searchRadius; i <= baseBucket + searchRadius; i++ {
                if match := mq.tryMatchInBucket(i, player); match != nil {
                    return match, nil
                }
            }
            
        case <-time.After(60 * time.Second):
            return nil, ErrTimeout
        }
    }
}
```

**Balance:** Skill fairness vs queue time

---

# ANTI-CHEAT INTEGRATION

## 🟡 HIGH: Server-Side Validation

```go
type ActionValidator struct {
    lastActions sync.Map // playerID -> *LastAction
}

func (av *ActionValidator) ValidateShot(playerID uint64, shot *ShotAction) error {
    // Rate check (max 10 shots/sec)
    if last, ok := av.lastActions.Load(playerID); ok {
        lastAction := last.(*LastAction)
        if time.Since(lastAction.Timestamp) < 100*time.Millisecond {
            return ErrTooFast // Potential aimbot
        }
    }
    
    // Distance check
    if shot.Distance > 1000 {
        return ErrImpossibleShot
    }
    
    // Line of sight
    if !av.hasLineOfSight(shot.From, shot.To) {
        return ErrWallhack
    }
    
    av.lastActions.Store(playerID, &LastAction{Timestamp: time.Now()})
    return nil
}
```

**Prevents:** Aimbots, wallhacks, speed hacks

---

## 🟡 HIGH: Anomaly Detection

```go
type AnomalyDetector struct {
    playerStats sync.Map // playerID -> *Stats
}

type PlayerStats struct {
    Headshots     atomic.Int64
    TotalShots    atomic.Int64
    AvgReaction   atomic.Int64 // Microseconds
    Flags         atomic.Int32
}

func (ad *AnomalyDetector) RecordShot(playerID uint64, headshot bool, reactionTime time.Duration) {
    stats := ad.getOrCreateStats(playerID)
    
    stats.TotalShots.Add(1)
    if headshot {
        stats.Headshots.Add(1)
    }
    stats.AvgReaction.Store(int64(reactionTime.Microseconds()))
    
    // Check anomalies
    headshotRate := float64(stats.Headshots.Load()) / float64(stats.TotalShots.Load())
    if headshotRate > 0.7 { // 70% headshots = suspicious
        stats.Flags.Add(1)
        
        if stats.Flags.Load() > 10 {
            ad.reportSuspicious(playerID)
        }
    }
}
```

---

# 📊 MMO-Specific Gains

| Feature | Without | With | Gain |
|---------|---------|------|------|
| **Session lookup** | 50ms | 1-2ms | -96% |
| **Inventory load** | 100ms | 5-10ms | -90% |
| **Guild ops** | 500ms | 10-20ms | -96% |
| **Leaderboard** | O(N) slow | O(log N) | 1000x+ |
| **Matchmaking** | O(N²) | O(1) | ∞ scale |
| **Trading** | Deadlocks | Optimistic | Stable |
| **Config reload** | Restart | Live | 0 downtime |
| **Persistence** | Every action | Batch 5s | -95% writes |

---

# 🎯 MMO Checklist

**ОБЯЗАТЕЛЬНО для MMO игр:**

- [ ] Redis session store (stateless servers)
- [ ] Inventory caching + diff updates
- [ ] Guild action batching
- [ ] Leaderboard через Redis sorted sets
- [ ] Player sharding (>1M players)
- [ ] CQRS (read/write separation)
- [ ] Event sourcing (audit trail)
- [ ] Write-behind persistence
- [ ] Matchmaking buckets (O(1) matching)
- [ ] Server-side validation (anti-cheat)

---

# 💡 Key Insights

**Для MMO критично:**

1. **Stateless Services** → Horizontal scaling
2. **Multi-Level Caching** → L1 (memory) + L2 (Redis) + L3 (DB)
3. **Batching Everywhere** → Guild ops, trades, persistence
4. **Sharding** → Players, leaderboards, по регионам
5. **CQRS** → Read/write models разделены
6. **Event Sourcing** → Audit trail + replay
7. **Optimistic Locking** → No deadlocks в trading
8. **Hot Reload** → Balance без restart

---

**All Parts:**
1. [Memory, Concurrency, DB](./01-memory-concurrency-db.md)
2. [Network](./02a-network-optimizations.md) + [Game](./02b-game-patterns.md)
3. [Profiling](./03a-profiling-testing.md) + [Tools](./03b-tools-summary.md)
4. [MMO Sessions/Inventory](./04a-mmo-sessions-inventory.md) + [Persistence/Matching](./04b-persistence-matching.md) + [Anti-Cheat](./04c-matchmaking-anticheat.md)

**Main:** [GO_BACKEND_PERFORMANCE_BIBLE.md](../GO_BACKEND_PERFORMANCE_BIBLE.md)

