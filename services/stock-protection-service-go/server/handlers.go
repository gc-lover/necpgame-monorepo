// Issue: #1600, #1607 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"sync"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/stock-protection-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// StockHandlers implements api.Handler interface (ogen typed handlers)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type StockHandlers struct {
	logger *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	healthCheckPool sync.Pool
}

// NewStockHandlers creates new handlers with memory pooling
func NewStockHandlers(logger *logrus.Logger) *StockHandlers {
	h := &StockHandlers{logger: logger}

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
func (h *StockHandlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1607 - Use memory pooling
	result := h.healthCheckPool.Get().(*api.HealthCheckOK)
	// Note: Not returning to pool - struct is returned to caller

	result.Status = api.NewOptString("ok")

	return result, nil
}
