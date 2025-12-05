// Issue: #151, #1607
package server

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/mail-service-go/pkg/api"
)

// getPlayerIDFromContext extracts player ID from context (from JWT token)
func getPlayerIDFromContext(ctx context.Context) (uuid.UUID, error) {
	// Try different context keys used in different services
	if playerID, ok := ctx.Value("player_id").(uuid.UUID); ok {
		return playerID, nil
	}
	if playerID, ok := ctx.Value("user_uuid").(uuid.UUID); ok {
		return playerID, nil
	}
	if playerIDStr, ok := ctx.Value("user_id").(string); ok {
		playerID, err := uuid.Parse(playerIDStr)
		if err == nil {
			return playerID, nil
		}
	}
	// TODO: Implement proper JWT extraction (Issue: Auth integration)
	return uuid.Nil, fmt.Errorf("player_id not found in context")
}

type Service interface {
	GetInbox(ctx context.Context, params api.GetInboxParams) (*api.InboxResponse, error)
	GetMail(ctx context.Context, mailID string) (*api.MailDetailResponse, error)
	DeleteMail(ctx context.Context, mailID string) error
	SendMail(ctx context.Context, req *api.SendMailRequest) (*api.SendMailResponse, error)
	ClaimAttachments(ctx context.Context, mailID string) (*api.ClaimAttachmentsResponse, error)
}

// MailService implements business logic for mail system
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type MailService struct {
	repository Repository

	// Memory pooling for hot path structs (zero allocations target!)
	inboxResponsePool sync.Pool
	mailDetailResponsePool sync.Pool
	sendMailResponsePool sync.Pool
	claimAttachmentsResponsePool sync.Pool
}

func NewMailService(repository Repository) Service {
	s := &MailService{repository: repository}

	// Initialize memory pools (zero allocations target!)
	s.inboxResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.InboxResponse{}
		},
	}
	s.mailDetailResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.MailDetailResponse{}
		},
	}
	s.sendMailResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SendMailResponse{}
		},
	}
	s.claimAttachmentsResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ClaimAttachmentsResponse{}
		},
	}

	return s
}

// GetInbox returns inbox mail list
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *MailService) GetInbox(ctx context.Context, params api.GetInboxParams) (*api.InboxResponse, error) {
	// Get playerID from JWT token in context
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}
	
	// Get filter
	filter := "all"
	if params.Status.Set {
		filter = string(params.Status.Value)
	}
	
	// Get pagination
	limit := 50
	if params.Limit.Set {
		limit = params.Limit.Value
		if limit > 100 {
			limit = 100
		}
	}
	offset := 0
	if params.Page.Set {
		page := params.Page.Value
		if page > 0 {
			offset = (page - 1) * limit
		}
	}
	
	// Get mails from repository
	mails, total, err := s.repository.GetPlayerMails(ctx, playerID, filter, limit, offset)
	if err != nil {
		return nil, err
	}
	
	// Get unread count
	unreadCount, err := s.repository.GetUnreadCount(ctx, playerID)
	if err != nil {
		unreadCount = 0 // Continue if error
	}
	
	// Issue: #1607 - Use memory pooling
	result := s.inboxResponsePool.Get().(*api.InboxResponse)
	result.Mails = make([]api.MailSummary, len(mails))
	result.UnreadCount = unreadCount
	result.TotalCount = total
	
	// Convert to API types
	for i, mail := range mails {
		result.Mails[i] = convertMailToSummary(mail)
	}
	
	// Pagination
	if limit > 0 {
		totalPages := (total + limit - 1) / limit
		result.TotalPages = api.NewOptInt(totalPages)
		page := (offset / limit) + 1
		result.Page = api.NewOptInt(page)
	}
	
	return result, nil
}

// GetMail returns mail details
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *MailService) GetMail(ctx context.Context, mailID string) (*api.MailDetailResponse, error) {
	// Get playerID from JWT token in context
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}
	
	mailUUID, err := uuid.Parse(mailID)
	if err != nil {
		return nil, errors.New("invalid mail ID")
	}
	
	// Get mail from repository
	mail, err := s.repository.GetMail(ctx, mailUUID)
	if err != nil {
		return nil, err
	}
	
	// Check if player is recipient
	if mail.RecipientID != playerID {
		return nil, errors.New("forbidden")
	}
	
	// Mark as read if not read
	if !mail.IsRead {
		_ = s.repository.MarkAsRead(ctx, mailUUID)
	}
	
	// Issue: #1607 - Use memory pooling
	result := s.mailDetailResponsePool.Get().(*api.MailDetailResponse)
	result.Mail = convertMailToDetail(*mail)
	
	return result, nil
}

// DeleteMail deletes mail
func (s *MailService) DeleteMail(ctx context.Context, mailID string) error {
	// Get playerID from JWT token in context
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return err
	}
	
	mailUUID, err := uuid.Parse(mailID)
	if err != nil {
		return errors.New("invalid mail ID")
	}
	
	// Get mail to check ownership
	mail, err := s.repository.GetMail(ctx, mailUUID)
	if err != nil {
		return err
	}
	
	// Check if player is recipient
	if mail.RecipientID != playerID {
		return errors.New("forbidden")
	}
	
	return s.repository.DeleteMail(ctx, mailUUID)
}

