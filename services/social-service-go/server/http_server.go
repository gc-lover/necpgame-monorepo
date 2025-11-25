package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          *mux.Router
	server          *http.Server
	logger          *logrus.Logger
	friendsService  FriendsServiceInterface
}

func NewHTTPServer(addr string, friendsService FriendsServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	
	s := &HTTPServer{
		addr:           addr,
		router:         router,
		logger:         GetLogger(),
		friendsService: friendsService,
	}
	
	router.Use(s.loggingMiddleware)
	router.Use(s.metricsMiddleware)
	router.Use(s.corsMiddleware)
	
	api := router.PathPrefix("/api/v1").Subrouter()
	
	RegisterFriendsHandlers(api, friendsService)
	
	router.HandleFunc("/health", s.healthCheck).Methods("GET")
	router.HandleFunc("/metrics", s.metricsHandler).Methods("GET")
	
	s.server = &http.Server{
		Addr:         addr,
		Handler:      router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
	
	return s
}

func (s *HTTPServer) Start() error {
	s.logger.WithField("addr", s.addr).Info("Starting HTTP server")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	s.logger.Info("Stopping HTTP server")
	return s.server.Shutdown(ctx)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func (s *HTTPServer) metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# Metrics endpoint\n"))
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		s.logger.WithFields(logrus.Fields{
			"method":   r.Method,
			"path":     r.URL.Path,
			"duration": time.Since(start),
		}).Info("Request completed")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
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
