package service

import (
	"context"
	"fmt"
	"strings"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/config"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/repository"
)

// SupportService implements business logic for support ticket management
type SupportService struct {
	repo   repository.SupportRepository
	logger *zap.Logger
	config *config.Config
}

// NewSupportService creates a new support service instance
func NewSupportService(repo repository.SupportRepository, logger *zap.Logger, cfg *config.Config) *SupportService {
	return &SupportService{
		repo:   repo,
		logger: logger,
		config: cfg,
	}
}

// CreateTicket creates a new support ticket
func (s *SupportService) CreateTicket(ctx context.Context, req *models.CreateTicketRequest) (*models.Ticket, error) {
	ticket := &models.Ticket{
		ID:          uuid.New(),
		CharacterID: req.CharacterID,
		Title:       req.Title,
		Description: req.Description,
		Category:    req.Category,
		Priority:    req.Priority,
		Status:      models.TicketStatusOpen,
		Tags:        req.Tags,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	// Set SLA deadline based on priority
	slaDeadline := s.calculateSLADeadline(ticket.Priority)
	ticket.SLADeadline = &slaDeadline

	// Auto-categorize if category not provided
	if ticket.Category == "" {
		ticket.Category = s.autoCategorizeTicket(ticket.Title, ticket.Description)
	}

	// Auto-assign priority if not provided
	if ticket.Priority == "" {
		ticket.Priority = s.autoAssignPriority(ticket.Title, ticket.Description, ticket.Category)
	}

	err := s.repo.CreateTicket(ctx, ticket)
	if err != nil {
		s.logger.Error("Failed to create ticket", zap.Error(err), zap.String("ticket_id", ticket.ID.String()))
		return nil, fmt.Errorf("failed to create ticket: %w", err)
	}

	s.logger.Info("Ticket created successfully",
		zap.String("ticket_id", ticket.ID.String()),
		zap.String("character_id", ticket.CharacterID.String()),
		zap.String("priority", string(ticket.Priority)))

	return ticket, nil
}

// GetTicket retrieves a ticket by ID
func (s *SupportService) GetTicket(ctx context.Context, ticketID uuid.UUID) (*models.Ticket, error) {
	ticket, err := s.repo.GetTicket(ctx, ticketID)
	if err != nil {
		s.logger.Error("Failed to get ticket", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return nil, fmt.Errorf("failed to get ticket: %w", err)
	}

	return ticket, nil
}

// UpdateTicket updates ticket information
func (s *SupportService) UpdateTicket(ctx context.Context, ticketID uuid.UUID, req *models.UpdateTicketRequest) (*models.Ticket, error) {
	ticket, err := s.repo.GetTicket(ctx, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get ticket: %w", err)
	}

	// Update fields
	if req.Title != nil && *req.Title != "" {
		ticket.Title = *req.Title
	}
	if req.Description != nil && *req.Description != "" {
		ticket.Description = *req.Description
	}
	if req.Category != nil {
		ticket.Category = *req.Category
	}
	if req.Priority != nil {
		ticket.Priority = *req.Priority
		// Recalculate SLA deadline for new priority
		slaDeadline := s.calculateSLADeadline(ticket.Priority)
		ticket.SLADeadline = &slaDeadline
	}
	if req.Tags != nil {
		ticket.Tags = req.Tags
	}

	ticket.UpdatedAt = time.Now()

	err = s.repo.UpdateTicket(ctx, ticket)
	if err != nil {
		s.logger.Error("Failed to update ticket", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return nil, fmt.Errorf("failed to update ticket: %w", err)
	}

	s.logger.Info("Ticket updated successfully", zap.String("ticket_id", ticketID.String()))
	return ticket, nil
}

// DeleteTicket deletes a ticket
func (s *SupportService) DeleteTicket(ctx context.Context, ticketID uuid.UUID) error {
	err := s.repo.DeleteTicket(ctx, ticketID)
	if err != nil {
		s.logger.Error("Failed to delete ticket", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return fmt.Errorf("failed to delete ticket: %w", err)
	}

	s.logger.Info("Ticket deleted successfully", zap.String("ticket_id", ticketID.String()))
	return nil
}

// ListTickets retrieves paginated list of tickets with filters
func (s *SupportService) ListTickets(ctx context.Context, filter *models.TicketFilter, page, limit int) (*models.TicketListResponse, error) {
	tickets, total, err := s.repo.ListTickets(ctx, filter, page, limit)
	if err != nil {
		s.logger.Error("Failed to list tickets", zap.Error(err))
		return nil, fmt.Errorf("failed to list tickets: %w", err)
	}

	totalPages := (total + limit - 1) / limit

	// Convert []*models.Ticket to []models.Ticket
	ticketValues := make([]models.Ticket, len(tickets))
	for i, ticket := range tickets {
		ticketValues[i] = *ticket
	}

	response := &models.TicketListResponse{
		Tickets: ticketValues,
		Pagination: models.PaginationInfo{
			Page:       page,
			Limit:      limit,
			Total:      total,
			TotalPages: totalPages,
		},
	}

	return response, nil
}

// AssignAgent assigns an agent to a ticket
func (s *SupportService) AssignAgent(ctx context.Context, ticketID, agentID uuid.UUID) (*models.Ticket, error) {
	ticket, err := s.repo.GetTicket(ctx, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get ticket: %w", err)
	}

	ticket.AgentID = &agentID
	ticket.Status = models.TicketStatusAssigned
	ticket.UpdatedAt = time.Now()

	err = s.repo.UpdateTicket(ctx, ticket)
	if err != nil {
		s.logger.Error("Failed to assign agent", zap.Error(err),
			zap.String("ticket_id", ticketID.String()), zap.String("agent_id", agentID.String()))
		return nil, fmt.Errorf("failed to assign agent: %w", err)
	}

	s.logger.Info("Agent assigned to ticket",
		zap.String("ticket_id", ticketID.String()), zap.String("agent_id", agentID.String()))

	return ticket, nil
}

// UpdateTicketStatus updates the status of a ticket
func (s *SupportService) UpdateTicketStatus(ctx context.Context, ticketID uuid.UUID, status models.TicketStatus, comment string) (*models.Ticket, error) {
	ticket, err := s.repo.GetTicket(ctx, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get ticket: %w", err)
	}

	oldStatus := ticket.Status
	ticket.Status = status
	ticket.UpdatedAt = time.Now()

	// Set resolved timestamp if status changed to resolved
	if status == models.TicketStatusResolved && oldStatus != models.TicketStatusResolved {
		now := time.Now()
		ticket.ResolvedAt = &now
	}

	err = s.repo.UpdateTicket(ctx, ticket)
	if err != nil {
		s.logger.Error("Failed to update ticket status", zap.Error(err),
			zap.String("ticket_id", ticketID.String()), zap.String("status", string(status)))
		return nil, fmt.Errorf("failed to update ticket status: %w", err)
	}

	// Create status change response if comment provided
	if comment != "" {
		// Get user information from context
		userCtx, ok := models.GetUserFromContext(ctx)
		if !ok {
			s.logger.Warn("Failed to get user from context for status change response")
			return ticket, nil // Don't fail the whole operation
		}

		response := &models.TicketResponse{
			ID:         uuid.New(),
			TicketID:   ticketID,
			AuthorID:   userCtx.UserID,
			AuthorType: userCtx.UserType,
			Content:    fmt.Sprintf("Status changed from %s to %s. %s", oldStatus, status, comment),
			IsInternal: false,
			CreatedAt:  time.Now(),
		}

		err = s.repo.CreateTicketResponse(ctx, response)
		if err != nil {
			s.logger.Warn("Failed to create status change response", zap.Error(err))
		}
	}

	s.logger.Info("Ticket status updated",
		zap.String("ticket_id", ticketID.String()),
		zap.String("old_status", string(oldStatus)),
		zap.String("new_status", string(status)))

	return ticket, nil
}

// UpdateTicketPriority updates the priority of a ticket
func (s *SupportService) UpdateTicketPriority(ctx context.Context, ticketID uuid.UUID, priority models.TicketPriority, reason string) (*models.Ticket, error) {
	ticket, err := s.repo.GetTicket(ctx, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get ticket: %w", err)
	}

	ticket.Priority = priority
	ticket.UpdatedAt = time.Now()

	// Recalculate SLA deadline for new priority
	slaDeadline := s.calculateSLADeadline(priority)
	ticket.SLADeadline = &slaDeadline

	err = s.repo.UpdateTicket(ctx, ticket)
	if err != nil {
		s.logger.Error("Failed to update ticket priority", zap.Error(err),
			zap.String("ticket_id", ticketID.String()), zap.String("priority", string(priority)))
		return nil, fmt.Errorf("failed to update ticket priority: %w", err)
	}

	// Create priority change response if reason provided
	if reason != "" {
		// Get user information from context
		userCtx, ok := models.GetUserFromContext(ctx)
		if !ok {
			s.logger.Warn("Failed to get user from context for priority change response")
			return ticket, nil // Don't fail the whole operation
		}

		response := &models.TicketResponse{
			ID:         uuid.New(),
			TicketID:   ticketID,
			AuthorID:   userCtx.UserID,
			AuthorType: userCtx.UserType,
			Content:    fmt.Sprintf("Priority changed to %s. Reason: %s", priority, reason),
			IsInternal: false,
			CreatedAt:  time.Now(),
		}

		err = s.repo.CreateTicketResponse(ctx, response)
		if err != nil {
			s.logger.Warn("Failed to create priority change response", zap.Error(err))
		}
	}

	s.logger.Info("Ticket priority updated",
		zap.String("ticket_id", ticketID.String()), zap.String("priority", string(priority)))

	return ticket, nil
}

// GetCharacterTickets retrieves all tickets for a character
func (s *SupportService) GetCharacterTickets(ctx context.Context, characterID uuid.UUID, page, limit int) (*models.TicketListResponse, error) {
	tickets, total, err := s.repo.GetTicketsByCharacter(ctx, characterID, page, limit)
	if err != nil {
		s.logger.Error("Failed to get character tickets", zap.Error(err), zap.String("character_id", characterID.String()))
		return nil, fmt.Errorf("failed to get character tickets: %w", err)
	}

	totalPages := (total + limit - 1) / limit

	// Convert []*models.Ticket to []models.Ticket
	ticketValues := make([]models.Ticket, len(tickets))
	for i, ticket := range tickets {
		ticketValues[i] = *ticket
	}

	response := &models.TicketListResponse{
		Tickets: ticketValues,
		Pagination: models.PaginationInfo{
			Page:       page,
			Limit:      limit,
			Total:      total,
			TotalPages: totalPages,
		},
	}

	return response, nil
}

// GetAgentTickets retrieves all tickets assigned to an agent
func (s *SupportService) GetAgentTickets(ctx context.Context, agentID uuid.UUID, page, limit int) (*models.TicketListResponse, error) {
	tickets, total, err := s.repo.GetTicketsByAgent(ctx, agentID, page, limit)
	if err != nil {
		s.logger.Error("Failed to get agent tickets", zap.Error(err), zap.String("agent_id", agentID.String()))
		return nil, fmt.Errorf("failed to get agent tickets: %w", err)
	}

	totalPages := (total + limit - 1) / limit

	// Convert []*models.Ticket to []models.Ticket
	ticketValues := make([]models.Ticket, len(tickets))
	for i, ticket := range tickets {
		ticketValues[i] = *ticket
	}

	response := &models.TicketListResponse{
		Tickets: ticketValues,
		Pagination: models.PaginationInfo{
			Page:       page,
			Limit:      limit,
			Total:      total,
			TotalPages: totalPages,
		},
	}

	return response, nil
}

// GetTicketQueue retrieves the current ticket queue for agent assignment
func (s *SupportService) GetTicketQueue(ctx context.Context, filter *models.QueueFilter, page, limit int) (*models.TicketQueueResponse, error) {
	queue, stats, err := s.repo.GetTicketQueue(ctx, filter, page, limit)
	if err != nil {
		s.logger.Error("Failed to get ticket queue", zap.Error(err))
		return nil, fmt.Errorf("failed to get ticket queue: %w", err)
	}

	// Convert []*models.Ticket to []models.Ticket
	ticketValues := make([]models.Ticket, len(queue))
	for i, ticket := range queue {
		ticketValues[i] = *ticket
	}

	response := &models.TicketQueueResponse{
		Queue:      ticketValues,
		QueueStats: *stats,
	}

	return response, nil
}

// AddTicketResponse adds a response to a ticket
func (s *SupportService) AddTicketResponse(ctx context.Context, ticketID uuid.UUID, req *models.AddResponseRequest, authorID uuid.UUID, authorType models.AuthorType) (*models.TicketResponse, error) {
	response := &models.TicketResponse{
		ID:         uuid.New(),
		TicketID:   ticketID,
		AuthorID:   authorID,
		AuthorType: authorType,
		Content:    req.Content,
		IsInternal: req.IsInternal,
		CreatedAt:  time.Now(),
	}

	// Handle attachments if provided
	if req.Attachments != nil && len(req.Attachments) > 0 {
		attachments := make([]models.Attachment, len(req.Attachments))
		for i, att := range req.Attachments {
			attachments[i] = models.Attachment{
				ID:          uuid.New(),
				Filename:    att.Filename,
				ContentType: att.ContentType,
				Size:        int64(len(att.Data)),
				Data:        att.Data,
			}
		}
		response.Attachments = attachments
	}

	err := s.repo.CreateTicketResponse(ctx, response)
	if err != nil {
		s.logger.Error("Failed to create ticket response", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return nil, fmt.Errorf("failed to create ticket response: %w", err)
	}

	// Update ticket's response count
	err = s.repo.IncrementTicketResponseCount(ctx, ticketID)
	if err != nil {
		s.logger.Warn("Failed to increment ticket response count", zap.Error(err))
	}

	s.logger.Info("Ticket response added",
		zap.String("ticket_id", ticketID.String()),
		zap.String("response_id", response.ID.String()),
		zap.String("author_type", string(authorType)))

	return response, nil
}

// GetTicketResponses retrieves all responses for a ticket
func (s *SupportService) GetTicketResponses(ctx context.Context, ticketID uuid.UUID, page, limit int) (*models.TicketResponseListResponse, error) {
	responses, total, err := s.repo.GetTicketResponses(ctx, ticketID, page, limit)
	if err != nil {
		s.logger.Error("Failed to get ticket responses", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return nil, fmt.Errorf("failed to get ticket responses: %w", err)
	}

	totalPages := (total + limit - 1) / limit

	// Convert []*models.TicketResponse to []models.TicketResponse
	responseValues := make([]models.TicketResponse, len(responses))
	for i, resp := range responses {
		responseValues[i] = *resp
	}

	response := &models.TicketResponseListResponse{
		Responses: responseValues,
		Pagination: models.PaginationInfo{
			Page:       page,
			Limit:      limit,
			Total:      total,
			TotalPages: totalPages,
		},
	}

	return response, nil
}

// RateTicket allows customer to rate ticket resolution
func (s *SupportService) RateTicket(ctx context.Context, ticketID uuid.UUID, rating int, comment string) error {
	err := s.repo.UpdateTicketRating(ctx, ticketID, rating, comment)
	if err != nil {
		s.logger.Error("Failed to rate ticket", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return fmt.Errorf("failed to rate ticket: %w", err)
	}

	s.logger.Info("Ticket rated",
		zap.String("ticket_id", ticketID.String()), zap.Int("rating", rating))

	return nil
}

// GetTicketSLA retrieves SLA information for a ticket
func (s *SupportService) GetTicketSLA(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAInfo, error) {
	slaInfo, err := s.repo.GetTicketSLAInfo(ctx, ticketID)
	if err != nil {
		s.logger.Error("Failed to get ticket SLA info", zap.Error(err), zap.String("ticket_id", ticketID.String()))
		return nil, fmt.Errorf("failed to get ticket SLA info: %w", err)
	}

	return slaInfo, nil
}

// GetSupportStats retrieves support statistics
func (s *SupportService) GetSupportStats(ctx context.Context, period string, category string) (*models.SupportStatsResponse, error) {
	stats, err := s.repo.GetSupportStats(ctx, period, category)
	if err != nil {
		s.logger.Error("Failed to get support stats", zap.Error(err), zap.String("period", period))
		return nil, fmt.Errorf("failed to get support stats: %w", err)
	}

	return stats, nil
}

// Helper methods

// calculateSLADeadline calculates SLA deadline based on priority
func (s *SupportService) calculateSLADeadline(priority models.TicketPriority) time.Time {
	now := time.Now()
	var duration time.Duration

	switch priority {
	case models.TicketPriorityCritical:
		duration = s.parseDuration(s.config.SLA.ResponseTimeUrgent)
	case models.TicketPriorityUrgent:
		duration = s.parseDuration(s.config.SLA.ResponseTimeUrgent)
	case models.TicketPriorityHigh:
		duration = s.parseDuration(s.config.SLA.ResponseTimeHigh)
	case models.TicketPriorityNormal:
		duration = s.parseDuration(s.config.SLA.ResponseTimeNormal)
	case models.TicketPriorityLow:
		duration = s.parseDuration(s.config.SLA.ResponseTimeLow)
	default:
		duration = s.parseDuration(s.config.SLA.ResponseTimeNormal)
	}

	return now.Add(duration)
}

// parseDuration parses duration string with default fallback
func (s *SupportService) parseDuration(durationStr string) time.Duration {
	duration, err := time.ParseDuration(durationStr)
	if err != nil {
		s.logger.Warn("Failed to parse duration, using default", zap.String("duration", durationStr), zap.Error(err))
		return 24 * time.Hour // Default 24 hours
	}
	return duration
}

// autoCategorizeTicket automatically categorizes ticket based on content
func (s *SupportService) autoCategorizeTicket(title, description string) models.TicketCategory {
	content := title + " " + description

	// Simple keyword-based categorization
	switch {
	case containsKeywords(content, "bug", "error", "crash", "not working", "broken"):
		return models.TicketCategoryBugReport
	case containsKeywords(content, "billing", "payment", "money", "charge", "refund"):
		return models.TicketCategoryBilling
	case containsKeywords(content, "gameplay", "game", "play", "level", "quest"):
		return models.TicketCategoryGameplay
	case containsKeywords(content, "account", "login", "password", "access"):
		return models.TicketCategoryAccount
	case containsKeywords(content, "feature", "suggest", "idea", "improve"):
		return models.TicketCategoryFeatureRequest
	default:
		return models.TicketCategoryOther
	}
}

// autoAssignPriority automatically assigns priority based on content
func (s *SupportService) autoAssignPriority(title, description string, category models.TicketCategory) models.TicketPriority {
	content := title + " " + description

	// Check for urgent keywords
	if containsKeywords(content, "emergency", "critical", "urgent", "immediate", "blocking") {
		return models.TicketPriorityUrgent
	}

	// Check for high priority keywords
	if containsKeywords(content, "crash", "login", "payment", "security", "hack") {
		return models.TicketPriorityHigh
	}

	// Category-based priority
	switch category {
	case models.TicketCategoryBugReport:
		if containsKeywords(content, "all players", "server", "everyone") {
			return models.TicketPriorityHigh
		}
		return models.TicketPriorityNormal
	case models.TicketCategoryBilling:
		return models.TicketPriorityHigh
	case models.TicketCategoryAccount:
		return models.TicketPriorityHigh
	default:
		return models.TicketPriorityNormal
	}
}

// containsKeywords checks if content contains any of the keywords (case insensitive)
func containsKeywords(content string, keywords ...string) bool {
	content = strings.ToLower(content)
	for _, keyword := range keywords {
		if strings.Contains(content, strings.ToLower(keyword)) {
			return true
		}
	}
	return false
}