package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/sirupsen/logrus"
)

type MovementRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewMovementRepository(db *pgxpool.Pool) *MovementRepository {
	return &MovementRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *MovementRepository) GetPositionByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	var pos models.CharacterPosition
	err := r.db.QueryRow(ctx,
		`SELECT id, character_id, position_x, position_y, position_z, yaw, velocity_x, velocity_y, velocity_z, updated_at, created_at
		 FROM mvp_core.character_positions
		 WHERE character_id = $1 AND deleted_at IS NULL`,
		characterID,
	).Scan(&pos.ID, &pos.CharacterID, &pos.PositionX, &pos.PositionY, &pos.PositionZ, &pos.Yaw,
		&pos.VelocityX, &pos.VelocityY, &pos.VelocityZ, &pos.UpdatedAt, &pos.CreatedAt)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get position")
		return nil, err
	}

	return &pos, nil
}

func (r *MovementRepository) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	now := time.Now()

	var pos models.CharacterPosition
	err := r.db.QueryRow(ctx,
		`INSERT INTO mvp_core.character_positions (character_id, position_x, position_y, position_z, yaw, velocity_x, velocity_y, velocity_z, updated_at, created_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
		 ON CONFLICT (character_id) WHERE deleted_at IS NULL
		 DO UPDATE SET 
		   position_x = EXCLUDED.position_x,
		   position_y = EXCLUDED.position_y,
		   position_z = EXCLUDED.position_z,
		   yaw = EXCLUDED.yaw,
		   velocity_x = EXCLUDED.velocity_x,
		   velocity_y = EXCLUDED.velocity_y,
		   velocity_z = EXCLUDED.velocity_z,
		   updated_at = EXCLUDED.updated_at
		 RETURNING id, character_id, position_x, position_y, position_z, yaw, velocity_x, velocity_y, velocity_z, updated_at, created_at`,
		characterID, req.PositionX, req.PositionY, req.PositionZ, req.Yaw,
		req.VelocityX, req.VelocityY, req.VelocityZ, now, now,
	).Scan(&pos.ID, &pos.CharacterID, &pos.PositionX, &pos.PositionY, &pos.PositionZ, &pos.Yaw,
		&pos.VelocityX, &pos.VelocityY, &pos.VelocityZ, &pos.UpdatedAt, &pos.CreatedAt)

	if err != nil {
		r.logger.WithError(err).Error("Failed to save position")
		return nil, err
	}

	if err := r.SavePositionHistory(ctx, characterID, req); err != nil {
		r.logger.WithError(err).Warn("Failed to save position history")
	}

	return &pos, nil
}

func (r *MovementRepository) SavePositionHistory(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) error {
	_, err := r.db.Exec(ctx,
		`INSERT INTO mvp_core.character_position_history (character_id, position_x, position_y, position_z, yaw, velocity_x, velocity_y, velocity_z, created_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`,
		characterID, req.PositionX, req.PositionY, req.PositionZ, req.Yaw,
		req.VelocityX, req.VelocityY, req.VelocityZ, time.Now(),
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to save position history")
		return err
	}

	return nil
}

func (r *MovementRepository) GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error) {
	if limit <= 0 || limit > 100 {
		limit = 50
	}

	rows, err := r.db.Query(ctx,
		`SELECT id, character_id, position_x, position_y, position_z, yaw, velocity_x, velocity_y, velocity_z, created_at
		 FROM mvp_core.character_position_history
		 WHERE character_id = $1
		 ORDER BY created_at DESC
		 LIMIT $2`,
		characterID, limit,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get position history")
		return nil, err
	}
	defer rows.Close()

	history := make([]models.PositionHistory, 0, limit)
	for rows.Next() {
		var h models.PositionHistory
		err := rows.Scan(&h.ID, &h.CharacterID, &h.PositionX, &h.PositionY, &h.PositionZ, &h.Yaw,
			&h.VelocityX, &h.VelocityY, &h.VelocityZ, &h.CreatedAt)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan position history")
			continue
		}
		history = append(history, h)
	}

	return history, nil
}
