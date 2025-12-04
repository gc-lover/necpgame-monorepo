// Issue: #1596 - ogen migration + performance optimizations
// Migrated from oapi-codegen to ogen for typed responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	logger *logrus.Logger
}

// NewHandlers creates handlers with dependency injection
func NewHandlers(logger *logrus.Logger) *Handlers {
	return &Handlers{logger: logger}
}

// ListContinents implements GET /world/continents (TYPED ogen response)
func (h *Handlers) ListContinents(ctx context.Context, params api.ListContinentsParams) (api.ListContinentsRes, error) {
	// CRITICAL: Context timeout for DB operations (50ms)
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic - get continents from DB
	// For now, return empty list
	response := &api.ContinentListResponse{
		Continents: []api.SchemasContinent{},
		Total:      api.NewOptInt(0),
	}

	return response, nil
}

// CreateContinent implements POST /world/continents (TYPED ogen response)
func (h *Handlers) CreateContinent(ctx context.Context, req *api.CreateContinentRequest) (api.CreateContinentRes, error) {
	// CRITICAL: Context timeout for DB operations (50ms)
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic - create continent in DB
	// For now, return dummy response
	continentID := uuid.New()
	response := &api.SchemasContinent{
		ID:       continentID,
		Name:     req.Name,
		PlanetID: req.PlanetID,
		Status:   api.SchemasContinentStatusActive,
	}

	// Set optional fields if provided
	if req.Description.Set {
		response.Description = req.Description
	}
	if req.Size.Set {
		response.Size = req.Size
	}
	if req.Climate.Set {
		// Convert CreateContinentRequestClimate to SchemasContinentClimate
		climateMap := map[api.CreateContinentRequestClimate]api.SchemasContinentClimate{
			api.CreateContinentRequestClimateTemperate: api.SchemasContinentClimateTemperate,
			api.CreateContinentRequestClimateArid:      api.SchemasContinentClimateArid,
			api.CreateContinentRequestClimateTropical:   api.SchemasContinentClimateTropical,
			api.CreateContinentRequestClimateArctic:     api.SchemasContinentClimateArctic,
			api.CreateContinentRequestClimateToxic:     api.SchemasContinentClimateToxic,
		}
		if climate, ok := climateMap[req.Climate.Value]; ok {
			response.Climate = api.NewOptSchemasContinentClimate(climate)
		}
	}

	return response, nil
}
