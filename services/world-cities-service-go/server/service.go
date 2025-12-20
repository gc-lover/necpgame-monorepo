// Package server Issue: #140875381
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"math"
	"net/http"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// WorldCitiesService содержит бизнес-логику работы с городами мира
type WorldCitiesService struct {
	logger *zap.Logger
	repo   *WorldCitiesRepository
	// Memory pooling для оптимизации аллокаций
	cityPool *sync.Pool
}

// City представляет город в системе (согласно OpenAPI спецификации)
type City struct {
	ID               string                 `json:"id" db:"id"`
	Name             string                 `json:"name" db:"name"`
	Country          string                 `json:"country" db:"country"`
	Region           string                 `json:"region" db:"region"`
	Latitude         float64                `json:"latitude" db:"latitude"`
	Longitude        float64                `json:"longitude" db:"longitude"`
	Population       int                    `json:"population" db:"population"`
	DevelopmentLevel string                 `json:"development_level" db:"development_level"`
	Description      string                 `json:"description" db:"description"`
	Landmarks        []string               `json:"landmarks" db:"landmarks"`
	EconomicData     map[string]interface{} `json:"economic_data" db:"economic_data"`
	SocialData       map[string]interface{} `json:"social_data" db:"social_data"`
	TimelineYear     int                    `json:"timeline_year" db:"timeline_year"`
	IsActive         bool                   `json:"is_active" db:"is_active"`
	CreatedAt        time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time              `json:"updated_at" db:"updated_at"`
}

// Coordinates представляет географические координаты
type Coordinates struct {
	Latitude  float64 `json:"latitude"`
	Longitude float64 `json:"longitude"`
}

// NewWorldCitiesService создает новый сервис городов мира
func NewWorldCitiesService(db *sql.DB, logger *zap.Logger) *WorldCitiesService {
	return &WorldCitiesService{
		repo:   NewWorldCitiesRepository(db, logger),
		logger: logger,
		cityPool: &sync.Pool{
			New: func() interface{} {
				return &City{}
			},
		},
	}
}

