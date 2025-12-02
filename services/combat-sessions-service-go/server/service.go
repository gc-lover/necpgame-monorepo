// Issue: #130

package server

import (
	"context"
	"errors"
	"time"

	"github.com/combat-sessions-service-go/pkg/api"
	"github.com/google/uuid"
)

var (
	ErrSessionNotFound = errors.New("session not found")
	ErrUnauthorized    = errors.New("unauthorized")
	ErrActionRejected  = errors.New("action rejected")
)

// CombatSessionService business logic
type CombatSessionService struct {
	repo         Repository
	cache        *RedisCache
	eventBus     *KafkaEventBus
	antiCheat    *AntiCheatValidator
	combatEngine *CombatEngine
}

// NewCombatSessionService creates new service
func NewCombatSessionService(repo Repository, redisAddr string, kafkaBrokers string) *CombatSessionService {
	return &CombatSessionService{
		repo:         repo,
		cache:        NewRedisCache(redisAddr),
		eventBus:     NewKafkaEventBus(kafkaBrokers),
		antiCheat:    NewAntiCheatValidator(),
		combatEngine: NewCombatEngine(),
	}
}

// CreateSession creates new combat session
func (s *CombatSessionService) CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSessionResponse, error) {
	sessionID := uuid.New().String()

	// Create session in DB
	maxParticipants := 100
	if req.Settings != nil {
		if val, ok := (*req.Settings)["max_participants"]; ok {
			if maxInt, ok := val.(int); ok {
				maxParticipants = maxInt
			}
		}
	}
	
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

	return toAPISession(session), nil
}

// ListSessions lists combat sessions
func (s *CombatSessionService) ListSessions(ctx context.Context, params api.ListCombatSessionsParams) (*api.SessionListResponse, error) {
	sessions, _, err := s.repo.ListSessions(ctx, params)
	if err != nil {
		return nil, err
	}

	items := make([]api.SessionSummary, len(sessions))
	for i, session := range sessions {
		items[i] = toAPISessionSummary(session)
	}

	// Convert to interface{} array for PaginationResponse
	itemsInterface := make([]interface{}, len(items))
	for i, item := range items {
		itemsInterface[i] = item
	}
	
	return &api.SessionListResponse{
		Items:      items,
		Pagination: api.PaginationResponse{Items: itemsInterface},
	}, nil
}

// GetSession gets combat session by ID
func (s *CombatSessionService) GetSession(ctx context.Context, sessionID string) (*api.CombatSessionResponse, error) {
	// Try cache first
	if cached, err := s.cache.GetSession(ctx, sessionID); err == nil {
		return toAPISession(cached), nil
	}

	// Get from DB
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return nil, ErrSessionNotFound
	}

	return toAPISession(session), nil
}

// EndSession ends combat session
func (s *CombatSessionService) EndSession(ctx context.Context, sessionID string, playerID string) (*api.SessionEndResponse, error) {
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return nil, ErrSessionNotFound
	}

	// Check authorization (simplified - should check if player is leader/admin)
	if session.Status != "active" {
		return nil, errors.New("session not active")
	}

	// Update session
	session.Status = "ended"
	session.EndedAt = time.Now()

	if err := s.repo.UpdateSession(ctx, session); err != nil {
		return nil, err
	}

	// Publish event
	s.eventBus.PublishSessionEnded(ctx, session)

	// Calculate rewards (simplified)
	rewards := s.calculateRewards(session)

	sessionUUID, _ := uuid.Parse(sessionID)
	status := api.SessionStatus(session.Status)
	winnerTeam := session.WinnerTeam
	
	return &api.SessionEndResponse{
		SessionId:  sessionUUID,
		Status:     status,
		WinnerTeam: &winnerTeam,
		Rewards:    rewards,
	}, nil
}

// ExecuteAction executes combat action
func (s *CombatSessionService) ExecuteAction(ctx context.Context, sessionID string, playerID string, req *api.ActionRequest) (*api.ActionResponse, error) {
	// Get session
	session, err := s.repo.GetSession(ctx, sessionID)
	if err != nil {
		return nil, ErrSessionNotFound
	}

	// Anti-cheat validation
	validation := s.antiCheat.ValidateAction(ctx, session, playerID, req)
	if !validation.AntiCheatPassed {
		return nil, ErrActionRejected
	}

	// Execute action through combat engine
	result := s.combatEngine.ExecuteAction(ctx, session, playerID, req)

	// Log action
	logEntry := &CombatLog{
		SessionID:      sessionID,
		EventType:      string(req.ActionType),
		ActorID:        playerID,
		Timestamp:      time.Now(),
		SequenceNumber: session.NextSequence,
	}
	s.repo.CreateLog(ctx, logEntry)

	// Publish event
	s.eventBus.PublishActionExecuted(ctx, session, result)

	return result, nil
}

// GetSessionState gets realtime session state
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
	
	return &api.LogsResponse{
		Logs:       logsAPI,
		Pagination: api.PaginationResponse{Items: logsInterface},
	}, nil
}

// GetSessionStats gets combat statistics
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
	stats.TotalDamage = &totalDamage

	return stats, nil
}

// Helper: calculate rewards (simplified)
func (s *CombatSessionService) calculateRewards(session *CombatSession) *[]api.Reward {
	// TODO: implement reward calculation
	return &[]api.Reward{}
}

// Helper: conversion functions
func toAPISession(session *CombatSession) *api.CombatSessionResponse {
	// TODO: implement full conversion
	return &api.CombatSessionResponse{}
}

func toAPISessionSummary(session *CombatSession) api.SessionSummary {
	id, _ := uuid.Parse(session.ID)
	sessionType := api.SessionType(session.SessionType)
	status := api.SessionStatus(session.Status)
	participantCount := 0
	
	return api.SessionSummary{
		Id:               id,
		SessionType:      sessionType,
		Status:           status,
		ParticipantCount: &participantCount,
		CreatedAt:        session.CreatedAt,
	}
}

func toAPIParticipantStates(participants []*CombatParticipant) []api.ParticipantState {
	// TODO: implement conversion
	return []api.ParticipantState{}
}

func toAPICombatLogs(logs []*CombatLog) []api.CombatLog {
	// TODO: implement conversion
	return []api.CombatLog{}
}

func toAPIParticipantStats(participants []*CombatParticipant) []api.ParticipantStats {
	// TODO: implement conversion
	return []api.ParticipantStats{}
}

