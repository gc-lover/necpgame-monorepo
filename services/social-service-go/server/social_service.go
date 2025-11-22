package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type NotificationRepositoryInterface interface {
	Create(ctx context.Context, notification *models.Notification) (*models.Notification, error)
	GetByID(ctx context.Context, id uuid.UUID) (*models.Notification, error)
	GetByAccountID(ctx context.Context, accountID uuid.UUID, limit, offset int) ([]models.Notification, error)
	CountByAccountID(ctx context.Context, accountID uuid.UUID) (int, error)
	CountUnreadByAccountID(ctx context.Context, accountID uuid.UUID) (int, error)
	UpdateStatus(ctx context.Context, id uuid.UUID, status models.NotificationStatus) (*models.Notification, error)
}

type ChatRepositoryInterface interface {
	CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error)
	GetMessagesByChannel(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, error)
	GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error)
	GetChannelByID(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error)
	CountMessagesByChannel(ctx context.Context, channelID uuid.UUID) (int, error)
}

type MailRepositoryInterface interface {
	Create(ctx context.Context, mail *models.MailMessage) error
	GetByID(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error)
	GetByRecipientID(ctx context.Context, recipientID uuid.UUID, limit, offset int) ([]models.MailMessage, error)
	UpdateStatus(ctx context.Context, mailID uuid.UUID, status models.MailStatus, readAt *time.Time) error
	MarkAsClaimed(ctx context.Context, mailID uuid.UUID) error
	Delete(ctx context.Context, mailID uuid.UUID) error
	CountByRecipientID(ctx context.Context, recipientID uuid.UUID) (int, error)
	CountUnreadByRecipientID(ctx context.Context, recipientID uuid.UUID) (int, error)
}

type GuildRepositoryInterface interface {
	Create(ctx context.Context, guild *models.Guild) error
	GetByID(ctx context.Context, id uuid.UUID) (*models.Guild, error)
	GetByName(ctx context.Context, name string) (*models.Guild, error)
	GetByTag(ctx context.Context, tag string) (*models.Guild, error)
	List(ctx context.Context, limit, offset int) ([]models.Guild, error)
	Count(ctx context.Context) (int, error)
	Update(ctx context.Context, guild *models.Guild) error
	UpdateLevel(ctx context.Context, guildID uuid.UUID, level, experience int) error
	Disband(ctx context.Context, guildID uuid.UUID) error
	AddMember(ctx context.Context, member *models.GuildMember) error
	GetMember(ctx context.Context, guildID, characterID uuid.UUID) (*models.GuildMember, error)
	GetMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]models.GuildMember, error)
	CountMembers(ctx context.Context, guildID uuid.UUID) (int, error)
	UpdateMemberRank(ctx context.Context, guildID, characterID uuid.UUID, rank models.GuildRank) error
	RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error
	KickMember(ctx context.Context, guildID, characterID uuid.UUID) error
	UpdateMemberContribution(ctx context.Context, guildID, characterID uuid.UUID, contribution int) error
	CreateInvitation(ctx context.Context, invitation *models.GuildInvitation) error
	GetInvitation(ctx context.Context, id uuid.UUID) (*models.GuildInvitation, error)
	GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error)
	AcceptInvitation(ctx context.Context, invitationID uuid.UUID) error
	RejectInvitation(ctx context.Context, invitationID uuid.UUID) error
	GetBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error)
	CreateBank(ctx context.Context, bank *models.GuildBank) error
	UpdateBank(ctx context.Context, bank *models.GuildBank) error
}

type SocialService struct {
	notificationRepo NotificationRepositoryInterface
	chatRepo         ChatRepositoryInterface
	mailRepo         MailRepositoryInterface
	guildRepo        GuildRepositoryInterface
	cache            *redis.Client
	logger           *logrus.Logger
}

func NewSocialService(dbURL, redisURL string) (*SocialService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	notificationRepo := NewNotificationRepository(dbPool)
	chatRepo := NewChatRepository(dbPool)
	mailRepo := NewMailRepository(dbPool)
	guildRepo := NewGuildRepository(dbPool)

	return &SocialService{
		notificationRepo: notificationRepo,
		chatRepo:         chatRepo,
		mailRepo:         mailRepo,
		guildRepo:        guildRepo,
		cache:            redisClient,
		logger:           GetLogger(),
	}, nil
}

func (s *SocialService) CreateNotification(ctx context.Context, req *models.CreateNotificationRequest) (*models.Notification, error) {
	notification := &models.Notification{
		ID:        uuid.New(),
		AccountID: req.AccountID,
		Type:      req.Type,
		Priority:  req.Priority,
		Title:     req.Title,
		Content:   req.Content,
		Data:      req.Data,
		Status:    models.NotificationStatusUnread,
		Channels:  req.Channels,
		CreatedAt: time.Now(),
		ExpiresAt: req.ExpiresAt,
	}

	if len(notification.Channels) == 0 {
		notification.Channels = []models.DeliveryChannel{models.DeliveryChannelInGame}
	}

	notification, err := s.notificationRepo.Create(ctx, notification)
	if err != nil {
		return nil, err
	}

	cacheKey := "notifications:account:" + req.AccountID.String()
	s.cache.Del(ctx, cacheKey)

	return notification, nil
}

