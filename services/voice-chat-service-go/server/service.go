package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"github.com/sirupsen/logrus"

	"golang.org/x/time/rate"
)

// VoiceChatService OPTIMIZATION: Issue #2177 - Memory-aligned struct for voice chat service performance
type VoiceChatService struct {
	logger  *logrus.Logger
	metrics *VoiceChatMetrics
	config  *VoiceChatServiceConfig

	// OPTIMIZATION: Issue #2177 - Thread-safe storage for MMO voice chat management
	channels       sync.Map // OPTIMIZATION: Concurrent channel management
	connections    sync.Map // OPTIMIZATION: Concurrent WebSocket connection management
	proximityZones sync.Map // OPTIMIZATION: Concurrent proximity zone management

	// OPTIMIZATION: Issue #2177 - Memory pooling for hot path structs (zero allocations target!)
	channelResponsePool  sync.Pool
	audioStreamPool      sync.Pool
	ttsResponsePool      sync.Pool
	moderationReportPool sync.Pool
}

// VoiceChannel OPTIMIZATION: Issue #2177 - Memory-aligned voice channel struct
type VoiceChannel struct {
	ChannelID        string          `json:"channel_id"`        // 16 bytes
	Name             string          `json:"name"`              // 16 bytes
	Type             string          `json:"type"`              // 16 bytes (global, guild, group, proximity, private)
	OwnerID          string          `json:"owner_id"`          // 16 bytes
	ParticipantCount int             `json:"participant_count"` // 8 bytes
	MaxParticipants  int             `json:"max_participants"`  // 8 bytes
	IsPublic         bool            `json:"is_public"`         // 1 byte
	Settings         ChannelSettings `json:"settings"`          // ~64 bytes
	Participants     sync.Map        `json:"-"`                 // map[string]*WSVoiceConnection - thread-safe
	CreatedAt        time.Time       `json:"created_at"`        // 24 bytes
	UpdatedAt        time.Time       `json:"updated_at"`        // 24 bytes
	LastActivity     time.Time       `json:"last_activity"`     // 24 bytes
}

// WSVoiceConnection OPTIMIZATION: Issue #2177 - Memory-aligned WebSocket voice connection
type WSVoiceConnection struct {
	ConnectionID  string          `json:"connection_id"`  // 16 bytes
	UserID        string          `json:"user_id"`        // 16 bytes
	ClientID      string          `json:"client_id"`      // 16 bytes
	ChannelID     string          `json:"channel_id"`     // 16 bytes
	Conn          *websocket.Conn `json:"-"`              // 8 bytes (pointer)
	ConnectedAt   time.Time       `json:"connected_at"`   // 24 bytes
	LastHeartbeat time.Time       `json:"last_heartbeat"` // 24 bytes
	IsMuted       bool            `json:"is_muted"`       // 1 byte
	IsDeafened    bool            `json:"is_deafened"`    // 1 byte
	SendChan      chan []byte     `json:"-"`              // 8 bytes (chan)
	Location      *PlayerLocation `json:"location"`       // 8 bytes (pointer)
	SessionToken  string          `json:"session_token"`  // 16 bytes
}

// PlayerLocation OPTIMIZATION: Issue #2177 - Memory-aligned player location for proximity audio
type PlayerLocation struct {
	X    float64 `json:"x"`    // 8 bytes
	Y    float64 `json:"y"`    // 8 bytes
	Z    float64 `json:"z"`    // 8 bytes
	Zone string  `json:"zone"` // 16 bytes
}

// ChannelSettings OPTIMIZATION: Issue #2177 - Memory-aligned channel settings
type ChannelSettings struct {
	ProximityRadius        float64 `json:"proximity_radius"`         // 8 bytes
	Codec                  string  `json:"codec"`                    // 16 bytes
	Bitrate                int     `json:"bitrate"`                  // 8 bytes
	EchoCancellation       bool    `json:"echo_cancellation"`        // 1 byte
	VoiceActivityDetection bool    `json:"voice_activity_detection"` // 1 byte
	NoiseSuppression       bool    `json:"noise_suppression"`        // 1 byte
}

