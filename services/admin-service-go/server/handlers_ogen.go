// Package server Issue: #1602 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/go-faster/jx"
	"github.com/necpgame/admin-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// AdminHandlers implements api.Handler interface (ogen typed handlers!)
type AdminHandlers struct {
	adminService AdminServiceInterface
	logger       *logrus.Logger
}

// NewAdminHandlers creates new handlers
func NewAdminHandlers(adminService AdminServiceInterface, logger *logrus.Logger) *AdminHandlers {
	return &AdminHandlers{
		adminService: adminService,
		logger:       logger,
	}
}

// GetDashboard implements getDashboard operation - TYPED response!
func (h *AdminHandlers) GetDashboard(ctx context.Context) (api.GetDashboardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get analytics data
	analytics, err := h.adminService.GetAnalytics(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get analytics")
		return &api.GetDashboardForbidden{}, err
	}

	// Get recent audit logs
	auditLogs, err := h.adminService.GetAuditLogs(ctx, nil, nil, 10, 0)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get audit logs")
		return &api.GetDashboardForbidden{}, err
	}

	// Convert audit logs to ogen types
	recentActions := make([]api.AuditLog, 0, len(auditLogs.Logs))
	for _, log := range auditLogs.Logs {
		var targetID api.OptUUID
		if log.TargetID != nil {
			targetID = api.NewOptUUID(*log.TargetID)
		}

		recentActions = append(recentActions, api.AuditLog{
			ID:         api.NewOptUUID(log.ID),
			AdminID:    api.NewOptUUID(log.AdminID),
			TargetID:   targetID,
			ActionType: api.NewOptString(string(log.ActionType)),
			IPAddress:  api.NewOptString(log.IPAddress),
			UserAgent:  api.NewOptString(log.UserAgent),
			TargetType: api.NewOptAuditLogTargetType(api.AuditLogTargetType(log.TargetType)),
			CreatedAt:  api.NewOptDateTime(log.CreatedAt),
			Details:    api.NewOptAuditLogDetails(convertDetails(log.Details)),
		})
	}

	// Build dashboard data
	// Note: AnalyticsResponse may not have all fields, use defaults if missing
	dashboard := &api.DashboardData{
		RecentActions: recentActions,
		OnlinePlayers: api.NewOptInt(analytics.OnlinePlayers),
		// TotalPlayers, ActiveMatches, PendingReports may not be in AnalyticsResponse
		// Use defaults or extract from metrics if available
		TotalPlayers:   api.NewOptInt(0), // TODO: Extract from analytics if available
		ActiveMatches:  api.NewOptInt(0), // TODO: Extract from analytics if available
		PendingReports: api.NewOptInt(0), // TODO: Extract from analytics if available
	}

	return dashboard, nil
}

// convertDetails converts map[string]interface{} to api.AuditLogDetails
func convertDetails(details map[string]interface{}) api.AuditLogDetails {
	result := make(map[string]jx.Raw, len(details))
	for k, v := range details {
		// Convert value to JSON using encoding/json, then wrap in jx.Raw
		detailsJSON, err := json.Marshal(v)
		if err == nil {
			result[k] = detailsJSON
		}
	}
	return result
}
