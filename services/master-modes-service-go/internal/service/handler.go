package service

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.uber.org/zap"

	"master-modes-service-go/pkg/api"
)

// Handler реализует OpenAPI Handler интерфейс с оптимизациями для MMOFPS
type Handler struct {
	service *Service
	logger  *zap.Logger
}

// Ensure Handler implements the Handler interface
var _ api.Handler = (*Handler)(nil)

// NewHandler создает новый обработчик запросов
func NewHandler(svc *Service, logger *zap.Logger) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
	}
}

// HealthGet реализует эндпоинт проверки здоровья сервиса
func (h *Handler) HealthGet(ctx context.Context) (api.HealthGetRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.HealthGet")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "health"))

	health := api.Health{
		Status:  "healthy",
		Version: "1.0.0",
		Uptime:  "0h 0m 0s", // В реальной реализации считать uptime
	}

	h.logger.Debug("Health check requested", zap.String("status", health.Status))

	return &health, nil
}

// DifficultyModesGet получает список всех режимов сложности
func (h *Handler) DifficultyModesGet(ctx context.Context, params api.DifficultyModesGetParams) (api.DifficultyModesGetRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.DifficultyModesGet")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "difficulty_modes"))

	// Получаем все режимы сложности
	modes, err := h.service.GetDifficultyManager().GetAllDifficultyModes(ctx)
	if err != nil {
		h.logger.Error("Failed to get difficulty modes", zap.Error(err))
		return &api.DifficultyModesGetInternalServerError{}, nil
	}

	// Конвертируем в API формат
	apiModes := make([]api.DifficultyMode, len(modes))
	for i, mode := range modes {
		apiModes[i] = api.DifficultyMode{
			Id:                  mode.ID.String(),
			Name:                mode.Name,
			Level:               api.DifficultyLevel(mode.Level),
			Description:         mode.Description,
			HpModifier:          mode.HpModifier,
			DamageModifier:      mode.DamageModifier,
			TimeLimitMultiplier: mode.TimeLimitMultiplier,
			RespawnLimit:        mode.RespawnLimit,
			CheckpointLimit:     mode.CheckpointLimit,
			SpecialMechanics:    mode.SpecialMechanics,
			IsActive:            mode.IsActive,
			CreatedAt:           mode.CreatedAt,
			UpdatedAt:           mode.UpdatedAt,
		}
	}

	h.logger.Debug("Retrieved difficulty modes", zap.Int("count", len(apiModes)))

	return &apiModes, nil
}

// DifficultyModesPost создает новый режим сложности (admin only)
func (h *Handler) DifficultyModesPost(ctx context.Context, req api.OptDifficultyModeCreate) (api.DifficultyModesPostRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.DifficultyModesPost")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "difficulty_modes_create"))

	if !req.IsSet() {
		return &api.DifficultyModesPostBadRequest{}, nil
	}

	modeData := req.Value

	// Конвертируем из API формата в внутренний
	mode := &DifficultyMode{
		Name:                modeData.Name,
		Level:               DifficultyLevel(modeData.Level),
		Description:         modeData.Description,
		HpModifier:          modeData.HpModifier,
		DamageModifier:      modeData.DamageModifier,
		TimeLimitMultiplier: modeData.TimeLimitMultiplier,
		RespawnLimit:        modeData.RespawnLimit,
		CheckpointLimit:     modeData.CheckpointLimit,
		SpecialMechanics:    modeData.SpecialMechanics,
		IsActive:            modeData.IsActive,
	}

	createdMode, err := h.service.GetDifficultyManager().CreateDifficultyMode(ctx, mode)
	if err != nil {
		h.logger.Error("Failed to create difficulty mode", zap.Error(err))
		return &api.DifficultyModesPostInternalServerError{}, nil
	}

	// Конвертируем обратно в API формат
	apiMode := api.DifficultyMode{
		Id:                  createdMode.ID.String(),
		Name:                createdMode.Name,
		Level:               api.DifficultyLevel(createdMode.Level),
		Description:         createdMode.Description,
		HpModifier:          createdMode.HpModifier,
		DamageModifier:      createdMode.DamageModifier,
		TimeLimitMultiplier: createdMode.TimeLimitMultiplier,
		RespawnLimit:        createdMode.RespawnLimit,
		CheckpointLimit:     createdMode.CheckpointLimit,
		SpecialMechanics:    createdMode.SpecialMechanics,
		IsActive:            createdMode.IsActive,
		CreatedAt:           createdMode.CreatedAt,
		UpdatedAt:           createdMode.UpdatedAt,
	}

	h.logger.Info("Created difficulty mode",
		zap.String("mode_id", createdMode.ID.String()),
		zap.String("name", createdMode.Name))

	return &apiMode, nil
}

