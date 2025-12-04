// Issue: #1598, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-player-orders-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// ServiceHandlers implements api.Handler interface (ogen typed handlers!)
type ServiceHandlers struct {
	logger *logrus.Logger
}

// NewServiceHandlers creates new handlers
func NewServiceHandlers(logger *logrus.Logger) *ServiceHandlers {
	return &ServiceHandlers{logger: logger}
}

// HealthCheck - TYPED response!
func (h *ServiceHandlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()
	_ = ctx

	response := &api.HealthCheckOK{
		Status: api.NewOptString("ok"),
	}

	return response, nil
}
