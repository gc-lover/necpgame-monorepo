package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type MailService struct {
	mailRepo  MailRepositoryInterface
	cache     *redis.Client
	logger    *logrus.Logger
	eventBus  EventBus
}

func NewMailService(
	mailRepo MailRepositoryInterface,
	cache *redis.Client,
	logger *logrus.Logger,
	eventBus EventBus,
) *MailService {
	return &MailService{
		mailRepo: mailRepo,
		cache:    cache,
		logger:   logger,
		eventBus: eventBus,
	}
}

func (s *MailService) SendMail(ctx context.Context, req *models.CreateMailRequest, senderID *uuid.UUID, senderName string) (*models.MailMessage, error) {
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
		IsClaimed:   false,
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
			"mail_id":         mail.ID.String(),
			"sender_id":       nil,
			"recipient_id":    mail.RecipientID.String(),
			"type":            string(mail.Type),
			"subject":         mail.Subject,
			"has_attachments": mail.Attachments != nil && len(mail.Attachments) > 0,
			"timestamp":       mail.SentAt.Format(time.RFC3339),
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

func (s *MailService) GetMails(ctx context.Context, recipientID uuid.UUID, limit, offset int) (*models.MailListResponse, error) {
	cacheKey := "mails:recipient:" + recipientID.String() + ":limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.MailListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		} else {
			s.logger.WithError(err).Error("Failed to unmarshal cached mails JSON")
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

	responseJSON, err := json.Marshal(response)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal mails response JSON")
	} else {
		s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)
	}

	return response, nil
}

func (s *MailService) MarkMailAsRead(ctx context.Context, mailID uuid.UUID) error {
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

func (s *MailService) ClaimAttachment(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
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
			"mail_id":      mail.ID.String(),
			"recipient_id": mail.RecipientID.String(),
			"attachments":  mail.Attachments,
			"timestamp":    time.Now().Format(time.RFC3339),
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

func (s *MailService) DeleteMail(ctx context.Context, mailID uuid.UUID) error {
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

func (s *MailService) GetMail(ctx context.Context, mailID uuid.UUID) (*models.MailMessage, error) {
	return s.mailRepo.GetByID(ctx, mailID)
}

func (s *MailService) GetUnreadMailCount(ctx context.Context, recipientID uuid.UUID) (*models.UnreadMailCountResponse, error) {
	unread, err := s.mailRepo.CountUnreadByRecipientID(ctx, recipientID)
	if err != nil {
		return nil, err
	}
	total, err := s.mailRepo.CountByRecipientID(ctx, recipientID)
	if err != nil {
		return nil, err
	}
	return &models.UnreadMailCountResponse{
		UnreadCount: unread,
		TotalCount:  total,
	}, nil
}

func (s *MailService) GetMailAttachments(ctx context.Context, mailID uuid.UUID) (*models.MailAttachmentsResponse, error) {
	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return nil, err
	}
	if mail == nil {
		return nil, nil
	}

	attachments := []models.MailAttachment{}
	if mail.Attachments != nil {
		if items, ok := mail.Attachments["items"].([]interface{}); ok {
			for _, item := range items {
				if itemMap, ok := item.(map[string]interface{}); ok {
					att := models.MailAttachment{
						Type: "item",
						Data: itemMap,
					}
					if itemID, ok := itemMap["item_id"].(string); ok {
						att.ItemID = &itemID
					}
					if qty, ok := itemMap["quantity"].(float64); ok {
						qtyInt := int(qty)
						att.Quantity = &qtyInt
					}
					attachments = append(attachments, att)
				}
			}
		}
		if currency, ok := mail.Attachments["currency"].(map[string]interface{}); ok {
			currencyMap := make(map[string]int)
			for k, v := range currency {
				if val, ok := v.(float64); ok {
					currencyMap[k] = int(val)
				}
			}
			if len(currencyMap) > 0 {
				attachments = append(attachments, models.MailAttachment{
					Type:     "currency",
					Currency: currencyMap,
				})
			}
		}
	}

	return &models.MailAttachmentsResponse{
		MailID:      mailID,
		Attachments: attachments,
		HasCOD:      mail.CODAmount != nil && *mail.CODAmount > 0,
		CODAmount:   mail.CODAmount,
	}, nil
}

func (s *MailService) PayMailCOD(ctx context.Context, mailID uuid.UUID) (*models.ClaimAttachmentResponse, error) {
	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return nil, err
	}
	if mail == nil {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}

	if mail.CODAmount == nil || *mail.CODAmount <= 0 {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}

	if mail.IsClaimed {
		return &models.ClaimAttachmentResponse{Success: false}, nil
	}

	err = s.mailRepo.MarkAsClaimed(ctx, mailID)
	if err != nil {
		return nil, err
	}

	s.invalidateMailCache(ctx, mail.RecipientID)

	items := make(map[string]interface{})
	currency := make(map[string]int)

	if mail.Attachments != nil {
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
	}

	return &models.ClaimAttachmentResponse{
		Success:  true,
		Items:    items,
		Currency: currency,
	}, nil
}

func (s *MailService) DeclineMailCOD(ctx context.Context, mailID uuid.UUID) error {
	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return err
	}
	if mail == nil {
		return nil
	}

	if mail.CODAmount == nil || *mail.CODAmount <= 0 {
		return nil
	}

	s.invalidateMailCache(ctx, mail.RecipientID)
	return nil
}

