//go:align 64
package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/NECPGAME/combat-system-service-go/internal/models"
	api "github.com/NECPGAME/combat-system-service-go/pkg/api"
)

// Conversion functions between API and models types

func convertCombatSystemRulesToModels(apiRules *api.CombatSystemRules) *models.CombatSystemRules {
	if apiRules == nil {
		return nil
	}
	return &models.CombatSystemRules{
		ID:                apiRules.ID.Value,
		Version:           apiRules.Version,
		DamageRules:       convertDamageRulesToModels(apiRules.DamageRules),
		CombatMechanics:   convertCombatMechanicsToModels(apiRules.CombatMechanics),
		BalanceParameters: convertBalanceParametersToModels(apiRules.BalanceParameters),
		CreatedAt:         apiRules.CreatedAt.Value,
		UpdatedAt:         apiRules.UpdatedAt.Value,
		CreatedBy:         apiRules.CreatedBy.Value,
	}
}

func convertCombatSystemRulesToAPI(modelRules *models.CombatSystemRules) *api.CombatSystemRules {
	if modelRules == nil {
		return nil
	}
	return &api.CombatSystemRules{
		ID:                api.NewOptUUID(modelRules.ID),
		Version:           modelRules.Version,
		DamageRules:       convertDamageRulesToAPI(modelRules.DamageRules),
		CombatMechanics:   convertCombatMechanicsToAPI(modelRules.CombatMechanics),
		BalanceParameters: convertBalanceParametersToAPI(modelRules.BalanceParameters),
		CreatedAt:         api.NewOptDateTime(modelRules.CreatedAt),
		UpdatedAt:         api.NewOptDateTime(modelRules.UpdatedAt),
		CreatedBy:         api.NewOptString(modelRules.CreatedBy),
	}
}

func convertDamageRulesToModels(apiRules api.DamageRules) models.DamageRules {
	return models.DamageRules{
		BaseDamageMultiplier:     apiRules.BaseDamageMultiplier,
		CriticalHitMultiplier:    apiRules.CriticalHitMultiplier,
		ArmorReductionFactor:     apiRules.ArmorReductionFactor,
		EnvironmentalModifiers:   convertEnvironmentalModifiersToModels(apiRules.EnvironmentalModifiers),
	}
}

func convertDamageRulesToAPI(modelRules models.DamageRules) api.DamageRules {
	return api.DamageRules{
		BaseDamageMultiplier:     modelRules.BaseDamageMultiplier,
		CriticalHitMultiplier:    modelRules.CriticalHitMultiplier,
		ArmorReductionFactor:     modelRules.ArmorReductionFactor,
		EnvironmentalModifiers:   convertEnvironmentalModifiersToAPI(modelRules.EnvironmentalModifiers),
	}
}

func convertEnvironmentalModifiersToModels(apiMods api.OptDamageRulesEnvironmentalModifiers) models.EnvironmentalModifiers {
	if !apiMods.IsSet() {
		return models.EnvironmentalModifiers{}
	}
	return models.EnvironmentalModifiers{
		WeatherDamageModifier: apiMods.Value.WeatherDamageModifier.Value,
		TimeOfDayModifier:     apiMods.Value.TimeOfDayModifier.Value,
	}
}

func convertEnvironmentalModifiersToAPI(modelMods models.EnvironmentalModifiers) api.OptDamageRulesEnvironmentalModifiers {
	return api.NewOptDamageRulesEnvironmentalModifiers(api.DamageRulesEnvironmentalModifiers{
		WeatherDamageModifier: api.NewOptFloat32(modelMods.WeatherDamageModifier),
		TimeOfDayModifier:     api.NewOptFloat32(modelMods.TimeOfDayModifier),
	})
}

func convertCombatMechanicsToModels(apiMech api.CombatMechanics) models.CombatMechanics {
	return models.CombatMechanics{
		TurnBasedEnabled:   apiMech.TurnBasedEnabled,
		ActionPointsSystem: convertActionPointsSystemToModels(apiMech.ActionPointsSystem),
		InterruptionRules:  convertInterruptionRulesToModels(apiMech.InterruptionRules),
	}
}

