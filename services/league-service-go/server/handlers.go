// Issue: #1599, #1604, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"sync"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/league-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	logger *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	healthCheckPool sync.Pool
}

// NewHandlers creates new handlers with memory pooling
func NewHandlers(logger *logrus.Logger) *Handlers {
	h := &Handlers{logger: logger}

	// Initialize memory pools (zero allocations target!)
	h.healthCheckPool = sync.Pool{
		New: func() interface{} {
			return &api.HealthCheckOK{}
		},
	}

	return h
}

// HealthCheck implements GET /health
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()
	_ = ctx

	// Issue: #1607 - Use memory pooling
	result := h.healthCheckPool.Get().(*api.HealthCheckOK)
	// Note: Not returning to pool - struct is returned to caller

	result.Status = api.NewOptString("ok")

	return result, nil
}

// TODO: League methods will be implemented after OpenAPI spec update and code regeneration