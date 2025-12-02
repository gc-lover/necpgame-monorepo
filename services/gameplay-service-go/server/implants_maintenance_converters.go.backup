// Issue: #142109955
package server

import (
	"github.com/necpgame/gameplay-service-go/pkg/implantsmaintenanceapi"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertRepairResultToAPI(result *RepairResult) *implantsmaintenanceapi.RepairResult {
	apiResult := &implantsmaintenanceapi.RepairResult{
		Success:    &result.Success,
		Durability: &result.Durability,
	}

	if result.Cost != nil {
		apiResult.Cost = &struct {
			Amount   *int    `json:"amount,omitempty"`
			Currency *string `json:"currency,omitempty"`
		}{
			Amount:   &result.Cost.Amount,
			Currency: &result.Cost.Currency,
		}
	}

	return apiResult
}

func convertUpgradeResultToAPI(result *UpgradeResult) *implantsmaintenanceapi.UpgradeResult {
	return &implantsmaintenanceapi.UpgradeResult{
		Success:  &result.Success,
		NewLevel: &result.NewLevel,
		NewStats: &result.NewStats,
	}
}

func convertModifyResultToAPI(result *ModifyResult) *implantsmaintenanceapi.ModifyResult {
	apiResult := &implantsmaintenanceapi.ModifyResult{
		Success: &result.Success,
	}

	if len(result.AppliedModifications) > 0 {
		appliedMods := make([]struct {
			Description    *string             `json:"description,omitempty"`
			ModificationId *openapi_types.UUID `json:"modification_id,omitempty"`
			Name           *string             `json:"name,omitempty"`
		}, len(result.AppliedModifications))

		for i, mod := range result.AppliedModifications {
			id := openapi_types.UUID(mod.ModificationID)
			appliedMods[i] = struct {
				Description    *string             `json:"description,omitempty"`
				ModificationId *openapi_types.UUID `json:"modification_id,omitempty"`
				Name           *string             `json:"name,omitempty"`
			}{
				ModificationId: &id,
				Name:           &mod.Name,
				Description:    &mod.Description,
			}
		}
		apiResult.AppliedModifications = &appliedMods
	}

	return apiResult
}

func convertVisualsSettingsToAPI(settings *VisualsSettings) *implantsmaintenanceapi.VisualsSettings {
	mode := implantsmaintenanceapi.VisualsSettingsVisibilityMode(settings.VisibilityMode)
	return &implantsmaintenanceapi.VisualsSettings{
		VisibilityMode: &mode,
		ColorScheme:    &settings.ColorScheme,
		EffectsEnabled: &settings.EffectsEnabled,
		BrandStyle:     &settings.BrandStyle,
	}
}

func convertCustomizeVisualsResultToAPI(result *CustomizeVisualsResult) *implantsmaintenanceapi.CustomizeVisualsResult {
	return &implantsmaintenanceapi.CustomizeVisualsResult{
		Success: &result.Success,
	}
}

