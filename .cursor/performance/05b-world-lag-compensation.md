# 📖 Go Performance Bible - Part 5B

**World Sharding, Lag Compensation & Load Balancing**

---

# LAG COMPENSATION (FPS!)

## 🔴 CRITICAL: Server-Side Rewind

```go
type WorldStateHistory struct {
    states []WorldSnapshot
    mu     sync.RWMutex
}

func (wsh *WorldStateHistory) Record(state WorldSnapshot) {
    wsh.mu.Lock()
    wsh.states = append(wsh.states, state)
    if len(wsh.states) > 100 { // Last 1-2 sec
        wsh.states = wsh.states[1:]
    }
    wsh.mu.Unlock()
}

func (wsh *WorldStateHistory) GetStateAt(t time.Time) *WorldSnapshot {
    wsh.mu.RLock()
    defer wsh.mu.RUnlock()
    
    for i := len(wsh.states) - 1; i >= 0; i-- {
        if wsh.states[i].Timestamp.Before(t) {
            return &wsh.states[i]
        }
    }
    return nil
}

// Validate shot с lag compensation
func (s *Server) ValidateShot(shooter, target *Player, shot *Shot) bool {
    // Rewind на ping игрока
    pastTime := time.Now().Add(-shooter.Ping / 2)
    pastState := s.history.GetStateAt(pastTime)
    pastPos := pastState.GetPlayerPosition(target.ID)
    
    return shot.HitPos.Distance(pastPos) < HitboxRadius
}
```

**Benefits:** Fair hits для high ping (150-200ms)

---

## 🟡 HIGH: Dead Reckoning

```go
type DeadReckoning struct {
    lastPos    Vector3
    lastVel    Vector3
    lastUpdate time.Time
    accel      Vector3
}

func (dr *DeadReckoning) Predict(now time.Time) Vector3 {
    dt := now.Sub(dr.lastUpdate).Seconds()
    
    // Physics: pos = pos0 + vel*t + 0.5*acc*t²
    return dr.lastPos.
        Add(dr.lastVel.Mul(dt)).
        Add(dr.accel.Mul(0.5 * dt * dt))
}
```

**Benefits:** Smooth при packet loss

---

# WORLD SHARDING

## 🔴 CRITICAL: Zone-Based Sharding

```go
type WorldShard struct {
    zones  map[ZoneID]*GameServer
    router *ZoneRouter
}

func (zr *ZoneRouter) GetServerForPosition(pos Vector3) string {
    zoneID := zr.GetZoneID(pos)
    return zr.zoneMap[zoneID]
}

// Seamless transfer
func (ws *WorldShard) TransferPlayer(player *Player, from, to ZoneID) error {
    fromServer := ws.zones[from]
    toServer := ws.zones[to]
    
    // Serialize state
    state := fromServer.SerializePlayer(player.ID)
    
    // Remove from old
    fromServer.RemovePlayer(player.ID)
    
    // Add to new
    return toServer.AddPlayer(player.ID, state)
}
```

**Benefits:** Horizontal scaling, isolation

---

## 🟡 HIGH: Dynamic Zone Scaling

```go
type ZoneScaler struct {
    zones     map[ZoneID]*Zone
    threshold int // Max players per zone
}

func (zs *ZoneScaler) CheckAndScale(zoneID ZoneID) {
    zone := zs.zones[zoneID]
    
    if zone.PlayerCount() > zs.threshold {
        // Split zone
        newZone := zs.createChildZone(zoneID)
        
        // Migrate half
        players := zone.GetPlayers()
        for i, p := range players {
            if i >= len(players)/2 {
                zs.migratePlayer(p, zoneID, newZone)
            }
        }
    }
}
```

---

# VISIBILITY CULLING

## 🔴 CRITICAL: Frustum Culling

```go
type VisibilityManager struct {
    players sync.Map
}

func (vm *VisibilityManager) GetVisiblePlayers(viewer *Player) []*Player {
    fov := viewer.GetFrustum()
    var visible []*Player
    
    vm.players.Range(func(key, value interface{}) bool {
        other := value.(*Player)
        
        if other.ID == viewer.ID {
            return true
        }
        
        // Distance first (cheap)
        if viewer.Position.Distance(other.Position) > 300 {
            return true
        }
        
        // FOV check
        if fov.Contains(other.Position) {
            visible = append(visible, other)
        }
        
        return true
    })
    
    return visible
}
```

**Gains:** Network ↓70-85%

---

## 🟡 HIGH: Occluder Culling

```go
func (vm *VisibilityManager) IsOccluded(from, to Vector3) bool {
    ray := Ray{Origin: from, Direction: to.Sub(from).Normalize()}
    
    for _, wall := range vm.staticWalls {
        if wall.Intersects(ray) {
            return true
        }
    }
    
    return false
}
```

**Gains:** Network ↓30-50%

---

# LOAD BALANCING

## 🔴 CRITICAL: Least-Connection

```go
type ServerPool struct {
    servers []*GameServer
}

type GameServer struct {
    address     string
    playerCount atomic.Int32
    lastHealth  time.Time
}

func (sp *ServerPool) GetBestServer() *GameServer {
    var best *GameServer
    minPlayers := int32(999999)
    
    for _, server := range sp.servers {
        // Skip unhealthy
        if time.Since(server.lastHealth) > 30*time.Second {
            continue
        }
        
        count := server.playerCount.Load()
        if count < minPlayers {
            minPlayers = count
            best = server
        }
    }
    
    return best
}
```

---

## 🟡 HIGH: Sticky Sessions

```go
type StickyRouter struct {
    playerToServer sync.Map
    pool           *ServerPool
}

func (sr *StickyRouter) Route(playerID uint64) *GameServer {
    // Check existing assignment
    if srvID, ok := sr.playerToServer.Load(playerID); ok {
        server := sr.pool.GetByID(srvID.(string))
        if server != nil && server.IsHealthy() {
            return server
        }
    }
    
    // Assign to best
    best := sr.pool.GetBestServer()
    sr.playerToServer.Store(playerID, best.ID)
    
    return best
}
```

---

# INSTANCE MANAGEMENT

## 🟡 HIGH: Dynamic Instances

```go
type InstanceManager struct {
    instances sync.Map
    pool      *WorkerPool
}

func (im *InstanceManager) CreateInstance(dungeonID string, party []Player) (*Instance, error) {
    instance := &Instance{
        ID:        generateID(),
        DungeonID: dungeonID,
        Players:   party,
        CreatedAt: time.Now(),
    }
    
    im.instances.Store(instance.ID, instance)
    
    // Start в worker pool
    im.pool.Submit(func() {
        instance.Run()
    })
    
    // Auto cleanup через 2h
    time.AfterFunc(2*time.Hour, func() {
        im.DestroyInstance(instance.ID)
    })
    
    return instance, nil
}
```

---

# CROSS-SERVER gRPC

## 🟡 HIGH: Server-to-Server

```go
import "google.golang.org/grpc"

type ServerClient struct {
    clients map[string]pb.GameServerClient
}

func (sc *ServerClient) TransferPlayer(playerID uint64, target string) error {
    client := sc.getClient(target)
    
    ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
    defer cancel()
    
    _, err := client.AcceptPlayer(ctx, &pb.PlayerTransferRequest{
        PlayerId: playerID,
        State:    serializeState(playerID),
    })
    
    return err
}
```

**Gains:** <5ms latency vs 20-30ms HTTP

---

**Next:** [Part 6 - Resilience](./06-resilience-compression.md)  
**Previous:** [Part 4C](./04c-matchmaking-anticheat.md)

