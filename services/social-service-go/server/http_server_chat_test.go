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
	messages         map[uuid.UUID][]models.ChatMessage
	channels         map[uuid.UUID]*models.ChatChannel
	channelList      []models.ChatChannel
	notifications    map[uuid.UUID]*models.Notification
	notificationList map[uuid.UUID][]models.Notification
	mails            map[uuid.UUID]*models.MailMessage
	mailList         map[uuid.UUID][]models.MailMessage
	guilds           map[uuid.UUID]*models.Guild
	guildList        []models.Guild
	guildMembers     map[uuid.UUID]*models.GuildMemberListResponse
	guildBanks       map[uuid.UUID]*models.GuildBank
	invitations      map[uuid.UUID][]models.GuildInvitation
	bans             map[uuid.UUID]models.ChatBan
	reports          map[uuid.UUID]models.ChatReport
	createErr        error
	getErr           error
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
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.notifications == nil {
		m.notifications = make(map[uuid.UUID]*models.Notification)
	}
	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: req.AccountID,
		Type:      req.Type,
		Priority:  req.Priority,
		Title:     req.Title,
		Content:   req.Content,
		Data:      req.Data,
		Status:    models.NotificationStatusUnread,
		Channels:  req.Channels,
		CreatedAt: time.Now(),
		ExpiresAt: req.ExpiresAt,
	}
	m.notifications[notification.ID] = notification
	return notification, nil
}

func (m *mockSocialService) GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.notificationList == nil {
		m.notificationList = make(map[uuid.UUID][]models.Notification)
	}
	notifications := m.notificationList[accountID]
	if notifications == nil {
		notifications = []models.Notification{}
	}
	total := len(notifications)
	unread := 0
	for _, n := range notifications {
		if n.Status == models.NotificationStatusUnread {
			unread++
		}
	}
	if offset >= total {
		return &models.NotificationListResponse{
			Notifications: []models.Notification{},
			Total:         total,
			Unread:        unread,
		}, nil
	}
	end := offset + limit
	if end > total {
		end = total
	}
	return &models.NotificationListResponse{
		Notifications: notifications[offset:end],
		Total:         total,
		Unread:        unread,
	}, nil
}

func (m *mockSocialService) GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.notifications == nil {
		return nil, nil
	}
	return m.notifications[notificationID], nil
}

func (m *mockSocialService) UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.notifications == nil {
		return nil, nil
	}
	notification := m.notifications[notificationID]
	if notification == nil {
		return nil, nil
	}
	notification.Status = status
	if status == models.NotificationStatusRead {
		now := time.Now()
		notification.ReadAt = &now
	}
	return notification, nil
}