func convertCombatMechanicsToAPI(modelMech models.CombatMechanics) api.CombatMechanics {
	return api.CombatMechanics{
		TurnBasedEnabled:   modelMech.TurnBasedEnabled,
		ActionPointsSystem: convertActionPointsSystemToAPI(modelMech.ActionPointsSystem),
		InterruptionRules:  convertInterruptionRulesToAPI(modelMech.InterruptionRules),
	}
}

func convertActionPointsSystemToModels(apiSys api.CombatMechanicsActionPointsSystem) models.ActionPointsSystem {
	return models.ActionPointsSystem{
		MaxActionPoints: apiSys.MaxActionPoints.Value,
		PointsPerTurn:   apiSys.PointsPerTurn.Value,
	}
}

func convertActionPointsSystemToAPI(modelSys models.ActionPointsSystem) api.CombatMechanicsActionPointsSystem {
	return api.CombatMechanicsActionPointsSystem{
		MaxActionPoints: api.NewOptInt(modelSys.MaxActionPoints),
		PointsPerTurn:   api.NewOptInt(modelSys.PointsPerTurn),
	}
}

func convertInterruptionRulesToModels(apiRules api.CombatMechanicsInterruptionRules) models.InterruptionRules {
	return models.InterruptionRules{
		AllowInterruptions:  apiRules.AllowInterruptions.Value,
		InterruptionPenalty: apiRules.InterruptionPenalty.Value,
	}
}

func convertInterruptionRulesToAPI(modelRules models.InterruptionRules) api.CombatMechanicsInterruptionRules {
	return api.CombatMechanicsInterruptionRules{
		AllowInterruptions:  api.NewOptBool(modelRules.AllowInterruptions),
		InterruptionPenalty: api.NewOptInt(modelRules.InterruptionPenalty),
	}
}

func convertBalanceParametersToModels(apiParams api.BalanceParameters) models.BalanceParameters {
	return models.BalanceParameters{
		DifficultyScaling: convertDifficultyScalingToModels(apiParams.DifficultyScaling),
		PlayerAdvantages:  convertPlayerAdvantagesToModels(apiParams.PlayerAdvantages),
		NPCMModifiers:     convertNPCMModifiersToModels(apiParams.NpcModifiers),
	}
}

func convertBalanceParametersToAPI(modelParams models.BalanceParameters) api.BalanceParameters {
	return api.BalanceParameters{
		DifficultyScaling: convertDifficultyScalingToAPI(modelParams.DifficultyScaling),
		PlayerAdvantages:  convertPlayerAdvantagesToAPI(modelParams.PlayerAdvantages),
		NpcModifiers:      convertNPCMModifiersToAPI(modelParams.NPCMModifiers),
	}
}

func convertDifficultyScalingToModels(apiScaling api.BalanceParametersDifficultyScaling) models.DifficultyScaling {
	return models.DifficultyScaling{
		ScalingFactor:           apiScaling.ScalingFactor.Value,
		LevelDifferenceModifier: apiScaling.LevelDifferenceModifier.Value,
	}
}

func convertDifficultyScalingToAPI(modelScaling models.DifficultyScaling) api.BalanceParametersDifficultyScaling {
	return api.BalanceParametersDifficultyScaling{
		ScalingFactor:           api.NewOptFloat32(modelScaling.ScalingFactor),
		LevelDifferenceModifier: api.NewOptFloat32(modelScaling.LevelDifferenceModifier),
	}
}

func convertPlayerAdvantagesToModels(apiAdv api.BalanceParametersPlayerAdvantages) models.PlayerAdvantages {
	return models.PlayerAdvantages{
		FirstStrikeBonus:    apiAdv.FirstStrikeBonus.Value,
		PositionalAdvantage: apiAdv.PositionalAdvantage.Value,
	}
}

func convertPlayerAdvantagesToAPI(modelAdv models.PlayerAdvantages) api.BalanceParametersPlayerAdvantages {
	return api.BalanceParametersPlayerAdvantages{
		FirstStrikeBonus:    api.NewOptFloat32(modelAdv.FirstStrikeBonus),
		PositionalAdvantage: api.NewOptFloat32(modelAdv.PositionalAdvantage),
	}
}

