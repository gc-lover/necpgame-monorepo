// Issue: #141888398
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/housing-service-go/models"
	"github.com/sirupsen/logrus"
)

type HousingRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewHousingRepository(db *pgxpool.Pool, logger *logrus.Logger) *HousingRepository {
	return &HousingRepository{
		db:     db,
		logger: logger,
	}
}

func (r *HousingRepository) CreateApartment(ctx context.Context, apartment *models.Apartment) error {
	guestsJSON, err := json.Marshal(apartment.Guests)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal guests JSON")
		return fmt.Errorf("failed to marshal guests: %w", err)
	}
	settingsJSON, err := json.Marshal(apartment.Settings)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal settings JSON")
		return fmt.Errorf("failed to marshal settings: %w", err)
	}

	query := `
		INSERT INTO housing.apartments (
			id, owner_id, owner_type, apartment_type, location, price,
			furniture_slots, prestige_score, is_public, guests, settings,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)
	`

	_, err = r.db.Exec(ctx, query,
		apartment.ID, apartment.OwnerID, apartment.OwnerType, apartment.ApartmentType,
		apartment.Location, apartment.Price, apartment.FurnitureSlots, apartment.PrestigeScore,
		apartment.IsPublic, guestsJSON, settingsJSON, apartment.CreatedAt, apartment.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create apartment: %w", err)
	}

	return nil
}

func (r *HousingRepository) GetApartmentByID(ctx context.Context, apartmentID uuid.UUID) (*models.Apartment, error) {
	query := `
		SELECT id, owner_id, owner_type, apartment_type, location, price,
			furniture_slots, prestige_score, is_public, guests, settings,
			created_at, updated_at
		FROM housing.apartments
		WHERE id = $1
	`

	var apartment models.Apartment
	var guestsJSON, settingsJSON []byte

	err := r.db.QueryRow(ctx, query, apartmentID).Scan(
		&apartment.ID, &apartment.OwnerID, &apartment.OwnerType, &apartment.ApartmentType,
		&apartment.Location, &apartment.Price, &apartment.FurnitureSlots, &apartment.PrestigeScore,
		&apartment.IsPublic, &guestsJSON, &settingsJSON, &apartment.CreatedAt, &apartment.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get apartment: %w", err)
	}

	if err := json.Unmarshal(guestsJSON, &apartment.Guests); err != nil {
		r.logger.WithError(err).Error("Failed to unmarshal guests JSON")
		apartment.Guests = []uuid.UUID{}
	}

	if err := json.Unmarshal(settingsJSON, &apartment.Settings); err != nil {
		r.logger.WithError(err).Error("Failed to unmarshal settings JSON")
		apartment.Settings = make(map[string]interface{})
	}

	return &apartment, nil
}

func (r *HousingRepository) ListApartments(ctx context.Context, ownerID *uuid.UUID, ownerType *string, isPublic *bool, limit, offset int) ([]models.Apartment, int, error) {
	query := `SELECT id, owner_id, owner_type, apartment_type, location, price,
		furniture_slots, prestige_score, is_public, guests, settings,
		created_at, updated_at
		FROM housing.apartments WHERE 1=1`
	args := []interface{}{}
	argIndex := 1

	if ownerID != nil {
		query += fmt.Sprintf(" AND owner_id = $%d", argIndex)
		args = append(args, *ownerID)
		argIndex++
	}

	if ownerType != nil {
		query += fmt.Sprintf(" AND owner_type = $%d", argIndex)
		args = append(args, *ownerType)
		argIndex++
	}

	if isPublic != nil {
		query += fmt.Sprintf(" AND is_public = $%d", argIndex)
		args = append(args, *isPublic)
		argIndex++
	}

	query += " ORDER BY prestige_score DESC, created_at DESC"

	countQuery := "SELECT COUNT(*) FROM (" + query + ") AS count_query"
	var total int
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count apartments: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list apartments: %w", err)
	}
	defer rows.Close()

	var apartments []models.Apartment
	for rows.Next() {
		var apartment models.Apartment
		var guestsJSON, settingsJSON []byte

		err := rows.Scan(
			&apartment.ID, &apartment.OwnerID, &apartment.OwnerType, &apartment.ApartmentType,
			&apartment.Location, &apartment.Price, &apartment.FurnitureSlots, &apartment.PrestigeScore,
			&apartment.IsPublic, &guestsJSON, &settingsJSON, &apartment.CreatedAt, &apartment.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan apartment: %w", err)
		}

		if err := json.Unmarshal(guestsJSON, &apartment.Guests); err != nil {
			apartment.Guests = []uuid.UUID{}
		}

		if err := json.Unmarshal(settingsJSON, &apartment.Settings); err != nil {
			apartment.Settings = make(map[string]interface{})
		}

		apartments = append(apartments, apartment)
	}

	return apartments, total, nil
}

