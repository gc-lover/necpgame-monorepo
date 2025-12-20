package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
)

// GetInventory retrieves character inventory with containers and items
func (s *InventoryService) GetInventory(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	s.metrics.ItemOperations.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.getInventoryResponsePool.Get().(*GetInventoryResponse)
	defer s.getInventoryResponsePool.Put(resp)

	resp.CharacterID = characterID
	resp.Containers = []*InventoryContainer{
		{
			ContainerID: "main_inventory",
			Name:        "Main Inventory",
			Type:        "INVENTORY",
			Capacity:    50,
			UsedSlots:   15,
			Rows:        5,
			Columns:     10,
			IsLocked:    false,
		},
	}
	resp.TotalItems = 42
	resp.TotalWeight = 1250
	resp.MaxWeight = 5000

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("character_id", characterID).Debug("inventory retrieved")
}

// ListInventoryItems lists items in character inventory with pagination
func (s *InventoryService) ListInventoryItems(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	container := r.URL.Query().Get("container")
	if container == "" {
		container = "main"
	}

	limit := 50 // Default limit
	offset := 0 // Default offset

	s.metrics.ItemOperations.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.listItemsResponsePool.Get().(*ListItemsResponse)
	defer s.listItemsResponsePool.Put(resp)

	resp.Items = []*InventoryItem{
		{
			InventoryItemID: uuid.New().String(),
			ItemID:          "sword_001",
			CharacterID:     characterID,
			Container:       container,
			SlotX:           0,
			SlotY:           0,
			Quantity:        1,
			Durability:      95,
			MaxDurability:   100,
			IsEquipped:      false,
			IsLocked:        false,
			AcquiredAt:      time.Now().Add(-24 * time.Hour),
		},
	}
	resp.TotalCount = 1
	resp.Limit = limit
	resp.Offset = offset

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"character_id": characterID,
		"container":    container,
		"limit":        limit,
		"offset":       offset,
	}).Debug("inventory items listed")
}

// MoveItem moves item within or between containers
func (s *InventoryService) MoveItem(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	var req MoveItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode move item request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.ItemOperations.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.moveItemResponsePool.Get().(*MoveItemResponse)
	defer s.moveItemResponsePool.Put(resp)

	resp.InventoryItemID = req.InventoryItemID
	resp.OldContainer = req.FromContainer
	resp.OldSlotX = 0 // Would be retrieved from DB
	resp.OldSlotY = 0
	resp.NewContainer = req.ToContainer
	resp.NewSlotX = req.ToSlotX
	resp.NewSlotY = req.ToSlotY
	resp.QuantityMoved = req.Quantity
	resp.Success = true

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"character_id":      characterID,
		"inventory_item_id": req.InventoryItemID,
		"from_container":    req.FromContainer,
		"to_container":      req.ToContainer,
	}).Info("item moved successfully")
}

// GetContainers retrieves character inventory containers
func (s *InventoryService) GetContainers(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	containers := []*InventoryContainer{
		{
			ContainerID: "main_inventory",
			Name:        "Main Inventory",
			Type:        "INVENTORY",
			Capacity:    50,
			UsedSlots:   15,
			Rows:        5,
			Columns:     10,
			IsLocked:    false,
		},
		{
			ContainerID: "equipment",
			Name:        "Equipment",
			Type:        "EQUIPMENT",
			Capacity:    12,
			UsedSlots:   2,
			Rows:        3,
			Columns:     4,
			IsLocked:    false,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(containers)

	s.logger.WithField("character_id", characterID).Debug("containers retrieved")
}
