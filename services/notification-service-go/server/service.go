// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #140874394
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// NotificationService содержит бизнес-логику уведомлений
type NotificationService struct {
	repo      *NotificationRepository
	wsManager *WebSocketManager
	logger    *zap.Logger
}

// Notification представляет уведомление в системе
type Notification struct {
	ID        string                 `json:"id" db:"id"`
	UserID    string                 `json:"user_id" db:"user_id"`
	Type      string                 `json:"type" db:"type"`
	Title     string                 `json:"title" db:"title"`
	Message   string                 `json:"message" db:"message"`
	Data      map[string]interface{} `json:"data" db:"data"`
	Priority  string                 `json:"priority" db:"priority"`
	Status    string                 `json:"status" db:"status"`
	ExpiresAt *time.Time             `json:"expires_at" db:"expires_at"`
	CreatedAt time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt time.Time              `json:"updated_at" db:"updated_at"`
}

// NewNotificationService создает новый сервис уведомлений
func NewNotificationService(db *sql.DB, wsManager *WebSocketManager, logger *zap.Logger) *NotificationService {
	return &NotificationService{
		repo:      NewNotificationRepository(db, logger),
		wsManager: wsManager,
		logger:    logger,
	}
}

// CreateNotification создает новое уведомление
func (s *NotificationService) CreateNotification(ctx context.Context, userID, notifType, title, message string, data map[string]interface{}, priority string, expiresAt *time.Time) (*Notification, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Creating notification",
		zap.String("user_id", userID),
		zap.String("type", notifType),
		zap.String("title", title))

	notification := &Notification{
		ID:        uuid.New().String(),
		UserID:    userID,
		Type:      notifType,
		Title:     title,
		Message:   message,
		Data:      data,
		Priority:  priority,
		Status:    "unread",
		ExpiresAt: expiresAt,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	if err := s.repo.CreateNotification(ctx, notification); err != nil {
		s.logger.Error("Failed to create notification", zap.Error(err))
		return nil, fmt.Errorf("failed to create notification: %w", err)
	}

	// Отправляем через WebSocket если пользователь онлайн
	s.wsManager.BroadcastNotification(userID, notification)

	s.logger.Info("Notification created successfully",
		zap.String("notification_id", notification.ID),
		zap.String("user_id", userID))

	return notification, nil
}

// GetUserNotifications получает уведомления пользователя
func (s *NotificationService) GetUserNotifications(ctx context.Context, userID string, limit, offset int, statusFilter string) ([]*Notification, int, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Getting user notifications",
		zap.String("user_id", userID),
		zap.Int("limit", limit),
		zap.Int("offset", offset),
		zap.String("status_filter", statusFilter))

	notifications, total, err := s.repo.GetUserNotifications(ctx, userID, limit, offset, statusFilter)
	if err != nil {
		s.logger.Error("Failed to get user notifications", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get notifications: %w", err)
	}

	return notifications, total, nil
}

// GetNotificationByID получает уведомление по ID
func (s *NotificationService) GetNotificationByID(ctx context.Context, notificationID, userID string) (*Notification, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	notification, err := s.repo.GetNotificationByID(ctx, notificationID, userID)
	if err != nil {
		s.logger.Error("Failed to get notification by ID",
			zap.String("notification_id", notificationID),
			zap.String("user_id", userID),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get notification: %w", err)
	}

	return notification, nil
}

// UpdateNotification обновляет уведомление
func (s *NotificationService) UpdateNotification(ctx context.Context, notificationID, userID string, updates map[string]interface{}) (*Notification, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Updating notification",
		zap.String("notification_id", notificationID),
		zap.String("user_id", userID))

	notification, err := s.repo.UpdateNotification(ctx, notificationID, userID, updates)
	if err != nil {
		s.logger.Error("Failed to update notification", zap.Error(err))
		return nil, fmt.Errorf("failed to update notification: %w", err)
	}

	return notification, nil
}

// MarkAsRead отмечает уведомление как прочитанное
func (s *NotificationService) MarkAsRead(ctx context.Context, notificationID, userID string) error {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	s.logger.Info("Marking notification as read",
		zap.String("notification_id", notificationID),
		zap.String("user_id", userID))

	if err := s.repo.MarkAsRead(ctx, notificationID, userID); err != nil {
		s.logger.Error("Failed to mark notification as read", zap.Error(err))
		return fmt.Errorf("failed to mark as read: %w", err)
	}

	return nil
}

// MarkBulkAsRead отмечает несколько уведомлений как прочитанные
func (s *NotificationService) MarkBulkAsRead(ctx context.Context, notificationIDs []string, userID string) (int, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	s.logger.Info("Marking bulk notifications as read",
		zap.Strings("notification_ids", notificationIDs),
		zap.String("user_id", userID))

	processedCount, err := s.repo.MarkBulkAsRead(ctx, notificationIDs, userID)
	if err != nil {
		s.logger.Error("Failed to mark bulk notifications as read", zap.Error(err))
		return 0, fmt.Errorf("failed to mark bulk as read: %w", err)
	}

	return processedCount, nil
}