func (m *mockSocialService) SendMail(ctx context.Context, req *models.CreateMailRequest, senderID *uuid.UUID, senderName string) (*models.MailMessage, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.mails == nil {
		m.mails = make(map[uuid.UUID]*models.MailMessage)
	}
	mail := &models.MailMessage{
		ID:          uuid.New(),
		SenderID:    senderID,
		SenderName:  senderName,
		RecipientID: req.RecipientID,
		Type:        models.MailTypePlayer,
		Subject:     req.Subject,
		Content:     req.Content,
		Attachments: req.Attachments,
		CODAmount:   req.CODAmount,
		Status:      models.MailStatusUnread,
		IsRead:      false,
		IsClaimed:   false,
		SentAt:      time.Now(),
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	if req.ExpiresIn != nil {
		expiresAt := time.Now().Add(time.Duration(*req.ExpiresIn) * time.Hour * 24)
		mail.ExpiresAt = &expiresAt
	}
	m.mails[mail.ID] = mail
	return mail, nil
}

func (m *mockSocialService) GetMails(ctx context.Context, recipientID uuid.UUID, limit, offset int) (*models.MailListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.mailList == nil {
		m.mailList = make(map[uuid.UUID][]models.MailMessage)
	}
	mails := m.mailList[recipientID]
	if mails == nil {
		mails = []models.MailMessage{}
	}
	total := len(mails)
	unread := 0
	for _, mail := range mails {
		if !mail.IsRead {
			unread++
		}
	}
	if offset >= total {
		return &models.MailListResponse{
			Messages: []models.MailMessage{},
			Total:    total,
			Unread:   unread,
		}, nil
	}
	end := offset + limit
	if end > total {
		end = total
	}
	return &models.MailListResponse{
		Messages: mails[offset:end],
		Total:    total,
		Unread:   unread,
	}, nil
}

func (m *mockSocialService) GetMail(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.mails == nil {
		return nil, nil
	}
	return m.mails[mailID], nil
}

func (m *mockSocialService) MarkMailAsRead(ctx context.Context, mailID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
	if m.mails == nil {
		return nil
	}
	mail := m.mails[mailID]
	if mail != nil {
		mail.IsRead = true
		now := time.Now()
		mail.ReadAt = &now
		mail.Status = models.MailStatusRead
	}
	return nil
}

func (m *mockSocialService) ClaimAttachment(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.mails == nil {
		return nil, nil
	}
	mail := m.mails[mailID]
	if mail == nil || mail.IsClaimed {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}
	mail.IsClaimed = true
	mail.Status = models.MailStatusClaimed
	return &models.ClaimAttachmentResponse{
		Success:  true,
		Items:    mail.Attachments,
		Currency: make(map[string]int),
	}, nil
}

func (m *mockSocialService) DeleteMail(ctx context.Context, mailID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
	if m.mails == nil {
		return nil
	}
	delete(m.mails, mailID)
	return nil
}

func (m *mockSocialService) CreateGuild(ctx context.Context, leaderID uuid.UUID, req *models.CreateGuildRequest) (*models.Guild, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.guilds == nil {
		m.guilds = make(map[uuid.UUID]*models.Guild)
	}
	guild := &models.Guild{
		ID:          uuid.New(),
		Name:        req.Name,
		Tag:         req.Tag,
		LeaderID:    leaderID,
		Level:       1,
		Experience:  0,
		MaxMembers:  50,
		Description: req.Description,
		Status:      models.GuildStatusActive,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	m.guilds[guild.ID] = guild
	return guild, nil
}

func (m *mockSocialService) ListGuilds(ctx context.Context, limit, offset int) (*models.GuildListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.guildList == nil {
		m.guildList = []models.Guild{}
	}
	total := len(m.guildList)
	if offset >= total {
		return &models.GuildListResponse{
			Guilds: []models.Guild{},
			Total:  total,
		}, nil
	}
	end := offset + limit
	if end > total {
		end = total
	}
	return &models.GuildListResponse{
		Guilds: m.guildList[offset:end],
		Total:  total,
	}, nil
}

func (m *mockSocialService) GetGuild(ctx context.Context, guildID uuid.UUID) (*models.GuildDetailResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.guilds == nil {
		return nil, nil
	}
	guild := m.guilds[guildID]
	if guild == nil {
		return nil, nil
	}
	return &models.GuildDetailResponse{
		Guild: *guild,
	}, nil
}

func (m *mockSocialService) UpdateGuild(ctx context.Context, guildID, leaderID uuid.UUID, req *models.UpdateGuildRequest) (*models.Guild, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.guilds == nil {
		return nil, nil
	}
	guild := m.guilds[guildID]
	if guild == nil || guild.LeaderID != leaderID {
		return nil, nil
	}
	if req.Name != nil {
		guild.Name = *req.Name
	}
	if req.Description != nil {
		guild.Description = *req.Description
	}
	guild.UpdatedAt = time.Now()
	return guild, nil
}

func (m *mockSocialService) DisbandGuild(ctx context.Context, guildID, leaderID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
	if m.guilds == nil {
		return nil
	}
	guild := m.guilds[guildID]
	if guild == nil || guild.LeaderID != leaderID {
		return nil
	}
	guild.Status = models.GuildStatusDisbanded
	return nil
}

func (m *mockSocialService) GetGuildMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) (*models.GuildMemberListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.guildMembers == nil {
		m.guildMembers = make(map[uuid.UUID]*models.GuildMemberListResponse)
	}
	response := m.guildMembers[guildID]
	if response == nil {
		response = &models.GuildMemberListResponse{
			Members: []models.GuildMember{},
			Total:   0,
		}
	}
	return response, nil
}

func (m *mockSocialService) InviteMember(ctx context.Context, guildID, inviterID uuid.UUID, req *models.InviteMemberRequest) (*models.GuildInvitation, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.guilds == nil {
		return nil, nil
	}
	guild := m.guilds[guildID]
	if guild == nil {
		return nil, nil
	}
	invitation := &models.GuildInvitation{
		ID:          uuid.New(),
		GuildID:     guildID,
		CharacterID: req.CharacterID,
		InvitedBy:   inviterID,
		Message:     req.Message,
		Status:      "pending",
		CreatedAt:   time.Now(),
		ExpiresAt:   time.Now().Add(7 * 24 * time.Hour),
	}
	return invitation, nil
}

func (m *mockSocialService) UpdateMemberRank(ctx context.Context, guildID, leaderID, characterID uuid.UUID, rank models.GuildRank) error {
	if m.getErr != nil {
		return m.getErr
	}
	if m.guilds == nil {
		return nil
	}
	guild := m.guilds[guildID]
	if guild == nil || guild.LeaderID != leaderID {
		return nil
	}
	return nil
}

func (m *mockSocialService) KickMember(ctx context.Context, guildID, leaderID, characterID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
	if m.guilds == nil {
		return nil
	}
	guild := m.guilds[guildID]
	if guild == nil || guild.LeaderID != leaderID {
		return nil
	}
	return nil
}

func (m *mockSocialService) RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
	return nil
}

