// Issue: #2211 - WebRTC Signaling Service for Voice Chat
// Models for Voice Chat Service - Real-time voice communication system

package models

import (
	"time"
)

// VoiceRoom represents a voice chat room/channel
type VoiceRoom struct {
	ID          string                 `json:"id" db:"id"`
	Name        string                 `json:"name" db:"name"`
	Description string                 `json:"description" db:"description"`
	RoomType    string                 `json:"room_type" db:"room_type"` // "guild", "party", "match", "tournament", "public", "private"
	GameID      string                 `json:"game_id" db:"game_id"`     // associated game/match ID
	GuildID     string                 `json:"guild_id" db:"guild_id"`   // for guild voice rooms
	OwnerID     string                 `json:"owner_id" db:"owner_id"`   // room creator/owner
	MaxParticipants int                `json:"max_participants" db:"max_participants"`
	CurrentParticipants int            `json:"current_participants" db:"current_participants"`
	IsActive    bool                   `json:"is_active" db:"is_active"`
	IsLocked    bool                   `json:"is_locked" db:"is_locked"`
	Password    string                 `json:"password,omitempty" db:"password"` // for private rooms
	Bitrate     int                    `json:"bitrate" db:"bitrate"`             // audio quality in kbps
	SampleRate  int                    `json:"sample_rate" db:"sample_rate"`     // audio sample rate
	Channels    int                    `json:"channels" db:"channels"`           // 1=mono, 2=stereo
	NoiseSuppression bool              `json:"noise_suppression" db:"noise_suppression"`
	EchoCancellation bool              `json:"echo_cancellation" db:"echo_cancellation"`
	Settings    map[string]interface{} `json:"settings" db:"settings"` // JSON additional settings
	CreatedAt   time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at" db:"updated_at"`
}

// VoiceParticipant represents a participant in a voice room
type VoiceParticipant struct {
	ID             string    `json:"id" db:"id"`
	RoomID         string    `json:"room_id" db:"room_id"`
	UserID         string    `json:"user_id" db:"user_id"`
	Username       string    `json:"username" db:"username"`
	IsMuted        bool      `json:"is_muted" db:"is_muted"`
	IsDeafened     bool      `json:"is_deafened" db:"is_deafened"`
	IsSpeaking     bool      `json:"is_speaking" db:"is_speaking"`
	VolumeLevel    float64   `json:"volume_level" db:"volume_level"`       // 0.0-1.0
	AudioQuality   int       `json:"audio_quality" db:"audio_quality"`     // 1-5 quality rating
	PingLatency    int       `json:"ping_latency" db:"ping_latency"`       // milliseconds
	PacketLoss     float64   `json:"packet_loss" db:"packet_loss"`         // 0.0-1.0
	JoinedAt       time.Time `json:"joined_at" db:"joined_at"`
	LastActivity   time.Time `json:"last_activity" db:"last_activity"`
	Status         string    `json:"status" db:"status"`                   // "connecting", "connected", "disconnected", "kicked", "banned"
	Permissions    []string  `json:"permissions" db:"permissions"`         // JSON array: ["speak", "mute", "kick", "manage"]
	Metadata       map[string]interface{} `json:"metadata" db:"metadata"` // JSON additional data
}

// WebRTCSession represents a WebRTC peer-to-peer session
type WebRTCSession struct {
	ID              string    `json:"id" db:"id"`
	RoomID          string    `json:"room_id" db:"room_id"`
	InitiatorID     string    `json:"initiator_id" db:"initiator_id"`
	ReceiverID      string    `json:"receiver_id" db:"receiver_id"`
	SessionType     string    `json:"session_type" db:"session_type"`     // "offer", "answer", "ice_candidate"
	OfferSDP        string    `json:"offer_sdp,omitempty" db:"offer_sdp"`
	AnswerSDP       string    `json:"answer_sdp,omitempty" db:"answer_sdp"`
	IceCandidates   []string  `json:"ice_candidates" db:"ice_candidates"` // JSON array
	SessionState    string    `json:"session_state" db:"session_state"`   // "negotiating", "connected", "failed", "closed"
	CreatedAt       time.Time `json:"created_at" db:"created_at"`
	EstablishedAt   *time.Time `json:"established_at" db:"established_at"`
	ClosedAt        *time.Time `json:"closed_at" db:"closed_at"`
	ConnectionQuality int      `json:"connection_quality" db:"connection_quality"` // 1-5
	DataTransferred  int64     `json:"data_transferred" db:"data_transferred"`     // bytes
}