// GetUnreadCount получает количество непрочитанных уведомлений пользователя
func (s *NotificationService) GetUnreadCount(ctx context.Context, userID string) (int, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	count, err := s.repo.GetUnreadCount(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to get unread count",
			zap.String("user_id", userID),
			zap.Error(err))
		return 0, fmt.Errorf("failed to get unread count: %w", err)
	}

	return count, nil
}

// CleanExpiredNotifications очищает истекшие уведомления
func (s *NotificationService) CleanExpiredNotifications(ctx context.Context) (int64, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	cleanedCount, err := s.repo.CleanExpiredNotifications(ctx)
	if err != nil {
		s.logger.Error("Failed to clean expired notifications", zap.Error(err))
		return 0, fmt.Errorf("failed to clean expired notifications: %w", err)
	}

	s.logger.Info("Cleaned expired notifications", zap.Int64("count", cleanedCount))
	return cleanedCount, nil
}

// HTTP Handlers

// GetNotificationsHandler обрабатывает GET /api/v1/notifications
func (s *NotificationService) GetNotificationsHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	// Парсим query параметры
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")
	statusFilter := r.URL.Query().Get("status")

	limit := 20 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	if statusFilter == "" {
		statusFilter = "unread"
	}

	// Получаем уведомления
	notifications, total, err := s.GetUserNotifications(r.Context(), userID, limit, offset, statusFilter)
	if err != nil {
		s.logger.Error("Failed to get notifications", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	// Получаем количество непрочитанных
	unreadCount, err := s.GetUnreadCount(r.Context(), userID)
	if err != nil {
		s.logger.Warn("Failed to get unread count", zap.Error(err))
		unreadCount = 0
	}

	response := map[string]interface{}{
		"notifications": notifications,
		"total":         total,
		"unread_count":  unreadCount,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// CreateNotificationHandler обрабатывает POST /api/v1/notifications
func (s *NotificationService) CreateNotificationHandler(w http.ResponseWriter, r *http.Request) {
	var req map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	userID, ok := req["user_id"].(string)
	if !ok {
		http.Error(w, "user_id is required", http.StatusBadRequest)
		return
	}

	notifType, ok := req["type"].(string)
	if !ok {
		http.Error(w, "type is required", http.StatusBadRequest)
		return
	}

	title, ok := req["title"].(string)
	if !ok {
		http.Error(w, "title is required", http.StatusBadRequest)
		return
	}

	message, ok := req["message"].(string)
	if !ok {
		http.Error(w, "message is required", http.StatusBadRequest)
		return
	}

	data, _ := req["data"].(map[string]interface{})
	priority, _ := req["priority"].(string)
	if priority == "" {
		priority = "normal"
	}

	var expiresAt *time.Time
	if expiresStr, ok := req["expires_at"].(string); ok {
		if t, err := time.Parse(time.RFC3339, expiresStr); err == nil {
			expiresAt = &t
		}
	}

	notification, err := s.CreateNotification(r.Context(), userID, notifType, title, message, data, priority, expiresAt)
	if err != nil {
		s.logger.Error("Failed to create notification", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(notification)
}

// GetNotificationHandler обрабатывает GET /api/v1/notifications/{notification_id}
func (s *NotificationService) GetNotificationHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)
	notificationID := chi.URLParam(r, "notification_id")

	notification, err := s.GetNotificationByID(r.Context(), notificationID, userID)
	if err != nil {
		http.Error(w, "Notification not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(notification)
}

// UpdateNotificationHandler обрабатывает PUT /api/v1/notifications/{notification_id}
func (s *NotificationService) UpdateNotificationHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)
	notificationID := chi.URLParam(r, "notification_id")

	var updates map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&updates); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	notification, err := s.UpdateNotification(r.Context(), notificationID, userID, updates)
	if err != nil {
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(notification)
}

// MarkAsReadHandler обрабатывает POST /api/v1/notifications/{notification_id}/read
func (s *NotificationService) MarkAsReadHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)
	notificationID := chi.URLParam(r, "notification_id")

	if err := s.MarkAsRead(r.Context(), notificationID, userID); err != nil {
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]bool{"success": true})
}

// MarkBulkAsReadHandler обрабатывает POST /api/v1/notifications/bulk-read
func (s *NotificationService) MarkBulkAsReadHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	var req struct {
		NotificationIDs []string `json:"notification_ids"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	if len(req.NotificationIDs) == 0 || len(req.NotificationIDs) > 100 {
		http.Error(w, "notification_ids must contain 1-100 items", http.StatusBadRequest)
		return
	}

	processedCount, err := s.MarkBulkAsRead(r.Context(), req.NotificationIDs, userID)
	if err != nil {
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"success":        true,
		"processed_count": processedCount,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// HealthCheckHandler проверяет здоровье сервиса
func (s *NotificationService) HealthCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "healthy"}`))
}

// ReadinessCheckHandler проверяет готовность сервиса
func (s *NotificationService) ReadinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "ready"}`))
}

// MetricsHandler предоставляет метрики сервиса
func (s *NotificationService) MetricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"service": "notification-service", "version": "1.0.0"}`))
}
