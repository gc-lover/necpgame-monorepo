package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/inventory-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr             string
	router           *mux.Router
	inventoryService *InventoryService
	logger           *logrus.Logger
	server           *http.Server
}

func NewHTTPServer(addr string, inventoryService *InventoryService) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:             addr,
		router:           router,
		inventoryService: inventoryService,
		logger:           GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()
	
	api.HandleFunc("/inventory/{characterId}", server.getInventory).Methods("GET")
	api.HandleFunc("/inventory/{characterId}/items", server.addItem).Methods("POST")
	api.HandleFunc("/inventory/{characterId}/items/{itemId}", server.removeItem).Methods("DELETE")
	api.HandleFunc("/inventory/{characterId}/equip", server.equipItem).Methods("POST")
	api.HandleFunc("/inventory/{characterId}/unequip/{itemId}", server.unequipItem).Methods("POST")
	
	router.HandleFunc("/health", server.healthCheck).Methods("GET")

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func (s *HTTPServer) getInventory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	response, err := s.inventoryService.GetInventory(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get inventory")
		s.respondError(w, http.StatusInternalServerError, "failed to get inventory")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) addItem(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	var req models.AddItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.ItemID == "" || req.StackCount <= 0 {
		s.respondError(w, http.StatusBadRequest, "invalid item id or stack count")
		return
	}

	err = s.inventoryService.AddItem(r.Context(), characterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to add item")
		if strings.Contains(err.Error(), "not found") || strings.Contains(err.Error(), "full") {
			s.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to add item")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) removeItem(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]
	itemIDStr := vars["itemId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	itemID, err := uuid.Parse(itemIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid item id")
		return
	}

	err = s.inventoryService.RemoveItem(r.Context(), characterID, itemID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove item")
		if strings.Contains(err.Error(), "not found") {
			s.respondError(w, http.StatusNotFound, err.Error())
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to remove item")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) equipItem(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	var req models.EquipItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.ItemID == "" || req.EquipSlot == "" {
		s.respondError(w, http.StatusBadRequest, "invalid item id or equip slot")
		return
	}

	err = s.inventoryService.EquipItem(r.Context(), characterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to equip item")
		if strings.Contains(err.Error(), "not found") || strings.Contains(err.Error(), "cannot be equipped") {
			s.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to equip item")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) unequipItem(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]
	itemIDStr := vars["itemId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	itemID, err := uuid.Parse(itemIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid item id")
		return
	}

	err = s.inventoryService.UnequipItem(r.Context(), characterID, itemID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to unequip item")
		if strings.Contains(err.Error(), "not found") {
			s.respondError(w, http.StatusNotFound, err.Error())
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to unequip item")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		next.ServeHTTP(w, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":     r.Method,
			"path":       r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":     http.StatusOK,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		
		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
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

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}