// SignalingMessage represents a WebRTC signaling message
type SignalingMessage struct {
	Type      string                 `json:"type"`      // "offer", "answer", "ice-candidate", "hangup"
	FromUser  string                 `json:"from_user"`
	ToUser    string                 `json:"to_user"`
	RoomID    string                 `json:"room_id"`
	Payload   map[string]interface{} `json:"payload"`
	Timestamp time.Time              `json:"timestamp"`
	MessageID string                 `json:"message_id"`
}

// VoiceMessage represents a voice/text message in a room
type VoiceMessage struct {
	ID        string                 `json:"id" db:"id"`
	RoomID    string                 `json:"room_id" db:"room_id"`
	UserID    string                 `json:"user_id" db:"user_id"`
	Username  string                 `json:"username" db:"username"`
	MessageType string               `json:"message_type" db:"message_type"` // "voice", "text", "system"
	Content   string                 `json:"content,omitempty" db:"content"` // text content
	AudioData []byte                 `json:"-" db:"audio_data"`              // binary audio data (not in JSON)
	Duration  int                    `json:"duration,omitempty" db:"duration"` // audio duration in seconds
	SizeBytes int64                  `json:"size_bytes" db:"size_bytes"`
	IsPrivate bool                   `json:"is_private" db:"is_private"`
	Timestamp time.Time              `json:"timestamp" db:"timestamp"`
	Metadata  map[string]interface{} `json:"metadata" db:"metadata"` // JSON
}

// VoiceRoomSettings represents configurable room settings
type VoiceRoomSettings struct {
	RoomID           string `json:"room_id"`
	Bitrate          int    `json:"bitrate"`           // kbps
	SampleRate       int    `json:"sample_rate"`       // Hz
	Channels         int    `json:"channels"`          // 1 or 2
	NoiseSuppression bool   `json:"noise_suppression"`
	EchoCancellation bool   `json:"echo_cancellation"`
	VoiceActivityDetection bool `json:"voice_activity_detection"`
	AutoGainControl  bool   `json:"auto_gain_control"`
	MaxParticipants  int    `json:"max_participants"`
	AllowRecording   bool   `json:"allow_recording"`
	AllowTextChat    bool   `json:"allow_text_chat"`
}

// RoomJoinRequest represents a request to join a voice room
type RoomJoinRequest struct {
	RoomID     string `json:"room_id"`
	UserID     string `json:"user_id"`
	Username   string `json:"username"`
	Password   string `json:"password,omitempty"` // for private rooms
	UserAgent  string `json:"user_agent"`
	ClientInfo map[string]interface{} `json:"client_info"`
}

// RoomJoinResponse contains join confirmation and room info
type RoomJoinResponse struct {
	Success       bool               `json:"success"`
	Room          *VoiceRoom         `json:"room,omitempty"`
	ParticipantID string             `json:"participant_id,omitempty"`
	Token         string             `json:"token,omitempty"`         // access token
	WebRTCConfig WebRTCConfiguration `json:"webrtc_config,omitempty"`
	Error         string             `json:"error,omitempty"`
}

// WebRTCConfiguration contains WebRTC connection settings
type WebRTCConfiguration struct {
	IceServers     []IceServer `json:"ice_servers"`
	PeerConnection PeerConnectionConfig `json:"peer_connection"`
	SignalingURL   string      `json:"signaling_url"`
	TurnServer     string      `json:"turn_server,omitempty"`
	StunServer     string      `json:"stun_server,omitempty"`
}

// IceServer represents a WebRTC ICE server configuration
type IceServer struct {
	URLs       []string `json:"urls"`
	Username   string   `json:"username,omitempty"`
	Credential string   `json:"credential,omitempty"`
}

// PeerConnectionConfig contains RTCPeerConnection settings
type PeerConnectionConfig struct {
	BundlePolicy          string `json:"bundle_policy,omitempty"`
	RtcpMuxPolicy         string `json:"rtcp_mux_policy,omitempty"`
	IceCandidatePoolSize  int    `json:"ice_candidate_pool_size,omitempty"`
}

