// Issue: #141888786, ogen migration
package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	clanwarapi "github.com/necpgame/clan-war-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type ClanWarServiceInterface interface {
	DeclareWar(ctx context.Context, req *models.DeclareWarRequest) (*models.ClanWar, error)
	GetWar(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error)
	ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error)
	StartWar(ctx context.Context, warID uuid.UUID) error
	CompleteWar(ctx context.Context, warID uuid.UUID) error
	CreateBattle(ctx context.Context, req *models.CreateBattleRequest) (*models.WarBattle, error)
	GetBattle(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error)
	ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error)
	StartBattle(ctx context.Context, battleID uuid.UUID) error
	UpdateBattleScore(ctx context.Context, req *models.UpdateBattleScoreRequest) error
	CompleteBattle(ctx context.Context, battleID uuid.UUID) error
	GetTerritory(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error)
	ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error)
}

type HTTPServer struct {
	addr           string
	router         *chi.Mux
	clanWarService ClanWarServiceInterface
	logger         *logrus.Logger
	server         *http.Server
	jwtValidator   *JwtValidator
	authEnabled    bool
}

func NewHTTPServer(addr string, clanWarService ClanWarServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := chi.NewRouter()
	server := &HTTPServer{
		addr:           addr,
		router:         router,
		clanWarService: clanWarService,
		logger:         GetLogger(),
		jwtValidator:   jwtValidator,
		authEnabled:    authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)
	router.Use(middleware.Recoverer)

	// Initialize ogen handlers and security handler
	ogenHandlers := NewHandlers(clanWarService)
	ogenSecurity := NewSecurityHandler(jwtValidator, authEnabled)

	// Create ogen server
	ogenServer, err := clanwarapi.NewServer(ogenHandlers, ogenSecurity)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	router.Mount("/api/v1/world/clan-war", ogenServer)

	// Legacy handlers for battles and territories (not in core spec yet)
	router.Route("/api/v1/clan-war", func(r chi.Router) {
		r.Post("/battles", server.createBattle)
		r.Get("/battles", server.listBattles)
		r.Get("/battles/{battle_id}", server.getBattle)
		r.Post("/battles/{battle_id}/start", server.startBattle)
		r.Post("/battles/{battle_id}/complete", server.completeBattle)
		r.Put("/battles/{battle_id}/score", server.updateBattleScore)
		r.Get("/territories", server.listTerritories)
		r.Get("/territories/{territory_id}", server.getTerritory)
	})

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

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":   r.Method,
			"path":     r.URL.Path,
			"duration": duration,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		recorder := &responseRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)
		duration := time.Since(start)
		RecordRequest(r.Method, r.URL.Path, strconv.Itoa(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration.Seconds())
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusNoContent)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusInternalServerError)
		errorResponse := map[string]string{"error": "Failed to encode response"}
		json.NewEncoder(w).Encode(errorResponse)
		return
	}
	
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		s.logger.WithError(err).Error("Failed to write JSON response")
	}
}

// Legacy handlers (battles, territories) - keep for backward compatibility
func (s *HTTPServer) createBattle(w http.ResponseWriter, r *http.Request) {
	var req models.CreateBattleRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	battle, err := s.clanWarService.CreateBattle(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create battle")
		s.respondError(w, http.StatusInternalServerError, "failed to create battle")
		return
	}

	s.respondJSON(w, http.StatusCreated, battle)
}

func (s *HTTPServer) getBattle(w http.ResponseWriter, r *http.Request) {
	battleIDStr := chi.URLParam(r, "battle_id")
	battleID, err := uuid.Parse(battleIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid battle ID")
		return
	}

	battle, err := s.clanWarService.GetBattle(r.Context(), battleID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get battle")
		s.respondError(w, http.StatusInternalServerError, "failed to get battle")
		return
	}

	s.respondJSON(w, http.StatusOK, battle)
}

