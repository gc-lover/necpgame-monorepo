// Issue: #141888033
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
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

