package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/housing-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr          string
	router        *mux.Router
	housingService *HousingService
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, housingService *HousingService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:          addr,
		router:        router,
		housingService: housingService,
		logger:        GetLogger(),
		jwtValidator:  jwtValidator,
		authEnabled:   authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	if authEnabled {
		router.Use(server.authMiddleware)
	}

	api := router.PathPrefix("/api/v1/housing").Subrouter()

	api.HandleFunc("/apartments", server.purchaseApartment).Methods("POST")
	api.HandleFunc("/apartments", server.listApartments).Methods("GET")
	api.HandleFunc("/apartments/{apartment_id}", server.getApartment).Methods("GET")
	api.HandleFunc("/apartments/{apartment_id}/detail", server.getApartmentDetail).Methods("GET")
	api.HandleFunc("/apartments/{apartment_id}/settings", server.updateApartmentSettings).Methods("PUT")

	api.HandleFunc("/apartments/{apartment_id}/furniture", server.placeFurniture).Methods("POST")
	api.HandleFunc("/apartments/{apartment_id}/furniture", server.listPlacedFurniture).Methods("GET")
	api.HandleFunc("/apartments/{apartment_id}/furniture/{furniture_id}", server.removeFurniture).Methods("DELETE")

	api.HandleFunc("/furniture", server.listFurnitureItems).Methods("GET")
	api.HandleFunc("/furniture/{item_id}", server.getFurnitureItem).Methods("GET")

	api.HandleFunc("/apartments/{apartment_id}/visit", server.visitApartment).Methods("POST")

	api.HandleFunc("/leaderboard/prestige", server.getPrestigeLeaderboard).Methods("GET")

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

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":   r.Method,
			"path":     r.URL.Path,
			"duration": duration,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		recorder := &responseRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)
		duration := time.Since(start)
		RecordRequest(r.Method, r.URL.Path, strconv.Itoa(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration.Seconds())
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusNoContent)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "missing authorization header")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode response")
	}
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) getCharacterID(r *http.Request) (uuid.UUID, error) {
	claims, ok := r.Context().Value("claims").(*Claims)
	if !ok || claims == nil {
		return uuid.Nil, nil
	}

	characterID, err := uuid.Parse(claims.Subject)
	if err != nil {
		return uuid.Nil, err
	}

	return characterID, nil
}

func (s *HTTPServer) purchaseApartment(w http.ResponseWriter, r *http.Request) {
	characterID, err := s.getCharacterID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req models.PurchaseApartmentRequest
	req.CharacterID = characterID
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	apartment, err := s.housingService.PurchaseApartment(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to purchase apartment")
		s.respondError(w, http.StatusInternalServerError, "failed to purchase apartment")
		return
	}

	s.respondJSON(w, http.StatusCreated, apartment)
}

func (s *HTTPServer) getApartment(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	apartment, err := s.housingService.GetApartment(r.Context(), apartmentID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get apartment")
		s.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	if apartment == nil {
		s.respondError(w, http.StatusNotFound, "apartment not found")
		return
	}

	s.respondJSON(w, http.StatusOK, apartment)
}

func (s *HTTPServer) listApartments(w http.ResponseWriter, r *http.Request) {
	var ownerID *uuid.UUID
	if ownerIDStr := r.URL.Query().Get("owner_id"); ownerIDStr != "" {
		id, err := uuid.Parse(ownerIDStr)
		if err == nil {
			ownerID = &id
		}
	}

	var ownerType *string
	if ownerTypeStr := r.URL.Query().Get("owner_type"); ownerTypeStr != "" {
		ownerType = &ownerTypeStr
	}

	var isPublic *bool
	if isPublicStr := r.URL.Query().Get("is_public"); isPublicStr != "" {
		val := isPublicStr == "true"
		isPublic = &val
	}

	limit, _ := strconv.Atoi(r.URL.Query().Get("limit"))
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}

	offset, _ := strconv.Atoi(r.URL.Query().Get("offset"))
	if offset < 0 {
		offset = 0
	}

	apartments, total, err := s.housingService.ListApartments(r.Context(), ownerID, ownerType, isPublic, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list apartments")
		s.respondError(w, http.StatusInternalServerError, "failed to list apartments")
		return
	}

	s.respondJSON(w, http.StatusOK, models.ApartmentListResponse{
		Apartments: apartments,
		Total:      total,
	})
}

func (s *HTTPServer) getApartmentDetail(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	detail, err := s.housingService.GetApartmentDetail(r.Context(), apartmentID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get apartment detail")
		s.respondError(w, http.StatusInternalServerError, "failed to get apartment detail")
		return
	}

	s.respondJSON(w, http.StatusOK, detail)
}

