// Package server Issue: #1607
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type CombatSessionServiceInterface interface {
	CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSessionResponse, error)
	GetSession(ctx context.Context, sessionID uuid.UUID) (*api.CombatSessionResponse, error)
	ListSessions(ctx context.Context, status *api.SessionStatus, sessionType *api.SessionType, limit, offset int) (*api.SessionListResponse, error)
	EndSession(ctx context.Context, sessionID uuid.UUID) (*api.SessionEndResponse, error)
}

type CombatSessionService struct {
	repo   CombatSessionRepositoryInterface
	logger *logrus.Logger
}

func NewCombatSessionService(db *pgxpool.Pool) *CombatSessionService {
	return &CombatSessionService{
		repo:   NewCombatSessionRepository(db),
		logger: logrus.New(),
	}
}

func (s *CombatSessionService) CreateSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSessionResponse, error) {
	// Validate participants
	if len(req.Participants) == 0 {
		return nil, errors.New("participants required")
	}

	if len(req.Participants) > 200 {
		return nil, errors.New("too many participants (max 200)")
	}

	return s.repo.CreateSession(ctx, req)
}

func (s *CombatSessionService) GetSession(ctx context.Context, sessionID uuid.UUID) (*api.CombatSessionResponse, error) {
	return s.repo.GetSession(ctx, sessionID)
}

func (s *CombatSessionService) ListSessions(ctx context.Context, status *api.SessionStatus, sessionType *api.SessionType, limit, offset int) (*api.SessionListResponse, error) {
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	return s.repo.ListSessions(ctx, status, sessionType, limit, offset)
}

func (s *CombatSessionService) EndSession(ctx context.Context, sessionID uuid.UUID) (*api.SessionEndResponse, error) {
	return s.repo.EndSession(ctx, sessionID)
}
