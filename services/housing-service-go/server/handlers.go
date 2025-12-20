// Package server Issue: #1597-#1599, ogen migration
package server

import (
	"context"
	"time"

	housingapi "github.com/necpgame/housing-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	logger *logrus.Logger
}

// NewHandlers creates new handlers
func NewHandlers(logger *logrus.Logger) *Handlers {
	return &Handlers{logger: logger}
}

// HealthCheck implements GET /health - TYPED response!
func (h *Handlers) HealthCheck(ctx context.Context) (*housingapi.HealthCheckOK, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()
	_ = ctx // Health check doesn't need DB, but timeout for consistency

	// Return TYPED response (ogen will marshal directly!)
	return &housingapi.HealthCheckOK{
		Status: housingapi.NewOptString("ok"),
	}, nil
}
