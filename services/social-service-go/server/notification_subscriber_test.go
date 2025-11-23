package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)


type mockPreferencesRepository struct {
	mock.Mock
}

func (m *mockPreferencesRepository) GetByAccountID(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	args := m.Called(ctx, accountID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.NotificationPreferences), args.Error(1)
}

func (m *mockPreferencesRepository) Update(ctx context.Context, prefs *models.NotificationPreferences) error {
	args := m.Called(ctx, prefs)
	return args.Error(0)
}

func TestNotificationSubscriber_getNotificationTypeFromChannel(t *testing.T) {
	ns := &NotificationSubscriber{logger: GetLogger()}

	tests := []struct {
		channel string
		expected models.NotificationType
	}{
		{"events:friend:request", models.NotificationTypeFriend},
		{"events:guild:invite", models.NotificationTypeGuild},
		{"events:trade:completed", models.NotificationTypeTrade},
		{"events:achievement:unlocked", models.NotificationTypeAchievement},
		{"events:quest:completed", models.NotificationTypeQuest},
		{"events:combat:kill", models.NotificationTypeCombat},
		{"events:unknown:event", models.NotificationTypeSystem},
	}

	for _, tt := range tests {
		t.Run(tt.channel, func(t *testing.T) {
			result := ns.getNotificationTypeFromChannel(tt.channel)
			assert.Equal(t, tt.expected, result)
		})
	}
}

func TestNotificationSubscriber_getPriorityFromEvent(t *testing.T) {
	ns := &NotificationSubscriber{logger: GetLogger()}

	tests := []struct {
		name     string
		eventData map[string]interface{}
		expected models.NotificationPriority
	}{
		{
			name: "Critical priority",
			eventData: map[string]interface{}{"priority": "critical"},
			expected: models.NotificationPriorityCritical,
		},
		{
			name: "High priority",
			eventData: map[string]interface{}{"priority": "high"},
			expected: models.NotificationPriorityHigh,
		},
		{
			name: "Medium priority",
			eventData: map[string]interface{}{"priority": "medium"},
			expected: models.NotificationPriorityMedium,
		},
		{
			name: "Low priority",
			eventData: map[string]interface{}{"priority": "low"},
			expected: models.NotificationPriorityLow,
		},
		{
			name: "Default priority",
			eventData: map[string]interface{}{},
			expected: models.NotificationPriorityMedium,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := ns.getPriorityFromEvent(tt.eventData)
			assert.Equal(t, tt.expected, result)
		})
	}
}

func TestNotificationSubscriber_createNotificationFromEvent(t *testing.T) {
	mockRepo := new(mockNotificationRepository)
	mockPrefsRepo := new(mockPreferencesRepository)
	
	redisClient := redis.NewClient(&redis.Options{})
	ns := NewNotificationSubscriber(mockRepo, redisClient)
	ns.SetPreferencesRepository(mockPrefsRepo)

	accountID := uuid.New()
	eventData := map[string]interface{}{
		"account_id": accountID.String(),
		"title":       "Test Title",
		"content":     "Test Content",
		"priority":    "high",
	}

	mockPrefsRepo.On("GetByAccountID", mock.Anything, accountID).Return(&models.NotificationPreferences{
		AccountID:         accountID,
		QuestEnabled:      true,
		MessageEnabled:    true,
		AchievementEnabled: true,
		SystemEnabled:     true,
		FriendEnabled:     true,
		GuildEnabled:      true,
		TradeEnabled:      true,
		CombatEnabled:     true,
		PreferredChannels: []models.DeliveryChannel{models.DeliveryChannelInGame, models.DeliveryChannelWebSocket},
	}, nil)

	notification := ns.createNotificationFromEvent("events:friend:request", eventData)

	assert.NotNil(t, notification)
	assert.Equal(t, accountID, notification.AccountID)
	assert.Equal(t, models.NotificationTypeFriend, notification.Type)
	assert.Equal(t, models.NotificationPriorityHigh, notification.Priority)
	assert.Equal(t, "Test Title", notification.Title)
	assert.Equal(t, "Test Content", notification.Content)
	assert.Contains(t, notification.Channels, models.DeliveryChannelWebSocket)

	mockPrefsRepo.AssertExpectations(t)
}

func TestNotificationSubscriber_shouldCreateNotification(t *testing.T) {
	mockPrefsRepo := new(mockPreferencesRepository)
	redisClient := redis.NewClient(&redis.Options{})
	ns := NewNotificationSubscriber(nil, redisClient)
	ns.SetPreferencesRepository(mockPrefsRepo)

	accountID := uuid.New()

	tests := []struct {
		name           string
		notificationType models.NotificationType
		prefs          *models.NotificationPreferences
		expected       bool
	}{
		{
			name:            "Quest enabled",
			notificationType: models.NotificationTypeQuest,
			prefs: &models.NotificationPreferences{QuestEnabled: true},
			expected:        true,
		},
		{
			name:            "Quest disabled",
			notificationType: models.NotificationTypeQuest,
			prefs: &models.NotificationPreferences{QuestEnabled: false},
			expected:        false,
		},
		{
			name:            "No preferences",
			notificationType: models.NotificationTypeQuest,
			prefs:           nil,
			expected:        true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.prefs != nil {
				tt.prefs.AccountID = accountID
				mockPrefsRepo.On("GetByAccountID", mock.Anything, accountID).Return(tt.prefs, nil)
			} else {
				mockPrefsRepo.On("GetByAccountID", mock.Anything, accountID).Return(nil, nil)
			}

			result := ns.shouldCreateNotification(accountID, tt.notificationType)
			assert.Equal(t, tt.expected, result)
		})
	}
}

func TestNotificationSubscriber_publishWebSocketNotification(t *testing.T) {
	mockRepo := new(mockNotificationRepository)
	redisClient := redis.NewClient(&redis.Options{})
	ns := NewNotificationSubscriber(mockRepo, redisClient)

	accountID := uuid.New()
	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: accountID,
		Type:      models.NotificationTypeFriend,
		Priority:  models.NotificationPriorityHigh,
		Title:     "Test",
		Content:   "Test Content",
		Channels:  []models.DeliveryChannel{models.DeliveryChannelWebSocket},
		CreatedAt: time.Now(),
	}

	ctx := context.Background()
	ns.publishWebSocketNotification(ctx, notification)

	time.Sleep(100 * time.Millisecond)
}

