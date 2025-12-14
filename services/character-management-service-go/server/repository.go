// Issue: #75
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"

	"github.com/NECPGAME/character-management-service-go/pkg/api"
)

// Repository предоставляет доступ к данным персонажей
type Repository struct {
	db          *sql.DB
	redisClient *redis.Client
	kafkaWriter *kafka.Writer
	logger      *zap.Logger
}

// NewRepository создает новый репозиторий
func NewRepository(db *sql.DB, redisClient *redis.Client, kafkaWriter *kafka.Writer, logger *zap.Logger) *Repository {
	return &Repository{
		db:          db,
		redisClient: redisClient,
		kafkaWriter: kafkaWriter,
		logger:      logger,
	}
}

// CreateCharacter создает нового персонажа
func (r *Repository) CreateCharacter(ctx context.Context, playerID uuid.UUID, req api.CreateCharacterRequest) (*Character, error) {
	characterID := uuid.New()
	now := time.Now()

	// Создаем персонажа в БД
	query := `
		INSERT INTO characters (
			id, player_id, name, character_class, origin, level, experience,
			appearance, attributes, status, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
		RETURNING id, player_id, name, character_class, origin, level, experience, appearance, attributes, status, created_at, updated_at
	`

	var character Character
	var appearanceJSON, attributesJSON []byte

	// Сериализуем внешность и атрибуты в JSON
	appearanceData, _ := json.Marshal(req.Appearance)
	attributesData, _ := json.Marshal(req.Attributes)

	err := r.db.QueryRowContext(ctx, query,
		characterID, playerID, req.Name, req.CharacterClass, req.Origin,
		1, 0, // level=1, experience=0
		string(appearanceData), string(attributesData),
		"active", now, now,
	).Scan(
		&character.ID, &character.PlayerID, &character.Name, &character.CharacterClass,
		&character.Origin, &character.Level, &character.Experience,
		&appearanceJSON, &attributesJSON, &character.Status, &character.CreatedAt, &character.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create character", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to create character: %w", err)
	}

	// Десериализуем JSON данные
	json.Unmarshal(appearanceJSON, &character.Appearance)
	json.Unmarshal(attributesJSON, &character.Attributes)

	// Публикуем событие создания персонажа
	r.publishEvent(ctx, "CharacterCreated", map[string]interface{}{
		"characterId": characterID.String(),
		"playerId":    playerID.String(),
		"name":        req.Name,
		"class":       req.CharacterClass,
		"origin":      req.Origin,
	})

	r.logger.Info("Character created", zap.String("character_id", characterID.String()), zap.String("player_id", playerID.String()))
	return &character, nil
}

// GetPlayerCharacters получает список персонажей игрока
func (r *Repository) GetPlayerCharacters(ctx context.Context, playerID uuid.UUID, includeDeleted bool) ([]Character, error) {
	statusCondition := "status = 'active'"
	if includeDeleted {
		statusCondition = "status IN ('active', 'deleted')"
	}

	query := fmt.Sprintf(`
		SELECT id, player_id, name, character_class, origin, level, experience,
			   appearance, attributes, status, created_at, updated_at
		FROM characters
		WHERE player_id = $1 AND %s
		ORDER BY created_at ASC
	`, statusCondition)

	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		r.logger.Error("Failed to get player characters", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to get player characters: %w", err)
	}
	defer rows.Close()

	var characters []Character
	for rows.Next() {
		var character Character
		var appearanceJSON, attributesJSON []byte

		err := rows.Scan(
			&character.ID, &character.PlayerID, &character.Name, &character.CharacterClass,
			&character.Origin, &character.Level, &character.Experience,
			&appearanceJSON, &attributesJSON, &character.Status, &character.CreatedAt, &character.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan character", zap.Error(err))
			return nil, fmt.Errorf("failed to scan character: %w", err)
		}

		// Десериализуем JSON данные
		json.Unmarshal(appearanceJSON, &character.Appearance)
		json.Unmarshal(attributesJSON, &character.Attributes)

		characters = append(characters, character)
	}

	return characters, nil
}

