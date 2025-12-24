// API handlers for Announcement Service
// Issue: #323
// PERFORMANCE: Memory pooling for response objects, optimized for high-throughput

package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Memory pools for response objects to reduce allocations
var (
	announcementResponsePool = &sync.Pool{
		New: func() interface{} {
			return &api.Announcement{}
		},
	}
	announcementListResponsePool = &sync.Pool{
		New: func() interface{} {
			return &api.AnnouncementListResponse{}
		},
	}
)

// GetAnnouncements implements getAnnouncements operation.
//
// Получить список объявлений
//
// GET /announcements
func (h *Handler) GetAnnouncements(ctx context.Context, params api.GetAnnouncementsParams) (api.GetAnnouncementsRes, error) {
	h.logger.Info("GetAnnouncements called", zap.Any("params", params))

	announcements, total, err := h.service.GetAnnouncements(ctx, params)
	if err != nil {
		h.logger.Error("Failed to get announcements", zap.Error(err))
		return api.NewGetAnnouncementsInternalServerError(api.Error{
			Error:   api.NewOptString("Internal Server Error"),
			Message: api.NewOptString(err.Error()),
		}), nil
	}

	response := announcementListResponsePool.Get().(*api.AnnouncementListResponse)
	defer announcementListResponsePool.Put(response)

	response.Announcements = announcements
	response.Total = api.NewOptInt(total)

	return api.NewGetAnnouncementsOK(*response), nil
}

// GetAnnouncement implements getAnnouncement operation.
//
// Получить объявление
//
// GET /announcements/{announcementId}
func (h *Handler) GetAnnouncement(ctx context.Context, params api.GetAnnouncementParams) (api.GetAnnouncementRes, error) {
	h.logger.Info("GetAnnouncement called", zap.String("announcementId", params.AnnouncementId.String()))

	announcement, err := h.service.GetAnnouncement(ctx, params.AnnouncementId)
	if err != nil {
		h.logger.Error("Failed to get announcement", zap.String("id", params.AnnouncementId.String()), zap.Error(err))
		return api.NewGetAnnouncementNotFound(api.Error{
			Error:   api.NewOptString("Not Found"),
			Message: api.NewOptString("Announcement not found"),
		}), nil
	}

	return api.NewGetAnnouncementOK(*announcement), nil
}

// CreateAnnouncement implements createAnnouncement operation.
//
// Создать объявление (admin)
//
// POST /liveops/announcements
func (h *Handler) CreateAnnouncement(ctx context.Context, req *api.CreateAnnouncementRequest) (api.CreateAnnouncementRes, error) {
	h.logger.Info("CreateAnnouncement called", zap.String("title", req.Title))

	announcement, err := h.service.CreateAnnouncement(ctx, req)
	if err != nil {
		h.logger.Error("Failed to create announcement", zap.Error(err))
		return api.NewCreateAnnouncementBadRequest(api.Error{
			Error:   api.NewOptString("Validation Error"),
			Message: api.NewOptString(err.Error()),
		}), nil
	}

	return api.NewCreateAnnouncementCreated(*announcement), nil
}

// UpdateAnnouncement implements updateAnnouncement operation.
//
// Обновить объявление (admin)
//
// PUT /liveops/announcements/{announcement_id}
func (h *Handler) UpdateAnnouncement(ctx context.Context, params api.UpdateAnnouncementParams, req *api.UpdateAnnouncementRequest) (api.UpdateAnnouncementRes, error) {
	h.logger.Info("UpdateAnnouncement called", zap.String("announcementId", params.AnnouncementId.String()))

	announcement, err := h.service.UpdateAnnouncement(ctx, params.AnnouncementId, req)
	if err != nil {
		h.logger.Error("Failed to update announcement", zap.String("id", params.AnnouncementId.String()), zap.Error(err))
		if err.Error() == "not found" {
			return api.NewUpdateAnnouncementNotFound(api.Error{
				Error:   api.NewOptString("Not Found"),
				Message: api.NewOptString("Announcement not found"),
			}), nil
		}
		return api.NewUpdateAnnouncementBadRequest(api.Error{
			Error:   api.NewOptString("Validation Error"),
			Message: api.NewOptString(err.Error()),
		}), nil
	}

	return api.NewUpdateAnnouncementOK(*announcement), nil
}

