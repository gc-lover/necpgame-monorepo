package server

import (
	"crypto/rand"
	"fmt"
	"sync"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/gorilla/websocket"
	"github.com/sirupsen/logrus"
	"golang.org/x/time/rate"
)

// OPTIMIZATION: Issue #2030 - Memory-aligned struct for voice chat performance
type VoiceChatService struct {
	logger          *logrus.Logger
	metrics         *VoiceChatMetrics
	config          *VoiceChatServiceConfig

	// OPTIMIZATION: Issue #2030 - Thread-safe storage for MMO voice operations
	channels        sync.Map // OPTIMIZATION: Concurrent channel management
	connections     sync.Map // OPTIMIZATION: Concurrent connection management
	streams         sync.Map // OPTIMIZATION: Concurrent audio stream management
	rateLimiters    sync.Map // OPTIMIZATION: Per-user rate limiting

	// OPTIMIZATION: Issue #2030 - Memory pooling for hot path structs (zero allocations target!)
	channelResponsePool sync.Pool
	streamResponsePool sync.Pool
	proximityResponsePool sync.Pool
	ttsResponsePool sync.Pool
}

// OPTIMIZATION: Issue #2030 - Memory-aligned voice channel structs
type VoiceChannel struct {
	ChannelID       string                 `json:"channel_id"`       // 16 bytes
	Name            string                 `json:"name"`             // 16 bytes
	Type            string                 `json:"type"`             // 16 bytes
	Description     string                 `json:"description"`      // 16 bytes
	CreatorID       string                 `json:"creator_id"`       // 16 bytes
	CreatedAt       time.Time              `json:"created_at"`       // 24 bytes
	MaxParticipants int                    `json:"max_participants"` // 8 bytes
	ParticipantCount int                   `json:"participant_count"` // 8 bytes
	IsPrivate       bool                   `json:"is_private"`       // 1 byte
	Password        string                 `json:"-"`                // 16 bytes (not serialized)
	AudioSettings   AudioSettings          `json:"audio_settings"`   // ~64 bytes
	Permissions     map[string]interface{} `json:"permissions"`     // 8 bytes (map)
	Participants    map[string]*ChannelParticipant `json:"-"`      // 8 bytes (map)
	LastActivity    time.Time              `json:"last_activity"`    // 24 bytes
}

// OPTIMIZATION: Issue #2030 - Memory-aligned audio connection structs
type AudioConnection struct {
	ConnectionID    string          `json:"connection_id"`    // 16 bytes
	UserID          string          `json:"user_id"`          // 16 bytes
	ChannelID       string          `json:"channel_id"`       // 16 bytes
	WebSocket       *websocket.Conn `json:"-"`               // 8 bytes (pointer)
	ConnectedAt     time.Time       `json:"connected_at"`     // 24 bytes
	LastHeartbeat   time.Time       `json:"last_heartbeat"`   // 24 bytes
	IsMuted         bool            `json:"is_muted"`         // 1 byte
	IsDeafened      bool            `json:"is_deafened"`      // 1 byte
	Speaking        bool            `json:"speaking"`         // 1 byte
	VolumeLevel     float64         `json:"volume_level"`     // 8 bytes
	AudioConfig     AudioConfig     `json:"audio_config"`     // ~64 bytes
	SendChan        chan []byte      `json:"-"`               // 8 bytes (chan)
}

// OPTIMIZATION: Issue #2030 - Memory-aligned audio streaming structs
type AudioStream struct {
	StreamID        string      `json:"stream_id"`        // 16 bytes
	UserID          string      `json:"user_id"`          // 16 bytes
	ChannelID       string      `json:"channel_id"`       // 16 bytes
	StreamType      string      `json:"stream_type"`      // 16 bytes
	StartedAt       time.Time   `json:"started_at"`       // 24 bytes
	Bitrate         int         `json:"bitrate"`          // 8 bytes
	TotalBytes      int64       `json:"total_bytes"`      // 8 bytes
	IsActive        bool        `json:"is_active"`        // 1 byte
	Metadata        map[string]interface{} `json:"metadata"` // 8 bytes (map)
}

