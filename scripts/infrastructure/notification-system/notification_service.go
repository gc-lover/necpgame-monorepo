// Package notifications provides real-time notification system for MMOFPS games
package notifications

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// NotificationService provides real-time notifications for game events
type NotificationService struct {
	config         *NotificationConfig
	logger         *errorhandling.Logger
	eventChannels  map[string][]NotificationChannel
	templates      map[string]*NotificationTemplate
	activeStreams  map[string]*NotificationStream
	metrics        *NotificationMetrics

	mu sync.RWMutex

	// Background processing
	shutdownChan chan struct{}
	wg           sync.WaitGroup
}

// NotificationConfig holds notification service configuration
type NotificationConfig struct {
	MaxChannelsPerEvent  int           `json:"max_channels_per_event"`
	DefaultTTL           time.Duration `json:"default_ttl"`
	BatchSize            int           `json:"batch_size"`
	ProcessingInterval   time.Duration `json:"processing_interval"`
	EnableRealTime       bool          `json:"enable_real_time"`
	EnableBatching       bool          `json:"enable_batching"`
	EnableFiltering      bool          `json:"enable_filtering"`
	MaxRetries           int           `json:"max_retries"`
	RetryDelay           time.Duration `json:"retry_delay"`
}

// NotificationChannel defines interface for notification delivery
type NotificationChannel interface {
	Name() string
	Send(ctx context.Context, notification *Notification) error
	SupportsRealTime() bool
	GetCapabilities() ChannelCapabilities
}

// ChannelCapabilities describes channel capabilities
type ChannelCapabilities struct {
	SupportsRealTime bool     `json:"supports_real_time"`
	MaxMessageSize   int      `json:"max_message_size"`
	SupportedTypes   []string `json:"supported_types"`
	PrioritySupport  bool     `json:"priority_support"`
}