// DifficultyModesModeIDGet получает режим сложности по ID
func (h *Handler) DifficultyModesModeIDGet(ctx context.Context, params api.DifficultyModesModeIDGetParams) (api.DifficultyModesModeIDGetRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.DifficultyModesModeIDGet")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "difficulty_mode_get"),
		attribute.String("mode_id", params.ModeID),
	)

	modeID, err := uuid.Parse(params.ModeID)
	if err != nil {
		h.logger.Warn("Invalid mode ID format", zap.String("mode_id", params.ModeID), zap.Error(err))
		return &api.DifficultyModesModeIDGetBadRequest{}, nil
	}

	mode, err := h.service.GetDifficultyManager().GetDifficultyMode(ctx, modeID)
	if err != nil {
		h.logger.Error("Failed to get difficulty mode", zap.String("mode_id", modeID.String()), zap.Error(err))
		return &api.DifficultyModesModeIDGetInternalServerError{}, nil
	}

	if mode == nil {
		return &api.DifficultyModesModeIDGetNotFound{}, nil
	}

	// Конвертируем в API формат
	apiMode := api.DifficultyMode{
		Id:                  mode.ID.String(),
		Name:                mode.Name,
		Level:               api.DifficultyLevel(mode.Level),
		Description:         mode.Description,
		HpModifier:          mode.HpModifier,
		DamageModifier:      mode.DamageModifier,
		TimeLimitMultiplier: mode.TimeLimitMultiplier,
		RespawnLimit:        mode.RespawnLimit,
		CheckpointLimit:     mode.CheckpointLimit,
		SpecialMechanics:    mode.SpecialMechanics,
		IsActive:            mode.IsActive,
		CreatedAt:           mode.CreatedAt,
		UpdatedAt:           mode.UpdatedAt,
	}

	h.logger.Debug("Retrieved difficulty mode", zap.String("mode_id", modeID.String()))

	return &apiMode, nil
}

// DifficultyModesModeIDPut обновляет режим сложности (admin only)
func (h *Handler) DifficultyModesModeIDPut(ctx context.Context, req api.OptDifficultyModeUpdate, params api.DifficultyModesModeIDPutParams) (api.DifficultyModesModeIDPutRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.DifficultyModesModeIDPut")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "difficulty_mode_update"),
		attribute.String("mode_id", params.ModeID),
	)

	modeID, err := uuid.Parse(params.ModeID)
	if err != nil {
		return &api.DifficultyModesModeIDPutBadRequest{}, nil
	}

	if !req.IsSet() {
		return &api.DifficultyModesModeIDPutBadRequest{}, nil
	}

	modeData := req.Value

	// Конвертируем из API формата в внутренний
	mode := &DifficultyMode{
		ID:                  modeID,
		Name:                modeData.Name,
		Level:               DifficultyLevel(modeData.Level),
		Description:         modeData.Description,
		HpModifier:          modeData.HpModifier,
		DamageModifier:      modeData.DamageModifier,
		TimeLimitMultiplier: modeData.TimeLimitMultiplier,
		RespawnLimit:        modeData.RespawnLimit,
		CheckpointLimit:     modeData.CheckpointLimit,
		SpecialMechanics:    modeData.SpecialMechanics,
		IsActive:            modeData.IsActive,
	}

	updatedMode, err := h.service.GetDifficultyManager().UpdateDifficultyMode(ctx, mode)
	if err != nil {
		h.logger.Error("Failed to update difficulty mode", zap.Error(err))
		return &api.DifficultyModesModeIDPutInternalServerError{}, nil
	}

	// Конвертируем обратно в API формат
	apiMode := api.DifficultyMode{
		Id:                  updatedMode.ID.String(),
		Name:                updatedMode.Name,
		Level:               api.DifficultyLevel(updatedMode.Level),
		Description:         updatedMode.Description,
		HpModifier:          updatedMode.HpModifier,
		DamageModifier:      updatedMode.DamageModifier,
		TimeLimitMultiplier: updatedMode.TimeLimitMultiplier,
		RespawnLimit:        updatedMode.RespawnLimit,
		CheckpointLimit:     updatedMode.CheckpointLimit,
		SpecialMechanics:    updatedMode.SpecialMechanics,
		IsActive:            updatedMode.IsActive,
		CreatedAt:           updatedMode.CreatedAt,
		UpdatedAt:           updatedMode.UpdatedAt,
	}

	h.logger.Info("Updated difficulty mode", zap.String("mode_id", modeID.String()))

	return &apiMode, nil
}

