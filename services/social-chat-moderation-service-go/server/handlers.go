// Package server Issue: #1598, #1604
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"strings"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-moderation-service-go/pkg/api"
	"github.com/google/uuid"
)

// DBTimeout Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// Memory pools for frequently allocated objects (optimization for hot path)
var (
	filterResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.FilterResponse{}
		},
	}

	warnResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.WarnResponse{}
		},
	}

	autoBanResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.AutoBanResponse{}
		},
	}
)

// ChatModerationHandlers implements api.Handler interface (ogen typed handlers!)
type ChatModerationHandlers struct{}

func NewChatModerationHandlers() *ChatModerationHandlers {
	return &ChatModerationHandlers{}
}

// ReportChatMessage implements POST /social/chat/report - TYPED response!
func (h *ChatModerationHandlers) ReportChatMessage(ctx context.Context, _ *api.ReportMessageRequest) (api.ReportChatMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	reportId := uuid.New()
	success := true

	return &api.ReportResponse{
		ReportID: api.NewOptUUID(reportId),
		Success:  api.NewOptBool(true),
	}, nil
}

// BanChatUser implements POST /social/chat/ban - TYPED response!
func (h *ChatModerationHandlers) BanChatUser(ctx context.Context, req *api.BanUserRequest) (api.BanChatUserRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	banId := uuid.New()
	success := true
	var expiresAt api.OptNilDateTime
	if durationHours, ok := req.DurationHours.Get(); ok {
		exp := time.Now().Add(time.Duration(durationHours) * time.Hour)
		expiresAt = api.NewOptNilDateTime(exp)
	}

	return &api.BanResponse{
		BanID:     api.NewOptUUID(banId),
		Success:   api.NewOptBool(true),
		ExpiresAt: expiresAt,
	}, nil
}

// GetChatBans implements GET /social/chat/bans - TYPED response!
func (h *ChatModerationHandlers) GetChatBans(ctx context.Context, params api.GetChatBansParams) (api.GetChatBansRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	banId1 := uuid.New()
	banId2 := uuid.New()
	playerId1 := uuid.New()
	playerId2 := uuid.New()
	bannedBy1 := uuid.New()
	bannedBy2 := uuid.New()
	now := time.Now()
	durationHours1 := 24
	durationHours2 := 48
	reason1 := "Spam"
	reason2 := "Toxic behavior"

	bans := []api.ChatBan{
		{
			ID:            api.NewOptUUID(banId1),
			PlayerID:      api.NewOptUUID(playerId1),
			BannedBy:      api.NewOptUUID(bannedBy1),
			Reason:        api.NewOptString(reason1),
			DurationHours: api.NewOptNilInt(durationHours1),
			CreatedAt:     api.NewOptDateTime(now),
			ExpiresAt:     api.OptNilDateTime{},
		},
		{
			ID:            api.NewOptUUID(banId2),
			PlayerID:      api.NewOptUUID(playerId2),
			BannedBy:      api.NewOptUUID(bannedBy2),
			Reason:        api.NewOptString(reason2),
			DurationHours: api.NewOptNilInt(durationHours2),
			CreatedAt:     api.NewOptDateTime(now),
			ExpiresAt:     api.OptNilDateTime{},
		},
	}

	total := len(bans)
	limit := 50
	offset := 0

	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	return &api.BanListResponse{
		Bans:   bans,
		Total:  api.NewOptInt(total),
		Limit:  api.NewOptInt(limit),
		Offset: api.NewOptInt(offset),
	}, nil
}

// RevokeChatBan implements DELETE /social/chat/bans/{ban_id} - TYPED response!
func (h *ChatModerationHandlers) RevokeChatBan(ctx context.Context, _ *api.RevokeBanRequest, params api.RevokeChatBanParams) (api.RevokeChatBanRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	success := true

	return &api.RevokeBanResponse{
		BanID:   api.NewOptUUID(params.BanID),
		Success: api.NewOptBool(true),
	}, nil
}

// FilterChatMessage implements POST /social/chat/filter - Message filtering with content analysis
func (h *ChatModerationHandlers) FilterChatMessage(ctx context.Context, req *api.FilterMessageRequest) (api.FilterChatMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	message := req.Message
	allowed := true
	var violations []string
	severity := "low"

	// Basic profanity filter (can be extended with ML models)
	profanityWords := []string{"spam", "toxic", "harass", "cheat"}
	messageLower := strings.ToLower(message) // Convert once for performance

	for _, word := range profanityWords {
		if strings.Contains(messageLower, word) {
			allowed = false
			violations = append(violations, word)
			severity = "high"
			break
		}
	}

	filteredMessage := message
	if !allowed {
		// Simple censoring - replace violations with asterisks
		for _, violation := range violations {
			filteredMessage = strings.ReplaceAll(filteredMessage, violation, strings.Repeat("*", len(violation)))
		}
	}

	// Use memory pool for response object (hot path optimization)
	response := filterResponsePool.Get().(*api.FilterResponse)
	defer filterResponsePool.Put(response)

	// Reset response fields
	*response = api.FilterResponse{
		Allowed:         api.NewOptBool(allowed),
		FilteredMessage: api.NewOptNilString(filteredMessage),
		Violations:      violations,
		Severity:        api.OptFilterResponseSeverity{Value: api.FilterResponseSeverity(severity), Set: true},
	}

	return response, nil
}

