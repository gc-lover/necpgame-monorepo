# 🏗️ Backend API Templates

**Шаблоны для CRUD API и REST endpoints**

## 📦 handlers.go

```go
// Issue: #123
package server

import (
    "bytes"
    "context"
    "encoding/json"
    "net/http"
    "sync"
    "time"
    
    "github.com/your-org/necpgame/services/{service}-go/pkg/api"
)

// Response pool (ОПТИМИЗАЦИЯ: Memory pooling)
var responsePool = sync.Pool{
    New: func() interface{} {
        return &bytes.Buffer{}
    },
}

type Handlers struct {
    service Service
}

func NewHandlers(service Service) *Handlers {
    return &Handlers{service: service}
}

// Реализация api.ServerInterface
func (h *Handlers) ListPlayers(w http.ResponseWriter, r *http.Request, params api.ListPlayersParams) {
    // ОПТИМИЗАЦИЯ: Context timeout
    ctx, cancel := context.WithTimeout(r.Context(), 100*time.Millisecond)
    defer cancel()
    
    // ОПТИМИЗАЦИЯ: Get buffer from pool
    buf := responsePool.Get().(*bytes.Buffer)
    defer func() {
        buf.Reset()
        responsePool.Put(buf)
    }()
    
    // Business logic
    players, err := h.service.ListPlayers(ctx, params)
    if err != nil {
        respondError(w, http.StatusInternalServerError, err.Error())
        return
    }
    
    // ОПТИМИЗАЦИЯ: Encode to pooled buffer
    encoder := json.NewEncoder(buf)
    if err := encoder.Encode(players); err != nil {
        respondError(w, http.StatusInternalServerError, err.Error())
        return
    }
    
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write(buf.Bytes())
}

// Helper functions
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
    buf := responsePool.Get().(*bytes.Buffer)
    defer func() {
        buf.Reset()
        responsePool.Put(buf)
    }()
    
    encoder := json.NewEncoder(buf)
    encoder.Encode(data)
    
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(status)
    w.Write(buf.Bytes())
}

func respondError(w http.ResponseWriter, status int, message string) {
    respondJSON(w, status, api.Error{
        Code:    status,
        Message: message,
    })
}
```

## 🔧 service.go

```go
// Issue: #123
package server

import (
    "context"
    "sync/atomic"
    "time"
    
    "github.com/your-org/necpgame/services/{service}-go/pkg/api"
)

type Service interface {
    ListPlayers(ctx context.Context, params api.ListPlayersParams) ([]api.Player, error)
    GetPlayer(ctx context.Context, playerID string) (*api.Player, error)
    UpdatePlayer(ctx context.Context, playerID string, update api.PlayerUpdate) error
}

type serviceImpl struct {
    repo    Repository
    cache   *Cache
    metrics *Metrics
}

// Metrics (ОПТИМИЗАЦИЯ: Lock-free counters)
type Metrics struct {
    requestCount atomic.Int64
    errorCount   atomic.Int64
    cacheHits    atomic.Int64
    cacheMisses  atomic.Int64
}

func NewService(repo Repository) Service {
    return &serviceImpl{
        repo:    repo,
        cache:   NewCache(5 * time.Minute),
        metrics: &Metrics{},
    }
}

func (s *serviceImpl) ListPlayers(ctx context.Context, params api.ListPlayersParams) ([]api.Player, error) {
    s.metrics.requestCount.Add(1)
    
    // ОПТИМИЗАЦИЯ: Context deadline check
    if ctx.Err() != nil {
        return nil, ctx.Err()
    }
    
    // ОПТИМИЗАЦИЯ: Batch DB query
    players, err := s.repo.ListPlayersBatch(ctx, params)
    if err != nil {
        s.metrics.errorCount.Add(1)
        return nil, err
    }
    
    return players, nil
}

func (s *serviceImpl) GetPlayer(ctx context.Context, playerID string) (*api.Player, error) {
    s.metrics.requestCount.Add(1)
    
    // ОПТИМИЗАЦИЯ: Cache check
    if cached, ok := s.cache.Get(playerID); ok {
        s.metrics.cacheHits.Add(1)
        return cached.(*api.Player), nil
    }
    s.metrics.cacheMisses.Add(1)
    
    // Fetch from DB
    player, err := s.repo.GetPlayer(ctx, playerID)
    if err != nil {
        s.metrics.errorCount.Add(1)
        return nil, err
    }
    
    // Store in cache
    s.cache.Set(playerID, player)
    
    return player, nil
}
```

