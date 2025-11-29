package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

var (
	ErrSlotNotFound      = errors.New("engram slot not found")
	ErrSlotAlreadyOccupied = errors.New("engram slot already occupied")
	ErrInvalidSlotID     = errors.New("invalid slot id (must be 1-3)")
	ErrEngramNotFound    = errors.New("engram not found")
)

type EngramServiceInterface interface {
	GetEngramSlots(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error)
	InstallEngram(ctx context.Context, characterID uuid.UUID, slotID int, engramID uuid.UUID, validateCompatibility bool) (*EngramSlot, error)
	RemoveEngram(ctx context.Context, characterID uuid.UUID, slotID int, removalType string) (*RemoveEngramResult, error)
	GetActiveEngrams(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error)
	GetEngramInfluence(ctx context.Context, characterID uuid.UUID, engramID uuid.UUID) (*EngramInfluenceInfo, error)
	UpdateEngramInfluence(ctx context.Context, characterID uuid.UUID, engramID uuid.UUID, reason string, changeAmount float64, actionData map[string]interface{}) (*EngramInfluenceInfo, error)
	GetEngramInfluenceLevels(ctx context.Context, characterID uuid.UUID) ([]*EngramInfluenceLevel, error)
}

type RemoveEngramResult struct {
	Success      bool
	RemovalRisk  float64
	Penalties    []string
	CooldownUntil *time.Time
}

type EngramInfluenceInfo struct {
	EngramID           uuid.UUID
	InfluenceLevel     float64
	InfluenceCategory  string
	UsagePoints        int
	SlotID             int
	GrowthRate         float64
	BlockerReduction   float64
}

type EngramInfluenceLevel struct {
	EngramID           uuid.UUID
	SlotID             int
	InfluenceLevel     float64
	InfluenceCategory  string
	UsagePoints        int
	DominancePercentage float64
}

type EngramService struct {
	repo     EngramRepositoryInterface
	characterRepo CharacterRepositoryInterface
	cache    *redis.Client
	logger   *logrus.Logger
}

func NewEngramService(repo EngramRepositoryInterface, characterRepo CharacterRepositoryInterface, cache *redis.Client) *EngramService {
	return &EngramService{
		repo:     repo,
		characterRepo: characterRepo,
		cache:    cache,
		logger:   GetLogger(),
	}
}

func (s *EngramService) GetEngramSlots(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error) {
	char, err := s.characterRepo.GetCharacterByID(ctx, characterID)
	if err != nil {
		return nil, err
	}
	if char == nil {
		return nil, errors.New("character not found")
	}

	slots, err := s.repo.GetEngramSlots(ctx, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get engram slots")
		return nil, err
	}

	return slots, nil
}

func (s *EngramService) InstallEngram(ctx context.Context, characterID uuid.UUID, slotID int, engramID uuid.UUID, validateCompatibility bool) (*EngramSlot, error) {
	if slotID < 1 || slotID > 3 {
		return nil, ErrInvalidSlotID
	}

	char, err := s.characterRepo.GetCharacterByID(ctx, characterID)
	if err != nil {
		return nil, err
	}
	if char == nil {
		return nil, errors.New("character not found")
	}

	slot, err := s.repo.GetEngramSlotBySlotID(ctx, characterID, slotID)
	if err != nil {
		return nil, err
	}

	if slot.EngramID != nil {
		return nil, ErrSlotAlreadyOccupied
	}

	err = s.repo.InstallEngram(ctx, slot.ID, engramID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to install engram")
		return nil, err
	}

	updatedSlot, err := s.repo.GetEngramSlotBySlotID(ctx, characterID, slotID)
	if err != nil {
		return nil, err
	}

	s.invalidateEngramCache(ctx, characterID)

	return updatedSlot, nil
}

func (s *EngramService) RemoveEngram(ctx context.Context, characterID uuid.UUID, slotID int, removalType string) (*RemoveEngramResult, error) {
	if slotID < 1 || slotID > 3 {
		return nil, ErrInvalidSlotID
	}

	slot, err := s.repo.GetEngramSlotBySlotID(ctx, characterID, slotID)
	if err != nil {
		return nil, err
	}

	if slot.EngramID == nil {
		return nil, errors.New("no engram installed in slot")
	}

	var removalRisk float64
	var penalties []string
	var cooldownUntil *time.Time

	switch removalType {
	case "safe":
		removalRisk = 5.0
		penalties = []string{"temporary_weakness"}
		cooldown := time.Now().Add(7 * 24 * time.Hour)
		cooldownUntil = &cooldown
	case "emergency":
		removalRisk = 25.0
		penalties = []string{"temporary_weakness", "skill_loss", "neuro_impairment"}
		cooldown := time.Now().Add(14 * 24 * time.Hour)
		cooldownUntil = &cooldown
	case "self":
		removalRisk = 60.0
		penalties = []string{"temporary_weakness", "skill_loss", "neuro_impairment", "trauma"}
		cooldown := time.Now().Add(28 * 24 * time.Hour)
		cooldownUntil = &cooldown
	default:
		removalRisk = 5.0
		penalties = []string{"temporary_weakness"}
		cooldown := time.Now().Add(7 * 24 * time.Hour)
		cooldownUntil = &cooldown
	}

	err = s.repo.RemoveEngram(ctx, slot.ID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove engram")
		return nil, err
	}

	s.invalidateEngramCache(ctx, characterID)

	return &RemoveEngramResult{
		Success:      true,
		RemovalRisk:  removalRisk,
		Penalties:    penalties,
		CooldownUntil: cooldownUntil,
	}, nil
}

