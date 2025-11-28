package server

import (
	"context"
	"fmt"

	"github.com/necpgame/combat-ai-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type Service struct {
	repo   *Repository
	logger *logrus.Logger
}

func NewService(repo *Repository) *Service {
	return &Service{
		repo:   repo,
		logger: GetLogger(),
	}
}

func (s *Service) GetAIProfiles(ctx context.Context, tier, faction *string, limit, offset int) ([]api.AIProfile, int, error) {
	profiles, total, err := s.repo.GetAIProfiles(ctx, tier, faction, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get AI profiles")
		return nil, 0, fmt.Errorf("failed to get AI profiles: %w", err)
	}

	return profiles, total, nil
}

func (s *Service) GetAIProfile(ctx context.Context, id string) (*api.AIProfileDetailed, error) {
	profile, err := s.repo.GetAIProfileByID(ctx, id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get AI profile")
		return nil, fmt.Errorf("failed to get AI profile: %w", err)
	}

	if profile == nil {
		return nil, fmt.Errorf("profile not found")
	}

	return profile, nil
}

func (s *Service) CreateEncounter(ctx context.Context, req api.CreateEncounterRequest) (*api.Encounter, error) {
	if err := s.validateEncounterRequest(req); err != nil {
		return nil, fmt.Errorf("invalid encounter request: %w", err)
	}

	encounter, err := s.repo.CreateEncounter(ctx, req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create encounter")
		return nil, fmt.Errorf("failed to create encounter: %w", err)
	}

	IncrementActiveEncounters()

	s.logger.WithFields(logrus.Fields{
		"encounter_id": encounter.Id,
	}).Info("Created encounter")

	return encounter, nil
}

func (s *Service) GetEncounter(ctx context.Context, id string) (*api.Encounter, error) {
	encounter, err := s.repo.GetEncounter(ctx, id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get encounter")
		return nil, fmt.Errorf("failed to get encounter: %w", err)
	}

	if encounter == nil {
		return nil, fmt.Errorf("encounter not found")
	}

	return encounter, nil
}

func (s *Service) StartEncounter(ctx context.Context, id string) error {
	if err := s.repo.UpdateEncounterStatus(ctx, id, "active"); err != nil {
		s.logger.WithError(err).Error("Failed to start encounter")
		return fmt.Errorf("failed to start encounter: %w", err)
	}

	s.logger.WithField("encounter_id", id).Info("Started encounter")
	return nil
}

func (s *Service) EndEncounter(ctx context.Context, id string) error {
	if err := s.repo.UpdateEncounterStatus(ctx, id, "completed"); err != nil {
		s.logger.WithError(err).Error("Failed to end encounter")
		return fmt.Errorf("failed to end encounter: %w", err)
	}

	DecrementActiveEncounters()

	s.logger.WithField("encounter_id", id).Info("Ended encounter")
	return nil
}

func (s *Service) AdvanceRaidPhase(ctx context.Context, raidID string, req api.RaidPhaseRequest) error {
	if err := s.validateRaidPhaseRequest(req); err != nil {
		return fmt.Errorf("invalid raid phase request: %w", err)
	}

	if err := s.repo.CreateRaidPhase(ctx, raidID, req); err != nil {
		s.logger.WithError(err).Error("Failed to advance raid phase")
		return fmt.Errorf("failed to advance raid phase: %w", err)
	}

	s.logger.WithFields(logrus.Fields{
		"raid_id":    raidID,
		"next_phase": req.NextPhase,
	}).Info("Advanced raid phase")

	return nil
}

func (s *Service) GetRaidPhases(ctx context.Context, raidID string) ([]api.RaidPhase, error) {
	phases, err := s.repo.GetRaidPhases(ctx, raidID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get raid phases")
		return nil, fmt.Errorf("failed to get raid phases: %w", err)
	}

	return phases, nil
}

func (s *Service) GetProfileTelemetry(ctx context.Context, profileID string) (*api.AIProfileTelemetry, error) {
	telemetry, err := s.repo.GetProfileTelemetry(ctx, profileID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get telemetry")
		return nil, fmt.Errorf("failed to get telemetry: %w", err)
	}

	if telemetry == nil {
		return nil, fmt.Errorf("telemetry not found")
	}

	return telemetry, nil
}

func (s *Service) validateEncounterRequest(req api.CreateEncounterRequest) error {
	if len(req.Enemies) == 0 {
		return fmt.Errorf("at least one enemy is required")
	}

	if req.AreaId == "" {
		return fmt.Errorf("area ID is required")
	}

	return nil
}

func (s *Service) validateRaidPhaseRequest(req api.RaidPhaseRequest) error {
	if req.NextPhase < 1 {
		return fmt.Errorf("next phase must be greater than 0")
	}

	return nil
}
