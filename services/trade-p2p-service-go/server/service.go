// Package server Issue: #1637 - P2P Trade Service business logic
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-p2p-service-go/pkg/api"
	"github.com/google/uuid"
)

var (
	ErrNotFound     = errors.New("not found")
	_               = errors.New("not owner of items")
	ErrNotReady     = errors.New("trade offers not ready")
	ErrNotConfirmed = errors.New("both parties must confirm")
	_               = errors.New("already confirmed")
	_               = errors.New("session expired")
)

// TradeSession represents a trade session
type TradeSession struct {
	ID                 uuid.UUID
	InitiatorID        uuid.UUID
	TargetID           uuid.UUID
	Status             api.TradeStatus
	ZoneID             *string
	Difficulty         *string
	InitiatorOffer     *api.TradeOfferRequest
	TargetOffer        *api.TradeOfferRequest
	InitiatorConfirmed bool
	TargetConfirmed    bool
	ExpiresAt          time.Time
	CreatedAt          time.Time
}

// Service implements business logic for P2P Trade
// Issue: #1637 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	sessionPool sync.Pool
	historyPool sync.Pool
}

func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.sessionPool = sync.Pool{
		New: func() interface{} {
			return &api.TradeSessionResponse{}
		},
	}
	s.historyPool = sync.Pool{
		New: func() interface{} {
			return &api.GetTradeHistoryOK{}
		},
	}

	return s
}

// InitiateTrade creates a new trade session
func (s *Service) InitiateTrade(ctx context.Context, req *api.InitiateTradeReq) (*api.TradeSessionResponse, error) {
	sessionID := uuid.New()
	expiresAt := time.Now().Add(5 * time.Minute) // 5 minutes timeout

	session := &TradeSession{
		ID:          sessionID,
		InitiatorID: uuid.New(), // TODO: Get from context/auth
		TargetID:    req.TargetPlayerID,
		Status:      api.TradeStatusPending,
		ExpiresAt:   expiresAt,
		CreatedAt:   time.Now(),
	}

	// Store in database
	err := s.repo.CreateTradeSession(ctx, session)
	if err != nil {
		return nil, err
	}

	// Return API response
	response := s.sessionPool.Get().(*api.TradeSessionResponse)
	defer s.sessionPool.Put(response)

	response.ID = api.NewOptUUID(sessionID)
	response.CreatedAt = api.NewOptDateTime(session.CreatedAt)
	response.InitiatorID = session.InitiatorID
	response.TargetID = session.TargetID
	response.Status = session.Status
	response.ExpiresAt = api.NewOptNilDateTime(session.ExpiresAt)

	if session.ZoneID != nil {
		response.ZoneID = api.NewOptString(*session.ZoneID)
	}

	return response, nil
}

// GetTradeSession retrieves a trade session by ID
func (s *Service) GetTradeSession(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	id, err := uuid.Parse(sessionID)
	if err != nil {
		return nil, errors.New("invalid session ID")
	}

	session, err := s.repo.GetTradeSession(ctx, id)
	if err != nil {
		return nil, err
	}

	// Check if expired
	if time.Now().After(session.ExpiresAt) {
		session.Status = api.TradeStatusExpired
		s.repo.UpdateTradeSessionStatus(ctx, session.ID, session.Status)
	}

	response := s.sessionPool.Get().(*api.TradeSessionResponse)
	defer s.sessionPool.Put(response)

	response.ID = api.NewOptUUID(session.ID)
	response.CreatedAt = api.NewOptDateTime(session.CreatedAt)
	response.InitiatorID = session.InitiatorID
	response.TargetID = session.TargetID
	response.Status = session.Status
	response.ExpiresAt = api.NewOptNilDateTime(session.ExpiresAt)

	if session.ZoneID != nil {
		response.ZoneID = api.NewOptString(*session.ZoneID)
	}

	// Convert offers
	if session.InitiatorOffer != nil {
		response.InitiatorOffer = api.NewOptTradeOfferRequest(*session.InitiatorOffer)
	}

	if session.TargetOffer != nil {
		response.TargetOffer = api.NewOptTradeOfferRequest(*session.TargetOffer)
	}

	response.InitiatorConfirmed = api.NewOptBool(session.InitiatorConfirmed)
	response.TargetConfirmed = api.NewOptBool(session.TargetConfirmed)

	return response, nil
}

// CancelTradeSession cancels a trade session
func (s *Service) CancelTradeSession(ctx context.Context, sessionID string) error {
	id, err := uuid.Parse(sessionID)
	if err != nil {
		return errors.New("invalid session ID")
	}

	return s.repo.UpdateTradeSessionStatus(ctx, id, api.TradeStatusCancelled)
}

