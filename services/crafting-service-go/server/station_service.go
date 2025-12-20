// Issue: #2203 - Station service implementation
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// StationService handles station business logic
type StationService struct {
	repo   *StationRepository
	redis  *redis.Client
	logger *logrus.Logger
}

// NewStationService creates new station service
func NewStationService(repo *StationRepository, redisClient *redis.Client) StationServiceInterface {
	return &StationService{
		repo:   repo,
		redis:  redisClient,
		logger: GetLogger(),
	}
}

// GetStation retrieves station by ID
func (s *StationService) GetStation(ctx context.Context, stationID uuid.UUID) (*Station, error) {
	station, err := s.repo.GetByID(ctx, stationID)
	if err != nil {
		return nil, fmt.Errorf("failed to get station: %w", err)
	}

	return station, nil
}

// ListStations retrieves stations with pagination
func (s *StationService) ListStations(ctx context.Context, zoneID *uuid.UUID, stationType *string, available *bool, limit, offset int) ([]Station, int, error) {
	stations, total, err := s.repo.List(ctx, zoneID, stationType, available, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list stations: %w", err)
	}

	return stations, total, nil
}

// UpdateStation updates existing station
func (s *StationService) UpdateStation(ctx context.Context, station *Station) error {
	now := time.Now()
	station.UpdatedAt = &now

	if err := s.repo.Update(ctx, station); err != nil {
		return fmt.Errorf("failed to update station: %w", err)
	}

	return nil
}

// BookStation books station for player
func (s *StationService) BookStation(ctx context.Context, stationID uuid.UUID, playerID uuid.UUID, duration int, priority int) (*StationBooking, error) {
	// Check if station is available
	station, err := s.repo.GetByID(ctx, stationID)
	if err != nil {
		return nil, fmt.Errorf("station not found: %w", err)
	}

	if !station.IsAvailable {
		return nil, fmt.Errorf("station is not available")
	}

	// Check for existing active booking
	if _, err := s.repo.GetActiveBooking(ctx, stationID); err == nil {
		return nil, fmt.Errorf("station is already booked")
	}

	// Validate duration and priority
	if duration < 1 || duration > 3600 {
		return nil, fmt.Errorf("duration must be between 1 and 3600 seconds")
	}

	if priority < 1 || priority > 10 {
		return nil, fmt.Errorf("priority must be between 1 and 10")
	}

	// Create booking
	now := time.Now()
	booking := &StationBooking{
		StationID:   stationID,
		PlayerID:    playerID,
		BookedUntil: now.Add(time.Duration(duration) * time.Second),
		Priority:    priority,
		CreatedAt:   now,
	}

	if err := s.repo.BookStation(ctx, booking); err != nil {
		return nil, fmt.Errorf("failed to book station: %w", err)
	}

	// Update station availability
	station.IsAvailable = false
	station.CurrentOrderID = &playerID // Temporary assignment
	if err := s.repo.Update(ctx, station); err != nil {
		s.logger.WithError(err).Warn("Failed to update station availability")
	}

	s.logger.WithFields(logrus.Fields{
		"station_id": stationID,
		"player_id":  playerID,
		"duration":   duration,
		"priority":   priority,
	}).Info("Station booked successfully")

	return booking, nil
}

// IsStationAvailable checks if station is available for booking
func (s *StationService) IsStationAvailable(ctx context.Context, stationID uuid.UUID) (bool, error) {
	station, err := s.repo.GetByID(ctx, stationID)
	if err != nil {
		return false, fmt.Errorf("station not found: %w", err)
	}

	// Check for active booking
	if _, err := s.repo.GetActiveBooking(ctx, stationID); err == nil {
		return false, nil // Has active booking
	}

	return station.IsAvailable, nil
}
