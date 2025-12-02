package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockHousingRepository struct {
	mock.Mock
}

func (m *mockHousingRepository) CreateApartment(ctx context.Context, apartment *models.Apartment) error {
	args := m.Called(ctx, apartment)
	return args.Error(0)
}

func (m *mockHousingRepository) GetApartmentByID(ctx context.Context, apartmentID uuid.UUID) (*models.Apartment, error) {
	args := m.Called(ctx, apartmentID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Apartment), args.Error(1)
}

func (m *mockHousingRepository) ListApartments(ctx context.Context, ownerID *uuid.UUID, ownerType *string, isPublic *bool, limit, offset int) ([]models.Apartment, int, error) {
	args := m.Called(ctx, ownerID, ownerType, isPublic, limit, offset)
	if args.Get(0) == nil {
		return nil, 0, args.Error(2)
	}
	return args.Get(0).([]models.Apartment), args.Int(1), args.Error(2)
}

func (m *mockHousingRepository) UpdateApartment(ctx context.Context, apartment *models.Apartment) error {
	args := m.Called(ctx, apartment)
	return args.Error(0)
}

func (m *mockHousingRepository) GetFurnitureItemByID(ctx context.Context, itemID string) (*models.FurnitureItem, error) {
	args := m.Called(ctx, itemID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.FurnitureItem), args.Error(1)
}

func (m *mockHousingRepository) ListFurnitureItems(ctx context.Context, category *models.FurnitureCategory, limit, offset int) ([]models.FurnitureItem, int, error) {
	args := m.Called(ctx, category, limit, offset)
	if args.Get(0) == nil {
		return nil, 0, args.Error(2)
	}
	return args.Get(0).([]models.FurnitureItem), args.Int(1), args.Error(2)
}

func (m *mockHousingRepository) CreatePlacedFurniture(ctx context.Context, furniture *models.PlacedFurniture) error {
	args := m.Called(ctx, furniture)
	return args.Error(0)
}

func (m *mockHousingRepository) GetPlacedFurnitureByID(ctx context.Context, furnitureID uuid.UUID) (*models.PlacedFurniture, error) {
	args := m.Called(ctx, furnitureID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlacedFurniture), args.Error(1)
}

func (m *mockHousingRepository) ListPlacedFurniture(ctx context.Context, apartmentID uuid.UUID) ([]models.PlacedFurniture, error) {
	args := m.Called(ctx, apartmentID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PlacedFurniture), args.Error(1)
}

func (m *mockHousingRepository) DeletePlacedFurniture(ctx context.Context, furnitureID uuid.UUID) error {
	args := m.Called(ctx, furnitureID)
	return args.Error(0)
}

func (m *mockHousingRepository) CountPlacedFurniture(ctx context.Context, apartmentID uuid.UUID) (int, error) {
	args := m.Called(ctx, apartmentID)
	return args.Int(0), args.Error(1)
}

func (m *mockHousingRepository) CreateVisit(ctx context.Context, visit *models.ApartmentVisit) error {
	args := m.Called(ctx, visit)
	return args.Error(0)
}

func (m *mockHousingRepository) GetPrestigeLeaderboard(ctx context.Context, limit, offset int) ([]models.PrestigeLeaderboardEntry, int, error) {
	args := m.Called(ctx, limit, offset)
	if args.Get(0) == nil {
		return nil, 0, args.Error(2)
	}
	return args.Get(0).([]models.PrestigeLeaderboardEntry), args.Int(1), args.Error(2)
}

func setupTestService(t *testing.T) (*HousingService, *mockHousingRepository, func()) {
	mockRepo := new(mockHousingRepository)

	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})
	redisClient.FlushDB(context.Background())

	service := &HousingService{
		repo:   mockRepo,
		redis:  redisClient,
		logger: GetLogger(),
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, cleanup
}

