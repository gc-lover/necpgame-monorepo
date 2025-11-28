package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr              string
	router            *mux.Router
	catalogService    *CosmeticCatalogService
	shopService       *CosmeticShopService
	purchaseService   *CosmeticPurchaseService
	equipmentService  *CosmeticEquipmentService
	inventoryService  *CosmeticInventoryService
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(
	addr string,
	catalogService *CosmeticCatalogService,
	shopService *CosmeticShopService,
	purchaseService *CosmeticPurchaseService,
	equipmentService *CosmeticEquipmentService,
	inventoryService *CosmeticInventoryService,
	jwtValidator *JwtValidator,
	authEnabled bool,
) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:              addr,
		router:            router,
		catalogService:    catalogService,
		shopService:       shopService,
		purchaseService:   purchaseService,
		equipmentService:  equipmentService,
		inventoryService:  inventoryService,
		logger:            GetLogger(),
		jwtValidator:      jwtValidator,
		authEnabled:       authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1/monetization/cosmetic").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	catalog := api.PathPrefix("/catalog").Subrouter()
	catalog.HandleFunc("", server.getCosmeticCatalog).Methods("GET")
	catalog.HandleFunc("/categories", server.getCosmeticCategories).Methods("GET")

	api.HandleFunc("/{cosmetic_id}", server.getCosmeticDetails).Methods("GET")

	shop := api.PathPrefix("/shop").Subrouter()
	shop.HandleFunc("/daily", server.getDailyShop).Methods("GET")
	shop.HandleFunc("/history", server.getShopHistory).Methods("GET")

	api.HandleFunc("/purchase", server.purchaseCosmetic).Methods("POST")
	api.HandleFunc("/purchase/history/{player_id}", server.getPurchaseHistory).Methods("GET")

	api.HandleFunc("/{cosmetic_id}/equip", server.equipCosmetic).Methods("POST")
	api.HandleFunc("/{cosmetic_id}/unequip", server.unequipCosmetic).Methods("POST")
	api.HandleFunc("/equipped/{player_id}", server.getEquippedCosmetics).Methods("GET")

	api.HandleFunc("/rarity/{rarity}", server.getCosmeticsByRarity).Methods("GET")
	api.HandleFunc("/inventory/{player_id}", server.getCosmeticInventory).Methods("GET")
	api.HandleFunc("/inventory/{player_id}/owned", server.checkCosmeticOwnership).Methods("GET")
	api.HandleFunc("/events/{player_id}", server.getCosmeticEvents).Methods("GET")

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

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")

	buf := &bytes.Buffer{}
	if err := json.NewEncoder(buf).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
		s.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		s.logger.WithError(err).Error("Failed to write JSON response")
	}
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

		duration := time.Since(start)
		RecordRequestDuration(r.Method, r.URL.Path, float64(duration.Seconds()))
		RecordRequest(r.Method, r.URL.Path, recorder.statusCode)
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
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "missing authorization header")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("Failed to verify JWT")
			s.respondError(w, http.StatusUnauthorized, "invalid token")
			return
		}

		ctx := context.WithValue(r.Context(), "user_id", claims.Subject)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (r *statusRecorder) WriteHeader(status int) {
	r.statusCode = status
	r.ResponseWriter.WriteHeader(status)
}

