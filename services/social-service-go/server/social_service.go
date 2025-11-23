package server

import (
	"context"
	"encoding/json"
	"errors"
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

type NotificationPreferencesRepositoryInterface interface {
	GetByAccountID(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error)
	Update(ctx context.Context, prefs *models.NotificationPreferences) error
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

type FriendRepositoryInterface interface {
	CreateRequest(ctx context.Context, fromCharacterID, toCharacterID uuid.UUID) (*models.Friendship, error)
	GetByID(ctx context.Context, id uuid.UUID) (*models.Friendship, error)
	GetByCharacterID(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error)
	GetPendingRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error)
	GetFriendship(ctx context.Context, characterAID, characterBID uuid.UUID) (*models.Friendship, error)
	AcceptRequest(ctx context.Context, id uuid.UUID) (*models.Friendship, error)
	Block(ctx context.Context, id uuid.UUID) (*models.Friendship, error)
	Delete(ctx context.Context, id uuid.UUID) error
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
	notificationRepo         NotificationRepositoryInterface
	notificationPrefsRepo    NotificationPreferencesRepositoryInterface
	friendRepo               FriendRepositoryInterface
	chatRepo                 ChatRepositoryInterface
	mailRepo                 MailRepositoryInterface
	guildRepo              GuildRepositoryInterface
	moderationRepo              ModerationRepositoryInterface
	moderationService      ModerationServiceInterface
	cache                  *redis.Client
	logger                 *logrus.Logger
	notificationSubscriber *NotificationSubscriber
	eventBus               EventBus
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
	notificationPrefsRepo := NewNotificationPreferencesRepository(dbPool)
	friendRepo := NewFriendRepository(dbPool)
	chatRepo := NewChatRepository(dbPool)
	mailRepo := NewMailRepository(dbPool)
	guildRepo := NewGuildRepository(dbPool)
	moderationRepo := NewModerationRepository(dbPool)
	moderationService := NewModerationService(moderationRepo, redisClient)
	notificationSubscriber := NewNotificationSubscriber(notificationRepo, redisClient)
	notificationSubscriber.SetPreferencesRepository(notificationPrefsRepo)
	eventBus := NewRedisEventBus(redisClient)

	service := &SocialService{
		notificationRepo:      notificationRepo,
		notificationPrefsRepo: notificationPrefsRepo,
		friendRepo:            friendRepo,
		chatRepo:              chatRepo,
		mailRepo:              mailRepo,
		guildRepo:             guildRepo,
		moderationRepo:        moderationRepo,
		moderationService:     moderationService,
		cache:                 redisClient,
		logger:                GetLogger(),
		notificationSubscriber: notificationSubscriber,
		eventBus:              eventBus,
	}

	mailRewardSubscriber := NewMailRewardSubscriber(mailRepo, notificationRepo, redisClient)
	if err := mailRewardSubscriber.Start(); err != nil {
		GetLogger().WithError(err).Warn("Failed to start mail reward subscriber")
	} else {
		GetLogger().Info("Mail reward subscriber started")
	}

	guildProgressionSubscriber := NewGuildProgressionSubscriber(guildRepo, eventBus, redisClient)
	if err := guildProgressionSubscriber.Start(); err != nil {
		GetLogger().WithError(err).Warn("Failed to start guild progression subscriber")
	} else {
		GetLogger().Info("Guild progression subscriber started")
	}

	return service, nil
}

func (s *SocialService) GetNotificationSubscriber() *NotificationSubscriber {
	return s.notificationSubscriber
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
	ban, err := s.moderationService.CheckBan(ctx, message.SenderID, &message.ChannelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to check ban")
		return nil, err
	}
	if ban != nil {
		return nil, errors.New("user is banned from this channel")
	}

	isSpam, err := s.moderationService.DetectSpam(ctx, message.SenderID, message.Content)
	if err != nil {
		s.logger.WithError(err).Error("Failed to detect spam")
		return nil, err
	}
	if isSpam {
		autoBan, err := s.moderationService.AutoBanIfSpam(ctx, message.SenderID, &message.ChannelID)
		if err != nil {
			s.logger.WithError(err).Error("Failed to create auto-ban for spam")
		} else if autoBan != nil {
			s.logger.WithField("character_id", message.SenderID).Warn("Auto-ban created for spam")
		}
		return nil, errors.New("message detected as spam")
	}

	filtered, hasViolation, err := s.moderationService.FilterMessage(ctx, message.Content)
	if err != nil {
		s.logger.WithError(err).Error("Failed to filter message")
		return nil, err
	}
	message.Content = filtered

	if hasViolation {
		s.logger.WithField("sender_id", message.SenderID).Warn("Message filtered for violations")
		
		violationKey := "violations:character:" + message.SenderID.String()
		violationCount, err := s.cache.Incr(ctx, violationKey).Result()
		if err == nil {
			if violationCount == 1 {
				s.cache.Expire(ctx, violationKey, 1*time.Hour)
			}
			
			if violationCount >= 3 {
				autoBan, err := s.moderationService.AutoBanIfSevereViolation(ctx, message.SenderID, &message.ChannelID, int(violationCount))
				if err != nil {
					s.logger.WithError(err).Error("Failed to create auto-ban for severe violations")
				} else if autoBan != nil {
					s.logger.WithField("character_id", message.SenderID).Warn("Auto-ban created for severe violations")
					s.cache.Del(ctx, violationKey)
				}
			}
		}
	}

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

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"mail_id":     mail.ID.String(),
			"sender_id":   nil,
			"recipient_id": mail.RecipientID.String(),
			"type":        string(mail.Type),
			"subject":     mail.Subject,
			"has_attachments": mail.Attachments != nil && len(mail.Attachments) > 0,
			"timestamp":   mail.SentAt.Format(time.RFC3339),
		}
		if mail.SenderID != nil {
			payload["sender_id"] = mail.SenderID.String()
		}
		s.eventBus.PublishEvent(ctx, "mail:sent", payload)

		payload["status"] = string(mail.Status)
		s.eventBus.PublishEvent(ctx, "mail:received", payload)
	}

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

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"mail_id":     mail.ID.String(),
			"recipient_id": mail.RecipientID.String(),
			"attachments": mail.Attachments,
			"timestamp":   time.Now().Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "mail:attachment-claimed", payload)
	}

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

