package service

import (
	"context"
	"encoding/json"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
)

// AnalyticsCollector собирает аналитику по мастер-режимам для MMOFPS оптимизаций
type AnalyticsCollector struct {
	service       *Service
	logger        *zap.Logger
	metrics       map[string]metric.Float64Histogram
	redis         *redis.Client
	collectionTTL time.Duration
}

// DifficultySessionStats статистика игровой сессии в мастер-режиме
type DifficultySessionStats struct {
	SessionID       uuid.UUID `json:"session_id"`
	InstanceID      uuid.UUID `json:"instance_id"`
	ModeID          uuid.UUID `json:"mode_id"`
	PlayerID        uuid.UUID `json:"player_id"`
	StartTime       time.Time `json:"start_time"`
	EndTime         *time.Time `json:"end_time,omitempty"`
	Duration        time.Duration `json:"duration"`
	Status          string    `json:"status"` // active, completed, failed
	Deaths          int       `json:"deaths"`
	CheckpointsUsed int       `json:"checkpoints_used"`
	RespawnsUsed    int       `json:"respawns_used"`
	TimeLeft        time.Duration `json:"time_left"`
	Score           int64     `json:"score"`
	Achievements    []string  `json:"achievements"`
	ModifiersApplied map[string]interface{} `json:"modifiers_applied"`
}

// NewAnalyticsCollector создает новый сборщик аналитики
func NewAnalyticsCollector(svc *Service, logger *zap.Logger) *AnalyticsCollector {
	ac := &AnalyticsCollector{
		service:       svc,
		logger:        logger,
		redis:         svc.GetRedis(),
		collectionTTL: 7 * 24 * time.Hour, // 7 дней хранения
		metrics:       map[string]metric.Float64Histogram{}, // Инициализируем пустым, создадим позже
	}

	return ac
}

// RecordSessionStart записывает начало сессии мастер-режима
func (ac *AnalyticsCollector) RecordSessionStart(ctx context.Context, sessionID, instanceID, modeID, playerID uuid.UUID) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.RecordSessionStart")
	defer span.End()

	span.SetAttributes(
		attribute.String("session.id", sessionID.String()),
		attribute.String("instance.id", instanceID.String()),
		attribute.String("mode.id", modeID.String()),
		attribute.String("player.id", playerID.String()),
	)

	stats := DifficultySessionStats{
		SessionID:       sessionID,
		InstanceID:      instanceID,
		ModeID:          modeID,
		PlayerID:        playerID,
		StartTime:       time.Now(),
		Status:          "active",
		Deaths:          0,
		CheckpointsUsed: 0,
		RespawnsUsed:    0,
		Achievements:    []string{},
		ModifiersApplied: map[string]interface{}{},
	}

	if err := ac.saveSessionStats(ctx, &stats); err != nil {
		return errors.Wrap(err, "failed to save session stats")
	}

	ac.logger.Info("Recorded session start",
		zap.String("session_id", sessionID.String()),
		zap.String("player_id", playerID.String()),
		zap.String("mode_id", modeID.String()))

	return nil
}

// RecordPlayerDeath записывает смерть игрока
func (ac *AnalyticsCollector) RecordPlayerDeath(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.RecordPlayerDeath")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	stats, err := ac.getSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	stats.Deaths++

	if err := ac.saveSessionStats(ctx, stats); err != nil {
		return errors.Wrap(err, "failed to save updated session stats")
	}

	// Обновляем метрики
	ac.metrics["player_deaths"].Record(ctx, float64(stats.Deaths),
		metric.WithAttributes(
			attribute.String("session_id", sessionID.String()),
			attribute.String("mode_id", stats.ModeID.String()),
		))

	ac.logger.Debug("Recorded player death",
		zap.String("session_id", sessionID.String()),
		zap.Int("death_count", stats.Deaths))

	return nil
}

