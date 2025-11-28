package server

import (
	"context"
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
	GetExpiringMailsByDays(ctx context.Context, recipientID uuid.UUID, days int, limit, offset int) ([]models.MailMessage, error)
	ExtendExpiration(ctx context.Context, mailID uuid.UUID, days int) error
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
	GetRanks(ctx context.Context, guildID uuid.UUID) ([]models.GuildRankEntity, error)
	GetRankByID(ctx context.Context, rankID uuid.UUID) (*models.GuildRankEntity, error)
	CreateRank(ctx context.Context, rank *models.GuildRankEntity) error
	UpdateRank(ctx context.Context, rank *models.GuildRankEntity) error
	DeleteRank(ctx context.Context, guildID, rankID uuid.UUID) error
	CreateBankTransaction(ctx context.Context, transaction *models.GuildBankTransaction) error
	GetBankTransactions(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]models.GuildBankTransaction, error)
	CountBankTransactions(ctx context.Context, guildID uuid.UUID) (int, error)
}

type OrderRepositoryInterface interface {
	Create(ctx context.Context, order *models.PlayerOrder) error
	GetByID(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
	List(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) ([]models.PlayerOrder, error)
	Count(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus) (int, error)
	UpdateStatus(ctx context.Context, orderID uuid.UUID, status models.OrderStatus) error
	AcceptOrder(ctx context.Context, orderID, executorID uuid.UUID) error
	StartOrder(ctx context.Context, orderID uuid.UUID) error
	CompleteOrder(ctx context.Context, orderID uuid.UUID) error
	CancelOrder(ctx context.Context, orderID uuid.UUID) error
	CreateReview(ctx context.Context, review *models.PlayerOrderReview) error
	GetReviewByOrderID(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrderReview, error)
}

type SocialService struct {
	notificationService    *NotificationService
	chatService            *ChatService
	mailService            *MailService
	friendService          *FriendService
	orderService           *OrderService
	moderationService      ModerationServiceInterface
	notificationSubscriber *NotificationSubscriber
	guildRepo              GuildRepositoryInterface
	eventBus               EventBus
	cache                  *redis.Client
	logger                 *logrus.Logger
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
	orderRepo := NewOrderRepository(dbPool)
	moderationRepo := NewModerationRepository(dbPool)
	moderationService := NewModerationService(moderationRepo, redisClient)
	notificationSubscriber := NewNotificationSubscriber(notificationRepo, redisClient)
	notificationSubscriber.SetPreferencesRepository(notificationPrefsRepo)
	eventBus := NewRedisEventBus(redisClient)

	logger := GetLogger()

	notificationService := NewNotificationService(notificationRepo, notificationPrefsRepo, redisClient, logger)
	chatService := NewChatService(chatRepo, moderationService, redisClient, logger)
	mailService := NewMailService(mailRepo, redisClient, logger, eventBus)
	friendService := NewFriendService(friendRepo, notificationRepo, eventBus)
	orderService := NewOrderService(orderRepo)

	service := &SocialService{
		notificationService:    notificationService,
		chatService:            chatService,
		mailService:            mailService,
		friendService:          friendService,
		orderService:           orderService,
		moderationService:      moderationService,
		notificationSubscriber: notificationSubscriber,
		guildRepo:              guildRepo,
		eventBus:               eventBus,
		cache:                  redisClient,
		logger:                 logger,
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
	return s.notificationService.CreateNotification(ctx, req)
}

func (s *SocialService) GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error) {
	return s.notificationService.GetNotifications(ctx, accountID, limit, offset)
}

func (s *SocialService) GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error) {
	return s.notificationService.GetNotification(ctx, notificationID)
}

func (s *SocialService) UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	return s.notificationService.UpdateNotificationStatus(ctx, notificationID, status)
}

func (s *SocialService) GetNotificationPreferences(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error) {
	return s.notificationService.GetNotificationPreferences(ctx, accountID)
}

func (s *SocialService) UpdateNotificationPreferences(ctx context.Context, prefs *models.NotificationPreferences) error {
	return s.notificationService.UpdateNotificationPreferences(ctx, prefs)
}

func (s *SocialService) CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error) {
	return s.chatService.CreateMessage(ctx, message)
}

func (s *SocialService) GetMessages(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, int, error) {
	return s.chatService.GetMessages(ctx, channelID, limit, offset)
}

func (s *SocialService) GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error) {
	return s.chatService.GetChannels(ctx, channelType)
}

func (s *SocialService) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error) {
	return s.chatService.GetChannel(ctx, channelID)
}

func (s *SocialService) SendMail(ctx context.Context, req *models.CreateMailRequest, senderID *uuid.UUID, senderName string) (*models.MailMessage, error) {
	return s.mailService.SendMail(ctx, req, senderID, senderName)
}

func (s *SocialService) GetMails(ctx context.Context, recipientID uuid.UUID, limit, offset int) (*models.MailListResponse, error) {
	return s.mailService.GetMails(ctx, recipientID, limit, offset)
}

