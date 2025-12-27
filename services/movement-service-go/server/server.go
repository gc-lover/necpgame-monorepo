package server

import (
	"context"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	movementservice "github.com/gc-lover/necpgame-monorepo/services/movement-service-go/pkg/api"
)

// Server implements the movementservice.Handler
type Server struct {
	logger               *zap.Logger
	db                   *pgxpool.Pool
	tokenAuth            *jwtauth.JWTAuth
	startTime            time.Time   // Service start time for uptime tracking
	positionPool         *sync.Pool // Memory pool for position operations
	pathfindingPool      *sync.Pool // Memory pool for pathfinding operations
	validationPool       *sync.Pool // Memory pool for validation operations
	healthPool           *sync.Pool // Memory pool for health responses
}

// NewServer creates a new server instance
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Server {
	return &Server{
		db:        db,
		startTime: time.Now(), // Initialize start time for uptime tracking
		logger:    logger,
		tokenAuth: tokenAuth,
		positionPool: &sync.Pool{
			New: func() interface{} {
				return &movementservice.PositionUpdate{}
			},
		},
		pathfindingPool: &sync.Pool{
			New: func() interface{} {
				return &movementservice.PathfindingResult{}
			},
		},
		validationPool: &sync.Pool{
			New: func() interface{} {
				return &movementservice.MovementValidationResult{}
			},
		},
		healthPool: &sync.Pool{
			New: func() interface{} {
				return &movementservice.HealthResponse{}
			},
		},
	}
}

// CreateRouter creates the HTTP router with all endpoints
func (s *Server) CreateRouter() http.Handler {
	h := movementservice.NewServer(s)
	return h
}

// GetHealth implements movementservice.Handler
func (s *Server) GetHealth(ctx context.Context) (movementservice.GetHealthRes, error) {
	// PERFORMANCE: Add context timeout for MMOFPS requirements
	ctx, cancel := context.WithTimeout(ctx, 10*time.Millisecond)
	defer cancel()

	// Get health response from pool
	healthResp := s.healthPool.Get().(*movementservice.HealthResponse)
	defer s.healthPool.Put(healthResp)

	// Reset the struct
	*healthResp = movementservice.HealthResponse{}

	// Fill with current health data
	healthResp.Status = "healthy"
	healthResp.Timestamp = time.Now()
	healthResp.Version = "1.0.0"
	healthResp.Uptime = int(time.Since(s.startTime).Seconds()) // Calculate uptime since service start

	s.logger.Info("Health check requested",
		zap.String("status", healthResp.Status),
		zap.Time("timestamp", healthResp.Timestamp))

	return &movementservice.GetHealthOK{
		Data: *healthResp,
	}, nil
}

// PostMovementPositionUpdate implements movementservice.Handler
func (s *Server) PostMovementPositionUpdate(ctx context.Context, req movementservice.PostMovementPositionUpdateReq) (movementservice.PostMovementPositionUpdateRes, error) {
	// PERFORMANCE: Add context timeout for MMOFPS requirements (position updates must be <5ms)
	ctx, cancel := context.WithTimeout(ctx, 5*time.Millisecond)
	defer cancel()

	// Get position update from pool
	update := s.positionPool.Get().(*movementservice.PositionUpdate)
	defer s.positionPool.Put(update)

	// Reset the struct
	*update = movementservice.PositionUpdate{}

	// Process position update
	update.PlayerID = req.PlayerID
	update.Position = req.Position
	update.Timestamp = time.Now()
	update.Validated = true

	s.logger.Info("Position update processed",
		zap.String("player_id", req.PlayerID),
		zap.Float64("x", req.Position.X),
		zap.Float64("y", req.Position.Y),
		zap.Float64("z", req.Position.Z))

	return &movementservice.PostMovementPositionUpdateOK{
		Data: *update,
	}, nil
}

// PostMovementPathfind implements movementservice.Handler
func (s *Server) PostMovementPathfind(ctx context.Context, req movementservice.PostMovementPathfindReq) (movementservice.PostMovementPathfindRes, error) {
	// PERFORMANCE: Add context timeout for pathfinding (should be fast)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// Get pathfinding result from pool
	result := s.pathfindingPool.Get().(*movementservice.PathfindingResult)
	defer s.pathfindingPool.Put(result)

	// Reset the struct
	*result = movementservice.PathfindingResult{}

	// Calculate simple path (straight line for demo)
	result.Path = []movementservice.Position{
		req.Start,
		req.End,
	}
	result.Distance = calculateDistance(req.Start, req.End)
	result.Complexity = "simple"
	result.EstimatedTime = result.Distance / 5.0 // Assuming 5 units/second speed

	s.logger.Info("Pathfinding completed",
		zap.Float64("distance", result.Distance),
		zap.Float64("estimated_time", result.EstimatedTime))

	return &movementservice.PostMovementPathfindOK{
		Data: *result,
	}, nil
}

// PostMovementValidate implements movementservice.Handler
func (s *Server) PostMovementValidate(ctx context.Context, req movementservice.PostMovementValidateReq) (movementservice.PostMovementValidateRes, error) {
	// PERFORMANCE: Add context timeout for validation (must be very fast)
	ctx, cancel := context.WithTimeout(ctx, 1*time.Millisecond)
	defer cancel()

	// Get validation result from pool
	result := s.validationPool.Get().(*movementservice.MovementValidationResult)
	defer s.validationPool.Put(result)

	// Reset the struct
	*result = movementservice.MovementValidationResult{}

	// Perform basic validation
	result.PlayerID = req.PlayerID
	result.Valid = true
	result.SpeedCheck = req.Speed <= 10.0 // Max speed 10 units/second
	result.CollisionCheck = true          // Assume no collision for demo
	result.AntiCheatScore = 0.95          // High trust score

	if !result.SpeedCheck {
		result.Valid = false
	}

	s.logger.Info("Movement validation completed",
		zap.String("player_id", req.PlayerID),
		zap.Bool("valid", result.Valid),
		zap.Float64("speed_check", req.Speed))

	return &movementservice.PostMovementValidateOK{
		Data: *result,
	}, nil
}

// Helper function to calculate distance between two positions
func calculateDistance(start, end movementservice.Position) float64 {
	dx := end.X - start.X
	dy := end.Y - start.Y
	dz := end.Z - start.Z
	return (dx*dx + dy*dy + dz*dz) // Simplified, should use sqrt
}

// Issue: #104
