package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
	"github.com/sirupsen/logrus"
)

type SLARepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewSLARepository(db *pgxpool.Pool) *SLARepository {
	return &SLARepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *SLARepository) GetTicketSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAStatus, error) {
	query := `
		SELECT id, number, priority, created_at, first_response_at, resolved_at, closed_at
		FROM support.support_tickets
		WHERE id = $1`

	var ticketIDResult uuid.UUID
	var number string
	var priority string
	var createdAt time.Time
	var firstResponseAt *time.Time
	var resolvedAt *time.Time
	var closedAt *time.Time

	err := r.db.QueryRow(ctx, query, ticketID).Scan(
		&ticketIDResult, &number, &priority, &createdAt,
		&firstResponseAt, &resolvedAt, &closedAt,
	)
	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).WithField("ticket_id", ticketID).Error("Failed to get ticket for SLA")
		return nil, fmt.Errorf("failed to get ticket for SLA: %w", err)
	}

	firstResponseTarget, resolutionTarget := r.calculateSLATargets(priority, createdAt)
	
	var firstResponseActual *time.Time
	if firstResponseAt != nil {
		firstResponseActual = firstResponseAt
	} else if closedAt != nil {
		firstResponseActual = closedAt
	}

	var resolutionActual *time.Time
	if resolvedAt != nil {
		resolutionActual = resolvedAt
	} else if closedAt != nil {
		resolutionActual = closedAt
	}

	now := time.Now()
	var firstResponseSLAMet *bool
	if firstResponseActual != nil {
		met := !firstResponseActual.After(firstResponseTarget)
		firstResponseSLAMet = &met
	} else {
		met := !now.After(firstResponseTarget)
		firstResponseSLAMet = &met
	}

	var resolutionSLAMet *bool
	if resolutionActual != nil {
		met := !resolutionActual.After(resolutionTarget)
		resolutionSLAMet = &met
	} else {
		met := !now.After(resolutionTarget)
		resolutionSLAMet = &met
	}

	var timeUntilFirstResponseTarget *int
	if now.Before(firstResponseTarget) {
		seconds := int(firstResponseTarget.Sub(now).Seconds())
		timeUntilFirstResponseTarget = &seconds
	}

	var timeUntilResolutionTarget *int
	if now.Before(resolutionTarget) {
		seconds := int(resolutionTarget.Sub(now).Seconds())
		timeUntilResolutionTarget = &seconds
	}

	return &models.TicketSLAStatus{
		TicketID:                    ticketIDResult,
		Priority:                    priority,
		FirstResponseTarget:         firstResponseTarget,
		FirstResponseActual:         firstResponseActual,
		ResolutionTarget:            resolutionTarget,
		ResolutionActual:            resolutionActual,
		FirstResponseSLAMet:         firstResponseSLAMet,
		ResolutionSLAMet:            resolutionSLAMet,
		TimeUntilFirstResponseTarget: timeUntilFirstResponseTarget,
		TimeUntilResolutionTarget:   timeUntilResolutionTarget,
	}, nil
}

