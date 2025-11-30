// Issue: #141886738
package server

import (
	"context"
	"encoding/json"
	"errors"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/support-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type TicketRepositoryInterface interface {
	Create(ctx context.Context, ticket *models.SupportTicket) error
	GetByID(ctx context.Context, id uuid.UUID) (*models.SupportTicket, error)
	GetByNumber(ctx context.Context, number string) (*models.SupportTicket, error)
	GetByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.SupportTicket, error)
	GetByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) ([]models.SupportTicket, error)
	GetByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) ([]models.SupportTicket, error)
	Update(ctx context.Context, ticket *models.SupportTicket) error
	CountByPlayerID(ctx context.Context, playerID uuid.UUID) (int, error)
	CountByStatus(ctx context.Context, status models.TicketStatus) (int, error)
	CreateResponse(ctx context.Context, response *models.TicketResponse) error
	GetResponsesByTicketID(ctx context.Context, ticketID uuid.UUID) ([]models.TicketResponse, error)
	GetNextTicketNumber(ctx context.Context) (string, error)
}

type TicketService struct {
	repo  TicketRepositoryInterface
	cache *redis.Client
	logger *logrus.Logger
}

func NewTicketService(dbURL, redisURL string) (*TicketService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewTicketRepository(dbPool)

	return &TicketService{
		repo:  repo,
		cache: redisClient,
		logger: GetLogger(),
	}, nil
}

func (s *TicketService) CreateTicket(ctx context.Context, playerID uuid.UUID, req *models.CreateTicketRequest) (*models.SupportTicket, error) {
	number, err := s.repo.GetNextTicketNumber(ctx)
	if err != nil {
		return nil, err
	}

	priority := models.TicketPriorityNormal
	if req.Priority != nil {
		priority = *req.Priority
	}

	now := time.Now()
	ticket := &models.SupportTicket{
		ID:          uuid.New(),
		Number:      number,
		PlayerID:    playerID,
		Category:    req.Category,
		Priority:    priority,
		Status:      models.TicketStatusOpen,
		Subject:     req.Subject,
		Description: req.Description,
		CreatedAt:   now,
		UpdatedAt:   now,
	}

	err = s.repo.Create(ctx, ticket)
	if err != nil {
		return nil, err
	}

	RecordTicket(string(ticket.Status), string(ticket.Priority), string(ticket.Category))
	s.invalidateTicketCache(ctx, playerID)

	return ticket, nil
}

func (s *TicketService) GetTicket(ctx context.Context, id uuid.UUID) (*models.SupportTicket, error) {
	return s.repo.GetByID(ctx, id)
}

func (s *TicketService) GetTicketByNumber(ctx context.Context, number string) (*models.SupportTicket, error) {
	return s.repo.GetByNumber(ctx, number)
}

func (s *TicketService) GetTicketsByPlayerID(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.TicketListResponse, error) {
	cacheKey := "tickets:player:" + playerID.String() + ":limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.TicketListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	tickets, err := s.repo.GetByPlayerID(ctx, playerID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountByPlayerID(ctx, playerID)
	if err != nil {
		return nil, err
	}

	response := &models.TicketListResponse{
		Tickets: tickets,
		Total:   total,
	}

	responseJSON, err := json.Marshal(response)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal response JSON for cache")
		// Continue without caching if marshal fails
	} else {
		s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)
	}

	return response, nil
}

func (s *TicketService) GetTicketsByAgentID(ctx context.Context, agentID uuid.UUID, limit, offset int) (*models.TicketListResponse, error) {
	tickets, err := s.repo.GetByAgentID(ctx, agentID, limit, offset)
	if err != nil {
		return nil, err
	}

	total := len(tickets)

	return &models.TicketListResponse{
		Tickets: tickets,
		Total:   total,
	}, nil
}

