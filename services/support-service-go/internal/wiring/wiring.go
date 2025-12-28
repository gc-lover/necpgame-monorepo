// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: Dependency injection with optimized component initialization

package wiring

import (
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"support-service-go/internal/handlers"
	"support-service-go/internal/repository"
	"support-service-go/internal/service"
)

// WireComponents creates and wires all SLA components together
func WireComponents(db *pgxpool.Pool, logger *zap.Logger) (*handlers.SLAHandlers, error) {
	// Create repository
	slaRepo := repository.NewSLARepository(db, logger)

	// Create service
	slaService := service.NewSLAService(slaRepo, logger)

	// Create handlers
	slaHandlers := handlers.NewSLAHandlers(slaService, logger)

	return slaHandlers, nil
}

// WireComponentsWithDefaults creates components with default configuration
func WireComponentsWithDefaults() (*handlers.SLAHandlers, error) {
	// Create default logger
	logger := zap.NewNop() // No-op logger for development

	// No database connection for now (using in-memory or mock)
	var db *pgxpool.Pool = nil

	return WireComponents(db, logger)
}

// Issue: #1489 - Support SLA Service: ogen handlers implementation
