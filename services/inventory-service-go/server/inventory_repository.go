// Issue: #141887950
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/inventory-service-go/models"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type InventoryRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewInventoryRepository(db *pgxpool.Pool) *InventoryRepository {
	return &InventoryRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *InventoryRepository) GetInventoryByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.Inventory, error) {
	var inv models.Inventory
	err := r.db.QueryRow(ctx,
		`SELECT id, character_id, capacity, used_slots, weight, max_weight, created_at, updated_at
		 FROM mvp_core.character_inventory
		 WHERE character_id = $1 AND deleted_at IS NULL`,
		characterID,
	).Scan(&inv.ID, &inv.CharacterID, &inv.Capacity, &inv.UsedSlots, &inv.Weight, &inv.MaxWeight, &inv.CreatedAt, &inv.UpdatedAt)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get inventory")
		return nil, err
	}

	return &inv, nil
}

func (r *InventoryRepository) CreateInventory(ctx context.Context, characterID uuid.UUID, capacity int, maxWeight float64) (*models.Inventory, error) {
	inv := &models.Inventory{
		ID:          uuid.New(),
		CharacterID: characterID,
		Capacity:    capacity,
		UsedSlots:   0,
		Weight:      0,
		MaxWeight:   maxWeight,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	_, err := r.db.Exec(ctx,
		`INSERT INTO mvp_core.character_inventory (id, character_id, capacity, used_slots, weight, max_weight, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
		inv.ID, inv.CharacterID, inv.Capacity, inv.UsedSlots, inv.Weight, inv.MaxWeight, inv.CreatedAt, inv.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create inventory")
		return nil, err
	}

	return inv, nil
}

func (r *InventoryRepository) GetInventoryItems(ctx context.Context, inventoryID uuid.UUID) ([]models.InventoryItem, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, inventory_id, item_id, slot_index, stack_count, max_stack_size, is_equipped, equip_slot, metadata, created_at, updated_at
		 FROM mvp_core.character_items
		 WHERE inventory_id = $1 AND deleted_at IS NULL
		 ORDER BY slot_index`,
		inventoryID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get inventory items")
		return nil, err
	}
	defer rows.Close()

	var items []models.InventoryItem
	for rows.Next() {
		var item models.InventoryItem
		var metadataJSON sql.NullString
		var equipSlot sql.NullString

		err := rows.Scan(
			&item.ID, &item.InventoryID, &item.ItemID, &item.SlotIndex, &item.StackCount,
			&item.MaxStackSize, &item.IsEquipped, &equipSlot, &metadataJSON, &item.CreatedAt, &item.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan inventory item")
			continue
		}

		if equipSlot.Valid {
			item.EquipSlot = equipSlot.String
		}

		if metadataJSON.Valid && metadataJSON.String != "" {
			if err := json.Unmarshal([]byte(metadataJSON.String), &item.Metadata); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
				item.Metadata = make(map[string]interface{})
			}
		}

		items = append(items, item)
	}

	return items, nil
}

func (r *InventoryRepository) AddItem(ctx context.Context, item *models.InventoryItem) error {
	metadataJSON, err := json.Marshal(item.Metadata)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal metadata JSON")
		return err
	}
	
	_, err = r.db.Exec(ctx,
		`INSERT INTO mvp_core.character_items (id, inventory_id, item_id, slot_index, stack_count, max_stack_size, is_equipped, equip_slot, metadata, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)`,
		item.ID, item.InventoryID, item.ItemID, item.SlotIndex, item.StackCount,
		item.MaxStackSize, item.IsEquipped, item.EquipSlot, metadataJSON, item.CreatedAt, item.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to add item")
		return err
	}

	return nil
}

func (r *InventoryRepository) UpdateItem(ctx context.Context, item *models.InventoryItem) error {
	metadataJSON, _ := json.Marshal(item.Metadata)
	
	_, err := r.db.Exec(ctx,
		`UPDATE mvp_core.character_items
		 SET slot_index = $1, stack_count = $2, is_equipped = $3, equip_slot = $4, metadata = $5, updated_at = $6
		 WHERE id = $7`,
		item.SlotIndex, item.StackCount, item.IsEquipped, item.EquipSlot, metadataJSON, time.Now(), item.ID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update item")
		return err
	}

	return nil
}

func (r *InventoryRepository) RemoveItem(ctx context.Context, itemID uuid.UUID) error {
	_, err := r.db.Exec(ctx,
		`UPDATE mvp_core.character_items
		 SET deleted_at = $1
		 WHERE id = $2`,
		time.Now(), itemID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to remove item")
		return err
	}

	return nil
}

func (r *InventoryRepository) UpdateInventoryStats(ctx context.Context, inventoryID uuid.UUID, usedSlots int, weight float64) error {
	_, err := r.db.Exec(ctx,
		`UPDATE mvp_core.character_inventory
		 SET used_slots = $1, weight = $2, updated_at = $3
		 WHERE id = $4`,
		usedSlots, weight, time.Now(), inventoryID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update inventory stats")
		return err
	}

	return nil
}

func (r *InventoryRepository) GetItemTemplate(ctx context.Context, itemID string) (*models.ItemTemplate, error) {
	var template models.ItemTemplate
	var requirementsJSON sql.NullString
	var statsJSON sql.NullString
	var metadataJSON sql.NullString
	var equipSlot sql.NullString

	err := r.db.QueryRow(ctx,
		`SELECT id, name, type, rarity, max_stack_size, weight, can_equip, equip_slot, requirements, stats, metadata
		 FROM mvp_core.item_templates
		 WHERE id = $1`,
		itemID,
	).Scan(
		&template.ID, &template.Name, &template.Type, &template.Rarity, &template.MaxStackSize,
		&template.Weight, &template.CanEquip, &equipSlot, &requirementsJSON, &statsJSON, &metadataJSON,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get item template")
		return nil, err
	}

	if equipSlot.Valid {
		template.EquipSlot = equipSlot.String
	}

	if requirementsJSON.Valid && requirementsJSON.String != "" {
		if err := json.Unmarshal([]byte(requirementsJSON.String), &template.Requirements); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal requirements JSON")
			template.Requirements = make(map[string]interface{})
		}
	}

	if statsJSON.Valid && statsJSON.String != "" {
		if err := json.Unmarshal([]byte(statsJSON.String), &template.Stats); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal stats JSON")
			template.Stats = make(map[string]interface{})
		}
	}

	if metadataJSON.Valid && metadataJSON.String != "" {
		if err := json.Unmarshal([]byte(metadataJSON.String), &template.Metadata); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal metadata JSON")
			template.Metadata = make(map[string]interface{})
		}
	}

	return &template, nil
}
