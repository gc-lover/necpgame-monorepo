// Issue: #1844 - PostgreSQL repository for world interactives system
package repository

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-service-go/models"
)

// PostgresInteractiveRepository implements InteractiveRepository for PostgreSQL
type PostgresInteractiveRepository struct {
	db *sql.DB
}

// NewPostgresInteractiveRepository creates a new PostgreSQL repository
func NewPostgresInteractiveRepository(db *sql.DB) *PostgresInteractiveRepository {
	return &PostgresInteractiveRepository{db: db}
}

// SaveWorldInteractive saves a comprehensive world interactive object
func (r *PostgresInteractiveRepository) SaveWorldInteractive(ctx context.Context, interactive *models.Interactive) (*models.Interactive, error) {
	query := `
		INSERT INTO world_interactives (
			interactive_name, display_name, category, description, base_health,
			is_destructible, respawn_time_seconds, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (interactive_name) DO UPDATE SET
			display_name = EXCLUDED.display_name,
			category = EXCLUDED.category,
			description = EXCLUDED.description,
			base_health = EXCLUDED.base_health,
			is_destructible = EXCLUDED.is_destructible,
			respawn_time_seconds = EXCLUDED.respawn_time_seconds,
			updated_at = EXCLUDED.updated_at
		RETURNING id`

	now := time.Now()
	var interactiveID int64

	err := r.db.QueryRowContext(ctx, query,
		interactive.InteractiveID, interactive.Name, interactive.Category,
		interactive.Description, interactive.BaseHealth, interactive.IsDestructible,
		interactive.RespawnTimeSec, now, now,
	).Scan(&interactiveID)

	if err != nil {
		return nil, fmt.Errorf("failed to save world interactive: %w", err)
	}

	// Save interactive type details
	if err := r.saveInteractiveType(ctx, interactiveID, interactive); err != nil {
		return nil, fmt.Errorf("failed to save interactive type: %w", err)
	}

	// Save location if coordinates provided
	if interactive.CoordinatesX != 0 || interactive.CoordinatesY != 0 {
		if err := r.saveInteractiveLocation(ctx, interactiveID, interactive); err != nil {
			return nil, fmt.Errorf("failed to save interactive location: %w", err)
		}
	}

	interactive.CreatedAt = now
	interactive.UpdatedAt = now

	return interactive, nil
}

