package server

import (
	"context"
	"encoding/json"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockRedisPubSub struct {
	mock.Mock
}

type mockSessionManager struct {
	mock.Mock
}

func (m *mockSessionManager) CreateSession(ctx context.Context, playerID, ipAddress, userAgent string, characterID *uuid.UUID) (*PlayerSession, error) {
	args := m.Called(ctx, playerID, ipAddress, userAgent, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*PlayerSession), args.Error(1)
}

func (m *mockSessionManager) GetSessionByToken(ctx context.Context, token string) (*PlayerSession, error) {
	args := m.Called(ctx, token)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*PlayerSession), args.Error(1)
}

func (m *mockSessionManager) GetSessionByPlayerID(ctx context.Context, playerID string) (*PlayerSession, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*PlayerSession), args.Error(1)
}

func (m *mockSessionManager) UpdateHeartbeat(ctx context.Context, token string) error {
	args := m.Called(ctx, token)
	return args.Error(0)
}

func (m *mockSessionManager) ReconnectSession(ctx context.Context, reconnectToken, ipAddress, userAgent string) (*PlayerSession, error) {
	args := m.Called(ctx, reconnectToken, ipAddress, userAgent)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*PlayerSession), args.Error(1)
}

func (m *mockSessionManager) CloseSession(ctx context.Context, token string) error {
	args := m.Called(ctx, token)
	return args.Error(0)
}

func (m *mockSessionManager) DisconnectSession(ctx context.Context, token string) error {
	args := m.Called(ctx, token)
	return args.Error(0)
}

func (m *mockSessionManager) GetActiveSessionsCount(ctx context.Context) (int, error) {
	args := m.Called(ctx)
	return args.Int(0), args.Error(1)
}

func (m *mockSessionManager) CleanupExpiredSessions(ctx context.Context) error {
	args := m.Called(ctx)
	return args.Error(0)
}

func (m *mockSessionManager) SaveSession(ctx context.Context, session *PlayerSession) error {
	args := m.Called(ctx, session)
	return args.Error(0)
}

func TestBanNotificationSubscriber_buildNotificationMessage(t *testing.T) {
	subscriber := &BanNotificationSubscriber{
		logger: GetLogger(),
	}

	characterID := uuid.New().String()
	banID := uuid.New().String()
	expiresAt := time.Now().Add(24 * time.Hour).Format(time.RFC3339)
	channelID := uuid.New().String()
	banType := "auto_spam"

	notification := BanNotification{
		BanID:       banID,
		CharacterID: characterID,
		Reason:      "Test ban reason",
		ExpiresAt:   &expiresAt,
		ChannelID:   &channelID,
		Type:        &banType,
		Timestamp:   time.Now().Format(time.RFC3339),
	}

	message := subscriber.buildNotificationMessage(notification, false)

	var result map[string]interface{}
	err := json.Unmarshal(message, &result)
	assert.NoError(t, err)

	assert.Equal(t, "ban_notification", result["type"])
	assert.Equal(t, banID, result["ban_id"])
	assert.Equal(t, "Test ban reason", result["reason"])
	assert.Equal(t, expiresAt, result["expires_at"])
	assert.Equal(t, channelID, result["channel_id"])
	assert.Equal(t, banType, result["ban_type"])
	assert.NotEmpty(t, result["timestamp"])
}

func TestBanNotificationSubscriber_buildNotificationMessage_Minimal(t *testing.T) {
	subscriber := &BanNotificationSubscriber{
		logger: GetLogger(),
	}

	characterID := uuid.New().String()
	banID := uuid.New().String()

	notification := BanNotification{
		BanID:       banID,
		CharacterID: characterID,
		Reason:      "Test ban reason",
		Timestamp:   time.Now().Format(time.RFC3339),
	}

	message := subscriber.buildNotificationMessage(notification, false)

	var result map[string]interface{}
	err := json.Unmarshal(message, &result)
	assert.NoError(t, err)

	assert.Equal(t, "ban_notification", result["type"])
	assert.Equal(t, banID, result["ban_id"])
	assert.Equal(t, "Test ban reason", result["reason"])
	assert.NotContains(t, result, "expires_at")
	assert.NotContains(t, result, "channel_id")
	assert.NotContains(t, result, "ban_type")
	assert.NotEmpty(t, result["timestamp"])
}
