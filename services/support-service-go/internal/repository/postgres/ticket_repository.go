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

type ticketRepository struct {
	db DBTX
}

// NewTicketRepository creates a new PostgreSQL ticket repository
func NewTicketRepository(db DBTX) repository.TicketRepository {
	return &ticketRepository{db: db}
}

func (r *ticketRepository) Create(ctx context.Context, ticket *models.Ticket) error {
	query := `
		INSERT INTO support_tickets (
			id, character_id, title, description, category, priority,
			status, agent_id, created_at, updated_at, closed_at, sla_status
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	now := time.Now()
	ticket.CreatedAt = now
	ticket.UpdatedAt = now
	ticket.SLAStatus = models.SLAStatusCompliant // Default to compliant

	_, err := r.db.ExecContext(ctx, query,
		ticket.ID, ticket.CharacterID, ticket.Title, ticket.Description,
		ticket.Category, ticket.Priority, ticket.Status, ticket.AgentID,
		ticket.CreatedAt, ticket.UpdatedAt, ticket.ClosedAt, ticket.SLAStatus,
	)

	if err != nil {
		return fmt.Errorf("failed to create ticket: %w", err)
	}

	return nil
}

func (r *ticketRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets WHERE id = $1
	`

	ticket := &models.Ticket{}
	var agentID sql.NullString
	var closedAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&ticket.ID, &ticket.CharacterID, &ticket.Title, &ticket.Description,
		&ticket.Category, &ticket.Priority, &ticket.Status, &agentID,
		&ticket.CreatedAt, &ticket.UpdatedAt, &closedAt, &ticket.SLAStatus,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("ticket not found")
		}
		return nil, fmt.Errorf("failed to get ticket: %w", err)
	}

	if agentID.Valid {
		aid, _ := uuid.Parse(agentID.String)
		ticket.AgentID = &aid
	}

	if closedAt.Valid {
		ticket.ClosedAt = &closedAt.Time
	}

	return ticket, nil
}

func (r *ticketRepository) Update(ctx context.Context, ticket *models.Ticket) error {
	query := `
		UPDATE support_tickets SET
			title = $1, description = $2, category = $3, priority = $4,
			status = $5, agent_id = $6, updated_at = $7, closed_at = $8, sla_status = $9
		WHERE id = $10
	`

	ticket.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		ticket.Title, ticket.Description, ticket.Category, ticket.Priority,
		ticket.Status, ticket.AgentID, ticket.UpdatedAt, ticket.ClosedAt,
		ticket.SLAStatus, ticket.ID,
	)

	if err != nil {
		return fmt.Errorf("failed to update ticket: %w", err)
	}

	return nil
}

func (r *ticketRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.TicketStatus, agentID *uuid.UUID) error {
	query := `
		UPDATE support_tickets SET
			status = $1, agent_id = $2, updated_at = $3,
			closed_at = CASE WHEN $1 = 'closed' THEN $3 ELSE closed_at END
		WHERE id = $4
	`

	now := time.Now()

	_, err := r.db.ExecContext(ctx, query, status, agentID, now, id)
	if err != nil {
		return fmt.Errorf("failed to update ticket status: %w", err)
	}

	return nil
}

