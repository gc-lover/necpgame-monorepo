// Issue: #1600 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	api "github.com/necpgame/stock-integration-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// IntegrationHandlers implements api.Handler interface (ogen typed handlers)
type IntegrationHandlers struct {
	logger *logrus.Logger
}

// NewIntegrationHandlers creates new handlers
func NewIntegrationHandlers(logger *logrus.Logger) *IntegrationHandlers {
	return &IntegrationHandlers{logger: logger}
}

// HealthCheck - TYPED response!
func (h *IntegrationHandlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	return &api.HealthCheckOK{
		Status: api.NewOptString("ok"),
	}, nil
}

