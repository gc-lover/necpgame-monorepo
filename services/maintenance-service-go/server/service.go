package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/maintenance-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type MaintenanceService interface {
	CreateMaintenanceWindow(ctx context.Context, req api.CreateMaintenanceWindowRequest) (*api.MaintenanceWindow, error)
	ListMaintenanceWindows(ctx context.Context, maintenanceType *string, status *string, limit int, offset int) (*api.MaintenanceWindowsResponse, error)
	GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, error)
	UpdateMaintenanceWindow(ctx context.Context, windowID uuid.UUID, req api.UpdateMaintenanceWindowRequest) (*api.MaintenanceWindow, error)
	CancelMaintenanceWindow(ctx context.Context, windowID uuid.UUID) error
	GetMaintenanceStatus(ctx context.Context) (*api.MaintenanceStatus, error)
	UpdateMaintenanceStatus(ctx context.Context, req api.UpdateMaintenanceStatusRequest) (*api.MaintenanceStatus, error)
	ScheduleMaintenance(ctx context.Context, req api.ScheduleMaintenanceRequest) (*api.MaintenanceWindow, error)
	GetNextMaintenance(ctx context.Context) (*api.MaintenanceWindow, error)
	StartGracefulShutdown(ctx context.Context, req api.StartGracefulShutdownRequest) (*api.GracefulShutdownStatus, error)
	GetGracefulShutdownStatus(ctx context.Context) (*api.GracefulShutdownStatus, error)
	SendMaintenanceNotification(ctx context.Context, req api.SendMaintenanceNotificationRequest) (int, error)
	GetMaintenanceNotifications(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceNotificationsResponse, error)
	GetMaintenanceEvents(ctx context.Context, windowID uuid.UUID, limit int, offset int) (*api.MaintenanceEventsResponse, error)
	AdminStartMaintenance(ctx context.Context, req api.AdminStartMaintenanceRequest) (*api.MaintenanceWindow, error)
	AdminStopMaintenance(ctx context.Context, req api.AdminStopMaintenanceRequest) error
}

type maintenanceService struct {
	repo   MaintenanceRepository
	logger interface {
		WithField(key string, value interface{}) interface{}
	}
}

func NewMaintenanceService(repo MaintenanceRepository) MaintenanceService {
	return &maintenanceService{
		repo: repo,
	}
}

func (s *maintenanceService) CreateMaintenanceWindow(ctx context.Context, req api.CreateMaintenanceWindowRequest) (*api.MaintenanceWindow, error) {
	id := openapi_types.UUID(uuid.New())
	maintenanceType := api.MaintenanceWindowMaintenanceType(req.MaintenanceType)
	status := api.MaintenanceWindowStatusPLANNED
	now := time.Now()

	window := &api.MaintenanceWindow{
		Id:               &id,
		MaintenanceType:  &maintenanceType,
		Status:           &status,
		ScheduledStart:   &req.ScheduledStart,
		ScheduledEnd:     &req.ScheduledEnd,
		Title:            &req.Title,
		Description:      req.Description,
		Impact:           req.Impact,
		AffectedServices: req.AffectedServices,
		CreatedAt:        &now,
		UpdatedAt:        &now,
	}

	if err := s.repo.CreateMaintenanceWindow(ctx, window); err != nil {
		return nil, err
	}

	return window, nil
}

func (s *maintenanceService) ListMaintenanceWindows(ctx context.Context, maintenanceType *string, status *string, limit int, offset int) (*api.MaintenanceWindowsResponse, error) {
	windows, total, err := s.repo.ListMaintenanceWindows(ctx, maintenanceType, status, limit, offset)
	if err != nil {
		return nil, err
	}

	return &api.MaintenanceWindowsResponse{
		Windows: &windows,
		Total:   &total,
		Limit:   &limit,
		Offset:  &offset,
	}, nil
}

func (s *maintenanceService) GetMaintenanceWindow(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceWindow, error) {
	return s.repo.GetMaintenanceWindow(ctx, windowID)
}

func (s *maintenanceService) UpdateMaintenanceWindow(ctx context.Context, windowID uuid.UUID, req api.UpdateMaintenanceWindowRequest) (*api.MaintenanceWindow, error) {
	window, err := s.repo.GetMaintenanceWindow(ctx, windowID)
	if err != nil {
		return nil, err
	}

	if req.ScheduledStart != nil {
		window.ScheduledStart = req.ScheduledStart
	}
	if req.ScheduledEnd != nil {
		window.ScheduledEnd = req.ScheduledEnd
	}
	if req.Title != nil {
		window.Title = req.Title
	}
	if req.Description != nil {
		window.Description = req.Description
	}
	if req.Impact != nil {
		window.Impact = req.Impact
	}
	if req.AffectedServices != nil {
		window.AffectedServices = req.AffectedServices
	}
	if req.Status != nil {
		status := api.MaintenanceWindowStatus(*req.Status)
		window.Status = &status
	}
	now := time.Now()
	window.UpdatedAt = &now

	if err := s.repo.UpdateMaintenanceWindow(ctx, window); err != nil {
		return nil, err
	}

	return window, nil
}

func (s *maintenanceService) CancelMaintenanceWindow(ctx context.Context, windowID uuid.UUID) error {
	return s.repo.CancelMaintenanceWindow(ctx, windowID)
}

func (s *maintenanceService) GetMaintenanceStatus(ctx context.Context) (*api.MaintenanceStatus, error) {
	return s.repo.GetMaintenanceStatus(ctx)
}

