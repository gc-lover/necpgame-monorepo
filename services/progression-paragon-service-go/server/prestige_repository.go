package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type PrestigeRepositoryInterface interface {
	GetPrestigeInfo(ctx context.Context, characterID uuid.UUID) (*PrestigeInfo, error)
	CreatePrestigeInfo(ctx context.Context, info *PrestigeInfo) error
	UpdatePrestigeInfo(ctx context.Context, info *PrestigeInfo) error
	RecordPrestigeReset(ctx context.Context, characterID uuid.UUID, levelBefore, levelAfter int, bonuses map[string]float64) error
}

type PrestigeRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func (r *PrestigeRepository) GetPrestigeInfo(ctx context.Context, characterID uuid.UUID) (*PrestigeInfo, error) {
	var info PrestigeInfo
	var bonusesJSON []byte
	var lastResetAt *time.Time

	err := r.db.QueryRow(ctx,
		`SELECT character_id, prestige_level, reset_count, bonuses_applied,
		        last_reset_at, created_at, updated_at
		 FROM progression.prestige_levels
		 WHERE character_id = $1`,
		characterID,
	).Scan(&info.CharacterID, &info.PrestigeLevel, &info.ResetCount, &bonusesJSON,
		&lastResetAt, &info.UpdatedAt, &info.UpdatedAt)

	if errors.Is(err, pgx.ErrNoRows) {
		info = PrestigeInfo{
			CharacterID:    characterID,
			PrestigeLevel:  0,
			ResetCount:     0,
			BonusesApplied: make(map[string]float64),
			UpdatedAt:      time.Now(),
		}
		return &info, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get prestige info")
		return nil, err
	}

	if bonusesJSON != nil {
		if err := json.Unmarshal(bonusesJSON, &info.BonusesApplied); err != nil {
			r.logger.WithError(err).Warn("Failed to unmarshal bonuses")
			info.BonusesApplied = make(map[string]float64)
		}
	} else {
		info.BonusesApplied = make(map[string]float64)
	}

	if lastResetAt != nil {
		info.LastResetAt = lastResetAt
	}

	return &info, nil
}

func (r *PrestigeRepository) CreatePrestigeInfo(ctx context.Context, info *PrestigeInfo) error {
	bonusesJSON, err := json.Marshal(info.BonusesApplied)
	if err != nil {
		return err
	}

	_, err = r.db.Exec(ctx,
		`INSERT INTO progression.prestige_levels
		 (character_id, prestige_level, reset_count, bonuses_applied, updated_at)
		 VALUES ($1, $2, $3, $4, $5)`,
		info.CharacterID, info.PrestigeLevel, info.ResetCount, bonusesJSON, time.Now(),
	)
	return err
}

func (r *PrestigeRepository) UpdatePrestigeInfo(ctx context.Context, info *PrestigeInfo) error {
	bonusesJSON, err := json.Marshal(info.BonusesApplied)
	if err != nil {
		return err
	}

	_, err = r.db.Exec(ctx,
		`UPDATE progression.prestige_levels
		 SET prestige_level = $1, reset_count = $2, bonuses_applied = $3,
		     last_reset_at = $4, updated_at = $5
		 WHERE character_id = $6`,
		info.PrestigeLevel, info.ResetCount, bonusesJSON, info.LastResetAt, time.Now(), info.CharacterID,
	)
	return err
}

func (r *PrestigeRepository) RecordPrestigeReset(ctx context.Context, characterID uuid.UUID, levelBefore, levelAfter int, bonuses map[string]float64) error {
	bonusesJSON, err := json.Marshal(bonuses)
	if err != nil {
		return err
	}

	_, err = r.db.Exec(ctx,
		`INSERT INTO progression.prestige_resets
		 (character_id, prestige_level_before, prestige_level_after, bonuses_gained)
		 VALUES ($1, $2, $3, $4)`,
		characterID, levelBefore, levelAfter, bonusesJSON,
	)
	return err
}
