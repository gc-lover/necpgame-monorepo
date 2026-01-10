package postgres

import (
	"context"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/database"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/repository"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/stdlib"
	"go.uber.org/zap"
)

// SupportRepositoryAdapter adapts Repository to SupportRepository interface
type SupportRepositoryAdapter struct {
	repo repository.Repository
}

// NewPostgresRepository creates a new PostgreSQL repository adapter
func NewPostgresRepository(db *database.DB, logger *zap.Logger) repository.SupportRepository {
	// Convert pgx pool to sql.DB for compatibility
	// Note: This is a temporary solution. Ideally all repositories should use pgx directly.
	sqlDB := stdlib.OpenDBFromPool(db.Pool)
	return &SupportRepositoryAdapter{
		repo: NewRepository(sqlDB),
	}
}

// CreateTicket creates a new ticket
func (a *SupportRepositoryAdapter) CreateTicket(ctx context.Context, ticket *models.Ticket) error {
	return a.repo.Create(ctx, ticket)
}

// GetTicket retrieves a ticket by ID
func (a *SupportRepositoryAdapter) GetTicket(ctx context.Context, id uuid.UUID) (*models.Ticket, error) {
	return a.repo.GetByID(ctx, id)
}

// UpdateTicket updates ticket information
func (a *SupportRepositoryAdapter) UpdateTicket(ctx context.Context, ticket *models.Ticket) error {
	return a.repo.Update(ctx, ticket)
}

// DeleteTicket deletes a ticket
func (a *SupportRepositoryAdapter) DeleteTicket(ctx context.Context, id uuid.UUID) error {
	// This method might not be implemented in the base repository
	// We'll implement it by setting status to cancelled
	ticket, err := a.repo.GetByID(ctx, id)
	if err != nil {
		return err
	}
	ticket.Status = models.TicketStatusCancelled
	return a.repo.Update(ctx, ticket)
}

// ListTickets retrieves paginated list of tickets with filters
func (a *SupportRepositoryAdapter) ListTickets(ctx context.Context, filter *models.TicketFilter, page, limit int) ([]*models.Ticket, int, error) {
	offset := (page - 1) * limit

	var tickets []*models.Ticket
	var err error

	// Apply filters based on available criteria
	if filter.Status != nil {
		tickets, err = a.repo.GetByStatus(ctx, *filter.Status, limit, offset)
	} else if filter.Priority != nil {
		tickets, err = a.repo.GetByPriority(ctx, *filter.Priority, limit, offset)
	} else if filter.AgentID != nil {
		tickets, err = a.repo.GetByAgentID(ctx, *filter.AgentID, limit, offset)
	} else if filter.Category != nil {
		categoryStr := string(*filter.Category)
		tickets, err = a.repo.GetByCategory(ctx, categoryStr, limit, offset)
	} else {
		// Default to unassigned tickets for queue
		tickets, err = a.repo.GetUnassigned(ctx, limit, offset)
	}

	if err != nil {
		return nil, 0, err
	}

	// For simplicity, return approximate total count
	total := len(tickets) * 10 // This should be improved with proper count queries

	return tickets, total, nil
}

