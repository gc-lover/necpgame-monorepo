// Issue: #142109955
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

type Component struct {
	ComponentID uuid.UUID
	Quantity    int
}

type RepairResult struct {
	Success    bool
	Durability float32
	Cost       *Cost
}

type Cost struct {
	Currency string
	Amount   int
}

type UpgradeResult struct {
	Success   bool
	NewLevel  int
	NewStats  map[string]interface{}
}

type ModifyResult struct {
	Success             bool
	AppliedModifications []AppliedModification
}

type AppliedModification struct {
	ModificationID uuid.UUID
	Name           string
	Description    string
}

type VisualsSettings struct {
	VisibilityMode string
	ColorScheme    string
	EffectsEnabled bool
	BrandStyle     string
}

type CustomizeVisualsResult struct {
	Success bool
}

type ImplantsMaintenanceRepositoryInterface interface {
	RepairImplant(ctx context.Context, implantID uuid.UUID, repairType string) (*RepairResult, error)
	UpgradeImplant(ctx context.Context, implantID uuid.UUID, components []Component) (*UpgradeResult, error)
	ModifyImplant(ctx context.Context, implantID uuid.UUID, modificationID uuid.UUID) (*ModifyResult, error)
	GetVisuals(ctx context.Context) (*VisualsSettings, error)
	CustomizeVisuals(ctx context.Context, implantID uuid.UUID, visibilityMode *string, colorScheme *string) (*CustomizeVisualsResult, error)
}

type ImplantsMaintenanceRepository struct {
	db *pgxpool.Pool
}

func NewImplantsMaintenanceRepository(db *pgxpool.Pool) *ImplantsMaintenanceRepository {
	return &ImplantsMaintenanceRepository{
		db: db,
	}
}

func (r *ImplantsMaintenanceRepository) RepairImplant(ctx context.Context, implantID uuid.UUID, repairType string) (*RepairResult, error) {
	var durability float32
	var costAmount int
	var costCurrency string

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(new_durability, 100.0)::float,
			COALESCE(cost_amount, 0),
			COALESCE(cost_currency, 'credits')
		FROM character_implants_repair
		WHERE implant_id = $1 AND repair_type = $2
	`, implantID, repairType).Scan(&durability, &costAmount, &costCurrency)

	if err != nil {
		durability = 100.0
		costAmount = 0
		costCurrency = "credits"
	}

	return &RepairResult{
		Success:    true,
		Durability: durability,
		Cost: &Cost{
			Currency: costCurrency,
			Amount:   costAmount,
		},
	}, nil
}

func (r *ImplantsMaintenanceRepository) UpgradeImplant(ctx context.Context, implantID uuid.UUID, components []Component) (*UpgradeResult, error) {
	var newLevel int
	var newStats map[string]interface{}

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(new_level, 1),
			COALESCE(new_stats, '{}'::jsonb)
		FROM character_implants_upgrade
		WHERE implant_id = $1
	`, implantID).Scan(&newLevel, &newStats)

	if err != nil {
		newLevel = 1
		newStats = make(map[string]interface{})
	}

	return &UpgradeResult{
		Success:  true,
		NewLevel: newLevel,
		NewStats: newStats,
	}, nil
}

func (r *ImplantsMaintenanceRepository) ModifyImplant(ctx context.Context, implantID uuid.UUID, modificationID uuid.UUID) (*ModifyResult, error) {
	var appliedMods []AppliedModification

	rows, err := r.db.Query(ctx, `
		SELECT 
			modification_id,
			name,
			description
		FROM character_implants_modifications
		WHERE implant_id = $1 AND modification_id = $2
	`, implantID, modificationID)

	if err == nil {
		defer rows.Close()
		for rows.Next() {
			var mod AppliedModification
			if err := rows.Scan(&mod.ModificationID, &mod.Name, &mod.Description); err == nil {
				appliedMods = append(appliedMods, mod)
			}
		}
	}

	return &ModifyResult{
		Success:              true,
		AppliedModifications: appliedMods,
	}, nil
}

func (r *ImplantsMaintenanceRepository) GetVisuals(ctx context.Context) (*VisualsSettings, error) {
	var settings VisualsSettings

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(visibility_mode, 'full'),
			COALESCE(color_scheme, 'default'),
			COALESCE(effects_enabled, true),
			COALESCE(brand_style, 'default')
		FROM character_implants_visuals
		LIMIT 1
	`).Scan(&settings.VisibilityMode, &settings.ColorScheme, &settings.EffectsEnabled, &settings.BrandStyle)

	if err != nil {
		settings = VisualsSettings{
			VisibilityMode: "full",
			ColorScheme:    "default",
			EffectsEnabled: true,
			BrandStyle:     "default",
		}
	}

	return &settings, nil
}

func (r *ImplantsMaintenanceRepository) CustomizeVisuals(ctx context.Context, implantID uuid.UUID, visibilityMode *string, colorScheme *string) (*CustomizeVisualsResult, error) {
	_, err := r.db.Exec(ctx, `
		INSERT INTO character_implants_visuals (implant_id, visibility_mode, color_scheme)
		VALUES ($1, $2, $3)
		ON CONFLICT (implant_id) 
		DO UPDATE SET 
			visibility_mode = COALESCE($2, character_implants_visuals.visibility_mode),
			color_scheme = COALESCE($3, character_implants_visuals.color_scheme)
	`, implantID, visibilityMode, colorScheme)

	if err != nil {
		return &CustomizeVisualsResult{Success: false}, err
	}

	return &CustomizeVisualsResult{Success: true}, nil
}

