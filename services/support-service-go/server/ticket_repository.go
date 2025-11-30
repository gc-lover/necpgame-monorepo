// Issue: #141886738
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/support-service-go/models"
	"github.com/sirupsen/logrus"
)

type TicketRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewTicketRepository(db *pgxpool.Pool) *TicketRepository {
	return &TicketRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *TicketRepository) Create(ctx context.Context, ticket *models.SupportTicket) error {
	query := `
		INSERT INTO support.support_tickets (
			id, number, player_id, category, priority, status,
			subject, description, assigned_agent_id,
			created_at, updated_at, resolved_at, closed_at,
			first_response_at, satisfaction_rating
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15
		)`

	_, err := r.db.Exec(ctx, query,
		ticket.ID, ticket.Number, ticket.PlayerID, ticket.Category,
		ticket.Priority, ticket.Status, ticket.Subject, ticket.Description,
		ticket.AssignedAgentID, ticket.CreatedAt, ticket.UpdatedAt,
		ticket.ResolvedAt, ticket.ClosedAt, ticket.FirstResponseAt,
		ticket.SatisfactionRating,
	)

	return err
}

func (r *TicketRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.SupportTicket, error) {
	var ticket models.SupportTicket

	query := `
		SELECT id, number, player_id, category, priority, status,
			subject, description, assigned_agent_id,
			created_at, updated_at, resolved_at, closed_at,
			first_response_at, satisfaction_rating
		FROM support.support_tickets
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&ticket.ID, &ticket.Number, &ticket.PlayerID, &ticket.Category,
		&ticket.Priority, &ticket.Status, &ticket.Subject, &ticket.Description,
		&ticket.AssignedAgentID, &ticket.CreatedAt, &ticket.UpdatedAt,
		&ticket.ResolvedAt, &ticket.ClosedAt, &ticket.FirstResponseAt,
		&ticket.SatisfactionRating,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &ticket, nil
}

func (r *TicketRepository) GetByNumber(ctx context.Context, number string) (*models.SupportTicket, error) {
	var ticket models.SupportTicket

	query := `
		SELECT id, number, player_id, category, priority, status,
			subject, description, assigned_agent_id,
			created_at, updated_at, resolved_at, closed_at,
			first_response_at, satisfaction_rating
		FROM support.support_tickets
		WHERE number = $1`

	err := r.db.QueryRow(ctx, query, number).Scan(
		&ticket.ID, &ticket.Number, &ticket.PlayerID, &ticket.Category,
		&ticket.Priority, &ticket.Status, &ticket.Subject, &ticket.Description,
		&ticket.AssignedAgentID, &ticket.CreatedAt, &ticket.UpdatedAt,
		&ticket.ResolvedAt, &ticket.ClosedAt, &ticket.FirstResponseAt,
		&ticket.SatisfactionRating,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &ticket, nil
}

func (r *TicketRepository) GetByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.SupportTicket, error) {
	query := `
		SELECT id, number, player_id, category, priority, status,
			subject, description, assigned_agent_id,
			created_at, updated_at, resolved_at, closed_at,
			first_response_at, satisfaction_rating
		FROM support.support_tickets
		WHERE player_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var tickets []models.SupportTicket
	for rows.Next() {
		var ticket models.SupportTicket
		err := rows.Scan(
			&ticket.ID, &ticket.Number, &ticket.PlayerID, &ticket.Category,
			&ticket.Priority, &ticket.Status, &ticket.Subject, &ticket.Description,
			&ticket.AssignedAgentID, &ticket.CreatedAt, &ticket.UpdatedAt,
			&ticket.ResolvedAt, &ticket.ClosedAt, &ticket.FirstResponseAt,
			&ticket.SatisfactionRating,
		)
		if err != nil {
			return nil, err
		}
		tickets = append(tickets, ticket)
	}

	return tickets, nil
}