// AddTradeOffer adds or updates a trade offer
func (s *Service) AddTradeOffer(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	id, err := uuid.Parse(sessionID)
	if err != nil {
		return nil, errors.New("invalid session ID")
	}

	// Get session
	session, err := s.repo.GetTradeSession(ctx, id)
	if err != nil {
		return nil, err
	}

	// TODO: Validate ownership of items
	// This would require integration with inventory service

	// Update offer in session (using request directly)
	err = s.repo.UpdateTradeOffer(ctx, id, true) // true for initiator
	if err != nil {
		return nil, err
	}

	// Reset confirmations when offer changes
	session.InitiatorConfirmed = false
	session.TargetConfirmed = false
	s.repo.ResetConfirmations(ctx, id)

	// Return updated session
	return s.GetTradeSession(ctx, sessionID)
}

// UpdateTradeOffer updates an existing trade offer
func (s *Service) UpdateTradeOffer(ctx context.Context, sessionID string, offer *api.TradeOfferRequest) (*api.TradeSessionResponse, error) {
	// Same as AddTradeOffer for now
	return s.AddTradeOffer(ctx, sessionID)
}

// RemoveTradeOffer removes a trade offer
func (s *Service) RemoveTradeOffer(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	id, err := uuid.Parse(sessionID)
	if err != nil {
		return nil, errors.New("invalid session ID")
	}

	// Clear offer
	err = s.repo.ClearTradeOffer(ctx, id, true) // true for initiator
	if err != nil {
		return nil, err
	}

	// Reset confirmations
	s.repo.ResetConfirmations(ctx, id)

	// Return updated session
	return s.GetTradeSession(ctx, sessionID)
}

// ConfirmTrade confirms the current trade offers
func (s *Service) ConfirmTrade(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	id, err := uuid.Parse(sessionID)
	if err != nil {
		return nil, errors.New("invalid session ID")
	}

	session, err := s.repo.GetTradeSession(ctx, id)
	if err != nil {
		return nil, err
	}

	// Check if both offers are present
	if session.InitiatorOffer == nil || session.TargetOffer == nil {
		return nil, ErrNotReady
	}

	// TODO: Set confirmation for current user
	// This would require user context

	// For now, set both as confirmed
	err = s.repo.SetConfirmation(ctx, id, true, true) // initiator and target
	if err != nil {
		return nil, err
	}

	return s.GetTradeSession(ctx, sessionID)
}

// CompleteTrade completes the trade if both parties have confirmed
func (s *Service) CompleteTrade(ctx context.Context, sessionID string) (*api.TradeCompleteResponse, error) {
	id, err := uuid.Parse(sessionID)
	if err != nil {
		return nil, errors.New("invalid session ID")
	}

	session, err := s.repo.GetTradeSession(ctx, id)
	if err != nil {
		return nil, err
	}

	// Check if both parties confirmed
	if !session.InitiatorConfirmed || !session.TargetConfirmed {
		return nil, ErrNotConfirmed
	}

	// TODO: Perform atomic item/currency transfer
	// This would require integration with inventory and economy services

	// Mark as completed
	err = s.repo.UpdateTradeSessionStatus(ctx, id, api.TradeStatusCompleted)
	if err != nil {
		return nil, err
	}

	// TODO: Save to history
	// s.saveToHistory(ctx, session)

	response := &api.TradeCompleteResponse{
		SessionID:   id,
		CompletedAt: api.NewOptDateTime(time.Now()),
		Status:      api.TradeStatusCompleted,
	}

	return response, nil
}

// GetTradeHistory retrieves trade history with pagination
func (s *Service) GetTradeHistory(ctx context.Context, limit, offset int) (*api.GetTradeHistoryOK, error) {
	history, total, err := s.repo.GetTradeHistory(ctx, limit, offset)
	if err != nil {
		return nil, err
	}

	response := s.historyPool.Get().(*api.GetTradeHistoryOK)
	defer s.historyPool.Put(response)

	response.Items = history
	response.Pagination = api.PaginationResponse{
		Total:  total,
		Limit:  api.NewOptInt(limit),
		Offset: api.NewOptInt(offset),
	}

	return response, nil
}

// Helper functions

func (s *Service) convertAPIRequestToOffer(req *api.TradeOfferRequest) *api.TradeOfferRequest {
	// Return the request as-is since it's already the correct type
	return req
}