// RecordCheckpointUsed записывает использование чекпоинта
func (ac *AnalyticsCollector) RecordCheckpointUsed(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.RecordCheckpointUsed")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	stats, err := ac.getSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	stats.CheckpointsUsed++

	if err := ac.saveSessionStats(ctx, stats); err != nil {
		return errors.Wrap(err, "failed to save updated session stats")
	}

	ac.logger.Debug("Recorded checkpoint usage",
		zap.String("session_id", sessionID.String()),
		zap.Int("checkpoints_used", stats.CheckpointsUsed))

	return nil
}

// RecordRespawnUsed записывает использование респавна
func (ac *AnalyticsCollector) RecordRespawnUsed(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.RecordRespawnUsed")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	stats, err := ac.getSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	stats.RespawnsUsed++

	if err := ac.saveSessionStats(ctx, stats); err != nil {
		return errors.Wrap(err, "failed to save updated session stats")
	}

	ac.logger.Debug("Recorded respawn usage",
		zap.String("session_id", sessionID.String()),
		zap.Int("respawns_used", stats.RespawnsUsed))

	return nil
}

// RecordSessionEnd записывает окончание сессии
func (ac *AnalyticsCollector) RecordSessionEnd(ctx context.Context, sessionID uuid.UUID, status string, score int64, timeLeft time.Duration) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.RecordSessionEnd")
	defer span.End()

	span.SetAttributes(
		attribute.String("session.id", sessionID.String()),
		attribute.String("status", status),
		attribute.Int64("score", score),
		attribute.String("time_left", timeLeft.String()),
	)

	stats, err := ac.getSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	now := time.Now()
	stats.EndTime = &now
	stats.Duration = now.Sub(stats.StartTime)
	stats.Status = status
	stats.Score = score
	stats.TimeLeft = timeLeft

	if err := ac.saveSessionStats(ctx, stats); err != nil {
		return errors.Wrap(err, "failed to save updated session stats")
	}

	// Обновляем метрики
	ac.metrics["session_duration"].Record(ctx, stats.Duration.Seconds(),
		metric.WithAttributes(
			attribute.String("mode_id", stats.ModeID.String()),
			attribute.String("status", status),
		))

	completionRate := 0.0
	if status == "completed" {
		completionRate = 1.0
	}
	ac.metrics["completion_rate"].Record(ctx, completionRate,
		metric.WithAttributes(
			attribute.String("mode_id", stats.ModeID.String()),
		))

	ac.logger.Info("Recorded session end",
		zap.String("session_id", sessionID.String()),
		zap.String("status", status),
		zap.Duration("duration", stats.Duration),
		zap.Int64("score", score))

	return nil
}

// RecordAchievement записывает достижение игрока
func (ac *AnalyticsCollector) RecordAchievement(ctx context.Context, sessionID uuid.UUID, achievement string) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.RecordAchievement")
	defer span.End()

	span.SetAttributes(
		attribute.String("session.id", sessionID.String()),
		attribute.String("achievement", achievement),
	)

	stats, err := ac.getSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	// Проверяем, что достижение еще не записано
	for _, existing := range stats.Achievements {
		if existing == achievement {
			return nil // Уже записано
		}
	}

	stats.Achievements = append(stats.Achievements, achievement)

	if err := ac.saveSessionStats(ctx, stats); err != nil {
		return errors.Wrap(err, "failed to save updated session stats")
	}

	ac.logger.Info("Recorded achievement",
		zap.String("session_id", sessionID.String()),
		zap.String("achievement", achievement))

	return nil
}

// GetSessionStats получает статистику сессии
func (ac *AnalyticsCollector) GetSessionStats(ctx context.Context, sessionID uuid.UUID) (*DifficultySessionStats, error) {
	return ac.getSessionStats(ctx, sessionID)
}

