package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type ClanWarRepositoryInterface interface {
	CreateWar(ctx context.Context, war *models.ClanWar) error
	GetWarByID(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error)
	ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error)
	UpdateWar(ctx context.Context, war *models.ClanWar) error
	CreateBattle(ctx context.Context, battle *models.WarBattle) error
	GetBattleByID(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error)
	ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error)
	UpdateBattle(ctx context.Context, battle *models.WarBattle) error
	GetTerritoryByID(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error)
	ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error)
	UpdateTerritoryOwner(ctx context.Context, territoryID, ownerGuildID uuid.UUID) error
}

type ClanWarService struct {
	repo   ClanWarRepositoryInterface
	redis  *redis.Client
	logger *logrus.Logger
}

func NewClanWarService(repo *ClanWarRepository, redis *redis.Client, logger *logrus.Logger) *ClanWarService {
	return &ClanWarService{
		repo:   repo,
		redis:  redis,
		logger: logger,
	}
}

func (s *ClanWarService) DeclareWar(ctx context.Context, req *models.DeclareWarRequest) (*models.ClanWar, error) {
	if req.AttackerGuildID == req.DefenderGuildID {
		return nil, fmt.Errorf("attacker and defender cannot be the same guild")
	}

	war := &models.ClanWar{
		ID:              uuid.New(),
		AttackerGuildID: req.AttackerGuildID,
		DefenderGuildID: req.DefenderGuildID,
		Allies:          req.Allies,
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
		TerritoryID:     req.TerritoryID,
		AttackerScore:   0,
		DefenderScore:   0,
		StartTime:       time.Now().Add(24 * time.Hour),
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	if err := s.repo.CreateWar(ctx, war); err != nil {
		return nil, fmt.Errorf("failed to create war: %w", err)
	}

	s.publishEvent(ctx, "clan_war:declared", map[string]interface{}{
		"war_id":            war.ID,
		"attacker_guild_id": war.AttackerGuildID,
		"defender_guild_id": war.DefenderGuildID,
		"territory_id":      war.TerritoryID,
		"start_time":        war.StartTime,
	})

	RecordWarDeclared()

	return war, nil
}

func (s *ClanWarService) GetWar(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error) {
	war, err := s.repo.GetWarByID(ctx, warID)
	if err != nil {
		return nil, fmt.Errorf("failed to get war: %w", err)
	}

	return war, nil
}

func (s *ClanWarService) ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error) {
	wars, total, err := s.repo.ListWars(ctx, guildID, status, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list wars: %w", err)
	}

	return wars, total, nil
}

func (s *ClanWarService) StartWar(ctx context.Context, warID uuid.UUID) error {
	war, err := s.repo.GetWarByID(ctx, warID)
	if err != nil {
		return fmt.Errorf("failed to get war: %w", err)
	}

	if war.Status != models.WarStatusDeclared {
		return fmt.Errorf("war is not in declared status")
	}

	if time.Now().Before(war.StartTime) {
		return fmt.Errorf("war start time has not been reached")
	}

	war.Status = models.WarStatusOngoing
	war.Phase = models.WarPhaseActive
	war.UpdatedAt = time.Now()

	if err := s.repo.UpdateWar(ctx, war); err != nil {
		return fmt.Errorf("failed to update war: %w", err)
	}

	s.publishEvent(ctx, "clan_war:started", map[string]interface{}{
		"war_id": war.ID,
	})

	return nil
}

func (s *ClanWarService) CompleteWar(ctx context.Context, warID uuid.UUID) error {
	war, err := s.repo.GetWarByID(ctx, warID)
	if err != nil {
		return fmt.Errorf("failed to get war: %w", err)
	}

	if war.Status != models.WarStatusOngoing {
		return fmt.Errorf("war is not in ongoing status")
	}

	var winnerGuildID *uuid.UUID
	if war.AttackerScore > war.DefenderScore {
		winnerGuildID = &war.AttackerGuildID
	} else if war.DefenderScore > war.AttackerScore {
		winnerGuildID = &war.DefenderGuildID
	}

	war.Status = models.WarStatusCompleted
	war.Phase = models.WarPhaseCompleted
	war.WinnerGuildID = winnerGuildID
	now := time.Now()
	war.EndTime = &now
	war.UpdatedAt = now

	if err := s.repo.UpdateWar(ctx, war); err != nil {
		return fmt.Errorf("failed to update war: %w", err)
	}

	if war.TerritoryID != nil && winnerGuildID != nil {
		if err := s.repo.UpdateTerritoryOwner(ctx, *war.TerritoryID, *winnerGuildID); err != nil {
			s.logger.WithError(err).Warn("Failed to update territory owner")
		}
	}

	s.publishEvent(ctx, "clan_war:completed", map[string]interface{}{
		"war_id":          war.ID,
		"winner_guild_id": winnerGuildID,
		"attacker_score":  war.AttackerScore,
		"defender_score":  war.DefenderScore,
	})

	RecordWarCompleted()

	return nil
}

