package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/admin-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type AdminRepositoryInterface interface {
	CreateAuditLog(ctx context.Context, log *models.AdminAuditLog) error
	GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error)
	ListAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) ([]models.AdminAuditLog, error)
	CountAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType) (int, error)
}

type AdminService struct {
	repo     AdminRepositoryInterface
	cache    *redis.Client
	logger   *logrus.Logger
	eventBus EventBus
	httpClient *http.Client
}

type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

type RedisEventBus struct {
	client *redis.Client
	logger *logrus.Logger
}

func NewRedisEventBus(redisClient *redis.Client) *RedisEventBus {
	return &RedisEventBus{
		client: redisClient,
		logger: GetLogger(),
	}
}

func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	channel := "events:" + eventType
	return b.client.Publish(ctx, channel, eventData).Err()
}

func NewAdminService(dbURL, redisURL string) (*AdminService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewAdminRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &AdminService{
		repo:       repo,
		cache:      redisClient,
		logger:     GetLogger(),
		eventBus:   eventBus,
		httpClient: &http.Client{Timeout: 10 * time.Second},
	}, nil
}

func (s *AdminService) LogAction(ctx context.Context, adminID uuid.UUID, actionType models.AdminActionType, targetID *uuid.UUID, targetType string, details map[string]interface{}, ipAddress, userAgent string) error {
	auditLog := &models.AdminAuditLog{
		AdminID:    adminID,
		ActionType: actionType,
		TargetID:   targetID,
		TargetType: targetType,
		Details:    details,
		IPAddress:  ipAddress,
		UserAgent:  userAgent,
	}

	return s.repo.CreateAuditLog(ctx, auditLog)
}

func (s *AdminService) BanPlayer(ctx context.Context, adminID uuid.UUID, req *models.BanPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
		"permanent":    req.Permanent,
	}
	if req.Duration != nil {
		details["duration"] = *req.Duration
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeBan, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishPlayerBannedEvent(ctx, req.CharacterID, req.Reason, req.Permanent, req.Duration)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish player banned event")
	}

	RecordAdminAction(string(models.AdminActionTypeBan))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Player banned successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) KickPlayer(ctx context.Context, adminID uuid.UUID, req *models.KickPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeKick, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishPlayerKickedEvent(ctx, req.CharacterID, req.Reason)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish player kicked event")
	}

	RecordAdminAction(string(models.AdminActionTypeKick))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Player kicked successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) MutePlayer(ctx context.Context, adminID uuid.UUID, req *models.MutePlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
		"duration":     req.Duration,
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeMute, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishPlayerMutedEvent(ctx, req.CharacterID, req.Reason, req.Duration)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish player muted event")
	}

	RecordAdminAction(string(models.AdminActionTypeMute))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Player muted successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) GiveItem(ctx context.Context, adminID uuid.UUID, req *models.GiveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"item_id":      req.ItemID,
		"quantity":     req.Quantity,
		"reason":       req.Reason,
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeGiveItem, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishItemGivenEvent(ctx, req.CharacterID, req.ItemID, req.Quantity)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish item given event")
	}

	RecordAdminAction(string(models.AdminActionTypeGiveItem))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Item given successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) RemoveItem(ctx context.Context, adminID uuid.UUID, req *models.RemoveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"item_id":      req.ItemID,
		"quantity":     req.Quantity,
		"reason":       req.Reason,
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeRemoveItem, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	RecordAdminAction(string(models.AdminActionTypeRemoveItem))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Item removed successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) SetCurrency(ctx context.Context, adminID uuid.UUID, req *models.SetCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id":  req.CharacterID.String(),
		"currency_type": req.CurrencyType,
		"amount":        req.Amount,
		"reason":        req.Reason,
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeSetCurrency, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishCurrencySetEvent(ctx, req.CharacterID, req.CurrencyType, req.Amount)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish currency set event")
	}

	RecordAdminAction(string(models.AdminActionTypeSetCurrency))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Currency set successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) AddCurrency(ctx context.Context, adminID uuid.UUID, req *models.AddCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"character_id":  req.CharacterID.String(),
		"currency_type": req.CurrencyType,
		"amount":        req.Amount,
		"reason":        req.Reason,
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeAddCurrency, &req.CharacterID, "character", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishCurrencyAddedEvent(ctx, req.CharacterID, req.CurrencyType, req.Amount)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish currency added event")
	}

	RecordAdminAction(string(models.AdminActionTypeAddCurrency))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Currency added successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) SetWorldFlag(ctx context.Context, adminID uuid.UUID, req *models.SetWorldFlagRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"flag_name":  req.FlagName,
		"flag_value": req.FlagValue,
	}
	if req.Region != nil {
		details["region"] = *req.Region
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeSetWorldFlag, nil, "world", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishWorldFlagSetEvent(ctx, req.FlagName, req.FlagValue, req.Region)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish world flag set event")
	}

	RecordAdminAction(string(models.AdminActionTypeSetWorldFlag))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "World flag set successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) CreateEvent(ctx context.Context, adminID uuid.UUID, req *models.CreateEventRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	details := map[string]interface{}{
		"event_name":    req.EventName,
		"event_type":    req.EventType,
		"description":   req.Description,
		"start_time":    req.StartTime.Format(time.RFC3339),
		"announcement":  req.Announcement,
		"settings":      req.Settings,
	}
	if req.EndTime != nil {
		details["end_time"] = req.EndTime.Format(time.RFC3339)
	}

	err := s.LogAction(ctx, adminID, models.AdminActionTypeCreateEvent, nil, "event", details, ipAddress, userAgent)
	if err != nil {
		return nil, err
	}

	err = s.publishEventCreatedEvent(ctx, req.EventName, req.EventType, req.StartTime, req.EndTime, req.Announcement)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish event created event")
	}

	RecordAdminAction(string(models.AdminActionTypeCreateEvent))

	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Event created successfully",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (s *AdminService) SearchPlayers(ctx context.Context, req *models.SearchPlayersRequest) (*models.PlayerSearchResponse, error) {
	return &models.PlayerSearchResponse{
		Players: []models.PlayerSearchResult{},
		Total:   0,
	}, nil
}