func (s *TicketService) GetTicketsByStatus(ctx context.Context, status models.TicketStatus, limit, offset int) (*models.TicketListResponse, error) {
	tickets, err := s.repo.GetByStatus(ctx, status, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountByStatus(ctx, status)
	if err != nil {
		return nil, err
	}

	return &models.TicketListResponse{
		Tickets: tickets,
		Total:   total,
	}, nil
}

func (s *TicketService) UpdateTicket(ctx context.Context, id uuid.UUID, req *models.UpdateTicketRequest) (*models.SupportTicket, error) {
	ticket, err := s.repo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}
	if ticket == nil {
		return nil, nil
	}

	if req.Category != nil {
		ticket.Category = *req.Category
	}
	if req.Priority != nil {
		ticket.Priority = *req.Priority
	}
	if req.Status != nil {
		ticket.Status = *req.Status
		
		now := time.Now()
		if ticket.Status == models.TicketStatusResolved && ticket.ResolvedAt == nil {
			ticket.ResolvedAt = &now
			RecordTicketResolved()
		}
		if ticket.Status == models.TicketStatusClosed && ticket.ClosedAt == nil {
			ticket.ClosedAt = &now
		}

		RecordTicket(string(ticket.Status), string(ticket.Priority), string(ticket.Category))
	}
	if req.Subject != nil {
		ticket.Subject = *req.Subject
	}

	ticket.UpdatedAt = time.Now()
	err = s.repo.Update(ctx, ticket)
	if err != nil {
		return nil, err
	}

	s.invalidateTicketCache(ctx, ticket.PlayerID)
	if ticket.AssignedAgentID != nil {
		s.invalidateAgentCache(ctx, *ticket.AssignedAgentID)
	}

	return ticket, nil
}

func (s *TicketService) AssignTicket(ctx context.Context, id uuid.UUID, agentID uuid.UUID) (*models.SupportTicket, error) {
	ticket, err := s.repo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}
	if ticket == nil {
		return nil, nil
	}

	ticket.AssignedAgentID = &agentID
	if ticket.Status == models.TicketStatusOpen {
		ticket.Status = models.TicketStatusAssigned
	}
	ticket.UpdatedAt = time.Now()

	err = s.repo.Update(ctx, ticket)
	if err != nil {
		return nil, err
	}

	RecordTicket(string(ticket.Status), string(ticket.Priority), string(ticket.Category))
	s.invalidateTicketCache(ctx, ticket.PlayerID)
	s.invalidateAgentCache(ctx, agentID)

	return ticket, nil
}

func (s *TicketService) AddResponse(ctx context.Context, ticketID uuid.UUID, authorID uuid.UUID, isAgent bool, req *models.AddResponseRequest) (*models.TicketResponse, error) {
	ticket, err := s.repo.GetByID(ctx, ticketID)
	if err != nil {
		return nil, err
	}
	if ticket == nil {
		return nil, nil
	}

	visibility := models.TicketVisibilityPublic
	if req.Visibility != "" {
		visibility = req.Visibility
	}

	response := &models.TicketResponse{
		ID:          uuid.New(),
		TicketID:    ticketID,
		AuthorID:    authorID,
		IsAgent:     isAgent,
		Message:     req.Message,
		Attachments: req.Attachments,
		Visibility:  visibility,
		CreatedAt:   time.Now(),
	}

	err = s.repo.CreateResponse(ctx, response)
	if err != nil {
		return nil, err
	}

	if ticket.FirstResponseAt == nil && isAgent {
		now := time.Now()
		ticket.FirstResponseAt = &now
		if ticket.Status == models.TicketStatusOpen || ticket.Status == models.TicketStatusAssigned {
			ticket.Status = models.TicketStatusInProgress
		}
		ticket.UpdatedAt = now
		s.repo.Update(ctx, ticket)
	}

	s.invalidateTicketCache(ctx, ticket.PlayerID)
	if ticket.AssignedAgentID != nil {
		s.invalidateAgentCache(ctx, *ticket.AssignedAgentID)
	}

	return response, nil
}

func (s *TicketService) GetTicketDetail(ctx context.Context, id uuid.UUID) (*models.TicketDetailResponse, error) {
	ticket, err := s.repo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}
	if ticket == nil {
		return nil, nil
	}

	responses, err := s.repo.GetResponsesByTicketID(ctx, id)
	if err != nil {
		return nil, err
	}

	return &models.TicketDetailResponse{
		Ticket:    *ticket,
		Responses: responses,
	}, nil
}

func (s *TicketService) RateTicket(ctx context.Context, id uuid.UUID, rating int) error {
	if rating < 1 || rating > 5 {
		return nil
	}

	ticket, err := s.repo.GetByID(ctx, id)
	if err != nil {
		return err
	}
	if ticket == nil {
		return errors.New("ticket not found")
	}

	ticket.SatisfactionRating = &rating
	ticket.UpdatedAt = time.Now()

	err = s.repo.Update(ctx, ticket)
	if err != nil {
		return err
	}

	s.invalidateTicketCache(ctx, ticket.PlayerID)

	return nil
}

func (s *TicketService) invalidateTicketCache(ctx context.Context, playerID uuid.UUID) {
	pattern := "tickets:player:" + playerID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

func (s *TicketService) invalidateAgentCache(ctx context.Context, agentID uuid.UUID) {
	pattern := "tickets:agent:" + agentID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

