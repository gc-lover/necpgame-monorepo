package server

import (
	"context"
	"log"
	"net/http"

	"combat-hacking-service-go/pkg/handlers"
)

type Server struct {
	httpServer *http.Server
	logger     *log.Logger
}

func NewServer(h *handlers.Handlers, logger *log.Logger) *Server {
	mux := http.NewServeMux()

	// Health check
	mux.HandleFunc("/health", h.HealthCheck)

	// API routes
	mux.HandleFunc("/hacking/screen-hack/blind", h.ActivateScreenHackBlind)
	mux.HandleFunc("/hacking/glitch-doubles/activate", h.ActivateGlitchDoubles)

	// Other existing routes can be added here

	return &Server{
		httpServer: &http.Server{
			Addr:    ":8084",
			Handler: mux,
		},
		logger: logger,
	}
}

func (s *Server) Start(addr string) error {
	s.httpServer.Addr = addr
	return s.httpServer.ListenAndServe()
}

func (s *Server) Shutdown(ctx context.Context) error {
	return s.httpServer.Shutdown(ctx)
}

// Issue: #143875347, #143875814

