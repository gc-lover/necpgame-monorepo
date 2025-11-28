package models

import (
	"time"

	"github.com/google/uuid"
)

type VoiceChannelType string

const (
	VoiceChannelTypeParty     VoiceChannelType = "party"
	VoiceChannelTypeGuild     VoiceChannelType = "guild"
	VoiceChannelTypeRaid      VoiceChannelType = "raid"
	VoiceChannelTypeProximity VoiceChannelType = "proximity"
)

type VoiceChannel struct {
	ID          uuid.UUID              `json:"id" db:"id"`
	Type        VoiceChannelType       `json:"type" db:"type"`
	OwnerID     uuid.UUID              `json:"owner_id" db:"owner_id"`
	OwnerType   string                 `json:"owner_type" db:"owner_type"`
	Name        string                 `json:"name" db:"name"`
	MaxMembers  int                    `json:"max_members" db:"max_members"`
	QualityPreset string               `json:"quality_preset" db:"quality_preset"`
	Settings    map[string]interface{} `json:"settings" db:"settings"`
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
}

type ParticipantStatus string

const (
	ParticipantStatusConnected ParticipantStatus = "connected"
	ParticipantStatusMuted     ParticipantStatus = "muted"
	ParticipantStatusDeafened  ParticipantStatus = "deafened"
	ParticipantStatusSpeaking  ParticipantStatus = "speaking"
)

type VoiceParticipant struct {
	ID          uuid.UUID              `json:"id" db:"id"`
	ChannelID   uuid.UUID              `json:"channel_id" db:"channel_id"`
	CharacterID uuid.UUID              `json:"character_id" db:"character_id"`
	Status      ParticipantStatus      `json:"status" db:"status"`
	WebRTCToken string                 `json:"webrtc_token" db:"webrtc_token"`
	Position    map[string]interface{} `json:"position" db:"position"`
	Stats       map[string]interface{} `json:"stats" db:"stats"`
	JoinedAt    time.Time              `json:"joined_at" db:"joined_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
}

type CreateChannelRequest struct {
	CharacterID   uuid.UUID        `json:"character_id"`
	Type          VoiceChannelType `json:"type"`
	Name          string           `json:"name"`
	MaxMembers    int              `json:"max_members"`
	QualityPreset string           `json:"quality_preset"`
	Settings      map[string]interface{} `json:"settings"`
}

type JoinChannelRequest struct {
	CharacterID uuid.UUID              `json:"character_id"`
	ChannelID   uuid.UUID              `json:"channel_id"`
	Position    map[string]interface{} `json:"position,omitempty"`
}

type LeaveChannelRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	ChannelID   uuid.UUID `json:"channel_id"`
}

type UpdateParticipantStatusRequest struct {
	CharacterID uuid.UUID         `json:"character_id"`
	ChannelID   uuid.UUID         `json:"channel_id"`
	Status      ParticipantStatus `json:"status"`
}

type UpdateParticipantPositionRequest struct {
	CharacterID uuid.UUID              `json:"character_id"`
	ChannelID   uuid.UUID              `json:"channel_id"`
	Position    map[string]interface{} `json:"position"`
}

type ChannelListResponse struct {
	Channels []VoiceChannel `json:"channels"`
	Total    int            `json:"total"`
}

type ParticipantListResponse struct {
	Participants []VoiceParticipant `json:"participants"`
	Total        int                `json:"total"`
}

type ChannelDetailResponse struct {
	Channel     *VoiceChannel        `json:"channel"`
	Participants []VoiceParticipant `json:"participants"`
}

type WebRTCTokenResponse struct {
	Token     string    `json:"token"`
	ServerURL string    `json:"server_url"`
	ExpiresAt time.Time `json:"expires_at"`
}

type SubchannelType string

const (
	SubchannelTypeMain      SubchannelType = "main"
	SubchannelTypeRoleBased SubchannelType = "role_based"
	SubchannelTypeCommander SubchannelType = "commander"
	SubchannelTypeParty     SubchannelType = "party"
)

type Subchannel struct {
	SubchannelID      uuid.UUID `json:"subchannel_id" db:"subchannel_id"`
	LobbyID           uuid.UUID `json:"lobby_id" db:"lobby_id"`
	Name              string    `json:"name" db:"name"`
	Description       *string   `json:"description,omitempty" db:"description"`
	SubchannelType    SubchannelType `json:"subchannel_type" db:"subchannel_type"`
	MaxParticipants   *int      `json:"max_participants,omitempty" db:"max_participants"`
	CurrentParticipants int     `json:"current_participants" db:"current_participants"`
	RequiredRole      *string   `json:"required_role,omitempty" db:"required_role"`
	IsLocked          bool      `json:"is_locked" db:"is_locked"`
	CreatedAt         time.Time `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time `json:"updated_at" db:"updated_at"`
}

type CreateSubchannelRequest struct {
	Name            string        `json:"name"`
	Description     *string       `json:"description,omitempty"`
	SubchannelType  SubchannelType `json:"subchannel_type"`
	MaxParticipants *int          `json:"max_participants,omitempty"`
	RequiredRole    *string       `json:"required_role,omitempty"`
	IsLocked        bool          `json:"is_locked"`
}

type UpdateSubchannelRequest struct {
	Name            *string `json:"name,omitempty"`
	Description     *string `json:"description,omitempty"`
	MaxParticipants *int    `json:"max_participants,omitempty"`
	RequiredRole    *string `json:"required_role,omitempty"`
	IsLocked        *bool   `json:"is_locked,omitempty"`
}

type SubchannelListResponse struct {
	LobbyID     uuid.UUID   `json:"lobby_id"`
	Subchannels []Subchannel `json:"subchannels"`
	TotalCount  int         `json:"total_count"`
}

type MoveToSubchannelRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Force       bool      `json:"force"`
}

type MoveToSubchannelResponse struct {
	SubchannelID uuid.UUID `json:"subchannel_id"`
	CharacterID  uuid.UUID `json:"character_id"`
	MovedAt      time.Time `json:"moved_at"`
}

type SubchannelParticipant struct {
	CharacterID  uuid.UUID `json:"character_id"`
	CharacterName string   `json:"character_name"`
	Role         *string   `json:"role,omitempty"`
	JoinedAt     time.Time `json:"joined_at"`
}

type SubchannelParticipantsResponse struct {
	SubchannelID uuid.UUID              `json:"subchannel_id"`
	Participants []SubchannelParticipant `json:"participants"`
	TotalCount   int                    `json:"total_count"`
}

