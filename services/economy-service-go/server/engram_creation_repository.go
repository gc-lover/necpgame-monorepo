// Package server Issue: #141887883
package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type EngramCreation struct {
	ID                     uuid.UUID              `json:"id"`
	CreationID             uuid.UUID              `json:"creation_id"`
	EngramID               uuid.UUID              `json:"engram_id"`
	CharacterID            uuid.UUID              `json:"character_id"`
	TargetPersonID         *uuid.UUID             `json:"target_person_id,omitempty"`
	ChipTier               int                    `json:"chip_tier"`
	AttitudeType           string                 `json:"attitude_type"`
	CustomAttitudeSettings map[string]interface{} `json:"custom_attitude_settings,omitempty"`
	CreationStage          string                 `json:"creation_stage"`
	DataLossPercent        float64                `json:"data_loss_percent"`
	IsComplete             bool                   `json:"is_complete"`
	CreationCost           float64                `json:"creation_cost"`
	ReputationSnapshot     map[string]interface{} `json:"reputation_snapshot,omitempty"`
	CreatedAt              time.Time              `json:"created_at"`
	UpdatedAt              time.Time              `json:"updated_at"`
	CompletedAt            *time.Time             `json:"completed_at,omitempty"`
}

type EngramCreationRepositoryInterface interface {
	CreateCreationLog(ctx context.Context, creation *EngramCreation) error
	GetCreationLogByCreationID(ctx context.Context, creationID uuid.UUID) (*EngramCreation, error)
	GetCreationLogByEngramID(ctx context.Context, engramID uuid.UUID) (*EngramCreation, error)
	UpdateCreationStage(ctx context.Context, creationID uuid.UUID, stage string, dataLossPercent *float64, isComplete *bool) error
	CompleteCreation(ctx context.Context, creationID uuid.UUID, engramID uuid.UUID) error
}

type EngramCreationRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewEngramCreationRepository(db *pgxpool.Pool) *EngramCreationRepository {
	return &EngramCreationRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *EngramCreationRepository) CreateCreationLog(ctx context.Context, creation *EngramCreation) error {
	customAttitudeJSON, err := json.Marshal(creation.CustomAttitudeSettings)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal custom attitude settings JSON")
		return err
	}
	reputationJSON, err := json.Marshal(creation.ReputationSnapshot)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal reputation snapshot JSON")
		return err
	}

	_, err = r.db.Exec(ctx,
		`INSERT INTO economy.engram_creation_log 
		 (id, creation_id, engram_id, character_id, target_person_id, chip_tier, attitude_type,
		  custom_attitude_settings, creation_stage, data_loss_percent, is_complete, creation_cost,
		  reputation_snapshot, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)`,
		creation.ID, creation.CreationID, creation.EngramID, creation.CharacterID,
		creation.TargetPersonID, creation.ChipTier, creation.AttitudeType,
		customAttitudeJSON, creation.CreationStage, creation.DataLossPercent,
		creation.IsComplete, creation.CreationCost, reputationJSON,
		creation.CreatedAt, creation.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create engram creation log")
		return err
	}

	return nil
}

func (r *EngramCreationRepository) GetCreationLogByCreationID(ctx context.Context, creationID uuid.UUID) (*EngramCreation, error) {
	creation := &EngramCreation{}
	var customAttitudeJSON []byte
	var reputationJSON []byte
	var targetPersonID *uuid.UUID
	var completedAt *time.Time

	err := r.db.QueryRow(ctx,
		`SELECT id, creation_id, engram_id, character_id, target_person_id, chip_tier, attitude_type,
		 custom_attitude_settings, creation_stage, data_loss_percent, is_complete, creation_cost,
		 reputation_snapshot, created_at, updated_at, completed_at
		 FROM economy.engram_creation_log
		 WHERE creation_id = $1`,
		creationID,
	).Scan(
		&creation.ID, &creation.CreationID, &creation.EngramID, &creation.CharacterID,
		&targetPersonID, &creation.ChipTier, &creation.AttitudeType,
		&customAttitudeJSON, &creation.CreationStage, &creation.DataLossPercent,
		&creation.IsComplete, &creation.CreationCost, &reputationJSON,
		&creation.CreatedAt, &creation.UpdatedAt, &completedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get creation log")
		return nil, err
	}

	if customAttitudeJSON != nil {
		if err := json.Unmarshal(customAttitudeJSON, &creation.CustomAttitudeSettings); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal custom attitude settings JSON")
			return nil, err
		}
	}
	if reputationJSON != nil {
		if err := json.Unmarshal(reputationJSON, &creation.ReputationSnapshot); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal reputation snapshot JSON")
			return nil, err
		}
	}
	creation.TargetPersonID = targetPersonID
	creation.CompletedAt = completedAt

	return creation, nil
}

