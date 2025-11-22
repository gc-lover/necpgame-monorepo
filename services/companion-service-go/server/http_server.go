package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/companion-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr            string
	router          *mux.Router
	companionService *CompanionService
	logger          *logrus.Logger
	server          *http.Server
}

func NewHTTPServer(addr string, companionService *CompanionService) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:            addr,
		router:          router,
		companionService: companionService,
		logger:          GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/companions").Subrouter()

	api.HandleFunc("/types", server.listCompanionTypes).Methods("GET")
	api.HandleFunc("/types/{type_id}", server.getCompanionType).Methods("GET")
	api.HandleFunc("/purchase", server.purchaseCompanion).Methods("POST")
	api.HandleFunc("/characters/{character_id}", server.listPlayerCompanions).Methods("GET")
	api.HandleFunc("/{companion_id}", server.getCompanionDetail).Methods("GET")
	api.HandleFunc("/{companion_id}/summon", server.summonCompanion).Methods("POST")
	api.HandleFunc("/{companion_id}/dismiss", server.dismissCompanion).Methods("POST")
	api.HandleFunc("/{companion_id}/rename", server.renameCompanion).Methods("POST")
	api.HandleFunc("/{companion_id}/experience", server.addExperience).Methods("POST")
	api.HandleFunc("/{companion_id}/abilities/{ability_id}/use", server.useAbility).Methods("POST")

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

func (s *HTTPServer) listCompanionTypes(w http.ResponseWriter, r *http.Request) {
	var category *models.CompanionCategory
	if categoryStr := r.URL.Query().Get("category"); categoryStr != "" {
		cat := models.CompanionCategory(categoryStr)
		category = &cat
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.companionService.ListCompanionTypes(r.Context(), category, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list companion types")
		s.respondError(w, http.StatusInternalServerError, "failed to list companion types")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getCompanionType(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionType, err := s.companionService.GetCompanionType(r.Context(), vars["type_id"])
	if err != nil {
		s.logger.WithError(err).Error("Failed to get companion type")
		s.respondError(w, http.StatusInternalServerError, "failed to get companion type")
		return
	}

	if companionType == nil {
		s.respondError(w, http.StatusNotFound, "companion type not found")
		return
	}

	s.respondJSON(w, http.StatusOK, companionType)
}

func (s *HTTPServer) purchaseCompanion(w http.ResponseWriter, r *http.Request) {
	var req models.PurchaseCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	companion, err := s.companionService.PurchaseCompanion(r.Context(), req.CharacterID, req.CompanionTypeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to purchase companion")
		if err.Error() == "companion type not found" || err.Error() == "companion already owned" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to purchase companion")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, companion)
}

func (s *HTTPServer) listPlayerCompanions(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var status *models.CompanionStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		st := models.CompanionStatus(statusStr)
		status = &st
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.companionService.ListPlayerCompanions(r.Context(), characterID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list player companions")
		s.respondError(w, http.StatusInternalServerError, "failed to list player companions")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getCompanionDetail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionID, err := uuid.Parse(vars["companion_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid companion_id")
		return
	}

	response, err := s.companionService.GetCompanionDetail(r.Context(), companionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get companion detail")
		if err.Error() == "companion not found" {
			s.respondError(w, http.StatusNotFound, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to get companion detail")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) summonCompanion(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionID, err := uuid.Parse(vars["companion_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid companion_id")
		return
	}

	var req models.SummonCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CompanionID = companionID

	err = s.companionService.SummonCompanion(r.Context(), req.CharacterID, req.CompanionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to summon companion")
		if err.Error() == "companion not found" || err.Error() == "companion already summoned" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to summon companion")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) dismissCompanion(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionID, err := uuid.Parse(vars["companion_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid companion_id")
		return
	}

	var req models.DismissCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CompanionID = companionID

	err = s.companionService.DismissCompanion(r.Context(), req.CharacterID, req.CompanionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to dismiss companion")
		if err.Error() == "companion not found" || err.Error() == "companion is not summoned" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to dismiss companion")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) renameCompanion(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionID, err := uuid.Parse(vars["companion_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid companion_id")
		return
	}

	var req models.RenameCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CompanionID = companionID

	err = s.companionService.RenameCompanion(r.Context(), req.CharacterID, req.CompanionID, req.CustomName)
	if err != nil {
		s.logger.WithError(err).Error("Failed to rename companion")
		if err.Error() == "companion not found" {
			s.respondError(w, http.StatusNotFound, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to rename companion")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) addExperience(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionID, err := uuid.Parse(vars["companion_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid companion_id")
		return
	}

	var req models.AddExperienceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CompanionID = companionID
	if req.Source == "" {
		req.Source = "unknown"
	}

	err = s.companionService.AddExperience(r.Context(), req.CharacterID, req.CompanionID, req.Amount, req.Source)
	if err != nil {
		s.logger.WithError(err).Error("Failed to add experience")
		if err.Error() == "companion not found" {
			s.respondError(w, http.StatusNotFound, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to add experience")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) useAbility(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	companionID, err := uuid.Parse(vars["companion_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid companion_id")
		return
	}

	abilityID := vars["ability_id"]

	var req models.UseAbilityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CompanionID = companionID
	req.AbilityID = abilityID

	err = s.companionService.UseAbility(r.Context(), req.CharacterID, req.CompanionID, req.AbilityID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to use ability")
		if err.Error() == "companion not found" || err.Error() == "companion is not summoned" || err.Error() == "ability is on cooldown" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to use ability")
		}
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

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      recorder.statusCode,
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

