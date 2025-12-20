// Package server implements database operations for Cyberware Service
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/cyberware-service-go/models"
)

// CyberwareRepository handles database operations for cyberware
type CyberwareRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewCyberwareRepository creates a new cyberware repository
func NewCyberwareRepository(db *sql.DB, logger *zap.Logger) *CyberwareRepository {
	return &CyberwareRepository{
		db:     db,
		logger: logger,
	}
}

// GetImplantCatalog retrieves implants from catalog with filtering and pagination
func (r *CyberwareRepository) GetImplantCatalog(ctx context.Context, implantType, category, rarity string, limit, offset int) ([]*models.ImplantCatalogResponse, error) {
	query := `
		SELECT implant_id, name, description, type, category, rarity, stats, cyberpsychosis, cost, requirements, unlock_level
		FROM implant_catalog
		WHERE ($1 = '' OR type = $1)
		AND ($2 = '' OR category = $2)
		AND ($3 = '' OR rarity = $3)
		ORDER BY name
		LIMIT $4 OFFSET $5
	`

	rows, err := r.db.QueryContext(ctx, query, implantType, category, rarity, limit, offset)
	if err != nil {
		r.logger.Error("Failed to query implant catalog", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var implants []*models.ImplantCatalogResponse
	for rows.Next() {
		var implant models.ImplantCatalogResponse
		var statsJSON, requirementsJSON []byte

		err := rows.Scan(
			&implant.ImplantID, &implant.Name, &implant.Description,
			&implant.Type, &implant.Category, &implant.Rarity,
			&statsJSON, &implant.Cyberpsychosis, &implant.Cost,
			&requirementsJSON, &implant.UnlockLevel,
		)
		if err != nil {
			r.logger.Error("Failed to scan implant catalog row", zap.Error(err))
			return nil, err
		}

		// Parse JSON fields
		if err := json.Unmarshal(statsJSON, &implant.Stats); err != nil {
			r.logger.Error("Failed to unmarshal implant stats", zap.Error(err))
			return nil, err
		}

		if len(requirementsJSON) > 0 {
			if err := json.Unmarshal(requirementsJSON, &implant.Requirements); err != nil {
				r.logger.Error("Failed to unmarshal implant requirements", zap.Error(err))
				return nil, err
			}
		}

		implants = append(implants, &implant)
	}

	return implants, rows.Err()
}

// GetImplantByID retrieves a specific implant from catalog
func (r *CyberwareRepository) GetImplantByID(ctx context.Context, implantID string) (*models.ImplantCatalogResponse, error) {
	query := `
		SELECT implant_id, name, description, type, category, rarity, stats, cyberpsychosis, cost, requirements, unlock_level
		FROM implant_catalog
		WHERE implant_id = $1
	`

	var implant models.ImplantCatalogResponse
	var statsJSON, requirementsJSON []byte

	err := r.db.QueryRowContext(ctx, query, implantID).Scan(
		&implant.ImplantID, &implant.Name, &implant.Description,
		&implant.Type, &implant.Category, &implant.Rarity,
		&statsJSON, &implant.Cyberpsychosis, &implant.Cost,
		&requirementsJSON, &implant.UnlockLevel,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, fmt.Errorf("implant not found")
		}
		r.logger.Error("Failed to query implant by ID", zap.Error(err))
		return nil, err
	}

	// Parse JSON fields
	if err := json.Unmarshal(statsJSON, &implant.Stats); err != nil {
		r.logger.Error("Failed to unmarshal implant stats", zap.Error(err))
		return nil, err
	}

	if len(requirementsJSON) > 0 {
		if err := json.Unmarshal(requirementsJSON, &implant.Requirements); err != nil {
			r.logger.Error("Failed to unmarshal implant requirements", zap.Error(err))
			return nil, err
		}
	}

	return &implant, nil
}

// GetPlayerImplants retrieves all implants for a player
func (r *CyberwareRepository) GetPlayerImplants(ctx context.Context, userID string) ([]*models.PlayerImplant, error) {
	query := `
		SELECT implant_id, name, type, category, level, active, slot, stats, cyberpsychosis, installed_at, last_used_at
		FROM player_implants
		WHERE user_id = $1
		ORDER BY slot
	`

	rows, err := r.db.QueryContext(ctx, query, userID)
	if err != nil {
		r.logger.Error("Failed to query player implants", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var implants []*models.PlayerImplant
	for rows.Next() {
		var implant models.PlayerImplant
		var statsJSON []byte

		err := rows.Scan(
			&implant.ImplantID, &implant.Name, &implant.Type, &implant.Category,
			&implant.Level, &implant.Active, &implant.Slot, &statsJSON,
			&implant.Cyberpsychosis, &implant.InstalledAt, &implant.LastUsedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan player implant row", zap.Error(err))
			return nil, err
		}

		// Parse JSON stats
		if err := json.Unmarshal(statsJSON, &implant.Stats); err != nil {
			r.logger.Error("Failed to unmarshal implant stats", zap.Error(err))
			return nil, err
		}

		implants = append(implants, &implant)
	}

	return implants, rows.Err()
}

// InstallImplant installs a new implant for the player
func (r *CyberwareRepository) InstallImplant(ctx context.Context, userID string, implant *models.PlayerImplant) error {
	statsJSON, err := json.Marshal(implant.Stats)
	if err != nil {
		r.logger.Error("Failed to marshal implant stats", zap.Error(err))
		return err
	}

	query := `
		INSERT INTO player_implants (
			user_id, implant_id, name, type, category, level, active, slot,
			stats, cyberpsychosis, installed_at, last_used_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
		ON CONFLICT (user_id, implant_id) DO UPDATE SET
			level = EXCLUDED.level,
			active = EXCLUDED.active,
			slot = EXCLUDED.slot,
			stats = EXCLUDED.stats,
			cyberpsychosis = EXCLUDED.cyberpsychosis,
			last_used_at = EXCLUDED.last_used_at
	`

	_, err = r.db.ExecContext(ctx, query,
		userID, implant.ImplantID, implant.Name, implant.Type, implant.Category,
		implant.Level, implant.Active, implant.Slot, statsJSON, implant.Cyberpsychosis,
		implant.InstalledAt, implant.LastUsedAt,
	)

	if err != nil {
		r.logger.Error("Failed to install implant", zap.Error(err))
		return err
	}

	r.logger.Info("Implant installed successfully",
		zap.String("user_id", userID),
		zap.String("implant_id", implant.ImplantID),
		zap.Int("slot", implant.Slot))

	return nil
}

// UpdateImplant updates an existing implant
func (r *CyberwareRepository) UpdateImplant(ctx context.Context, userID string, implant *models.PlayerImplant) error {
	statsJSON, err := json.Marshal(implant.Stats)
	if err != nil {
		r.logger.Error("Failed to marshal implant stats", zap.Error(err))
		return err
	}

	query := `
		UPDATE player_implants
		SET level = $1, active = $2, stats = $3, cyberpsychosis = $4, last_used_at = $5
		WHERE user_id = $6 AND implant_id = $7
	`

	result, err := r.db.ExecContext(ctx, query,
		implant.Level, implant.Active, statsJSON, implant.Cyberpsychosis,
		implant.LastUsedAt, userID, implant.ImplantID,
	)

	if err != nil {
		r.logger.Error("Failed to update implant", zap.Error(err))
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return err
	}

	if rowsAffected == 0 {
		return fmt.Errorf("implant not found")
	}

	return nil
}

// ActivateImplant activates or deactivates an implant
func (r *CyberwareRepository) ActivateImplant(ctx context.Context, userID, implantID string, active bool) error {
	query := `
		UPDATE player_implants
		SET active = $1, last_used_at = CASE WHEN $1 THEN NOW() ELSE last_used_at END
		WHERE user_id = $2 AND implant_id = $3
	`

	result, err := r.db.ExecContext(ctx, query, active, userID, implantID)
	if err != nil {
		r.logger.Error("Failed to activate/deactivate implant", zap.Error(err))
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return err
	}

	if rowsAffected == 0 {
		return fmt.Errorf("implant not found")
	}

	r.logger.Info("Implant activation status updated",
		zap.String("user_id", userID),
		zap.String("implant_id", implantID),
		zap.Bool("active", active))

	return nil
}

// GetImplantStats returns comprehensive statistics about implant usage
func (r *CyberwareRepository) GetImplantStats(ctx context.Context) (*models.ImplantStatsResponse, error) {
	// Get total implants count
	var totalImplants int
	err := r.db.QueryRowContext(ctx, "SELECT COUNT(*) FROM player_implants").Scan(&totalImplants)
	if err != nil {
		r.logger.Error("Failed to get total implants count", zap.Error(err))
		return nil, err
	}

	// Get active implants count
	var activeImplants int
	err = r.db.QueryRowContext(ctx, "SELECT COUNT(*) FROM player_implants WHERE active = true").Scan(&activeImplants)
	if err != nil {
		r.logger.Error("Failed to get active implants count", zap.Error(err))
		return nil, err
	}

	// Get type breakdown
	typeBreakdown := make(map[models.ImplantType]int)
	rows, err := r.db.QueryContext(ctx, "SELECT type, COUNT(*) FROM player_implants GROUP BY type")
	if err != nil {
		r.logger.Error("Failed to get type breakdown", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var implantType models.ImplantType
		var count int
		if err := rows.Scan(&implantType, &count); err != nil {
			r.logger.Error("Failed to scan type breakdown", zap.Error(err))
			return nil, err
		}
		typeBreakdown[implantType] = count
	}

	// Get category stats
	categoryStats := make(map[models.ImplantCategory]int)
	rows, err = r.db.QueryContext(ctx, "SELECT category, COUNT(*) FROM player_implants GROUP BY category")
	if err != nil {
		r.logger.Error("Failed to get category stats", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var category models.ImplantCategory
		var count int
		if err := rows.Scan(&category, &count); err != nil {
			r.logger.Error("Failed to scan category stats", zap.Error(err))
			return nil, err
		}
		categoryStats[category] = count
	}

	// Get cyberpsychosis stats
	var cyberpsychosisStats models.CyberpsychosisStats
	err = r.db.QueryRowContext(ctx, `
		SELECT AVG(cyberpsychosis), MIN(cyberpsychosis), MAX(cyberpsychosis), COUNT(*)
		FROM player_implants WHERE active = true
	`).Scan(&cyberpsychosisStats.Average, &cyberpsychosisStats.Min,
		&cyberpsychosisStats.Max, &cyberpsychosisStats.Count)
	if err != nil {
		r.logger.Error("Failed to get cyberpsychosis stats", zap.Error(err))
		return nil, err
	}

	// Get average level
	var averageLevel float64
	err = r.db.QueryRowContext(ctx, "SELECT AVG(level) FROM player_implants").Scan(&averageLevel)
	if err != nil {
		r.logger.Error("Failed to get average level", zap.Error(err))
		return nil, err
	}

	response := &models.ImplantStatsResponse{
		TotalImplants:  totalImplants,
		ActiveImplants: activeImplants,
		TypeBreakdown:  typeBreakdown,
		CategoryStats:  categoryStats,
		AverageLevel:   averageLevel,
		Cyberpsychosis: cyberpsychosisStats,
		// TODO: Populate Popularity and SynergyBonuses
	}

	return response, nil
}

// GetImplantUpgradeCosts GetImplantByID retrieves a specific implant by ID
// GetImplantUpgradeCosts retrieves upgrade costs for an implant
func (r *CyberwareRepository) GetImplantUpgradeCosts() ([]map[string]interface{}, error) {
	// This would be implemented with a proper upgrade costs table
	// For now, return empty array
	return []map[string]interface{}{}, nil
}

// GetCharacterImplants retrieves all implants for a character
func (r *CyberwareRepository) GetCharacterImplants(ctx context.Context, characterID string, activeOnly bool) ([]*models.CharacterImplant, error) {
	query := `
		SELECT ci.id, ci.implant_id, ic.name, ic.type, ic.category, ci.upgrade_level, ic.max_level,
			   ci.slot, ci.is_active, ci.installed_at, ic.effects
		FROM cyberware.character_implants ci
		JOIN cyberware.implants_catalog ic ON ci.implant_id = ic.id
		WHERE ci.character_id = $1
	`

	if activeOnly {
		query += " AND ci.is_active = true"
	}

	query += " ORDER BY ci.installed_at DESC"

	rows, err := r.db.QueryContext(ctx, query, characterID)
	if err != nil {
		return nil, fmt.Errorf("failed to query character implants: %w", err)
	}
	defer rows.Close()

	var implants []*models.CharacterImplant
	for rows.Next() {
		var implant models.CharacterImplant
		var effectsJSON []byte

		err := rows.Scan(
			&implant.ID, &implant.ImplantID, &implant.Name, &implant.Type, &implant.Category,
			&implant.CurrentLevel, &implant.MaxLevel, &implant.Slot, &implant.IsActive,
			&implant.InstalledAt, &effectsJSON,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan character implant row: %w", err)
		}

		// Unmarshal effects
		if err := json.Unmarshal(effectsJSON, &implant.Effects); err != nil {
			r.logger.Warn("Failed to unmarshal implant effects", zap.Error(err), zap.String("implant_id", implant.ImplantID))
			implant.Effects = make(map[string]interface{})
		}

		implants = append(implants, &implant)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating character implant rows: %w", err)
	}

	return implants, nil
}

// CharacterOwnsImplant checks if a character owns a specific implant
func (r *CyberwareRepository) CharacterOwnsImplant(ctx context.Context, characterID, implantID string) (bool, error) {
	query := `
		SELECT EXISTS(
			SELECT 1 FROM cyberware.implant_acquisitions
			WHERE character_id = $1 AND implant_id = $2
		)
	`

	var exists bool
	err := r.db.QueryRowContext(ctx, query, characterID, implantID).Scan(&exists)
	if err != nil {
		return false, fmt.Errorf("failed to check implant ownership: %w", err)
	}

	return exists, nil
}

// RecordImplantAcquisition records a new implant acquisition
func (r *CyberwareRepository) RecordImplantAcquisition(ctx context.Context, acquisition *models.ImplantAcquisition) error {
	query := `
		INSERT INTO cyberware.implant_acquisitions (id, character_id, implant_id, acquisition_type, cost, acquired_at)
		VALUES ($1, $2, $3, $4, $5, $6)
	`

	costJSON, err := json.Marshal(acquisition.Cost)
	if err != nil {
		return fmt.Errorf("failed to marshal cost: %w", err)
	}

	_, err = r.db.ExecContext(ctx, query,
		acquisition.ID, acquisition.CharacterID, acquisition.ImplantID,
		acquisition.AcquisitionType, costJSON, acquisition.AcquiredAt,
	)

	if err != nil {
		return fmt.Errorf("failed to record implant acquisition: %w", err)
	}

	r.logger.Info("Recorded implant acquisition",
		zap.String("acquisition_id", acquisition.ID),
		zap.String("character_id", acquisition.CharacterID),
		zap.String("implant_id", acquisition.ImplantID),
		zap.String("type", acquisition.AcquisitionType),
	)

	return nil
}

// GetCharacterLimits retrieves implant limits for a character
func (r *CyberwareRepository) GetCharacterLimits(ctx context.Context, characterID string) (*models.ImplantLimitsState, error) {
	query := `
		SELECT id, character_id, total_energy_used, max_energy, total_humanity_lost, max_humanity, slots_used, last_update
		FROM cyberware.implant_limits_state
		WHERE character_id = $1
	`

	var limits models.ImplantLimitsState
	var slotsUsedJSON []byte

	err := r.db.QueryRowContext(ctx, query, characterID).Scan(
		&limits.ID, &limits.CharacterID, &limits.TotalEnergyUsed, &limits.MaxEnergy,
		&limits.TotalHumanityLost, &limits.MaxHumanity, &slotsUsedJSON, &limits.LastUpdate,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Return default limits for new characters
			return &models.ImplantLimitsState{
				CharacterID: characterID,
				MaxEnergy:   100,
				MaxHumanity: 100,
				SlotsUsed:   make(map[string]interface{}),
				LastUpdate:  time.Now(),
			}, nil
		}
		return nil, fmt.Errorf("failed to get character limits: %w", err)
	}

	// Unmarshal slots used
	if err := json.Unmarshal(slotsUsedJSON, &limits.SlotsUsed); err != nil {
		r.logger.Warn("Failed to unmarshal slots used", zap.Error(err), zap.String("character_id", characterID))
		limits.SlotsUsed = make(map[string]interface{})
	}

	return &limits, nil
}

// InstallImplant installs an implant for a character

// UpdateCyberpsychosis updates cyberpsychosis level for a character
func (r *CyberwareRepository) UpdateCyberpsychosis(ctx context.Context, characterID string, increase int) error {
	query := `
		INSERT INTO cyberware.cyberpsychosis_state (id, character_id, current_level, threshold_level, effects_active, last_update)
		VALUES (gen_random_uuid(), $1, $2, 100, '[]'::jsonb, NOW())
		ON CONFLICT (character_id)
		DO UPDATE SET
			current_level = cyberpsychosis_state.current_level + $2,
			last_update = NOW()
	`

	_, err := r.db.ExecContext(ctx, query, characterID, increase)
	if err != nil {
		return fmt.Errorf("failed to update cyberpsychosis: %w", err)
	}

	r.logger.Info("Updated cyberpsychosis",
		zap.String("character_id", characterID),
		zap.Int("increase", increase),
	)

	return nil
}

// UpdateLimits updates implant limits for a character
func (r *CyberwareRepository) UpdateLimits(ctx context.Context, characterID string, energyCost, humanityCost int, slot string) error {
	// Update energy and humanity
	energyQuery := `
		UPDATE cyberware.implant_limits_state
		SET total_energy_used = total_energy_used + $2,
			total_humanity_lost = total_humanity_lost + $3,
			last_update = NOW()
		WHERE character_id = $1
	`

	_, err := r.db.ExecContext(ctx, energyQuery, characterID, energyCost, humanityCost)
	if err != nil {
		return fmt.Errorf("failed to update energy/humanity limits: %w", err)
	}

	// Update slot usage (simplified - would need proper slot type tracking)
	r.logger.Info("Updated implant limits",
		zap.String("character_id", characterID),
		zap.Int("energy_cost", energyCost),
		zap.Int("humanity_cost", humanityCost),
		zap.String("slot", slot),
	)

	return nil
}

// UninstallImplant Placeholder methods for future implementation
func (r *CyberwareRepository) UninstallImplant() error {
	// TODO: Implement uninstall logic
	return fmt.Errorf("uninstall not implemented")
}

func (r *CyberwareRepository) UpgradeImplant() error {
	// TODO: Implement upgrade logic
	return fmt.Errorf("upgrade not implemented")
}

func (r *CyberwareRepository) CheckCompatibility() ([]models.CompatibilityConflict, error) {
	// TODO: Implement compatibility checking
	return []models.CompatibilityConflict{}, nil
}

func (r *CyberwareRepository) GetCyberpsychosisState(characterID string) (*models.CyberpsychosisState, error) {
	// TODO: Implement cyberpsychosis state retrieval
	return &models.CyberpsychosisState{
		CharacterID:    characterID,
		CurrentLevel:   0,
		ThresholdLevel: 100,
		EffectsActive:  []models.CyberpsychosisEffect{},
		LastUpdate:     time.Now(),
	}, nil
}

func (r *CyberwareRepository) GetActiveSynergies() ([]*models.ImplantSynergy, error) {
	// TODO: Implement synergy retrieval
	return []*models.ImplantSynergy{}, nil
}

func (r *CyberwareRepository) GetImplantVisuals() ([]*models.ImplantVisuals, error) {
	// TODO: Implement visuals retrieval
	return []*models.ImplantVisuals{}, nil
}

func (r *CyberwareRepository) UpdateImplantVisuals() error {
	// TODO: Implement visuals update
	return fmt.Errorf("visuals update not implemented")
}
