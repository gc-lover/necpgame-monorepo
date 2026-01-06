package postgres

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/repository"
	"github.com/google/uuid"
)

type simplePostgresRepository struct {
	db *sql.DB
}

// NewSimpleRepository creates a simple PostgreSQL repository
func NewSimpleRepository(db *sql.DB) repository.Repository {
	return &simplePostgresRepository{db: db}
}

// TicketRepository methods
func (r *simplePostgresRepository) Create(ctx context.Context, ticket *models.Ticket) error {
	return NewTicketRepository(r.db).Create(ctx, ticket)
}

func (r *simplePostgresRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Ticket, error) {
	return NewTicketRepository(r.db).GetByID(ctx, id)
}

func (r *simplePostgresRepository) GetByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetByPlayerID(ctx, playerID, limit, offset)
}

func (r *simplePostgresRepository) GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetByAgentID(ctx, agentID, limit, offset)
}

func (r *simplePostgresRepository) GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetByStatus(ctx, status, limit, offset)
}

func (r *simplePostgresRepository) GetByCategory(ctx context.Context, category string, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetByCategory(ctx, category, limit, offset)
}

func (r *simplePostgresRepository) GetByPriority(ctx context.Context, priority models.TicketPriority, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetByPriority(ctx, priority, limit, offset)
}

func (r *simplePostgresRepository) Update(ctx context.Context, ticket *models.Ticket) error {
	return NewTicketRepository(r.db).Update(ctx, ticket)
}

func (r *simplePostgresRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.TicketStatus, agentID *uuid.UUID) error {
	return NewTicketRepository(r.db).UpdateStatus(ctx, id, status, agentID)
}

func (r *simplePostgresRepository) AssignAgent(ctx context.Context, id uuid.UUID, agentID uuid.UUID) error {
	return NewTicketRepository(r.db).AssignAgent(ctx, id, agentID)
}

func (r *simplePostgresRepository) Close(ctx context.Context, id uuid.UUID, resolution string) error {
	return NewTicketRepository(r.db).Close(ctx, id, resolution)
}

func (r *simplePostgresRepository) GetQueue(ctx context.Context, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetQueue(ctx, limit, offset)
}

func (r *simplePostgresRepository) GetUnassigned(ctx context.Context, limit, offset int) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetUnassigned(ctx, limit, offset)
}

func (r *simplePostgresRepository) GetOverdueSLA(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error) {
	return NewTicketRepository(r.db).GetOverdueSLA(ctx, currentTime)
}

func (r *simplePostgresRepository) GetStatistics(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error) {
	return NewTicketRepository(r.db).GetStatistics(ctx, periodStart, periodEnd)
}

// TicketResponseRepository methods
func (r *simplePostgresRepository) CreateResponse(ctx context.Context, response *models.TicketResponse) error {
	return NewTicketResponseRepository(r.db).CreateResponse(ctx, response)
}

func (r *simplePostgresRepository) GetResponseByID(ctx context.Context, id uuid.UUID) (*models.TicketResponse, error) {
	return NewTicketResponseRepository(r.db).GetResponseByID(ctx, id)
}

func (r *simplePostgresRepository) GetByTicketID(ctx context.Context, ticketID uuid.UUID) ([]*models.TicketResponse, error) {
	return NewTicketResponseRepository(r.db).GetByTicketID(ctx, ticketID)
}

func (r *simplePostgresRepository) UpdateResponse(ctx context.Context, response *models.TicketResponse) error {
	return NewTicketResponseRepository(r.db).UpdateResponse(ctx, response)
}

func (r *simplePostgresRepository) DeleteResponse(ctx context.Context, id uuid.UUID) error {
	return NewTicketResponseRepository(r.db).DeleteResponse(ctx, id)
}

// SLARepository methods
func (r *simplePostgresRepository) GetSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error) {
	return NewSLARepository(r.db).GetSLAInfo(ctx, ticketID)
}

func (r *simplePostgresRepository) UpdateSLAStatus(ctx context.Context, ticketID uuid.UUID, status models.SLAStatus) error {
	return NewSLARepository(r.db).UpdateSLAStatus(ctx, ticketID, status)
}

func (r *simplePostgresRepository) GetSLAStats(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error) {
	return NewSLARepository(r.db).GetSLAStats(ctx, periodStart, periodEnd)
}

func (r *simplePostgresRepository) GetOverdueTickets(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error) {
	return NewSLARepository(r.db).GetOverdueTickets(ctx, currentTime)
}

func (r *simplePostgresRepository) Commit() error {
	return nil
}

func (r *simplePostgresRepository) Rollback() error {
	return nil
}

// Transaction methods
func (r *simplePostgresRepository) BeginTx(ctx context.Context) (repository.Transaction, error) {
	// For simple repository, we return itself as the transaction (simplified)
	return r, nil
}


