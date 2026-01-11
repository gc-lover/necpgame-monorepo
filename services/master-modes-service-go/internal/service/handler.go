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

// CreateDifficultyMode создает новый режим сложности (admin only)
func (h *Handler) CreateDifficultyMode(ctx context.Context, req *api.CreateDifficultyModeRequest) (api.CreateDifficultyModeRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.CreateDifficultyMode")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "create_difficulty_mode"))

	// Конвертируем из API формата в внутренний
	description := ""
	if req.Description.IsSet() {
		description = req.Description.Value
	}

	specialMechanics := make([]string, len(req.SpecialMechanics))
	for i, item := range req.SpecialMechanics {
		specialMechanics[i] = string(item)
	}

	mode := &DifficultyMode{
		Name:                req.Name,
		Level:               DifficultyLevel(req.Level),
		Description:         description,
		HpModifier:          float64(req.HpModifier),
		DamageModifier:      float64(req.DamageModifier),
		TimeLimitMultiplier: float64(req.TimeLimitMultiplier),
		RespawnLimit:        req.RespawnLimit,
		CheckpointLimit:     req.CheckpointLimit,
		SpecialMechanics:    specialMechanics,
		IsActive:            true, // По умолчанию активен
	}

	err := h.service.GetDifficultyManager().CreateDifficultyMode(ctx, mode)
	if err != nil {
		h.logger.Error("Failed to create difficulty mode", zap.Error(err))
		return &api.CreateDifficultyModeInternalServerError{}, nil
	}

	// Конвертируем обратно в API формат
	apiMode := api.DifficultyMode{
		ID:             mode.ID,
		Name:           mode.Name,
		Level:          api.DifficultyModeLevel(mode.Level),
		Description:    api.NewOptString(mode.Description),
		HpModifier:     float32(mode.HpModifier),
		DamageModifier: float32(mode.DamageModifier),
		TimeLimitMultiplier: float32(mode.TimeLimitMultiplier),
		RespawnLimit:   mode.RespawnLimit,
		CheckpointLimit: mode.CheckpointLimit,
		RewardModifier: float32(mode.RewardModifier),
	}

	h.logger.Info("Created difficulty mode",
		zap.String("mode_id", mode.ID.String()),
		zap.String("name", mode.Name))

	return &apiMode, nil
}

// GetContentDifficultyModes возвращает режимы сложности доступные для конкретного контента
func (h *Handler) GetContentDifficultyModes(ctx context.Context, params api.GetContentDifficultyModesParams) (api.GetContentDifficultyModesRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetContentDifficultyModes")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "get_content_difficulty_modes"),
		attribute.String("content_id", params.ContentId.String()),
	)

	contentID := params.ContentId

	modes, err := h.service.GetDifficultyManager().GetContentDifficultyModes(ctx, contentID)
	if err != nil {
		h.logger.Error("Failed to get content difficulty modes", zap.Error(err))
		return &api.GetContentDifficultyModesInternalServerError{}, nil
	}

	// Конвертируем в API формат
	apiModes := make([]api.ContentDifficultyMode, len(modes))
	for i, mode := range modes {
		var unlockDate api.OptDateTime
		if mode.UnlockDate != nil {
			unlockDate = api.NewOptDateTime(*mode.UnlockDate)
		}

		apiModes[i] = api.ContentDifficultyMode{
			ContentId:  mode.ContentID,
			ModeId:     mode.ModeID,
			IsEnabled:  mode.IsEnabled,
			UnlockDate: unlockDate,
		}
	}

	h.logger.Debug("Retrieved content difficulty modes",
		zap.String("content_id", contentID.String()),
		zap.Int("count", len(apiModes)))

	return &api.GetContentDifficultyModesOK{
		ContentId:      api.OptUUID{Value: contentID, Set: true},
		AvailableModes: apiModes,
	}, nil
}

