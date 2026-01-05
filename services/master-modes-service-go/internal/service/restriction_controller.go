package service

import (
	"context"
	"sync"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.uber.org/zap"
)

// RestrictionController отслеживает ограничения в реальном времени для MMOFPS
type RestrictionController struct {
	service    *Service
	logger     *zap.Logger
	ticker     *time.Ticker
	stopChan   chan struct{}
	sessions   map[uuid.UUID]*DifficultySession // In-memory cache для быстрого доступа
	sessionsMu sync.RWMutex
}

// NewRestrictionController создает новый контроллер ограничений
func NewRestrictionController(svc *Service, logger *zap.Logger) *RestrictionController {
	rc := &RestrictionController{
		service:  svc,
		logger:   logger,
		stopChan: make(chan struct{}),
		sessions: make(map[uuid.UUID]*DifficultySession),
	}

	// Запускаем фоновый мониторинг ограничений
	go rc.startMonitoring()

	return rc
}

// StartSession начинает отслеживание сессии
func (rc *RestrictionController) StartSession(ctx context.Context, session *DifficultySession) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RestrictionController.StartSession")
	defer span.End()

	span.SetAttributes(
		attribute.String("session.id", session.ID.String()),
		attribute.String("instance.id", session.InstanceID.String()),
	)

	rc.sessionsMu.Lock()
	rc.sessions[session.ID] = session
	rc.sessionsMu.Unlock()

	rc.logger.Info("Started monitoring difficulty session",
		zap.String("session_id", session.ID.String()),
		zap.String("instance_id", session.InstanceID.String()),
		zap.String("mode_id", session.ModeID.String()))

	return nil
}

// UpdateTimeRemaining обновляет оставшееся время сессии
func (rc *RestrictionController) UpdateTimeRemaining(ctx context.Context, sessionID uuid.UUID, secondsElapsed int) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RestrictionController.UpdateTimeRemaining")
	defer span.End()

	span.SetAttributes(
		attribute.String("session.id", sessionID.String()),
		attribute.Int("seconds_elapsed", secondsElapsed),
	)

	rc.sessionsMu.RLock()
	session, exists := rc.sessions[sessionID]
	rc.sessionsMu.RUnlock()

	if !exists {
		return errors.New("session not found in memory cache")
	}

	session.DecrementTime(secondsElapsed)

	// Проверяем критические ограничения
	if session.IsExpired() {
		rc.logger.Warn("Session time expired",
			zap.String("session_id", sessionID.String()),
			zap.Int("time_remaining", session.TimeRemaining))

		// Отправляем предупреждение через WebSocket
		rc.sendRestrictionWarning(ctx, session, "time_expired")

		// Автоматически завершаем сессию неудачей
		rc.failSession(ctx, session)
		return nil
	}

	// Предупреждение за 5 минут до истечения
	if session.TimeRemaining <= 300 && session.TimeRemaining > 290 {
		rc.sendRestrictionWarning(ctx, session, "time_low")
	}

	return nil
}

// UseRespawn регистрирует использование респавна
func (rc *RestrictionController) UseRespawn(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RestrictionController.UseRespawn")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	rc.sessionsMu.RLock()
	session, exists := rc.sessions[sessionID]
	rc.sessionsMu.RUnlock()

	if !exists {
		return errors.New("session not found in memory cache")
	}

	if !session.UseRespawn() {
		rc.logger.Warn("No respawns remaining",
			zap.String("session_id", sessionID.String()),
			zap.Int("respawns_remaining", session.RespawnsRemaining))

		rc.sendRestrictionWarning(ctx, session, "no_respawns")
		return errors.New("no respawns remaining")
	}

	rc.logger.Debug("Respawn used",
		zap.String("session_id", sessionID.String()),
		zap.Int("respawns_remaining", session.RespawnsRemaining))

	// Предупреждение при последнем респавне
	if session.RespawnsRemaining == 0 {
		rc.sendRestrictionWarning(ctx, session, "last_respawn")
	}

	return nil
}

// UseCheckpoint регистрирует использование чекпоинта
func (rc *RestrictionController) UseCheckpoint(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RestrictionController.UseCheckpoint")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	rc.sessionsMu.RLock()
	session, exists := rc.sessions[sessionID]
	rc.sessionsMu.RUnlock()

	if !exists {
		return errors.New("session not found in memory cache")
	}

	if !session.UseCheckpoint() {
		rc.logger.Warn("No checkpoints remaining",
			zap.String("session_id", sessionID.String()),
			zap.Int("checkpoints_used", session.CheckpointsUsed),
			zap.Int("checkpoint_limit", session.Restrictions.CheckpointLimit))

		rc.sendRestrictionWarning(ctx, session, "no_checkpoints")
		return errors.New("no checkpoints remaining")
	}

	rc.logger.Debug("Checkpoint used",
		zap.String("session_id", sessionID.String()),
		zap.Int("checkpoints_used", session.CheckpointsUsed))

	// Предупреждение при последнем чекпоинте
	if session.CheckpointsUsed == session.Restrictions.CheckpointLimit {
		rc.sendRestrictionWarning(ctx, session, "last_checkpoint")
	}

	return nil
}

// CheckRestrictions проверяет все ограничения сессии
func (rc *RestrictionController) CheckRestrictions(ctx context.Context, sessionID uuid.UUID) (*SessionRestrictions, error) {
	rc.sessionsMu.RLock()
	session, exists := rc.sessions[sessionID]
	rc.sessionsMu.RUnlock()

	if !exists {
		return nil, errors.New("session not found in memory cache")
	}

	return &session.Restrictions, nil
}

