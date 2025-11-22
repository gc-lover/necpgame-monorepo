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
	"github.com/necpgame/housing-service-go/models"
)

type mockHousingService struct {
	apartments        map[uuid.UUID]*models.Apartment
	apartmentList     map[string][]models.Apartment
	apartmentDetails  map[uuid.UUID]*models.ApartmentDetailResponse
	furnitureItems    map[string]*models.FurnitureItem
	furnitureList     []models.FurnitureItem
	placedFurniture   map[uuid.UUID][]models.PlacedFurniture
	leaderboard       []models.PrestigeLeaderboardEntry
	purchaseErr       error
	updateErr         error
	placeErr          error
	removeErr         error
	visitErr          error
}

func (m *mockHousingService) PurchaseApartment(ctx context.Context, req *models.PurchaseApartmentRequest) (*models.Apartment, error) {
	if m.purchaseErr != nil {
		return nil, m.purchaseErr
	}

	apartment := &models.Apartment{
		ID:            uuid.New(),
		OwnerID:       req.CharacterID,
		OwnerType:     "character",
		ApartmentType: req.ApartmentType,
		Location:      req.Location,
		Price:         100000,
		FurnitureSlots: 10,
		PrestigeScore: 0,
		IsPublic:      false,
		Guests:        []uuid.UUID{},
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	m.apartments[apartment.ID] = apartment
	return apartment, nil
}

func (m *mockHousingService) GetApartment(ctx context.Context, apartmentID uuid.UUID) (*models.Apartment, error) {
	apartment, ok := m.apartments[apartmentID]
	if !ok {
		return nil, errors.New("apartment not found")
	}
	return apartment, nil
}

func (m *mockHousingService) ListApartments(ctx context.Context, ownerID *uuid.UUID, ownerType *string, isPublic *bool, limit, offset int) ([]models.Apartment, int, error) {
	var apartments []models.Apartment
	key := "all"
	if ownerID != nil {
		key = ownerID.String()
	}

	list, ok := m.apartmentList[key]
	if !ok {
		list = []models.Apartment{}
	}

	for _, apt := range list {
		if ownerID != nil && apt.OwnerID != *ownerID {
			continue
		}
		if ownerType != nil && apt.OwnerType != *ownerType {
			continue
		}
		if isPublic != nil && apt.IsPublic != *isPublic {
			continue
		}
		apartments = append(apartments, apt)
	}

	total := len(apartments)
	if offset >= total {
		return []models.Apartment{}, total, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return apartments[offset:end], total, nil
}

func (m *mockHousingService) UpdateApartmentSettings(ctx context.Context, apartmentID uuid.UUID, req *models.UpdateApartmentSettingsRequest) error {
	if m.updateErr != nil {
		return m.updateErr
	}

	apartment, ok := m.apartments[apartmentID]
	if !ok {
		return errors.New("apartment not found")
	}

	if req.IsPublic != nil {
		apartment.IsPublic = *req.IsPublic
	}
	if req.Guests != nil {
		apartment.Guests = req.Guests
	}
	if req.Settings != nil {
		apartment.Settings = req.Settings
	}
	apartment.UpdatedAt = time.Now()
	return nil
}

func (m *mockHousingService) PlaceFurniture(ctx context.Context, apartmentID uuid.UUID, req *models.PlaceFurnitureRequest) (*models.PlacedFurniture, error) {
	if m.placeErr != nil {
		return nil, m.placeErr
	}

	furniture := &models.PlacedFurniture{
		ID:            uuid.New(),
		ApartmentID:   apartmentID,
		FurnitureItemID: req.FurnitureItemID,
		Position:      req.Position,
		Rotation:      req.Rotation,
		Scale:         req.Scale,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	m.placedFurniture[apartmentID] = append(m.placedFurniture[apartmentID], *furniture)
	return furniture, nil
}

func (m *mockHousingService) RemoveFurniture(ctx context.Context, apartmentID, furnitureID uuid.UUID, characterID uuid.UUID) error {
	if m.removeErr != nil {
		return m.removeErr
	}

	furnitureList, ok := m.placedFurniture[apartmentID]
	if !ok {
		return errors.New("apartment not found")
	}

	for i, f := range furnitureList {
		if f.ID == furnitureID {
			m.placedFurniture[apartmentID] = append(furnitureList[:i], furnitureList[i+1:]...)
			return nil
		}
	}

	return errors.New("furniture not found")
}

func (m *mockHousingService) GetFurnitureItem(ctx context.Context, itemID string) (*models.FurnitureItem, error) {
	item, ok := m.furnitureItems[itemID]
	if !ok {
		return nil, errors.New("furniture item not found")
	}
	return item, nil
}

func (m *mockHousingService) ListFurnitureItems(ctx context.Context, category *models.FurnitureCategory, limit, offset int) ([]models.FurnitureItem, int, error) {
	var items []models.FurnitureItem
	for _, item := range m.furnitureList {
		if category != nil && item.Category != *category {
			continue
		}
		items = append(items, item)
	}

	total := len(items)
	if offset >= total {
		return []models.FurnitureItem{}, total, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return items[offset:end], total, nil
}

func (m *mockHousingService) GetApartmentDetail(ctx context.Context, apartmentID uuid.UUID) (*models.ApartmentDetailResponse, error) {
	detail, ok := m.apartmentDetails[apartmentID]
	if !ok {
		apartment, ok := m.apartments[apartmentID]
		if !ok {
			return nil, errors.New("apartment not found")
		}
		detail = &models.ApartmentDetailResponse{
			Apartment: apartment,
			Furniture: m.placedFurniture[apartmentID],
		}
	}
	return detail, nil
}

func (m *mockHousingService) VisitApartment(ctx context.Context, req *models.VisitApartmentRequest) error {
	return m.visitErr
}

func (m *mockHousingService) GetPrestigeLeaderboard(ctx context.Context, limit, offset int) ([]models.PrestigeLeaderboardEntry, int, error) {
	total := len(m.leaderboard)
	if offset >= total {
		return []models.PrestigeLeaderboardEntry{}, total, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return m.leaderboard[offset:end], total, nil
}

func createRequestWithCharacterID(method, url string, body []byte, characterID uuid.UUID) *http.Request {
	req := httptest.NewRequest(method, url, bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	claims := &Claims{
		Subject: characterID.String(),
	}
	ctx := context.WithValue(req.Context(), "claims", claims)
	return req.WithContext(ctx)
}

func TestHTTPServer_PurchaseApartment(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	server := NewHTTPServer(":8080", mockService, nil, false)

	characterID := uuid.New()
	reqBody := map[string]interface{}{
		"apartment_type": models.ApartmentTypeStandard,
		"location":       "District 1",
	}

	body, _ := json.Marshal(reqBody)
	req := createRequestWithCharacterID("POST", "/api/v1/housing/apartments", body, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var apartment models.Apartment
	if err := json.Unmarshal(w.Body.Bytes(), &apartment); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if apartment.OwnerID != characterID {
		t.Errorf("Expected owner_id %s, got %s", characterID, apartment.OwnerID)
	}
}

func TestHTTPServer_GetApartment(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypeStandard,
		Location:      "District 1",
		Price:         100000,
		FurnitureSlots: 10,
		PrestigeScore: 0,
		IsPublic:      false,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartments[apartmentID] = apartment

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/apartments/"+apartmentID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Apartment
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != apartmentID {
		t.Errorf("Expected apartment_id %s, got %s", apartmentID, response.ID)
	}
}

func TestHTTPServer_ListApartments(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	ownerID := uuid.New()
	apartment1 := models.Apartment{
		ID:            uuid.New(),
		OwnerID:       ownerID,
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypeStandard,
		IsPublic:      true,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	apartment2 := models.Apartment{
		ID:            uuid.New(),
		OwnerID:       ownerID,
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypePenthouse,
		IsPublic:      false,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartmentList[ownerID.String()] = []models.Apartment{apartment1, apartment2}

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/apartments?owner_id="+ownerID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ApartmentListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetApartmentDetail(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		ApartmentType: models.ApartmentTypeStandard,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	detail := &models.ApartmentDetailResponse{
		Apartment: apartment,
		Furniture: []models.PlacedFurniture{},
	}

	mockService.apartments[apartmentID] = apartment
	mockService.apartmentDetails[apartmentID] = detail

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/apartments/"+apartmentID.String()+"/detail", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_UpdateApartmentSettings(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		OwnerType:     "character",
		IsPublic:      false,
		Settings:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartments[apartmentID] = apartment

	server := NewHTTPServer(":8080", mockService, nil, false)

	isPublic := true
	reqBody := map[string]interface{}{
		"is_public": isPublic,
	}

	body, _ := json.Marshal(reqBody)
	req := createRequestWithCharacterID("PUT", "/api/v1/housing/apartments/"+apartmentID.String()+"/settings", body, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_PlaceFurniture(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartments[apartmentID] = apartment
	mockService.placedFurniture[apartmentID] = []models.PlacedFurniture{}

	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := map[string]interface{}{
		"furniture_item_id": "chair1",
		"position":          map[string]interface{}{"x": 1.0, "y": 0.0, "z": 2.0},
		"rotation":           map[string]interface{}{"x": 0.0, "y": 90.0, "z": 0.0},
		"scale":              map[string]interface{}{"x": 1.0, "y": 1.0, "z": 1.0},
	}

	body, _ := json.Marshal(reqBody)
	req := createRequestWithCharacterID("POST", "/api/v1/housing/apartments/"+apartmentID.String()+"/furniture", body, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}
}

func TestHTTPServer_ListPlacedFurniture(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	furniture := models.PlacedFurniture{
		ID:            uuid.New(),
		ApartmentID:   apartmentID,
		FurnitureItemID: "chair1",
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartments[apartmentID] = apartment
	mockService.placedFurniture[apartmentID] = []models.PlacedFurniture{furniture}
	mockService.apartmentDetails[apartmentID] = &models.ApartmentDetailResponse{
		Apartment: apartment,
		Furniture: []models.PlacedFurniture{furniture},
	}

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/apartments/"+apartmentID.String()+"/furniture", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.PlacedFurnitureListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_RemoveFurniture(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	furnitureID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       characterID,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	furniture := models.PlacedFurniture{
		ID:            furnitureID,
		ApartmentID:   apartmentID,
		FurnitureItemID: "chair1",
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartments[apartmentID] = apartment
	mockService.placedFurniture[apartmentID] = []models.PlacedFurniture{furniture}

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := createRequestWithCharacterID("DELETE", "/api/v1/housing/apartments/"+apartmentID.String()+"/furniture/"+furnitureID.String(), nil, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetFurnitureItem(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	itemID := "chair1"
	item := &models.FurnitureItem{
		ID:            itemID,
		Category:      models.FurnitureCategoryComfort,
		Name:          "Comfortable Chair",
		Description:   "A comfortable chair",
		Price:         5000,
		PrestigeValue: 10,
		CreatedAt:     time.Now(),
	}

	mockService.furnitureItems[itemID] = item

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/furniture/"+itemID, nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.FurnitureItem
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != itemID {
		t.Errorf("Expected item_id %s, got %s", itemID, response.ID)
	}
}

func TestHTTPServer_ListFurnitureItems(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	item1 := models.FurnitureItem{
		ID:            "chair1",
		Category:      models.FurnitureCategoryComfort,
		Name:          "Chair",
		Price:         5000,
		CreatedAt:     time.Now(),
	}

	item2 := models.FurnitureItem{
		ID:            "table1",
		Category:      models.FurnitureCategoryFunctional,
		Name:          "Table",
		Price:         10000,
		CreatedAt:     time.Now(),
	}

	mockService.furnitureList = []models.FurnitureItem{item1, item2}

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/furniture", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.FurnitureListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_VisitApartment(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	apartmentID := uuid.New()
	characterID := uuid.New()
	apartment := &models.Apartment{
		ID:            apartmentID,
		OwnerID:       uuid.New(),
		IsPublic:      true,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.apartments[apartmentID] = apartment

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := createRequestWithCharacterID("POST", "/api/v1/housing/apartments/"+apartmentID.String()+"/visit", nil, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetPrestigeLeaderboard(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	entry1 := models.PrestigeLeaderboardEntry{
		ApartmentID:   uuid.New(),
		OwnerID:       uuid.New(),
		OwnerName:     "Player 1",
		PrestigeScore: 1000,
		ApartmentType: models.ApartmentTypePenthouse,
		Location:      "District 1",
	}

	entry2 := models.PrestigeLeaderboardEntry{
		ApartmentID:   uuid.New(),
		OwnerID:       uuid.New(),
		OwnerName:     "Player 2",
		PrestigeScore: 800,
		ApartmentType: models.ApartmentTypeStandard,
		Location:      "District 2",
	}

	mockService.leaderboard = []models.PrestigeLeaderboardEntry{entry1, entry2}

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/housing/leaderboard/prestige", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.PrestigeLeaderboardResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockHousingService{
		apartments:      make(map[uuid.UUID]*models.Apartment),
		apartmentList:   make(map[string][]models.Apartment),
		apartmentDetails: make(map[uuid.UUID]*models.ApartmentDetailResponse),
		furnitureItems:  make(map[string]*models.FurnitureItem),
		placedFurniture: make(map[uuid.UUID][]models.PlacedFurniture),
	}

	server := NewHTTPServer(":8080", mockService, nil, false)

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

