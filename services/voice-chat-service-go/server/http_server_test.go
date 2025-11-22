package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/voice-chat-service-go/models"
)

type mockVoiceService struct {
	channels     map[uuid.UUID]*models.VoiceChannel
	participants map[uuid.UUID][]models.VoiceParticipant
	createErr    error
	getErr       error
}

func (m *mockVoiceService) CreateChannel(ctx context.Context, req *models.CreateChannelRequest) (*models.VoiceChannel, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}

	maxMembers := req.MaxMembers
	if maxMembers == 0 {
		switch req.Type {
		case models.VoiceChannelTypeParty:
			maxMembers = 5
		case models.VoiceChannelTypeGuild:
			maxMembers = 100
		case models.VoiceChannelTypeRaid:
			maxMembers = 25
		case models.VoiceChannelTypeProximity:
			maxMembers = 20
		}
	}

	channel := &models.VoiceChannel{
		ID:            uuid.New(),
		OwnerID:       req.CharacterID,
		OwnerType:     "character",
		Type:          req.Type,
		Name:          req.Name,
		MaxMembers:    maxMembers,
		QualityPreset: req.QualityPreset,
		Settings:      req.Settings,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	if channel.Settings == nil {
		channel.Settings = make(map[string]interface{})
	}

	m.channels[channel.ID] = channel
	return channel, nil
}

func (m *mockVoiceService) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.channels[channelID], nil
}

