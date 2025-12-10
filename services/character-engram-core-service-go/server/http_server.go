// Issue: ogen migration
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/character-engram-core-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type requestIDKey struct{}

type responseRecorder struct {
	http.ResponseWriter
	status int
}

func (r *responseRecorder) WriteHeader(statusCode int) {
	if r.status == 0 {
		r.status = statusCode
	}
	r.ResponseWriter.WriteHeader(statusCode)
}

func (r *responseRecorder) Write(b []byte) (int, error) {
	if r.status == 0 {
		r.status = http.StatusOK
	}
	return r.ResponseWriter.Write(b)
}

type HTTPServer struct {
	addr   string
	router *http.ServeMux
	logger *logrus.Logger
	server *http.Server
}

func NewHTTPServer(addr string) *HTTPServer {
	router := http.NewServeMux()

	server := &HTTPServer{
		addr:   addr,
		router: router,
		logger: GetLogger(),
	}

	handlers := NewHandlers()
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	var handler http.Handler = ogenServer
	handler = requestIDMiddleware(handler)
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)
	router.Handle("/api/v1/", handler)

	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	server.server = &http.Server{
		Addr:         addr,
		Handler:      router,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	return server
}

func (s *HTTPServer) ListenAndServe() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)
		next.ServeHTTP(ww, r)
		s.logger.WithFields(logrus.Fields{
			"method":    r.Method,
			"path":      r.URL.Path,
			"status":    ww.Status(),
			"duration":  time.Since(start).String(),
			"requestID": middleware.GetReqID(r.Context()),
		}).Info("Request completed")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)
		next.ServeHTTP(ww, r)
		RecordRequest(r.Method, r.URL.Path, http.StatusText(ww.Status()))
		RecordRequestDuration(r.Method, r.URL.Path, time.Since(start).Seconds())
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Accept, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}
		next.ServeHTTP(w, r)
	})
}

func respondJSON(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		GetLogger().WithError(err).Error("Failed to encode JSON response")
	}
}

func respondError(w http.ResponseWriter, statusCode int, err error, details string) {
	GetLogger().WithError(err).Error(details)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	errorResponse := api.Error{
		Code:    api.OptNilString{},
		Message: details,
	}
	if err := json.NewEncoder(w).Encode(errorResponse); err != nil {
		GetLogger().WithError(err).Error("Failed to encode JSON error response")
	}
}