// GetCharacter получает персонажа по ID
func (r *Repository) GetCharacter(ctx context.Context, characterID uuid.UUID) (*Character, error) {
	query := `
		SELECT id, player_id, name, character_class, origin, level, experience,
			   appearance, attributes, status, created_at, updated_at
		FROM characters
		WHERE id = $1 AND status = 'active'
	`

	var character Character
	var appearanceJSON, attributesJSON []byte

	err := r.db.QueryRowContext(ctx, query, characterID).Scan(
		&character.ID, &character.PlayerID, &character.Name, &character.CharacterClass,
		&character.Origin, &character.Level, &character.Experience,
		&appearanceJSON, &attributesJSON, &character.Status, &character.CreatedAt, &character.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrCharacterNotFound
		}
		r.logger.Error("Failed to get character", zap.Error(err), zap.String("character_id", characterID.String()))
		return nil, fmt.Errorf("failed to get character: %w", err)
	}

	// Десериализуем JSON данные
	json.Unmarshal(appearanceJSON, &character.Appearance)
	json.Unmarshal(attributesJSON, &character.Attributes)

	return &character, nil
}

// UpdateCharacter обновляет персонажа
func (r *Repository) UpdateCharacter(ctx context.Context, characterID uuid.UUID, req api.UpdateCharacterRequest) (*Character, error) {
	now := time.Now()

	// Формируем запрос обновления
	setParts := []string{"updated_at = $1"}
	args := []interface{}{now}
	argCount := 1

	if req.Name != nil {
		argCount++
		setParts = append(setParts, fmt.Sprintf("name = $%d", argCount))
		args = append(args, *req.Name)
	}

	if req.Appearance != nil {
		argCount++
		appearanceData, _ := json.Marshal(req.Appearance)
		setParts = append(setParts, fmt.Sprintf("appearance = $%d", argCount))
		args = append(args, string(appearanceData))
	}

	args = append(args, characterID)
	query := fmt.Sprintf(`
		UPDATE characters
		SET %s
		WHERE id = $%d AND status = 'active'
		RETURNING id, player_id, name, character_class, origin, level, experience,
		          appearance, attributes, status, created_at, updated_at
	`, strings.Join(setParts, ", "), argCount+1)

	var character Character
	var appearanceJSON, attributesJSON []byte

	err := r.db.QueryRowContext(ctx, query, args...).Scan(
		&character.ID, &character.PlayerID, &character.Name, &character.CharacterClass,
		&character.Origin, &character.Level, &character.Experience,
		&appearanceJSON, &attributesJSON, &character.Status, &character.CreatedAt, &character.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrCharacterNotFound
		}
		r.logger.Error("Failed to update character", zap.Error(err), zap.String("character_id", characterID.String()))
		return nil, fmt.Errorf("failed to update character: %w", err)
	}

	// Десериализуем JSON данные
	json.Unmarshal(appearanceJSON, &character.Appearance)
	json.Unmarshal(attributesJSON, &character.Attributes)

	return &character, nil
}

// DeleteCharacter выполняет soft delete персонажа
func (r *Repository) DeleteCharacter(ctx context.Context, characterID uuid.UUID) error {
	now := time.Now()

	query := `
		UPDATE characters
		SET status = 'deleted', updated_at = $1
		WHERE id = $2 AND status = 'active'
	`

	result, err := r.db.ExecContext(ctx, query, now, characterID)
	if err != nil {
		r.logger.Error("Failed to delete character", zap.Error(err), zap.String("character_id", characterID.String()))
		return fmt.Errorf("failed to delete character: %w", err)
	}

	if rowsAffected, _ := result.RowsAffected(); rowsAffected == 0 {
		return ErrCharacterNotFound
	}

	// Публикуем событие удаления персонажа
	r.publishEvent(ctx, "CharacterDeleted", map[string]interface{}{
		"characterId": characterID.String(),
	})

	r.logger.Info("Character deleted", zap.String("character_id", characterID.String()))
	return nil
}

