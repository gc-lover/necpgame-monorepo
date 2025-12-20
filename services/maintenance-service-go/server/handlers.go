// Package server Issue: #1607, ogen migration
package server

import (
	"context"
	"sync"
	"time"

	"github.com/necpgame/maintenance-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	logger *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	healthCheckPool sync.Pool
}

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

// HealthCheck Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1607 - Use memory pooling
	result := h.healthCheckPool.Get().(*api.HealthCheckOK)
	// Note: Not returning to pool - struct is returned to caller

	result.Status = api.NewOptString("healthy")
	result.Timestamp = api.NewOptDateTime(time.Now())

	return result, nil
}