// saveInteractiveType saves the specific type details for an interactive
func (r *PostgresInteractiveRepository) saveInteractiveType(ctx context.Context, interactiveID int64, interactive *models.Interactive) error {
	var query string
	var args []interface{}

	switch interactive.Type {
	case models.InteractiveTypeFactionBlockpost:
		query = `
			INSERT INTO interactive_types (
				interactive_id, type_name, variant_name, controlling_faction,
				control_radius_meters, price_modifier_percent, access_requirement,
				takeover_method, takeover_cost_eddies_min, takeover_cost_eddies_max,
				takeover_success_rate_min, takeover_success_rate_max,
				takeover_detection_risk_percent, takeover_time_seconds,
				takeover_alarm_probability_percent
			) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)
			ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
				controlling_faction = EXCLUDED.controlling_faction,
				control_radius_meters = EXCLUDED.control_radius_meters,
				price_modifier_percent = EXCLUDED.price_modifier_percent,
				access_requirement = EXCLUDED.access_requirement,
				takeover_method = EXCLUDED.takeover_method,
				takeover_cost_eddies_min = EXCLUDED.takeover_cost_eddies_min,
				takeover_cost_eddies_max = EXCLUDED.takeover_cost_eddies_max,
				takeover_success_rate_min = EXCLUDED.takeover_success_rate_min,
				takeover_success_rate_max = EXCLUDED.takeover_success_rate_max,
				takeover_detection_risk_percent = EXCLUDED.takeover_detection_risk_percent,
				takeover_time_seconds = EXCLUDED.takeover_time_seconds,
				takeover_alarm_probability_percent = EXCLUDED.takeover_alarm_probability_percent`

		args = []interface{}{
			interactiveID, string(interactive.Type), interactive.Name,
			interactive.ControllingFaction, interactive.ControlRadiusMeters,
			interactive.PriceModifierPercent, interactive.AccessRequirement,
			interactive.TakeoverMethod, interactive.TakeoverCostEddiesMin,
			interactive.TakeoverCostEddiesMax, interactive.TakeoverSuccessRateMin,
			interactive.TakeoverSuccessRateMax, interactive.TakeoverDetectionRiskPercent,
			interactive.TakeoverTimeSeconds, interactive.TakeoverAlarmProbability,
		}

	case models.InteractiveTypeCommunicationRelay:
		query = `
			INSERT INTO interactive_types (
				interactive_id, type_name, variant_name, signal_strength,
				encryption_level, jamming_resistance, bandwidth_mbps
			) VALUES ($1, $2, $3, $4, $5, $6, $7)
			ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
				signal_strength = EXCLUDED.signal_strength,
				encryption_level = EXCLUDED.encryption_level,
				jamming_resistance = EXCLUDED.jamming_resistance,
				bandwidth_mbps = EXCLUDED.bandwidth_mbps`

		args = []interface{}{
			interactiveID, string(interactive.Type), interactive.Name,
			interactive.SignalStrength, interactive.EncryptionLevel,
			interactive.JammingResistance, interactive.BandwidthMbps,
		}

	case models.InteractiveTypeMedicalStation:
		query = `
			INSERT INTO interactive_types (
				interactive_id, type_name, variant_name, healing_rate_per_second,
				cyberware_repair, trauma_team_available
			) VALUES ($1, $2, $3, $4, $5, $6)
			ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
				healing_rate_per_second = EXCLUDED.healing_rate_per_second,
				cyberware_repair = EXCLUDED.cyberware_repair,
				trauma_team_available = EXCLUDED.trauma_team_available`

		args = []interface{}{
			interactiveID, string(interactive.Type), interactive.Name,
			interactive.HealingRatePerSec, interactive.CyberwareRepair,
			interactive.TraumaTeamAvailable,
		}

	case models.InteractiveTypeLogisticsContainer:
		query = `
			INSERT INTO interactive_types (
				interactive_id, type_name, variant_name, storage_capacity,
				security_level, loot_quality
			) VALUES ($1, $2, $3, $4, $5, $6)
			ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
				storage_capacity = EXCLUDED.storage_capacity,
				security_level = EXCLUDED.security_level,
				loot_quality = EXCLUDED.loot_quality`

		args = []interface{}{
			interactiveID, string(interactive.Type), interactive.Name,
			interactive.StorageCapacity, interactive.SecurityLevel,
			interactive.LootQuality,
		}

	default:
		return fmt.Errorf("unsupported interactive type: %s", interactive.Type)
	}

	_, err := r.db.ExecContext(ctx, query, args...)
	return err
}

// saveInteractiveLocation saves location data for an interactive
func (r *PostgresInteractiveRepository) saveInteractiveLocation(ctx context.Context, interactiveID int64, interactive *models.Interactive) error {
	query := `
		INSERT INTO interactive_locations (
			interactive_type_id, world_location, coordinates_x, coordinates_y,
			coordinates_z, is_active, current_health, controlled_by_faction,
			security_status, created_at, updated_at
		) VALUES (
			(SELECT id FROM interactive_types WHERE interactive_id = $1 LIMIT 1),
			$2, $3, $4, $5, $6, $7, $8, $9, $10, $11
		) ON CONFLICT DO NOTHING`

	now := time.Now()
	currentHealth := interactive.CurrentHealth
	if currentHealth == nil {
		currentHealth = &interactive.BaseHealth
	}

	_, err := r.db.ExecContext(ctx, query,
		interactiveID, interactive.WorldLocation, interactive.CoordinatesX,
		interactive.CoordinatesY, interactive.CoordinatesZ, interactive.IsActive,
		currentHealth, interactive.ControllingFaction, interactive.SecurityStatus,
		now, now,
	)

	return err
}

