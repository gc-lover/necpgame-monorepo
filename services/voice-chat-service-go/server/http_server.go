// Issue: ogen migration
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type VoiceServiceInterface interface {
	CreateChannel(ctx context.Context, req *models.CreateChannelRequest) (*models.VoiceChannel, error)
	GetChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error)
	ListChannels(ctx context.Context, channelType *models.VoiceChannelType, ownerID *uuid.UUID, limit, offset int) (*models.ChannelListResponse, error)
	JoinChannel(ctx context.Context, req *models.JoinChannelRequest) (*models.WebRTCTokenResponse, error)
	LeaveChannel(ctx context.Context, req *models.LeaveChannelRequest) error
	UpdateParticipantStatus(ctx context.Context, req *models.UpdateParticipantStatusRequest) error
	UpdateParticipantPosition(ctx context.Context, req *models.UpdateParticipantPositionRequest) error
	GetChannelParticipants(ctx context.Context, channelID uuid.UUID) (*models.ParticipantListResponse, error)
	GetChannelDetail(ctx context.Context, channelID uuid.UUID) (*models.ChannelDetailResponse, error)
}

type HTTPServer struct {
	addr         string
	router       *chi.Mux
	voiceService VoiceServiceInterface
	logger       *logrus.Logger
	server       *http.Server
	jwtValidator *JwtValidator
	authEnabled  bool
}

func NewHTTPServer(addr string, voiceService VoiceServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()

	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))

	server := &HTTPServer{
		addr:         addr,
		router:       router,
		voiceService: voiceService,
		logger:       GetLogger(),
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	handlers := NewHandlers(voiceService)
	secHandler := NewSecurityHandler(jwtValidator, authEnabled)

	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		server.logger.Fatalf("Failed to create ogen server: %v", err)
	}

	router.Mount("/social/voice", ogenServer)
	router.Get("/health", server.healthCheck)

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}


func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      recorder.statusCode,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}


type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

