package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/leaderboard-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr              string
	router            *mux.Router
	leaderboardService LeaderboardService
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, leaderboardService LeaderboardService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:              addr,
		router:            router,
		leaderboardService: leaderboardService,
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

	api.HandleFunc("/leaderboards/global", server.getGlobalLeaderboard).Methods("GET")
	api.HandleFunc("/leaderboards/seasonal", server.getSeasonalLeaderboard).Methods("GET")
	api.HandleFunc("/leaderboards/class", server.getClassLeaderboard).Methods("GET")
	api.HandleFunc("/leaderboards/friends", server.getFriendsLeaderboard).Methods("GET")
	api.HandleFunc("/leaderboards/guild", server.getGuildLeaderboard).Methods("GET")

	api.HandleFunc("/leaderboards/rank", server.getPlayerRank).Methods("GET")
	api.HandleFunc("/leaderboards/rank/neighbors", server.getRankNeighbors).Methods("GET")

	api.HandleFunc("/world/leaderboards", server.listLeaderboards).Methods("GET")
	api.HandleFunc("/world/leaderboards/{leaderboardId}", server.getLeaderboard).Methods("GET")
	api.HandleFunc("/world/leaderboards/{leaderboardId}/top", server.getLeaderboardTop).Methods("GET")
	api.HandleFunc("/world/leaderboards/{leaderboardId}/rank/{playerId}", server.getLeaderboardPlayerRank).Methods("GET")
	api.HandleFunc("/world/leaderboards/{leaderboardId}/around/{playerId}", server.getLeaderboardRankAround).Methods("GET")

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

func (s *HTTPServer) getGlobalLeaderboard(w http.ResponseWriter, r *http.Request) {
	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 1000 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetGlobalLeaderboard(r.Context(), metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get global leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get global leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getSeasonalLeaderboard(w http.ResponseWriter, r *http.Request) {
	seasonID := r.URL.Query().Get("season_id")
	if seasonID == "" {
		s.respondError(w, http.StatusBadRequest, "season_id is required")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 1000 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetSeasonalLeaderboard(r.Context(), seasonID, metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get seasonal leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get seasonal leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getClassLeaderboard(w http.ResponseWriter, r *http.Request) {
	classIDStr := r.URL.Query().Get("class_id")
	if classIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "class_id is required")
		return
	}

	classID, err := uuid.Parse(classIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid class_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 1000 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetClassLeaderboard(r.Context(), classID, metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get class leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get class leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getFriendsLeaderboard(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	result, err := s.leaderboardService.GetFriendsLeaderboard(r.Context(), characterID, metric, limit)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get friends leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get friends leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getGuildLeaderboard(w http.ResponseWriter, r *http.Request) {
	guildIDStr := r.URL.Query().Get("guild_id")
	if guildIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "guild_id is required")
		return
	}

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 1000 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetGuildLeaderboard(r.Context(), guildID, metric, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getPlayerRank(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	scope := models.ScopeGlobal
	if scopeStr := r.URL.Query().Get("scope"); scopeStr != "" {
		scope = models.LeaderboardScope(scopeStr)
	}

	var seasonID *string
	if seasonIDStr := r.URL.Query().Get("season_id"); seasonIDStr != "" {
		seasonID = &seasonIDStr
	}

	rank, err := s.leaderboardService.GetPlayerRank(r.Context(), characterID, metric, scope, seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player rank")
		s.respondError(w, http.StatusInternalServerError, "failed to get player rank")
		return
	}

	if rank == nil {
		s.respondError(w, http.StatusNotFound, "player rank not found")
		return
	}

	s.respondJSON(w, http.StatusOK, rank)
}

func (s *HTTPServer) getRankNeighbors(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	metricStr := r.URL.Query().Get("metric")
	if metricStr == "" {
		s.respondError(w, http.StatusBadRequest, "metric is required")
		return
	}

	metric := models.LeaderboardMetric(metricStr)
	scope := models.ScopeGlobal
	if scopeStr := r.URL.Query().Get("scope"); scopeStr != "" {
		scope = models.LeaderboardScope(scopeStr)
	}

	rangeSize := 5
	if rangeStr := r.URL.Query().Get("range"); rangeStr != "" {
		if r, err := strconv.Atoi(rangeStr); err == nil && r > 0 && r <= 50 {
			rangeSize = r
		}
	}

	var seasonID *string
	if seasonIDStr := r.URL.Query().Get("season_id"); seasonIDStr != "" {
		seasonID = &seasonIDStr
	}

	entries, err := s.leaderboardService.GetRankNeighbors(r.Context(), characterID, metric, scope, rangeSize, seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get rank neighbors")
		s.respondError(w, http.StatusInternalServerError, "failed to get rank neighbors")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"entries": entries,
		"total":   len(entries),
	})
}

func (s *HTTPServer) listLeaderboards(w http.ResponseWriter, r *http.Request) {
	var leaderboardType *models.LeaderboardType
	if typeStr := r.URL.Query().Get("type"); typeStr != "" {
		lt := models.LeaderboardType(typeStr)
		leaderboardType = &lt
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetLeaderboards(r.Context(), leaderboardType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list leaderboards")
		s.respondError(w, http.StatusInternalServerError, "failed to list leaderboards")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getLeaderboard(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	result, err := s.leaderboardService.GetLeaderboard(r.Context(), leaderboardID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard")
		return
	}

	if result == nil {
		s.respondError(w, http.StatusNotFound, "leaderboard not found")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getLeaderboardTop(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	limit := 100
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 1000 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	result, err := s.leaderboardService.GetLeaderboardTop(r.Context(), leaderboardID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard top")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard top")
		return
	}

	if result == nil {
		s.respondError(w, http.StatusNotFound, "leaderboard not found")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) getLeaderboardPlayerRank(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]
	playerIDStr := vars["playerId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	rank, err := s.leaderboardService.GetLeaderboardPlayerRank(r.Context(), leaderboardID, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard player rank")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard player rank")
		return
	}

	if rank == nil {
		s.respondError(w, http.StatusNotFound, "player rank not found")
		return
	}

	s.respondJSON(w, http.StatusOK, rank)
}

func (s *HTTPServer) getLeaderboardRankAround(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	leaderboardIDStr := vars["leaderboardId"]
	playerIDStr := vars["playerId"]

	leaderboardID, err := uuid.Parse(leaderboardIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid leaderboard_id")
		return
	}

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	rangeSize := 5
	if rangeStr := r.URL.Query().Get("range"); rangeStr != "" {
		if r, err := strconv.Atoi(rangeStr); err == nil && r > 0 && r <= 50 {
			rangeSize = r
		}
	}

	entries, err := s.leaderboardService.GetLeaderboardRankAround(r.Context(), leaderboardID, playerID, rangeSize)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get leaderboard rank around")
		s.respondError(w, http.StatusInternalServerError, "failed to get leaderboard rank around")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"entries": entries,
		"total":   len(entries),
	})
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ok"})
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

