package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type ModerationRepositoryInterface interface {
	CreateBan(ctx context.Context, ban *models.ChatBan) error
	GetActiveBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error)
	GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) ([]models.ChatBan, int, error)
	DeactivateBan(ctx context.Context, banID uuid.UUID) error
	CreateReport(ctx context.Context, report *models.ChatReport) error
	GetReportByID(ctx context.Context, reportID uuid.UUID) (*models.ChatReport, error)
	GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error)
	UpdateReportStatus(ctx context.Context, reportID uuid.UUID, status string, adminID *uuid.UUID) error
}

type ModerationRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewModerationRepository(db *pgxpool.Pool) *ModerationRepository {
	return &ModerationRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *ModerationRepository) CreateBan(ctx context.Context, ban *models.ChatBan) error {
	err := r.db.QueryRow(ctx,
		`INSERT INTO social.chat_bans 
		 (id, character_id, channel_id, channel_type, reason, admin_id, expires_at, created_at, is_active)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		 RETURNING id, character_id, channel_id, channel_type, reason, admin_id, expires_at, created_at, is_active`,
		ban.ID, ban.CharacterID, ban.ChannelID, ban.ChannelType, ban.Reason,
		ban.AdminID, ban.ExpiresAt, ban.CreatedAt, ban.IsActive,
	).Scan(&ban.ID, &ban.CharacterID, &ban.ChannelID, &ban.ChannelType, &ban.Reason,
		&ban.AdminID, &ban.ExpiresAt, &ban.CreatedAt, &ban.IsActive)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create chat ban")
		return err
	}

	return nil
}

func (r *ModerationRepository) GetActiveBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error) {
	var ban models.ChatBan
	var channelIDPtr *uuid.UUID
	var channelType *models.ChannelType
	var adminID *uuid.UUID
	var expiresAt *time.Time

	query := `SELECT id, character_id, channel_id, channel_type, reason, admin_id, expires_at, created_at, is_active
			  FROM social.chat_bans
			  WHERE character_id = $1 AND is_active = true
			    AND (expires_at IS NULL OR expires_at > NOW())`
	args := []interface{}{characterID}

	if channelID != nil {
		query += ` AND (channel_id = $2 OR channel_id IS NULL)`
		args = append(args, *channelID)
	} else {
		query += ` AND channel_id IS NULL`
	}

	err := r.db.QueryRow(ctx, query, args...).Scan(
		&ban.ID, &ban.CharacterID, &channelIDPtr, &channelType, &ban.Reason,
		&adminID, &expiresAt, &ban.CreatedAt, &ban.IsActive,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get active ban")
		return nil, err
	}

	ban.ChannelID = channelIDPtr
	ban.ChannelType = channelType
	ban.AdminID = adminID
	ban.ExpiresAt = expiresAt

	return &ban, nil
}

func (r *ModerationRepository) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) ([]models.ChatBan, int, error) {
	var rows pgx.Rows
	var err error
	var count int

	if characterID != nil {
		err = r.db.QueryRow(ctx,
			`SELECT COUNT(*) FROM social.chat_bans WHERE character_id = $1`,
			*characterID,
		).Scan(&count)

		if err != nil {
			r.logger.WithError(err).Error("Failed to count bans")
			return nil, 0, err
		}

		rows, err = r.db.Query(ctx,
			`SELECT id, character_id, channel_id, channel_type, reason, admin_id, expires_at, created_at, is_active
			 FROM social.chat_bans
			 WHERE character_id = $1
			 ORDER BY created_at DESC
			 LIMIT $2 OFFSET $3`,
			*characterID, limit, offset,
		)
	} else {
		err = r.db.QueryRow(ctx,
			`SELECT COUNT(*) FROM social.chat_bans`,
		).Scan(&count)

		if err != nil {
			r.logger.WithError(err).Error("Failed to count bans")
			return nil, 0, err
		}

		rows, err = r.db.Query(ctx,
			`SELECT id, character_id, channel_id, channel_type, reason, admin_id, expires_at, created_at, is_active
			 FROM social.chat_bans
			 ORDER BY created_at DESC
			 LIMIT $1 OFFSET $2`,
			limit, offset,
		)
	}

	if err != nil {
		r.logger.WithError(err).Error("Failed to get bans")
		return nil, 0, err
	}
	defer rows.Close()

	var bans []models.ChatBan
	for rows.Next() {
		var ban models.ChatBan
		var channelIDPtr *uuid.UUID
		var channelType *models.ChannelType
		var adminID *uuid.UUID
		var expiresAt *time.Time

		err := rows.Scan(&ban.ID, &ban.CharacterID, &channelIDPtr, &channelType, &ban.Reason,
			&adminID, &expiresAt, &ban.CreatedAt, &ban.IsActive)

		if err != nil {
			r.logger.WithError(err).Error("Failed to scan chat ban")
			continue
		}

		ban.ChannelID = channelIDPtr
		ban.ChannelType = channelType
		ban.AdminID = adminID
		ban.ExpiresAt = expiresAt

		bans = append(bans, ban)
	}

	return bans, count, nil
}

