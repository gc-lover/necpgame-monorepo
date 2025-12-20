package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
)

// EquipItem equips item to character
func (s *InventoryService) EquipItem(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	var req EquipItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode equip item request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.EquipmentChanges.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.equipItemResponsePool.Get().(*EquipItemResponse)
	defer s.equipItemResponsePool.Put(resp)

	resp.InventoryItemID = req.InventoryItemID
	resp.SlotType = req.SlotType
	resp.OldItemID = "" // No item was equipped in this slot
	resp.Success = true
	resp.Message = "Item equipped successfully"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"character_id":      characterID,
		"inventory_item_id": req.InventoryItemID,
		"slot_type":         req.SlotType,
	}).Info("item equipped successfully")
}

// UnequipItem unequips item from character
func (s *InventoryService) UnequipItem(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	var req UnequipItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode unequip item request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.EquipmentChanges.Inc()

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.unequipItemResponsePool.Get().(*UnequipItemResponse)
	defer s.unequipItemResponsePool.Put(resp)

	resp.SlotType = req.SlotType
	resp.InventoryItemID = uuid.New().String() // Would be the actual item ID
	resp.Container = req.TargetContainer
	resp.SlotX = 0
	resp.SlotY = 0
	resp.Success = true
	resp.Message = "Item unequipped successfully"

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"character_id": characterID,
		"slot_type":    req.SlotType,
	}).Info("item unequipped successfully")
}

// GetEquipment retrieves character equipment
func (s *InventoryService) GetEquipment(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.getEquipmentResponsePool.Get().(*GetEquipmentResponse)
	defer s.getEquipmentResponsePool.Put(resp)

	resp.CharacterID = characterID
	resp.Equipment = map[string]*EquipmentSlot{
		"MAIN_HAND": {
			SlotType:        "MAIN_HAND",
			InventoryItemID: uuid.New().String(),
			ItemID:          "sword_001",
			EquippedAt:      time.Now().Add(-1 * time.Hour),
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("character_id", characterID).Debug("equipment retrieved")
}

// GetEquipmentStats retrieves equipment stats and bonuses
func (s *InventoryService) GetEquipmentStats(w http.ResponseWriter, r *http.Request) {
	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		http.Error(w, "Character ID is required", http.StatusBadRequest)
		return
	}

	// OPTIMIZATION: Issue #1949 - Use memory pool
	resp := s.equipmentStatsResponsePool.Get().(*EquipmentStatsResponse)
	defer s.equipmentStatsResponsePool.Put(resp)

	resp.CharacterID = characterID
	resp.Stats = &EquipmentStats{
		TotalStats: map[string]interface{}{
			"attack_power": 25,
			"defense":      10,
			"health":       0,
		},
		ActiveBonuses: []*ItemEffect{},
		SetBonuses:    map[string]string{},
		DefenseRating: 10,
		MagicResistance: 5,
		AttackPower:   25,
		SpellPower:    0,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("character_id", characterID).Debug("equipment stats retrieved")
}
