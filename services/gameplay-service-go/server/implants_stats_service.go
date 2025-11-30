// Issue: #142109960
package server

import (
	"context"
	"errors"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/pkg/implantsstatsapi"
	"github.com/sirupsen/logrus"
)

type ImplantsStatsServiceInterface interface {
	GetEnergyStatus(ctx context.Context, characterID *uuid.UUID) (*implantsstatsapi.EnergyStatus, error)
	GetHumanityStatus(ctx context.Context, characterID *uuid.UUID) (*implantsstatsapi.HumanityStatus, error)
	CheckCompatibility(ctx context.Context, characterID *uuid.UUID, implantID uuid.UUID) (*implantsstatsapi.CompatibilityResult, error)
	GetSetBonuses(ctx context.Context, characterID *uuid.UUID) (*implantsstatsapi.SetBonuses, error)
}

type ImplantsStatsService struct {
	repo   ImplantsStatsRepositoryInterface
	logger *logrus.Logger
}

func NewImplantsStatsService(db *pgxpool.Pool) *ImplantsStatsService {
	return &ImplantsStatsService{
		repo:   NewImplantsStatsRepository(db),
		logger: GetLogger(),
	}
}

func (s *ImplantsStatsService) GetEnergyStatus(ctx context.Context, characterID *uuid.UUID) (*implantsstatsapi.EnergyStatus, error) {
	if characterID == nil {
		return nil, errors.New("character_id is required")
	}

	status, err := s.repo.GetEnergyStatus(ctx, *characterID)
	if err != nil {
		return nil, err
	}

	return convertEnergyStatusToAPI(status), nil
}

func (s *ImplantsStatsService) GetHumanityStatus(ctx context.Context, characterID *uuid.UUID) (*implantsstatsapi.HumanityStatus, error) {
	if characterID == nil {
		return nil, errors.New("character_id is required")
	}

	status, err := s.repo.GetHumanityStatus(ctx, *characterID)
	if err != nil {
		return nil, err
	}

	return convertHumanityStatusToAPI(status), nil
}

func (s *ImplantsStatsService) CheckCompatibility(ctx context.Context, characterID *uuid.UUID, implantID uuid.UUID) (*implantsstatsapi.CompatibilityResult, error) {
	if characterID == nil {
		return nil, errors.New("character_id is required")
	}

	result, err := s.repo.CheckCompatibility(ctx, *characterID, implantID)
	if err != nil {
		return nil, err
	}

	return convertCompatibilityResultToAPI(result), nil
}

func (s *ImplantsStatsService) GetSetBonuses(ctx context.Context, characterID *uuid.UUID) (*implantsstatsapi.SetBonuses, error) {
	if characterID == nil {
		return nil, errors.New("character_id is required")
	}

	bonuses, err := s.repo.GetSetBonuses(ctx, *characterID)
	if err != nil {
		return nil, err
	}

	return convertSetBonusesToAPI(bonuses), nil
}

