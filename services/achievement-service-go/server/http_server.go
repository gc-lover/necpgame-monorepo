// Issue: #141888195
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/sirupsen/logrus"
)

type AchievementServiceInterface interface {
	CreateAchievement(ctx context.Context, achievement *models.Achievement) error
	GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error)
	GetAchievementByCode(ctx context.Context, code string) (*models.Achievement, error)
	ListAchievements(ctx context.Context, category *models.AchievementCategory, limit, offset int) (*models.AchievementListResponse, error)
	TrackProgress(ctx context.Context, playerID, achievementID uuid.UUID, progress int, progressData map[string]interface{}) error
	UnlockAchievement(ctx context.Context, playerID, achievementID uuid.UUID) error
	GetPlayerAchievements(ctx context.Context, playerID uuid.UUID, category *models.AchievementCategory, limit, offset int) (*models.PlayerAchievementResponse, error)
	GetLeaderboard(ctx context.Context, period string, limit int) (*models.LeaderboardResponse, error)
	GetAchievementStats(ctx context.Context, achievementID uuid.UUID) (*models.AchievementStatsResponse, error)
}

type HTTPServer struct {
	addr              string
	router            *mux.Router
	achievementService AchievementServiceInterface
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, achievementService AchievementServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:              addr,
		router:            router,
		achievementService: achievementService,
		logger:            GetLogger(),
		jwtValidator:      jwtValidator,
		authEnabled:       authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.HandleFunc("/achievements", server.listAchievements).Methods("GET")
	api.HandleFunc("/achievements", server.createAchievement).Methods("POST")
	api.HandleFunc("/achievements/leaderboard", server.getLeaderboard).Methods("GET")
	api.HandleFunc("/achievements/code/{code}", server.getAchievementByCode).Methods("GET")
	api.HandleFunc("/achievements/{id}/stats", server.getAchievementStats).Methods("GET")
	api.HandleFunc("/achievements/{id}", server.getAchievement).Methods("GET")

	api.HandleFunc("/achievements/players/{playerId}", server.getPlayerAchievements).Methods("GET")
	api.HandleFunc("/achievements/players/{playerId}/progress", server.trackProgress).Methods("POST")
	api.HandleFunc("/achievements/players/{playerId}/unlock/{achievementId}", server.unlockAchievement).Methods("POST")

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

func (s *HTTPServer) createAchievement(w http.ResponseWriter, r *http.Request) {
	var achievement models.Achievement
	if err := json.NewDecoder(r.Body).Decode(&achievement); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if achievement.Code == "" {
		s.respondError(w, http.StatusBadRequest, "code is required")
		return
	}

	err := s.achievementService.CreateAchievement(r.Context(), &achievement)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create achievement")
		s.respondError(w, http.StatusInternalServerError, "failed to create achievement")
		return
	}

	s.respondJSON(w, http.StatusCreated, achievement)
}

func (s *HTTPServer) getAchievement(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid achievement id")
		return
	}

	achievement, err := s.achievementService.GetAchievement(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get achievement")
		s.respondError(w, http.StatusInternalServerError, "failed to get achievement")
		return
	}

	if achievement == nil {
		s.respondError(w, http.StatusNotFound, "achievement not found")
		return
	}

	s.respondJSON(w, http.StatusOK, achievement)
}

func (s *HTTPServer) getAchievementByCode(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	code := vars["code"]

	achievement, err := s.achievementService.GetAchievementByCode(r.Context(), code)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get achievement by code")
		s.respondError(w, http.StatusInternalServerError, "failed to get achievement")
		return
	}

	if achievement == nil {
		s.respondError(w, http.StatusNotFound, "achievement not found")
		return
	}

	s.respondJSON(w, http.StatusOK, achievement)
}

func (s *HTTPServer) listAchievements(w http.ResponseWriter, r *http.Request) {
	var category *models.AchievementCategory
	if catStr := r.URL.Query().Get("category"); catStr != "" {
		cat := models.AchievementCategory(catStr)
		category = &cat
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

	response, err := s.achievementService.ListAchievements(r.Context(), category, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list achievements")
		s.respondError(w, http.StatusInternalServerError, "failed to list achievements")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getPlayerAchievements(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["playerId"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player id")
		return
	}

	var category *models.AchievementCategory
	if catStr := r.URL.Query().Get("category"); catStr != "" {
		cat := models.AchievementCategory(catStr)
		category = &cat
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

	response, err := s.achievementService.GetPlayerAchievements(r.Context(), playerID, category, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player achievements")
		s.respondError(w, http.StatusInternalServerError, "failed to get player achievements")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) trackProgress(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["playerId"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player id")
		return
	}

	var req struct {
		AchievementID uuid.UUID              `json:"achievement_id"`
		Progress      int                    `json:"progress"`
		ProgressData  map[string]interface{} `json:"progress_data,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err = s.achievementService.TrackProgress(r.Context(), playerID, req.AchievementID, req.Progress, req.ProgressData)
	if err != nil {
		s.logger.WithError(err).Error("Failed to track progress")
		s.respondError(w, http.StatusInternalServerError, "failed to track progress")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) unlockAchievement(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["playerId"]
	achievementIDStr := vars["achievementId"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player id")
		return
	}

	achievementID, err := uuid.Parse(achievementIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid achievement id")
		return
	}

	err = s.achievementService.UnlockAchievement(r.Context(), playerID, achievementID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to unlock achievement")
		s.respondError(w, http.StatusInternalServerError, "failed to unlock achievement")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getLeaderboard(w http.ResponseWriter, r *http.Request) {
	period := r.URL.Query().Get("period")
	if period == "" {
		period = "all"
	}

	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 500 {
			limit = l
		}
	}

	response, err := s.achievementService.GetLeaderboard(r.Context(), period, limit)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAchievementStats(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid achievement id")
		return
	}

	stats, err := s.achievementService.GetAchievementStats(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get achievement stats")
		s.respondError(w, http.StatusInternalServerError, "failed to get achievement stats")
		return
	}

	s.respondJSON(w, http.StatusOK, stats)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		next.ServeHTTP(w, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
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

