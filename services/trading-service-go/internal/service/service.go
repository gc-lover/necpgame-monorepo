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

// ============================================================================
// TRADE ORDERS IMPLEMENTATION
// ============================================================================

// CreateTradeOrder creates a new trade order (buy/sell)
func (h *Handler) CreateTradeOrder(ctx context.Context, req api.CreateTradeOrderRequest) api.CreateTradeOrderRes {
	playerID, err := uuid.Parse(req.PlayerID)
	if err != nil {
		h.logger.Error("Invalid player ID", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid player ID format",
			},
		}
	}

	itemID, err := uuid.Parse(req.ItemID)
	if err != nil {
		h.logger.Error("Invalid item ID", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid item ID format",
			},
		}
	}

	order := &models.TradeOrder{
		ID:          uuid.New(),
		PlayerID:    playerID,
		ItemID:      itemID,
		OrderType:   req.OrderType,
		OrderMode:   req.OrderMode,
		ItemName:    req.ItemName,
		Quantity:    req.Quantity,
		Price:       req.Price,
		MinQuantity: req.MinQuantity,
		MaxQuantity: req.MaxQuantity,
		CurrencyType: req.CurrencyType,
		IsActive:    true,
		IsPartial:   req.IsPartial,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(24 * time.Hour), // Default 24h expiry
	}

	if req.ExpiresAt != nil {
		order.ExpiresAt = *req.ExpiresAt
	}

	err = h.repo.CreateTradeOrder(ctx, order)
	if err != nil {
		h.logger.Error("Failed to create trade order", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.ErrorResponse{
				Error:   "InternalError",
				Message: "Failed to create trade order",
			},
		}
	}

	// Try to match the order immediately
	go h.matchOrder(ctx, order)

	return &api.CreateTradeOrderResponse{
		OrderID:   order.ID.String(),
		Status:    "created",
		Message:   "Trade order created successfully",
		CreatedAt: order.CreatedAt,
	}
}

// GetTradeOrder retrieves a trade order by ID
func (h *Handler) GetTradeOrder(ctx context.Context, params api.GetTradeOrderParams) api.GetTradeOrderRes {
	orderID, err := uuid.Parse(params.OrderID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid order ID format",
			},
		}
	}

	order, err := h.repo.GetTradeOrder(ctx, orderID)
	if err != nil {
		h.logger.Error("Failed to get trade order", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 404,
			Response: api.ErrorResponse{
				Error:   "NotFound",
				Message: "Trade order not found",
			},
		}
	}

	return &api.TradeOrderResponse{
		ID:             order.ID.String(),
		PlayerID:       order.PlayerID.String(),
		ItemID:         order.ItemID.String(),
		OrderType:      order.OrderType,
		OrderMode:      order.OrderMode,
		ItemName:       order.ItemName,
		Quantity:       order.Quantity,
		Price:          order.Price,
		MinQuantity:    order.MinQuantity,
		MaxQuantity:    order.MaxQuantity,
		FilledQuantity: order.FilledQuantity,
		CurrencyType:   order.CurrencyType,
		Status:         string(order.Status),
		IsActive:       order.IsActive,
		IsPartial:      order.IsPartial,
		CreatedAt:      order.CreatedAt,
		UpdatedAt:      order.UpdatedAt,
		ExpiresAt:      order.ExpiresAt,
	}
}

// CancelTradeOrder cancels an active trade order
func (h *Handler) CancelTradeOrder(ctx context.Context, params api.CancelTradeOrderParams) api.CancelTradeOrderRes {
	orderID, err := uuid.Parse(params.OrderID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid order ID format",
			},
		}
	}

	err = h.repo.CancelTradeOrder(ctx, orderID)
	if err != nil {
		h.logger.Error("Failed to cancel trade order", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.ErrorResponse{
				Error:   "InternalError",
				Message: "Failed to cancel trade order",
			},
		}
	}

	return &api.CancelTradeOrderResponse{
		OrderID: params.OrderID,
		Status:  "cancelled",
		Message: "Trade order cancelled successfully",
	}
}

// ListTradeOrders lists trade orders for a player
func (h *Handler) ListTradeOrders(ctx context.Context, params api.ListTradeOrdersParams) api.ListTradeOrdersRes {
	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid player ID format",
			},
		}
	}

	orders, err := h.repo.ListPlayerTradeOrders(ctx, playerID, params.Status, params.OrderType)
	if err != nil {
		h.logger.Error("Failed to list trade orders", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.ErrorResponse{
				Error:   "InternalError",
				Message: "Failed to list trade orders",
			},
		}
	}

	var response []api.TradeOrderSummary
	for _, order := range orders {
		response = append(response, api.TradeOrderSummary{
			ID:             order.ID.String(),
			ItemName:       order.ItemName,
			OrderType:      order.OrderType,
			OrderMode:      order.OrderMode,
			Quantity:       order.Quantity,
			Price:          order.Price,
			FilledQuantity: order.FilledQuantity,
			Status:         string(order.Status),
			CreatedAt:      order.CreatedAt,
			ExpiresAt:      order.ExpiresAt,
		})
	}

	return &api.TradeOrdersListResponse{
		Orders: response,
		Count:  len(response),
	}
}

