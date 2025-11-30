// Issue: #141888033
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) CreateBan(ctx context.Context, adminID uuid.UUID, req *models.CreateBanRequest) (*models.ChatBan, error) {
	return s.moderationService.CreateBan(ctx, adminID, req)
}

func (s *SocialService) GetBans(ctx context.Context, characterID *uuid.UUID, limit, offset int) (*models.BanListResponse, error) {
	return s.moderationService.GetBans(ctx, characterID, limit, offset)
}

func (s *SocialService) RemoveBan(ctx context.Context, banID uuid.UUID) error {
	return s.moderationService.RemoveBan(ctx, banID)
}

func (s *SocialService) CreateReport(ctx context.Context, reporterID uuid.UUID, req *models.CreateReportRequest) (*models.ChatReport, error) {
	return s.moderationService.CreateReport(ctx, reporterID, req)
}

func (s *SocialService) GetReports(ctx context.Context, status *string, limit, offset int) ([]models.ChatReport, int, error) {
	return s.moderationService.GetReports(ctx, status, limit, offset)
}

func (s *SocialService) ResolveReport(ctx context.Context, reportID uuid.UUID, adminID uuid.UUID, status string) error {
	return s.moderationService.ResolveReport(ctx, reportID, adminID, status)
}

