package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type VoiceRepositoryInterface interface {
	CreateChannel(ctx context.Context, channel *models.VoiceChannel) error
	GetChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error)
	ListChannels(ctx context.Context, channelType *models.VoiceChannelType, ownerID *uuid.UUID, limit, offset int) ([]models.VoiceChannel, error)
	AddParticipant(ctx context.Context, participant *models.VoiceParticipant) error
	RemoveParticipant(ctx context.Context, channelID, characterID uuid.UUID) error
	GetParticipant(ctx context.Context, channelID, characterID uuid.UUID) (*models.VoiceParticipant, error)
	ListParticipants(ctx context.Context, channelID uuid.UUID) ([]models.VoiceParticipant, error)
	UpdateParticipantStatus(ctx context.Context, channelID, characterID uuid.UUID, status models.ParticipantStatus) error
	UpdateParticipantPosition(ctx context.Context, channelID, characterID uuid.UUID, position map[string]interface{}) error
	CountParticipants(ctx context.Context, channelID uuid.UUID) (int, error)
}

type VoiceService struct {
	repo       VoiceRepositoryInterface
	cache      *redis.Client
	logger     *logrus.Logger
	webrtcURL  string
	webrtcKey  string
}

func NewVoiceService(dbURL, redisURL, webrtcURL, webrtcKey string) (*VoiceService, error) {
	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewVoiceRepository(dbPool)

	return &VoiceService{
		repo:      repo,
		cache:     redisClient,
		logger:    GetLogger(),
		webrtcURL: webrtcURL,
		webrtcKey: webrtcKey,
	}, nil
}

func (s *VoiceService) CreateChannel(ctx context.Context, req *models.CreateChannelRequest) (*models.VoiceChannel, error) {
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

	qualityPreset := req.QualityPreset
	if qualityPreset == "" {
		qualityPreset = "standard"
	}

	channel := &models.VoiceChannel{
		OwnerID:      req.CharacterID,
		OwnerType:    "character",
		Type:         req.Type,
		Name:         req.Name,
		MaxMembers:   maxMembers,
		QualityPreset: qualityPreset,
		Settings:     req.Settings,
	}

	if channel.Settings == nil {
		channel.Settings = make(map[string]interface{})
	}

	err := s.repo.CreateChannel(ctx, channel)
	if err != nil {
		return nil, err
	}

	s.publishChannelCreatedEvent(ctx, channel)

	SetChannelsTotal(string(channel.Type), 1)

	return channel, nil
}

func (s *VoiceService) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error) {
	return s.repo.GetChannel(ctx, channelID)
}

