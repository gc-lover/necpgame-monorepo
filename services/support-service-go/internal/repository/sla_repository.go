// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: SLA repository with optimized database queries and connection pooling

package repository

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"support-service-go/internal/models"
)

// SLARepository defines SLA data access methods
type SLARepository interface {
	// SLA Status operations
	CreateSLAStatus(ctx context.Context, status *models.SLAStatus) error
	GetSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.SLAStatus, error)
	UpdateSLAStatus(ctx context.Context, status *models.SLAStatus) error
	DeleteSLAStatus(ctx context.Context, ticketID uuid.UUID) error

	// SLA Violations operations
	CreateSLAViolation(ctx context.Context, violation *models.SLAViolation) error
	GetSLAViolations(ctx context.Context, limit, offset int, priority, violationType *string) ([]models.SLAViolation, int, error)
	GetSLAViolationsByTicket(ctx context.Context, ticketID uuid.UUID) ([]models.SLAViolation, error)

	// SLA Priority operations
	GetSLAPriority(ctx context.Context, priority string) (*models.SLAPriority, error)

	// SLA Analytics
	GetSLAViolationStats(ctx context.Context, days int) (map[string]int, error)
}

// PostgresSLARepository implements SLARepository for PostgreSQL
type PostgresSLARepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewSLARepository creates a new SLA repository instance
func NewSLARepository(db *pgxpool.Pool, logger *zap.Logger) *PostgresSLARepository {
	return &PostgresSLARepository{
		db:     db,
		logger: logger,
	}
}

// CreateSLAStatus creates a new SLA status record
func (r *PostgresSLARepository) CreateSLAStatus(ctx context.Context, status *models.SLAStatus) error {
	query := `
		INSERT INTO support.sla_status (
			ticket_id, priority, first_response_target, first_response_actual,
			resolution_target, resolution_actual, first_response_sla_met,
			resolution_sla_met, time_until_first_response, time_until_resolution,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
		ON CONFLICT (ticket_id) DO UPDATE SET
			first_response_actual = EXCLUDED.first_response_actual,
			resolution_actual = EXCLUDED.resolution_actual,
			first_response_sla_met = EXCLUDED.first_response_sla_met,
			resolution_sla_met = EXCLUDED.resolution_sla_met,
			time_until_first_response = EXCLUDED.time_until_first_response,
			time_until_resolution = EXCLUDED.time_until_resolution,
			updated_at = EXCLUDED.updated_at
	`

	var firstResponseActual, resolutionActual *time.Time
	if status.FirstResponseActual != nil {
		firstResponseActual = status.FirstResponseActual
	}
	if status.ResolutionActual != nil {
		resolutionActual = status.ResolutionActual
	}

	_, err := r.db.Exec(ctx, query,
		status.TicketID, status.Priority, status.FirstResponseTarget, firstResponseActual,
		status.ResolutionTarget, resolutionActual, status.FirstResponseSLAMet,
		status.ResolutionSLAMet, status.TimeUntilFirstResponse, status.TimeUntilResolution,
		status.CreatedAt, status.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create SLA status",
			zap.String("ticket_id", status.TicketID.String()),
			zap.Error(err))
		return fmt.Errorf("failed to create SLA status: %w", err)
	}

	return nil
}

// GetSLAStatus retrieves SLA status for a ticket
func (r *PostgresSLARepository) GetSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.SLAStatus, error) {
	query := `
		SELECT ticket_id, priority, first_response_target, first_response_actual,
			   resolution_target, resolution_actual, first_response_sla_met,
			   resolution_sla_met, time_until_first_response, time_until_resolution,
			   created_at, updated_at
		FROM support.sla_status
		WHERE ticket_id = $1
	`

	var status models.SLAStatus
	var firstResponseActual, resolutionActual sql.NullTime
	var firstResponseSLAMet, resolutionSLAMet sql.NullBool
	var timeUntilFirstResponse, timeUntilResolution sql.NullInt64

	err := r.db.QueryRow(ctx, query, ticketID).Scan(
		&status.TicketID, &status.Priority, &status.FirstResponseTarget, &firstResponseActual,
		&status.ResolutionTarget, &resolutionActual, &firstResponseSLAMet,
		&resolutionSLAMet, &timeUntilFirstResponse, &timeUntilResolution,
		&status.CreatedAt, &status.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("SLA status not found for ticket %s", ticketID)
		}
		r.logger.Error("Failed to get SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get SLA status: %w", err)
	}

	// Convert nullable fields
	if firstResponseActual.Valid {
		status.FirstResponseActual = &firstResponseActual.Time
	}
	if resolutionActual.Valid {
		status.ResolutionActual = &resolutionActual.Time
	}
	if firstResponseSLAMet.Valid {
		status.FirstResponseSLAMet = &firstResponseSLAMet.Bool
	}
	if resolutionSLAMet.Valid {
		status.ResolutionSLAMet = &resolutionSLAMet.Bool
	}
	if timeUntilFirstResponse.Valid {
		val := int(timeUntilFirstResponse.Int64)
		status.TimeUntilFirstResponse = &val
	}
	if timeUntilResolution.Valid {
		val := int(timeUntilResolution.Int64)
		status.TimeUntilResolution = &val
	}

	return &status, nil
}

// UpdateSLAStatus updates an existing SLA status
func (r *PostgresSLARepository) UpdateSLAStatus(ctx context.Context, status *models.SLAStatus) error {
	return r.CreateSLAStatus(ctx, status) // Uses UPSERT
}