func (s *HTTPServer) updateApartmentSettings(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	characterID, err := s.getCharacterID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req models.UpdateApartmentSettingsRequest
	req.CharacterID = characterID
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := s.housingService.UpdateApartmentSettings(r.Context(), apartmentID, &req); err != nil {
		s.logger.WithError(err).Error("Failed to update apartment settings")
		s.respondError(w, http.StatusInternalServerError, "failed to update apartment settings")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "updated"})
}

func (s *HTTPServer) placeFurniture(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	characterID, err := s.getCharacterID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req models.PlaceFurnitureRequest
	req.CharacterID = characterID
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	furniture, err := s.housingService.PlaceFurniture(r.Context(), apartmentID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to place furniture")
		s.respondError(w, http.StatusInternalServerError, "failed to place furniture")
		return
	}

	s.respondJSON(w, http.StatusCreated, furniture)
}

func (s *HTTPServer) listPlacedFurniture(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	detail, err := s.housingService.GetApartmentDetail(r.Context(), apartmentID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list placed furniture")
		s.respondError(w, http.StatusInternalServerError, "failed to list placed furniture")
		return
	}

	s.respondJSON(w, http.StatusOK, models.PlacedFurnitureListResponse{
		Furniture: detail.Furniture,
		Total:     len(detail.Furniture),
	})
}

func (s *HTTPServer) removeFurniture(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	furnitureIDStr := mux.Vars(r)["furniture_id"]
	furnitureID, err := uuid.Parse(furnitureIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid furniture ID")
		return
	}

	characterID, err := s.getCharacterID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	if err := s.housingService.RemoveFurniture(r.Context(), apartmentID, furnitureID, characterID); err != nil {
		s.logger.WithError(err).Error("Failed to remove furniture")
		s.respondError(w, http.StatusInternalServerError, "failed to remove furniture")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "removed"})
}

func (s *HTTPServer) getFurnitureItem(w http.ResponseWriter, r *http.Request) {
	itemID := mux.Vars(r)["item_id"]

	item, err := s.housingService.GetFurnitureItem(r.Context(), itemID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get furniture item")
		s.respondError(w, http.StatusInternalServerError, "failed to get furniture item")
		return
	}

	if item == nil {
		s.respondError(w, http.StatusNotFound, "furniture item not found")
		return
	}

	s.respondJSON(w, http.StatusOK, item)
}

func (s *HTTPServer) listFurnitureItems(w http.ResponseWriter, r *http.Request) {
	var category *models.FurnitureCategory
	if categoryStr := r.URL.Query().Get("category"); categoryStr != "" {
		c := models.FurnitureCategory(categoryStr)
		category = &c
	}

	limit, _ := strconv.Atoi(r.URL.Query().Get("limit"))
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}

	offset, _ := strconv.Atoi(r.URL.Query().Get("offset"))
	if offset < 0 {
		offset = 0
	}

	items, total, err := s.housingService.ListFurnitureItems(r.Context(), category, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list furniture items")
		s.respondError(w, http.StatusInternalServerError, "failed to list furniture items")
		return
	}

	s.respondJSON(w, http.StatusOK, models.FurnitureListResponse{
		Items: items,
		Total: total,
	})
}

func (s *HTTPServer) visitApartment(w http.ResponseWriter, r *http.Request) {
	apartmentIDStr := mux.Vars(r)["apartment_id"]
	apartmentID, err := uuid.Parse(apartmentIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid apartment ID")
		return
	}

	characterID, err := s.getCharacterID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	req := models.VisitApartmentRequest{
		CharacterID: characterID,
		ApartmentID: apartmentID,
	}

	if err := s.housingService.VisitApartment(r.Context(), &req); err != nil {
		s.logger.WithError(err).Error("Failed to visit apartment")
		s.respondError(w, http.StatusInternalServerError, "failed to visit apartment")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "visited"})
}

func (s *HTTPServer) getPrestigeLeaderboard(w http.ResponseWriter, r *http.Request) {
	limit, _ := strconv.Atoi(r.URL.Query().Get("limit"))
	if limit <= 0 {
		limit = 20
	}
	if limit > 100 {
		limit = 100
	}

	offset, _ := strconv.Atoi(r.URL.Query().Get("offset"))
	if offset < 0 {
		offset = 0
	}

	entries, total, err := s.housingService.GetPrestigeLeaderboard(r.Context(), limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get prestige leaderboard")
		s.respondError(w, http.StatusInternalServerError, "failed to get prestige leaderboard")
		return
	}

	s.respondJSON(w, http.StatusOK, models.PrestigeLeaderboardResponse{
		Entries: entries,
		Total:   total,
	})
}

type responseRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (r *responseRecorder) WriteHeader(statusCode int) {
	r.statusCode = statusCode
	r.ResponseWriter.WriteHeader(statusCode)
}