func (s *EngramService) GetActiveEngrams(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error) {
	char, err := s.characterRepo.GetCharacterByID(ctx, characterID)
	if err != nil {
		return nil, err
	}
	if char == nil {
		return nil, errors.New("character not found")
	}

	slots, err := s.repo.GetActiveEngrams(ctx, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get active engrams")
		return nil, err
	}

	return slots, nil
}

func (s *EngramService) GetEngramInfluence(ctx context.Context, characterID uuid.UUID, engramID uuid.UUID) (*EngramInfluenceInfo, error) {
	slots, err := s.repo.GetEngramSlots(ctx, characterID)
	if err != nil {
		return nil, err
	}

	var targetSlot *EngramSlot
	for _, slot := range slots {
		if slot.EngramID != nil && *slot.EngramID == engramID {
			targetSlot = slot
			break
		}
	}

	if targetSlot == nil {
		return nil, ErrEngramNotFound
	}

	category := s.getInfluenceCategory(targetSlot.InfluenceLevel)

	return &EngramInfluenceInfo{
		EngramID:          engramID,
		InfluenceLevel:    targetSlot.InfluenceLevel,
		InfluenceCategory: category,
		UsagePoints:       targetSlot.UsagePoints,
		SlotID:            targetSlot.SlotID,
		GrowthRate:        1.0,
		BlockerReduction:  0.0,
	}, nil
}

func (s *EngramService) UpdateEngramInfluence(ctx context.Context, characterID uuid.UUID, engramID uuid.UUID, reason string, changeAmount float64, actionData map[string]interface{}) (*EngramInfluenceInfo, error) {
	slots, err := s.repo.GetEngramSlots(ctx, characterID)
	if err != nil {
		return nil, err
	}

	var targetSlot *EngramSlot
	for _, slot := range slots {
		if slot.EngramID != nil && *slot.EngramID == engramID {
			targetSlot = slot
			break
		}
	}

	if targetSlot == nil {
		return nil, ErrEngramNotFound
	}

	oldInfluence := targetSlot.InfluenceLevel
	newInfluence := oldInfluence + changeAmount

	if newInfluence < 0 {
		newInfluence = 0
	}
	if newInfluence > 100 {
		newInfluence = 100
	}

	err = s.repo.UpdateInfluenceLevel(ctx, targetSlot.ID, newInfluence)
	if err != nil {
		return nil, err
	}

	history := &EngramInfluenceHistory{
		CharacterID:         characterID,
		EngramID:            engramID,
		SlotID:              targetSlot.SlotID,
		InfluenceLevelBefore: oldInfluence,
		InfluenceLevelAfter:  newInfluence,
		ChangeAmount:        changeAmount,
		ChangeReason:        reason,
		ActionData:          actionData,
	}

	err = s.repo.RecordInfluenceChange(ctx, history)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to record influence change history")
	}

	category := s.getInfluenceCategory(newInfluence)

	s.invalidateEngramCache(ctx, characterID)

	return &EngramInfluenceInfo{
		EngramID:          engramID,
		InfluenceLevel:    newInfluence,
		InfluenceCategory: category,
		UsagePoints:       targetSlot.UsagePoints,
		SlotID:            targetSlot.SlotID,
		GrowthRate:        1.0,
		BlockerReduction:  0.0,
	}, nil
}

func (s *EngramService) GetEngramInfluenceLevels(ctx context.Context, characterID uuid.UUID) ([]*EngramInfluenceLevel, error) {
	slots, err := s.repo.GetActiveEngrams(ctx, characterID)
	if err != nil {
		return nil, err
	}

	var levels []*EngramInfluenceLevel
	var totalUsagePoints int

	for _, slot := range slots {
		if slot.EngramID != nil {
			totalUsagePoints += slot.UsagePoints
		}
	}

	for _, slot := range slots {
		if slot.EngramID == nil {
			continue
		}

		category := s.getInfluenceCategory(slot.InfluenceLevel)
		
		dominancePercentage := 0.0
		if totalUsagePoints > 0 {
			dominancePercentage = (float64(slot.UsagePoints) / float64(totalUsagePoints)) * 100.0
		}

		levels = append(levels, &EngramInfluenceLevel{
			EngramID:            *slot.EngramID,
			SlotID:              slot.SlotID,
			InfluenceLevel:      slot.InfluenceLevel,
			InfluenceCategory:   category,
			UsagePoints:         slot.UsagePoints,
			DominancePercentage: dominancePercentage,
		})
	}

	return levels, nil
}

func (s *EngramService) getInfluenceCategory(level float64) string {
	if level < 20 {
		return "low"
	} else if level < 50 {
		return "medium"
	} else if level < 70 {
		return "high"
	} else if level < 90 {
		return "critical"
	} else {
		return "takeover"
	}
}

func (s *EngramService) invalidateEngramCache(ctx context.Context, characterID uuid.UUID) {
	keys := []string{
		"engram:slots:" + characterID.String(),
		"engram:active:" + characterID.String(),
		"engram:influence:" + characterID.String(),
	}

	for _, key := range keys {
		s.cache.Del(ctx, key)
	}
}