// GetCities возвращает список городов с фильтрацией и пагинацией
func (s *WorldCitiesService) GetCities(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Парсим query параметры
	query := r.URL.Query()
	filters := make(map[string]interface{})

	if region := query.Get("region"); region != "" {
		filters["region"] = region
	}
	if country := query.Get("country"); country != "" {
		filters["country"] = country
	}
	if minPop := query.Get("min_population"); minPop != "" {
		if pop, err := strconv.Atoi(minPop); err == nil {
			filters["min_population"] = pop
		}
	}
	if maxPop := query.Get("max_population"); maxPop != "" {
		if pop, err := strconv.Atoi(maxPop); err == nil {
			filters["max_population"] = pop
		}
	}
	if devLevel := query.Get("development_level"); devLevel != "" {
		filters["development_level"] = devLevel
	}

	// Геофильтр
	if lat := query.Get("latitude"); lat != "" {
		if latitude, err := strconv.ParseFloat(lat, 64); err == nil {
			filters["latitude"] = latitude
		}
	}
	if lon := query.Get("longitude"); lon != "" {
		if longitude, err := strconv.ParseFloat(lon, 64); err == nil {
			filters["longitude"] = longitude
		}
	}
	if radius := query.Get("radius_km"); radius != "" {
		if r, err := strconv.ParseFloat(radius, 64); err == nil {
			filters["radius_km"] = r
		}
	}

	// Пагинация
	limit := 20 // default
	if l := query.Get("limit"); l != "" {
		if parsedLimit, err := strconv.Atoi(l); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0 // default
	if o := query.Get("offset"); o != "" {
		if parsedOffset, err := strconv.Atoi(o); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	// Получаем города из репозитория
	cities, total, err := s.repo.GetCities(ctx, filters, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get cities", zap.Error(err))
		http.Error(w, "Failed to get cities", http.StatusInternalServerError)
		return
	}

	// Создаем pagination info
	totalPages := (total + limit - 1) / limit
	currentPage := (offset / limit) + 1

	response := map[string]interface{}{
		"cities": cities,
		"total":  total,
		"pagination": map[string]interface{}{
			"page":        currentPage,
			"page_size":   limit,
			"total_pages": totalPages,
			"has_next":    currentPage < totalPages,
			"has_prev":    currentPage > 1,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// GetCity возвращает город по ID
func (s *WorldCitiesService) GetCity(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	cityID := chi.URLParam(r, "cityID")

	city, err := s.repo.GetCityByID(ctx, cityID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			http.Error(w, "City not found", http.StatusNotFound)
			return
		}
		s.logger.Error("Failed to get city", zap.Error(err), zap.String("city_id", cityID))
		http.Error(w, "Failed to get city", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(city)
}

// CreateCity создает новый город
func (s *WorldCitiesService) CreateCity(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	var req struct {
		Name             string                 `json:"name"`
		Country          string                 `json:"country"`
		Region           string                 `json:"region"`
		Coordinates      Coordinates            `json:"coordinates"`
		Population       int                    `json:"population"`
		DevelopmentLevel string                 `json:"development_level"`
		Description      string                 `json:"description"`
		Landmarks        []string               `json:"landmarks"`
		EconomicData     map[string]interface{} `json:"economic_data"`
		SocialData       map[string]interface{} `json:"social_data"`
		TimelineYear     int                    `json:"timeline_year"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Валидация обязательных полей
	if req.Name == "" || req.Country == "" || req.Region == "" || req.TimelineYear == 0 {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	city := &City{
		ID:               uuid.New().String(),
		Name:             req.Name,
		Country:          req.Country,
		Region:           req.Region,
		Latitude:         req.Coordinates.Latitude,
		Longitude:        req.Coordinates.Longitude,
		Population:       req.Population,
		DevelopmentLevel: req.DevelopmentLevel,
		Description:      req.Description,
		Landmarks:        req.Landmarks,
		EconomicData:     req.EconomicData,
		SocialData:       req.SocialData,
		TimelineYear:     req.TimelineYear,
		IsActive:         true,
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
	}

	if err := s.repo.CreateCity(ctx, city); err != nil {
		s.logger.Error("Failed to create city", zap.Error(err))
		http.Error(w, "Failed to create city", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Location", fmt.Sprintf("/api/v1/cities/%s", city.ID))
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(city)
}

// UpdateCity обновляет город
func (s *WorldCitiesService) UpdateCity(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	cityID := chi.URLParam(r, "cityID")

	var req struct {
		Name             *string                 `json:"name"`
		Population       *int                    `json:"population"`
		DevelopmentLevel *string                 `json:"development_level"`
		Description      *string                 `json:"description"`
		Landmarks        *[]string               `json:"landmarks"`
		EconomicData     *map[string]interface{} `json:"economic_data"`
		SocialData       *map[string]interface{} `json:"social_data"`
		IsActive         *bool                   `json:"is_active"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	updates := make(map[string]interface{})
	if req.Name != nil {
		updates["name"] = *req.Name
	}
	if req.Population != nil {
		updates["population"] = *req.Population
	}
	if req.DevelopmentLevel != nil {
		updates["development_level"] = *req.DevelopmentLevel
	}
	if req.Description != nil {
		updates["description"] = *req.Description
	}
	if req.Landmarks != nil {
		updates["landmarks"] = *req.Landmarks
	}
	if req.EconomicData != nil {
		updates["economic_data"] = *req.EconomicData
	}
	if req.SocialData != nil {
		updates["social_data"] = *req.SocialData
	}
	if req.IsActive != nil {
		updates["is_active"] = *req.IsActive
	}

	if len(updates) == 0 {
		http.Error(w, "No fields to update", http.StatusBadRequest)
		return
	}

	if err := s.repo.UpdateCity(ctx, cityID, updates); err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			http.Error(w, "City not found", http.StatusNotFound)
			return
		}
		s.logger.Error("Failed to update city", zap.Error(err), zap.String("city_id", cityID))
		http.Error(w, "Failed to update city", http.StatusInternalServerError)
		return
	}

	// Возвращаем обновленный город
	city, err := s.repo.GetCityByID(ctx, cityID)
	if err != nil {
		s.logger.Error("Failed to get updated city", zap.Error(err), zap.String("city_id", cityID))
		http.Error(w, "Failed to get updated city", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(city)
}

// DeleteCity удаляет город
func (s *WorldCitiesService) DeleteCity(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	cityID := chi.URLParam(r, "cityID")

	if err := s.repo.DeleteCity(ctx, cityID); err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			http.Error(w, "City not found", http.StatusNotFound)
			return
		}
		s.logger.Error("Failed to delete city", zap.Error(err), zap.String("city_id", cityID))
		http.Error(w, "Failed to delete city", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// GetNearbyCities находит ближайшие города
func (s *WorldCitiesService) GetNearbyCities(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	query := r.URL.Query()

	latStr := query.Get("latitude")
	lonStr := query.Get("longitude")
	radiusStr := query.Get("radius_km")

	if latStr == "" || lonStr == "" {
		http.Error(w, "latitude and longitude are required", http.StatusBadRequest)
		return
	}

	lat, err := strconv.ParseFloat(latStr, 64)
	if err != nil {
		http.Error(w, "Invalid latitude", http.StatusBadRequest)
		return
	}

	lon, err := strconv.ParseFloat(lonStr, 64)
	if err != nil {
		http.Error(w, "Invalid longitude", http.StatusBadRequest)
		return
	}

	radius := 100.0 // default 100km
	if radiusStr != "" {
		if r, err := strconv.ParseFloat(radiusStr, 64); err == nil && r > 0 && r <= 500 {
			radius = r
		}
	}

	limit := 10 // default
	if l := query.Get("limit"); l != "" {
		if parsedLimit, err := strconv.Atoi(l); err == nil && parsedLimit > 0 && parsedLimit <= 50 {
			limit = parsedLimit
		}
	}

	cities, err := s.repo.GetNearbyCities(ctx, lat, lon, radius, limit)
	if err != nil {
		s.logger.Error("Failed to get nearby cities", zap.Error(err))
		http.Error(w, "Failed to get nearby cities", http.StatusInternalServerError)
		return
	}

	// Рассчитываем расстояния
	type CityWithDistance struct {
		City     *City   `json:"city"`
		Distance float64 `json:"distance_km"`
	}

	result := make([]CityWithDistance, len(cities))
	for i, city := range cities {
		distance := s.calculateDistance(lat, lon, city.Latitude, city.Longitude)
		result[i] = CityWithDistance{
			City:     city,
			Distance: distance,
		}
	}

	response := map[string]interface{}{
		"cities": result,
		"total":  len(result),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// GetRegions возвращает список регионов
func (s *WorldCitiesService) GetRegions(w http.ResponseWriter) {
	regions := []map[string]interface{}{
		{
			"id":          "North_America",
			"name":        "North America",
			"description": "North America region including USA, Canada, Mexico",
			"city_count":  150,
		},
		{
			"id":          "South_America",
			"name":        "South America",
			"description": "South America region",
			"city_count":  80,
		},
		{
			"id":          "Europe",
			"name":        "Europe",
			"description": "European countries and cities",
			"city_count":  200,
		},
		{
			"id":          "Asia",
			"name":        "Asia",
			"description": "Asian continent cities",
			"city_count":  300,
		},
		{
			"id":          "Africa",
			"name":        "Africa",
			"description": "African cities and regions",
			"city_count":  120,
		},
		{
			"id":          "Oceania",
			"name":        "Oceania",
			"description": "Australia, New Zealand and Pacific islands",
			"city_count":  50,
		},
		{
			"id":          "Antarctica",
			"name":        "Antarctica",
			"description": "Antarctic research stations",
			"city_count":  5,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{"regions": regions})
}

// GetCitiesStats возвращает статистику городов
func (s *WorldCitiesService) GetCitiesStats(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	stats, err := s.repo.GetCitiesStats(ctx)
	if err != nil {
		s.logger.Error("Failed to get cities stats", zap.Error(err))
		http.Error(w, "Failed to get cities stats", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(stats)
}

// SearchCities осуществляет поиск городов
func (s *WorldCitiesService) SearchCities(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	query := r.URL.Query()
	searchQuery := strings.TrimSpace(query.Get("query"))

	if searchQuery == "" || len(searchQuery) < 2 {
		http.Error(w, "Search query must be at least 2 characters", http.StatusBadRequest)
		return
	}

	limit := 10 // default
	if l := query.Get("limit"); l != "" {
		if parsedLimit, err := strconv.Atoi(l); err == nil && parsedLimit > 0 && parsedLimit <= 20 {
			limit = parsedLimit
		}
	}

	cities, err := s.repo.SearchCities(ctx, searchQuery, limit)
	if err != nil {
		s.logger.Error("Failed to search cities", zap.Error(err), zap.String("query", searchQuery))
		http.Error(w, "Failed to search cities", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"results": cities,
		"total":   len(cities),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// calculateDistance рассчитывает расстояние между двумя точками по формуле Haversine
func (s *WorldCitiesService) calculateDistance(lat1, lon1, lat2, lon2 float64) float64 {
	const earthRadiusKm = 6371.0

	// Конвертируем в радианы
	lat1Rad := lat1 * math.Pi / 180
	lon1Rad := lon1 * math.Pi / 180
	lat2Rad := lat2 * math.Pi / 180
	lon2Rad := lon2 * math.Pi / 180

	deltaLat := lat2Rad - lat1Rad
	deltaLon := lon2Rad - lon1Rad

	a := math.Sin(deltaLat/2)*math.Sin(deltaLat/2) +
		math.Cos(lat1Rad)*math.Cos(lat2Rad)*
			math.Sin(deltaLon/2)*math.Sin(deltaLon/2)

	c := 2 * math.Atan2(math.Sqrt(a), math.Sqrt(1-a))

	return earthRadiusKm * c
}
