package models

import (
	"time"
)

// VoiceChannel represents a voice channel
type VoiceChannel struct {
	ID               string                     `json:"id" db:"id"`
	Name             string                     `json:"name" db:"name"`
	Type             string                     `json:"type" db:"type"` // guild, party, global, private
	MaxParticipants  int                        `json:"max_participants" db:"max_participants"`
	CurrentParticipants int                     `json:"current_participants" db:"current_participants"`
	Description      string                     `json:"description,omitempty" db:"description"`
	CreatedAt        time.Time                  `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time                  `json:"updated_at" db:"updated_at"`
	Status           string                     `json:"status" db:"status"` // active, inactive, full, closed
	IsActive         bool                       `json:"is_active" db:"is_active"`
	QualitySettings  VoiceQualitySettings       `json:"quality_settings" db:"quality_settings"`
}

// VoiceQualitySettings represents voice quality configuration
type VoiceQualitySettings struct {
	Bitrate         int  `json:"bitrate" db:"bitrate"`
	SampleRate      int  `json:"sample_rate" db:"sample_rate"`
	Channels        int  `json:"channels" db:"channels"`
	EchoCancellation bool `json:"echo_cancellation" db:"echo_cancellation"`
	NoiseSuppression bool `json:"noise_suppression" db:"noise_suppression"`
}

// JoinChannelRequest represents a request to join a voice channel
type JoinChannelRequest struct {
	UserID            string                `json:"user_id"`
	ClientCapabilities WebRTCCapabilities   `json:"client_capabilities"`
	PreferredRegion   string                `json:"preferred_region,omitempty"`
}

// WebRTCCapabilities represents client WebRTC capabilities
type WebRTCCapabilities struct {
	WebRTCSupport     bool `json:"webrtc_support"`
	DTLSSupport       bool `json:"dtls_support"`
	ICECandidates     bool `json:"ice_candidates"`
	MaxBitrate        int  `json:"max_bitrate"`
}

// JoinChannelResponse represents the response to a join request
type JoinChannelResponse struct {
	Channel      VoiceChannel       `json:"channel"`
	ICEServers   []ICEServer        `json:"ice_servers"`
	SessionToken string             `json:"session_token"`
	Participants []VoiceParticipant `json:"participants"`
}

// ICEServer represents a STUN/TURN server configuration
type ICEServer struct {
	URLs       []string `json:"urls"`
	Username   string   `json:"username,omitempty"`
	Credential string   `json:"credential,omitempty"`
}

// VoiceParticipant represents a participant in a voice channel
type VoiceParticipant struct {
	UserID           string    `json:"user_id"`
	DisplayName      string    `json:"display_name,omitempty"`
	JoinedAt         time.Time `json:"joined_at"`
	IsMuted          bool      `json:"is_muted"`
	ConnectionQuality string   `json:"connection_quality"`
}

// SignalingMessage represents a WebRTC signaling message
type SignalingMessage struct {
	Type        string      `json:"type"` // offer, answer, ice_candidate, hangup
	SenderID    string      `json:"sender_id"`
	TargetID    string      `json:"target_id"`
	SDP         string      `json:"sdp,omitempty"`
	ICECandidate *ICECandidate `json:"ice_candidate,omitempty"`
	Timestamp   time.Time   `json:"timestamp"`
}

// ICECandidate represents a WebRTC ICE candidate
type ICECandidate struct {
	Candidate     string `json:"candidate"`
	SDPMid        string `json:"sdp_mid"`
	SDPMLineIndex int    `json:"sdp_mline_index"`
}

// SignalingResponse represents the response to a signaling message
type SignalingResponse struct {
	Success    bool      `json:"success"`
	MessageID  string    `json:"message_id"`
	DeliveredAt time.Time `json:"delivered_at"`
}

// LeaveChannelResponse represents the response to leaving a channel
type LeaveChannelResponse struct {
	Success          bool    `json:"success"`
	SessionDuration  int     `json:"session_duration"`
	QualityScore     float64 `json:"quality_score"`
}

// VoiceQualityReport represents a voice quality report from client
type VoiceQualityReport struct {
	UserID   string               `json:"user_id"`
	Metrics  VoiceQualityMetrics  `json:"metrics"`
	Timestamp time.Time           `json:"timestamp"`
}

// VoiceQualityMetrics represents voice quality measurements
type VoiceQualityMetrics struct {
	LatencyMs           float64 `json:"latency_ms"`
	PacketLossPercent   float64 `json:"packet_loss_percent"`
	JitterMs            float64 `json:"jitter_ms"`
	BitrateBps          int     `json:"bitrate_bps"`
	VolumeLevel         float64 `json:"volume_level"`
}

// VoiceQualityResponse represents the response to a quality report
type VoiceQualityResponse struct {
	Acknowledged       bool                  `json:"acknowledged"`
	RecommendedSettings VoiceQualitySettings `json:"recommended_settings,omitempty"`
	NextReportInterval  int                   `json:"next_report_interval"`
}

// CreateVoiceChannelRequest represents a request to create a voice channel
type CreateVoiceChannelRequest struct {
	Name            string `json:"name"`
	Type            string `json:"type"`
	MaxParticipants int    `json:"max_participants,omitempty"`
	Description     string `json:"description,omitempty"`
}

// UpdateVoiceChannelRequest represents a request to update a voice channel
type UpdateVoiceChannelRequest struct {
	Name            *string `json:"name,omitempty"`
	MaxParticipants *int    `json:"max_participants,omitempty"`
	Description     *string `json:"description,omitempty"`
	Status          *string `json:"status,omitempty"`
}

// VoiceChannelListResponse represents a paginated list of voice channels
type VoiceChannelListResponse struct {
	Channels []VoiceChannel `json:"channels"`
	Total    int            `json:"total"`
	HasMore  bool           `json:"has_more"`
}

// VoiceChannelResponse represents a single voice channel response
type VoiceChannelResponse struct {
	Channel VoiceChannel `json:"channel"`
}
