// Trading Service Business Logic
// Issue: #2260 - Trading Service Implementation
// Agent: Backend Agent
package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	api "necpgame/services/trading-service-go/api"
	"necpgame/services/trading-service-go/internal/models"
	"necpgame/services/trading-service-go/internal/repository"
)

// Handler implements api.Handler interface
// PERFORMANCE: Optimized for high-throughput trading operations
type Handler struct {
	logger *zap.Logger
	repo   *repository.Repository
}

// NewHandler creates a new trading service handler
func NewHandler(logger *zap.Logger, repo *repository.Repository) *Handler {
	return &Handler{
		logger: logger.With(zap.String("component", "handler")),
		repo:   repo,
	}
}

// NewSecurityHandler creates a new security handler
func NewSecurityHandler() *SecurityHandler {
	return &SecurityHandler{}
}

// SecurityHandler implements api.SecurityHandler interface
type SecurityHandler struct{}

// HandleBearerAuth implements api.SecurityHandler.HandleBearerAuth
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Mock authentication - in production, validate JWT token
	return ctx, nil
}

// NewError implements api.Handler.NewError
func (h *Handler) NewError(ctx context.Context, err error) *api.ErrorStatusCode {
	return &api.ErrorStatusCode{
		StatusCode: 500,
		Response: api.Error{
			Code:    "500",
			Message: err.Error(),
		},
	}
}

// Trading session operations

