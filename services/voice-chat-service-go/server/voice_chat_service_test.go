// Unit tests for VoiceChatService
// Issue: #140895495
// PERFORMANCE: Tests run without external dependencies

package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap/zaptest"
)

// MockVoiceChatRepository is a mock implementation for testing
type MockVoiceChatRepository struct {
	mock.Mock
}

func (m *MockVoiceChatRepository) CreateVoiceChannel(ctx context.Context, channel *VoiceChannel) error {
	args := m.Called(ctx, channel)
	return args.Error(0)
}

func (m *MockVoiceChatRepository) GetVoiceChannelByID(ctx context.Context, channelID uuid.UUID) (*VoiceChannel, error) {
	args := m.Called(ctx, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*VoiceChannel), args.Error(1)
}

func (m *MockVoiceChatRepository) ListVoiceChannels(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*VoiceChannel, error) {
	args := m.Called(ctx, guildID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]*VoiceChannel), args.Error(1)
}

func (m *MockVoiceChatRepository) UpdateVoiceChannel(ctx context.Context, channel *VoiceChannel) error {
	args := m.Called(ctx, channel)
	return args.Error(0)
}

func (m *MockVoiceChatRepository) DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error {
	args := m.Called(ctx, channelID)
	return args.Error(0)
}

func (m *MockVoiceChatRepository) AddUserToChannel(ctx context.Context, userID, channelID uuid.UUID) error {
	args := m.Called(ctx, userID, channelID)
	return args.Error(0)
}

func (m *MockVoiceChatRepository) RemoveUserFromChannel(ctx context.Context, userID, channelID uuid.UUID) error {
	args := m.Called(ctx, userID, channelID)
	return args.Error(0)
}

func (m *MockVoiceChatRepository) GetChannelUsers(ctx context.Context, channelID uuid.UUID) ([]*VoiceChannelUser, error) {
	args := m.Called(ctx, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]*VoiceChannelUser), args.Error(1)
}

func (m *MockVoiceChatRepository) CreateVoiceSession(ctx context.Context, session *VoiceSession) error {
	args := m.Called(ctx, session)
	return args.Error(0)
}

func (m *MockVoiceChatRepository) GetVoiceSession(ctx context.Context, sessionID uuid.UUID) (*VoiceSession, error) {
	args := m.Called(ctx, sessionID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*VoiceSession), args.Error(1)
}

func (m *MockVoiceChatRepository) EndVoiceSession(ctx context.Context, sessionID uuid.UUID) error {
	args := m.Called(ctx, sessionID)
	return args.Error(0)
}

func TestVoiceChatServiceInterface_ImplementsInterface(t *testing.T) {
	// This test ensures our service implements the interface
	var service VoiceChatServiceInterface = &VoiceChatService{}
	assert.NotNil(t, service)
}

func TestNewVoiceChatService(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}

	service := NewVoiceChatService(logger, mockRepo)

	assert.NotNil(t, service)
	assert.Equal(t, logger, service.logger)
	assert.Equal(t, mockRepo, service.repository)
	assert.NotNil(t, service.activeSessions)
	assert.NotNil(t, service.channelUsers)
}

func TestVoiceChatService_CreateVoiceChannel_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	guildID := uuid.New()
	name := "Test Channel"
	description := "Test Description"
	maxUsers := 10

	expectedChannel := &VoiceChannel{
		ID:          uuid.New(),
		GuildID:     guildID,
		Name:        name,
		Description: description,
		MaxUsers:    maxUsers,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("CreateVoiceChannel", ctx, mock.AnythingOfType("*server.VoiceChannel")).Return(nil)

	channel, err := service.CreateVoiceChannel(ctx, guildID, name, description, maxUsers)

	assert.NoError(t, err)
	assert.NotNil(t, channel)
	assert.Equal(t, guildID, channel.GuildID)
	assert.Equal(t, name, channel.Name)
	assert.Equal(t, description, channel.Description)
	assert.Equal(t, maxUsers, channel.MaxUsers)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_CreateVoiceChannel_EmptyName(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	guildID := uuid.New()

	_, err := service.CreateVoiceChannel(ctx, guildID, "", "description", 10)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "channel name cannot be empty")
}

