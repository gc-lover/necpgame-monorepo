// Issue: #1599 - ogen migration
package server

import (
	"context"
	"net/http"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/mail-service-go/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

type HTTPServer struct {
	addr    string
	router  *http.ServeMux
	server  *http.Server
	service Service
}

func NewHTTPServer(addr string, service Service, jwtValidator *JwtValidator) *HTTPServer {
	router := http.NewServeMux()

	handlers := NewHandlers(service)
	secHandler := NewSecurityHandler(jwtValidator)

	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = ogenServer
	handler = corsMiddleware(handler)
	router.Handle("/api/v1/", handler)
	router.Handle("/metrics", promhttp.Handler())
	router.HandleFunc("/health", healthCheck)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
		service: service,
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func corsMiddleware(next http.Handler) http.Handler {
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









