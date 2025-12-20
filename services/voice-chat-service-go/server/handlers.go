// Package server Issue: ogen migration, #1607
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

type Handlers struct {
	voiceService VoiceServiceInterface
	logger       *logrus.Logger

	// Memory pooling for hot path structs (Issue #1607)
	channelPool             sync.Pool
	channelDetailPool       sync.Pool
	channelListPool         sync.Pool
	participantListPool     sync.Pool
	webRTCTokenResponsePool sync.Pool
}

func NewHandlers(voiceService VoiceServiceInterface) *Handlers {
	h := &Handlers{
		voiceService: voiceService,
		logger:       GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.channelPool = sync.Pool{
		New: func() interface{} {
			return &api.VoiceChannel{}
		},
	}
	h.channelDetailPool = sync.Pool{
		New: func() interface{} {
			return &api.ChannelDetailResponse{}
		},
	}
	h.channelListPool = sync.Pool{
		New: func() interface{} {
			return &api.ChannelListResponse{}
		},
	}
	h.participantListPool = sync.Pool{
		New: func() interface{} {
			return &api.ParticipantListResponse{}
		},
	}
	h.webRTCTokenResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.WebRTCTokenResponse{}
		},
	}

	return h
}

func (h *Handlers) CreateChannel(ctx context.Context, req *api.CreateChannelRequest) (api.CreateChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	internalReq := &models.CreateChannelRequest{
		CharacterID:   req.PlayerID,
		Type:          models.VoiceChannelType(req.ChannelType),
		Name:          req.Name,
		MaxMembers:    50,
		QualityPreset: "medium",
	}

	if req.MaxParticipants.Set {
		internalReq.MaxMembers = req.MaxParticipants.Value
	}

	channel, err := h.voiceService.CreateChannel(ctx, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create channel")
		return &api.CreateChannelInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to create channel",
		}, nil
	}

	apiChannel := convertVoiceChannelToAPI(channel)
	return &apiChannel, nil
}

func (h *Handlers) GetChannel(ctx context.Context, params api.GetChannelParams) (api.GetChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	channel, err := h.voiceService.GetChannel(ctx, params.ChannelID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get channel")
		return &api.GetChannelInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get channel",
		}, nil
	}

	if channel == nil {
		return &api.GetChannelNotFound{
			Error:   "Not Found",
			Message: "channel not found",
		}, nil
	}

	apiChannel := convertVoiceChannelToAPI(channel)
	return &apiChannel, nil
}

func (h *Handlers) ListChannels(ctx context.Context, params api.ListChannelsParams) (api.ListChannelsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var channelType *models.VoiceChannelType
	if params.Type.Set {
		ct := models.VoiceChannelType(params.Type.Value)
		channelType = &ct
	}

	var ownerID *uuid.UUID
	if params.OwnerID.Set {
		ownerID = &params.OwnerID.Value
	}

	limit := 50
	if params.Limit.Set {
		limit = params.Limit.Value
	}

	offset := 0
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	response, err := h.voiceService.ListChannels(ctx, channelType, ownerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list channels")
		return &api.ListChannelsInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to list channels",
		}, nil
	}

	apiChannels := make([]api.VoiceChannel, len(response.Channels))
	for i, ch := range response.Channels {
		apiChannels[i] = convertVoiceChannelToAPI(&ch)
	}

	return &api.ChannelListResponse{
		Channels: apiChannels,
		Total:    api.NewOptInt(response.Total),
	}, nil
}

func (h *Handlers) GetChannelDetail(ctx context.Context, params api.GetChannelDetailParams) (api.GetChannelDetailRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.voiceService.GetChannelDetail(ctx, params.ChannelID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get channel detail")
		return &api.GetChannelDetailInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get channel detail",
		}, nil
	}

	if response == nil {
		return &api.GetChannelDetailNotFound{
			Error:   "Not Found",
			Message: "channel not found",
		}, nil
	}

	apiParticipants := make([]api.VoiceParticipant, len(response.Participants))
	for i, p := range response.Participants {
		apiParticipants[i] = convertParticipantToAPI(&p)
	}

	return &api.ChannelDetailResponse{
		Channel:      api.NewOptVoiceChannel(convertVoiceChannelToAPI(response.Channel)),
		Participants: apiParticipants,
	}, nil
}