// SessionsPost начинает новую сессию мастер-режима
func (h *Handler) SessionsPost(ctx context.Context, req api.OptDifficultySessionCreate) (api.SessionsPostRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.SessionsPost")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "session_create"))

	if !req.IsSet() {
		return &api.SessionsPostBadRequest{}, nil
	}

	sessionData := req.Value

	modeID, err := uuid.Parse(sessionData.ModeID)
	if err != nil {
		return &api.SessionsPostBadRequest{}, nil
	}

	playerID, err := uuid.Parse(sessionData.PlayerID)
	if err != nil {
		return &api.SessionsPostBadRequest{}, nil
	}

	sessionID, instanceID, err := h.service.GetRestrictionController().StartSession(ctx, modeID, playerID)
	if err != nil {
		h.logger.Error("Failed to start session", zap.Error(err))
		return &api.SessionsPostInternalServerError{}, nil
	}

	// Записываем начало сессии в аналитику
	if err := h.service.GetAnalyticsCollector().RecordSessionStart(ctx, sessionID, instanceID, modeID, playerID); err != nil {
		h.logger.Error("Failed to record session start", zap.Error(err))
		// Не возвращаем ошибку, сессия уже создана
	}

	session := api.DifficultySession{
		Id:         sessionID.String(),
		InstanceID: instanceID.String(),
		ModeID:     sessionData.ModeID,
		PlayerID:   sessionData.PlayerID,
		Status:     "active",
		CreatedAt:  time.Now(),
	}

	h.logger.Info("Started difficulty session",
		zap.String("session_id", sessionID.String()),
		zap.String("instance_id", instanceID.String()),
		zap.String("player_id", playerID.String()))

	return &session, nil
}

// SessionsSessionIDGet получает статус сессии
func (h *Handler) SessionsSessionIDGet(ctx context.Context, params api.SessionsSessionIDGetParams) (api.SessionsSessionIDGetRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.SessionsSessionIDGet")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "session_get"),
		attribute.String("session_id", params.SessionID),
	)

	sessionID, err := uuid.Parse(params.SessionID)
	if err != nil {
		return &api.SessionsSessionIDGetBadRequest{}, nil
	}

	session, err := h.service.GetRestrictionController().GetSession(ctx, sessionID)
	if err != nil {
		h.logger.Error("Failed to get session", zap.Error(err))
		return &api.SessionsSessionIDGetInternalServerError{}, nil
	}

	if session == nil {
		return &api.SessionsSessionIDGetNotFound{}, nil
	}

	// Получаем статистику сессии
	stats, err := h.service.GetAnalyticsCollector().GetSessionStats(ctx, sessionID)
	if err != nil {
		h.logger.Warn("Failed to get session stats", zap.Error(err))
		// Продолжаем без статистики
	}

	apiSession := api.DifficultySession{
		Id:              session.ID.String(),
		InstanceID:      session.InstanceID.String(),
		ModeID:          session.ModeID.String(),
		PlayerID:        session.PlayerID.String(),
		Status:          session.Status,
		Deaths:          0,
		CheckpointsUsed: 0,
		RespawnsUsed:    0,
		CreatedAt:       session.CreatedAt,
	}

	if stats != nil {
		apiSession.Deaths = stats.Deaths
		apiSession.CheckpointsUsed = stats.CheckpointsUsed
		apiSession.RespawnsUsed = stats.RespawnsUsed
		apiSession.Score = &stats.Score
		apiSession.Achievements = &stats.Achievements
	}

	h.logger.Debug("Retrieved session status", zap.String("session_id", sessionID.String()))

	return &apiSession, nil
}

