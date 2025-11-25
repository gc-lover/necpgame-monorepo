package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/models"
	"github.com/stretchr/testify/assert"
)

type MockHousingRepository struct{}

func (m *MockHousingRepository) GetHouse(ctx context.Context, characterID, houseID uuid.UUID) (*models.House, error) {
	return &models.House{
		ID:          houseID,
		OwnerID:     characterID,
		Name:        "Test House",
		Type:        models.HouseTypeApartment,
		Level:       1,
		Capacity:    100,
		IsPublic:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockHousingRepository) ListHouses(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.House, error) {
	return []models.House{}, nil
}

func (m *MockHousingRepository) CreateHouse(ctx context.Context, house *models.House) error {
	return nil
}

func (m *MockHousingRepository) UpdateHouse(ctx context.Context, house *models.House) error {
	return nil
}

func (m *MockHousingRepository) DeleteHouse(ctx context.Context, characterID, houseID uuid.UUID) error {
	return nil
}

func (m *MockHousingRepository) GetFurniture(ctx context.Context, houseID uuid.UUID) ([]models.Furniture, error) {
	return []models.Furniture{}, nil
}

func (m *MockHousingRepository) AddFurniture(ctx context.Context, furniture *models.Furniture) error {
	return nil
}

func (m *MockHousingRepository) RemoveFurniture(ctx context.Context, houseID, furnitureID uuid.UUID) error {
	return nil
}

func (m *MockHousingRepository) MoveFurniture(ctx context.Context, furnitureID uuid.UUID, x, y, z float64) error {
	return nil
}

func (m *MockHousingRepository) GetVisitors(ctx context.Context, houseID uuid.UUID) ([]models.Visitor, error) {
	return []models.Visitor{}, nil
}

func (m *MockHousingRepository) AddVisitor(ctx context.Context, visitor *models.Visitor) error {
	return nil
}

func (m *MockHousingRepository) RemoveVisitor(ctx context.Context, houseID, visitorID uuid.UUID) error {
	return nil
}

func (m *MockHousingRepository) Close() error {
	return nil
}

func TestNewHousingService(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	assert.NotNil(t, service)
}

func TestGetHouse(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	houseID := uuid.New()
	
	house, err := service.GetHouse(ctx, characterID, houseID)
	assert.NoError(t, err)
	assert.NotNil(t, house)
	assert.Equal(t, characterID, house.OwnerID)
	assert.Equal(t, models.HouseTypeApartment, house.Type)
}

func TestListHouses(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	houses, err := service.ListHouses(ctx, characterID, 50, 0)
	assert.NoError(t, err)
	assert.NotNil(t, houses)
}

func TestCreateHouse(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	req := &models.CreateHouseRequest{
		OwnerID: uuid.New(),
		Name:    "New House",
		Type:    models.HouseTypeVilla,
	}
	
	house, err := service.CreateHouse(ctx, req)
	assert.NoError(t, err)
	assert.NotNil(t, house)
	assert.Equal(t, req.Name, house.Name)
	assert.Equal(t, req.Type, house.Type)
}

func TestUpdateHouse(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	houseID := uuid.New()
	req := &models.UpdateHouseRequest{
		Name:     "Updated House",
		IsPublic: true,
	}
	
	house, err := service.UpdateHouse(ctx, houseID, req)
	assert.NoError(t, err)
	assert.NotNil(t, house)
}

func TestDeleteHouse(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	houseID := uuid.New()
	
	err := service.DeleteHouse(ctx, characterID, houseID)
	assert.NoError(t, err)
}

func TestGetFurniture(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	houseID := uuid.New()
	
	furniture, err := service.GetFurniture(ctx, houseID)
	assert.NoError(t, err)
	assert.NotNil(t, furniture)
}

func TestAddFurniture(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	req := &models.AddFurnitureRequest{
		HouseID:      uuid.New(),
		FurnitureID:  uuid.New(),
		PositionX:    10.5,
		PositionY:    0.0,
		PositionZ:    5.5,
	}
	
	furniture, err := service.AddFurniture(ctx, req)
	assert.NoError(t, err)
	assert.NotNil(t, furniture)
}

func TestRemoveFurniture(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	houseID := uuid.New()
	furnitureID := uuid.New()
	
	err := service.RemoveFurniture(ctx, houseID, furnitureID)
	assert.NoError(t, err)
}

func TestMoveFurniture(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	furnitureID := uuid.New()
	
	err := service.MoveFurniture(ctx, furnitureID, 15.0, 0.0, 10.0)
	assert.NoError(t, err)
}

func TestGetVisitors(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	houseID := uuid.New()
	
	visitors, err := service.GetVisitors(ctx, houseID)
	assert.NoError(t, err)
	assert.NotNil(t, visitors)
}

func TestAddVisitor(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	houseID := uuid.New()
	visitorID := uuid.New()
	
	err := service.AddVisitor(ctx, houseID, visitorID)
	assert.NoError(t, err)
}

func TestRemoveVisitor(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	houseID := uuid.New()
	visitorID := uuid.New()
	
	err := service.RemoveVisitor(ctx, houseID, visitorID)
	assert.NoError(t, err)
}

func TestHousingServiceNilRepository(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Expected panic with nil repository")
		}
	}()
	NewHousingService(nil)
}

func TestHouseLifecycle(t *testing.T) {
	repo := &MockHousingRepository{}
	service := NewHousingService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	createReq := &models.CreateHouseRequest{
		OwnerID: characterID,
		Name:    "My House",
		Type:    models.HouseTypeApartment,
	}
	
	house, err := service.CreateHouse(ctx, createReq)
	assert.NoError(t, err)
	assert.NotNil(t, house)
	
	updateReq := &models.UpdateHouseRequest{
		Name:     "My Updated House",
		IsPublic: true,
	}
	
	updatedHouse, err := service.UpdateHouse(ctx, house.ID, updateReq)
	assert.NoError(t, err)
	assert.NotNil(t, updatedHouse)
	
	err = service.DeleteHouse(ctx, characterID, house.ID)
	assert.NoError(t, err)
}

