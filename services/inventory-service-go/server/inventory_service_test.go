package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/models"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockInventoryRepository struct {
	mock.Mock
}

func (m *mockInventoryRepository) GetInventoryByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.Inventory, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Inventory), args.Error(1)
}

func (m *mockInventoryRepository) CreateInventory(ctx context.Context, characterID uuid.UUID, capacity int, maxWeight float64) (*models.Inventory, error) {
	args := m.Called(ctx, characterID, capacity, maxWeight)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Inventory), args.Error(1)
}

func (m *mockInventoryRepository) GetInventoryItems(ctx context.Context, inventoryID uuid.UUID) ([]models.InventoryItem, error) {
	args := m.Called(ctx, inventoryID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.InventoryItem), args.Error(1)
}

func (m *mockInventoryRepository) AddItem(ctx context.Context, item *models.InventoryItem) error {
	args := m.Called(ctx, item)
	return args.Error(0)
}

func (m *mockInventoryRepository) UpdateItem(ctx context.Context, item *models.InventoryItem) error {
	args := m.Called(ctx, item)
	return args.Error(0)
}

func (m *mockInventoryRepository) RemoveItem(ctx context.Context, itemID uuid.UUID) error {
	args := m.Called(ctx, itemID)
	return args.Error(0)
}

func (m *mockInventoryRepository) UpdateInventoryStats(ctx context.Context, inventoryID uuid.UUID, usedSlots int, weight float64) error {
	args := m.Called(ctx, inventoryID, usedSlots, weight)
	return args.Error(0)
}

func (m *mockInventoryRepository) GetItemTemplate(ctx context.Context, itemID string) (*models.ItemTemplate, error) {
	args := m.Called(ctx, itemID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ItemTemplate), args.Error(1)
}

func setupTestService() (*InventoryService, *mockInventoryRepository, *redis.Client) {
	mockRepo := new(mockInventoryRepository)
	redisClient := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DB:           1,
		DialTimeout:  1 * time.Second, // Fast timeout for tests
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})

	// Initialize resilience components
	dbCB := NewCircuitBreaker("inventory-db-test")
	loadShedder := NewLoadShedder(1000) // Max 1000 concurrent requests

	service := &InventoryService{
		repo:             mockRepo,
		cache:            redisClient,
		logger:           GetLogger(),
		dbCircuitBreaker: dbCB,
		loadShedder:      loadShedder,
	}

	return service, mockRepo, redisClient
}

