// Issue: #141886477
package server

import (
	"context"
	"encoding/json"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/models"
	"github.com/sirupsen/logrus"
)

type ResetRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewResetRepository(db *pgxpool.Pool) *ResetRepository {
	return &ResetRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *ResetRepository) Create(ctx context.Context, record *models.ResetRecord) error {
	metadataJSON, err := json.Marshal(record.Metadata)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal metadata JSON")
		return err
	}

	query := `
		INSERT INTO operations.reset_records (
			id, type, status, started_at, completed_at, error, metadata
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7
		)`

	_, err = r.db.Exec(ctx, query,
		record.ID, record.Type, record.Status, record.StartedAt,
		record.CompletedAt, record.Error, metadataJSON,
	)

	return err
}

func (r *ResetRepository) Update(ctx context.Context, record *models.ResetRecord) error {
	metadataJSON, err := json.Marshal(record.Metadata)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal metadata JSON")
		return err
	}

	query := `
		UPDATE operations.reset_records
		SET status = $1, completed_at = $2, error = $3, metadata = $4
		WHERE id = $5`

	_, err = r.db.Exec(ctx, query,
		record.Status, record.CompletedAt, record.Error, metadataJSON,
		record.ID,
	)

	return err
}

func (r *ResetRepository) GetLastReset(ctx context.Context, resetType models.ResetType) (*models.ResetRecord, error) {
	var record models.ResetRecord
	var metadataJSON []byte

	query := `
		SELECT id, type, status, started_at, completed_at, error, metadata
		FROM operations.reset_records
		WHERE type = $1 AND status = 'completed'
		ORDER BY completed_at DESC
		LIMIT 1`

	err := r.db.QueryRow(ctx, query, resetType).Scan(
		&record.ID, &record.Type, &record.Status, &record.StartedAt,
		&record.CompletedAt, &record.Error, &metadataJSON,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(metadataJSON) > 0 {
		if err := json.Unmarshal(metadataJSON, &record.Metadata); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
			return nil, err
		}
	}

	return &record, nil
}

func (r *ResetRepository) List(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetRecord, error) {
	var query string
	var args []interface{}

	if resetType != nil {
		query = `
			SELECT id, type, status, started_at, completed_at, error, metadata
			FROM operations.reset_records
			WHERE type = $1
			ORDER BY started_at DESC
			LIMIT $2 OFFSET $3`
		args = []interface{}{*resetType, limit, offset}
	} else {
		query = `
			SELECT id, type, status, started_at, completed_at, error, metadata
			FROM operations.reset_records
			ORDER BY started_at DESC
			LIMIT $1 OFFSET $2`
		args = []interface{}{limit, offset}
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var records []models.ResetRecord
	for rows.Next() {
		var record models.ResetRecord
		var metadataJSON []byte

		err := rows.Scan(
			&record.ID, &record.Type, &record.Status, &record.StartedAt,
			&record.CompletedAt, &record.Error, &metadataJSON,
		)
		if err != nil {
			return nil, err
		}

		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &record.Metadata); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
				return nil, err
			}
		}

		records = append(records, record)
	}

	return records, nil
}

func (r *ResetRepository) Count(ctx context.Context, resetType *models.ResetType) (int, error) {
	var count int
	var query string
	var args []interface{}

	if resetType != nil {
		query = `SELECT COUNT(*) FROM operations.reset_records WHERE type = $1`
		args = []interface{}{*resetType}
	} else {
		query = `SELECT COUNT(*) FROM operations.reset_records`
		args = []interface{}{}
	}

	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

