// Package server Issue: #75
package server

import (
	"context"
	"fmt"
	"strings"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/NECPGAME/character-management-service-go/pkg/api"
)

// Service содержит бизнес-логику character-management-service
type Service struct {
	repo   *Repository
	config *Config
	logger *zap.Logger
}

// NewService создает новый сервис
func NewService(repo *Repository, config *Config, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		config: config,
		logger: logger,
	}
}

// CreateCharacter создает нового персонажа
func (s *Service) CreateCharacter(ctx context.Context, playerID uuid.UUID, req api.CreateCharacterRequest) (*Character, error) {
	s.logger.Info("Creating character",
		zap.String("player_id", playerID.String()),
		zap.String("name", req.Name),
		zap.String("class", req.CharacterClass))

	// Валидация входных данных
	if err := s.validateCreateCharacterRequest(req); err != nil {
		s.logger.Warn("Invalid character creation request", zap.Error(err))
		return nil, err
	}

	// Проверяем лимиты слотов
	slots, err := s.repo.GetPlayerSlots(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get player slots", zap.Error(err))
		return nil, fmt.Errorf("failed to check slots")
	}

	if slots.UsedSlots >= slots.TotalSlots {
		return nil, fmt.Errorf("no available character slots")
	}

	// Создаем персонажа
	character, err := s.repo.CreateCharacter(ctx, playerID, req)
	if err != nil {
		s.logger.Error("Failed to create character", zap.Error(err))
		return nil, err
	}

	// Пересчитываем статистики персонажа
	if err := s.recalculateCharacterStats(ctx, character.ID); err != nil {
		s.logger.Warn("Failed to recalculate stats after creation", zap.Error(err))
		// Не возвращаем ошибку, персонаж создан
	}

	s.logger.Info("Character created successfully",
		zap.String("character_id", character.ID.String()),
		zap.String("player_id", playerID.String()))

	return character, nil
}

// GetPlayerCharacters получает список персонажей игрока
func (s *Service) GetPlayerCharacters(ctx context.Context, playerID uuid.UUID, includeDeleted bool) ([]Character, error) {
	s.logger.Info("Getting player characters",
		zap.String("player_id", playerID.String()),
		zap.Bool("include_deleted", includeDeleted))

	characters, err := s.repo.GetPlayerCharacters(ctx, playerID, includeDeleted)
	if err != nil {
		s.logger.Error("Failed to get player characters", zap.Error(err))
		return nil, err
	}

	return characters, nil
}

// GetCharacter получает персонажа по ID
func (s *Service) GetCharacter(ctx context.Context, characterID uuid.UUID) (*Character, error) {
	s.logger.Info("Getting character", zap.String("character_id", characterID.String()))

	character, err := s.repo.GetCharacter(ctx, characterID)
	if err != nil {
		s.logger.Error("Failed to get character", zap.Error(err))
		return nil, err
	}

	return character, nil
}

