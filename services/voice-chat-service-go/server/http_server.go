package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/sirupsen/logrus"
)

// VoiceChatServiceConfig holds configuration for the Voice Chat Service
type VoiceChatServiceConfig struct {
	Port                     string
	ReadTimeout              time.Duration
	WriteTimeout             time.Duration
	MaxHeaderBytes           int
	RedisAddr                string
	WebSocketReadTimeout     time.Duration
	WebSocketWriteTimeout    time.Duration
	MaxVoiceConnections      int
	ProximityUpdateInterval  time.Duration
	ConnectionCleanupInterval time.Duration
	ChannelCleanupInterval   time.Duration
}

// VoiceChatMetrics holds Prometheus metrics for the Voice Chat Service
type VoiceChatMetrics struct {
	// Metrics implementation would go here
	// For now, using placeholder counters
	ActiveChannels      float64
	ActiveConnections   float64
	AudioStreams        float64
	TTSSynthesizations  float64
	ModerationReports   float64
	Errors              float64
	BytesSent           float64
	BytesReceived       float64
	WebSocketErrors     float64
}

// HTTP server for Voice Chat Service
type HTTPServer struct {
	service *VoiceChatService
	logger  *logrus.Logger
	config  *VoiceChatServiceConfig
}

// NewHTTPServer creates a new HTTP server for Voice Chat Service
func NewHTTPServer(service *VoiceChatService, logger *logrus.Logger, config *VoiceChatServiceConfig) *HTTPServer {
	return &HTTPServer{
		service: service,
		logger:  logger,
		config:  config,
	}
}

// SetupRoutes configures all HTTP routes for the Voice Chat Service
func (hs *HTTPServer) SetupRoutes() *chi.Mux {
	r := chi.NewRouter()

	// Middleware stack
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	// CORS middleware for web client support
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure based on environment
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	// Rate limiting middleware
	r.Use(hs.service.RateLimitMiddleware())

	// Health check endpoint
	r.Get("/voice-chat/health", hs.service.HealthCheck)

	// Voice channel management routes
	r.Route("/voice-chat/channels", func(r chi.Router) {
		r.Get("/", hs.service.ListChannels)     // List channels with filtering
		r.Post("/", hs.service.CreateChannel)   // Create channel

		r.Route("/{channelId}", func(r chi.Router) {
			r.Get("/", hs.service.GetChannel)         // Get channel details
			r.Put("/", hs.service.UpdateChannel)      // Update channel settings
			r.Delete("/", hs.service.DeleteChannel)   // Delete channel

			// Channel participation
			r.Post("/join", hs.service.JoinChannel)     // Join channel
			r.Post("/leave", hs.service.LeaveChannel)   // Leave channel
		})
	})

	// Audio streaming routes
	r.Route("/voice-chat/audio", func(r chi.Router) {
		r.Post("/stream", hs.service.StartAudioStream)   // Start audio streaming
		r.Post("/proximity", hs.service.UpdateProximity) // Update proximity audio
	})

	// Text-to-speech routes
	r.Post("/voice-chat/tts/synthesize", hs.service.SynthesizeSpeech) // Synthesize speech

	// Moderation routes
	r.Post("/voice-chat/moderation/report", hs.service.ReportVoiceAbuse) // Report abuse

	return r
}

// Start starts the HTTP server
func (hs *HTTPServer) Start() error {
	r := hs.SetupRoutes()

	server := &http.Server{
		Addr:           ":" + hs.config.Port,
		Handler:        r,
		ReadTimeout:    hs.config.ReadTimeout,
		WriteTimeout:   hs.config.WriteTimeout,
		MaxHeaderBytes: hs.config.MaxHeaderBytes,
	}

	hs.logger.WithField("port", hs.config.Port).Info("voice chat service HTTP server starting")

	if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		hs.logger.WithError(err).Error("voice chat service HTTP server failed")
		return err
	}

	return nil
}