package server

// API Request/Response models for Voice Chat Service

// Voice channel models
type VoiceChannelSummary struct {
	ChannelID        string `json:"channel_id"`
	Name             string `json:"name"`
	Type             string `json:"type"`
	OwnerID          string `json:"owner_id"`
	ParticipantCount int    `json:"participant_count"`
	MaxParticipants  int    `json:"max_participants"`
	IsPublic         bool   `json:"is_public"`
	Status           string `json:"status"`
	CreatedAt        int64  `json:"created_at"`
}

// Request models
type CreateChannelRequest struct {
	Name            string          `json:"name"`
	Type            string          `json:"type"`
	MaxParticipants int             `json:"max_participants"`
	IsPublic        bool            `json:"is_public"`
	Settings        ChannelSettings `json:"settings"`
}

type UpdateChannelRequest struct {
	Name            string          `json:"name,omitempty"`
	MaxParticipants int             `json:"max_participants,omitempty"`
	IsPublic        bool            `json:"is_public,omitempty"`
	Settings        ChannelSettings `json:"settings,omitempty"`
}

type JoinChannelRequest struct {
	UserID      string `json:"user_id"`
	MuteOnJoin  bool   `json:"mute_on_join,omitempty"`
	DeafenOnJoin bool  `json:"deafen_on_join,omitempty"`
}

// Response models
type CreateChannelResponse struct {
	ChannelID    string   `json:"channel_id"`
	Name         string   `json:"name"`
	Type         string   `json:"type"`
	WebsocketURL string   `json:"websocket_url"`
	StunServers  []string `json:"stun_servers"`
	TurnServers  []string `json:"turn_servers"`
	CreatedAt    int64    `json:"created_at"`
}

type ListChannelsResponse struct {
	Channels   []*VoiceChannelSummary `json:"channels"`
	TotalCount int                    `json:"total_count"`
}

type GetChannelResponse struct {
	Channel      *VoiceChannel        `json:"channel"`
	Participants []*ChannelParticipant `json:"participants"`
}

type UpdateChannelResponse struct {
	ChannelID     string   `json:"channel_id"`
	UpdatedFields []string `json:"updated_fields"`
	UpdatedAt     int64    `json:"updated_at"`
}

type JoinChannelResponse struct {
	ChannelID     string `json:"channel_id"`
	UserID        string `json:"user_id"`
	WebsocketURL  string `json:"websocket_url"`
	SessionToken  string `json:"session_token"`
	JoinedAt      int64  `json:"joined_at"`
}

type LeaveChannelResponse struct {
	ChannelID string `json:"channel_id"`
	UserID    string `json:"user_id"`
	LeftAt    int64  `json:"left_at"`
}

// Audio streaming models
type StartAudioStreamRequest struct {
	ChannelID    string      `json:"channel_id"`
	UserID       string      `json:"user_id"`
	AudioConfig  AudioConfig `json:"audio_config"`
}

type UpdateProximityRequest struct {
	UserID    string         `json:"user_id"`
	ChannelID string         `json:"channel_id"`
	Position  PlayerLocation `json:"position"`
}

// Response models
type StartAudioStreamResponse struct {
	StreamID      string   `json:"stream_id"`
	ChannelID     string   `json:"channel_id"`
	UserID        string   `json:"user_id"`
	WebsocketURL  string   `json:"websocket_url"`
	ICEServers    []string `json:"ice_servers"`
	SessionToken  string   `json:"session_token"`
	StartedAt     int64    `json:"started_at"`
}

type UpdateProximityResponse struct {
	UserID       string         `json:"user_id"`
	ChannelID    string         `json:"channel_id"`
	AudibleUsers []*AudibleUser `json:"audible_users"`
	UpdatedAt    int64          `json:"updated_at"`
}

// Text-to-speech models
type SynthesizeSpeechRequest struct {
	Text    string  `json:"text"`
	Voice   string  `json:"voice,omitempty"`
	Language string `json:"language,omitempty"`
	Speed   float64 `json:"speed,omitempty"`
	Pitch   float64 `json:"pitch,omitempty"`
}

type SynthesizeSpeechResponse struct {
	AudioID     string  `json:"audio_id"`
	Text        string  `json:"text"`
	AudioURL    string  `json:"audio_url"`
	Duration    float64 `json:"duration"`
	SizeBytes   int     `json:"size_bytes"`
	GeneratedAt int64   `json:"generated_at"`
}

// Moderation models
type ReportVoiceAbuseRequest struct {
	ReportedUserID string   `json:"reported_user_id"`
	ChannelID      string   `json:"channel_id"`
	AbuseType      string   `json:"abuse_type"`
	Description    string   `json:"description,omitempty"`
	EvidenceURLs   []string `json:"evidence_urls,omitempty"`
}

type ReportVoiceAbuseResponse struct {
	ReportID        string `json:"report_id"`
	ReportedUserID  string `json:"reported_user_id"`
	ChannelID       string `json:"channel_id"`
	Status          string `json:"status"`
	SubmittedAt     int64  `json:"submitted_at"`
}

// Helper models
type ChannelParticipant struct {
	UserID      string `json:"user_id"`
	DisplayName string `json:"display_name"`
	IsMuted     bool   `json:"is_muted"`
	IsDeafened  bool   `json:"is_deafened"`
	JoinedAt    int64  `json:"joined_at"`
}

type AudibleUser struct {
	UserID  string  `json:"user_id"`
	Distance float64 `json:"distance"`
	Volume   float64 `json:"volume"`
}

// Error response model
type ErrorResponse struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Details string `json:"details,omitempty"`
}
