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
	addr            string
	router          *mux.Router
	referralService ReferralService
	logger          *logrus.Logger
	server          *http.Server
	jwtValidator    *JwtValidator
	authEnabled     bool
}

func NewHTTPServer(addr string, referralService ReferralService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		referralService: referralService,
		logger:          GetLogger(),
		jwtValidator:    jwtValidator,
		authEnabled:     authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/growth/referral").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.HandleFunc("/code", server.getReferralCode).Methods("GET")
	api.HandleFunc("/code/generate", server.generateReferralCode).Methods("POST")
	api.HandleFunc("/code/{code}/validate", server.validateReferralCode).Methods("GET")

	api.HandleFunc("/register", server.registerWithCode).Methods("POST")
	api.HandleFunc("/status/{player_id}", server.getReferralStatus).Methods("GET")

	api.HandleFunc("/milestones/{player_id}", server.getMilestones).Methods("GET")
	api.HandleFunc("/milestones/{milestone_id}/claim", server.claimMilestoneReward).Methods("POST")

	api.HandleFunc("/rewards/distribute", server.distributeRewards).Methods("POST")
	api.HandleFunc("/rewards/history/{player_id}", server.getRewardHistory).Methods("GET")

	api.HandleFunc("/stats/{player_id}", server.getReferralStats).Methods("GET")
	api.HandleFunc("/stats/public/{code}", server.getPublicReferralStats).Methods("GET")

	api.HandleFunc("/leaderboard", server.getLeaderboard).Methods("GET")
	api.HandleFunc("/leaderboard/{player_id}/position", server.getLeaderboardPosition).Methods("GET")

	api.HandleFunc("/events/{player_id}", server.getEvents).Methods("GET")

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