// RoomLeaveRequest represents a request to leave a voice room
type RoomLeaveRequest struct {
	RoomID        string `json:"room_id"`
	ParticipantID string `json:"participant_id"`
	Reason        string `json:"reason,omitempty"`
}

// SignalingRequest represents a WebRTC signaling request
type SignalingRequest struct {
	RoomID        string                 `json:"room_id"`
	ParticipantID string                 `json:"participant_id"`
	Message       SignalingMessage       `json:"message"`
	TargetUsers   []string               `json:"target_users,omitempty"` // for targeted messages
}

// SignalingResponse contains signaling confirmation
type SignalingResponse struct {
	Success      bool     `json:"success"`
	MessageID    string   `json:"message_id"`
	DeliveredTo  []string `json:"delivered_to,omitempty"`
	Error        string   `json:"error,omitempty"`
}

// VoiceQualityReport represents audio quality metrics
type VoiceQualityReport struct {
	RoomID          string    `json:"room_id" db:"room_id"`
	ParticipantID   string    `json:"participant_id" db:"participant_id"`
	ReportTime      time.Time `json:"report_time" db:"report_time"`
	AudioLevel      float64   `json:"audio_level" db:"audio_level"`           // 0.0-1.0
	JitterBuffer    int       `json:"jitter_buffer" db:"jitter_buffer"`       // milliseconds
	RoundTripTime   int       `json:"round_trip_time" db:"round_trip_time"`   // milliseconds
	PacketsLost     int       `json:"packets_lost" db:"packets_lost"`
	PacketsReceived int       `json:"packets_received" db:"packets_received"`
	Bitrate         int       `json:"bitrate" db:"bitrate"`                   // kbps
	SampleRate      int       `json:"sample_rate" db:"sample_rate"`           // Hz
	Codec           string    `json:"codec" db:"codec"`                       // "opus", "vp8", etc.
	NetworkType     string    `json:"network_type" db:"network_type"`         // "wifi", "cellular", "ethernet"
	DeviceInfo      map[string]interface{} `json:"device_info" db:"device_info"` // JSON
}

// VoiceRoomAnalytics provides analytics for voice rooms
type VoiceRoomAnalytics struct {
	RoomID              string    `json:"room_id"`
	TimeRange           string    `json:"time_range"`
	TotalParticipants   int64     `json:"total_participants"`
	AverageSessionTime  int64     `json:"average_session_time"`  // seconds
	PeakConcurrentUsers int64     `json:"peak_concurrent_users"`
	AudioQualityScore   float64   `json:"audio_quality_score"`   // 1-5 average
	PacketLossRate      float64   `json:"packet_loss_rate"`      // 0.0-1.0
	DisconnectRate      float64   `json:"disconnect_rate"`       // 0.0-1.0
	MostActiveHour      int       `json:"most_active_hour"`      // 0-23
	PlatformStats       map[string]int64 `json:"platform_stats"` // platform -> count
	ErrorCount          int64     `json:"error_count"`
	LastUpdated         time.Time `json:"last_updated"`
}

// VoiceBan represents a user ban from voice chat
type VoiceBan struct {
	ID          string    `json:"id" db:"id"`
	UserID      string    `json:"user_id" db:"user_id"`
	RoomID      string    `json:"room_id" db:"room_id"`           // specific room or "*" for global
	BannedBy    string    `json:"banned_by" db:"banned_by"`
	Reason      string    `json:"reason" db:"reason"`
	BanType     string    `json:"ban_type" db:"ban_type"`         // "mute", "kick", "ban"
	Duration    *time.Duration `json:"duration" db:"duration"`    // ban duration
	IsPermanent bool      `json:"is_permanent" db:"is_permanent"`
	BannedAt    time.Time `json:"banned_at" db:"banned_at"`
	ExpiresAt   *time.Time `json:"expires_at" db:"expires_at"`
}

// VoiceModerationAction represents a moderation action
type VoiceModerationAction struct {
	ID        string    `json:"id" db:"id"`
	RoomID    string    `json:"room_id" db:"room_id"`
	Moderator string    `json:"moderator" db:"moderator"`
	TargetUser string   `json:"target_user" db:"target_user"`
	Action    string    `json:"action" db:"action"`    // "mute", "unmute", "kick", "ban", "unban"
	Reason    string    `json:"reason" db:"reason"`
	Timestamp time.Time `json:"timestamp" db:"timestamp"`
	Duration  *time.Duration `json:"duration" db:"duration"` // for temporary actions
}

