//go:build ignore
// +build ignore

package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/necpgame/social-service-go/pkg/api/chat"
	"github.com/necpgame/social-service-go/pkg/api/guilds"
	"github.com/necpgame/social-service-go/pkg/api/mail"
	"github.com/necpgame/social-service-go/pkg/api/notifications"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type MockGuildsService struct{}

func (m *MockGuildsService) SearchGuilds(ctx context.Context, params guilds.SearchGuildsParams) (*guilds.GuildListResponse, error) {
	return &guilds.GuildListResponse{Guilds: &[]guilds.Guild{}, Total: new(int)}, nil
}
func (m *MockGuildsService) CreateGuild(ctx context.Context, req *guilds.CreateGuildRequest) (*guilds.Guild, error) {
	return &guilds.Guild{}, nil
}
func (m *MockGuildsService) GetGuild(ctx context.Context, guildID guilds.GuildId) (*guilds.Guild, error) {
	return &guilds.Guild{}, nil
}
func (m *MockGuildsService) UpdateGuild(ctx context.Context, guildID guilds.GuildId, req *guilds.UpdateGuildRequest) (*guilds.Guild, error) {
	return &guilds.Guild{}, nil
}

type MockChatService struct{}

func (m *MockChatService) GetMessages(ctx context.Context, params chat.GetMessagesParams) (*chat.MessageListResponse, error) {
	return &chat.MessageListResponse{Items: []chat.ChatMessage{}}, nil
}
func (m *MockChatService) ProcessChatMessage(ctx context.Context, req *chat.ProcessMessageRequest) (*chat.ProcessedMessageResponse, error) {
	return &chat.ProcessedMessageResponse{}, nil
}
func (m *MockChatService) SendChatMessage(ctx context.Context, req *chat.SendMessageRequest) (*chat.ChatMessage, error) {
	return &chat.ChatMessage{}, nil
}
func (m *MockChatService) GetChannelMessages(ctx context.Context, channelID openapi_types.UUID, params chat.GetChannelMessagesParams) (*chat.MessageListResponse, error) {
	return &chat.MessageListResponse{Items: []chat.ChatMessage{}}, nil
}

type MockMailService struct{}

func (m *MockMailService) GetInbox(ctx context.Context, params mail.GetInboxParams) (*mail.MailListResponse, error) {
	return &mail.MailListResponse{Items: []mail.MailMessage{}}, nil
}
func (m *MockMailService) SendMail(ctx context.Context, req *mail.CreateMailRequest) (*mail.MailMessage, error) {
	return &mail.MailMessage{}, nil
}
func (m *MockMailService) GetUnreadMailCount(ctx context.Context) (*mail.UnreadMailCountResponse, error) {
	count := 0
	return &mail.UnreadMailCountResponse{UnreadCount: &count}, nil
}
func (m *MockMailService) GetMail(ctx context.Context, mailID mail.MailId) (*mail.MailMessage, error) {
	return &mail.MailMessage{}, nil
}
func (m *MockMailService) MarkMailAsRead(ctx context.Context, mailID mail.MailId) error {
	return nil
}

type MockNotificationsService struct{}

func (m *MockNotificationsService) GetNotifications(ctx context.Context, params notifications.GetNotificationsParams) (*notifications.NotificationListResponse, error) {
	return &notifications.NotificationListResponse{Notifications: &[]notifications.Notification{}, Total: new(int)}, nil
}
func (m *MockNotificationsService) CreateNotification(ctx context.Context, req *notifications.CreateNotificationRequest) (*notifications.Notification, error) {
	return &notifications.Notification{}, nil
}
func (m *MockNotificationsService) GetNotification(ctx context.Context, notificationID openapi_types.UUID) (*notifications.Notification, error) {
	return &notifications.Notification{}, nil
}

type MockPartyService struct{}

func (m *MockPartyService) CreateParty(ctx context.Context, leaderID uuid.UUID, req *models.CreatePartyRequest) (*models.Party, error) {
	return &models.Party{}, nil
}

func (m *MockPartyService) GetParty(ctx context.Context, partyID uuid.UUID) (*models.Party, error) {
	return &models.Party{}, nil
}

func (m *MockPartyService) GetPartyByPlayerID(ctx context.Context, accountID uuid.UUID) (*models.Party, error) {
	return &models.Party{}, nil
}

func (m *MockPartyService) GetPartyLeader(ctx context.Context, partyID uuid.UUID) (*models.PartyMember, error) {
	return &models.PartyMember{}, nil
}

func (m *MockPartyService) TransferLeadership(ctx context.Context, partyID, newLeaderID uuid.UUID) (*models.Party, error) {
	return &models.Party{}, nil
}