// WarnChatUser implements POST /social/chat/warnings - Issue warnings to users
func (h *ChatModerationHandlers) WarnChatUser(ctx context.Context, _ *api.WarnUserRequest) (api.WarnChatUserRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	warningId := uuid.New()
	totalWarnings := 1 // In real implementation, count from DB

	// Use memory pool for response object (hot path optimization)
	response := warnResponsePool.Get().(*api.WarnResponse)
	defer warnResponsePool.Put(response)

	// Reset response fields
	*response = api.WarnResponse{
		Success:       api.NewOptBool(true),
		WarningID:     api.NewOptUUID(warningId),
		TotalWarnings: api.NewOptInt(totalWarnings),
	}

	return response, nil
}

// GetChatWarnings implements GET /social/chat/warnings - Get user warnings list
func (h *ChatModerationHandlers) GetChatWarnings(ctx context.Context, params api.GetChatWarningsParams) (api.GetChatWarningsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	warningId := uuid.New()
	playerId := uuid.New()
	issuedBy := uuid.New()
	now := time.Now()
	severity := "medium"
	reason := "Inappropriate language"

	warnings := []api.ChatWarning{
		{
			ID:        api.NewOptUUID(warningId),
			PlayerID:  api.NewOptUUID(playerId),
			Reason:    api.NewOptString(reason),
			Severity:  api.OptChatWarningSeverity{Value: api.ChatWarningSeverity(severity), Set: true},
			IssuedBy:  api.NewOptUUID(issuedBy),
			CreatedAt: api.NewOptDateTime(now),
		},
	}

	total := len(warnings)
	limit := 50
	offset := 0

	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	return &api.WarningListResponse{
		Warnings: warnings,
		Total:    api.NewOptInt(total),
		Limit:    api.NewOptInt(limit),
		Offset:   api.NewOptInt(offset),
	}, nil
}

// AutoBanUser implements POST /social/chat/auto-ban - Automatic banning based on violations
func (h *ChatModerationHandlers) AutoBanUser(ctx context.Context, req *api.AutoBanRequest) (api.AutoBanUserRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	banId := uuid.New()
	durationHours := 24 // Default auto-ban duration

	// Adjust duration based on trigger type (pre-calculated durations for performance)
	if triggerType, ok := req.TriggerType.Get(); ok {
		switch triggerType {
		case api.AutoBanRequestTriggerTypeSpam:
			durationHours = 12
		case api.AutoBanRequestTriggerTypeToxicity:
			durationHours = 24
		case api.AutoBanRequestTriggerTypeHarassment:
			durationHours = 48
		case api.AutoBanRequestTriggerTypeCheating:
			durationHours = 168 // 1 week
		}
	}

	// Use memory pool for response object (hot path optimization)
	response := autoBanResponsePool.Get().(*api.AutoBanResponse)
	defer autoBanResponsePool.Put(response)

	// Reset response fields
	*response = api.AutoBanResponse{
		Success:       api.NewOptBool(true),
		BanID:         api.NewOptUUID(banId),
		DurationHours: api.NewOptInt(durationHours),
		Reason:        api.NewOptString(req.Reason),
	}

	return response, nil
}

// GetChatReports implements GET /social/chat/reports - Get reports list for moderation
func (h *ChatModerationHandlers) GetChatReports(ctx context.Context, params api.GetChatReportsParams) (api.GetChatReportsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	reportId := uuid.New()
	messageId := uuid.New()
	playerId := uuid.New()
	reportedBy := uuid.New()
	now := time.Now()
	reason := "Spam content"
	status := "pending"

	reports := []api.ChatReport{
		{
			ID:         api.NewOptUUID(reportId),
			MessageID:  api.NewOptUUID(messageId),
			PlayerID:   api.NewOptUUID(playerId),
			ReportedBy: api.NewOptUUID(reportedBy),
			Reason:     api.NewOptString(reason),
			Status:     api.OptChatReportStatus{Value: api.ChatReportStatus(status), Set: true},
			CreatedAt:  api.NewOptDateTime(now),
		},
	}

	// Filter by status if provided
	if params.Status.IsSet() {
		statusFilter := params.Status.Value
		filteredReports := make([]api.ChatReport, 0)
		for _, report := range reports {
			if string(report.Status.Value) == string(statusFilter) {
				filteredReports = append(filteredReports, report)
			}
		}
		reports = filteredReports
	}

	total := len(reports)
	limit := 50
	offset := 0

	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	return &api.ReportListResponse{
		Reports: reports,
		Total:   api.NewOptInt(total),
		Limit:   api.NewOptInt(limit),
		Offset:  api.NewOptInt(offset),
	}, nil
}

// ResolveChatReport implements POST /social/chat/reports/{report_id}/resolve - Resolve user reports
func (h *ChatModerationHandlers) ResolveChatReport(ctx context.Context, req *api.ResolveReportRequest, params api.ResolveChatReportParams) (api.ResolveChatReportRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	action := req.Action

	var banId, warningId api.OptNilUUID

	switch action {
	case api.ResolveReportRequestActionBan:
		// Create ban
		banUUID := uuid.New()
		banId = api.NewOptNilUUID(banUUID)
	case api.ResolveReportRequestActionWarn:
		// Create warning
		warningUUID := uuid.New()
		warningId = api.NewOptNilUUID(warningUUID)
	case api.ResolveReportRequestActionDismiss:
		// Just mark as resolved
	}

	return &api.ResolveReportResponse{
		Success:     api.NewOptBool(true),
		ReportID:    api.NewOptUUID(params.ReportID),
		ActionTaken: api.OptResolveReportResponseActionTaken{Value: api.ResolveReportResponseActionTaken(action), Set: true},
		BanID:       banId,
		WarningID:   warningId,
	}, nil
}