// CreateTradeSession implements createTradeSession operation
func (h *Handler) CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest, params api.CreateTradeSessionParams) (api.CreateTradeSessionRes, error) {
	h.logger.Info("Creating trade session",
		zap.String("player_id", req.PlayerId),
		zap.String("participant_id", req.ParticipantId))

	// Validate request
	initiatorID, err := uuid.Parse(req.PlayerId)
	if err != nil {
		return &api.CreateTradeSessionBadRequest{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	participantID, err := uuid.Parse(req.ParticipantId)
	if err != nil {
		return &api.CreateTradeSessionBadRequest{
			Code:    "400",
			Message: "Invalid participant ID format",
		}, nil
	}

	// Check if players are already in active sessions
	activeSessions, err := h.repo.ListActiveTradeSessions(ctx, initiatorID)
	if err != nil {
		h.logger.Error("Failed to check active sessions", zap.Error(err))
		return &api.CreateTradeSessionInternalServerError{
			Code:    "500",
			Message: "Failed to check active trade sessions",
		}, nil
	}

	if len(activeSessions) > 0 {
		return &api.CreateTradeSessionConflict{
			Code:    "409",
			Message: "Player already in active trade session",
		}, nil
	}

	// Create new trade session
	sessionID := uuid.New()
	session := &models.TradeSession{
		ID:            sessionID,
		InitiatorID:   initiatorID,
		ParticipantID: participantID,
		Status:        string(models.StatusPending),
		CurrencyType:  "eurodollars", // Default currency
		TotalValue:    0,
		CreatedAt:     time.Now().UTC(),
		UpdatedAt:     time.Now().UTC(),
		ExpiresAt:     time.Now().UTC().Add(30 * time.Minute), // 30 minute timeout
		IsActive:      true,
	}

	if err := h.repo.CreateTradeSession(ctx, session); err != nil {
		h.logger.Error("Failed to create trade session", zap.Error(err))
		return &api.CreateTradeSessionInternalServerError{
			Code:    "500",
			Message: "Failed to create trade session",
		}, nil
	}

	return &api.CreateTradeSessionOK{
		Data: api.TradeSessionResponse{
			Id:            session.ID.String(),
			InitiatorId:   session.InitiatorID.String(),
			ParticipantId: session.ParticipantID.String(),
			Status:        api.TradeSessionStatus(session.Status),
			CurrencyType:  session.CurrencyType,
			TotalValue:    session.TotalValue,
			CreatedAt:     session.CreatedAt,
			ExpiresAt:     session.ExpiresAt,
			IsActive:      session.IsActive,
		},
	}, nil
}

// GetTradeSession implements getTradeSession operation
func (h *Handler) GetTradeSession(ctx context.Context, params api.GetTradeSessionParams) (api.GetTradeSessionRes, error) {
	sessionID, err := uuid.Parse(params.SessionId)
	if err != nil {
		return &api.GetTradeSessionBadRequest{
			Code:    "400",
			Message: "Invalid session ID format",
		}, nil
	}

	session, err := h.repo.GetTradeSession(ctx, sessionID)
	if err != nil {
		h.logger.Error("Failed to get trade session", zap.Error(err))
		return &api.GetTradeSessionNotFound{
			Code:    "404",
			Message: "Trade session not found",
		}, nil
	}

	return &api.GetTradeSessionOK{
		Data: api.TradeSessionResponse{
			Id:            session.ID.String(),
			InitiatorId:   session.InitiatorID.String(),
			ParticipantId: session.ParticipantID.String(),
			Status:        api.TradeSessionStatus(session.Status),
			CurrencyType:  session.CurrencyType,
			TotalValue:    session.TotalValue,
			CreatedAt:     session.CreatedAt,
			ExpiresAt:     session.ExpiresAt,
			IsActive:      session.IsActive,
		},
	}, nil
}

// UpdateTradeSession implements updateTradeSession operation
func (h *Handler) UpdateTradeSession(ctx context.Context, req *api.UpdateTradeRequest, params api.UpdateTradeSessionParams) (api.UpdateTradeSessionRes, error) {
	sessionID, err := uuid.Parse(params.SessionId)
	if err != nil {
		return &api.UpdateTradeSessionBadRequest{
			Code:    "400",
			Message: "Invalid session ID format",
		}, nil
	}

	session, err := h.repo.GetTradeSession(ctx, sessionID)
	if err != nil {
		return &api.UpdateTradeSessionNotFound{
			Code:    "404",
			Message: "Trade session not found",
		}, nil
	}

	// Update session fields
	if req.Status != nil {
		session.Status = string(*req.Status)
	}
	if req.CurrencyType != nil {
		session.CurrencyType = *req.CurrencyType
	}
	if req.TotalValue != nil {
		session.TotalValue = *req.TotalValue
	}

	session.UpdatedAt = time.Now().UTC()

	if err := h.repo.UpdateTradeSession(ctx, session); err != nil {
		h.logger.Error("Failed to update trade session", zap.Error(err))
		return &api.UpdateTradeSessionInternalServerError{
			Code:    "500",
			Message: "Failed to update trade session",
		}, nil
	}

	return &api.UpdateTradeSessionOK{
		Data: api.TradeSessionResponse{
			Id:            session.ID.String(),
			InitiatorId:   session.InitiatorID.String(),
			ParticipantId: session.ParticipantID.String(),
			Status:        api.TradeSessionStatus(session.Status),
			CurrencyType:  session.CurrencyType,
			TotalValue:    session.TotalValue,
			CreatedAt:     session.CreatedAt,
			ExpiresAt:     session.ExpiresAt,
			IsActive:      session.IsActive,
		},
	}, nil
}

// CancelTradeSession implements cancelTradeSession operation
func (h *Handler) CancelTradeSession(ctx context.Context, params api.CancelTradeSessionParams) (api.CancelTradeSessionRes, error) {
	sessionID, err := uuid.Parse(params.SessionId)
	if err != nil {
		return &api.CancelTradeSessionBadRequest{
			Code:    "400",
			Message: "Invalid session ID format",
		}, nil
	}

	session, err := h.repo.GetTradeSession(ctx, sessionID)
	if err != nil {
		return &api.CancelTradeSessionNotFound{
			Code:    "404",
			Message: "Trade session not found",
		}, nil
	}

	// Update session status
	session.Status = string(models.StatusCancelled)
	session.IsActive = false
	session.UpdatedAt = time.Now().UTC()

	if err := h.repo.UpdateTradeSession(ctx, session); err != nil {
		h.logger.Error("Failed to cancel trade session", zap.Error(err))
		return &api.CancelTradeSessionInternalServerError{
			Code:    "500",
			Message: "Failed to cancel trade session",
		}, nil
	}

	return &api.CancelTradeSessionOK{
		Data: api.CancelTradeResponse{
			SessionId: session.ID.String(),
			Status:    api.TradeSessionStatus(session.Status),
			Message:   "Trade session cancelled successfully",
		},
	}, nil
}

// ListTradeSessions implements listTradeSessions operation
func (h *Handler) ListTradeSessions(ctx context.Context, params api.ListTradeSessionsParams) (api.ListTradeSessionsRes, error) {
	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.ListTradeSessionsBadRequest{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	sessions, err := h.repo.ListActiveTradeSessions(ctx, playerID)
	if err != nil {
		h.logger.Error("Failed to list trade sessions", zap.Error(err))
		return &api.ListTradeSessionsInternalServerError{
			Code:    "500",
			Message: "Failed to list trade sessions",
		}, nil
	}

	// Convert to API response
	var sessionResponses []api.TradeSessionResponse
	for _, session := range sessions {
		sessionResponses = append(sessionResponses, api.TradeSessionResponse{
			Id:            session.ID.String(),
			InitiatorId:   session.InitiatorID.String(),
			ParticipantId: session.ParticipantID.String(),
			Status:        api.TradeSessionStatus(session.Status),
			CurrencyType:  session.CurrencyType,
			TotalValue:    session.TotalValue,
			CreatedAt:     session.CreatedAt,
			ExpiresAt:     session.ExpiresAt,
			IsActive:      session.IsActive,
		})
	}

	return &api.ListTradeSessionsOK{
		Data: api.TradeSessionList{
			Sessions: sessionResponses,
			Total:    int64(len(sessionResponses)),
		},
	}, nil
}

// ExecuteTrade implements executeTrade operation
func (h *Handler) ExecuteTrade(ctx context.Context, req *api.ExecuteTradeRequest, params api.ExecuteTradeParams) (api.ExecuteTradeRes, error) {
	sessionID, err := uuid.Parse(params.SessionId)
	if err != nil {
		return &api.ExecuteTradeBadRequest{
			Code:    "400",
			Message: "Invalid session ID format",
		}, nil
	}

	// Get session
	session, err := h.repo.GetTradeSession(ctx, sessionID)
	if err != nil {
		return &api.ExecuteTradeNotFound{
			Code:    "404",
			Message: "Trade session not found",
		}, nil
	}

	if !session.IsActive || session.Status != string(models.StatusActive) {
		return &api.ExecuteTradeConflict{
			Code:    "409",
			Message: "Trade session is not active",
		}, nil
	}

	// Create trade transaction
	transactionID := uuid.New()
	itemID, err := uuid.Parse(req.ItemId)
	if err != nil {
		return &api.ExecuteTradeBadRequest{
			Code:    "400",
			Message: "Invalid item ID format",
		}, nil
	}

	transaction := &models.TradeTransaction{
		ID:             transactionID,
		SessionID:      sessionID,
		BuyerID:        session.ParticipantID, // Assuming participant is buyer
		SellerID:       session.InitiatorID,   // Assuming initiator is seller
		ItemID:         itemID,
		Quantity:       req.Quantity,
		TotalPrice:     req.TotalPrice,
		CurrencyType:   session.CurrencyType,
		TransactionFee: int64(float64(req.TotalPrice) * 0.05), // 5% fee
		Status:         string(models.TxStatusCompleted),
		ExecutedAt:     time.Now().UTC(),
	}

	if err := h.repo.CreateTradeTransaction(ctx, transaction); err != nil {
		h.logger.Error("Failed to create trade transaction", zap.Error(err))
		return &api.ExecuteTradeInternalServerError{
			Code:    "500",
			Message: "Failed to execute trade",
		}, nil
	}

	// Update session status
	session.Status = string(models.StatusCompleted)
	session.IsActive = false
	session.UpdatedAt = time.Now().UTC()

	if err := h.repo.UpdateTradeSession(ctx, session); err != nil {
		h.logger.Error("Failed to update trade session after execution", zap.Error(err))
		// Don't return error here as transaction was successful
	}

	return &api.ExecuteTradeOK{
		Data: api.TradeResult{
			TradeId:       transaction.ID.String(),
			SessionId:     session.ID.String(),
			BuyerId:       transaction.BuyerID.String(),
			SellerId:      transaction.SellerID.String(),
			ItemId:        transaction.ItemID.String(),
			Quantity:      transaction.Quantity,
			TotalPrice:    transaction.TotalPrice,
			CurrencyType:  transaction.CurrencyType,
			TransactionFee: transaction.TransactionFee,
			ExecutedAt:    transaction.ExecutedAt,
			Success:       true,
		},
	}, nil
}

// GetTradeHistory implements getTradeHistory operation
func (h *Handler) GetTradeHistory(ctx context.Context, params api.GetTradeHistoryParams) (api.GetTradeHistoryRes, error) {
	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.GetTradeHistoryBadRequest{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	// Default pagination
	limit := 50
	offset := 0
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = int(*params.Limit)
	}
	if params.Offset != nil && *params.Offset >= 0 {
		offset = int(*params.Offset)
	}

	transactions, err := h.repo.GetTradeHistory(ctx, playerID, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get trade history", zap.Error(err))
		return &api.GetTradeHistoryInternalServerError{
			Code:    "500",
			Message: "Failed to get trade history",
		}, nil
	}

	// Convert to API response
	var transactionResponses []api.TradeTransaction
	for _, tx := range transactions {
		transactionResponses = append(transactionResponses, api.TradeTransaction{
			Id:             tx.ID.String(),
			SessionId:      tx.SessionID.String(),
			BuyerId:       tx.BuyerID.String(),
			SellerId:      tx.SellerID.String(),
			ItemId:        tx.ItemID.String(),
			Quantity:      tx.Quantity,
			TotalPrice:    tx.TotalPrice,
			CurrencyType:  tx.CurrencyType,
			TransactionFee: tx.TransactionFee,
			Status:        api.TransactionStatus(tx.Status),
			ExecutedAt:    tx.ExecutedAt,
		})
	}

	return &api.GetTradeHistoryOK{
		Data: api.TradeHistoryResponse{
			Transactions: transactionResponses,
			Total:        int64(len(transactionResponses)),
			Limit:        int64(limit),
			Offset:       int64(offset),
		},
	}, nil
}

// Health check implementation
func (h *Handler) HealthCheck(ctx context.Context) api.HealthCheckRes {
	return &api.HealthOK{
		Data: api.HealthResponse{
			Status:  "healthy",
			Message: "Trading service is operational",
			Timestamp: time.Now().UTC(),
		},
	}
}