package postgres

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/repository"
	"github.com/google/uuid"
)

// DBTX defines the common interface for sql.DB and sql.Tx
type DBTX interface {
	ExecContext(ctx context.Context, query string, args ...interface{}) (sql.Result, error)
	QueryContext(ctx context.Context, query string, args ...interface{}) (*sql.Rows, error)
	QueryRowContext(ctx context.Context, query string, args ...interface{}) *sql.Row
}

type postgresRepository struct {
	db             *sql.DB
	ticketRepo     repository.TicketRepository
	ticketRespRepo repository.TicketResponseRepository
	slaRepo        repository.SLARepository
}

// NewRepository creates a new PostgreSQL repository
func NewRepository(db *sql.DB) repository.Repository {
	return &postgresRepository{
		db:             db,
		ticketRepo:     NewTicketRepository(db),
		ticketRespRepo: NewTicketResponseRepository(db),
		slaRepo:        NewSLARepository(db),
	}
}

// TicketRepository delegate methods
func (r *postgresRepository) Create(ctx context.Context, ticket *models.Ticket) error {
	return r.ticketRepo.Create(ctx, ticket)
}

func (r *postgresRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Ticket, error) {
	return r.ticketRepo.GetByID(ctx, id)
}

func (r *postgresRepository) GetByCharacterID(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetByCharacterID(ctx, characterID, limit, offset)
}

func (r *postgresRepository) GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetByAgentID(ctx, agentID, limit, offset)
}

func (r *postgresRepository) GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetByStatus(ctx, status, limit, offset)
}

func (r *postgresRepository) GetByCategory(ctx context.Context, category string, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetByCategory(ctx, category, limit, offset)
}

func (r *postgresRepository) GetByPriority(ctx context.Context, priority models.TicketPriority, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetByPriority(ctx, priority, limit, offset)
}

func (r *postgresRepository) Update(ctx context.Context, ticket *models.Ticket) error {
	return r.ticketRepo.Update(ctx, ticket)
}

func (r *postgresRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.TicketStatus, agentID *uuid.UUID) error {
	return r.ticketRepo.UpdateStatus(ctx, id, status, agentID)
}

func (r *postgresRepository) AssignAgent(ctx context.Context, id uuid.UUID, agentID uuid.UUID) error {
	return r.ticketRepo.AssignAgent(ctx, id, agentID)
}

func (r *postgresRepository) Close(ctx context.Context, id uuid.UUID, resolution string) error {
	return r.ticketRepo.Close(ctx, id, resolution)
}

func (r *postgresRepository) GetQueue(ctx context.Context, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetQueue(ctx, limit, offset)
}

func (r *postgresRepository) GetUnassigned(ctx context.Context, limit, offset int) ([]*models.Ticket, error) {
	return r.ticketRepo.GetUnassigned(ctx, limit, offset)
}

func (r *postgresRepository) GetOverdueSLA(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error) {
	return r.ticketRepo.GetOverdueSLA(ctx, currentTime)
}

func (r *postgresRepository) GetStatistics(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error) {
	return r.ticketRepo.GetStatistics(ctx, periodStart, periodEnd)
}

// TicketResponseRepository delegate methods
func (r *postgresRepository) CreateResponse(ctx context.Context, response *models.TicketResponse) error {
	return r.ticketRespRepo.CreateResponse(ctx, response)
}

func (r *postgresRepository) GetResponseByID(ctx context.Context, id uuid.UUID) (*models.TicketResponse, error) {
	return r.ticketRespRepo.GetResponseByID(ctx, id)
}

func (r *postgresRepository) GetByTicketID(ctx context.Context, ticketID uuid.UUID) ([]*models.TicketResponse, error) {
	return r.ticketRespRepo.GetByTicketID(ctx, ticketID)
}

func (r *postgresRepository) UpdateResponse(ctx context.Context, response *models.TicketResponse) error {
	return r.ticketRespRepo.UpdateResponse(ctx, response)
}

func (r *postgresRepository) DeleteResponse(ctx context.Context, id uuid.UUID) error {
	return r.ticketRespRepo.DeleteResponse(ctx, id)
}

// SLARepository delegate methods
func (r *postgresRepository) GetSLAInfo(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error) {
	return r.slaRepo.GetSLAInfo(ctx, ticketID)
}

func (r *postgresRepository) UpdateSLAStatus(ctx context.Context, ticketID uuid.UUID, status models.SLAStatus) error {
	return r.slaRepo.UpdateSLAStatus(ctx, ticketID, status)
}

func (r *postgresRepository) GetSLAStats(ctx context.Context, periodStart, periodEnd time.Time) (*models.SupportStatsResponse, error) {
	return r.slaRepo.GetSLAStats(ctx, periodStart, periodEnd)
}

func (r *postgresRepository) GetOverdueTickets(ctx context.Context, currentTime time.Time) ([]*models.Ticket, error) {
	return r.slaRepo.GetOverdueTickets(ctx, currentTime)
}

// Transaction implementation
type postgresTransaction struct {
	tx *sql.Tx
	repository.TicketRepository
	repository.TicketResponseRepository
	repository.SLARepository
}

func (t *postgresTransaction) Commit() error {
	return t.tx.Commit()
}

func (t *postgresTransaction) Rollback() error {
	return t.tx.Rollback()
}

func (r *postgresRepository) BeginTx(ctx context.Context) (repository.Transaction, error) {
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to begin transaction: %w", err)
	}

	return &postgresTransaction{
		tx:                       tx,
		TicketRepository:         NewTicketRepositoryWithTx(tx),
		TicketResponseRepository: NewTicketResponseRepositoryWithTx(tx),
		SLARepository:            NewSLARepositoryWithTx(tx),
	}, nil
}

// Helper functions for transaction-aware repositories
func NewTicketRepositoryWithTx(tx DBTX) repository.TicketRepository {
	return NewTicketRepository(tx)
}

func NewTicketResponseRepositoryWithTx(tx DBTX) repository.TicketResponseRepository {
	return NewTicketResponseRepository(tx)
}

func NewSLARepositoryWithTx(tx DBTX) repository.SLARepository {
	return NewSLARepository(tx)
}





