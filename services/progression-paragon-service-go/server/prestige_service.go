package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type PrestigeServiceInterface interface {
	GetPrestigeInfo(ctx context.Context, characterID uuid.UUID) (*PrestigeInfo, error)
	ResetPrestige(ctx context.Context, characterID uuid.UUID) (*PrestigeInfo, error)
	GetPrestigeBonuses(ctx context.Context, characterID uuid.UUID) (*PrestigeBonuses, error)
}

type PrestigeService struct {
	repo        PrestigeRepositoryInterface
	paragonRepo ParagonRepositoryInterface
	db          *pgxpool.Pool
	cache       *redis.Client
	logger      *logrus.Logger
}

func (s *PrestigeService) GetPrestigeInfo(ctx context.Context, characterID uuid.UUID) (*PrestigeInfo, error) {
	cacheKey := fmt.Sprintf("prestige:info:%s", characterID.String())

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var info PrestigeInfo
		if json.Unmarshal([]byte(cached), &info) == nil {
			return &info, nil
		}
	}

	info, err := s.repo.GetPrestigeInfo(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if info.PrestigeLevel == 0 && info.ResetCount == 0 {
		err = s.repo.CreatePrestigeInfo(ctx, info)
		if err != nil {
			return nil, err
		}
	}

	info.NextPrestigeRequirements = s.calculateNextPrestigeRequirements(info.PrestigeLevel)

	data, _ := json.Marshal(info)
	s.cache.Set(ctx, cacheKey, data, 5*time.Minute)

	return info, nil
}

func (s *PrestigeService) ResetPrestige(ctx context.Context, characterID uuid.UUID) (*PrestigeInfo, error) {
	info, err := s.repo.GetPrestigeInfo(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if info.PrestigeLevel >= 10 {
		return nil, fmt.Errorf("maximum prestige level reached")
	}

	requirements := s.calculateNextPrestigeRequirements(info.PrestigeLevel)
	if !s.checkPrestigeRequirements(ctx, characterID, requirements) {
		return nil, fmt.Errorf("prestige requirements not met")
	}

	levelBefore := info.PrestigeLevel
	info.PrestigeLevel++
	info.ResetCount++
	now := time.Now()
	info.LastResetAt = &now

	bonuses := s.calculatePrestigeBonuses(info.PrestigeLevel)
	info.BonusesApplied = bonuses

	err = s.repo.UpdatePrestigeInfo(ctx, info)
	if err != nil {
		return nil, err
	}

	err = s.repo.RecordPrestigeReset(ctx, characterID, levelBefore, info.PrestigeLevel, bonuses)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to record prestige reset")
	}

	cacheKey := fmt.Sprintf("prestige:info:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	info.NextPrestigeRequirements = s.calculateNextPrestigeRequirements(info.PrestigeLevel)

	return info, nil
}

func (s *PrestigeService) GetPrestigeBonuses(ctx context.Context, characterID uuid.UUID) (*PrestigeBonuses, error) {
	info, err := s.GetPrestigeInfo(ctx, characterID)
	if err != nil {
		return nil, err
	}

	bonuses := &PrestigeBonuses{
		CharacterID:      characterID,
		PrestigeLevel:    info.PrestigeLevel,
		MaxPrestigeLevel: 10,
		AvailableBonuses: s.getAvailableBonuses(info.PrestigeLevel),
	}

	return bonuses, nil
}

func (s *PrestigeService) calculateNextPrestigeRequirements(level int) *PrestigeRequirements {
	return &PrestigeRequirements{
		MinLevel:         50,
		MinParagonLevel:  20 + (level * 5),
		CompletedContent: []string{},
	}
}

func (s *PrestigeService) checkPrestigeRequirements(ctx context.Context, characterID uuid.UUID, requirements *PrestigeRequirements) bool {
	paragonLevels, err := s.paragonRepo.GetParagonLevels(ctx, characterID)
	if err != nil {
		return false
	}

	if paragonLevels.ParagonLevel < requirements.MinParagonLevel {
		return false
	}

	return true
}

func (s *PrestigeService) calculatePrestigeBonuses(level int) map[string]float64 {
	bonuses := make(map[string]float64)

	bonuses["experience_gain"] = 1.0 + (float64(level) * 0.015)
	bonuses["loot_quality"] = 1.0 + (float64(level) * 0.01)
	bonuses["currency_gain"] = 1.0 + (float64(level) * 0.02)

	return bonuses
}

func (s *PrestigeService) getAvailableBonuses(level int) []PrestigeBonusItem {
	bonuses := s.calculatePrestigeBonuses(level)
	var items []PrestigeBonusItem

	for bonusType, value := range bonuses {
		item := PrestigeBonusItem{
			Type:  bonusType,
			Value: value,
		}

		switch bonusType {
		case "experience_gain":
			item.Description = fmt.Sprintf("+%.0f%% к получению опыта", (value-1.0)*100)
		case "loot_quality":
			item.Description = fmt.Sprintf("+%.0f%% к качеству лута", (value-1.0)*100)
		case "currency_gain":
			item.Description = fmt.Sprintf("+%.0f%% к получению валюты", (value-1.0)*100)
		}

		items = append(items, item)
	}

	return items
}
