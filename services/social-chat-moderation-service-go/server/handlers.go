// Issue: #1598, #1604
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/social-chat-moderation-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// ChatModerationHandlers implements api.Handler interface (ogen typed handlers!)
type ChatModerationHandlers struct{}

func NewChatModerationHandlers() *ChatModerationHandlers {
	return &ChatModerationHandlers{}
}

// ReportChatMessage implements POST /social/chat/report - TYPED response!
func (h *ChatModerationHandlers) ReportChatMessage(ctx context.Context, req *api.ReportMessageRequest) (api.ReportChatMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	reportId := uuid.New()
	success := true

	return &api.ReportResponse{
		ReportID: api.NewOptUUID(reportId),
		Success:  api.NewOptBool(success),
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
		Success:   api.NewOptBool(success),
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
func (h *ChatModerationHandlers) RevokeChatBan(ctx context.Context, req *api.RevokeBanRequest, params api.RevokeChatBanParams) (api.RevokeChatBanRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	success := true

	return &api.RevokeBanResponse{
		BanID:   api.NewOptUUID(params.BanID),
		Success: api.NewOptBool(success),
	}, nil
}