func convertNPCMModifiersToModels(apiMods api.BalanceParametersNpcModifiers) models.NPCMModifiers {
	return models.NPCMModifiers{
		EliteMultiplier: apiMods.EliteMultiplier.Value,
		BossMultiplier:  apiMods.BossMultiplier.Value,
	}
}

func convertNPCMModifiersToAPI(modelMods models.NPCMModifiers) api.BalanceParametersNpcModifiers {
	return api.BalanceParametersNpcModifiers{
		EliteMultiplier: api.NewOptFloat32(modelMods.EliteMultiplier),
		BossMultiplier:  api.NewOptFloat32(modelMods.BossMultiplier),
	}
}

func convertGlobalMultipliersToAPI(modelMods models.GlobalMultipliers) api.GlobalMultipliers {
	return api.GlobalMultipliers{
		DamageMultiplier:   api.NewOptFloat32(modelMods.DamageMultiplier),
		HealingMultiplier:  api.NewOptFloat32(modelMods.HealingMultiplier),
		CooldownMultiplier: api.NewOptFloat32(modelMods.CooldownMultiplier),
	}
}

func convertCharacterBalanceToAPI(modelBalance models.CharacterBalance) api.CharacterBalance {
	// Convert map[string]CharacterBalanceEntry to api format
	balanceMap := make(map[string]api.CharacterBalanceEntry)
	for k, v := range modelBalance {
		balanceMap[k] = api.CharacterBalanceEntry{
			HealthMultiplier: api.NewOptFloat32(v.HealthMultiplier),
			DamageMultiplier: api.NewOptFloat32(v.DamageMultiplier),
			SpeedMultiplier:  api.NewOptFloat32(v.SpeedMultiplier),
		}
	}
	return balanceMap
}

func convertEnvironmentalBalanceToAPI(modelBalance models.EnvironmentalBalance) api.EnvironmentBalance {
	// Convert maps to api format
	weatherEffects := make(map[string]api.OptFloat32)
	timeOfDayEffects := make(map[string]api.OptFloat32)

	for k, v := range modelBalance.WeatherEffects {
		weatherEffects[k] = api.NewOptFloat32(v)
	}
	for k, v := range modelBalance.TimeOfDayEffects {
		timeOfDayEffects[k] = api.NewOptFloat32(v)
	}

	return api.EnvironmentBalance{
		WeatherEffects:    weatherEffects,
		TimeOfDayEffects:  timeOfDayEffects,
	}
}

//go:align 64
type Repository interface {
	// Combat Rules operations
	GetCombatSystemRules(ctx context.Context) (*models.CombatSystemRules, error)
	UpdateCombatSystemRules(ctx context.Context, rules *models.CombatSystemRules) error

	// Balance Config operations
	GetCombatBalanceConfig(ctx context.Context) (*api.CombatBalanceConfig, error)
	UpdateCombatBalanceConfig(ctx context.Context, config *api.CombatBalanceConfig) error

	// Ability operations
	ListAbilityConfigurations(ctx context.Context, limit, offset int, abilityType *string) ([]*models.AbilityConfiguration, int, error)
	GetAbilityConfiguration(ctx context.Context, abilityID uuid.UUID) (*models.AbilityConfiguration, error)
	CreateAbilityConfiguration(ctx context.Context, ability *models.AbilityConfiguration) error
	UpdateAbilityConfiguration(ctx context.Context, ability *models.AbilityConfiguration) error

	// Health check
	GetSystemHealth(ctx context.Context) (*models.SystemHealth, error)
}

//go:align 64
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

//go:align 64
func NewPostgresRepository(db *pgxpool.Pool, logger *zap.Logger) Repository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

