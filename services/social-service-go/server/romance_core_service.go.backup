// Issue: #140876112
package server

import (
	"context"
	"errors"

	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type RomanceCoreServiceInterface interface {
	GetRomanceTypes(ctx context.Context) ([]string, error)
	GetPlayerRomanceRelationships(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceRelationship, int, error)
	GetPlayerRomanceRelationshipsByType(ctx context.Context, playerID uuid.UUID, romanceType string, limit, offset int) ([]RomanceRelationship, int, error)
	GetPlayerPlayerRomance(ctx context.Context, playerID1, playerID2 uuid.UUID) (*RomanceRelationship, error)
	InitiatePlayerPlayerRomance(ctx context.Context, playerID, targetPlayerID uuid.UUID, message string, privacySettings map[string]interface{}) (*RomanceRelationship, error)
	AcceptPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) (*RomanceRelationship, error)
	RejectPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error
	BreakupPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error
	GetRomanceCompatibility(ctx context.Context, playerID, targetID uuid.UUID) (*CompatibilityResult, error)
	UpdateRomancePrivacy(ctx context.Context, playerID uuid.UUID, romanceType string, settings map[string]interface{}) error
	GetRomanceNotifications(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceNotification, int, error)
}

type romanceCoreService struct {
	repo   RomanceCoreRepository
	logger *logrus.Logger
}

func NewRomanceCoreService(repo RomanceCoreRepository) RomanceCoreServiceInterface {
	return &romanceCoreService{
		repo:   repo,
		logger: GetLogger(),
	}
}

func (s *romanceCoreService) GetRomanceTypes(ctx context.Context) ([]string, error) {
	return s.repo.GetRomanceTypes(ctx)
}

func (s *romanceCoreService) GetPlayerRomanceRelationships(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceRelationship, int, error) {
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	return s.repo.GetPlayerRomanceRelationships(ctx, playerID, limit, offset)
}

func (s *romanceCoreService) GetPlayerRomanceRelationshipsByType(ctx context.Context, playerID uuid.UUID, romanceType string, limit, offset int) ([]RomanceRelationship, int, error) {
	validTypes := map[string]bool{
		"player_player":        true,
		"player_npc":           true,
		"player_digital_avatar": true,
	}
	if !validTypes[romanceType] {
		return nil, 0, errors.New("invalid romance type")
	}

	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	return s.repo.GetPlayerRomanceRelationshipsByType(ctx, playerID, romanceType, limit, offset)
}

func (s *romanceCoreService) GetPlayerPlayerRomance(ctx context.Context, playerID1, playerID2 uuid.UUID) (*RomanceRelationship, error) {
	return s.repo.GetPlayerPlayerRomance(ctx, playerID1, playerID2)
}

func (s *romanceCoreService) InitiatePlayerPlayerRomance(ctx context.Context, playerID, targetPlayerID uuid.UUID, message string, privacySettings map[string]interface{}) (*RomanceRelationship, error) {
	if playerID == targetPlayerID {
		return nil, errors.New("cannot initiate romance with yourself")
	}

	// Check if relationship already exists
	existing, err := s.repo.GetPlayerPlayerRomance(ctx, playerID, targetPlayerID)
	if err != nil {
		return nil, err
	}
	if existing != nil {
		return nil, errors.New("romance relationship already exists")
	}

	return s.repo.CreatePlayerPlayerRomance(ctx, playerID, targetPlayerID, message, privacySettings)
}

func (s *romanceCoreService) AcceptPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) (*RomanceRelationship, error) {
	return s.repo.AcceptPlayerPlayerRomance(ctx, relationshipID, playerID)
}

func (s *romanceCoreService) RejectPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error {
	return s.repo.RejectPlayerPlayerRomance(ctx, relationshipID, playerID)
}

func (s *romanceCoreService) BreakupPlayerPlayerRomance(ctx context.Context, relationshipID uuid.UUID, playerID uuid.UUID) error {
	return s.repo.BreakupPlayerPlayerRomance(ctx, relationshipID, playerID)
}

func (s *romanceCoreService) GetRomanceCompatibility(ctx context.Context, playerID, targetID uuid.UUID) (*CompatibilityResult, error) {
	return s.repo.GetRomanceCompatibility(ctx, playerID, targetID)
}

func (s *romanceCoreService) UpdateRomancePrivacy(ctx context.Context, playerID uuid.UUID, romanceType string, settings map[string]interface{}) error {
	validTypes := map[string]bool{
		"player_player":        true,
		"player_npc":           true,
		"player_digital_avatar": true,
	}
	if !validTypes[romanceType] {
		return errors.New("invalid romance type")
	}

	return s.repo.UpdateRomancePrivacy(ctx, playerID, romanceType, settings)
}

func (s *romanceCoreService) GetRomanceNotifications(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]RomanceNotification, int, error) {
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	return s.repo.GetRomanceNotifications(ctx, playerID, limit, offset)
}