func (r *ticketRepository) AssignAgent(ctx context.Context, id uuid.UUID, agentID uuid.UUID) error {
	query := `
		UPDATE support_tickets SET
			agent_id = $1, status = 'in_progress', updated_at = $2
		WHERE id = $3 AND status IN ('open', 'pending')
	`

	now := time.Now()

	result, err := r.db.ExecContext(ctx, query, agentID, now, id)
	if err != nil {
		return fmt.Errorf("failed to assign agent: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("ticket not found or cannot be assigned")
	}

	return nil
}

func (r *ticketRepository) Close(ctx context.Context, id uuid.UUID, resolution string) error {
	query := `
		UPDATE support_tickets SET
			status = 'closed', closed_at = $1, updated_at = $1
		WHERE id = $2 AND status != 'closed'
	`

	now := time.Now()

	result, err := r.db.ExecContext(ctx, query, now, id)
	if err != nil {
		return fmt.Errorf("failed to close ticket: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("ticket not found or already closed")
	}

	// Add resolution as a response
	response := &models.TicketResponse{
		ID:         uuid.New(),
		TicketID:   id,
		AuthorID:   uuid.Nil,
		AuthorType: models.AuthorTypeSystem,
		Content:    fmt.Sprintf("Ticket closed with resolution: %s", resolution),
		IsPublic:   true,
		CreatedAt:  now,
	}

	responseRepo := NewTicketResponseRepository(r.db)
	return responseRepo.CreateResponse(ctx, response)
}

func (r *ticketRepository) GetByCharacterID(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE character_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	return r.queryTickets(ctx, query, characterID, limit, offset)
}

func (r *ticketRepository) GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE agent_id = $1
		ORDER BY updated_at DESC
		LIMIT $2 OFFSET $3
	`

	return r.queryTickets(ctx, query, agentID, limit, offset)
}

func (r *ticketRepository) GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE status = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	return r.queryTickets(ctx, query, status, limit, offset)
}

func (r *ticketRepository) GetByCategory(ctx context.Context, category string, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE category = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	return r.queryTickets(ctx, query, category, limit, offset)
}

func (r *ticketRepository) GetByPriority(ctx context.Context, priority models.TicketPriority, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE priority = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	return r.queryTickets(ctx, query, priority, limit, offset)
}

func (r *ticketRepository) GetQueue(ctx context.Context, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE status IN ('open', 'pending')
		ORDER BY
			CASE priority
				WHEN 'urgent' THEN 1
				WHEN 'high' THEN 2
				WHEN 'normal' THEN 3
				WHEN 'low' THEN 4
			END,
			created_at ASC
		LIMIT $1 OFFSET $2
	`

	return r.queryTickets(ctx, query, limit, offset)
}

func (r *ticketRepository) GetUnassigned(ctx context.Context, limit, offset int) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE agent_id IS NULL AND status IN ('open', 'pending')
		ORDER BY
			CASE priority
				WHEN 'urgent' THEN 1
				WHEN 'high' THEN 2
				WHEN 'normal' THEN 3
				WHEN 'low' THEN 4
			END,
			created_at ASC
		LIMIT $1 OFFSET $2
	`

	return r.queryTickets(ctx, query, limit, offset)
}

func (r *ticketRepository) GetOverdueSLA(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error) {
	query := `
		SELECT id, character_id, title, description, category, priority,
			   status, agent_id, created_at, updated_at, closed_at, sla_status
		FROM support_tickets
		WHERE status != 'closed'
		AND (
			(created_at + INTERVAL '1 hour' < $1 AND priority IN ('urgent', 'high')) OR
			(created_at + INTERVAL '4 hours' < $1 AND priority = 'normal') OR
			(created_at + INTERVAL '24 hours' < $1 AND priority = 'low')
		)
		ORDER BY created_at ASC
	`

	return r.queryTickets(ctx, query, currentTime)
}

func (r *ticketRepository) GetStatistics(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error) {
	// This is a simplified implementation
	// In a real system, this would involve complex aggregations
	stats := &models.SupportStatsResponse{
		Period:                fmt.Sprintf("%s to %s", periodStart.Format("2006-01-02"), periodEnd.Format("2006-01-02")),
		TotalTickets:          0,
		ResolvedTickets:       0,
		AverageResolutionTime: "PT0S",
		AverageFirstResponseTime: "PT0S",
		SLAComplianceRate:     0.0,
		TicketsByStatus:       make(map[string]int),
		TicketsByPriority:     make(map[string]int),
		TicketsByCategory:     make(map[string]int),
		AgentPerformance:      []models.AgentPerformance{},
	}

	// Count tickets by status
	statusQuery := `
		SELECT status, COUNT(*) as count
		FROM support_tickets
		WHERE created_at BETWEEN $1 AND $2
		GROUP BY status
	`

	rows, err := r.db.QueryContext(ctx, statusQuery, periodStart, periodEnd)
	if err != nil {
		return nil, fmt.Errorf("failed to get status statistics: %w", err)
	}
	defer rows.Close()

	for rows.Next() {
		var status string
		var count int
		if err := rows.Scan(&status, &count); err != nil {
			return nil, fmt.Errorf("failed to scan status stats: %w", err)
		}
		stats.TicketsByStatus[status] = count
		stats.TotalTickets += count
		if status == "closed" {
			stats.ResolvedTickets = count
		}
	}

	// Count tickets by priority
	priorityQuery := `
		SELECT priority, COUNT(*) as count
		FROM support_tickets
		WHERE created_at BETWEEN $1 AND $2
		GROUP BY priority
	`

	rows, err = r.db.QueryContext(ctx, priorityQuery, periodStart, periodEnd)
	if err != nil {
		return nil, fmt.Errorf("failed to get priority statistics: %w", err)
	}
	defer rows.Close()

	for rows.Next() {
		var priority string
		var count int
		if err := rows.Scan(&priority, &count); err != nil {
			return nil, fmt.Errorf("failed to scan priority stats: %w", err)
		}
		stats.TicketsByPriority[priority] = count
	}

	// Count tickets by category
	categoryQuery := `
		SELECT category, COUNT(*) as count
		FROM support_tickets
		WHERE created_at BETWEEN $1 AND $2
		GROUP BY category
	`

	rows, err = r.db.QueryContext(ctx, categoryQuery, periodStart, periodEnd)
	if err != nil {
		return nil, fmt.Errorf("failed to get category statistics: %w", err)
	}
	defer rows.Close()

	for rows.Next() {
		var category string
		var count int
		if err := rows.Scan(&category, &count); err != nil {
			return nil, fmt.Errorf("failed to scan category stats: %w", err)
		}
		stats.TicketsByCategory[category] = count
	}

	return stats, nil
}

// Helper method to execute ticket queries
func (r *ticketRepository) queryTickets(ctx context.Context, query string, args ...interface{}) ([]*models.Ticket, error) {
	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query tickets: %w", err)
	}
	defer rows.Close()

	var tickets []*models.Ticket
	for rows.Next() {
		ticket := &models.Ticket{}
		var agentID sql.NullString
		var closedAt sql.NullTime

		err := rows.Scan(
			&ticket.ID, &ticket.CharacterID, &ticket.Title, &ticket.Description,
			&ticket.Category, &ticket.Priority, &ticket.Status, &agentID,
			&ticket.CreatedAt, &ticket.UpdatedAt, &closedAt, &ticket.SLAStatus,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan ticket: %w", err)
		}

		if agentID.Valid {
			aid, _ := uuid.Parse(agentID.String)
			ticket.AgentID = &aid
		}

		if closedAt.Valid {
			ticket.ClosedAt = &closedAt.Time
		}

		tickets = append(tickets, ticket)
	}

	if err = rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating tickets: %w", err)
	}

	return tickets, nil
}






