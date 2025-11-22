package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockNotificationRepository struct {
	mock.Mock
}

func (m *mockNotificationRepository) Create(ctx context.Context, notification *models.Notification) (*models.Notification, error) {
	args := m.Called(ctx, notification)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Notification), args.Error(1)
}

func (m *mockNotificationRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Notification, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Notification), args.Error(1)
}

func (m *mockNotificationRepository) GetByAccountID(ctx context.Context, accountID uuid.UUID, limit, offset int) ([]models.Notification, error) {
	args := m.Called(ctx, accountID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.Notification), args.Error(1)
}

func (m *mockNotificationRepository) CountByAccountID(ctx context.Context, accountID uuid.UUID) (int, error) {
	args := m.Called(ctx, accountID)
	return args.Int(0), args.Error(1)
}

func (m *mockNotificationRepository) CountUnreadByAccountID(ctx context.Context, accountID uuid.UUID) (int, error) {
	args := m.Called(ctx, accountID)
	return args.Int(0), args.Error(1)
}

func (m *mockNotificationRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	args := m.Called(ctx, id, status)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Notification), args.Error(1)
}

type mockChatRepository struct {
	mock.Mock
}

func (m *mockChatRepository) CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error) {
	args := m.Called(ctx, message)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatMessage), args.Error(1)
}

func (m *mockChatRepository) GetMessagesByChannel(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, error) {
	args := m.Called(ctx, channelID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.ChatMessage), args.Error(1)
}

func (m *mockChatRepository) GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error) {
	args := m.Called(ctx, channelType)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.ChatChannel), args.Error(1)
}

func (m *mockChatRepository) GetChannelByID(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error) {
	args := m.Called(ctx, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatChannel), args.Error(1)
}

func (m *mockChatRepository) CountMessagesByChannel(ctx context.Context, channelID uuid.UUID) (int, error) {
	args := m.Called(ctx, channelID)
	return args.Int(0), args.Error(1)
}

type mockMailRepository struct {
	mock.Mock
}

func (m *mockMailRepository) Create(ctx context.Context, mail *models.MailMessage) error {
	args := m.Called(ctx, mail)
	return args.Error(0)
}

func (m *mockMailRepository) GetByID(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error) {
	args := m.Called(ctx, mailID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.MailMessage), args.Error(1)
}

func (m *mockMailRepository) GetByRecipientID(ctx context.Context, recipientID uuid.UUID, limit, offset int) ([]models.MailMessage, error) {
	args := m.Called(ctx, recipientID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.MailMessage), args.Error(1)
}

func (m *mockMailRepository) UpdateStatus(ctx context.Context, mailID uuid.UUID, status models.MailStatus, readAt *time.Time) error {
	args := m.Called(ctx, mailID, status, readAt)
	return args.Error(0)
}

func (m *mockMailRepository) MarkAsClaimed(ctx context.Context, mailID uuid.UUID) error {
	args := m.Called(ctx, mailID)
	return args.Error(0)
}

func (m *mockMailRepository) Delete(ctx context.Context, mailID uuid.UUID) error {
	args := m.Called(ctx, mailID)
	return args.Error(0)
}

func (m *mockMailRepository) CountByRecipientID(ctx context.Context, recipientID uuid.UUID) (int, error) {
	args := m.Called(ctx, recipientID)
	return args.Int(0), args.Error(1)
}

func (m *mockMailRepository) CountUnreadByRecipientID(ctx context.Context, recipientID uuid.UUID) (int, error) {
	args := m.Called(ctx, recipientID)
	return args.Int(0), args.Error(1)
}

type mockGuildRepository struct {
	mock.Mock
}

func (m *mockGuildRepository) Create(ctx context.Context, guild *models.Guild) error {
	args := m.Called(ctx, guild)
	return args.Error(0)
}

func (m *mockGuildRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Guild, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Guild), args.Error(1)
}

func (m *mockGuildRepository) GetByName(ctx context.Context, name string) (*models.Guild, error) {
	args := m.Called(ctx, name)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Guild), args.Error(1)
}

func (m *mockGuildRepository) GetByTag(ctx context.Context, tag string) (*models.Guild, error) {
	args := m.Called(ctx, tag)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Guild), args.Error(1)
}

func (m *mockGuildRepository) List(ctx context.Context, limit, offset int) ([]models.Guild, error) {
	args := m.Called(ctx, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.Guild), args.Error(1)
}

func (m *mockGuildRepository) Count(ctx context.Context) (int, error) {
	args := m.Called(ctx)
	return args.Int(0), args.Error(1)
}