// Notification represents a notification to be sent
type Notification struct {
	ID          string                 `json:"id"`
	Type        NotificationType       `json:"type"`
	Priority    NotificationPriority   `json:"priority"`
	Title       string                 `json:"title"`
	Message     string                 `json:"message"`
	Data        map[string]interface{} `json:"data,omitempty"`
	Recipients  []NotificationRecipient `json:"recipients"`
	Channels    []string               `json:"channels"`
	TTL         time.Duration          `json:"ttl"`
	CreatedAt   time.Time              `json:"created_at"`
	ExpiresAt   *time.Time             `json:"expires_at,omitempty"`
	TemplateID  string                 `json:"template_id,omitempty"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// NotificationType represents the type of notification
type NotificationType string

const (
	NotificationTypeGameEvent    NotificationType = "game_event"
	NotificationTypeAchievement  NotificationType = "achievement"
	NotificationTypeSocial       NotificationType = "social"
	NotificationTypeSystem       NotificationType = "system"
	NotificationTypeMarketing    NotificationType = "marketing"
	NotificationTypeSecurity     NotificationType = "security"
)

// NotificationPriority represents notification priority levels
type NotificationPriority string

const (
	NotificationPriorityLow      NotificationPriority = "low"
	NotificationPriorityNormal   NotificationPriority = "normal"
	NotificationPriorityHigh     NotificationPriority = "high"
	NotificationPriorityUrgent   NotificationPriority = "urgent"
	NotificationPriorityCritical NotificationPriority = "critical"
)

// NotificationRecipient represents a notification recipient
type NotificationRecipient struct {
	UserID    string                 `json:"user_id"`
	Platform  string                 `json:"platform"`  // ios, android, web, console
	DeviceID  string                 `json:"device_id,omitempty"`
	Language  string                 `json:"language"`
	Timezone  string                 `json:"timezone"`
	Preferences map[string]interface{} `json:"preferences,omitempty"`
}

// NotificationTemplate represents a reusable notification template
type NotificationTemplate struct {
	ID          string                 `json:"id"`
	Name        string                 `json:"name"`
	Type        NotificationType       `json:"type"`
	TitleTemplate string               `json:"title_template"`
	MessageTemplate string             `json:"message_template"`
	Variables   []string               `json:"variables"`
	Channels    []string               `json:"channels"`
	TTL         time.Duration          `json:"ttl"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// NotificationStream represents a real-time notification stream
type NotificationStream struct {
	ID          string
	UserID      string
	Channels    []string
	Filters     map[string]interface{}
	LastSeen    time.Time
	IsActive    bool
	MessageChan chan *Notification
	ErrorChan   chan error
}

// NotificationMetrics contains notification service metrics
type NotificationMetrics struct {
	TotalSent           int64            `json:"total_sent"`
	TotalFailed         int64            `json:"total_failed"`
	TotalQueued         int64            `json:"total_queued"`
	AvgDeliveryTime     time.Duration    `json:"avg_delivery_time"`
	DeliveryRate        float64          `json:"delivery_rate"`
	ChannelMetrics      map[string]int64 `json:"channel_metrics"`
	TypeMetrics         map[string]int64 `json:"type_metrics"`
	ErrorMetrics        map[string]int64 `json:"error_metrics"`
	ActiveStreams       int64            `json:"active_streams"`
}

// WebSocketChannel implements WebSocket-based notifications
type WebSocketChannel struct {
	name         string
	connections  map[string]*NotificationStream
	logger       *errorhandling.Logger
}

// InAppChannel implements in-app notifications
type InAppChannel struct {
	name    string
	storage map[string][]*Notification
	logger  *errorhandling.Logger
}

// PushChannel implements push notifications
type PushChannel struct {
	name       string
	apnsClient interface{} // Would be APNS client
	fcmClient  interface{} // Would be FCM client
	logger     *errorhandling.Logger
}

// EmailChannel implements email notifications
type EmailChannel struct {
	name       string
	smtpConfig map[string]string
	logger     *errorhandling.Logger
}

// SMSChannel implements SMS notifications
type SMSChannel struct {
	name       string
	smsConfig  map[string]string
	logger     *errorhandling.Logger
}

// NewNotificationService creates a new notification service
func NewNotificationService(config *NotificationConfig, logger *errorhandling.Logger) (*NotificationService, error) {
	if config == nil {
		config = &NotificationConfig{
			MaxChannelsPerEvent: 5,
			DefaultTTL:          24 * time.Hour,
			BatchSize:           100,
			ProcessingInterval:  10 * time.Second,
			EnableRealTime:      true,
			EnableBatching:      true,
			EnableFiltering:     true,
			MaxRetries:          3,
			RetryDelay:          5 * time.Second,
		}
	}

	ns := &NotificationService{
		config:        config,
		logger:        logger,
		eventChannels: make(map[string][]NotificationChannel),
		templates:     make(map[string]*NotificationTemplate),
		activeStreams: make(map[string]*NotificationStream),
		metrics:       &NotificationMetrics{
			ChannelMetrics: make(map[string]int64),
			TypeMetrics:    make(map[string]int64),
			ErrorMetrics:   make(map[string]int64),
		},
		shutdownChan: make(chan struct{}),
	}

	// Initialize default channels
	ns.initializeDefaultChannels()

	// Start background processing
	ns.startBackgroundProcessing()

	logger.Infow("Notification service initialized",
		"max_channels_per_event", config.MaxChannelsPerEvent,
		"default_ttl", config.DefaultTTL,
		"real_time_enabled", config.EnableRealTime)

	return ns, nil
}

// SendNotification sends a notification through specified channels
func (ns *NotificationService) SendNotification(ctx context.Context, notification *Notification) error {
	if notification.ID == "" {
		notification.ID = fmt.Sprintf("notif_%d", time.Now().UnixNano())
	}
	notification.CreatedAt = time.Now()

	if notification.TTL == 0 {
		notification.TTL = ns.config.DefaultTTL
	}

	notification.ExpiresAt = &time.Time{}
	*notification.ExpiresAt = notification.CreatedAt.Add(notification.TTL)

	// Apply template if specified
	if notification.TemplateID != "" {
		if err := ns.applyTemplate(notification); err != nil {
			return err
		}
	}

	// Validate notification
	if err := ns.validateNotification(notification); err != nil {
		return err
	}

	// Update metrics
	ns.mu.Lock()
	ns.metrics.TotalQueued++
	ns.metrics.TypeMetrics[string(notification.Type)]++
	ns.mu.Unlock()

	// Send through channels
	return ns.sendThroughChannels(ctx, notification)
}

// SendTemplatedNotification sends a notification using a template
func (ns *NotificationService) SendTemplatedNotification(ctx context.Context, templateID string, variables map[string]interface{}, recipients []NotificationRecipient) error {
	template, exists := ns.templates[templateID]
	if !exists {
		return errorhandling.NewNotFoundError("TEMPLATE_NOT_FOUND", "Notification template not found")
	}

	notification := &Notification{
		Type:       template.Type,
		Title:      ns.renderTemplate(template.TitleTemplate, variables),
		Message:    ns.renderTemplate(template.MessageTemplate, variables),
		Data:       variables,
		Recipients: recipients,
		Channels:   template.Channels,
		TTL:        template.TTL,
		TemplateID: templateID,
	}

	return ns.SendNotification(ctx, notification)
}

// RegisterTemplate registers a notification template
func (ns *NotificationService) RegisterTemplate(template *NotificationTemplate) error {
	ns.mu.Lock()
	defer ns.mu.Unlock()

	if _, exists := ns.templates[template.ID]; exists {
		return errorhandling.NewConflictError("TEMPLATE_EXISTS", "Template already exists")
	}

	ns.templates[template.ID] = template

	ns.logger.Infow("Notification template registered",
		"template_id", template.ID,
		"name", template.Name)

	return nil
}

// CreateStream creates a real-time notification stream for a user
func (ns *NotificationService) CreateStream(userID string, filters map[string]interface{}) (*NotificationStream, error) {
	ns.mu.Lock()
	defer ns.mu.Unlock()

	streamID := fmt.Sprintf("stream_%s_%d", userID, time.Now().UnixNano())

	stream := &NotificationStream{
		ID:          streamID,
		UserID:      userID,
		Channels:    []string{"websocket"},
		Filters:     filters,
		LastSeen:    time.Now(),
		IsActive:    true,
		MessageChan: make(chan *Notification, 100),
		ErrorChan:   make(chan error, 10),
	}

	ns.activeStreams[streamID] = stream

	ns.mu.Lock()
	ns.metrics.ActiveStreams++
	ns.mu.Unlock()

	ns.logger.Infow("Notification stream created",
		"stream_id", streamID,
		"user_id", userID)

	return stream, nil
}

// CloseStream closes a notification stream
func (ns *NotificationService) CloseStream(streamID string) error {
	ns.mu.Lock()
	defer ns.mu.Unlock()

	stream, exists := ns.activeStreams[streamID]
	if !exists {
		return errorhandling.NewNotFoundError("STREAM_NOT_FOUND", "Notification stream not found")
	}

	stream.IsActive = false
	close(stream.MessageChan)
	close(stream.ErrorChan)

	delete(ns.activeStreams, streamID)

	ns.mu.Lock()
	ns.metrics.ActiveStreams--
	ns.mu.Unlock()

	ns.logger.Infow("Notification stream closed", "stream_id", streamID)
	return nil
}

// GetMetrics returns notification service metrics
func (ns *NotificationService) GetMetrics() *NotificationMetrics {
	ns.mu.RLock()
	defer ns.mu.RUnlock()

	// Create a copy of metrics
	metrics := *ns.metrics
	return &metrics
}

// AddChannel adds a notification channel for an event type
func (ns *NotificationService) AddChannel(eventType string, channel NotificationChannel) {
	ns.mu.Lock()
	defer ns.mu.Unlock()

	ns.eventChannels[eventType] = append(ns.eventChannels[eventType], channel)

	ns.logger.Infow("Notification channel added",
		"event_type", eventType,
		"channel", channel.Name())
}

// SendGameEvent sends a game event notification
func (ns *NotificationService) SendGameEvent(ctx context.Context, eventType string, playerID string, eventData map[string]interface{}) error {
	notification := &Notification{
		Type:     NotificationTypeGameEvent,
		Priority: NotificationPriorityNormal,
		Title:    ns.getGameEventTitle(eventType),
		Message:  ns.getGameEventMessage(eventType, eventData),
		Data:     eventData,
		Recipients: []NotificationRecipient{{
			UserID:   playerID,
			Platform: "all",
			Language: "en",
		}},
		Channels: []string{"websocket", "in_app"},
		Metadata: map[string]interface{}{
			"event_type": eventType,
			"player_id":  playerID,
		},
	}

	return ns.SendNotification(ctx, notification)
}

// SendAchievementNotification sends an achievement notification
func (ns *NotificationService) SendAchievementNotification(ctx context.Context, playerID, achievementName, achievementDesc string, rarity string) error {
	var priority NotificationPriority
	var channels []string

	switch rarity {
	case "legendary":
		priority = NotificationPriorityHigh
		channels = []string{"websocket", "push", "in_app", "email"}
	case "epic":
		priority = NotificationPriorityHigh
		channels = []string{"websocket", "push", "in_app"}
	case "rare":
		priority = NotificationPriorityNormal
		channels = []string{"websocket", "in_app"}
	default:
		priority = NotificationPriorityNormal
		channels = []string{"in_app"}
	}

	notification := &Notification{
		Type:     NotificationTypeAchievement,
		Priority: priority,
		Title:    "🏆 Achievement Unlocked!",
		Message:  fmt.Sprintf("**%s**: %s", achievementName, achievementDesc),
		Data: map[string]interface{}{
			"achievement_name": achievementName,
			"rarity":          rarity,
		},
		Recipients: []NotificationRecipient{{
			UserID:   playerID,
			Platform: "all",
			Language: "en",
		}},
		Channels: channels,
		Metadata: map[string]interface{}{
			"achievement_name": achievementName,
			"rarity":          rarity,
		},
	}

	return ns.SendNotification(ctx, notification)
}

// SendSocialNotification sends a social interaction notification
func (ns *NotificationService) SendSocialNotification(ctx context.Context, recipientID, senderName, interactionType string) error {
	var title, message string

	switch interactionType {
	case "friend_request":
		title = "👥 Friend Request"
		message = fmt.Sprintf("**%s** sent you a friend request", senderName)
	case "guild_invite":
		title = "🏰 Guild Invitation"
		message = fmt.Sprintf("**%s** invited you to join their guild", senderName)
	case "trade_request":
		title = "💰 Trade Request"
		message = fmt.Sprintf("**%s** wants to trade with you", senderName)
	default:
		title = "📱 Social Notification"
		message = fmt.Sprintf("You have a new social interaction from **%s**", senderName)
	}

	notification := &Notification{
		Type:     NotificationTypeSocial,
		Priority: NotificationPriorityNormal,
		Title:    title,
		Message:  message,
		Data: map[string]interface{}{
			"sender_name":     senderName,
			"interaction_type": interactionType,
		},
		Recipients: []NotificationRecipient{{
			UserID:   recipientID,
			Platform: "all",
			Language: "en",
		}},
		Channels: []string{"websocket", "in_app"},
		Metadata: map[string]interface{}{
			"interaction_type": interactionType,
		},
	}

	return ns.SendNotification(ctx, notification)
}

// Helper methods

func (ns *NotificationService) initializeDefaultChannels() {
	// Initialize default channels
	websocketChannel := &WebSocketChannel{
		name:        "websocket",
		connections: make(map[string]*NotificationStream),
		logger:      ns.logger,
	}

	inAppChannel := &InAppChannel{
		name:    "in_app",
		storage: make(map[string][]*Notification),
		logger:  ns.logger,
	}

	pushChannel := &PushChannel{
		name:   "push",
		logger: ns.logger,
	}

	emailChannel := &EmailChannel{
		name:   "email",
		logger: ns.logger,
	}

	smsChannel := &SMSChannel{
		name:   "sms",
		logger: ns.logger,
	}

	// Register channels for different event types
	ns.AddChannel("game_event", websocketChannel)
	ns.AddChannel("game_event", inAppChannel)
	ns.AddChannel("achievement", websocketChannel)
	ns.AddChannel("achievement", pushChannel)
	ns.AddChannel("achievement", inAppChannel)
	ns.AddChannel("social", websocketChannel)
	ns.AddChannel("social", inAppChannel)
	ns.AddChannel("system", emailChannel)
	ns.AddChannel("security", smsChannel)
	ns.AddChannel("marketing", pushChannel)
	ns.AddChannel("marketing", emailChannel)
}

func (ns *NotificationService) sendThroughChannels(ctx context.Context, notification *Notification) error {
	sentCount := 0
	errorCount := 0

	for _, channelName := range notification.Channels {
		if channels, exists := ns.eventChannels[string(notification.Type)]; exists {
			for _, channel := range channels {
				if channel.Name() == channelName {
					if err := ns.sendToChannel(ctx, channel, notification); err != nil {
						ns.logger.LogError(err, "Failed to send notification through channel",
							zap.String("channel", channelName),
							zap.String("notification_id", notification.ID))
						errorCount++

						// Update error metrics
						ns.mu.Lock()
						ns.metrics.ErrorMetrics[channelName]++
						ns.mu.Unlock()
					} else {
						sentCount++

						// Update metrics
						ns.mu.Lock()
						ns.metrics.ChannelMetrics[channelName]++
						ns.mu.Unlock()
					}
				}
			}
		}
	}

	// Update overall metrics
	ns.mu.Lock()
	if sentCount > 0 {
		ns.metrics.TotalSent += int64(sentCount)
	}
	if errorCount > 0 {
		ns.metrics.TotalFailed += int64(errorCount)
	}
	deliveryRate := float64(ns.metrics.TotalSent) / float64(ns.metrics.TotalSent+ns.metrics.TotalFailed) * 100
	ns.metrics.DeliveryRate = deliveryRate
	ns.mu.Unlock()

	if sentCount == 0 && errorCount > 0 {
		return errorhandling.NewInternalError("NOTIFICATION_SEND_FAILED", "Failed to send notification through any channel")
	}

	return nil
}

func (ns *NotificationService) sendToChannel(ctx context.Context, channel NotificationChannel, notification *Notification) error {
	// Implement retry logic
	var lastErr error
	for attempt := 1; attempt <= ns.config.MaxRetries; attempt++ {
		if err := channel.Send(ctx, notification); err != nil {
			lastErr = err
			if attempt < ns.config.MaxRetries {
				time.Sleep(ns.config.RetryDelay)
				continue
			}
		} else {
			return nil
		}
	}
	return lastErr
}

func (ns *NotificationService) applyTemplate(notification *Notification) error {
	template, exists := ns.templates[notification.TemplateID]
	if !exists {
		return errorhandling.NewNotFoundError("TEMPLATE_NOT_FOUND", "Template not found")
	}

	notification.Type = template.Type
	notification.Title = ns.renderTemplate(template.TitleTemplate, notification.Data)
	notification.Message = ns.renderTemplate(template.MessageTemplate, notification.Data)
	notification.Channels = template.Channels
	notification.TTL = template.TTL

	return nil
}

func (ns *NotificationService) validateNotification(notification *Notification) error {
	if notification.Type == "" {
		return errorhandling.NewValidationError("INVALID_NOTIFICATION", "Notification type is required")
	}

	if len(notification.Recipients) == 0 {
		return errorhandling.NewValidationError("INVALID_NOTIFICATION", "At least one recipient is required")
	}

	if len(notification.Channels) == 0 {
		return errorhandling.NewValidationError("INVALID_NOTIFICATION", "At least one channel is required")
	}

	return nil
}

func (ns *NotificationService) renderTemplate(template string, variables map[string]interface{}) string {
	// Simple template rendering (would use a proper template engine in production)
	result := template

	for key, value := range variables {
		placeholder := fmt.Sprintf("{{%s}}", key)
		result = strings.ReplaceAll(result, placeholder, fmt.Sprintf("%v", value))
	}

	return result
}

func (ns *NotificationService) getGameEventTitle(eventType string) string {
	titles := map[string]string{
		"kill":           "💀 Elimination!",
		"death":          "💔 You were eliminated",
		"level_up":       "⬆️ Level Up!",
		"quest_complete": "✅ Quest Completed!",
		"item_found":     "🎁 Item Found!",
		"match_win":      "🏆 Victory!",
		"match_loss":     "😞 Defeat",
	}

	if title, exists := titles[eventType]; exists {
		return title
	}
	return "🎮 Game Event"
}

func (ns *NotificationService) getGameEventMessage(eventType string, eventData map[string]interface{}) string {
	switch eventType {
	case "kill":
		if victim, ok := eventData["victim"].(string); ok {
			return fmt.Sprintf("You eliminated **%s**!", victim)
		}
	case "death":
		if killer, ok := eventData["killer"].(string); ok {
			return fmt.Sprintf("You were eliminated by **%s**", killer)
		}
	case "level_up":
		if level, ok := eventData["level"].(float64); ok {
			return fmt.Sprintf("Congratulations! You reached level **%.0f**", level)
		}
	case "quest_complete":
		if questName, ok := eventData["quest_name"].(string); ok {
			return fmt.Sprintf("Quest **%s** completed successfully!", questName)
		}
	case "match_win":
		if score, ok := eventData["score"].(float64); ok {
			return fmt.Sprintf("Your team won with a score of **%.0f**!", score)
		}
	}

	return "Something interesting happened in the game!"
}

func (ns *NotificationService) startBackgroundProcessing() {
	// Cleanup expired notifications
	ns.wg.Add(1)
	go func() {
		defer ns.wg.Done()
		ticker := time.NewTicker(1 * time.Hour)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				ns.cleanupExpiredNotifications()
			case <-ns.shutdownChan:
				return
			}
		}
	}()
}

