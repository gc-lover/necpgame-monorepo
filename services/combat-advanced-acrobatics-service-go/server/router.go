package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

// NewRouter wires HTTP routes to service handlers.
func NewRouter(svc *Service) http.Handler {
	r := chi.NewRouter()
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(2 * time.Second))

	r.Post("/api/v1/gameplay/combat/acrobatics/air-dash", svc.HandlePerformAirDash)
	r.Get("/api/v1/gameplay/combat/acrobatics/air-dash/available", svc.HandleGetAirDashAvailability)
	r.Get("/api/v1/gameplay/combat/acrobatics/air-dash/charges", svc.HandleGetAirDashCharges)

	r.Post("/api/v1/gameplay/combat/acrobatics/wall-kick", svc.HandlePerformWallKick)
	r.Get("/api/v1/gameplay/combat/acrobatics/wall-kick/available", svc.HandleGetWallKickAvailability)

	r.Post("/api/v1/gameplay/combat/acrobatics/vault", svc.HandlePerformVault)
	r.Get("/api/v1/gameplay/combat/acrobatics/vault/obstacles", svc.HandleListVaultObstacles)

	r.Get("/api/v1/gameplay/combat/acrobatics/advanced/state", svc.HandleGetAdvancedState)

	return r
}

