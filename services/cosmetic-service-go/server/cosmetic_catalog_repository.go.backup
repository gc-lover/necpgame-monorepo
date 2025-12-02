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

type CosmeticCatalogRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewCosmeticCatalogRepository(db *pgxpool.Pool) *CosmeticCatalogRepository {
	return &CosmeticCatalogRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *CosmeticCatalogRepository) GetCatalog(ctx context.Context, category, rarity *string, limit, offset int) ([]models.CosmeticItem, int, error) {
	query := `
		SELECT id, code, name, category, cosmetic_type, rarity, description,
			cost, currency_type, is_exclusive, source, visual_effects, assets,
			is_active, created_at, updated_at
		FROM monetization.cosmetic_items
		WHERE is_active = true`
	args := []interface{}{}
	argPos := 1

	if category != nil {
		query += fmt.Sprintf(" AND category = $%d", argPos)
		args = append(args, *category)
		argPos++
	}

	if rarity != nil {
		query += fmt.Sprintf(" AND rarity = $%d", argPos)
		args = append(args, *rarity)
		argPos++
	}

	query += " ORDER BY created_at DESC"

	var total int
	countQuery := "SELECT COUNT(*) FROM monetization.cosmetic_items WHERE is_active = true"
	if category != nil {
		countQuery += fmt.Sprintf(" AND category = $%d", 1)
		if rarity != nil {
			countQuery += fmt.Sprintf(" AND rarity = $%d", 2)
		}
	} else if rarity != nil {
		countQuery += fmt.Sprintf(" AND rarity = $%d", 1)
	}

	countArgs := []interface{}{}
	if category != nil {
		countArgs = append(countArgs, *category)
	}
	if rarity != nil {
		countArgs = append(countArgs, *rarity)
	}

	err := r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count catalog items: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argPos, argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get catalog: %w", err)
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
			return nil, 0, fmt.Errorf("failed to scan catalog item: %w", err)
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

func (r *CosmeticCatalogRepository) GetCosmeticByID(ctx context.Context, cosmeticID uuid.UUID) (*models.CosmeticItem, error) {
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
		return nil, nil
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

func (r *CosmeticCatalogRepository) GetCategories(ctx context.Context) ([]models.CosmeticCategoryInfo, error) {
	query := `
		SELECT category, COUNT(*) as count
		FROM monetization.cosmetic_items
		WHERE is_active = true
		GROUP BY category
		ORDER BY category ASC`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to get categories: %w", err)
	}
	defer rows.Close()

	var categories []models.CosmeticCategoryInfo
	for rows.Next() {
		var cat models.CosmeticCategoryInfo
		err := rows.Scan(&cat.Category, &cat.Count)
		if err != nil {
			return nil, fmt.Errorf("failed to scan category: %w", err)
		}
		categories = append(categories, cat)
	}

	return categories, nil
}

