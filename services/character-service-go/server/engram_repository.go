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

type EngramSlot struct {
	ID             uuid.UUID
	CharacterID    uuid.UUID
	SlotID         int
	EngramID       *uuid.UUID
	InstalledAt    *time.Time
	InfluenceLevel float64
	UsagePoints    int
	IsActive       bool
	CreatedAt      time.Time
	UpdatedAt      time.Time
}

type EngramInfluenceHistory struct {
	ID                   uuid.UUID
	CharacterID          uuid.UUID
	EngramID             uuid.UUID
	SlotID               int
	InfluenceLevelBefore float64
	InfluenceLevelAfter  float64
	ChangeAmount         float64
	ChangeReason         string
	ActionData           map[string]interface{}
	CreatedAt            time.Time
}

type EngramRepositoryInterface interface {
	GetEngramSlots(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error)
	GetEngramSlotBySlotID(ctx context.Context, characterID uuid.UUID, slotID int) (*EngramSlot, error)
	CreateEngramSlot(ctx context.Context, characterID uuid.UUID, slotID int) (*EngramSlot, error)
	InstallEngram(ctx context.Context, slotID uuid.UUID, engramID uuid.UUID) error
	RemoveEngram(ctx context.Context, slotID uuid.UUID) error
	UpdateInfluenceLevel(ctx context.Context, slotID uuid.UUID, influenceLevel float64) error
	UpdateUsagePoints(ctx context.Context, slotID uuid.UUID, usagePoints int) error
	RecordInfluenceChange(ctx context.Context, history *EngramInfluenceHistory) error
	GetActiveEngrams(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error)
}

type EngramRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewEngramRepository(db *pgxpool.Pool) *EngramRepository {
	return &EngramRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *EngramRepository) GetEngramSlots(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, character_id, slot_id, engram_id, installed_at, influence_level, usage_points, is_active, created_at, updated_at
		 FROM character.engram_slots
		 WHERE character_id = $1
		 ORDER BY slot_id ASC`,
		characterID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get engram slots")
		return nil, err
	}
	defer rows.Close()

	var slots []*EngramSlot
	for rows.Next() {
		slot := &EngramSlot{}
		var engramID *uuid.UUID
		var installedAt *time.Time

		err := rows.Scan(
			&slot.ID, &slot.CharacterID, &slot.SlotID, &engramID, &installedAt,
			&slot.InfluenceLevel, &slot.UsagePoints, &slot.IsActive, &slot.CreatedAt, &slot.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan engram slot")
			continue
		}

		slot.EngramID = engramID
		slot.InstalledAt = installedAt
		slots = append(slots, slot)
	}

	if len(slots) == 0 {
		for i := 1; i <= 3; i++ {
			slot, err := r.CreateEngramSlot(ctx, characterID, i)
			if err != nil {
				return nil, err
			}
			slots = append(slots, slot)
		}
	}

	return slots, nil
}

func (r *EngramRepository) GetEngramSlotBySlotID(ctx context.Context, characterID uuid.UUID, slotID int) (*EngramSlot, error) {
	slot := &EngramSlot{}
	var engramID *uuid.UUID
	var installedAt *time.Time

	err := r.db.QueryRow(ctx,
		`SELECT id, character_id, slot_id, engram_id, installed_at, influence_level, usage_points, is_active, created_at, updated_at
		 FROM character.engram_slots
		 WHERE character_id = $1 AND slot_id = $2`,
		characterID, slotID,
	).Scan(
		&slot.ID, &slot.CharacterID, &slot.SlotID, &engramID, &installedAt,
		&slot.InfluenceLevel, &slot.UsagePoints, &slot.IsActive, &slot.CreatedAt, &slot.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return r.CreateEngramSlot(ctx, characterID, slotID)
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get engram slot")
		return nil, err
	}

	slot.EngramID = engramID
	slot.InstalledAt = installedAt
	return slot, nil
}

func (r *EngramRepository) CreateEngramSlot(ctx context.Context, characterID uuid.UUID, slotID int) (*EngramSlot, error) {
	slot := &EngramSlot{
		ID:             uuid.New(),
		CharacterID:    characterID,
		SlotID:         slotID,
		InfluenceLevel: 0.0,
		UsagePoints:    0,
		IsActive:       false,
		CreatedAt:      time.Now(),
		UpdatedAt:      time.Now(),
	}

	err := r.db.QueryRow(ctx,
		`INSERT INTO character.engram_slots (id, character_id, slot_id, influence_level, usage_points, is_active, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		 RETURNING id, character_id, slot_id, engram_id, installed_at, influence_level, usage_points, is_active, created_at, updated_at`,
		slot.ID, slot.CharacterID, slot.SlotID, slot.InfluenceLevel, slot.UsagePoints, slot.IsActive, slot.CreatedAt, slot.UpdatedAt,
	).Scan(
		&slot.ID, &slot.CharacterID, &slot.SlotID, &slot.EngramID, &slot.InstalledAt,
		&slot.InfluenceLevel, &slot.UsagePoints, &slot.IsActive, &slot.CreatedAt, &slot.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create engram slot")
		return nil, err
	}

	return slot, nil
}

func (r *EngramRepository) InstallEngram(ctx context.Context, slotID uuid.UUID, engramID uuid.UUID) error {
	now := time.Now()
	_, err := r.db.Exec(ctx,
		`UPDATE character.engram_slots
		 SET engram_id = $1, installed_at = $2, is_active = true, updated_at = $3
		 WHERE id = $4`,
		engramID, now, now, slotID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to install engram")
		return err
	}

	return nil
}

func (r *EngramRepository) RemoveEngram(ctx context.Context, slotID uuid.UUID) error {
	_, err := r.db.Exec(ctx,
		`UPDATE character.engram_slots
		 SET engram_id = NULL, installed_at = NULL, is_active = false, influence_level = 0.0, usage_points = 0, updated_at = $1
		 WHERE id = $2`,
		time.Now(), slotID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to remove engram")
		return err
	}

	return nil
}

func (r *EngramRepository) UpdateInfluenceLevel(ctx context.Context, slotID uuid.UUID, influenceLevel float64) error {
	if influenceLevel < 0 {
		influenceLevel = 0
	}
	if influenceLevel > 100 {
		influenceLevel = 100
	}

	_, err := r.db.Exec(ctx,
		`UPDATE character.engram_slots
		 SET influence_level = $1, updated_at = $2
		 WHERE id = $3`,
		influenceLevel, time.Now(), slotID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update influence level")
		return err
	}

	return nil
}

func (r *EngramRepository) UpdateUsagePoints(ctx context.Context, slotID uuid.UUID, usagePoints int) error {
	if usagePoints < 0 {
		usagePoints = 0
	}

	_, err := r.db.Exec(ctx,
		`UPDATE character.engram_slots
		 SET usage_points = $1, updated_at = $2
		 WHERE id = $3`,
		usagePoints, time.Now(), slotID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update usage points")
		return err
	}

	return nil
}

func (r *EngramRepository) RecordInfluenceChange(ctx context.Context, history *EngramInfluenceHistory) error {
	history.ID = uuid.New()
	history.CreatedAt = time.Now()

	actionDataJSON, _ := json.Marshal(history.ActionData)

	_, err := r.db.Exec(ctx,
		`INSERT INTO character.engram_influence_history 
		 (id, character_id, engram_id, slot_id, influence_level_before, influence_level_after, change_amount, change_reason, action_data, created_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`,
		history.ID, history.CharacterID, history.EngramID, history.SlotID,
		history.InfluenceLevelBefore, history.InfluenceLevelAfter, history.ChangeAmount,
		history.ChangeReason, actionDataJSON, history.CreatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to record influence change")
		return err
	}

	return nil
}

func (r *EngramRepository) GetActiveEngrams(ctx context.Context, characterID uuid.UUID) ([]*EngramSlot, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, character_id, slot_id, engram_id, installed_at, influence_level, usage_points, is_active, created_at, updated_at
		 FROM character.engram_slots
		 WHERE character_id = $1 AND is_active = true AND engram_id IS NOT NULL
		 ORDER BY slot_id ASC`,
		characterID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get active engrams")
		return nil, err
	}
	defer rows.Close()

	var slots []*EngramSlot
	for rows.Next() {
		slot := &EngramSlot{}
		var engramID *uuid.UUID
		var installedAt *time.Time

		err := rows.Scan(
			&slot.ID, &slot.CharacterID, &slot.SlotID, &engramID, &installedAt,
			&slot.InfluenceLevel, &slot.UsagePoints, &slot.IsActive, &slot.CreatedAt, &slot.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan active engram")
			continue
		}

		slot.EngramID = engramID
		slot.InstalledAt = installedAt
		slots = append(slots, slot)
	}

	return slots, nil
}
