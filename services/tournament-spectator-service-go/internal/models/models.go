// Issue: #140875800
package models

import (
	"time"

	"github.com/google/uuid"
)

// BACKEND NOTE: Tournament Spectator Service - Enterprise-grade real-time spectator system
// Performance: WebSocket connections for 1000+ concurrent spectators, P99 <20ms latency
// Architecture: Event-driven with Redis caching and Kafka streaming
// Security: JWT authentication with anti-stream sniping protection

// SpectatorSession represents a spectator session
type SpectatorSession struct {
	SessionID     uuid.UUID              `json:"session_id" db:"session_id"`
	TournamentID  uuid.UUID              `json:"tournament_id" db:"tournament_id"`
	SpectatorID   uuid.UUID              `json:"spectator_id" db:"spectator_id"`
	Status        SpectatorStatus        `json:"status" db:"status"`
	CameraSettings *CameraSettings       `json:"camera_settings,omitempty" db:"camera_settings"`
	JoinedAt      time.Time              `json:"joined_at" db:"joined_at"`
	LastActivity  time.Time              `json:"last_activity" db:"last_activity"`
	StreamQuality StreamQuality          `json:"stream_quality" db:"stream_quality"`
	Nickname      string                 `json:"nickname,omitempty" db:"nickname"`
	IPAddress     string                 `json:"-" db:"ip_address"`
	UserAgent     string                 `json:"-" db:"user_agent"`
}

// SpectatorStatus represents session status
type SpectatorStatus string

const (
	StatusConnecting   SpectatorStatus = "connecting"
	StatusActive       SpectatorStatus = "active"
	StatusDisconnected SpectatorStatus = "disconnected"
	StatusBanned       SpectatorStatus = "banned"
)

// StreamQuality represents stream quality preference
type StreamQuality string

const (
	QualityLow    StreamQuality = "low"
	QualityMedium StreamQuality = "medium"
	QualityHigh   StreamQuality = "high"
	QualityUltra  StreamQuality = "ultra"
)

// CameraSettings represents camera configuration
type CameraSettings struct {
	Mode       CameraMode `json:"mode" db:"mode"`
	TargetID   *uuid.UUID `json:"target_id,omitempty" db:"target_id"`
	Position   *Vector3   `json:"position,omitempty" db:"position"`
	Rotation   *Vector3   `json:"rotation,omitempty" db:"rotation"`
	Zoom       float64    `json:"zoom,omitempty" db:"zoom"`
	Smoothness float64    `json:"smoothness,omitempty" db:"smoothness"`
}

// CameraMode represents camera mode
type CameraMode string

const (
	ModeFree      CameraMode = "free"
	ModeFollow    CameraMode = "follow"
	ModeOverview  CameraMode = "overview"
	ModeCinematic CameraMode = "cinematic"
)

// Vector3 represents 3D vector
type Vector3 struct {
	X float64 `json:"x" db:"x"`
	Y float64 `json:"y" db:"y"`
	Z float64 `json:"z" db:"z"`
}

// JoinSpectatorRequest represents join request
type JoinSpectatorRequest struct {
	TournamentID       uuid.UUID     `json:"tournament_id"`
	PreferredCameraMode CameraMode   `json:"preferred_camera_mode,omitempty"`
	Nickname           string        `json:"nickname,omitempty"`
	StreamQuality      StreamQuality `json:"stream_quality,omitempty"`
}

// SpectatorSessionList represents list of sessions
type SpectatorSessionList struct {
	Sessions   []*SpectatorSession `json:"sessions"`
	TotalCount int                 `json:"total_count"`
	HasMore    bool                `json:"has_more"`
}

// ReplaySession represents replay session
type ReplaySession struct {
	SessionID    uuid.UUID     `json:"session_id" db:"session_id"`
	TournamentID uuid.UUID     `json:"tournament_id" db:"tournament_id"`
	SpectatorID  uuid.UUID     `json:"spectator_id" db:"spectator_id"`
	Status       SpectatorStatus `json:"status" db:"status"`
	StartTime    time.Time     `json:"start_time" db:"start_time"`
	CurrentTime  time.Time     `json:"current_time" db:"current_time"`
	Speed        float64       `json:"speed" db:"speed"`
	Paused       bool          `json:"paused" db:"paused"`
	CameraSettings *CameraSettings `json:"camera_settings,omitempty" db:"camera_settings"`
}

// ChatMessage represents chat message
type ChatMessage struct {
	MessageID   uuid.UUID    `json:"message_id" db:"message_id"`
	SessionID   uuid.UUID    `json:"session_id" db:"session_id"`
	SenderID    uuid.UUID    `json:"sender_id" db:"sender_id"`
	SenderName  string       `json:"sender_name,omitempty" db:"sender_name"`
	Content     string       `json:"content" db:"content"`
	Timestamp   time.Time    `json:"timestamp" db:"timestamp"`
	MessageType MessageType  `json:"message_type,omitempty" db:"message_type"`
	ReplyTo     *uuid.UUID   `json:"reply_to,omitempty" db:"reply_to"`
}

// MessageType represents message type
type MessageType string

const (
	TypeText   MessageType = "text"
	TypeEmoji  MessageType = "emoji"
	TypeSystem MessageType = "system"
)

// ChatMessageList represents list of messages
type ChatMessageList struct {
	Messages   []*ChatMessage `json:"messages"`
	TotalCount int            `json:"total_count"`
	HasMore    bool           `json:"has_more"`
}

// TournamentStats represents live tournament statistics
type TournamentStats struct {
	TournamentID     uuid.UUID              `json:"tournament_id"`
	TotalSpectators  int                    `json:"total_spectators"`
	ActiveSessions   int                    `json:"active_sessions"`
	PeakSpectators   int                    `json:"peak_spectators"`
	TopPlayers       []*PlayerRanking       `json:"top_players"`
	RecentEvents     []*TournamentEvent     `json:"recent_events"`
	LastUpdated      time.Time              `json:"last_updated"`
}

// PlayerRanking represents player ranking
type PlayerRanking struct {
	PlayerID     uuid.UUID `json:"player_id"`
	PlayerName   string    `json:"player_name"`
	TeamName     string    `json:"team_name,omitempty"`
	Rank         int       `json:"rank"`
	Value        float64   `json:"value"`
	PreviousRank int       `json:"previous_rank,omitempty"`
	Change       int       `json:"change,omitempty"`
}

// TournamentEvent represents tournament event
type TournamentEvent struct {
	EventID     uuid.UUID `json:"event_id"`
	EventType   string    `json:"event_type"`
	Description string    `json:"description"`
	Timestamp   time.Time `json:"timestamp"`
	PlayerID    *uuid.UUID `json:"player_id,omitempty"`
	TeamID      *uuid.UUID `json:"team_id,omitempty"`
	Data        map[string]interface{} `json:"data,omitempty"`
}

// HealthResponse represents health check response
type HealthResponse struct {
	Status    string    `json:"status"`
	Domain    string    `json:"domain"`
	Timestamp time.Time `json:"timestamp"`
	Version   string    `json:"version"`
}

// ErrorResponse represents error response
type ErrorResponse struct {
	Message   string                 `json:"message"`
	Domain    string                 `json:"domain"`
	Timestamp time.Time              `json:"timestamp"`
	Code      int                    `json:"code"`
	Details   map[string]interface{} `json:"details,omitempty"`
}