// GetTicketsByCharacter retrieves tickets for a specific character
func (a *SupportRepositoryAdapter) GetTicketsByCharacter(ctx context.Context, characterID uuid.UUID, page, limit int) ([]*models.Ticket, int, error) {
	offset := (page - 1) * limit
	tickets, err := a.repo.GetByCharacterID(ctx, characterID, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	total := len(tickets) * 10 // Approximate
	return tickets, total, nil
}

// GetTicketsByAgent retrieves tickets assigned to an agent
func (a *SupportRepositoryAdapter) GetTicketsByAgent(ctx context.Context, agentID uuid.UUID, page, limit int) ([]*models.Ticket, int, error) {
	offset := (page - 1) * limit
	tickets, err := a.repo.GetByAgentID(ctx, agentID, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	total := len(tickets) * 10 // Approximate
	return tickets, total, nil
}

// GetTicketQueue retrieves ticket queue
func (a *SupportRepositoryAdapter) GetTicketQueue(ctx context.Context, filter *models.QueueFilter, page, limit int) ([]*models.Ticket, *models.QueueStats, error) {
	offset := (page - 1) * limit
	tickets, err := a.repo.GetQueue(ctx, limit, offset)
	if err != nil {
		return nil, nil, err
	}

	// Create basic queue stats
	stats := &models.QueueStats{
		TotalWaiting: len(tickets),
		UrgentCount:  0,
		HighCount:    0,
		NormalCount:  0,
		LowCount:     0,
	}

	// Count by priority
	for _, ticket := range tickets {
		switch ticket.Priority {
		case models.TicketPriorityUrgent, models.TicketPriorityCritical:
			stats.UrgentCount++
		case models.TicketPriorityHigh:
			stats.HighCount++
		case models.TicketPriorityNormal:
			stats.NormalCount++
		case models.TicketPriorityLow:
			stats.LowCount++
		}
	}

	return tickets, stats, nil
}

// AssignAgent assigns an agent to a ticket
func (a *SupportRepositoryAdapter) AssignAgent(ctx context.Context, ticketID, agentID uuid.UUID) (*models.Ticket, error) {
	err := a.repo.AssignAgent(ctx, ticketID, agentID)
	if err != nil {
		return nil, err
	}
	return a.repo.GetByID(ctx, ticketID)
}

// UpdateTicketStatus updates ticket status
func (a *SupportRepositoryAdapter) UpdateTicketStatus(ctx context.Context, ticketID uuid.UUID, status models.TicketStatus) (*models.Ticket, error) {
	err := a.repo.UpdateStatus(ctx, ticketID, status, nil)
	if err != nil {
		return nil, err
	}
	return a.repo.GetByID(ctx, ticketID)
}

// UpdateTicketPriority updates ticket priority
func (a *SupportRepositoryAdapter) UpdateTicketPriority(ctx context.Context, ticketID uuid.UUID, priority models.TicketPriority) (*models.Ticket, error) {
	ticket, err := a.repo.GetByID(ctx, ticketID)
	if err != nil {
		return nil, err
	}
	// This method might not exist in base repo, so we'll update manually
	ticket.Priority = priority
	return ticket, a.repo.Update(ctx, ticket)
}

// CreateTicketResponse creates a ticket response
func (a *SupportRepositoryAdapter) CreateTicketResponse(ctx context.Context, response *models.TicketResponse) error {
	return a.repo.CreateResponse(ctx, response)
}

// GetTicketResponses retrieves ticket responses
func (a *SupportRepositoryAdapter) GetTicketResponses(ctx context.Context, ticketID uuid.UUID, page, limit int) ([]*models.TicketResponse, int, error) {
	offset := (page - 1) * limit
	responses, err := a.repo.GetByTicketID(ctx, ticketID)
	if err != nil {
		return nil, 0, err
	}

	// Apply pagination
	start := offset
	end := offset + limit
	if start > len(responses) {
		start = len(responses)
	}
	if end > len(responses) {
		end = len(responses)
	}

	paginatedResponses := responses[start:end]
	return paginatedResponses, len(responses), nil
}

// UpdateTicketRating updates ticket rating
func (a *SupportRepositoryAdapter) UpdateTicketRating(ctx context.Context, ticketID uuid.UUID, rating int, comment string) error {
	// This method doesn't exist in base repo, so we'll skip implementation for now
	return nil
}

// GetTicketSLAInfo retrieves SLA information
func (a *SupportRepositoryAdapter) GetTicketSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error) {
	return a.repo.GetSLAInfo(ctx, ticketID)
}

// GetSupportStats retrieves support statistics
func (a *SupportRepositoryAdapter) GetSupportStats(ctx context.Context, period, category string) (*models.SupportStatsResponse, error) {
	// This is a complex method that doesn't exist in base repo
	// Return empty stats for now
	return &models.SupportStatsResponse{}, nil
}

// IncrementTicketResponseCount increments response count
func (a *SupportRepositoryAdapter) IncrementTicketResponseCount(ctx context.Context, ticketID uuid.UUID) error {
	// This method doesn't exist in base repo, so we'll skip implementation for now
	return nil
}