func (s *maintenanceService) UpdateMaintenanceStatus(ctx context.Context, req api.UpdateMaintenanceStatusRequest) (*api.MaintenanceStatus, error) {
	status, err := s.repo.GetMaintenanceStatus(ctx)
	if err != nil {
		return nil, err
	}

	if req.IsMaintenanceMode != nil {
		status.IsMaintenanceMode = req.IsMaintenanceMode
	}
	if req.BlockNewConnections != nil {
		status.BlockNewConnections = req.BlockNewConnections
	}
	if req.AllowAdminAccess != nil {
		status.AllowAdminAccess = req.AllowAdminAccess
	}
	if req.MaintenanceMessage != nil {
		status.MaintenanceMessage = req.MaintenanceMessage
	}
	now := time.Now()
	status.UpdatedAt = &now

	if err := s.repo.UpdateMaintenanceStatus(ctx, status); err != nil {
		return nil, err
	}

	return status, nil
}

func (s *maintenanceService) ScheduleMaintenance(ctx context.Context, req api.ScheduleMaintenanceRequest) (*api.MaintenanceWindow, error) {
	windowID := uuid.UUID(req.WindowId)
	window, err := s.repo.GetMaintenanceWindow(ctx, windowID)
	if err != nil {
		return nil, err
	}

	status := api.MaintenanceWindowStatusNOTIFIED
	now := time.Now()
	window.Status = &status
	window.UpdatedAt = &now

	if err := s.repo.UpdateMaintenanceWindow(ctx, window); err != nil {
		return nil, err
	}

	return window, nil
}

func (s *maintenanceService) GetNextMaintenance(ctx context.Context) (*api.MaintenanceWindow, error) {
	return s.repo.GetNextMaintenance(ctx)
}

func (s *maintenanceService) StartGracefulShutdown(ctx context.Context, req api.StartGracefulShutdownRequest) (*api.GracefulShutdownStatus, error) {
	timeout := 300
	if req.TimeoutSeconds != nil {
		timeout = *req.TimeoutSeconds
	}

	estimatedCompletion := time.Now().Add(time.Duration(timeout) * time.Second)
	totalServices := 10
	trueVal := true
	zeroInt := 0

	status := &api.GracefulShutdownStatus{
		IsShuttingDown:      &trueVal,
		WindowId:            &req.WindowId,
		StartedAt:           ptrTime(time.Now()),
		ServicesNotified:    &zeroInt,
		ServicesShutdown:    &zeroInt,
		TotalServices:       &totalServices,
		EstimatedCompletion: ptrTime(estimatedCompletion),
	}

	if err := s.repo.SaveGracefulShutdownStatus(ctx, status); err != nil {
		return nil, err
	}

	return status, nil
}

func (s *maintenanceService) GetGracefulShutdownStatus(ctx context.Context) (*api.GracefulShutdownStatus, error) {
	return s.repo.GetGracefulShutdownStatus(ctx)
}

func (s *maintenanceService) SendMaintenanceNotification(ctx context.Context, req api.SendMaintenanceNotificationRequest) (int, error) {
	recipientsCount := 100
	id := openapi_types.UUID(uuid.New())
	notificationType := api.MaintenanceNotificationNotificationType(req.NotificationType)
	now := time.Now()

	notification := &api.MaintenanceNotification{
		Id:                  &id,
		MaintenanceWindowId: &req.WindowId,
		NotificationType:    &notificationType,
		SentAt:              &now,
		RecipientsCount:     &recipientsCount,
	}

	if err := s.repo.SaveMaintenanceNotification(ctx, notification); err != nil {
		return 0, err
	}

	return *notification.RecipientsCount, nil
}

func (s *maintenanceService) GetMaintenanceNotifications(ctx context.Context, windowID uuid.UUID) (*api.MaintenanceNotificationsResponse, error) {
	notifications, total, err := s.repo.GetMaintenanceNotifications(ctx, windowID)
	if err != nil {
		return nil, err
	}

	return &api.MaintenanceNotificationsResponse{
		Notifications: &notifications,
		Total:         &total,
	}, nil
}

func (s *maintenanceService) GetMaintenanceEvents(ctx context.Context, windowID uuid.UUID, limit int, offset int) (*api.MaintenanceEventsResponse, error) {
	events, total, err := s.repo.GetMaintenanceEvents(ctx, windowID, limit, offset)
	if err != nil {
		return nil, err
	}

	return &api.MaintenanceEventsResponse{
		Events: &events,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}, nil
}

func (s *maintenanceService) AdminStartMaintenance(ctx context.Context, req api.AdminStartMaintenanceRequest) (*api.MaintenanceWindow, error) {
	windowID := uuid.UUID(req.WindowId)
	window, err := s.repo.GetMaintenanceWindow(ctx, windowID)
	if err != nil {
		return nil, err
	}

	status := api.MaintenanceWindowStatusACTIVE
	now := time.Now()
	window.Status = &status
	window.ActualStart = &now
	window.UpdatedAt = &now

	if err := s.repo.UpdateMaintenanceWindow(ctx, window); err != nil {
		return nil, err
	}

	return window, nil
}

func (s *maintenanceService) AdminStopMaintenance(ctx context.Context, req api.AdminStopMaintenanceRequest) error {
	windowID := uuid.UUID(req.WindowId)
	window, err := s.repo.GetMaintenanceWindow(ctx, windowID)
	if err != nil {
		return err
	}

	status := api.MaintenanceWindowStatusCOMPLETED
	now := time.Now()
	window.Status = &status
	window.ActualEnd = &now
	window.UpdatedAt = &now

	return s.repo.UpdateMaintenanceWindow(ctx, window)
}

func ptrTime(t time.Time) *time.Time {
	return &t
}
