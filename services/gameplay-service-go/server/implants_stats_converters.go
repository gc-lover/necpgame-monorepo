// Issue: #142109960
package server

import (
	"github.com/necpgame/gameplay-service-go/pkg/implantsstatsapi"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertEnergyStatusToAPI(status *EnergyStatus) *implantsstatsapi.EnergyStatus {
	return &implantsstatsapi.EnergyStatus{
		Current:     &status.Current,
		Max:         &status.Max,
		Consumption: &status.Consumption,
		Overheated:  &status.Overheated,
		CoolingRate: &status.CoolingRate,
	}
}

func convertHumanityStatusToAPI(status *HumanityStatus) *implantsstatsapi.HumanityStatus {
	return &implantsstatsapi.HumanityStatus{
		Current:           &status.Current,
		Max:               &status.Max,
		CyberpsychosisRisk: &status.CyberpsychosisRisk,
		ImplantCount:      &status.ImplantCount,
	}
}

func convertCompatibilityResultToAPI(result *CompatibilityResult) *implantsstatsapi.CompatibilityResult {
	apiResult := &implantsstatsapi.CompatibilityResult{
		Compatible: &result.Compatible,
	}
	
	if len(result.Conflicts) > 0 {
		conflicts := make([]struct {
			ImplantId *openapi_types.UUID `json:"implant_id,omitempty"`
			Reason    *string             `json:"reason,omitempty"`
		}, len(result.Conflicts))
		
		for i, conflict := range result.Conflicts {
			id := openapi_types.UUID(conflict.ImplantID)
			conflicts[i] = struct {
				ImplantId *openapi_types.UUID `json:"implant_id,omitempty"`
				Reason    *string             `json:"reason,omitempty"`
			}{
				ImplantId: &id,
				Reason:    &conflict.Reason,
			}
		}
		apiResult.Conflicts = &conflicts
	}
	
	if len(result.Warnings) > 0 {
		apiResult.Warnings = &result.Warnings
	}
	
	apiResult.EnergyCheck = &struct {
		Available  *float32 `json:"available,omitempty"`
		Required   *float32 `json:"required,omitempty"`
		Sufficient *bool    `json:"sufficient,omitempty"`
	}{
		Available:  &result.EnergyCheck.Available,
		Required:   &result.EnergyCheck.Required,
		Sufficient: &result.EnergyCheck.Sufficient,
	}
	
	apiResult.HumanityCheck = &struct {
		Available  *float32 `json:"available,omitempty"`
		Required   *float32 `json:"required,omitempty"`
		Sufficient *bool    `json:"sufficient,omitempty"`
	}{
		Available:  &result.HumanityCheck.Available,
		Required:   &result.HumanityCheck.Required,
		Sufficient: &result.HumanityCheck.Sufficient,
	}
	
	return apiResult
}

func convertSetBonusesToAPI(bonuses *SetBonuses) *implantsstatsapi.SetBonuses {
	if len(bonuses.ActiveSets) == 0 {
		return &implantsstatsapi.SetBonuses{ActiveSets: &[]struct {
			Bonuses      *[]struct {
				Description *string  `json:"description,omitempty"`
				Name        *string  `json:"name,omitempty"`
				Value       *float32 `json:"value,omitempty"`
			} `json:"bonuses,omitempty"`
			Brand        *string `json:"brand,omitempty"`
			ImplantsCount *int   `json:"implants_count,omitempty"`
		}{}}
	}
	
	activeSets := make([]struct {
		Bonuses      *[]struct {
			Description *string  `json:"description,omitempty"`
			Name        *string  `json:"name,omitempty"`
			Value       *float32 `json:"value,omitempty"`
		} `json:"bonuses,omitempty"`
		Brand        *string `json:"brand,omitempty"`
		ImplantsCount *int   `json:"implants_count,omitempty"`
	}, len(bonuses.ActiveSets))
	
	for i, set := range bonuses.ActiveSets {
		bonusesList := make([]struct {
			Description *string  `json:"description,omitempty"`
			Name        *string  `json:"name,omitempty"`
			Value       *float32 `json:"value,omitempty"`
		}, len(set.Bonuses))
		
		for j, bonus := range set.Bonuses {
			bonusesList[j] = struct {
				Description *string  `json:"description,omitempty"`
				Name        *string  `json:"name,omitempty"`
				Value       *float32 `json:"value,omitempty"`
			}{
				Name:        &bonus.Name,
				Description: &bonus.Description,
				Value:       &bonus.Value,
			}
		}
		
		activeSets[i] = struct {
			Bonuses      *[]struct {
				Description *string  `json:"description,omitempty"`
				Name        *string  `json:"name,omitempty"`
				Value       *float32 `json:"value,omitempty"`
			} `json:"bonuses,omitempty"`
			Brand        *string `json:"brand,omitempty"`
			ImplantsCount *int   `json:"implants_count,omitempty"`
		}{
			Brand:        &set.Brand,
			ImplantsCount: &set.ImplantsCount,
			Bonuses:      &bonusesList,
		}
	}
	
	return &implantsstatsapi.SetBonuses{
		ActiveSets: &activeSets,
	}
}