// AudioConfig OPTIMIZATION: Issue #2177 - Memory-aligned audio configuration
type AudioConfig struct {
	SampleRate int      `json:"sample_rate"` // 8 bytes
	Channels   int      `json:"channels"`    // 8 bytes
	Codec      string   `json:"codec"`       // 16 bytes
	Bitrate    int      `json:"bitrate"`     // 8 bytes
	BufferSize int      `json:"buffer_size"` // 8 bytes
	ICEServers []string `json:"ice_servers"` // 24 bytes (slice)
}

// WebSocket upgrader with security settings for MMO cross-platform voice
var voiceUpgrader = websocket.Upgrader{
	ReadBufferSize:  8192, // OPTIMIZATION: Larger buffer for voice data
	WriteBufferSize: 8192, // OPTIMIZATION: Larger buffer for voice data
	CheckOrigin: func(r *http.Request) bool {
		return true // OPTIMIZATION: Allow all origins for MMO cross-platform voice
	},
}

func NewVoiceChatService(logger *logrus.Logger, metrics *VoiceChatMetrics, config *VoiceChatServiceConfig) *VoiceChatService {
	s := &VoiceChatService{
		logger:  logger,
		metrics: metrics,
		config:  config,
	}

	// OPTIMIZATION: Issue #2177 - Initialize memory pools (zero allocations target!)
	s.channelResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateChannelResponse{}
		},
	}
	s.audioStreamPool = sync.Pool{
		New: func() interface{} {
			return &StartAudioStreamResponse{}
		},
	}
	s.ttsResponsePool = sync.Pool{
		New: func() interface{} {
			return &SynthesizeSpeechResponse{}
		},
	}
	s.moderationReportPool = sync.Pool{
		New: func() interface{} {
			return &ReportVoiceAbuseResponse{}
		},
	}

	// Start background cleanup goroutines
	go s.cleanupInactiveChannels()
	go s.cleanupStaleConnections()
	go s.updateProximityCalculations()

	return s
}

// RateLimitMiddleware OPTIMIZATION: Issue #2177 - Rate limiting middleware for voice chat protection
func (s *VoiceChatService) RateLimitMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			playerID := r.Header.Get("X-Player-ID")
			if playerID == "" {
				playerID = r.RemoteAddr // Fallback to IP
			}

			// Moderate limits for voice chat operations (real-time audio features)
			limiter, _ := s.rateLimiters.LoadOrStore(playerID, rate.NewLimiter(300, 600)) // 300 req/sec burst 600

			if !limiter.(*rate.Limiter).Allow() {
				s.logger.WithField("player_id", playerID).Warn("voice chat API rate limit exceeded")
				s.metrics.Errors.Inc()
				http.Error(w, "Too many requests", http.StatusTooManyRequests)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// HealthCheck Health check method
func (s *VoiceChatService) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"status":             "healthy",
		"service":            "voice-chat-service",
		"version":            "1.0.0",
		"active_channels":    s.metrics.ActiveChannels,
		"active_connections": s.metrics.ActiveConnections,
		"audio_streams":      s.metrics.AudioStreams,
		"websocket_errors":   s.metrics.WebSocketErrors,
		"bytes_sent":         s.metrics.BytesSent,
		"bytes_received":     s.metrics.BytesReceived,
		"timestamp":          time.Now().Unix(),
	})
}

