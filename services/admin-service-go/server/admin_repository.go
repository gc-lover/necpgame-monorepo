package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/admin-service-go/models"
	"github.com/sirupsen/logrus"
)

type AdminRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewAdminRepository(db *pgxpool.Pool) *AdminRepository {
	return &AdminRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *AdminRepository) CreateAuditLog(ctx context.Context, log *models.AdminAuditLog) error {
	detailsJSON, err := json.Marshal(log.Details)
	if err != nil {
		r.logger.WithError(err).WithField("admin_id", log.AdminID).Error("Failed to marshal audit log details JSON")
		return fmt.Errorf("failed to marshal audit log details: %w", err)
	}

	query := `
		INSERT INTO admin.admin_audit_log (
			id, admin_id, action_type, target_id, target_type,
			details, ip_address, user_agent, created_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, $7, NOW()
		) RETURNING id, created_at`

	err = r.db.QueryRow(ctx, query,
		log.AdminID, log.ActionType, log.TargetID, log.TargetType,
		detailsJSON, log.IPAddress, log.UserAgent,
	).Scan(&log.ID, &log.CreatedAt)

	return err
}

func (r *AdminRepository) GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error) {
	var log models.AdminAuditLog
	var detailsJSON []byte

	query := `
		SELECT id, admin_id, action_type, target_id, target_type,
		       details, ip_address, user_agent, created_at
		FROM admin.admin_audit_log
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, logID).Scan(
		&log.ID, &log.AdminID, &log.ActionType, &log.TargetID,
		&log.TargetType, &detailsJSON, &log.IPAddress, &log.UserAgent,
		&log.CreatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(detailsJSON) > 0 {
		if err := json.Unmarshal(detailsJSON, &log.Details); err != nil {
			r.logger.WithError(err).WithField("log_id", log.ID).Error("Failed to unmarshal audit log details JSON")
			return nil, fmt.Errorf("failed to unmarshal audit log details: %w", err)
		}
	} else {
		log.Details = make(map[string]interface{})
	}

	return &log, nil
}

func (r *AdminRepository) ListAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) ([]models.AdminAuditLog, error) {
	var args []interface{}

	baseQuery := `
		SELECT id, admin_id, action_type, target_id, target_type,
		       details, ip_address, user_agent, created_at
		FROM admin.admin_audit_log
		WHERE 1=1`

	args = []interface{}{}

	if adminID != nil {
		baseQuery += fmt.Sprintf(" AND admin_id = $%d", len(args)+1)
		args = append(args, *adminID)
	}

	if actionType != nil {
		baseQuery += fmt.Sprintf(" AND action_type = $%d", len(args)+1)
		args = append(args, *actionType)
	}

	baseQuery += fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, baseQuery, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var logs []models.AdminAuditLog
	for rows.Next() {
		var log models.AdminAuditLog
		var detailsJSON []byte

		err := rows.Scan(
			&log.ID, &log.AdminID, &log.ActionType, &log.TargetID,
			&log.TargetType, &detailsJSON, &log.IPAddress, &log.UserAgent,
			&log.CreatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(detailsJSON) > 0 {
			if err := json.Unmarshal(detailsJSON, &log.Details); err != nil {
				r.logger.WithError(err).WithField("log_id", log.ID).Error("Failed to unmarshal audit log details JSON")
				return nil, fmt.Errorf("failed to unmarshal audit log details for log %s: %w", log.ID, err)
			}
		} else {
			log.Details = make(map[string]interface{})
		}

		logs = append(logs, log)
	}

	return logs, nil
}

func (r *AdminRepository) CountAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType) (int, error) {
	var args []interface{}

	baseQuery := `SELECT COUNT(*) FROM admin.admin_audit_log WHERE 1=1`
	args = []interface{}{}

	if adminID != nil {
		baseQuery += fmt.Sprintf(" AND admin_id = $%d", len(args)+1)
		args = append(args, *adminID)
	}

	if actionType != nil {
		baseQuery += fmt.Sprintf(" AND action_type = $%d", len(args)+1)
		args = append(args, *actionType)
	}

	var count int
	err := r.db.QueryRow(ctx, baseQuery, args...).Scan(&count)
	return count, err
}

