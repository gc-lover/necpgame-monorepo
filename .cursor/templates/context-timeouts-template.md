# Context Timeouts Template

**Issue: #1604**

## Добавление в handlers.go

```go
// Issue: #1604 - Context Timeouts
package server

import (
    "context"
    "time"
)

// Context timeout constants
const (
    DBTimeout    = 50 * time.Millisecond
    CacheTimeout = 10 * time.Millisecond
    HTTPTimeout  = 5 * time.Second
)

// В каждом handler:
func (h *Handlers) GetSomething(ctx context.Context, ...) (..., error) {
    ctx, cancel := context.WithTimeout(ctx, DBTimeout)
    defer cancel()
    
    // ... handler logic
}
```

## Для HTTP handlers (oapi-codegen style)

```go
func (h *Handlers) GetSomething(w http.ResponseWriter, r *http.Request) {
    ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
    defer cancel()
    
    // ... handler logic
}
```

## Для service methods (если вызываются напрямую)

```go
func (s *Service) GetData(ctx context.Context, id string) (*Data, error) {
    // Если service вызывается из handler, timeout уже есть
    // Если вызывается напрямую, добавить:
    ctx, cancel := context.WithTimeout(ctx, DBTimeout)
    defer cancel()
    
    // ... service logic
}
```

## Timeout Values

- **DBTimeout:** 50ms (database queries)
- **CacheTimeout:** 10ms (Redis/cache operations)
- **HTTPTimeout:** 5s (external HTTP calls)

## Reference

- `services/combat-combos-service-ogen-go/server/handlers.go`
- `services/chat-service-go/server/handlers.go`
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - Part 1

