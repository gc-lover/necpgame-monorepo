// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #141888786, ogen migration
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	clanwarapi "github.com/necpgame/clan-war-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type requestIDKey struct{}

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
	router         *http.ServeMux
	clanWarService ClanWarServiceInterface
	logger         *logrus.Logger
	server         *http.Server
	jwtValidator   *JwtValidator
	authEnabled    bool
}

func NewHTTPServer(addr string, clanWarService ClanWarServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := http.NewServeMux()
	server := &HTTPServer{
		addr:           addr,
		router:         router,
		clanWarService: clanWarService,
		logger:         GetLogger(),
		jwtValidator:   jwtValidator,
		authEnabled:    authEnabled,
	}

	// Initialize ogen handlers and security handler
	ogenHandlers := NewHandlers(clanWarService)
	ogenSecurity := NewSecurityHandler(jwtValidator, authEnabled)

	// Create ogen server
	ogenServer, err := clanwarapi.NewServer(ogenHandlers, ogenSecurity)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	var handler http.Handler = ogenServer
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)
	handler = requestIDMiddleware(handler)
	router.Handle("/api/v1/world/clan-war/", handler)

	// Legacy/manual war handlers for tests/compat
	router.HandleFunc("/api/v1/clan-war/wars", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodPost {
			server.declareWar(w, r)
			return
		}
		if r.Method == http.MethodGet {
			server.listWars(w, r)
			return
		}
		http.NotFound(w, r)
	})
	router.HandleFunc("/api/v1/clan-war/wars/", func(w http.ResponseWriter, r *http.Request) {
		path := strings.TrimPrefix(r.URL.Path, "/api/v1/clan-war/wars/")
		if path == "" {
			if r.Method == http.MethodGet {
				server.listWars(w, r)
				return
			}
			http.NotFound(w, r)
			return
		}
		idPart, rest, _ := strings.Cut(path, "/")
		ctx := context.WithValue(r.Context(), "war_id", idPart)
		r = r.WithContext(ctx)
		switch {
		case r.Method == http.MethodGet && rest == "":
			server.getWar(w, r)
		case r.Method == http.MethodPost && rest == "start":
			server.startWar(w, r)
		case r.Method == http.MethodPost && rest == "complete":
			server.completeWar(w, r)
		default:
			http.NotFound(w, r)
		}
	})

	// Legacy handlers for battles and territories (not in core spec yet)
	router.HandleFunc("/api/v1/clan-war/battles", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodPost {
			server.createBattle(w, r)
			return
		}
		if r.Method == http.MethodGet {
			server.listBattles(w, r)
			return
		}
		http.NotFound(w, r)
	})
	router.HandleFunc("/api/v1/clan-war/battles/", func(w http.ResponseWriter, r *http.Request) {
		path := strings.TrimPrefix(r.URL.Path, "/api/v1/clan-war/battles/")
		if path == "" {
			server.listBattles(w, r)
			return
		}
		idPart, rest, _ := strings.Cut(path, "/")
		r = r.WithContext(context.WithValue(r.Context(), "battle_id", idPart))
		switch {
		case r.Method == http.MethodGet && rest == "":
			server.getBattle(w, r)
		case r.Method == http.MethodPost && rest == "start":
			server.startBattle(w, r)
		case r.Method == http.MethodPost && rest == "complete":
			server.completeBattle(w, r)
		case r.Method == http.MethodPut && rest == "score":
			server.updateBattleScore(w, r)
		default:
			http.NotFound(w, r)
		}
	})
	router.HandleFunc("/api/v1/clan-war/territories", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			path := strings.TrimPrefix(r.URL.Path, "/api/v1/clan-war/territories")
			if path == "" || path == "/" {
				server.listTerritories(w, r)
				return
			}
			idPart := strings.TrimPrefix(path, "/")
			r = r.WithContext(context.WithValue(r.Context(), "territory_id", idPart))
			server.getTerritory(w, r)
			return
		}
		http.NotFound(w, r)
	})
	router.HandleFunc("/api/v1/clan-war/territories/", func(w http.ResponseWriter, r *http.Request) {
		path := strings.TrimPrefix(r.URL.Path, "/api/v1/clan-war/territories/")
		if path == "" {
			server.listTerritories(w, r)
			return
		}
		r = r.WithContext(context.WithValue(r.Context(), "territory_id", path))
		server.getTerritory(w, r)
	})

	router.HandleFunc("/health", server.healthCheck)

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks,
		WriteTimeout: 30 * time.Second,  // Prevent hanging writes,
		IdleTimeout:  120 * time.Second, // Keep connections alive for reuse,
	}

	errChan := make(chan error, 1)
	go func() {
			defer close(errChan)
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
		rr := &responseRecorder{ResponseWriter: w}
		next.ServeHTTP(rr, r)
		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":   r.Method,
			"path":     r.URL.Path,
			"status":   rr.statusCode,
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

func requestIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		reqID := r.Header.Get("X-Request-ID")
		if reqID == "" {
			reqID = time.Now().UTC().Format(time.RFC3339Nano)
		}
		ctx := context.WithValue(r.Context(), requestIDKey{}, reqID)
		w.Header().Set("X-Request-ID", reqID)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func queryInt(r *http.Request, key string, def int) int {
	v, _ := strconv.Atoi(r.URL.Query().Get(key))
	if v <= 0 {
		return def
	}
	return v
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
	battleIDStr, _ := r.Context().Value("battle_id").(string)
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
	battleIDStr, _ := r.Context().Value("battle_id").(string)
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
	battleIDStr, _ := r.Context().Value("battle_id").(string)
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
	battleIDStr, _ := r.Context().Value("battle_id").(string)
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
	territoryIDStr, _ := r.Context().Value("territory_id").(string)
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

func (s *HTTPServer) declareWar(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.NotFound(w, r)
		return
	}
	var req models.DeclareWarRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	war, err := s.clanWarService.DeclareWar(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to declare war")
		s.respondError(w, http.StatusInternalServerError, "failed to declare war")
		return
	}
	s.respondJSON(w, http.StatusCreated, war)
}

func (s *HTTPServer) getWar(w http.ResponseWriter, r *http.Request) {
	warIDStr, _ := r.Context().Value("war_id").(string)
	warID, err := uuid.Parse(warIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid war ID")
		return
	}
	war, err := s.clanWarService.GetWar(r.Context(), warID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get war")
		s.respondError(w, http.StatusInternalServerError, "failed to get war")
		return
	}
	s.respondJSON(w, http.StatusOK, war)
}

func (s *HTTPServer) listWars(w http.ResponseWriter, r *http.Request) {
	var guildID *uuid.UUID
	if gid := r.URL.Query().Get("guild_id"); gid != "" {
		id, err := uuid.Parse(gid)
		if err == nil {
			guildID = &id
		}
	}
	var status *models.WarStatus
	if ss := r.URL.Query().Get("status"); ss != "" {
		st := models.WarStatus(ss)
		status = &st
	}
	limit := queryInt(r, "limit", 20)
	offset := queryInt(r, "offset", 0)
	wars, total, err := s.clanWarService.ListWars(r.Context(), guildID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list wars")
		s.respondError(w, http.StatusInternalServerError, "failed to list wars")
		return
	}
	s.respondJSON(w, http.StatusOK, models.WarListResponse{
		Wars:  wars,
		Total: total,
	})
}

func (s *HTTPServer) startWar(w http.ResponseWriter, r *http.Request) {
	warIDStr, _ := r.Context().Value("war_id").(string)
	warID, err := uuid.Parse(warIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid war ID")
		return
	}
	if err := s.clanWarService.StartWar(r.Context(), warID); err != nil {
		s.logger.WithError(err).Error("Failed to start war")
		s.respondError(w, http.StatusInternalServerError, "failed to start war")
		return
	}
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "started"})
}

func (s *HTTPServer) completeWar(w http.ResponseWriter, r *http.Request) {
	warIDStr, _ := r.Context().Value("war_id").(string)
	warID, err := uuid.Parse(warIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid war ID")
		return
	}
	if err := s.clanWarService.CompleteWar(r.Context(), warID); err != nil {
		s.logger.WithError(err).Error("Failed to complete war")
		s.respondError(w, http.StatusInternalServerError, "failed to complete war")
		return
	}
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "completed"})
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




