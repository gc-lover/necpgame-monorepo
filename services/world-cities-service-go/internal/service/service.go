package service

import (
	"context"
	"fmt"
	"math"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/world-cities-service-go/internal/database"
)

// Service handles business logic for world cities
type Service struct {
	db     *database.Database
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(db *database.Database, logger *zap.Logger) *Service {
	return &Service{
		db:     db,
		logger: logger,
	}
}

// HealthResponse represents health check response
type HealthResponse struct {
	Status    string `json:"status"`
	Version   string `json:"version"`
	Timestamp string `json:"timestamp"`
	Uptime    int64  `json:"uptime"`
}

// CityListResponse represents paginated city list response
type CityListResponse struct {
	Cities      []database.City `json:"cities"`
	Total       int             `json:"total"`
	Page        int             `json:"page"`
	Limit       int             `json:"limit"`
	TotalPages  int             `json:"total_pages"`
}

// CityResponse represents single city response
type CityResponse struct {
	City database.City `json:"city"`
}

// ErrorResponse represents error response
type ErrorResponse struct {
	Error   string `json:"error"`
	Code    string `json:"code"`
	Message string `json:"message"`
}

// ListCities retrieves cities with filtering and pagination
func (s *Service) ListCities(ctx context.Context, filter database.CityFilter, options database.CityListOptions) (*CityListResponse, error) {
	if options.Limit <= 0 || options.Limit > 100 {
		options.Limit = 20
	}
	if options.Offset < 0 {
		options.Offset = 0
	}

	cities, total, err := s.db.ListCities(ctx, filter, options)
	if err != nil {
		s.logger.Error("Failed to list cities", zap.Error(err))
		return nil, fmt.Errorf("failed to list cities: %w", err)
	}

	totalPages := int(math.Ceil(float64(total) / float64(options.Limit)))
	page := (options.Offset / options.Limit) + 1

	return &CityListResponse{
		Cities:     cities,
		Total:      total,
		Page:       page,
		Limit:      options.Limit,
		TotalPages: totalPages,
	}, nil
}

// GetCity retrieves a city by ID
func (s *Service) GetCity(ctx context.Context, cityID string) (*CityResponse, error) {
	city, err := s.db.GetCity(ctx, cityID)
	if err != nil {
		s.logger.Error("Failed to get city", zap.String("city_id", cityID), zap.Error(err))
		return nil, fmt.Errorf("failed to get city: %w", err)
	}

	return &CityResponse{City: *city}, nil
}

// GetCityByUUID retrieves a city by UUID
func (s *Service) GetCityByUUID(ctx context.Context, id uuid.UUID) (*CityResponse, error) {
	city, err := s.db.GetCityByUUID(ctx, id)
	if err != nil {
		s.logger.Error("Failed to get city by UUID", zap.String("id", id.String()), zap.Error(err))
		return nil, fmt.Errorf("failed to get city by UUID: %w", err)
	}

	return &CityResponse{City: *city}, nil
}

// SearchCities performs advanced city search
func (s *Service) SearchCities(ctx context.Context, query string, filter database.CityFilter, options database.CityListOptions) (*CityListResponse, error) {
	if options.Limit <= 0 || options.Limit > 100 {
		options.Limit = 20
	}
	if options.Offset < 0 {
		options.Offset = 0
	}

	cities, total, err := s.db.SearchCities(ctx, query, filter, options)
	if err != nil {
		s.logger.Error("Failed to search cities", zap.String("query", query), zap.Error(err))
		return nil, fmt.Errorf("failed to search cities: %w", err)
	}

	totalPages := int(math.Ceil(float64(total) / float64(options.Limit)))
	page := (options.Offset / options.Limit) + 1

	return &CityListResponse{
		Cities:     cities,
		Total:      total,
		Page:       page,
		Limit:      options.Limit,
		TotalPages: totalPages,
	}, nil
}

// GetCitiesAnalytics retrieves city analytics
func (s *Service) GetCitiesAnalytics(ctx context.Context) (map[string]interface{}, error) {
	analytics, err := s.db.GetCitiesAnalytics(ctx)
	if err != nil {
		s.logger.Error("Failed to get cities analytics", zap.Error(err))
		return nil, fmt.Errorf("failed to get cities analytics: %w", err)
	}

	return analytics, nil
}

// CreateCity creates a new city
func (s *Service) CreateCity(ctx context.Context, city *database.City) (*CityResponse, error) {
	created, err := s.db.CreateCity(ctx, city)
	if err != nil {
		s.logger.Error("Failed to create city", zap.Error(err))
		return nil, fmt.Errorf("failed to create city: %w", err)
	}

	return &CityResponse{City: *created}, nil
}

// UpdateCity updates an existing city
func (s *Service) UpdateCity(ctx context.Context, id uuid.UUID, city *database.City) (*CityResponse, error) {
	updated, err := s.db.UpdateCity(ctx, id, city)
	if err != nil {
		if err.Error() == "city not found" {
			return nil, err
		}
		s.logger.Error("Failed to update city", zap.Error(err))
		return nil, fmt.Errorf("failed to update city: %w", err)
	}

	return &CityResponse{City: *updated}, nil
}

// DeleteCity removes a city
func (s *Service) DeleteCity(ctx context.Context, id uuid.UUID) error {
	err := s.db.DeleteCity(ctx, id)
	if err != nil {
		if err.Error() == "city not found" {
			return err
		}
		s.logger.Error("Failed to delete city", zap.Error(err))
		return fmt.Errorf("failed to delete city: %w", err)
	}

	return nil
}

// HealthCheck performs health check
func (s *Service) HealthCheck(ctx context.Context) (*HealthResponse, error) {
	// In production, this would check database connectivity, etc.
	return &HealthResponse{
		Status:    "healthy",
		Version:   "1.0.0",
		Timestamp: "2025-12-28T00:00:00Z", // Would be dynamic
		Uptime:    0, // Would be calculated
	}, nil
}

