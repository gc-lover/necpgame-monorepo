// Schedule Management Service for Maintenance Mode System
// Issue: #316
// PERFORMANCE: Optimized for high-throughput notification scheduling

package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// ScheduleManager handles maintenance window scheduling and notifications
type ScheduleManager struct {
	repo      *Repository
	cache     *Cache
	logger    *zap.Logger
	mu        sync.RWMutex

	// Notification schedules
	scheduledNotifications map[uuid.UUID][]*ScheduledNotification
	notificationTimers     map[uuid.UUID]*time.Timer
}

// ScheduledNotification represents a scheduled notification event
type ScheduledNotification struct {
	ID               uuid.UUID
	MaintenanceID    uuid.UUID
	NotificationType string
	ScheduledAt      time.Time
	CreatedAt        time.Time
}

// NewScheduleManager creates a new schedule manager
func NewScheduleManager(repo *Repository, cache *Cache) *ScheduleManager {
	sm := &ScheduleManager{
		repo:                   repo,
		cache:                  cache,
		scheduledNotifications: make(map[uuid.UUID][]*ScheduledNotification),
		notificationTimers:     make(map[uuid.UUID]*time.Timer),
		logger:                 zap.NewNop(), // Use proper logger in production
	}

	// Start background notification processor
	go sm.processNotifications()

	return sm
}

// ScheduleMaintenanceNotifications schedules notifications for a maintenance window
func (sm *ScheduleManager) ScheduleMaintenanceNotifications(ctx context.Context, maintenanceID uuid.UUID, startTime time.Time) error {
	sm.mu.Lock()
	defer sm.mu.Unlock()

	// Calculate notification times (24h, 6h, 1h before start)
	baseTime := startTime
	notifications := []struct {
		notificationType string
		offset           time.Duration
	}{
		{"SCHEDULED_24H", -24 * time.Hour},
		{"SCHEDULED_6H", -6 * time.Hour},
		{"SCHEDULED_1H", -1 * time.Hour},
	}

	scheduled := make([]*ScheduledNotification, 0, len(notifications))

	for _, notif := range notifications {
		scheduledAt := baseTime.Add(notif.offset)

		// Only schedule future notifications
		if scheduledAt.After(time.Now()) {
			scheduledNotif := &ScheduledNotification{
				ID:               uuid.New(),
				MaintenanceID:    maintenanceID,
				NotificationType: notif.notificationType,
				ScheduledAt:      scheduledAt,
				CreatedAt:        time.Now(),
			}

			scheduled = append(scheduled, scheduledNotif)

			// Schedule timer for this notification
			sm.scheduleNotificationTimer(scheduledNotif)
		}
	}

	sm.scheduledNotifications[maintenanceID] = scheduled

	sm.logger.Info("Scheduled maintenance notifications",
		zap.String("maintenance_id", maintenanceID.String()),
		zap.Int("notification_count", len(scheduled)))

	return nil
}

// CancelMaintenanceNotifications cancels all scheduled notifications for a maintenance window
func (sm *ScheduleManager) CancelMaintenanceNotifications(ctx context.Context, maintenanceID uuid.UUID) error {
	sm.mu.Lock()
	defer sm.mu.Unlock()

	// Cancel and remove timers
	if timer, exists := sm.notificationTimers[maintenanceID]; exists {
		timer.Stop()
		delete(sm.notificationTimers, maintenanceID)
	}

	// Remove scheduled notifications
	delete(sm.scheduledNotifications, maintenanceID)

	sm.logger.Info("Cancelled maintenance notifications",
		zap.String("maintenance_id", maintenanceID.String()))

	return nil
}

// GetScheduledNotifications returns scheduled notifications for a maintenance window
func (sm *ScheduleManager) GetScheduledNotifications(ctx context.Context, maintenanceID uuid.UUID) ([]*ScheduledNotification, error) {
	sm.mu.RLock()
	defer sm.mu.RUnlock()

	notifications, exists := sm.scheduledNotifications[maintenanceID]
	if !exists {
		return []*ScheduledNotification{}, nil
	}

	// Return copy to prevent external modifications
	result := make([]*ScheduledNotification, len(notifications))
	copy(result, notifications)

	return result, nil
}

// scheduleNotificationTimer schedules a timer for a notification
func (sm *ScheduleManager) scheduleNotificationTimer(notification *ScheduledNotification) {
	duration := time.Until(notification.ScheduledAt)

	if duration <= 0 {
		// Notification should be sent immediately
		go sm.sendNotification(notification)
		return
	}

	timer := time.AfterFunc(duration, func() {
		sm.sendNotification(notification)
	})

	sm.notificationTimers[notification.MaintenanceID] = timer
}

// sendNotification sends a scheduled notification
func (sm *ScheduleManager) sendNotification(notification *ScheduledNotification) {
	sm.logger.Info("Sending scheduled notification",
		zap.String("notification_id", notification.ID.String()),
		zap.String("maintenance_id", notification.MaintenanceID.String()),
		zap.String("type", notification.NotificationType))

	// TODO: Integrate with actual notification service
	// For now, just log the notification

	// Remove from scheduled notifications after sending
	sm.mu.Lock()
	if notifications, exists := sm.scheduledNotifications[notification.MaintenanceID]; exists {
		for i, n := range notifications {
			if n.ID == notification.ID {
				// Remove this notification
				sm.scheduledNotifications[notification.MaintenanceID] = append(
					notifications[:i],
					notifications[i+1:]...,
				)
				break
			}
		}
	}
	sm.mu.Unlock()
}

// processNotifications processes pending notifications (background worker)
func (sm *ScheduleManager) processNotifications() {
	ticker := time.NewTicker(1 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		sm.mu.RLock()
		now := time.Now()

		for maintenanceID, notifications := range sm.scheduledNotifications {
			for _, notification := range notifications {
				if notification.ScheduledAt.Before(now) || notification.ScheduledAt.Equal(now) {
					// Send notification asynchronously
					go sm.sendNotification(notification)
				}
			}
		}

		sm.mu.RUnlock()
	}
}

// ValidateSchedule validates that a maintenance schedule is reasonable
func (sm *ScheduleManager) ValidateSchedule(ctx context.Context, startTime, endTime time.Time) error {
	now := time.Now()

	if startTime.Before(now.Add(1 * time.Hour)) {
		return errors.New("maintenance must be scheduled at least 1 hour in advance")
	}

	if endTime.Before(startTime) {
		return errors.New("end time must be after start time")
	}

	duration := endTime.Sub(startTime)
	if duration < 15*time.Minute {
		return errors.New("maintenance must be at least 15 minutes long")
	}

	if duration > 24*time.Hour {
		return errors.New("maintenance cannot be longer than 24 hours")
	}

	return nil
}

// GetUpcomingMaintenance returns upcoming maintenance windows
func (sm *ScheduleManager) GetUpcomingMaintenance(ctx context.Context, within time.Duration) ([]*server.MaintenanceWindow, error) {
	// TODO: Query database for upcoming maintenance windows
	// For now, return empty slice

	return []*server.MaintenanceWindow{}, nil
}