func (s *HTTPServer) listBattles(w http.ResponseWriter, r *http.Request) {
	var warID *uuid.UUID
	if warIDStr := r.URL.Query().Get("war_id"); warIDStr != "" {
		id, err := uuid.Parse(warIDStr)
		if err == nil {
			warID = &id
		}
	}

	var status *models.BattleStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		s := models.BattleStatus(statusStr)
		status = &s
	}

	limit, _ := strconv.Atoi(r.URL.Query().Get("limit"))
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}

	offset, _ := strconv.Atoi(r.URL.Query().Get("offset"))
	if offset < 0 {
		offset = 0
	}

	battles, total, err := s.clanWarService.ListBattles(r.Context(), warID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list battles")
		s.respondError(w, http.StatusInternalServerError, "failed to list battles")
		return
	}

	s.respondJSON(w, http.StatusOK, models.BattleListResponse{
		Battles: battles,
		Total:   total,
	})
}

func (s *HTTPServer) startBattle(w http.ResponseWriter, r *http.Request) {
	battleIDStr := chi.URLParam(r, "battle_id")
	battleID, err := uuid.Parse(battleIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid battle ID")
		return
	}

	if err := s.clanWarService.StartBattle(r.Context(), battleID); err != nil {
		s.logger.WithError(err).Error("Failed to start battle")
		s.respondError(w, http.StatusInternalServerError, "failed to start battle")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "battle started"})
}

func (s *HTTPServer) completeBattle(w http.ResponseWriter, r *http.Request) {
	battleIDStr := chi.URLParam(r, "battle_id")
	battleID, err := uuid.Parse(battleIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid battle ID")
		return
	}

	if err := s.clanWarService.CompleteBattle(r.Context(), battleID); err != nil {
		s.logger.WithError(err).Error("Failed to complete battle")
		s.respondError(w, http.StatusInternalServerError, "failed to complete battle")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "battle completed"})
}

func (s *HTTPServer) updateBattleScore(w http.ResponseWriter, r *http.Request) {
	battleIDStr := chi.URLParam(r, "battle_id")
	battleID, err := uuid.Parse(battleIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid battle ID")
		return
	}

	var req models.UpdateBattleScoreRequest
	req.BattleID = battleID
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := s.clanWarService.UpdateBattleScore(r.Context(), &req); err != nil {
		s.logger.WithError(err).Error("Failed to update battle score")
		s.respondError(w, http.StatusInternalServerError, "failed to update battle score")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "score updated"})
}

func (s *HTTPServer) getTerritory(w http.ResponseWriter, r *http.Request) {
	territoryIDStr := chi.URLParam(r, "territory_id")
	territoryID, err := uuid.Parse(territoryIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid territory ID")
		return
	}

	territory, err := s.clanWarService.GetTerritory(r.Context(), territoryID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get territory")
		s.respondError(w, http.StatusInternalServerError, "failed to get territory")
		return
	}

	s.respondJSON(w, http.StatusOK, territory)
}

func (s *HTTPServer) listTerritories(w http.ResponseWriter, r *http.Request) {
	var ownerGuildID *uuid.UUID
	if ownerGuildIDStr := r.URL.Query().Get("owner_guild_id"); ownerGuildIDStr != "" {
		id, err := uuid.Parse(ownerGuildIDStr)
		if err == nil {
			ownerGuildID = &id
		}
	}

	limit, _ := strconv.Atoi(r.URL.Query().Get("limit"))
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}

	offset, _ := strconv.Atoi(r.URL.Query().Get("offset"))
	if offset < 0 {
		offset = 0
	}

	territories, total, err := s.clanWarService.ListTerritories(r.Context(), ownerGuildID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list territories")
		s.respondError(w, http.StatusInternalServerError, "failed to list territories")
		return
	}

	s.respondJSON(w, http.StatusOK, models.TerritoryListResponse{
		Territories: territories,
		Total:       total,
	})
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

type responseRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (r *responseRecorder) WriteHeader(statusCode int) {
	r.statusCode = statusCode
	r.ResponseWriter.WriteHeader(statusCode)
}