func (s *SocialService) CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error) {
	return s.moderationService.CreateBan(ctx, adminID, req)
}

func (s *SocialService) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error) {
	return s.moderationService.GetBans(ctx, characterID, limit, offset)
}

func (s *SocialService) RemoveBan(ctx context.Context, banID uuid.UUID) error {
	return s.moderationService.RemoveBan(ctx, banID)
}

func (s *SocialService) CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error) {
	return s.moderationService.CreateReport(ctx, reporterID, req)
}

func (s *SocialService) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	return s.moderationService.GetReports(ctx, status, limit, offset)
}

func (s *SocialService) ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error {
	return s.moderationService.ResolveReport(ctx, reportID, adminID, status)
}

func (s *SocialService) GetNotificationPreferences(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	return s.notificationPrefsRepo.GetByAccountID(ctx, accountID)
}

func (s *SocialService) UpdateNotificationPreferences(ctx context.Context, prefs *models.NotificationPreferences) error {
	return s.notificationPrefsRepo.Update(ctx, prefs)
}

func (s *SocialService) SendFriendRequest(ctx context.Context, fromCharacterID uuid.UUID, req *models.SendFriendRequestRequest) (*models.Friendship, error) {
	if fromCharacterID == req.ToCharacterID {
		return nil, errors.New("cannot send friend request to yourself")
	}

	existing, err := s.friendRepo.GetFriendship(ctx, fromCharacterID, req.ToCharacterID)
	if err != nil {
		return nil, err
	}
	if existing != nil {
		if existing.Status == models.FriendshipStatusAccepted {
			return nil, errors.New("already friends")
		}
		if existing.Status == models.FriendshipStatusPending {
			return nil, errors.New("friend request already pending")
		}
		if existing.Status == models.FriendshipStatusBlocked {
			return nil, errors.New("cannot send friend request to blocked user")
		}
	}

	friendship, err := s.friendRepo.CreateRequest(ctx, fromCharacterID, req.ToCharacterID)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"friendship_id":   friendship.ID.String(),
			"from_character_id": fromCharacterID.String(),
			"to_character_id":   req.ToCharacterID.String(),
			"status":          string(friendship.Status),
			"timestamp":       time.Now().Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "friend:request-sent", payload)
	}

	if s.notificationRepo != nil {
		notificationReq := &models.CreateNotificationRequest{
			AccountID: req.ToCharacterID,
			Type:      models.NotificationTypeFriend,
			Priority:  models.NotificationPriorityMedium,
			Title:     "New Friend Request",
			Content:   "You have received a friend request",
			Data: map[string]interface{}{
				"friendship_id":    friendship.ID.String(),
				"from_character_id": fromCharacterID.String(),
			},
			Channels: []models.DeliveryChannel{models.DeliveryChannelInGame, models.DeliveryChannelWebSocket},
		}
		s.notificationRepo.Create(ctx, &models.Notification{
			ID:        uuid.New(),
			AccountID: notificationReq.AccountID,
			Type:      notificationReq.Type,
			Priority:  notificationReq.Priority,
			Title:     notificationReq.Title,
			Content:   notificationReq.Content,
			Data:      notificationReq.Data,
			Status:    models.NotificationStatusUnread,
			Channels:  notificationReq.Channels,
			CreatedAt: time.Now(),
		})
	}

	return friendship, nil
}

