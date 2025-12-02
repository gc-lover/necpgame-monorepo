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

func TestHTTPServer_CreateNotification(t *testing.T) {
	accountID := uuid.New()
	mockService := &mockSocialService{
		notifications: make(map[uuid.UUID]*models.Notification),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateNotificationRequest{
		AccountID: accountID,
		Type:      models.NotificationTypeQuest,
		Priority:  models.NotificationPriorityHigh,
		Title:     "Test Notification",
		Content:   "Test content",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/notifications", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.Notification
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Title != "Test Notification" {
		t.Errorf("Expected title 'Test Notification', got %s", response.Title)
	}
}

func TestHTTPServer_CreateNotificationInvalidRequest(t *testing.T) {
	mockService := &mockSocialService{}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateNotificationRequest{
		Title: "Test Notification",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/notifications", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}
}

func TestHTTPServer_GetNotifications(t *testing.T) {
	accountID := uuid.New()
	notifications := []models.Notification{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Type:      models.NotificationTypeQuest,
			Title:     "Notification 1",
			Status:    models.NotificationStatusUnread,
			CreatedAt: time.Now(),
		},
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Type:      models.NotificationTypeMessage,
			Title:     "Notification 2",
			Status:    models.NotificationStatusRead,
			CreatedAt: time.Now(),
		},
	}

	mockService := &mockSocialService{
		notificationList: map[uuid.UUID][]models.Notification{
			accountID: notifications,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/notifications?account_id="+accountID.String()+"&limit=10&offset=0", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.NotificationListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}

	if response.Unread != 1 {
		t.Errorf("Expected unread 1, got %d", response.Unread)
	}
}

func TestHTTPServer_GetNotification(t *testing.T) {
	notificationID := uuid.New()
	notification := &models.Notification{
		ID:        notificationID,
		Type:      models.NotificationTypeQuest,
		Title:     "Test Notification",
		Status:    models.NotificationStatusUnread,
		CreatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		notifications: map[uuid.UUID]*models.Notification{
			notificationID: notification,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/notifications/"+notificationID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Notification
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != notificationID {
		t.Errorf("Expected ID %s, got %s", notificationID, response.ID)
	}
}

func TestHTTPServer_GetNotificationNotFound(t *testing.T) {
	mockService := &mockSocialService{
		notifications: make(map[uuid.UUID]*models.Notification),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/notifications/"+uuid.New().String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_UpdateNotificationStatus(t *testing.T) {
	notificationID := uuid.New()
	notification := &models.Notification{
		ID:        notificationID,
		Type:      models.NotificationTypeQuest,
		Title:     "Test Notification",
		Status:    models.NotificationStatusUnread,
		CreatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		notifications: map[uuid.UUID]*models.Notification{
			notificationID: notification,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.UpdateNotificationStatusRequest{
		Status: models.NotificationStatusRead,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/social/notifications/"+notificationID.String()+"/status", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Notification
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Status != models.NotificationStatusRead {
		t.Errorf("Expected status 'read', got %s", response.Status)
	}
}

func TestHTTPServer_SendMail(t *testing.T) {
	recipientID := uuid.New()
	senderID := uuid.New()
	mockService := &mockSocialService{
		mails: make(map[uuid.UUID]*models.MailMessage),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateMailRequest{
		RecipientID: recipientID,
		Subject:     "Test Mail",
		Content:     "Test content",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/mail", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", senderID.String())
	ctx = context.WithValue(ctx, "username", "testuser")
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.MailMessage
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Subject != "Test Mail" {
		t.Errorf("Expected subject 'Test Mail', got %s", response.Subject)
	}
}

func TestHTTPServer_SendMailInvalidRequest(t *testing.T) {
	mockService := &mockSocialService{}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateMailRequest{
		RecipientID: uuid.New(),
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/mail", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}
}

func TestHTTPServer_GetMails(t *testing.T) {
	recipientID := uuid.New()
	mails := []models.MailMessage{
		{
			ID:          uuid.New(),
			RecipientID: recipientID,
			Subject:     "Mail 1",
			IsRead:      false,
			SentAt:      time.Now(),
		},
		{
			ID:          uuid.New(),
			RecipientID: recipientID,
			Subject:     "Mail 2",
			IsRead:      true,
			SentAt:      time.Now(),
		},
	}

	mockService := &mockSocialService{
		mailList: map[uuid.UUID][]models.MailMessage{
			recipientID: mails,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/mail?recipient_id="+recipientID.String()+"&limit=10&offset=0", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.MailListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}

	if response.Unread != 1 {
		t.Errorf("Expected unread 1, got %d", response.Unread)
	}
}

func TestHTTPServer_GetMail(t *testing.T) {
	mailID := uuid.New()
	mail := &models.MailMessage{
		ID:          mailID,
		Subject:     "Test Mail",
		Status:     models.MailStatusUnread,
		SentAt:     time.Now(),
		CreatedAt:  time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService := &mockSocialService{
		mails: map[uuid.UUID]*models.MailMessage{
			mailID: mail,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/mail/"+mailID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.MailMessage
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != mailID {
		t.Errorf("Expected ID %s, got %s", mailID, response.ID)
	}
}

func TestHTTPServer_GetMailNotFound(t *testing.T) {
	mockService := &mockSocialService{
		mails: make(map[uuid.UUID]*models.MailMessage),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/mail/"+uuid.New().String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_MarkMailAsRead(t *testing.T) {
	mailID := uuid.New()
	mail := &models.MailMessage{
		ID:          mailID,
		Subject:     "Test Mail",
		IsRead:      false,
		Status:     models.MailStatusUnread,
		SentAt:     time.Now(),
		CreatedAt:  time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService := &mockSocialService{
		mails: map[uuid.UUID]*models.MailMessage{
			mailID: mail,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("PUT", "/api/v1/social/mail/"+mailID.String()+"/read", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_ClaimAttachment(t *testing.T) {
	mailID := uuid.New()
	mail := &models.MailMessage{
		ID:          mailID,
		Subject:     "Test Mail",
		IsClaimed:   false,
		Attachments: map[string]interface{}{"item_id": "123"},
		Status:     models.MailStatusRead,
		SentAt:     time.Now(),
		CreatedAt:  time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService := &mockSocialService{
		mails: map[uuid.UUID]*models.MailMessage{
			mailID: mail,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/social/mail/"+mailID.String()+"/claim", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ClaimAttachmentResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if !response.Success {
		t.Errorf("Expected success true, got false")
	}
}

func TestHTTPServer_DeleteMail(t *testing.T) {
	mailID := uuid.New()
	mail := &models.MailMessage{
		ID:          mailID,
		Subject:     "Test Mail",
		SentAt:     time.Now(),
		CreatedAt:  time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService := &mockSocialService{
		mails: map[uuid.UUID]*models.MailMessage{
			mailID: mail,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("DELETE", "/api/v1/social/mail/"+mailID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_CreateGuild(t *testing.T) {
	leaderID := uuid.New()
	mockService := &mockSocialService{
		guilds: make(map[uuid.UUID]*models.Guild),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateGuildRequest{
		Name:        "Test Guild",
		Tag:         "TEST",
		Description: "Test description",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/guilds", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", leaderID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.Guild
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Name != "Test Guild" {
		t.Errorf("Expected name 'Test Guild', got %s", response.Name)
	}
}

func TestHTTPServer_CreateGuildInvalidRequest(t *testing.T) {
	leaderID := uuid.New()
	mockService := &mockSocialService{}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateGuildRequest{
		Name: "Test Guild",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/guilds", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", leaderID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}
}

func TestHTTPServer_ListGuilds(t *testing.T) {
	guilds := []models.Guild{
		{
			ID:        uuid.New(),
			Name:      "Guild 1",
			Tag:       "G1",
			Status:    models.GuildStatusActive,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
		{
			ID:        uuid.New(),
			Name:      "Guild 2",
			Tag:       "G2",
			Status:    models.GuildStatusActive,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockService := &mockSocialService{
		guildList: guilds,
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds?limit=10&offset=0", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.GuildListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetGuild(t *testing.T) {
	guildID := uuid.New()
	guild := &models.Guild{
		ID:        guildID,
		Name:      "Test Guild",
		Tag:       "TEST",
		Status:    models.GuildStatusActive,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guilds: map[uuid.UUID]*models.Guild{
			guildID: guild,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds/"+guildID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.GuildDetailResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Guild.ID != guildID {
		t.Errorf("Expected ID %s, got %s", guildID, response.Guild.ID)
	}
}

func TestHTTPServer_GetGuildNotFound(t *testing.T) {
	mockService := &mockSocialService{
		guilds: make(map[uuid.UUID]*models.Guild),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds/"+uuid.New().String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_UpdateGuild(t *testing.T) {
	guildID := uuid.New()
	leaderID := uuid.New()
	guild := &models.Guild{
		ID:        guildID,
		Name:      "Test Guild",
		Tag:       "TEST",
		LeaderID:  leaderID,
		Status:    models.GuildStatusActive,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guilds: map[uuid.UUID]*models.Guild{
			guildID: guild,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	newName := "Updated Guild"
	reqBody := models.UpdateGuildRequest{
		Name: &newName,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/social/guilds/"+guildID.String(), bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", leaderID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Guild
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Name != "Updated Guild" {
		t.Errorf("Expected name 'Updated Guild', got %s", response.Name)
	}
}

func TestHTTPServer_DisbandGuild(t *testing.T) {
	guildID := uuid.New()
	leaderID := uuid.New()
	guild := &models.Guild{
		ID:        guildID,
		Name:      "Test Guild",
		Tag:       "TEST",
		LeaderID:  leaderID,
		Status:    models.GuildStatusActive,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guilds: map[uuid.UUID]*models.Guild{
			guildID: guild,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("DELETE", "/api/v1/social/guilds/"+guildID.String()+"/disband", nil)
	ctx := context.WithValue(req.Context(), "user_id", leaderID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetGuildMembers(t *testing.T) {
	guildID := uuid.New()
	members := []models.GuildMember{
		{
			ID:          uuid.New(),
			GuildID:     guildID,
			CharacterID: uuid.New(),
			Rank:        models.GuildRankLeader,
			Status:      models.GuildMemberStatusActive,
			JoinedAt:    time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockService := &mockSocialService{
		guildMembers: map[uuid.UUID]*models.GuildMemberListResponse{
			guildID: {
				Members: members,
				Total:   1,
			},
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds/"+guildID.String()+"/members?limit=10&offset=0", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.GuildMemberListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_InviteMember(t *testing.T) {
	guildID := uuid.New()
	inviterID := uuid.New()
	characterID := uuid.New()
	guild := &models.Guild{
		ID:        guildID,
		Name:      "Test Guild",
		Tag:       "TEST",
		LeaderID:  inviterID,
		Status:    models.GuildStatusActive,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guilds: map[uuid.UUID]*models.Guild{
			guildID: guild,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.InviteMemberRequest{
		CharacterID: characterID,
		Message:     "Join our guild!",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/guilds/"+guildID.String()+"/members/invite", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", inviterID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d", http.StatusCreated, w.Code)
	}

	var response models.GuildInvitation
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.CharacterID != characterID {
		t.Errorf("Expected character ID %s, got %s", characterID, response.CharacterID)
	}
}

func TestHTTPServer_UpdateMemberRank(t *testing.T) {
	guildID := uuid.New()
	leaderID := uuid.New()
	characterID := uuid.New()
	guild := &models.Guild{
		ID:        guildID,
		Name:      "Test Guild",
		Tag:       "TEST",
		LeaderID:  leaderID,
		Status:    models.GuildStatusActive,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guilds: map[uuid.UUID]*models.Guild{
			guildID: guild,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.UpdateMemberRankRequest{
		Rank: models.GuildRankOfficer,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/social/guilds/"+guildID.String()+"/members/"+characterID.String()+"/rank", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", leaderID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_KickMember(t *testing.T) {
	guildID := uuid.New()
	leaderID := uuid.New()
	characterID := uuid.New()
	guild := &models.Guild{
		ID:        guildID,
		Name:      "Test Guild",
		Tag:       "TEST",
		LeaderID:  leaderID,
		Status:    models.GuildStatusActive,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guilds: map[uuid.UUID]*models.Guild{
			guildID: guild,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("DELETE", "/api/v1/social/guilds/"+guildID.String()+"/members/"+characterID.String()+"/kick", nil)
	ctx := context.WithValue(req.Context(), "user_id", leaderID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetGuildBank(t *testing.T) {
	guildID := uuid.New()
	bank := &models.GuildBank{
		ID:        uuid.New(),
		GuildID:   guildID,
		Currency:  map[string]int{"gold": 1000},
		Items:     []map[string]interface{}{},
		UpdatedAt: time.Now(),
	}

	mockService := &mockSocialService{
		guildBanks: map[uuid.UUID]*models.GuildBank{
			guildID: bank,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds/"+guildID.String()+"/bank", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.GuildBank
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.GuildID != guildID {
		t.Errorf("Expected guild ID %s, got %s", guildID, response.GuildID)
	}
}

func TestHTTPServer_GetGuildBankNotFound(t *testing.T) {
	mockService := &mockSocialService{
		guildBanks: make(map[uuid.UUID]*models.GuildBank),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds/"+uuid.New().String()+"/bank", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_GetInvitations(t *testing.T) {
	characterID := uuid.New()
	invitations := []models.GuildInvitation{
		{
			ID:          uuid.New(),
			GuildID:     uuid.New(),
			CharacterID: characterID,
			Status:      "pending",
			CreatedAt:   time.Now(),
			ExpiresAt:   time.Now().Add(7 * 24 * time.Hour),
		},
	}

	mockService := &mockSocialService{
		invitations: map[uuid.UUID][]models.GuildInvitation{
			characterID: invitations,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/guilds/invitations", nil)
	ctx := context.WithValue(req.Context(), "user_id", characterID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response []models.GuildInvitation
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if len(response) != 1 {
		t.Errorf("Expected 1 invitation, got %d", len(response))
	}
}

func TestHTTPServer_AcceptInvitation(t *testing.T) {
	invitationID := uuid.New()
	characterID := uuid.New()
	mockService := &mockSocialService{}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/social/guilds/invitations/"+invitationID.String()+"/accept", nil)
	ctx := context.WithValue(req.Context(), "user_id", characterID.String())
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_RejectInvitation(t *testing.T) {
	invitationID := uuid.New()
	mockService := &mockSocialService{}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/social/guilds/invitations/"+invitationID.String()+"/reject", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