// SessionsSessionIDDelete завершает сессию
func (h *Handler) SessionsSessionIDDelete(ctx context.Context, req api.OptDifficultySessionEnd, params api.SessionsSessionIDDeleteParams) (api.SessionsSessionIDDeleteRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.SessionsSessionIDDelete")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "session_end"),
		attribute.String("session_id", params.SessionID),
	)

	sessionID, err := uuid.Parse(params.SessionID)
	if err != nil {
		return &api.SessionsSessionIDDeleteBadRequest{}, nil
	}

	var status string
	var score int64
	var timeLeft time.Duration

	if req.IsSet() {
		endData := req.Value
		status = endData.Status
		if endData.Score.IsSet() {
			score = endData.Score.Value
		}
		if endData.TimeLeft.IsSet() {
			timeLeft = time.Duration(endData.TimeLeft.Value) * time.Second
		}
	} else {
		status = "abandoned"
	}

	// Записываем окончание сессии в аналитику
	if err := h.service.GetAnalyticsCollector().RecordSessionEnd(ctx, sessionID, status, score, timeLeft); err != nil {
		h.logger.Error("Failed to record session end", zap.Error(err))
		// Продолжаем
	}

	// Завершаем сессию
	if err := h.service.GetRestrictionController().EndSession(ctx, sessionID, status); err != nil {
		h.logger.Error("Failed to end session", zap.Error(err))
		return &api.SessionsSessionIDDeleteInternalServerError{}, nil
	}

	h.logger.Info("Ended difficulty session",
		zap.String("session_id", sessionID.String()),
		zap.String("status", status))

	return &api.SessionsSessionIDDeleteNoContent{}, nil
}

// AnalyticsGlobalGet получает глобальную аналитику
func (h *Handler) AnalyticsGlobalGet(ctx context.Context) (api.AnalyticsGlobalGetRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.AnalyticsGlobalGet")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "analytics_global"))

	stats, err := h.service.GetAnalyticsCollector().GetGlobalStats(ctx)
	if err != nil {
		h.logger.Error("Failed to get global stats", zap.Error(err))
		return &api.AnalyticsGlobalGetInternalServerError{}, nil
	}

	// Конвертируем в API формат
	analytics := api.GlobalAnalytics{
		TotalSessions:           int(stats["total_sessions"].(int)),
		CompletedSessions:       int(stats["completed_sessions"].(int)),
		CompletionRate:          stats["completion_rate"].(float64),
		AverageSessionTime:      stats["average_session_time"].(float64),
		MostPopularMode:         stats["most_popular_mode"].(string),
		HighestScore:            int64(stats["highest_score"].(int)),
		TotalDeaths:             int(stats["total_deaths"].(int)),
		AverageDeathsPerSession: stats["average_deaths_per_session"].(float64),
		UniquePlayers:           int(stats["unique_players"].(int)),
	}

	h.logger.Debug("Retrieved global analytics")

	return &analytics, nil
}

// AnalyticsModesModeIDGet получает аналитику по режиму
func (h *Handler) AnalyticsModesModeIDGet(ctx context.Context, params api.AnalyticsModesModeIDGetParams) (api.AnalyticsModesModeIDGetRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.AnalyticsModesModeIDGet")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "analytics_mode"),
		attribute.String("mode_id", params.ModeID),
	)

	modeID, err := uuid.Parse(params.ModeID)
	if err != nil {
		return &api.AnalyticsModesModeIDGetBadRequest{}, nil
	}

	stats, err := h.service.GetAnalyticsCollector().GetModeStats(ctx, modeID)
	if err != nil {
		h.logger.Error("Failed to get mode stats", zap.Error(err))
		return &api.AnalyticsModesModeIDGetInternalServerError{}, nil
	}

	// Конвертируем в API формат
	modeAnalytics := api.ModeAnalytics{
		ModeID:               params.ModeID,
		TotalSessions:        int(stats["total_sessions"].(int)),
		CompletedSessions:    int(stats["completed_sessions"].(int)),
		CompletionRate:       stats["completion_rate"].(float64),
		AverageSessionTime:   stats["average_session_time"].(float64),
		AverageDeaths:        stats["average_deaths"].(float64),
		AverageScore:         stats["average_score"].(float64),
		BestScore:            int64(stats["best_score"].(int)),
		MostCommonFailure:    stats["most_common_failure"].(string),
	}

	h.logger.Debug("Retrieved mode analytics", zap.String("mode_id", modeID.String()))

	return &modeAnalytics, nil
}