// CompleteSession завершает сессию успехом
func (rc *RestrictionController) CompleteSession(ctx context.Context, sessionID uuid.UUID, score int) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RestrictionController.CompleteSession")
	defer span.End()

	span.SetAttributes(
		attribute.String("session.id", sessionID.String()),
		attribute.Int("final_score", score),
	)

	rc.sessionsMu.RLock()
	session, exists := rc.sessions[sessionID]
	rc.sessionsMu.RUnlock()

	if !exists {
		return errors.New("session not found in memory cache")
	}

	session.Complete(score)

	rc.logger.Info("Session completed successfully",
		zap.String("session_id", sessionID.String()),
		zap.Duration("duration", session.GetDuration()),
		zap.Int("final_score", score))

	// Удаляем из памяти после завершения
	rc.sessionsMu.Lock()
	delete(rc.sessions, sessionID)
	rc.sessionsMu.Unlock()

	return nil
}

// FailSession завершает сессию неудачей
func (rc *RestrictionController) FailSession(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RestrictionController.FailSession")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	rc.sessionsMu.RLock()
	session, exists := rc.sessions[sessionID]
	rc.sessionsMu.RUnlock()

	if !exists {
		return errors.New("session not found in memory cache")
	}

	rc.failSession(ctx, session)
	return nil
}

// Stop останавливает контроллер ограничений
func (rc *RestrictionController) Stop() {
	rc.logger.Info("Stopping Restriction Controller...")

	close(rc.stopChan)

	// Сохраняем состояние активных сессий перед остановкой
	rc.sessionsMu.RLock()
	activeSessions := make([]*DifficultySession, 0, len(rc.sessions))
	for _, session := range rc.sessions {
		activeSessions = append(activeSessions, session)
	}
	rc.sessionsMu.RUnlock()

	// В реальной реализации сохраняем в БД для восстановления после перезапуска
	rc.logger.Info("Restriction Controller stopped",
		zap.Int("active_sessions_saved", len(activeSessions)))
}

// startMonitoring запускает фоновый мониторинг ограничений
func (rc *RestrictionController) startMonitoring() {
	rc.ticker = time.NewTicker(10 * time.Second) // Проверка каждые 10 секунд
	defer rc.ticker.Stop()

	rc.logger.Info("Started restriction monitoring")

	for {
		select {
		case <-rc.stopChan:
			return
		case <-rc.ticker.C:
			rc.checkAllRestrictions(context.Background())
		}
	}
}

// checkAllRestrictions проверяет ограничения всех активных сессий
func (rc *RestrictionController) checkAllRestrictions(ctx context.Context) {
	rc.sessionsMu.RLock()
	sessions := make([]*DifficultySession, 0, len(rc.sessions))
	for _, session := range rc.sessions {
		sessions = append(sessions, session)
	}
	rc.sessionsMu.RUnlock()

	for _, session := range sessions {
		rc.checkSessionRestrictions(ctx, session)
	}
}

// checkSessionRestrictions проверяет ограничения конкретной сессии
func (rc *RestrictionController) checkSessionRestrictions(ctx context.Context, session *DifficultySession) {
	// Проверяем время
	if session.IsExpired() {
		rc.logger.Warn("Session expired during monitoring",
			zap.String("session_id", session.ID.String()))
		rc.failSession(ctx, session)
		return
	}

	// Предупреждения о низком времени
	if session.TimeRemaining <= 600 && session.TimeRemaining > 590 { // 10 минут
		rc.sendRestrictionWarning(ctx, session, "time_critical")
	}

	// Проверяем респавны
	if !session.HasRespawnsLeft() && session.Status == SessionActive {
		rc.sendRestrictionWarning(ctx, session, "no_respawns_warning")
	}
}

// failSession завершает сессию неудачей
func (rc *RestrictionController) failSession(ctx context.Context, session *DifficultySession) {
	session.Fail()

	rc.logger.Warn("Session failed due to restrictions",
		zap.String("session_id", session.ID.String()),
		zap.String("reason", rc.getFailureReason(session)),
		zap.Duration("duration", session.GetDuration()))

	// Отправляем финальное предупреждение
	rc.sendRestrictionWarning(ctx, session, "session_failed")

	// Удаляем из памяти
	rc.sessionsMu.Lock()
	delete(rc.sessions, session.ID)
	rc.sessionsMu.Unlock()
}

// getFailureReason определяет причину неудачи сессии
func (rc *RestrictionController) getFailureReason(session *DifficultySession) string {
	if session.TimeRemaining <= 0 {
		return "time_expired"
	}
	if !session.HasRespawnsLeft() && session.Permadeath {
		return "permadeath_no_respawns"
	}
	return "unknown"
}

// sendRestrictionWarning отправляет предупреждение о нарушении ограничений
func (rc *RestrictionController) sendRestrictionWarning(ctx context.Context, session *DifficultySession, warningType string) {
	// В реальной реализации здесь будет отправка через WebSocket или событие
	rc.logger.Info("Restriction warning sent",
		zap.String("session_id", session.ID.String()),
		zap.String("instance_id", session.InstanceID.String()),
		zap.String("warning_type", warningType),
		zap.Int("time_remaining", session.TimeRemaining),
		zap.Int("respawns_remaining", session.RespawnsRemaining),
		zap.Int("checkpoints_used", session.CheckpointsUsed))

	// Можно добавить отправку события в event bus для realtime уведомлений клиентам
}