func (m *mockGuildRepository) Update(ctx context.Context, guild *models.Guild) error {
	args := m.Called(ctx, guild)
	return args.Error(0)
}

func (m *mockGuildRepository) UpdateLevel(ctx context.Context, guildID uuid.UUID, level, experience int) error {
	args := m.Called(ctx, guildID, level, experience)
	return args.Error(0)
}

func (m *mockGuildRepository) Disband(ctx context.Context, guildID uuid.UUID) error {
	args := m.Called(ctx, guildID)
	return args.Error(0)
}

func (m *mockGuildRepository) AddMember(ctx context.Context, member *models.GuildMember) error {
	args := m.Called(ctx, member)
	return args.Error(0)
}

func (m *mockGuildRepository) GetMember(ctx context.Context, guildID, characterID uuid.UUID) (*models.GuildMember, error) {
	args := m.Called(ctx, guildID, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.GuildMember), args.Error(1)
}

func (m *mockGuildRepository) GetMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]models.GuildMember, error) {
	args := m.Called(ctx, guildID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.GuildMember), args.Error(1)
}

func (m *mockGuildRepository) CountMembers(ctx context.Context, guildID uuid.UUID) (int, error) {
	args := m.Called(ctx, guildID)
	return args.Int(0), args.Error(1)
}

func (m *mockGuildRepository) UpdateMemberRank(ctx context.Context, guildID, characterID uuid.UUID, rank models.GuildRank) error {
	args := m.Called(ctx, guildID, characterID, rank)
	return args.Error(0)
}

func (m *mockGuildRepository) RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	args := m.Called(ctx, guildID, characterID)
	return args.Error(0)
}

func (m *mockGuildRepository) KickMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	args := m.Called(ctx, guildID, characterID)
	return args.Error(0)
}

func (m *mockGuildRepository) UpdateMemberContribution(ctx context.Context, guildID, characterID uuid.UUID, contribution int) error {
	args := m.Called(ctx, guildID, characterID, contribution)
	return args.Error(0)
}

func (m *mockGuildRepository) CreateInvitation(ctx context.Context, invitation *models.GuildInvitation) error {
	args := m.Called(ctx, invitation)
	return args.Error(0)
}

func (m *mockGuildRepository) GetInvitation(ctx context.Context, id uuid.UUID) (*models.GuildInvitation, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.GuildInvitation), args.Error(1)
}

func (m *mockGuildRepository) GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.GuildInvitation), args.Error(1)
}

func (m *mockGuildRepository) AcceptInvitation(ctx context.Context, invitationID uuid.UUID) error {
	args := m.Called(ctx, invitationID)
	return args.Error(0)
}

func (m *mockGuildRepository) RejectInvitation(ctx context.Context, invitationID uuid.UUID) error {
	args := m.Called(ctx, invitationID)
	return args.Error(0)
}

func (m *mockGuildRepository) GetBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error) {
	args := m.Called(ctx, guildID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.GuildBank), args.Error(1)
}

func (m *mockGuildRepository) CreateBank(ctx context.Context, bank *models.GuildBank) error {
	args := m.Called(ctx, bank)
	return args.Error(0)
}

func (m *mockGuildRepository) UpdateBank(ctx context.Context, bank *models.GuildBank) error {
	args := m.Called(ctx, bank)
	return args.Error(0)
}

type mockModerationService struct {
	mock.Mock
}

func (m *mockModerationService) CheckBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error) {
	args := m.Called(ctx, characterID, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatBan), args.Error(1)
}

func (m *mockModerationService) FilterMessage(ctx context.Context, content string) (string, bool, error) {
	args := m.Called(ctx, content)
	return args.String(0), args.Bool(1), args.Error(2)
}

func (m *mockModerationService) DetectSpam(ctx context.Context, characterID uuid.UUID, content string) (bool, error) {
	args := m.Called(ctx, characterID, content)
	return args.Bool(0), args.Error(1)
}

func (m *mockModerationService) AutoBanIfSpam(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error) {
	args := m.Called(ctx, characterID, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatBan), args.Error(1)
}

func (m *mockModerationService) AutoBanIfSevereViolation(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID, violationCount int) (*models.ChatBan, error) {
	args := m.Called(ctx, characterID, channelID, violationCount)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatBan), args.Error(1)
}

func (m *mockModerationService) CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error) {
	args := m.Called(ctx, adminID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatBan), args.Error(1)
}

func (m *mockModerationService) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error) {
	args := m.Called(ctx, characterID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.BanListResponse), args.Error(1)
}

func (m *mockModerationService) RemoveBan(ctx context.Context, banID uuid.UUID) error {
	args := m.Called(ctx, banID)
	return args.Error(0)
}

