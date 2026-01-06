package postgres

import (
	"context"
	"database/sql"
	"fmt"

	"necpgame/services/support-service-go/internal/repository"
)

type postgresRepository struct {
	db               *sql.DB
	ticketRepo       repository.TicketRepository
	ticketRespRepo   repository.TicketResponseRepository
	slaRepo          repository.SLARepository
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

// TicketRepository methods
func (r *postgresRepository) Create(ctx context.Context, ticket interface{}) error {
	if t, ok := ticket.(*models.Ticket); ok {
		return r.ticketRepo.Create(ctx, t)
	}
	return fmt.Errorf("invalid ticket type")
}

func (r *postgresRepository) GetByID(ctx context.Context, id interface{}) (interface{}, error) {
	if uid, ok := id.(uuid.UUID); ok {
		return r.ticketRepo.GetByID(ctx, uid)
	}
	return nil, fmt.Errorf("invalid ID type")
}

func (r *postgresRepository) GetByPlayerID(ctx context.Context, playerID interface{}, limit, offset int) (interface{}, error) {
	if uid, ok := playerID.(uuid.UUID); ok {
		return r.ticketRepo.GetByPlayerID(ctx, uid, limit, offset)
	}
	return nil, fmt.Errorf("invalid player ID type")
}

func (r *postgresRepository) GetByAgentID(ctx context.Context, agentID interface{}, limit, offset int) (interface{}, error) {
	if uid, ok := agentID.(uuid.UUID); ok {
		return r.ticketRepo.GetByAgentID(ctx, uid, limit, offset)
	}
	return nil, fmt.Errorf("invalid agent ID type")
}

func (r *postgresRepository) GetByStatus(ctx context.Context, status interface{}, limit, offset int) (interface{}, error) {
	if s, ok := status.(models.TicketStatus); ok {
		return r.ticketRepo.GetByStatus(ctx, s, limit, offset)
	}
	return nil, fmt.Errorf("invalid status type")
}

func (r *postgresRepository) GetByCategory(ctx context.Context, category string, limit, offset int) (interface{}, error) {
	return r.ticketRepo.GetByCategory(ctx, category, limit, offset)
}

func (r *postgresRepository) GetByPriority(ctx context.Context, priority interface{}, limit, offset int) (interface{}, error) {
	if p, ok := priority.(models.TicketPriority); ok {
		return r.ticketRepo.GetByPriority(ctx, p, limit, offset)
	}
	return nil, fmt.Errorf("invalid priority type")
}

func (r *postgresRepository) Update(ctx context.Context, ticket interface{}) error {
	if t, ok := ticket.(*models.Ticket); ok {
		return r.ticketRepo.Update(ctx, t)
	}
	return fmt.Errorf("invalid ticket type")
}

func (r *postgresRepository) UpdateStatus(ctx context.Context, id interface{}, status interface{}, agentID interface{}) error {
	if uid, ok := id.(uuid.UUID); ok {
		if s, ok := status.(models.TicketStatus); ok {
			var aid *uuid.UUID
			if agentID != nil {
				if a, ok := agentID.(*uuid.UUID); ok {
					aid = a
				}
			}
			return r.ticketRepo.UpdateStatus(ctx, uid, s, aid)
		}
	}
	return fmt.Errorf("invalid parameters for UpdateStatus")
}

func (r *postgresRepository) AssignAgent(ctx context.Context, id interface{}, agentID interface{}) error {
	if uid, ok := id.(uuid.UUID); ok {
		if aid, ok := agentID.(uuid.UUID); ok {
			return r.ticketRepo.AssignAgent(ctx, uid, aid)
		}
	}
	return fmt.Errorf("invalid parameters for AssignAgent")
}

func (r *postgresRepository) Close(ctx context.Context, id interface{}, resolution string) error {
	if uid, ok := id.(uuid.UUID); ok {
		return r.ticketRepo.Close(ctx, uid, resolution)
	}
	return fmt.Errorf("invalid ID type")
}

func (r *postgresRepository) GetQueue(ctx context.Context, limit, offset int) (interface{}, error) {
	return r.ticketRepo.GetQueue(ctx, limit, offset)
}

func (r *postgresRepository) GetUnassigned(ctx context.Context, limit, offset int) (interface{}, error) {
	return r.ticketRepo.GetUnassigned(ctx, limit, offset)
}

func (r *postgresRepository) GetOverdueSLA(ctx context.Context, currentTime interface{}) (interface{}, error) {
	if t, ok := currentTime.(time.Time); ok {
		return r.ticketRepo.GetOverdueSLA(ctx, t)
	}
	return nil, fmt.Errorf("invalid time type")
}

func (r *postgresRepository) GetStatistics(ctx context.Context, periodStart, periodEnd interface{}) (interface{}, error) {
	if start, ok := periodStart.(time.Time); ok {
		if end, ok := periodEnd.(time.Time); ok {
			return r.ticketRepo.GetStatistics(ctx, start, end)
		}
	}
	return nil, fmt.Errorf("invalid time parameters")
}

// TicketResponseRepository methods
func (r *postgresRepository) CreateResponse(ctx context.Context, response interface{}) error {
	if resp, ok := response.(*models.TicketResponse); ok {
		return r.ticketRespRepo.Create(ctx, resp)
	}
	return fmt.Errorf("invalid response type")
}

func (r *postgresRepository) GetResponseByID(ctx context.Context, id interface{}) (interface{}, error) {
	if uid, ok := id.(uuid.UUID); ok {
		return r.ticketRespRepo.GetByID(ctx, uid)
	}
	return nil, fmt.Errorf("invalid ID type")
}

func (r *postgresRepository) GetResponsesByTicketID(ctx context.Context, ticketID interface{}) (interface{}, error) {
	if uid, ok := ticketID.(uuid.UUID); ok {
		return r.ticketRespRepo.GetByTicketID(ctx, uid)
	}
	return nil, fmt.Errorf("invalid ticket ID type")
}

func (r *postgresRepository) UpdateResponse(ctx context.Context, response interface{}) error {
	if resp, ok := response.(*models.TicketResponse); ok {
		return r.ticketRespRepo.Update(ctx, resp)
	}
	return fmt.Errorf("invalid response type")
}

func (r *postgresRepository) DeleteResponse(ctx context.Context, id interface{}) error {
	if uid, ok := id.(uuid.UUID); ok {
		return r.ticketRespRepo.Delete(ctx, uid)
	}
	return fmt.Errorf("invalid ID type")
}

// SLARepository methods
func (r *postgresRepository) GetSLAInfo(ctx context.Context, ticketID interface{}) (interface{}, error) {
	if uid, ok := ticketID.(uuid.UUID); ok {
		return r.slaRepo.GetSLAInfo(ctx, uid)
	}
	return nil, fmt.Errorf("invalid ticket ID type")
}

func (r *postgresRepository) UpdateSLAStatus(ctx context.Context, ticketID interface{}, status interface{}) error {
	if uid, ok := ticketID.(uuid.UUID); ok {
		if s, ok := status.(models.SLAStatus); ok {
			return r.slaRepo.UpdateSLAStatus(ctx, uid, s)
		}
	}
	return nil, fmt.Errorf("invalid parameters for UpdateSLAStatus")
}

func (r *postgresRepository) GetSLAStats(ctx context.Context, periodStart, periodEnd interface{}) (interface{}, error) {
	if start, ok := periodStart.(time.Time); ok {
		if end, ok := periodEnd.(time.Time); ok {
			return r.slaRepo.GetSLAStats(ctx, start, end)
		}
	}
	return nil, fmt.Errorf("invalid time parameters")
}

func (r *postgresRepository) GetOverdueSLATickets(ctx context.Context, currentTime interface{}) (interface{}, error) {
	if t, ok := currentTime.(time.Time); ok {
		return r.slaRepo.GetOverdueTickets(ctx, t)
	}
	return nil, fmt.Errorf("invalid time type")
}

// Transaction methods
func (r *postgresRepository) BeginTx(ctx context.Context) (repository.Transaction, error) {
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to begin transaction: %w", err)
	}

	return &postgresTransaction{
		tx:             tx,
		ticketRepo:     NewTicketRepositoryWithTx(tx),
		ticketRespRepo: NewTicketResponseRepositoryWithTx(tx),
		slaRepo:        NewSLARepositoryWithTx(tx),
	}, nil
}

// Transaction implementation
type postgresTransaction struct {
	tx             *sql.Tx
	ticketRepo     repository.TicketRepository
	ticketRespRepo repository.TicketResponseRepository
	slaRepo        repository.SLARepository
}

func (t *postgresTransaction) Commit() error {
	return t.tx.Commit()
}

func (t *postgresTransaction) Rollback() error {
	return t.tx.Rollback()
}

// Delegate all repository methods to the transaction versions
// ... (implementing all the interface methods similarly)

// Helper functions to create transaction-based repositories
func NewTicketRepositoryWithTx(tx *sql.Tx) repository.TicketRepository {
	return &txTicketRepository{tx: tx}
}

func NewTicketResponseRepositoryWithTx(tx *sql.Tx) repository.TicketResponseRepository {
	return &txTicketResponseRepository{tx: tx}
}

func NewSLARepositoryWithTx(tx *sql.Tx) repository.SLARepository {
	return &txSLARepository{tx: tx}
}

// Transaction repository implementations (similar to regular ones but using tx instead of db)
// ... (implementing the same methods but using t.tx instead of r.db)