func (r *EngramCreationRepository) GetCreationLogByEngramID(ctx context.Context, engramID uuid.UUID) (*EngramCreation, error) {
	creation := &EngramCreation{}
	var customAttitudeJSON []byte
	var reputationJSON []byte
	var targetPersonID *uuid.UUID
	var completedAt *time.Time

	err := r.db.QueryRow(ctx,
		`SELECT id, creation_id, engram_id, character_id, target_person_id, chip_tier, attitude_type,
		 custom_attitude_settings, creation_stage, data_loss_percent, is_complete, creation_cost,
		 reputation_snapshot, created_at, updated_at, completed_at
		 FROM economy.engram_creation_log
		 WHERE engram_id = $1
		 ORDER BY created_at DESC
		 LIMIT 1`,
		engramID,
	).Scan(
		&creation.ID, &creation.CreationID, &creation.EngramID, &creation.CharacterID,
		&targetPersonID, &creation.ChipTier, &creation.AttitudeType,
		&customAttitudeJSON, &creation.CreationStage, &creation.DataLossPercent,
		&creation.IsComplete, &creation.CreationCost, &reputationJSON,
		&creation.CreatedAt, &creation.UpdatedAt, &completedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get creation log by engram id")
		return nil, err
	}

	if customAttitudeJSON != nil {
		if err := json.Unmarshal(customAttitudeJSON, &creation.CustomAttitudeSettings); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal custom attitude settings JSON")
			return nil, err
		}
	}
	if reputationJSON != nil {
		if err := json.Unmarshal(reputationJSON, &creation.ReputationSnapshot); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal reputation snapshot JSON")
			return nil, err
		}
	}
	creation.TargetPersonID = targetPersonID
	creation.CompletedAt = completedAt

	return creation, nil
}

func (r *EngramCreationRepository) UpdateCreationStage(ctx context.Context, creationID uuid.UUID, stage string, dataLossPercent *float64, isComplete *bool) error {
	query := `UPDATE economy.engram_creation_log SET creation_stage = $1, updated_at = $2`
	args := []interface{}{stage, time.Now()}
	argIndex := 3

	if dataLossPercent != nil {
		query += `, data_loss_percent = $` + strconv.Itoa(argIndex)
		args = append(args, *dataLossPercent)
		argIndex++
	}

	if isComplete != nil {
		query += `, is_complete = $` + strconv.Itoa(argIndex)
		args = append(args, *isComplete)
		argIndex++
	}

	query += ` WHERE creation_id = $` + strconv.Itoa(argIndex)
	args = append(args, creationID)

	_, err := r.db.Exec(ctx, query, args...)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update creation stage")
		return err
	}

	return nil
}

func (r *EngramCreationRepository) CompleteCreation(ctx context.Context, creationID uuid.UUID, engramID uuid.UUID) error {
	now := time.Now()
	_, err := r.db.Exec(ctx,
		`UPDATE economy.engram_creation_log
		 SET creation_stage = 'completed', engram_id = $1, completed_at = $2, updated_at = $3
		 WHERE creation_id = $4`,
		engramID, now, now, creationID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to complete creation")
		return err
	}

	return nil
}
