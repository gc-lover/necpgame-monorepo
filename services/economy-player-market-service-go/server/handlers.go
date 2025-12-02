// Issue: #42
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
)

type Handlers struct {
	service *PlayerMarketService
}

func NewHandlers(service *PlayerMarketService) *Handlers {
	return &Handlers{service: service}
}

// Реализация api.ServerInterface

// ListListings получает список объявлений на маркете
func (h *Handlers) ListListings(w http.ResponseWriter, r *http.Request, params api.ListListingsParams) {
	listings, err := h.service.ListListings(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, listings)
}

// CreateListing создает новое объявление
func (h *Handlers) CreateListing(w http.ResponseWriter, r *http.Request) {
	var req api.CreateListingRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	listing, err := h.service.CreateListing(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, listing)
}

// GetListing получает объявление по ID
func (h *Handlers) GetListing(w http.ResponseWriter, r *http.Request, listingId string) {
	listing, err := h.service.GetListing(r.Context(), listingId)
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, listing)
}

// PurchaseListing покупает товар
func (h *Handlers) PurchaseListing(w http.ResponseWriter, r *http.Request, listingId string) {
	var req api.PurchaseListingRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	transaction, err := h.service.PurchaseListing(r.Context(), listingId, &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, transaction)
}

// Helper functions

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func respondError(w http.ResponseWriter, status int, message string) {
	error := api.Error{
		Code:    status,
		Message: message,
	}
	respondJSON(w, status, error)
}

