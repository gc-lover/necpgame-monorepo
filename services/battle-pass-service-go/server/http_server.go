package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/battle-pass-service-go/models"
	"github.com/necpgame/battle-pass-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          *mux.Router
	battlePassService BattlePassServiceInterface
	logger          *logrus.Logger
	server          *http.Server
	jwtValidator    *JwtValidator
	authEnabled     bool
}

func NewHTTPServer(addr string, battlePassService BattlePassServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		battlePassService: battlePassService,
		logger:          GetLogger(),
		jwtValidator:    jwtValidator,
		authEnabled:     authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	apiRouter := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		apiRouter.Use(server.authMiddleware)
	}

	handlers := NewBattlePassHandlers(battlePassService)
	api.HandlerWithOptions(handlers, api.GorillaServerOptions{
		BaseRouter: apiRouter,
	})

	apiRouter.HandleFunc("/battle-pass/progress/xp", server.awardBattlePassXP).Methods("POST")
	apiRouter.HandleFunc("/battle-pass/rewards", server.getSeasonRewards).Methods("GET")
	apiRouter.HandleFunc("/battle-pass/rewards/claim", server.claimReward).Methods("POST")
	apiRouter.HandleFunc("/battle-pass/challenges/weekly", server.getWeeklyChallenges).Methods("GET")
	apiRouter.HandleFunc("/battle-pass/challenges/season/{player_id}", server.getSeasonChallenges).Methods("GET")
	apiRouter.HandleFunc("/battle-pass/challenges/{challengeId}/complete", server.completeChallenge).Methods("POST")
	apiRouter.HandleFunc("/battle-pass/season/{season_id}", server.getSeasonInfo).Methods("GET")
	apiRouter.HandleFunc("/battle-pass/season/create", server.createSeason).Methods("POST")

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


func (s *HTTPServer) awardBattlePassXP(w http.ResponseWriter, r *http.Request) {
	var req struct {
		CharacterID uuid.UUID        `json:"character_id"`
		XPAmount    int              `json:"xp_amount"`
		Source      models.XPSource  `json:"source"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.XPAmount <= 0 {
		s.respondError(w, http.StatusBadRequest, "xp_amount must be positive")
		return
	}

	progress, err := s.battlePassService.AwardXP(r.Context(), req.CharacterID, req.XPAmount, req.Source)
	if err != nil {
		s.logger.WithError(err).Error("Failed to award XP")
		s.respondError(w, http.StatusInternalServerError, "failed to award XP")
		return
	}

	s.respondJSON(w, http.StatusOK, progress)
}


func (s *HTTPServer) getSeasonRewards(w http.ResponseWriter, r *http.Request) {
	seasonIDStr := r.URL.Query().Get("season_id")
	if seasonIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "season_id is required")
		return
	}

	seasonID, err := uuid.Parse(seasonIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid season_id")
		return
	}

	rewards, err := s.battlePassService.GetRewards(r.Context(), seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get rewards")
		s.respondError(w, http.StatusInternalServerError, "failed to get rewards")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{"rewards": rewards})
}

func (s *HTTPServer) claimReward(w http.ResponseWriter, r *http.Request) {
	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
		RewardID    uuid.UUID `json:"reward_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err := s.battlePassService.ClaimReward(r.Context(), req.CharacterID, req.RewardID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to claim reward")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{"success": true})
}

func (s *HTTPServer) getWeeklyChallenges(w http.ResponseWriter, r *http.Request) {
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

	challenges, err := s.battlePassService.GetWeeklyChallenges(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get weekly challenges")
		s.respondError(w, http.StatusInternalServerError, "failed to get weekly challenges")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{"challenges": challenges})
}

func (s *HTTPServer) getSeasonChallenges(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var seasonID *uuid.UUID
	seasonIDStr := r.URL.Query().Get("season_id")
	if seasonIDStr != "" {
		parsedSeasonID, err := uuid.Parse(seasonIDStr)
		if err != nil {
			s.respondError(w, http.StatusBadRequest, "invalid season_id")
			return
		}
		seasonID = &parsedSeasonID
	}

	challenges, actualSeasonID, err := s.battlePassService.GetSeasonChallenges(r.Context(), playerID, seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get season challenges")
		if err.Error() == "no active season" {
			s.respondError(w, http.StatusNotFound, err.Error())
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to get season challenges")
		return
	}

	response := map[string]interface{}{
		"player_id":  playerID.String(),
		"season_id":  actualSeasonID.String(),
		"challenges": challenges,
		"total":      len(challenges),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) completeChallenge(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	challengeIDStr := vars["challengeId"]

	challengeID, err := uuid.Parse(challengeIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid challenge_id")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.CharacterID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	err = s.battlePassService.CompleteChallenge(r.Context(), req.CharacterID, challengeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete challenge")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	challenges, err := s.battlePassService.GetWeeklyChallenges(r.Context(), req.CharacterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get updated challenges")
		s.respondError(w, http.StatusInternalServerError, "failed to get updated challenges")
		return
	}

	var completedChallenge *models.WeeklyChallenge
	for i := range challenges {
		if challenges[i].ID == challengeID {
			completedChallenge = &challenges[i]
			break
		}
	}

	if completedChallenge == nil {
		s.respondError(w, http.StatusNotFound, "challenge not found")
		return
	}

	s.respondJSON(w, http.StatusOK, completedChallenge)
}

func (s *HTTPServer) getSeasonInfo(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	seasonIDStr := vars["season_id"]

	seasonID, err := uuid.Parse(seasonIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid season_id")
		return
	}

	season, err := s.battlePassService.GetSeasonByID(r.Context(), seasonID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get season")
		s.respondError(w, http.StatusInternalServerError, "failed to get season")
		return
	}

	if season == nil {
		s.respondError(w, http.StatusNotFound, "season not found")
		return
	}

	s.respondJSON(w, http.StatusOK, season)
}

func (s *HTTPServer) createSeason(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Name      string    `json:"name"`
		StartDate time.Time `json:"start_date"`
		EndDate   time.Time `json:"end_date"`
		MaxLevel  int       `json:"max_level"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	season := &models.BattlePassSeason{
		Name:      req.Name,
		StartDate: req.StartDate,
		EndDate:   req.EndDate,
		MaxLevel:  req.MaxLevel,
		IsActive:  true,
	}

	err := s.battlePassService.CreateSeason(r.Context(), season)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create season")
		s.respondError(w, http.StatusInternalServerError, "failed to create season")
		return
	}

	s.respondJSON(w, http.StatusCreated, season)
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

