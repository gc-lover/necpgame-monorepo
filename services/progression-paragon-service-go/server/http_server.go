package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/necpgame/progression-paragon-service-go/pkg/api"
	prestigeapi "github.com/necpgame/progression-paragon-service-go/pkg/api/prestige"
	masteryapi "github.com/necpgame/progression-paragon-service-go/pkg/api/mastery"
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

	apiRouter := router.PathPrefix("/api/v1/progression").Subrouter()

	paragonHandlers := NewParagonHandlers(paragonService)
	api.HandlerFromMux(paragonHandlers, apiRouter)

	prestigeHandlers := NewPrestigeHandlers(prestigeService)
	prestigeapi.HandlerFromMux(prestigeHandlers, apiRouter)

	masteryHandlers := NewMasteryHandlers(masteryService)
	masteryapi.HandlerFromMux(masteryHandlers, apiRouter)

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