// UpdateCharacter обновляет персонажа
func (s *Service) UpdateCharacter(ctx context.Context, characterID uuid.UUID, req api.UpdateCharacterRequest) (*Character, error) {
	s.logger.Info("Updating character", zap.String("character_id", characterID.String()))

	// Валидация входных данных
	if err := s.validateUpdateCharacterRequest(req); err != nil {
		s.logger.Warn("Invalid character update request", zap.Error(err))
		return nil, err
	}

	character, err := s.repo.UpdateCharacter(ctx, characterID, req)
	if err != nil {
		s.logger.Error("Failed to update character", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Character updated successfully", zap.String("character_id", characterID.String()))
	return character, nil
}

// DeleteCharacter удаляет персонажа
func (s *Service) DeleteCharacter(ctx context.Context, characterID uuid.UUID) error {
	s.logger.Info("Deleting character", zap.String("character_id", characterID.String()))

	// Проверяем, что персонаж не в бою (интеграция с gameplay-service)
	// Для простоты пропускаем эту проверку

	if err := s.repo.DeleteCharacter(ctx, characterID); err != nil {
		s.logger.Error("Failed to delete character", zap.Error(err))
		return err
	}

	s.logger.Info("Character deleted successfully", zap.String("character_id", characterID.String()))
	return nil
}

// RestoreCharacter восстанавливает персонажа
func (s *Service) RestoreCharacter(ctx context.Context, characterID uuid.UUID) (*Character, error) {
	s.logger.Info("Restoring character", zap.String("character_id", characterID.String()))

	character, err := s.repo.RestoreCharacter(ctx, characterID)
	if err != nil {
		s.logger.Error("Failed to restore character", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Character restored successfully", zap.String("character_id", characterID.String()))
	return character, nil
}

// SwitchCharacter переключает активного персонажа
func (s *Service) SwitchCharacter(ctx context.Context, playerID, characterID uuid.UUID) error {
	s.logger.Info("Switching character",
		zap.String("player_id", playerID.String()),
		zap.String("character_id", characterID.String()))

	// Проверяем, что персонаж не в бою (интеграция с gameplay-service)
	// Для простоты пропускаем эту проверку

	if err := s.repo.SwitchCharacter(ctx, playerID, characterID); err != nil {
		s.logger.Error("Failed to switch character", zap.Error(err))
		return err
	}

	s.logger.Info("Character switched successfully",
		zap.String("player_id", playerID.String()),
		zap.String("character_id", characterID.String()))

	return nil
}

// GetActiveCharacter получает активного персонажа игрока
func (s *Service) GetActiveCharacter(ctx context.Context, playerID uuid.UUID) (*Character, error) {
	s.logger.Info("Getting active character", zap.String("player_id", playerID.String()))

	activeCharacterID, err := s.repo.GetActiveCharacterID(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get active character ID", zap.Error(err))
		return nil, err
	}

	if activeCharacterID == nil {
		return nil, fmt.Errorf("no active character found")
	}

	character, err := s.repo.GetCharacter(ctx, *activeCharacterID)
	if err != nil {
		s.logger.Error("Failed to get active character", zap.Error(err))
		return nil, err
	}

	return character, nil
}

// GetPlayerSlots получает информацию о слотах персонажей
func (s *Service) GetPlayerSlots(ctx context.Context, playerID uuid.UUID) (*PlayerSlots, error) {
	s.logger.Info("Getting player slots", zap.String("player_id", playerID.String()))

	slots, err := s.repo.GetPlayerSlots(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get player slots", zap.Error(err))
		return nil, err
	}

	return slots, nil
}

// PurchaseSlots покупает дополнительные слоты
func (s *Service) PurchaseSlots(ctx context.Context, playerID uuid.UUID, slotsCount int) error {
	s.logger.Info("Purchasing slots",
		zap.String("player_id", playerID.String()),
		zap.Int("slots_count", slotsCount))

	// Валидация количества слотов
	if slotsCount <= 0 || slotsCount > 5 {
		return fmt.Errorf("invalid slots count: must be between 1 and 5")
	}

	// Здесь должна быть интеграция с economy-service для оплаты
	// Для простоты пропускаем проверку оплаты

	if err := s.repo.PurchaseSlots(ctx, playerID, slotsCount); err != nil {
		s.logger.Error("Failed to purchase slots", zap.Error(err))
		return err
	}

	s.logger.Info("Slots purchased successfully",
		zap.String("player_id", playerID.String()),
		zap.Int("slots_count", slotsCount))

	return nil
}

// RecalculateCharacterStats пересчитывает статистики персонажа
func (s *Service) RecalculateCharacterStats(ctx context.Context, characterID uuid.UUID) (*CharacterStats, error) {
	s.logger.Info("Recalculating character stats", zap.String("character_id", characterID.String()))

	if err := s.recalculateCharacterStats(ctx, characterID); err != nil {
		s.logger.Error("Failed to recalculate character stats", zap.Error(err))
		return nil, err
	}

	// Возвращаем базовую информацию о статах
	// В реальной реализации нужно получить актуальные статы из БД
	return &CharacterStats{
		Health:       100,
		MaxHealth:    100,
		Armor:        0,
		ActionPoints: 10,
		Humanity:     100,
		MaxHumanity:  100,
		Reputation:   0,
		StreetCred:   0,
	}, nil
}

// Вспомогательные методы

func (s *Service) validateCreateCharacterRequest(req api.CreateCharacterRequest) error {
	if strings.TrimSpace(req.Name) == "" {
		return fmt.Errorf("character name is required")
	}

	if len(req.Name) < 3 || len(req.Name) > 50 {
		return fmt.Errorf("character name must be between 3 and 50 characters")
	}

	// Проверяем уникальность имени (в рамках одного игрока)
	// Для простоты пропускаем эту проверку

	validClasses := []string{"solo", "nomad", "corpo", "fixer", "netrunner", "techie", "media", "rockerboy", "edgerunner"}
	if !contains(validClasses, req.CharacterClass) {
		return fmt.Errorf("invalid character class")
	}

	validOrigins := []string{"nomad", "corpo", "street_kid", "edgerunner", "trauma_team"}
	if !contains(validOrigins, req.Origin) {
		return fmt.Errorf("invalid character origin")
	}

	return nil
}

func (s *Service) validateUpdateCharacterRequest(req api.UpdateCharacterRequest) error {
	if req.Name != nil && (len(*req.Name) < 3 || len(*req.Name) > 50) {
		return fmt.Errorf("character name must be between 3 and 50 characters")
	}

	return nil
}

func (s *Service) recalculateCharacterStats(ctx context.Context, characterID uuid.UUID) error {
	// Получаем персонажа
	character, err := s.repo.GetCharacter(ctx, characterID)
	if err != nil {
		return err
	}

	// Пересчитываем статы на основе атрибутов, экипировки и т.д.
	// Для простоты просто обновляем время последнего пересчета

	query := `UPDATE characters SET updated_at = $1 WHERE id = $2`
	_, err = s.repo.db.ExecContext(ctx, query, time.Now(), characterID)
	if err != nil {
		return fmt.Errorf("failed to update character timestamp: %w", err)
	}

	// Публикуем событие обновления статов
	s.repo.publishEvent(ctx, "CharacterStatsUpdated", map[string]interface{}{
		"characterId": characterID.String(),
		"level":       character.Level,
	})

	return nil
}

func contains(slice []string, item api.CreateCharacterRequestOrigin) bool {
	for _, s := range slice {
		if s == item {
			return true
		}
	}
	return false
}

// Дополнительные структуры

type CharacterStats struct {
	Health       int
	MaxHealth    int
	Armor        int
	ActionPoints int
	Humanity     int
	MaxHumanity  int
	Reputation   int
	StreetCred   int
}
