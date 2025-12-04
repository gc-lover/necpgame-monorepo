// Issue: #140890954
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
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockMovementServiceForHandlers struct {
	mock.Mock
}

func (m *mockMovementServiceForHandlers) GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterPosition), args.Error(1)
}

func (m *mockMovementServiceForHandlers) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	args := m.Called(ctx, characterID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterPosition), args.Error(1)
}

func (m *mockMovementServiceForHandlers) GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error) {
	args := m.Called(ctx, characterID, limit)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PositionHistory), args.Error(1)
}

func TestNewMovementHandlers(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	assert.NotNil(t, handlers)
	assert.Equal(t, mockService, handlers.service)
	assert.NotNil(t, handlers.logger)
}

func TestMovementHandlers_GetPosition_Success(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	expectedPos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.On("GetPosition", mock.Anything, characterID).Return(expectedPos, nil)

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/position", nil)
	apiCharID := openapi_types.UUID(characterID)

	handlers.GetPosition(w, r, apiCharID)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)

	var response api.CharacterPosition
	err := json.Unmarshal(w.Body.Bytes(), &response)
	require.NoError(t, err)
	assert.NotNil(t, response.CharacterId)
	assert.Equal(t, characterID, uuid.UUID(*response.CharacterId))
}

func TestMovementHandlers_GetPosition_NotFound(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPosition", mock.Anything, characterID).Return(nil, nil)

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/position", nil)
	apiCharID := openapi_types.UUID(characterID)

	handlers.GetPosition(w, r, apiCharID)

	assert.Equal(t, http.StatusNotFound, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_GetPosition_Error(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPosition", mock.Anything, characterID).Return(nil, errors.New("database error"))

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/position", nil)
	apiCharID := openapi_types.UUID(characterID)

	handlers.GetPosition(w, r, apiCharID)

	assert.Equal(t, http.StatusInternalServerError, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_GetPosition_PositionNotFoundError(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPosition", mock.Anything, characterID).Return(nil, errors.New("position not found"))

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/position", nil)
	apiCharID := openapi_types.UUID(characterID)

	handlers.GetPosition(w, r, apiCharID)

	assert.Equal(t, http.StatusNotFound, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_SavePosition_Success(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	reqBody := api.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: float32Ptr(1.0),
		VelocityY: float32Ptr(0.0),
		VelocityZ: float32Ptr(0.0),
	}

	expectedPos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.On("SavePosition", mock.Anything, characterID, mock.AnythingOfType("*models.SavePositionRequest")).Return(expectedPos, nil)

	body, _ := json.Marshal(reqBody)
	w := httptest.NewRecorder()
	r := httptest.NewRequest("POST", "/api/v1/movement/"+characterID.String()+"/position", bytes.NewBuffer(body))
	r.Header.Set("Content-Type", "application/json")
	apiCharID := openapi_types.UUID(characterID)

	handlers.SavePosition(w, r, apiCharID)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)

	var response api.CharacterPosition
	err := json.Unmarshal(w.Body.Bytes(), &response)
	require.NoError(t, err)
	assert.NotNil(t, response.CharacterId)
	assert.Equal(t, characterID, uuid.UUID(*response.CharacterId))
}

func TestMovementHandlers_SavePosition_InvalidJSON(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	w := httptest.NewRecorder()
	r := httptest.NewRequest("POST", "/api/v1/movement/"+characterID.String()+"/position", bytes.NewBufferString("invalid json"))
	r.Header.Set("Content-Type", "application/json")
	apiCharID := openapi_types.UUID(characterID)

	handlers.SavePosition(w, r, apiCharID)

	assert.Equal(t, http.StatusBadRequest, w.Code)
	mockService.AssertNotCalled(t, "SavePosition")
}

func TestMovementHandlers_SavePosition_ServiceError(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	reqBody := api.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
	}

	mockService.On("SavePosition", mock.Anything, characterID, mock.AnythingOfType("*models.SavePositionRequest")).Return(nil, errors.New("database error"))

	body, _ := json.Marshal(reqBody)
	w := httptest.NewRecorder()
	r := httptest.NewRequest("POST", "/api/v1/movement/"+characterID.String()+"/position", bytes.NewBuffer(body))
	r.Header.Set("Content-Type", "application/json")
	apiCharID := openapi_types.UUID(characterID)

	handlers.SavePosition(w, r, apiCharID)

	assert.Equal(t, http.StatusInternalServerError, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_GetPositionHistory_Success(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	expectedHistory := []models.PositionHistory{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			PositionX:   10.5,
			PositionY:   20.3,
			PositionZ:   30.1,
			Yaw:         45.0,
			CreatedAt:   time.Now(),
		},
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			PositionX:   11.5,
			PositionY:   21.3,
			PositionZ:   31.1,
			Yaw:         46.0,
			CreatedAt:   time.Now(),
		},
	}

	mockService.On("GetPositionHistory", mock.Anything, characterID, 10).Return(expectedHistory, nil)

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history?limit=10", nil)
	apiCharID := openapi_types.UUID(characterID)
	limit := 10
	params := api.GetPositionHistoryParams{
		Limit: &limit,
	}

	handlers.GetPositionHistory(w, r, apiCharID, params)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)

	var response []api.PositionHistory
	err := json.Unmarshal(w.Body.Bytes(), &response)
	require.NoError(t, err)
	assert.Len(t, response, 2)
}

func TestMovementHandlers_GetPositionHistory_Empty(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPositionHistory", mock.Anything, characterID, 50).Return([]models.PositionHistory{}, nil)

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history", nil)
	apiCharID := openapi_types.UUID(characterID)
	params := api.GetPositionHistoryParams{}

	handlers.GetPositionHistory(w, r, apiCharID, params)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)

	var response []api.PositionHistory
	err := json.Unmarshal(w.Body.Bytes(), &response)
	require.NoError(t, err)
	assert.Len(t, response, 0)
}

func TestMovementHandlers_GetPositionHistory_InvalidLimit(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPositionHistory", mock.Anything, characterID, 50).Return([]models.PositionHistory{}, nil)

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history?limit=0", nil)
	apiCharID := openapi_types.UUID(characterID)
	limit := 0
	params := api.GetPositionHistoryParams{
		Limit: &limit,
	}

	handlers.GetPositionHistory(w, r, apiCharID, params)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_GetPositionHistory_LimitExceedsMax(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPositionHistory", mock.Anything, characterID, 50).Return([]models.PositionHistory{}, nil)

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history?limit=200", nil)
	apiCharID := openapi_types.UUID(characterID)
	limit := 200
	params := api.GetPositionHistoryParams{
		Limit: &limit,
	}

	handlers.GetPositionHistory(w, r, apiCharID, params)

	assert.Equal(t, http.StatusOK, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_GetPositionHistory_ServiceError(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewMovementHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPositionHistory", mock.Anything, characterID, 50).Return(nil, errors.New("database error"))

	w := httptest.NewRecorder()
	r := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history", nil)
	apiCharID := openapi_types.UUID(characterID)
	params := api.GetPositionHistoryParams{}

	handlers.GetPositionHistory(w, r, apiCharID, params)

	assert.Equal(t, http.StatusInternalServerError, w.Code)
	mockService.AssertExpectations(t)
}

func TestMovementHandlers_respondJSON(t *testing.T) {
	handlers := NewMovementHandlers(new(mockMovementServiceForHandlers))

	w := httptest.NewRecorder()
	data := map[string]string{"test": "value"}

	handlers.respondJSON(w, http.StatusOK, data)

	assert.Equal(t, http.StatusOK, w.Code)
	assert.Equal(t, "application/json", w.Header().Get("Content-Type"))

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	require.NoError(t, err)
	assert.Equal(t, "value", response["test"])
}

func TestMovementHandlers_respondError(t *testing.T) {
	handlers := NewMovementHandlers(new(mockMovementServiceForHandlers))

	w := httptest.NewRecorder()
	handlers.respondError(w, http.StatusBadRequest, "test error")

	assert.Equal(t, http.StatusBadRequest, w.Code)
	assert.Equal(t, "application/json", w.Header().Get("Content-Type"))

	var response api.Error
	err := json.Unmarshal(w.Body.Bytes(), &response)
	require.NoError(t, err)
	assert.Equal(t, "test error", response.Message)
}

func TestToAPICharacterPosition(t *testing.T) {
	pos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: uuid.New(),
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	apiPos := toAPICharacterPosition(pos)

	assert.NotNil(t, apiPos.Id)
	assert.NotNil(t, apiPos.CharacterId)
	assert.NotNil(t, apiPos.PositionX)
	assert.Equal(t, float32(10.5), *apiPos.PositionX)
	assert.Equal(t, float32(20.3), *apiPos.PositionY)
	assert.Equal(t, float32(30.1), *apiPos.PositionZ)
	assert.Equal(t, float32(45.0), *apiPos.Yaw)
}

func TestToAPICharacterPosition_Nil(t *testing.T) {
	apiPos := toAPICharacterPosition(nil)

	assert.Nil(t, apiPos.Id)
	assert.Nil(t, apiPos.CharacterId)
}

func TestToAPIPositionHistory(t *testing.T) {
	ph := &models.PositionHistory{
		ID:          uuid.New(),
		CharacterID: uuid.New(),
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
		CreatedAt:   time.Now(),
	}

	apiPh := toAPIPositionHistory(ph)

	assert.NotNil(t, apiPh.Id)
	assert.NotNil(t, apiPh.CharacterId)
	assert.NotNil(t, apiPh.PositionX)
	assert.Equal(t, float32(10.5), *apiPh.PositionX)
}

func TestToAPIPositionHistory_Nil(t *testing.T) {
	apiPh := toAPIPositionHistory(nil)

	assert.Nil(t, apiPh.Id)
	assert.Nil(t, apiPh.CharacterId)
}

func TestToModelSavePositionRequest(t *testing.T) {
	velX := float32(1.0)
	velY := float32(0.0)
	velZ := float32(0.0)
	req := &api.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: &velX,
		VelocityY: &velY,
		VelocityZ: &velZ,
	}

	modelReq := toModelSavePositionRequest(req)

	assert.NotNil(t, modelReq)
	assert.InDelta(t, 10.5, modelReq.PositionX, 0.0001)
	assert.InDelta(t, 20.3, modelReq.PositionY, 0.0001)
	assert.InDelta(t, 30.1, modelReq.PositionZ, 0.0001)
	assert.InDelta(t, 45.0, modelReq.Yaw, 0.0001)
	assert.InDelta(t, 1.0, modelReq.VelocityX, 0.0001)
	assert.InDelta(t, 0.0, modelReq.VelocityY, 0.0001)
	assert.InDelta(t, 0.0, modelReq.VelocityZ, 0.0001)
}

func TestToModelSavePositionRequest_NilVelocity(t *testing.T) {
	req := &api.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
	}

	modelReq := toModelSavePositionRequest(req)

	assert.NotNil(t, modelReq)
	assert.Equal(t, 0.0, modelReq.VelocityX)
	assert.Equal(t, 0.0, modelReq.VelocityY)
	assert.Equal(t, 0.0, modelReq.VelocityZ)
}

func TestToModelSavePositionRequest_Nil(t *testing.T) {
	modelReq := toModelSavePositionRequest(nil)

	assert.Nil(t, modelReq)
}

func float32Ptr(f float32) *float32 {
	return &f
}