func (s *MailService) GetExpiringMails(ctx context.Context, recipientID uuid.UUID, days int, limit, offset int) (*models.MailListResponse, error) {
	messages, err := s.mailRepo.GetExpiringMailsByDays(ctx, recipientID, days, limit, offset)
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

	return &models.MailListResponse{
		Messages: messages,
		Total:    total,
		Unread:   unread,
	}, nil
}

func (s *MailService) ExtendMailExpiration(ctx context.Context, mailID uuid.UUID, days int) (*models.MailMessage, error) {
	err := s.mailRepo.ExtendExpiration(ctx, mailID, days)
	if err != nil {
		return nil, err
	}

	mail, err := s.mailRepo.GetByID(ctx, mailID)
	if err != nil {
		return nil, err
	}

	if mail != nil {
		s.invalidateMailCache(ctx, mail.RecipientID)
	}

	return mail, nil
}

func (s *MailService) SendSystemMail(ctx context.Context, req *models.SendSystemMailRequest) (*models.MailMessage, error) {
	now := time.Now()
	expiresAt := (*time.Time)(nil)
	if req.ExpiresIn != nil && *req.ExpiresIn > 0 {
		exp := now.Add(time.Duration(*req.ExpiresIn) * 24 * time.Hour)
		expiresAt = &exp
	}

	mail := &models.MailMessage{
		ID:          uuid.New(),
		SenderID:    nil,
		SenderName:  "System",
		RecipientID: req.RecipientID,
		Type:        req.Type,
		Subject:     req.Title,
		Content:     req.Content,
		Attachments: req.Attachments,
		Status:      models.MailStatusUnread,
		IsRead:      false,
		IsClaimed:   false,
		SentAt:      now,
		CreatedAt:   now,
		UpdatedAt:   now,
		ExpiresAt:   expiresAt,
	}

	err := s.mailRepo.Create(ctx, mail)
	if err != nil {
		return nil, err
	}

	s.invalidateMailCache(ctx, req.RecipientID)

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"mail_id":      mail.ID.String(),
			"recipient_id": mail.RecipientID.String(),
			"type":         string(mail.Type),
			"subject":      mail.Subject,
			"timestamp":    mail.SentAt.Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "mail:system-sent", payload)
	}

	return mail, nil
}

func (s *MailService) BroadcastSystemMail(ctx context.Context, req *models.BroadcastSystemMailRequest) (*models.BroadcastResult, error) {
	result := &models.BroadcastResult{
		TotalSent:       0,
		TotalFailed:     0,
		FailedRecipients: []string{},
	}

	if len(req.RecipientIDs) == 0 {
		return result, nil
	}

	for _, recipientID := range req.RecipientIDs {
		systemReq := &models.SendSystemMailRequest{
			RecipientID: recipientID,
			Type:        req.Type,
			Title:       req.Title,
			Content:     req.Content,
			Attachments: req.Attachments,
			ExpiresIn:   req.ExpiresIn,
		}

		_, err := s.SendSystemMail(ctx, systemReq)
		if err != nil {
			result.TotalFailed++
			result.FailedRecipients = append(result.FailedRecipients, recipientID.String())
		} else {
			result.TotalSent++
		}
	}

	return result, nil
}

func (s *MailService) invalidateMailCache(ctx context.Context, recipientID uuid.UUID) {
	pattern := "mails:recipient:" + recipientID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