// ============================================================================
// TRADE CONTRACTS IMPLEMENTATION
// ============================================================================

// CreateTradeContract creates a new trade contract
func (h *Handler) CreateTradeContract(ctx context.Context, req api.CreateTradeContractRequest) api.CreateTradeContractRes {
	sellerID, err := uuid.Parse(req.SellerID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid seller ID format",
			},
		}
	}

	buyerID, err := uuid.Parse(req.BuyerID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid buyer ID format",
			},
		}
	}

	contract := &models.TradeContract{
		ID:              uuid.New(),
		SellerID:        sellerID,
		BuyerID:         buyerID,
		ContractType:    req.ContractType,
		Status:          string(models.ContractStatusActive),
		ItemName:        req.ItemName,
		TotalQuantity:   req.TotalQuantity,
		UnitPrice:       req.UnitPrice,
		EscrowAmount:    req.EscrowAmount,
		DeliveryDeadline: req.DeliveryDeadline,
		IsEscrowActive:  req.IsEscrowActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(30 * 24 * time.Hour), // Default 30 days
	}

	if req.ExpiresAt != nil {
		contract.ExpiresAt = *req.ExpiresAt
	}

	err = h.repo.CreateTradeContract(ctx, contract)
	if err != nil {
		h.logger.Error("Failed to create trade contract", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.ErrorResponse{
				Error:   "InternalError",
				Message: "Failed to create trade contract",
			},
		}
	}

	return &api.CreateTradeContractResponse{
		ContractID: contract.ID.String(),
		Status:     "created",
		Message:    "Trade contract created successfully",
		CreatedAt:  contract.CreatedAt,
	}
}

// GetTradeContract retrieves a trade contract by ID
func (h *Handler) GetTradeContract(ctx context.Context, params api.GetTradeContractParams) api.GetTradeContractRes {
	contractID, err := uuid.Parse(params.ContractID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid contract ID format",
			},
		}
	}

	contract, err := h.repo.GetTradeContract(ctx, contractID)
	if err != nil {
		h.logger.Error("Failed to get trade contract", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 404,
			Response: api.ErrorResponse{
				Error:   "NotFound",
				Message: "Trade contract not found",
			},
		}
	}

	var deliveries []api.ContractDeliverySummary
	for _, delivery := range contract.Deliveries {
		deliveries = append(deliveries, api.ContractDeliverySummary{
			ID:          delivery.ID.String(),
			Quantity:    delivery.Quantity,
			DeliveredAt: delivery.DeliveredAt,
			Status:      delivery.Status,
		})
	}

	return &api.TradeContractResponse{
		ID:               contract.ID.String(),
		SellerID:         contract.SellerID.String(),
		BuyerID:          contract.BuyerID.String(),
		ContractType:     contract.ContractType,
		Status:           contract.Status,
		ItemName:         contract.ItemName,
		TotalQuantity:    contract.TotalQuantity,
		DeliveredQuantity: contract.DeliveredQuantity,
		UnitPrice:        contract.UnitPrice,
		EscrowAmount:     contract.EscrowAmount,
		DeliveryDeadline: contract.DeliveryDeadline,
		ExpiresAt:        contract.ExpiresAt,
		Deliveries:       deliveries,
		IsEscrowActive:   contract.IsEscrowActive,
		IsCompleted:      contract.IsCompleted,
		CreatedAt:        contract.CreatedAt,
		UpdatedAt:        contract.UpdatedAt,
	}
}

// ============================================================================
// AUCTION IMPLEMENTATION
// ============================================================================

// CreateAuction creates a new auction
func (h *Handler) CreateAuction(ctx context.Context, req api.CreateAuctionRequest) api.CreateAuctionRes {
	sellerID, err := uuid.Parse(req.SellerID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid seller ID format",
			},
		}
	}

	itemID, err := uuid.Parse(req.ItemID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid item ID format",
			},
		}
	}

	auction := &models.Auction{
		ID:            uuid.New(),
		SellerID:      sellerID,
		ItemID:        itemID,
		ItemName:      req.ItemName,
		AuctionType:   req.AuctionType,
		Status:        string(models.AuctionStatusActive),
		StartingPrice: req.StartingPrice,
		CurrentPrice:  req.StartingPrice,
		ReservePrice:  req.ReservePrice,
		Quantity:      req.Quantity,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
		EndsAt:        req.EndsAt,
	}

	err = h.repo.CreateAuction(ctx, auction)
	if err != nil {
		h.logger.Error("Failed to create auction", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.ErrorResponse{
				Error:   "InternalError",
				Message: "Failed to create auction",
			},
		}
	}

	return &api.CreateAuctionResponse{
		AuctionID: auction.ID.String(),
		Status:    "created",
		Message:   "Auction created successfully",
		CreatedAt: auction.CreatedAt,
	}
}

