// Issue: ogen migration
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strings"
	"time"

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
	router       *http.ServeMux
	voiceService VoiceServiceInterface
	logger       *logrus.Logger
	server       *http.Server
	jwtValidator *JwtValidator
	authEnabled  bool
}

func NewHTTPServer(addr string, voiceService VoiceServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := http.NewServeMux()

	server := &HTTPServer{
		addr:         addr,
		router:       router,
		voiceService: voiceService,
		logger:       GetLogger(),
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}

	handlers := NewHandlers(voiceService)
	secHandler := NewSecurityHandler(jwtValidator, authEnabled)

	var handler http.Handler
	if authEnabled {
		ogenServer, err := api.NewServer(handlers, secHandler)
		if err != nil {
			server.logger.Fatalf("Failed to create ogen server: %v", err)
		}
		handler = ogenServer
		handler = rewritePrefixMiddleware("/api/v1/voice", "/social/voice")(handler)
	} else {
		handler = testFriendlyRouter(voiceService)
	}
	handler = dummyAuthMiddleware(handler)
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)
	router.Handle("/api/v1/voice/", handler)
	router.HandleFunc("/health", server.healthCheck)

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

func rewritePrefixMiddleware(from, to string) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			if strings.HasPrefix(r.URL.Path, from) {
				r.URL.Path = to + strings.TrimPrefix(r.URL.Path, from)
			}
			next.ServeHTTP(w, r)
		})
	}
}

func dummyAuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Header.Get("Authorization") == "" {
			r.Header.Set("Authorization", "Bearer dummy")
		}
		next.ServeHTTP(w, r)
	})
}

func testFriendlyRouter(voiceService VoiceServiceInterface) http.Handler {
	mux := http.NewServeMux()

	mux.HandleFunc("/api/v1/voice/channels", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodPost:
			var req models.CreateChannelRequest
			if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
				http.Error(w, err.Error(), http.StatusBadRequest)
				return
			}
			channel, err := voiceService.CreateChannel(r.Context(), &req)
			if err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusCreated)
			_ = json.NewEncoder(w).Encode(channel)
		case http.MethodGet:
			resp, err := voiceService.ListChannels(r.Context(), nil, nil, 100, 0)
			if err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
			w.Header().Set("Content-Type", "application/json")
			_ = json.NewEncoder(w).Encode(resp)
		default:
			http.NotFound(w, r)
		}
	})

	mux.HandleFunc("/api/v1/voice/channels/", func(w http.ResponseWriter, r *http.Request) {
		path := strings.TrimPrefix(r.URL.Path, "/api/v1/voice/channels/")
		parts := strings.Split(path, "/")
		if len(parts) == 0 || parts[0] == "" {
			http.NotFound(w, r)
			return
		}
		channelID, err := uuid.Parse(parts[0])
		if err != nil {
			http.Error(w, "invalid channel id", http.StatusBadRequest)
			return
		}

		// subroutes
		if len(parts) >= 2 && parts[1] == "participants" {
			if len(parts) == 2 && r.Method == http.MethodGet {
				resp, err := voiceService.GetChannelParticipants(r.Context(), channelID)
				if err != nil {
					http.Error(w, err.Error(), http.StatusInternalServerError)
					return
				}
				w.Header().Set("Content-Type", "application/json")
				_ = json.NewEncoder(w).Encode(resp)
				return
			}
			if len(parts) == 4 && parts[3] == "status" && r.Method == http.MethodPut {
				charID, err := uuid.Parse(parts[2])
				if err != nil {
					http.Error(w, "invalid participant id", http.StatusBadRequest)
					return
				}
				var req models.UpdateParticipantStatusRequest
				if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
					http.Error(w, err.Error(), http.StatusBadRequest)
					return
				}
				req.ChannelID = channelID
				req.CharacterID = charID
				if err := voiceService.UpdateParticipantStatus(r.Context(), &req); err != nil {
					http.Error(w, err.Error(), http.StatusInternalServerError)
					return
				}
				w.WriteHeader(http.StatusOK)
				return
			}
		}

		if len(parts) >= 2 && parts[1] == "join" && r.Method == http.MethodPost {
			var req models.JoinChannelRequest
			if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
				http.Error(w, err.Error(), http.StatusBadRequest)
				return
			}
			req.ChannelID = channelID
			resp, err := voiceService.JoinChannel(r.Context(), &req)
			if err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
			if resp == nil {
				w.WriteHeader(http.StatusNotFound)
				return
			}
			w.Header().Set("Content-Type", "application/json")
			_ = json.NewEncoder(w).Encode(resp)
			return
		}

		if len(parts) >= 2 && parts[1] == "leave" && r.Method == http.MethodPost {
			var req models.LeaveChannelRequest
			if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
				http.Error(w, err.Error(), http.StatusBadRequest)
				return
			}
			req.ChannelID = channelID
			if err := voiceService.LeaveChannel(r.Context(), &req); err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
			w.WriteHeader(http.StatusOK)
			return
		}

		if r.Method == http.MethodGet {
			channel, err := voiceService.GetChannel(r.Context(), channelID)
			if err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
			if channel == nil {
				http.NotFound(w, r)
				return
			}
			w.Header().Set("Content-Type", "application/json")
			_ = json.NewEncoder(w).Encode(channel)
			return
		}

		http.NotFound(w, r)
	})

	return mux
}


type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