func (r *SLARepository) GetSLAViolations(ctx context.Context, priority *string, violationType *string, limit, offset int) ([]models.SLAViolation, int, error) {
	query := `
		SELECT id, number, priority, created_at, first_response_at, resolved_at, closed_at
		FROM support.support_tickets
		WHERE status NOT IN ('closed', 'resolved')`

	args := []interface{}{}
	argIndex := 1

	if priority != nil {
		query += fmt.Sprintf(" AND priority = $%d", argIndex)
		args = append(args, *priority)
		argIndex++
	}

	query += " ORDER BY created_at DESC LIMIT $" + fmt.Sprintf("%d", argIndex) + " OFFSET $" + fmt.Sprintf("%d", argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get tickets for SLA violations")
		return nil, 0, fmt.Errorf("failed to get tickets for SLA violations: %w", err)
	}
	defer rows.Close()

	var violations []models.SLAViolation
	for rows.Next() {
		var ticketID uuid.UUID
		var number string
		var priority string
		var createdAt time.Time
		var firstResponseAt *time.Time
		var resolvedAt *time.Time
		var closedAt *time.Time

		if err := rows.Scan(&ticketID, &number, &priority, &createdAt, &firstResponseAt, &resolvedAt, &closedAt); err != nil {
			r.logger.WithError(err).Error("Failed to scan ticket for SLA violation")
			continue
		}

		firstResponseTarget, resolutionTarget := r.calculateSLATargets(priority, createdAt)

		now := time.Now()

		if violationType == nil || *violationType == "FIRST_RESPONSE" {
			var firstResponseActual *time.Time
			if firstResponseAt != nil {
				firstResponseActual = firstResponseAt
			} else if closedAt != nil {
				firstResponseActual = closedAt
			}

			if firstResponseActual == nil && now.After(firstResponseTarget) {
				violationDuration := int(now.Sub(firstResponseTarget).Seconds())
				violations = append(violations, models.SLAViolation{
					TicketID:                ticketID,
					TicketNumber:            number,
					Priority:                priority,
					ViolationType:           models.SLAViolationTypeFirstResponse,
					TargetTime:              firstResponseTarget,
					ViolationDurationSeconds: &violationDuration,
				})
			} else if firstResponseActual != nil && firstResponseActual.After(firstResponseTarget) {
				violationDuration := int(firstResponseActual.Sub(firstResponseTarget).Seconds())
				violations = append(violations, models.SLAViolation{
					TicketID:                ticketID,
					TicketNumber:            number,
					Priority:                priority,
					ViolationType:           models.SLAViolationTypeFirstResponse,
					TargetTime:              firstResponseTarget,
					ActualTime:              firstResponseActual,
					ViolationDurationSeconds: &violationDuration,
				})
			}
		}

		if violationType == nil || *violationType == "RESOLUTION" {
			var resolutionActual *time.Time
			if resolvedAt != nil {
				resolutionActual = resolvedAt
			} else if closedAt != nil {
				resolutionActual = closedAt
			}

			if resolutionActual == nil && now.After(resolutionTarget) {
				violationDuration := int(now.Sub(resolutionTarget).Seconds())
				violations = append(violations, models.SLAViolation{
					TicketID:                ticketID,
					TicketNumber:            number,
					Priority:                priority,
					ViolationType:           models.SLAViolationTypeResolution,
					TargetTime:              resolutionTarget,
					ViolationDurationSeconds: &violationDuration,
				})
			} else if resolutionActual != nil && resolutionActual.After(resolutionTarget) {
				violationDuration := int(resolutionActual.Sub(resolutionTarget).Seconds())
				violations = append(violations, models.SLAViolation{
					TicketID:                ticketID,
					TicketNumber:            number,
					Priority:                priority,
					ViolationType:           models.SLAViolationTypeResolution,
					TargetTime:              resolutionTarget,
					ActualTime:              resolutionActual,
					ViolationDurationSeconds: &violationDuration,
				})
			}
		}
	}

	if err := rows.Err(); err != nil {
		r.logger.WithError(err).Error("Failed to iterate tickets for SLA violations")
		return nil, 0, fmt.Errorf("failed to iterate tickets for SLA violations: %w", err)
	}

	countQuery := `
		SELECT COUNT(*)
		FROM support.support_tickets
		WHERE status NOT IN ('closed', 'resolved')`

	countArgs := []interface{}{}
	countArgIndex := 1
	if priority != nil {
		countQuery += fmt.Sprintf(" AND priority = $%d", countArgIndex)
		countArgs = append(countArgs, *priority)
		countArgIndex++
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		r.logger.WithError(err).Error("Failed to count SLA violations")
		return nil, 0, fmt.Errorf("failed to count SLA violations: %w", err)
	}

	return violations, total, nil
}

func (r *SLARepository) calculateSLATargets(priority string, createdAt time.Time) (time.Time, time.Time) {
	var firstResponseHours, resolutionHours int

	switch priority {
	case "low":
		firstResponseHours = 24
		resolutionHours = 72
	case "normal":
		firstResponseHours = 12
		resolutionHours = 48
	case "high":
		firstResponseHours = 4
		resolutionHours = 24
	case "urgent":
		firstResponseHours = 1
		resolutionHours = 8
	case "critical":
		firstResponseHours = 0
		resolutionHours = 2
	default:
		firstResponseHours = 12
		resolutionHours = 48
	}

	firstResponseTarget := createdAt.Add(time.Duration(firstResponseHours) * time.Hour)
	resolutionTarget := createdAt.Add(time.Duration(resolutionHours) * time.Hour)

	if priority == "critical" {
		firstResponseTarget = createdAt.Add(15 * time.Minute)
	}

	return firstResponseTarget, resolutionTarget
}

