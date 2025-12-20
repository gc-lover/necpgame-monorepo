// Package server Issue: ogen migration
package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/pkg/api"
	"github.com/google/uuid"
)

func convertVoiceChannelToAPI(ch *models.VoiceChannel) api.VoiceChannel {
	if ch == nil {
		return api.VoiceChannel{}
	}

	apiChannel := api.VoiceChannel{
		ID:              api.NewOptUUID(ch.ID),
		ChannelType:     api.NewOptVoiceChannelChannelType(convertChannelTypeToAPI(ch.Type)),
		Name:            api.NewOptString(ch.Name),
		MaxParticipants: api.NewOptInt(ch.MaxMembers),
		CreatedAt:       api.NewOptDateTime(ch.CreatedAt),
		UpdatedAt:       api.NewOptDateTime(ch.UpdatedAt),
		IsActive:        api.NewOptBool(true),
	}

	if ch.OwnerID != (uuid.UUID{}) {
		apiChannel.OwnerID = api.NewOptNilUUID(ch.OwnerID)
	}

	return apiChannel
}

func convertParticipantToAPI(p *models.VoiceParticipant) api.VoiceParticipant {
	if p == nil {
		return api.VoiceParticipant{}
	}

	apiParticipant := api.VoiceParticipant{
		ID:        api.NewOptUUID(p.ID),
		ChannelID: api.NewOptUUID(p.ChannelID),
		PlayerID:  api.NewOptUUID(p.CharacterID),
		JoinedAt:  api.NewOptDateTime(p.JoinedAt),
	}

	if p.WebRTCToken != "" {
		apiParticipant.WebrtcToken = api.NewOptString(p.WebRTCToken)
	}

	// Convert status to boolean flags
	switch p.Status {
	case models.ParticipantStatusMuted:
		apiParticipant.IsMuted = api.NewOptBool(true)
	case models.ParticipantStatusDeafened:
		apiParticipant.IsDeafened = api.NewOptBool(true)
	case models.ParticipantStatusSpeaking:
		apiParticipant.IsSpeaking = api.NewOptBool(true)
	}

	if p.Position != nil {
		if x, ok := p.Position["x"].(float64); ok {
			apiParticipant.PositionX = api.NewOptNilFloat32(float32(x))
		}
		if y, ok := p.Position["y"].(float64); ok {
			apiParticipant.PositionY = api.NewOptNilFloat32(float32(y))
		}
		if z, ok := p.Position["z"].(float64); ok {
			apiParticipant.PositionZ = api.NewOptNilFloat32(float32(z))
		}
	}

	if !p.UpdatedAt.IsZero() {
		apiParticipant.LastActivityAt = api.NewOptDateTime(p.UpdatedAt)
	}

	return apiParticipant
}

func convertChannelTypeToAPI(t models.VoiceChannelType) api.VoiceChannelChannelType {
	switch t {
	case models.VoiceChannelTypeParty:
		return api.VoiceChannelChannelTypePARTY
	case models.VoiceChannelTypeGuild:
		return api.VoiceChannelChannelTypeGUILD
	case models.VoiceChannelTypeRaid:
		return api.VoiceChannelChannelTypeRAID
	case models.VoiceChannelTypeProximity:
		return api.VoiceChannelChannelTypePROXIMITY
	default:
		return ""
	}
}