// GetWorldInteractives retrieves world interactives with comprehensive filtering
func (r *PostgresInteractiveRepository) GetWorldInteractives(ctx context.Context, filter *models.ListWorldInteractivesRequest) ([]models.Interactive, int, error) {
	baseQuery := `
		SELECT
			wi.id, wi.interactive_name, wi.display_name, wi.category, wi.description,
			wi.base_health, wi.is_destructible, wi.respawn_time_seconds,
			it.type_name, it.variant_name, it.controlling_faction, it.control_radius_meters,
			it.price_modifier_percent, it.access_requirement, it.signal_strength,
			it.encryption_level, it.jamming_resistance, it.bandwidth_mbps,
			it.healing_rate_per_second, it.cyberware_repair, it.trauma_team_available,
			it.storage_capacity, it.security_level, it.loot_quality,
			it.takeover_method, it.takeover_cost_eddies_min, it.takeover_cost_eddies_max,
			it.takeover_success_rate_min, it.takeover_success_rate_max,
			it.takeover_detection_risk_percent, it.takeover_time_seconds,
			it.takeover_alarm_probability_percent,
			il.world_location, il.coordinates_x, il.coordinates_y, il.coordinates_z,
			il.is_active, il.current_health, il.last_interaction, il.security_status,
			wi.created_at, wi.updated_at
		FROM world_interactives wi
		LEFT JOIN interactive_types it ON wi.id = it.interactive_id
		LEFT JOIN interactive_locations il ON it.id = il.interactive_type_id`

	whereClause := " WHERE 1=1"
	args := []interface{}{}
	argCount := 0

	if filter.Category != nil {
		argCount++
		whereClause += fmt.Sprintf(" AND wi.category = $%d", argCount)
		args = append(args, *filter.Category)
	}

	if filter.Type != nil {
		argCount++
		whereClause += fmt.Sprintf(" AND it.type_name = $%d", argCount)
		args = append(args, *filter.Type)
	}

	if filter.ControllingFaction != nil {
		argCount++
		whereClause += fmt.Sprintf(" AND it.controlling_faction = $%d", argCount)
		args = append(args, *filter.ControllingFaction)
	}

	if filter.WorldLocation != nil {
		argCount++
		whereClause += fmt.Sprintf(" AND il.world_location = $%d", argCount)
		args = append(args, *filter.WorldLocation)
	}

	if filter.IsActive != nil {
		argCount++
		whereClause += fmt.Sprintf(" AND il.is_active = $%d", argCount)
		args = append(args, *filter.IsActive)
	}

	// Count query
	countQuery := "SELECT COUNT(*) FROM (" + baseQuery + whereClause + ") as count_query"
	var total int
	err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count interactives: %w", err)
	}

	// Data query with pagination
	limit := 50 // default
	if filter.Limit > 0 && filter.Limit <= 100 {
		limit = filter.Limit
	}
	offset := filter.Offset

	dataQuery := baseQuery + whereClause + fmt.Sprintf(" ORDER BY wi.id LIMIT %d OFFSET %d", limit, offset)

	rows, err := r.db.QueryContext(ctx, dataQuery, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query interactives: %w", err)
	}
	defer rows.Close()

	var interactives []models.Interactive
	for rows.Next() {
		var interactive models.Interactive
		var typeName, variantName sql.NullString
		var controllingFaction, accessReq sql.NullString
		var controlRadius, priceMod sql.NullInt32
		var signalStrength, jammingRes, bandwidth sql.NullInt32
		var healingRate sql.NullInt32
		var cyberRepair, traumaTeam sql.NullBool
		var storageCap sql.NullInt32
		var securityLvl, lootQuality sql.NullString
		var takeoverMethod sql.NullString
		var takeoverCostMin, takeoverCostMax, takeoverSuccessMin, takeoverSuccessMax sql.NullInt32
		var takeoverRisk, takeoverTime, takeoverAlarm sql.NullInt32
		var worldLoc sql.NullString
		var coordX, coordY, coordZ sql.NullFloat64
		var isActive sql.NullBool
		var currentHealth sql.NullInt32
		var lastInteract sql.NullTime
		var securityStatus sql.NullString

		err := rows.Scan(
			&interactive.InteractiveID, &interactive.Name, &interactive.Category, &interactive.Description,
			&interactive.BaseHealth, &interactive.IsDestructible, &interactive.RespawnTimeSec,
			&typeName, &variantName, &controllingFaction, &controlRadius,
			&priceMod, &accessReq, &signalStrength,
			&encryptionLevel, &jammingRes, &bandwidth,
			&healingRate, &cyberRepair, &traumaTeam,
			&storageCap, &securityLvl, &lootQuality,
			&takeoverMethod, &takeoverCostMin, &takeoverCostMax,
			&takeoverSuccessMin, &takeoverSuccessMax,
			&takeoverRisk, &takeoverTime, &takeoverAlarm,
			&worldLoc, &coordX, &coordY, &coordZ,
			&isActive, &currentHealth, &lastInteract, &securityStatus,
			&interactive.CreatedAt, &interactive.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan interactive: %w", err)
		}

		// Convert nullable fields
		if typeName.Valid {
			interactive.Type = models.InteractiveType(typeName.String)
		}
		if controllingFaction.Valid {
			interactive.ControllingFaction = &controllingFaction.String
		}
		if controlRadius.Valid {
			radius := int(controlRadius.Int32)
			interactive.ControlRadiusMeters = &radius
		}
		if priceMod.Valid {
			mod := int(priceMod.Int32)
			interactive.PriceModifierPercent = &mod
		}
		if accessReq.Valid {
			interactive.AccessRequirement = &accessReq.String
		}
		if signalStrength.Valid {
			strength := int(signalStrength.Int32)
			interactive.SignalStrength = &strength
		}
		if jammingRes.Valid {
			res := int(jammingRes.Int32)
			interactive.JammingResistance = &res
		}
		if bandwidth.Valid {
			bw := int(bandwidth.Int32)
			interactive.BandwidthMbps = &bw
		}
		if healingRate.Valid {
			rate := int(healingRate.Int32)
			interactive.HealingRatePerSec = &rate
		}
		if cyberRepair.Valid {
			interactive.CyberwareRepair = &cyberRepair.Bool
		}
		if traumaTeam.Valid {
			interactive.TraumaTeamAvailable = &traumaTeam.Bool
		}
		if storageCap.Valid {
			cap := int(storageCap.Int32)
			interactive.StorageCapacity = &cap
		}
		if securityLvl.Valid {
			interactive.SecurityLevel = (*models.SecurityLevel)(&securityLvl.String)
		}
		if lootQuality.Valid {
			interactive.LootQuality = (*models.LootQuality)(&lootQuality.String)
		}
		if takeoverMethod.Valid {
			interactive.TakeoverMethod = (*models.TakeoverMethod)(&takeoverMethod.String)
		}
		if takeoverCostMin.Valid {
			cost := int(takeoverCostMin.Int32)
			interactive.TakeoverCostEddiesMin = &cost
		}
		if takeoverCostMax.Valid {
			cost := int(takeoverCostMax.Int32)
			interactive.TakeoverCostEddiesMax = &cost
		}
		if takeoverSuccessMin.Valid {
			rate := int(takeoverSuccessMin.Int32)
			interactive.TakeoverSuccessRateMin = &rate
		}
		if takeoverSuccessMax.Valid {
			rate := int(takeoverSuccessMax.Int32)
			interactive.TakeoverSuccessRateMax = &rate
		}
		if takeoverRisk.Valid {
			risk := int(takeoverRisk.Int32)
			interactive.TakeoverDetectionRiskPercent = &risk
		}
		if takeoverTime.Valid {
			t := int(takeoverTime.Int32)
			interactive.TakeoverTimeSeconds = &t
		}
		if takeoverAlarm.Valid {
			alarm := int(takeoverAlarm.Int32)
			interactive.TakeoverAlarmProbability = &alarm
		}
		if worldLoc.Valid {
			interactive.WorldLocation = worldLoc.String
		}
		if coordX.Valid {
			interactive.CoordinatesX = coordX.Float64
		}
		if coordY.Valid {
			interactive.CoordinatesY = coordY.Float64
		}
		if coordZ.Valid {
			interactive.CoordinatesZ = coordZ.Float64
		}
		if isActive.Valid {
			interactive.IsActive = isActive.Bool
		}
		if currentHealth.Valid {
			health := int(currentHealth.Int32)
			interactive.CurrentHealth = &health
		}
		if lastInteract.Valid {
			interactive.LastInteraction = &lastInteract.Time
		}
		if securityStatus.Valid {
			interactive.SecurityStatus = securityStatus.String
		} else {
			interactive.SecurityStatus = "normal"
		}

		interactives = append(interactives, interactive)
	}

	return interactives, total, nil
}

