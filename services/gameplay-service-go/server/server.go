package server

import (
	"context"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	gameplayservice "github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
)

// Server implements the gameplayservice.Handler
type Server struct {
	logger               *zap.Logger
	db                   *pgxpool.Pool
	tokenAuth            *jwtauth.JWTAuth
	combatPool           *sync.Pool // Memory pool for combat operations
	abilitiesPool        *sync.Pool // Memory pool for abilities operations
	implantsPool         *sync.Pool // Memory pool for implants operations
}

// NewServer creates a new server instance
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Server {
	return &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
		combatPool: &sync.Pool{
			New: func() interface{} {
				return &gameplayservice.CombatSession{}
			},
		},
		abilitiesPool: &sync.Pool{
			New: func() interface{} {
				return &gameplayservice.AbilityActivationResult{}
			},
		},
		implantsPool: &sync.Pool{
			New: func() interface{} {
				return &gameplayservice.ImplantStatsResponse{}
			},
		},
	}
}

// CreateRouter creates the HTTP router with all endpoints
func (s *Server) CreateRouter() http.Handler {
	h := gameplayservice.NewServer(s)
	return h
}

// GetHealth implements gameplayservice.Handler
func (s *Server) GetHealth(ctx context.Context) (gameplayservice.GetHealthRes, error) {
	// Get health response from pool
	healthResp := s.combatPool.Get().(*gameplayservice.HealthResponse)
	defer s.combatPool.Put(healthResp)

	// Reset the struct
	*healthResp = gameplayservice.HealthResponse{}

	// Fill with current health data
	healthResp.Status = "healthy"
	healthResp.Timestamp = time.Now()
	healthResp.Version = "2.0.0"
	healthResp.Uptime = 0 // TODO: Implement uptime tracking

	s.logger.Info("Health check requested",
		zap.String("status", healthResp.Status),
		zap.Time("timestamp", healthResp.Timestamp))

	return &gameplayservice.GetHealthOK{
		Data: *healthResp,
	}, nil
}

// PostCombatSessionsCreate implements gameplayservice.Handler
func (s *Server) PostCombatSessionsCreate(ctx context.Context, req gameplayservice.PostCombatSessionsCreateReq) (gameplayservice.PostCombatSessionsCreateRes, error) {
	// Get combat session from pool
	session := s.combatPool.Get().(*gameplayservice.CombatSession)
	defer s.combatPool.Put(session)

	// Reset the struct
	*session = gameplayservice.CombatSession{}

	// Create new combat session
	session.SessionID = "session-" + time.Now().Format("20060102150405")
	session.PlayerID = req.PlayerID
	session.StartTime = time.Now()
	session.Status = "active"

	s.logger.Info("Combat session created",
		zap.String("session_id", session.SessionID),
		zap.String("player_id", req.PlayerID))

	return &gameplayservice.PostCombatSessionsCreateOK{
		Data: *session,
	}, nil
}

// PostCombatAbilitiesActivate implements gameplayservice.Handler
func (s *Server) PostCombatAbilitiesActivate(ctx context.Context, req gameplayservice.PostCombatAbilitiesActivateReq) (gameplayservice.PostCombatAbilitiesActivateRes, error) {
	// Get ability result from pool
	result := s.abilitiesPool.Get().(*gameplayservice.AbilityActivationResult)
	defer s.abilitiesPool.Put(result)

	// Reset the struct
	*result = gameplayservice.AbilityActivationResult{}

	// Simulate ability activation
	result.AbilityID = req.AbilityID
	result.PlayerID = req.PlayerID
	result.ActivatedAt = time.Now()
	result.Success = true
	result.CooldownMs = 5000

	s.logger.Info("Ability activated",
		zap.String("ability_id", req.AbilityID),
		zap.String("player_id", req.PlayerID))

	return &gameplayservice.PostCombatAbilitiesActivateOK{
		Data: *result,
	}, nil
}

// GetCombatImplantsStats implements gameplayservice.Handler
func (s *Server) GetCombatImplantsStats(ctx context.Context, params gameplayservice.GetCombatImplantsStatsParams) (gameplayservice.GetCombatImplantsStatsRes, error) {
	// Get implant stats from pool
	stats := s.implantsPool.Get().(*gameplayservice.ImplantStatsResponse)
	defer s.implantsPool.Put(stats)

	// Reset the struct
	*stats = gameplayservice.ImplantStatsResponse{}

	// Simulate implant stats retrieval
	stats.ImplantID = params.ImplantID
	stats.Level = 5
	stats.SuccessRate = 0.95
	stats.TotalUses = 150

	s.logger.Info("Implant stats retrieved",
		zap.String("implant_id", params.ImplantID))

	return &gameplayservice.GetCombatImplantsStatsOK{
		Data: *stats,
	}, nil
}

// Issue: #104
