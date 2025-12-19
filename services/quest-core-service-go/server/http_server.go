package server

import (
	"context"
	"encoding/json"
	"net/http"
	"os"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/quest-core-service-go/pkg/api"
	"github.com/redis/go-redis/v9"
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

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
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

	// Issue: #1609 - Initialize Redis client for caching
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379")
	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		server.logger.WithError(err).Warn("Failed to parse Redis URL, caching disabled")
		redisOpts = nil
	}
	var redisClient *redis.Client
	if redisOpts != nil {
		redisClient = redis.NewClient(redisOpts)
	}

	handlers := NewHandlers(redisClient)

	// Integration with ogen
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	var handler http.Handler = ogenServer
	handler = requestIDMiddleware(handler)
	handler = server.loggingMiddleware(handler)
	handler = server.metricsMiddleware(handler)
	handler = server.corsMiddleware(handler)

	// Mount ogen server under /api/v1
	router.Handle("/api/v1/", handler)

	// Manual route for quest content reload (not in ogen spec yet)
	router.HandleFunc("/api/v1/gameplay/quests/content/reload", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			return
		}

		var req ReloadQuestContentRequest
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			http.Error(w, "Invalid JSON: "+err.Error(), http.StatusBadRequest)
			return
		}

		response, err := handlers.ReloadQuestContent(r.Context(), &req)
		if err != nil {
			http.Error(w, "Internal error: "+err.Error(), http.StatusInternalServerError)
			return
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	})

	server.server = &http.Server{
		Addr:         addr,
		Handler:      router,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	return server
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := &responseRecorder{ResponseWriter: w}
		next.ServeHTTP(ww, r)
		status := ww.status
		if status == 0 {
			status = http.StatusOK
		}
		s.logger.WithFields(logrus.Fields{
			"method":    r.Method,
			"path":      r.URL.Path,
			"status":    status,
			"duration":  time.Since(start).String(),
			"requestID": getRequestID(r.Context()),
		}).Info("Request completed")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := &responseRecorder{ResponseWriter: w}
		next.ServeHTTP(ww, r)
		status := ww.status
		if status == 0 {
			status = http.StatusOK
		}
		RecordRequest(r.Method, r.URL.Path, http.StatusText(status))
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
	code := http.StatusText(statusCode)
	errorResponse := api.Error{
		Code:    api.NewOptNilString(code),
		Message: details,
		Error:   "error",
	}
	if err := json.NewEncoder(w).Encode(errorResponse); err != nil {
		GetLogger().WithError(err).Error("Failed to encode JSON error response")
	}
}

func requestIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		reqID := r.Header.Get("X-Request-ID")
		if reqID == "" {
			reqID = uuid.NewString()
		}
		ctx := context.WithValue(r.Context(), requestIDKey{}, reqID)
		w.Header().Set("X-Request-ID", reqID)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func getRequestID(ctx context.Context) string {
	if ctx == nil {
		return ""
	}
	if v, ok := ctx.Value(requestIDKey{}).(string); ok {
		return v
	}
	return ""
}




