// DeleteSLAStatus deletes SLA status for a ticket
func (r *PostgresSLARepository) DeleteSLAStatus(ctx context.Context, ticketID uuid.UUID) error {
	query := `DELETE FROM support.sla_status WHERE ticket_id = $1`

	result, err := r.db.Exec(ctx, query, ticketID)
	if err != nil {
		r.logger.Error("Failed to delete SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return fmt.Errorf("failed to delete SLA status: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("SLA status not found for ticket %s", ticketID)
	}

	return nil
}

// CreateSLAViolation creates a new SLA violation record
func (r *PostgresSLARepository) CreateSLAViolation(ctx context.Context, violation *models.SLAViolation) error {
	query := `
		INSERT INTO support.sla_violations (
			ticket_id, ticket_number, priority, violation_type,
			target_time, actual_time, violation_duration, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	var actualTime *time.Time
	if violation.ActualTime != nil {
		actualTime = violation.ActualTime
	}

	_, err := r.db.Exec(ctx, query,
		violation.TicketID, violation.TicketNumber, violation.Priority,
		violation.ViolationType, violation.TargetTime, actualTime,
		violation.ViolationDuration, violation.CreatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create SLA violation",
			zap.String("ticket_id", violation.TicketID.String()),
			zap.Error(err))
		return fmt.Errorf("failed to create SLA violation: %w", err)
	}

	return nil
}

// GetSLAViolations retrieves SLA violations with pagination and filtering
func (r *PostgresSLARepository) GetSLAViolations(ctx context.Context, limit, offset int, priority, violationType *string) ([]models.SLAViolation, int, error) {
	// Build query with optional filters
	query := `
		SELECT ticket_id, ticket_number, priority, violation_type,
			   target_time, actual_time, violation_duration, created_at
		FROM support.sla_violations
		WHERE 1=1
	`
	args := []interface{}{}
	argCount := 0

	if priority != nil {
		argCount++
		query += fmt.Sprintf(" AND priority = $%d", argCount)
		args = append(args, *priority)
	}

	if violationType != nil {
		argCount++
		query += fmt.Sprintf(" AND violation_type = $%d", argCount)
		args = append(args, *violationType)
	}

	query += " ORDER BY created_at DESC"

	// Add pagination
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to get SLA violations", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get SLA violations: %w", err)
	}
	defer rows.Close()

	var violations []models.SLAViolation
	for rows.Next() {
		var violation models.SLAViolation
		var actualTime sql.NullTime

		err := rows.Scan(
			&violation.TicketID, &violation.TicketNumber, &violation.Priority,
			&violation.ViolationType, &violation.TargetTime, &actualTime,
			&violation.ViolationDuration, &violation.CreatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan SLA violation", zap.Error(err))
			continue
		}

		if actualTime.Valid {
			violation.ActualTime = &actualTime.Time
		}

		violations = append(violations, violation)
	}

	// Get total count
	countQuery := `
		SELECT COUNT(*) FROM support.sla_violations WHERE 1=1
	`
	countArgs := []interface{}{}
	countArgCount := 0

	if priority != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND priority = $%d", countArgCount)
		countArgs = append(countArgs, *priority)
	}

	if violationType != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND violation_type = $%d", countArgCount)
		countArgs = append(countArgs, *violationType)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to get SLA violations count", zap.Error(err))
		return violations, 0, fmt.Errorf("failed to get violations count: %w", err)
	}

	return violations, total, nil
}

// GetSLAViolationsByTicket gets all violations for a specific ticket
func (r *PostgresSLARepository) GetSLAViolationsByTicket(ctx context.Context, ticketID uuid.UUID) ([]models.SLAViolation, error) {
	query := `
		SELECT ticket_id, ticket_number, priority, violation_type,
			   target_time, actual_time, violation_duration, created_at
		FROM support.sla_violations
		WHERE ticket_id = $1
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, ticketID)
	if err != nil {
		r.logger.Error("Failed to get SLA violations by ticket",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get SLA violations by ticket: %w", err)
	}
	defer rows.Close()

	var violations []models.SLAViolation
	for rows.Next() {
		var violation models.SLAViolation
		var actualTime sql.NullTime

		err := rows.Scan(
			&violation.TicketID, &violation.TicketNumber, &violation.Priority,
			&violation.ViolationType, &violation.TargetTime, &actualTime,
			&violation.ViolationDuration, &violation.CreatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan SLA violation", zap.Error(err))
			continue
		}

		if actualTime.Valid {
			violation.ActualTime = &actualTime.Time
		}

		violations = append(violations, violation)
	}

	return violations, nil
}

// GetSLAPriority gets SLA priority configuration
func (r *PostgresSLARepository) GetSLAPriority(ctx context.Context, priority string) (*models.SLAPriority, error) {
	// For now, return from default priorities
	// In production, this could be stored in database
	if slaPriority, exists := models.DefaultSLAPriorities[priority]; exists {
		return &slaPriority, nil
	}

	return nil, fmt.Errorf("SLA priority %s not found", priority)
}

// GetSLAViolationStats gets violation statistics for the last N days
func (r *PostgresSLARepository) GetSLAViolationStats(ctx context.Context, days int) (map[string]int, error) {
	query := `
		SELECT violation_type, COUNT(*) as count
		FROM support.sla_violations
		WHERE created_at >= NOW() - INTERVAL '%d days'
		GROUP BY violation_type
	`

	rows, err := r.db.Query(ctx, fmt.Sprintf(query, days))
	if err != nil {
		r.logger.Error("Failed to get SLA violation stats",
			zap.Int("days", days),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get SLA violation stats: %w", err)
	}
	defer rows.Close()

	stats := make(map[string]int)
	for rows.Next() {
		var violationType string
		var count int

		err := rows.Scan(&violationType, &count)
		if err != nil {
			r.logger.Error("Failed to scan violation stats", zap.Error(err))
			continue
		}

		stats[violationType] = count
	}

	return stats, nil
}

// Issue: #1489 - Support SLA Service: ogen handlers implementation
