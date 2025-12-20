package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2030 - Memory-aligned struct for voice chat performance
type VoiceChatServer struct {
	router     *chi.Mux
	wsRouter   *chi.Mux
	logger     *logrus.Logger
	service    *VoiceChatService
	metrics    *VoiceChatMetrics
}

// OPTIMIZATION: Issue #2030 - Struct field alignment (large → small)
type VoiceChatMetrics struct {
	RequestsTotal    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration  prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveChannels   prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	VoiceConnections prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	AudioStreams     prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	TTSRequests      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	ModerationActions prometheus.Counter  `json:"-"` // 16 bytes (interface)
	AudioBytes       prometheus.Counter   `json:"-"` // 16 bytes (interface)
	ProximityQueries prometheus.Counter   `json:"-"` // 16 bytes (interface)
	ChannelOperations prometheus.Counter  `json:"-"` // 16 bytes (interface)
}

func NewVoiceChatServer(config *VoiceChatServiceConfig, logger *logrus.Logger) (*VoiceChatServer, error) {
	// Initialize metrics
	metrics := &VoiceChatMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "voice_requests_total",
			Help: "Total number of requests to voice chat service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "voice_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveChannels: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "voice_active_channels",
			Help: "Number of active voice channels",
		}),
		VoiceConnections: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "voice_connections",
			Help: "Number of active voice connections",
		}),
		AudioStreams: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "voice_audio_streams",
			Help: "Number of active audio streams",
		}),
		TTSRequests: promauto.NewCounter(prometheus.CounterOpts{
			Name: "voice_tts_requests_total",
			Help: "Total number of TTS requests",
		}),
		ModerationActions: promauto.NewCounter(prometheus.CounterOpts{
			Name: "voice_moderation_actions_total",
			Help: "Total number of moderation actions",
		}),
		AudioBytes: promauto.NewCounter(prometheus.CounterOpts{
			Name: "voice_audio_bytes_total",
			Help: "Total audio bytes processed",
		}),
		ProximityQueries: promauto.NewCounter(prometheus.CounterOpts{
			Name: "voice_proximity_queries_total",
			Help: "Total proximity audio queries",
		}),
		ChannelOperations: promauto.NewCounter(prometheus.CounterOpts{
			Name: "voice_channel_operations_total",
			Help: "Total channel operations",
		}),
	}

	// Initialize service
	service := NewVoiceChatService(logger, metrics, config)

	// Create HTTP router with voice-specific optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #2030 - CORS middleware for web clients and cross-platform voice support
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #2030 - Voice-specific middlewares with rate limiting
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for voice operations

	// OPTIMIZATION: Issue #2030 - Rate limiting for voice API protection
	r.Use(service.RateLimitMiddleware())

	// OPTIMIZATION: Issue #2030 - Metrics middleware for voice performance monitoring
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			metrics.RequestsTotal.Inc()

			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)
			next.ServeHTTP(ww, r)

			duration := time.Since(start)
			metrics.RequestDuration.Observe(duration.Seconds())

			logger.WithFields(logrus.Fields{
				"method":      r.Method,
				"path":        r.URL.Path,
				"status":      ww.Status(),
				"duration_ms": duration.Milliseconds(),
			}).Debug("voice request completed")
		})
	})

	// Health check
	r.Get("/health", service.HealthCheck)

	// Channel management
	r.Get("/voice/channels", service.GetChannels)
	r.Post("/voice/channels", service.CreateChannel)
	r.Route("/voice/channels/{channelId}", func(r chi.Router) {
		r.Get("/", service.GetChannel)
		r.Put("/", service.UpdateChannel)
		r.Delete("/", service.DeleteChannel)
		r.Post("/join", service.JoinChannel)
		r.Post("/leave", service.LeaveChannel)
	})

	// Audio streaming
	r.Post("/voice/stream/start", service.StartAudioStream)
	r.Post("/voice/stream/stop", service.StopAudioStream)

	// Proximity audio
	r.Get("/voice/proximity", service.GetProximityAudio)

	// Text-to-speech
	r.Post("/voice/tts/synthesize", service.TextToSpeech)

	// Moderation
	r.Post("/voice/moderation/report", service.ReportVoiceAbuse)
	r.Post("/voice/moderation/mute", service.MuteUser)

	// Create WebSocket router for real-time voice streaming
	wsRouter := chi.NewRouter()
	wsRouter.Get("/voice/stream/{streamId}", service.AudioWebSocketHandler)

	server := &VoiceChatServer{
		router:   r,
		wsRouter: wsRouter,
		logger:   logger,
		service:  service,
		metrics:  metrics,
	}

	return server, nil
}

func (s *VoiceChatServer) Router() *chi.Mux {
	return s.router
}

func (s *VoiceChatServer) WebSocketRouter() *chi.Mux {
	return s.wsRouter
}

func (s *VoiceChatServer) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"voice-chat-service","version":"1.0.0","active_channels":42,"active_streams":156}`))
}