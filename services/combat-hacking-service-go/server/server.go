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

	// Enemy hacking routes
	mux.HandleFunc("/hacking/enemies/scan", h.ScanEnemyCyberware)
	mux.HandleFunc("/hacking/enemies/hack", h.HackEnemyCyberware)

	// Device hacking routes
	mux.HandleFunc("/hacking/devices/scan", h.ScanDevice)
	mux.HandleFunc("/hacking/devices/hack", h.HackDevice)

	// Network hacking routes
	mux.HandleFunc("/hacking/networks/infiltrate", h.InfiltrateNetwork)
	mux.HandleFunc("/hacking/networks/extract", h.ExtractData)

	// Combat support route
	mux.HandleFunc("/hacking/combat/support", h.ActivateCombatSupport)

	// Anti-cheat validation route
	mux.HandleFunc("/hacking/validate", h.ValidateHackingAttempt)

	// Active hacks management routes
	mux.HandleFunc("/hacking/active/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			// Extract player_id from query or assume it's in the path
			playerID := r.URL.Query().Get("player_id")
			if playerID == "" {
				// Try to extract from path if it's /hacking/active/{player_id}
				path := r.URL.Path
				if len(path) > len("/hacking/active/") {
					playerID = path[len("/hacking/active/"):]
					if playerID != "" && playerID[len(playerID)-1] == '/' {
						playerID = playerID[:len(playerID)-1]
					}
				}
			}
			if playerID != "" {
				r = r.WithContext(context.WithValue(r.Context(), "player_id", playerID))
			}
			h.GetActiveHacks(w, r)
		} else {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	// Screen hack and glitch routes
	mux.HandleFunc("/hacking/screen-hack/blind", h.ActivateScreenHackBlind)
	mux.HandleFunc("/hacking/glitch-doubles/activate", h.ActivateGlitchDoubles)

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