func TestVoiceChatService_CreateVoiceChannel_InvalidMaxUsers(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	guildID := uuid.New()

	_, err := service.CreateVoiceChannel(ctx, guildID, "Test Channel", "description", -1)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "max users must be between 0 and 100")

	_, err = service.CreateVoiceChannel(ctx, guildID, "Test Channel", "description", 101)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "max users must be between 0 and 100")
}

func TestVoiceChatService_GetVoiceChannel_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	channelID := uuid.New()
	expectedChannel := &VoiceChannel{
		ID:          channelID,
		GuildID:     uuid.New(),
		Name:        "Test Channel",
		Description: "Test Description",
		MaxUsers:    10,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(expectedChannel, nil)

	channel, err := service.GetVoiceChannel(ctx, channelID)

	assert.NoError(t, err)
	assert.NotNil(t, channel)
	assert.Equal(t, expectedChannel, channel)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_GetVoiceChannel_NotFound(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	channelID := uuid.New()

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(nil, nil)

	_, err := service.GetVoiceChannel(ctx, channelID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "voice channel not found")

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_ListVoiceChannels_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	guildID := uuid.New()
	limit := 10
	offset := 0

	expectedChannels := []*VoiceChannel{
		{
			ID:      uuid.New(),
			GuildID: guildID,
			Name:    "Channel 1",
		},
		{
			ID:      uuid.New(),
			GuildID: guildID,
			Name:    "Channel 2",
		},
	}

	mockRepo.On("ListVoiceChannels", ctx, guildID, limit, offset).Return(expectedChannels, nil)

	channels, err := service.ListVoiceChannels(ctx, guildID, limit, offset)

	assert.NoError(t, err)
	assert.NotNil(t, channels)
	assert.Len(t, channels, 2)
	assert.Equal(t, expectedChannels, channels)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_UpdateVoiceChannel_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	channelID := uuid.New()
	existingChannel := &VoiceChannel{
		ID:          channelID,
		GuildID:     uuid.New(),
		Name:        "Old Name",
		Description: "Old Description",
		MaxUsers:    5,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	newName := "New Name"
	newDescription := "New Description"
	newMaxUsers := 20

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(existingChannel, nil)
	mockRepo.On("UpdateVoiceChannel", ctx, mock.AnythingOfType("*server.VoiceChannel")).Return(nil)

	err := service.UpdateVoiceChannel(ctx, channelID, newName, newDescription, newMaxUsers)

	assert.NoError(t, err)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_UpdateVoiceChannel_NotFound(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	channelID := uuid.New()

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(nil, nil)

	err := service.UpdateVoiceChannel(ctx, channelID, "New Name", "New Description", 10)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "voice channel not found")

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_DeleteVoiceChannel_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	channelID := uuid.New()
	existingChannel := &VoiceChannel{
		ID:          channelID,
		GuildID:     uuid.New(),
		Name:        "Test Channel",
		Description: "Test Description",
		MaxUsers:    10,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(existingChannel, nil)
	mockRepo.On("GetChannelUsers", ctx, channelID).Return([]*VoiceChannelUser{}, nil)
	mockRepo.On("DeleteVoiceChannel", ctx, channelID).Return(nil)

	err := service.DeleteVoiceChannel(ctx, channelID)

	assert.NoError(t, err)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_JoinVoiceChannel_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	userID := uuid.New()
	channelID := uuid.New()
	channel := &VoiceChannel{
		ID:          channelID,
		GuildID:     uuid.New(),
		Name:        "Test Channel",
		Description: "Test Description",
		MaxUsers:    10,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(channel, nil)
	mockRepo.On("GetChannelUsers", ctx, channelID).Return([]*VoiceChannelUser{}, nil)
	mockRepo.On("AddUserToChannel", ctx, userID, channelID).Return(nil)

	err := service.JoinVoiceChannel(ctx, userID, channelID)

	assert.NoError(t, err)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_JoinVoiceChannel_ChannelFull(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	userID := uuid.New()
	channelID := uuid.New()
	channel := &VoiceChannel{
		ID:          channelID,
		GuildID:     uuid.New(),
		Name:        "Test Channel",
		Description: "Test Description",
		MaxUsers:    2,
		IsLocked:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	users := []*VoiceChannelUser{
		{UserID: uuid.New(), ChannelID: channelID, JoinedAt: time.Now()},
		{UserID: uuid.New(), ChannelID: channelID, JoinedAt: time.Now()},
	}

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(channel, nil)
	mockRepo.On("GetChannelUsers", ctx, channelID).Return(users, nil)

	err := service.JoinVoiceChannel(ctx, userID, channelID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "voice channel is full")

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_JoinVoiceChannel_ChannelLocked(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	userID := uuid.New()
	channelID := uuid.New()
	channel := &VoiceChannel{
		ID:          channelID,
		GuildID:     uuid.New(),
		Name:        "Test Channel",
		Description: "Test Description",
		MaxUsers:    10,
		IsLocked:    true,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetVoiceChannelByID", ctx, channelID).Return(channel, nil)

	err := service.JoinVoiceChannel(ctx, userID, channelID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "voice channel is locked")

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_LeaveVoiceChannel_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	userID := uuid.New()
	channelID := uuid.New()

	mockRepo.On("RemoveUserFromChannel", ctx, userID, channelID).Return(nil)

	err := service.LeaveVoiceChannel(ctx, userID, channelID)

	assert.NoError(t, err)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_StartVoiceSession_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	userID := uuid.New()
	channelID := uuid.New()

	mockRepo.On("CreateVoiceSession", ctx, mock.AnythingOfType("*server.VoiceSession")).Return(nil)

	session, err := service.StartVoiceSession(ctx, userID, channelID)

	assert.NoError(t, err)
	assert.NotNil(t, session)
	assert.Equal(t, userID, session.UserID)
	assert.Equal(t, channelID, session.ChannelID)
	assert.False(t, session.IsMuted)
	assert.False(t, session.IsDeafened)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_EndVoiceSession_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	sessionID := uuid.New()

	mockRepo.On("EndVoiceSession", ctx, sessionID).Return(nil)

	err := service.EndVoiceSession(ctx, sessionID)

	assert.NoError(t, err)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_GetVoiceSession_Success(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	sessionID := uuid.New()
	expectedSession := &VoiceSession{
		ID:         sessionID,
		UserID:     uuid.New(),
		ChannelID:  uuid.New(),
		StartedAt:  time.Now(),
		IsMuted:    false,
		IsDeafened: false,
	}

	mockRepo.On("GetVoiceSession", ctx, sessionID).Return(expectedSession, nil)

	session, err := service.GetVoiceSession(ctx, sessionID)

	assert.NoError(t, err)
	assert.NotNil(t, session)
	assert.Equal(t, expectedSession, session)

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_GetVoiceSession_NotFound(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	sessionID := uuid.New()

	mockRepo.On("GetVoiceSession", ctx, sessionID).Return(nil, nil)

	_, err := service.GetVoiceSession(ctx, sessionID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "voice session not found")

	mockRepo.AssertExpectations(t)
}

func TestVoiceChatService_MuteUser(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	sessionID := uuid.New()

	err := service.MuteUser(ctx, sessionID, true)

	assert.NoError(t, err)
}

func TestVoiceChatService_DeafenUser(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	sessionID := uuid.New()

	err := service.DeafenUser(ctx, sessionID, true)

	assert.NoError(t, err)
}

// Test concurrent operations
func TestVoiceChatService_ConcurrentOperations(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	guildID := uuid.New()

	// Mock successful operations
	mockRepo.On("CreateVoiceChannel", ctx, mock.AnythingOfType("*server.VoiceChannel")).Return(nil).Maybe()

	done := make(chan bool, 3)

	// Test concurrent channel creation
	for i := 0; i < 3; i++ {
		go func() {
			service.CreateVoiceChannel(ctx, guildID, "Concurrent Channel", "Description", 10)
			done <- true
		}()
	}

	for i := 0; i < 3; i++ {
		<-done
	}
}

// Test error propagation
func TestVoiceChatService_ErrorPropagation(t *testing.T) {
	logger := zaptest.NewLogger(t)
	mockRepo := &MockVoiceChatRepository{}
	service := NewVoiceChatService(logger, mockRepo)

	ctx := context.Background()
	expectedError := errors.New("repository error")

	// Test CreateVoiceChannel error propagation
	mockRepo.On("CreateVoiceChannel", ctx, mock.AnythingOfType("*server.VoiceChannel")).Return(expectedError)

	_, err := service.CreateVoiceChannel(ctx, uuid.New(), "Test", "Description", 10)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "failed to create voice channel")

	mockRepo.AssertExpectations(t)
}