func (r *ModerationRepository) DeactivateBan(ctx context.Context, banID uuid.UUID) error {
	_, err := r.db.Exec(ctx,
		`UPDATE social.chat_bans SET is_active = false WHERE id = $1`,
		banID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to deactivate ban")
		return err
	}

	return nil
}

func (r *ModerationRepository) CreateReport(ctx context.Context, report *models.ChatReport) error {
	err := r.db.QueryRow(ctx,
		`INSERT INTO social.chat_reports 
		 (id, reporter_id, reported_id, message_id, channel_id, reason, status, created_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		 RETURNING id, reporter_id, reported_id, message_id, channel_id, reason, status, created_at`,
		report.ID, report.ReporterID, report.ReportedID, report.MessageID, report.ChannelID,
		report.Reason, report.Status, report.CreatedAt,
	).Scan(&report.ID, &report.ReporterID, &report.ReportedID, &report.MessageID, &report.ChannelID,
		&report.Reason, &report.Status, &report.CreatedAt)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create chat report")
		return err
	}

	return nil
}

func (r *ModerationRepository) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	var rows pgx.Rows
	var err error
	var count int

	if status != nil {
		err = r.db.QueryRow(ctx,
			`SELECT COUNT(*) FROM social.chat_reports WHERE status = $1`,
			*status,
		).Scan(&count)

		if err != nil {
			r.logger.WithError(err).Error("Failed to count reports")
			return nil, 0, err
		}

		rows, err = r.db.Query(ctx,
			`SELECT id, reporter_id, reported_id, message_id, channel_id, reason, status, admin_id, created_at, resolved_at
			 FROM social.chat_reports
			 WHERE status = $1
			 ORDER BY created_at DESC
			 LIMIT $2 OFFSET $3`,
			*status, limit, offset,
		)
	} else {
		err = r.db.QueryRow(ctx,
			`SELECT COUNT(*) FROM social.chat_reports`,
		).Scan(&count)

		if err != nil {
			r.logger.WithError(err).Error("Failed to count reports")
			return nil, 0, err
		}

		rows, err = r.db.Query(ctx,
			`SELECT id, reporter_id, reported_id, message_id, channel_id, reason, status, admin_id, created_at, resolved_at
			 FROM social.chat_reports
			 ORDER BY created_at DESC
			 LIMIT $1 OFFSET $2`,
			limit, offset,
		)
	}

	if err != nil {
		r.logger.WithError(err).Error("Failed to get reports")
		return nil, 0, err
	}
	defer rows.Close()

	var reports []models.ChatReport
	for rows.Next() {
		var report models.ChatReport
		var messageID *uuid.UUID
		var channelID *uuid.UUID
		var adminID *uuid.UUID
		var resolvedAt *time.Time

		err := rows.Scan(&report.ID, &report.ReporterID, &report.ReportedID, &messageID, &channelID,
			&report.Reason, &report.Status, &adminID, &report.CreatedAt, &resolvedAt)

		if err != nil {
			r.logger.WithError(err).Error("Failed to scan chat report")
			continue
		}

		report.MessageID = messageID
		report.ChannelID = channelID
		report.AdminID = adminID
		report.ResolvedAt = resolvedAt

		reports = append(reports, report)
	}

	return reports, count, nil
}

func (r *ModerationRepository) GetReportByID(ctx context.Context, reportID uuid.UUID) (*models.ChatReport, error) {
	var report models.ChatReport
	var messageID, channelID, adminID *uuid.UUID
	var resolvedAt *time.Time

	err := r.db.QueryRow(ctx,
		`SELECT id, reporter_id, reported_id, message_id, channel_id, reason, status, admin_id, created_at, resolved_at
		 FROM social.chat_reports
		 WHERE id = $1`,
		reportID,
	).Scan(&report.ID, &report.ReporterID, &report.ReportedID, &messageID, &channelID,
		&report.Reason, &report.Status, &adminID, &report.CreatedAt, &resolvedAt)

	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).Error("Failed to get chat report by ID")
		return nil, err
	}

	report.MessageID = messageID
	report.ChannelID = channelID
	report.AdminID = adminID
	report.ResolvedAt = resolvedAt

	return &report, nil
}

func (r *ModerationRepository) UpdateReportStatus(ctx context.Context, reportID uuid.UUID, status string, adminID *uuid.UUID) error {
	resolvedAt := time.Now()
	_, err := r.db.Exec(ctx,
		`UPDATE social.chat_reports 
		 SET status = $1, admin_id = $2, resolved_at = $3
		 WHERE id = $4`,
		status, adminID, resolvedAt, reportID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update report status")
		return err
	}

	return nil
}

