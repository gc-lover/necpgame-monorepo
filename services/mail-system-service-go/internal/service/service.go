package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/mail-system-service-go/internal/repository"
	"services/mail-system-service-go/pkg/models"
)

// Service handles business logic for mail system
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// GetMailbox retrieves user's mailbox with pagination and filtering
func (s *Service) GetMailbox(ctx context.Context, userID uuid.UUID, folder, status, category string, limit, offset int) (*models.MailboxResponse, error) {
	if limit <= 0 || limit > 100 {
		limit = 50
	}
	if offset < 0 {
		offset = 0
	}

	mails, totalCount, err := s.repo.GetMailbox(ctx, userID, folder, status, category, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get mailbox", zap.Error(err), zap.String("user_id", userID.String()))
		return nil, fmt.Errorf("failed to get mailbox: %w", err)
	}

	unreadCount, err := s.repo.GetUnreadCount(ctx, userID)
	if err != nil {
		s.logger.Warn("Failed to get unread count", zap.Error(err))
		unreadCount = 0
	}

	hasMore := offset+limit < totalCount

	return &models.MailboxResponse{
		Pagination: models.PaginationInfo{
			HasMore:    hasMore,
			Offset:     offset,
			Limit:      limit,
			TotalItems: totalCount,
		},
		Mails:       mails,
		TotalCount:  totalCount,
		UnreadCount: unreadCount,
	}, nil
}

// GetMail retrieves a specific mail
func (s *Service) GetMail(ctx context.Context, mailID, userID uuid.UUID) (*models.Mail, error) {
	mail, err := s.repo.GetMail(ctx, mailID, userID)
	if err != nil {
		s.logger.Error("Failed to get mail", zap.Error(err),
			zap.String("mail_id", mailID.String()),
			zap.String("user_id", userID.String()))
		return nil, err
	}

	return mail, nil
}

// SendMail sends a new mail
func (s *Service) SendMail(ctx context.Context, request *models.SendMailRequest, senderID uuid.UUID) (*models.SendMailResponse, error) {
	if len(request.RecipientIDs) == 0 {
		return nil, fmt.Errorf("at least one recipient is required")
	}

	if len(request.RecipientIDs) > 50 {
		return nil, fmt.Errorf("maximum 50 recipients allowed")
	}

	// Validate attachments
	totalSize := int64(0)
	for _, attachment := range request.Attachments {
		if attachment.SizeBytes > 10*1024*1024 { // 10MB limit
			return nil, fmt.Errorf("attachment size exceeds limit")
		}
		totalSize += attachment.SizeBytes
		if totalSize > 50*1024*1024 { // 50MB total limit
			return nil, fmt.Errorf("total attachment size exceeds limit")
		}
	}

	response := &models.SendMailResponse{
		MailID:         uuid.New(),
		SentAt:         time.Now(),
		DeliveryStatus: "sent",
		RecipientCount: len(request.RecipientIDs),
	}

	// Send to each recipient
	for _, recipientID := range request.RecipientIDs {
		mail := &models.Mail{
			ID:          response.MailID,
			SenderID:    senderID,
			RecipientID: recipientID,
			Subject:     request.Subject,
			Category:    request.Category,
			Priority:    request.Priority,
			SentAt:      response.SentAt,
			Content:     request.Content,
		}

		// Set expiration
		if request.ExpiresInHours > 0 {
			expiresAt := response.SentAt.Add(time.Duration(request.ExpiresInHours) * time.Hour)
			mail.ExpiresAt = &expiresAt
		}

		// Add attachments
		for _, upload := range request.Attachments {
			attachment := models.Attachment{
				ID:          uuid.New(),
				MailID:      mail.ID,
				Filename:    upload.Filename,
				ContentType: upload.ContentType,
				SizeBytes:   upload.SizeBytes,
				Data:        upload.Data,
			}
			mail.Attachments = append(mail.Attachments, attachment)
		}

		if err := s.repo.SendMail(ctx, mail); err != nil {
			s.logger.Error("Failed to send mail to recipient",
				zap.Error(err),
				zap.String("mail_id", mail.ID.String()),
				zap.String("recipient_id", recipientID.String()))
			response.DeliveryStatus = "partial_failure"
		}
	}

	s.logger.Info("Mail sent successfully",
		zap.String("mail_id", response.MailID.String()),
		zap.Int("recipient_count", response.RecipientCount),
		zap.String("status", response.DeliveryStatus))

	return response, nil
}

// MarkAsRead marks a mail as read
func (s *Service) MarkAsRead(ctx context.Context, mailID, userID uuid.UUID) error {
	if err := s.repo.MarkAsRead(ctx, mailID, userID); err != nil {
		s.logger.Error("Failed to mark mail as read", zap.Error(err),
			zap.String("mail_id", mailID.String()),
			zap.String("user_id", userID.String()))
		return err
	}

	s.logger.Info("Mail marked as read",
		zap.String("mail_id", mailID.String()),
		zap.String("user_id", userID.String()))

	return nil
}

