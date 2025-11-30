package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/voice-chat-service-go/models"
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
	addr             string
	router           *mux.Router
	voiceService     VoiceServiceInterface
	subchannelService SubchannelServiceInterface
	logger           *logrus.Logger
	server           *http.Server
	jwtValidator     *JwtValidator
	authEnabled      bool
}

func NewHTTPServer(addr string, voiceService VoiceServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:             addr,
		router:           router,
		voiceService:     voiceService,
		subchannelService: nil, // Can be set later if needed
		logger:           GetLogger(),
		jwtValidator:     jwtValidator,
		authEnabled:      authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/voice").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.HandleFunc("/channels", server.createChannel).Methods("POST")
	api.HandleFunc("/channels", server.listChannels).Methods("GET")
	api.HandleFunc("/channels/{channel_id}", server.getChannel).Methods("GET")
	api.HandleFunc("/channels/{channel_id}/detail", server.getChannelDetail).Methods("GET")
	api.HandleFunc("/channels/{channel_id}/join", server.joinChannel).Methods("POST")
	api.HandleFunc("/channels/{channel_id}/leave", server.leaveChannel).Methods("POST")
	api.HandleFunc("/channels/{channel_id}/participants", server.getParticipants).Methods("GET")
	api.HandleFunc("/channels/{channel_id}/participants/{character_id}/status", server.updateParticipantStatus).Methods("PUT")
	api.HandleFunc("/channels/{channel_id}/participants/{character_id}/position", server.updateParticipantPosition).Methods("PUT")

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

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

func (s *HTTPServer) createChannel(w http.ResponseWriter, r *http.Request) {
	var req models.CreateChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	channel, err := s.voiceService.CreateChannel(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create channel")
		s.respondError(w, http.StatusInternalServerError, "failed to create channel")
		return
	}

	s.respondJSON(w, http.StatusCreated, channel)
}

func (s *HTTPServer) listChannels(w http.ResponseWriter, r *http.Request) {
	var channelType *models.VoiceChannelType
	if typeStr := r.URL.Query().Get("type"); typeStr != "" {
		ct := models.VoiceChannelType(typeStr)
		channelType = &ct
	}

	var ownerID *uuid.UUID
	if ownerIDStr := r.URL.Query().Get("owner_id"); ownerIDStr != "" {
		if id, err := uuid.Parse(ownerIDStr); err == nil {
			ownerID = &id
		}
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.voiceService.ListChannels(r.Context(), channelType, ownerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list channels")
		s.respondError(w, http.StatusInternalServerError, "failed to list channels")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getChannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	channel, err := s.voiceService.GetChannel(r.Context(), channelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get channel")
		s.respondError(w, http.StatusInternalServerError, "failed to get channel")
		return
	}

	if channel == nil {
		s.respondError(w, http.StatusNotFound, "channel not found")
		return
	}

	s.respondJSON(w, http.StatusOK, channel)
}

func (s *HTTPServer) getChannelDetail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	response, err := s.voiceService.GetChannelDetail(r.Context(), channelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get channel detail")
		s.respondError(w, http.StatusInternalServerError, "failed to get channel detail")
		return
	}

	if response == nil {
		s.respondError(w, http.StatusNotFound, "channel not found")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) joinChannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	var req models.JoinChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.ChannelID = channelID

	response, err := s.voiceService.JoinChannel(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to join channel")
		if err.Error() == "channel is full" {
			s.respondError(w, http.StatusConflict, "channel is full")
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to join channel")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) leaveChannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	var req models.LeaveChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.ChannelID = channelID

	err = s.voiceService.LeaveChannel(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to leave channel")
		s.respondError(w, http.StatusInternalServerError, "failed to leave channel")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getParticipants(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	response, err := s.voiceService.GetChannelParticipants(r.Context(), channelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get participants")
		s.respondError(w, http.StatusInternalServerError, "failed to get participants")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) updateParticipantStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req models.UpdateParticipantStatusRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.ChannelID = channelID
	req.CharacterID = characterID

	err = s.voiceService.UpdateParticipantStatus(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update participant status")
		s.respondError(w, http.StatusInternalServerError, "failed to update participant status")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) updateParticipantPosition(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelID, err := uuid.Parse(vars["channel_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel_id")
		return
	}

	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req models.UpdateParticipantPositionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.ChannelID = channelID
	req.CharacterID = characterID

	err = s.voiceService.UpdateParticipantPosition(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update participant position")
		s.respondError(w, http.StatusInternalServerError, "failed to update participant position")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
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

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		ctx = context.WithValue(ctx, "user_id", claims.Subject)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
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