func (s *SocialService) GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error) {
	cacheKey := "notifications:account:" + accountID.String() + ":limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)
	
	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.NotificationListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	notifications, err := s.notificationRepo.GetByAccountID(ctx, accountID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.notificationRepo.CountByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	unread, err := s.notificationRepo.CountUnreadByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	response := &models.NotificationListResponse{
		Notifications: notifications,
		Total:         total,
		Unread:        unread,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 1*time.Minute)

	return response, nil
}

func (s *SocialService) GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error) {
	return s.notificationRepo.GetByID(ctx, notificationID)
}

func (s *SocialService) UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	notification, err := s.notificationRepo.UpdateStatus(ctx, notificationID, status)
	if err != nil {
		return nil, err
	}

	if notification != nil {
		cacheKey := "notifications:account:" + notification.AccountID.String()
		s.cache.Del(ctx, cacheKey)
	}

	return notification, nil
}

func (s *SocialService) CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error) {
	return s.chatRepo.CreateMessage(ctx, message)
}

func (s *SocialService) GetMessages(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, int, error) {
	messages, err := s.chatRepo.GetMessagesByChannel(ctx, channelID, limit, offset)
	if err != nil {
		return nil, 0, err
	}

	total, err := s.chatRepo.CountMessagesByChannel(ctx, channelID)
	if err != nil {
		return nil, 0, err
	}

	return messages, total, nil
}

func (s *SocialService) GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error) {
	return s.chatRepo.GetChannels(ctx, channelType)
}

func (s *SocialService) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error) {
	return s.chatRepo.GetChannelByID(ctx, channelID)
}

func (s *SocialService) SendMail(ctx context.Context, req *models.CreateMailRequest, senderID *uuid.UUID, senderName string) (*models.MailMessage, error) {
	now := time.Now()
	expiresAt := (*time.Time)(nil)
	if req.ExpiresIn != nil && *req.ExpiresIn > 0 {
		exp := now.Add(time.Duration(*req.ExpiresIn) * 24 * time.Hour)
		expiresAt = &exp
	}

	mail := &models.MailMessage{
		ID:          uuid.New(),
		SenderID:    senderID,
		SenderName:  senderName,
		RecipientID: req.RecipientID,
		Type:        models.MailTypePlayer,
		Subject:     req.Subject,
		Content:     req.Content,
		Attachments: req.Attachments,
		CODAmount:   req.CODAmount,
		Status:      models.MailStatusUnread,
		IsRead:      false,
		IsClaimed:    false,
		SentAt:      now,
		CreatedAt:   now,
		UpdatedAt:   now,
		ExpiresAt:   expiresAt,
	}

	if senderID == nil {
		mail.Type = models.MailTypeSystem
	}

	err := s.mailRepo.Create(ctx, mail)
	if err != nil {
		return nil, err
	}

	s.invalidateMailCache(ctx, req.RecipientID)

	return mail, nil
}

func (s *SocialService) GetMails(ctx context.Context, recipientID uuid.UUID, limit, offset int) (*models.MailListResponse, error) {
	cacheKey := "mails:recipient:" + recipientID.String() + ":limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.MailListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		}
	}

	messages, err := s.mailRepo.GetByRecipientID(ctx, recipientID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.mailRepo.CountByRecipientID(ctx, recipientID)
	if err != nil {
		return nil, err
	}

	unread, err := s.mailRepo.CountUnreadByRecipientID(ctx, recipientID)
	if err != nil {
		return nil, err
	}

	response := &models.MailListResponse{
		Messages: messages,
		Total:    total,
		Unread:   unread,
	}

	responseJSON, _ := json.Marshal(response)
	s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)

	return response, nil
}

func (s *SocialService) MarkMailAsRead(ctx context.Context, mailID uuid.UUID) error {
	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return err
	}
	if mail == nil {
		return nil
	}

	now := time.Now()
	err = s.mailRepo.UpdateStatus(ctx, mailID, models.MailStatusRead, &now)
	if err != nil {
		return err
	}

	s.invalidateMailCache(ctx, mail.RecipientID)
	return nil
}

func (s *SocialService) ClaimAttachment(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return nil, err
	}
	if mail == nil {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}

	if mail.IsClaimed {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}

	if mail.Attachments == nil || len(mail.Attachments) == 0 {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}

	err = s.mailRepo.MarkAsClaimed(ctx, mailID)
	if err != nil {
		return nil, err
	}

	s.invalidateMailCache(ctx, mail.RecipientID)

	items := make(map[string]interface{})
	currency := make(map[string]int)

	if attachments, ok := mail.Attachments["items"].([]interface{}); ok {
		items["items"] = attachments
	}
	if attachments, ok := mail.Attachments["currency"].(map[string]interface{}); ok {
		for k, v := range attachments {
			if val, ok := v.(float64); ok {
				currency[k] = int(val)
			}
		}
	}

	return &models.ClaimAttachmentResponse{
		Success:  true,
		Items:    items,
		Currency: currency,
	}, nil
}

func (s *SocialService) DeleteMail(ctx context.Context, mailID uuid.UUID) error {
	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return err
	}
	if mail == nil {
		return nil
	}

	err = s.mailRepo.Delete(ctx, mailID)
	if err != nil {
		return err
	}

	s.invalidateMailCache(ctx, mail.RecipientID)
	return nil
}

func (s *SocialService) GetMail(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error) {
	return s.mailRepo.GetByID(ctx, mailID)
}

func (s *SocialService) invalidateMailCache(ctx context.Context, recipientID uuid.UUID) {
	pattern := "mails:recipient:" + recipientID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}