## 🗄️ repository.go

```go
// Issue: #123
package server

import (
    "context"
    "database/sql"
    "time"
    
    "github.com/lib/pq"
    "github.com/your-org/necpgame/services/{service}-go/pkg/api"
)

type Repository interface {
    ListPlayersBatch(ctx context.Context, params api.ListPlayersParams) ([]api.Player, error)
    GetPlayer(ctx context.Context, playerID string) (*api.Player, error)
    GetPlayersBatch(ctx context.Context, playerIDs []string) ([]api.Player, error)
}

type postgresRepo struct {
    db *sql.DB
}

func NewRepository(db *sql.DB) Repository {
    // ОПТИМИЗАЦИЯ: Connection pool settings
    db.SetMaxOpenConns(25)
    db.SetMaxIdleConns(25)
    db.SetConnMaxLifetime(5 * time.Minute)
    db.SetConnMaxIdleTime(10 * time.Minute)
    
    return &postgresRepo{db: db}
}

func (r *postgresRepo) ListPlayersBatch(ctx context.Context, params api.ListPlayersParams) ([]api.Player, error) {
    // ОПТИМИЗАЦИЯ: Preallocation
    players := make([]api.Player, 0, *params.Limit)
    
    query := `SELECT id, name, level, health FROM players LIMIT $1 OFFSET $2`
    rows, err := r.db.QueryContext(ctx, query, *params.Limit, *params.Offset)
    if err != nil {
        return nil, err
    }
    defer rows.Close()
    
    for rows.Next() {
        var player api.Player
        if err := rows.Scan(&player.Id, &player.Name, &player.Level, &player.Health); err != nil {
            return nil, err
        }
        players = append(players, player)
    }
    
    return players, rows.Err()
}

// ОПТИМИЗАЦИЯ: Batch query вместо N queries
func (r *postgresRepo) GetPlayersBatch(ctx context.Context, playerIDs []string) ([]api.Player, error) {
    if len(playerIDs) == 0 {
        return []api.Player{}, nil
    }
    
    // ОПТИМИЗАЦИЯ: Preallocation
    players := make([]api.Player, 0, len(playerIDs))
    
    // Single query with IN clause
    query := `SELECT id, name, level, health FROM players WHERE id = ANY($1)`
    rows, err := r.db.QueryContext(ctx, query, pq.Array(playerIDs))
    if err != nil {
        return nil, err
    }
    defer rows.Close()
    
    for rows.Next() {
        var player api.Player
        if err := rows.Scan(&player.Id, &player.Name, &player.Level, &player.Health); err != nil {
            return nil, err
        }
        players = append(players, player)
    }
    
    return players, rows.Err()
}
```

## 🔧 Использование

**Backend Agent должен:**

1. Копировать структуру из шаблона
2. Заменить `{service}` на имя сервиса
3. Адаптировать типы под OpenAPI spec
4. Добавить бизнес-логику с сохранением оптимизаций

## 🆕 Новые техники (2025)

**MMO Patterns:**
- Redis session store: `.cursor/performance/04a-mmo-sessions-inventory.md`
- Inventory caching: `.cursor/performance/04a-mmo-sessions-inventory.md`
- Materialized views: `.cursor/performance/05a-database-cache-advanced.md`

**Resilience:**
- Circuit breaker: `.cursor/performance/06-resilience-compression.md`
- Load shedding: `.cursor/performance/06-resilience-compression.md`

## См. также

- `.cursor/templates/backend-game-templates.md` - game servers
- `.cursor/templates/backend-utils-templates.md` - utilities и tests
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - чек-лист
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - 150+ техник

