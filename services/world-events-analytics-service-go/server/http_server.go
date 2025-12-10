// Issue: #44
package server

import (
	"context"
	"net/http"

	api "github.com/gc-lover/necpgame-monorepo/services/world-events-analytics-service-go/pkg/api"
	"go.uber.org/zap"
)

type HTTPServer struct {
	addr   string
	server *http.Server
	logger *zap.Logger
}

func NewHTTPServer(addr string, handlers *Handlers, logger *zap.Logger) *HTTPServer {
	mux := http.NewServeMux()

	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		logger.Fatal("Failed to create ogen server", zap.Error(err))
	}

	var handler http.Handler = ogenServer
	handler = CORSMiddleware()(handler)
	handler = LoggingMiddleware(logger)(handler)

	mux.Handle("/api/v1", handler)

	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:    addr,
			Handler: mux,
		},
		logger: logger,
	}
}

func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}