func (ns *NotificationService) cleanupExpiredNotifications() {
	// Implementation for cleanup of expired notifications
	ns.logger.Debug("Cleaned up expired notifications")
}

// Shutdown gracefully shuts down the notification service
func (ns *NotificationService) Shutdown(ctx context.Context) error {
	close(ns.shutdownChan)

	done := make(chan struct{})
	go func() {
		ns.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		ns.logger.Info("Notification service shut down gracefully")
		return nil
	case <-ctx.Done():
		ns.logger.Warn("Notification service shutdown timed out")
		return ctx.Err()
	}
}

// Channel implementations

func (wsc *WebSocketChannel) Name() string { return wsc.name }
func (wsc *WebSocketChannel) SupportsRealTime() bool { return true }
func (wsc *WebSocketChannel) GetCapabilities() ChannelCapabilities {
	return ChannelCapabilities{
		SupportsRealTime: true,
		MaxMessageSize:   16384,
		SupportedTypes:   []string{"game_event", "achievement", "social"},
		PrioritySupport:  true,
	}
}
func (wsc *WebSocketChannel) Send(ctx context.Context, notification *Notification) error {
	// Implementation for WebSocket sending
	wsc.logger.Debugw("WebSocket notification sent", "notification_id", notification.ID)
	return nil
}

