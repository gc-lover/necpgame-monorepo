// Issue: #1600 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	api "github.com/necpgame/stock-indices-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// StockHandlers implements api.Handler interface (ogen typed handlers)
type StockHandlers struct {
	logger *logrus.Logger
}

// NewStockHandlers creates new handlers
func NewStockHandlers(logger *logrus.Logger) *StockHandlers {
	return &StockHandlers{logger: logger}
}

// HealthCheck - TYPED response!
func (h *StockHandlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	return &api.HealthCheckOK{
		Status: api.NewOptString("ok"),
	}, nil
}

