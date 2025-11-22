package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/character-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr             string
	router           *mux.Router
	characterService *CharacterService
	logger           *logrus.Logger
	server           *http.Server
	jwtValidator     *JwtValidator
	authEnabled      bool
}

func NewHTTPServer(addr string, characterService *CharacterService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:             addr,
		router:           router,
		characterService: characterService,
		logger:           GetLogger(),
		jwtValidator:     jwtValidator,
		authEnabled:      authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	api.HandleFunc("/accounts/{accountId}", server.getAccount).Methods("GET")
	api.HandleFunc("/accounts", server.createAccount).Methods("POST")
	
	api.HandleFunc("/characters", server.getCharacters).Methods("GET")
	api.HandleFunc("/characters/{characterId}", server.getCharacter).Methods("GET")
	api.HandleFunc("/characters", server.createCharacter).Methods("POST")
	api.HandleFunc("/characters/{characterId}", server.updateCharacter).Methods("PUT")
	api.HandleFunc("/characters/{characterId}", server.deleteCharacter).Methods("DELETE")
	api.HandleFunc("/characters/{characterId}/validate", server.validateCharacter).Methods("GET")

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

func (s *HTTPServer) getAccount(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	accountIDStr := vars["accountId"]

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid account id")
		return
	}

	account, err := s.characterService.GetAccount(r.Context(), accountID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get account")
		s.respondError(w, http.StatusInternalServerError, "failed to get account")
		return
	}

	if account == nil {
		s.respondError(w, http.StatusNotFound, "account not found")
		return
	}

	s.respondJSON(w, http.StatusOK, account)
}

func (s *HTTPServer) createAccount(w http.ResponseWriter, r *http.Request) {
	var req models.CreateAccountRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Nickname == "" {
		s.respondError(w, http.StatusBadRequest, "nickname is required")
		return
	}

	account, err := s.characterService.CreateAccount(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create account")
		if strings.Contains(err.Error(), "unique") || strings.Contains(err.Error(), "duplicate") {
			s.respondError(w, http.StatusConflict, "account with this nickname already exists")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to create account")
		return
	}

	s.respondJSON(w, http.StatusCreated, account)
}

func (s *HTTPServer) getCharacters(w http.ResponseWriter, r *http.Request) {
	accountIDStr := r.URL.Query().Get("account_id")
	if accountIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "account_id query parameter is required")
		return
	}

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid account id")
		return
	}

	characters, err := s.characterService.GetCharactersByAccountID(r.Context(), accountID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get characters")
		s.respondError(w, http.StatusInternalServerError, "failed to get characters")
		return
	}

	response := models.CharacterListResponse{
		Characters: characters,
		Total:      len(characters),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getCharacter(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	char, err := s.characterService.GetCharacter(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get character")
		s.respondError(w, http.StatusInternalServerError, "failed to get character")
		return
	}

	if char == nil {
		s.respondError(w, http.StatusNotFound, "character not found")
		return
	}

	s.respondJSON(w, http.StatusOK, char)
}

func (s *HTTPServer) createCharacter(w http.ResponseWriter, r *http.Request) {
	var req models.CreateCharacterRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Name == "" {
		s.respondError(w, http.StatusBadRequest, "name is required")
		return
	}

	char, err := s.characterService.CreateCharacter(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create character")
		s.respondError(w, http.StatusInternalServerError, "failed to create character")
		return
	}

	s.respondJSON(w, http.StatusCreated, char)
}

func (s *HTTPServer) updateCharacter(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	var req models.UpdateCharacterRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	char, err := s.characterService.UpdateCharacter(r.Context(), characterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update character")
		if strings.Contains(err.Error(), "not found") {
			s.respondError(w, http.StatusNotFound, "character not found")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to update character")
		return
	}

	s.respondJSON(w, http.StatusOK, char)
}

func (s *HTTPServer) deleteCharacter(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	err = s.characterService.DeleteCharacter(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to delete character")
		s.respondError(w, http.StatusInternalServerError, "failed to delete character")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) validateCharacter(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	valid, err := s.characterService.ValidateCharacter(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to validate character")
		s.respondError(w, http.StatusInternalServerError, "failed to validate character")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]bool{"valid": valid})
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
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      http.StatusOK,
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

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		ctx = context.WithValue(ctx, "user_id", claims.Subject)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
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
