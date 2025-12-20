package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/pkg/api"
)

func convertInitiateTradeRequestFromAPI(req *api.InitiateTradeRequest) *models.CreateTradeRequest {
	if req == nil {
		return nil
	}
	return &models.CreateTradeRequest{
		RecipientID: req.TargetPlayerID,
		ZoneID:      nil,
	}
}

func convertTradeSessionToAPI(session *models.TradeSession) *api.TradeSession {
	if session == nil {
		return nil
	}

	status := convertTradeStatusToAPI(session.Status)
	initiatorOffer := convertTradeOfferToInitiatorOffer(session.InitiatorOffer)
	targetOffer := convertTradeOfferToTargetOffer(session.RecipientOffer)

	expiresAt := api.OptDateTime{}
	if !session.ExpiresAt.IsZero() {
		expiresAt = api.NewOptDateTime(session.ExpiresAt)
	}

	return &api.TradeSession{
		TradeID:        session.ID,
		InitiatorID:    session.InitiatorID,
		TargetPlayerID: session.RecipientID,
		Status:         status,
		InitiatorOffer: initiatorOffer,
		TargetOffer:    targetOffer,
		CreatedAt:      session.CreatedAt,
		ExpiresAt:      expiresAt,
	}
}

func convertTradeOfferToInitiatorOffer(offer models.TradeOffer) api.OptTradeSessionInitiatorOffer {
	items := make([]api.TradeSessionInitiatorOfferItemsItem, 0, len(offer.Items))
	for range offer.Items {
		// TradeSessionInitiatorOfferItemsItem is empty struct, just append
		items = append(items, api.TradeSessionInitiatorOfferItemsItem{})
	}

	currency := float32(0)
	if len(offer.Currency) > 0 {
		for _, v := range offer.Currency {
			currency += float32(v)
		}
	}

	result := api.TradeSessionInitiatorOffer{
		Items:    items,
		Currency: api.NewOptFloat32(currency),
	}

	return api.NewOptTradeSessionInitiatorOffer(result)
}

func convertTradeOfferToTargetOffer(offer models.TradeOffer) api.OptTradeSessionTargetOffer {
	items := make([]api.TradeSessionTargetOfferItemsItem, 0, len(offer.Items))
	for range offer.Items {
		// TradeSessionTargetOfferItemsItem is empty struct, just append
		items = append(items, api.TradeSessionTargetOfferItemsItem{})
	}

	currency := float32(0)
	if len(offer.Currency) > 0 {
		for _, v := range offer.Currency {
			currency += float32(v)
		}
	}

	result := api.TradeSessionTargetOffer{
		Items:    items,
		Currency: api.NewOptFloat32(currency),
	}

	return api.NewOptTradeSessionTargetOffer(result)
}

func convertTradeStatusToAPI(status models.TradeStatus) api.TradeSessionStatus {
	switch status {
	case models.TradeStatusPending:
		return api.TradeSessionStatusPending
	case models.TradeStatusActive:
		return api.TradeSessionStatusAccepted // Map Active to Accepted
	case models.TradeStatusConfirmed:
		return api.TradeSessionStatusAccepted // Map Confirmed to Accepted
	case models.TradeStatusCompleted:
		return api.TradeSessionStatusCompleted
	case models.TradeStatusCancelled:
		return api.TradeSessionStatusCancelled
	case models.TradeStatusExpired:
		return api.TradeSessionStatusCancelled // Map Expired to Cancelled
	default:
		return api.TradeSessionStatusPending
	}
}
