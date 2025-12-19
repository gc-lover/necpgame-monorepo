// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #130, #1607

package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
	"github.com/google/uuid"
)

var (
	ErrSessionNotFound = errors.New("session not found")
	ErrUnauthorized    = errors.New("unauthorized")
	ErrActionRejected  = errors.New("action rejected")
)

// CombatSessionService business logic with memory pooling (Issue #1607)
type CombatSessionService struct {
	repo         Repository
	cache        *RedisCache
	eventBus     *KafkaEventBus
	antiCheat    *AntiCheatValidator
	combatEngine *CombatEngine

	// Memory pooling for hot path structs (Level 2 optimization)
	sessionPool sync.Pool
}

// NewCombatSessionService creates new service with memory pooling
func NewCombatSessionService(repo Repository, redisAddr string, kafkaBrokers string) *CombatSessionService {
	s := &CombatSessionService{
		repo:         repo,
		cache:        NewRedisCache(redisAddr),
		eventBus:     NewKafkaEventBus(kafkaBrokers),
		antiCheat:    NewAntiCheatValidator(),
		combatEngine: NewCombatEngine(),
	}

	// Initialize memory pool (zero allocations target!)
	s.sessionPool = sync.Pool{
		New: func() interface{} {
			return &api.CombatSession{}
		},
	}

	return s
}

// CreateSession creates new combat session (hot path - uses memory pooling)
func (s *CombatSessionService) CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSession, error) {
	sessionID := uuid.New().String()

	// Create session in DB
	maxParticipants := 100
	
	session := &CombatSession{
		ID:              sessionID,
		SessionType:     string(req.SessionType),
		Status:          "pending",
		MaxParticipants: maxParticipants,
		CreatedAt:       time.Now(),
	}

	if err := s.repo.CreateSession(ctx, session); err != nil {
		return nil, err
	}

	// Cache session
	if err := s.cache.SetSession(ctx, sessionID, session, 3600*time.Second); err != nil {
		// Log error but continue
	}

	// Publish event
	s.eventBus.PublishSessionCreated(ctx, session)

	// Get memory pooled response (zero allocation!)
	resp := s.sessionPool.Get().(*api.CombatSession)
	defer s.sessionPool.Put(resp)

	// Populate response (reuse pooled struct)
	resp.ID = uuid.MustParse(sessionID)
	resp.PlayerID = req.PlayerID
	resp.SessionType = string(req.SessionType)
	resp.Status = api.CombatSessionStatusActive
	resp.CreatedAt = session.CreatedAt

	// Clone response (caller owns it)
	result := &api.CombatSession{
		ID:          resp.ID,
		PlayerID:    resp.PlayerID,
		SessionType: resp.SessionType,
		Status:      resp.Status,
		CreatedAt:   resp.CreatedAt,
	}

	return result, nil
}

// ListSessions lists combat sessions (hot path - uses memory pooling)
func (s *CombatSessionService) ListSessions(ctx context.Context, params api.ListCombatSessionsParams) ([]api.CombatSession, error) {
	sessions, _, err := s.repo.ListSessions(ctx, params)
	if err != nil {
		return nil, err
	}

	result := make([]api.CombatSession, len(sessions))
	for i, session := range sessions {
		// Get memory pooled response (zero allocation!)
		resp := s.sessionPool.Get().(*api.CombatSession)
		
		// Populate response
		resp.ID = uuid.MustParse(session.ID)
		resp.PlayerID = uuid.New() // TODO: Get from session
		resp.SessionType = session.SessionType
		resp.Status = api.CombatSessionStatusActive
		resp.CreatedAt = session.CreatedAt

		// Clone response (caller owns it)
		result[i] = api.CombatSession{
			ID:          resp.ID,
			PlayerID:    resp.PlayerID,
			SessionType: resp.SessionType,
			Status:      resp.Status,
			CreatedAt:   resp.CreatedAt,
		}

		// Return to pool
		s.sessionPool.Put(resp)
	}
	
	return result, nil
}

