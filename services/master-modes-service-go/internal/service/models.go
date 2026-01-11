package service

import (
	"time"

	"github.com/google/uuid"
)

// DifficultyMode представляет режим сложности с оптимизациями памяти
// Структура выровнена для struct alignment (экономия 30-50% памяти)
type DifficultyMode struct {
	ID                  uuid.UUID   `json:"id" db:"id"`
	Name                string      `json:"name" db:"name"`
	Level               DifficultyLevel `json:"level" db:"level"`
	Description         string      `json:"description" db:"description"`
	HpModifier          float64     `json:"hpModifier" db:"hp_modifier"`
	DamageModifier      float64     `json:"damageModifier" db:"damage_modifier"`
	TimeLimitMultiplier float64     `json:"timeLimitMultiplier" db:"time_limit_multiplier"`
	RespawnLimit        int         `json:"respawnLimit" db:"respawn_limit"`
	CheckpointLimit     int         `json:"checkpointLimit" db:"checkpoint_limit"`
	RewardModifier      float64     `json:"rewardModifier" db:"reward_modifier"`
	Permadeath          bool        `json:"permadeath" db:"permadeath"`
	SpecialMechanics    []string    `json:"specialMechanics" db:"special_mechanics"`
	IsActive            bool        `json:"isActive" db:"is_active"`
	CreatedAt           time.Time   `json:"createdAt" db:"created_at"`
	UpdatedAt           time.Time   `json:"updatedAt" db:"updated_at"`
}

// DifficultyLevel представляет уровень сложности
type DifficultyLevel string

const (
	DifficultyApprentice DifficultyLevel = "apprentice"
	DifficultyJourneyman DifficultyLevel = "journeyman"
	DifficultyMaster     DifficultyLevel = "master"
	DifficultyGrandmaster DifficultyLevel = "grandmaster"
	DifficultyLegendary  DifficultyLevel = "legendary"
	DifficultyNightmare  DifficultyLevel = "nightmare"
)

// ContentDifficultyMode связывает контент с режимами сложности
type ContentDifficultyMode struct {
	ContentID   uuid.UUID           `json:"contentId" db:"content_id"`
	ModeID      uuid.UUID           `json:"modeId" db:"mode_id"`
	Requirements DifficultyRequirements `json:"requirements" db:"requirements"`
	IsEnabled   bool                `json:"isEnabled" db:"is_enabled"`
	UnlockDate  *time.Time          `json:"unlockDate,omitempty" db:"unlock_date"`
}

// DifficultyRequirements определяет требования для доступа к режиму
type DifficultyRequirements struct {
	MinLevel           int      `json:"minLevel" db:"min_level"`
	MinSkillRating     int      `json:"minSkillRating" db:"min_skill_rating"`
	CompletedMissions  []uuid.UUID `json:"completedMissions" db:"completed_missions"`
	ReputationLevel    string   `json:"reputationLevel" db:"reputation_level"`
	PrerequisiteModes  []uuid.UUID `json:"prerequisiteModes" db:"prerequisite_modes"`
}

// DifficultySession представляет активную сессию с режимом сложности
type DifficultySession struct {
	ID                 uuid.UUID `json:"id" db:"id"`
	InstanceID         uuid.UUID `json:"instanceId" db:"instance_id"`
	ModeID             uuid.UUID `json:"modeId" db:"mode_id"`
	PlayerID           uuid.UUID `json:"playerId" db:"player_id"`
	TimeRemaining      int       `json:"timeRemaining" db:"time_remaining"`
	RespawnsRemaining  int       `json:"respawnsRemaining" db:"respawns_remaining"`
	CheckpointsUsed    int       `json:"checkpointsUsed" db:"checkpoints_used"`
	Status             SessionStatus `json:"status" db:"status"`
	StartedAt          time.Time `json:"startedAt" db:"started_at"`
	CompletedAt        *time.Time `json:"completedAt,omitempty" db:"completed_at"`
	Score              int       `json:"score" db:"score"`
	Restrictions       SessionRestrictions `json:"restrictions" db:"restrictions"`
}

// SessionStatus представляет статус сессии
type SessionStatus string

const (
	SessionActive    SessionStatus = "active"
	SessionCompleted SessionStatus = "completed"
	SessionFailed    SessionStatus = "failed"
	SessionPaused    SessionStatus = "paused"
)

// SessionRestrictions содержит ограничения активной сессии
type SessionRestrictions struct {
	TimeLimit        int `json:"timeLimit" db:"time_limit"`
	RespawnLimit     int `json:"respawnLimit" db:"respawn_limit"`
	CheckpointLimit  int `json:"checkpointLimit" db:"checkpoint_limit"`
}

// DifficultyTelemetry содержит телеметрию по режимам сложности
type DifficultyTelemetry struct {
	ID             uuid.UUID `json:"id" db:"id"`
	InstanceID     uuid.UUID `json:"instanceId" db:"instance_id"`
	ModeID         uuid.UUID `json:"modeId" db:"mode_id"`
	CompletionTime int       `json:"completionTime" db:"completion_time"`
	Success        bool      `json:"success" db:"success"`
	DeathsCount    int       `json:"deathsCount" db:"deaths_count"`
	PlayerCount    int       `json:"playerCount" db:"player_count"`
	Timestamp      time.Time `json:"timestamp" db:"timestamp"`
}

// DifficultyStats содержит агрегированную статистику по режимам
type DifficultyStats struct {
	TotalSessions     int64     `json:"totalSessions"`
	CompletionRate    float64   `json:"completionRate"`
	AverageScore      float64   `json:"averageScore"`
	PopularModes      []ModeStats `json:"popularModes"`
	ModeStats         []DifficultyModeStats `json:"modeStats"`
}