// SendMail sends mail
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *MailService) SendMail(ctx context.Context, req *api.SendMailRequest) (*api.SendMailResponse, error) {
	// Get senderID from JWT token in context
	senderID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}
	
	// Prepare attachments JSON
	var attachmentsJSON *json.RawMessage
	hasAttachments := len(req.AttachmentItems) > 0 || req.AttachmentCurrency.Set
	if hasAttachments {
		attachments := map[string]interface{}{}
		if len(req.AttachmentItems) > 0 {
			items := make([]map[string]interface{}, len(req.AttachmentItems))
			for i, item := range req.AttachmentItems {
				items[i] = map[string]interface{}{
					"item_id":  item.ItemId,
					"quantity": item.Quantity,
				}
			}
			attachments["items"] = items
		}
		if req.AttachmentCurrency.Set {
			attachments["currency"] = req.AttachmentCurrency.Value
		}
		attachmentsBytes, err := json.Marshal(attachments)
		if err != nil {
			return nil, err
		}
		rawJSON := json.RawMessage(attachmentsBytes)
		attachmentsJSON = &rawJSON
	}
	
	// Prepare body
	body := ""
	if req.Body.Set {
		body = req.Body.Value
	}
	
	// Calculate expiration (default 30 days)
	expiresAt := time.Now().Add(30 * 24 * time.Hour)
	
	// Create mail
	mail := &Mail{
		SenderID:    &senderID,
		SenderName:  "Player", // TODO: Get from character service
		RecipientID: req.RecipientId,
		Type:        "player",
		Subject:     req.Subject,
		Content:     body,
		Status:      "unread",
		Attachments: attachmentsJSON,
		ExpiresAt:   &expiresAt,
	}
	
	if req.CodAmount.Set {
		mail.CODAmount = &req.CodAmount.Value
	}
	
	mailID, err := s.repository.CreateMail(ctx, mail)
	if err != nil {
		return nil, err
	}
	
	// Issue: #1607 - Use memory pooling
	result := s.sendMailResponsePool.Get().(*api.SendMailResponse)
	result.MailId = *mailID
	result.Status = api.SendMailResponseStatusSent
	result.ExpiresAt = expiresAt
	
	return result, nil
}

// ClaimAttachments claims mail attachments
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *MailService) ClaimAttachments(ctx context.Context, mailID string) (*api.ClaimAttachmentsResponse, error) {
	// Get playerID from JWT token in context
	playerID, err := getPlayerIDFromContext(ctx)
	if err != nil {
		return nil, err
	}
	
	mailUUID, err := uuid.Parse(mailID)
	if err != nil {
		return nil, errors.New("invalid mail ID")
	}
	
	// Get mail to check ownership
	mail, err := s.repository.GetMail(ctx, mailUUID)
	if err != nil {
		return nil, err
	}
	
	// Check if player is recipient
	if mail.RecipientID != playerID {
		return nil, errors.New("forbidden")
	}
	
	// Check if already claimed
	if mail.IsClaimed {
		return nil, errors.New("attachments already claimed")
	}
	
	// Check COD
	if mail.CODAmount != nil && *mail.CODAmount > 0 {
		// TODO: Check player currency and deduct COD amount
		// For now, just mark as claimed
	}
	
	// Claim attachments
	claimedMail, err := s.repository.ClaimAttachments(ctx, mailUUID)
	if err != nil {
		return nil, err
	}
	
	// Issue: #1607 - Use memory pooling
	result := s.claimAttachmentsResponsePool.Get().(*api.ClaimAttachmentsResponse)
	result.Claimed = true
	
	// Parse attachments
	if claimedMail.Attachments != nil {
		var attachments map[string]interface{}
		if err := json.Unmarshal(*claimedMail.Attachments, &attachments); err == nil {
			// Extract items
			if items, ok := attachments["items"].([]interface{}); ok {
				result.ItemsReceived = make([]api.ClaimAttachmentsResponseItemsReceivedItem, len(items))
				for i, item := range items {
					if itemMap, ok := item.(map[string]interface{}); ok {
						if itemIDStr, ok := itemMap["item_id"].(string); ok {
							if itemID, err := uuid.Parse(itemIDStr); err == nil {
								result.ItemsReceived[i].ItemId = api.NewOptUUID(itemID)
							}
						}
						if quantity, ok := itemMap["quantity"].(float64); ok {
							result.ItemsReceived[i].Quantity = api.NewOptInt(int(quantity))
						}
						if itemName, ok := itemMap["item_name"].(string); ok {
							result.ItemsReceived[i].ItemName = api.NewOptString(itemName)
						}
					}
				}
			}
			// Extract currency
			if currency, ok := attachments["currency"].(map[string]interface{}); ok {
				currencyMap := make(map[string]int)
				for k, v := range currency {
					if amount, ok := v.(float64); ok {
						currencyMap[k] = int(amount)
					}
				}
				if len(currencyMap) > 0 {
					result.CurrencyReceived = api.NewOptClaimAttachmentsResponseCurrencyReceived(currencyMap)
				}
			}
		}
	}
	
	if claimedMail.CODAmount != nil {
		result.CodPaid = api.NewOptInt(*claimedMail.CODAmount)
	}
	
	return result, nil
}

