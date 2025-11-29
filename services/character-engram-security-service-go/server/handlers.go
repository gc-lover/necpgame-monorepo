package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/character-engram-security-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type EngramSecurityHandlers struct{}

func NewEngramSecurityHandlers() *EngramSecurityHandlers {
	return &EngramSecurityHandlers{}
}

func (h *EngramSecurityHandlers) EncodeEngram(w http.ResponseWriter, r *http.Request, engramId openapi_types.UUID) {
	req := api.EncodeEngramJSONRequestBody{}
	if err := readJSON(r, &req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	protectionTierName := getProtectionTierName(req.ProtectionTier)
	requiredNetrunnerLevel := getRequiredNetrunnerLevel(req.ProtectionTier)

	copyProtection := true
	hackProtection := true
	installProtection := false
	var boundCharacterId *openapi_types.UUID
	if req.ProtectionSettings != nil {
		if req.ProtectionSettings.CopyProtection != nil {
			copyProtection = *req.ProtectionSettings.CopyProtection
		}
		if req.ProtectionSettings.HackProtection != nil {
			hackProtection = *req.ProtectionSettings.HackProtection
		}
		if req.ProtectionSettings.InstallProtection != nil {
			installProtection = *req.ProtectionSettings.InstallProtection
		}
		boundCharacterId = req.ProtectionSettings.BoundCharacterId
	}

	now := time.Now()
	encodedBy := openapi_types.UUID{}

	response := api.EngramProtection{
		EngramId:              engramId,
		ProtectionTier:        req.ProtectionTier,
		ProtectionTierName:    &protectionTierName,
		RequiredNetrunnerLevel: &requiredNetrunnerLevel,
		ProtectionSettings: &struct {
			BoundCharacterId *openapi_types.UUID `json:"bound_character_id"`
			CopyProtection   *bool                `json:"copy_protection,omitempty"`
			HackProtection   *bool                `json:"hack_protection,omitempty"`
			InstallProtection *bool               `json:"install_protection,omitempty"`
		}{
			CopyProtection:    &copyProtection,
			HackProtection:    &hackProtection,
			InstallProtection: &installProtection,
			BoundCharacterId:  boundCharacterId,
		},
		EncodedAt: &now,
		EncodedBy: &encodedBy,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *EngramSecurityHandlers) GetEngramProtection(w http.ResponseWriter, r *http.Request, engramId openapi_types.UUID) {
	protectionTier := 3
	protectionTierName := api.Advanced
	requiredNetrunnerLevel := 75
	copyProtection := true
	hackProtection := true
	installProtection := false
	now := time.Now()
	encodedBy := openapi_types.UUID{}

	response := api.EngramProtection{
		EngramId:              engramId,
		ProtectionTier:        protectionTier,
		ProtectionTierName:    &protectionTierName,
		RequiredNetrunnerLevel: &requiredNetrunnerLevel,
		ProtectionSettings: &struct {
			BoundCharacterId *openapi_types.UUID `json:"bound_character_id"`
			CopyProtection   *bool                `json:"copy_protection,omitempty"`
			HackProtection   *bool                `json:"hack_protection,omitempty"`
			InstallProtection *bool               `json:"install_protection,omitempty"`
		}{
			CopyProtection:    &copyProtection,
			HackProtection:    &hackProtection,
			InstallProtection: &installProtection,
			BoundCharacterId:  nil,
		},
		EncodedAt: &now,
		EncodedBy: &encodedBy,
	}

	respondJSON(w, http.StatusOK, response)
}

func getProtectionTierName(tier int) api.EngramProtectionProtectionTierName {
	switch tier {
	case 1:
		return api.Basic
	case 2:
		return api.Standard
	case 3:
		return api.Advanced
	case 4:
		return api.Corporate
	case 5:
		return api.Military
	default:
		return api.Basic
	}
}

func getRequiredNetrunnerLevel(tier int) int {
	switch tier {
	case 1:
		return 20
	case 2:
		return 50
	case 3:
		return 75
	case 4:
		return 90
	case 5:
		return 95
	default:
		return 20
	}
}

func readJSON(r *http.Request, v interface{}) error {
	return json.NewDecoder(r.Body).Decode(v)
}

