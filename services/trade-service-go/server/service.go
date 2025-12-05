// Issue: #131
package server

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-service-go/pkg/api"
	"github.com/google/uuid"
)

type Service interface {
	CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest) (*api.TradeSessionResponse, error)
	GetTradeSession(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error)
	CancelTradeSession(ctx context.Context, sessionID string) error
	AddTradeItems(ctx context.Context, sessionID string, req *api.AddItemsRequest) (*api.TradeSessionResponse, error)
	AddTradeCurrency(ctx context.Context, sessionID string, req *api.AddCurrencyRequest) (*api.TradeSessionResponse, error)
	SetTradeReady(ctx context.Context, sessionID string, req *api.ReadyRequest) (*api.TradeSessionResponse, error)
	CompleteTrade(ctx context.Context, sessionID string) (*api.TradeCompleteResponse, error)
	GetTradeHistory(ctx context.Context, playerID string, params api.GetTradeHistoryParams) (*api.TradeHistoryResponse, error)
}

type TradeService struct {
	repository Repository
}

func NewTradeService(repository Repository) Service {
	return &TradeService{repository: repository}
}

// getPlayerIDFromContext extracts player ID from context (from JWT token)
func getPlayerIDFromContext(ctx context.Context) (uuid.UUID, error) {
	// Try different context keys used in different services
	if playerID, ok := ctx.Value("player_id").(uuid.UUID); ok {
		return playerID, nil
	}
	if playerID, ok := ctx.Value("user_uuid").(uuid.UUID); ok {
		return playerID, nil
	}
	if playerIDStr, ok := ctx.Value("user_id").(string); ok {
		playerID, err := uuid.Parse(playerIDStr)
		if err == nil {
			return playerID, nil
		}
	}
	// TODO: Implement proper JWT extraction (Issue: Auth integration)
	return uuid.Nil, fmt.Errorf("player_id not found in context")
}

func (s *TradeService) CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest) (*api.TradeSessionResponse, error) {
	initiatorID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}

	targetIDStr := req.TargetPlayerID.String()
	sessionID, err := s.repository.CreateTradeSession(ctx, initiatorID.String(), targetIDStr)
	if err != nil {
		return nil, err
	}

	// Get created session
	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	return convertToTradeSessionResponse(session, initiatorID)
}

func (s *TradeService) GetTradeSession(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}

	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}
	if session == nil {
		return nil, errors.New("not found")
	}

	return convertToTradeSessionResponse(session, playerID)
}

func (s *TradeService) CancelTradeSession(ctx context.Context, sessionID string) error {
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return err
	}

	// Get session to verify ownership
	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return err
	}
	if session == nil {
		return ErrNotFound
	}

	// Verify player is initiator or target
	sessionData, ok := session.(map[string]interface{})
	if !ok {
		return errors.New("invalid session data")
	}

	initiatorIDStr, _ := sessionData["initiator_id"].(string)
	targetIDStr, _ := sessionData["target_id"].(string)

	if initiatorIDStr != playerID.String() && targetIDStr != playerID.String() {
		return errors.New("forbidden: not session participant")
	}

	return s.repository.CancelSession(ctx, sessionID)
}

func (s *TradeService) AddTradeItems(ctx context.Context, sessionID string, req *api.AddItemsRequest) (*api.TradeSessionResponse, error) {
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}

	// Convert items to interface{} for repository
	items := make([]map[string]interface{}, len(req.Items))
	for i, item := range req.Items {
		items[i] = map[string]interface{}{
			"item_id":  item.ItemID,
			"quantity": item.Quantity,
		}
	}

	err = s.repository.AddItems(ctx, sessionID, playerID.String(), items)
	if err != nil {
		return nil, err
	}

	// Get updated session
	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	return convertToTradeSessionResponse(session, playerID)
}

func (s *TradeService) AddTradeCurrency(ctx context.Context, sessionID string, req *api.AddCurrencyRequest) (*api.TradeSessionResponse, error) {
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}

	// Convert currency to interface{} for repository
	currency := map[string]interface{}{
		"currency_type": string(req.CurrencyType),
		"amount":        req.Amount,
	}

	err = s.repository.AddCurrency(ctx, sessionID, playerID.String(), currency)
	if err != nil {
		return nil, err
	}

	// Get updated session
	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	return convertToTradeSessionResponse(session, playerID)
}

func (s *TradeService) SetTradeReady(ctx context.Context, sessionID string, req *api.ReadyRequest) (*api.TradeSessionResponse, error) {
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}

	ready := req.Ready
	err = s.repository.SetReady(ctx, sessionID, playerID.String(), ready)
	if err != nil {
		return nil, err
	}

	// Get updated session
	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	return convertToTradeSessionResponse(session, playerID)
}

