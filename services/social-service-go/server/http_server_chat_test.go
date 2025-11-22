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
	"github.com/necpgame/social-service-go/models"
)

type mockSocialService struct {
	messages      map[uuid.UUID][]models.ChatMessage
	channels      map[uuid.UUID]*models.ChatChannel
	channelList   []models.ChatChannel
	createErr     error
	getErr        error
}

func (m *mockSocialService) CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.messages[message.ChannelID] == nil {
		m.messages[message.ChannelID] = []models.ChatMessage{}
	}
	m.messages[message.ChannelID] = append(m.messages[message.ChannelID], *message)
	return message, nil
}

func (m *mockSocialService) GetMessages(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, int, error) {
	if m.getErr != nil {
		return nil, 0, m.getErr
	}
	messages := m.messages[channelID]
	if messages == nil {
		return []models.ChatMessage{}, 0, nil
	}
	total := len(messages)
	if offset >= total {
		return []models.ChatMessage{}, total, nil
	}
	end := offset + limit
	if end > total {
		end = total
	}
	return messages[offset:end], total, nil
}

func (m *mockSocialService) GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if channelType == nil {
		return m.channelList, nil
	}
	filtered := []models.ChatChannel{}
	for _, ch := range m.channelList {
		if ch.Type == *channelType {
			filtered = append(filtered, ch)
		}
	}
	return filtered, nil
}

func (m *mockSocialService) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.channels[channelID], nil
}

func (m *mockSocialService) CreateNotification(ctx context.Context, req *models.CreateNotificationRequest) (*models.Notification, error) {
	return nil, nil
}

func (m *mockSocialService) GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error) {
	return nil, nil
}

func (m *mockSocialService) GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error) {
	return nil, nil
}

func (m *mockSocialService) UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	return nil, nil
}

func (m *mockSocialService) SendMail(ctx context.Context, req *models.CreateMailRequest, senderID *uuid.UUID, senderName string) (*models.MailMessage, error) {
	return nil, nil
}

func (m *mockSocialService) GetMails(ctx context.Context, recipientID uuid.UUID, limit, offset int) (*models.MailListResponse, error) {
	return nil, nil
}

func (m *mockSocialService) GetMail(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error) {
	return nil, nil
}

func (m *mockSocialService) MarkMailAsRead(ctx context.Context, mailID uuid.UUID) error {
	return nil
}

func (m *mockSocialService) ClaimAttachment(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
	return nil, nil
}

func (m *mockSocialService) DeleteMail(ctx context.Context, mailID uuid.UUID) error {
	return nil
}

func (m *mockSocialService) CreateGuild(ctx context.Context, leaderID uuid.UUID, req *models.CreateGuildRequest) (*models.Guild, error) {
	return nil, nil
}

func (m *mockSocialService) ListGuilds(ctx context.Context, limit, offset int) (*models.GuildListResponse, error) {
	return nil, nil
}

func (m *mockSocialService) GetGuild(ctx context.Context, guildID uuid.UUID) (*models.GuildDetailResponse, error) {
	return nil, nil
}

func (m *mockSocialService) UpdateGuild(ctx context.Context, guildID, leaderID uuid.UUID, req *models.UpdateGuildRequest) (*models.Guild, error) {
	return nil, nil
}

func (m *mockSocialService) DisbandGuild(ctx context.Context, guildID, leaderID uuid.UUID) error {
	return nil
}

func (m *mockSocialService) GetGuildMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) (*models.GuildMemberListResponse, error) {
	return nil, nil
}

func (m *mockSocialService) InviteMember(ctx context.Context, guildID, inviterID uuid.UUID, req *models.InviteMemberRequest) (*models.GuildInvitation, error) {
	return nil, nil
}

func (m *mockSocialService) UpdateMemberRank(ctx context.Context, guildID, leaderID, characterID uuid.UUID, rank models.GuildRank) error {
	return nil
}

func (m *mockSocialService) KickMember(ctx context.Context, guildID, leaderID, characterID uuid.UUID) error {
	return nil
}

func (m *mockSocialService) RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	return nil
}

func (m *mockSocialService) GetGuildBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error) {
	return nil, nil
}

func (m *mockSocialService) GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error) {
	return nil, nil
}