// RestoreCharacter восстанавливает удаленного персонажа
func (r *Repository) RestoreCharacter(ctx context.Context, characterID uuid.UUID) (*Character, error) {
	now := time.Now()

	query := `
		UPDATE characters
		SET status = 'active', updated_at = $1
		WHERE id = $2 AND status = 'deleted'
		RETURNING id, player_id, name, character_class, origin, level, experience,
		          appearance, attributes, status, created_at, updated_at
	`

	var character Character
	var appearanceJSON, attributesJSON []byte

	err := r.db.QueryRowContext(ctx, query, now, characterID).Scan(
		&character.ID, &character.PlayerID, &character.Name, &character.CharacterClass,
		&character.Origin, &character.Level, &character.Experience,
		&appearanceJSON, &attributesJSON, &character.Status, &character.CreatedAt, &character.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrCharacterNotFound
		}
		r.logger.Error("Failed to restore character", zap.Error(err), zap.String("character_id", characterID.String()))
		return nil, fmt.Errorf("failed to restore character: %w", err)
	}

	// Десериализуем JSON данные
	json.Unmarshal(appearanceJSON, &character.Appearance)
	json.Unmarshal(attributesJSON, &character.Attributes)

	// Публикуем событие восстановления персонажа
	r.publishEvent(ctx, "CharacterRestored", map[string]interface{}{
		"characterId": characterID.String(),
	})

	r.logger.Info("Character restored", zap.String("character_id", characterID.String()))
	return &character, nil
}

