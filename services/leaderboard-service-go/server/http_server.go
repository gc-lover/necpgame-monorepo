package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gorilla/mux"
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

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ok"})
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

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}