// GetDifficultyMode возвращает подробную информацию о конкретном режиме сложности
func (h *Handler) GetDifficultyMode(ctx context.Context, params api.GetDifficultyModeParams) (api.GetDifficultyModeRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetDifficultyMode")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "get_difficulty_mode"),
		attribute.String("mode_id", params.ModeId.String()),
	)

	modeID := params.ModeId

	mode, err := h.service.GetDifficultyManager().GetDifficultyMode(ctx, modeID)
	if err != nil {
		h.logger.Error("Failed to get difficulty mode", zap.String("mode_id", modeID.String()), zap.Error(err))
		return &api.GetDifficultyModeInternalServerError{}, nil
	}

	if mode == nil {
		return &api.GetDifficultyModeNotFound{}, nil
	}

	// Конвертируем в API формат
	apiMode := api.DifficultyMode{
		ID:             mode.ID,
		Name:           mode.Name,
		Level:          api.DifficultyModeLevel(mode.Level),
		Description:    api.NewOptString(mode.Description),
		HpModifier:     float32(mode.HpModifier),
		DamageModifier: float32(mode.DamageModifier),
		TimeLimitMultiplier: float32(mode.TimeLimitMultiplier),
		RespawnLimit:   mode.RespawnLimit,
		CheckpointLimit: mode.CheckpointLimit,
		RewardModifier: float32(mode.RewardModifier),
	}

	h.logger.Debug("Retrieved difficulty mode", zap.String("mode_id", modeID.String()))

	return &apiMode, nil
}

// GetDifficultyModeRequirements возвращает требования для разблокировки режима сложности
func (h *Handler) GetDifficultyModeRequirements(ctx context.Context, params api.GetDifficultyModeRequirementsParams) (api.GetDifficultyModeRequirementsRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetDifficultyModeRequirements")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "get_difficulty_mode_requirements"),
		attribute.String("mode_id", params.ModeId.String()),
	)

	modeID := params.ModeId

	requirements, err := h.service.GetDifficultyManager().GetModeRequirements(ctx, modeID)
	if err != nil {
		h.logger.Error("Failed to get mode requirements", zap.String("mode_id", modeID.String()), zap.Error(err))
		return &api.GetDifficultyModeRequirementsInternalServerError{}, nil
	}

	// Конвертируем в API формат
	apiRequirements := api.DifficultyRequirements{
		MinLevel:          requirements.MinLevel,
		MinSkillRating:    requirements.MinSkillRating,
		CompletedMissions: requirements.CompletedMissions,
		ReputationLevel:   api.OptDifficultyRequirementsReputationLevel{Value: api.DifficultyRequirementsReputationLevel(requirements.ReputationLevel), Set: true},
		PrerequisiteModes: requirements.PrerequisiteModes,
	}

	h.logger.Debug("Retrieved difficulty mode requirements", zap.String("mode_id", modeID.String()))

	return &apiRequirements, nil
}

// GetDifficultyModeStats возвращает детальную статистику по выбранному режиму сложности
func (h *Handler) GetDifficultyModeStats(ctx context.Context, params api.GetDifficultyModeStatsParams) (api.GetDifficultyModeStatsRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetDifficultyModeStats")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "get_difficulty_mode_stats"),
		attribute.String("mode_id", params.ModeId.String()),
	)

	modeID := params.ModeId

	stats, err := h.service.GetAnalyticsCollector().GetModeStats(ctx, modeID)
	if err != nil {
		h.logger.Error("Failed to get mode stats", zap.Error(err))
		return &api.GetDifficultyModeStatsInternalServerError{}, nil
	}

	// Получаем топ игроков для этого режима
	topPlayersData, err := h.service.GetAnalyticsCollector().GetTopPlayersForMode(ctx, modeID, 5)
	if err != nil {
		h.logger.Warn("Failed to get top players for mode", zap.Error(err))
		topPlayersData = []PlayerStats{} // Fallback to empty array
	}

	// Конвертируем топ игроков в API формат
	topPlayers := make([]api.PlayerStats, len(topPlayersData))
	for i, player := range topPlayersData {
		topPlayers[i] = api.PlayerStats{
			PlayerID:      player.PlayerID,
			PlayerName:    player.PlayerName,
			Score:         player.Score,
			CompletionTime: api.OptInt{Value: player.CompletionTime, Set: true},
			Rank:          api.OptInt{Value: player.Rank, Set: true},
			Achievements:  player.Achievements,
		}
	}

	// Конвертируем в API формат
	modeStats := api.DifficultyModeStats{
		ModeId: modeID,
		TotalSessions: int(stats["total_sessions"].(int)),
		CompletedSessions: int(stats["completed_sessions"].(int)),
		FailedSessions: int(stats["failed_sessions"].(int)),
		CompletionRate:        float32(stats["completion_rate"].(float64)),
		AverageCompletionTime: api.OptInt{Value: int(stats["average_completion_time"].(float64)), Set: true},
		BestScore:             api.OptInt{Value: stats["best_score"].(int), Set: true},
		AverageScore:          api.OptFloat32{Value: float32(stats["average_score"].(float64)), Set: true},
		TopPlayers: topPlayers,
	}

	h.logger.Debug("Retrieved difficulty mode stats", zap.String("mode_id", modeID.String()))

	return &modeStats, nil
}

