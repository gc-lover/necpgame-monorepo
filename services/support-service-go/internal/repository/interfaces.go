package repository

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/google/uuid"
)

// TicketRepository defines interface for ticket operations
type TicketRepository interface {
	Create(ctx context.Context, ticket *models.Ticket) error
	GetByID(ctx context.Context, id uuid.UUID) (*models.Ticket, error)
	GetByCharacterID(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]*models.Ticket, error)
	GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]*models.Ticket, error)
	GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]*models.Ticket, error)
	GetByCategory(ctx context.Context, category string, limit, offset int) ([]*models.Ticket, error)
	GetByPriority(ctx context.Context, priority models.TicketPriority, limit, offset int) ([]*models.Ticket, error)
	Update(ctx context.Context, ticket *models.Ticket) error
	UpdateStatus(ctx context.Context, id uuid.UUID, status models.TicketStatus, agentID *uuid.UUID) error
	AssignAgent(ctx context.Context, id uuid.UUID, agentID uuid.UUID) error
	Close(ctx context.Context, id uuid.UUID, resolution string) error
	GetQueue(ctx context.Context, limit, offset int) ([]*models.Ticket, error)
	GetUnassigned(ctx context.Context, limit, offset int) ([]*models.Ticket, error)
	GetOverdueSLA(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error)
	GetStatistics(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error)
}

// TicketResponseRepository defines interface for ticket response operations
type TicketResponseRepository interface {
	CreateResponse(ctx context.Context, response *models.TicketResponse) error
	GetByTicketID(ctx context.Context, ticketID uuid.UUID) ([]*models.TicketResponse, error)
	GetResponseByID(ctx context.Context, id uuid.UUID) (*models.TicketResponse, error)
	UpdateResponse(ctx context.Context, response *models.TicketResponse) error
	DeleteResponse(ctx context.Context, id uuid.UUID) error
}

// SLARepository defines interface for SLA operations
type SLARepository interface {
	GetSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error)
	UpdateSLAStatus(ctx context.Context, ticketID uuid.UUID, status models.SLAStatus) error
	GetSLAStats(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error)
	GetOverdueTickets(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error)
}

// Repository defines the main repository interface
type Repository interface {
	TicketRepository
	TicketResponseRepository
	SLARepository
	BeginTx(ctx context.Context) (Transaction, error)
}

// Transaction defines transaction interface
type Transaction interface {
	Commit() error
	Rollback() error
	TicketRepository
	TicketResponseRepository
	SLARepository
}

// SupportRepository defines the complete support repository interface
type SupportRepository interface {
	CreateTicket(ctx context.Context, ticket *models.Ticket) error
	GetTicket(ctx context.Context, id uuid.UUID) (*models.Ticket, error)
	UpdateTicket(ctx context.Context, ticket *models.Ticket) error
	DeleteTicket(ctx context.Context, id uuid.UUID) error
	ListTickets(ctx context.Context, filter *models.TicketFilter, page, limit int) ([]*models.Ticket, int, error)
	GetTicketsByCharacter(ctx context.Context, characterID uuid.UUID, page, limit int) ([]*models.Ticket, int, error)
	GetTicketsByAgent(ctx context.Context, agentID uuid.UUID, page, limit int) ([]*models.Ticket, int, error)
	GetTicketQueue(ctx context.Context, filter *models.QueueFilter, page, limit int) ([]*models.Ticket, *models.QueueStats, error)
	AssignAgent(ctx context.Context, ticketID, agentID uuid.UUID) (*models.Ticket, error)
	UpdateTicketStatus(ctx context.Context, ticketID uuid.UUID, status models.TicketStatus) (*models.Ticket, error)
	UpdateTicketPriority(ctx context.Context, ticketID uuid.UUID, priority models.TicketPriority) (*models.Ticket, error)
	CreateTicketResponse(ctx context.Context, response *models.TicketResponse) error
	GetTicketResponses(ctx context.Context, ticketID uuid.UUID, page, limit int) ([]*models.TicketResponse, int, error)
	UpdateTicketRating(ctx context.Context, ticketID uuid.UUID, rating int, comment string) error
	GetTicketSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error)
	GetSupportStats(ctx context.Context, period, category string) (*models.SupportStatsResponse, error)
	IncrementTicketResponseCount(ctx context.Context, ticketID uuid.UUID) error
}