// DeleteMail deletes a mail
func (s *Service) DeleteMail(ctx context.Context, mailID, userID uuid.UUID) error {
	if err := s.repo.DeleteMail(ctx, mailID, userID); err != nil {
		s.logger.Error("Failed to delete mail", zap.Error(err),
			zap.String("mail_id", mailID.String()),
			zap.String("user_id", userID.String()))
		return err
	}

	s.logger.Info("Mail deleted",
		zap.String("mail_id", mailID.String()),
		zap.String("user_id", userID.String()))

	return nil
}

// ArchiveMail archives a mail
func (s *Service) ArchiveMail(ctx context.Context, mailID, userID uuid.UUID) error {
	if err := s.repo.ArchiveMail(ctx, mailID, userID); err != nil {
		s.logger.Error("Failed to archive mail", zap.Error(err),
			zap.String("mail_id", mailID.String()),
			zap.String("user_id", userID.String()))
		return err
	}

	s.logger.Info("Mail archived",
		zap.String("mail_id", mailID.String()),
		zap.String("user_id", userID.String()))

	return nil
}

// DownloadAttachment retrieves a mail attachment
func (s *Service) DownloadAttachment(ctx context.Context, attachmentID, userID uuid.UUID) (*models.Attachment, error) {
	attachment, err := s.repo.GetAttachment(ctx, attachmentID, userID)
	if err != nil {
		s.logger.Error("Failed to download attachment", zap.Error(err),
			zap.String("attachment_id", attachmentID.String()),
			zap.String("user_id", userID.String()))
		return nil, err
	}

	return attachment, nil
}

// ReportMail reports a mail for moderation
func (s *Service) ReportMail(ctx context.Context, mailID, reporterID uuid.UUID, request *models.ReportRequest) (*models.Report, error) {
	report := &models.Report{
		ID:          uuid.New(),
		MailID:      mailID,
		ReporterID:  reporterID,
		Reason:      request.Reason,
		Description: request.Description,
		Severity:    request.Severity,
		SubmittedAt: time.Now(),
		Status:      "submitted",
	}

	if err := s.repo.ReportMail(ctx, report); err != nil {
		s.logger.Error("Failed to report mail", zap.Error(err),
			zap.String("mail_id", mailID.String()),
			zap.String("reporter_id", reporterID.String()))
		return nil, fmt.Errorf("failed to submit report: %w", err)
	}

	s.logger.Info("Mail reported for moderation",
		zap.String("report_id", report.ID.String()),
		zap.String("mail_id", mailID.String()),
		zap.String("reason", report.Reason))

	return report, nil
}

// SendBulkMail sends bulk mail based on criteria
func (s *Service) SendBulkMail(ctx context.Context, request *models.BulkMailRequest) (*models.BulkMailResponse, error) {
	// This would implement complex recipient selection logic
	// For now, return a placeholder response
	response := &models.BulkMailResponse{
		OperationID:          uuid.New(),
		EstimatedCompletion:  time.Now().Add(30 * time.Minute),
		Status:               "queued",
		TotalRecipients:      1000, // Placeholder
	}

	s.logger.Info("Bulk mail operation queued",
		zap.String("operation_id", response.OperationID.String()),
		zap.Int("estimated_recipients", response.TotalRecipients))

	return response, nil
}

// SendSystemAnnouncement sends a system-wide announcement
func (s *Service) SendSystemAnnouncement(ctx context.Context, request *models.SystemAnnouncementRequest) (*models.SystemAnnouncementResponse, error) {
	response := &models.SystemAnnouncementResponse{
		AnnouncementID:   uuid.New(),
		ScheduledDelivery: request.ScheduledDelivery,
		Priority:         request.Priority,
		RecipientCount:   10000, // Placeholder for all active players
	}

	s.logger.Info("System announcement queued",
		zap.String("announcement_id", response.AnnouncementID.String()),
		zap.String("priority", response.Priority),
		zap.Int("recipient_count", response.RecipientCount))

	return response, nil
}

// GetMailAnalytics returns mail analytics data
func (s *Service) GetMailAnalytics(ctx context.Context, timeframe string) (*models.AnalyticsResponse, error) {
	// This would implement analytics aggregation logic
	// For now, return placeholder data
	response := &models.AnalyticsResponse{
		Timeframe: timeframe,
		CategoryBreakdown: map[string]int{
			"personal":  1500,
			"system":    800,
			"trade":     300,
			"guild":     200,
			"event":     100,
			"reward":    50,
		},
		AverageResponseTime:  2.5,
		DeliverySuccessRate:  0.98,
		TotalMailsSent:       2950,
		TotalMailsReceived:   2950,
		SpamReports:          15,
	}

	return response, nil
}

