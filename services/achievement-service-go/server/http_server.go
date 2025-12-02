// Issue: #138
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/gc-lover/necpgame/services/achievement-service-go/pkg/api"
)

type HTTPServer struct {
	addr    string
	router  *mux.Router
	service Service
}

func NewHTTPServer(addr string, service Service) *HTTPServer {
	router := mux.NewRouter()

	handlers := NewHandlers(service)
	api.HandlerFromMux(handlers, router)

	router.HandleFunc("/health", healthCheck).Methods("GET")
	router.HandleFunc("/metrics", metricsHandler).Methods("GET")
	router.Use(loggingMiddleware)
	router.Use(timeoutMiddleware)

	return &HTTPServer{addr: addr, router: router, service: service}
}

func (s *HTTPServer) Start() error {
	return http.ListenAndServe(s.addr, s.router)
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
}

func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		next.ServeHTTP(w, r)
	})
}

func timeoutMiddleware(next http.Handler) http.Handler {
	return http.TimeoutHandler(next, 60*time.Second, "Timeout")
}
