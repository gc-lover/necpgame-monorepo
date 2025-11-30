// Issue: #140876058
package server

import (
	"context"
	"errors"
	"time"

	"github.com/necpgame/world-service-go/models"
	"github.com/sirupsen/logrus"
)

type WorldStateServiceInterface interface {
	GetStateByKey(ctx context.Context, key string) (*models.GlobalState, error)
	GetStateByCategory(ctx context.Context, category string) ([]models.GlobalState, error)
	CreateState(ctx context.Context, key, category string, value map[string]interface{}, syncType string) (*models.GlobalState, error)
	UpdateState(ctx context.Context, key string, value map[string]interface{}, version *int, syncType *string) (*models.GlobalState, error)
	DeleteState(ctx context.Context, key string) error
	BatchUpdateState(ctx context.Context, updates []StateUpdate) ([]models.GlobalState, error)
}

type worldStateService struct {
	repo   WorldStateRepository
	logger *logrus.Logger
}

func NewWorldStateService(repo WorldStateRepository) WorldStateServiceInterface {
	return &worldStateService{
		repo:   repo,
		logger: GetLogger(),
	}
}

func (s *worldStateService) GetStateByKey(ctx context.Context, key string) (*models.GlobalState, error) {
	if key == "" {
		return nil, errors.New("key is required")
	}

	state, err := s.repo.GetStateByKey(ctx, key)
	if err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to get state by key")
		return nil, err
	}

	return state, nil
}

func (s *worldStateService) GetStateByCategory(ctx context.Context, category string) ([]models.GlobalState, error) {
	if category == "" {
		return nil, errors.New("category is required")
	}

	states, err := s.repo.GetStateByCategory(ctx, category)
	if err != nil {
		s.logger.WithError(err).WithField("category", category).Error("Failed to get state by category")
		return nil, err
	}

	return states, nil
}

func (s *worldStateService) CreateState(ctx context.Context, key, category string, value map[string]interface{}, syncType string) (*models.GlobalState, error) {
	if key == "" {
		return nil, errors.New("key is required")
	}
	if category == "" {
		return nil, errors.New("category is required")
	}
	if value == nil {
		value = make(map[string]interface{})
	}
	if syncType == "" {
		syncType = "SERVER_WIDE"
	}

	// Validate sync_type
	if syncType != "SERVER_WIDE" && syncType != "PLAYER_SPECIFIC" && syncType != "PHASED" {
		return nil, errors.New("invalid sync_type")
	}

	// Check if state already exists
	existing, err := s.repo.GetStateByKey(ctx, key)
	if err != nil {
		return nil, err
	}
	if existing != nil {
		return nil, errors.New("state already exists")
	}

	state := &models.GlobalState{
		Key:       key,
		Category:  category,
		Value:     value,
		Version:   1,
		SyncType:  syncType,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	if err := s.repo.CreateState(ctx, state); err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to create state")
		return nil, err
	}

	return state, nil
}

func (s *worldStateService) UpdateState(ctx context.Context, key string, value map[string]interface{}, version *int, syncType *string) (*models.GlobalState, error) {
	if key == "" {
		return nil, errors.New("key is required")
	}
	if value == nil {
		return nil, errors.New("value is required")
	}

	if syncType != nil {
		if *syncType != "SERVER_WIDE" && *syncType != "PLAYER_SPECIFIC" && *syncType != "PHASED" {
			return nil, errors.New("invalid sync_type")
		}
	}

	state, err := s.repo.UpdateState(ctx, key, value, version, syncType)
	if err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to update state")
		return nil, err
	}

	return state, nil
}

func (s *worldStateService) DeleteState(ctx context.Context, key string) error {
	if key == "" {
		return errors.New("key is required")
	}

	if err := s.repo.DeleteState(ctx, key); err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to delete state")
		return err
	}

	return nil
}

func (s *worldStateService) BatchUpdateState(ctx context.Context, updates []StateUpdate) ([]models.GlobalState, error) {
	if len(updates) == 0 {
		return nil, errors.New("updates are required")
	}

	states, err := s.repo.BatchUpdateState(ctx, updates)
	if err != nil {
		s.logger.WithError(err).Error("Failed to batch update state")
		return nil, err
	}

	return states, nil
}