func (m *mockSocialService) AcceptInvitation(ctx context.Context, invitationID, characterID uuid.UUID) error {
	return nil
}

func (m *mockSocialService) RejectInvitation(ctx context.Context, invitationID uuid.UUID) error {
	return nil
}

func TestHTTPServer_CreateMessage(t *testing.T) {
	channelID := uuid.New()
	senderID := uuid.New()
	mockService := &mockSocialService{
		messages: make(map[uuid.UUID][]models.ChatMessage),
		channels: make(map[uuid.UUID]*models.ChatChannel),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateMessageRequest{
		ChannelID:   channelID,
		ChannelType: models.ChannelTypeGlobal,
		Content:     "Hello, world!",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/chat/messages", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", senderID.String())
	ctx = context.WithValue(ctx, "username", "testuser")
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.ChatMessage
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Content != "Hello, world!" {
		t.Errorf("Expected content 'Hello, world!', got %s", response.Content)
	}
}

func TestHTTPServer_GetMessages(t *testing.T) {
	channelID := uuid.New()
	messages := []models.ChatMessage{
		{
			ID:          uuid.New(),
			ChannelID:   channelID,
			ChannelType: models.ChannelTypeGlobal,
			SenderID:    uuid.New(),
			SenderName:  "User1",
			Content:     "Message 1",
			CreatedAt:   time.Now(),
		},
		{
			ID:          uuid.New(),
			ChannelID:   channelID,
			ChannelType: models.ChannelTypeGlobal,
			SenderID:    uuid.New(),
			SenderName:  "User2",
			Content:     "Message 2",
			CreatedAt:   time.Now(),
		},
	}

	mockService := &mockSocialService{
		messages: map[uuid.UUID][]models.ChatMessage{
			channelID: messages,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/messages/"+channelID.String()+"?limit=10&offset=0", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.MessageListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetChannels(t *testing.T) {
	channels := []models.ChatChannel{
		{
			ID:              uuid.New(),
			Type:            models.ChannelTypeGlobal,
			Name:            "Global",
			CooldownSeconds: 2,
			MaxLength:       200,
			IsActive:        true,
			CreatedAt:       time.Now(),
		},
		{
			ID:              uuid.New(),
			Type:            models.ChannelTypeTrade,
			Name:            "Trade",
			CooldownSeconds: 5,
			MaxLength:       150,
			IsActive:        true,
			CreatedAt:       time.Now(),
		},
	}

	mockService := &mockSocialService{
		channelList: channels,
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/channels", nil)
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

func TestHTTPServer_GetChannelsByType(t *testing.T) {
	channels := []models.ChatChannel{
		{
			ID:              uuid.New(),
			Type:            models.ChannelTypeGlobal,
			Name:            "Global",
			CooldownSeconds: 2,
			MaxLength:       200,
			IsActive:        true,
			CreatedAt:       time.Now(),
		},
		{
			ID:              uuid.New(),
			Type:            models.ChannelTypeTrade,
			Name:            "Trade",
			CooldownSeconds: 5,
			MaxLength:       150,
			IsActive:        true,
			CreatedAt:       time.Now(),
		},
	}

	mockService := &mockSocialService{
		channelList: channels,
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/channels?type=global", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ChannelListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}

	if response.Channels[0].Type != models.ChannelTypeGlobal {
		t.Errorf("Expected type 'global', got %s", response.Channels[0].Type)
	}
}

func TestHTTPServer_GetChannel(t *testing.T) {
	channelID := uuid.New()
	channel := &models.ChatChannel{
		ID:              channelID,
		Type:            models.ChannelTypeGlobal,
		Name:            "Global",
		Description:     "Global chat channel",
		CooldownSeconds: 2,
		MaxLength:       200,
		IsActive:        true,
		CreatedAt:       time.Now(),
	}

	mockService := &mockSocialService{
		channels: map[uuid.UUID]*models.ChatChannel{
			channelID: channel,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/channels/"+channelID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ChatChannel
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != channelID {
		t.Errorf("Expected ID %s, got %s", channelID, response.ID)
	}
}

func TestHTTPServer_GetChannelNotFound(t *testing.T) {
	mockService := &mockSocialService{
		channels: make(map[uuid.UUID]*models.ChatChannel),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/channels/"+uuid.New().String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

