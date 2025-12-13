// Issue: #1594 - HTTP server with ogen
package server

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/jackc/pgx/v5/pgxpool"
)

type HTTPServerOgen struct {
	addr   string
	server *http.Server
	router *http.ServeMux
}

func NewHTTPServerOgen(db *pgxpool.Pool, addr string) *HTTPServerOgen {
	router := http.NewServeMux()

	handlers := NewMarketHandlersOgen(db)
	security := NewSecurityHandler()

	srv, err := api.NewServer(handlers, security)
	if err != nil {
		log.Fatal(err)
	}

	var handler http.Handler = srv
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = RecoveryMiddleware(handler)
	router.Handle("/api/v1/", handler)
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

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
