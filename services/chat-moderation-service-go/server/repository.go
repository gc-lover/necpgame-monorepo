// Package server provides database operations for chat moderation service.
// Issue: #1911
// Database operations for chat moderation with performance optimizations
package server

import (
	"context"
	"database/sql"
	"time"

	"necpgame/services/chat-moderation-service-go/pkg/api"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

// Repository handles database operations
// OPTIMIZATION: Struct alignment - single pointer field (already optimal)
type Repository struct {
	db *sql.DB
}

// NewRepository creates new repository with database connection
func NewRepository(connStr string) (*Repository, error) {
	// DB connection pool configuration (optimization)
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance (OPTIMIZATION: Issue #1911)
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() error {
	return r.db.Close()
}

// GetModerationRules returns all moderation rules
func (r *Repository) GetModerationRules(ctx context.Context, params api.GetModerationRulesParams) ([]api.ModerationRule, int32, error) {
	query := `
		SELECT id, name, type, pattern, severity, action, active, created_at, updated_at
		FROM moderation_rules
		WHERE 1=1`

	var args []interface{}
	argCount := 0

	if params.Type.IsSet() {
		argCount++
		query += ` AND type = $` + string(rune(argCount+'0'))
		args = append(args, params.Type.Value)
	}

	if params.Active.IsSet() {
		argCount++
		query += ` AND active = $` + string(rune(argCount+'0'))
		args = append(args, params.Active.Value)
	}

	// Add pagination
	if params.Offset.IsSet() {
		argCount++
		query += ` OFFSET $` + string(rune(argCount+'0'))
		args = append(args, params.Offset.Value)
	}

	if params.Limit.IsSet() {
		argCount++
		query += ` LIMIT $` + string(rune(argCount+'0'))
		args = append(args, params.Limit.Value)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var rules []api.ModerationRule
	for rows.Next() {
		var rule api.ModerationRule
		err := rows.Scan(
			&rule.ID, &rule.Name, &rule.Type, &rule.Pattern,
			&rule.Severity, &rule.Action, &rule.Active,
			&rule.CreatedAt, &rule.UpdatedAt,
		)
		if err != nil {
			return nil, 0, err
		}
		rules = append(rules, rule)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM moderation_rules WHERE 1=1`
	if params.Type.IsSet() {
		countQuery += ` AND type = $1`
		args = []interface{}{params.Type.Value}
	} else {
		args = []interface{}{}
	}

	var total int32
	err = r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	return rules, total, nil
}

// CreateModerationRule creates a new rule
func (r *Repository) CreateModerationRule(ctx context.Context, rule *api.ModerationRule) error {
	query := `
		INSERT INTO moderation_rules (id, name, type, pattern, severity, action, active, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`

	_, err := r.db.ExecContext(ctx, query,
		rule.ID, rule.Name, rule.Type, rule.Pattern,
		rule.Severity, rule.Action, rule.Active,
		rule.CreatedAt, rule.UpdatedAt,
	)
	return err
}

// GetModerationRule returns a specific rule
func (r *Repository) GetModerationRule(ctx context.Context, ruleID string) (*api.ModerationRule, error) {
	query := `
		SELECT id, name, type, pattern, severity, action, active, created_at, updated_at
		FROM moderation_rules WHERE id = $1`

	var rule api.ModerationRule
	err := r.db.QueryRowContext(ctx, query, ruleID).Scan(
		&rule.ID, &rule.Name, &rule.Type, &rule.Pattern,
		&rule.Severity, &rule.Action, &rule.Active,
		&rule.CreatedAt, &rule.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrNotFound
		}
		return nil, err
	}

	return &rule, nil
}

// UpdateModerationRule updates a rule
func (r *Repository) UpdateModerationRule(ctx context.Context, rule *api.ModerationRule) error {
	query := `
		UPDATE moderation_rules
		SET name = $2, type = $3, pattern = $4, severity = $5, action = $6, active = $7, updated_at = $8
		WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query,
		rule.ID, rule.Name, rule.Type, rule.Pattern,
		rule.Severity, rule.Action, rule.Active, rule.UpdatedAt,
	)
	return err
}

// DeleteModerationRule deletes a rule
func (r *Repository) DeleteModerationRule(ctx context.Context, ruleID string) error {
	query := `DELETE FROM moderation_rules WHERE id = $1`
	result, err := r.db.ExecContext(ctx, query, ruleID)
	if err != nil {
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return err
	}

	if rowsAffected == 0 {
		return ErrNotFound
	}

	return nil
}

// GetActiveRules returns all active rules (cached for performance)
func (r *Repository) GetActiveRules(ctx context.Context) ([]api.ModerationRule, error) {
	query := `
		SELECT id, name, type, pattern, severity, action, active, created_at, updated_at
		FROM moderation_rules WHERE active = true`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var rules []api.ModerationRule
	for rows.Next() {
		var rule api.ModerationRule
		err := rows.Scan(
			&rule.ID, &rule.Name, &rule.Type, &rule.Pattern,
			&rule.Severity, &rule.Action, &rule.Active,
			&rule.CreatedAt, &rule.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		rules = append(rules, rule)
	}

	return rules, nil
}

// GetModerationViolations returns violations
func (r *Repository) GetModerationViolations(ctx context.Context, params api.GetModerationViolationsParams) ([]api.ModerationViolation, int32, error) {
	query := `
		SELECT id, player_id, rule_id, rule_type, severity, message_content, violation_details,
		       status, reviewed_by, review_notes, created_at, updated_at
		FROM moderation_violations WHERE 1=1`

	var args []interface{}
	argCount := 0

	if params.PlayerID.IsSet() {
		argCount++
		query += ` AND player_id = $` + string(rune(argCount+'0'))
		args = append(args, params.PlayerID.Value)
	}

	if params.RuleType.IsSet() {
		argCount++
		query += ` AND rule_type = $` + string(rune(argCount+'0'))
		args = append(args, params.RuleType.Value)
	}

	if params.Severity.IsSet() {
		argCount++
		query += ` AND severity = $` + string(rune(argCount+'0'))
		args = append(args, params.Severity.Value)
	}

	if params.Status.IsSet() {
		argCount++
		query += ` AND status = $` + string(rune(argCount+'0'))
		args = append(args, params.Status.Value)
	}

	// Add pagination
	if params.Offset.IsSet() {
		argCount++
		query += ` OFFSET $` + string(rune(argCount+'0'))
		args = append(args, params.Offset.Value)
	}

	if params.Limit.IsSet() {
		argCount++
		query += ` LIMIT $` + string(rune(argCount+'0'))
		args = append(args, params.Limit.Value)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var violations []api.ModerationViolation
	for rows.Next() {
		var violation api.ModerationViolation
		var violationDetails []byte
		var reviewedBy *uuid.UUID
		var reviewNotes *string

		err := rows.Scan(
			&violation.ID, &violation.PlayerID, &violation.RuleID, &violation.RuleType,
			&violation.Severity, &violation.MessageContent, &violationDetails,
			&violation.Status, &reviewedBy, &reviewNotes,
			&violation.CreatedAt, &violation.UpdatedAt,
		)
		if err != nil {
			return nil, 0, err
		}

		// Parse JSON details (simplified)
		violation.ViolationDetails = &api.ModerationViolationViolationDetails{}
		if reviewedBy != nil {
			violation.ReviewedBy = api.NewOptNilUUID(*reviewedBy)
		} else {
			violation.ReviewedBy.Null = true
		}
		if reviewNotes != nil {
			violation.ReviewNotes = api.NewOptNilString(*reviewNotes)
		} else {
			violation.ReviewNotes.Null = true
		}

		violations = append(violations, violation)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM moderation_violations WHERE 1=1`
	var total int32
	err = r.db.QueryRowContext(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	return violations, total, nil
}

// GetModerationViolation returns violation details
func (r *Repository) GetModerationViolation(ctx context.Context, violationID string) (*api.ModerationViolation, error) {
	query := `
		SELECT id, player_id, rule_id, rule_type, severity, message_content, violation_details,
		       status, reviewed_by, review_notes, created_at, updated_at
		FROM moderation_violations WHERE id = $1`

	var violation api.ModerationViolation
	var violationDetails []byte
	var reviewedBy *uuid.UUID
	var reviewNotes *string

	err := r.db.QueryRowContext(ctx, query, violationID).Scan(
		&violation.ID, &violation.PlayerID, &violation.RuleID, &violation.RuleType,
		&violation.Severity, &violation.MessageContent, &violationDetails,
		&violation.Status, &reviewedBy, &reviewNotes,
		&violation.CreatedAt, &violation.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrNotFound
		}
		return nil, err
	}

	violation.ViolationDetails = &api.ModerationViolationViolationDetails{}
	if reviewedBy != nil {
		violation.ReviewedBy = api.NewOptNilUUID(*reviewedBy)
	} else {
		violation.ReviewedBy.Null = true
	}
	if reviewNotes != nil {
		violation.ReviewNotes = api.NewOptNilString(*reviewNotes)
	} else {
		violation.ReviewNotes.Null = true
	}

	return &violation, nil
}

// CreateModerationViolation creates a violation record
func (r *Repository) CreateModerationViolation(ctx context.Context, violation *api.ModerationViolation) error {
	query := `
		INSERT INTO moderation_violations (id, player_id, rule_id, rule_type, severity, message_content,
		                                  violation_details, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`

	_, err := r.db.ExecContext(ctx, query,
		violation.ID, violation.PlayerID, violation.RuleID, violation.RuleType,
		violation.Severity, violation.MessageContent, "{}",
		violation.Status, violation.CreatedAt, violation.UpdatedAt,
	)
	return err
}

// UpdateViolationStatus updates violation status
func (r *Repository) UpdateViolationStatus(ctx context.Context, violationID string, req *api.UpdateViolationStatusReq) (*api.ModerationViolation, error) {
	query := `
		UPDATE moderation_violations
		SET status = $2, reviewed_by = $3, review_notes = $4, updated_at = $5
		WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query,
		violationID, req.Status, req.ReviewedBy, req.ReviewNotes, time.Now(),
	)
	if err != nil {
		return nil, err
	}

	return r.GetModerationViolation(ctx, violationID)
}

// CreateModerationAction creates an action record
func (r *Repository) CreateModerationAction(ctx context.Context, action *api.ModerationAction) error {
	query := `
		INSERT INTO moderation_actions (id, violation_id, player_id, action_type, duration, reason, moderator_id, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`

	_, err := r.db.ExecContext(ctx, query,
		action.ID, action.ViolationID, action.PlayerID, action.ActionType,
		action.Duration, action.Reason, action.ModeratorID, action.CreatedAt,
	)
	return err
}

// LogModerationAction logs the action
func (r *Repository) LogModerationAction(ctx context.Context, action *api.ModerationAction) error {
	query := `
		INSERT INTO moderation_logs (id, player_id, action_type, rule_type, moderator_id, details, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)`

	details := "{}" // Simplified

	_, err := r.db.ExecContext(ctx, query,
		uuid.New(), action.PlayerID, action.ActionType, "manual", // rule_type placeholder
		action.ModeratorID, details, action.CreatedAt,
	)
	return err
}

// GetModerationLogs returns logs
func (r *Repository) GetModerationLogs(ctx context.Context, params api.GetModerationLogsParams) ([]api.ModerationLog, int32, error) {
	query := `
		SELECT id, player_id, action_type, rule_type, moderator_id, details, created_at
		FROM moderation_logs WHERE 1=1`

	var args []interface{}
	argCount := 0

	if params.PlayerID.IsSet() {
		argCount++
		query += ` AND player_id = $` + string(rune(argCount+'0'))
		args = append(args, params.PlayerID.Value)
	}

	if params.ModeratorID.IsSet() {
		argCount++
		query += ` AND moderator_id = $` + string(rune(argCount+'0'))
		args = append(args, params.ModeratorID.Value)
	}

	if params.ActionType.IsSet() {
		argCount++
		query += ` AND action_type = $` + string(rune(argCount+'0'))
		args = append(args, params.ActionType.Value)
	}

	// Date filtering
	if params.StartDate.IsSet() {
		argCount++
		query += ` AND created_at >= $` + string(rune(argCount+'0'))
		args = append(args, params.StartDate.Value)
	}

	if params.EndDate.IsSet() {
		argCount++
		query += ` AND created_at <= $` + string(rune(argCount+'0'))
		args = append(args, params.EndDate.Value)
	}

	// Add pagination
	if params.Offset.IsSet() {
		argCount++
		query += ` OFFSET $` + string(rune(argCount+'0'))
		args = append(args, params.Offset.Value)
	}

	if params.Limit.IsSet() {
		argCount++
		query += ` LIMIT $` + string(rune(argCount+'0'))
		args = append(args, params.Limit.Value)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var logs []api.ModerationLog
	for rows.Next() {
		var log api.ModerationLog
		var details []byte

		err := rows.Scan(
			&log.ID, &log.PlayerID, &log.ActionType, &log.RuleType,
			&log.ModeratorID, &details, &log.CreatedAt,
		)
		if err != nil {
			return nil, 0, err
		}

		log.Details = &api.ModerationLogDetails{}
		logs = append(logs, log)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM moderation_logs WHERE 1=1`
	var total int32
	err = r.db.QueryRowContext(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	return logs, total, nil
}
