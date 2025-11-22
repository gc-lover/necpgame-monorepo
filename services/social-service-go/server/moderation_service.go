package server

import (
	"context"
	"encoding/json"
	"regexp"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type ModerationServiceInterface interface {
	CheckBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error)
	FilterMessage(ctx context.Context, content string) (string, bool, error)
	DetectSpam(ctx context.Context, characterID uuid.UUID, content string) (bool, error)
	AutoBanIfSpam(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error)
	AutoBanIfSevereViolation(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID, violationCount int) (*models.ChatBan, error)
	CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error)
	GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error)
	RemoveBan(ctx context.Context, banID uuid.UUID) error
	CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error)
	GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error)
	ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error
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

type ModerationService struct {
	repo     ModerationRepositoryInterface
	cache    *redis.Client
	logger   *logrus.Logger
	eventBus EventBus
	profanityWords []string
	urlWhitelist   []string
	autoBanEnabled bool
	spamBanDuration int
	severeViolationBanDuration int
}

func NewModerationService(repo ModerationRepositoryInterface, cache *redis.Client) *ModerationService {
	eventBus := NewRedisEventBus(cache)
	return &ModerationService{
		repo:     repo,
		cache:    cache,
		logger:   GetLogger(),
		eventBus: eventBus,
		profanityWords: []string{
			"spam", "hack", "cheat", "bot",
		},
		urlWhitelist: []string{
			"necp.game", "discord.gg",
		},
		autoBanEnabled: true,
		spamBanDuration: 1,
		severeViolationBanDuration: 24,
	}
}

func (s *ModerationService) CheckBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error) {
	return s.repo.GetActiveBan(ctx, characterID, channelID)
}

func (s *ModerationService) FilterMessage(ctx context.Context, content string) (string, bool, error) {
	filtered := content
	hasViolation := false

	contentLower := strings.ToLower(content)

	for _, word := range s.profanityWords {
		if strings.Contains(contentLower, strings.ToLower(word)) {
			re := regexp.MustCompile(`(?i)` + regexp.QuoteMeta(word))
			filtered = re.ReplaceAllString(filtered, strings.Repeat("*", len(word)))
			hasViolation = true
		}
	}

	urlPattern := regexp.MustCompile(`https?://[^\s]+`)
	urls := urlPattern.FindAllString(filtered, -1)
	for _, url := range urls {
		isWhitelisted := false
		for _, whitelist := range s.urlWhitelist {
			if strings.Contains(url, whitelist) {
				isWhitelisted = true
				break
			}
		}
		if !isWhitelisted {
			filtered = urlPattern.ReplaceAllString(filtered, "[LINK REMOVED]")
			hasViolation = true
		}
	}

	repeatingPattern := regexp.MustCompile(`(.)\1{4,}`)
	if repeatingPattern.MatchString(filtered) {
		filtered = repeatingPattern.ReplaceAllStringFunc(filtered, func(match string) string {
			return string(match[0]) + "..."
		})
		hasViolation = true
	}

	upperCasePattern := regexp.MustCompile(`[A-Z]{10,}`)
	if upperCasePattern.MatchString(filtered) {
		filtered = upperCasePattern.ReplaceAllStringFunc(filtered, func(match string) string {
			return strings.ToLower(match)
		})
		hasViolation = true
	}

	return filtered, hasViolation, nil
}

func (s *ModerationService) DetectSpam(ctx context.Context, characterID uuid.UUID, content string) (bool, error) {
	key := "spam:character:" + characterID.String()
	
	count, err := s.cache.Incr(ctx, key).Result()
	if err != nil {
		return false, err
	}

	if count == 1 {
		s.cache.Expire(ctx, key, 1*time.Minute)
	}

	if count > 10 {
		return true, nil
	}

	contentKey := "spam:content:" + characterID.String() + ":" + content
	duplicateCount, err := s.cache.Incr(ctx, contentKey).Result()
	if err != nil {
		return false, err
	}

	if duplicateCount == 1 {
		s.cache.Expire(ctx, contentKey, 5*time.Minute)
	}

	if duplicateCount > 2 {
		return true, nil
	}

	return false, nil
}