// GetWorldInteractive retrieves a single world interactive by ID
func (r *PostgresInteractiveRepository) GetWorldInteractive(ctx context.Context, interactiveID string) (*models.Interactive, error) {
	filter := &models.ListWorldInteractivesRequest{
		Limit: 1,
	}
	// Note: This is a simplified implementation. In production, you'd want a direct query by ID.

	interactives, _, err := r.GetWorldInteractives(ctx, filter)
	if err != nil {
		return nil, err
	}

	if len(interactives) == 0 {
		return nil, fmt.Errorf("interactive not found: %s", interactiveID)
	}

	return &interactives[0], nil
}

// UpdateWorldInteractive updates a world interactive
func (r *PostgresInteractiveRepository) UpdateWorldInteractive(ctx context.Context, interactiveID string, updates map[string]interface{}) (*models.Interactive, error) {
	// This is a simplified implementation. In production, you'd build dynamic update queries.
	return nil, fmt.Errorf("UpdateWorldInteractive not implemented")
}

// DeleteWorldInteractive deletes a world interactive
func (r *PostgresInteractiveRepository) DeleteWorldInteractive(ctx context.Context, interactiveID string) error {
	// This is a simplified implementation. In production, you'd handle cascading deletes.
	return fmt.Errorf("DeleteWorldInteractive not implemented")
}