func (m *mockModerationService) CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error) {
	args := m.Called(ctx, reporterID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ChatReport), args.Error(1)
}

func (m *mockModerationService) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	args := m.Called(ctx, status, limit, offset)
	return args.Get(0).([]models.ChatReport), args.Int(1), args.Error(2)
}

func (m *mockModerationService) ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error {
	args := m.Called(ctx, reportID, adminID, status)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*SocialService, *mockNotificationRepository, *mockChatRepository, *mockMailRepository, *mockGuildRepository, *mockModerationService, func()) {
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return nil, nil, nil, nil, nil, nil, nil
	}
	redisClient := redis.NewClient(redisOpts)

	mockNotificationRepo := new(mockNotificationRepository)
	mockChatRepo := new(mockChatRepository)
	mockMailRepo := new(mockMailRepository)
	mockGuildRepo := new(mockGuildRepository)
	mockModerationService := new(mockModerationService)

	service := &SocialService{
		notificationRepo: mockNotificationRepo,
		chatRepo:         mockChatRepo,
		mailRepo:         mockMailRepo,
		guildRepo:        mockGuildRepo,
		moderationService: mockModerationService,
		cache:            redisClient,
		logger:           GetLogger(),
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockNotificationRepo, mockChatRepo, mockMailRepo, mockGuildRepo, mockModerationService, cleanup
}

func TestSocialService_CreateNotification_Success(t *testing.T) {
	service, mockNotificationRepo, _, _, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	req := &models.CreateNotificationRequest{
		AccountID: accountID,
		Type:      models.NotificationTypeSystem,
		Priority:  models.NotificationPriorityMedium,
		Title:     "Test Notification",
		Content:   "Test content",
	}

	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: accountID,
		Type:      req.Type,
		Priority:  req.Priority,
		Title:     req.Title,
		Content:   req.Content,
		Status:    models.NotificationStatusUnread,
		Channels:  []models.DeliveryChannel{models.DeliveryChannelInGame},
		CreatedAt: time.Now(),
	}

	mockNotificationRepo.On("Create", mock.Anything, mock.AnythingOfType("*models.Notification")).Return(notification, nil)

	ctx := context.Background()
	result, err := service.CreateNotification(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, req.Title, result.Title)
	assert.Equal(t, accountID, result.AccountID)
	mockNotificationRepo.AssertExpectations(t)
}

func TestSocialService_GetNotifications_Success(t *testing.T) {
	service, mockNotificationRepo, _, _, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	notifications := []models.Notification{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Title:     "Notification 1",
			Status:    models.NotificationStatusUnread,
		},
	}

	mockNotificationRepo.On("GetByAccountID", mock.Anything, accountID, 10, 0).Return(notifications, nil)
	mockNotificationRepo.On("CountByAccountID", mock.Anything, accountID).Return(1, nil)
	mockNotificationRepo.On("CountUnreadByAccountID", mock.Anything, accountID).Return(1, nil)

	ctx := context.Background()
	result, err := service.GetNotifications(ctx, accountID, 10, 0)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Notifications, 1)
	assert.Equal(t, 1, result.Total)
	assert.Equal(t, 1, result.Unread)
	mockNotificationRepo.AssertExpectations(t)
}

func TestSocialService_GetNotifications_Cache(t *testing.T) {
	service, mockNotificationRepo, _, _, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	response := &models.NotificationListResponse{
		Notifications: []models.Notification{
			{
				ID:        uuid.New(),
				AccountID: accountID,
				Title:     "Cached Notification",
			},
		},
		Total:  1,
		Unread: 1,
	}

	responseJSON, _ := json.Marshal(response)

	ctx := context.Background()
	service.cache.Set(ctx, "notifications:account:"+accountID.String()+":limit:10:offset:0", responseJSON, 1*time.Minute)

	cached, err := service.GetNotifications(ctx, accountID, 10, 0)

	require.NoError(t, err)
	assert.NotNil(t, cached)
	assert.Len(t, cached.Notifications, 1)
	mockNotificationRepo.AssertNotCalled(t, "GetByAccountID", mock.Anything, accountID, 10, 0)
}

func TestSocialService_UpdateNotificationStatus_Success(t *testing.T) {
	service, mockNotificationRepo, _, _, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	notificationID := uuid.New()
	accountID := uuid.New()

	updatedNotification := &models.Notification{
		ID:        notificationID,
		AccountID: accountID,
		Status:    models.NotificationStatusRead,
	}

	mockNotificationRepo.On("UpdateStatus", mock.Anything, notificationID, models.NotificationStatusRead).Return(updatedNotification, nil)

	ctx := context.Background()
	result, err := service.UpdateNotificationStatus(ctx, notificationID, models.NotificationStatusRead)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.NotificationStatusRead, result.Status)
	mockNotificationRepo.AssertExpectations(t)
}

