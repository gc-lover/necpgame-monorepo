// Issue: #141888104
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type MovementServiceInterface interface {
	GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error)
	SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error)
	GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error)
}

type HTTPServer struct {
	addr            string
	router          *http.ServeMux
	movementService MovementServiceInterface
	logger          *logrus.Logger
	server          *http.Server
}

func NewHTTPServer(addr string, movementService MovementServiceInterface) *HTTPServer {
	router := http.NewServeMux()

	logger := GetLogger()
	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(movementService)

	// Integration with ogen
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1
	var handler http.Handler = ogenServer
	handler = loggingMiddleware(logger)(handler)
	handler = metricsMiddleware()(handler)
	handler = corsMiddleware()(handler)
	router.Handle("/api/v1/", handler)

	router.HandleFunc("/health", healthCheck)

	server := &HTTPServer{
		addr:            addr,
		router:          router,
		movementService: movementService,
		logger:          logger,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
	}

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.logger.WithField("addr", s.addr).Info("Movement Service starting")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down Movement Service")
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			ww := &responseRecorder{ResponseWriter: w, statusCode: http.StatusOK}
			next.ServeHTTP(ww, r)
			logger.WithFields(logrus.Fields{
				"method":    r.Method,
				"path":      r.URL.Path,
				"status":    ww.statusCode,
				"duration":  time.Since(start).String(),
			}).Info("Request completed")
		})
	}
}

func metricsMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			ww := &responseRecorder{ResponseWriter: w, statusCode: http.StatusOK}
			next.ServeHTTP(ww, r)
			RecordRequest(r.Method, r.URL.Path, http.StatusText(ww.statusCode))
			RecordRequestDuration(r.Method, r.URL.Path, time.Since(start).Seconds())
		})
	}
}

func corsMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
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
}

type responseRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (rr *responseRecorder) WriteHeader(code int) {
	rr.statusCode = code
	rr.ResponseWriter.WriteHeader(code)
}