// GetDifficultyModes возвращает все доступные режимы сложности с их параметрами
func (h *Handler) GetDifficultyModes(ctx context.Context) (api.GetDifficultyModesRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetDifficultyModes")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "get_difficulty_modes"))

	// Получаем все режимы сложности
	modes, err := h.service.GetDifficultyManager().GetAllDifficultyModes(ctx, 100, 0)
	if err != nil {
		h.logger.Error("Failed to get difficulty modes", zap.Error(err))
		return &api.Error{}, nil
	}

	// Конвертируем в API формат
	apiModes := make([]api.DifficultyMode, len(modes))
	for i, mode := range modes {
		apiModes[i] = api.DifficultyMode{
			ID:             mode.ID,
			Name:           mode.Name,
			Level:          api.DifficultyModeLevel(mode.Level),
			Description:    api.NewOptString(mode.Description),
			HpModifier:     float32(mode.HpModifier),
			DamageModifier: float32(mode.DamageModifier),
			TimeLimitMultiplier: float32(mode.TimeLimitMultiplier),
			RespawnLimit:   mode.RespawnLimit,
			CheckpointLimit: mode.CheckpointLimit,
			RewardModifier: float32(mode.RewardModifier),
		}
	}

	h.logger.Debug("Retrieved difficulty modes", zap.Int("count", len(apiModes)))

	return &api.GetDifficultyModesOK{Modes: apiModes}, nil
}