func (s *ClanWarService) CreateBattle(ctx context.Context, req *models.CreateBattleRequest) (*models.WarBattle, error) {
	war, err := s.repo.GetWarByID(ctx, req.WarID)
	if err != nil {
		return nil, fmt.Errorf("failed to get war: %w", err)
	}

	if war.Status != models.WarStatusOngoing {
		return nil, fmt.Errorf("war is not in ongoing status")
	}

	battle := &models.WarBattle{
		ID:            uuid.New(),
		WarID:         req.WarID,
		Type:          req.Type,
		TerritoryID:   req.TerritoryID,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     req.StartTime,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	if err := s.repo.CreateBattle(ctx, battle); err != nil {
		return nil, fmt.Errorf("failed to create battle: %w", err)
	}

	s.publishEvent(ctx, "clan_war:battle:created", map[string]interface{}{
		"battle_id":    battle.ID,
		"war_id":       battle.WarID,
		"type":         battle.Type,
		"territory_id": battle.TerritoryID,
		"start_time":   battle.StartTime,
	})

	RecordBattleCreated(string(battle.Type))

	return battle, nil
}

func (s *ClanWarService) GetBattle(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error) {
	battle, err := s.repo.GetBattleByID(ctx, battleID)
	if err != nil {
		return nil, fmt.Errorf("failed to get battle: %w", err)
	}

	return battle, nil
}

func (s *ClanWarService) ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error) {
	battles, total, err := s.repo.ListBattles(ctx, warID, status, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list battles: %w", err)
	}

	return battles, total, nil
}

func (s *ClanWarService) StartBattle(ctx context.Context, battleID uuid.UUID) error {
	battle, err := s.repo.GetBattleByID(ctx, battleID)
	if err != nil {
		return fmt.Errorf("failed to get battle: %w", err)
	}

	if battle.Status != models.BattleStatusScheduled {
		return fmt.Errorf("battle is not in scheduled status")
	}

	if time.Now().Before(battle.StartTime) {
		return fmt.Errorf("battle start time has not been reached")
	}

	battle.Status = models.BattleStatusActive
	battle.UpdatedAt = time.Now()

	if err := s.repo.UpdateBattle(ctx, battle); err != nil {
		return fmt.Errorf("failed to update battle: %w", err)
	}

	s.publishEvent(ctx, "clan_war:battle:started", map[string]interface{}{
		"battle_id": battle.ID,
		"war_id":    battle.WarID,
	})

	return nil
}

func (s *ClanWarService) UpdateBattleScore(ctx context.Context, req *models.UpdateBattleScoreRequest) error {
	battle, err := s.repo.GetBattleByID(ctx, req.BattleID)
	if err != nil {
		return fmt.Errorf("failed to get battle: %w", err)
	}

	if battle.Status != models.BattleStatusActive {
		return fmt.Errorf("battle is not in active status")
	}

	battle.AttackerScore = req.AttackerScore
	battle.DefenderScore = req.DefenderScore
	battle.UpdatedAt = time.Now()

	if err := s.repo.UpdateBattle(ctx, battle); err != nil {
		return fmt.Errorf("failed to update battle: %w", err)
	}

	war, err := s.repo.GetWarByID(ctx, battle.WarID)
	if err == nil {
		war.AttackerScore += req.AttackerScore
		war.DefenderScore += req.DefenderScore
		war.UpdatedAt = time.Now()
		s.repo.UpdateWar(ctx, war)
	}

	s.publishEvent(ctx, "clan_war:battle:score_updated", map[string]interface{}{
		"battle_id":      battle.ID,
		"war_id":         battle.WarID,
		"attacker_score": req.AttackerScore,
		"defender_score": req.DefenderScore,
	})

	return nil
}

func (s *ClanWarService) CompleteBattle(ctx context.Context, battleID uuid.UUID) error {
	battle, err := s.repo.GetBattleByID(ctx, battleID)
	if err != nil {
		return fmt.Errorf("failed to get battle: %w", err)
	}

	if battle.Status != models.BattleStatusActive {
		return fmt.Errorf("battle is not in active status")
	}

	battle.Status = models.BattleStatusCompleted
	now := time.Now()
	battle.EndTime = &now
	battle.UpdatedAt = now

	if err := s.repo.UpdateBattle(ctx, battle); err != nil {
		return fmt.Errorf("failed to update battle: %w", err)
	}

	s.publishEvent(ctx, "clan_war:battle:completed", map[string]interface{}{
		"battle_id":      battle.ID,
		"war_id":         battle.WarID,
		"attacker_score": battle.AttackerScore,
		"defender_score": battle.DefenderScore,
	})

	return nil
}

func (s *ClanWarService) GetTerritory(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error) {
	territory, err := s.repo.GetTerritoryByID(ctx, territoryID)
	if err != nil {
		return nil, fmt.Errorf("failed to get territory: %w", err)
	}

	return territory, nil
}

func (s *ClanWarService) ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error) {
	territories, total, err := s.repo.ListTerritories(ctx, ownerGuildID, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list territories: %w", err)
	}

	return territories, total, nil
}

func (s *ClanWarService) publishEvent(ctx context.Context, eventType string, data map[string]interface{}) {
	event := map[string]interface{}{
		"type":      eventType,
		"timestamp": time.Now().Unix(),
		"data":      data,
	}

	eventJSON, err := json.Marshal(event)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal event")
		return
	}

	if err := s.redis.Publish(ctx, "clan_war:events", eventJSON).Err(); err != nil {
		s.logger.WithError(err).Error("Failed to publish event")
	}
}