func TestHousingService_PurchaseApartment_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	req := &models.PurchaseApartmentRequest{
		CharacterID:   characterID,
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
	}

	mockRepo.On("CreateApartment", context.Background(), mock.AnythingOfType("*models.Apartment")).Return(nil)

	result, err := service.PurchaseApartment(context.Background(), req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.OwnerID)
	assert.Equal(t, models.ApartmentTypeStudio, result.ApartmentType)
	assert.Equal(t, int64(50000), result.Price)
	assert.Equal(t, 20, result.FurnitureSlots)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_PurchaseApartment_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	req := &models.PurchaseApartmentRequest{
		CharacterID:   characterID,
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
	}
	expectedErr := errors.New("database error")

	mockRepo.On("CreateApartment", context.Background(), mock.AnythingOfType("*models.Apartment")).Return(expectedErr)

	result, err := service.PurchaseApartment(context.Background(), req)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetApartment_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	expectedApartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
		Price:         50000,
		FurnitureSlots: 20,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(expectedApartment, nil)

	result, err := service.GetApartment(context.Background(), apartmentID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, apartmentID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_ListApartments_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	ownerID := uuid.New()
	apartments := []models.Apartment{
		{
			ID:            uuid.New(),
			OwnerID:       ownerID,
			ApartmentType: models.ApartmentTypeStudio,
			Location:      "district_01",
			Price:         50000,
		},
	}

	mockRepo.On("ListApartments", context.Background(), &ownerID, (*string)(nil), (*bool)(nil), 10, 0).Return(apartments, 1, nil)

	result, total, err := service.ListApartments(context.Background(), &ownerID, nil, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 1)
	assert.Equal(t, 1, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_UpdateApartmentSettings_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		ApartmentType: models.ApartmentTypeStudio,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	isPublic := true
	req := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		IsPublic:    &isPublic,
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("UpdateApartment", context.Background(), mock.AnythingOfType("*models.Apartment")).Return(nil)

	err := service.UpdateApartmentSettings(context.Background(), apartmentID, req)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_UpdateApartmentSettings_Unauthorized(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	ownerID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       ownerID,
		ApartmentType: models.ApartmentTypeStudio,
		IsPublic:      false,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	isPublic := true
	req := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		IsPublic:    &isPublic,
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)

	err := service.UpdateApartmentSettings(context.Background(), apartmentID, req)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "unauthorized")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_PlaceFurniture_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		ApartmentType: models.ApartmentTypeStudio,
		FurnitureSlots: 20,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	req := &models.PlaceFurnitureRequest{
		CharacterID:    characterID,
		FurnitureItemID: "furniture_001",
		Position:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Rotation:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Scale:          map[string]interface{}{"x": 1, "y": 1, "z": 1},
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("CountPlacedFurniture", context.Background(), apartmentID).Return(0, nil)
	mockRepo.On("CreatePlacedFurniture", context.Background(), mock.AnythingOfType("*models.PlacedFurniture")).Return(nil)
	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("ListPlacedFurniture", context.Background(), apartmentID).Return([]models.PlacedFurniture{}, nil)
	mockRepo.On("UpdateApartment", context.Background(), mock.AnythingOfType("*models.Apartment")).Return(nil)

	result, err := service.PlaceFurniture(context.Background(), apartmentID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, apartmentID, result.ApartmentID)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_PlaceFurniture_SlotsLimitReached(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		ApartmentType: models.ApartmentTypeStudio,
		FurnitureSlots: 20,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	req := &models.PlaceFurnitureRequest{
		CharacterID:    characterID,
		FurnitureItemID: "furniture_001",
		Position:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Rotation:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Scale:          map[string]interface{}{"x": 1, "y": 1, "z": 1},
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("CountPlacedFurniture", context.Background(), apartmentID).Return(20, nil)

	result, err := service.PlaceFurniture(context.Background(), apartmentID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "furniture slots limit reached")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_RemoveFurniture_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	furnitureID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		ApartmentType: models.ApartmentTypeStudio,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	furniture := &models.PlacedFurniture{
		ID:          furnitureID,
		ApartmentID: apartmentID,
		FurnitureItemID: "furniture_001",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("GetPlacedFurnitureByID", context.Background(), furnitureID).Return(furniture, nil)
	mockRepo.On("DeletePlacedFurniture", context.Background(), furnitureID).Return(nil)
	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("ListPlacedFurniture", context.Background(), apartmentID).Return([]models.PlacedFurniture{}, nil)
	mockRepo.On("UpdateApartment", context.Background(), mock.AnythingOfType("*models.Apartment")).Return(nil)

	err := service.RemoveFurniture(context.Background(), apartmentID, furnitureID, characterID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetFurnitureItem_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	itemID := "furniture_001"
	expectedItem := &models.FurnitureItem{
		ID:            itemID,
		Category:      models.FurnitureCategoryDecorative,
		Name:          "Test Furniture",
		Description:   "Test Description",
		Price:         1000,
		PrestigeValue: 10,
		FunctionBonus: make(map[string]interface{}),
		CreatedAt:     time.Now(),
	}

	mockRepo.On("GetFurnitureItemByID", context.Background(), itemID).Return(expectedItem, nil)

	result, err := service.GetFurnitureItem(context.Background(), itemID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, itemID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_ListFurnitureItems_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	items := []models.FurnitureItem{
		{
			ID:            "furniture_001",
			Category:      models.FurnitureCategoryDecorative,
			Name:          "Test Furniture",
			Price:         1000,
			PrestigeValue: 10,
		},
	}

	mockRepo.On("ListFurnitureItems", context.Background(), (*models.FurnitureCategory)(nil), 10, 0).Return(items, 1, nil)

	result, total, err := service.ListFurnitureItems(context.Background(), nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 1)
	assert.Equal(t, 1, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetApartmentDetail_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		ApartmentType: models.ApartmentTypeStudio,
		Location:      "district_01",
		Price:         50000,
		FurnitureSlots: 20,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	furniture := []models.PlacedFurniture{
		{
			ID:            uuid.New(),
			ApartmentID:   apartmentID,
			FurnitureItemID: "furniture_001",
			CreatedAt:     time.Now(),
			UpdatedAt:     time.Now(),
		},
	}
	item := &models.FurnitureItem{
		ID:            "furniture_001",
		Category:      models.FurnitureCategoryDecorative,
		Name:          "Test Furniture",
		PrestigeValue: 10,
		FunctionBonus: make(map[string]interface{}),
		CreatedAt:     time.Now(),
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("ListPlacedFurniture", context.Background(), apartmentID).Return(furniture, nil)
	mockRepo.On("GetFurnitureItemByID", context.Background(), "furniture_001").Return(item, nil)

	result, err := service.GetApartmentDetail(context.Background(), apartmentID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, apartmentID, result.Apartment.ID)
	assert.Len(t, result.Furniture, 1)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_VisitApartment_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		ApartmentType: models.ApartmentTypeStudio,
		IsPublic:      true,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	req := &models.VisitApartmentRequest{
		CharacterID: characterID,
		ApartmentID: apartmentID,
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("CreateVisit", context.Background(), mock.AnythingOfType("*models.ApartmentVisit")).Return(nil)

	err := service.VisitApartment(context.Background(), req)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_VisitApartment_PrivateUnauthorized(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	ownerID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       ownerID,
		ApartmentType: models.ApartmentTypeStudio,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	req := &models.VisitApartmentRequest{
		CharacterID: characterID,
		ApartmentID: apartmentID,
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)

	err := service.VisitApartment(context.Background(), req)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "unauthorized")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetPrestigeLeaderboard_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	entries := []models.PrestigeLeaderboardEntry{
		{
			ApartmentID:   uuid.New(),
			OwnerID:       uuid.New(),
			OwnerName:     "Test Player",
			PrestigeScore: 1000,
			ApartmentType: models.ApartmentTypePenthouse,
			Location:      "district_01",
		},
	}

	mockRepo.On("GetPrestigeLeaderboard", context.Background(), 10, 0).Return(entries, 1, nil)

	result, total, err := service.GetPrestigeLeaderboard(context.Background(), 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 1)
	assert.Equal(t, 1, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetApartment_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, expectedErr)

	result, err := service.GetApartment(context.Background(), apartmentID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_UpdateApartmentSettings_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	isPublic := true
	req := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		IsPublic:    &isPublic,
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, nil)

	err := service.UpdateApartmentSettings(context.Background(), apartmentID, req)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "apartment not found")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetApartment_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, nil)

	result, err := service.GetApartment(context.Background(), apartmentID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_ListApartments_Empty(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	mockRepo.On("ListApartments", context.Background(), (*uuid.UUID)(nil), (*string)(nil), (*bool)(nil), 10, 0).Return([]models.Apartment{}, 0, nil)

	result, total, err := service.ListApartments(context.Background(), nil, nil, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 0)
	assert.Equal(t, 0, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_ListApartments_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	expectedErr := errors.New("database error")
	mockRepo.On("ListApartments", context.Background(), (*uuid.UUID)(nil), (*string)(nil), (*bool)(nil), 10, 0).Return(nil, 0, expectedErr)

	result, total, err := service.ListApartments(context.Background(), nil, nil, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Equal(t, 0, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetApartmentDetail_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, nil)

	result, err := service.GetApartmentDetail(context.Background(), apartmentID)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "apartment not found")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetApartmentDetail_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, expectedErr)

	result, err := service.GetApartmentDetail(context.Background(), apartmentID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_PlaceFurniture_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	req := &models.PlaceFurnitureRequest{
		CharacterID:    characterID,
		FurnitureItemID: "furniture_001",
		Position:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Rotation:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Scale:          map[string]interface{}{"x": 1, "y": 1, "z": 1},
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, nil)

	result, err := service.PlaceFurniture(context.Background(), apartmentID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "apartment not found")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_PlaceFurniture_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		ApartmentType: models.ApartmentTypeStudio,
		FurnitureSlots: 20,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	req := &models.PlaceFurnitureRequest{
		CharacterID:    characterID,
		FurnitureItemID: "furniture_001",
		Position:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Rotation:       map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Scale:          map[string]interface{}{"x": 1, "y": 1, "z": 1},
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("CountPlacedFurniture", context.Background(), apartmentID).Return(0, nil)
	mockRepo.On("CreatePlacedFurniture", context.Background(), mock.AnythingOfType("*models.PlacedFurniture")).Return(expectedErr)

	result, err := service.PlaceFurniture(context.Background(), apartmentID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_RemoveFurniture_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	furnitureID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, nil)

	err := service.RemoveFurniture(context.Background(), apartmentID, furnitureID, characterID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "apartment not found")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_RemoveFurniture_FurnitureNotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	furnitureID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		ApartmentType: models.ApartmentTypeStudio,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)
	mockRepo.On("GetPlacedFurnitureByID", context.Background(), furnitureID).Return(nil, nil)

	err := service.RemoveFurniture(context.Background(), apartmentID, furnitureID, characterID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "furniture not found")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_RemoveFurniture_Unauthorized(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	furnitureID := uuid.New()
	ownerID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       ownerID,
		ApartmentType: models.ApartmentTypeStudio,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(apartment, nil)

	err := service.RemoveFurniture(context.Background(), apartmentID, furnitureID, characterID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "unauthorized")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetFurnitureItem_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	itemID := "nonexistent"

	mockRepo.On("GetFurnitureItemByID", context.Background(), itemID).Return(nil, nil)

	result, err := service.GetFurnitureItem(context.Background(), itemID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetFurnitureItem_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	itemID := "furniture_001"
	expectedErr := errors.New("database error")

	mockRepo.On("GetFurnitureItemByID", context.Background(), itemID).Return(nil, expectedErr)

	result, err := service.GetFurnitureItem(context.Background(), itemID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_ListFurnitureItems_Empty(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	mockRepo.On("ListFurnitureItems", context.Background(), (*models.FurnitureCategory)(nil), 10, 0).Return([]models.FurnitureItem{}, 0, nil)

	result, total, err := service.ListFurnitureItems(context.Background(), nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 0)
	assert.Equal(t, 0, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_ListFurnitureItems_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	expectedErr := errors.New("database error")
	mockRepo.On("ListFurnitureItems", context.Background(), (*models.FurnitureCategory)(nil), 10, 0).Return(nil, 0, expectedErr)

	result, total, err := service.ListFurnitureItems(context.Background(), nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Equal(t, 0, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_VisitApartment_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	apartmentID := uuid.New()
	characterID := uuid.New()
	req := &models.VisitApartmentRequest{
		CharacterID: characterID,
		ApartmentID: apartmentID,
	}

	mockRepo.On("GetApartmentByID", context.Background(), apartmentID).Return(nil, nil)

	err := service.VisitApartment(context.Background(), req)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "apartment not found")
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetPrestigeLeaderboard_Empty(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	mockRepo.On("GetPrestigeLeaderboard", context.Background(), 10, 0).Return([]models.PrestigeLeaderboardEntry{}, 0, nil)

	result, total, err := service.GetPrestigeLeaderboard(context.Background(), 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result, 0)
	assert.Equal(t, 0, total)
	mockRepo.AssertExpectations(t)
}

func TestHousingService_GetPrestigeLeaderboard_RepositoryError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	defer cleanup()

	expectedErr := errors.New("database error")
	mockRepo.On("GetPrestigeLeaderboard", context.Background(), 10, 0).Return(nil, 0, expectedErr)

	result, total, err := service.GetPrestigeLeaderboard(context.Background(), 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Equal(t, 0, total)
	mockRepo.AssertExpectations(t)
}

