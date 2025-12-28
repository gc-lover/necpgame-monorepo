package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/social-domain-service-go/internal/config"
	"services/social-domain-service-go/internal/service"
)

// Handler handles HTTP requests for the Social Domain
type Handler struct {
	service *service.Service
	logger  *zap.Logger
	config  *config.Config
}

// NewHandler creates a new handler instance with MMOFPS optimizations
func NewHandler(svc *service.Service, logger *zap.Logger, config *config.Config) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
		config:  config,
	}
}

// HealthCheck handles health check requests
func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	if err := h.service.HealthCheck(ctx); err != nil {
		h.respondError(w, http.StatusServiceUnavailable, "Service unhealthy")
		return
	}

	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "social-domain",
		"timestamp": r.Context().Value("timestamp"),
		"version":   "1.0.0",
	}

	h.respondJSON(w, http.StatusOK, response)
}

// AuthMiddleware validates JWT tokens
func (h *Handler) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// TODO: Implement JWT validation
		// For now, just pass through
		next.ServeHTTP(w, r)
	})
}

// Chat handlers

// GetChatChannels gets available chat channels
func (h *Handler) GetChatChannels(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement channel listing with filtering
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// CreateChatChannel creates a new chat channel
func (h *Handler) CreateChatChannel(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Name        string `json:"name"`
		ChannelType string `json:"channel_type"`
		IsPrivate   bool   `json:"is_private"`
		MaxMembers  *int   `json:"max_members,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	channel, err := h.service.CreateChatChannel(ctx, userID, req.Name, req.ChannelType, req.IsPrivate, req.MaxMembers)
	if err != nil {
		h.logger.Error("Failed to create chat channel", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create chat channel")
		return
	}

	h.respondJSON(w, http.StatusCreated, channel)
}

// GetChannelMessages gets messages from a channel
func (h *Handler) GetChannelMessages(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channelID")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel ID")
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 50
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 200 {
			limit = parsed
		}
	}

	ctx := r.Context()
	messages, err := h.service.GetChannelMessages(ctx, channelID, limit)
	if err != nil {
		h.logger.Error("Failed to get channel messages", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get messages")
		return
	}

	h.respondJSON(w, http.StatusOK, messages)
}

// SendMessage sends a message to a channel
func (h *Handler) SendMessage(w http.ResponseWriter, r *http.Request) {
	channelIDStr := chi.URLParam(r, "channelID")
	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid channel ID")
		return
	}

	var req struct {
		MessageType string `json:"message_type"`
		Content     string `json:"content"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	message, err := h.service.SendChatMessage(ctx, channelID, userID, req.MessageType, req.Content)
	if err != nil {
		h.logger.Error("Failed to send message", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to send message")
		return
	}

	h.respondJSON(w, http.StatusCreated, message)
}

// Guild handlers

// GetGuilds gets available guilds
func (h *Handler) GetGuilds(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	guilds, err := h.service.GetGuilds(ctx)
	if err != nil {
		h.logger.Error("Failed to get guilds", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get guilds")
		return
	}

	h.respondJSON(w, http.StatusOK, guilds)
}

// CreateGuild creates a new guild
func (h *Handler) CreateGuild(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Name        string `json:"name"`
		Description string `json:"description"`
		MaxMembers  int    `json:"max_members"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	guild, err := h.service.CreateGuild(ctx, userID, req.Name, req.Description, req.MaxMembers)
	if err != nil {
		h.logger.Error("Failed to create guild", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create guild")
		return
	}

	h.respondJSON(w, http.StatusCreated, guild)
}

// GetGuild gets a specific guild
func (h *Handler) GetGuild(w http.ResponseWriter, r *http.Request) {
	guildIDStr := chi.URLParam(r, "guildID")
	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid guild ID")
		return
	}

	ctx := r.Context()
	guild, err := h.service.GetGuild(ctx, guildID)
	if err != nil {
		h.logger.Error("Failed to get guild", zap.Error(err))
		h.respondError(w, http.StatusNotFound, "Guild not found")
		return
	}

	h.respondJSON(w, http.StatusOK, guild)
}

// UpdateGuild updates guild information
func (h *Handler) UpdateGuild(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement guild update logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// JoinGuild allows a player to join a guild
func (h *Handler) JoinGuild(w http.ResponseWriter, r *http.Request) {
	guildIDStr := chi.URLParam(r, "guildID")
	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid guild ID")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	err = h.service.JoinGuild(ctx, guildID, userID)
	if err != nil {
		h.logger.Error("Failed to join guild", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to join guild")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "joined"})
}

// LeaveGuild allows a player to leave a guild
func (h *Handler) LeaveGuild(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement leave guild logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// Party handlers

// GetParties gets available parties
func (h *Handler) GetParties(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	parties, err := h.service.GetParties(ctx)
	if err != nil {
		h.logger.Error("Failed to get parties", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get parties")
		return
	}

	h.respondJSON(w, http.StatusOK, parties)
}

// CreateParty creates a new party
func (h *Handler) CreateParty(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Name       string `json:"name"`
		MaxMembers int    `json:"max_members"`
		IsPrivate  bool   `json:"is_private"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	party, err := h.service.CreateParty(ctx, userID, req.Name, req.MaxMembers, req.IsPrivate)
	if err != nil {
		h.logger.Error("Failed to create party", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create party")
		return
	}

	h.respondJSON(w, http.StatusCreated, party)
}

// GetParty gets a specific party
func (h *Handler) GetParty(w http.ResponseWriter, r *http.Request) {
	partyIDStr := chi.URLParam(r, "partyID")
	partyID, err := uuid.Parse(partyIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid party ID")
		return
	}

	ctx := r.Context()
	party, err := h.service.GetParty(ctx, partyID)
	if err != nil {
		h.logger.Error("Failed to get party", zap.Error(err))
		h.respondError(w, http.StatusNotFound, "Party not found")
		return
	}

	h.respondJSON(w, http.StatusOK, party)
}

// JoinParty allows a player to join a party
func (h *Handler) JoinParty(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement join party logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// LeaveParty allows a player to leave a party
func (h *Handler) LeaveParty(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement leave party logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// Relationships handlers

// GetRelationships gets all relationships for a player
func (h *Handler) GetRelationships(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	relationships, err := h.service.GetRelationships(ctx, userID)
	if err != nil {
		h.logger.Error("Failed to get relationships", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get relationships")
		return
	}

	h.respondJSON(w, http.StatusOK, relationships)
}

// CreateRelationship creates a new relationship between players
func (h *Handler) CreateRelationship(w http.ResponseWriter, r *http.Request) {
	var req struct {
		TargetUserID string `json:"target_user_id"`
		RelationshipType string `json:"relationship_type"`
		Message string `json:"message,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	targetUserID, err := uuid.Parse(req.TargetUserID)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid target user ID")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	relationship, err := h.service.CreateRelationship(ctx, userID, targetUserID, req.RelationshipType, req.Message)
	if err != nil {
		h.logger.Error("Failed to create relationship", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create relationship")
		return
	}

	h.respondJSON(w, http.StatusCreated, relationship)
}

// GetRelationship gets a specific relationship by ID
func (h *Handler) GetRelationship(w http.ResponseWriter, r *http.Request) {
	relationshipIDStr := chi.URLParam(r, "relationshipID")
	relationshipID, err := uuid.Parse(relationshipIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid relationship ID")
		return
	}

	ctx := r.Context()
	relationship, err := h.service.GetRelationship(ctx, relationshipID)
	if err != nil {
		h.logger.Error("Failed to get relationship", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get relationship")
		return
	}

	h.respondJSON(w, http.StatusOK, relationship)
}

// UpdateRelationship updates an existing relationship
func (h *Handler) UpdateRelationship(w http.ResponseWriter, r *http.Request) {
	relationshipIDStr := chi.URLParam(r, "relationshipID")
	relationshipID, err := uuid.Parse(relationshipIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid relationship ID")
		return
	}

	var req struct {
		Status string `json:"status,omitempty"`
		Message string `json:"message,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	ctx := r.Context()
	err = h.service.UpdateRelationship(ctx, relationshipID, req.Status, req.Message)
	if err != nil {
		h.logger.Error("Failed to update relationship", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to update relationship")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "updated"})
}

// Orders handlers

// GetOrders gets available player orders
func (h *Handler) GetOrders(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	orders, err := h.service.GetOrders(ctx)
	if err != nil {
		h.logger.Error("Failed to get orders", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get orders")
		return
	}

	h.respondJSON(w, http.StatusOK, orders)
}

// CreateOrder creates a new player order
func (h *Handler) CreateOrder(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Title        string `json:"title"`
		Description  string `json:"description"`
		RewardType   string `json:"reward_type"`
		RewardAmount int    `json:"reward_amount"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	order, err := h.service.CreateOrder(ctx, userID, req.Title, req.Description, req.RewardType, req.RewardAmount)
	if err != nil {
		h.logger.Error("Failed to create order", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create order")
		return
	}

	h.respondJSON(w, http.StatusCreated, order)
}

// GetOrder gets a specific order
func (h *Handler) GetOrder(w http.ResponseWriter, r *http.Request) {
	orderIDStr := chi.URLParam(r, "orderID")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	ctx := r.Context()
	order, err := h.service.GetOrder(ctx, orderID)
	if err != nil {
		h.logger.Error("Failed to get order", zap.Error(err))
		h.respondError(w, http.StatusNotFound, "Order not found")
		return
	}

	h.respondJSON(w, http.StatusOK, order)
}

// UpdateOrder updates an order
func (h *Handler) UpdateOrder(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement order update logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// AcceptOrder allows accepting an order
func (h *Handler) AcceptOrder(w http.ResponseWriter, r *http.Request) {
	orderIDStr := chi.URLParam(r, "orderID")
	orderID, err := uuid.Parse(orderIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid order ID")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	err = h.service.AcceptOrder(ctx, orderID, userID)
	if err != nil {
		h.logger.Error("Failed to accept order", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to accept order")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "accepted"})
}

// Mentorship handlers

// GetMentors gets available mentors
func (h *Handler) GetMentors(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	mentors, err := h.service.GetMentors(ctx)
	if err != nil {
		h.logger.Error("Failed to get mentors", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get mentors")
		return
	}

	h.respondJSON(w, http.StatusOK, mentors)
}

// CreateMentorshipProposal creates a mentorship proposal
func (h *Handler) CreateMentorshipProposal(w http.ResponseWriter, r *http.Request) {
	var req struct {
		MentorID     string `json:"mentor_id"`
		StudentID    string `json:"student_id"`
		ProposalType string `json:"proposal_type"`
		Message      string `json:"message"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	mentorID, err := uuid.Parse(req.MentorID)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid mentor ID")
		return
	}

	studentID, err := uuid.Parse(req.StudentID)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid student ID")
		return
	}

	ctx := r.Context()
	proposal, err := h.service.CreateMentorshipProposal(ctx, mentorID, studentID, req.ProposalType, req.Message)
	if err != nil {
		h.logger.Error("Failed to create mentorship proposal", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create proposal")
		return
	}

	h.respondJSON(w, http.StatusCreated, proposal)
}

// GetMentorshipProposals gets mentorship proposals
func (h *Handler) GetMentorshipProposals(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	proposals, err := h.service.GetMentorshipProposals(ctx)
	if err != nil {
		h.logger.Error("Failed to get mentorship proposals", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get proposals")
		return
	}

	h.respondJSON(w, http.StatusOK, proposals)
}

// AcceptMentorshipProposal accepts a mentorship proposal
func (h *Handler) AcceptMentorshipProposal(w http.ResponseWriter, r *http.Request) {
	proposalIDStr := chi.URLParam(r, "proposalID")
	proposalID, err := uuid.Parse(proposalIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid proposal ID")
		return
	}

	ctx := r.Context()
	err = h.service.AcceptMentorshipProposal(ctx, proposalID)
	if err != nil {
		h.logger.Error("Failed to accept mentorship proposal", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to accept proposal")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "accepted"})
}

// Reputation handlers

// GetPlayerReputation gets player reputation
func (h *Handler) GetPlayerReputation(w http.ResponseWriter, r *http.Request) {
	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	reputation, err := h.service.GetPlayerReputation(ctx, userID)
	if err != nil {
		h.logger.Error("Failed to get player reputation", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get reputation")
		return
	}

	h.respondJSON(w, http.StatusOK, reputation)
}

// GetReputationLeaderboard gets reputation leaderboard
func (h *Handler) GetReputationLeaderboard(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	leaderboard, err := h.service.GetReputationLeaderboard(ctx)
	if err != nil {
		h.logger.Error("Failed to get reputation leaderboard", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get leaderboard")
		return
	}

	h.respondJSON(w, http.StatusOK, leaderboard)
}

// GetReputationBenefits gets reputation benefits
func (h *Handler) GetReputationBenefits(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	benefits, err := h.service.GetReputationBenefits(ctx)
	if err != nil {
		h.logger.Error("Failed to get reputation benefits", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get benefits")
		return
	}

	h.respondJSON(w, http.StatusOK, benefits)
}

// Notifications handlers

// GetNotifications gets player notifications
func (h *Handler) GetNotifications(w http.ResponseWriter, r *http.Request) {
	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	notifications, err := h.service.GetPlayerNotifications(ctx, userID)
	if err != nil {
		h.logger.Error("Failed to get notifications", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get notifications")
		return
	}

	h.respondJSON(w, http.StatusOK, notifications)
}

// MarkNotificationRead marks notification as read
func (h *Handler) MarkNotificationRead(w http.ResponseWriter, r *http.Request) {
	notificationIDStr := chi.URLParam(r, "notificationID")
	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid notification ID")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	err = h.service.MarkNotificationRead(ctx, notificationID, userID)
	if err != nil {
		h.logger.Error("Failed to mark notification read", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to mark notification read")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "marked_read"})
}

// UpdateNotificationPreferences updates notification preferences
func (h *Handler) UpdateNotificationPreferences(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement notification preferences update
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// Utility methods

func (h *Handler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handler) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}
