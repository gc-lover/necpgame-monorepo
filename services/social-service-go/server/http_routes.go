package server

import (
	"github.com/gorilla/mux"
)

func (s *HTTPServer) setupRoutes() {
	api := s.router.PathPrefix("/api/v1").Subrouter()

	if s.authEnabled {
		api.Use(s.authMiddleware)
	}

	social := api.PathPrefix("/social").Subrouter()

	s.setupNotificationRoutes(social)
	s.setupFriendRoutes(social)
	s.setupChatRoutes(social)
	s.setupMailRoutes(social)
	s.setupGuildRoutes(social)
	s.setupOrderRoutes(social)
	s.setupModerationRoutes(social)
	s.setupPartyRoutes(social)
	s.setupRomanceRoutes(social)

	s.router.HandleFunc("/health", s.healthCheck).Methods("GET")
}

func (s *HTTPServer) setupNotificationRoutes(social *mux.Router) {
	social.HandleFunc("/notifications", s.createNotification).Methods("POST")
	social.HandleFunc("/notifications", s.getNotifications).Methods("GET")
	social.HandleFunc("/notifications/{id}", s.getNotification).Methods("GET")
	social.HandleFunc("/notifications/{id}/status", s.updateNotificationStatus).Methods("PUT")
	social.HandleFunc("/notifications/preferences", s.getNotificationPreferences).Methods("GET")
	social.HandleFunc("/notifications/preferences", s.updateNotificationPreferences).Methods("PUT")
}

func (s *HTTPServer) setupFriendRoutes(social *mux.Router) {
	social.HandleFunc("/friends", s.getFriends).Methods("GET")
	social.HandleFunc("/friends/requests", s.getFriendRequests).Methods("GET")
	social.HandleFunc("/friends/request", s.sendFriendRequest).Methods("POST")
	social.HandleFunc("/friends/requests/{id}/accept", s.acceptFriendRequest).Methods("POST")
	social.HandleFunc("/friends/requests/{id}/reject", s.rejectFriendRequest).Methods("POST")
	social.HandleFunc("/friends/{id}", s.removeFriend).Methods("DELETE")
	social.HandleFunc("/friends/{id}/block", s.blockFriend).Methods("POST")
}

func (s *HTTPServer) setupChatRoutes(social *mux.Router) {
	social.HandleFunc("/chat/channels", s.getChannels).Methods("GET")
	social.HandleFunc("/chat/channels/{id}", s.getChannel).Methods("GET")
	social.HandleFunc("/chat/messages", s.createMessage).Methods("POST")
	social.HandleFunc("/chat/messages/{channelId}", s.getMessages).Methods("GET")
	social.HandleFunc("/chat/report", s.createReport).Methods("POST")
	social.HandleFunc("/chat/ban", s.createBan).Methods("POST")
	social.HandleFunc("/chat/bans", s.getBans).Methods("GET")
	social.HandleFunc("/chat/bans/{id}", s.removeBan).Methods("DELETE")
	social.HandleFunc("/chat/reports", s.getReports).Methods("GET")
	social.HandleFunc("/chat/reports/{id}/resolve", s.resolveReport).Methods("POST")

	chatCommandService := NewChatCommandService()
	chatCommandHandlers := NewChatCommandHandlers(chatCommandService)
	social.HandleFunc("/chat/commands/execute", chatCommandHandlers.executeChatCommand).Methods("POST")
}

func (s *HTTPServer) setupMailRoutes(social *mux.Router) {
	social.HandleFunc("/mail/send", s.sendMail).Methods("POST")
	social.HandleFunc("/mail/inbox", s.getMails).Methods("GET")
	social.HandleFunc("/mail/{mail_id}", s.getMail).Methods("GET")
	social.HandleFunc("/mail/{mail_id}/read", s.markMailAsRead).Methods("PUT")
	social.HandleFunc("/mail/{mail_id}/attachments/claim", s.claimAttachment).Methods("POST")
	social.HandleFunc("/mail/{mail_id}", s.deleteMail).Methods("DELETE")
	social.HandleFunc("/mail/unread-count", s.getUnreadMailCount).Methods("GET")
	social.HandleFunc("/mail/{mail_id}/attachments", s.getMailAttachments).Methods("GET")
	social.HandleFunc("/mail/{mail_id}/cod/pay", s.payMailCOD).Methods("POST")
	social.HandleFunc("/mail/{mail_id}/cod/decline", s.declineMailCOD).Methods("POST")
	social.HandleFunc("/mail/expiring", s.getExpiringMails).Methods("GET")
	social.HandleFunc("/mail/{mail_id}/extend", s.extendMailExpiration).Methods("POST")
	social.HandleFunc("/mail/system/send", s.sendSystemMail).Methods("POST")
	social.HandleFunc("/mail/system/broadcast", s.broadcastSystemMail).Methods("POST")
}

