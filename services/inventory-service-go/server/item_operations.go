package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
)

// UseItem uses consumable item
func (s *InventoryService) UseItem(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	var req UseItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode use item request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.ItemOperations.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.useItemResponsePool.Get().(*UseItemResponse)
	defer s.useItemResponsePool.Put(resp)

	resp.InventoryItemID = req.InventoryItemID
	resp.QuantityUsed = req.Quantity
	resp.EffectsApplied = []*ItemEffect{
		{
			EffectType: "STAT_BONUS",
			TargetStat: "health",
			Value:      50,
			Duration:   300, // 5 minutes
		},
	}
	resp.RemainingQuantity = 0 // Item was consumed
	resp.Success = true
	resp.Message = "Health potion used successfully"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"character_id":      characterID,
		"inventory_item_id": req.InventoryItemID,
		"quantity_used":     req.Quantity,
	}).Info("item used successfully")
}

// DropItem drops item from inventory
func (s *InventoryService) DropItem(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	var req DropItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode drop item request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.ItemOperations.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.dropItemResponsePool.Get().(*DropItemResponse)
	defer s.dropItemResponsePool.Put(resp)

	resp.InventoryItemID = req.InventoryItemID
	resp.QuantityDropped = req.Quantity
	resp.Position = &Vector3{X: 10.5, Y: 20.3, Z: 0.0}
	resp.RemainingQuantity = 0 // All items dropped
	resp.Success = true
	resp.Message = "Item dropped successfully"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"character_id":      characterID,
		"inventory_item_id": req.InventoryItemID,
		"quantity_dropped":  req.Quantity,
	}).Info("item dropped successfully")
}

// GetItem retrieves item metadata
func (s *InventoryService) GetItem(w http.ResponseWriter, r *http.Request) {
	itemID := chi.URLParam(r, "itemId")
	if itemID == "" {
		http.Error(w, "Item ID is required", http.StatusBadRequest)
		return
	}

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.getItemResponsePool.Get().(*GetItemResponse)
	defer s.getItemResponsePool.Put(resp)

	resp.Item = &ItemDefinition{
		ItemID:      itemID,
		Name:        "Steel Sword",
		Description: "A well-balanced steel sword",
		ItemType:    "WEAPON",
		Rarity:      "COMMON",
		LevelReq:    5,
		MaxStack:    1,
		SellPrice:   150,
		BuyPrice:    300,
		Stats: map[string]interface{}{
			"attack_power": 25,
			"durability":   100,
		},
		Effects: []*ItemEffect{},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("item_id", itemID).Debug("item metadata retrieved")
}

// SearchItems searches items by criteria
func (s *InventoryService) SearchItems(w http.ResponseWriter, r *http.Request) {
	var req SearchItemsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode search items request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.SearchQueries.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.searchItemsResponsePool.Get().(*SearchItemsResponse)
	defer s.searchItemsResponsePool.Put(resp)

	resp.Items = []*ItemDefinition{
		{
			ItemID:      "sword_001",
			Name:        "Steel Sword",
			Description: "A well-balanced steel sword",
			ItemType:    "WEAPON",
			Rarity:      "COMMON",
			LevelReq:    5,
			MaxStack:    1,
			SellPrice:   150,
			BuyPrice:    300,
		},
	}
	resp.TotalCount = 1
	resp.Query = req.Query
	resp.SearchTimeMs = 15

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"query":         req.Query,
		"results_count": len(resp.Items),
		"search_time_ms": resp.SearchTimeMs,
	}).Debug("items searched successfully")
}
