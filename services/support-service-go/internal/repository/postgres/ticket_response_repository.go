package postgres

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
	"necpgame/services/support-service-go/internal/models"
	"necpgame/services/support-service-go/internal/repository"
)

type ticketResponseRepository struct {
	db *sql.DB
}

// NewTicketResponseRepository creates a new PostgreSQL ticket response repository
func NewTicketResponseRepository(db *sql.DB) repository.TicketResponseRepository {
	return &ticketResponseRepository{db: db}
}

func (r *ticketResponseRepository) Create(ctx context.Context, response *models.TicketResponse) error {
	query := `
		INSERT INTO ticket_responses (
			id, ticket_id, author_id, content, is_public, created_at
		) VALUES ($1, $2, $3, $4, $5, $6)
	`

	response.CreatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		response.ID, response.TicketID, response.AuthorID,
		response.Content, response.IsPublic, response.CreatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create ticket response: %w", err)
	}

	return nil
}

func (r *ticketResponseRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.TicketResponse, error) {
	query := `
		SELECT id, ticket_id, author_id, content, is_public, created_at
		FROM ticket_responses WHERE id = $1
	`

	response := &models.TicketResponse{}
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&response.ID, &response.TicketID, &response.AuthorID,
		&response.Content, &response.IsPublic, &response.CreatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("ticket response not found")
		}
		return nil, fmt.Errorf("failed to get ticket response: %w", err)
	}

	return response, nil
}

func (r *ticketResponseRepository) GetByTicketID(ctx context.Context, ticketID uuid.UUID) ([]*models.TicketResponse, error) {
	query := `
		SELECT id, ticket_id, author_id, content, is_public, created_at
		FROM ticket_responses
		WHERE ticket_id = $1
		ORDER BY created_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to query ticket responses: %w", err)
	}
	defer rows.Close()

	var responses []*models.TicketResponse
	for rows.Next() {
		response := &models.TicketResponse{}
		err := rows.Scan(
			&response.ID, &response.TicketID, &response.AuthorID,
			&response.Content, &response.IsPublic, &response.CreatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan ticket response: %w", err)
		}
		responses = append(responses, response)
	}

	if err = rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating responses: %w", err)
	}

	return responses, nil
}

func (r *ticketResponseRepository) Update(ctx context.Context, response *models.TicketResponse) error {
	query := `
		UPDATE ticket_responses SET
			content = $1, is_public = $2
		WHERE id = $3
	`

	_, err := r.db.ExecContext(ctx, query,
		response.Content, response.IsPublic, response.ID,
	)

	if err != nil {
		return fmt.Errorf("failed to update ticket response: %w", err)
	}

	return nil
}

func (r *ticketResponseRepository) Delete(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM ticket_responses WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete ticket response: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("ticket response not found")
	}

	return nil
}