func (s *HTTPServer) setupGuildRoutes(social *mux.Router) {
	social.HandleFunc("/guilds", s.createGuild).Methods("POST")
	social.HandleFunc("/guilds", s.listGuilds).Methods("GET")
	social.HandleFunc("/guilds/invitations", s.getInvitations).Methods("GET")
	social.HandleFunc("/guilds/invitations/{id}/accept", s.acceptInvitation).Methods("POST")
	social.HandleFunc("/guilds/invitations/{id}/reject", s.rejectInvitation).Methods("POST")
	social.HandleFunc("/guilds/{id}/disband", s.disbandGuild).Methods("DELETE")
	social.HandleFunc("/guilds/{id}/members", s.getGuildMembers).Methods("GET")
	social.HandleFunc("/guilds/{id}/members/invite", s.inviteMember).Methods("POST")
	social.HandleFunc("/guilds/{id}/members/{characterId}/rank", s.updateMemberRank).Methods("PUT")
	social.HandleFunc("/guilds/{id}/members/{characterId}/kick", s.kickMember).Methods("DELETE")
	social.HandleFunc("/guilds/{id}/members/{characterId}/leave", s.leaveGuild).Methods("POST")
	social.HandleFunc("/guilds/{guild_id}/bank", s.getGuildBank).Methods("GET")
	social.HandleFunc("/guilds/{guild_id}/bank/deposit", s.depositToGuildBank).Methods("POST")
	social.HandleFunc("/guilds/{guild_id}/bank/withdraw", s.withdrawFromGuildBank).Methods("POST")
	social.HandleFunc("/guilds/{guild_id}/bank/transactions", s.getGuildBankTransactions).Methods("GET")
	social.HandleFunc("/guilds/{guild_id}/ranks", s.getGuildRanks).Methods("GET")
	social.HandleFunc("/guilds/{guild_id}/ranks", s.createGuildRank).Methods("POST")
	social.HandleFunc("/guilds/{guild_id}/ranks/{rank_id}", s.updateGuildRank).Methods("PUT")
	social.HandleFunc("/guilds/{guild_id}/ranks/{rank_id}", s.deleteGuildRank).Methods("DELETE")
	social.HandleFunc("/guilds/{id}", s.getGuild).Methods("GET")
	social.HandleFunc("/guilds/{id}", s.updateGuild).Methods("PUT")
}

func (s *HTTPServer) setupOrderRoutes(social *mux.Router) {
	social.HandleFunc("/orders/create", s.createPlayerOrder).Methods("POST")
	social.HandleFunc("/orders", s.getPlayerOrders).Methods("GET")
	social.HandleFunc("/orders/{orderId}", s.getPlayerOrder).Methods("GET")
	social.HandleFunc("/orders/{orderId}/accept", s.acceptPlayerOrder).Methods("POST")
	social.HandleFunc("/orders/{orderId}/start", s.startPlayerOrder).Methods("POST")
	social.HandleFunc("/orders/{orderId}/complete", s.completePlayerOrder).Methods("POST")
	social.HandleFunc("/orders/{orderId}/cancel", s.cancelPlayerOrder).Methods("POST")
	social.HandleFunc("/orders/{orderId}/review", s.reviewPlayerOrder).Methods("POST")
}

func (s *HTTPServer) setupModerationRoutes(social *mux.Router) {
}

func (s *HTTPServer) setupPartyRoutes(social *mux.Router) {
	partyHandlers := NewPartyHandlers(s.partyService)
	social.HandleFunc("/party", partyHandlers.createParty).Methods("POST")
	social.HandleFunc("/party", partyHandlers.getParty).Methods("GET")
	social.HandleFunc("/party/{partyId}", partyHandlers.getPartyById).Methods("GET")
	social.HandleFunc("/party/{partyId}/transfer-leadership", partyHandlers.transferLeadership).Methods("POST")
	social.HandleFunc("/party/{partyId}/leader", partyHandlers.getPartyLeader).Methods("GET")
	social.HandleFunc("/party/player/{accountId}", partyHandlers.getPlayerParty).Methods("GET")
}

func (s *HTTPServer) setupRomanceRoutes(social *mux.Router) {
	if s.engramRomanceService != nil {
		romanceHandlers := NewEngramRomanceHandlers(s.engramRomanceService)
		social.HandleFunc("/romance/engrams/{engram_id}/comment", romanceHandlers.EngramRomanceComment).Methods("POST")
		social.HandleFunc("/romance/engrams/{engram_id}/influence", romanceHandlers.GetEngramRomanceInfluence).Methods("GET")
	}
}

