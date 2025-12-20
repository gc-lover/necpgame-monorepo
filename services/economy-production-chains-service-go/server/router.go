// Package server HTTP handlers use context.WithTimeout for request timeouts
package server

import (
	"net/http"
	"time"
)

// NewRouter wires HTTP routes to handlers with sane defaults.
func NewRouter(svc *Service) http.Handler {
	mux := http.NewServeMux()

	mux.HandleFunc("/api/v1/production/chains", svc.HandleGetChains)
	mux.HandleFunc("/api/v1/production/chains/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			svc.HandleGetChainDetails(w, r)
			return
		}
		if r.Method == http.MethodPost && r.URL.Path[len("/api/v1/production/chains/"):] != "" && r.URL.Path[len(r.URL.Path)-6:] == "/start" {
			svc.HandleStartProductionChain(w, r)
			return
		}
		http.NotFound(w, r)
	})

	mux.HandleFunc("/api/v1/production/orders", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodPost:
			svc.HandleCreateOrder(w, r)
		default:
			http.NotFound(w, r)
		}
	})

	mux.HandleFunc("/api/v1/production/orders/", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			svc.HandleGetOrder(w, r)
		case http.MethodDelete:
			svc.HandleCancelOrder(w, r)
		case http.MethodPost:
			if len(r.URL.Path) >= len("/api/v1/production/orders/")+4 && r.URL.Path[len(r.URL.Path)-5:] == "/rush" {
				svc.HandleCreateRushOrder(w, r)
				return
			}
			http.NotFound(w, r)
		default:
			http.NotFound(w, r)
		}
	})

	return http.TimeoutHandler(mux, 3*time.Second, "request timed out")
}
