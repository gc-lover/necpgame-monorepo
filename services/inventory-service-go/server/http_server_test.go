package server

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/inventory-service-go/models"
)

type mockInventoryService struct {
	inventories map[uuid.UUID]*models.Inventory
	items       map[uuid.UUID][]models.InventoryItem
	templates   map[string]*models.ItemTemplate
	createErr   error
	getErr      error
}

func (m *mockInventoryService) GetInventory(ctx context.Context, characterID uuid.UUID) (*models.InventoryResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	inv := m.inventories[characterID]
	if inv == nil {
		inv = &models.Inventory{
			ID:          uuid.New(),
			CharacterID: characterID,
			Capacity:    50,
			UsedSlots:   0,
			Weight:      0,
			MaxWeight:   100.0,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
		m.inventories[characterID] = inv
	}

	items := m.items[inv.ID]
	return &models.InventoryResponse{
		Inventory: *inv,
		Items:     items,
	}, nil
}

func (m *mockInventoryService) AddItem(ctx context.Context, characterID uuid.UUID, req *models.AddItemRequest) error {
	if m.createErr != nil {
		return m.createErr
	}

	template := m.templates[req.ItemID]
	if template == nil {
		return errors.New("item template not found")
	}

	inv := m.inventories[characterID]
	if inv == nil {
		inv = &models.Inventory{
			ID:          uuid.New(),
			CharacterID: characterID,
			Capacity:    50,
			UsedSlots:   0,
			Weight:      0,
			MaxWeight:   100.0,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
		m.inventories[characterID] = inv
	}

	if inv.UsedSlots >= inv.Capacity {
		return errors.New("inventory is full")
	}

	items := m.items[inv.ID]
	var existingItem *models.InventoryItem
	for i := range items {
		if items[i].ItemID == req.ItemID && !items[i].IsEquipped {
			existingItem = &items[i]
			break
		}
	}

	if existingItem != nil && template.MaxStackSize > 1 {
		newCount := existingItem.StackCount + req.StackCount
		if newCount <= template.MaxStackSize {
			existingItem.StackCount = newCount
			existingItem.UpdatedAt = time.Now()
		} else {
			existingItem.StackCount = template.MaxStackSize
			existingItem.UpdatedAt = time.Now()
			remaining := newCount - template.MaxStackSize
			return m.AddItem(ctx, characterID, &models.AddItemRequest{ItemID: req.ItemID, StackCount: remaining})
		}
	} else {
		freeSlot := len(items)
		if freeSlot >= inv.Capacity {
			return errors.New("no free slots available")
		}

		item := &models.InventoryItem{
			ID:           uuid.New(),
			InventoryID:  inv.ID,
			ItemID:       req.ItemID,
			SlotIndex:    freeSlot,
			StackCount:   req.StackCount,
			MaxStackSize: template.MaxStackSize,
			IsEquipped:   false,
			CreatedAt:    time.Now(),
			UpdatedAt:    time.Now(),
		}

		m.items[inv.ID] = append(m.items[inv.ID], *item)
		inv.UsedSlots++
		inv.Weight += template.Weight * float64(req.StackCount)
	}

	return nil
}

func (m *mockInventoryService) RemoveItem(ctx context.Context, characterID uuid.UUID, itemID uuid.UUID) error {
	inv := m.inventories[characterID]
	if inv == nil {
		return errors.New("inventory not found")
	}

	items := m.items[inv.ID]
	for i, item := range items {
		if item.ID == itemID {
			template := m.templates[item.ItemID]
			if template != nil {
				inv.Weight -= template.Weight * float64(item.StackCount)
			}
			m.items[inv.ID] = append(items[:i], items[i+1:]...)
			inv.UsedSlots--
			return nil
		}
	}

	return errors.New("item not found")
}

func (m *mockInventoryService) EquipItem(ctx context.Context, characterID uuid.UUID, req *models.EquipItemRequest) error {
	inv := m.inventories[characterID]
	if inv == nil {
		return errors.New("inventory not found")
	}

	items := m.items[inv.ID]
	var item *models.InventoryItem
	for i := range items {
		if items[i].ItemID == req.ItemID {
			item = &items[i]
			break
		}
	}

	if item == nil {
		return errors.New("item not found")
	}

	template := m.templates[item.ItemID]
	if template == nil || !template.CanEquip {
		return errors.New("item cannot be equipped")
	}

	for i := range items {
		if items[i].IsEquipped && items[i].EquipSlot == req.EquipSlot {
			items[i].IsEquipped = false
			items[i].EquipSlot = ""
			items[i].UpdatedAt = time.Now()
		}
	}

	item.IsEquipped = true
	item.EquipSlot = req.EquipSlot
	item.UpdatedAt = time.Now()
	return nil
}

func (m *mockInventoryService) UnequipItem(ctx context.Context, characterID uuid.UUID, itemID uuid.UUID) error {
	inv := m.inventories[characterID]
	if inv == nil {
		return errors.New("inventory not found")
	}

	items := m.items[inv.ID]
	for i := range items {
		if items[i].ID == itemID {
			items[i].IsEquipped = false
			items[i].EquipSlot = ""
			items[i].UpdatedAt = time.Now()
			return nil
		}
	}

	return errors.New("item not found")
}

func TestHTTPServer_GetInventory(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	inventory := &models.Inventory{
		ID:          uuid.New(),
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   2,
		Weight:      10.5,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.inventories[characterID] = inventory
	mockService.items[inventory.ID] = []models.InventoryItem{
		{
			ID:           uuid.New(),
			InventoryID: inventory.ID,
			ItemID:       "item1",
			SlotIndex:    0,
			StackCount:   1,
			CreatedAt:    time.Now(),
			UpdatedAt:    time.Now(),
		},
	}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/inventory/"+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.InventoryResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Inventory.CharacterID != characterID {
		t.Errorf("Expected character_id %s, got %s", characterID, response.Inventory.CharacterID)
	}

	if len(response.Items) != 1 {
		t.Errorf("Expected 1 item, got %d", len(response.Items))
	}
}

func TestHTTPServer_AddItem(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	inventory := &models.Inventory{
		ID:          uuid.New(),
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	template := &models.ItemTemplate{
		ID:           "item1",
		Name:         "Test Item",
		Type:         "weapon",
		MaxStackSize: 10,
		Weight:       2.5,
		CanEquip:     true,
	}

	mockService.inventories[characterID] = inventory
	mockService.items[inventory.ID] = []models.InventoryItem{}
	mockService.templates["item1"] = template

	server := NewHTTPServer(":8080", mockService)

	reqBody := models.AddItemRequest{
		ItemID:     "item1",
		StackCount: 5,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/inventory/"+characterID.String()+"/items", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_RemoveItem(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	inventory := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      5.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           itemID,
		InventoryID:  inventoryID,
		ItemID:       "item1",
		SlotIndex:    0,
		StackCount:   1,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	mockService.inventories[characterID] = inventory
	mockService.items[inventoryID] = []models.InventoryItem{item}
	mockService.templates["item1"] = &models.ItemTemplate{
		ID:     "item1",
		Weight: 5.0,
	}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("DELETE", "/api/v1/inventory/"+characterID.String()+"/items/"+itemID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_EquipItem(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	inventoryID := uuid.New()
	inventory := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      5.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           uuid.New(),
		InventoryID:  inventoryID,
		ItemID:       "item1",
		SlotIndex:    0,
		StackCount:   1,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	template := &models.ItemTemplate{
		ID:       "item1",
		CanEquip: true,
		EquipSlot: "weapon",
	}

	mockService.inventories[characterID] = inventory
	mockService.items[inventoryID] = []models.InventoryItem{item}
	mockService.templates["item1"] = template

	server := NewHTTPServer(":8080", mockService)

	reqBody := models.EquipItemRequest{
		ItemID:    "item1",
		EquipSlot: "weapon",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/inventory/"+characterID.String()+"/equip", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_UnequipItem(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	inventory := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      5.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           itemID,
		InventoryID:  inventoryID,
		ItemID:       "item1",
		SlotIndex:    0,
		StackCount:   1,
		IsEquipped:   true,
		EquipSlot:    "weapon",
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	mockService.inventories[characterID] = inventory
	mockService.items[inventoryID] = []models.InventoryItem{item}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("POST", "/api/v1/inventory/"+characterID.String()+"/unequip/"+itemID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_AddItemInvalidRequest(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	server := NewHTTPServer(":8080", mockService)

	reqBody := models.AddItemRequest{
		ItemID:     "",
		StackCount: 0,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/inventory/"+characterID.String()+"/items", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}
}

func TestHTTPServer_AddItemInventoryFull(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}

	characterID := uuid.New()
	inventoryID := uuid.New()
	inventory := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   50,
		Weight:      100.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	template := &models.ItemTemplate{
		ID:           "item1",
		MaxStackSize: 1,
		Weight:       1.0,
	}

	mockService.inventories[characterID] = inventory
	mockService.items[inventoryID] = make([]models.InventoryItem, 50)
	mockService.templates["item1"] = template

	server := NewHTTPServer(":8080", mockService)

	reqBody := models.AddItemRequest{
		ItemID:     "item1",
		StackCount: 1,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/inventory/"+characterID.String()+"/items", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusBadRequest, w.Code, w.Body.String())
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockInventoryService{
		inventories: make(map[uuid.UUID]*models.Inventory),
		items:       make(map[uuid.UUID][]models.InventoryItem),
		templates:   make(map[string]*models.ItemTemplate),
	}
	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response map[string]string
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response["status"] != "healthy" {
		t.Errorf("Expected status 'healthy', got %s", response["status"])
	}
}

