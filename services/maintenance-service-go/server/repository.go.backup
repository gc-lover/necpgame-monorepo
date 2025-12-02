package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/maintenance-service-go/pkg/api"
)

type MaintenanceRepository interface {
	CreateMaintenanceWindow(ctx context.Context, window *api.MaintenanceWindow) error
	ListMaintenanceWindows(ctx context.Context, maintenanceType *string, status *string, limit int, offset int) ([]api.MaintenanceWindow, int, error)
	GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, error)
	UpdateMaintenanceWindow(ctx context.Context, window *api.MaintenanceWindow) error
	CancelMaintenanceWindow(ctx context.Context, windowID uuid.UUID) error
	GetMaintenanceStatus(ctx context.Context) (*api.MaintenanceStatus, error)
	UpdateMaintenanceStatus(ctx context.Context, status *api.MaintenanceStatus) error
	GetNextMaintenance(ctx context.Context) (*api.MaintenanceWindow, error)
	SaveGracefulShutdownStatus(ctx context.Context, status *api.GracefulShutdownStatus) error
	GetGracefulShutdownStatus(ctx context.Context) (*api.GracefulShutdownStatus, error)
	SaveMaintenanceNotification(ctx context.Context, notification *api.MaintenanceNotification) error
	GetMaintenanceNotifications(ctx context.Context, windowID uuid.UUID) ([]api.MaintenanceNotification, int, error)
	GetMaintenanceEvents(ctx context.Context, windowID uuid.UUID, limit int, offset int) ([]api.MaintenanceEvent, int, error)
}

type inMemoryRepository struct {
	mu             sync.RWMutex
	windows        map[uuid.UUID]*api.MaintenanceWindow
	status         *api.MaintenanceStatus
	shutdownStatus *api.GracefulShutdownStatus
	notifications  map[uuid.UUID][]api.MaintenanceNotification
	events         map[uuid.UUID][]api.MaintenanceEvent
}

func NewInMemoryRepository() MaintenanceRepository {
	falseVal := false
	trueVal := true
	now := time.Now()
	zeroInt := 0

	return &inMemoryRepository{
		windows:       make(map[uuid.UUID]*api.MaintenanceWindow),
		notifications: make(map[uuid.UUID][]api.MaintenanceNotification),
		events:        make(map[uuid.UUID][]api.MaintenanceEvent),
		status: &api.MaintenanceStatus{
			IsMaintenanceMode:   &falseVal,
			CurrentWindowId:     nil,
			BlockNewConnections: &falseVal,
			AllowAdminAccess:    &trueVal,
			MaintenanceMessage:  nil,
			UpdatedAt:           &now,
		},
		shutdownStatus: &api.GracefulShutdownStatus{
			IsShuttingDown:      &falseVal,
			WindowId:            nil,
			StartedAt:           nil,
			ServicesNotified:    &zeroInt,
			ServicesShutdown:    &zeroInt,
			TotalServices:       &zeroInt,
			EstimatedCompletion: nil,
		},
	}
}

func (r *inMemoryRepository) CreateMaintenanceWindow(ctx context.Context, window *api.MaintenanceWindow) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	r.windows[uuid.UUID(*window.Id)] = window
	return nil
}

func (r *inMemoryRepository) ListMaintenanceWindows(ctx context.Context, maintenanceType *string, status *string, limit int, offset int) ([]api.MaintenanceWindow, int, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	var filtered []api.MaintenanceWindow
	for _, window := range r.windows {
		if maintenanceType != nil && window.MaintenanceType != nil && string(*window.MaintenanceType) != *maintenanceType {
			continue
		}
		if status != nil && window.Status != nil && string(*window.Status) != *status {
			continue
		}
		filtered = append(filtered, *window)
	}

	total := len(filtered)
	start := offset
	end := offset + limit

	if start > total {
		return []api.MaintenanceWindow{}, total, nil
	}
	if end > total {
		end = total
	}

	return filtered[start:end], total, nil
}

func (r *inMemoryRepository) GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	window, ok := r.windows[windowID]
	if !ok {
		return nil, errors.New("maintenance window not found")
	}

	return window, nil
}

func (r *inMemoryRepository) UpdateMaintenanceWindow(ctx context.Context, window *api.MaintenanceWindow) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	windowID := uuid.UUID(*window.Id)
	if _, ok := r.windows[windowID]; !ok {
		return errors.New("maintenance window not found")
	}

	r.windows[windowID] = window
	return nil
}

func (r *inMemoryRepository) CancelMaintenanceWindow(ctx context.Context, windowID uuid.UUID) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	window, ok := r.windows[windowID]
	if !ok {
		return errors.New("maintenance window not found")
	}

	status := api.MaintenanceWindowStatusCANCELLED
	now := time.Now()
	window.Status = &status
	window.UpdatedAt = &now
	return nil
}

func (r *inMemoryRepository) GetMaintenanceStatus(ctx context.Context) (*api.MaintenanceStatus, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	return r.status, nil
}

func (r *inMemoryRepository) UpdateMaintenanceStatus(ctx context.Context, status *api.MaintenanceStatus) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	r.status = status
	return nil
}

func (r *inMemoryRepository) GetNextMaintenance(ctx context.Context) (*api.MaintenanceWindow, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	var nextWindow *api.MaintenanceWindow
	var earliestStart time.Time

	for _, window := range r.windows {
		if window.Status != nil && (*window.Status == api.MaintenanceWindowStatusPLANNED || *window.Status == api.MaintenanceWindowStatusNOTIFIED) {
			if window.ScheduledStart != nil {
				if nextWindow == nil || window.ScheduledStart.Before(earliestStart) {
					nextWindow = window
					earliestStart = *window.ScheduledStart
				}
			}
		}
	}

	if nextWindow == nil {
		return nil, errors.New("no scheduled maintenance found")
	}

	return nextWindow, nil
}

func (r *inMemoryRepository) SaveGracefulShutdownStatus(ctx context.Context, status *api.GracefulShutdownStatus) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	r.shutdownStatus = status
	return nil
}

func (r *inMemoryRepository) GetGracefulShutdownStatus(ctx context.Context) (*api.GracefulShutdownStatus, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	return r.shutdownStatus, nil
}

func (r *inMemoryRepository) SaveMaintenanceNotification(ctx context.Context, notification *api.MaintenanceNotification) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	windowID := uuid.UUID(*notification.MaintenanceWindowId)
	r.notifications[windowID] = append(r.notifications[windowID], *notification)
	return nil
}

func (r *inMemoryRepository) GetMaintenanceNotifications(ctx context.Context, windowID uuid.UUID) ([]api.MaintenanceNotification, int, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	notifications, ok := r.notifications[windowID]
	if !ok {
		return []api.MaintenanceNotification{}, 0, nil
	}

	return notifications, len(notifications), nil
}

func (r *inMemoryRepository) GetMaintenanceEvents(ctx context.Context, windowID uuid.UUID, limit int, offset int) ([]api.MaintenanceEvent, int, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	events, ok := r.events[windowID]
	if !ok {
		return []api.MaintenanceEvent{}, 0, nil
	}

	total := len(events)
	start := offset
	end := offset + limit

	if start > total {
		return []api.MaintenanceEvent{}, total, nil
	}
	if end > total {
		end = total
	}

	return events[start:end], total, nil
}