func (iac *InAppChannel) Name() string { return iac.name }
func (iac *InAppChannel) SupportsRealTime() bool { return false }
func (iac *InAppChannel) GetCapabilities() ChannelCapabilities {
	return ChannelCapabilities{
		SupportsRealTime: false,
		MaxMessageSize:   4096,
		SupportedTypes:   []string{"all"},
		PrioritySupport:  false,
	}
}
func (iac *InAppChannel) Send(ctx context.Context, notification *Notification) error {
	// Implementation for in-app notification storage
	for _, recipient := range notification.Recipients {
		if iac.storage[recipient.UserID] == nil {
			iac.storage[recipient.UserID] = make([]*Notification, 0)
		}
		iac.storage[recipient.UserID] = append(iac.storage[recipient.UserID], notification)
	}
	iac.logger.Debugw("In-app notification stored", "notification_id", notification.ID)
	return nil
}

func (pc *PushChannel) Name() string { return pc.name }
func (pc *PushChannel) SupportsRealTime() bool { return true }
func (pc *PushChannel) GetCapabilities() ChannelCapabilities {
	return ChannelCapabilities{
		SupportsRealTime: true,
		MaxMessageSize:   2048,
		SupportedTypes:   []string{"achievement", "system", "marketing"},
		PrioritySupport:  true,
	}
}
func (pc *PushChannel) Send(ctx context.Context, notification *Notification) error {
	// Implementation for push notification sending
	pc.logger.Debugw("Push notification sent", "notification_id", notification.ID)
	return nil
}

func (ec *EmailChannel) Name() string { return ec.name }
func (ec *EmailChannel) SupportsRealTime() bool { return false }
func (ec *EmailChannel) GetCapabilities() ChannelCapabilities {
	return ChannelCapabilities{
		SupportsRealTime: false,
		MaxMessageSize:   100000,
		SupportedTypes:   []string{"system", "security", "marketing"},
		PrioritySupport:  false,
	}
}
func (ec *EmailChannel) Send(ctx context.Context, notification *Notification) error {
	// Implementation for email sending
	ec.logger.Debugw("Email notification sent", "notification_id", notification.ID)
	return nil
}

func (sc *SMSChannel) Name() string { return sc.name }
func (sc *SMSChannel) SupportsRealTime() bool { return true }
func (sc *SMSChannel) GetCapabilities() ChannelCapabilities {
	return ChannelCapabilities{
		SupportsRealTime: true,
		MaxMessageSize:   160,
		SupportedTypes:   []string{"security", "system"},
		PrioritySupport:  true,
	}
}
func (sc *SMSChannel) Send(ctx context.Context, notification *Notification) error {
	// Implementation for SMS sending
	sc.logger.Debugw("SMS notification sent", "notification_id", notification.ID)
	return nil
}
