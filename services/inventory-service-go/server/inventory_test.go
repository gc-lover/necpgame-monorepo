package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/inventory-service-go/models"
	"github.com/stretchr/testify/assert"
)

type MockRepository struct{}

func (m *MockRepository) GetItem(ctx context.Context, characterID, itemID uuid.UUID) (*models.InventoryItem, error) {
	return &models.InventoryItem{
		ID:          itemID,
		CharacterID: characterID,
		ItemID:      uuid.New(),
		Quantity:    10,
		Equipped:    false,
		CreatedAt:   time.Now(),
	}, nil
}

func (m *MockRepository) AddItem(ctx context.Context, item *models.InventoryItem) error {
	return nil
}

func (m *MockRepository) RemoveItem(ctx context.Context, characterID, itemID uuid.UUID) error {
	return nil
}

func (m *MockRepository) UpdateQuantity(ctx context.Context, characterID, itemID uuid.UUID, quantity int) error {
	return nil
}

func (m *MockRepository) ListItems(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.InventoryItem, error) {
	return []models.InventoryItem{}, nil
}

func (m *MockRepository) EquipItem(ctx context.Context, characterID, itemID uuid.UUID) error {
	return nil
}

func (m *MockRepository) UnequipItem(ctx context.Context, characterID, itemID uuid.UUID) error {
	return nil
}

func (m *MockRepository) GetCapacity(ctx context.Context, characterID uuid.UUID) (int, error) {
	return 100, nil
}

func (m *MockRepository) Close() error {
	return nil
}

func TestNewInventoryService(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	assert.NotNil(t, service)
}

func TestGetItem(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	itemID := uuid.New()
	
	item, err := service.GetItem(ctx, characterID, itemID)
	assert.NoError(t, err)
	assert.NotNil(t, item)
	assert.Equal(t, characterID, item.CharacterID)
}

func TestAddItem(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	req := &models.AddItemRequest{
		CharacterID: uuid.New(),
		ItemID:      uuid.New(),
		Quantity:    5,
	}
	
	item, err := service.AddItem(ctx, req.CharacterID, req.ItemID, req.Quantity)
	assert.NoError(t, err)
	assert.NotNil(t, item)
	assert.Equal(t, req.Quantity, item.Quantity)
}

func TestRemoveItem(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	itemID := uuid.New()
	
	err := service.RemoveItem(ctx, characterID, itemID)
	assert.NoError(t, err)
}

func TestUpdateQuantity(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	itemID := uuid.New()
	newQuantity := 20
	
	err := service.UpdateQuantity(ctx, characterID, itemID, newQuantity)
	assert.NoError(t, err)
}

func TestListItems(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	items, err := service.ListItems(ctx, characterID, 50, 0)
	assert.NoError(t, err)
	assert.NotNil(t, items)
}

func TestEquipItem(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	itemID := uuid.New()
	
	err := service.EquipItem(ctx, characterID, itemID)
	assert.NoError(t, err)
}

func TestUnequipItem(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	itemID := uuid.New()
	
	err := service.UnequipItem(ctx, characterID, itemID)
	assert.NoError(t, err)
}

func TestGetCapacity(t *testing.T) {
	repo := &MockRepository{}
	service := NewInventoryService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	capacity, err := service.GetCapacity(ctx, characterID)
	assert.NoError(t, err)
	assert.Equal(t, 100, capacity)
}

func TestInventoryServiceNilRepository(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Expected panic with nil repository")
		}
	}()
	NewInventoryService(nil)
}