func (s *SocialService) AcceptFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) (*models.Friendship, error) {
	friendship, err := s.friendRepo.GetByID(ctx, requestID)
	if err != nil {
		return nil, err
	}
	if friendship == nil {
		return nil, errors.New("friend request not found")
	}

	if friendship.CharacterAID != characterID && friendship.CharacterBID != characterID {
		return nil, errors.New("not authorized to accept this request")
	}

	if friendship.Status != models.FriendshipStatusPending {
		return nil, errors.New("friend request is not pending")
	}

	accepted, err := s.friendRepo.AcceptRequest(ctx, requestID)
	if err != nil {
		return nil, err
	}
	if accepted == nil {
		return nil, errors.New("failed to accept friend request")
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"friendship_id":   accepted.ID.String(),
			"character_a_id":  accepted.CharacterAID.String(),
			"character_b_id":  accepted.CharacterBID.String(),
			"initiator_id":    accepted.InitiatorID.String(),
			"status":          string(accepted.Status),
			"timestamp":       time.Now().Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "friend:request-accepted", payload)
	}

	return accepted, nil
}

func (s *SocialService) RejectFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) error {
	friendship, err := s.friendRepo.GetByID(ctx, requestID)
	if err != nil {
		return err
	}
	if friendship == nil {
		return errors.New("friend request not found")
	}

	if friendship.CharacterAID != characterID && friendship.CharacterBID != characterID {
		return errors.New("not authorized to reject this request")
	}

	return s.friendRepo.Delete(ctx, requestID)
}

func (s *SocialService) RemoveFriend(ctx context.Context, characterID uuid.UUID, friendID uuid.UUID) error {
	friendship, err := s.friendRepo.GetFriendship(ctx, characterID, friendID)
	if err != nil {
		return err
	}
	if friendship == nil {
		return errors.New("friendship not found")
	}

	if friendship.Status != models.FriendshipStatusAccepted {
		return errors.New("not friends")
	}

	return s.friendRepo.Delete(ctx, friendship.ID)
}

func (s *SocialService) BlockFriend(ctx context.Context, characterID uuid.UUID, targetID uuid.UUID) (*models.Friendship, error) {
	if characterID == targetID {
		return nil, errors.New("cannot block yourself")
	}

	friendship, err := s.friendRepo.GetFriendship(ctx, characterID, targetID)
	if err != nil {
		return nil, err
	}

	if friendship == nil {
		friendship, err = s.friendRepo.CreateRequest(ctx, characterID, targetID)
		if err != nil {
			return nil, err
		}
	}

	blocked, err := s.friendRepo.Block(ctx, friendship.ID)
	if err != nil {
		return nil, err
	}
	if blocked == nil {
		return nil, errors.New("failed to block friend")
	}

	return blocked, nil
}

func (s *SocialService) GetFriends(ctx context.Context, characterID uuid.UUID) (*models.FriendListResponse, error) {
	friendships, err := s.friendRepo.GetByCharacterID(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return &models.FriendListResponse{
		Friends: friendships,
		Total:   len(friendships),
	}, nil
}

func (s *SocialService) GetFriendRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	return s.friendRepo.GetPendingRequests(ctx, characterID)
}
