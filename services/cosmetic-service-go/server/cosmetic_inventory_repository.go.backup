package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/cosmetic-service-go/models"
	"github.com/sirupsen/logrus"
)

type CosmeticInventoryRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewCosmeticInventoryRepository(db *pgxpool.Pool) *CosmeticInventoryRepository {
	return &CosmeticInventoryRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *CosmeticInventoryRepository) GetInventory(ctx context.Context, playerID uuid.UUID, category *string, rarity *string, limit, offset int) ([]models.PlayerCosmetic, int, error) {
	query := `
		SELECT pc.id, pc.player_id, pc.cosmetic_item_id, pc.source, pc.obtained_at,
			pc.times_used, pc.last_used_at, pc.created_at
		FROM monetization.player_cosmetics pc
		JOIN monetization.cosmetic_items ci ON pc.cosmetic_item_id = ci.id
		WHERE pc.player_id = $1`
	args := []interface{}{playerID}
	argPos := 2

	if category != nil {
		query += fmt.Sprintf(" AND ci.category = $%d", argPos)
		args = append(args, *category)
		argPos++
	}

	if rarity != nil {
		query += fmt.Sprintf(" AND ci.rarity = $%d", argPos)
		args = append(args, *rarity)
		argPos++
	}

	var total int
	countQuery := `
		SELECT COUNT(*)
		FROM monetization.player_cosmetics pc
		JOIN monetization.cosmetic_items ci ON pc.cosmetic_item_id = ci.id
		WHERE pc.player_id = $1`
	countArgs := []interface{}{playerID}
	if category != nil {
		countQuery += " AND ci.category = $2"
		countArgs = append(countArgs, *category)
		if rarity != nil {
			countQuery += " AND ci.rarity = $3"
			countArgs = append(countArgs, *rarity)
		}
	} else if rarity != nil {
		countQuery += " AND ci.rarity = $2"
		countArgs = append(countArgs, *rarity)
	}

	err := r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count inventory items: %w", err)
	}

	query += " ORDER BY pc.obtained_at DESC"
	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argPos, argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get inventory: %w", err)
	}
	defer rows.Close()

	var cosmetics []models.PlayerCosmetic
	for rows.Next() {
		var pc models.PlayerCosmetic

		err := rows.Scan(
			&pc.ID, &pc.PlayerID, &pc.CosmeticItemID, &pc.Source,
			&pc.ObtainedAt, &pc.TimesUsed, &pc.LastUsedAt, &pc.CreatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan player cosmetic: %w", err)
		}

		cosmeticItem, err := r.getCosmeticItem(ctx, pc.CosmeticItemID)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to get cosmetic item: %w", err)
		}
		pc.CosmeticItem = cosmeticItem

		cosmetics = append(cosmetics, pc)
	}

	return cosmetics, total, nil
}

func (r *CosmeticInventoryRepository) CheckOwnership(ctx context.Context, playerID, cosmeticID uuid.UUID) (*models.PlayerCosmetic, error) {
	var pc models.PlayerCosmetic

	query := `
		SELECT id, player_id, cosmetic_item_id, source, obtained_at,
			times_used, last_used_at, created_at
		FROM monetization.player_cosmetics
		WHERE player_id = $1 AND cosmetic_item_id = $2`

	err := r.db.QueryRow(ctx, query, playerID, cosmeticID).Scan(
		&pc.ID, &pc.PlayerID, &pc.CosmeticItemID, &pc.Source,
		&pc.ObtainedAt, &pc.TimesUsed, &pc.LastUsedAt, &pc.CreatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to check ownership: %w", err)
	}

	cosmeticItem, err := r.getCosmeticItem(ctx, pc.CosmeticItemID)
	if err != nil {
		return nil, fmt.Errorf("failed to get cosmetic item: %w", err)
	}
	pc.CosmeticItem = cosmeticItem

	return &pc, nil
}

func (r *CosmeticInventoryRepository) GetCosmeticsByRarity(ctx context.Context, rarity string, category *string, limit, offset int) ([]models.CosmeticItem, int, error) {
	query := `
		SELECT id, code, name, category, cosmetic_type, rarity, description,
			cost, currency_type, is_exclusive, source, visual_effects, assets,
			is_active, created_at, updated_at
		FROM monetization.cosmetic_items
		WHERE rarity = $1 AND is_active = true`
	args := []interface{}{rarity}
	argPos := 2

	if category != nil {
		query += fmt.Sprintf(" AND category = $%d", argPos)
		args = append(args, *category)
		argPos++
	}

	var total int
	countQuery := "SELECT COUNT(*) FROM monetization.cosmetic_items WHERE rarity = $1 AND is_active = true"
	countArgs := []interface{}{rarity}
	if category != nil {
		countQuery += " AND category = $2"
		countArgs = append(countArgs, *category)
	}

	err := r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count cosmetics by rarity: %w", err)
	}

	query += " ORDER BY created_at DESC"
	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argPos, argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get cosmetics by rarity: %w", err)
	}
	defer rows.Close()

	var items []models.CosmeticItem
	for rows.Next() {
		var item models.CosmeticItem
		var visualEffectsJSON []byte
		var assetsJSON []byte

		err := rows.Scan(
			&item.ID, &item.Code, &item.Name, &item.Category, &item.CosmeticType,
			&item.Rarity, &item.Description, &item.Cost, &item.CurrencyType,
			&item.IsExclusive, &item.Source, &visualEffectsJSON, &assetsJSON,
			&item.IsActive, &item.CreatedAt, &item.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan cosmetic item: %w", err)
		}

		if len(visualEffectsJSON) > 0 {
			if err := json.Unmarshal(visualEffectsJSON, &item.VisualEffects); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal visual_effects JSON")
				return nil, 0, fmt.Errorf("failed to unmarshal visual_effects JSON: %w", err)
			}
		}

		if len(assetsJSON) > 0 {
			if err := json.Unmarshal(assetsJSON, &item.Assets); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal assets JSON")
				return nil, 0, fmt.Errorf("failed to unmarshal assets JSON: %w", err)
			}
		}

		items = append(items, item)
	}

	return items, total, nil
}