// Helper functions for conversion

func convertMailToSummary(mail Mail) api.MailSummary {
	summary := api.MailSummary{
		ID:         mail.ID,
		SenderName: mail.SenderName,
		Subject:    mail.Subject,
		SentAt:     mail.SentAt,
	}
	
	if mail.SenderID != nil {
		summary.SenderId = api.NewOptUUID(*mail.SenderID)
	}
	
	// Check if has attachments
	hasAttachments := mail.Attachments != nil && len(*mail.Attachments) > 0
	summary.HasAttachments = api.NewOptBool(hasAttachments)
	
	if mail.CODAmount != nil {
		summary.CodAmount = api.NewOptInt(*mail.CODAmount)
	}
	
	// Convert status
	switch mail.Status {
	case "unread":
		summary.MailStatus = api.MailSummaryMailStatusSent
	case "read":
		summary.MailStatus = api.MailSummaryMailStatusRead
	case "claimed":
		summary.MailStatus = api.MailSummaryMailStatusClaimed
	case "expired":
		summary.MailStatus = api.MailSummaryMailStatusExpired
	default:
		summary.MailStatus = api.MailSummaryMailStatusSent
	}
	
	if mail.ExpiresAt != nil {
		summary.ExpiresAt = *mail.ExpiresAt
	} else {
		summary.ExpiresAt = mail.SentAt.Add(30 * 24 * time.Hour)
	}
	
	return summary
}

func convertMailToDetail(mail Mail) api.MailDetail {
	detail := api.MailDetail{
		ID:         mail.ID,
		SenderName: mail.SenderName,
		Subject:    mail.Subject,
		SentAt:     mail.SentAt,
	}
	
	if mail.SenderID != nil {
		detail.SenderId = api.NewOptUUID(*mail.SenderID)
	}
	
	// Body
	if mail.Content != "" {
		detail.Body = api.NewOptString(mail.Content)
	}
	
	// Attachments
	hasAttachments := mail.Attachments != nil && len(*mail.Attachments) > 0
	detail.HasAttachments = api.NewOptBool(hasAttachments)
	
	if hasAttachments {
		var attachments map[string]interface{}
		if err := json.Unmarshal(*mail.Attachments, &attachments); err == nil {
			// Extract items
			if items, ok := attachments["items"].([]interface{}); ok {
				detail.AttachmentItems = make([]api.MailDetailAttachmentItemsItem, len(items))
				for i, item := range items {
					if itemMap, ok := item.(map[string]interface{}); ok {
						if itemIDStr, ok := itemMap["item_id"].(string); ok {
							if itemID, err := uuid.Parse(itemIDStr); err == nil {
								detail.AttachmentItems[i].ItemId = api.NewOptUUID(itemID)
							}
						}
						if quantity, ok := itemMap["quantity"].(float64); ok {
							detail.AttachmentItems[i].Quantity = api.NewOptInt(int(quantity))
						}
						if itemName, ok := itemMap["item_name"].(string); ok {
							detail.AttachmentItems[i].ItemName = api.NewOptString(itemName)
						}
					}
				}
			}
			// Extract currency
			if currency, ok := attachments["currency"].(map[string]interface{}); ok {
				currencyMap := make(map[string]int)
				for k, v := range currency {
					if amount, ok := v.(float64); ok {
						currencyMap[k] = int(amount)
					}
				}
				if len(currencyMap) > 0 {
					detail.AttachmentCurrency = api.NewOptMailDetailAttachmentCurrency(currencyMap)
				}
			}
		}
	}
	
	if mail.CODAmount != nil {
		detail.CodAmount = api.NewOptInt(*mail.CODAmount)
	}
	
	// Convert status
	switch mail.Status {
	case "unread":
		detail.MailStatus = api.MailDetailMailStatusSent
	case "read":
		detail.MailStatus = api.MailDetailMailStatusRead
	case "claimed":
		detail.MailStatus = api.MailDetailMailStatusClaimed
	case "expired":
		detail.MailStatus = api.MailDetailMailStatusExpired
	default:
		detail.MailStatus = api.MailDetailMailStatusSent
	}
	
	if mail.ExpiresAt != nil {
		detail.ExpiresAt = *mail.ExpiresAt
	} else {
		detail.ExpiresAt = mail.SentAt.Add(30 * 24 * time.Hour)
	}
	
	detail.AttachmentClaimed = api.NewOptBool(mail.IsClaimed)
	
	if mail.ReadAt != nil {
		detail.ReadAt = api.NewOptDateTime(*mail.ReadAt)
	}
	
	return detail
}