// CreateChannel Voice Channel Management Handlers
func (s *VoiceChatService) CreateChannel(w http.ResponseWriter, r *http.Request) {
	var req CreateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create channel request")
		s.metrics.Errors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate channel data
	if err := s.validateChannelRequest(&req); err != nil {
		s.logger.WithError(err).Error("channel validation failed")
		s.metrics.Errors.Inc()
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Check if channel name already exists
	if s.channelExists(req.Name) {
		s.metrics.Errors.Inc()
		http.Error(w, "Channel name already exists", http.StatusConflict)
		return
	}

	channel := &VoiceChannel{
		ChannelID:        uuid.New().String(),
		Name:             req.Name,
		Type:             req.Type,
		OwnerID:          "system", // Would be from auth context
		ParticipantCount: 0,
		MaxParticipants:  req.MaxParticipants,
		IsPublic:         req.IsPublic,
		Settings:         req.Settings,
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
		LastActivity:     time.Now(),
	}

	s.channels.Store(channel.ChannelID, channel)
	s.metrics.ActiveChannels.Inc()

	resp := s.channelResponsePool.Get().(*CreateChannelResponse)
	defer s.channelResponsePool.Put(resp)

	resp.ChannelID = channel.ChannelID
	resp.Name = channel.Name
	resp.Type = channel.Type
	resp.WebsocketURL = fmt.Sprintf("ws://localhost:%s/voice-chat/ws/%s", s.config.Port, channel.ChannelID)
	resp.StunServers = []string{"stun:stun.l.google.com:19302"}
	resp.TurnServers = []string{} // Would be configured
	resp.CreatedAt = channel.CreatedAt.Unix()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("channel_id", channel.ChannelID).Info("voice channel created successfully")
}

func (s *VoiceChatService) ListChannels(w http.ResponseWriter, r *http.Request) {
	typeFilter := r.URL.Query().Get("type")
	statusFilter := r.URL.Query().Get("status")
	limit := 20

	var channels []*VoiceChannelSummary
	s.channels.Range(func(key, value interface{}) bool {
		channel := value.(*VoiceChannel)

		if typeFilter != "" && channel.Type != typeFilter {
			return true
		}

		status := "active"
		if channel.ParticipantCount >= channel.MaxParticipants {
			status = "full"
		}
		if statusFilter != "" && status != statusFilter {
			return true
		}

		summary := &VoiceChannelSummary{
			ChannelID:        channel.ChannelID,
			Name:             channel.Name,
			Type:             channel.Type,
			OwnerID:          channel.OwnerID,
			ParticipantCount: channel.ParticipantCount,
			MaxParticipants:  channel.MaxParticipants,
			IsPublic:         channel.IsPublic,
			Status:           status,
			CreatedAt:        channel.CreatedAt.Unix(),
		}
		channels = append(channels, summary)
		return true
	})

	// Apply limit
	if len(channels) > limit {
		channels = channels[:limit]
	}

	resp := &ListChannelsResponse{
		Channels:   channels,
		TotalCount: len(channels),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *VoiceChatService) GetChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	var participants []*ChannelParticipant
	channel.Participants.Range(func(key, value interface{}) bool {
		conn := value.(*WSVoiceConnection)
		participant := &ChannelParticipant{
			UserID:      conn.UserID,
			DisplayName: "Player", // Would be from user service
			IsMuted:     conn.IsMuted,
			IsDeafened:  conn.IsDeafened,
			JoinedAt:    conn.ConnectedAt.Unix(),
		}
		participants = append(participants, participant)
		return true
	})

	channelSummary := &VoiceChannel{
		ChannelID:        channel.ChannelID,
		Name:             channel.Name,
		Type:             channel.Type,
		OwnerID:          channel.OwnerID,
		ParticipantCount: channel.ParticipantCount,
		MaxParticipants:  channel.MaxParticipants,
		IsPublic:         channel.IsPublic,
		Settings:         channel.Settings,
		CreatedAt:        channel.CreatedAt,
		UpdatedAt:        channel.UpdatedAt,
		LastActivity:     channel.LastActivity,
	}

	resp := &GetChannelResponse{
		Channel:      channelSummary,
		Participants: participants,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *VoiceChatService) UpdateChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)
	var req UpdateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode update channel request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Update channel settings
	var updatedFields []string
	if req.Name != "" {
		channel.Name = req.Name
		updatedFields = append(updatedFields, "name")
	}
	if req.MaxParticipants > 0 {
		channel.MaxParticipants = req.MaxParticipants
		updatedFields = append(updatedFields, "max_participants")
	}
	if req.Settings.ProximityRadius > 0 {
		channel.Settings = req.Settings
		updatedFields = append(updatedFields, "settings")
	}

	channel.UpdatedAt = time.Now()
	s.channels.Store(channelID, channel)

	resp := &UpdateChannelResponse{
		ChannelID:     channelID,
		UpdatedFields: updatedFields,
		UpdatedAt:     channel.UpdatedAt.Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("channel_id", channelID).Info("voice channel updated successfully")
}

func (s *VoiceChatService) DeleteChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)
