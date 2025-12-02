# 🎮 Backend Game Server Templates

**Шаблоны для real-time game servers**

## 🎮 game_server.go

```go
// Issue: #123
package server

import (
    "context"
    "runtime"
    "sync"
    "sync/atomic"
    "time"
)

// ОПТИМИЗАЦИЯ: Правильный struct alignment
type Player struct {
    ID       uint64      // 8 bytes
    Position Vector3     // 12 bytes (3x float32)
    Rotation Quaternion  // 16 bytes (4x float32)
    Health   int32       // 4 bytes
    Level    uint16      // 2 bytes
    IsActive bool        // 1 byte
    _        byte        // 1 byte padding (explicit)
} // Total: 44 bytes (оптимально)

type GameServer struct {
    playerCount atomic.Int32
    tickRate    time.Duration
    
    // ОПТИМИЗАЦИЯ: Spatial grid
    spatialGrid *SpatialGrid
    
    // ОПТИМИЗАЦИЯ: Worker pool
    workerPool *WorkerPool
    
    // ОПТИМИЗАЦИЯ: Object pools
    updatePool sync.Pool
    packetPool sync.Pool
}

func NewGameServer() *GameServer {
    // ОПТИМИЗАЦИЯ: GC tuning для game server
    runtime.GOMAXPROCS(runtime.NumCPU() - 1)
    
    return &GameServer{
        tickRate:    16 * time.Millisecond, // 60 Hz default
        spatialGrid: NewSpatialGrid(100.0), // 100m cells
        workerPool:  NewWorkerPool(1000),   // Max 1000 workers
        
        updatePool: sync.Pool{
            New: func() interface{} {
                return &PlayerUpdate{}
            },
        },
        
        packetPool: sync.Pool{
            New: func() interface{} {
                buf := make([]byte, 1500) // MTU
                return &buf
            },
        },
    }
}

// ОПТИМИЗАЦИЯ: Adaptive tick rate
func (s *GameServer) AdaptTickRate() {
    count := s.playerCount.Load()
    
    switch {
    case count < 50:
        s.tickRate = 8 * time.Millisecond   // 128 Hz
    case count < 200:
        s.tickRate = 16 * time.Millisecond  // 60 Hz
    case count < 500:
        s.tickRate = 33 * time.Millisecond  // 30 Hz
    default:
        s.tickRate = 50 * time.Millisecond  // 20 Hz
    }
}

// Game loop
func (s *GameServer) Run(ctx context.Context) {
    ticker := time.NewTicker(s.tickRate)
    defer ticker.Stop()
    
    for {
        select {
        case <-ctx.Done():
            return
        case <-ticker.C:
            s.Update()
            s.AdaptTickRate()
            ticker.Reset(s.tickRate)
        }
    }
}

// ОПТИМИЗАЦИЯ: Spatial partitioning для broadcast
func (s *GameServer) BroadcastUpdate(player *Player, update []byte) {
    // Только игрокам в радиусе
    nearby := s.spatialGrid.GetNearbyPlayers(player.Position, 100.0)
    
    // ОПТИМИЗАЦИЯ: Worker pool для parallel send
    for _, p := range nearby {
        p := p // Capture
        s.workerPool.Submit(func() {
            p.SendUpdate(update)
        })
    }
}
```

## 🌐 spatial_grid.go