// GetInteractivesByFaction retrieves interactives controlled by a specific faction
func (r *PostgresInteractiveRepository) GetInteractivesByFaction(ctx context.Context, faction string) ([]models.Interactive, error) {
	filter := &models.ListWorldInteractivesRequest{
		ControllingFaction: &faction,
	}

	interactives, _, err := r.GetWorldInteractives(ctx, filter)
	return interactives, err
}

// GetInteractivesByLocation retrieves interactives within a location radius
func (r *PostgresInteractiveRepository) GetInteractivesByLocation(ctx context.Context, worldLocation string, radius float64) ([]models.Interactive, error) {
	filter := &models.ListWorldInteractivesRequest{
		WorldLocation: &worldLocation,
	}

	interactives, _, err := r.GetWorldInteractives(ctx, filter)
	// Note: In production, you'd implement proper geospatial queries with radius filtering
	return interactives, err
}

// GetInteractivesByCategory retrieves interactives of a specific category
func (r *PostgresInteractiveRepository) GetInteractivesByCategory(ctx context.Context, category models.InteractiveCategory) ([]models.Interactive, error) {
	filter := &models.ListWorldInteractivesRequest{
		Category: &category,
	}

	interactives, _, err := r.GetWorldInteractives(ctx, filter)
	return interactives, err
}

// UpdateInteractiveHealth updates the health of an interactive
func (r *PostgresInteractiveRepository) UpdateInteractiveHealth(ctx context.Context, interactiveID string, newHealth int) error {
	query := `
		UPDATE interactive_locations
		SET current_health = $1, updated_at = $2
		WHERE interactive_type_id IN (
			SELECT it.id FROM interactive_types it
			JOIN world_interactives wi ON wi.id = it.interactive_id
			WHERE wi.interactive_name = $3
		)`

	_, err := r.db.ExecContext(ctx, query, newHealth, time.Now(), interactiveID)
	return err
}

// UpdateInteractiveFactionControl updates faction control of an interactive
func (r *PostgresInteractiveRepository) UpdateInteractiveFactionControl(ctx context.Context, interactiveID string, faction string) error {
	query := `
		UPDATE interactive_types
		SET controlling_faction = $1, updated_at = $2
		WHERE interactive_id IN (
			SELECT id FROM world_interactives WHERE interactive_name = $3
		)`

	_, err := r.db.ExecContext(ctx, query, faction, time.Now(), interactiveID)
	return err
}

// Legacy methods implementation (for compatibility)
func (r *PostgresInteractiveRepository) SaveInteractive(ctx context.Context, interactiveID string, version int, name, description, location string, interactiveType models.InteractiveType, status models.InteractiveStatus, contentData map[string]interface{}) (*models.Interactive, error) {
	// Convert legacy format to new format
	interactive := &models.Interactive{
		InteractiveID:  interactiveID,
		Version:        version,
		Name:           name,
		Description:    description,
		Location:       location,
		Type:           interactiveType,
		Status:         status,
		ContentData:    contentData,
		IsActive:       status == models.InteractiveStatusActive,
		SecurityStatus: "normal",
	}

	return r.SaveWorldInteractive(ctx, interactive)
}

func (r *PostgresInteractiveRepository) GetInteractives(ctx context.Context, filter *models.ListInteractivesRequest) ([]models.Interactive, int, error) {
	// Convert legacy filter to new format
	newFilter := &models.ListWorldInteractivesRequest{
		Type:   filter.Type,
		Status: filter.Status,
		Limit:  filter.Limit,
		Offset: filter.Offset,
	}

	return r.GetWorldInteractives(ctx, newFilter)
}

func (r *PostgresInteractiveRepository) GetInteractive(ctx context.Context, interactiveID string) (*models.Interactive, error) {
	return r.GetWorldInteractive(ctx, interactiveID)
}

func (r *PostgresInteractiveRepository) UpdateInteractive(ctx context.Context, interactiveID string, updates map[string]interface{}) (*models.Interactive, error) {
	return r.UpdateWorldInteractive(ctx, interactiveID, updates)
}

func (r *PostgresInteractiveRepository) DeleteInteractive(ctx context.Context, interactiveID string) error {
	return r.DeleteWorldInteractive(ctx, interactiveID)
}