// GetDifficultyModesStats возвращает глобальную статистику по всем режимам сложности
func (h *Handler) GetDifficultyModesStats(ctx context.Context, params api.GetDifficultyModesStatsParams) (api.GetDifficultyModesStatsRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetDifficultyModesStats")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "get_difficulty_modes_stats"))

	stats, err := h.service.GetAnalyticsCollector().GetGlobalStats(ctx)
	if err != nil {
		h.logger.Error("Failed to get global stats", zap.Error(err))
		return &api.Error{}, nil
	}

	// Получаем популярные режимы
	popularModesData, err := h.service.GetAnalyticsCollector().GetPopularModes(ctx, 5)
	if err != nil {
		h.logger.Warn("Failed to get popular modes", zap.Error(err))
		popularModesData = []ModeStats{} // Fallback to empty array
	}

	// Конвертируем популярные режимы в API формат
	popularModes := make([]api.ModeStats, len(popularModesData))
	for i, mode := range popularModesData {
		popularModes[i] = api.ModeStats{
			ModeID:                mode.ModeID,
			ModeName:              mode.ModeName,
			Popularity:            float32(mode.Popularity),
			TotalPlayers:          api.OptInt{Value: mode.TotalPlayers, Set: true},
			AverageCompletionRate: api.OptFloat32{Value: float32(mode.AverageCompletionRate), Set: true},
			DifficultyRating:      api.OptFloat32{Value: float32(mode.DifficultyRating), Set: true},
		}
	}

	// Получаем статистику по всем режимам
	allModes, err := h.service.GetDifficultyManager().GetAllDifficultyModes(ctx, 100, 0)
	if err != nil {
		h.logger.Warn("Failed to get all difficulty modes for stats", zap.Error(err))
		allModes = []*DifficultyMode{} // Fallback to empty array
	}

	// Конвертируем статистику режимов в API формат
	modeStats := make([]api.DifficultyModeStats, len(allModes))
	for i, mode := range allModes {
		// Получаем статистику для каждого режима
		modeStatsData, err := h.service.GetAnalyticsCollector().GetModeStats(ctx, mode.ID)
		if err != nil {
			h.logger.Warn("Failed to get stats for mode", zap.String("mode_id", mode.ID.String()), zap.Error(err))
			continue
		}

		// Получаем топ игроков для этого режима
		topPlayersData, err := h.service.GetAnalyticsCollector().GetTopPlayersForMode(ctx, mode.ID, 3)
		if err != nil {
			h.logger.Warn("Failed to get top players for mode", zap.String("mode_id", mode.ID.String()), zap.Error(err))
			topPlayersData = []PlayerStats{}
		}

		// Конвертируем топ игроков
		topPlayers := make([]api.PlayerStats, len(topPlayersData))
		for j, player := range topPlayersData {
			topPlayers[j] = api.PlayerStats{
				PlayerID:       player.PlayerID,
				PlayerName:     player.PlayerName,
				Score:          player.Score,
				CompletionTime: api.OptInt{Value: player.CompletionTime, Set: true},
				Rank:           api.OptInt{Value: player.Rank, Set: true},
				Achievements:   player.Achievements,
			}
		}

		modeStats[i] = api.DifficultyModeStats{
			ModeId:                mode.ID,
			ModeName:              mode.Name,
			TotalSessions:         modeStatsData["total_sessions"].(int),
			CompletedSessions:     modeStatsData["completed_sessions"].(int),
			FailedSessions:        modeStatsData["failed_sessions"].(int),
			CompletionRate:        float32(modeStatsData["completion_rate"].(float64)),
			AverageCompletionTime: api.OptInt{Value: int(modeStatsData["average_completion_time"].(float64)), Set: true},
			BestScore:             api.OptInt{Value: modeStatsData["best_score"].(int), Set: true},
			AverageScore:          api.OptFloat32{Value: float32(modeStatsData["average_score"].(float64)), Set: true},
			TopPlayers:            topPlayers,
		}
	}

	// Конвертируем в API формат
	globalStats := api.DifficultyStats{
		TotalSessions:  int(stats["total_sessions"].(int)),
		CompletionRate: float32(stats["completion_rate"].(float64)),
		AverageScore:   float32(stats["average_score"].(float64)),
		PopularModes:   popularModes,
		ModeStats:      modeStats,
	}

	h.logger.Debug("Retrieved global difficulty stats")

	return &globalStats, nil
}

// GetInstanceDifficulty получает текущий режим сложности для игровой сессии
func (h *Handler) GetInstanceDifficulty(ctx context.Context, params api.GetInstanceDifficultyParams) (api.GetInstanceDifficultyRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.GetInstanceDifficulty")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "get_instance_difficulty"),
		attribute.String("instance_id", params.InstanceId.String()),
	)

	instanceID := params.InstanceId

	difficulty, err := h.service.GetRestrictionController().GetInstanceDifficulty(ctx, instanceID)
	if err != nil {
		h.logger.Error("Failed to get instance difficulty", zap.String("instance_id", instanceID.String()), zap.Error(err))
		return &api.GetInstanceDifficultyInternalServerError{}, nil
	}

	if difficulty == nil {
		return &api.GetInstanceDifficultyNotFound{}, nil
	}

	// Конвертируем в API формат
	apiDifficulty := api.DifficultySession{
		ID:         uuid.New(), // Generate session ID
		InstanceId: instanceID,
		ModeId:     difficulty.ID,
	}

	h.logger.Debug("Retrieved instance difficulty", zap.String("instance_id", instanceID.String()))

	return &apiDifficulty, nil
}