func (s *ModerationService) AutoBanIfSpam(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID) (*models.ChatBan, error) {
	if !s.autoBanEnabled {
		return nil, nil
	}

	key := "spam:character:" + characterID.String()
	count, err := s.cache.Get(ctx, key).Int()
	if err != nil || count <= 10 {
		return nil, nil
	}

	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		ChannelID:   channelID,
		Reason:      "Automatic ban: spam detected (>10 messages per minute)",
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	expiresAt := time.Now().Add(time.Duration(s.spamBanDuration) * time.Hour)
	ban.ExpiresAt = &expiresAt

	err = s.repo.CreateBan(ctx, ban)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"ban_id":       ban.ID.String(),
			"character_id": ban.CharacterID.String(),
			"reason":       ban.Reason,
			"type":         "auto_spam",
			"expires_at":   ban.ExpiresAt.Format(time.RFC3339),
			"timestamp":   time.Now().Format(time.RFC3339),
		}
		if ban.ChannelID != nil {
			payload["channel_id"] = ban.ChannelID.String()
		}
		s.eventBus.PublishEvent(ctx, "chat:ban:auto:spam", payload)
	}

	return ban, nil
}

func (s *ModerationService) AutoBanIfSevereViolation(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID, violationCount int) (*models.ChatBan, error) {
	if !s.autoBanEnabled || violationCount < 3 {
		return nil, nil
	}

	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: characterID,
		ChannelID:   channelID,
		Reason:      "Automatic ban: severe violations detected",
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	expiresAt := time.Now().Add(time.Duration(s.severeViolationBanDuration) * time.Hour)
	ban.ExpiresAt = &expiresAt

	err := s.repo.CreateBan(ctx, ban)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"ban_id":          ban.ID.String(),
			"character_id":    ban.CharacterID.String(),
			"reason":          ban.Reason,
			"type":            "auto_severe_violation",
			"violation_count": violationCount,
			"expires_at":      ban.ExpiresAt.Format(time.RFC3339),
			"timestamp":       time.Now().Format(time.RFC3339),
		}
		if ban.ChannelID != nil {
			payload["channel_id"] = ban.ChannelID.String()
		}
		s.eventBus.PublishEvent(ctx, "chat:ban:auto:severe", payload)
	}

	return ban, nil
}

func (s *ModerationService) CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error) {
	ban := &models.ChatBan{
		ID:          uuid.New(),
		CharacterID: req.CharacterID,
		ChannelID:   req.ChannelID,
		ChannelType: req.ChannelType,
		Reason:      req.Reason,
		AdminID:     &adminID,
		CreatedAt:   time.Now(),
		IsActive:    true,
	}

	if req.Duration != nil && *req.Duration > 0 {
		expiresAt := time.Now().Add(time.Duration(*req.Duration) * time.Hour)
		ban.ExpiresAt = &expiresAt
	}

	err := s.repo.CreateBan(ctx, ban)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"ban_id":       ban.ID.String(),
			"character_id": ban.CharacterID.String(),
			"reason":       ban.Reason,
			"expires_at":   nil,
			"timestamp":    time.Now().Format(time.RFC3339),
		}
		if ban.ExpiresAt != nil {
			payload["expires_at"] = ban.ExpiresAt.Format(time.RFC3339)
		}
		if ban.ChannelID != nil {
			payload["channel_id"] = ban.ChannelID.String()
		}
		if ban.AdminID != nil {
			payload["admin_id"] = ban.AdminID.String()
		}
		s.eventBus.PublishEvent(ctx, "chat:ban:created", payload)
	}

	return ban, nil
}

func (s *ModerationService) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error) {
	bans, total, err := s.repo.GetBans(ctx, characterID, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.BanListResponse{
		Bans:  bans,
		Total: total,
	}, nil
}

func (s *ModerationService) RemoveBan(ctx context.Context, banID uuid.UUID) error {
	err := s.repo.DeactivateBan(ctx, banID)
	if err != nil {
		return err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"ban_id":    banID.String(),
			"timestamp": time.Now().Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "chat:ban:removed", payload)
	}

	return nil
}

func (s *ModerationService) CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error) {
	report := &models.ChatReport{
		ID:         uuid.New(),
		ReporterID: reporterID,
		ReportedID: req.ReportedID,
		MessageID:  req.MessageID,
		ChannelID:  req.ChannelID,
		Reason:     req.Reason,
		Status:     "pending",
		CreatedAt:  time.Now(),
	}

	err := s.repo.CreateReport(ctx, report)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"report_id":   report.ID.String(),
			"reporter_id": report.ReporterID.String(),
			"reported_id": report.ReportedID.String(),
			"reason":      report.Reason,
			"timestamp":   time.Now().Format(time.RFC3339),
		}
		if report.MessageID != nil {
			payload["message_id"] = report.MessageID.String()
		}
		if report.ChannelID != nil {
			payload["channel_id"] = report.ChannelID.String()
		}
		s.eventBus.PublishEvent(ctx, "chat:report:created", payload)
	}

	return report, nil
}

func (s *ModerationService) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	return s.repo.GetReports(ctx, status, limit, offset)
}

func (s *ModerationService) ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error {
	return s.repo.UpdateReportStatus(ctx, reportID, status, &adminID)
}

