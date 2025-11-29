package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr           string
	router         *mux.Router
	paragonService ParagonServiceInterface
	prestigeService PrestigeServiceInterface
	masteryService MasteryServiceInterface
	logger         *logrus.Logger
	server         *http.Server
}

func NewHTTPServer(addr string, paragonService ParagonServiceInterface, prestigeService PrestigeServiceInterface, masteryService MasteryServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:           addr,
		router:         router,
		paragonService: paragonService,
		prestigeService: prestigeService,
		masteryService: masteryService,
		logger:         GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/progression").Subrouter()

	paragonHandlers := NewParagonHandlers(paragonService)
	api.HandleFunc("/paragon/levels", paragonHandlers.GetParagonLevels).Methods("GET")
	api.HandleFunc("/paragon/distribute", paragonHandlers.DistributeParagonPoints).Methods("POST")
	api.HandleFunc("/paragon/stats", paragonHandlers.GetParagonStats).Methods("GET")

	prestigeHandlers := NewPrestigeHandlers(prestigeService)
	api.HandleFunc("/prestige/info", prestigeHandlers.GetPrestigeInfo).Methods("GET")
	api.HandleFunc("/prestige/reset", prestigeHandlers.ResetPrestige).Methods("POST")
	api.HandleFunc("/prestige/bonuses", prestigeHandlers.GetPrestigeBonuses).Methods("GET")

	masteryHandlers := NewMasteryHandlers(masteryService)
	api.HandleFunc("/mastery/levels", masteryHandlers.GetMasteryLevels).Methods("GET")
	api.HandleFunc("/mastery/{type}/progress", masteryHandlers.GetMasteryProgress).Methods("GET")
	api.HandleFunc("/mastery/rewards", masteryHandlers.GetMasteryRewards).Methods("GET")

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
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