func (r *HousingRepository) UpdateApartment(ctx context.Context, apartment *models.Apartment) error {
	guestsJSON, err := json.Marshal(apartment.Guests)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal guests JSON")
		return fmt.Errorf("failed to marshal guests: %w", err)
	}
	settingsJSON, err := json.Marshal(apartment.Settings)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal settings JSON")
		return fmt.Errorf("failed to marshal settings: %w", err)
	}

	query := `
		UPDATE housing.apartments
		SET is_public = $2, guests = $3, settings = $4, prestige_score = $5, updated_at = $6
		WHERE id = $1
	`

	_, err = r.db.Exec(ctx, query,
		apartment.ID, apartment.IsPublic, guestsJSON, settingsJSON,
		apartment.PrestigeScore, apartment.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to update apartment: %w", err)
	}

	return nil
}

func (r *HousingRepository) GetFurnitureItemByID(ctx context.Context, itemID string) (*models.FurnitureItem, error) {
	query := `
		SELECT id, category, name, description, price, prestige_value, function_bonus, created_at
		FROM housing.furniture_items
		WHERE id = $1
	`

	var item models.FurnitureItem
	var functionBonusJSON []byte

	err := r.db.QueryRow(ctx, query, itemID).Scan(
		&item.ID, &item.Category, &item.Name, &item.Description,
		&item.Price, &item.PrestigeValue, &functionBonusJSON, &item.CreatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get furniture item: %w", err)
	}

	if err := json.Unmarshal(functionBonusJSON, &item.FunctionBonus); err != nil {
		r.logger.WithError(err).Error("Failed to unmarshal function bonus JSON")
		item.FunctionBonus = make(map[string]interface{})
	}

	return &item, nil
}

func (r *HousingRepository) ListFurnitureItems(ctx context.Context, category *models.FurnitureCategory, limit, offset int) ([]models.FurnitureItem, int, error) {
	query := `SELECT id, category, name, description, price, prestige_value, function_bonus, created_at
		FROM housing.furniture_items WHERE 1=1`
	args := []interface{}{}
	argIndex := 1

	if category != nil {
		query += fmt.Sprintf(" AND category = $%d", argIndex)
		args = append(args, *category)
		argIndex++
	}

	query += " ORDER BY name"

	countQuery := "SELECT COUNT(*) FROM (" + query + ") AS count_query"
	var total int
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count furniture items: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list furniture items: %w", err)
	}
	defer rows.Close()

	var items []models.FurnitureItem
	for rows.Next() {
		var item models.FurnitureItem
		var functionBonusJSON []byte

		err := rows.Scan(
			&item.ID, &item.Category, &item.Name, &item.Description,
			&item.Price, &item.PrestigeValue, &functionBonusJSON, &item.CreatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan furniture item: %w", err)
		}

		if err := json.Unmarshal(functionBonusJSON, &item.FunctionBonus); err != nil {
			item.FunctionBonus = make(map[string]interface{})
		}

		items = append(items, item)
	}

	return items, total, nil
}