func (s *SocialService) MarkMailAsRead(ctx context.Context, mailID uuid.UUID) error {
	return s.mailService.MarkMailAsRead(ctx, mailID)
}

func (s *SocialService) ClaimAttachment(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
	return s.mailService.ClaimAttachment(ctx, mailID)
}

func (s *SocialService) DeleteMail(ctx context.Context, mailID uuid.UUID) error {
	return s.mailService.DeleteMail(ctx, mailID)
}

func (s *SocialService) GetMail(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error) {
	return s.mailService.GetMail(ctx, mailID)
}

func (s *SocialService) GetUnreadMailCount(ctx context.Context, recipientID uuid.UUID) (*models.UnreadMailCountResponse, error) {
	return s.mailService.GetUnreadMailCount(ctx, recipientID)
}

func (s *SocialService) GetMailAttachments(ctx context.Context, mailID uuid.UUID) (*models.MailAttachmentsResponse, error) {
	return s.mailService.GetMailAttachments(ctx, mailID)
}

func (s *SocialService) PayMailCOD(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
	return s.mailService.PayMailCOD(ctx, mailID)
}

func (s *SocialService) DeclineMailCOD(ctx context.Context, mailID uuid.UUID) error {
	return s.mailService.DeclineMailCOD(ctx, mailID)
}

func (s *SocialService) GetExpiringMails(ctx context.Context, recipientID uuid.UUID, days int, limit, offset int) (*models.MailListResponse, error) {
	return s.mailService.GetExpiringMails(ctx, recipientID, days, limit, offset)
}

func (s *SocialService) ExtendMailExpiration(ctx context.Context, mailID uuid.UUID, days int) (*models.MailMessage, error) {
	return s.mailService.ExtendMailExpiration(ctx, mailID, days)
}

func (s *SocialService) SendSystemMail(ctx context.Context, req *models.SendSystemMailRequest) (*models.MailMessage, error) {
	return s.mailService.SendSystemMail(ctx, req)
}

func (s *SocialService) BroadcastSystemMail(ctx context.Context, req *models.BroadcastSystemMailRequest) (*models.BroadcastResult, error) {
	return s.mailService.BroadcastSystemMail(ctx, req)
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

func (s *SocialService) SendFriendRequest(ctx context.Context, fromCharacterID uuid.UUID, req *models.SendFriendRequestRequest) (*models.Friendship, error) {
	return s.friendService.SendFriendRequest(ctx, fromCharacterID, req)
}

func (s *SocialService) AcceptFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) (*models.Friendship, error) {
	return s.friendService.AcceptFriendRequest(ctx, characterID, requestID)
}

func (s *SocialService) RejectFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) error {
	return s.friendService.RejectFriendRequest(ctx, characterID, requestID)
}

func (s *SocialService) RemoveFriend(ctx context.Context, characterID uuid.UUID, friendID uuid.UUID) error {
	return s.friendService.RemoveFriend(ctx, characterID, friendID)
}

func (s *SocialService) BlockFriend(ctx context.Context, characterID uuid.UUID, targetID uuid.UUID) (*models.Friendship, error) {
	return s.friendService.BlockFriend(ctx, characterID, targetID)
}

func (s *SocialService) GetFriends(ctx context.Context, characterID uuid.UUID) (*models.FriendListResponse, error) {
	return s.friendService.GetFriends(ctx, characterID)
}

func (s *SocialService) GetFriendRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	return s.friendService.GetFriendRequests(ctx, characterID)
}

func (s *SocialService) CreatePlayerOrder(ctx context.Context, customerID uuid.UUID, req *models.CreatePlayerOrderRequest) (*models.PlayerOrder, error) {
	return s.orderService.CreatePlayerOrder(ctx, customerID, req)
}

func (s *SocialService) GetPlayerOrders(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) (*models.PlayerOrdersResponse, error) {
	return s.orderService.GetPlayerOrders(ctx, orderType, status, limit, offset)
}

func (s *SocialService) GetPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	return s.orderService.GetPlayerOrder(ctx, orderID)
}

func (s *SocialService) AcceptPlayerOrder(ctx context.Context, orderID, executorID uuid.UUID) (*models.PlayerOrder, error) {
	return s.orderService.AcceptPlayerOrder(ctx, orderID, executorID)
}

func (s *SocialService) StartPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	return s.orderService.StartPlayerOrder(ctx, orderID)
}

func (s *SocialService) CompletePlayerOrder(ctx context.Context, orderID uuid.UUID, req *models.CompletePlayerOrderRequest) (*models.PlayerOrder, error) {
	return s.orderService.CompletePlayerOrder(ctx, orderID, req)
}

func (s *SocialService) CancelPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	return s.orderService.CancelPlayerOrder(ctx, orderID)
}

func (s *SocialService) ReviewPlayerOrder(ctx context.Context, orderID, reviewerID uuid.UUID, req *models.ReviewPlayerOrderRequest) (*models.PlayerOrderReview, error) {
	return s.orderService.ReviewPlayerOrder(ctx, orderID, reviewerID, req)
}
