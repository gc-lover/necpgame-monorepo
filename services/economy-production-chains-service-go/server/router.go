package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

// NewRouter wires HTTP routes to handlers with sane defaults.
func NewRouter(svc *Service) http.Handler {
	r := chi.NewRouter()
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(3 * time.Second))

	r.Get("/api/v1/production/chains", svc.HandleGetChains)
	r.Get("/api/v1/production/chains/{chain_id}", svc.HandleGetChainDetails)
	r.Post("/api/v1/production/chains/{chain_id}/start", svc.HandleStartProductionChain)

	r.Post("/api/v1/production/orders", svc.HandleCreateOrder)
	r.Get("/api/v1/production/orders/{order_id}", svc.HandleGetOrder)
	r.Delete("/api/v1/production/orders/{order_id}", svc.HandleCancelOrder)
	r.Post("/api/v1/production/orders/{order_id}/rush", svc.HandleCreateRushOrder)

	return r
}





