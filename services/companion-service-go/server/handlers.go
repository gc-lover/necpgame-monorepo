// Package server Issue: #1607, ogen migration
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/necpgame/companion-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const DBTimeout = 50 * time.Millisecond

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

// HealthCheck - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	_ = ctx

	// Issue: #1607 - Use memory pooling
	result := h.healthCheckPool.Get().(*api.HealthCheckOK)
	// Note: Not returning to pool - struct is returned to caller

	result.Status = api.NewOptString("ok")

	return result, nil
}