func (h *Handlers) JoinChannel(ctx context.Context, req *api.JoinChannelRequest, params api.JoinChannelParams) (api.JoinChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	position := make(map[string]interface{})
	if req.PositionX.Set {
		position["x"] = req.PositionX.Value
	}
	if req.PositionY.Set {
		position["y"] = req.PositionY.Value
	}
	if req.PositionZ.Set {
		position["z"] = req.PositionZ.Value
	}

	internalReq := &models.JoinChannelRequest{
		ChannelID:   params.ChannelID,
		CharacterID: req.PlayerID,
		Position:    position,
	}

	response, err := h.voiceService.JoinChannel(ctx, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to join channel")
		if err.Error() == "channel is full" {
			return &api.JoinChannelConflict{
				Error:   "Conflict",
				Message: "channel is full",
			}, nil
		}
		return &api.JoinChannelInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to join channel",
		}, nil
	}

	return &api.WebRTCTokenResponse{
		Token:     api.NewOptString(response.Token),
		ServerURL: api.NewOptString(response.ServerURL),
		ExpiresAt: api.NewOptDateTime(response.ExpiresAt),
	}, nil
}

func (h *Handlers) LeaveChannel(ctx context.Context, req *api.LeaveChannelRequest, params api.LeaveChannelParams) (api.LeaveChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	internalReq := &models.LeaveChannelRequest{
		ChannelID:   params.ChannelID,
		CharacterID: req.PlayerID,
	}

	err := h.voiceService.LeaveChannel(ctx, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to leave channel")
		return &api.LeaveChannelInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to leave channel",
		}, nil
	}

	return &api.LeaveChannelOK{}, nil
}

func (h *Handlers) GetChannelParticipants(ctx context.Context, params api.GetChannelParticipantsParams) (api.GetChannelParticipantsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.voiceService.GetChannelParticipants(ctx, params.ChannelID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get participants")
		return &api.GetChannelParticipantsInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get participants",
		}, nil
	}

	apiParticipants := make([]api.VoiceParticipant, len(response.Participants))
	for i, p := range response.Participants {
		apiParticipants[i] = convertParticipantToAPI(&p)
	}

	return &api.ParticipantListResponse{
		Participants: apiParticipants,
		Total:        api.NewOptInt(response.Total),
	}, nil
}

func (h *Handlers) UpdateParticipantStatus(ctx context.Context, req *api.UpdateParticipantStatusRequest, params api.UpdateParticipantStatusParams) (api.UpdateParticipantStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	status := models.ParticipantStatusConnected
	if req.IsMuted.Set && req.IsMuted.Value {
		status = models.ParticipantStatusMuted
	} else if req.IsDeafened.Set && req.IsDeafened.Value {
		status = models.ParticipantStatusDeafened
	}

	internalReq := &models.UpdateParticipantStatusRequest{
		ChannelID:   params.ChannelID,
		CharacterID: params.PlayerID,
		Status:      status,
	}

	err := h.voiceService.UpdateParticipantStatus(ctx, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update participant status")
		return &api.UpdateParticipantStatusInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to update participant status",
		}, nil
	}

	return &api.UpdateParticipantStatusOK{}, nil
}

func (h *Handlers) UpdateParticipantPosition(ctx context.Context, req *api.UpdateParticipantPositionRequest, params api.UpdateParticipantPositionParams) (api.UpdateParticipantPositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	internalReq := &models.UpdateParticipantPositionRequest{
		ChannelID:   params.ChannelID,
		CharacterID: params.PlayerID,
		Position: map[string]interface{}{
			"x": req.PositionX,
			"y": req.PositionY,
			"z": req.PositionZ,
		},
	}

	err := h.voiceService.UpdateParticipantPosition(ctx, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update participant position")
		return &api.UpdateParticipantPositionInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to update participant position",
		}, nil
	}

	return &api.UpdateParticipantPositionOK{}, nil
}

func (h *Handlers) GetWebRTCToken(ctx context.Context) (api.GetWebRTCTokenRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement WebRTC token generation
	return &api.GetWebRTCTokenInternalServerError{
		Error:   "Internal Server Error",
		Message: "not implemented",
	}, nil
}

func (h *Handlers) ConnectWebRTC(ctx context.Context) (api.ConnectWebRTCRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement WebRTC connection
	return &api.ConnectWebRTCInternalServerError{
		Error:   "Internal Server Error",
		Message: "not implemented",
	}, nil
}

func (h *Handlers) DisconnectWebRTC(ctx context.Context) (api.DisconnectWebRTCRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement WebRTC disconnection
	return &api.DisconnectWebRTCInternalServerError{
		Error:   "Internal Server Error",
		Message: "not implemented",
	}, nil
}
