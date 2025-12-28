package server

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/world-cities-service-go/internal/database"
	"services/world-cities-service-go/internal/service"
)

// Server handles HTTP requests for world cities
type Server struct {
	service *service.Service
	logger  *zap.Logger
	router  *chi.Mux
}

// NewServer creates a new server instance
func NewServer(db *database.Database, logger *zap.Logger) *Server {
	svc := service.NewService(db, logger)

	s := &Server{
		service: svc,
		logger:  logger,
	}

	s.setupRouter()
	return s
}

// setupRouter configures the HTTP router
func (s *Server) setupRouter() {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)

	// CORS middleware
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			w.Header().Set("Access-Control-Allow-Origin", "*")
			w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
			w.Header().Set("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type, X-CSRF-Token")

			if r.Method == "OPTIONS" {
				w.WriteHeader(http.StatusOK)
				return
			}

			next.ServeHTTP(w, r)
		})
	})

	// Routes
	r.Get("/health", s.HealthCheck)
	r.Get("/api/v1/world-cities/health", s.HealthCheck)

	// City routes
	r.Route("/api/v1/world-cities/cities", func(r chi.Router) {
		r.Get("/", s.ListCities)
		r.Get("/search", s.SearchCities)
		r.Get("/{cityId}", s.GetCity)
		r.Get("/analytics", s.GetCitiesAnalytics)
	})

	s.router = r
}

// Handler returns the HTTP handler
func (s *Server) Handler() http.Handler {
	return s.router
}