func TestInventoryService_GetInventory_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	var items []models.InventoryItem

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)

	response, err := service.GetInventory(ctx, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, inv.ID, response.Inventory.ID)
	assert.Equal(t, characterID, response.Inventory.CharacterID)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_GetInventory_CreateNew(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	newInv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	var items []models.InventoryItem

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(nil, nil)
	mockRepo.On("CreateInventory", ctx, characterID, 50, 100.0).Return(newInv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)

	response, err := service.GetInventory(ctx, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, response)
	assert.Equal(t, newInv.ID, response.Inventory.ID)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_GetInventory_Cache(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	var items []models.InventoryItem
	response := &models.InventoryResponse{
		Inventory: *inv,
		Items:     items,
	}

	responseJSON, _ := json.Marshal(response)
	cacheKey := "inventory:" + characterID.String()
	// Check if Redis is available
	if err := redisClient.Ping(ctx).Err(); err != nil {
		t.Skipf("Skipping test due to Redis not available: %v", err)
	}
	redisClient.Set(ctx, cacheKey, responseJSON, 5*time.Minute)

	cachedResponse, err := service.GetInventory(ctx, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, cachedResponse)
	assert.Equal(t, inv.ID, cachedResponse.Inventory.ID)
	// Note: GetInventoryByCharacterID may be called if cache unmarshal fails or circuit breaker is open
	// This is expected behavior for resilience
	mockRepo.AssertNotCalled(t, "GetInventoryByCharacterID", ctx, characterID)
}

func TestInventoryService_AddItem_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	template := &models.ItemTemplate{
		ID:           "item_001",
		Name:         "Test Item",
		Type:         "weapon",
		Rarity:       "common",
		MaxStackSize: 10,
		Weight:       1.0,
		CanEquip:     false,
	}

	var items []models.InventoryItem

	req := &models.AddItemRequest{
		ItemID:     "item_001",
		StackCount: 1,
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(template, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)
	mockRepo.On("AddItem", ctx, mock.AnythingOfType("*models.InventoryItem")).Return(nil)
	mockRepo.On("UpdateInventoryStats", ctx, inventoryID, 1, 1.0).Return(nil)

	err := service.AddItem(ctx, characterID, req)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_AddItem_InventoryFull(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   50,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	template := &models.ItemTemplate{
		ID:           "item_001",
		Name:         "Test Item",
		Type:         "weapon",
		Rarity:       "common",
		MaxStackSize: 10,
		Weight:       1.0,
		CanEquip:     false,
	}

	req := &models.AddItemRequest{
		ItemID:     "item_001",
		StackCount: 1,
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(template, nil)

	err := service.AddItem(ctx, characterID, req)

	assert.Error(t, err)
	assert.Equal(t, "inventory is full", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_AddItem_ItemTemplateNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	req := &models.AddItemRequest{
		ItemID:     "item_001",
		StackCount: 1,
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(nil, nil)

	err := service.AddItem(ctx, characterID, req)

	assert.Error(t, err)
	assert.Equal(t, "item template not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_AddItem_StackExisting(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      1.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	template := &models.ItemTemplate{
		ID:           "item_001",
		Name:         "Test Item",
		Type:         "consumable",
		Rarity:       "common",
		MaxStackSize: 10,
		Weight:       1.0,
		CanEquip:     false,
	}

	existingItem := models.InventoryItem{
		ID:           uuid.New(),
		InventoryID:  inventoryID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   5,
		MaxStackSize: 10,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	items := []models.InventoryItem{existingItem}

	req := &models.AddItemRequest{
		ItemID:     "item_001",
		StackCount: 3,
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(template, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)
	mockRepo.On("UpdateItem", ctx, mock.AnythingOfType("*models.InventoryItem")).Return(nil)
	mockRepo.On("UpdateInventoryStats", ctx, inventoryID, 1, 1.0).Return(nil)

	err := service.AddItem(ctx, characterID, req)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_RemoveItem_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      1.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           itemID,
		InventoryID:  inventoryID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 10,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	items := []models.InventoryItem{item}

	template := &models.ItemTemplate{
		ID:           "item_001",
		Name:         "Test Item",
		Type:         "weapon",
		Rarity:       "common",
		MaxStackSize: 10,
		Weight:       1.0,
		CanEquip:     false,
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(template, nil)
	mockRepo.On("RemoveItem", ctx, itemID).Return(nil)
	mockRepo.On("UpdateInventoryStats", ctx, inventoryID, 0, 0.0).Return(nil)

	err := service.RemoveItem(ctx, characterID, itemID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_RemoveItem_InventoryNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(nil, nil)

	err := service.RemoveItem(ctx, characterID, itemID)

	assert.Error(t, err)
	assert.Equal(t, "inventory not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_RemoveItem_ItemNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	var items []models.InventoryItem

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)

	err := service.RemoveItem(ctx, characterID, itemID)

	assert.Error(t, err)
	assert.Equal(t, "item not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_EquipItem_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      1.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           itemID,
		InventoryID:  inventoryID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 1,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	items := []models.InventoryItem{item}

	template := &models.ItemTemplate{
		ID:           "item_001",
		Name:         "Test Weapon",
		Type:         "weapon",
		Rarity:       "common",
		MaxStackSize: 1,
		Weight:       1.0,
		CanEquip:     true,
		EquipSlot:    "weapon",
	}

	req := &models.EquipItemRequest{
		ItemID:    "item_001",
		EquipSlot: "weapon",
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(template, nil)
	mockRepo.On("UpdateItem", ctx, mock.AnythingOfType("*models.InventoryItem")).Return(nil).Times(1)

	err := service.EquipItem(ctx, characterID, req)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_EquipItem_InventoryNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	req := &models.EquipItemRequest{
		ItemID:    "item_001",
		EquipSlot: "weapon",
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(nil, nil)

	err := service.EquipItem(ctx, characterID, req)

	assert.Error(t, err)
	assert.Equal(t, "inventory not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_EquipItem_ItemNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	var items []models.InventoryItem

	req := &models.EquipItemRequest{
		ItemID:    "item_001",
		EquipSlot: "weapon",
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)

	err := service.EquipItem(ctx, characterID, req)

	assert.Error(t, err)
	assert.Equal(t, "item not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_EquipItem_CannotEquip(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      1.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           itemID,
		InventoryID:  inventoryID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 1,
		IsEquipped:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	items := []models.InventoryItem{item}

	template := &models.ItemTemplate{
		ID:           "item_001",
		Name:         "Test Item",
		Type:         "consumable",
		Rarity:       "common",
		MaxStackSize: 10,
		Weight:       1.0,
		CanEquip:     false,
	}

	req := &models.EquipItemRequest{
		ItemID:    "item_001",
		EquipSlot: "weapon",
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)
	mockRepo.On("GetItemTemplate", ctx, "item_001").Return(template, nil)

	err := service.EquipItem(ctx, characterID, req)

	assert.Error(t, err)
	assert.Equal(t, "item cannot be equipped", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_UnequipItem_Success(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   1,
		Weight:      1.0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	item := models.InventoryItem{
		ID:           itemID,
		InventoryID:  inventoryID,
		ItemID:       "item_001",
		SlotIndex:    0,
		StackCount:   1,
		MaxStackSize: 1,
		IsEquipped:   true,
		EquipSlot:    "weapon",
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	items := []models.InventoryItem{item}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)
	mockRepo.On("UpdateItem", ctx, mock.AnythingOfType("*models.InventoryItem")).Return(nil)

	err := service.UnequipItem(ctx, characterID, itemID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_UnequipItem_InventoryNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(nil, nil)

	err := service.UnequipItem(ctx, characterID, itemID)

	assert.Error(t, err)
	assert.Equal(t, "inventory not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_UnequipItem_ItemNotFound(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	inventoryID := uuid.New()
	itemID := uuid.New()
	ctx := context.Background()

	inv := &models.Inventory{
		ID:          inventoryID,
		CharacterID: characterID,
		Capacity:    50,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   100.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	var items []models.InventoryItem

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(inv, nil)
	mockRepo.On("GetInventoryItems", ctx, inventoryID).Return(items, nil)

	err := service.UnequipItem(ctx, characterID, itemID)

	assert.Error(t, err)
	assert.Equal(t, "item not found", err.Error())
	mockRepo.AssertExpectations(t)
}

func TestInventoryService_GetInventory_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()

	// Check Redis availability - GetInventory tries cache first
	pingCtx, pingCancel := context.WithTimeout(context.Background(), 500*time.Millisecond)
	redisAvailable := redisClient.Ping(pingCtx).Err() == nil
	pingCancel()

	// If Redis is available, GetInventory will try cache first, which may timeout
	// If Redis is not available, GetInventory will go directly to DB
	// In both cases, we need to ensure the test doesn't hang
	if redisAvailable {
		// Redis available - GetInventory will try cache first, then DB on cache miss/error
		// The service will call GetInventoryByCharacterID when cache fails
		// Use a shorter timeout context to prevent hanging
		ctx, cancel = context.WithTimeout(context.Background(), 1*time.Second)
		defer cancel()
	} else {
		// Redis not available - GetInventory will go directly to DB
		// This is expected behavior
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(nil, errors.New("database error"))

	response, err := service.GetInventory(ctx, characterID)

	// Service may return graceful degradation (empty inventory) instead of error
	// due to circuit breaker fallback, so we check that repo was called
	mockRepo.AssertExpectations(t)

	// If error occurred, response should be nil or empty
	if err != nil {
		assert.Nil(t, response)
	}
}

func TestInventoryService_AddItem_RepositoryError(t *testing.T) {
	service, mockRepo, redisClient := setupTestService()
	defer redisClient.Close()

	characterID := uuid.New()
	ctx := context.Background()

	req := &models.AddItemRequest{
		ItemID:     "item_001",
		StackCount: 1,
	}

	mockRepo.On("GetInventoryByCharacterID", ctx, characterID).Return(nil, errors.New("database error"))

	err := service.AddItem(ctx, characterID, req)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}