func (m *mockVoiceService) ListChannels(ctx context.Context, channelType *models.VoiceChannelType, ownerID *uuid.UUID, limit, offset int) (*models.ChannelListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	channels := []models.VoiceChannel{}
	for _, ch := range m.channels {
		if channelType != nil && ch.Type != *channelType {
			continue
		}
		if ownerID != nil && ch.OwnerID != *ownerID {
			continue
		}
		channels = append(channels, *ch)
	}

	total := len(channels)
	if offset >= total {
		return &models.ChannelListResponse{Channels: []models.VoiceChannel{}, Total: total}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.ChannelListResponse{
		Channels: channels[offset:end],
		Total:    total,
	}, nil
}

func (m *mockVoiceService) JoinChannel(ctx context.Context, req *models.JoinChannelRequest) (*models.WebRTCTokenResponse, error) {
	channel := m.channels[req.ChannelID]
	if channel == nil {
		return nil, nil
	}

	participants := m.participants[req.ChannelID]
	if len(participants) >= channel.MaxMembers {
		return nil, &channelFullError{}
	}

	for _, p := range participants {
		if p.CharacterID == req.CharacterID {
			return &models.WebRTCTokenResponse{
				Token:     p.WebRTCToken,
				ServerURL: "wss://voice.example.com",
				ExpiresAt: time.Now().Add(24 * time.Hour),
			}, nil
		}
	}

	token := uuid.New().String()
	participant := models.VoiceParticipant{
		ID:          uuid.New(),
		ChannelID:   req.ChannelID,
		CharacterID: req.CharacterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: token,
		Position:    req.Position,
		Stats:       make(map[string]interface{}),
		JoinedAt:    time.Now(),
		UpdatedAt:   time.Now(),
	}

	m.participants[req.ChannelID] = append(m.participants[req.ChannelID], participant)

	return &models.WebRTCTokenResponse{
		Token:     token,
		ServerURL: "wss://voice.example.com",
		ExpiresAt: time.Now().Add(24 * time.Hour),
	}, nil
}

func (m *mockVoiceService) LeaveChannel(ctx context.Context, req *models.LeaveChannelRequest) error {
	participants := m.participants[req.ChannelID]
	for i, p := range participants {
		if p.CharacterID == req.CharacterID {
			m.participants[req.ChannelID] = append(participants[:i], participants[i+1:]...)
			return nil
		}
	}
	return nil
}

func (m *mockVoiceService) UpdateParticipantStatus(ctx context.Context, req *models.UpdateParticipantStatusRequest) error {
	participants := m.participants[req.ChannelID]
	for i, p := range participants {
		if p.CharacterID == req.CharacterID {
			participants[i].Status = req.Status
			participants[i].UpdatedAt = time.Now()
			return nil
		}
	}
	return nil
}

func (m *mockVoiceService) UpdateParticipantPosition(ctx context.Context, req *models.UpdateParticipantPositionRequest) error {
	participants := m.participants[req.ChannelID]
	for i, p := range participants {
		if p.CharacterID == req.CharacterID {
			participants[i].Position = req.Position
			participants[i].UpdatedAt = time.Now()
			return nil
		}
	}
	return nil
}

func (m *mockVoiceService) GetChannelParticipants(ctx context.Context, channelID uuid.UUID) (*models.ParticipantListResponse, error) {
	participants := m.participants[channelID]
	return &models.ParticipantListResponse{
		Participants: participants,
		Total:        len(participants),
	}, nil
}

func (m *mockVoiceService) GetChannelDetail(ctx context.Context, channelID uuid.UUID) (*models.ChannelDetailResponse, error) {
	channel := m.channels[channelID]
	if channel == nil {
		return nil, nil
	}

	participants := m.participants[channelID]
	return &models.ChannelDetailResponse{
		Channel:      channel,
		Participants: participants,
	}, nil
}

type channelFullError struct{}

func (e *channelFullError) Error() string {
	return "channel is full"
}

func TestHTTPServer_CreateChannel(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	characterID := uuid.New()
	reqBody := models.CreateChannelRequest{
		CharacterID:   characterID,
		Type:          models.VoiceChannelTypeParty,
		Name:          "Test Party",
		QualityPreset: "standard",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/voice/channels", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.VoiceChannel
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Name != "Test Party" {
		t.Errorf("Expected name 'Test Party', got %s", response.Name)
	}

	if response.Type != models.VoiceChannelTypeParty {
		t.Errorf("Expected type 'party', got %s", response.Type)
	}
}

func TestHTTPServer_ListChannels(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}

	characterID := uuid.New()
	channel1 := &models.VoiceChannel{
		ID:          uuid.New(),
		OwnerID:     characterID,
		Type:        models.VoiceChannelTypeParty,
		Name:        "Party 1",
		MaxMembers:  5,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	channel2 := &models.VoiceChannel{
		ID:          uuid.New(),
		OwnerID:     characterID,
		Type:        models.VoiceChannelTypeGuild,
		Name:        "Guild 1",
		MaxMembers:  100,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.channels[channel1.ID] = channel1
	mockService.channels[channel2.ID] = channel2

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/voice/channels", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ChannelListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetChannel(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}

	channelID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		OwnerID:     uuid.New(),
		Type:        models.VoiceChannelTypeParty,
		Name:        "Test Party",
		MaxMembers:  5,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.channels[channelID] = channel
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/voice/channels/"+channelID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.VoiceChannel
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != channelID {
		t.Errorf("Expected ID %s, got %s", channelID, response.ID)
	}
}

func TestHTTPServer_GetChannelNotFound(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/voice/channels/"+uuid.New().String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_JoinChannel(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}

	channelID := uuid.New()
	characterID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		OwnerID:     characterID,
		Type:        models.VoiceChannelTypeParty,
		Name:        "Test Party",
		MaxMembers:  5,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.channels[channelID] = channel
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.JoinChannelRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/voice/channels/"+channelID.String()+"/join", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response models.WebRTCTokenResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Token == "" {
		t.Error("Token is empty")
	}
}

func TestHTTPServer_LeaveChannel(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}

	channelID := uuid.New()
	characterID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		OwnerID:     characterID,
		Type:        models.VoiceChannelTypeParty,
		Name:        "Test Party",
		MaxMembers:  5,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.channels[channelID] = channel
	participant := models.VoiceParticipant{
		ID:          uuid.New(),
		ChannelID:   channelID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		JoinedAt:    time.Now(),
		UpdatedAt:   time.Now(),
	}
	mockService.participants[channelID] = []models.VoiceParticipant{participant}

	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.LeaveChannelRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/voice/channels/"+channelID.String()+"/leave", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetParticipants(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}

	channelID := uuid.New()
	characterID := uuid.New()
	participant := models.VoiceParticipant{
		ID:          uuid.New(),
		ChannelID:   channelID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		JoinedAt:    time.Now(),
		UpdatedAt:   time.Now(),
	}
	mockService.participants[channelID] = []models.VoiceParticipant{participant}

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/voice/channels/"+channelID.String()+"/participants", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ParticipantListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_UpdateParticipantStatus(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
	}

	channelID := uuid.New()
	characterID := uuid.New()
	participant := models.VoiceParticipant{
		ID:          uuid.New(),
		ChannelID:   channelID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		JoinedAt:    time.Now(),
		UpdatedAt:   time.Now(),
	}
	mockService.participants[channelID] = []models.VoiceParticipant{participant}

	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.UpdateParticipantStatusRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
		Status:      models.ParticipantStatusMuted,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/voice/channels/"+channelID.String()+"/participants/"+characterID.String()+"/status", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockVoiceService{
		channels:     make(map[uuid.UUID]*models.VoiceChannel),
		participants: make(map[uuid.UUID][]models.VoiceParticipant),
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