```go
// Issue: #123
package server

import "sync"

type CellID struct {
    X, Y int32
}

type Cell struct {
    mu      sync.RWMutex
    players map[uint64]*Player
}

type SpatialGrid struct {
    cellSize float32
    cells    sync.Map // Lock-free map
}

func NewSpatialGrid(cellSize float32) *SpatialGrid {
    return &SpatialGrid{
        cellSize: cellSize,
    }
}

func (g *SpatialGrid) GetCell(pos Vector3) CellID {
    return CellID{
        X: int32(pos.X / g.cellSize),
        Y: int32(pos.Z / g.cellSize), // XZ plane
    }
}

func (g *SpatialGrid) AddPlayer(player *Player) {
    cellID := g.GetCell(player.Position)
    
    cellInterface, _ := g.cells.LoadOrStore(cellID, &Cell{
        players: make(map[uint64]*Player),
    })
    cell := cellInterface.(*Cell)
    
    cell.mu.Lock()
    cell.players[player.ID] = player
    cell.mu.Unlock()
}

func (g *SpatialGrid) RemovePlayer(player *Player) {
    cellID := g.GetCell(player.Position)
    
    if cellInterface, ok := g.cells.Load(cellID); ok {
        cell := cellInterface.(*Cell)
        
        cell.mu.Lock()
        delete(cell.players, player.ID)
        cell.mu.Unlock()
    }
}

// ОПТИМИЗАЦИЯ: Получить игроков только в радиусе
func (g *SpatialGrid) GetNearbyPlayers(pos Vector3, radius float32) []*Player {
    cellID := g.GetCell(pos)
    cellRadius := int32(radius / g.cellSize)
    
    // ОПТИМИЗАЦИЯ: Preallocation (примерно)
    nearby := make([]*Player, 0, 64)
    
    // Check surrounding cells
    for x := cellID.X - cellRadius; x <= cellID.X + cellRadius; x++ {
        for y := cellID.Y - cellRadius; y <= cellID.Y + cellRadius; y++ {
            if cellInterface, ok := g.cells.Load(CellID{X: x, Y: y}); ok {
                cell := cellInterface.(*Cell)
                
                cell.mu.RLock()
                for _, player := range cell.players {
                    // Check exact distance
                    distSq := pos.DistanceSquared(player.Position)
                    if distSq <= radius*radius {
                        nearby = append(nearby, player)
                    }
                }
                cell.mu.RUnlock()
            }
        }
    }
    
    return nearby
}
```

## 📡 udp_server.go

```go
// Issue: #123
package server

import (
    "net"
    "runtime"
    "sync"
)

// ОПТИМИЗАЦИЯ: Buffer pool для UDP packets
var udpBufferPool = sync.Pool{
    New: func() interface{} {
        buf := make([]byte, 1500) // MTU size
        return &buf
    },
}

type UDPServer struct {
    conn   *net.UDPConn
    router *PacketRouter
}

func NewUDPServer(addr string) (*UDPServer, error) {
    udpAddr, err := net.ResolveUDPAddr("udp", addr)
    if err != nil {
        return nil, err
    }
    
    conn, err := net.ListenUDP("udp", udpAddr)
    if err != nil {
        return nil, err
    }
    
    // ОПТИМИЗАЦИЯ: Set buffer sizes
    conn.SetReadBuffer(4 * 1024 * 1024)  // 4MB
    conn.SetWriteBuffer(4 * 1024 * 1024) // 4MB
    
    return &UDPServer{
        conn:   conn,
        router: NewPacketRouter(),
    }, nil
}

func (s *UDPServer) Start() {
    // ОПТИМИЗАЦИЯ: Multiple readers для throughput
    for i := 0; i < runtime.NumCPU(); i++ {
        go s.readLoop()
    }
}

func (s *UDPServer) readLoop() {
    for {
        // ОПТИМИЗАЦИЯ: Get buffer from pool
        bufPtr := udpBufferPool.Get().(*[]byte)
        buf := *bufPtr
        
        n, addr, err := s.conn.ReadFromUDP(buf)
        if err != nil {
            udpBufferPool.Put(bufPtr)
            continue
        }
        
        // Process packet (don't block!)
        go func(data []byte, addr *net.UDPAddr) {
            defer udpBufferPool.Put(bufPtr)
            s.router.Route(data[:n], addr)
        }(buf, addr)
    }
}

// ОПТИМИЗАЦИЯ: Batch send
func (s *UDPServer) SendBatch(updates []PlayerUpdate, addrs []*net.UDPAddr) {
    batch := make([]byte, 0, 64*1024) // 64KB buffer
    
    for i, update := range updates {
        // Serialize update
        data := update.Marshal()
        batch = append(batch, data...)
        
        // Flush before MTU or end
        if len(batch) > 60000 || i == len(updates)-1 {
            s.conn.WriteToUDP(batch, addrs[i])
            batch = batch[:0]
        }
    }
}
```

## См. также

- `.cursor/templates/backend-api-templates.md` - API handlers/service/repository
- `.cursor/templates/backend-utils-templates.md` - utilities и tests