func (m *mockSocialService) GetGuildBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.guildBanks == nil {
		m.guildBanks = make(map[uuid.UUID]*models.GuildBank)
	}
	bank := m.guildBanks[guildID]
	if bank == nil {
		return nil, nil
	}
	return bank, nil
}

func (m *mockSocialService) GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.invitations == nil {
		m.invitations = make(map[uuid.UUID][]models.GuildInvitation)
	}
	return m.invitations[characterID], nil
}

func (m *mockSocialService) AcceptInvitation(ctx context.Context, invitationID, characterID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
	return nil
}

func (m *mockSocialService) RejectInvitation(ctx context.Context, invitationID uuid.UUID) error {
	if m.getErr != nil {
		return m.getErr
	}
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

func (m *mockSocialService) CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.bans == nil {
		m.bans = make(map[uuid.UUID]models.ChatBan)
	}
	ban := models.ChatBan{
		ID:          uuid.New(),
		CharacterID: req.CharacterID,
		ChannelID:   req.ChannelID,
		ChannelType: req.ChannelType,
		Reason:      req.Reason,
		AdminID:     &adminID,
		CreatedAt:   time.Now(),
		IsActive:    true,
	}
	if req.Duration != nil && *req.Duration > 0 {
		expiresAt := time.Now().Add(time.Duration(*req.Duration) * time.Hour)
		ban.ExpiresAt = &expiresAt
	}
	m.bans[ban.ID] = ban
	return &ban, nil
}

func (m *mockSocialService) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	if m.bans == nil {
		m.bans = make(map[uuid.UUID]models.ChatBan)
	}
	bans := []models.ChatBan{}
	for _, ban := range m.bans {
		if characterID == nil || ban.CharacterID == *characterID {
			bans = append(bans, ban)
		}
	}
	total := len(bans)
	if offset >= total {
		return &models.BanListResponse{Bans: []models.ChatBan{}, Total: total}, nil
	}
	end := offset + limit
	if end > total {
		end = total
	}
	return &models.BanListResponse{Bans: bans[offset:end], Total: total}, nil
}

func (m *mockSocialService) RemoveBan(ctx context.Context, banID uuid.UUID) error {
	if m.createErr != nil {
		return m.createErr
	}
	if m.bans == nil {
		m.bans = make(map[uuid.UUID]models.ChatBan)
	}
	if ban, exists := m.bans[banID]; exists {
		ban.IsActive = false
		m.bans[banID] = ban
	}
	return nil
}

func (m *mockSocialService) CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	if m.reports == nil {
		m.reports = make(map[uuid.UUID]models.ChatReport)
	}
	report := models.ChatReport{
		ID:         uuid.New(),
		ReporterID: reporterID,
		ReportedID: req.ReportedID,
		MessageID:  req.MessageID,
		ChannelID:  req.ChannelID,
		Reason:     req.Reason,
		Status:     "pending",
		CreatedAt:  time.Now(),
	}
	m.reports[report.ID] = report
	return &report, nil
}

func (m *mockSocialService) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	if m.getErr != nil {
		return nil, 0, m.getErr
	}
	if m.reports == nil {
		m.reports = make(map[uuid.UUID]models.ChatReport)
	}
	reports := []models.ChatReport{}
	for _, report := range m.reports {
		if status == nil || report.Status == *status {
			reports = append(reports, report)
		}
	}
	total := len(reports)
	if offset >= total {
		return []models.ChatReport{}, total, nil
	}
	end := offset + limit
	if end > total {
		end = total
	}
	return reports[offset:end], total, nil
}

func (m *mockSocialService) ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error {
	if m.createErr != nil {
		return m.createErr
	}
	if m.reports == nil {
		m.reports = make(map[uuid.UUID]models.ChatReport)
	}
	if report, exists := m.reports[reportID]; exists {
		report.Status = status
		report.AdminID = &adminID
		now := time.Now()
		report.ResolvedAt = &now
		m.reports[reportID] = report
	}
	return nil
}

func (m *mockSocialService) GetNotificationPreferences(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return &models.NotificationPreferences{
		AccountID:          accountID,
		QuestEnabled:       true,
		MessageEnabled:     true,
		AchievementEnabled: true,
		SystemEnabled:      true,
		FriendEnabled:      true,
		GuildEnabled:       true,
		TradeEnabled:       true,
		CombatEnabled:      true,
		PreferredChannels:  []models.DeliveryChannel{models.DeliveryChannelInGame, models.DeliveryChannelWebSocket},
		UpdatedAt:          time.Now(),
	}, nil
}

func (m *mockSocialService) UpdateNotificationPreferences(ctx context.Context, prefs *models.NotificationPreferences) error {
	if m.getErr != nil {
		return m.getErr
	}
	return nil
}

