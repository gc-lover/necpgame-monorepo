package server

import (
	"context"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type SocialServiceInterface interface {
	CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error)
	GetMessages(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, int, error)
	GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error)
	GetChannel(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error)
	CreateNotification(ctx context.Context, req *models.CreateNotificationRequest) (*models.Notification, error)
	GetNotifications(ctx context.Context, accountID uuid.UUID, limit, offset int) (*models.NotificationListResponse, error)
	GetNotification(ctx context.Context, notificationID uuid.UUID) (*models.Notification, error)
	UpdateNotificationStatus(ctx context.Context, notificationID uuid.UUID, status models.NotificationStatus) (*models.Notification, error)
	SendMail(ctx context.Context, req *models.CreateMailRequest, senderID *uuid.UUID, senderName string) (*models.MailMessage, error)
	GetMails(ctx context.Context, recipientID uuid.UUID, limit, offset int) (*models.MailListResponse, error)
	GetMail(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error)
	MarkMailAsRead(ctx context.Context, mailID uuid.UUID) error
	ClaimAttachment(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error)
	DeleteMail(ctx context.Context, mailID uuid.UUID) error
	GetUnreadMailCount(ctx context.Context, recipientID uuid.UUID) (*models.UnreadMailCountResponse, error)
	GetMailAttachments(ctx context.Context, mailID uuid.UUID) (*models.MailAttachmentsResponse, error)
	PayMailCOD(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error)
	DeclineMailCOD(ctx context.Context, mailID uuid.UUID) error
	GetExpiringMails(ctx context.Context, recipientID uuid.UUID, days int, limit, offset int) (*models.MailListResponse, error)
	ExtendMailExpiration(ctx context.Context, mailID uuid.UUID, days int) (*models.MailMessage, error)
	SendSystemMail(ctx context.Context, req *models.SendSystemMailRequest) (*models.MailMessage, error)
	BroadcastSystemMail(ctx context.Context, req *models.BroadcastSystemMailRequest) (*models.BroadcastResult, error)
	CreateGuild(ctx context.Context, leaderID uuid.UUID, req *models.CreateGuildRequest) (*models.Guild, error)
	ListGuilds(ctx context.Context, limit, offset int) (*models.GuildListResponse, error)
	GetGuild(ctx context.Context, guildID uuid.UUID) (*models.GuildDetailResponse, error)
	UpdateGuild(ctx context.Context, guildID, leaderID uuid.UUID, req *models.UpdateGuildRequest) (*models.Guild, error)
	DisbandGuild(ctx context.Context, guildID, leaderID uuid.UUID) error
	GetGuildMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) (*models.GuildMemberListResponse, error)
	InviteMember(ctx context.Context, guildID, inviterID uuid.UUID, req *models.InviteMemberRequest) (*models.GuildInvitation, error)
	UpdateMemberRank(ctx context.Context, guildID, leaderID, characterID uuid.UUID, rank models.GuildRank) error
	KickMember(ctx context.Context, guildID, leaderID, characterID uuid.UUID) error
	RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error
	GetGuildBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error)
	DepositToGuildBank(ctx context.Context, guildID, accountID uuid.UUID, req *models.GuildBankDepositRequest) (*models.GuildBankTransaction, error)
	WithdrawFromGuildBank(ctx context.Context, guildID, accountID uuid.UUID, req *models.GuildBankWithdrawRequest) (*models.GuildBankTransaction, error)
	GetGuildBankTransactions(ctx context.Context, guildID uuid.UUID, limit, offset int) (*models.GuildBankTransactionsResponse, error)
	GetGuildRanks(ctx context.Context, guildID uuid.UUID) (*models.GuildRanksResponse, error)
	CreateGuildRank(ctx context.Context, guildID, leaderID uuid.UUID, req *models.CreateGuildRankRequest) (*models.GuildRankEntity, error)
	UpdateGuildRank(ctx context.Context, guildID, rankID, leaderID uuid.UUID, req *models.UpdateGuildRankRequest) (*models.GuildRankEntity, error)
	DeleteGuildRank(ctx context.Context, guildID, rankID, leaderID uuid.UUID) error
	GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error)
	AcceptInvitation(ctx context.Context, invitationID, characterID uuid.UUID) error
	RejectInvitation(ctx context.Context, invitationID uuid.UUID) error
	CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error)
	GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error)
	RemoveBan(ctx context.Context, banID uuid.UUID) error
	CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error)
	GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error)
	ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error
	GetNotificationPreferences(ctx context.Context, accountID uuid.UUID) (*models.NotificationPreferences, error)
	UpdateNotificationPreferences(ctx context.Context, prefs *models.NotificationPreferences) error
	SendFriendRequest(ctx context.Context, fromCharacterID uuid.UUID, req *models.SendFriendRequestRequest) (*models.Friendship, error)
	AcceptFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) (*models.Friendship, error)
	RejectFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) error
	RemoveFriend(ctx context.Context, characterID uuid.UUID, friendID uuid.UUID) error
	BlockFriend(ctx context.Context, characterID uuid.UUID, targetID uuid.UUID) (*models.Friendship, error)
	GetFriends(ctx context.Context, characterID uuid.UUID) (*models.FriendListResponse, error)
	GetFriendRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error)
	CreatePlayerOrder(ctx context.Context, customerID uuid.UUID, req *models.CreatePlayerOrderRequest) (*models.PlayerOrder, error)
	GetPlayerOrders(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) (*models.PlayerOrdersResponse, error)
	GetPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
	AcceptPlayerOrder(ctx context.Context, orderID, executorID uuid.UUID) (*models.PlayerOrder, error)
	StartPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
	CompletePlayerOrder(ctx context.Context, orderID uuid.UUID, req *models.CompletePlayerOrderRequest) (*models.PlayerOrder, error)
	CancelPlayerOrder(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error)
	ReviewPlayerOrder(ctx context.Context, orderID, reviewerID uuid.UUID, req *models.ReviewPlayerOrderRequest) (*models.PlayerOrderReview, error)
}

type HTTPServer struct {
	addr              string
	router            *mux.Router
	socialService     SocialServiceInterface
	partyService      PartyServiceInterface
	engramRomanceService EngramRomanceServiceInterface
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, socialService SocialServiceInterface, partyService PartyServiceInterface, jwtValidator *JwtValidator, authEnabled bool, engramRomanceService EngramRomanceServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:                addr,
		router:              router,
		socialService:       socialService,
		partyService:        partyService,
		engramRomanceService: engramRomanceService,
		logger:              GetLogger(),
		jwtValidator:        jwtValidator,
		authEnabled:         authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	server.setupRoutes()

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}