// DeleteAnnouncement implements deleteAnnouncement operation.
//
// Удалить объявление (admin)
//
// DELETE /liveops/announcements/{announcement_id}
func (h *Handler) DeleteAnnouncement(ctx context.Context, params api.DeleteAnnouncementParams) (api.DeleteAnnouncementRes, error) {
	h.logger.Info("DeleteAnnouncement called", zap.String("announcementId", params.AnnouncementId.String()))

	err := h.service.DeleteAnnouncement(ctx, params.AnnouncementId)
	if err != nil {
		h.logger.Error("Failed to delete announcement", zap.String("id", params.AnnouncementId.String()), zap.Error(err))
		if err.Error() == "not found" {
			return api.NewDeleteAnnouncementNotFound(api.Error{
				Error:   api.NewOptString("Not Found"),
				Message: api.NewOptString("Announcement not found"),
			}), nil
		}
		return api.NewDeleteAnnouncementInternalServerError(api.Error{
			Error:   api.NewOptString("Internal Server Error"),
			Message: api.NewOptString(err.Error()),
		}), nil
	}

	return api.NewDeleteAnnouncementNoContent(), nil
}

// PublishAnnouncement implements publishAnnouncement operation.
//
// Опубликовать объявление (admin)
//
// POST /liveops/announcements/{announcement_id}/publish
func (h *Handler) PublishAnnouncement(ctx context.Context, params api.PublishAnnouncementParams) (api.PublishAnnouncementRes, error) {
	h.logger.Info("PublishAnnouncement called", zap.String("announcementId", params.AnnouncementId.String()))

	announcement, err := h.service.PublishAnnouncement(ctx, params.AnnouncementId)
	if err != nil {
		h.logger.Error("Failed to publish announcement", zap.String("id", params.AnnouncementId.String()), zap.Error(err))
		if err.Error() == "not found" {
			return api.NewPublishAnnouncementNotFound(api.Error{
				Error:   api.NewOptString("Not Found"),
				Message: api.NewOptString("Announcement not found"),
			}), nil
		}
		return api.NewPublishAnnouncementInternalServerError(api.Error{
			Error:   api.NewOptString("Internal Server Error"),
			Message: api.NewOptString(err.Error()),
		}), nil
	}

	return api.NewPublishAnnouncementOK(*announcement), nil
}

// ScheduleAnnouncement implements scheduleAnnouncement operation.
//
// Запланировать публикацию объявления (admin)
//
// POST /liveops/announcements/{announcement_id}/schedule
func (h *Handler) ScheduleAnnouncement(ctx context.Context, params api.ScheduleAnnouncementParams, req *api.ScheduleAnnouncementRequest) (api.ScheduleAnnouncementRes, error) {
	h.logger.Info("ScheduleAnnouncement called", zap.String("announcementId", params.AnnouncementId.String()))

	announcement, err := h.service.ScheduleAnnouncement(ctx, params.AnnouncementId, req)
	if err != nil {
		h.logger.Error("Failed to schedule announcement", zap.String("id", params.AnnouncementId.String()), zap.Error(err))
		if err.Error() == "not found" {
			return api.NewScheduleAnnouncementNotFound(api.Error{
				Error:   api.NewOptString("Not Found"),
				Message: api.NewOptString("Announcement not found"),
			}), nil
		}
		return api.NewScheduleAnnouncementBadRequest(api.Error{
			Error:   api.NewOptString("Validation Error"),
			Message: api.NewOptString(err.Error()),
		}), nil
	}

	return api.NewScheduleAnnouncementOK(*announcement), nil
}