// SelectInstanceDifficulty выбирает режим сложности для игровой сессии
func (h *Handler) SelectInstanceDifficulty(ctx context.Context, req *api.SelectInstanceDifficultyReq, params api.SelectInstanceDifficultyParams) (api.SelectInstanceDifficultyRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.SelectInstanceDifficulty")
	defer span.End()

	span.SetAttributes(
		attribute.String("endpoint", "select_instance_difficulty"),
		attribute.String("instance_id", params.InstanceId.String()),
	)

	instanceID := params.InstanceId
	modeID := req.ModeId
	playerID := uuid.New() // TODO: Get from auth context

	// Проверяем доступ к режиму
	if err := h.service.ValidateDifficultyModeAccess(ctx, playerID, modeID); err != nil {
		h.logger.Warn("Access denied to difficulty mode",
			zap.String("player_id", playerID.String()),
			zap.String("mode_id", modeID.String()),
			zap.Error(err))
		return &api.SelectInstanceDifficultyForbidden{}, nil
	}

	// Создаем сессию
	sessionID, _, err := h.service.GetRestrictionController().StartSessionByMode(ctx, modeID, playerID)
	if err != nil {
		h.logger.Error("Failed to start session", zap.Error(err))
		return &api.SelectInstanceDifficultyInternalServerError{}, nil
	}

	// Записываем начало сессии в аналитику
	if err := h.service.GetAnalyticsCollector().RecordSessionStart(ctx, sessionID, instanceID, modeID, playerID); err != nil {
		h.logger.Error("Failed to record session start", zap.Error(err))
		// Не возвращаем ошибку, сессия уже создана
	}

	response := api.DifficultySession{
		ID:         sessionID,
		InstanceId: instanceID,
		ModeId:     modeID,
		PlayerId:   playerID,
	}

	h.logger.Info("Selected difficulty mode for instance",
		zap.String("instance_id", instanceID.String()),
		zap.String("mode_id", modeID.String()),
		zap.String("player_id", playerID.String()))

	return &response, nil
}

// UpdateDifficultyMode обновляет существующий режим сложности (admin only)
func (h *Handler) UpdateDifficultyMode(ctx context.Context, req *api.UpdateDifficultyModeRequest) (api.UpdateDifficultyModeRes, error) {
	ctx, span := h.service.GetTracer().Start(ctx, "Handler.UpdateDifficultyMode")
	defer span.End()

	span.SetAttributes(attribute.String("endpoint", "update_difficulty_mode"))

	modeID, err := uuid.Parse(req.Id)
	if err != nil {
		h.logger.Warn("Invalid mode ID format", zap.String("mode_id", req.Id), zap.Error(err))
		return &api.UpdateDifficultyModeBadRequest{}, nil
	}

	// Конвертируем из API формата в внутренний
	mode := &DifficultyMode{
		ID:                  modeID,
		Name:                req.Name,
		Level:               DifficultyLevel(req.Level),
		Description:         req.Description,
		HpModifier:          req.HpModifier,
		DamageModifier:      req.DamageModifier,
		TimeLimitMultiplier: req.TimeLimitMultiplier,
		RespawnLimit:        req.RespawnLimit,
		CheckpointLimit:     req.CheckpointLimit,
		SpecialMechanics:    req.SpecialMechanics,
		IsActive:            req.IsActive,
	}

	err = h.service.GetDifficultyManager().UpdateDifficultyMode(ctx, mode)
	if err != nil {
		h.logger.Error("Failed to update difficulty mode", zap.Error(err))
		return &api.UpdateDifficultyModeInternalServerError{}, nil
	}

	// Конвертируем обратно в API формат
	apiMode := api.DifficultyMode{
		ID:             mode.ID,
		Name:           mode.Name,
		Level:          api.DifficultyModeLevel(mode.Level),
		Description:    api.NewOptString(mode.Description),
		HpModifier:     float32(mode.HpModifier),
		DamageModifier: float32(mode.DamageModifier),
		TimeLimitMultiplier: float32(mode.TimeLimitMultiplier),
		RespawnLimit:   mode.RespawnLimit,
		CheckpointLimit: mode.CheckpointLimit,
		RewardModifier: float32(mode.RewardModifier),
	}

	h.logger.Info("Updated difficulty mode", zap.String("mode_id", modeID.String()))

	return &apiMode, nil
}