//go:align 64
func (r *PostgresRepository) GetCombatSystemRules(ctx context.Context) (*models.CombatSystemRules, error) {
	query := `
		SELECT id, version, damage_rules, combat_mechanics, balance_parameters,
			   created_at, updated_at, created_by
		FROM combat_system_rules
		WHERE id = (SELECT id FROM combat_system_rules ORDER BY version DESC LIMIT 1)
	`

	var rules models.CombatSystemRules
	var damageRulesJSON, combatMechanicsJSON, balanceParamsJSON []byte

	err := r.db.QueryRow(ctx, query).Scan(
		&rules.ID, &rules.Version, &damageRulesJSON, &combatMechanicsJSON,
		&balanceParamsJSON, &rules.CreatedAt, &rules.UpdatedAt, &rules.CreatedBy,
	)

	if err != nil {
		r.logger.Error("Failed to get combat system rules", zap.Error(err))
		return nil, fmt.Errorf("failed to get combat system rules: %w", err)
	}

	if err := json.Unmarshal(damageRulesJSON, &rules.DamageRules); err != nil {
		return nil, fmt.Errorf("failed to unmarshal damage rules: %w", err)
	}

	if err := json.Unmarshal(combatMechanicsJSON, &rules.CombatMechanics); err != nil {
		return nil, fmt.Errorf("failed to unmarshal combat mechanics: %w", err)
	}

	if err := json.Unmarshal(balanceParamsJSON, &rules.BalanceParameters); err != nil {
		return nil, fmt.Errorf("failed to unmarshal balance parameters: %w", err)
	}

	return &rules, nil
}