// PlaceAuctionBid places a bid on an auction
func (h *Handler) PlaceAuctionBid(ctx context.Context, req api.PlaceAuctionBidRequest) api.PlaceAuctionBidRes {
	auctionID, err := uuid.Parse(req.AuctionID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid auction ID format",
			},
		}
	}

	bidderID, err := uuid.Parse(req.BidderID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: 400,
			Response: api.ErrorResponse{
				Error:   "InvalidRequest",
				Message: "Invalid bidder ID format",
			},
		}
	}

	bid := &models.AuctionBid{
		ID:        uuid.New(),
		AuctionID: auctionID,
		BidderID:  bidderID,
		Amount:    req.Amount,
		BidTime:   time.Now(),
	}

	err = h.repo.PlaceAuctionBid(ctx, bid)
	if err != nil {
		h.logger.Error("Failed to place auction bid", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.ErrorResponse{
				Error:   "InternalError",
				Message: "Failed to place bid",
			},
		}
	}

	return &api.PlaceAuctionBidResponse{
		BidID:    bid.ID.String(),
		Status:   "placed",
		Message:  "Bid placed successfully",
		BidTime:  bid.BidTime,
	}
}

// ============================================================================
// ORDER MATCHING ENGINE
// ============================================================================

// matchOrder attempts to match a trade order with existing orders
func (h *Handler) matchOrder(ctx context.Context, order *models.TradeOrder) {
	h.logger.Info("Attempting to match order",
		zap.String("order_id", order.ID.String()),
		zap.String("order_type", order.OrderType),
		zap.String("item_name", order.ItemName))

	// Get opposite orders (buy for sell, sell for buy)
	oppositeType := "sell"
	if order.OrderType == "sell" {
		oppositeType = "buy"
	}

	oppositeOrders, err := h.repo.GetMatchingOrders(ctx, order.ItemID, oppositeType, order.OrderMode)
	if err != nil {
		h.logger.Error("Failed to get matching orders", zap.Error(err))
		return
	}

	remainingQuantity := order.Quantity - order.FilledQuantity

	for _, oppositeOrder := range oppositeOrders {
		if remainingQuantity <= 0 {
			break
		}

		// Check if prices match
		canMatch := false
		if order.OrderType == "buy" && oppositeOrder.OrderType == "sell" {
			canMatch = order.Price >= oppositeOrder.Price
		} else if order.OrderType == "sell" && oppositeOrder.OrderType == "buy" {
			canMatch = order.Price <= oppositeOrder.Price
		}

		if !canMatch {
			continue
		}

		// Calculate trade quantity
		tradeQuantity := remainingQuantity
		if oppositeOrder.Quantity-oppositeOrder.FilledQuantity < tradeQuantity {
			tradeQuantity = oppositeOrder.Quantity - oppositeOrder.FilledQuantity
		}

		// Execute trade
		err = h.executeOrderMatch(ctx, order, oppositeOrder, tradeQuantity, oppositeOrder.Price)
		if err != nil {
			h.logger.Error("Failed to execute order match", zap.Error(err))
			continue
		}

		remainingQuantity -= tradeQuantity
		order.FilledQuantity += tradeQuantity
		oppositeOrder.FilledQuantity += tradeQuantity

		// Update order status
		if order.FilledQuantity >= order.Quantity {
			order.Status = models.OrderStatusFilled
		} else {
			order.Status = models.OrderStatusPartial
		}

		if oppositeOrder.FilledQuantity >= oppositeOrder.Quantity {
			oppositeOrder.Status = models.OrderStatusFilled
		} else {
			oppositeOrder.Status = models.OrderStatusPartial
		}

		// Update orders in database
		h.repo.UpdateTradeOrder(ctx, order)
		h.repo.UpdateTradeOrder(ctx, oppositeOrder)
	}

	// Update original order
	if remainingQuantity > 0 && order.FilledQuantity > 0 {
		order.Status = models.OrderStatusPartial
	}
	h.repo.UpdateTradeOrder(ctx, order)
}

// executeOrderMatch executes a matched trade between two orders
func (h *Handler) executeOrderMatch(ctx context.Context, buyOrder, sellOrder *models.TradeOrder, quantity int32, price int64) error {
	// Create trade transaction
	transaction := &models.TradeTransaction{
		ID:             uuid.New(),
		SessionID:      uuid.Nil, // No session for order matching
		BuyerID:        buyOrder.PlayerID,
		SellerID:       sellOrder.PlayerID,
		ItemID:         buyOrder.ItemID,
		Quantity:       quantity,
		TotalPrice:     price * int64(quantity),
		CurrencyType:   buyOrder.CurrencyType,
		TransactionFee: int64(float64(price*int64(quantity)) * 0.02), // 2% fee
		Status:         string(models.TxStatusCompleted),
		ExecutedAt:     time.Now(),
	}

	return h.repo.CreateTradeTransaction(ctx, transaction)
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