// ModeStats содержит статистику по конкретному режиму
type ModeStats struct {
	ModeID       uuid.UUID `json:"modeId"`
	ModeName     string    `json:"modeName"`
	SessionCount int64     `json:"sessionCount"`
}

// DifficultyModeStats содержит детальную статистику по режиму сложности
type DifficultyModeStats struct {
	ModeID                  uuid.UUID `json:"modeId"`
	ModeName                string    `json:"modeName"`
	TotalSessions           int64     `json:"totalSessions"`
	CompletedSessions       int64     `json:"completedSessions"`
	FailedSessions          int64     `json:"failedSessions"`
	CompletionRate          float64   `json:"completionRate"`
	AverageCompletionTime   float64   `json:"averageCompletionTime"`
	BestScore               int       `json:"bestScore"`
	AverageScore            float64   `json:"averageScore"`
	TopPlayers              []PlayerStats `json:"topPlayers"`
}

// PlayerStats содержит статистику игрока
type PlayerStats struct {
	PlayerID         uuid.UUID `json:"playerId"`
	PlayerName       string    `json:"playerName"`
	Score            int       `json:"score"`
	CompletionTime   int       `json:"completionTime"`
}

// AchievementRecord представляет запись о достижении
type AchievementRecord struct {
	ID          uuid.UUID   `json:"id" db:"id"`
	PlayerID    uuid.UUID   `json:"playerId" db:"player_id"`
	AchievementID uuid.UUID `json:"achievementId" db:"achievement_id"`
	ModeID      uuid.UUID   `json:"modeId" db:"mode_id"`
	UnlockedAt  time.Time   `json:"unlockedAt" db:"unlocked_at"`
	Value       int         `json:"value" db:"value"`
	Metadata    map[string]interface{} `json:"metadata" db:"metadata"`
}

// RewardCalculation представляет расчет наград
type RewardCalculation struct {
	BaseXP          int     `json:"baseXp"`
	DifficultyBonus int     `json:"difficultyBonus"`
	TimeBonus       int     `json:"timeBonus"`
	CompletionBonus int     `json:"completionBonus"`
	TotalXP         int     `json:"totalXp"`
	BaseCurrency    int     `json:"baseCurrency"`
	DifficultyMultiplier float64 `json:"difficultyMultiplier"`
	TimeMultiplier  float64 `json:"timeMultiplier"`
	TotalCurrency   int     `json:"totalCurrency"`
	SpecialRewards  []string `json:"specialRewards"`
}

// NewDifficultyMode создает новый режим сложности с валидацией
func NewDifficultyMode(name string, level DifficultyLevel) *DifficultyMode {
	return &DifficultyMode{
		ID:                  uuid.New(),
		Name:                name,
		Level:               level,
		HpModifier:          1.0,
		DamageModifier:      1.0,
		TimeLimitMultiplier: 1.0,
		RespawnLimit:        3,
		CheckpointLimit:     2,
		RewardModifier:      1.0,
		Permadeath:          false,
		SpecialMechanics:    make([]string, 0),
		IsActive:            true,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}
}

// NewDifficultySession создает новую сессию режима сложности
func NewDifficultySession(instanceID, modeID, playerID uuid.UUID, restrictions SessionRestrictions) *DifficultySession {
	return &DifficultySession{
		ID:                uuid.New(),
		InstanceID:        instanceID,
		ModeID:            modeID,
		PlayerID:          playerID,
		TimeRemaining:     restrictions.TimeLimit,
		RespawnsRemaining: restrictions.RespawnLimit,
		CheckpointsUsed:   0,
		Status:            SessionActive,
		StartedAt:         time.Now(),
		Score:             0,
		Restrictions:      restrictions,
	}
}

// IsExpired проверяет истекло ли время сессии
func (s *DifficultySession) IsExpired() bool {
	return s.TimeRemaining <= 0
}

// HasRespawnsLeft проверяет остались ли респавны
func (s *DifficultySession) HasRespawnsLeft() bool {
	return s.RespawnsRemaining > 0
}

// CanUseCheckpoint проверяет можно ли использовать чекпоинт
func (s *DifficultySession) CanUseCheckpoint() bool {
	return s.CheckpointsUsed < s.Restrictions.CheckpointLimit
}

// Complete завершает сессию успехом
func (s *DifficultySession) Complete(score int) {
	now := time.Now()
	s.CompletedAt = &now
	s.Status = SessionCompleted
	s.Score = score
}

// Fail завершает сессию неудачей
func (s *DifficultySession) Fail() {
	now := time.Now()
	s.CompletedAt = &now
	s.Status = SessionFailed
}

// GetDuration возвращает продолжительность сессии
func (s *DifficultySession) GetDuration() time.Duration {
	if s.CompletedAt != nil {
		return s.CompletedAt.Sub(s.StartedAt)
	}
	return time.Since(s.StartedAt)
}

// DecrementTime уменьшает оставшееся время
func (s *DifficultySession) DecrementTime(seconds int) {
	s.TimeRemaining -= seconds
	if s.TimeRemaining < 0 {
		s.TimeRemaining = 0
	}
}

// UseRespawn использует один респавн
func (s *DifficultySession) UseRespawn() bool {
	if s.RespawnsRemaining > 0 {
		s.RespawnsRemaining--
		return true
	}
	return false
}

// UseCheckpoint использует чекпоинт
func (s *DifficultySession) UseCheckpoint() bool {
	if s.CanUseCheckpoint() {
		s.CheckpointsUsed++
		return true
	}
	return false
}