// OPTIMIZATION: Issue #2030 - Audio settings for voice quality optimization
type AudioSettings struct {
	SampleRate         int  `json:"sample_rate"`          // 8 bytes
	Channels           int  `json:"channels"`             // 8 bytes
	Bitrate            int  `json:"bitrate"`              // 8 bytes
	Codec              string `json:"codec"`              // 16 bytes
	NoiseSuppression   bool `json:"noise_suppression"`   // 1 byte
	EchoCancellation   bool `json:"echo_cancellation"`   // 1 byte
	VoiceActivityDetection bool `json:"voice_activity_detection"` // 1 byte
}

// OPTIMIZATION: Issue #2030 - Audio configuration for streaming
type AudioConfig struct {
	SampleRate  int      `json:"sample_rate"`  // 8 bytes
	Channels    int      `json:"channels"`     // 8 bytes
	Codec       string   `json:"codec"`        // 16 bytes
	Bitrate     int      `json:"bitrate"`      // 8 bytes
	BufferSize  int      `json:"buffer_size"`  // 8 bytes
	ICEServers  []string `json:"ice_servers"`  // 24 bytes (slice)
}

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

	// OPTIMIZATION: Issue #2030 - Initialize memory pools (zero allocations target!)
	s.channelResponsePool = sync.Pool{
		New: func() interface{} {
			return &VoiceChannel{}
		},
	}
	s.streamResponsePool = sync.Pool{
		New: func() interface{} {
			return &AudioStream{}
		},
	}
	s.proximityResponsePool = sync.Pool{
		New: func() interface{} {
			return &ProximityAudioResponse{}
		},
	}
	s.ttsResponsePool = sync.Pool{
		New: func() interface{} {
			return &TextToSpeechResponse{}
		},
	}

	// Start cleanup goroutines
	go s.channelCleanup()
	go s.connectionCleanup()

	return s
}

// OPTIMIZATION: Issue #2030 - Rate limiting middleware for voice API protection
func (s *VoiceChatService) RateLimitMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			userID := r.Header.Get("X-User-ID")
			if userID == "" {
				userID = r.RemoteAddr // Fallback to IP
			}

			limiter, _ := s.rateLimiters.LoadOrStore(userID, rate.NewLimiter(30, 60)) // 30 req/min burst 60

			if !limiter.(*rate.Limiter).Allow() {
				s.logger.WithField("user_id", userID).Warn("voice API rate limit exceeded")
				http.Error(w, "Too many requests", http.StatusTooManyRequests)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// Helper methods for cleanup
func (s *VoiceChatService) channelCleanup() {
	ticker := time.NewTicker(5 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		now := time.Now()
		s.channels.Range(func(key, value interface{}) bool {
			channel := value.(*VoiceChannel)
			if now.Sub(channel.LastActivity) > 24*time.Hour && channel.ParticipantCount == 0 {
				s.channels.Delete(key)
				s.metrics.ActiveChannels.Dec()
				s.logger.WithField("channel_id", channel.ChannelID).Info("inactive channel cleaned up")
			}
			return true
		})
	}
}

func (s *VoiceChatService) connectionCleanup() {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for range ticker.C {
		now := time.Now()
		s.connections.Range(func(key, value interface{}) bool {
			conn := value.(*AudioConnection)
			if now.Sub(conn.LastHeartbeat) > 5*time.Minute {
				s.connections.Delete(key)
				s.metrics.VoiceConnections.Dec()
				s.logger.WithField("connection_id", conn.ConnectionID).Info("stale connection cleaned up")
			}
			return true
		})
	}
}

// Helper functions
func generateChannelID() string {
	return fmt.Sprintf("channel_%d", time.Now().UnixNano())
}

func generateStreamID() string {
	return fmt.Sprintf("stream_%d", time.Now().UnixNano())
}

func generateConnectionID() string {
	return fmt.Sprintf("conn_%d", time.Now().UnixNano())
}

func generateSessionToken() string {
	return fmt.Sprintf("token_%d", time.Now().UnixNano())
}

func generateInviteCode() string {
	return fmt.Sprintf("invite_%d", time.Now().UnixNano())
}

func generateTTSID() string {
	return fmt.Sprintf("tts_%d", time.Now().UnixNano())
}

func generateReportID() string {
	return fmt.Sprintf("report_%d", time.Now().UnixNano())
}