func (r *CosmeticInventoryRepository) GetEvents(ctx context.Context, playerID uuid.UUID, eventType *string, limit, offset int) ([]models.CosmeticEvent, int, error) {
	query := `
		SELECT id, player_id, event_type, cosmetic_id, event_data, created_at
		FROM monetization.cosmetic_events
		WHERE player_id = $1`
	args := []interface{}{playerID}
	argPos := 2

	if eventType != nil {
		query += fmt.Sprintf(" AND event_type = $%d", argPos)
		args = append(args, *eventType)
		argPos++
	}

	var total int
	countQuery := "SELECT COUNT(*) FROM monetization.cosmetic_events WHERE player_id = $1"
	countArgs := []interface{}{playerID}
	if eventType != nil {
		countQuery += " AND event_type = $2"
		countArgs = append(countArgs, *eventType)
	}

	err := r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count events: %w", err)
	}

	query += " ORDER BY created_at DESC"
	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argPos, argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get events: %w", err)
	}
	defer rows.Close()

	var events []models.CosmeticEvent
	for rows.Next() {
		var event models.CosmeticEvent
		var eventDataJSON []byte

		err := rows.Scan(
			&event.ID, &event.PlayerID, &event.EventType,
			&event.CosmeticID, &eventDataJSON, &event.CreatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan event: %w", err)
		}

		if len(eventDataJSON) > 0 {
			if err := json.Unmarshal(eventDataJSON, &event.EventData); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal event_data JSON")
				return nil, 0, fmt.Errorf("failed to unmarshal event_data JSON: %w", err)
			}
		}

		events = append(events, event)
	}

	return events, total, nil
}

func (r *CosmeticInventoryRepository) CreateEvent(ctx context.Context, event *models.CosmeticEvent) error {
	eventDataJSON, err := json.Marshal(event.EventData)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal event_data JSON")
		return fmt.Errorf("failed to marshal event_data JSON: %w", err)
	}

	query := `
		INSERT INTO monetization.cosmetic_events (
			id, player_id, event_type, cosmetic_id, event_data, created_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, NOW()
		) RETURNING id, created_at`

	err = r.db.QueryRow(ctx, query,
		event.PlayerID, event.EventType, event.CosmeticID, eventDataJSON,
	).Scan(&event.ID, &event.CreatedAt)

	if err != nil {
		return fmt.Errorf("failed to create event: %w", err)
	}

	return nil
}

func (r *CosmeticInventoryRepository) getCosmeticItem(ctx context.Context, cosmeticID uuid.UUID) (*models.CosmeticItem, error) {
	var item models.CosmeticItem
	var visualEffectsJSON []byte
	var assetsJSON []byte

	query := `
		SELECT id, code, name, category, cosmetic_type, rarity, description,
			cost, currency_type, is_exclusive, source, visual_effects, assets,
			is_active, created_at, updated_at
		FROM monetization.cosmetic_items
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, cosmeticID).Scan(
		&item.ID, &item.Code, &item.Name, &item.Category, &item.CosmeticType,
		&item.Rarity, &item.Description, &item.Cost, &item.CurrencyType,
		&item.IsExclusive, &item.Source, &visualEffectsJSON, &assetsJSON,
		&item.IsActive, &item.CreatedAt, &item.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, fmt.Errorf("cosmetic item not found: %s", cosmeticID)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get cosmetic item: %w", err)
	}

	if len(visualEffectsJSON) > 0 {
		if err := json.Unmarshal(visualEffectsJSON, &item.VisualEffects); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal visual_effects JSON")
			return nil, fmt.Errorf("failed to unmarshal visual_effects JSON: %w", err)
		}
	}

	if len(assetsJSON) > 0 {
		if err := json.Unmarshal(assetsJSON, &item.Assets); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal assets JSON")
			return nil, fmt.Errorf("failed to unmarshal assets JSON: %w", err)
		}
	}

	return &item, nil
}

