package server

import (
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/economy-service-go/models"
	"github.com/necpgame/economy-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertCreateTradeSessionRequestFromAPI(req api.CreateTradeSessionRequest) *models.CreateTradeRequest {
	return &models.CreateTradeRequest{
		RecipientID: uuid.UUID(req.TargetCharacterId),
		ZoneID:      nil,
	}
}

func convertTradeSessionToAPI(session *models.TradeSession) *api.TradeSession {
	if session == nil {
		return nil
	}

	sessionID := openapi_types.UUID(session.ID)
	initiatorID := openapi_types.UUID(session.InitiatorID)
	targetID := openapi_types.UUID(session.RecipientID)

	status := convertTradeStatusToAPI(session.Status)
	createdAt := session.CreatedAt
	updatedAt := session.UpdatedAt
	expiresAt := session.ExpiresAt

	initiatorOffer := convertTradeOfferToAPI(session.InitiatorOffer)
	targetOffer := convertTradeOfferToAPI(session.RecipientOffer)

	initiatorConfirmed := session.InitiatorConfirmed
	targetConfirmed := session.RecipientConfirmed

	return &api.TradeSession{
		Id:                 &sessionID,
		InitiatorId:        &initiatorID,
		TargetId:           &targetID,
		InitiatorOffer:     initiatorOffer,
		TargetOffer:        targetOffer,
		InitiatorConfirmed: &initiatorConfirmed,
		TargetConfirmed:    &targetConfirmed,
		Status:             &status,
		CreatedAt:          &createdAt,
		UpdatedAt:          &updatedAt,
		ExpiresAt:          &expiresAt,
	}
}

func convertTradeOfferToAPI(offer models.TradeOffer) *api.TradeOffer {
	items := make([]api.TradeItem, 0, len(offer.Items))
	for _, item := range offer.Items {
		if itemID, ok := item["item_id"].(string); ok {
			if parsedID, err := uuid.Parse(itemID); err == nil {
				quantity := 1
				if qty, ok := item["quantity"].(int); ok {
					quantity = qty
				} else if qty, ok := item["quantity"].(float64); ok {
					quantity = int(qty)
				}
				items = append(items, api.TradeItem{
					ItemId:   openapi_types.UUID(parsedID),
					Quantity: quantity,
				})
			}
		}
	}

	currency := 0
	if len(offer.Currency) > 0 {
		for _, v := range offer.Currency {
			currency += v
		}
	}

	return &api.TradeOffer{
		Items:    &items,
		Currency: &currency,
	}
}

func convertTradeStatusToAPI(status models.TradeStatus) api.TradeSessionStatus {
	switch status {
	case models.TradeStatusPending:
		return api.Pending
	case models.TradeStatusActive:
		return api.Active
	case models.TradeStatusConfirmed:
		return api.Confirmed
	case models.TradeStatusCompleted:
		return api.Completed
	case models.TradeStatusCancelled:
		return api.Cancelled
	case models.TradeStatusExpired:
		return api.Expired
	default:
		return api.Pending
	}
}

func convertTradeSessionToAudit(session *models.TradeSession) *api.TradeAudit {
	if session == nil {
		return nil
	}

	sessionID := openapi_types.UUID(session.ID)
	createdAt := session.CreatedAt
	completedAt := session.CompletedAt

	events := make([]struct {
		ActorId   *openapi_types.UUID     `json:"actor_id,omitempty"`
		Details   *map[string]interface{} `json:"details,omitempty"`
		EventType *string                 `json:"event_type,omitempty"`
		Timestamp *time.Time              `json:"timestamp,omitempty"`
	}, 0)

	initiatorID := openapi_types.UUID(session.InitiatorID)
	createdEventType := "created"
	events = append(events, struct {
		ActorId   *openapi_types.UUID     `json:"actor_id,omitempty"`
		Details   *map[string]interface{} `json:"details,omitempty"`
		EventType *string                 `json:"event_type,omitempty"`
		Timestamp *time.Time              `json:"timestamp,omitempty"`
	}{
		ActorId:   &initiatorID,
		EventType: &createdEventType,
		Timestamp: &createdAt,
	})

	if session.UpdatedAt.After(session.CreatedAt) {
		updatedEventType := "updated"
		events = append(events, struct {
			ActorId   *openapi_types.UUID     `json:"actor_id,omitempty"`
			Details   *map[string]interface{} `json:"details,omitempty"`
			EventType *string                 `json:"event_type,omitempty"`
			Timestamp *time.Time              `json:"timestamp,omitempty"`
		}{
			EventType: &updatedEventType,
			Timestamp: &session.UpdatedAt,
		})
	}

	if session.CompletedAt != nil {
		completedEventType := "completed"
		events = append(events, struct {
			ActorId   *openapi_types.UUID     `json:"actor_id,omitempty"`
			Details   *map[string]interface{} `json:"details,omitempty"`
			EventType *string                 `json:"event_type,omitempty"`
			Timestamp *time.Time              `json:"timestamp,omitempty"`
		}{
			EventType: &completedEventType,
			Timestamp: completedAt,
		})
	}

	return &api.TradeAudit{
		SessionId:   &sessionID,
		CreatedAt:   &createdAt,
		CompletedAt: completedAt,
		Events:      &events,
	}
}

func convertTradeSessionToConfirmationStatus(session *models.TradeSession) *api.TradeConfirmationStatus {
	if session == nil {
		return nil
	}

	sessionID := openapi_types.UUID(session.ID)
	initiatorConfirmed := session.InitiatorConfirmed
	recipientConfirmed := session.RecipientConfirmed
	bothConfirmed := initiatorConfirmed && recipientConfirmed
	canExecute := bothConfirmed && session.Status == models.TradeStatusConfirmed

	return &api.TradeConfirmationStatus{
		SessionId:          &sessionID,
		InitiatorConfirmed: &initiatorConfirmed,
		RecipientConfirmed: &recipientConfirmed,
		BothConfirmed:      &bothConfirmed,
		CanExecute:         &canExecute,
	}
}