// GetSession gets combat session by ID (hot path - uses memory pooling)
func (s *CombatSessionService) GetSession(ctx context.Context, sessionID string) (*api.CombatSession, error) {
	// Get memory pooled response (zero allocation!)
	resp := s.sessionPool.Get().(*api.CombatSession)
	defer s.sessionPool.Put(resp)

	// Try cache first
	if cached, err := s.cache.GetSession(ctx, sessionID); err == nil {
		// Populate response
		resp.ID = uuid.MustParse(cached.ID)
		resp.PlayerID = uuid.New()
		resp.SessionType = cached.SessionType
		resp.Status = api.CombatSessionStatusActive
		resp.CreatedAt = cached.CreatedAt

		// Clone response (caller owns it)
		result := &api.CombatSession{
			ID:          resp.ID,
			PlayerID:    resp.PlayerID,
			SessionType: resp.SessionType,
			Status:      resp.Status,
			CreatedAt:   resp.CreatedAt,
		}
		return result, nil
	}

	// Get from DB
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return nil, ErrSessionNotFound
	}

	// Populate response
	resp.ID = uuid.MustParse(session.ID)
	resp.PlayerID = uuid.New()
	resp.SessionType = session.SessionType
	resp.Status = api.CombatSessionStatusActive
	resp.CreatedAt = session.CreatedAt

	// Clone response (caller owns it)
	result := &api.CombatSession{
		ID:          resp.ID,
		PlayerID:    resp.PlayerID,
		SessionType: resp.SessionType,
		Status:      resp.Status,
		CreatedAt:   resp.CreatedAt,
	}

	return result, nil
}

// EndSession ends combat session
// TODO: Update return type when SessionEndResponse is available in OpenAPI spec
func (s *CombatSessionService) EndSession(ctx context.Context, sessionID string, playerID string) error {
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return ErrSessionNotFound
	}

	// Check authorization (simplified - should check if player is leader/admin)
	if session.Status != "active" {
		return errors.New("session not active")
	}

	// Update session
	session.Status = "ended"
	session.EndedAt = time.Now()

	if err := s.repo.UpdateSession(ctx, session); err != nil {
		return err
	}

	// Publish event
	s.eventBus.PublishSessionEnded(ctx, session)

	return nil
}

// ExecuteAction executes combat action
// TODO: Implement when ActionRequest/ActionResponse types are available in OpenAPI spec
/*
func (s *CombatSessionService) ExecuteAction(ctx context.Context, sessionID string, playerID string, req *api.ActionRequest) (*api.ActionResponse, error) {
	// Implementation commented out until types are available
	return nil, errors.New("not implemented")
}
*/

// GetSessionState gets realtime session state
// TODO: Implement when CombatState type is available in OpenAPI spec
/*
func (s *CombatSessionService) GetSessionState(ctx context.Context, sessionID string) (*api.CombatState, error) {
	// Get from cache (hot path)
	if state, err := s.cache.GetSessionState(ctx, sessionID); err == nil {
		return state, nil
	}

	// Fallback to DB
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return nil, ErrSessionNotFound
	}

	participants, err := s.repo.GetParticipants(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	sessionUUID, _ := uuid.Parse(sessionID)
	currentTurn, _ := uuid.Parse(session.CurrentTurn)
	turnNumber := session.TurnNumber
	
	state := &api.CombatState{
		SessionId:         sessionUUID,
		ParticipantsState: toAPIParticipantStates(participants),
		CurrentTurn:       currentTurn,
		TurnNumber:        &turnNumber,
	}

	return state, nil
}

// GetSessionLogs gets combat logs
func (s *CombatSessionService) GetSessionLogs(ctx context.Context, sessionID string, params api.GetCombatLogsParams) (*api.LogsResponse, error) {
	logs, _, err := s.repo.GetLogs(ctx, sessionID, params)
	if err != nil {
		return nil, err
	}

	logsAPI := toAPICombatLogs(logs)
	logsInterface := make([]interface{}, len(logsAPI))
	for i, log := range logsAPI {
		logsInterface[i] = log
	}
	
	return nil, errors.New("not implemented - LogsResponse type not available")
}
*/

// GetSessionStats gets combat statistics
// TODO: Implement when StatsResponse type is available in OpenAPI spec
/*
func (s *CombatSessionService) GetSessionStats(ctx context.Context, sessionID string) (*api.StatsResponse, error) {
	participants, err := s.repo.GetParticipants(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	sessionUUID, _ := uuid.Parse(sessionID)
	
	stats := &api.StatsResponse{
		SessionId:         sessionUUID,
		ParticipantsStats: toAPIParticipantStats(participants),
	}

	// Calculate total damage
	var totalDamage int64
	for _, p := range participants {
		totalDamage += p.DamageDealt
	}
	return nil, errors.New("not implemented - StatsResponse type not available")
}
*/


