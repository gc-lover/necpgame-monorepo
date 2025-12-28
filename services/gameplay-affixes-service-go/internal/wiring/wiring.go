// Issue: #1495 - Gameplay Affixes Service implementation
// PERFORMANCE: Dependency injection with optimized component initialization

package wiring

import (
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"gameplay-affixes-service-go/internal/handlers"
	"gameplay-affixes-service-go/internal/repository"
	"gameplay-affixes-service-go/internal/service"
)

// WireComponents creates and wires all affix components together
func WireComponents(db *pgxpool.Pool, logger *zap.Logger) (*handlers.AffixHandlers, error) {
	// Create repository
	affixRepo := repository.NewAffixRepository(db, logger)

	// Create service
	affixService := service.NewAffixService(affixRepo, logger)

	// Create handlers
	affixHandlers := handlers.NewAffixHandlers(affixService, logger)

	return affixHandlers, nil
}
