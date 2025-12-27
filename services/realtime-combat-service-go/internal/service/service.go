// Issue: #2232
package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"go.uber.org/zap"

	"realtime-combat-service-go/internal/repository"
	"realtime-combat-service-go/internal/metrics"
)

// CombatService handles combat business logic
type CombatService struct {
	repo     *repository.CombatRepository
	metrics  *metrics.Collector
	logger   *zap.SugaredLogger
}

// NewCombatService creates a new combat service
func NewCombatService(repo *repository.CombatRepository, metrics *metrics.Collector, logger *zap.SugaredLogger) *CombatService {
	return &CombatService{
		repo:    repo,
		metrics: metrics,
		logger:  logger,
	}
}

// CreateCombatSession creates a new combat session
func (s *CombatService) CreateCombatSession(ctx context.Context, name, sessionType, mapID, gameMode string, maxPlayers int) (*repository.CombatSession, error) {
	sessionID := fmt.Sprintf("combat_%d", time.Now().UnixNano())

	session := &repository.CombatSession{
		ID:         sessionID,
		Name:       name,
		Type:       sessionType,
		Status:     "waiting",
		MaxPlayers: maxPlayers,
		CreatedAt:  time.Now(),
		MapID:      mapID,
		GameMode:   gameMode,
	}

	if err := s.repo.CreateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to create combat session: %w", err)
	}

	s.metrics.IncrementSessionsCreated()
	s.logger.Infof("Created combat session: %s", sessionID)

	return session, nil
}

// GetCombatSession retrieves a combat session
func (s *CombatService) GetCombatSession(ctx context.Context, sessionID string) (*repository.CombatSession, error) {
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get combat session: %w", err)
	}

	return session, nil
}

// StartCombatSession starts a combat session
func (s *CombatService) StartCombatSession(ctx context.Context, sessionID string) error {
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		return err
	}

	now := time.Now()
	session.Status = "active"
	session.StartedAt = &now

	if err := s.repo.UpdateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to start combat session: %w", err)
	}

	s.metrics.IncrementSessionsStarted()
	s.logger.Infof("Started combat session: %s", sessionID)

	return nil
}

// EndCombatSession ends a combat session
func (s *CombatService) EndCombatSession(ctx context.Context, sessionID string) error {
	session, err := s.repo.GetCombatSession(ctx, sessionID)
	if err != nil {
		return err
	}

	now := time.Now()
	session.Status = "ended"
	session.EndedAt = &now

	if err := s.repo.UpdateCombatSession(ctx, session); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to end combat session: %w", err)
	}

	s.metrics.IncrementSessionsEnded()
	s.logger.Infof("Ended combat session: %s", sessionID)

	return nil
}

// ApplyDamage applies damage to a player in combat
func (s *CombatService) ApplyDamage(ctx context.Context, sessionID, attackerID, victimID string, damage int, damageType string) error {
	// Store damage event
	event := &repository.CombatEvent{
		ID:        fmt.Sprintf("damage_%d", time.Now().UnixNano()),
		SessionID: sessionID,
		Type:      "damage",
		PlayerID:  attackerID,
		Data:      []byte(fmt.Sprintf(`{"victim_id":"%s","damage":%d,"type":"%s"}`, victimID, damage, damageType)),
		Timestamp: time.Now(),
	}

	if err := s.repo.StoreCombatEvent(ctx, event); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to apply damage: %w", err)
	}

	s.metrics.IncrementDamageEvents()
	s.logger.Debugf("Applied damage: session=%s attacker=%s victim=%s damage=%d", sessionID, attackerID, victimID, damage)

	return nil
}

// ExecuteAction executes a combat action
func (s *CombatService) ExecuteAction(ctx context.Context, sessionID, playerID, actionType string, actionData map[string]interface{}) error {
	// Store action event
	data, _ := json.Marshal(actionData)
	event := &repository.CombatEvent{
		ID:        fmt.Sprintf("action_%d", time.Now().UnixNano()),
		SessionID: sessionID,
		Type:      "action",
		PlayerID:  playerID,
		Data:      data,
		Timestamp: time.Now(),
	}

	if err := s.repo.StoreCombatEvent(ctx, event); err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to execute action: %w", err)
	}

	s.metrics.IncrementActionEvents()
	s.logger.Debugf("Executed action: session=%s player=%s action=%s", sessionID, playerID, actionType)

	return nil
}

// GetCombatEvents retrieves combat events for a session
func (s *CombatService) GetCombatEvents(ctx context.Context, sessionID string, limit int) ([]*repository.CombatEvent, error) {
	events, err := s.repo.GetCombatEvents(ctx, sessionID, limit)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get combat events: %w", err)
	}

	return events, nil
}
