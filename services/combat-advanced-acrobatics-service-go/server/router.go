package server

import (
	"context"
	"net/http"
	"time"
)

// NewRouter wires HTTP routes to service handlers.
func NewRouter(svc *Service) http.Handler {
	mux := http.NewServeMux()

	withTimeout := func(h http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
			defer cancel()
			h.ServeHTTP(w, r.WithContext(ctx))
		}
	}

	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/air-dash", withTimeout(svc.HandlePerformAirDash))
	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/air-dash/available", withTimeout(svc.HandleGetAirDashAvailability))
	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/air-dash/charges", withTimeout(svc.HandleGetAirDashCharges))

	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/wall-kick", withTimeout(svc.HandlePerformWallKick))
	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/wall-kick/available", withTimeout(svc.HandleGetWallKickAvailability))

	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/vault", withTimeout(svc.HandlePerformVault))
	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/vault/obstacles", withTimeout(svc.HandleListVaultObstacles))

	mux.HandleFunc("/api/v1/gameplay/combat/acrobatics/advanced/state", withTimeout(svc.HandleGetAdvancedState))

	return mux
}