func (s *AdminService) GetAnalytics(ctx context.Context) (*models.AnalyticsResponse, error) {
	return &models.AnalyticsResponse{
		OnlinePlayers:     0,
		EconomyMetrics:   make(map[string]interface{}),
		CombatMetrics:    make(map[string]interface{}),
		PerformanceMetrics: make(map[string]interface{}),
		Timestamp:        time.Now(),
	}, nil
}

func (s *AdminService) GetAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) (*models.AuditLogListResponse, error) {
	logs, err := s.repo.ListAuditLogs(ctx, adminID, actionType, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountAuditLogs(ctx, adminID, actionType)
	if err != nil {
		return nil, err
	}

	return &models.AuditLogListResponse{
		Logs:  logs,
		Total: total,
	}, nil
}

func (s *AdminService) GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error) {
	return s.repo.GetAuditLog(ctx, logID)
}

func (s *AdminService) publishPlayerBannedEvent(ctx context.Context, characterID uuid.UUID, reason string, permanent bool, duration *int64) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"reason":       reason,
		"permanent":    permanent,
		"timestamp":    time.Now().Format(time.RFC3339),
	}
	if duration != nil {
		payload["duration"] = *duration
	}
	return s.eventBus.PublishEvent(ctx, "admin:player-banned", payload)
}

func (s *AdminService) publishPlayerKickedEvent(ctx context.Context, characterID uuid.UUID, reason string) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"reason":       reason,
		"timestamp":    time.Now().Format(time.RFC3339),
	}
	return s.eventBus.PublishEvent(ctx, "admin:player-kicked", payload)
}

func (s *AdminService) publishPlayerMutedEvent(ctx context.Context, characterID uuid.UUID, reason string, duration int64) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"reason":       reason,
		"duration":     duration,
		"timestamp":    time.Now().Format(time.RFC3339),
	}
	return s.eventBus.PublishEvent(ctx, "admin:player-muted", payload)
}

func (s *AdminService) publishItemGivenEvent(ctx context.Context, characterID uuid.UUID, itemID string, quantity int) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"item_id":      itemID,
		"quantity":     quantity,
		"timestamp":    time.Now().Format(time.RFC3339),
	}
	return s.eventBus.PublishEvent(ctx, "admin:item-given", payload)
}

func (s *AdminService) publishCurrencySetEvent(ctx context.Context, characterID uuid.UUID, currencyType string, amount int64) error {
	payload := map[string]interface{}{
		"character_id":  characterID.String(),
		"currency_type": currencyType,
		"amount":        amount,
		"timestamp":     time.Now().Format(time.RFC3339),
	}
	return s.eventBus.PublishEvent(ctx, "admin:currency-set", payload)
}

func (s *AdminService) publishCurrencyAddedEvent(ctx context.Context, characterID uuid.UUID, currencyType string, amount int64) error {
	payload := map[string]interface{}{
		"character_id":  characterID.String(),
		"currency_type": currencyType,
		"amount":        amount,
		"timestamp":     time.Now().Format(time.RFC3339),
	}
	return s.eventBus.PublishEvent(ctx, "admin:currency-added", payload)
}

func (s *AdminService) publishWorldFlagSetEvent(ctx context.Context, flagName string, flagValue map[string]interface{}, region *string) error {
	payload := map[string]interface{}{
		"flag_name":  flagName,
		"flag_value": flagValue,
		"timestamp":  time.Now().Format(time.RFC3339),
	}
	if region != nil {
		payload["region"] = *region
	}
	return s.eventBus.PublishEvent(ctx, "admin:world-flag-set", payload)
}

func (s *AdminService) publishEventCreatedEvent(ctx context.Context, eventName, eventType string, startTime time.Time, endTime *time.Time, announcement bool) error {
	payload := map[string]interface{}{
		"event_name":   eventName,
		"event_type":   eventType,
		"start_time":   startTime.Format(time.RFC3339),
		"announcement": announcement,
		"timestamp":    time.Now().Format(time.RFC3339),
	}
	if endTime != nil {
		payload["end_time"] = endTime.Format(time.RFC3339)
	}
	return s.eventBus.PublishEvent(ctx, "admin:event-created", payload)
}

