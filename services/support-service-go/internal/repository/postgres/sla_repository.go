package postgres

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/repository"
	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

type slaRepository struct {
	db DBTX
}

// NewSLARepository creates a new PostgreSQL SLA repository
func NewSLARepository(db DBTX) repository.SLARepository {
	return &slaRepository{db: db}
}

func (r *slaRepository) GetSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error) {
	// This would require a dedicated SLA table in a real implementation
	// For now, we'll calculate SLA info on the fly from ticket data

	query := `
		SELECT id, player_id, priority, status, created_at, updated_at, closed_at
		FROM support_tickets WHERE id = $1
	`

	var ticket models.Ticket
	var agentID sql.NullString
	var closedAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, ticketID).Scan(
		&ticket.ID, &ticket.PlayerID, &ticket.Priority, &ticket.Status,
		&ticket.CreatedAt, &ticket.UpdatedAt, &closedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("ticket not found")
		}
		return nil, fmt.Errorf("failed to get ticket for SLA: %w", err)
	}

	if agentID.Valid {
		aid, _ := uuid.Parse(agentID.String)
		ticket.AgentID = &aid
	}

	if closedAt.Valid {
		ticket.ClosedAt = &closedAt.Time
	}

	// Calculate SLA deadlines based on priority
	slaDueDate := r.calculateSLADueDate(ticket.CreatedAt, ticket.Priority)
	responseDeadline := r.calculateResponseDeadline(ticket.CreatedAt, ticket.Priority)
	resolutionDeadline := slaDueDate

	slaInfo := &models.TicketSLAInfo{
		TicketID:           ticketID,
		Priority:           ticket.Priority,
		CreatedAt:          ticket.CreatedAt,
		SLADueDate:         slaDueDate,
		ResponseDeadline:   responseDeadline,
		ResolutionDeadline: resolutionDeadline,
		SLAStatus:          models.SLAStatusCompliant,
	}

	// Get first response time
	firstResponseQuery := `
		SELECT MIN(created_at) FROM ticket_responses
		WHERE ticket_id = $1 AND is_public = true
	`

	var firstResponseTime sql.NullTime
	err = r.db.QueryRowContext(ctx, firstResponseQuery, ticketID).Scan(&firstResponseTime)
	if err != nil && err != sql.ErrNoRows {
		return nil, fmt.Errorf("failed to get first response time: %w", err)
	}

	if firstResponseTime.Valid {
		slaInfo.FirstResponseAt = &firstResponseTime.Time
		timeToFirstResponse := firstResponseTime.Time.Sub(ticket.CreatedAt)
		duration := fmt.Sprintf("PT%dS", int(timeToFirstResponse.Seconds()))
		slaInfo.TimeToFirstResponse = &duration
	}

	if ticket.ClosedAt != nil {
		slaInfo.ResolvedAt = ticket.ClosedAt
		timeToResolution := ticket.ClosedAt.Sub(ticket.CreatedAt)
		duration := fmt.Sprintf("PT%dS", int(timeToResolution.Seconds()))
		slaInfo.TimeToResolution = &duration
	}

	// Determine SLA status
	now := time.Now()
	if ticket.Status != models.TicketStatusClosed {
		if now.After(slaDueDate) {
			slaInfo.SLAStatus = models.SLAStatusBreached
		} else if now.After(slaDueDate.Add(-time.Hour)) {
			slaInfo.SLAStatus = models.SLAStatusWarning
		}
	} else {
		if ticket.ClosedAt != nil && ticket.ClosedAt.After(slaDueDate) {
			slaInfo.SLAStatus = models.SLAStatusBreached
		}
	}

	return slaInfo, nil
}

func (r *slaRepository) UpdateSLAStatus(ctx context.Context, ticketID uuid.UUID, status models.SLAStatus) error {
	query := `UPDATE support_tickets SET sla_status = $1, updated_at = $2 WHERE id = $3`

	_, err := r.db.ExecContext(ctx, query, status, time.Now(), ticketID)
	if err != nil {
		return fmt.Errorf("failed to update SLA status: %w", err)
	}

	return nil
}