// SwitchCharacter переключает активного персонажа игрока
func (r *Repository) SwitchCharacter(ctx context.Context, playerID uuid.UUID, characterID uuid.UUID) error {
	// Проверяем, что персонаж принадлежит игроку и активен
	character, err := r.GetCharacter(ctx, characterID)
	if err != nil {
		return err
	}

	if character.PlayerID != playerID {
		return ErrCharacterNotFound
	}

	// Получаем текущего активного персонажа
	activeCharacterID, err := r.GetActiveCharacterID(ctx, playerID)
	if err != nil {
		return fmt.Errorf("failed to get active character: %w", err)
	}

	now := time.Now()

	// Начинаем транзакцию
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Сохраняем состояние предыдущего активного персонажа (если есть)
	if activeCharacterID != nil {
		if err := r.saveCharacterStateSnapshot(ctx, tx, *activeCharacterID); err != nil {
			return fmt.Errorf("failed to save character state: %w", err)
		}
	}

	// Устанавливаем нового активного персонажа
	if err := r.setActiveCharacterInSession(ctx, playerID, characterID); err != nil {
		return fmt.Errorf("failed to set active character: %w", err)
	}

	// Обновляем время последнего обновления персонажа
	if _, err := tx.ExecContext(ctx, "UPDATE characters SET updated_at = $1 WHERE id = $2", now, characterID); err != nil {
		return fmt.Errorf("failed to update character timestamp: %w", err)
	}

	if err := tx.Commit(); err != nil {
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	// Публикуем событие переключения персонажа
	r.publishEvent(ctx, "CharacterSwitched", map[string]interface{}{
		"playerId":            playerID.String(),
		"newCharacterId":      characterID.String(),
		"previousCharacterId": activeCharacterID,
	})

	r.logger.Info("Character switched",
		zap.String("player_id", playerID.String()),
		zap.String("character_id", characterID.String()))

	return nil
}

// GetActiveCharacterID получает ID активного персонажа игрока
func (r *Repository) GetActiveCharacterID(ctx context.Context, playerID uuid.UUID) (*uuid.UUID, error) {
	// Проверяем кэш Redis
	cacheKey := fmt.Sprintf("active_character:%s", playerID.String())
	cachedID, err := r.redisClient.Get(ctx, cacheKey).Result()

	if err == nil {
		if id, parseErr := uuid.Parse(cachedID); parseErr == nil {
			return &id, nil
		}
	}

	// Если нет в кэше, проверяем сессию или базу данных
	// Для простоты возвращаем первый активный персонаж
	characters, err := r.GetPlayerCharacters(ctx, playerID, false)
	if err != nil {
		return nil, err
	}

	if len(characters) > 0 {
		// Кэшируем на 1 час
		r.redisClient.Set(ctx, cacheKey, characters[0].ID.String(), time.Hour)
		return &characters[0].ID, nil
	}

	return nil, nil
}

// GetPlayerSlots получает информацию о слотах персонажей игрока
func (r *Repository) GetPlayerSlots(ctx context.Context, playerID uuid.UUID) (*PlayerSlots, error) {
	// Получаем общее количество слотов (базовое + купленные)
	query := `
		SELECT COALESCE(slots_purchased, 0) + 3 as total_slots
		FROM players
		WHERE id = $1
	`

	var totalSlots int
	err := r.db.QueryRowContext(ctx, query, playerID).Scan(&totalSlots)
	if err != nil {
		if err == sql.ErrNoRows {
			// Создаем запись игрока с базовыми слотами
			totalSlots = 3
			_, err := r.db.ExecContext(ctx, "INSERT INTO players (id) VALUES ($1)", playerID)
			if err != nil {
				r.logger.Error("Failed to create player record", zap.Error(err))
			}
		} else {
			r.logger.Error("Failed to get player slots", zap.Error(err), zap.String("player_id", playerID.String()))
			return nil, fmt.Errorf("failed to get player slots: %w", err)
		}
	}

	// Получаем количество занятых слотов
	characters, err := r.GetPlayerCharacters(ctx, playerID, false)
	if err != nil {
		return nil, err
	}

	usedSlots := len(characters)
	availableSlots := totalSlots - usedSlots
	canPurchaseMore := totalSlots < 10 // Максимум 10 слотов

	return &PlayerSlots{
		TotalSlots:      totalSlots,
		UsedSlots:       usedSlots,
		AvailableSlots:  availableSlots,
		CanPurchaseMore: canPurchaseMore,
	}, nil
}

// PurchaseSlots покупает дополнительные слоты для персонажей
func (r *Repository) PurchaseSlots(ctx context.Context, playerID uuid.UUID, slotsCount int) error {
	// Проверяем, что не превышен лимит
	currentSlots, err := r.GetPlayerSlots(ctx, playerID)
	if err != nil {
		return err
	}

	if currentSlots.TotalSlots+slotsCount > 10 {
		return fmt.Errorf("maximum slots limit exceeded")
	}

	// Обновляем количество купленных слотов
	query := `
		UPDATE players
		SET slots_purchased = COALESCE(slots_purchased, 0) + $1, updated_at = $2
		WHERE id = $3
	`

	_, err = r.db.ExecContext(ctx, query, slotsCount, time.Now(), playerID)
	if err != nil {
		r.logger.Error("Failed to purchase slots", zap.Error(err), zap.String("player_id", playerID.String()))
		return fmt.Errorf("failed to purchase slots: %w", err)
	}

	r.logger.Info("Slots purchased",
		zap.String("player_id", playerID.String()),
		zap.Int("slots_count", slotsCount))

	return nil
}

// Вспомогательные методы

func (r *Repository) setActiveCharacterInSession(ctx context.Context, playerID, characterID uuid.UUID) error {
	cacheKey := fmt.Sprintf("active_character:%s", playerID.String())
	return r.redisClient.Set(ctx, cacheKey, characterID.String(), time.Hour).Err()
}

func (r *Repository) saveCharacterStateSnapshot(ctx context.Context, tx *sql.Tx, characterID uuid.UUID) error {
	// Для простоты просто обновляем время последнего обновления
	_, err := tx.ExecContext(ctx, "UPDATE characters SET updated_at = $1 WHERE id = $2", time.Now(), characterID)
	return err
}

func (r *Repository) publishEvent(ctx context.Context, eventType string, data map[string]interface{}) {
	event := map[string]interface{}{
		"type":      eventType,
		"timestamp": time.Now().Unix(),
		"data":      data,
	}

	eventJSON, _ := json.Marshal(event)

	err := r.kafkaWriter.WriteMessages(ctx, kafka.Message{
		Key:   []byte(fmt.Sprintf("%s-%d", eventType, time.Now().Unix())),
		Value: eventJSON,
	})

	if err != nil {
		r.logger.Error("Failed to publish event", zap.Error(err), zap.String("event_type", eventType))
	} else {
		r.logger.Debug("Event published", zap.String("event_type", eventType))
	}
}

// Структуры данных

type Character struct {
	ID             uuid.UUID
	PlayerID       uuid.UUID
	Name           string
	CharacterClass string
	Origin         string
	Level          int
	Experience     int
	Appearance     *api.CharacterAppearance
	Attributes     *api.CharacterAttributes
	Status         string
	CreatedAt      time.Time
	UpdatedAt      time.Time
}

type PlayerSlots struct {
	TotalSlots      int
	UsedSlots       int
	AvailableSlots  int
	CanPurchaseMore bool
}

// Стандартные ошибки
var (
	ErrCharacterNotFound = fmt.Errorf("character not found")
)