func (r *HousingRepository) CreatePlacedFurniture(ctx context.Context, furniture *models.PlacedFurniture) error {
	positionJSON, err := json.Marshal(furniture.Position)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal position JSON")
		return fmt.Errorf("failed to marshal position: %w", err)
	}
	rotationJSON, err := json.Marshal(furniture.Rotation)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal rotation JSON")
		return fmt.Errorf("failed to marshal rotation: %w", err)
	}
	scaleJSON, err := json.Marshal(furniture.Scale)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal scale JSON")
		return fmt.Errorf("failed to marshal scale: %w", err)
	}

	query := `
		INSERT INTO housing.placed_furniture (
			id, apartment_id, furniture_item_id, position, rotation, scale,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err = r.db.Exec(ctx, query,
		furniture.ID, furniture.ApartmentID, furniture.FurnitureItemID,
		positionJSON, rotationJSON, scaleJSON, furniture.CreatedAt, furniture.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create placed furniture: %w", err)
	}

	return nil
}

func (r *HousingRepository) GetPlacedFurnitureByID(ctx context.Context, furnitureID uuid.UUID) (*models.PlacedFurniture, error) {
	query := `
		SELECT id, apartment_id, furniture_item_id, position, rotation, scale,
			created_at, updated_at
		FROM housing.placed_furniture
		WHERE id = $1
	`

	var furniture models.PlacedFurniture
	var positionJSON, rotationJSON, scaleJSON []byte

	err := r.db.QueryRow(ctx, query, furnitureID).Scan(
		&furniture.ID, &furniture.ApartmentID, &furniture.FurnitureItemID,
		&positionJSON, &rotationJSON, &scaleJSON, &furniture.CreatedAt, &furniture.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get placed furniture: %w", err)
	}

	if err := json.Unmarshal(positionJSON, &furniture.Position); err != nil {
		r.logger.WithError(err).Error("Failed to unmarshal position JSON")
		furniture.Position = make(map[string]interface{})
	}

	if err := json.Unmarshal(rotationJSON, &furniture.Rotation); err != nil {
		r.logger.WithError(err).Error("Failed to unmarshal rotation JSON")
		furniture.Rotation = make(map[string]interface{})
	}

	if err := json.Unmarshal(scaleJSON, &furniture.Scale); err != nil {
		r.logger.WithError(err).Error("Failed to unmarshal scale JSON")
		furniture.Scale = make(map[string]interface{})
	}

	return &furniture, nil
}

func (r *HousingRepository) ListPlacedFurniture(ctx context.Context, apartmentID uuid.UUID) ([]models.PlacedFurniture, error) {
	query := `
		SELECT id, apartment_id, furniture_item_id, position, rotation, scale,
			created_at, updated_at
		FROM housing.placed_furniture
		WHERE apartment_id = $1
		ORDER BY created_at
	`

	rows, err := r.db.Query(ctx, query, apartmentID)
	if err != nil {
		return nil, fmt.Errorf("failed to list placed furniture: %w", err)
	}
	defer rows.Close()

	var furniture []models.PlacedFurniture
	for rows.Next() {
		var f models.PlacedFurniture
		var positionJSON, rotationJSON, scaleJSON []byte

		err := rows.Scan(
			&f.ID, &f.ApartmentID, &f.FurnitureItemID,
			&positionJSON, &rotationJSON, &scaleJSON, &f.CreatedAt, &f.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan placed furniture: %w", err)
		}

		if err := json.Unmarshal(positionJSON, &f.Position); err != nil {
			f.Position = make(map[string]interface{})
		}

		if err := json.Unmarshal(rotationJSON, &f.Rotation); err != nil {
			f.Rotation = make(map[string]interface{})
		}

		if err := json.Unmarshal(scaleJSON, &f.Scale); err != nil {
			f.Scale = make(map[string]interface{})
		}

		furniture = append(furniture, f)
	}

	return furniture, nil
}

func (r *HousingRepository) UpdatePlacedFurniture(ctx context.Context, furniture *models.PlacedFurniture) error {
	positionJSON, err := json.Marshal(furniture.Position)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal position JSON")
		return fmt.Errorf("failed to marshal position: %w", err)
	}

	rotationJSON, err := json.Marshal(furniture.Rotation)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal rotation JSON")
		return fmt.Errorf("failed to marshal rotation: %w", err)
	}

	scaleJSON, err := json.Marshal(furniture.Scale)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal scale JSON")
		return fmt.Errorf("failed to marshal scale: %w", err)
	}

	query := `
		UPDATE housing.placed_furniture
		SET position = $1, rotation = $2, scale = $3, updated_at = $4
		WHERE id = $5
	`

	_, err = r.db.Exec(ctx, query,
		positionJSON, rotationJSON, scaleJSON, furniture.UpdatedAt, furniture.ID,
	)

	if err != nil {
		return fmt.Errorf("failed to update placed furniture: %w", err)
	}

	return nil
}

func (r *HousingRepository) DeletePlacedFurniture(ctx context.Context, furnitureID uuid.UUID) error {
	query := `DELETE FROM housing.placed_furniture WHERE id = $1`

	_, err := r.db.Exec(ctx, query, furnitureID)
	if err != nil {
		return fmt.Errorf("failed to delete placed furniture: %w", err)
	}

	return nil
}

func (r *HousingRepository) CountPlacedFurniture(ctx context.Context, apartmentID uuid.UUID) (int, error) {
	query := `SELECT COUNT(*) FROM housing.placed_furniture WHERE apartment_id = $1`

	var count int
	err := r.db.QueryRow(ctx, query, apartmentID).Scan(&count)
	if err != nil {
		return 0, fmt.Errorf("failed to count placed furniture: %w", err)
	}

	return count, nil
}

func (r *HousingRepository) CreateVisit(ctx context.Context, visit *models.ApartmentVisit) error {
	query := `
		INSERT INTO housing.apartment_visits (id, apartment_id, visitor_id, visited_at)
		VALUES ($1, $2, $3, $4)
	`

	_, err := r.db.Exec(ctx, query, visit.ID, visit.ApartmentID, visit.VisitorID, visit.VisitedAt)
	if err != nil {
		return fmt.Errorf("failed to create visit: %w", err)
	}

	return nil
}

func (r *HousingRepository) GetPrestigeLeaderboard(ctx context.Context, limit, offset int) ([]models.PrestigeLeaderboardEntry, int, error) {
	query := `
		SELECT a.id, a.owner_id, a.apartment_type, a.location, a.prestige_score,
			COALESCE(c.name, '') as owner_name
		FROM housing.apartments a
		LEFT JOIN mvp_core.characters c ON a.owner_id = c.id AND a.owner_type = 'character'
		WHERE a.is_public = true
		ORDER BY a.prestige_score DESC, a.created_at DESC
	`

	countQuery := "SELECT COUNT(*) FROM housing.apartments WHERE is_public = true"
	var total int
	err := r.db.QueryRow(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count leaderboard entries: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $1 OFFSET $2")

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get leaderboard: %w", err)
	}
	defer rows.Close()

	var entries []models.PrestigeLeaderboardEntry
	for rows.Next() {
		var entry models.PrestigeLeaderboardEntry
		var ownerName sql.NullString

		err := rows.Scan(
			&entry.ApartmentID, &entry.OwnerID, &entry.ApartmentType,
			&entry.Location, &entry.PrestigeScore, &ownerName,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan leaderboard entry: %w", err)
		}

		if ownerName.Valid {
			entry.OwnerName = ownerName.String
		}

		entries = append(entries, entry)
	}

	return entries, total, nil
}

