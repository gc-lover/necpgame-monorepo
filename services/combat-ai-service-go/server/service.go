// Issue: #1595
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/combat-ai-service-go/pkg/api"
)

var ErrNotFound = errors.New("not found")

type Service struct {
	repo *Repository
}

func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) GetAIProfile(ctx context.Context, profileID string) (*api.AIProfile, error) {
	// TODO: Implement
	return &api.AIProfile{}, nil
}

func (s *Service) GetAIProfileTelemetry(ctx context.Context, profileID string) (*api.GetAIProfileTelemetryOK, error) {
	// TODO: Implement
	return &api.GetAIProfileTelemetryOK{}, nil
}

func (s *Service) ListAIProfiles(ctx context.Context, params api.ListAIProfilesParams) (*api.ListAIProfilesOK, error) {
	// TODO: Implement
	return &api.ListAIProfilesOK{}, nil
}