// HealthCheck handles health check requests
func (s *Server) HealthCheck(w http.ResponseWriter, r *http.Request) {
	response, err := s.service.HealthCheck(r.Context())
	if err != nil {
		s.logger.Error("Health check failed", zap.Error(err))
		s.respondError(w, http.StatusServiceUnavailable, "Service unavailable")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

// ListCities handles GET /api/v1/world-cities/cities
func (s *Server) ListCities(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	filter := database.CityFilter{}

	if continent := r.URL.Query().Get("continent"); continent != "" {
		filter.Continent = &continent
	}

	if country := r.URL.Query().Get("country"); country != "" {
		filter.Country = &country
	}

	if cyberpunkMinStr := r.URL.Query().Get("cyberpunk_level_min"); cyberpunkMinStr != "" {
		if cyberpunkMin, err := strconv.Atoi(cyberpunkMinStr); err == nil {
			filter.CyberpunkLevelMin = &cyberpunkMin
		}
	}

	if cyberpunkMaxStr := r.URL.Query().Get("cyberpunk_level_max"); cyberpunkMaxStr != "" {
		if cyberpunkMax, err := strconv.Atoi(cyberpunkMaxStr); err == nil {
			filter.CyberpunkLevelMax = &cyberpunkMax
		}
	}

	if isMegacityStr := r.URL.Query().Get("is_megacity"); isMegacityStr != "" {
		if isMegacity, err := strconv.ParseBool(isMegacityStr); err == nil {
			filter.IsMegacity = &isMegacity
		}
	}

	if latitudeStr := r.URL.Query().Get("latitude"); latitudeStr != "" {
		if latitude, err := strconv.ParseFloat(latitudeStr, 64); err == nil {
			filter.Latitude = &latitude
		}
	}

	if longitudeStr := r.URL.Query().Get("longitude"); longitudeStr != "" {
		if longitude, err := strconv.ParseFloat(longitudeStr, 64); err == nil {
			filter.Longitude = &longitude
		}
	}

	if radiusStr := r.URL.Query().Get("radius_km"); radiusStr != "" {
		if radius, err := strconv.ParseFloat(radiusStr, 64); err == nil {
			filter.RadiusKm = &radius
		}
	}

	if status := r.URL.Query().Get("status"); status != "" {
		filter.Status = &status
	}

	// Parse pagination options
	options := database.CityListOptions{
		Limit:  20,
		Offset: 0,
	}

	if pageStr := r.URL.Query().Get("page"); pageStr != "" {
		if page, err := strconv.Atoi(pageStr); err == nil && page > 0 {
			options.Offset = (page - 1) * options.Limit
		}
	}

	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if limit, err := strconv.Atoi(limitStr); err == nil && limit > 0 && limit <= 100 {
			options.Limit = limit
		}
	}

	if sortBy := r.URL.Query().Get("sort_by"); sortBy != "" {
		options.SortBy = sortBy
	}

	if sortOrder := r.URL.Query().Get("sort_order"); sortOrder != "" {
		options.SortOrder = sortOrder
	}

	response, err := s.service.ListCities(r.Context(), filter, options)
	if err != nil {
		s.logger.Error("Failed to list cities", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to list cities")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetCity handles GET /api/v1/world-cities/cities/{cityId}
func (s *Server) GetCity(w http.ResponseWriter, r *http.Request) {
	cityID := chi.URLParam(r, "cityId")

	// Try to parse as UUID first
	if id, err := uuid.Parse(cityID); err == nil {
		response, err := s.service.GetCityByUUID(r.Context(), id)
		if err != nil {
			if err.Error() == "city not found" {
				s.respondError(w, http.StatusNotFound, "City not found")
			} else {
				s.logger.Error("Failed to get city by UUID", zap.Error(err))
				s.respondError(w, http.StatusInternalServerError, "Failed to get city")
			}
			return
		}
		s.respondJSON(w, http.StatusOK, response)
		return
	}

	// Otherwise treat as city_id string
	response, err := s.service.GetCity(r.Context(), cityID)
	if err != nil {
		if err.Error() == "city not found" {
			s.respondError(w, http.StatusNotFound, "City not found")
		} else {
			s.logger.Error("Failed to get city", zap.Error(err))
			s.respondError(w, http.StatusInternalServerError, "Failed to get city")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

// SearchCities handles GET /api/v1/world-cities/cities/search
func (s *Server) SearchCities(w http.ResponseWriter, r *http.Request) {
	query := r.URL.Query().Get("q")
	if query == "" {
		s.respondError(w, http.StatusBadRequest, "Search query is required")
		return
	}

	// Parse filters (same as ListCities)
	filter := database.CityFilter{}

	if continent := r.URL.Query().Get("continent"); continent != "" {
		filter.Continent = &continent
	}

	if country := r.URL.Query().Get("country"); country != "" {
		filter.Country = &country
	}

	if cyberpunkMinStr := r.URL.Query().Get("cyberpunk_level_min"); cyberpunkMinStr != "" {
		if cyberpunkMin, err := strconv.Atoi(cyberpunkMinStr); err == nil {
			filter.CyberpunkLevelMin = &cyberpunkMin
		}
	}

	if cyberpunkMaxStr := r.URL.Query().Get("cyberpunk_level_max"); cyberpunkMaxStr != "" {
		if cyberpunkMax, err := strconv.Atoi(cyberpunkMaxStr); err == nil {
			filter.CyberpunkLevelMax = &cyberpunkMax
		}
	}

	if isMegacityStr := r.URL.Query().Get("is_megacity"); isMegacityStr != "" {
		if isMegacity, err := strconv.ParseBool(isMegacityStr); err == nil {
			filter.IsMegacity = &isMegacity
		}
	}

	if status := r.URL.Query().Get("status"); status != "" {
		filter.Status = &status
	}

	// Parse pagination options
	options := database.CityListOptions{
		Limit:  20,
		Offset: 0,
	}

	if pageStr := r.URL.Query().Get("page"); pageStr != "" {
		if page, err := strconv.Atoi(pageStr); err == nil && page > 0 {
			options.Offset = (page - 1) * options.Limit
		}
	}

	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if limit, err := strconv.Atoi(limitStr); err == nil && limit > 0 && limit <= 100 {
			options.Limit = limit
		}
	}

	response, err := s.service.SearchCities(r.Context(), query, filter, options)
	if err != nil {
		s.logger.Error("Failed to search cities", zap.String("query", query), zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to search cities")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetCitiesAnalytics handles GET /api/v1/world-cities/cities/analytics
func (s *Server) GetCitiesAnalytics(w http.ResponseWriter, r *http.Request) {
	analytics, err := s.service.GetCitiesAnalytics(r.Context())
	if err != nil {
		s.logger.Error("Failed to get cities analytics", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get cities analytics")
		return
	}

	s.respondJSON(w, http.StatusOK, analytics)
}

// respondJSON sends a JSON response
func (s *Server) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

// respondError sends an error response
func (s *Server) respondError(w http.ResponseWriter, status int, message string) {
	response := service.ErrorResponse{
		Error:   http.StatusText(status),
		Code:    strconv.Itoa(status),
		Message: message,
	}
	s.respondJSON(w, status, response)
}