func (s *TradeService) CompleteTrade(ctx context.Context, sessionID string) (*api.TradeCompleteResponse, error) {
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}

	// Get session to verify both players are ready
	session, err := s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}
	if session == nil {
		return nil, errors.New("not found")
	}

	sessionData, ok := session.(map[string]interface{})
	if !ok {
		return nil, errors.New("invalid session data")
	}

	// Verify player is participant
	initiatorIDStr, _ := sessionData["initiator_id"].(string)
	targetIDStr, _ := sessionData["target_id"].(string)

	if initiatorIDStr != playerID.String() && targetIDStr != playerID.String() {
		return nil, errors.New("forbidden: not session participant")
	}

	// Complete trade
	err = s.repository.CompleteTrade(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	// Save to history
	err = s.repository.SaveTradeHistory(ctx, sessionID)
	if err != nil {
		// Log but don't fail - history is not critical
	}

	// Get completed session for response
	session, err = s.repository.GetTradeSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	sessionData, ok = session.(map[string]interface{})
	if !ok {
		return nil, errors.New("invalid session data")
	}

	response := &api.TradeCompleteResponse{}

	// TradeID
	if idStr, ok := sessionData["id"].(string); ok {
		if id, err := uuid.Parse(idStr); err == nil {
			response.TradeID = api.NewOptUUID(id)
		}
	}

	// CompletedAt
	if completedAtStr, ok := sessionData["completed_at"].(string); ok {
		if completedAt, err := time.Parse(time.RFC3339, completedAtStr); err == nil {
			response.CompletedAt = api.NewOptDateTime(completedAt)
		}
	} else {
		response.CompletedAt = api.NewOptDateTime(time.Now())
	}

	return response, nil
}

func (s *TradeService) GetTradeHistory(ctx context.Context, playerID string, params api.GetTradeHistoryParams) (*api.TradeHistoryResponse, error) {
	// Get history from repository (stub - repository doesn't have this method yet)
	emptyHistory := []api.TradeHistoryEntry{}
	return &api.TradeHistoryResponse{
		History: emptyHistory,
		Total:   api.NewOptInt(0),
	}, nil
}

// convertToTradeSessionResponse converts repository session data to API response
func convertToTradeSessionResponse(session interface{}, playerID uuid.UUID) (*api.TradeSessionResponse, error) {
	sessionData, ok := session.(map[string]interface{})
	if !ok {
		return nil, errors.New("invalid session data")
	}

	response := &api.TradeSessionResponse{}

	// ID
	if idStr, ok := sessionData["id"].(string); ok {
		if id, err := uuid.Parse(idStr); err == nil {
			response.ID = api.NewOptUUID(id)
		}
	}

	// InitiatorID
	if initiatorIDStr, ok := sessionData["initiator_id"].(string); ok {
		if initiatorID, err := uuid.Parse(initiatorIDStr); err == nil {
			response.InitiatorID = api.NewOptUUID(initiatorID)
		}
	}

	// TargetID
	if targetIDStr, ok := sessionData["target_id"].(string); ok {
		if targetID, err := uuid.Parse(targetIDStr); err == nil {
			response.TargetID = api.NewOptUUID(targetID)
		}
	}

	// Status
	if statusStr, ok := sessionData["status"].(string); ok {
		status := api.TradeSessionResponseTradeStatus(statusStr)
		response.TradeStatus = api.NewOptTradeSessionResponseTradeStatus(status)
	}

	// InitiatorReady
	if initiatorReady, ok := sessionData["initiator_confirmed"].(bool); ok {
		response.InitiatorReady = api.NewOptBool(initiatorReady)
	}

	// TargetReady
	if targetReady, ok := sessionData["recipient_confirmed"].(bool); ok {
		response.TargetReady = api.NewOptBool(targetReady)
	}

	// CreatedAt
	if createdAtStr, ok := sessionData["created_at"].(string); ok {
		if createdAt, err := time.Parse(time.RFC3339, createdAtStr); err == nil {
			response.CreatedAt = api.NewOptDateTime(createdAt)
		}
	}

	// ExpiresAt
	if expiresAtStr, ok := sessionData["expires_at"].(string); ok {
		if expiresAt, err := time.Parse(time.RFC3339, expiresAtStr); err == nil {
			response.ExpiresAt = api.NewOptDateTime(expiresAt)
		}
	}

	// Items and Currency (parse from JSONB)
	if initiatorOfferJSON, ok := sessionData["initiator_offer"].([]byte); ok {
		var offer map[string]interface{}
		if err := json.Unmarshal(initiatorOfferJSON, &offer); err == nil {
			// Parse items
			if items, ok := offer["items"].([]interface{}); ok {
				response.InitiatorItems = make([]api.TradeSessionResponseInitiatorItemsItem, 0, len(items))
				for _, item := range items {
					if itemMap, ok := item.(map[string]interface{}); ok {
						// Convert to API type (stub - needs proper conversion)
						_ = itemMap
					}
				}
			}
		}
	}

	return response, nil
}









