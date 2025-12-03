// Issue: #1594 - HTTP server with ogen
package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
)

type HTTPServerOgen struct {
	addr   string
	server *http.Server
	router *chi.Mux
}

func NewHTTPServerOgen(addr string) *HTTPServerOgen {
	router := chi.NewRouter()
	
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)
	
	handlers := NewMarketHandlersOgen()
	security := NewSecurityHandler()
	
	srv, err := api.NewServer(handlers, security)
	if err != nil {
		log.Fatal(err)
	}
	
	router.Mount("/api/v1", srv)
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)
	
	return &HTTPServerOgen{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  10 * time.Second,
			WriteTimeout: 10 * time.Second,
		},
	}
}

func (s *HTTPServerOgen) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServerOgen) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
}