func (r *TicketRepository) GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]models.SupportTicket, error) {
	query := `
		SELECT id, number, player_id, category, priority, status,
			subject, description, assigned_agent_id,
			created_at, updated_at, resolved_at, closed_at,
			first_response_at, satisfaction_rating
		FROM support.support_tickets
		WHERE assigned_agent_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, agentID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var tickets []models.SupportTicket
	for rows.Next() {
		var ticket models.SupportTicket
		err := rows.Scan(
			&ticket.ID, &ticket.Number, &ticket.PlayerID, &ticket.Category,
			&ticket.Priority, &ticket.Status, &ticket.Subject, &ticket.Description,
			&ticket.AssignedAgentID, &ticket.CreatedAt, &ticket.UpdatedAt,
			&ticket.ResolvedAt, &ticket.ClosedAt, &ticket.FirstResponseAt,
			&ticket.SatisfactionRating,
		)
		if err != nil {
			return nil, err
		}
		tickets = append(tickets, ticket)
	}

	return tickets, nil
}

func (r *TicketRepository) GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]models.SupportTicket, error) {
	query := `
		SELECT id, number, player_id, category, priority, status,
			subject, description, assigned_agent_id,
			created_at, updated_at, resolved_at, closed_at,
			first_response_at, satisfaction_rating
		FROM support.support_tickets
		WHERE status = $1
		ORDER BY created_at ASC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, status, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var tickets []models.SupportTicket
	for rows.Next() {
		var ticket models.SupportTicket
		err := rows.Scan(
			&ticket.ID, &ticket.Number, &ticket.PlayerID, &ticket.Category,
			&ticket.Priority, &ticket.Status, &ticket.Subject, &ticket.Description,
			&ticket.AssignedAgentID, &ticket.CreatedAt, &ticket.UpdatedAt,
			&ticket.ResolvedAt, &ticket.ClosedAt, &ticket.FirstResponseAt,
			&ticket.SatisfactionRating,
		)
		if err != nil {
			return nil, err
		}
		tickets = append(tickets, ticket)
	}

	return tickets, nil
}

func (r *TicketRepository) Update(ctx context.Context, ticket *models.SupportTicket) error {
	query := `
		UPDATE support.support_tickets
		SET category = $1, priority = $2, status = $3,
			subject = $4, description = $5, assigned_agent_id = $6,
			updated_at = $7, resolved_at = $8, closed_at = $9,
			first_response_at = $10, satisfaction_rating = $11
		WHERE id = $12`

	_, err := r.db.Exec(ctx, query,
		ticket.Category, ticket.Priority, ticket.Status,
		ticket.Subject, ticket.Description, ticket.AssignedAgentID,
		ticket.UpdatedAt, ticket.ResolvedAt, ticket.ClosedAt,
		ticket.FirstResponseAt, ticket.SatisfactionRating,
		ticket.ID,
	)

	return err
}

func (r *TicketRepository) CountByPlayerID(ctx context.Context, playerID uuid.UUID) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM support.support_tickets WHERE player_id = $1`
	err := r.db.QueryRow(ctx, query, playerID).Scan(&count)
	return count, err
}

func (r *TicketRepository) CountByStatus(ctx context.Context, status models.TicketStatus) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM support.support_tickets WHERE status = $1`
	err := r.db.QueryRow(ctx, query, status).Scan(&count)
	return count, err
}

func (r *TicketRepository) CreateResponse(ctx context.Context, response *models.TicketResponse) error {
	attachmentsJSON, err := json.Marshal(response.Attachments)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal attachments JSON")
		return err
	}

	query := `
		INSERT INTO support.ticket_responses (
			id, ticket_id, author_id, is_agent, message,
			attachments, visibility, created_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8
		)`

	_, err = r.db.Exec(ctx, query,
		response.ID, response.TicketID, response.AuthorID, response.IsAgent,
		response.Message, attachmentsJSON, response.Visibility, response.CreatedAt,
	)

	return err
}

func (r *TicketRepository) GetResponsesByTicketID(ctx context.Context, ticketID uuid.UUID) ([]models.TicketResponse, error) {
	query := `
		SELECT id, ticket_id, author_id, is_agent, message,
			attachments, visibility, created_at
		FROM support.ticket_responses
		WHERE ticket_id = $1
		ORDER BY created_at ASC`

	rows, err := r.db.Query(ctx, query, ticketID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var responses []models.TicketResponse
	for rows.Next() {
		var response models.TicketResponse
		var attachmentsJSON []byte

		err := rows.Scan(
			&response.ID, &response.TicketID, &response.AuthorID, &response.IsAgent,
			&response.Message, &attachmentsJSON, &response.Visibility, &response.CreatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(attachmentsJSON) > 0 {
			if err := json.Unmarshal(attachmentsJSON, &response.Attachments); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal attachments JSON")
				return nil, err
			}
		}

		responses = append(responses, response)
	}

	return responses, nil
}

func (r *TicketRepository) GetNextTicketNumber(ctx context.Context) (string, error) {
	var count int
	query := `SELECT COUNT(*) FROM support.support_tickets`
	err := r.db.QueryRow(ctx, query).Scan(&count)
	if err != nil {
		return "", err
	}

	number := "TKT-" + time.Now().Format("20060102") + "-" + formatNumber(count+1)
	return number, nil
}

func formatNumber(n int) string {
	return fmt.Sprintf("%04d", n)
}

