// Issue: #1600 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond // Performance: context timeout for DB ops
)

// IntegrationHandlers implements handlers for stock integration service
type IntegrationHandlers struct {
	logger *logrus.Logger
}

// NewIntegrationHandlers creates new handlers
func NewIntegrationHandlers(logger *logrus.Logger) *IntegrationHandlers {
	return &IntegrationHandlers{logger: logger}
}

// HealthCheck returns health status
func (h *IntegrationHandlers) HealthCheck(ctx context.Context) (map[string]string, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	return map[string]string{
		"status": "ok",
	}, nil
}

