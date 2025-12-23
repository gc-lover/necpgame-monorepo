// Business logic service for Maintenance Windows
// Issue: #316
// PERFORMANCE: Optimized for high-throughput maintenance window operations

package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/maintenance-windows-service-go/pkg/api"
	"github.com/google/uuid"
)

// MaintenanceWindowsService handles business logic for maintenance windows
type MaintenanceWindowsService struct {
	repo      *Repository
	cache     *Cache
	validator *Validator
	metrics   *Metrics
	mu        sync.RWMutex
}

// NewMaintenanceWindowsService creates a new maintenance windows service
func NewMaintenanceWindowsService() *MaintenanceWindowsService {
	return &MaintenanceWindowsService{
		repo:      NewRepository(),
		cache:     NewCache(),
		validator: NewValidator(),
		metrics:   NewMetrics(),
	}
}

// CreateMaintenanceWindow creates a new maintenance window
func (s *MaintenanceWindowsService) CreateMaintenanceWindow(ctx context.Context, req *api.CreateMaintenanceWindowRequest) (*api.MaintenanceWindow, error) {
	s.metrics.RecordRequest("CreateMaintenanceWindow")

	// Validate request
	if err := s.validator.ValidateCreateRequest(ctx, req); err != nil {
		s.metrics.RecordError("CreateMaintenanceWindow")
		return nil, err
	}

	// Create window entity
	window := &api.MaintenanceWindow{
		ID:               api.NewOptUUID(uuid.New()),
		Title:            req.Title,
		Description:      api.NewOptString(req.Description),
		MaintenanceType:  req.MaintenanceType,
		Status:           api.MaintenanceWindowStatusPlanned,
		ScheduledStart:   req.ScheduledStart,
		ScheduledEnd:     req.ScheduledEnd,
		AffectedServices: api.NewOptMaintenanceWindowAffectedServices(req.AffectedServices),
		CreatedAt:        api.NewOptDateTime(time.Now()),
		UpdatedAt:        api.NewOptDateTime(time.Now()),
	}

	// Save to database
	if err := s.repo.CreateMaintenanceWindow(ctx, window); err != nil {
		s.metrics.RecordError("CreateMaintenanceWindow")
		return nil, err
	}

	// Cache the window
	s.cache.SetMaintenanceWindow(ctx, window.ID.Value, window)

	s.metrics.RecordSuccess("CreateMaintenanceWindow")
	return window, nil
}

// GetMaintenanceWindows retrieves maintenance windows with filtering and pagination
func (s *MaintenanceWindowsService) GetMaintenanceWindows(ctx context.Context, filter *MaintenanceWindowFilter, limit, offset int) ([]*api.MaintenanceWindow, int, error) {
	s.metrics.RecordRequest("GetMaintenanceWindows")

	// Try cache first for common queries
	if filter.IsDefault() && offset == 0 {
		if cached, total, found := s.cache.GetMaintenanceWindows(ctx, limit); found {
			s.metrics.RecordCacheHit("GetMaintenanceWindows")
			return cached, total, nil
		}
	}

	// Query database
	windows, total, err := s.repo.GetMaintenanceWindows(ctx, filter, limit, offset)
	if err != nil {
		s.metrics.RecordError("GetMaintenanceWindows")
		return nil, 0, err
	}

	// Cache results for future queries
	if filter.IsDefault() && offset == 0 {
		s.cache.SetMaintenanceWindows(ctx, windows, total, limit)
	}

	s.metrics.RecordSuccess("GetMaintenanceWindows")
	return windows, total, nil
}

// GetMaintenanceWindow retrieves a specific maintenance window by ID
func (s *MaintenanceWindowsService) GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, error) {
	s.metrics.RecordRequest("GetMaintenanceWindow")

	// Try cache first
	if cached, found := s.cache.GetMaintenanceWindow(ctx, windowID); found {
		s.metrics.RecordCacheHit("GetMaintenanceWindow")
		return cached, nil
	}

	// Query database
	window, err := s.repo.GetMaintenanceWindow(ctx, windowID)
	if err != nil {
		s.metrics.RecordError("GetMaintenanceWindow")
		return nil, err
	}

	// Cache for future requests
	s.cache.SetMaintenanceWindow(ctx, windowID, window)

	s.metrics.RecordSuccess("GetMaintenanceWindow")
	return window, nil
}

// UpdateMaintenanceWindow updates an existing maintenance window
func (s *MaintenanceWindowsService) UpdateMaintenanceWindow(ctx context.Context, windowID uuid.UUID, req *api.UpdateMaintenanceWindowRequest) (*api.MaintenanceWindow, error) {
	s.metrics.RecordRequest("UpdateMaintenanceWindow")

	// Validate request
	if err := s.validator.ValidateUpdateRequest(ctx, req); err != nil {
		s.metrics.RecordError("UpdateMaintenanceWindow")
		return nil, err
	}

	// Get existing window
	existing, err := s.repo.GetMaintenanceWindow(ctx, windowID)
	if err != nil {
		s.metrics.RecordError("UpdateMaintenanceWindow")
		return nil, err
	}

	// Apply updates
	if req.Title != nil {
		existing.Title = *req.Title
	}
	if req.Description != nil {
		existing.Description = req.Description
	}
	if req.Status != nil {
		existing.Status = *req.Status
	}
	if req.ScheduledStart != nil {
		existing.ScheduledStart = *req.ScheduledStart
	}
	if req.ScheduledEnd != nil {
		existing.ScheduledEnd = *req.ScheduledEnd
	}
	if req.AffectedServices != nil {
		existing.AffectedServices = api.NewOptMaintenanceWindowAffectedServices(*req.AffectedServices)
	}
	existing.UpdatedAt = api.NewOptDateTime(time.Now())

	// Save to database
	if err := s.repo.UpdateMaintenanceWindow(ctx, existing); err != nil {
		s.metrics.RecordError("UpdateMaintenanceWindow")
		return nil, err
	}

	// Update cache
	s.cache.SetMaintenanceWindow(ctx, windowID, existing)

	s.metrics.RecordSuccess("UpdateMaintenanceWindow")
	return existing, nil
}

// CancelMaintenanceWindow cancels a maintenance window
func (s *MaintenanceWindowsService) CancelMaintenanceWindow(ctx context.Context, windowID uuid.UUID) error {
	s.metrics.RecordRequest("CancelMaintenanceWindow")

	// Update status to cancelled
	updateReq := &api.UpdateMaintenanceWindowRequest{
		Status: api.NewOptMaintenanceWindowStatus(api.MaintenanceWindowStatusCancelled),
	}

	if _, err := s.UpdateMaintenanceWindow(ctx, windowID, updateReq); err != nil {
		s.metrics.RecordError("CancelMaintenanceWindow")
		return err
	}

	// Remove from cache
	s.cache.DeleteMaintenanceWindow(ctx, windowID)

	s.metrics.RecordSuccess("CancelMaintenanceWindow")
	return nil
}
