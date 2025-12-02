package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/cosmetic-service-go/models"
	"github.com/sirupsen/logrus"
)

type CosmeticShopRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewCosmeticShopRepository(db *pgxpool.Pool) *CosmeticShopRepository {
	return &CosmeticShopRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *CosmeticShopRepository) GetDailyShop(ctx context.Context) (*models.DailyShop, error) {
	today := time.Now().Format("2006-01-02")

	query := `
		SELECT sr.id, sr.rotation_date, sr.created_at
		FROM monetization.shop_rotations sr
		WHERE sr.rotation_date = $1
		ORDER BY sr.created_at DESC
		LIMIT 1`

	var rotationID uuid.UUID
	var rotationDate time.Time
	var createdAt time.Time

	err := r.db.QueryRow(ctx, query, today).Scan(&rotationID, &rotationDate, &createdAt)
	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get daily shop rotation: %w", err)
	}

	itemsQuery := `
		SELECT ci.id, ci.code, ci.name, ci.category, ci.cosmetic_type, ci.rarity,
			ci.description, ci.cost, ci.currency_type, ci.is_exclusive, ci.source,
			ci.visual_effects, ci.assets, ci.is_active, ci.created_at, ci.updated_at
		FROM monetization.shop_rotation_items sri
		JOIN monetization.cosmetic_items ci ON sri.cosmetic_item_id = ci.id
		WHERE sri.rotation_id = $1
		ORDER BY sri.position ASC`

	rows, err := r.db.Query(ctx, itemsQuery, rotationID)
	if err != nil {
		return nil, fmt.Errorf("failed to get daily shop items: %w", err)
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
			return nil, fmt.Errorf("failed to scan shop item: %w", err)
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

		items = append(items, item)
	}

	nextRotation := time.Now().Add(24 * time.Hour)
	nextRotation = time.Date(nextRotation.Year(), nextRotation.Month(), nextRotation.Day(), 0, 0, 0, 0, nextRotation.Location())

	return &models.DailyShop{
		RotationID:     rotationID,
		RotationDate:  rotationDate,
		Items:         items,
		NextRotationAt: nextRotation,
	}, nil
}

func (r *CosmeticShopRepository) GetShopHistory(ctx context.Context, limit, offset int) ([]models.ShopRotation, int, error) {
	var total int
	countQuery := "SELECT COUNT(*) FROM monetization.shop_rotations"
	err := r.db.QueryRow(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count shop rotations: %w", err)
	}

	query := `
		SELECT sr.id, sr.rotation_date, sr.created_at
		FROM monetization.shop_rotations sr
		ORDER BY sr.rotation_date DESC, sr.created_at DESC
		LIMIT $1 OFFSET $2`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get shop history: %w", err)
	}
	defer rows.Close()

	var rotations []models.ShopRotation
	for rows.Next() {
		var rotation models.ShopRotation

		err := rows.Scan(&rotation.ID, &rotation.RotationDate, &rotation.CreatedAt)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan shop rotation: %w", err)
		}

		itemsQuery := `
			SELECT ci.id, ci.code, ci.name, ci.category, ci.cosmetic_type, ci.rarity,
				ci.description, ci.cost, ci.currency_type, ci.is_exclusive, ci.source,
				ci.visual_effects, ci.assets, ci.is_active, ci.created_at, ci.updated_at
			FROM monetization.shop_rotation_items sri
			JOIN monetization.cosmetic_items ci ON sri.cosmetic_item_id = ci.id
			WHERE sri.rotation_id = $1
			ORDER BY sri.position ASC`

		itemRows, err := r.db.Query(ctx, itemsQuery, rotation.ID)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to get rotation items: %w", err)
		}

		var items []models.CosmeticItem
		for itemRows.Next() {
			var item models.CosmeticItem
			var visualEffectsJSON []byte
			var assetsJSON []byte

			err := itemRows.Scan(
				&item.ID, &item.Code, &item.Name, &item.Category, &item.CosmeticType,
				&item.Rarity, &item.Description, &item.Cost, &item.CurrencyType,
				&item.IsExclusive, &item.Source, &visualEffectsJSON, &assetsJSON,
				&item.IsActive, &item.CreatedAt, &item.UpdatedAt,
			)
			if err != nil {
				itemRows.Close()
				return nil, 0, fmt.Errorf("failed to scan rotation item: %w", err)
			}

			if len(visualEffectsJSON) > 0 {
				if err := json.Unmarshal(visualEffectsJSON, &item.VisualEffects); err != nil {
					r.logger.WithError(err).Error("Failed to unmarshal visual_effects JSON")
					itemRows.Close()
					return nil, 0, fmt.Errorf("failed to unmarshal visual_effects JSON: %w", err)
				}
			}

			if len(assetsJSON) > 0 {
				if err := json.Unmarshal(assetsJSON, &item.Assets); err != nil {
					r.logger.WithError(err).Error("Failed to unmarshal assets JSON")
					itemRows.Close()
					return nil, 0, fmt.Errorf("failed to unmarshal assets JSON: %w", err)
				}
			}

			items = append(items, item)
		}
		itemRows.Close()

		rotation.Items = items
		rotations = append(rotations, rotation)
	}

	return rotations, total, nil
}

