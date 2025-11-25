package server

import (
	"time"

	"github.com/necpgame/reset-service-go/models"
	"github.com/necpgame/reset-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func toAPIResetStats(stats *models.ResetStats) *api.ResetStats {
	if stats == nil {
		return nil
	}

	return &api.ResetStats{
		LastDailyReset:  stats.LastDailyReset,
		LastWeeklyReset: stats.LastWeeklyReset,
		NextDailyReset:  timePtr(stats.NextDailyReset),
		NextWeeklyReset: timePtr(stats.NextWeeklyReset),
	}
}

func toAPIResetListResponse(response *models.ResetListResponse, limit, offset int) *api.ResetListResponse {
	if response == nil {
		return nil
	}

	items := make([]api.ResetRecord, len(response.Resets))
	for i, record := range response.Resets {
		items[i] = toAPIResetRecord(record)
	}

	hasMore := false
	if offset+len(response.Resets) < response.Total {
		hasMore = true
	}

	return &api.ResetListResponse{
		Items:   items,
		Total:   response.Total,
		HasMore: boolPtr(hasMore),
		Limit:   intPtr(limit),
		Offset:  intPtr(offset),
	}
}

func toAPIResetRecord(record models.ResetRecord) api.ResetRecord {
	uuid := openapi_types.UUID(record.ID)
	
	apiRecord := api.ResetRecord{
		Id:          &uuid,
		Type:        resetTypeToAPI(record.Type),
		Status:      resetStatusToAPI(record.Status),
		StartedAt:   timePtr(record.StartedAt),
		CompletedAt: record.CompletedAt,
		Error:       record.Error,
	}

	if record.Metadata != nil {
		apiRecord.Metadata = &record.Metadata
	}

	return apiRecord
}

func resetTypeToAPI(rt models.ResetType) *api.ResetType {
	apiType := api.ResetType(rt)
	return &apiType
}

func resetStatusToAPI(rs models.ResetStatus) *api.ResetStatus {
	apiStatus := api.ResetStatus(rs)
	return &apiStatus
}

func timePtr(t time.Time) *time.Time {
	return &t
}

func boolPtr(b bool) *bool {
	return &b
}

func intPtr(i int) *int {
	return &i
}

