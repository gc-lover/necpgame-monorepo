//go:build unit

package handlers

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"

	"necpgame/services/achievement-service-go/internal/models"
	"necpgame/services/achievement-service-go/internal/service"
	"necpgame/scripts/core/error-handling"
)

// Mock service for testing
type mockAchievementService struct {
	mock.Mock
}

func (m *mockAchievementService) CreateAchievement(ctx context.Context, achievement *models.Achievement) error {
	args := m.Called(ctx, achievement)
	return args.Error(0)
}

func (m *mockAchievementService) GetAchievement(ctx context.Context, id string) (*models.Achievement, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Achievement), args.Error(1)
}

func (m *mockAchievementService) UpdateAchievement(ctx context.Context, achievement *models.Achievement) error {
	args := m.Called(ctx, achievement)
	return args.Error(0)
}

func (m *mockAchievementService) DeleteAchievement(ctx context.Context, id string) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

func (m *mockAchievementService) ListAchievements(ctx context.Context, limit, offset int) ([]*models.Achievement, error) {
	args := m.Called(ctx, limit, offset)
	return args.Get(0).([]*models.Achievement), args.Error(1)
}

func TestAchievementHandler_GetAchievement_Success(t *testing.T) {
	// Setup
	mockService := &mockAchievementService{}
	logger := errorhandling.NewLogger("test")
	handler := NewAchievementHandler(mockService, logger)

	achievementID := "test-achievement-123"
	expectedAchievement := &models.Achievement{
		ID:          achievementID,
		Name:        "Test Achievement",
		Description: "Test Description",
		Type:        models.AchievementTypeProgress,
		CreatedAt:   time.Now(),
	}

	// Create request
	req := httptest.NewRequest("GET", "/api/v1/achievements/"+achievementID, nil)

	// Add path parameter
	rctx := chi.NewRouteContext()
	rctx.URLParams.Add("achievementId", achievementID)
	req = req.WithContext(context.WithValue(req.Context(), chi.RouteCtxKey, rctx))

	// Create response recorder
	w := httptest.NewRecorder()

	// Mock expectations
	mockService.On("GetAchievement", mock.Anything, achievementID).Return(expectedAchievement, nil)

	// Execute
	handler.GetAchievement(w, req)

	// Assert
	assert.Equal(t, http.StatusOK, w.Code)

	var response map[string]interface{}
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)

	// Check response structure
	assert.Equal(t, "success", response["status"])
	assert.NotNil(t, response["data"])

	data := response["data"].(map[string]interface{})
	assert.Equal(t, achievementID, data["id"])
	assert.Equal(t, "Test Achievement", data["name"])

	mockService.AssertExpectations(t)
}

func TestAchievementHandler_GetAchievement_NotFound(t *testing.T) {
	// Setup
	mockService := &mockAchievementService{}
	logger := errorhandling.NewLogger("test")
	handler := NewAchievementHandler(mockService, logger)

	achievementID := "non-existent-id"

	// Create request
	req := httptest.NewRequest("GET", "/api/v1/achievements/"+achievementID, nil)

	// Add path parameter
	rctx := chi.NewRouteContext()
	rctx.URLParams.Add("achievementId", achievementID)
	req = req.WithContext(context.WithValue(req.Context(), chi.RouteCtxKey, rctx))

	// Create response recorder
	w := httptest.NewRecorder()

	// Mock expectations - return not found error
	mockService.On("GetAchievement", mock.Anything, achievementID).Return((*models.Achievement)(nil), models.ErrAchievementNotFound)

	// Execute
	handler.GetAchievement(w, req)

	// Assert
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]interface{}
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)

	// Check error response structure
	assert.Equal(t, "error", response["error"])
	assert.Equal(t, "NOT_FOUND_ERROR", response["type"])

	mockService.AssertExpectations(t)
}

func TestAchievementHandler_CreateAchievement_Success(t *testing.T) {
	// Setup
	mockService := &mockAchievementService{}
	logger := errorhandling.NewLogger("test")
	handler := NewAchievementHandler(mockService, logger)

	achievement := &models.Achievement{
		Name:        "New Achievement",
		Description: "New Description",
		Type:        models.AchievementTypeProgress,
	}

	// Create request body
	requestBody := map[string]interface{}{
		"name":        achievement.Name,
		"description": achievement.Description,
		"type":        string(achievement.Type),
	}
	bodyBytes, _ := json.Marshal(requestBody)

	req := httptest.NewRequest("POST", "/api/v1/achievements", bytes.NewReader(bodyBytes))
	req.Header.Set("Content-Type", "application/json")

	w := httptest.NewRecorder()

	// Mock expectations
	mockService.On("CreateAchievement", mock.Anything, mock.MatchedBy(func(a *models.Achievement) bool {
		return a.Name == achievement.Name && a.Description == achievement.Description
	})).Return(nil)

	// Execute
	handler.CreateAchievement(w, req)

	// Assert
	assert.Equal(t, http.StatusCreated, w.Code)

	var response map[string]interface{}
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)

	assert.Equal(t, "success", response["status"])

	mockService.AssertExpectations(t)
}

func TestAchievementHandler_CreateAchievement_ValidationError(t *testing.T) {
	// Setup
	mockService := &mockAchievementService{}
	logger := errorhandling.NewLogger("test")
	handler := NewAchievementHandler(mockService, logger)

	// Create request with invalid data (empty name)
	requestBody := map[string]interface{}{
		"name":        "",
		"description": "Description",
		"type":        "invalid_type",
	}
	bodyBytes, _ := json.Marshal(requestBody)

	req := httptest.NewRequest("POST", "/api/v1/achievements", bytes.NewReader(bodyBytes))
	req.Header.Set("Content-Type", "application/json")

	w := httptest.NewRecorder()

	// Execute - should fail validation before calling service
	handler.CreateAchievement(w, req)

	// Assert - should return validation error
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]interface{}
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)

	assert.Equal(t, "error", response["error"])
	assert.Equal(t, "VALIDATION_ERROR", response["type"])
}

// Integration test example (would be in separate file with build tag)
// func TestAchievementHandler_Integration(t *testing.T) {
//     if testing.Short() {
//         t.Skip("Skipping integration test in short mode")
//     }
//
//     // Setup real database connection
//     // Setup real service
//     // Test full request/response cycle
// }