func TestSocialService_CreateMessage_Success(t *testing.T) {
	service, _, mockChatRepo, _, _, mockModerationService, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	message := &models.ChatMessage{
		ID:          uuid.New(),
		ChannelID:   uuid.New(),
		ChannelType: models.ChannelTypeGuild,
		SenderID:    uuid.New(),
		SenderName:  "Test User",
		Content:     "Test message",
		CreatedAt:   time.Now(),
	}

	mockModerationService.On("CheckBan", mock.Anything, message.SenderID, &message.ChannelID).Return(nil, nil)
	mockModerationService.On("DetectSpam", mock.Anything, message.SenderID, message.Content).Return(false, nil)
	mockModerationService.On("FilterMessage", mock.Anything, message.Content).Return(message.Content, false, nil)
	mockChatRepo.On("CreateMessage", mock.Anything, message).Return(message, nil)

	ctx := context.Background()
	result, err := service.CreateMessage(ctx, message)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, message.Content, result.Content)
	mockChatRepo.AssertExpectations(t)
	mockModerationService.AssertExpectations(t)
}

func TestSocialService_SendMail_Success(t *testing.T) {
	service, _, _, mockMailRepo, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	senderID := uuid.New()
	recipientID := uuid.New()
	req := &models.CreateMailRequest{
		RecipientID: recipientID,
		Subject:    "Test Mail",
		Content:    "Test content",
	}

	mockMailRepo.On("Create", mock.Anything, mock.AnythingOfType("*models.MailMessage")).Return(nil)

	ctx := context.Background()
	mail, err := service.SendMail(ctx, req, &senderID, "Test Sender")

	require.NoError(t, err)
	assert.NotNil(t, mail)
	assert.Equal(t, req.Subject, mail.Subject)
	assert.Equal(t, recipientID, mail.RecipientID)
	mockMailRepo.AssertExpectations(t)
}

func TestSocialService_GetMails_Success(t *testing.T) {
	service, _, _, mockMailRepo, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	recipientID := uuid.New()
	mails := []models.MailMessage{
		{
			ID:          uuid.New(),
			RecipientID: recipientID,
			Subject:     "Mail 1",
			Status:      models.MailStatusUnread,
		},
	}

	mockMailRepo.On("GetByRecipientID", mock.Anything, recipientID, 10, 0).Return(mails, nil)
	mockMailRepo.On("CountByRecipientID", mock.Anything, recipientID).Return(1, nil)
	mockMailRepo.On("CountUnreadByRecipientID", mock.Anything, recipientID).Return(1, nil)

	ctx := context.Background()
	result, err := service.GetMails(ctx, recipientID, 10, 0)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Messages, 1)
	assert.Equal(t, 1, result.Total)
	assert.Equal(t, 1, result.Unread)
	mockMailRepo.AssertExpectations(t)
}

func TestSocialService_MarkMailAsRead_Success(t *testing.T) {
	service, _, _, mockMailRepo, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	mailID := uuid.New()
	recipientID := uuid.New()
	mail := &models.MailMessage{
		ID:          mailID,
		RecipientID: recipientID,
		Status:      models.MailStatusUnread,
	}

	mockMailRepo.On("GetByID", mock.Anything, mailID).Return(mail, nil)
	mockMailRepo.On("UpdateStatus", mock.Anything, mailID, models.MailStatusRead, mock.AnythingOfType("*time.Time")).Return(nil)

	ctx := context.Background()
	err := service.MarkMailAsRead(ctx, mailID)

	require.NoError(t, err)
	mockMailRepo.AssertExpectations(t)
}

func TestSocialService_CreateNotification_DatabaseError(t *testing.T) {
	service, mockNotificationRepo, _, _, _, _, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	req := &models.CreateNotificationRequest{
		AccountID: accountID,
		Type:      models.NotificationTypeSystem,
		Priority:  models.NotificationPriorityMedium,
		Title:     "Test Notification",
		Content:   "Test content",
	}
	expectedErr := errors.New("database error")

	mockNotificationRepo.On("Create", mock.Anything, mock.AnythingOfType("*models.Notification")).Return(nil, expectedErr)

	ctx := context.Background()
	notification, err := service.CreateNotification(ctx, req)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, notification)
	mockNotificationRepo.AssertExpectations(t)
}