// VoicePushToTalk represents push-to-talk configuration
type VoicePushToTalk struct {
	UserID       string `json:"user_id"`
	RoomID       string `json:"room_id"`
	KeyBinding   string `json:"key_binding"`   // keyboard key or mouse button
	IsEnabled    bool   `json:"is_enabled"`
	VoiceActivationSensitivity float64 `json:"voice_activation_sensitivity"` // 0.0-1.0
	NoiseGateThreshold float64 `json:"noise_gate_threshold"` // 0.0-1.0
}

// VoiceRecording represents a voice recording session
type VoiceRecording struct {
	ID          string    `json:"id" db:"id"`
	RoomID      string    `json:"room_id" db:"room_id"`
	InitiatorID string    `json:"initiator_id" db:"initiator_id"`
	Title       string    `json:"title" db:"title"`
	Description string    `json:"description" db:"description"`
	Status      string    `json:"status" db:"status"`      // "recording", "paused", "stopped", "processing", "completed"
	StartedAt   time.Time `json:"started_at" db:"started_at"`
	EndedAt     *time.Time `json:"ended_at" db:"ended_at"`
	Duration    int       `json:"duration" db:"duration"`  // seconds
	FileSize    int64     `json:"file_size" db:"file_size"` // bytes
	Format      string    `json:"format" db:"format"`      // "wav", "mp3", "ogg"
	Quality     string    `json:"quality" db:"quality"`    // "low", "medium", "high"
	Participants []string `json:"participants" db:"participants"` // JSON array
	DownloadURL string    `json:"download_url" db:"download_url"`
	IsPublic    bool      `json:"is_public" db:"is_public"`
	Tags        []string  `json:"tags" db:"tags"`          // JSON array
	Metadata    map[string]interface{} `json:"metadata" db:"metadata"` // JSON
}

// VoiceTranscription represents transcribed voice chat
type VoiceTranscription struct {
	ID            string    `json:"id" db:"id"`
	RecordingID   string    `json:"recording_id" db:"recording_id"`
	UserID        string    `json:"user_id" db:"user_id"`
	Timestamp     time.Time `json:"timestamp" db:"timestamp"`
	Text          string    `json:"text" db:"text"`
	Confidence    float64   `json:"confidence" db:"confidence"` // 0.0-1.0
	Language      string    `json:"language" db:"language"`
	IsPartial     bool      `json:"is_partial" db:"is_partial"`
	StartTime     float64   `json:"start_time" db:"start_time"` // seconds into recording
	EndTime       float64   `json:"end_time" db:"end_time"`
	SpeakerLabel  string    `json:"speaker_label" db:"speaker_label"`
	Metadata      map[string]interface{} `json:"metadata" db:"metadata"` // JSON
}

// VoiceSpatialAudio represents 3D spatial audio configuration
type VoiceSpatialAudio struct {
	UserID     string  `json:"user_id"`
	RoomID     string  `json:"room_id"`
	IsEnabled  bool    `json:"is_enabled"`
	AttenuationModel string `json:"attenuation_model"` // "linear", "inverse", "exponential"
	RefDistance      float64 `json:"ref_distance"`      // reference distance
	MaxDistance      float64 `json:"max_distance"`      // maximum distance
	RollOffFactor    float64 `json:"roll_off_factor"`   // rolloff factor
	ConeInnerAngle   float64 `json:"cone_inner_angle"`  // inner cone angle in degrees
	ConeOuterAngle   float64 `json:"cone_outer_angle"`  // outer cone angle in degrees
	ConeOuterGain    float64 `json:"cone_outer_gain"`   // outer cone gain
	Position         [3]float64 `json:"position"`        // 3D position [x,y,z]
	Orientation      [3]float64 `json:"orientation"`     // 3D orientation [x,y,z]
}

// VoiceCrossPlatform represents cross-platform voice chat support
type VoiceCrossPlatform struct {
	UserID          string   `json:"user_id"`
	PrimaryPlatform string   `json:"primary_platform"`  // "pc", "console", "mobile"
	LinkedPlatforms []string `json:"linked_platforms"`  // other platforms
	CrossPlayEnabled bool    `json:"cross_play_enabled"`
	PlatformSettings map[string]interface{} `json:"platform_settings"` // platform-specific settings
}