func (s *VoiceService) ListChannels(ctx context.Context, channelType *models.VoiceChannelType, ownerID *uuid.UUID, limit, offset int) (*models.ChannelListResponse, error) {
	channels, err := s.repo.ListChannels(ctx, channelType, ownerID, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.ChannelListResponse{
		Channels: channels,
		Total:    len(channels),
	}, nil
}

func (s *VoiceService) JoinChannel(ctx context.Context, req *models.JoinChannelRequest) (*models.WebRTCTokenResponse, error) {
	channel, err := s.repo.GetChannel(ctx, req.ChannelID)
	if err != nil {
		return nil, err
	}
	if channel == nil {
		return nil, err
	}

	count, err := s.repo.CountParticipants(ctx, req.ChannelID)
	if err != nil {
		return nil, err
	}

	if count >= channel.MaxMembers {
		return nil, fmt.Errorf("channel is full")
	}

	existing, err := s.repo.GetParticipant(ctx, req.ChannelID, req.CharacterID)
	if err != nil {
		return nil, err
	}
	if existing != nil {
		return &models.WebRTCTokenResponse{
			Token:     existing.WebRTCToken,
			ServerURL: s.webrtcURL,
			ExpiresAt: time.Now().Add(24 * time.Hour),
		}, nil
	}

	token := s.generateWebRTCToken(req.CharacterID, req.ChannelID)

	participant := &models.VoiceParticipant{
		ChannelID:   req.ChannelID,
		CharacterID: req.CharacterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: token,
		Position:    req.Position,
		Stats:       make(map[string]interface{}),
	}

	if participant.Position == nil {
		participant.Position = make(map[string]interface{})
	}

	err = s.repo.AddParticipant(ctx, participant)
	if err != nil {
		return nil, err
	}

	s.publishParticipantJoinedEvent(ctx, participant)

	SetParticipantsTotal(string(channel.Type), float64(count+1))

	return &models.WebRTCTokenResponse{
		Token:     token,
		ServerURL: s.webrtcURL,
		ExpiresAt: time.Now().Add(24 * time.Hour),
	}, nil
}

func (s *VoiceService) LeaveChannel(ctx context.Context, req *models.LeaveChannelRequest) error {
	channel, err := s.repo.GetChannel(ctx, req.ChannelID)
	if err != nil {
		return err
	}
	if channel == nil {
		return nil
	}

	err = s.repo.RemoveParticipant(ctx, req.ChannelID, req.CharacterID)
	if err != nil {
		return err
	}

	s.publishParticipantLeftEvent(ctx, req.ChannelID, req.CharacterID)

	count, _ := s.repo.CountParticipants(ctx, req.ChannelID)
	SetParticipantsTotal(string(channel.Type), float64(count))

	return nil
}

func (s *VoiceService) UpdateParticipantStatus(ctx context.Context, req *models.UpdateParticipantStatusRequest) error {
	return s.repo.UpdateParticipantStatus(ctx, req.ChannelID, req.CharacterID, req.Status)
}

func (s *VoiceService) UpdateParticipantPosition(ctx context.Context, req *models.UpdateParticipantPositionRequest) error {
	return s.repo.UpdateParticipantPosition(ctx, req.ChannelID, req.CharacterID, req.Position)
}

func (s *VoiceService) GetChannelParticipants(ctx context.Context, channelID uuid.UUID) (*models.ParticipantListResponse, error) {
	participants, err := s.repo.ListParticipants(ctx, channelID)
	if err != nil {
		return nil, err
	}

	return &models.ParticipantListResponse{
		Participants: participants,
		Total:        len(participants),
	}, nil
}

func (s *VoiceService) GetChannelDetail(ctx context.Context, channelID uuid.UUID) (*models.ChannelDetailResponse, error) {
	channel, err := s.repo.GetChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}
	if channel == nil {
		return nil, nil
	}

	participants, err := s.repo.ListParticipants(ctx, channelID)
	if err != nil {
		return nil, err
	}

	return &models.ChannelDetailResponse{
		Channel:      channel,
		Participants: participants,
	}, nil
}

func (s *VoiceService) generateWebRTCToken(characterID, channelID uuid.UUID) string {
	return uuid.New().String()
}

func (s *VoiceService) publishChannelCreatedEvent(ctx context.Context, channel *models.VoiceChannel) {
	payload := map[string]interface{}{
		"channel_id": channel.ID.String(),
		"type":       string(channel.Type),
		"owner_id":   channel.OwnerID.String(),
		"name":       channel.Name,
		"timestamp":  time.Now().Format(time.RFC3339),
	}

	eventData, _ := json.Marshal(payload)
	s.cache.Publish(ctx, "events:voice:channel-created", eventData)
}

func (s *VoiceService) publishParticipantJoinedEvent(ctx context.Context, participant *models.VoiceParticipant) {
	payload := map[string]interface{}{
		"channel_id":   participant.ChannelID.String(),
		"character_id": participant.CharacterID.String(),
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	eventData, _ := json.Marshal(payload)
	s.cache.Publish(ctx, "events:voice:participant-joined", eventData)
}

func (s *VoiceService) publishParticipantLeftEvent(ctx context.Context, channelID, characterID uuid.UUID) {
	payload := map[string]interface{}{
		"channel_id":   channelID.String(),
		"character_id": characterID.String(),
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	eventData, _ := json.Marshal(payload)
	s.cache.Publish(ctx, "events:voice:participant-left", eventData)
}

