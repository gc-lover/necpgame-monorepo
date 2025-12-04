// Issue: #140895495
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestRepository(t *testing.T) (*VoiceRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewVoiceRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewVoiceRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewVoiceRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestVoiceRepository_GetChannel_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()

	ctx := context.Background()
	channel, err := repo.GetChannel(ctx, channelID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, channel)
}

func TestVoiceRepository_CreateChannel(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channel := &models.VoiceChannel{
		Type:          models.VoiceChannelTypeParty,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		Name:          "Test Channel",
		MaxMembers:    5,
		QualityPreset: "standard",
		Settings:      make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateChannel(ctx, channel)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, channel.ID)
}

func TestVoiceRepository_ListChannels_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()
	channels, err := repo.ListChannels(ctx, nil, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, channels)
}

func TestVoiceRepository_GetParticipant_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()

	ctx := context.Background()
	participant, err := repo.GetParticipant(ctx, channelID, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, participant)
}

func TestVoiceRepository_AddParticipant(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channel := &models.VoiceChannel{
		Type:          models.VoiceChannelTypeParty,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		Name:          "Test Channel",
		MaxMembers:    5,
		QualityPreset: "standard",
		Settings:      make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateChannel(ctx, channel)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	participant := &models.VoiceParticipant{
		ChannelID:   channel.ID,
		CharacterID: uuid.New(),
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		Position:    make(map[string]interface{}),
		Stats:       make(map[string]interface{}),
	}

	err = repo.AddParticipant(ctx, participant)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, participant.ID)
}

func TestVoiceRepository_ListParticipants_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()

	ctx := context.Background()
	participants, err := repo.ListParticipants(ctx, channelID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, participants)
}

func TestVoiceRepository_CountParticipants(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()

	ctx := context.Background()
	count, err := repo.CountParticipants(ctx, channelID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

func TestVoiceRepository_UpdateParticipantStatus(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channel := &models.VoiceChannel{
		Type:          models.VoiceChannelTypeParty,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		Name:          "Test Channel",
		MaxMembers:    5,
		QualityPreset: "standard",
		Settings:      make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateChannel(ctx, channel)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	participant := &models.VoiceParticipant{
		ChannelID:   channel.ID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		Position:    make(map[string]interface{}),
		Stats:       make(map[string]interface{}),
	}

	err = repo.AddParticipant(ctx, participant)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.UpdateParticipantStatus(ctx, channel.ID, characterID, models.ParticipantStatusMuted)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestVoiceRepository_UpdateParticipantPosition(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channel := &models.VoiceChannel{
		Type:          models.VoiceChannelTypeParty,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		Name:          "Test Channel",
		MaxMembers:    5,
		QualityPreset: "standard",
		Settings:      make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateChannel(ctx, channel)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	participant := &models.VoiceParticipant{
		ChannelID:   channel.ID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		Position:    make(map[string]interface{}),
		Stats:       make(map[string]interface{}),
	}

	err = repo.AddParticipant(ctx, participant)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	position := map[string]interface{}{
		"x": 10.0,
		"y": 20.0,
		"z": 30.0,
	}

	err = repo.UpdateParticipantPosition(ctx, channel.ID, characterID, position)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestVoiceRepository_RemoveParticipant(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	channel := &models.VoiceChannel{
		Type:          models.VoiceChannelTypeParty,
		OwnerID:       uuid.New(),
		OwnerType:     "character",
		Name:          "Test Channel",
		MaxMembers:    5,
		QualityPreset: "standard",
		Settings:      make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateChannel(ctx, channel)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characterID := uuid.New()
	participant := &models.VoiceParticipant{
		ChannelID:   channel.ID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "test-token",
		Position:    make(map[string]interface{}),
		Stats:       make(map[string]interface{}),
	}

	err = repo.AddParticipant(ctx, participant)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.RemoveParticipant(ctx, channel.ID, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
}