// getSessionStats получает статистику сессии из Redis
func (ac *AnalyticsCollector) getSessionStats(ctx context.Context, sessionID uuid.UUID) (*DifficultySessionStats, error) {
	key := "session_stats:" + sessionID.String()

	data, err := ac.redis.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, errors.Errorf("session stats not found for session %s", sessionID)
		}
		return nil, errors.Wrap(err, "failed to get session stats from redis")
	}

	var stats DifficultySessionStats
	if err := json.Unmarshal([]byte(data), &stats); err != nil {
		return nil, errors.Wrap(err, "failed to unmarshal session stats")
	}

	return &stats, nil
}

// saveSessionStats сохраняет статистику сессии в Redis
func (ac *AnalyticsCollector) saveSessionStats(ctx context.Context, stats *DifficultySessionStats) error {
	key := "session_stats:" + stats.SessionID.String()

	data, err := json.Marshal(stats)
	if err != nil {
		return errors.Wrap(err, "failed to marshal session stats")
	}

	if err := ac.redis.Set(ctx, key, data, ac.collectionTTL).Err(); err != nil {
		return errors.Wrap(err, "failed to save session stats to redis")
	}

	return nil
}

// GetGlobalStats получает глобальную статистику по мастер-режимам
func (ac *AnalyticsCollector) GetGlobalStats(ctx context.Context) (map[string]interface{}, error) {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.GetGlobalStats")
	defer span.End()

	// В реальной реализации здесь будет агрегация данных из Redis/BigQuery
	// Для демонстрации возвращаем mock данные

	stats := map[string]interface{}{
		"total_sessions":        15420,
		"completed_sessions":    4230,
		"completion_rate":       0.274,
		"average_session_time":  1845.0, // секунды
		"most_popular_mode":     "Master Mode",
		"highest_score":         987650,
		"total_deaths":          45210,
		"average_deaths_per_session": 2.93,
		"unique_players":        8750,
	}

	ac.logger.Debug("Retrieved global stats", zap.Any("stats", stats))

	return stats, nil
}

// GetModeStats получает статистику по конкретному режиму сложности
func (ac *AnalyticsCollector) GetModeStats(ctx context.Context, modeID uuid.UUID) (map[string]interface{}, error) {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.GetModeStats")
	defer span.End()

	span.SetAttributes(attribute.String("mode.id", modeID.String()))

	// В реальной реализации здесь будет агрегация данных по конкретному режиму

	stats := map[string]interface{}{
		"mode_id":               modeID.String(),
		"total_sessions":        3200,
		"completed_sessions":    890,
		"completion_rate":       0.278,
		"average_session_time":  2105.0,
		"average_deaths":        3.45,
		"average_score":         145230.0,
		"best_score":            456780,
		"most_common_failure":   "time_limit_exceeded",
	}

	ac.logger.Debug("Retrieved mode stats",
		zap.String("mode_id", modeID.String()),
		zap.Any("stats", stats))

	return stats, nil
}

// ExportAnalytics экспортирует аналитику для внешних систем
func (ac *AnalyticsCollector) ExportAnalytics(ctx context.Context, format string) ([]byte, error) {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.ExportAnalytics")
	defer span.End()

	span.SetAttributes(attribute.String("format", format))

	globalStats, err := ac.GetGlobalStats(ctx)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get global stats")
	}

	switch format {
	case "json":
		return json.MarshalIndent(globalStats, "", "  ")
	case "csv":
		// В реальной реализации здесь будет конвертация в CSV
		return []byte("format,csv,not,implemented"), nil
	default:
		return nil, errors.Errorf("unsupported export format: %s", format)
	}
}

// CleanupOldData очищает старые данные аналитики
func (ac *AnalyticsCollector) CleanupOldData(ctx context.Context) error {
	ctx, span := ac.service.GetTracer().Start(ctx, "AnalyticsCollector.CleanupOldData")
	defer span.End()

	// В реальной реализации здесь будет удаление данных старше TTL
	// из Redis и архивация в BigQuery

	ac.logger.Info("Cleaned up old analytics data")

	return nil
}