func (r *slaRepository) GetSLAStats(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error) {
	// Get basic statistics first
	basicStats, err := NewTicketRepository(r.db).GetStatistics(ctx, periodStart, periodEnd)
	if err != nil {
		return nil, fmt.Errorf("failed to get basic stats: %w", err)
	}

	// Calculate SLA compliance
	slaQuery := `
		SELECT
			COUNT(*) as total_tickets,
			COUNT(CASE WHEN sla_status = 'compliant' THEN 1 END) as compliant_count
		FROM support_tickets
		WHERE created_at BETWEEN $1 AND $2 AND status = 'closed'
	`

	var totalTickets, compliantCount int
	err = r.db.QueryRowContext(ctx, slaQuery, periodStart, periodEnd).Scan(&totalTickets, &compliantCount)
	if err != nil {
		return nil, fmt.Errorf("failed to get SLA stats: %w", err)
	}

	var slaComplianceRate float64
	if totalTickets > 0 {
		slaComplianceRate = float64(compliantCount) / float64(totalTickets) * 100
	}

	basicStats.SLAComplianceRate = slaComplianceRate

	// Calculate average response times
	responseTimeQuery := `
		SELECT AVG(EXTRACT(EPOCH FROM (tr.created_at - t.created_at)))
		FROM ticket_responses tr
		JOIN support_tickets t ON tr.ticket_id = t.id
		WHERE t.created_at BETWEEN $1 AND $2
		AND tr.is_public = true
		AND tr.created_at = (
			SELECT MIN(created_at) FROM ticket_responses
			WHERE ticket_id = tr.ticket_id AND is_public = true
		)
	`

	var avgResponseTime sql.NullFloat64
	err = r.db.QueryRowContext(ctx, responseTimeQuery, periodStart, periodEnd).Scan(&avgResponseTime)
	if err != nil {
		return nil, fmt.Errorf("failed to get average response time: %w", err)
	}

	if avgResponseTime.Valid {
		duration := fmt.Sprintf("PT%dS", int(avgResponseTime.Float64))
		basicStats.AverageFirstResponseTime = duration
	}

	// Calculate average resolution time
	resolutionTimeQuery := `
		SELECT AVG(EXTRACT(EPOCH FROM (closed_at - created_at)))
		FROM support_tickets
		WHERE created_at BETWEEN $1 AND $2 AND status = 'closed'
	`

	var avgResolutionTime sql.NullFloat64
	err = r.db.QueryRowContext(ctx, resolutionTimeQuery, periodStart, periodEnd).Scan(&avgResolutionTime)
	if err != nil {
		return nil, fmt.Errorf("failed to get average resolution time: %w", err)
	}

	if avgResolutionTime.Valid {
		duration := fmt.Sprintf("PT%dS", int(avgResolutionTime.Float64))
		basicStats.AverageResolutionTime = duration
	}

	return basicStats, nil
}

func (r *slaRepository) GetOverdueTickets(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetOverdueSLA(ctx, currentTime)
}

// Helper methods for SLA calculations
func (r *slaRepository) calculateSLADueDate(createdAt time.Time, priority models.TicketPriority) time.Time {
	switch priority {
	case models.TicketPriorityUrgent:
		return createdAt.Add(time.Hour) // 1 hour
	case models.TicketPriorityHigh:
		return createdAt.Add(time.Hour) // 1 hour
	case models.TicketPriorityNormal:
		return createdAt.Add(4 * time.Hour) // 4 hours
	case models.TicketPriorityLow:
		return createdAt.Add(24 * time.Hour) // 24 hours
	default:
		return createdAt.Add(4 * time.Hour) // Default to normal
	}
}

func (r *slaRepository) calculateResponseDeadline(createdAt time.Time, priority models.TicketPriority) time.Time {
	switch priority {
	case models.TicketPriorityUrgent:
		return createdAt.Add(15 * time.Minute) // 15 minutes
	case models.TicketPriorityHigh:
		return createdAt.Add(30 * time.Minute) // 30 minutes
	case models.TicketPriorityNormal:
		return createdAt.Add(time.Hour) // 1 hour
	case models.TicketPriorityLow:
		return createdAt.Add(4 * time.Hour) // 4 hours
	default:
		return createdAt.Add(time.Hour) // Default to normal
	}
}