//go:align 64
func (r *PostgresRepository) UpdateCombatSystemRules(ctx context.Context, rules *models.CombatSystemRules) error {

	damageRulesJSON, err := json.Marshal(rules.DamageRules)
	if err != nil {
		return fmt.Errorf("failed to marshal damage rules: %w", err)
	}

	combatMechanicsJSON, err := json.Marshal(rules.CombatMechanics)
	if err != nil {
		return fmt.Errorf("failed to marshal combat mechanics: %w", err)
	}

	balanceParamsJSON, err := json.Marshal(rules.BalanceParameters)
	if err != nil {
		return fmt.Errorf("failed to marshal balance parameters: %w", err)
	}

	query := `
		INSERT INTO combat_system_rules (
			id, version, damage_rules, combat_mechanics, balance_parameters,
			created_at, updated_at, created_by
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	rules.UpdatedAt = time.Now()

	_, err = r.db.Exec(ctx, query,
		rules.ID, rules.Version, damageRulesJSON, combatMechanicsJSON,
		balanceParamsJSON, rules.CreatedAt, rules.UpdatedAt, rules.CreatedBy,
	)

	if err != nil {
		r.logger.Error("Failed to update combat system rules", zap.Error(err))
		return fmt.Errorf("failed to update combat system rules: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetCombatBalanceConfig(ctx context.Context) (*api.CombatBalanceConfig, error) {
	query := `
		SELECT id, version, global_multipliers, character_balance, environmental_balance,
			   created_at, updated_at
		FROM combat_balance_configs
		WHERE id = (SELECT id FROM combat_balance_configs ORDER BY version DESC LIMIT 1)
	`

	var id uuid.UUID
	var version int
	var globalMultJSON, charBalanceJSON, envBalanceJSON []byte
	var createdAt, updatedAt time.Time

	err := r.db.QueryRow(ctx, query).Scan(
		&id, &version, &globalMultJSON, &charBalanceJSON,
		&envBalanceJSON, &createdAt, &updatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get combat balance config", zap.Error(err))
		return nil, fmt.Errorf("failed to get combat balance config: %w", err)
	}

	// Parse JSON and convert to API types
	var globalMult models.GlobalMultipliers
	var charBalance models.CharacterBalance
	var envBalance models.EnvironmentalBalance

	if err := json.Unmarshal(globalMultJSON, &globalMult); err != nil {
		return nil, fmt.Errorf("failed to unmarshal global multipliers: %w", err)
	}

	if err := json.Unmarshal(charBalanceJSON, &charBalance); err != nil {
		return nil, fmt.Errorf("failed to unmarshal character balance: %w", err)
	}

	if err := json.Unmarshal(envBalanceJSON, &envBalance); err != nil {
		return nil, fmt.Errorf("failed to unmarshal environmental balance: %w", err)
	}

	// Convert to API types
	config := &api.CombatBalanceConfig{
		ID:                  api.NewOptUUID(id),
		Version:             version,
		GlobalMultipliers:  convertGlobalMultipliersToAPI(globalMult),
		CharacterBalance:    convertCharacterBalanceToAPI(charBalance),
		EnvironmentBalance:  convertEnvironmentalBalanceToAPI(envBalance),
		CreatedAt:           api.NewOptDateTime(createdAt),
		UpdatedAt:           api.NewOptDateTime(updatedAt),
	}

	return config, nil
}

//go:align 64
func (r *PostgresRepository) UpdateCombatBalanceConfig(ctx context.Context, config *models.CombatBalanceConfig) error {
	globalMultJSON, err := json.Marshal(config.GlobalMultipliers)
	if err != nil {
		return fmt.Errorf("failed to marshal global multipliers: %w", err)
	}

	charBalanceJSON, err := json.Marshal(config.CharacterBalance)
	if err != nil {
		return fmt.Errorf("failed to marshal character balance: %w", err)
	}

	envBalanceJSON, err := json.Marshal(config.EnvironmentalBalance)
	if err != nil {
		return fmt.Errorf("failed to marshal environmental balance: %w", err)
	}

	query := `
		INSERT INTO combat_balance_configs (
			id, version, global_multipliers, character_balance, environmental_balance,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	config.UpdatedAt = time.Now()

	_, err = r.db.Exec(ctx, query,
		config.ID, config.Version, globalMultJSON, charBalanceJSON,
		envBalanceJSON, config.CreatedAt, config.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to update combat balance config", zap.Error(err))
		return fmt.Errorf("failed to update combat balance config: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) ListAbilityConfigurations(ctx context.Context, limit, offset int, abilityType *string) ([]*models.AbilityConfiguration, int, error) {
	query := `
		SELECT id, name, type, description, damage, cooldown_ms, mana_cost, range,
			   cast_time_ms, balance_notes, stat_requirements, effects, created_at, updated_at
		FROM ability_configurations
		WHERE 1=1
	`

	args := []interface{}{}
	argCount := 0

	if abilityType != nil && *abilityType != "" {
		argCount++
		query += fmt.Sprintf(" AND type = $%d", argCount)
		args = append(args, *abilityType)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM (" + query + ") as count_query"
	var total int
	countArgs := make([]interface{}, len(args))
	copy(countArgs, args)

	err := r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	// Add ordering and pagination
	query += " ORDER BY name ASC"
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list ability configurations: %w", err)
	}
	defer rows.Close()

	var abilities []*models.AbilityConfiguration
	for rows.Next() {
		var ability models.AbilityConfiguration
		var statReqJSON, effectsJSON []byte

		err := rows.Scan(
			&ability.ID, &ability.Name, &ability.Type, &ability.Description,
			&ability.Damage, &ability.CooldownMs, &ability.ManaCost, &ability.Range,
			&ability.CastTimeMs, &ability.BalanceNotes, &statReqJSON, &effectsJSON,
			&ability.CreatedAt, &ability.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan ability configuration: %w", err)
		}

		if err := json.Unmarshal(statReqJSON, &ability.StatRequirements); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal stat requirements: %w", err)
		}

		if err := json.Unmarshal(effectsJSON, &ability.Effects); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal effects: %w", err)
		}

		abilities = append(abilities, &ability)
	}

	return abilities, total, nil
}

//go:align 64
func (r *PostgresRepository) GetAbilityConfiguration(ctx context.Context, abilityID uuid.UUID) (*models.AbilityConfiguration, error) {
	query := `
		SELECT id, name, type, description, damage, cooldown_ms, mana_cost, range,
			   cast_time_ms, balance_notes, stat_requirements, effects, created_at, updated_at
		FROM ability_configurations WHERE id = $1
	`

	var ability models.AbilityConfiguration
	var statReqJSON, effectsJSON []byte

	err := r.db.QueryRow(ctx, query, abilityID).Scan(
		&ability.ID, &ability.Name, &ability.Type, &ability.Description,
		&ability.Damage, &ability.CooldownMs, &ability.ManaCost, &ability.Range,
		&ability.CastTimeMs, &ability.BalanceNotes, &statReqJSON, &effectsJSON,
		&ability.CreatedAt, &ability.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get ability configuration", zap.Error(err), zap.String("ability_id", abilityID.String()))
		return nil, fmt.Errorf("failed to get ability configuration: %w", err)
	}

	if err := json.Unmarshal(statReqJSON, &ability.StatRequirements); err != nil {
		return nil, fmt.Errorf("failed to unmarshal stat requirements: %w", err)
	}

	if err := json.Unmarshal(effectsJSON, &ability.Effects); err != nil {
		return nil, fmt.Errorf("failed to unmarshal effects: %w", err)
	}

	return &ability, nil
}

//go:align 64
func (r *PostgresRepository) CreateAbilityConfiguration(ctx context.Context, ability *models.AbilityConfiguration) error {
	statReqJSON, err := json.Marshal(ability.StatRequirements)
	if err != nil {
		return fmt.Errorf("failed to marshal stat requirements: %w", err)
	}

	effectsJSON, err := json.Marshal(ability.Effects)
	if err != nil {
		return fmt.Errorf("failed to marshal effects: %w", err)
	}

	query := `
		INSERT INTO ability_configurations (
			id, name, type, description, damage, cooldown_ms, mana_cost, range,
			cast_time_ms, balance_notes, stat_requirements, effects, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
	`

	_, err = r.db.Exec(ctx, query,
		ability.ID, ability.Name, ability.Type, ability.Description, ability.Damage,
		ability.CooldownMs, ability.ManaCost, ability.Range, ability.CastTimeMs,
		ability.BalanceNotes, statReqJSON, effectsJSON, ability.CreatedAt, ability.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create ability configuration", zap.Error(err))
		return fmt.Errorf("failed to create ability configuration: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) UpdateAbilityConfiguration(ctx context.Context, ability *models.AbilityConfiguration) error {
	statReqJSON, err := json.Marshal(ability.StatRequirements)
	if err != nil {
		return fmt.Errorf("failed to marshal stat requirements: %w", err)
	}

	effectsJSON, err := json.Marshal(ability.Effects)
	if err != nil {
		return fmt.Errorf("failed to marshal effects: %w", err)
	}

	query := `
		UPDATE ability_configurations SET
			name = $2, type = $3, description = $4, damage = $5, cooldown_ms = $6,
			mana_cost = $7, range = $8, cast_time_ms = $9, balance_notes = $10,
			stat_requirements = $11, effects = $12, updated_at = $13
		WHERE id = $1
	`

	ability.UpdatedAt = time.Now()

	_, err = r.db.Exec(ctx, query,
		ability.ID, ability.Name, ability.Type, ability.Description, ability.Damage,
		ability.CooldownMs, ability.ManaCost, ability.Range, ability.CastTimeMs,
		ability.BalanceNotes, statReqJSON, effectsJSON, ability.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to update ability configuration", zap.Error(err))
		return fmt.Errorf("failed to update ability configuration: %w", err)
	}

	return nil
}

//go:align 64
func (r *PostgresRepository) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	query := `
		SELECT
			(SELECT COUNT(*) FROM combat_system_rules) as total_combat_calculations,
			(SELECT COUNT(*) FROM ability_configurations) as total_abilities,
			(SELECT COUNT(*) FROM combat_balance_configs) as active_balance_configs
	`

	var health models.SystemHealth
	err := r.db.QueryRow(ctx, query).Scan(
		&health.TotalCombatCalculations,
		&health.TotalAbilities,
		&health.ActiveBalanceConfigs,
	)

	if err != nil {
		r.logger.Error("Failed to get system health", zap.Error(err))
		return nil, fmt.Errorf("failed to get system health: %w", err)
	}

	// Set active combat sessions (would be tracked in Redis in real implementation)
	health.ActiveCombatSessions = 0

	return &health, nil
}